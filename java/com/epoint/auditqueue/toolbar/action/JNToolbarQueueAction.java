package com.epoint.auditqueue.toolbar.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.composite.auditqueue.handletoolbar.inter.IHandleToolBar;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigServiceInternal;

@RestController("jntoolbarqueueaction")
@Scope("request")
public class JNToolbarQueueAction extends BaseController
{

    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditQueue auditqueueservice;
    @Autowired
    private IHandleRabbitMQ handlemqservice;
    @Autowired
    private IHandleToolBar handletoolbarservice;
    @Autowired
    private IAuditOnlineEvaluat evaluateservice;
    @Autowired
    private IHandleConfig configservice;


    private UserSession userSession = UserSession.getInstance();
    private ZwfwUserSession zwfwuserSession = ZwfwUserSession.getInstance();

    private String hidStatus;
    private String oldhidStatus;
    private Boolean issendmsg = true;
    @Override
    public void pageLoad() {
      String call =   configservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", zwfwuserSession.getCenterGuid(),"1").getResult();
      if("0".equals(call)){
          issendmsg = false;
      }else{
          issendmsg = true;
      }
         
        if (!isPostback()) {

            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))
                    && StringUtil.isNotBlank(zwfwuserSession.getWindowGuid())) {
                // 初始化窗口信息,更新人员已经在线，状态为暂停，不直接分配号码
                handletoolbarservice.initQueueLogin(zwfwuserSession.getWindowGuid(), userSession.getUserGuid(),
                        userSession.getDisplayName(), QueueConstant.Window_WorkStatus_Pause);

                handlemqservice.sendMQLoginbyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
                // 在线
                handlemqservice.sendMQOnLinebyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());

            }

            //判断是否需要启动排队叫号
            if (QueueConstant.Common_yes_String.equals(zwfwuserSession.getIsUseQueue())) {
                addCallbackParam("ISShow", QueueConstant.Common_yes_String);
                addCallbackParam("windowno", zwfwuserSession.getWindowNo());

                // 窗口办理状态：0：未登录，1：空闲，2：正在处理，3：等待处理，4：暂离
                setHidStatus(QueueConstant.Window_WorkStatus_Pause);
                // 获取等待人数
                GetWaitNum();
            }
            else {
                addCallbackParam("ISShow", QueueConstant.Common_no_String);
            }

        }
    }

    public void delayclick() {

        if (!QueueConstant.Window_WorkStatus_Pause.equals(getHidStatus())) {
            // 更新窗口当前状态为暂停
            handletoolbarservice.pauseQueue(zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                    zwfwuserSession.getCenterGuid());
            addCallbackParam("workstatus", QueueConstant.Window_WorkStatus_Pause);
        }
        else {
            // 更新窗口当前状态为开始
            addCallbackParam("QNO",
                    handletoolbarservice.startQueue(zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                            zwfwuserSession.getCenterGuid(), userSession.getUserGuid(), issendmsg).getResult());
            GetWaitNum();
            addCallbackParam("workstatus", QueueConstant.Window_WorkStatus_Free);

        }
    }

    public void zhuxiaoclick() {
        if (!QueueConstant.Window_WorkStatus_NotLogin.equals(getHidStatus())) {
            // 更新窗口人员为离线，状态离开
            handletoolbarservice.zhuXiaoQueue(zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                    zwfwuserSession.getCenterGuid());
            addCallbackParam("workstatus", QueueConstant.Window_WorkStatus_NotLogin);

        }
        else {
            // 更新窗口人员为在线，状态暂停
            handletoolbarservice.pauseQueue(zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                    zwfwuserSession.getCenterGuid());
            addCallbackParam("workstatus", QueueConstant.Window_WorkStatus_Pause);

        }
    }

    // 再次呼叫
    public void recallclick(String QNO) {
        handletoolbarservice.reCallQueue(QNO, zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                zwfwuserSession.getCenterGuid());

    }

    public void evaluate(String QNO) {
        addCallbackParam("msg", evaluateData(QNO, zwfwuserSession.getWindowGuid(), zwfwuserSession.getCenterGuid()));
    }

    public String evaluateData(String QNO, String windowguid, String CenterGuid) {
        String fieldstr = " Rowguid ";
        AuditQueue queue = auditqueueservice.getQNODetailByQNO(fieldstr, QNO, CenterGuid).getResult();
        if (queue != null) {
            // 先判断是否已评价
            if (!evaluateservice.isExistEvaluate(queue.getRowguid()).getResult()) {
                // rabbitmq推送
                handlemqservice.sendMQEvaluatebyEvaluate(windowguid, queue.getRowguid(),
                        ZwfwConstant.Evaluate_clienttype_QNO);
                return "success";
            }
            else {

                return "该排队号已评价，请勿重复评价！";
            }
        }
        else {
            return "该排队号不存在！";
        }

    }

    // 下一位
    public void nextclick(String QNO) {

        addCallbackParam("QNO",
                handletoolbarservice.nextQueue(QNO, zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                        zwfwuserSession.getCenterGuid(), userSession.getUserGuid(), issendmsg).getResult());
        GetWaitNum();
    }

    // 过号
    public void passclick(String QNO) {
        addCallbackParam("QNO",
                handletoolbarservice.passQueue(QNO, zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                        zwfwuserSession.getCenterGuid(), userSession.getUserGuid(), issendmsg).getResult());
        GetWaitNum();
    }

    // 自动刷新
    public void AutoRefresh() {
        addCallbackParam("QNO",
                handletoolbarservice.getNextQNO(zwfwuserSession.getWindowGuid(), zwfwuserSession.getWindowNo(),
                        zwfwuserSession.getCenterGuid(), userSession.getUserGuid(), issendmsg).getResult());
        GetWaitNum();
    }

    // 获取人数
    public void GetWaitNum() {
        addCallbackParam("waitnum", StringUtil.getNotNullString(
                auditqueueservice.getWindowWaitNum(zwfwuserSession.getWindowGuid(), true).getResult()));
    }

    // 自动刷新的等待人数
    public void GetWaitNumAuto(String waitnum) {
        addCallbackParam("waitnum", StringUtil.getNotNullString(
                handletoolbarservice.getWindowWaitNumAuto(zwfwuserSession.getWindowGuid(), waitnum).getResult()));
    }

    // 是否显示工具条
    public void ISShowToolbar() {

        addCallbackParam("ISShow", ZwfwUserSession.getInstance().getIsUseQueue());
    }

    // 获取窗口编号
    public void getWindowNo() {
        if (StringUtil.isNotBlank(zwfwuserSession.getWindowGuid())) {
            addCallbackParam("windowno", zwfwuserSession.getWindowNo());
        }
    }

    // 推送登录信息
    public void sendMQStatusbyEvaluate() {

        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))
                && StringUtil.isNotBlank(zwfwuserSession.getWindowGuid())) {

            // 如果使用排队叫号，则由工具条初始化推送，防止有工具条人员信息初始化两次
            if (!QueueConstant.Common_yes_String.equals(ZwfwUserSession.getInstance().getIsUseQueue())) {
                // 初始化窗口信息,更新人员已经在线，状态为暂停，不直接分配号码
                handletoolbarservice.initQueueLogin(zwfwuserSession.getWindowGuid(), userSession.getUserGuid(),
                        userSession.getDisplayName(), QueueConstant.Window_WorkStatus_Pause);

                handlemqservice.sendMQLoginbyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
                // 在线
                handlemqservice.sendMQOnLinebyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
            }
        }
    }

    public String getHidStatus() {
        return hidStatus;
    }

    public void setHidStatus(String hidStatus) {
        this.hidStatus = hidStatus;
    }

    public String getOldhidStatus() {
        return oldhidStatus;
    }

    public void setOldhidStatus(String oldhidStatus) {
        this.oldhidStatus = oldhidStatus;
    }

    // 联办
    public void lianban(String status) {
        String A = zwfwuserSession.getWindowGuid();
        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
                .getComponent(IAuditQueueOrgaWindow.class);
        if (!"10".equals(status)) {
            windowservice.updateWindowWorkStatus(zwfwuserSession.getWindowGuid(), "10");
            handlemqservice.sendMQJSbyWindow(zwfwuserSession.getWindowGuid(), "refresh()");
            // getViewData(getHidStatus());
            addViewData("hidstatus", getHidStatus());

        }
        else {

            windowservice.updateWindowWorkStatus(zwfwuserSession.getWindowGuid(), getViewData("hidstatus"));
            handlemqservice.sendMQJSbyWindow(zwfwuserSession.getWindowGuid(), "refresh()");
            setHidStatus(getViewData("hidstatus"));
        }
    }

}
