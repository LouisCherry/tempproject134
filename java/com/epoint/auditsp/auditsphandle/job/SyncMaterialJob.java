package com.epoint.auditsp.auditsphandle.job;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.attach.api.IAttachService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;


/**
 * 竣工验收阶段复制材料
 */
@DisallowConcurrentExecution
public class SyncMaterialJob implements Job {

    private static final Logger logger = Logger.getLogger(SyncMaterialJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            IAuditProject projectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAttachService attachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
            IAuditProjectMaterial materialService = ContainerFactory.getContainInfo().getComponent(IAuditProjectMaterial.class);
            // 获取状态为办结  通信基础设施竣工验收事项的办件
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.setLeftJoinTable("audit_task b", "a.taskGuid", "b.rowGuid");
            sqlConditionUtil.eq("a.status", "90");
            sqlConditionUtil.eq("b.taskName", "通信基础设施竣工验收");
            List<AuditProject> projectList = projectService.getAuditProjectListByCondition(sqlConditionUtil.getMap()).getResult();
            logger.info("获取的已办结的通信基础设施竣工验收办件数量：" + (projectList == null ? 0 : projectList.size()));
            if (CollectionUtils.isNotEmpty(projectList)) {
                for (AuditProject auditProject : projectList) {
                    // 判断是否材料为空
                    List<AuditProjectMaterial> projectMaterials = materialService.selectProjectMaterial(auditProject.getRowguid()).getResult();
                    if (CollectionUtils.isEmpty(projectMaterials)) {
                        continue;
                    }
                    desMaterialLoop:
                    for (AuditProjectMaterial projectMaterial : projectMaterials) {
                        String cliengGuid = projectMaterial.getCliengguid();
                        int count = attachService.getAttachCountByClientGuid(cliengGuid);
                        if (count == 0) {
                            logger.info("已有材料附件，不再复制");
                            continue;
                        }
                        // 复制 同一个审批实例中 通信基础设施工程规划 同名材料
                        sqlConditionUtil.clear();
                        sqlConditionUtil.setLeftJoinTable("audit_sp_i_task b", "a.rowGuid", "b.projectGuid");
                        sqlConditionUtil.eq("a.status", "90");
                        sqlConditionUtil.eq("b.taskName", "建设工程规划许可证核发");
                        sqlConditionUtil.eq("a.biGuid", auditProject.getBiguid());
                        AuditProject project = projectService.getAuditProjectByCondition(sqlConditionUtil.getMap());
                        if (project == null) {
                            logger.info("未找到对应工程规划的办件");
                            continue;
                        }
                        List<AuditProjectMaterial> srcMaterials = materialService.selectProjectMaterial(project.getRowguid()).getResult();
                        if (CollectionUtils.isEmpty(srcMaterials)) {
                            logger.info("工程规划的办件材料为空");
                            continue;
                        }
                        // 复制材料
                        for (AuditProjectMaterial srcMaterial : srcMaterials) {
                            if (projectMaterial.getTaskmaterial().equals(srcMaterial.getTaskmaterial())) {
                                int srcCount = attachService.getAttachCountByClientGuid(srcMaterial.getCliengguid());
                                if (srcCount > 0) {
                                    logger.info("开始复制同名材料附件");
                                    attachService.copyAttachByClientGuid(srcMaterial.getCliengguid(),
                                            "", "竣工验收复制工程规划材料", cliengGuid);
                                    continue desMaterialLoop;
                                }
                            }
                        }
                    }
                }
            }
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }
}
