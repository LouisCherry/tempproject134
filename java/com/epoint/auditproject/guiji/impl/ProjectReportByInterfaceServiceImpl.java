package com.epoint.auditproject.guiji.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.spring.util.SpringContextUtil;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.auditproject.guiji.api.ProjectReportByInterfaceService;
import com.epoint.auditproject.guiji.constant.MaterialType;
import com.epoint.auditproject.guiji.constant.ProjectShStatus;
import com.epoint.auditproject.guiji.constant.WorkItemExtraColumn;
import com.epoint.auditproject.guiji.dto.JiaoFeiDto;
import com.epoint.auditproject.guiji.dto.ProjectDto;
import com.epoint.auditproject.guiji.dto.ReportResult;
import com.epoint.auditproject.guiji.utils.FileUtils;
import com.epoint.auditproject.guiji.utils.HttpUtil;
import com.epoint.auditproject.guiji.utils.SM2Util;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

/**
 * 办件信息上报service
 *
 * @author 刘雨雨
 * @time 2018年8月20日上午9:27:41
 */
public class ProjectReportByInterfaceServiceImpl implements ProjectReportByInterfaceService {

    private Logger logger = Logger.getLogger(ProjectReportByInterfaceServiceImpl.class);

    private IAttachService attachService = SpringContextUtil.getBeanByType(IAttachService.class);

    private LocalProjectServiceImpl localProjectService;
    

    private String systemname = "济宁市政务服务平台";

    /**
     * 构造函数
     *
     * @param localCommonDao      本地业务库
     * @param projectQzkCommonDao 办件前置库
     * @param dzjcCommonDao       电子监察前置库
     */
    public ProjectReportByInterfaceServiceImpl(ICommonDao localCommonDao, ICommonDao projectQzkCommonDao,
                                               ICommonDao dzjcCommonDao) {
        localProjectService = new LocalProjectServiceImpl();

        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String configsysname = configService.getFrameConfigValue("systemname");
        if (StringUtil.isNotBlank(configsysname)) {
            systemname = configsysname;
        }
    }

    public ProjectReportByInterfaceServiceImpl() {
        localProjectService = new LocalProjectServiceImpl();
        

        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        String configsysname = configService.getFrameConfigValue("systemname");
        if (StringUtil.isNotBlank(configsysname)) {
            systemname = configsysname;
        }
    }
    

    @Override
    public void reportNever() {
        List<AuditProject> projectsOfNeverSync = localProjectService.queryProjectsOfNeverSync();
        logger.info("本次查询到的待上报的办件记录共有(接口上报):" + projectsOfNeverSync.size() + "条...");
        if (CollectionUtils.isNotEmpty(projectsOfNeverSync)) {
            logger.info("开始上报...");
            // todo
            ReportResult reportResult = startReport(projectsOfNeverSync.subList(0, 1));
            logger.info("结束上报，成功：" + reportResult.getSuccessCount() + "条，失败:" + reportResult.getFailedCount() + "条");
        } else {
            logger.info("结束上报...");
        }
    }

    @Override
    public void reportFailed() {
        List<AuditProject> projectsOfSyncFailed = localProjectService.queryProjectsOfSyncFailed();
        logger.info("本次查询到的上报失败的办件记录共有:" + projectsOfSyncFailed.size() + "条...");
        if (CollectionUtils.isNotEmpty(projectsOfSyncFailed)) {
            logger.info("开始上报...");
            ReportResult reportResult = startReport(projectsOfSyncFailed);
            logger.info("结束上报，成功：" + reportResult.getSuccessCount() + "条，失败:" + reportResult.getFailedCount() + "条");
        } else {
            logger.info("结束上报...");
        }
    }

    @Override
    public ReportResult startReport(List<AuditProject> projects) {
        if (CollectionUtils.isEmpty(projects)) {
            return new ReportResult(0, 0, 0);
        }
        int successCount = 0;
        int failedCount = 0;
        for (AuditProject project : projects) {
            try {
                logger.info("本次上报的办件：编号:" + project.getFlowsn());
                ProjectDto projectDto = localProjectService.genProjectDto(project);
                // 办件基本信息
                JSONObject basicInfo = createBasicInfo(project, projectDto);
                //办件材料数据生成
                List<JSONObject> preFiles = createPreFiles(project, projectDto);
                // 审批过程信息
                List<JSONObject> stepProcs = createStepProcs(project, projectDto);
                // 办结信息
                JSONObject stepDone = createStepDone(project, projectDto);
                // 特殊环节信息
                List<JSONObject> specialNodes = createSpecialNodes(project, projectDto);
                try {

                    // 基本信息推送结果
                    boolean baseinfopushresult = true;
                    // 办件推送结果
                    boolean pushresult = true;
                    // 判断办件基本信息是否已经上报
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(project.getStr("baseinfosync"))) {
                        // 上报办件基本信息
                        String result = pushProjectInfo("receiveBasicInfo", basicInfo);
                        baseinfopushresult = checkPushResult(result);
                        if (baseinfopushresult) {
                            localProjectService.updateToBaseInfoSyncSuccess(project);
                        }
                    }
                    if (baseinfopushresult) {
                        // 上报办件审批过程
                        String doneResult = "";
                        for (int i = 0; i < stepProcs.size(); i++) {
                            doneResult = pushProjectInfo("receiveProc", stepProcs.get(i));
                            pushresult = checkPushResult(doneResult);
                            if(!pushresult){
                                break;
                            }
                        }
                        
                        if(pushresult){
                            // 上报办件特殊环节
                            for (int i = 0; i < specialNodes.size(); i++) {
                                doneResult = pushProjectInfo("receiveSpecialnode", specialNodes.get(i));
                                pushresult = checkPushResult(doneResult);
                                if(!pushresult){
                                    break;
                                }
                            }
                        }

                        if(pushresult){
                            // 上报办件申请材料
                            for (int i = 0; i < preFiles.size(); i++) {
                                doneResult = pushProjectInfo("receiveApplyFile", preFiles.get(i));
                                pushresult = checkPushResult(doneResult);
                                if(!pushresult){
                                    break;
                                }
                            }
                        }

                        if(pushresult){
                            // 上报办件办结信息
                            doneResult = pushProjectInfo("receiveDone", stepDone);
                            pushresult = checkPushResult(doneResult);
                        }

                    }
                    if (pushresult && baseinfopushresult) {
                        localProjectService.updateToSuccess(project.getRowguid());
                        logger.info("办件上报成功,办件编号:" + project.getFlowsn());
                        logger.info("------------------------");
                        successCount++;
                    } else {
                        logger.info("办件上报失败,办件编号:" + project.getFlowsn());
                        localProjectService.updateToFailed(project);
                        failedCount++;
                    }
                } catch (Exception e) {
                    logger.info("办件上报失败,办件编号:" + project.getFlowsn());
                    logger.error(e.getMessage(), e);
                    localProjectService.updateToFailed(project);
                    failedCount++;
                }
            } catch (Exception e) {
                logger.error("办件上报遇到异常，办件编号:" + project.getFlowsn());
                logger.error(e.getMessage(), e);
                localProjectService.updateToFailed(project);
                failedCount++;
            }
        }
        return new ReportResult(successCount, failedCount, projects.size());
    }

    /**
     * 上报办件基本信息和第一个环节以用于统一评价
     *
     * @param project
     * @return
     */
    @Override
    public ReportResult reportBasic(AuditProject project) {
        if (project == null) {
            return new ReportResult(0, 0, 0);
        }
        int successCount = 0;
        int failedCount = 0;
        try {
            IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            project = iAuditProject.getAuditProjectByRowGuid("*", project.getRowguid(), project.getAreacode()).getResult();
            logger.info("本次上报基本信息的办件：编号:" + project.getFlowsn());
            ProjectDto projectDto = localProjectService.genProjectDto(project);

            JSONObject basicInfo = createBasicInfo(project, projectDto); // 办件基本信息
            List<JSONObject> stepProcs = createStepProcs(project, projectDto); // 审批过程信息
            try {

                // 基本信息推送结果
                boolean baseinfopushresult = true;
                // 判断办件基本信息是否已经上报
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(project.getStr("baseinfosync"))) {
                    // 上报办件基本信息
                    String result = pushProjectInfo("receiveBasicInfo", basicInfo);
                    baseinfopushresult = checkPushResult(result);
                    if(baseinfopushresult){
                        JSONObject stepInfo = stepProcs.get(0);
                        stepInfo.put("serviceidentify", ZwfwConstant.CONSTANT_STR_ONE); // 是否产生服务，为是时才会推送好差评
                        if (StringUtil.isBlank(stepInfo.getString("nodetype"))) {
                            stepInfo.put("nodetype", ZwfwConstant.CONSTANT_STR_ONE);
                            stepInfo.put("acceptdate", EpointDateUtil.convertDate2String(projectDto.getShouliTime(), EpointDateUtil.DATE_TIME_FORMAT)); // 受理时间 环节处理结果为受理或不予受理时，此项必填
                            IAuditOrgaWorkingDay workingDayService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWorkingDay.class);
                            AuditTask auditTask = projectDto.getAuditTask();
                            if (auditTask.getPromise_day() != null) {
                                Date promisedate = workingDayService.getWorkingDayWithOfficeSet(project.getCenterguid(), projectDto.getShouliTime(), auditTask.getPromise_day()).getResult();
                                if (promisedate != null) {
                                    stepInfo.put("promisedate", EpointDateUtil.convertDate2String(
                                            promisedate, EpointDateUtil.DATE_TIME_FORMAT));  // 承诺办结时间，受理时间+事项的承诺办结时间
                                }
                            }
                        }
                        stepInfo.put("servicename", "业务受理信息");
                        stepInfo.put("servicetime", stepInfo.getString("nodestarttime"));
                        stepInfo.put("servicenumber", ZwfwConstant.CONSTANT_STR_ONE);
                        pushProjectInfo("receiveProc", stepInfo);
                    }

                    if (baseinfopushresult) {
                        localProjectService.updateToBaseInfoSyncSuccess(project);
                    }
                }

            } catch (Exception e) {
                logger.info("办件基本信息上报失败,办件编号:" + project.getFlowsn());
                logger.error(e.getMessage(), e);
                failedCount++;
            }
        } catch (Exception e) {
            logger.error("办件基本信息上报遇到异常，办件编号:" + project.getFlowsn());
            logger.error(e.getMessage(), e);
            failedCount++;
        }

        return new ReportResult(successCount, failedCount, ZwfwConstant.CONSTANT_INT_ONE);
    }




    /**
     * 生成基础信息
     * @param project
     * @param projectDto
     * @return
     */
    public JSONObject createBasicInfo(AuditProject project, ProjectDto projectDto) {
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        String searchPassword = projectDto.getSearchPassword();
        AuditTask auditTask = projectDto.getAuditTask();
        String regionId = projectDto.getRegionId();
        FrameOu acceptDept = projectDto.getAcceptDept();
        String projectType = projectDto.getProjectType();
        String promiseLimitUnit = projectDto.getPromiseLimitUnit(); // 默认承诺期限单位
        // 1 工作日
        String legalLimitUnit = ZwfwConstant.CONSTANT_STR_ONE; // 法定期限单位 1 工作日
        String applyFrom = projectDto.getApplyFrom(); // 网上提交还是窗口提交
        Date occurTime = projectDto.getShouliTime(); // 受理时间
        String transactor = projectDto.getShouliRenName(); // 受理人
        String signState = "0"; // 前置库中默认就是0
        JSONObject basicInfo = new JSONObject();
        basicInfo.put("orgbusno", project.getRowguid());
        basicInfo.put("projid", project.getFlowsn());
        basicInfo.put("projpwd", searchPassword);
        basicInfo.put("itemno", auditTask.getStr("New_ITEM_CODE"));
        String catalogCode = "";
        String qlfullid = auditTask.getCatalogcode();
        /**if (StringUtil.isNotBlank(qlfullid) && qlfullid.length() > 12) {
         catalogCode = qlfullid.substring(0, 11);
         } else {
         catalogCode = qlfullid;
         }**/
        basicInfo.put("catalogcode", qlfullid);// 不可空，对应事项在山东省政务服务事项管理系统中的基本编码

        String itemid = auditTask.getItem_id();
        String taskHandleItem = null;
        int length = itemid.length();
        if (length != 31) {// 33
            // 业务办理项编码
            itemid = itemid.substring(0, 31);
            taskHandleItem = auditTask.getItem_id();
        } else {
            itemid = auditTask.getItem_id();
        }
        basicInfo.put("taskcode", itemid);// 不可空，对应事项在山东省政务服务事项管理系统中的基本编码
        basicInfo.put("taskhandleitem", taskHandleItem);
        basicInfo.put("itemname", auditTask.getTaskname());
        basicInfo.put("itemname", auditTask.getTaskname());
        basicInfo.put("itemversion", auditTask.getVersion());
        basicInfo.put("regionid", regionId);
        basicInfo.put("itemtype", getshenpilb(auditTask.getShenpilb()));
        basicInfo.put("itemregionid", regionId);
        basicInfo.put("projectname", "关于" + project.getApplyername() + "的" + auditTask.getTaskname());
        basicInfo.put("applicant", project.getApplyername());
        //服务对象
        basicInfo.put("applyertype", getServiceObject(project.getApplyertype()));
        //证件类型
        basicInfo.put("applyerpagetype", getCerttype(project.getCerttype()));
        basicInfo.put("applyerpagecode", project.getCertnum());
        basicInfo.put("applydate", EpointDateUtil.convertDate2String(project.getApplydate(), EpointDateUtil.DATE_TIME_FORMAT));
        basicInfo.put("submit", applyFrom);
        if (project.getApplyertype() == 20) { // 个人
            basicInfo.put("legalman", null);// 申请人证件类型为营业执照或申请人类型为企业法人或事业法人及其他社团组织，则该要素必填
        } else {
            if (StringUtil.isBlank(project.getLegal())) {
                basicInfo.put("legalman", project.getContactperson());// 申请人证件类型为营业执照或申请人类型为企业法人或事业法人及其他社团组织，则该要素必填
            } else {
                basicInfo.put("legalman", project.getLegal());// 申请人证件类型为营业执照或申请人类型为企业法人或事业法人及其他社团组织，则该要素必填
            }
        }
        basicInfo.put("applicantname", project.getApplyername());
        if (StringUtil.isNotBlank(project.getContactcertnum())) {
            basicInfo.put("applicanttype", "111");// 联系人/代理人证件类型 字典“证件类型”, 可为空
            basicInfo.put("applicantcode", project.getContactcertnum());// 联系人/代理人证件号码
            // 可为空
        } else {
            basicInfo.put("applicanttype", null);// 联系人/代理人证件类型 字典“证件类型”, 可为空
            basicInfo.put("applicantcode", null);// 联系人/代理人证件号码 可为空
        }
        if (StringUtil.isNotBlank(project.getContactmobile())) {
            basicInfo.put("applicantmobile", project.getContactmobile()); // 申报者手机，不为空
        } else {
            basicInfo.put("applicantmobile", project.getContactphone()); // 申报者手机，不为空
        }
        basicInfo.put("address", project.getAddress());
        basicInfo.put("acceptdeptname", acceptDept.getOuname());
        basicInfo.put("acceptdeptid", acceptDept.getOucode());

        String orgcode = "";
        FrameOuExtendInfo ouExtendInfo = iOuService.getFrameOuExtendInfo(project.getOuguid());
        if (ouExtendInfo != null && StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))) {
            orgcode = ouExtendInfo.getStr("orgcode");
        }

        basicInfo.put("acceptdeptcode", orgcode); // 部门统一社会信用代码
        basicInfo.put("approvaltype", projectType);
        basicInfo.put("promisetimelimit", new BigDecimal(auditTask.getPromise_day()));
        basicInfo.put("promisetimeunit", promiseLimitUnit);
        basicInfo.put("timelimit", new BigDecimal(auditTask.getAnticipate_day()));
        basicInfo.put("timeunit", legalLimitUnit);
        if (ZwfwConstant.APPLY_WAY_CKDJ.equals(String.valueOf(project.getApplyway()))) {
            basicInfo.put("datasources", "2"); // 服务渠道：窗口登记默认为政务服务中心
        } else if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(String.valueOf(project.getApplyway())) || ZwfwConstant.APPLY_WAY_NETZJSB.equals(String.valueOf(project.getApplyway()))) {
            basicInfo.put("datasources", "1"); // 服务渠道：互联网政务服务门户
        } else {
            basicInfo.put("datasources", "9"); // 其他
            basicInfo.put("otherdatasource", project.getApplyway());
        }
        basicInfo.put("scxfsign", "0"); // 下发标识
        basicInfo.put("datasourcessysname", systemname); // 数据来源系统名称
        basicInfo.put("systemlevel", "3"); // 系统层级 ：市级
        basicInfo.put("isagency", "0"); // 是否待办
        basicInfo.put("isdraft", "0"); // 是否草稿
        basicInfo.put("ischain", "0"); // 是否一件事业务
        return basicInfo;
    }

    /**
     * 证件类型
     *
     * @description
     * @author shibin
     * @date 2020年7月5日 下午10:25:22
     */
    private String getCerttype(String certtype) {
        String lctype = null;
        switch (certtype) {
            case "13":
                lctype = "999";
                break;
            case "14":
                lctype = "001";
                break;
            case "15":
                lctype = "001";
                break;
            case "16":
                lctype = "001";
                break;
            case "22":
                lctype = "111";
                break;
            case "23":
                lctype = "414";
                break;
            case "24":
                lctype = "511";
                break;
            case "25":
                lctype = "111";
                break;
            default:
                lctype = "111";
                break;
        }
        return lctype;
    }

    /**
     * 服务对象
     *
     * @description
     * @author shibin
     * @date 2020年7月3日 下午2:53:06
     */
    private String getServiceObject(Integer applyertype) {

        String lctype = null;
        switch (applyertype) {
            case 20:
                lctype = "1";
                break;
            default:
                lctype = "2";
                break;
        }
        return lctype;
    }

    /**
     * 事项类型
     *
     * @description
     * @author shibin
     * @date 2020年7月3日 下午2:46:42
     */
    private String getshenpilb(String shenpilb) {

        String lcshenpilb = null;
        switch (shenpilb) {
            case "06":
                lcshenpilb = "09";
                break;
            case "09":
                lcshenpilb = "06";
                break;
            case "11":
                lcshenpilb = "20";
                break;
            default:
                lcshenpilb = shenpilb;
                break;
        }
        return lcshenpilb;
    }

    /**
     * 办件材料数据生成
      * @param project
     * @param projectDto
     * @return
     */    
    public List<JSONObject> createPreFiles(AuditProject project, ProjectDto projectDto) {
        
        List<JSONObject> preFiles = new ArrayList<JSONObject>();
        Date shouliTime = projectDto.getShouliTime();
        String isTake = "1"; // 是否收取1收取0没有收
        String amount = "1"; // 收取数量，记录所收取材料的数量
        List<AuditProjectMaterial> projectMaterials = projectDto.getProjectMaterials();
        int sequence = 1;
        if (CollectionUtils.isNotEmpty(projectMaterials)) {
            for (int i = 0, length = projectMaterials.size(); i < length; i++) {

                BigDecimal sn = new BigDecimal(sequence++);
                AuditProjectMaterial projectMaterial = projectMaterials.get(i);
                String submitType = localProjectService.getMaterialSubmitType(projectMaterial);
                List<FrameAttachStorage> storages = attachService.getAttachListByGuid(projectMaterial.getCliengguid());
                FrameAttachStorage storage = new FrameAttachStorage();
                if (CollectionUtils.isNotEmpty(storages)) {
                    storage = storages.get(0);
                }
                byte[] entity = FileUtils.readBytesFromInputStream(storage.getContent());
                JSONObject preFile = new JSONObject();
                preFile.put("unid", projectMaterial.getRowguid());
                preFile.put("projid", project.getFlowsn());
                preFile.put("regionid", projectDto.getRegionId());
                preFile.put("attrname", projectMaterial.getTaskmaterial());
                preFile.put("attrid", projectMaterial.getRowguid());
                // 1、收取纸质材料 2、上传电子文件 3、证照库引用 4、数据共享 9、其他
                submitType = projectMaterial.getStr("submittype");
                if (MaterialType.ELECTRONIC.getValue().equals(submitType)) {
                    submitType = "2";
                } else if (MaterialType.PAPER.getValue().equals(submitType)) {
                    submitType = "1";
                } else if (MaterialType.ELECTRONIC_OR_PAPER.getValue().equals(submitType)) {
                    submitType = "2";
                } else if (MaterialType.ELECTRONIC_AND_PAPER.getValue().equals(submitType)) {
                    submitType = "2";
                } else {
                    submitType = "2";
                }

                if (projectMaterial == null || projectMaterial.getStatus() == null || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == (projectMaterial.getStatus()!= null? projectMaterial.getStatus() : null)) {
                    isTake = ZwfwConstant.CONSTANT_STR_ZERO;
                    amount = "0";
                }
                preFile.put("gettype", submitType);
                preFile.put("istake", isTake);
                preFile.put("amount", amount);
                preFile.put("taketime", EpointDateUtil.convertDate2String(shouliTime, EpointDateUtil.DATE_TIME_FORMAT));
                preFile.put("filename", storage.getAttachFileName());
                preFile.put("memo", null);
                preFile.put("scxfsign", ZwfwConstant.CONSTANT_STR_ZERO);
                preFile.put("datasourcessysname", systemname);
                preFile.put("systemlevel", ZwfwConstant.CONSTANT_STR_THREE);

                preFiles.add(preFile);
            }
        }
        return preFiles;
    }

    /**
     * 审批过程信息
     * @param project
     * @param projectDto
     * @return
     */
    public List<JSONObject> createStepProcs(AuditProject project, ProjectDto projectDto) {

        AuditTask auditTask = projectDto.getAuditTask();
        ArrayList<JSONObject> processes = new ArrayList<JSONObject>();
        List<WorkflowWorkItem> workItems = localProjectService.getNormalWorkItems(project, projectDto);
        IAuditOrgaWorkingDay workingDayService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWorkingDay.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        if (CollectionUtils.isEmpty(workItems)) {
            // 未查询到流程信息的，默认受理-审核-办结
            workItems = createWorkflowWorkItem(project, projectDto);
        }
        if (CollectionUtils.isNotEmpty(workItems)) {
            int sequence = 1;
            for (WorkflowWorkItem workItem : workItems) {
                Date nodeEndTime = workItem.getEndDate() != null ? workItem.getEndDate() : workItem.getCreateDate();
                String nodeState = "02";
                String nodeType = workItem.getStr("nodetype");
                String nodeResult = localProjectService.getNodeResult(workItem.getActivityName());
                BigDecimal sn = new BigDecimal(sequence++);
                // （1）nodetype 等于 1 时代表起始环节，noderesult 对应的值是：4-受理，5
                // 补齐补正，6-特别程序（挂起操作）。
                // （2）nodetype 等于 2 时代表中间环节；noderesult 对应的值是：0 - 不同意， 1 -
                // 同意，5-补齐补正，6-特别程序（挂起操作），7-退回。
                // （3）nodetype 等于 3 时代表结束环节，noderesult 对应的值是：0 - 不同意， 1 - 同意。
                // （4）nodetype 等于 4 时代表开始及结束（比如即办件，直接不予受理、不受理），
                // noderesult 对应的值是：1 - 同意，2-不受理，3-不予受理，4-受理。
                JSONObject process = new JSONObject();
                process.put("orgbusno", project.getRowguid()); // 办件流水号，不可空，由业务系统按规则自动生成
                process.put("projid", project.getFlowsn()); // 办件编号
                process.put("noderesult", nodeResult); // 环节处理结果
                process.put("sn", sn); // 审批过程序号，数字递增
                process.put("nodename", workItem.getActivityName()); // 环节名称
                process.put("nodecode", ""); // 环节批次id，记录环节的批次信息，可为空
                process.put("nodetype", nodeType); // 环节类型 // 1-开始环节；2-中间环节；3-结束环节；4-只有一条审批数据（开始既是结束）
                process.put("nodeprocername", workItem.getOperatorForDisplayName()); // 环节处理人姓名
                process.put("regionid", projectDto.getRegionId()); // 环节处理人行政区划
                process.put("procunit", workItem.getStr(WorkItemExtraColumn.OUCODE_KEY)); // 处理单位id，组织机构代码，不可空，组织机构代码
                String orgcode = "";
                String operatorGuid = workItem.getOperatorGuid();
                FrameUser frameUser = userService.getUserByUserField("userguid", operatorGuid);
                if (frameUser != null) {
                    FrameOuExtendInfo ouExtendInfo = ouService.getFrameOuExtendInfo(frameUser.getOuGuid());
                    if (ouExtendInfo != null && StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))) {
                        orgcode = ouExtendInfo.getStr("orgcode");
                    }
                }
                process.put("procdeptcode", orgcode); // 处理单位统一社会信用代码
                process.put("procunitname", workItem.getStr(WorkItemExtraColumn.OUNAME_KEY)); // 处理单位名称
                process.put("nodestarttime", EpointDateUtil.convertDate2String(workItem.getCreateDate(), EpointDateUtil.DATE_TIME_FORMAT)); // 环节开始时间
                process.put("nodeendtime", EpointDateUtil.convertDate2String(nodeEndTime, EpointDateUtil.DATE_TIME_FORMAT)); // 环节结束时间
                process.put("nodeadv", workItem.getOpinion()); // 环节处理意见
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(nodeType)) {
                    process.put("acceptdate", EpointDateUtil.convertDate2String(workItem.getCreateDate(), EpointDateUtil.DATE_TIME_FORMAT)); // 受理时间 环节处理结果为受理或不予受理时，此项必填

                    if (auditTask.getPromise_day() != null) {
                        Date promisedate = workingDayService.getWorkingDayWithOfficeSet(project.getCenterguid(), projectDto.getShouliTime(), auditTask.getPromise_day()).getResult();
                        if (promisedate != null) {
                            process.put("promisedate", EpointDateUtil.convertDate2String(
                                    promisedate, EpointDateUtil.DATE_TIME_FORMAT));  // 承诺办结时间，受理时间+事项的承诺办结时间
                        }
                    }
                }
                process.put("datasourcessysname", systemname); // 数据来源系统名称
                process.put("systemlevel", ZwfwConstant.CONSTANT_STR_THREE); // 数据来源系统层级，市级
                //办结环节需要推送评价
                if("3".equals(nodeType)){
                    process.put("serviceidentify", ZwfwConstant.CONSTANT_STR_ONE); // 是否产生服务，为是时会推送办件到好差评
                    process.put("servicename", "业务办结");
                    process.put("servicetime", EpointDateUtil.convertDate2String(nodeEndTime, EpointDateUtil.DATE_TIME_FORMAT));
                    process.put("servicenumber", "1");
                }else{
                    process.put("serviceidentify", ZwfwConstant.CONSTANT_STR_ZERO); // 是否产生服务，为是时会推送办件到好差评
                }
                
                process.put("scxfsign", ZwfwConstant.CONSTANT_STR_ZERO);
                processes.add(process);
            }
        }
        return processes;
    }

    /**
     * @description
     * @author shibin
     * @date 2020年7月28日 下午2:07:19
     */
    private List<WorkflowWorkItem> createWorkflowWorkItem(AuditProject project, ProjectDto projectDto) {

        List<WorkflowWorkItem> workItems = new ArrayList<>();
        String shouliOuName = projectDto.getAcceptDept().getOuname();
        String shouliOuCode = projectDto.getAcceptDept().getOucode();
        String banjieOuName = projectDto.getBanjieDept().getOuname();
        String banjieOuCode = projectDto.getBanjieDept().getOucode();
        Date banjieDate = projectDto.getBanjieDate();
        // noderesult 4受理 1同意
        WorkflowWorkItem shouli = newWorkItem("1", "受理", projectDto.getShouliRenGuid(), projectDto.getShouliRenName(),
                shouliOuName, shouliOuCode, "4", banjieDate, banjieDate, project);
        WorkflowWorkItem shenhe = newWorkItem("2", "审核", project.getBanjieuserguid(), project.getBanjieusername(),
                banjieOuName, banjieOuCode, "1", banjieDate, banjieDate, project);
        WorkflowWorkItem banjie = newWorkItem("3", "办结", project.getBanjieuserguid(), project.getBanjieusername(),
                banjieOuName, banjieOuCode, "1", banjieDate, banjieDate, project);
        workItems.add(shouli);
        workItems.add(shenhe);
        workItems.add(banjie);

        return workItems;
    }

    /**
     * 生成一个工作项对象，用作审批过程
     *
     * @param activityName
     * @param operatorGuid
     * @param operatorName
     * @param ouName
     * @param ouCode
     * @param nodeResult
     * @return
     */
    private WorkflowWorkItem newWorkItem(String nodetype, String activityName, String operatorGuid, String operatorName,
                                         String ouName, String ouCode, String nodeResult, Date createDate, Date endDate, AuditProject project) {
        if (StringUtil.isBlank(operatorGuid)) {
            if (StringUtil.isNotBlank(project.getAcceptuserguid())) {
                operatorGuid = project.getAcceptuserguid();
            } else if (StringUtil.isNotBlank(project.getReceiveuserguid())) {
                operatorGuid = project.getReceiveuserguid();
            } else {
                operatorGuid = UUID.randomUUID().toString();
                logger.info("随机生成流程处理人标识信息：" + operatorGuid);
            }
        }
        if (StringUtil.isBlank(operatorName)) {
            if (StringUtil.isNotBlank(project.getAcceptusername())) {
                operatorName = project.getAcceptusername();
            } else if (StringUtil.isNotBlank(project.getReceiveusername())) {
                operatorName = project.getReceiveusername();
            } else {
                operatorName = project.getOperateusername();
            }
        }

        WorkflowWorkItem process = new WorkflowWorkItem();
        process.setActivityName(activityName);
        process.setWorkItemGuid(UUID.randomUUID().toString());
        process.setOperatorGuid(operatorGuid);
        process.setOperatorForDisplayName(operatorName);
        process.set(WorkItemExtraColumn.OUNAME_KEY, ouName);
        process.set(WorkItemExtraColumn.OUCODE_KEY, ouCode);
        process.set(WorkItemExtraColumn.NODE_RESULT_KEY, nodeResult);
        process.set("nodetype", nodetype);
        process.setCreateDate(createDate);
        if (endDate == null) {
            endDate = createDate;
        }
        process.setEndDate(endDate);
        process.setOpinion(activityName);
        return process;
    }

    /**
     * 办结信息
     * @param project
     * @param projectDto
     * @return
     */
    public JSONObject createStepDone(AuditProject project, ProjectDto projectDto) {

        AuditTask auditTask = projectDto.getAuditTask();
        // BigDecimal certificatenam = new BigDecimal(1); // 旧数据存放的就是1
        BigDecimal doneResult = new BigDecimal(6);
        if (ProjectShStatus.ZCBJ.getValue().equals(project.getStatus())) {
            doneResult = new BigDecimal(6);
            if ("50".equals(project.getBanjieresult())) {
                doneResult = new BigDecimal(7);
            }
        } else if (ProjectShStatus.BYSL.getValue().equals(project.getStatus())) {
            doneResult = new BigDecimal(1);
        } else if (ProjectShStatus.YCZZ.getValue().equals(project.getStatus())) {
            doneResult = new BigDecimal(3);
        } else if (ProjectShStatus.CXSQ.getValue().equals(project.getStatus())) {
            doneResult = new BigDecimal(3);
        }
        String exitres = localProjectService.getExitres(doneResult.intValue());
        BigDecimal totalJine = new BigDecimal(0);
        String feeStandAccord = projectDto.getAuditTask().getCharge_basis(); // 收费依据
        if (feeStandAccord != null && feeStandAccord.length() * 2 > 790) {// 前置库最大长度800
            feeStandAccord = feeStandAccord.substring(390);
        }
        CertInfo certInfo = localProjectService.getCertInfo(project.getCertrowguid());
        JiaoFeiDto jiaoFeiDto = projectDto.getJiaoFeiDto(); // 缴费人相关信息
        JSONObject stepDone = new JSONObject();
        stepDone.put("orgbusno", project.getRowguid()); // 原系统中具体业务的唯一id
        stepDone.put("projid", project.getFlowsn()); // 办件的唯一标识
        stepDone.put("regionid", projectDto.getRegionId()); // 不可空，12 位标准区划
        stepDone.put("doneresult", doneResult); // 办理结果
        stepDone.put("exitres", exitres); // 作废或退回原因，在办结结果是上述的1、4、5时，本字段必须写明原因
        stepDone.put("isdeliveryresults", ZwfwConstant.CONSTANT_STR_ZERO); // 是否快递递送结果

        // 获取事项的办理结果类型
        IAuditTaskResult iAuditTaskResult = ContainerFactory.getContainInfo().getComponent(IAuditTaskResult.class);
        AuditTaskResult result = iAuditTaskResult.getAuditResultByTaskGuid(auditTask.getRowguid(), true).getResult();
        if (result != null) {
            if (ZwfwConstant.LHSP_RESULTTYPE_ZZ == result.getResulttype()) {
                stepDone.put("resulttype", "10"); // 办理结果类型:证照
            } else if (ZwfwConstant.LHSP_RESULTTYPE_PW == result.getResulttype()) {
                stepDone.put("resulttype", "20"); // 批文
            } else {
                stepDone.put("resulttype", "30"); // 办理结果类型：其他
            }
        } else {
            stepDone.put("resulttype", "99"); // 办理结果类型:无
        }
        stepDone.put("occurtime", EpointDateUtil.convertDate2String(projectDto.getBanjieDate(), EpointDateUtil.DATE_TIME_FORMAT)); // 办结时间
        if (certInfo != null) {
            stepDone.put("certificatenam", certInfo.getCertname()); // 结果证照名称
            stepDone.put("certificateno", certInfo.getCertno()); // 结果证照编号
            stepDone.put("publisher", certInfo.getCertawarddept()); // 发证、盖章单位
            stepDone.put("doneResult", ZwfwConstant.CONSTANT_STR_ONE); // 办理结果：出证办结
            if (certInfo.getExpiredateto() != null) {
                stepDone.put("certificatelimit", EpointDateUtil.convertDate2String(certInfo.getExpiredateto(), EpointDateUtil.DATE_TIME_FORMAT)); // 结果证照有效期限
            }
        }
        stepDone.put("isfee", ZwfwConstant.CONSTANT_STR_ZERO);
        List<AuditProjectCharge> charges = jiaoFeiDto.getCharges();
        List<AuditProjectChargeDetail> chargeDetails = jiaoFeiDto.getChargeDetails();
        if (CollectionUtils.isNotEmpty(charges)) {
            AuditProjectCharge charge = charges.get(0);
            for (AuditProjectChargeDetail chargeDetail : chargeDetails) {
                String state = localProjectService.getIsJiaoFei(charge); // 是否缴费
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(state)) {
                    stepDone.put("isfee", ZwfwConstant.CONSTANT_STR_ONE);
                    stepDone.put("fee", totalJine);  // 收费金额
                    String chargeStandard = projectDto.getAuditTask().getCharge_standard();
                    stepDone.put("feestandard", chargeStandard);  // 收费标准
                    stepDone.put("feestandaccord", feeStandAccord);  // 收费依据
                    stepDone.put("paypersonname", jiaoFeiDto.getJiaofeiRenName());  // 缴费人姓名
                    stepDone.put("payperidcard", jiaoFeiDto.getJiaofeiRenCardId());  // 缴费人证件号
                    stepDone.put("payermobile", jiaoFeiDto.getJiaofeiRenMobile());  // 缴费人手机号
                    break;
                }
            }
        }
        stepDone.put("occurtime", EpointDateUtil.convertDate2String(projectDto.getBanjieDate(), EpointDateUtil.DATE_TIME_FORMAT)); // 办结时间
        stepDone.put("transactor", projectDto.getBanjieRenName()); // 环节办理人
        stepDone.put("scxfsign", ZwfwConstant.CONSTANT_STR_ZERO); // 是否下发
        stepDone.put("datasourcessysname", systemname); // 数据来源系统名称
        stepDone.put("systemlevel", ZwfwConstant.CONSTANT_STR_THREE); // 数据来源系统名称
        return stepDone;
    }

    /**
     * 特殊环节信息
     * @param project
     * @param projectDto
     * @return
     */
    public List<JSONObject> createSpecialNodes(AuditProject project, ProjectDto projectDto) {
        List<JSONObject> specialNodes = new ArrayList<JSONObject>();
        String nodeProcAccord = "法律";
        List<AuditProjectUnusual> projectNnusuals = localProjectService.getProjectUnusuals(project);
        IUserService iUserService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        if (CollectionUtils.isNotEmpty(projectNnusuals)) {
            int sequece = 1;
            for (AuditProjectUnusual projectNnusual : projectNnusuals) {
                BigDecimal sn = new BigDecimal(sequece++);
                JSONObject specialNode = new JSONObject();
                specialNode.put("orgbusno", project.getRowguid());
                specialNode.put("projid", project.getFlowsn());
                // 1、延长审批 2、除延长审批之外的情况
                specialNode.put("specialtype", ZwfwConstant.CONSTANT_STR_ONE);
                specialNode.put("nodename", projectNnusual.getStr("activityname"));
                specialNode.put("specialnodetype", ZwfwConstant.CONSTANT_STR_ZERO); // 环节类型 0 开始 1结束
                if ("11".equals(projectNnusual.getOperatetype())) {
                    specialNode.put("specialtype", ZwfwConstant.CONSTANT_STR_ONE);
                    specialNode.put("result", projectNnusual.getNote());
                }
                if (projectNnusual.getOperatedate() != null) {
                    specialNode.put("nodetime", EpointDateUtil.convertDate2String(projectNnusual.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                } else {
                    specialNode.put("nodetime", "");
                }
                specialNode.put("regionid", projectDto.getRegionId());
                specialNode.put("sn", sn);
                specialNode.put("procunitname", projectNnusual.getStr("ouname"));
                specialNode.put("procunitid", projectNnusual.getStr("oucode"));
                String operatorGuid = projectNnusual.getOperateuserguid();
                String orgcode = "";
                FrameUser frameUser = iUserService.getUserByUserField("userguid", operatorGuid);
                if (frameUser != null) {
                    FrameOuExtendInfo ouExtendInfo = iOuService.getFrameOuExtendInfo(frameUser.getOuGuid());
                    if (ouExtendInfo != null && StringUtil.isNotBlank(ouExtendInfo.getStr("orgcode"))) {
                        orgcode = ouExtendInfo.getStr("orgcode");
                    }
                }
                specialNode.put("procdeptcode", orgcode);
                specialNode.put("procername", projectNnusual.getStr("displayname"));
                specialNode.put("nodeprocadv", projectNnusual.getNote());
                specialNode.put("nodeprocaccord", nodeProcAccord);
                specialNode.put("scxfsign", ZwfwConstant.CONSTANT_STR_ZERO);
                specialNode.put("datasourcessysname", systemname);
                specialNode.put("systemlevel", ZwfwConstant.CONSTANT_STR_THREE);
                specialNode.put("serviceidentify", ZwfwConstant.CONSTANT_STR_ZERO);

                specialNodes.add(specialNode);
            }
        }
        return specialNodes;
    }

    /**
     * 调用办件库接口推送办件
     *
     * @param apiname 接口名称
     * @param params  未加密的入参
     * @return
     */
    public String pushProjectInfo(String apiname, JSONObject params) {
        // 办件库地址
        String url = ConfigUtil.getConfigValue("projectcollection", "projectcollectionurl");
        // 接口地址
        String apiurl = ConfigUtil.getConfigValue("projectcollection", apiname);

        // 获取分配的鉴权token信息
        String token = ConfigUtil.getConfigValue("projectcollection", "projectcollectiontoken");
        // 获取办件库分配的sm2公钥
        String publicKey = ConfigUtil.getConfigValue("projectcollection", "projectcollectionpublickey");

        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        JSONObject obj = new JSONObject();
        obj.put("data", SM2Util.encryptData(publicKey, params.toString()));
        logger.info("办件推送===apiname：" + apiname + ";加密入参：" + obj);
        logger.info("办件推送===apiname：" + apiname + ";入参：" + params);
        String result = HttpUtil.doPostJson(url + apiurl, obj.toString(), headers, null);
        logger.info("办件推送===apiname：" + apiname + ";结果：" + result);
        return result;
    }

    /**
     * 解析推送结果
     *
     * @param result
     * @return
     */
    private boolean checkPushResult(String result) {
        try {
            JSONObject resultOjbect = JSON.parseObject(result);
            return "200".equals(resultOjbect.getString("code"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
