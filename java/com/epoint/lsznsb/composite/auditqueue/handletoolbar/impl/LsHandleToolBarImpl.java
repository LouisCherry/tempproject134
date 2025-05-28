package com.epoint.lsznsb.composite.auditqueue.handletoolbar.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuebreachrecord.domain.AuditQueueBreachRecord;
import com.epoint.basic.auditqueue.auditqueuebreachrecord.inter.IAuditQueueBreachRecord;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlecommon.inter.IHandleCommon;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.composite.auditqueue.handlewindow.inter.IHandleWindow;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.lsznsb.auditqueue.handlecommon.inter.LsIHandleCommon;
import com.epoint.lsznsb.auditqueue.handlequeue.inter.LsIHandleQueue;
import com.epoint.lsznsb.composite.auditqueue.handletoolbar.inter.LsIHandleToolBar;

@Component
@Service
public class LsHandleToolBarImpl implements LsIHandleToolBar
{

    @Override
    public AuditCommonResult<String> initQueueLogin(String windowguid, String userguid, String displayname,
            String workstatus) {
        IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        IHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(IHandleCommon.class);
        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        IHandleWindow handlewindowservice = ContainerFactory.getContainInfo().getComponent(IHandleWindow.class);
        IAuditQueueWindowTasktype windowtasktypeservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueWindowTasktype.class);
        IAuditQueueTasktype tasktypeservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueTasktype.class);
        IAuditOrgaWindow orgawindowservice = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindow.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        String ledcontent = "";
        String AS_IS_USE_LED = "";
        try {
            // 判断是否存在该窗口redis，无则新增
            ZwfwRedisCacheUtil redis = null;
            try {
                redis = new ZwfwRedisCacheUtil(false);
                if (!redis.isExist("AuditQueueOrgaWindow_" + windowguid)) {
                    handlewindowservice.initAuditQueueOrgaWindowbyWindow(windowguid);
                }
            }
            catch (Exception e) {
                throw new RuntimeException("redis执行发生了异常", e);
            }
            finally {
                if (redis != null) {
                    redis.close();
                }
            }

            windowservice.updateWindowUser(windowguid, userguid, displayname);
            windowservice.updateWindowWorkStatus(windowguid, workstatus);

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowguid);
            List<AuditQueueWindowTasktype> tasktypelists = windowtasktypeservice.getAllWindowTasktype(sql.getMap())
                    .getResult();
            if (tasktypelists != null && !tasktypelists.isEmpty()) {
                AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(tasktypelists.get(0).getTasktypeguid())
                        .getResult();
                if (tasktype != null) {
                    if (StringUtil.isNotBlank(tasktype.getQueuevalue())) {
                        windowservice.updateWindowQueueValue(windowguid, tasktype.getQueuevalue());
                    }

                }

            }
            // led 推送（空闲）
            AuditOrgaWindow window = orgawindowservice.getWindowByWindowGuid(windowguid).getResult();
            AS_IS_USE_LED = handleConfigservice.getFrameConfig("AS_IS_USE_LED", window.getCenterguid()).getResult();
            if (QueueConstant.Common_yes_String.equals(AS_IS_USE_LED)) {
                ledcontent = handleConfigservice.getFrameConfig("AS_QUEUE_LED_FREE", window.getCenterguid())
                        .getResult();
                if (StringUtil.isBlank(ledcontent)) {
                    ledcontent = "欢迎光临！";
                }
                handlecommonservice.sendLEDMQ(ledcontent, window.getWindowno(), window.getCenterguid());

            }
            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> pauseQueue(String windowguid, String windowno, String centerguid) {

        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        IHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(IHandleCommon.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        String ledcontent = "";
        try {
            windowservice.updateWindowWorkStatus(windowguid, QueueConstant.Window_WorkStatus_Pause);
            String config = handleConfigservice.getFrameConfig("AS_IS_USE_LED", centerguid).getResult();
            if (StringUtil.isNotBlank(config) && QueueConstant.Common_yes_String.equals(config)) {
                ledcontent = handleConfigservice.getFrameConfig("AS_QUEUE_LED_PAUSE", centerguid).getResult();
                if (StringUtil.isBlank(ledcontent)) {
                    ledcontent = "暂离！";
                }
                handlecommonservice.sendLEDMQ(ledcontent, windowno, centerguid);

            }
            // 推送暂停信息
            handlemqservice.sendMQPausebyEvaluate(windowguid);
            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> zhuXiaoQueue(String windowguid, String windowno, String centerguid) {

        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        IHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(IHandleCommon.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        String ledcontent = "";
        try {

            windowservice.updateWindowWorkStatus(windowguid, QueueConstant.Window_WorkStatus_NotLogin);

            String config = handleConfigservice.getFrameConfig("AS_IS_USE_LED", centerguid).getResult();
            if (StringUtil.isNotBlank(config) && QueueConstant.Common_yes_String.equals(config)) {
                ledcontent = handleConfigservice.getFrameConfig("AS_QUEUE_LED_NOTLOGIN", centerguid).getResult();
                if (StringUtil.isBlank(ledcontent)) {
                    ledcontent = "未登录！";
                }
                handlecommonservice.sendLEDMQ(ledcontent, windowno, centerguid);
            }
            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> reCallQueue(String qno, String windowguid, String windowno, String centerguid) {
        LsIHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(LsIHandleCommon.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            handlecommonservice.sendQueueMessage(qno, windowguid, centerguid, windowno);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> nextQueue(String qno, String windowguid, String windowno, String centerguid,
            String UserGuid, Boolean UseCall) {
        IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
        LsIHandleQueue handlequeueservice = ContainerFactory.getContainInfo().getComponent(LsIHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            if (StringUtil.isNotBlank(qno) && !QueueConstant.Window_Bar_status_None.equals(qno)) {
                // 取号状态：0：未分配，1：正在处理，2：处理完成，3：过号
                queueservice.updateQNOStatus(qno, QueueConstant.Qno_Status_Processed, centerguid, new Date());
            }
            // 取出下一个号
            result.setResult(
                    handlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall).getResult());
            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String windowguid, String windowno, String centerguid, String UserGuid,
            Boolean UseCall) {
        LsIHandleQueue handlequeueservice = ContainerFactory.getContainInfo().getComponent(LsIHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 取出下一个号
            result.setResult(
                    handlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall).getResult());

            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> passQueue(String qno, String windowguid, String windowno, String centerguid,
            String UserGuid, Boolean UseCall) {
        IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
        LsIHandleQueue handlequeueservice = ContainerFactory.getContainInfo().getComponent(LsIHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        // 违约记录service
        IAuditQueueBreachRecord breachRecordService = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueBreachRecord.class);
        // 系统参数service
        IConfigService configService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
        // 排队申请人信息service
        IAuditQueueUserinfo userInfoService = ContainerFactory.getContainInfo().getComponent(IAuditQueueUserinfo.class);
        // 短信service
        IMessagesCenterService messageservice = ContainerFactory.getContainInfo()
                .getComponent(IMessagesCenterService.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            if (StringUtil.isNotBlank(qno) && !QueueConstant.Window_Bar_status_None.equals(qno)) {
                // 取号状态：0：未分配，1：正在处理，2：处理完成，3：过号
                queueservice.updateQNOStatus(qno, QueueConstant.Qno_Status_Pass, centerguid, new Date());

                // 判断是否使用了黑名单功能
                String useBlackList = configService.getFrameConfigValue("AS_IS_USE_BLACKLIST");
                if (StringUtil.isNotBlank(useBlackList) && QueueConstant.CONSTANT_STR_ONE.equals(useBlackList)) {
                    String field = " IDENTITYCARDNUM,phone ";
                    // 查询当前过号记录详情
                    AuditQueue currentPassQueue = queueservice.getQNODetailByQNO(field, qno, centerguid).getResult();

                    if (currentPassQueue != null) {
                        if (StringUtil.isNotBlank(currentPassQueue.getIdentitycardnum())) {
                            String cardNo = currentPassQueue.getIdentitycardnum();
                            // 查询过号违约记录
                            AuditQueueBreachRecord passQueue = breachRecordService.getPassQueueRecord(qno, centerguid)
                                    .getResult();
                            breachRecordService.insert(passQueue);
                            // 获取系统参数中的过号统计时间和最大次数
                            String passqnoCountTimeStr = configService.getFrameConfigValue("AS_PASSQNO_COUNT_TIME");
                            String passqnoMaxAllowCountStr = configService
                                    .getFrameConfigValue("AS_PASSQNO_MAX_ALLOW_COUNT");
                            String autoReleaseBlackTimeStr = configService
                                    .getFrameConfigValue("AS_AUTO_RELEASEBLACK_TIME");
                            int passqnoCountTime = StringUtil.isNotBlank(passqnoCountTimeStr)
                                    ? Integer.parseInt(passqnoCountTimeStr)
                                    : 30;
                            int passqnoMaxAllowCount = StringUtil.isNotBlank(passqnoMaxAllowCountStr)
                                    ? Integer.parseInt(passqnoMaxAllowCountStr)
                                    : 3;
                            int autoReleaseBlackTime = StringUtil.isNotBlank(autoReleaseBlackTimeStr)
                                    ? Integer.parseInt(autoReleaseBlackTimeStr)
                                    : 30;
                            // 查询当前身份证过号违约次数
                            Integer passCount = breachRecordService
                                    .getPassQNOCountByCard(cardNo,
                                            EpointDateUtil.addDay(new Date(), (-1) * passqnoCountTime), new Date())
                                    .getResult();
                            // 进行比较,如果次数大于最多过号次数，将加入黑名单
                            if (passCount == passqnoMaxAllowCount || passCount > passqnoMaxAllowCount) {
                                AuditQueueUserinfo userinfo = userInfoService.getUserinfo(cardNo).getResult();
                                Date blackListToDate = EpointDateUtil.addDay(EpointDateUtil.getBeginOfDate(new Date()),
                                        autoReleaseBlackTime);
                                userinfo.setBlacklisttodate(blackListToDate);
                                userinfo.setIs_foreverclose(QueueConstant.CONSTANT_STR_ZERO);
                                userInfoService.update(userinfo);
                                // 发送短信
                                String blackListToDateStr = EpointDateUtil.convertDate2String(blackListToDate,
                                        "yyyy年MM月dd日");
                                String messagecontent = "您在" + passqnoCountTime + "天内被窗口人员过号至少" + passCount
                                        + "次，已被拉入黑名单，自动解禁时间为" + blackListToDateStr + "，如有疑问，可到中心进行申诉。";
                                messageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent,
                                        new Date(), 0, new Date(), currentPassQueue.getPhone(),
                                        UUID.randomUUID().toString(), "", "", "", "", "", null, false, "");
                            }
                        }
                    }
                }

            }
            // 取出下一个号
            result.setResult(
                    handlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall).getResult());

            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> startQueue(String windowguid, String windowno, String centerguid, String UserGuid,
            Boolean UseCall) {

        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        LsIHandleQueue lsxhandlequeueservice = ContainerFactory.getContainInfo().getComponent(LsIHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String NextQNO = lsxhandlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall)
                    .getResult();

            // 取出下一个号
            result.setResult(NextQNO);

            windowservice.updateWindowWorkStatus(windowguid, QueueConstant.Window_WorkStatus_Free);

            // 推送在线信息
            handlemqservice.sendMQOnLinebyEvaluate(windowguid);

            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");

        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getWindowWaitNumAuto(String windowguid, String waitnum) {
        IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);

        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String newwaitnum = StringUtil
                    .getNotNullString(queueservice.getWindowWaitNum(windowguid, true).getResult());
            if (!waitnum.equals(newwaitnum)) {
                handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
            }
            result.setResult(newwaitnum);

        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> nextPassQueue(String qno, String passqno, String windowguid, String windowno,
            String centerguid, String userguid) {
        IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        LsIHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(LsIHandleCommon.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            if (StringUtil.isNotBlank(passqno) && StringUtil.isNotBlank(qno)) {
                if (!QueueConstant.Window_Bar_status_None.equals(qno)) {
                    queueservice.updateQNOStatus(qno, QueueConstant.Qno_Status_Processed, centerguid, new Date());
                }
                queueservice.updateQNOStatus(passqno, QueueConstant.Qno_Status_Processing, centerguid, new Date());
                result.setResult(passqno);
                handlecommonservice.sendQueueMessage(passqno, windowguid, centerguid, windowno);
                // 窗口屏推送
                handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
            }
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateWindowAfterLogout(String windowGuid, String centerGuid) {
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        IHandleConfig handleConfigservice = ContainerFactory.getContainInfo().getComponent(IHandleConfig.class);
        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        String isRefreshWindow = handleConfigservice.getFrameConfig("AS_REFRESH_LOGOUT", centerGuid).getResult();
        if (QueueConstant.Common_yes_String.equals(isRefreshWindow)) {
            // redis清空人员信息
            windowservice.updateWindowUser(windowGuid, "", "");
            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowGuid, "refresh()");
            // 评价器推送
            handlemqservice.sendMQLoginbyEvaluate(windowGuid);
        }
        return null;
    }

}
