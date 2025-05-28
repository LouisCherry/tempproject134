package com.epoint.auditproject.guiji.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.auditproject.guiji.constant.ProjectSyncStatus;

import java.util.List;

/**
 * 增删改查，不做逻辑处理 
 * @author 刘雨雨
 * @time 2018年8月23日下午1:58:27
 */
public class LocalProjectDaoImpl
{

    private ICommonDao localCommonDao;

    public LocalProjectDaoImpl() {
        localCommonDao = CommonDao.getInstance();
    }
    public LocalProjectDaoImpl(ICommonDao localCommonDao) {
        this.localCommonDao = localCommonDao;
    }
    
    public List<AuditProjectChargeDetail> getChargeDetailsByProjectGuid(String projectGuid) {
        String sql = "select a.*,b.itemname from audit_project_chargedetail a join audit_task_chargeitem b"
                + " on a.chargeitemguid = b.rowguid where a.projectguid = ?";
        return localCommonDao.findList(sql, AuditProjectChargeDetail.class, projectGuid);
    }

    
    public List<AuditProjectMaterial> getProjectMaterialsByProjectGuid(String projectGuid) {
        String sql = "select a.rowguid,a.taskmaterial,a.cliengguid,b.submittype from audit_project_material a left join audit_task_material b "
                + " on a.taskmaterialguid = b.rowguid where a.projectguid = ?";
        List<AuditProjectMaterial> projectMaterials = localCommonDao.findList(sql, AuditProjectMaterial.class,
                projectGuid);
        return projectMaterials;
    }

    
    public AuditTask getAuditTask(String taskGuid) {
        StringBuilder sql = new StringBuilder("select * from audit_task where rowguid = ?");
        AuditTask auditTask = localCommonDao.find(sql.toString(), AuditTask.class, taskGuid);
        return auditTask;
    }

    
    public List<WorkflowWorkItem> getNormalWorkItems(String pviGuid) {
        StringBuffer sql = new StringBuffer("select t.*,b.ouname,b.oucode from (");
        sql.append(" select workitemguid,activityname,operatorguid,operatorfordisplayname,ouguid,createdate,");
        sql.append("enddate,opinion,case when activityname='受理' then '4' else '1' end as noderesult,");
        sql.append("case when activityname='受理' then '1' WHEN activityname = '办结' THEN '3' else '2' end as nodetype ");
        sql.append(" from workflow_workitem where processversioninstanceguid = ? and status = '80'");
        sql.append(" union");
        sql.append(" select workitemguid,activityname,operatorguid,operatorfordisplayname,ouguid,createdate,");
        sql.append("enddate, opinion, case when activityname='受理' then '4' else '1' end as noderesult,");
        sql.append("case when activityname='受理' then '1' WHEN activityname = '办结' THEN '3' else '2' end as nodetype ");
        sql.append(" from workflow_workitem_history where processversioninstanceguid = ? and status = '80' ) t");
        sql.append(" left join frame_ou b on t.ouguid = b.ouguid order by t.createdate");
        List<WorkflowWorkItem> workItems = localCommonDao.findList(sql.toString(), WorkflowWorkItem.class, pviGuid,
                pviGuid);
        return workItems;
    }

    /* 
    public List<WorkflowWorkItem> getNormalWorkItems(String pviGuid) {
        StringBuffer sql = new StringBuffer("select t.*,b.ouname,b.oucode from (");
        sql.append(" select workitemguid,activityname,operatorguid,operatorfordisplayname,ouguid,createdate,");
        sql.append("enddate,opinion,case when activityname='受理' then '4' else '1' end as noderesult");
        sql.append(" from workflow_workitem where processversioninstanceguid = ? and status = '80'");
        sql.append(" union");
        sql.append(" select workitemguid,activityname,operatorguid,operatorfordisplayname,ouguid,createdate,");
        sql.append("enddate, opinion, case when activityname='受理' then '4' else '1' end as noderesult");
        sql.append(" from workflow_workitem_history where processversioninstanceguid = ? and status = '80' ) t");
        sql.append(" left join frame_ou b on t.ouguid = b.ouguid order by t.createdate");
        List<WorkflowWorkItem> workItems = localCommonDao.findList(sql.toString(), WorkflowWorkItem.class, pviGuid,
                pviGuid);
        return workItems;
    }*/

    
    public int updateProjectSignStatus(String projectGuid, ProjectSyncStatus syncStatus) {
        String sql = "update audit_project set report_status = ?," + "report_date = now() where rowguid = ?";
        int affectRows = localCommonDao.execute(sql, syncStatus.getValue(), projectGuid);
        return affectRows;
    }

    
    public List<AuditProject> queryProjects(ProjectSyncStatus syncStatus) {
        // 办件若无法关联到事项，则不推送
        String sql = "select a.* from audit_project a join audit_task b on a.taskguid = b.rowguid"
                + " where b.shenpilb = '01' and a.status >= 90 and a.flowsn like '09%' and ifnull(a.searchpwd,'') <> ''"
                + " and ifnull(a.applyername,'') <> '' and a.report_status = ?";
        List<AuditProject> projects = localCommonDao.findList(sql, AuditProject.class, syncStatus.getValue());
        return projects;
    }

    
    public List<AuditProjectUnusual> getProjectUnusuals(String rowguid) {
        StringBuffer sql = new StringBuffer("select t.*,c.displayname,d.ouguid,d.oucode,d.ouname from (");
        sql.append(" select a.*,b.activityname");
        sql.append(" from AUDIT_PROJECT_UNUSUAL a left join workflow_workitem b");
        sql.append(" on a.workitemguid = b.workitemguid where a.projectguid = ?");
        sql.append(" union");
        sql.append(" select a.*,b.activityname");
        sql.append(" from AUDIT_PROJECT_UNUSUAL a left join workflow_workitem_history b");
        sql.append(" on a.workitemguid = b.workitemguid where a.projectguid = ?");
        sql.append(
                " ) t left join frame_user c on t.operateuserguid = c.userguid left join frame_ou d on d.ouguid = c.ouguid");
        return localCommonDao.findList(sql.toString(), AuditProjectUnusual.class, rowguid, rowguid);
    }

    
    public CertInfo getCertInfo(String certrowguid) {
        String sql = "SELECT * from cert_info WHERE RowGuid = ? ";
        return localCommonDao.find(sql, CertInfo.class, certrowguid);
    }

    /**
     * 查询已经办结且待上报的一件事信息
     * @param projectSyncStatus
     * @return
     */
    public List<AuditSpISubapp> queryAuditSpInstances() {
        String sql = "select b.* from audit_sp_instance a inner join audit_sp_i_subapp b on a.rowguid = b.biguid" +
                " inner join audit_sp_business c on a.businessguid = c.rowguid " +
                " where b.status = ? and ifnull(b.report_status,0) = 0 and c.is_need_sync = '1' and b.createdate > ? limit 100 ";
        String timestamp = "2025-01-01 00:00:00";
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String yjssynctimestamp = configService.getFrameConfigValue("yjssynctimestamp");
        if (StringUtil.isNotBlank(yjssynctimestamp)) {
            timestamp = yjssynctimestamp;
        }
        return localCommonDao.findList(sql, AuditSpISubapp.class, ZwfwConstant.LHSP_Status_YBJ,  timestamp);
    }

    public List<AuditProject> queryYjsAuditProjects() {
        String sql = "select a.* from audit_project a inner join audit_sp_i_subapp b on a.SUBAPPGUID = b.RowGuid " +
                " and b.report_status = '1' and ifnull(a.interface_report_status,0) = 0 and b.createdate > ? and a.status = 90" +
                " limit 240 ";
        String timestamp = "2025-01-24 00:00:00";
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String yjssynctimestamp = configService.getFrameConfigValue("yjssynctimestamp");
        if (StringUtil.isNotBlank(yjssynctimestamp)) {
            timestamp = yjssynctimestamp;
        }
        return localCommonDao.findList(sql, AuditProject.class, timestamp);
    }
}
