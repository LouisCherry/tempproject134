package com.epoint.auditproject.guiji.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectcharge.inter.IAuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.spring.util.SpringContextUtil;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.auditproject.guiji.constant.*;
import com.epoint.auditproject.guiji.dto.JiaoFeiDto;
import com.epoint.auditproject.guiji.dto.ProjectDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询办件信息的service，操作市级业务库的，比如办件申报信息，申报材料，材料附件，环节，特殊环节，办件结果，评价等信息
 * @author 刘雨雨
 * @time 2018年8月20日上午9:29:18
 */
public class LocalProjectServiceImpl
{

    private Logger logger = Logger.getLogger(LocalProjectServiceImpl.class);

    /**
     * 默认行政区划代码
     */
    private static final String DEFAULT_REGION_ID = "370800000000";

    private LocalProjectDaoImpl localProjectDao;

    private IAttachService attachService = SpringContextUtil.getBeanByType(IAttachService.class);

    private IAuditProjectCharge chargeService = SpringContextUtil.getBeanByType(IAuditProjectCharge.class);

    private ICodeItemsService iCodeItemsService = SpringContextUtil.getBeanByType(ICodeItemsService.class);

    public LocalProjectServiceImpl() {
        localProjectDao = new LocalProjectDaoImpl();
    }

    public LocalProjectServiceImpl(ICommonDao localCommonDao) {
        localProjectDao = new LocalProjectDaoImpl(localCommonDao);
    }
    
    public List<AuditProject> queryProjectsOfNeverSync() {
        return localProjectDao.queryProjects(ProjectSyncStatus.NEVER);
    }

    
    public List<AuditProject> queryProjectsOfSyncFailed() {
        return localProjectDao.queryProjects(ProjectSyncStatus.FAILED);
    }

    /**
     * 获取办件的文档附件信息
     * 含申报材料附件，批文，证照
     * @param project
     * @param projectDto
     * @return
     */
    
    public List<FrameAttachStorage> getAttachments(AuditProject project, ProjectDto projectDto) {
        List<FrameAttachStorage> projectMaterialAttachments = getProjectMaterialFiles(project, projectDto);
        List<FrameAttachStorage> piWenAttachments = getPiwen(project);
        List<FrameAttachStorage> zhengZhaoAttachments = getCerts(project);
        List<FrameAttachStorage> storages = new ArrayList<>();
        storages.addAll(projectMaterialAttachments);
        storages.addAll(piWenAttachments);
        storages.addAll(zhengZhaoAttachments);
        return storages;
    }

    /**
     * 获取出证后证照附件
     * @param project
     * @return
     */
    private List<FrameAttachStorage> getCerts(AuditProject project) {
        if (StringUtil.isNotBlank(project.getCertdocguid())) {
            return attachService.getAttachListByGuid(project.getCertdocguid());
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * 获取批文附件信息
     * @param project
     * @return
     */
    private List<FrameAttachStorage> getPiwen(AuditProject project) {
        if (StringUtil.isNotBlank(project.getOfficaldocguid())) {
            return attachService.getAttachListByGuid(project.getOfficaldocguid());
        }
        else {
            return Collections.emptyList();
        }
    }

    /**
     * 获取办件所有申报材料的关联的所有附件实体
     * @param project
     * @param projectDto
     * @return
     */
    private List<FrameAttachStorage> getProjectMaterialFiles(AuditProject project, ProjectDto projectDto) {
        List<FrameAttachStorage> storages = new ArrayList<>();
        List<AuditProjectMaterial> projectMaterials = projectDto.getProjectMaterials();
        for (AuditProjectMaterial projectMaterial : projectMaterials) {
            if (StringUtil.isNotBlank(projectMaterial.getCliengguid())) {
                List<FrameAttachStorage> storagesOfProjectMaterial = attachService
                        .getAttachListByGuid(projectMaterial.getCliengguid());
                storages.addAll(storagesOfProjectMaterial);
            }
        }
        return storages;
    }

    /**
     * 更新办件同步标识为已成功1
     * @param projectGuid  办件主键
     * @return
     */
    
    public int updateToSuccess(String projectGuid) {
        logger.info("开始更新办件标志位");
        try {
            int affectRows = localProjectDao.updateProjectSignStatus(projectGuid, ProjectSyncStatus.SUCCESS);
            logger.info("办件标志位已被更新为[成功]");
            return affectRows;
        }
        catch (Exception e) {
            logger.warn("更新办件标志位遇到错误，错误如下：");
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    

    
    public ProjectDto genProjectDto(AuditProject project) {
        IAuditTaskMaterial taskMaterialService = SpringContextUtil.getBeanByType(IAuditTaskMaterial.class);
        String searchPassword = getSearchPassword(project);
        AuditTask auditTask = getAuditTask(project.getTaskguid());
        String regionId = getRegionId(project, auditTask);
        List<AuditTaskMaterial> taskMaterials = taskMaterialService
                .selectTaskMaterialListByTaskGuid(project.getTaskguid(), false).getResult();
        String taskMaterialIdNames = getTaskMaterialIdAndNames(taskMaterials);
        List<AuditProjectMaterial> projectMaterials = localProjectDao
                .getProjectMaterialsByProjectGuid(project.getRowguid());
        //受理部门
        FrameOu acceptDept = getAcceptDept(project);
        //TODO
        String projectType = getProjectType(project);
        //TODO  默认承诺期限单位 1 工作日
        String promiseLimitUnit = "1";
        String legalLimitUnit = "g"; // 法定期限单位 g 工作日
        String signState = "0"; //数据交换状态标志位,默认0
        BigDecimal validityFlag = new BigDecimal(1); //数据有效标识：0无效；1有效。默认1，不可为空
        //TODO 
        String applyFrom = getApplyFrom(project);
        Date shouliTime = getShouliTime(project); // 受理时间
        String shouliRenName = getShouliRenName(project); // 受理人name
        String shouliRenGuid = getShouliRenGuid(project); //受理人guid
        BigDecimal doneResult = getDoneResult(project);
        
        BigDecimal stdVer = new BigDecimal(1);
        //办结部门
        FrameOu banjieDept = getBanjieDept(project);
        String banjieRenGuid = getBanjieRenGuid(project);
        String banjieRenName = getBanjieRenName(project);
        Date banjieDate = project.getBanjiedate();
        //缴费信息
        JiaoFeiDto jiaoFeiDto = genJiaoFeiDto(project);
        ProjectDto projectDto = new ProjectDto();
        projectDto.setAcceptDept(acceptDept);
        projectDto.setApplyFrom(applyFrom);
        projectDto.setAuditTask(auditTask);
        projectDto.setLegalLimitUnit(legalLimitUnit);
        projectDto.setTaskMaterialIdNames(taskMaterialIdNames);
        projectDto.setProjectType(projectType);
        projectDto.setPromiseLimitUnit(promiseLimitUnit);
        projectDto.setRegionId(regionId);
        projectDto.setSearchPassword(searchPassword);
        projectDto.setShouliTime(shouliTime);
        projectDto.setShouliRenName(shouliRenName);
        projectDto.setShouliRenGuid(shouliRenGuid);
        projectDto.setBanjieDept(banjieDept);
        projectDto.setBanjieDate(banjieDate);
        projectDto.setBanjieRenGuid(banjieRenGuid);
        projectDto.setBanjieRenName(banjieRenName);
        projectDto.setSignState(signState);
        projectDto.setValidityFlag(validityFlag);
        projectDto.setTaskMaterials(taskMaterials);
        projectDto.setProjectMaterials(projectMaterials);
        projectDto.setStdVer(stdVer);
        projectDto.setDoneResult(doneResult);
        projectDto.setJiaoFeiDto(jiaoFeiDto);
        return projectDto;
    }

    /**
     * 缴费信息
     * @param project
     * @return
     */
    private JiaoFeiDto genJiaoFeiDto(AuditProject project) {
        Double actualTotalJine = new Double(0);
        Double shouldlyTotalJine = new Double(0);
        String jiaofeiRenName = null;
        List<AuditProjectCharge> charges = chargeService.selectChargeByProjectGuid(project.getRowguid()).getResult();
        List<AuditProjectChargeDetail> chargeDetails = localProjectDao
                .getChargeDetailsByProjectGuid(project.getRowguid());
        if (CollectionUtils.isNotEmpty(charges)) {
            AuditProjectCharge charge = charges.get(0);
            jiaofeiRenName = charge.getJiaokuanperson();
            if (charge.getRealsum() != null) {
                actualTotalJine = charge.getRealsum();
            }
            if (charge.getTotalsum() != null) {
                shouldlyTotalJine = charge.getTotalsum();
            }
        }
        String isCharge = getIsCharge(project);
        JiaoFeiDto jiaofeiDto = new JiaoFeiDto();
        jiaofeiDto.setJiaofeiRenTel(null);
        jiaofeiDto.setJiaofeiRenCardId(null);
        jiaofeiDto.setJiaofeiRenMobile(null);
        jiaofeiDto.setJiaofeiRenName(jiaofeiRenName);
        jiaofeiDto.setActualTotalJine(actualTotalJine.doubleValue());
        jiaofeiDto.setShouldlyTotalJine(shouldlyTotalJine.doubleValue());
        jiaofeiDto.setIsCharge(isCharge);
        jiaofeiDto.setCharges(charges);
        jiaofeiDto.setChargeDetails(chargeDetails);
        return jiaofeiDto;
    }

    
    public String getIsJiaoFei(AuditProjectCharge charge) {
        if (charge.getIscharged() == null || charge.getIscharged() == 0) {
            return "2";
        }
        return "1";
    }

    
    public String getNodeResult(String activityName) {
        if ("受理".equals(activityName) || "接件".equals(activityName)) {
            return "4";
        }
        return "1";
    }

    /**
     * 获取审批过程信息
     * @param project
     * @param projectDto
     * @return
     */
    
    public List<WorkflowWorkItem> getNormalWorkItems(AuditProject project, ProjectDto projectDto) {
        List<WorkflowWorkItem> workItems = new ArrayList<>();
        String shouliOuName = projectDto.getAcceptDept().getOuname();
        String shouliOuCode = projectDto.getAcceptDept().getOucode();
        String banjieOuName = projectDto.getBanjieDept().getOuname();
        String banjieOuCode = projectDto.getBanjieDept().getOucode();
        Date banjieDate = projectDto.getBanjieDate();
        //1即办件 2承诺件
        //nodeType办理结果取值 0 - 不同意，1 - 同意 ，2-不受理，3-不予受理，4-受理，5-补齐补正，6-特别程序（挂起操作），7-退回
        //1即办件 2承诺件
        //nodeType （不可空）1-开始环节；2-中间 环节；3-结束环节；4-只有一条 审批数据（开始既是结束） 
        if (new Integer(1).equals(project.getTasktype())) {
            if (ProjectShStatus.ZCBJ.getValue().equals(project.getStatus())) {
                //即办件只有受理和办结两个环节
                // noderesult 4受理 1同意
                WorkflowWorkItem shouli = newWorkItem("1", "受理", projectDto.getShouliRenGuid(),
                        projectDto.getShouliRenName(), shouliOuName, shouliOuCode, "4", banjieDate, banjieDate);
                WorkflowWorkItem banjie = newWorkItem("3", "办结", project.getBanjieuserguid(),
                        project.getBanjieusername(), banjieOuName, banjieOuCode, "1", banjieDate, banjieDate);
                workItems.add(shouli);
                workItems.add(banjie);
            }
            else if (ProjectShStatus.BYSL.getValue().equals(project.getStatus())) {//不予受理
                //不予受理的状态下，banjieuserguid和banjieusername都为空，只有banjiedate,所以办结人只能取受理人
                WorkflowWorkItem banjie = newWorkItem("4", "不予受理", projectDto.getShouliRenGuid(),
                        projectDto.getShouliRenName(), banjieOuName, banjieOuCode, "3", banjieDate, banjieDate);
                workItems.add(banjie);
            }
        }
        else { //承诺件
            if (ProjectShStatus.ZCBJ.getValue().equals(project.getStatus())) {
                workItems = localProjectDao.getNormalWorkItems(project.getPviguid());
            }
            else if (ProjectShStatus.BYSL.getValue().equals(project.getStatus())) {//不予受理
                //不予受理的状态下，banjieuserguid和banjieusername都为空，只有banjiedate,所以办结人只能取受理人
                WorkflowWorkItem banjie = newWorkItem("4", "不予受理", projectDto.getShouliRenGuid(),
                        projectDto.getShouliRenName(), banjieOuName, banjieOuCode, "3", banjieDate, banjieDate);
                workItems.add(banjie);
            }
        }
        return workItems;
    }

    /**
     * 生成一个工作项对象，用作审批过程
     * @param activityName
     * @param operatorGuid
     * @param operatorName
     * @param ouName
     * @param ouCode
     * @param nodeResult
     * @return
     */
    private WorkflowWorkItem newWorkItem(String nodetype, String activityName, String operatorGuid, String operatorName,
            String ouName, String ouCode, String nodeResult, Date createDate, Date endDate) {
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

    private String getIsCharge(AuditProject project) {
        Integer isCharge = project.getIs_charge(); //是否收费
        if (isCharge == null) {
            return "0";
        }
        return isCharge.toString();
    }

    /**
     * 获取办结的状态，到底是哪一种办结
     * 0 :出证办结（正常产生证照、批文的办结），1 : 退回办结（退回或驳回申请的办结），2 :作废办结（指业务处理上无效的纪录），
     * 3 : 删除办结（指录入错误、操作错误等技术上的无效纪录），4 : 转报办结（指转报其他单位或上级单位的办结情况），
     * 5 :补正不来办结（指出现补正告知时，通知之后，申请人长期不来补正材料的办结），6 :办结（除以上所述情况外的办结）。
     * 
     * 
     *  0 – 出证办结（正常产生证照、批文的办结；准予许可的办结）
     *  1 – 不予许可
     *  5 – 退件（申请人长期不来补齐补正材料的办结；申请人主动放弃继续办理业务的办结）
     *  6 – 不予受理
     * @param project
     * @return
     */
    
    public BigDecimal getDoneResult(AuditProject project) {
        if (ProjectShStatus.ZCBJ.getValue().equals(project.getStatus())) {
            return new BigDecimal(0);
        }
        else if (ProjectShStatus.BYSL.getValue().equals(project.getStatus())) {
            return new BigDecimal(6);
        }
        else if (ProjectShStatus.YCZZ.getValue().equals(project.getStatus())) {
            return new BigDecimal(1);
        }
        else if (ProjectShStatus.CXSQ.getValue().equals(project.getStatus())) {
            return new BigDecimal(5);
        }
        else {
            return new BigDecimal(0);
        }
    }

    /**
     * 办理结果为不予受理的，或者作废的，获取它的作废或退回原因
     * @param doneResult
     * @return
     */
    
    public String getExitres(int doneResult) {

        if (doneResult == 1) {
            return "异常终止";
        }
        if (doneResult == 2 || doneResult == 3 || doneResult == 4 || doneResult == 5 || doneResult == 6) {
            return "不予受理或退回申请";
        }
        return null;
    }

    
    public String getCreateTimeForPreFile(AuditProjectMaterial projectMaterial, AuditProject project) {
        Date createDate = projectMaterial.getOperatedate();
        if (createDate == null) {
            createDate = project.getApplydate();
        }
        String createDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDate);
        return createDateStr;
    }

    /**
     * 获取材料提交类型，电子还是纸质
     * @param projectMaterial
     * @return
     */
    
    public String getMaterialSubmitType(AuditProjectMaterial projectMaterial) {
        String submitType = projectMaterial.getStr("submittype");
        if (MaterialType.ELECTRONIC.getValue().equals(submitType)) {
            submitType = "电子文件";
        }
        else if (MaterialType.PAPER.getValue().equals(submitType)) {
            submitType = "纸质文件";
        }
        else if (MaterialType.ELECTRONIC_OR_PAPER.getValue().equals(submitType)) {
            submitType = "电子或纸质";
        }
        else if (MaterialType.ELECTRONIC_AND_PAPER.getValue().equals(submitType)) {
            submitType = "同时提交电子和纸质";
        }
        else {
            submitType = "电子和纸质";
        }
        return submitType;
    }

    /**
     * 获取办结人名称，如果没有，取受理人，受理人也没有，取接件人，接件人还没有，抛异常
     * @param project
     * @return
     */
    public String getBanjieRenName(AuditProject project) {
        if (StringUtil.isNotBlank(project.getBanjieusername())) {
            return project.getBanjieusername();
        }
        logger.warn("该办件（编号：" + project.getFlowsn() + "）没有办结人姓名，将取受理人姓名作为办结人姓名");
        return getShouliRenName(project);
    }

    /**
     * 获取办结人标识，如果没有，取受理人，受理人也没有，取接件人，接件人还没有，抛异常
     * @param project
     * @return
     */
    public String getBanjieRenGuid(AuditProject project) {
        if (StringUtil.isNotBlank(project.getBanjieuserguid())) {
            return project.getBanjieuserguid();
        }
        logger.warn("该办件（编号：" + project.getFlowsn() + "）没有办结人标识，将取受理人标识作为办结人标识");
        return getShouliRenGuid(project);
    }

    /**
     * 获取受理环节办理人name
     * @param project
     * @return
     */
    public String getShouliRenName(AuditProject project) {
        if (StringUtil.isNotBlank(project.getAcceptusername())) {
            return project.getAcceptusername();
        }
        else if (StringUtil.isNotBlank(project.getReceiveusername())) {
            logger.warn("该办件（编号：" + project.getFlowsn() + "）没有受理人姓名，将取接件人姓名作为受理人姓名");
            return project.getReceiveusername();
        }
        else {
            throw new RuntimeException("找不到办件的受理或接件人员，无法上报办件信息！办件编号:" + project.getFlowsn());
        }
    }

    /**
     * 获取受理环节办理人guid
     * @param project
     * @return
     */
    public String getShouliRenGuid(AuditProject project) {
        if (StringUtil.isNotBlank(project.getAcceptuserguid())) {
            return project.getAcceptuserguid();
        }
        else if (StringUtil.isNotBlank(project.getReceiveuserguid())) {
            return project.getReceiveuserguid();
        }
        else {
            throw new RuntimeException("找不到受理人标识，无法上报办件信息，办件编号:" + project.getFlowsn());
        }
    }

    /**
     * 环节发生时间，也就是受理时间
     * @param project
     * @return
     */
    private Date getShouliTime(AuditProject project) {
        if (project.getAcceptuserdate() != null) {
            return project.getAcceptuserdate();
        }
        else if (project.getReceivedate() != null) {
            logger.warn("该办件（编号：" + project.getFlowsn() + "）没有受理时间，将取接件时间作为受理时间");
            return project.getReceivedate();
        }
        else {
            logger.warn("该办件（编号：" + project.getFlowsn() + "）没有受理和接件时间，将取操作时间operatedate作为受理时间");
            return project.getOperatedate();
        }
    }

    /**
     * TODO 获取申请来源，是窗口提交还是网上申请的
     * @param project
     * @return
     */
    private String getApplyFrom(AuditProject project) {
        /*if ("20".equals(project.getApplyway())) {
        	return "0"; // 窗口提交
        } else if ("10".equals(project.getApplyway()) || "11".equals(project.getApplyway())) {
        	return "1"; // 网上申报
        } else {
        	return "8"; // 其他
        }*/
        //领导应对上级检查，凑数，统统按网上申报处理
        return "1"; // 网上申报
    }

    private AuditTask getAuditTask(String taskGuid) {
        AuditTask auditTask = localProjectDao.getAuditTask(taskGuid);
        if (auditTask == null) {
            throw new RuntimeException("办件关联的事项已不存在，相关信息无法获取，办件信息上报失败");
        }
        if (auditTask.getPromise_day() == null || auditTask.getAnticipate_day() == null) {
            throw new RuntimeException("事项承诺期限或法定期限为空，无法完成办件上报，请设置。");
        }
        if (StringUtil.isBlank(auditTask.getFullid())) {
            throw new RuntimeException("事项的权力编码fullid为空，无法完成办件上报，请设置。");
        }
        return auditTask;
    }

    /**
     * 
     * @param project
     * @return
     */
    private String getProjectType(AuditProject project) {
        // 新点的上报件3对应前置库上报件4
        if (new Integer(3).equals(project.getTasktype())) {
            return "4";
        }
        // 若为null，统一按即办件1处理
        if (project.getTasktype() == null) {
            return "1";
        }
        return project.getTasktype().toString();
    }

    /**
     * 办结部门
     * @param project
     * @return
     */
    private FrameOu getBanjieDept(AuditProject project) {
        IOuService ouService = SpringContextUtil.getBeanByType(IOuService.class);
        FrameOu dept = ouService.getOuByUserGuid(project.getBanjieuserguid());
        if (dept == null) {
            dept = ouService.getOuByUserGuid(project.getAcceptuserguid());
            if (dept == null) {
                dept = ouService.getOuByUserGuid(project.getReceiveuserguid());
                if (dept == null) {
                    dept = ouService.getOuByOuGuid(project.getOuguid());
                }
            }
        }
        if (dept == null || StringUtil.isBlank(dept.getOuname()) || StringUtil.isBlank(dept.getOucode())) {
            throw new RuntimeException("无法找到办件的办结部门，上报办件失败，办件编号：" + project.getFlowsn());
        }
        return dept;
    }

    /**
     * 受理部门
     * @param project
     * @return
     */
    private FrameOu getAcceptDept(AuditProject project) {
        IOuService ouService = SpringContextUtil.getBeanByType(IOuService.class);
        FrameOu dept = ouService.getOuByUserGuid(project.getAcceptuserguid());
        if (dept == null) {
            dept = ouService.getOuByUserGuid(project.getReceiveuserguid());
            if (dept == null) {
                dept = ouService.getOuByOuGuid(project.getOuguid());
            }
        }
        if (dept == null || StringUtil.isBlank(dept.getOuname()) || StringUtil.isBlank(dept.getOucode())) {
            throw new RuntimeException("无法找到办件的受理部门，上报办件失败，办件编号：" + project.getFlowsn());
        }
        return dept;
    }

    String getTaskMaterialIdAndNames(List<AuditTaskMaterial> taskMaterialList) {
        StringBuffer materialsStr = new StringBuffer();
        String separator1 = ":";
        String separator2 = ",";
        if (CollectionUtils.isNotEmpty(taskMaterialList)) {
            for (AuditTaskMaterial taskMaterial : taskMaterialList) {
                materialsStr.append(taskMaterial.getRowguid()).append(separator1).append(taskMaterial.getMaterialname())
                        .append(separator2);
            }
        }
        if (materialsStr.toString().endsWith(separator2)) {
            return materialsStr.substring(0, materialsStr.length() - 1);
        }
        return materialsStr.toString();
    }

    
    public String getProjectMaterialIdAndNames(List<AuditProjectMaterial> projectMaterialList) {
        StringBuffer materialsStr = new StringBuffer();
        String separator1 = ":";
        String separator2 = ",";
        if (CollectionUtils.isNotEmpty(projectMaterialList)) {
            for (AuditProjectMaterial projectMaterial : projectMaterialList) {
                materialsStr.append(projectMaterial.getRowguid()).append(separator1)
                        .append(projectMaterial.getTaskmaterial()).append(separator2);
            }
        }
        if (materialsStr.toString().endsWith(separator2)) {
            return materialsStr.substring(0, materialsStr.length() - 1);
        }
        return materialsStr.toString();
    }

    private String getSearchPassword(AuditProject project) {
        String searchPassword = project.getStr("searchpwd");
        if (StringUtil.isBlank(searchPassword)) {
            searchPassword = new SecureRandom().nextInt(900000) + 100000 + "";
        }
        return searchPassword;
    }

    private String getRegionId(AuditProject project, AuditTask auditTask) {
        String regionId = project.getAreacode()+ "000000";
        if (StringUtil.isBlank(regionId)) {
            regionId = auditTask.getAreacode()+ "000000";
        }
        if (StringUtil.isBlank(regionId)) {
            regionId = DEFAULT_REGION_ID;
        }
        return regionId;
    }

    
    public int updateToFailed(AuditProject project) {
        try {
            //如果已经失败则更新为彻底失败，否则更新为失败
            logger.info("开始更新办件标志位...");
            if (ProjectSyncStatus.FAILED.getValue().equals(project.getStr("report_status"))) {
                int affectRows = localProjectDao.updateProjectSignStatus(project.getRowguid(),
                        ProjectSyncStatus.FAILED_COMPLETELY);
                logger.info("办件标志位已被更新为彻底失败3...");
                return affectRows;
            }
            else {
                int affectRows = localProjectDao.updateProjectSignStatus(project.getRowguid(),
                        ProjectSyncStatus.FAILED);
                logger.info("办件标志位已被更新为失败2...");
                return affectRows;
            }
        }
        catch (Exception e) {
            logger.error("更新办件标志位时遇到错误...");
            logger.error(e.getMessage(), e);
            return 0;
        }
    }

    /**
     * 审批类型 1普通办件 2绿色通道 3联合会审 4并联审批 9其他
     */
    
    public String getApproveType(AuditProject project) {
        //暂时返回1普通办件
        return "1";
    }

    /**
     * 获取办件状态
     * 01草稿 02收件 03预受理 04预受理退回 05受理 06补齐补正 07不予受理 08在办 09挂起 10办结 11转报办结 12作废办结 13退件 
     */
    
    public String getProjectState(AuditProject project) {
        Integer status = project.getStatus();
        if (ProjectShStatus.BYSL.getValue().equals(status)) {
            return QzkProjectStatus.REFUSE_ACCEPT;
        }
        if (ProjectShStatus.ZCBJ.getValue().equals(status)) {
            return QzkProjectStatus.NORMAL_DONE;
        }
        if (ProjectShStatus.YCZZ.getValue().equals(status)) {
            return QzkProjectStatus.OBSOLETE_DONE;
        }
        if (ProjectShStatus.CXSQ.getValue().equals(status)) {
            return QzkProjectStatus.CANCELL_DONE;
        }
        if (ProjectShStatus.WW_INITIAL.getValue().equals(status)) {
            return QzkProjectStatus.DRAFT;
        }
        if (ProjectShStatus.WW_YSTH.getValue().equals(status)) {
            return QzkProjectStatus.PREPARE_ACCEPT_SEND_BACK;
        }
        if (ProjectShStatus.WW_YSTG.getValue().equals(status)) {
            return QzkProjectStatus.PREPARE_ACCEPTED;
        }
        if (ProjectShStatus.YJJ.getValue().equals(status)) {
            return QzkProjectStatus.RECEIVE;
        }
        if (ProjectShStatus.YSL.getValue().equals(status)) {
            return QzkProjectStatus.ACCEPTED;
        }
        if (status > ProjectShStatus.YSL.getValue() && status < ProjectShStatus.ZCBJ.getValue()) {
            return QzkProjectStatus.WORKED;
        }
        return QzkProjectStatus.DRAFT;
    }

    /**
     * 基础代码集文档里找不到证件类型代码项，暂时参照旧的推送记录的赋值
     */
    
    public String getApplyCardType(AuditProject project) {
        return "1";
    }

    /**
     * 更新办件同步标识为彻底失败3
     * @param projectGuid  办件主键
     * @return
     */
    
    public int updateToFailedCompletely(String projectGuid) {
        return localProjectDao.updateProjectSignStatus(projectGuid, ProjectSyncStatus.FAILED_COMPLETELY);
    }

    
    public List<AuditProjectUnusual> getProjectUnusuals(AuditProject project) {
        List<AuditProjectUnusual> newProjectUnusuals = new ArrayList<>();
        List<AuditProjectUnusual> projectUnusuals = localProjectDao.getProjectUnusuals(project.getRowguid());
        //即办件不予受理操作发生在受理环节；撤销申请、异常终止发生了办结环节
        //承诺件是不是也是这样，未知
        final String activitynameKey = "activityname";
        for (AuditProjectUnusual projectUnusual : projectUnusuals) {
            if (StringUtil.isBlank(projectUnusual.getStr(activitynameKey))) {
                Integer operateType = projectUnusual.getOperatetype();
                if(operateType!=null){
                    String nodename = iCodeItemsService.getItemTextByCodeName("办件特殊操作",operateType+"");
                    if(nodename!=null){
                        projectUnusual.set(activitynameKey, nodename);
                    }
                }
                if (ProjectShStatus.BYSL.getValue().equals(operateType)
                        || Integer.valueOf(ZwfwConstant.SHENPIOPERATE_TYPE_BYSL).equals(operateType)) {
                    projectUnusual.set(activitynameKey, "受理");
                }
                else if (ProjectShStatus.CXSQ.getValue().equals(operateType)
                        || ProjectShStatus.YCZZ.getValue().equals(operateType)) {
                    projectUnusual.set(activitynameKey, "办结");
                }
            }
            if(StringUtils.isNotBlank(projectUnusual.get(activitynameKey))){
                projectUnusual.set(activitynameKey, "其他");
            }
            if (StringUtil.isNotBlank(projectUnusual.getStr("oucode"))) {
                newProjectUnusuals.add(projectUnusual);
            }
            
        }
        return newProjectUnusuals;
    }

    
    public CertInfo getCertInfo(String certrowguid) {
        return localProjectDao.getCertInfo(certrowguid);
    }

    
    public int updateToBaseInfoSyncSuccess(AuditProject project) {
        return CommonDao.getInstance().execute("update audit_project set baseinfosync = '1' where rowguid = ?", project.getRowguid());
    }
}
