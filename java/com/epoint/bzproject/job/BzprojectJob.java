package com.epoint.bzproject.job;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.bzproject.api.IBzprojectService;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.workingday.api.IWorkingdayService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 补正办件服务
 * 逻辑：办件发起补正后，五个工作日后，申请人没有重新上传材料，自动进行不予处理。
 */
public class BzprojectJob implements Job {

    transient Logger log = LogUtil.getLog(BzprojectJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("======补正办件自动不予处理服务开始");
        doBzproject();
        log.info("======补正办件自动不予处理服务开始");
    }

    private void doBzproject() {
        IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
        IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        IWorkingdayService workingdayService = ContainerFactory.getContainInfo().getComponent(IWorkingdayService.class);
        IAuditTaskExtension iAuditTaskExtension = ContainerFactory.getContainInfo().getComponent(IAuditTaskExtension.class);
        IAuditProjectUnusual projectUnusualService = ContainerFactory.getContainInfo().getComponent(IAuditProjectUnusual.class);
        IAuditProjectOperation auditProjectOperationService = ContainerFactory.getContainInfo().getComponent(IAuditProjectOperation.class);
        IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo().getComponent(IMessagesCenterService.class);
        IWFInstanceAPI9 wfinstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
        AuditWorkflowBizlogic auditWorkflowBizlogic = new AuditWorkflowBizlogic();
        IBzprojectService bzprojectService = ContainerFactory.getContainInfo().getComponent(IBzprojectService.class);
        IAuditOnlineProject OnlineProject = ContainerFactory.getContainInfo().getComponent(IAuditOnlineProject.class);
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.isBlank("biguid");
        sqlConditionUtil.eq("status", "28");
        sqlConditionUtil.isBlank("bubanfinishdate");
        List<AuditProject> list = iAuditProject.getAuditProjectListByCondition(sqlConditionUtil.getMap()).getResult();
        if (list != null && !list.isEmpty()) {
            for (AuditProject auditProject : list) {
                //先判断事项是否发送短信，如果是的则跑服务
                AuditTaskExtension taskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
                if (taskExtension != null) {
                    String is_sendbz = taskExtension.getStr("is_sendbz");
                    if ("1".equals(is_sendbz)) {
                        //取补正时间
                        SqlConditionUtil conditionUtil = new SqlConditionUtil();
                        conditionUtil.eq("projectguid", auditProject.getRowguid());
                        //补正状态
                        conditionUtil.eq("operatetype", "22");
                        AuditProjectOperation operation = iAuditProjectOperation.getAuditOperationByCondition(conditionUtil.getMap()).getResult();
                        if (operation != null) {
                            String userguid = operation.getOperateUserGuid();
                            String username = operation.getOperateusername();
                            Date begindate = operation.getOperatedate();
                            Date enddate = workingdayService.getWorkingDayWithOfficeSet(begindate, 6, true);
                            Date now = new Date();
                            //当前时间超过逾期时间，自动把办件置为不予受理
                            if (EpointDateUtil.compareDateOnDay(now, enddate) > 0) {
                                auditProject.setStatus(97);
                                //不予受理
                                auditProject.setBanjieresult(31);
                                String reason = "超期未补正";
                                auditProject.set("backreason", reason);// 退回原因
                                iAuditProject.updateProject(auditProject);
                                //网上办件状态同样置为97
                                AuditOnlineProject auditOnlineProject = bzprojectService.getOnlineProjectByguid(auditProject.getRowguid());
                                if (auditOnlineProject != null) {
                                    auditOnlineProject.setStatus("97");
                                    OnlineProject.updateProject(auditOnlineProject);
                                }

                                // 插入异常操作信息
                                projectUnusualService.insertProjectUnusual(userguid, username,
                                        auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_BYSL), "", reason);

                                String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
                                        + ZwfwConstant.OPERATE_BYSL + "'";
                                String Filds = " rowguid,remarks ";
                                // 添加不予受理备注
                                AuditProjectOperation auditProjectOperation = auditProjectOperationService
                                        .getOperationFileldByProjectGuid(strWhere, Filds, "").getResult();
                                if (auditProjectOperation != null) {
                                    auditProjectOperation.setRemarks(reason);
                                    auditProjectOperationService.updateAuditProjectOperation(auditProjectOperation);
                                }

                                // 不予受理短信
                                if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
                                    String targetUser = "";
                                    String targetDispName = "";
                                    // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
                                    if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                                        targetUser = auditProject.getAcceptuserguid();
                                        targetDispName = auditProject.getAcceptusername();
                                    }
                                    String content = "";
                                    if (StringUtil.isNotBlank(taskExtension.getNotify_nsl())) {
                                        content = taskExtension.getNotify_nsl();
                                        content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                                        content = content.replace("[#=ApplyDate#]",
                                                EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                                        content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                                        content = content.replace("[#=Reason#]", reason);
                                    }
                                    // 调整MessageType字段"短信"为办件的areacode
                                    messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                                            auditProject.getContactmobile(), targetUser, targetDispName, userguid, username, "", "", "", false,
                                            auditProject.getAreacode());
                                }

                                // 流程中止操作
                                if (StringUtil.isNotBlank(auditProject.getPviguid())) {
                                    ProcessVersionInstance pvi = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
                                    List<WorkflowWorkItem> listWorkItem = wfinstance.getWorkItemListByUserGuid(pvi, userguid);
                                    if (pvi != null && !listWorkItem.isEmpty()) {
                                        auditWorkflowBizlogic.finish(listWorkItem.get(0).getWorkItemGuid(), reason, pvi, "");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
