package com.epoint.composite.auditsp.handleproject.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.apache.commons.lang3.StringUtils;

public class JnHandleProjectService
{

    /**
     * 
     * 不同操作按钮更新待办标题
     * 
     * @param title
     *            标题
     */
    public void updateProjectTitle(String title, String processVersionInstanceGuid, String messageItemGuid,
            String operateUserGuid) {
        IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        MessagesCenter messagescenter = messagesCenterService.getDetail(messageItemGuid, operateUserGuid);
        if (messagescenter != null) {
            messagesCenterService.updateMessageTitle(messagescenter.getMessageItemGuid(), title, operateUserGuid);

        }
    }

    /**
     * 
     * 生成办件流水号
     * 
     */
    public void checkFlowSN(AuditProject auditProject) {
        IHandleFlowSn flowSn = ContainerFactory.getContainInfo().getComponent(IHandleFlowSn.class);
        IHandleConfig handleConfig = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        IAuditTask auditTaskService = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        if (StringUtil.isBlank(auditProject.getFlowsn())) {
            String numberFlag = handleConfig.getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE, auditProject.getCenterguid())
                    .getResult();
            if (StringUtil.isBlank(numberFlag)) {
                numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
            }
            /*String flowsn = flowSn.getFlowsn("办件编号", numberFlag).getResult();

            String useJSInternalNO = handleConfig.getFrameConfig("AS_USEJSINTERNALNO", auditProject.getCenterguid())
                    .getResult();
            // 如果使用江苏流水号标准，那么需要将流水号转换一下
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(useJSInternalNO)) {
                AuditTask task = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                flowsn = flowsn.replace(numberFlag, "");
                flowsn = flowsn.substring(0, 8) + flowsn.substring(flowsn.length() - 4, flowsn.length());
                flowsn = (StringUtil.isBlank(task.getItem_id()) ? "" : task.getItem_id()) + flowsn;
            }*/
            AuditTask task = auditTaskService.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
            String flowsn= FlowsnUtil.createReceiveNum(task.getStr("unid"), task.getRowguid());

            auditProject.setFlowsn(flowsn);// 设置办件流水号
            if (auditProject.getStatus() < ZwfwConstant.BANJIAN_STATUS_DJJ) {
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_DJJ);// 办件状态：待接件
                if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                        || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
                    Map<String, String> updateFieldMap = new HashMap<>(16);
                    updateFieldMap.put("status=", auditProject.getStatus().toString());
                    Map<String, String> conditionMap = new HashMap<>();
                    conditionMap.put("sourceguid=", auditProject.getRowguid());
                }
            }
            IAuditProject auditProjectService = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            auditProjectService.updateProject(auditProject);
        }
    }

    /**
     * 发送延期审核待办
     * 
     * @param rowguid
     *            特殊操作标示
     * @param auditProject
     *            办件实体
     * @return 显示的提示信息
     * @throws UnsupportedEncodingException
     */
    public String sendMGSToYQSH(String rowguid, AuditProject auditProject, String operateUserGuid,
            String oldmessageItemGuid) throws UnsupportedEncodingException {
        String retstr = "";
        IRoleService roleService = ContainerFactory.getContainInfo().getComponent(IRoleService.class);
        IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
        IMessagesCenterService messageCenterService = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        IAuditOrgaArea auditOrgaAreaService = ContainerFactory.getContainInfo().getComponent(IAuditOrgaArea.class);
        // 1、获取角色标识
        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "办件特殊操作审核");
        if (frameRole != null) {
            String roleguid = frameRole.getRoleGuid();
            String ouGuid = auditOrgaAreaService.getAreaByAreacode(auditProject.getAreacode()).getResult().getOuguid();
            // 2、获取该角色的对应的人员
            List<FrameUser> listUser = userService.listUserByOuGuid(ouGuid, roleguid, "", "", false, true, false, 3);
            if (listUser != null && listUser.size() > 0) {
                // 3、发送待办给审核人员
                for (FrameUser frameUser : listUser) {
                    // String targetusername =
                    // userService.getUserNameByUserGuid(targetuserguid);
                    // 待办名称
                    String title = "办件：" + auditProject.getProjectname() + "延期申请审核！";
                    // 处理页面
                    String handleurl = "epointzwfw/auditbusiness/auditproject/auditprojectdelaysh?projectguid="
                            + auditProject.getRowguid() + "&ProcessVersionInstanceGuid=" + auditProject.getPviguid()
                            + "&ztguid=" + rowguid + "&areaCode="
                            + URLEncoder.encode(auditProject.getAreacode(), "utf-8") + "&sqrUserGuid=" + operateUserGuid
                            + "&oldmessageItemGuid=" + oldmessageItemGuid;
                    String messageItemGuid = UUID.randomUUID().toString();
                    if (StringUtil.isNotBlank(handleurl) && (handleurl.indexOf("?") > 0)) {
                        handleurl += "&clientIdentifier=" + auditProject.getRowguid();
                    }
                    else {
                        handleurl += "?clientIdentifier=" + auditProject.getRowguid();
                    }
                    messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                            IMessagesCenterService.MESSAGETYPE_WAIT, frameUser.getUserGuid(),
                            frameUser.getDisplayName(), operateUserGuid,
                            userService.getUserNameByUserGuid(operateUserGuid), "延期申请审核", handleurl,
                            frameUser.getOuGuid(), "", ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(),
                            auditProject.getRowguid().substring(0, 1), new Date(), auditProject.getPviguid(),
                            frameUser.getUserGuid(), "", "");
                    // messageSendUtil.sendShortMessage(auditProject.getContactmobile(),
                    // null, title,frameUser);
                }
                retstr = "ok";
            }
            else {
                retstr = "请先确认系统是否存在‘办件特殊操作审核’角色人员！";
            }
        }
        else {
            retstr = "请先确认系统是否存在‘办件特殊操作审核’角色！";
        }
        return retstr;
    }

    /**
     * 办件操作日志保存
     * 
     * @param auditProject
     * @param opeateType
     * @param operateUserGuid
     * @param operateUserName
     * @param workItemGuid
     * @param reason
     */
    public void HandProjectLog(AuditProject auditProject, String opeateType, String operateUserGuid,
            String operateUserName, String workItemGuid, String reason) {
        AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
        // 如果存在启动工作流，则需要把工作流流转意见存入日志表
        if (StringUtil.isNotBlank(workItemGuid)) {
            IWFInstanceAPI9 wfInstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
            ProcessVersionInstance pVersionInstance = wfInstance.getProcessVersionInstance(auditProject.getPviguid());
            WorkflowWorkItem workflowWorkItem = wfInstance.getWorkItem(pVersionInstance, workItemGuid);
            if (StringUtil.isNotBlank(workflowWorkItem.getOpinion())) {
                auditProjectOperation.setRemarks(workflowWorkItem.getOpinion());
            }
        }
        // 有些方法没有获取到operateuser,可以手动获取一下
        if (operateUserName.equals("") && !operateUserGuid.equals("")) {
            IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
            operateUserName = userService.getUserNameByUserGuid(operateUserGuid);
        }
        // 目前采用插入数据库操作
        auditProjectOperation.setRowguid(UUID.randomUUID().toString());
        auditProjectOperation.setAreaCode(auditProject.getAreacode());
        auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
        auditProjectOperation.setApplyerName(auditProject.getApplyername());
        auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
        auditProjectOperation.setPVIGuid(auditProject.getPviguid());
        auditProjectOperation.setOperatedate(new Date());
        auditProjectOperation.setOperateusername(operateUserName);
        auditProjectOperation.setOperateUserGuid(operateUserGuid);
        auditProjectOperation.setOperateType(opeateType);
        auditProjectOperation.setProjectGuid(auditProject.getRowguid());
        String bzRemark = auditProject.getStr("bzRemark");
        if (StringUtil.isNotBlank(bzRemark)) {
            auditProjectOperation.setRemarks(bzRemark);
        }
        if (StringUtil.isNotBlank(reason)) {
            auditProjectOperation.setRemarks(reason);
        }
        IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectOperation.class);
        iAuditProjectOperation.addProjectOperation(auditProjectOperation);

    }

    /**
     * [一句话功能简述] [功能详细描述]
     * 
     * @param auditProject
     * @param opeateType
     * @param operateUserGuid
     * @param operateUserName
     * @param workItemGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void HandProjectLog(AuditProject auditProject, String opeateType, String operateUserGuid,
            String operateUserName, String workItemGuid) {
        HandProjectLog(auditProject, opeateType, operateUserGuid, operateUserName, workItemGuid, "");
    }

    /**
     * [一句话功能简述] [功能详细描述]
     * 
     * @param auditProject
     * @param opeateType
     * @param operateUserGuid
     * @param operateUserName
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void HandProjectLog(AuditProject auditProject, String opeateType, String operateUserGuid,
            String operateUserName) {
        HandProjectLog(auditProject, opeateType, operateUserGuid, operateUserName, "", "");
    }

    /**
     * 
     * 处理办件
     * 
     */
    @SuppressWarnings("unused")
    public String handleRevokeOrSuspension(AuditProject auditProject, String operateUserName, String operateUserGuid,
            String operateType, String workItemGuid, String reason) {

        IAuditProject project = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
        IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
        IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        // IWFEngineAPI9 wfEngineAPI9 =
        // ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
        IAuditProjectUnusual projectUnusual = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectUnusual.class);
        IAuditProjectSparetime auditProjectSparetime = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        // 1、插入异常操作信息
        String unusualGuid = projectUnusual.insertProjectUnusual(operateUserGuid, operateUserName, auditProject,
                Integer.parseInt(operateType), workItemGuid, reason).getResult();
        // 2、 插入办件审批操作信息
        // operationLogService.insertProjectOperation(userSession,
        // auditProject, Integer.parseInt(operateType));

        // 3、插入每日一填的记录
        // new AuditProjectNumberService().insertProjectNumber(auditTask,
        // auditProject, false);
        // 4、更新办件状态
        auditProject.setBanjieresult(Integer.parseInt(operateType));
        auditProject.setBanjiedate(new Date());
        auditProject.setBanwandate(new Date());
        auditProject.setStatus(Integer.parseInt(operateType));

        if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditProject.getApplyway().toString())
                || ZwfwConstant.APPLY_WAY_NETZJSB.equals(auditProject.getApplyway().toString())) {
            IAuditOnlineProject auditOnlineProjectService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOnlineProject.class);
            String applyerguid = null;
            if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo = ContainerFactory.getContainInfo()
                        .getComponent(IAuditRsCompanyBaseinfo.class);
                AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                        .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                applyerguid = auditrscompanybaseinfo.getCompanyid();
            }
            Map<String, String> updateFieldMap = new HashMap<>();
            Map<String, String> updateDateFieldMap = new HashMap<>();// 更新日期类型的字段
            updateFieldMap.put("status=", auditProject.getStatus().toString());
            Map<String, String> conditionMap = new HashMap<>(16);
            // 内网在走办结过程中，applyeruserguid 变化，导致查不到
            // conditionMap.put("applyerguid=",
            // auditProject.getApplyeruserguid());
            conditionMap.put("sourceguid=", auditProject.getRowguid());
            /*
             * if(applyerguid != null){
             * conditionMap.put("applyerguid=", applyerguid);
             * }
             * else{
             * conditionMap.put("applyerguid=",
             * auditProject.getApplyeruserguid());
             * }
             */
            auditOnlineProjectService.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionMap);
        }

        int promiseday = auditProject.getPromise_day();
        int spendtime = 0;// 已用时间
        int sparetime = 0;// 剩余时间

        AuditProjectSparetime auditprojectsparetime = auditProjectSparetime
                .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();

        // AuditProjectSparetime auditprojectsparetime =
        // zwfwCommonService.getBeanByOneField(AuditProjectSparetime.class,"projectguid",projectguid);
        // 办件计时信息存在
        if (auditprojectsparetime != null) {
            sparetime = auditprojectsparetime.getSpareminutes();
            if (promiseday > 0) {
                if (promiseday * 24 * 60 > sparetime) {
                    spendtime = promiseday * 24 * 60 - sparetime;
                }
                else {
                    spendtime = auditprojectsparetime.getSpendminutes();
                }
            }
            else {
                spendtime = -1 * sparetime;
            }
            auditProject.setSparetime(sparetime);
            auditProject.setSpendtime(spendtime);
        }
        auditProject.setGuidangdate(new Date());
        auditProject.setGuidanguserguid(operateUserGuid);
        auditProject.setGuidangusername(operateUserName);
        auditProject.setIs_guidang(1);


        //查看有无centerguid
        if(StringUtils.isBlank(auditProject.getCenterguid())){
            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
            if(auditOrgaServiceCenter!=null){
                auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
            }
        }
        //更新承诺办结时间
        AuditTask  auditTask = iaudittask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
        if(auditTask!=null) {
            List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
            Date acceptdat = auditProject.getAcceptuserdate();
            Date shouldEndDate;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOrgaWorkingDay.class);
                shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                        auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
            } else {
                shouldEndDate = null;
            }
            if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                Duration totalDuration = Duration.ZERO;  // 用于累加时间差（以秒为单位）
                LocalDateTime currentTime = null;
                for(AuditProjectUnusual auditProjectUnusual:auditProjectUnusuals) {
                    // 将Date转换为Instant
                    Instant instant = auditProjectUnusual.getOperatedate().toInstant();
                    if(10==auditProjectUnusual.getOperatetype()){
                        // 通过Instant和系统默认时区获取LocalDateTime
                        currentTime= LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    }
                    if(currentTime!=null && 11==auditProjectUnusual.getOperatetype()){
                        LocalDateTime nextTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        Duration danci = Duration.between(currentTime, nextTime);
                        totalDuration = totalDuration.plus(danci);
                        currentTime = null;
                    }
                }
                // 将累加的时间差加到初始的Date类型的shouldEndDate上
                Instant instant = shouldEndDate.toInstant();
                Instant newInstant = instant.plus(totalDuration);
                shouldEndDate = Date.from(newInstant);
            }
            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       auditProject.setPromiseenddate(shouldEndDate);
}
        }
        project.updateProject(auditProject);
        // 5、 删除计时信息
        if (auditprojectsparetime != null) {
            auditProjectSparetime.deleteSpareTimeByRowGuid(auditprojectsparetime.getRowguid());
            // 如果是并联审批办件，判断并联审批是否需要计时
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                handleBlspSparetime(auditProject.getSubappguid());
            }
        }
        // 6、流程终止
        String opinion = "";
        if (ZwfwConstant.SHENPIOPERATE_TYPE_CX.equals(operateType)) {
            opinion = "撤销申请";
        }
        else if (ZwfwConstant.SHENPIOPERATE_TYPE_ZZ.equals(operateType)) {
            opinion = "异常终止";
        }
        return unusualGuid;
    };

    public void handleBlspSparetime(String subappguid) {
        IAuditSpITask iauditspitask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IAuditProjectSparetime iauditprojectsparetime = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectSparetime.class);
        AuditProjectSparetime auditProjectSparetime = iauditprojectsparetime.getSparetimeByProjectGuid(subappguid)
                .getResult();
        // 如果没有查到计时数据，停止
        if (auditProjectSparetime == null) {
            return;
        }
        List<String> projectguidlist = iauditspitask.getProjectguidsBySubappGuid(subappguid).getResult();
        Integer count = iauditprojectsparetime.getSpareTimeCountByProjectguids(projectguidlist).getResult();

        // 存在办件计时
        if (count > 0) {
            if (!ZwfwConstant.CONSTANT_STR_ZERO.equals(auditProjectSparetime.getPause())) {
                auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ZERO);
                iauditprojectsparetime.updateSpareTime(auditProjectSparetime);
            }
        }
        else {
            if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditProjectSparetime.getPause())) {
                auditProjectSparetime.setPause(ZwfwConstant.CONSTANT_STR_ONE);
                iauditprojectsparetime.updateSpareTime(auditProjectSparetime);
            }
        }
    }

}
