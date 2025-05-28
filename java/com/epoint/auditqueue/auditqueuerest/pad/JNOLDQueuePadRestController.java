package com.epoint.auditqueue.auditqueuerest.pad;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.auditqueue.auditqueuerest.pad.api.IJNQueuePad;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbterminalapp.domain.AuditZnsbTerminalApp;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.composite.auditqueue.handletoolbar.inter.IHandleToolBar;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.MDUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.spring.util.SpringContextUtil;
import com.epoint.util.TARequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/jnoldqueuePad" })
public class JNOLDQueuePadRestController
{
    @Autowired
    private IUserService userservice;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditQueueOrgaWindow queuewindowservice;
    @Autowired
    private IAuditQueue queueservice;
    @Autowired
    private IAuditOrgaWindowYjs windowservice;
    @Autowired
    private IAuditProject projectservice;
    @Autowired
    private IAuditOrgaServiceCenter orgacenterservice;
    @Autowired
    private IAuditTask auditTaskService;
    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;
    @Autowired
    private IAuditQueueTasktypeTask tasktypetaskservice;
    @Autowired
    private IAuditQueueUserinfo userinfoservice;
    @Autowired
    private IAuditOrgaWorkingDay workingDayService;
    @Autowired
    private IAuditProjectSparetime sparetimeService;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditZnsbTerminalApp appservice;
    @Autowired
    private IHandleToolBar handletoolbarservice;
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IHandleRabbitMQ handlemqservice;
    @Autowired
    private IAuditOnlineEvaluat evaluateservice;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private IConfigService configservice;
    @Autowired
    private IJNQueuePad jnpadService;
    private Boolean issendmsg = true;
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");
    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;
    @RequestMapping(value = {"/login" }, method = {RequestMethod.POST })
    public String login(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String loginid = obj.getString("loginid");
            String password = obj.getString("password");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String windowguid = "";
            FrameUser user = this.userservice.getUserByUserField("LoginID", loginid);
            if (user != null) {
                if (user.getPassword().equals(MDUtil.authPassword(password))) {
                    dataJson.put("userguid", user.getUserGuid());
                    if (windowservice.getWindowCountByUserGuid(user.getUserGuid()).getResult() > 1) {
                        windowguid = windowservice.getWindowByMacandUserGuid(macaddress, user.getUserGuid())
                                .getResult();
                        if (StringUtil.isBlank(windowguid)) {
                            return JsonUtils.zwdtRestReturn("0", "由于您的账户配置了多个窗口，且该pad的机器码未绑定窗口或者绑定的窗口不是您账户配置的窗口，无法登陆！",
                                    "");
                        }
                    }
                    else {
                        AuditOrgaWindow auditwindow = windowservice.getWindowByUserGuid(user.getUserGuid()).getResult();
                        if (auditwindow == null) {
                            return JsonUtils.zwdtRestReturn("0", "该用户未配置窗口！", "");
                        }

                        windowguid = auditwindow.getRowguid();
                    }

                    dataJson.put("windowguid", windowguid);
                    this.handletoolbarservice.initQueueLogin(windowguid, user.getUserGuid(), user.getDisplayName(),
                            "4");
                    this.handlemqservice.sendMQLoginbyEvaluate(windowguid);
                    this.handlemqservice.sendMQOnLinebyEvaluate(windowguid);
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "您输入的密码不正确！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该用户不存在！", "");
            }
        }
        catch (JSONException var11) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var11.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/userDetail" }, method = {RequestMethod.POST })
    public String userDetail(@RequestBody String params, HttpServletRequest request) throws IOException {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            JSONObject dataJson = new JSONObject();
            FrameUser user = this.userservice.getUserByUserField("UserGuid", userguid);
            if (user != null) {
                FrameOu ou = this.ouservice.getOuByOuGuid(user.getOuGuid());
                if (ou != null) {
                    this.userservice.getUserExtendInfoByUserGuid(userguid);
                    dataJson.put("ouname",
                            StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                    dataJson.put("displayname", user.getDisplayName());
                    dataJson.put("photourl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/rest/auditattach/getUserPic?userguid=" + userguid);
                    AuditOrgaWindow auditwindow = windowservice.getWindowByWindowGuid(windowguid).getResult();
                    if (auditwindow != null) {
                        AuditOrgaServiceCenter auditCenter = orgacenterservice
                                .findAuditServiceCenterByGuid(auditwindow.getCenterguid()).getResult();
                        if (StringUtil.isNotBlank(auditCenter)) {
                            dataJson.put("areacode", auditCenter.getBelongxiaqu());
                            dataJson.put("windowguid", auditwindow.getRowguid());
                            dataJson.put("windowno", auditwindow.getWindowno());
                            dataJson.put("hallguid", auditwindow.getLobbytype());
                            dataJson.put("centerguid", auditwindow.getCenterguid());
                            dataJson.put("banlinum", StringUtil.getNotNullString(
                                    this.queueservice.getBanLiNumByWindowGuid(auditwindow.getRowguid()).getResult()));
                            return JsonUtils.zwdtRestReturn("1", "", dataJson);
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "未分配辖区！", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该用户未配置窗口！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该用户对应部门不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该用户不存在！", "");
            }
        }
        catch (JSONException var13) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var13.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/startQueue" }, method = {RequestMethod.POST })
    public String startQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String qno = "";
            JSONObject dataJson = new JSONObject();
            String call = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", centerguid, "1").getResult();
            if ("0".equals(call)) {
                issendmsg = false;
            }
            else {
                issendmsg = true;
            }
            qno = handletoolbarservice.startQueue(windowguid, windowno, centerguid, userguid, issendmsg).getResult();
            dataJson.put("handleno", qno);
            dataJson.put("waitnum",
                    StringUtil.getNotNullString(this.queueservice.getWindowWaitNum(windowguid, true).getResult()));
            dataJson.put("banlinum",
                    StringUtil.getNotNullString(this.queueservice.getBanLiNumByWindowGuid(windowguid).getResult()));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var11) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var11.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/stopQueue" }, method = {RequestMethod.POST })
    public String stopQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            this.handletoolbarservice.pauseQueue(windowguid, windowno, centerguid);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var10) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var10.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/nextQueue" }, method = {RequestMethod.POST })
    public String nextQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            JSONObject dataJson = new JSONObject();
            String call = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", centerguid, "1").getResult();
            if ("0".equals(call)) {
                issendmsg = false;
            }
            else {
                issendmsg = true;
            }
            String nextqno = handletoolbarservice.nextQueue(qno, windowguid, windowno, centerguid, userguid, issendmsg)
                    .getResult();
            dataJson.put("handleno", nextqno);
            dataJson.put("waitnum",
                    StringUtil.getNotNullString(this.queueservice.getWindowWaitNum(windowguid, true).getResult()));
            dataJson.put("banlinum",
                    StringUtil.getNotNullString(this.queueservice.getBanLiNumByWindowGuid(windowguid).getResult()));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var12) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var12.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/passQueue" }, method = {RequestMethod.POST })
    public String passQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            JSONObject dataJson = new JSONObject();
            String call = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", centerguid, "1").getResult();
            if ("0".equals(call)) {
                issendmsg = false;
            }
            else {
                issendmsg = true;
            }
            String nextqno = handletoolbarservice.passQueue(qno, windowguid, windowno, centerguid, userguid, issendmsg)
                    .getResult();
            dataJson.put("handleno", nextqno);
            dataJson.put("waitnum",
                    StringUtil.getNotNullString(this.queueservice.getWindowWaitNum(windowguid, true).getResult()));
            dataJson.put("banlinum",
                    StringUtil.getNotNullString(this.queueservice.getBanLiNumByWindowGuid(windowguid).getResult()));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var12) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var12.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/reCallQueue" }, method = {RequestMethod.POST })
    public String reCallQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            JSONObject dataJson = new JSONObject();
            this.handletoolbarservice.reCallQueue(qno, windowguid, windowno, centerguid);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var11) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var11.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/readCardQueue" }, method = {RequestMethod.POST })
    public String readCardQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String sfz = obj.getString("sfz");
            String fieldstr = " Identitycardnum ";
            JSONObject dataJson = new JSONObject();
            if (StringUtil.isNotBlank(sfz)) {
                AuditQueue QueueInfo = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
                if (QueueInfo != null) {
                    return StringUtil.isNotBlank(QueueInfo.getIdentitycardnum())
                            && !sfz.equals(QueueInfo.getIdentitycardnum())
                                    ? JsonUtils.zwdtRestReturn("0", "排队号与身份证不匹配！", "")
                                    : JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份证不能为空！", "");
            }
        }
        catch (JSONException var14) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var14.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/pingJiaQueue" }, method = {RequestMethod.POST })
    public String pingJiaQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String fieldstr = " Rowguid ";
            JSONObject dataJson = new JSONObject();
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (queue != null) {
                if ((Boolean) this.evaluateservice.isExistEvaluate(queue.getRowguid()).getResult()) {
                    return JsonUtils.zwdtRestReturn("0", "该排队号已评价，请勿重复评价！", "");
                }

                this.handlemqservice.sendMQEvaluatebyEvaluate(windowguid, queue.getRowguid(), "30");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var13) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var13.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/autoRefreshQueue" }, method = {RequestMethod.POST })
    public String autoRefreshQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String waitnum = obj.getString("waitnum");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String call = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_CALL", centerguid, "1").getResult();
            if ("0".equals(call)) {
                issendmsg = false;
            }
            else {
                issendmsg = true;
            }
            JSONObject dataJson = new JSONObject();
            AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
            String nextqno;
            if (queuewindow != null && "1".equals(queuewindow.getWorkstatus())) {
                if ((StringUtil.isBlank(qno) || "无办理人员！".equals(qno)) && StringUtil.isNotBlank(queuewindow.getWaitnum())
                        && Integer.parseInt(queuewindow.getWaitnum()) > 0) {
                    nextqno = handletoolbarservice.getNextQNO(windowguid, windowno, centerguid, userguid, issendmsg)
                            .getResult();
                }
                else {
                    nextqno = qno;
                }
            }
            else {
                nextqno = qno;
            }

            dataJson.put("handleno", nextqno);
            dataJson.put("waitnum", this.handletoolbarservice.getWindowWaitNumAuto(windowguid, waitnum).getResult());
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var14) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var14.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/zhuXiaoQueue" }, method = {RequestMethod.POST })
    public String zhuXiaoQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            this.handletoolbarservice.zhuXiaoQueue(windowguid, windowno, centerguid);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var10) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var10.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/scanQueue" }, method = {RequestMethod.POST })
    public String scanQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String areacode = obj.getString("areacode");
            String code = obj.getString("code");
            String qno = obj.getString("qno");
            JSONObject dataJson = new JSONObject();

            if (StringUtil.isBlank(qno) || "无办理人员！".equals(qno)) {
                return JsonUtils.zwdtRestReturn("0", "当前无办理人员，无法受理！", "");
            }
            String fieldstr = " Projectno,Projectguid ";
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (!StringUtil.isNotBlank(queue)) {
                return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
            }

            if (StringUtil.isBlank(queue.getStr("Projectno"))) {

                if (!StringUtil.isBlank(queue.getProjectguid())) {
                    return JsonUtils.zwdtRestReturn("0", "该排队号已受理，请勿重复受理！", "");
                }

                dataJson.put("codestatus", "1");
            }
            else {
                String fields = "  rowguid,taskguid,projectname,pviguid,status ";
                AuditProject project = projectservice
                        .getAuditProjectByFlowsn(fields, queue.getStr("Projectno"), areacode).getResult();
                if (project.getStatus() == 90) {
                    return JsonUtils.zwdtRestReturn("0", "该办件已办结，请勿重复办结！", "");
                }

                dataJson.put("codestatus", "2");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var17) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var17.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/scandvmQueue" }, method = {RequestMethod.POST })
    public String scandvmQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String areacode = obj.getString("areacode");

            String qno = obj.getString("qno");
            JSONObject dataJson = new JSONObject();

            if (StringUtil.isBlank(qno) || "无办理人员！".equals(qno)) {
                return JsonUtils.zwdtRestReturn("0", "当前无办理人员，无法受理！", "");
            }
            String fieldstr = " Projectno,Projectguid,taskguid,status ";
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (!StringUtil.isNotBlank(queue)) {
                return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
            }
            if ("0".equals(queue.getStatus())) {
                jnpadService.selectqno(qno, windowguid, windowno, centerguid, userguid);
            }
            if (StringUtil.isBlank(queue.getStr("Projectno"))) {

                if (!StringUtil.isBlank(queue.getProjectguid())) {
                    return JsonUtils.zwdtRestReturn("0", "该排队号已受理，请勿重复受理！", "");
                }

                dataJson.put("codestatus", "1");
            }
            else {
                String fields = "  rowguid,taskguid,projectname,pviguid,status ";
                AuditProject project = projectservice
                        .getAuditProjectByFlowsn(fields, queue.getStr("Projectno"), areacode).getResult();
                if (project.getStatus() == 90) {
                    return JsonUtils.zwdtRestReturn("0", "该办件已办结，请勿重复办结！", "");
                }

                dataJson.put("codestatus", "2");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var17) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var17.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/shouliQueue" }, method = {RequestMethod.POST })
    public String shouLiQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String areacode = obj.getString("areacode");
            String code = obj.getString("code");
            String qno = obj.getString("qno");
            String projectguid = UUID.randomUUID().toString();
            String username = userservice.getUserByUserField("userguid", userguid).getDisplayName();
            String windowname = "";
            AuditOrgaWindow auditwindow = windowservice.getWindowByWindowGuid(windowguid).getResult();
            if (auditwindow != null) {
                windowname = auditwindow.getWindowname();
            }

            JSONObject dataJson = new JSONObject();
            String fieldstr = " * ";
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (!StringUtil.isNotBlank(queue)) {
                return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
            }
            else {
                AuditProject project = new AuditProject();

                project.setOperateusername(username);
                project.setOperatedate(new Date());
                project.setRowguid(projectguid);
                project.setWindowguid(windowguid);
                project.setWindowname(windowname);
                List<String> taskidlist = tasktypetaskservice.getTaskIDbyTaskTypeGuid(queue.getTaskguid());
                if (!taskidlist.isEmpty()) {
                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(taskidlist.get(0)).getResult();
                    if (auditTask != null) {
                        project.setTask_id(auditTask.getTask_id());
                        project.setTaskguid(auditTask.getRowguid());
                        project.setOuguid(auditTask.getOuguid());
                        project.setProjectname(auditTask.getTaskname());
                        project.setIs_charge(auditTask.getCharge_flag());
                        project.setAreacode(auditTask.getAreacode());
                        project.setTasktype(auditTask.getType());
                        project.setPromise_day(auditTask.getPromise_day());
                        AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                                .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                        if (auditTaskExtension != null) {
                            project.setCharge_when(auditTaskExtension.getCharge_when());
                        }

                        Date shouldEndDate = null;
                        int ts;
                        if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                            shouldEndDate = workingDayService
                                    .getWorkingDayWithOfficeSet(centerguid, new Date(), auditTask.getPromise_day())
                                    .getResult();
                            ts = auditTask.getPromise_day() * 24 * 60;
                        }
                        else {
                            shouldEndDate = null;
                            ts = 0;
                        }

                        if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       project.setPromiseenddate(shouldEndDate);
}
                        sparetimeService.addSpareTimeByProjectGuid(projectguid, ts, areacode, centerguid);
                    }
                    project.setOuname(auditTask.getOuname());
                    project.setOuguid(auditTask.getOuguid());
                    project.setCenterguid(centerguid);
                    project.setApplyertype(Integer.parseInt("20"));
                    project.setApplydate(new Date());
                    project.setApplyway(Integer.parseInt("20"));
                    project.setCerttype("22");
                    project.setIs_test(Integer.parseInt("0"));
                    project.setIs_delay(20);
                    if (StringUtil.isNotBlank(queue.getIdentitycardnum())) {
                        Record userinfo = userinfoservice.getUserByIdentityCardNum(queue.getIdentitycardnum())
                                .getResult();
                        if (userinfo != null) {
                            project.setApplyeruserguid(StringUtil.getNotNullString(userinfo.get("UserGuid")));
                            project.setApplyername(StringUtil.getNotNullString(userinfo.get("DisplayName")));
                            project.setContactmobile(StringUtil.getNotNullString(userinfo.get("Mobile")));
                            project.setAddress(StringUtil.getNotNullString(userinfo.get("address")));
                            project.setCertnum(StringUtil.getNotNullString(queue.getIdentitycardnum()));
                        }
                    }

                    // 在浪潮库生成办件
                    // 来源（外网还是其他系统）
                    ICommonDao baseDao = CommonDao.getInstance();
                    String resource = "1";
                    String sql = "select unid from audit_task where rowguid=?";
                    String unid = baseDao.queryString(sql, auditTask.getRowguid());
                    String receiveNum = code;
                    if (StringUtil.isNotBlank(unid)) {
                     // 请求接口获取受理编码
            			if (StringUtil.isNotBlank(unid)) {
            				String result = FlowsnUtil.createReceiveNum(unid,auditTask.getRowguid());
            				if (!"error".equals(result)) {
            					receiveNum = result;
            				} else {
            					log.info("========================>获取受理编号异常，请重试！");
            				}
            			}
            			
            			/*
                        // 构造获取受理编码的请求入参
                        String params2Get = "?itemId=" + item_Id + "&applyFrom=" + resource;
                        String pwd = "";
                        // 请求接口获取受理编码
                        try {
                            JSONObject jsonObj = WavePushInterfaceUtils.createReceiveNum(params2Get,
                                    auditTask.getShenpilb());

                            if (jsonObj != null && "200".equals(jsonObj.getString("state"))) {
                                log.info("========================>获取受理编码成功！" + jsonObj.getString("receiveNum")
                                        + "#####" + jsonObj.getString("password"));
                                pwd = jsonObj.getString("password");
                                receiveNum = jsonObj.getString("receiveNum");
                            }
                            else {
                                log.info("========================>获取受理编码失败！");
                            }
                        }
                        catch (IOException e) {
                            log.info("接口请求报错！========================>" + e.getMessage());
                            e.printStackTrace();
                        }
                        project.set("pwd", pwd);*/
                    }
                    project.setFlowsn(receiveNum);
                    project.setReceiveuserguid(userguid);
                    project.setReceiveusername(username);
                    project.setReceivedate(new Date());
                    project.setAcceptuserguid(userguid);
                    project.setAcceptusername(username);
                    project.setAcceptuserdate(new Date());
                    project.setStatus(30);
                    project.set("is_lczj", 3);
                    projectservice.addProject(project);

                    queueservice.updateQNOProject(projectguid, qno, centerguid);
                    String sql2 = "update AUDIT_QUEUE set Projectno='" + receiveNum + "' where QNO='" + qno
                            + "' and CenterGuid='" + centerguid + "'  and  date(GETNOTIME) = curdate() ";
                    log.info(sql2);
                    ICommonDao commondao = CommonDao.getInstance();
                    commondao.execute(sql2);
                    log.info("开始调用推送好差评数据接口");
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项分类未关联事项！", "");
                }
            }
        }
        catch (JSONException var25) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var25.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/banJieQueue" }, method = {RequestMethod.POST })
    public String banJieQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String macaddress = obj.getString("macaddress");
            String windowno = obj.getString("windowno");
            String centerguid = obj.getString("centerguid");
            String areacode = obj.getString("areacode");
            String code = obj.getString("code");
            String qno = obj.getString("qno");
            String banjiestatus = obj.getString("banjiestatus");
            String username = userservice.getUserByUserField("userguid", userguid).getDisplayName();
            JSONObject dataJson = new JSONObject();

            if (StringUtil.isBlank(qno) || "无办理人员！".equals(qno)) {
                return JsonUtils.zwdtRestReturn("0", "当前无办理人员，无法受理！", "");
            }
            String fieldstr = " Projectno,Projectguid ";
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            String fields = "  * ";
            AuditProject project = projectservice.getAuditProjectByFlowsn(fields, queue.getStr("Projectno"), areacode)
                    .getResult();
            if (StringUtil.isNotBlank(project)) {
                project.setBanjieresult(Integer.valueOf(banjiestatus));
                project.setBanwandate(new Date());
                AuditProjectSparetime auditprojectspare = sparetimeService
                        .getSparetimeByProjectGuid(project.getRowguid()).getResult();
                int spendtime = auditprojectspare.getSpendminutes();
                int sparetime = auditprojectspare.getSpareminutes();
                project.setStatus(90);
                project.setBanjiedate(new Date());
                project.setBanjieuserguid(userguid);
                project.setBanjieusername(username);
                project.setSparetime(sparetime);
                project.setSpendtime(spendtime);
                project.setIs_guidang(1);
                project.setGuidangdate(new Date());
                project.setGuidanguserguid(userguid);
                project.setGuidangusername(username);

                //查看有无centerguid
                if(StringUtils.isBlank(project.getCenterguid())){
                    AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(project.getAreacode());
                    if(auditOrgaServiceCenter!=null){
                        project.setCenterguid(auditOrgaServiceCenter.getRowguid());
                    }
                }
                IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                        .getComponent(IAuditOrgaWorkingDay.class);
                //更新承诺办结时间
                AuditTask  auditTask = auditTaskService.getAuditTaskByGuid(project.getTaskguid(),true).getResult();
                if(auditTask!=null) {
                    List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(project.getRowguid());
                    Date acceptdat = project.getAcceptuserdate();
                    Date shouldEndDate;
                    if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {

                        shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                                project.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                        log.info("shouldEndDate:"+shouldEndDate);
                    } else {
                        shouldEndDate = null;
                    }
                    if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                                        AuditProjectUnusualUtils auditProjectUnusualUtils= new AuditProjectUnusualUtils();
                int totalWorkingDaysPaused  = auditProjectUnusualUtils.calculateTotalWorkingDaysPaused(auditProjectUnusuals,project.getCenterguid());
                if (totalWorkingDaysPaused > 0 && shouldEndDate != null) {
                    // 重新计算包含暂停时间的预计结束日期
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            project.getCenterguid(), shouldEndDate, (int) totalWorkingDaysPaused).getResult();
                    log.info("考虑暂停时间后的预计结束日期 shouldEndDate: " + shouldEndDate);
                }
                log.info("shouldEndDate:"+shouldEndDate);
                    }
                    if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       project.setPromiseenddate(shouldEndDate);
}
                }
                projectservice.updateProject(project);
                sparetimeService.deleteSpareTimeByRowGuid(auditprojectspare.getRowguid());
                log.info("开始调用推送好差评数据接口2");

                turnhcpevaluate(auditTask, project, 1, "出证办结");
                if (StringUtil.isNotBlank(project.getApplyername())) {
                    evaluate("1", windowguid, project);
                }

                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }
        }
        catch (JSONException var20) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var20.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/updateApp" }, method = {RequestMethod.POST })
    public String updateApp(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String info = obj.getString("info");
            String apppath = "";
            String centerguid = "";
            JSONObject dataJson = new JSONObject();
            centerguid = ((AuditZnsbEquipment) this.equipmentservice.getDetailbyMacaddress(macaddress).getResult())
                    .getCenterguid();
            if (StringUtil.isNotBlank(centerguid)) {
                AuditZnsbTerminalApp app = appservice.getDetailbyApptype("2", centerguid).getResult();
                if (StringUtil.isNotBlank(app)
                        && Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))
                        && StringUtil.isNotBlank(app.getAppattachguid())) {
                    List<FrameAttachInfo> attachinfolist = this.attachservice
                            .getAttachInfoListByGuid(app.getAppattachguid());
                    if (!attachinfolist.isEmpty()) {
                        apppath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/rest/auditattach/readAttach?attachguid="
                                + ((FrameAttachInfo) attachinfolist.get(0)).getAttachGuid();
                    }
                }
            }

            dataJson.put("apppath", apppath);
            log.info(apppath);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException var12) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var12.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/init" }, method = {RequestMethod.POST })
    public String init(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";
            AuditZnsbEquipment equipment;
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setStatus("1");
                equipment.setMachinetype("9");
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                this.equipmentservice.insertEquipment(equipment);
            }

            equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    String config = handleConfigservice.getFrameConfig("AS_IS_ENABLE_APPLOG", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("recordlog", config);
                    }
                    else {
                        dataJson.put("recordlog", "0");
                    }

                    if ("1".equals(this.configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE"))) {
                        dataJson.put("epointdevice", "1");
                    }
                    else {
                        dataJson.put("epointdevice", "0");
                    }

                    config = handleConfigservice.getFrameConfig("AS_RabbitMQ", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("rabbitmq", config);
                        CachingConnectionFactory conn = null;
                        if (SpringContextUtil.getApplicationContext().containsBean("connectionFactory")) {
                            conn = (CachingConnectionFactory) SpringContextUtil.getBean("connectionFactory");
                            dataJson.put("rabbitmqusername", conn.getRabbitConnectionFactory().getUsername());
                            dataJson.put("rabbitmqpassword", conn.getRabbitConnectionFactory().getPassword());
                        }
                    }

                    if (StringUtil.isNotBlank(equipment.getUrl())) {
                        dataJson.put("homepageurl", equipment.getUrl());
                        return "0".equals(equipment.getStatus()) ? JsonUtils.zwdtRestReturn("0", "设备已离线！", "")
                                : JsonUtils.zwdtRestReturn("1", "", dataJson);
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "首页地址未配置！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
        }
        catch (JSONException var10) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var10.getMessage(), "");
        }
    }

    public void turnhcpevaluate(AuditTask auditTask, AuditProject auditProject, int serviceNumber, String servicename) {
        try {
            EpointFrameDsManager.begin(null);
            JSONObject json = new JSONObject();
            String ouguid = auditProject.getOuguid();
            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(ouguid);
            String deptcode = "";
            if (frameOuExten != null) {
                deptcode = frameOuExten.getStr("orgcode");
            }
            else {
                deptcode = "11370900MB28449441";
            }

            json.put("taskCode", auditTask.getItem_id());
            json.put("areaCode", auditProject.getAreacode() + "000000");
            json.put("taskName", auditTask.getTaskname());
            json.put("projectNo", auditProject.getFlowsn());
            String proStatus = serviceNumber + "";
            Integer status = auditProject.getStatus();
            // if (status < 30) {
            // proStatus = "1";
            // }else if (status >= 30 && status< 90) {
            // proStatus = "2";
            // }else if (status >= 90) {
            // proStatus = "3";
            // }else {
            // proStatus = "1";
            // }
            String acceptdate = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
            json.put("proStatus", proStatus);
            json.put("orgcode", auditProject.getAreacode() + "000000_" + deptcode);
            // json.put("orgcode", "370900000000_11370900004341048Y");
            json.put("orgName", auditTask.getOuname());
            json.put("acceptDate", acceptdate);
            Integer applyertype = auditProject.getApplyertype();
            if (applyertype == 10) {
                applyertype = 2;
            }
            else if (applyertype == 20) {
                applyertype = 1;
            }
            else if (applyertype == 30) {
                applyertype = 9;
            }
            else {
                applyertype = 9;
            }
            json.put("userProp", applyertype);
            json.put("userName", auditProject.getApplyername());
            json.put("userPageType", "111");
            json.put("proManager", auditProject.getAcceptusername());
            json.put("certKey", auditProject.getCertnum());
            json.put("certKeyGOV", auditProject.getCertnum());
            String auditServiceName = "";
            json.put("serviceName", servicename);// 环节名称

            String serviceNumUrl = ConfigUtil.getConfigValue("hcp", "HcpServiceNumber");

            JSONObject evJson = new JSONObject();
            evJson.put("commented", "0");
            evJson.put("mark", "0");
            evJson.put("userName", auditProject.getApplyername());
            evJson.put("creditType", 111);
            evJson.put("creditNum", auditProject.getCertnum());
            // evJson.put("commented", "0");
            String getServiceNumber = HttpRequestUtils.sendPost(serviceNumUrl, evJson.toString(),
                    new String[] {"application/json;charset=utf-8" });
            JSONObject jsonN = JSONObject.parseObject(getServiceNumber);
            String content = jsonN.getString("content");
            JSONArray jsonArr = JSONArray.parseArray(content);
            json.put("serviceNumber", serviceNumber);
            json.put("serviceTime", acceptdate);
            json.put("projectType", auditProject.getTasktype());
            if (3 == Integer.parseInt(proStatus)) {
                json.put("resultDate",
                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
            }
            json.put("tasktype", auditProject.getTasktype());
            json.put("mobile", auditProject.getContactmobile());
            json.put("deptCode", deptcode);
            json.put("projectName", "关于" + auditProject.getApplyername() + auditTask.getTaskname() + "的业务");
            json.put("creditNum", auditProject.getCertnum());
            // 默认证照类型为身份证
            json.put("creditType", "111");
            json.put("promiseDay", auditTask.getPromise_day() + "");
            // 默认办结时间单位为工作日
            json.put("anticipateDay", "1");
            // 线上评价为1
            json.put("proChannel", "2");
            json.put("promiseTime", auditTask.getType() + "");
            log.info("办件数据加密前入参：" + json.toString());
            JSONObject submit = new JSONObject();
            submit.put("params", json);
            String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject result = JSONObject.parseObject(resultsign);
                if ("success".equals(result.getString("custom"))) {
                    log.info("保存办件服务数据成功:" + auditProject.getFlowsn());
                }
                else {
                    log.info("保存办件服务数据失败:" + auditProject.getFlowsn());
                }
            }
            else {

                log.info("保存办件服务数据失败：" + auditProject.getFlowsn());
            }
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }

    }

    public void evaluate(String assessNumber, String windowguid, AuditProject auditProject) {
        // rabbitmq推送
        log.info("扫码evaluate+++++windowguid" + windowguid);
        String Macaddress = equipmentservice
                .getMacaddressbyWindowGuidAndType(windowguid, QueueConstant.EQUIPMENT_TYPE_PJPAD).getResult();
        log.info("扫码evaluate+++++Macaddress" + Macaddress);
        log.info("扫码evaluate+++++Areacode" + auditProject.getAreacode());
        if ("370830".equals(auditProject.getAreacode()) || "370831".equals(auditProject.getAreacode())
                || "370891".equals(auditProject.getAreacode()) || "370890".equals(auditProject.getAreacode())
                || "370832".equals(auditProject.getAreacode()) || "370826".equals(auditProject.getAreacode())
                || "370800".equals(auditProject.getAreacode()) || "370828".equals(auditProject.getAreacode())
                || "370883".equals(auditProject.getAreacode())) {
            String proStatus = "";
            if (StringUtil.isNotBlank(Macaddress)) {
                try {
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("status", "100");
                    if ("370883".equals(auditProject.getAreacode())) {
                        dataJson.put("url",
                                "http://112.6.110.176:28080/jnzwfw/jiningzwfw/pages/zoucchengevaluate/step1?projectNo="
                                        + auditProject.getFlowsn() + "&areacode=" + auditProject.getAreacode()
                                        + "&proStatus=" + proStatus + "&assessNumber=" + assessNumber + "&iszj=1");
                    }
                    else {
                        dataJson.put("url",
                                "http://112.6.110.176:28080/jnzwfw/jiningzwfw/pages/evaluate/step1?projectNo="
                                        + auditProject.getFlowsn() + "&areacode=" + auditProject.getAreacode()
                                        + "&proStatus=" + proStatus + "&assessNumber=" + assessNumber + "&iszj=1");
                    }
                    ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        else {
            // 先判断是否已评价
            if (!evaluateservice.isExistEvaluate(auditProject.getRowguid()).getResult()) {
                try {
                    if (StringUtil.isNotBlank(Macaddress)) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("status", QueueConstant.Evaluate_Status_Evaluate);
                        dataJson.put("clientidentifier", auditProject.getRowguid());
                        dataJson.put("clienttype", ZwfwConstant.Evaluate_clienttype_Project);
                        ProducerMQ.send(this.getAppMQQueue(Macaddress), dataJson.toString());
                    }
                }
                catch (Exception e) {

                    e.printStackTrace();
                }
            }
            else {

            }
        }

    }

    public String getAppMQQueue(String Macaddress) {

        return "mqtt-subscription-" + Macaddress + "qos1";
    }
}
