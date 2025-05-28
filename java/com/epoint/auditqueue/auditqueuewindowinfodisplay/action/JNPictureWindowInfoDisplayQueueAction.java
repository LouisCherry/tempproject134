package com.epoint.auditqueue.auditqueuewindowinfodisplay.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditorga.auditwindow.action.IJNAuditWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;

@RestController("jnpicturewindowinfodisplayqueueaction")
@Scope("request")
public class JNPictureWindowInfoDisplayQueueAction extends BaseController
{

    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditOrgaWindow windowservice;
    @Autowired
    private IAuditTask taskservice;
    @Autowired
    private IAuditQueue queueservice;
    @Autowired
    private IAuditQueueOrgaWindow queuewindowservice;
    @Autowired
    private IUserService frameuser;
    @Autowired
    private IJNAuditWindow jnauditwindow;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAttachService attachservice;

    @Override
    public void pageLoad() {
        addCallbackParam("pictureguid", getEquipmentPicture(getRequestParameter("MacAddress")));
    }

    public String getEquipmentPicture(String MacAddress) {
        AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(MacAddress).getResult();
        if (equipment != null) {
            return equipment.getStr("windowpictureguid");
        }
        return "";
    }

    public String getWindowTask(String windowguid) {

        StringBuilder html = new StringBuilder();
        AuditTask task = null;
        List<AuditOrgaWindowTask> windowtasklist = windowservice.getTaskByWindow(windowguid).getResult();
        int count = 0;
        for (AuditOrgaWindowTask windowtask : windowtasklist) {
            task = taskservice.selectUsableTaskByTaskID(windowtask.getTaskid()).getResult();
            if (StringUtil.isNotBlank(task)) {
                html.append("<li class=\"item\"><a href=\"javascript:void(0)\">" + task.getTaskname() + "</a></li>");
            }
            count++;
            if (count >= 50) {
                break;
            }

        }
        return html.toString();

    }

    public void showWindowInfo() {
        String windowguid = getRequestParameter("windowguid");
        getAddCommonParam(windowguid);
    }

    public void showWindowInfowithPic() {
        String windowguid = getRequestParameter("windowguid");
        getAddCommonParam(windowguid);
        addCallbackParam("WindowUserGH", getWindowUserGH(windowguid));
        addCallbackParam("MyPic", getMyPic(windowguid));
    }

    private void getAddCommonParam(String windowguid) {
        Record window = jnauditwindow.getauditwindow(windowguid);
        String indicating = window.get("indicating");
        String childindicating = window.get("childindicating");
        addCallbackParam("indicating", indicating);
        addCallbackParam("childindicating", childindicating);
        String windowbh = jnauditwindow.getauditwindowdetail(windowguid).getWindowbh();
        addCallbackParam("windowbh", windowbh);
        addCallbackParam("WindowNO", window.get("windowno"));
        addCallbackParam("WindowName", window.get("windowname"));
        String qno = "";
        AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
        if (StringUtil.isNotBlank(queuewindow)) {

            if (QueueConstant.Window_WorkStatus_NotLogin.equals(queuewindow.getWorkstatus())) {
                qno = "未登录";
            }
            else if (QueueConstant.Window_WorkStatus_Pause.equals(queuewindow.getWorkstatus())) {
                qno = "请您稍候";
            }
            else if ("10".equals(queuewindow.getWorkstatus())) {
                qno = "联合办理";
            }
            else if (StringUtil.isNotBlank(queuewindow.getCurrenthandleqno())) {
                qno = queuewindow.getCurrenthandleqno();
            }
            else {
                qno = "空闲";
            }
        }
        addCallbackParam("CurrentNO", qno);
        addCallbackParam("WaitCount",
                StringUtil.getNotNullString(queueservice.getWindowWaitNum(windowguid, true).getResult()));
    }

    public String getWindowUserGH(String windowguid) {
        AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
        if (StringUtil.isNotBlank(queuewindow)) {
            if (StringUtil.isNotBlank(queuewindow.getUserguid())) {
                FrameUserExtendInfo userExtendInfo = frameuser.getUserExtendInfoByUserGuid(queuewindow.getUserguid());
                if (StringUtil.isNotBlank(userExtendInfo)) {
                    return userExtendInfo.getShortMobile();
                }
                else {
                    return "";
                }

            }
            else {
                return "";
            }
        }
        else {
            return "";
        }
    }

    public String getMyPic(String windowguid) {
        AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
        FrameUserExtendInfo extendInfo = null;
        if (StringUtil.isNotBlank(queuewindow)) {
            if (StringUtil.isNotBlank(queuewindow.getUserguid())) {
                extendInfo = frameuser.getUserExtendInfoWithPicContent(queuewindow.getUserguid());

            }
            if (extendInfo != null) {
                byte[] mypic = extendInfo.getPicContent();
                if (mypic != null) {
                    return "data:" + extendInfo.getPicContentType() + ";base64," + Base64Util.encode(mypic);
                }
                else {
                    return "";
                }
            }
            return "";
        }
        else {
            return "";
        }
    }
}
