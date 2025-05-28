package com.epoint.auditqueue.auditqueuerest.pad;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditqueue.auditpadconsult.domain.AuditZnsbConsult;
import com.epoint.basic.auditqueue.auditpadconsult.inter.IAuditZnsbConsult;
import com.epoint.basic.auditqueue.auditpadtimedcount.domain.AuditZnsbTimedcount;
import com.epoint.basic.auditqueue.auditpadtimedcount.inter.IAuditZnsbTimedcount;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbappconfig.domain.AuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbappconfig.inter.IAuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbterminalapp.domain.AuditZnsbTerminalApp;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueCommonUtil;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.composite.auditqueue.handletoolbar.inter.IHandleToolBar;
import com.epoint.composite.auditqueue.handletoolbar.inter.IJXHandleToolBar;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.MDUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import com.epoint.frame.spring.util.SpringContextUtil;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/jxqueuePad")
public class JXQueuePadRestController
{

    @Autowired
    private IUserService userservice;

    @Autowired
    private IAuditZnsbConsult consultService;
    @Autowired
    private IAuditOrgaServiceCenter auditOrgaServiceCenter;

    @Autowired
    private IAuditZnsbTimedcount timedcountservice;

    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditQueueTasktype tasktypeservice;

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
    private IJXHandleToolBar jxhandletoolbarservice;

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
    private IAuditZnsbAppConfig appConfigService;


    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * pad登录
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
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
            FrameUser user = userservice.getUserByUserField("LoginID", loginid);
            if (user != null) {

                // 匹配密码
                if (user.getPassword().equals(MDUtil.authPassword(password))) {

                    dataJson.put("userguid", user.getUserGuid());
                    // 该用户配置多个窗口，需要padMac跟窗口先进行绑定
                    if (windowservice.getWindowCountByUserGuid(user.getUserGuid()).getResult() > 1) {
                        windowguid = windowservice.getWindowByMacandUserGuid(macaddress, user.getUserGuid())
                                .getResult();
                        if (StringUtil.isBlank(windowguid)) {
                            return JsonUtils.zwdtRestReturn("0", "由于您的账户配置了多个窗口，且该pad的机器码未绑定窗口或者绑定的窗口不是您账户配置的窗口，无法登陆！",
                                    "");
                        }
                    }
                    else {
                        // 取出人员对应窗口guid
                        AuditOrgaWindow auditwindow = windowservice.getWindowByUserGuid(user.getUserGuid()).getResult();
                        if (auditwindow != null) {

                            windowguid = auditwindow.getRowguid();
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "该用户未配置窗口！", "");
                        }
                    }

                    dataJson.put("windowguid", windowguid);
                    // 存入窗口redis缓存,默认为暂停状态
                    handletoolbarservice.initQueueLogin(windowguid, user.getUserGuid(), user.getDisplayName(),
                            QueueConstant.Window_WorkStatus_Pause);

                    // 评价pad推送
                    handlemqservice.sendMQLoginbyEvaluate(windowguid);
                    // 在线
                    handlemqservice.sendMQOnLinebyEvaluate(windowguid);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "您输入的密码不正确！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该用户不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 用户详情
     * 
     * @params params
     * @return
     * @throws IOException
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/userDetail", method = RequestMethod.POST)
    public String userDetail(@RequestBody String params, HttpServletRequest request) throws IOException {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            JSONObject dataJson = new JSONObject();

            FrameUser user = userservice.getUserByUserField("UserGuid", userguid);
            if (user != null) {

                FrameOu ou = ouservice.getOuByOuGuid(user.getOuGuid());
                if (ou != null) {

                    FrameUserExtendInfo userextendinfo = userservice.getUserExtendInfoByUserGuid(userguid);

                    dataJson.put("ouname",
                            StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                    dataJson.put("displayname", user.getDisplayName());

                    dataJson.put("photourl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/rest/auditattach/getUserPic?userguid=" + userguid);

                    // 取出人员对应窗口guid
                    AuditOrgaWindow auditwindow = windowservice.getWindowByWindowGuid(windowguid).getResult();
                    if (auditwindow != null) {
                        AuditOrgaServiceCenter auditCenter = orgacenterservice
                                .findAuditServiceCenterByGuid(auditwindow.getCenterguid()).getResult();
                        if (StringUtil.isNotBlank(auditCenter)) {
                            dataJson.put("areacode", auditCenter.getBelongxiaqu());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "未分配辖区！", "");
                        }
                        dataJson.put("windowguid", auditwindow.getRowguid());
                        dataJson.put("windowno", auditwindow.getWindowno());
                        dataJson.put("hallguid", auditwindow.getLobbytype());
                        dataJson.put("centerguid", auditwindow.getCenterguid());

                        dataJson.put("banlinum", StringUtil.getNotNullString(
                                queueservice.getBanLiNumByWindowGuid(auditwindow.getRowguid()).getResult()));

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
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 开始叫号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/startQueue", method = RequestMethod.POST)
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

            // 取出办理号
            qno = handletoolbarservice.startQueue(windowguid, windowno, centerguid, userguid, true).getResult();

            dataJson.put("handleno", qno);
            dataJson.put("waitnum",
                    StringUtil.getNotNullString(queueservice.getWindowWaitNum(windowguid, true).getResult()));
            dataJson.put("banlinum",
                    StringUtil.getNotNullString(queueservice.getBanLiNumByWindowGuid(windowguid).getResult()));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 暂停叫号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/stopQueue", method = RequestMethod.POST)
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
            handletoolbarservice.pauseQueue(windowguid, windowno, centerguid);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 下一位
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/nextQueue", method = RequestMethod.POST)
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

            String nextqno = jxhandletoolbarservice.nextQueue(qno, windowguid, windowno, centerguid, userguid, true)
                    .getResult();
            dataJson.put("handleno", nextqno);
            dataJson.put("waitnum",
                    StringUtil.getNotNullString(queueservice.getWindowWaitNum(windowguid, true).getResult()));
            dataJson.put("banlinum",
                    StringUtil.getNotNullString(queueservice.getBanLiNumByWindowGuid(windowguid).getResult()));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 过号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/passQueue", method = RequestMethod.POST)
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
            String nextqno = handletoolbarservice.passQueue(qno, windowguid, windowno, centerguid, userguid, true)
                    .getResult();
            dataJson.put("handleno", nextqno);
            dataJson.put("waitnum",
                    StringUtil.getNotNullString(queueservice.getWindowWaitNum(windowguid, true).getResult()));
            dataJson.put("banlinum",
                    StringUtil.getNotNullString(queueservice.getBanLiNumByWindowGuid(windowguid).getResult()));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 重新呼叫
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/reCallQueue", method = RequestMethod.POST)
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
            // 发送语音短信
            handletoolbarservice.reCallQueue(qno, windowguid, windowno, centerguid);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 读卡
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/readCardQueue", method = RequestMethod.POST)
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
                    if (StringUtil.isNotBlank(QueueInfo.getIdentitycardnum())) {
                        if (!sfz.equals(QueueInfo.getIdentitycardnum())) {
                            return JsonUtils.zwdtRestReturn("0", "排队号与身份证不匹配！", "");
                        }

                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份证不能为空！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 评价
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/pingJiaQueue", method = RequestMethod.POST)
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
                // 先判断是否已评价
                if (!evaluateservice.isExistEvaluate(queue.getRowguid()).getResult()) {
                    // rabbitmq推送
                    handlemqservice.sendMQEvaluatebyEvaluate(windowguid, queue.getRowguid(),
                            ZwfwConstant.Evaluate_clienttype_QNO);
                }
                else {

                    return JsonUtils.zwdtRestReturn("0", "该排队号已评价，请勿重复评价！", "");
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 自动刷新
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/autoRefreshQueue", method = RequestMethod.POST)
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

            String nextqno;
            JSONObject dataJson = new JSONObject();
            // 判断当前窗口状态是否为空闲，并且当前无办理人员
            AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();

            if (queuewindow != null && QueueConstant.Window_WorkStatus_Free.equals(queuewindow.getWorkstatus())) {
                if ((StringUtil.isBlank(qno) || QueueConstant.Window_Bar_status_None.equals(qno))
                        && StringUtil.isNotBlank(queuewindow.getWaitnum())
                        && Integer.parseInt(queuewindow.getWaitnum()) > 0) {
                    // 取出下一个号
                    nextqno = handletoolbarservice.getNextQNO(windowguid, windowno, centerguid, userguid, true)
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
            dataJson.put("waitnum", handletoolbarservice.getWindowWaitNumAuto(windowguid, waitnum).getResult());

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 注销
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/zhuXiaoQueue", method = RequestMethod.POST)
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

            handletoolbarservice.zhuXiaoQueue(windowguid, windowno, centerguid);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 条形码扫描
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/scanQueue", method = RequestMethod.POST)
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

            // 根据扫描的条形码找到对应的办件guid
            String fields = "  rowguid,taskguid,projectname,pviguid,status ";
            AuditProject project = projectservice.getAuditProjectByFlowsn(fields, code, areacode).getResult();
            if (StringUtil.isBlank(code)) {
                return JsonUtils.zwdtRestReturn("0", "流水号不能为空！", "");
            }
            AuditZnsbConsult consult = consultService.getDetailByFlowsn(code).getResult();
            if (StringUtil.isBlank(project) && StringUtil.isBlank(consult)) {
                if (StringUtil.isBlank(qno) || "无办理人员！".equals(qno)) {
                    return JsonUtils.zwdtRestReturn("0", "当前无办理人员，无法受理！", "");
                }
                else {
                    String fieldstr = " Projectguid ";
                    AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
                    if (StringUtil.isNotBlank(queue)) {
                        if (StringUtil.isBlank(queue.getProjectguid())) {
                            // 显示受理界面
                            dataJson.put("codestatus", "1");
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "该排队号已受理，请勿重复受理！", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
                    }
                }
            }
            else {
                // 判断该办件的状态

                if (StringUtil.isNotBlank(project)&&project.getStatus() == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                    return JsonUtils.zwdtRestReturn("0", "该办件已办结，请勿重复办结！", "");
                }
                else if(StringUtil.isNotBlank(consult)){
                    return JsonUtils.zwdtRestReturn("0", "该取号已经咨询过！", "");
                }else{
                 // 显示办件界面
                    dataJson.put("codestatus", "2");
                }

            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 暂停计时
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/startTiming", method = RequestMethod.POST)
    public String timedCountStart(@RequestBody String params) {
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
            String rowguid = UUID.randomUUID().toString();
            AuditZnsbTimedcount record = new AuditZnsbTimedcount();
            record.setRowguid(rowguid);
            record.setStarttime(new Date());
            record.setUserguid(userguid);
            record.setWindowguid(windowguid);
            record.setWindowno(windowno);
            record.setMacaddress(macaddress);
            record.setCenterguid(centerguid);
            String msg = timedcountservice.insert(record).getResult();
            if ("success".equals(msg)) {
                dataJson.put("rowguid", rowguid);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "开始计时失败", "");
            }
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 暂停计时
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/endTiming", method = RequestMethod.POST)
    public String timedCountEnd(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String rowguid = obj.getString("rowguid");
            if (StringUtil.isNotBlank(rowguid)) {
                JSONObject dataJson = new JSONObject();
                AuditZnsbTimedcount record = timedcountservice.getDetailByRowguid(rowguid).getResult();
                if (StringUtil.isNotBlank(record)) {
                    record.setEndtime(new Date());
                    timedcountservice.update(record);
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "参数rowguid对应记录不存在", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "参数不能为空", "");
            }
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 受理
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/shouLiQueue", method = RequestMethod.POST)
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
            String fieldstr = " Taskguid ";
            AuditQueue queue = queueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            if (StringUtil.isNotBlank(queue)) {

                AuditProject project = new AuditProject();
                project.setOperateusername(username);
                project.setOperatedate(new Date());
                project.setRowguid(projectguid);
                project.setWindowguid(windowguid);
                project.setWindowname(windowname);

                List<String> taskidlist = tasktypetaskservice.getTaskIDbyTaskTypeGuid(queue.getTaskguid());
                if (taskidlist != null && taskidlist.size() > 0) {

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

                        Date shouldEndDate = null;// 承诺办结日期
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

                        // 4、添加办件剩余时间

                        sparetimeService.addSpareTimeByProjectGuid(projectguid, ts, areacode, centerguid);

                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项分类未关联事项！", "");
                }
                project.setCenterguid(centerguid);
                project.setApplyertype(Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));// 申请人类型：个人
                project.setApplydate(new Date());// 办件申请时间
                project.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ));// 办件申请方式：窗口申请
                project.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
                project.setIs_test(Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
                project.setIs_delay(20);// 是否延期
                // 人员信息
                if (StringUtil.isNotBlank(queue.getIdentitycardnum())) {
                    Record userinfo = userinfoservice.getUserByIdentityCardNum(queue.getIdentitycardnum()).getResult();
                    if (userinfo != null) {
                        project.setApplyeruserguid(StringUtil.getNotNullString(userinfo.get("UserGuid")));
                        project.setApplyername(StringUtil.getNotNullString(userinfo.get("DisplayName")));
                        project.setContactmobile(StringUtil.getNotNullString(userinfo.get("Mobile")));
                        project.setAddress(StringUtil.getNotNullString(userinfo.get("address")));
                    }

                }
                project.setFlowsn(code);

                project.setReceiveuserguid(userguid);
                project.setReceiveusername(username);
                project.setReceivedate(new Date());
                project.setAcceptuserguid(userguid);
                project.setAcceptusername(username);
                project.setAcceptuserdate(new Date());

                project.setStatus(ZwfwConstant.BANJIAN_STATUS_YSL);

                projectservice.addProject(project);
                queueservice.updateQNOProject(projectguid, qno, centerguid);

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 咨询
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/consultQueue", method = RequestMethod.POST)
    public String scanConsult(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String userguid = obj.getString("userguid");
            String windowguid = obj.getString("windowguid");
            String centerguid = obj.getString("centerguid");
            String areacode = obj.getString("areacode");
            String code = obj.getString("code");
            String qno = obj.getString("qno");

            String username = userservice.getUserByUserField("userguid", userguid).getDisplayName();
            JSONObject dataJson = new JSONObject();
            AuditZnsbConsult consult=consultService.getDetailByFlowsn(code).getResult();
            if(StringUtil.isNotBlank(consult)){
                return JsonUtils.zwdtRestReturn("0", "该取号已咨询过！", "");
            }
            AuditZnsbConsult record = new AuditZnsbConsult();
            AuditQueue QueueInfo = queueservice
                    .getQNODetailByQNO(" centername,identitycardnum,taskguid ", qno, centerguid).getResult();
            if (QueueInfo != null) {
                AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(QueueInfo.getTaskguid()).getResult();
                if (StringUtil.isNotBlank(tasktype)) {
                    record.setUserguid(userguid);
                    record.setUsername(username);
                    record.setTasktypeguid(tasktype.getRowguid());
                    record.setTasktypename(tasktype.getTasktypename());
                    record.setCenterguid(centerguid);
                    record.setCentername(QueueInfo.getCentername());
                    record.setFlowsn(code);
                    if (StringUtil.isNotBlank(QueueInfo.getIdentitycardnum())) {
                        AuditQueueUserinfo auditQueueUserinfo = (AuditQueueUserinfo) userinfoservice
                                .getUserByIdentityCardNum(QueueInfo.getIdentitycardnum()).getResult();
                        if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                            record.setTaskusername(auditQueueUserinfo.getDisplayname());
                            record.setPhone(auditQueueUserinfo.getMobile());
                            record.setIdcard(QueueInfo.getIdentitycardnum());
                        }
                    }
                    record.setConsulttime(new Date());
                    record.setRowguid(UUID.randomUUID().toString());
                    consultService.insert(record);

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该取号事项不存在！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该排队号不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 办结
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/banJieQueue", method = RequestMethod.POST)
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

            String fields = "  rowguid,taskguid,projectname,pviguid,status ";
            AuditProject project = projectservice.getAuditProjectByFlowsn(fields, code, areacode).getResult();

            if (StringUtil.isNotBlank(project)) {

                project.setBanjieresult(Integer.valueOf(banjiestatus));
                project.setBanwandate(new Date());

                AuditProjectSparetime auditprojectspare = sparetimeService
                        .getSparetimeByProjectGuid(project.getRowguid()).getResult();
                int spendtime = auditprojectspare.getSpendminutes();// 已用时间
                int sparetime = auditprojectspare.getSpareminutes();// 剩余的时间
                project.setStatus(ZwfwConstant.BANJIAN_STATUS_ZCBJ);// 办件状态：正常办结
                project.setBanjiedate(new Date());
                project.setBanjieuserguid(userguid);
                project.setBanjieusername(username);
                project.setSparetime(sparetime);
                project.setSpendtime(spendtime);
                project.setIs_guidang(ZwfwConstant.CONSTANT_INT_ONE);// 办件归档
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
                // 删除剩余时间表记录
                sparetimeService.deleteSpareTimeByRowGuid(auditprojectspare.getRowguid());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * app更新
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/updateApp", method = RequestMethod.POST)
    public String updateApp(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String macaddress = obj.getString("macaddress");
            String info = obj.getString("info");

            String apppath = "";
            String centerguid = "";
            String macrowguid = "";
            JSONObject dataJson = new JSONObject();

            // 根据macaddress获取中心guid
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress, "centerguid,rowguid")
                    .getResult();
            centerguid = equipment.getCenterguid();
            macrowguid = equipment.getRowguid();
            String fieldstr1 = "centerguid,macrowguid,appguid,equipmentname";
            AuditZnsbAppConfig appConfig = appConfigService.getConfigbyMacrowguid(macrowguid,fieldstr1).getResult();

            // 判断该设备是否使用个性化的APP
            if (StringUtil.isNotBlank(centerguid)) {
                // 设备与APP关系表里是否存在
                if (StringUtil.isNotBlank(appConfig)) {
                    String fieldstr2 = "AppInfo,UpdateTime,AppType,CenterGuid,CenterName,AppAttachGuid,IsUniversal";
                    AuditZnsbTerminalApp app = appservice.getAppByGuid(appConfig.getAppguid(),fieldstr2).getResult();
                    if (StringUtil.isNotBlank(app.getAppattachguid())) {
                        if (Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))) {

                            List<FrameAttachInfo> attachinfolist = attachservice
                                    .getAttachInfoListByGuid(app.getAppattachguid());
                            if (attachinfolist != null && attachinfolist.size() > 0) {
                                apppath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid="
                                        + attachinfolist.get(0).getAttachGuid();
                            }
                        }
                    }

                } else {
                    AuditZnsbTerminalApp app = appservice
                            .getDetailbyApptype(QueueConstant.TerminalApp_Type_QueuePad, centerguid).getResult();
                    if (StringUtil.isNotBlank(app)) {
                        if (Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))) {

                            if (StringUtil.isNotBlank(app.getAppattachguid())) {
                                List<FrameAttachInfo> attachinfolist = attachservice
                                        .getAttachInfoListByGuid(app.getAppattachguid());
                                if (attachinfolist != null && attachinfolist.size() > 0) {
                                    apppath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                            + "/rest/auditattach/readAttach?attachguid="
                                            + attachinfolist.get(0).getAttachGuid();
                                }

                            }
                        }

                    }
                }
            }

            dataJson.put("apppath", apppath);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 初始化
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_JHPAD);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    String config = handleConfigservice.getFrameConfig("AS_IS_ENABLE_APPLOG", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("recordlog", config);
                    }
                    else {
                        // 默认不记录日志
                        dataJson.put("recordlog", "0");
                    }

                    if (QueueConstant.Common_yes_String
                            .equals(configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE"))) {
                        dataJson.put("epointdevice", "1");
                    }
                    else {
                        // 默认不启用硬件管理平台
                        dataJson.put("epointdevice", "0");
                    }
                    config = handleConfigservice.getFrameConfig("AS_RabbitMQ", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("rabbitmq", config);
                        CachingConnectionFactory conn = null;
                        if (SpringContextUtil.getApplicationContext().containsBean("connectionFactory")) {
                            conn = SpringContextUtil.getBean("connectionFactory");
                            dataJson.put("rabbitmqusername", conn.getRabbitConnectionFactory().getUsername());
                            dataJson.put("rabbitmqpassword", conn.getRabbitConnectionFactory().getPassword());
                        }

                    }
                    // 首页地址
                    if (StringUtil.isNotBlank(equipment.getUrl())) {
                        dataJson.put("homepageurl", equipment.getUrl());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "首页地址未配置！", "");
                    }

                    // 判断是否启用
                    if (QueueConstant.EQUIPMENT_STATUS_OFFLINE.equals(equipment.getStatus())) {
                        return JsonUtils.zwdtRestReturn("0", "设备已离线！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
