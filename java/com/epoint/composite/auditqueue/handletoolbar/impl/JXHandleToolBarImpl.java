package com.epoint.composite.auditqueue.handletoolbar.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlecommon.inter.IHandleCommon;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.composite.auditqueue.handletoolbar.inter.IJXHandleToolBar;
import com.epoint.composite.auditqueue.handlewindow.inter.IHandleWindow;
import com.epoint.composite.auditqueue.jxhandlequeue.inter.IJXHandleQueue;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Service
public class JXHandleToolBarImpl implements IJXHandleToolBar {

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
        IAuditOrgaWindowYjs orgawindowservice = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindowYjs.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        String ledcontent = "";
        String AS_IS_USE_LED = "";
        ZwfwRedisCacheUtil redis = null;
        try {
            // 判断是否存在该窗口redis，无则新增
            redis = new ZwfwRedisCacheUtil();

            if (!redis.isExist("AuditQueueOrgaWindow_" + windowguid)) {
                handlewindowservice.initAuditQueueOrgaWindowbyWindow(windowguid);
            }
            windowservice.updateWindowUser(windowguid, userguid, displayname);
            windowservice.updateWindowWorkStatus(windowguid, workstatus);

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("windowguid", windowguid);
            List<AuditQueueWindowTasktype> tasktypelists = windowtasktypeservice.getAllWindowTasktype(sql.getMap())
                    .getResult();
            if (tasktypelists != null && tasktypelists.size() > 0) {
                AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(tasktypelists.get(0).getTasktypeguid())
                        .getResult();
                if (StringUtil.isNotBlank(tasktype)) {
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
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        } finally {
            if (redis != null) {
                redis.close();
            }
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> reCallQueue(String qno, String windowguid, String windowno, String centerguid) {

        IHandleCommon handlecommonservice = ContainerFactory.getContainInfo().getComponent(IHandleCommon.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            handlecommonservice.sendQueueMessage(qno, windowguid, centerguid, windowno);

        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> nextQueue(String qno, String windowguid, String windowno, String centerguid,
                                               String UserGuid, Boolean UseCall) {
        IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
        IJXHandleQueue jxhandlequeueservice = ContainerFactory.getContainInfo().getComponent(IJXHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            if (StringUtil.isNotBlank(qno) && !QueueConstant.Window_Bar_status_None.equals(qno)) {

                // 取号状态：0：未分配，1：正在处理，2：处理完成，3：过号
                queueservice.updateQNOStatus(qno, QueueConstant.Qno_Status_Processed, centerguid, new Date());

            }
            // 取出下一个号
            result.setResult(
                    jxhandlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall).getResult());
            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String windowguid, String windowno, String centerguid, String UserGuid,
                                                Boolean UseCall) {
        IJXHandleQueue jxhandlequeueservice = ContainerFactory.getContainInfo().getComponent(IJXHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            // 取出下一个号
            result.setResult(
                    jxhandlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall).getResult());

            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> passQueue(String qno, String windowguid, String windowno, String centerguid,
                                               String UserGuid, Boolean UseCall) {
        IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
        IHandleQueue handlequeueservice = ContainerFactory.getContainInfo().getComponent(IHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            if (StringUtil.isNotBlank(qno) && !QueueConstant.Window_Bar_status_None.equals(qno)) {

                // 取号状态：0：未分配，1：正在处理，2：处理完成，3：过号
                queueservice.updateQNOStatus(qno, QueueConstant.Qno_Status_Pass, centerguid, new Date());

            }
            // 取出下一个号
            result.setResult(
                    handlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall).getResult());

            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> startQueue(String windowguid, String windowno, String centerguid, String UserGuid,
                                                Boolean UseCall) {

        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        IHandleQueue handlequeueservice = ContainerFactory.getContainInfo().getComponent(IHandleQueue.class);
        IHandleRabbitMQ handlemqservice = ContainerFactory.getContainInfo().getComponent(IHandleRabbitMQ.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String NextQNO = handlequeueservice.getNextQNO(windowguid, windowno, centerguid, UserGuid, UseCall)
                    .getResult();

            // 取出下一个号
            result.setResult(NextQNO);

            windowservice.updateWindowWorkStatus(windowguid, QueueConstant.Window_WorkStatus_Free);

            // 推送在线信息
            handlemqservice.sendMQOnLinebyEvaluate(windowguid);

            // 窗口屏推送
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");

        } catch (Exception e) {
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

        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

}
