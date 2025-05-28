package com.epoint.power.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.AttachUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.gjdw.api.IGjdwHandleSPInstance;
import com.epoint.power.util.PowerDataUtil;
import com.epoint.xmz.auditelectricdata.api.IAuditElectricDataService;
import com.epoint.xmz.auditelectricdata.api.entity.AuditElectricData;
import com.epoint.xmz.auditelectricmaterialmapping.api.IAuditElectricMaterialMappingService;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.InputStream;
import java.util.*;

/**
 * 办件信息生成job
 */
@DisallowConcurrentExecution
public class ProjectInfoGenerateJob implements Job {

    private static final Logger logger = Logger.getLogger(ProjectInfoGenerateJob.class);

    private IAuditElectricDataService electricDataService;
    private IAuditTaskExtension auditTaskExtensionService;

    private IAuditElectricMaterialMappingService materialMappingService;

    private IAuditProject projectService;
    private IAuditOrgaServiceCenter serviceCenter;

    private IAuditTaskMaterial taskMaterialService;

    private IAuditProjectMaterial projectMaterialService;

    private IGjdwHandleSPInstance gjdwHandleSPInstance;

    private IAuditOrgaWindow windowService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            logger.info("***************办件信息生成job开始执行***************");
            EpointFrameDsManager.begin(null);
            electricDataService = ContainerFactory.getContainInfo().getComponent(IAuditElectricDataService.class);
            auditTaskExtensionService = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
            serviceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
            projectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            materialMappingService = ContainerFactory.getContainInfo().getComponent(IAuditElectricMaterialMappingService.class);
            taskMaterialService = ContainerFactory.getContainInfo().getComponent(IAuditTaskMaterial.class);
            projectMaterialService = ContainerFactory.getContainInfo().getComponent(IAuditProjectMaterial.class);
            gjdwHandleSPInstance = ContainerFactory.getContainInfo().getComponent(IGjdwHandleSPInstance.class);
            windowService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
            generateProjectInfoRecursive();
            EpointFrameDsManager.commit();
            logger.info("***************办件信息生成job结束执行***************");
        }
        catch (Exception e) {
            logger.info("***************办件信息生成job出错***************");
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    /**
     * 获取电力登记表循环生成办件信息
     */
    private void generateProjectInfoRecursive() {
        List<AuditElectricData> electricDataList = electricDataService.getElectricDataList();
        if (CollectionUtils.isNotEmpty(electricDataList)) {
            electricDataList.forEach(this::generateSingleProjectInfo);
            EpointFrameDsManager.commit();
            if (electricDataList.size() == 10) {
                generateProjectInfoRecursive();
            }
        }
    }

    /**
     * 生成办件信息
     *
     * @param electricData 电力信息表实体
     */
    private void generateSingleProjectInfo(AuditElectricData electricData) {
        try {
            String params = electricData.getParams();

            JSONObject paramJson = PowerDataUtil.dealPowerRequestParam(params);

            JSONObject innerItem = paramJson.getJSONObject("item").getJSONObject("DATALIST")
                    .getJSONObject("item").getJSONObject("TEXT").getJSONObject("item");

            String itemId = innerItem.getString("SPSXBH");
            AuditTask auditTask = electricDataService.getTaskByItemId(itemId);

            String projectGuid = UUID.randomUUID().toString();
            electricData.setProjectguid(projectGuid);
            electricData.setUpdatetime(new Date());

            AuditProject auditProject = new AuditProject();
            logger.info("开始生成电力办件");
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditProject.setOperateusername("电力办件生成job");
            auditProject.setOperatedate(new Date());
            auditProject.setRowguid(projectGuid);
            auditProject.setFlowsn(electricData.getFlowsn());

            if (auditTask != null) {
                auditProject.setTask_id(auditTask.getTask_id());
                auditProject.setTaskguid(auditTask.getRowguid());
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYTJ);// 办件状态：初始化
                auditProject.setOuguid(auditTask.getOuguid());
                auditProject.setOuname(auditTask.getOuname());
                auditProject.setProjectname(auditTask.getTaskname());
                auditProject.setIs_charge(auditTask.getCharge_flag());
                auditProject.setIf_express(ZwfwConstant.CONSTANT_STR_ZERO);
                // 中心
                AuditOrgaServiceCenter center = serviceCenter.getAuditServiceCenterByBelongXiaqu(auditTask.getAreacode());
                // 窗口
                if (center != null) {
                    auditProject.setCenterguid(center.getRowguid());
                    List<AuditOrgaWindow> windows = windowService.getWindowListByTaskId(auditTask.getTask_id()).getResult();
                    if (CollectionUtils.isNotEmpty(windows)) {
                        auditProject.setWindowguid(windows.get(0).getRowguid());
                        auditProject.setWindowname(windows.get(0).getWindowname());
                    }
                }

                auditProject.setAreacode(auditTask.getAreacode());
                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                if (auditTaskExtension != null) {
                    auditProject.setCharge_when(auditTaskExtension.getCharge_when());
                    auditProject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());// 进驻大厅
                    auditProject.setIf_express(auditTaskExtension.getIf_express());
                }
                auditProject.setTasktype(auditTask.getType());
                auditProject.setPromise_day(auditTask.getPromise_day());
                // 企业类型
                if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditTask.getApplyertype())) {
                    auditProject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_QY));
                    auditProject.setCerttype(ZwfwConstant.CERT_TYPE_ZZJGDMZ);// 申请人证照类型：组织机构代码
                }
                else {
                    // 个人类型
                    auditProject.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));
                    auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
                }

            }
            auditProject.setApplydate(new Date());// 办件申请时间
            auditProject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 办件申请方式：窗口申请
            auditProject.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditProject.setIs_delay(20);// 是否延期
            auditProject.setApplyername(innerItem.getString("SQRXM"));
            auditProject.setContactphone(innerItem.getString("SQRDH"));
            auditProject.setContactmobile(innerItem.getString("SQRSJ"));
            auditProject.setCertnum(innerItem.getString("SQRZJH"));
            auditProject.setCerttype(innerItem.getString("SQRZJLX"));
            // 材料
            logger.info(innerItem);
            JSONObject clList = innerItem.getJSONObject("SQCLQD");
            Object clItem = clList.get("item");
            List<String> UuidList = new ArrayList<>();
            if (clItem instanceof JSONObject) {
                JSONObject item = (JSONObject) clItem;
                generateProjectMaterial(item.getString("UUID"), auditTask, projectGuid, paramJson, item.getJSONObject("DJFJ").getString("content"));
            }
            else if (clItem instanceof JSONArray) {
                JSONArray itemList = (JSONArray) clItem;
                itemList.forEach(e -> {
                    JSONObject item = (JSONObject) e;
                    generateProjectMaterial(item.getString("UUID"), auditTask, projectGuid, paramJson, item.getJSONObject("DJFJ").getString("content"));
                });
            }
            // 调用工改提供方法
            projectService.addProject(auditProject);
            gjdwHandleSPInstance.initInstance(auditProject.getRowguid(), PowerDataUtil.getXmlContent(params));
            electricDataService.update(electricData);
        }
        catch (Exception e) {
            e.printStackTrace();
            electricData.setProjectguid(null);
            electricData.setError(e.getMessage());
            electricData.setUpdatetime(new Date());
            electricDataService.update(electricData);
        }
    }

    private void generateProjectMaterial(String clly, AuditTask auditTask, String projectGuid, JSONObject paramJson, String fileName) {
        logger.info("开始生成电力办件材料");
        AuditElectricMaterialMapping materialMapping = materialMappingService.getMappingDataByUuid(clly, auditTask.getItem_id());
        if (materialMapping != null) {
            AuditTask taskMapping = electricDataService.getTaskByItemId(materialMapping.getItemid());
            logger.info("获取材料配置：" + taskMapping);
            if (taskMapping != null) {
                AuditTaskMaterial taskMaterial = taskMaterialService.selectTaskMaterialByTaskGuidAndMaterialId(
                        taskMapping.getRowguid(), materialMapping.getMaterialid()).getResult();
                if (taskMaterial != null) {
                    logger.info("生成办件材料：" + taskMaterial.getRowguid() + ";" + projectGuid);
                    AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                    auditProjectMaterial.clear();
                    auditProjectMaterial.setRowguid(UUID.randomUUID().toString());
                    auditProjectMaterial.setOperatedate(new Date());
                    auditProjectMaterial.setTaskguid(auditTask == null ? "" : auditTask.getRowguid());
                    auditProjectMaterial.setProjectguid(projectGuid);
                    auditProjectMaterial.setTaskmaterialguid(taskMaterial.getRowguid());
                    auditProjectMaterial.setStatus(10);
                    auditProjectMaterial.setAuditstatus(ZwfwConstant.Material_AuditStatus_WTJ);
                    auditProjectMaterial.setIs_rongque(0);
                    String cliengGuid = UUID.randomUUID().toString();
                    auditProjectMaterial.setCliengguid(cliengGuid);
                    auditProjectMaterial.setAttachfilefrom("1");
                    auditProjectMaterial.setTaskmaterial(taskMaterial.getMaterialname());
                    projectMaterialService.addProjectMateiral(auditProjectMaterial);
                    logger.info("开始保存附件");
                    // 保存附件
                    JSONArray fileList = new JSONArray();
                    JSONObject fileItem = paramJson.getJSONObject("item").getJSONObject("DATALIST")
                            .getJSONObject("item");
                    Object streamDatas = fileItem.getJSONObject("STREAMINGDATAS").get("item");
                    if (streamDatas instanceof JSONObject) {
                        fileList.add(streamDatas);
                    }
                    else if (streamDatas instanceof JSONArray) {
                        fileList = (JSONArray) streamDatas;
                    }
                    if (CollectionUtils.isNotEmpty(fileList)) {
                        for (Object file : fileList) {
                            JSONObject fileObj = (JSONObject) file;
                            String attachName = fileObj.getString("FILENAME");
                            if (!Objects.equals(attachName, fileName)) {
                                continue;
                            }
                            String contentType = StringUtil.isNotBlank(fileName) ? "." + fileName.split("\\.")[1] : "";
                            InputStream fileContent = null;
                            try {
                                fileContent = PowerDataUtil.base2InputStream(fileObj.getString("DATAHANDLER"));
                                AttachUtil.saveFileInputStream(UUID.randomUUID().toString(), cliengGuid,
                                        attachName, contentType, null, fileContent.available(), fileContent, null, null);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                            finally {
                                try {
                                    if (fileContent != null) {
                                        fileContent.close();
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
