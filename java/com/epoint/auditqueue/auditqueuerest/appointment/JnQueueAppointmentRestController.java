package com.epoint.auditqueue.auditqueuerest.appointment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditqueue.auditqueuerest.appointment.api.ICzQueueAppointment;
import com.epoint.auditqueue.auditqueuerest.servicecenterextension.api.IServicecenterExtensionService;
import com.epoint.auditqueue.auditqueuerest.servicecenterextension.api.entity.ServicecenterExtension;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditorga.auditworkingday.service.JNAuditOrgaWorkingDayService;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.inter.IAuditQueueYuyueTime;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jnqueueAppointment")
public class JnQueueAppointmentRestController {

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
     * 队列API
     */
    @Autowired
    private IAuditQueue iAuditQueue;

    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 事项扩展API
     */
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

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
     *
     */
    @Autowired
    private ICzQueueAppointment iCzQueueAppointment;
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IMessagesCenterService imessageservice;
    @Autowired
    private IAuditOrgaServiceCenter auditorgaserivce;
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userservice;
    @Autowired
    private IUserRoleRelationService roleRelationService;
    private JNAuditOrgaWorkingDayService OrgaWorkingDayService = new JNAuditOrgaWorkingDayService();

    @Autowired
    private IServicecenterExtensionService servicecenterExtensionService;

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取预约事项所属中心
     *
     * @return
     * @params params 请求参数
     */
    @RequestMapping(value = "/getAppointCenter", method = RequestMethod.POST)
    public String getAppointCenter(@RequestBody String params) {
        try {
            log.info("=======开始调用getAppointCenter接口=======");
            JSONObject json = JSON.parseObject(params);
            log.info("params:"+params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 办件GUID
            String taskguid = obj.getString("taskguid");
            List<JSONObject> list = new ArrayList<JSONObject>();
            // 获取事项信息
            AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {
                // 判断事项是否分配到相关排队分类
                List<String> TaskTypeGuidList = iAuditQueueTasktypeTask.getTaskTypeguidbyTaskID(task.getTask_id())
                        .getResult();
                // 如果存在排队分类则获取具体的预约信息
                if (TaskTypeGuidList != null && TaskTypeGuidList.size() > 0) {
                    for (String TaskTypeGuid : TaskTypeGuidList) {
                        JSONObject centerJson = new JSONObject();
                        AuditQueueTasktype tasktype = iAuditQueueTasktype.getTasktypeByguid(TaskTypeGuid).getResult();
                        // 如果事项允许预约，则返回对应的中心信息
                        if (tasktype!=null && QueueConstant.Common_yes_String.equals(tasktype.getIs_yuyue())) {
                            AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                                    .findAuditServiceCenterByGuid(tasktype.getCenterguid()).getResult();
                            if (center != null) {
                                centerJson.put("centerguid", tasktype.getCenterguid());
                                centerJson.put("centername", center.getCentername());
                                list.add(centerJson);
                            }
                        }
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别无法预约！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("centerlist", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }


    /**
     * 获取预约事项所属中心
     *
     * @return
     * @params params 请求参数
     */
    @RequestMapping(value = "/getCenterInfo", method = RequestMethod.POST)
    public String getCenterInfo(@RequestBody String params) {
        try {
            log.info("=======开始调用getCenterInfo接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 中心guid
            String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            ServicecenterExtension servicecenterExtension = servicecenterExtensionService.getInfoByCenterguid(centerguid);
            if (servicecenterExtension != null) {
                dataJson.put("address", servicecenterExtension.getAddress());
                dataJson.put("worktime", servicecenterExtension.getWorktime());
                dataJson.put("mobile", servicecenterExtension.getMobile());
                dataJson.put("centertitle", servicecenterExtension.getStr("centertitle"));
            }
            log.info("=======开始调用getCenterInfo接口=======");
            return JsonUtils.zwdtRestReturn("1", "中心信息获取成功！", dataJson.toJSONString());
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取预约日期
     *
     * @return
     * @params params 请求参数
     */
    @RequestMapping(value = "/getAppointDate", method = RequestMethod.POST)
    public String getAppointDate(@RequestBody String params) {
        try {
            log.info("=======开始调用getAppointDate接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 显示可向后可预约的天数，默认7天
            String showdays = obj.getString("showdays");
            // 中心Guid
            String centerguid = obj.getString("centerguid");
            List<JSONObject> list = new ArrayList<JSONObject>();
            // 返回当前日期向后可预约的7天日期信息
            for (int i = 1; i <= Integer.parseInt(showdays); i++) {
                JSONObject dateJson = new JSONObject();
                dateJson.put("appointdate", EpointDateUtil.convertDate2String(
                        OrgaWorkingDayService.getWorkingDayWithOfficeSet(centerguid, new Date(), i), "yyyy-MM-dd"));
                list.add(dateJson);
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("appointdatelist", list);
            log.info("=======结束调用getAppointDate接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            log.error("====Exception信息====" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取预约时间
     *
     * @return
     * @params params 请求参数
     */
    @RequestMapping(value = "/private/getAppointTime", method = RequestMethod.POST)
    public String getAppointTime(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppointTime接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 当前预约日期
            String appointdate = obj.getString("appointdate");
            // 中心GUID
            String centerguid = obj.getString("centerguid");
            // 事项GUID
            String taskguid = obj.getString("taskguid");
            // 用户guid
            String accountGuid = obj.getString("accountguid");// 用户guid

            List<JSONObject> list = new ArrayList<JSONObject>();
            if (StringUtil.isBlank(accountGuid)) {
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                accountGuid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
            }
            if (StringUtil.isNotBlank(accountGuid)) {
                // 判断预约日期是否小于当前日期
                if (EpointDateUtil.compareDateOnDay(new Date(),
                        EpointDateUtil.convertString2DateAuto(appointdate)) < 0) {
                    if (iAuditOrgaWorkingDay
                            .isWorkingDay(centerguid, EpointDateUtil.convertString2DateAuto(appointdate)).getResult()) {
                        AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                        if (task != null) {
                            // 根据事项id跟中心guid获取事项类别
                            String tasktypeguid = iAuditQueueTasktypeTask
                                    .getTaskTypeguidbyTaskIDandCenterGuid(task.getTask_id(), centerguid).getResult();
                            // 事项关联到类别后才可以获取其对应的预约信息
                            if (StringUtil.isNotBlank(tasktypeguid)) {
                                // 判断是否有个性化预约时间
                                List<AuditQueueYuyuetime> yuyuetimelist = iAuditQueueYuyueTime
                                        .getYuyuetimeList(tasktypeguid, "1", centerguid).getResult();
                                // 如果不存在个性化预约，获取全局预约时间信息
                                if ((yuyuetimelist == null) || yuyuetimelist.size() == 0) {
                                    yuyuetimelist = iAuditQueueYuyueTime.getYuyuetimeList("", "0", centerguid)
                                            .getResult();
                                }
                                // 返回预约时间信息，包括开始结束时间，和最大预约数量、当前预约数量
                                if ((yuyuetimelist != null) && yuyuetimelist.size() > 0) {
                                    for (AuditQueueYuyuetime yuyuetime : yuyuetimelist) {
                                        JSONObject timeJson = new JSONObject();
                                        // 开始时间
                                        timeJson.put("appointtimestart", yuyuetime.getYuyuetimestart());
                                        // 结束时间
                                        timeJson.put("appointtimeend", yuyuetime.getYuyuetimeend());
                                        int appointmaxsum = yuyuetime.getYuyuesum();
                                        int appointsum = iAuditQueueAppointment
                                                .getAppointCountByFwTime(tasktypeguid,
                                                        EpointDateUtil.convertString2DateAuto(
                                                                appointdate + " " + yuyuetime.getYuyuetimestart()),
                                                        EpointDateUtil.convertString2DateAuto(
                                                                appointdate + " " + yuyuetime.getYuyuetimeend()))
                                                .getResult();
                                        // 最大预约数量
                                        timeJson.put("appointmaxsum", StringUtil.getNotNullString(appointmaxsum));
                                        // 预约总数
                                        timeJson.put("appointsum", StringUtil.getNotNullString(appointsum));
                                        // appointleftover
                                        timeJson.put("appointleftover",
                                                StringUtil.getNotNullString(appointmaxsum - appointsum));
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
                                        } else {
                                            timeJson.put("isappoint", "0");
                                            timeJson.put("appointguid", "");
                                        }
                                        list.add(timeJson);
                                    }
                                } else {
                                    return JsonUtils.zwdtRestReturn("0", "该中心未配置预约时间段！", "");
                                }

                            } else {
                                return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别无法预约！", "");
                            }
                        } else {
                            return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                        }

                    } else {
                        return JsonUtils.zwdtRestReturn("0", "该时间为非工作日，不可预约！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "不能预约以前的日期！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("1", "获取用户失败！", "");
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("appointtimelist", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 预约取号
     *
     * @return
     * @params params 请求参数
     * @params request HTTP请求
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/private/getAppointQno", method = RequestMethod.POST)
    public String getAppointQno(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppointQno接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            //预约的主键，如果有说明是网厅预约时间变更
            String appointguid = obj.getString("appointguid");
            // 中心guid
            String centerguid = obj.getString("centerguid");
            // 事项guid
            String taskguid = obj.getString("taskguid");
            String accountguid = obj.getString("accountguid");
            // 用户名
            String username = obj.getString("username");
            // 身份证号码
            String identitycardid = obj.getString("idnumber");
            // 身份证号码
            if (StringUtil.isNotBlank(obj.getString("identitycardid"))) {
                identitycardid = obj.getString("identitycardid");
            }
            // 手机号
            String mobile = obj.getString("mobile");
            // 预约日期
            String appointdate = obj.getString("appointdate");

            String ouguid = obj.getString("ouguid");
            String yuyuetime = obj.getString("yuyuetime");
            String appointtimestart = "";
            String appointtimeend = "";
            if (StringUtil.isNotBlank(yuyuetime)) {
                appointtimestart = yuyuetime.substring(0, 5);
                appointtimeend = yuyuetime.substring(6, 11);
            }

            // 适配网厅的预约
            if (StringUtil.isNotBlank(obj.getString("appointtimestart"))) {
                appointtimestart = obj.getString("appointtimestart");
            }

            if (StringUtil.isNotBlank(obj.getString("appointtimeend"))) {
                appointtimeend = obj.getString("appointtimeend");
            }

            // 预约来源，1代表网厅，2代表手机，3代表微信,4代表一体机
            String appointtype = obj.getString("appointtype");
            JSONObject dataJson = new JSONObject();

            if (StringUtil.isBlank(accountguid)) {
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                accountguid = auditOnlineRegister == null ? "" : auditOnlineRegister.getAccountguid();
            }

            if (StringUtil.isNotBlank(accountguid)) {
                AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                if (task != null) {
                    // 根据事项id跟中心guid获取事项类别
                    String tasktypeguid = iAuditQueueTasktypeTask
                            .getTaskTypeguidbyTaskIDandCenterGuid(task.getTask_id(), centerguid).getResult();
                    if (StringUtil.isNotBlank(tasktypeguid)) {
                        //如果有值说明是预约时间变更的操作
                        if (StringUtil.isBlank(appointguid)) {
                            // 判断该身份证是否存在该事项预约
                            if (iAuditQueueAppointment.boolAppointByCardID(identitycardid, tasktypeguid).getResult()) {
                                // 判断该事项分类下是否有多个事项，如果有且只有一个，则默认为事项预约，否则为事项类别预约。
                                if (iAuditQueueTasktypeTask.getCountByTaskTypeguid(tasktypeguid).getResult() == 1) {
                                    return JsonUtils.zwdtRestReturn("0", "您的身份证已经预约过该事项，请不要重复预约！", "");
                                } else {
                                    return JsonUtils.zwdtRestReturn("0", "您的身份证已经预约过该类别的事项，请不要重复预约！", "");
                                }
                            }
                            // 获取事项该时间段内可预约的最大数量
                            String appointmaxsum = iAuditQueueYuyueTime
                                    .getAppointSum(tasktypeguid, "1", centerguid, appointtimestart, appointtimeend)
                                    .getResult();
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
                        }

                        AuditQueueTasktype tasktype = iAuditQueueTasktype.getTasktypeByguid(tasktypeguid).getResult();
                        AuditQueueAppointment appoint = new AuditQueueAppointment();
                        if (StringUtil.isNotBlank(appointguid)) {
                            appoint = iAuditQueueAppointment.getDetailByRowGuid(appointguid).getResult();
                            appoint.setTaskguid(taskguid);
                            appoint.setTaskname(task.getTaskname());
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
                            iAuditQueueAppointment.update(appoint);
                        } else {
                            appointguid = UUID.randomUUID().toString();
                            appoint.setRowguid(appointguid);
                            appoint.setTaskguid(taskguid);
                            appoint.setTaskname(task.getTaskname());
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
                        }

                        String yuyuedate = "";
                        if (StringUtil.isNotBlank(appoint.getAppointmentfromtime())) {
                            yuyuedate += EpointDateUtil.convertDate2String(appoint.getAppointmentfromtime(), "HH:mm");
                        }
                        yuyuedate += "-";
                        if (StringUtil.isNotBlank(appoint.getAppointmenttotime())) {
                            yuyuedate += EpointDateUtil.convertDate2String(appoint.getAppointmenttotime(), "HH:mm");
                        }
                        String appointusedate = EpointDateUtil.convertDate2String(
                                EpointDateUtil.convertString2DateAuto(appointdate + " " + appointtimeend), "MM月dd日");
                        if (StringUtil.isNotBlank(mobile)) {
                            // 如果值为0，则不发短信
                            String messagecontent = handleConfigservice
                                    .getFrameConfig("AS_QUEUE_MESSAGE_YUYUEQH", centerguid).getResult();
                            if (StringUtil.isNotBlank(messagecontent)) {
                                if (!QueueConstant.Common_no_String.equals(messagecontent)) {
                                    messagecontent = messagecontent.replace("[#=AppointUseDate#]", appointusedate);
                                    messagecontent = messagecontent.replace("[#=TaskName#]", task.getTaskname());
                                } else {
                                    messagecontent = "";
                                }
                            } else {
                                //取对应中心的配置模板
                                ServicecenterExtension servicecenterExtension = servicecenterExtensionService.getInfoByCenterguid(centerguid);
                                if (servicecenterExtension != null) {
                                    messagecontent = servicecenterExtension.getYuyuemessage();
                                    messagecontent = messagecontent.replace("[#=AppointUseDate#]", appointusedate);
                                    messagecontent = messagecontent.replace("[#=TaskName#]", task.getTaskname());
                                    messagecontent = messagecontent.replace("[#=yuyuedate#]", yuyuedate);
                                } else {
                                    messagecontent = "您已通过济宁政务服务网成功预约" + appointusedate + "办理" + task.getTaskname()
                                            + "事项，请于当日" + yuyuedate + "前携带本人身份证或预约二维码到济宁市政务服务中心取号办理。政务更阳光，办事更顺畅！济宁市政务服务中心欢迎您！咨询电话：0537-3512000";
                                }
                            }
                            if (StringUtil.isNotBlank(messagecontent)) {
                                AuditOrgaServiceCenter center = auditorgaserivce
                                        .findAuditServiceCenterByGuid(centerguid).getResult();
                                if (center != null) {
                                    imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent,
                                            new Date(), 0, new Date(), mobile, UUID.randomUUID().toString(), "", "", "",
                                            "", "", "", false, center.getBelongxiaqu());
                                } else {
                                    imessageservice.insertSmsMessage(UUID.randomUUID().toString(), messagecontent,
                                            new Date(), 0, new Date(), mobile, UUID.randomUUID().toString(), "", "", "",
                                            "", "", "", false, "");
                                }

                            }
                        }

                        FrameRole frameRole = roleService.getRoleByRoleField("rolename", "预约信息获取专员");
                        if (frameRole != null) {
                            String messagecontent = handleConfigservice
                                    .getFrameConfig("AS_QUEUE_MESSAGE_WORKERYUYUEQH", centerguid).getResult();
                            if (StringUtil.isNotBlank(messagecontent)) {
                                if (!QueueConstant.Common_no_String.equals(messagecontent)) {
                                    messagecontent = messagecontent.replace("[#=AppointUseDate#]", appointusedate);
                                    messagecontent = messagecontent.replace("[#=TaskName#]", task.getTaskname());
                                } else {
                                    messagecontent = "";
                                }
                            } else {
                                //取对应中心的配置模板
                                ServicecenterExtension servicecenterExtension = servicecenterExtensionService.getInfoByCenterguid(centerguid);
                                if (servicecenterExtension != null) {
                                    messagecontent = servicecenterExtension.getYuyuemessage();
                                    messagecontent = messagecontent.replace("[#=AppointUseDate#]", appointusedate);
                                    messagecontent = messagecontent.replace("[#=TaskName#]", task.getTaskname());
                                    messagecontent = messagecontent.replace("[#=yuyuedate#]", yuyuedate);
                                } else {
                                    messagecontent = "您已通过济宁政务服务网成功预约" + appointusedate + "办理" + task.getTaskname()
                                            + "事项，请于当日" + yuyuedate + "前携带本人身份证或预约二维码到济宁市政务服务中心取号办理。政务更阳光，办事更顺畅！济宁市政务服务中心欢迎您！咨询电话：0537-3512000";
                                }
                            }
                            if (StringUtil.isNotBlank(messagecontent)) {
                                Date nowdate = new Date();
                                AuditOrgaServiceCenter center = auditorgaserivce
                                        .findAuditServiceCenterByGuid(centerguid).getResult();
                                String roleguid = frameRole.getRoleGuid();
                                // 2、获取该角色的对应的人员
                                List<FrameUserRoleRelation> frameuserrolerelationlist = roleRelationService
                                        .getRelationListByField("roleGuid", roleguid, null, null);
                                if (frameuserrolerelationlist != null && !frameuserrolerelationlist.isEmpty()) {
                                    // 3、发送待办给审核人员
                                    for (FrameUserRoleRelation frameUserRoleRelation : frameuserrolerelationlist) {
                                        // 4、通过角色关系查到人员拓展实体
                                        FrameUser user = userservice.getUserByUserField("userguid",
                                                frameUserRoleRelation.getUserGuid());
                                        if (user != null && StringUtil.isNotBlank(user.getMobile())) {
                                            if (center != null) {
                                                imessageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                                        messagecontent, nowdate, 0, nowdate, user.getMobile(),
                                                        UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                                        center.getBelongxiaqu());
                                            } else {
                                                imessageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                                        messagecontent, nowdate, 0, nowdate, user.getMobile(),
                                                        UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                                        "");
                                            }

                                        }
                                    }
                                }

                            }
                        }
                        dataJson.put("appointguid", appointguid);
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别无法预约！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }

            } else {
                return JsonUtils.zwdtRestReturn("1", "获取用户失败！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 删除预约取号
     *
     * @return
     * @params params 请求参数
     */
    @RequestMapping(value = "/deleteAppoint", method = RequestMethod.POST)
    public String deleteAppoint(@RequestBody String params) {
        try {
            log.info("=======开始调用deleteAppoint接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String appointguid = obj.getString("appointguid");
            JSONObject dataJson = new JSONObject();
            iAuditQueueAppointment.updateStatusbyRowGuid(appointguid, QueueConstant.Appoint_Status_Delete, "",
                    new Date(), "");

            return JsonUtils.zwdtRestReturn("1", "取消预约成功", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取预约列表
     *
     * @return
     * @params params 请求参数
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
                } else {
                    // pc端
                    accountguid = ZwdtUserSession.getInstance("").getAccountGuid();
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
                appointJson.put("taskguid1", appoint.getTaskguid());
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
                } else if (QueueConstant.Appoint_Status_Process.equals(appoint.getStatus())) {
                    appointJson.put("status", QueueConstant.Appoint_Status_Process);// 已取号
                    appointJson.put("queueguid", appoint.getQueueguid());
                    appointJson.put("getnotime",
                            EpointDateUtil.convertDate2String(appoint.getGetnotime(), "yyyy-MM-dd HH:mm:ss"));
                } else {

                    if ("1".equals(type)) {
                        appointJson.put("status", QueueConstant.Appoint_Status_Init);// 未取号
                        appointJson.put("queueguid", "");
                        appointJson.put("getnotime", "");
                    } else {
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
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取预约详情
     *
     * @return
     * @params params 请求参数
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

                dataJson.put("appointguid", appoint.getRowguid());
                dataJson.put("appointtaskname", appoint.getApptaskname());
                dataJson.put("taskguid", appoint.getApptaskguid());
                tasktype = iAuditQueueTasktype.getTasktypeByguid(appoint.getApptaskguid()).getResult();
                dataJson.put("ouname",
                        StringUtil.isNotBlank(iOuService.getOuByOuGuid(tasktype.getOuguid()).getOushortName())
                                ? iOuService.getOuByOuGuid(tasktype.getOuguid())
                                .getOushortName()
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
                } else if (QueueConstant.Appoint_Status_Process.equals(appoint.getStatus())) {
                    dataJson.put("status", QueueConstant.Appoint_Status_Process);// 已取号
                    dataJson.put("queueguid", appoint.getQueueguid());
                    dataJson.put("getnotime",
                            EpointDateUtil.convertDate2String(appoint.getGetnotime(), "yyyy-MM-dd HH:mm:ss"));
                } else {

                    if (EpointDateUtil.compareDateOnDay(new Date(), appoint.getAppointmenttotime()) < 0) {
                        dataJson.put("status", QueueConstant.Appoint_Status_Init);// 未取号
                        dataJson.put("queueguid", "");
                        dataJson.put("getnotime", "");
                    } else {
                        dataJson.put("status", QueueConstant.Appoint_Status_Pass);// 已过号
                        dataJson.put("queueguid", "");
                        dataJson.put("getnotime", "");
                    }
                }

                AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                        .findAuditServiceCenterByGuid(appoint.getCenterguid()).getResult();
                dataJson.put("centername", center.getCentername());

                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(appoint.getTaskguid(), true).getResult();
                if (auditTask != null) {
                    String addresstime = auditTask.getStr("ACCEPT_ADDRESS_INFO");
                    String address = "";
                    if (StringUtil.isNotBlank(addresstime)) {
                        if (addresstime.contains("<nodes>")) {
                            address = DealAddress2(addresstime);// 办理地点
                        } else {
                            address = DealAddress1(addresstime);// 办理地点
                        }
                        if (StringUtil.isBlank(address)) {
                            address = center.getAddress();
                        }
                    }
                    dataJson.put("address", address);
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "该预约不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 办理地点解析方法一
     */
    @SuppressWarnings("unchecked")
    public static String DealAddress1(String banlididiansj) {
        String address = "";
        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(banlididiansj);
            if (StringUtil.isNotBlank(banlididiansj)) {
                if (document != null) {
                    Element root = document.getRootElement();
                    if (root == null) {
                        return "";
                    }
                    Element data1 = root.element("ACCEPT_ADDRESSS");
                    if (data1 == null) {
                        List<Element> dataflows = root.elements("node");
                        for (Element data : dataflows) {
                            Record record = new Record();
                            List<Element> content2 = data.content();
                            for (Element element : content2) {
                                String label = element.attributeValue("label");
                                String text = element.getText();
                                if ("ADDRESS".equals(label)) {
                                    address = text;
                                }
                            }

                        }
                        return "";
                    }
                    List<Element> datas = data1.elements("ACCEPT_ADDRESS");
                    if (datas == null) {
                        return "";
                    }
                    for (Element data : datas) {
                        Element ADDRESS = data.element("ADDRESS");
                        if (StringUtil.isNotBlank(ADDRESS)) {
                            address = ADDRESS.getStringValue();
                        }

                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * 办理地点解析方法二
     */
    @SuppressWarnings("unchecked")
    public static String DealAddress2(String info) {
        List<Record> list = new ArrayList<Record>();
        String address = "";

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(info);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (StringUtil.isNotBlank(info)) {
            if (document != null) {
                Element root = document.getRootElement();
                List<Element> dataflows = root.elements("node");
                for (Element data : dataflows) {
                    Record record = new Record();
                    List<Element> content2 = data.content();
                    for (Element element : content2) {
                        String label = element.attributeValue("label");
                        String text = element.getText();
                        if ("ADDRESS".equals(label)) {
                            record.set("address", text);
                        }
                        list.add(record);
                    }

                }
            }
            if (list != null && !list.isEmpty()) {
                address = list.get(0).getStr("address");
            }
        }
        return address;
    }

    /**
     * 获取各时间段等待人数
     *
     * @return
     * @params params 请求参数
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/getWaitNumbyTime", method = RequestMethod.POST)
    public String getWaitNumbyTime(@RequestBody String params) {
        try {
            log.info("=======开始调用getWaitNumbyTime接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String taskguid = obj.getString("taskguid");
            String tasktypeguid = "";
            String timeperiod;
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject waitnumJson = new JSONObject();
            AuditQueueTasktype tasktype = null;

            AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {
                // 根据事项guid跟中心guid获取事项类别
                tasktypeguid = iAuditQueueTasktypeTask
                        .getTaskTypeguidbyTaskIDandCenterGuid(task.getTask_id(), centerguid).getResult();
                if (StringUtil.isNotBlank(tasktypeguid)) {

                    Date FromDate = EpointDateUtil.convertString2Date(
                            EpointDateUtil.convertDate2String(EpointDateUtil.addMonth(new Date(), -1), "yyyy-MM-dd")
                                    + " 23:59:59");
                    Date ToDate = EpointDateUtil.convertString2Date(
                            EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd") + " 00:00:00");
                    int workingday = iAuditOrgaWorkingDay.GetWorkingDays_Between_From_To(centerguid, FromDate, ToDate)
                            .getResult();

                    // 早上8点到下午17点
                    for (int i = 8; i <= 17; i++) {
                        waitnumJson = new JSONObject();
                        if (i < 10) {
                            timeperiod = "0" + StringUtil.getNotNullString(i);
                        } else {
                            timeperiod = StringUtil.getNotNullString(i);
                        }
                        waitnumJson.put("timeperiod", timeperiod);
                        waitnumJson.put("waitnum", StringUtil.getNotNullString(
                                iAuditQueue.getWaitNumByTime(tasktypeguid, timeperiod, FromDate, ToDate).getResult()
                                        / workingday));

                        list.add(waitnumJson);
                    }
                    dataJson.put("waitnumlist", list);
                    AuditOrgaServiceCenter center = iAuditOrgaServiceCenter.findAuditServiceCenterByGuid(centerguid)
                            .getResult();
                    dataJson.put("centername", center.getCentername());
                    dataJson.put("address", center.getAddress());

                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 从事项进入预约，获取部门
     */
    @RequestMapping(value = "/getOuAndTask", method = RequestMethod.POST)
    public String getOuAndTask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getOuAndTask接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String taskguid = obj.getString("taskguid");
            JSONObject dataJson = new JSONObject();
            AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
            if (task != null) {
                dataJson.put("ouguid", task.getOuguid());
                dataJson.put("ouname", task.getOuname());
                dataJson.put("taskguid", task.getRowguid());
                dataJson.put("taskname", task.getTaskname());

            }
            dataJson.put("username", ZwdtUserSession.getInstance("").getClientName());
            dataJson.put("mobile", ZwdtUserSession.getInstance("").getMobile());
            dataJson.put("idnumber", ZwdtUserSession.getInstance("").getIdnum());
            log.info("=======结束调用getOuAndTask接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            log.info("=======getOuAndTask接口参数：params【" + params + "】=======");
            log.info("=======getOuAndTask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取窗口部门
     */
    @RequestMapping(value = "/getAppointOu", method = RequestMethod.POST)
    public String getAppointOu(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAppointOu接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // String areacode = obj.getString("areacode");
            String centerguid = obj.getString("centerguid");

            JSONObject dataJson = new JSONObject();
            OAuthCheckTokenInfo info = CheckTokenUtil.getCheckTokenInfo(request);
            AuditOnlineRegister onlineRegister = null;
            String loginid = "";

            if (info != null) {
                loginid = info.getLoginid();
                if (StringUtil.isNotBlank(loginid)) {
                    // 手机端
                    // 通过登录名获取用户
                    onlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(loginid).getResult();
                    if (onlineRegister == null) {
                        onlineRegister = iAuditOnlineRegister.getRegisterByLoginId(loginid).getResult();
                    }
                    dataJson.put("username", onlineRegister.getUsername());
                    dataJson.put("mobile", onlineRegister.getMobile());
                    dataJson.put("idnumber", onlineRegister.getIdnumber());
                } else {
                    loginid = ZwdtUserSession.getInstance("").getLoginid();
                    dataJson.put("username", ZwdtUserSession.getInstance("").getClientName());
                    dataJson.put("mobile", ZwdtUserSession.getInstance("").getMobile());
                    dataJson.put("idnumber", ZwdtUserSession.getInstance("").getIdnum());
                }
            } else {
                loginid = ZwdtUserSession.getInstance("").getLoginid();
                dataJson.put("username", ZwdtUserSession.getInstance("").getClientName());
                dataJson.put("mobile", ZwdtUserSession.getInstance("").getMobile());
                dataJson.put("idnumber", ZwdtUserSession.getInstance("").getIdnum());
            }
            List<Record> list = iCzQueueAppointment.getAppointOuByOuguid(centerguid).getResult();

            dataJson.put("ouList", list);

            log.info("=======结束调用getAppointOu接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            log.info("=======getAppointOu接口参数：params【" + params + "】=======");
            log.info("=======getAppointOu异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取在用事项 根据窗口部门
     */
    @RequestMapping(value = "/getTaskByOuguid", method = RequestMethod.POST)
    public String getTaskByOuguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskByOuguid接口=======");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String ouguid = obj.getString("ouguid");
            JSONObject dataJson = new JSONObject();

            String index = obj.getString("currentpage");
            String size = obj.getString("pagesize");
            List<AuditZnsbCentertask> list = iCzQueueAppointment.getAppointTaskByOuguid(ouguid, index, size)
                    .getResult();

            dataJson.put("taskList", list);
            log.info("=======结束调用getTaskByOuguid接口=======");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            log.info("=======getTaskByOuguid接口参数：params【" + params + "】=======");
            log.info("=======getTaskByOuguid异常信息：" + e.getMessage() + "=======");
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
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
        }
        return auditOnlineRegister;
    }

}
