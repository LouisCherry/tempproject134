package com.epoint.auditqueue.toolbar.action;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.composite.auditqueue.handletoolbar.inter.IHandleToolBar;
import com.epoint.composite.auditqueue.handletoolbar.inter.ISSHandleToolBar;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController("sstoolbarqueueaction")
@Scope("request")
public class SSToolbarQueueAction extends BaseController {
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditQueue auditqueueservice;
    @Autowired
    private IHandleRabbitMQ handlemqservice;
    @Autowired
    private IHandleToolBar handletoolbarservice;
    @Autowired
    private ISSHandleToolBar sshandletoolbarservice;
    @Autowired
    private IAuditOnlineEvaluat evaluateservice;
    @Autowired
    private IHandleConfig handleConfigservice;
    private UserSession usersession = UserSession.getInstance();
    private ZwfwUserSession zwfwuserSession = ZwfwUserSession.getInstance();
    private String hidStatus;

    public SSToolbarQueueAction() {
    }

    public void pageLoad() {
        if (!this.isPostback()) {
            if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting")) && StringUtil.isNotBlank(this.zwfwuserSession.getWindowGuid())) {
                this.handletoolbarservice.initQueueLogin(this.zwfwuserSession.getWindowGuid(), this.usersession.getUserGuid(), this.usersession.getDisplayName(), "4");
                this.handlemqservice.sendMQLoginbyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
                this.handlemqservice.sendMQOnLinebyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
            }

            if ("1".equals(this.zwfwuserSession.getIsUseQueue())) {
                this.addCallbackParam("ISShow", "1");
                this.addCallbackParam("windowno", this.zwfwuserSession.getWindowNo());
                this.setHidStatus("4");
                this.GetWaitNum();
            } else {
                this.addCallbackParam("ISShow", "0");
            }

            String showparts = (String)this.handleConfigservice.getFrameConfig("AS_ZNSB_TOOLBARPART", this.zwfwuserSession.getCenterGuid()).getResult();
            if (StringUtil.isNotBlank(showparts)) {
                this.addCallbackParam("showparts", showparts);
            } else {
                this.addCallbackParam("showparts", "");
            }
        }

    }

    public void delayclick() {
        if (!"4".equals(this.getHidStatus())) {
            this.handletoolbarservice.pauseQueue(this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid());
            this.addCallbackParam("workstatus", "4");
        } else {
            this.addCallbackParam("QNO", this.sshandletoolbarservice.startQueue(this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid(), this.usersession.getUserGuid(), true).getResult());
            this.GetWaitNum();
            this.addCallbackParam("workstatus", "1");
        }

    }

    public void zhuxiaoclick() {
        if (!"0".equals(this.getHidStatus())) {
            this.handletoolbarservice.zhuXiaoQueue(this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid());
            this.addCallbackParam("workstatus", "0");
        } else {
            this.handletoolbarservice.pauseQueue(this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid());
            this.addCallbackParam("workstatus", "4");
        }

    }

    public void logoutaction() {
        this.handletoolbarservice.updateWindowAfterLogout(this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getCenterGuid());
    }

    public void recallclick(String QNO) {
        this.handletoolbarservice.reCallQueue(QNO, this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid());
    }

    public void evaluate(String QNO) {
        this.addCallbackParam("msg", this.evaluateData(QNO, this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getCenterGuid()));
    }

    public String evaluateData(String QNO, String windowguid, String CenterGuid) {
        String fieldstr = " Rowguid ";
        AuditQueue queue = (AuditQueue)this.auditqueueservice.getQNODetailByQNO(fieldstr, QNO, CenterGuid).getResult();
        if (queue != null) {
            if (!(Boolean)this.evaluateservice.isExistEvaluate(queue.getRowguid()).getResult()) {
                this.handlemqservice.sendMQEvaluatebyEvaluate(windowguid, queue.getRowguid(), "30");
                return "success";
            } else {
                return "该排队号已评价，请勿重复评价！";
            }
        } else {
            return "该排队号不存在！";
        }
    }

    public void nextclick(String QNO) {
        this.addCallbackParam("QNO", this.sshandletoolbarservice.nextQueue(QNO, this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid(), this.usersession.getUserGuid(), true).getResult());
        this.GetWaitNum();
    }

    public void getpassclick(String QNO, String passQNO) {
        this.addCallbackParam("QNO", this.handletoolbarservice.nextPassQueue(QNO, passQNO, this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid(), this.usersession.getUserGuid()));
    }

    public void passclick(String QNO) {
        this.addCallbackParam("QNO", this.sshandletoolbarservice.passQueue(QNO, this.zwfwuserSession.getWindowGuid(), this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid(), this.usersession.getUserGuid(), true).getResult());
        this.GetWaitNum();
    }

    public void turnqnoclick(String QNO) {
        AuditQueue queue = (AuditQueue)this.auditqueueservice.getQNODetailByQNO("rowguid", QNO, this.zwfwuserSession.getCenterGuid()).getResult();
        this.addCallbackParam("guid", queue.getRowguid());
    }

    public void AutoRefresh() {
        this.addCallbackParam("QNO", sshandletoolbarservice.getNextQNO(this.zwfwuserSession.getWindowGuid(),
                this.zwfwuserSession.getWindowNo(), this.zwfwuserSession.getCenterGuid(),
                this.usersession.getUserGuid(), true,true).getResult());
        this.GetWaitNum();
    }

    public void GetWaitNum() {
        this.addCallbackParam("waitnum", StringUtil.getNotNullString(this.auditqueueservice.getWindowWaitNum(this.zwfwuserSession.getWindowGuid(), true).getResult()));
    }

    public void GetWaitNumAuto(String waitnum) {
        this.addCallbackParam("waitnum", StringUtil.getNotNullString(this.handletoolbarservice.getWindowWaitNumAuto(this.zwfwuserSession.getWindowGuid(), waitnum).getResult()));
    }

    public void ISShowToolbar() {
        this.addCallbackParam("ISShow", ZwfwUserSession.getInstance().getIsUseQueue());
    }

    public void getWindowNo() {
        if (StringUtil.isNotBlank(this.zwfwuserSession.getWindowGuid())) {
            this.addCallbackParam("windowno", this.zwfwuserSession.getWindowNo());
        }

    }

    public void sendMQStatusbyEvaluate() {
        if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting")) && StringUtil.isNotBlank(this.zwfwuserSession.getWindowGuid()) && !"1".equals(ZwfwUserSession.getInstance().getIsUseQueue())) {
            this.handletoolbarservice.initQueueLogin(this.zwfwuserSession.getWindowGuid(), this.usersession.getUserGuid(), this.usersession.getDisplayName(), "4");
            this.handlemqservice.sendMQLoginbyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
            this.handlemqservice.sendMQOnLinebyEvaluate(ZwfwUserSession.getInstance().getWindowGuid());
        }

    }

    public String getHidStatus() {
        return this.hidStatus;
    }

    public void setHidStatus(String hidStatus) {
        this.hidStatus = hidStatus;
    }
}

