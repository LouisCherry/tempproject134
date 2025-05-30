package com.epoint.wsxznsb.wechat.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.inter.IAuditQueueYuyueTime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController
@RequestMapping("/wsxqueueAppointment")
public class WsxQueueAppointmentRestController
{

    /**
     * 事项分类api
     */
    @Autowired
    private IAuditQueueTasktypeTask iAuditQueueTasktypeTask;

    /**
     * 工作日API
     */
    @Autowired
    private IAuditOrgaWorkingDay iAuditOrgaWorkingDay;

    /**
     * 预约时间API
     */
    @Autowired
    private IAuditQueueYuyueTime iAuditQueueYuyueTime;

    /**
     * 预约API
     */
    @Autowired
    private IAuditQueueAppointment iAuditQueueAppointment;

    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 事项分类API
     */
    @Autowired
    private IAuditQueueTasktype iAuditQueueTasktype;

    /**
     * 网上办事大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 部门API
     */
    @Autowired
    private IOuService iOuService;

    /**
     * 中心管理API
     */
    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;

    /**
     * 系统参数API
     */
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IMessagesCenterService imessageservice;
    @Autowired
    private IConfigService configService;
    @Autowired
    private IAuditQueueUserinfo userInfoService;
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取预约日期及相关日期可预约时间段
     * 
     * @params params 请求参数
     * @return
     */
    @RequestMapping(value = "/getAppointDateandTime", method = RequestMethod.POST)
    public String getAppointDateandTime(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppointDateandTime接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 显示可向后可预约的天数，默认7天
            String showdays = obj.getString("showdays");
            // 中心Guid
            String centerguid = obj.getString("centerguid");
            // 事项GUID
            String taskguid = obj.getString("taskguid");
            // 事项分类GUID
            String tasktypeguid = obj.getString("tasktypeguid");

            // 用户guid
            String accountGuid = obj.getString("accountguid");// 用户guid
            List<JSONObject> listdate = new ArrayList<JSONObject>();
            if (StringUtil.isBlank(accountGuid)) {
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // log.info("auditOnlineRegister" + auditOnlineRegister);
                accountGuid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
            }

            // 返回当前日期向后可预约的7天日期信息
            String appointdate = "";
            Date nowdate = new Date();
            for (int i = 1; i <= Integer.parseInt(showdays); i++) {
                appointdate = EpointDateUtil.convertDate2String(
                        iAuditOrgaWorkingDay.getWorkingDayWithOfficeSet(centerguid, nowdate, i).getResult(),
                        "yyyy-MM-dd");
                if (StringUtil.isNotBlank(accountGuid)) {
                    // 判断预约日期是否小于当前日期
                    if (EpointDateUtil.compareDateOnDay(nowdate,
                            EpointDateUtil.convertString2DateAuto(appointdate)) < 0) {
                        if (iAuditOrgaWorkingDay
                                .isWorkingDay(centerguid, EpointDateUtil.convertString2DateAuto(appointdate))
                                .getResult()) {
                            if (StringUtil.isBlank(tasktypeguid)) {
                                AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                                if (task != null) {
                                    // 根据事项id跟中心guid获取事项类别
                                    tasktypeguid = iAuditQueueTasktypeTask
                                            .getTaskTypeguidbyTaskIDandCenterGuid(task.getTask_id(), centerguid)
                                            .getResult();
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                                }
                            }

                            // 事项关联到类别后才可以获取其对应的预约信息
                            if (StringUtil.isNotBlank(tasktypeguid)) {
                                // 判断是否有个性化预约时间
                                List<AuditQueueYuyuetime> yuyuetimelist = iAuditQueueYuyueTime
                                        .getYuyuetimeList(tasktypeguid, "1", centerguid).getResult();
                                // 如果不存在个性化预约，获取全局预约时间信息
                                if ((yuyuetimelist == null) || yuyuetimelist.isEmpty()) {
                                    yuyuetimelist = iAuditQueueYuyueTime.getYuyuetimeList("", "0", centerguid)
                                            .getResult();
                                }

                                List<JSONObject> listtime = new ArrayList<JSONObject>();
                                // 返回预约时间信息，包括开始结束时间，和最大预约数量、当前预约数量
                                if ((yuyuetimelist != null) && !yuyuetimelist.isEmpty()) {
                                    for (AuditQueueYuyuetime yuyuetime : yuyuetimelist) {
                                        JSONObject timeJson = new JSONObject();
                                        // 开始时间
                                        timeJson.put("appointtimestart", yuyuetime.getYuyuetimestart());
                                        // 结束时间
                                        timeJson.put("appointtimeend", yuyuetime.getYuyuetimeend());
                                        // 最大预约数量
                                        timeJson.put("appointmaxsum",
                                                StringUtil.getNotNullString(yuyuetime.getYuyuesum()));
                                        // 预约总数
                                        timeJson.put("appointsum", StringUtil.getNotNullString(iAuditQueueAppointment
                                                .getAppointCountByFwTime(tasktypeguid,
                                                        EpointDateUtil.convertString2DateAuto(
                                                                appointdate + " " + yuyuetime.getYuyuetimestart()),
                                                        EpointDateUtil.convertString2DateAuto(
                                                                appointdate + " " + yuyuetime.getYuyuetimeend()))
                                                .getResult()));
                                        // 当前用户当前时间段内是否已经预约
                                        String appointguid = iAuditQueueAppointment
                                                .getAppointByFwTime(accountGuid, tasktypeguid,
                                                        EpointDateUtil.convertString2DateAuto(
                                                                appointdate + " " + yuyuetime.getYuyuetimestart()),
                                                        EpointDateUtil.convertString2DateAuto(
                                                                appointdate + " " + yuyuetime.getYuyuetimeend()))
                                                .getResult();
                                        // 已预约后设置isappoint字段为1
                                        if (StringUtil.isNotBlank(appointguid)) {
                                            timeJson.put("isappoint", "1");
                                            timeJson.put("appointguid", appointguid);
                                        }
                                        else {
                                            timeJson.put("isappoint", "0");
                                            timeJson.put("appointguid", "");
                                        }
                                        listtime.add(timeJson);
                                    }
                                    JSONObject dateJson = new JSONObject();
                                    dateJson.put("appointdate", appointdate);
                                    // 获取当前日期是周几
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(EpointDateUtil.convertString2DateAuto(appointdate));
                                    dateJson.put("week", EpointDateUtil
                                            .getCHSWeekNameByDayOfWeek(cal.get(Calendar.DAY_OF_WEEK) - 1));
                                    dateJson.put("appointtimelist", listtime);
                                    listdate.add(dateJson);
                                }
                                else {
                                    return JsonUtils.zwdtRestReturn("0", "该中心未配置预约时间段！", "");
                                }

                            }
                            else {
                                return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别无法预约！", "");
                            }
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "该时间为非工作日，不可预约！", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "不能预约以前的日期！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("1", "获取用户失败！", "");
                }
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("appointdatelist", listdate);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 预约取号
     * 
     * @params params 请求参数
     * @params request HTTP请求
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/private/getAppointQno", method = RequestMethod.POST)
    public String getAppointQno(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppointQno接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 中心guid
            String centerguid = obj.getString("centerguid");
            // 事项guid
            String taskguid = obj.getString("taskguid");
            // 事项分类GUID
            String tasktypeguid = obj.getString("tasktypeguid");
            String accountguid = obj.getString("accountguid");
            // 用户名
            String username = obj.getString("username");
            // 身份证号码
            String identitycardid = obj.getString("identitycardid");
            // 手机号
            String mobile = obj.getString("mobile");
            // 预约日期
            String appointdate = obj.getString("appointdate");
            // 预约开始时间
            String appointtimestart = obj.getString("appointtimestart");
            // 预约结束时间
            String appointtimeend = obj.getString("appointtimeend");
            // 预约来源，1代表网厅，2代表手机，3代表微信,4代表一体机
            String appointtype = obj.getString("appointtype");
            JSONObject dataJson = new JSONObject();

            String useBlackList = configService.getFrameConfigValue("AS_IS_USE_BLACKLIST");
            // 判断是否开启了黑名单功能
            if (StringUtil.isNotBlank(useBlackList) && QueueConstant.CONSTANT_STR_ONE.equals(useBlackList)) {
                // 判断用户是否在黑名单中
                AuditQueueUserinfo currentUser = userInfoService.getUserinfo(identitycardid).getResult();
                if (StringUtil.isNotBlank(currentUser) && StringUtil.isNotBlank(currentUser.getBlacklisttodate())) {
                    // 在黑名单中不能让该用户预约
                    return JsonUtils.zwdtRestReturn("0", "您在黑名单中，解禁时间为"
                            + EpointDateUtil.convertDate2String(currentUser.getBlacklisttodate(), "yyyy年MM月dd日，现在")
                            + "无法预约，如有疑问，请到中心进行申诉！", "");
                }
            }

            if (StringUtil.isBlank(accountguid)) {
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                accountguid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
            }
            if (StringUtil.isNotBlank(accountguid)) {
                if (StringUtil.isBlank(tasktypeguid)) {
                    AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                    if (task != null) {
                        // 根据事项id跟中心guid获取事项类别
                        tasktypeguid = iAuditQueueTasktypeTask
                                .getTaskTypeguidbyTaskIDandCenterGuid(task.getTask_id(), centerguid).getResult();
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                    }
                }

                if (StringUtil.isNotBlank(tasktypeguid)) {
                    // 判断该身份证是否存在该事项预约
                    if (iAuditQueueAppointment.boolAppointByCardID(identitycardid, tasktypeguid).getResult()) {
                        // 判断该事项分类下是否有多个事项，如果有且只有一个，则默认为事项预约，否则为事项类别预约。
                        if (iAuditQueueTasktypeTask.getCountByTaskTypeguid(tasktypeguid).getResult() == 1) {
                            return JsonUtils.zwdtRestReturn("0", "您已经预约过该事项，请不要重复预约！", "");
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "您已经预约过该类别的事项，请不要重复预约！", "");
                        }
                    }
                    // 获取事项该时间段内可预约的最大数量
                    String appointmaxsum = iAuditQueueYuyueTime
                            .getAppointSum(tasktypeguid, "1", centerguid, appointtimestart, appointtimeend).getResult();
                    if (StringUtil.isBlank(appointmaxsum)) {
                        // 无个性化时间段
                        appointmaxsum = iAuditQueueYuyueTime
                                .getAppointSum("", "0", centerguid, appointtimestart, appointtimeend).getResult();
                        if (StringUtil.isBlank(appointmaxsum)) {
                            return JsonUtils.zwdtRestReturn("0", "该时间段不能进行预约！", "");
                        }
                    }
                    // 事项该时间段内已预约的数量（默认为0）
                    Integer appointsum = 0;
                    appointsum = iAuditQueueAppointment
                            .getAppointCountByFwTime(tasktypeguid,
                                    EpointDateUtil.convertString2DateAuto(appointdate + " " + appointtimestart),
                                    EpointDateUtil.convertString2DateAuto(appointdate + " " + appointtimeend))
                            .getResult();
                    // 预约达到上限
                    if (Integer.parseInt(appointmaxsum) <= appointsum) {
                        return JsonUtils.zwdtRestReturn("0", "该事项在此时间段已经达到预约上限，请换个时间段！", "");
                    }
                    AuditQueueTasktype tasktype = iAuditQueueTasktype.getTasktypeByguid(tasktypeguid).getResult();
                    AuditQueueAppointment appoint = new AuditQueueAppointment();
                    String appointguid = UUID.randomUUID().toString();
                    appoint.setRowguid(appointguid);
                    appoint.setTaskguid(taskguid);
                    appoint.setTaskname(tasktype.getTasktypename());
                    appoint.setApplyuserguid(accountguid);
                    appoint.setAppointmenttype(appointtype);
                    appoint.setApptaskguid(tasktypeguid);
                    appoint.setApptaskname(tasktype.getTasktypename());
                    appoint.setCreatedate(new Date());
                    // 预约状态："0" 未取号 "1" 已取号 "2"; 过号 "3" 删除
                    appoint.setStatus(QueueConstant.Appoint_Status_Init);
                    // 预约开始时间
                    appoint.setAppointmentfromtime(
                            EpointDateUtil.convertString2DateAuto(appointdate + " " + appointtimestart));
                    // 预约结束时间
                    appoint.setAppointmenttotime(
                            EpointDateUtil.convertString2DateAuto(appointdate + " " + appointtimeend));
                    appoint.setPhone(mobile);
                    appoint.setDisplayname(username);
                    appoint.setIdentitycardnum(identitycardid);
                    appoint.setCenterguid(centerguid);
                    iAuditQueueAppointment.addAppoint(appoint);
                    dataJson.put("appointguid", appointguid);
                    // 发送短信
                    String messagecontent = "";
                    if (StringUtil.isNotBlank(mobile)) {
                        // 如果值为0，则不发短信
                        messagecontent = handleConfigservice.getFrameConfig("AS_QUEUE_MESSAGE_APQH", centerguid)
                                .getResult();
                        if (StringUtil.isNotBlank(messagecontent)) {
                            if (!QueueConstant.Common_no_String.equals(messagecontent)) {
                                messagecontent = messagecontent.replace("[#=TaskTypeName#]",
                                        tasktype.getTasktypename());
                                messagecontent = messagecontent.replace("[#=AppointmentFromTime#]",
                                        appointdate + " " + appointtimestart);
                                messagecontent = messagecontent.replace("[#=AppointmentToTime#]",
                                        appointdate + " " + appointtimeend);
                            }
                            else {
                                messagecontent = "";
                            }
                        }
                        else {
                            messagecontent = "您已成功预约事项：" + tasktype.getTasktypename() + "，预约时间为：" + appointdate + " "
                                    + appointtimestart + "~" + appointdate + " " + appointtimeend
                                    + "。请您留意预约时间，按时办理相关事项。";
                        }
                        if (StringUtil.isNotBlank(messagecontent)) {
                            imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent, new Date(),
                                    0, new Date(), mobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                    "");
                        }

                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别无法预约！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("1", "获取用户失败！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        // log.info("oAuthCheckTokenInfo=" + oAuthCheckTokenInfo);
        // log.info("Loginid=" + oAuthCheckTokenInfo.getLoginid());
        // 手机端
        // 通过登录名获取用户
        auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                .getResult();
        return auditOnlineRegister;
    }

    /**
     * 获取预约列表
     * 
     * @params params 请求参数
     * @return
     */
    @RequestMapping(value = "/private/getAppointList", method = RequestMethod.POST)
    public String getAppointList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppointList接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String accountguid = obj.getString("accountguid");
            String currentPage = obj.getString("currentpage");
            String taskname = obj.getString("taskname");
            String pageSize = obj.getString("pagesize");
            String type = obj.getString("type");// 1代表今日预约 2代表历史预约
            // 初始化session
            if (StringUtil.isBlank(accountguid)) {
                AuditOnlineRegister onlineRegister = null;
                OAuthCheckTokenInfo info = CheckTokenUtil.getCheckTokenInfo(request);
                String loginid = "";
                if (info != null) {
                    // 手机端
                    loginid = info.getLoginid();
                    // 通过登录名获取用户
                    onlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(loginid).getResult();
                    accountguid = onlineRegister.getAccountguid();
                }
                else {
                    // pc端
                    // accountguid =
                    // ZwdtUserSession.getInstance("").getAccountGuid();
                }
            }
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject appointJson = new JSONObject();
            AuditQueueTasktype tasktype = null;

            PageData<AuditQueueAppointment> pageData = iAuditQueueAppointment
                    .getAppointPageDataByType(type, taskname, accountguid,
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize))
                    .getResult();
            int totalcount = pageData.getRowCount();
            List<AuditQueueAppointment> appointList = pageData.getList();

            for (AuditQueueAppointment appoint : appointList) {
                appointJson = new JSONObject();
                appointJson.put("appointguid", appoint.getRowguid());
                appointJson.put("appointtaskname", appoint.getApptaskname());
                // 获取事项名称。外网大厅使用，不显示分类名称，显示事项名称
                appointJson.put("taskname", appoint.getTaskname());
                appointJson.put("taskguid", appoint.getApptaskguid());
                tasktype = iAuditQueueTasktype.getTasktypeByguid(appoint.getApptaskguid()).getResult();
                String ouname = "";
                if (tasktype != null) {
                    FrameOu frameOu = iOuService.getOuByOuGuid(tasktype.getOuguid());
                    if (frameOu != null) {
                        ouname = StringUtil.isNotBlank(frameOu.getOushortName()) ? frameOu.getOushortName()
                                : frameOu.getOuname();
                    }
                }
                appointJson.put("ouname", ouname);
                appointJson.put("creatdate",
                        EpointDateUtil.convertDate2String(appoint.getCreatedate(), "yyyy-MM-dd HH:mm:ss"));
                appointJson.put("appointtimestart",
                        EpointDateUtil.convertDate2String(appoint.getAppointmentfromtime(), "yyyy-MM-dd HH:mm"));
                appointJson.put("appointtimeend",
                        EpointDateUtil.convertDate2String(appoint.getAppointmenttotime(), "yyyy-MM-dd HH:mm"));

                appointJson.put("appointtime",
                        EpointDateUtil.convertDate2String(appoint.getAppointmentfromtime(), "yyyy-MM-dd HH:mm") + "~"
                                + EpointDateUtil.convertDate2String(appoint.getAppointmenttotime(), "HH:mm"));

                if (QueueConstant.Appoint_Status_Delete.equals(appoint.getStatus())) {
                    appointJson.put("status", QueueConstant.Appoint_Status_Delete);// 已取消
                    appointJson.put("queueguid", "");
                    appointJson.put("getnotime", "");
                }
                else if (QueueConstant.Appoint_Status_Process.equals(appoint.getStatus())) {
                    appointJson.put("status", QueueConstant.Appoint_Status_Process);// 已取号
                    appointJson.put("queueguid", appoint.getQueueguid());
                    appointJson.put("getnotime",
                            EpointDateUtil.convertDate2String(appoint.getGetnotime(), "yyyy-MM-dd HH:mm:ss"));
                }
                else {

                    if ("1".equals(type)) {
                        appointJson.put("status", QueueConstant.Appoint_Status_Init);// 未取号
                        appointJson.put("queueguid", "");
                        appointJson.put("getnotime", "");
                    }
                    else {
                        appointJson.put("status", QueueConstant.Appoint_Status_Pass);// 已过号
                        appointJson.put("queueguid", "");
                        appointJson.put("getnotime", "");
                    }
                }

                AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                        .findAuditServiceCenterByGuid(appoint.getCenterguid()).getResult();
                if (center != null) {
                    appointJson.put("centername", center.getCentername());
                }
                list.add(appointJson);
            }
            dataJson.put("appointdatelist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取预约详情
     * 
     * @params params 请求参数
     * @return
     */
    @RequestMapping(value = "/getAppointDetail", method = RequestMethod.POST)
    public String getAppointDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getAppointDetail接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String appointguid = obj.getString("appointguid");
            AuditQueueTasktype tasktype = null;
            AuditQueueAppointment appoint = iAuditQueueAppointment.getDetailByRowGuid(appointguid).getResult();
            if (StringUtil.isNotBlank(appoint)) {
                // 个性化：增加身份证、姓名、手机号
                dataJson.put("sfz", appoint.getIdentitycardnum());
                dataJson.put("name", appoint.getDisplayname());
                dataJson.put("phone", appoint.getPhone());
                dataJson.put("appointguid", appoint.getRowguid());
                dataJson.put("appointtaskname", appoint.getApptaskname());
                dataJson.put("taskguid", appoint.getApptaskguid());
                tasktype = iAuditQueueTasktype.getTasktypeByguid(appoint.getApptaskguid()).getResult();
                dataJson.put("ouname",
                        StringUtil.isNotBlank(iOuService.getOuByOuGuid(tasktype.getOuguid()).getOushortName())
                                ? iOuService.getOuByOuGuid(tasktype.getOuguid()).getOushortName()
                                : iOuService.getOuByOuGuid(tasktype.getOuguid()).getOuname());
                dataJson.put("creatdate",
                        EpointDateUtil.convertDate2String(appoint.getCreatedate(), "yyyy-MM-dd HH:mm:ss"));
                dataJson.put("appointtimestart",
                        EpointDateUtil.convertDate2String(appoint.getAppointmentfromtime(), "yyyy-MM-dd HH:mm:ss"));
                dataJson.put("appointtimeend",
                        EpointDateUtil.convertDate2String(appoint.getAppointmenttotime(), "yyyy-MM-dd HH:mm:ss"));
                if (QueueConstant.Appoint_Status_Delete.equals(appoint.getStatus())) {
                    dataJson.put("status", QueueConstant.Appoint_Status_Delete);// 已取消
                    dataJson.put("queueguid", "");
                    dataJson.put("getnotime", "");
                }
                else if (QueueConstant.Appoint_Status_Process.equals(appoint.getStatus())) {
                    dataJson.put("status", QueueConstant.Appoint_Status_Process);// 已取号
                    dataJson.put("queueguid", appoint.getQueueguid());
                    dataJson.put("getnotime",
                            EpointDateUtil.convertDate2String(appoint.getGetnotime(), "yyyy-MM-dd HH:mm:ss"));
                }
                else {
                    if (EpointDateUtil.compareDateOnDay(new Date(), appoint.getAppointmenttotime()) < 0) {
                        dataJson.put("status", QueueConstant.Appoint_Status_Init);// 未取号
                        dataJson.put("queueguid", "");
                        dataJson.put("getnotime", "");
                    }
                    else {
                        dataJson.put("status", QueueConstant.Appoint_Status_Pass);// 已过号
                        dataJson.put("queueguid", "");
                        dataJson.put("getnotime", "");
                    }
                }
                AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                        .findAuditServiceCenterByGuid(appoint.getCenterguid()).getResult();
                dataJson.put("centername", center.getCentername());
                dataJson.put("address", center.getAddress());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该预约不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
