package com.epoint.basic.auditqueue.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.SaveFormat;
import com.epoint.auditqueue.auditqueuerest.queue.TestCode128C;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueappointment.domain.AuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueueappointment.inter.IAuditQueueAppointment;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.JxIAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.domain.AuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.inter.IAuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.inter.JxIAuditQueueTasktype;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QrcodeUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.composite.auditqueue.jxhandlequeue.inter.IJXHandleQueue;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@RestController
@RequestMapping("/jxqhjqueue")
public class JxQhjQueueRestController
{
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditOrgaWindow windowservice;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditQueueAppointment appointmentservice;

    @Autowired
    private JxIAuditQueueTasktype tasktypeservice;

    @Autowired
    private IAuditQueueTasktypeTask tasktypeaskservice;

    @Autowired
    private IHandleQueue handlequeueservice;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditQueueXianhaotime xianhaoservice;

    @Autowired
    private IAuditQueue queueservice;

    @Autowired
    private IAuditOrgaServiceCenter centerservice;

    @Autowired
    private IAuditQueue auditqueueservice;

    @Autowired
    private IMessagesCenterService messageservice;

    @Autowired
    private JxIAuditQueueWindowTasktype queuewindowtaskservice;

    @Autowired
    private IAuditOrgaHall hallservice;

    @Autowired
    private IAuditOrgaServiceCenter auditorgaserivce;

    @Autowired
    private IJXHandleQueue jxhandlequeueservice;
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditOrgaWindow windowService;
    TestCode128C code128c = new TestCode128C();

    /**
     * 取号机初始化
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
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_QHJ);
                equipment.setIsneedpass(QueueConstant.Common_yes_String);
                equipment.setIsuseappointment(QueueConstant.Common_yes_String);
                equipment.setAlertlength(10.0);
                equipment.setLeftpaperpiece(100);
                equipment.setReadCardType(QueueConstant.QHJ_READCARD_JL);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    // 首页地址
                    dataJson.put("homepageurl", equipment.getHomepageurl());
                    // 是否可以跳过
                    dataJson.put("isneedpass", equipment.getIsneedpass());
                    // 是否显示预约
                    dataJson.put("isuseappointment", equipment.getIsuseappointment());
                    // 大厅guid
                    dataJson.put("hallguid", equipment.getHallguid());
                    // 中心guid
                    dataJson.put("centerguid", equipment.getCenterguid());

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
                return JsonUtils.zwdtRestReturn("0", "该设备未注册！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 取号机剩余纸张
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getLeftPiece", method = RequestMethod.POST)
    public String getLeftPiece(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            JSONObject dataJson = new JSONObject();
            String macaddress = obj.getString("macaddress");
            double alertlength = 0.0;
            int leftpaperpiece = 0;
            String isBlink = "no";
            AuditZnsbEquipment equipment = equipmentservice
                    .getDetailbyMacaddress(macaddress, " Alertlength,Leftpaperpiece ").getResult();
            if (StringUtil.isNotBlank(equipment)) {
                alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
                leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();
                // 预警
                if (alertlength >= leftpaperpiece) {
                    isBlink = "is";
                }
                // 缺纸
                if (leftpaperpiece == 0) {
                    isBlink = "red";
                }
                dataJson.put("leftpiece", String.valueOf(leftpaperpiece));
                dataJson.put("isblink", isBlink);
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项类别列表
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getTaskTypeList", method = RequestMethod.POST)
    public String getTaskTypeList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String ouguid = obj.getString("ouguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String tasktypename = obj.getString("tasktypename");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject tasktypeJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);

            if (StringUtil.isNotBlank(tasktypename)) {
                sql.like("tasktypename", tasktypename);
            }

            // 根据ouguid查询
            if (StringUtil.isNotBlank(ouguid)) {
                sql.eq("ouguid", ouguid);
                // 根据hallguid查询
            }
            else {
                // 如果为all查询整个中心的事项
                if (!"all".equals(hallguid)) {
                    List<String> oulist = windowservice.getOUbyHall(centerguid, hallguid).getResult();
                    String ous = "";
                    for (String ou : oulist) {
                        ous += ou + "','";
                    }
                    ous = "'" + ous + "'";
                    sql.in("ouguid", ous);
                }

            }
            PageData<AuditQueueTasktype> tasktypepagedata = tasktypeservice
                    .getAuditQueueTasktypeByPage(" rowguid,tasktypename,is_face ", sql.getMap(),
                            Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                            "ordernum", "desc")
                    .getResult();

            int totalcount = tasktypepagedata.getRowCount();
            for (AuditQueueTasktype tasktype : tasktypepagedata.getList()) {
                tasktypeJson = new JSONObject();
                tasktypeJson.put("tasktypeguid", tasktype.getRowguid());
                tasktypeJson.put("tasktypename", tasktype.getTasktypename());
                tasktypeJson.put("isface", tasktype.getIs_face());
                tasktypeJson.put("taskwaitnum", StringUtil
                        .getNotNullString(handlequeueservice.getTaskWaitNum(tasktype.getRowguid(), true).getResult()));
                list.add(tasktypeJson);
            }

            dataJson.put("tasktypelist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取取号部门列表
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getOUList", method = RequestMethod.POST)
    public String getOUList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            FrameOu ou = null;
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject ouJson = new JSONObject();

            List<String> ouguidlist = windowservice.getOUbyHall(centerguid, hallguid).getResult();

            for (String ouguid : ouguidlist) {
                ouJson = new JSONObject();
                ouJson.put("ouguid", ouguid);
                ouJson.put("ougnum",
                        StringUtil.isNotBlank(ouservice.getOuByOuGuid(ouguid).getOrderNumber())
                                ? ouservice.getOuByOuGuid(ouguid).getOrderNumber()
                                : 0);
                ou = ouservice.getOuByOuGuid(ouguid);
                if (StringUtil.isNotBlank(ou)) {
                    ouJson.put("ouname",
                            StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                }
                else {
                    ouJson.put("ouname", "");
                }
                list.add(ouJson);
            }
            // 根据ougnum对部门数据进行降序排序
            Collections.sort(list,
                    (JSONObject l1, JSONObject l2) -> l2.getIntValue("ougnum") - l1.getIntValue("ougnum"));
            // 截取对应页面的部门list数据
            int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
            int endint = (firstint + Integer.parseInt(pageSize)) >= list.size() ? list.size()
                    : (firstint + Integer.parseInt(pageSize));
            List<JSONObject> rtnlist = list.subList(firstint, endint);

            int totalcount = list.size();

            dataJson.put("oulist", rtnlist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断事项是否可以取号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/checkTaskType", method = RequestMethod.POST)
    public String checkTaskType(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String tasktypeguid = obj.getString("tasktypeguid");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();

            Boolean isusexiaohaotime = false;

            if (StringUtil.isNotBlank(macaddress)) // 如果macaddress不为空则检测系统缺纸
            {
                AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress, " leftpaperpiece ")
                        .getResult();

                if (StringUtil.isNotBlank(equipment)) {
                    int leftpaperpiece = equipment.getLeftpaperpiece();
                    if (leftpaperpiece <= 0) {
                        return JsonUtils.zwdtRestReturn("0", "剩余纸张数量为0，请联系管理员！", "");
                    }
                }
            }

            int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

            // 判断当前事项,当前星期是否有限号
            AuditQueueXianhaotime xianhaotime = null;
            xianhaotime = xianhaoservice.getDetailbyTasktypeguidandweek(String.valueOf(week), tasktypeguid, centerguid)
                    .getResult();
            if (xianhaotime != null) {
                isusexiaohaotime = true;
            }
            else {
                // 是否有通用设置
                xianhaotime = xianhaoservice
                        .getDetailbyTasktypeguidandweek(String.valueOf(week), "commonguid", centerguid).getResult();
                if (xianhaotime != null) {
                    isusexiaohaotime = true;
                }
            }
            if (isusexiaohaotime) {
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int date = Calendar.getInstance().get(Calendar.DATE);

                String amstart = year + "-" + month + "-" + date + " " + xianhaotime.getAmstart();
                String amend = year + "-" + month + "-" + date + " " + xianhaotime.getAmend();
                String pmstart = year + "-" + month + "-" + date + " " + xianhaotime.getPmstart();
                String pmend = year + "-" + month + "-" + date + " " + xianhaotime.getPmend();
                try {
                    long ams = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(amstart).getTime();
                    long ame = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(amend).getTime();
                    long pms = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pmstart).getTime();
                    long pme = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pmend).getTime();

                    long now = new Date().getTime();

                    if (now > ams && now < ame || now > pms && now < pme) {

                    }
                    else {
                        return JsonUtils
                                .zwdtRestReturn("0",
                                        "时间段" + xianhaotime.getAmstart() + "至" + xianhaotime.getAmend() + "以及"
                                                + xianhaotime.getPmstart() + "至" + xianhaotime.getPmend() + "外无法取号",
                                        "");
                    }
                }
                catch (ParseException e) {

                    return JsonUtils.zwdtRestReturn("0", "请检查相关限号时间格式是否正确！", "");
                }

            }
            // 判断事项数量限号
            AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(tasktypeguid).getResult();
            if (tasktype != null && StringUtil.isNotBlank(tasktype.getXianhaonum())) {
                if (queueservice.getCountByTaskGuid(tasktypeguid).getResult() >= Integer
                        .valueOf(tasktype.getXianhaonum())) {
                    return JsonUtils.zwdtRestReturn("0", "该事项今日取号数已达上限，无法取号！", "");
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 将人员信息存进人员库
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/insertUserInfo", method = RequestMethod.POST)
    public String insertUserInfo(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String cardno = obj.getString("cardno");
            String name = obj.getString("name");
            String sex = obj.getString("sex");
            String nation = obj.getString("nation");
            String born = obj.getString("born");
            String address = obj.getString("address");
            String picture = obj.getString("picture");

            byte[] pic = Base64Util.decodeBuffer(picture);
            JSONObject dataJson = new JSONObject();
            userinfoservice.insertuserinfo(cardno, name, sex, nation, born, address, pic);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 根据身份证获取电话号码
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getPhonebyCardNO", method = RequestMethod.POST)
    public String getPhonebyCardNO(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String cardno = obj.getString("cardno");
            String phone = "";
            JSONObject dataJson = new JSONObject();
            if (StringUtil.isNotBlank(cardno)) {
                phone = userinfoservice.getPhonebyCardNO(cardno).getResult();
                if (StringUtil.isNotBlank(phone)) {
                    dataJson.put("phone", phone);
                }
                else {
                    dataJson.put("phone", "");
                }
            }
            else {
                dataJson.put("phone", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 根据身份证获取人脸头像
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getPhotobyCardNO", method = RequestMethod.POST)
    public String getPhotobyCardNO(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String cardno = obj.getString("cardno");
            JSONObject dataJson = new JSONObject();
            if (StringUtil.isNotBlank(cardno)) {
                AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(cardno).getResult();
                if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                    if (StringUtil.isNotBlank(auditQueueUserinfo.getPhoto())) {
                        dataJson.put("photobase64", Base64Util.encode(auditQueueUserinfo.getPhoto()));
                        dataJson.put("username", auditQueueUserinfo.getDisplayname());
                    }
                    else {
                        dataJson.put("photobase64", "");
                        dataJson.put("username", "");
                    }
                }

            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断是否有预约
     * 
     * @param sfz
     *            身份证
     */
    @RequestMapping(value = "/checkAppointbyCardNO", method = RequestMethod.POST)
    public String checkAppointbyCardNO(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            JSONObject dataJson = new JSONObject();
            String cardno = obj.getString("cardno");
            String checkApp = "";
            if (appointmentservice.getAppointmentCount(cardno, new Date(), EpointDateUtil.addDay(new Date(), 7))
                    .getResult() > 0) {
                checkApp = "success";
            }
            else {
                checkApp = "none";
            }
            dataJson.put("msg", checkApp);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取预约列表
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getAppointList", method = RequestMethod.POST)
    public String getAppointList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String cardno = obj.getString("cardno");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String type = obj.getString("type");// 1代表今日预约 2代表本周预约
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject appointJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();

            sql.eq("centerguid", centerguid);
            sql.eq("status", "0");
            sql.eq("IDENTITYCARDNUM", cardno);
            if ("1".equals(type)) {
                sql.gt("APPOINTMENTTOTIME", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                sql.lt("APPOINTMENTTOTIME", EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDate(new Date()),
                        "yyyy-MM-dd HH:mm:ss"));
            }
            else {
                sql.gt("APPOINTMENTTOTIME", EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDate(new Date()),
                        "yyyy-MM-dd HH:mm:ss"));
                sql.lt("APPOINTMENTTOTIME",
                        EpointDateUtil.convertDate2String(EpointDateUtil.addDay(new Date(), 7), "yyyy-MM-dd HH:mm:ss"));
            }
            PageData<AuditQueueAppointment> pageData = appointmentservice
                    .getAppointByPage(sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "APPOINTMENTFROMTIME", "asc")
                    .getResult();
            int totalcount = pageData.getRowCount();
            List<AuditQueueAppointment> appointList = pageData.getList();

            for (AuditQueueAppointment appoint : appointList) {
                {
                    appointJson = new JSONObject();
                    appointJson.put("appointguid", appoint.getRowguid());
                    appointJson.put("tasktypename", appoint.getApptaskname());
                    appointJson.put("appointtimestart",
                            EpointDateUtil.convertDate2String(appoint.getAppointmentfromtime(), "yyyy-MM-dd HH:mm:ss"));
                    appointJson.put("appointtimeend",
                            EpointDateUtil.convertDate2String(appoint.getAppointmenttotime(), "yyyy-MM-dd HH:mm:ss"));
                    appointJson.put("appointtime",
                            EpointDateUtil.convertDate2String(appoint.getAppointmentfromtime(), "yyyy-MM-dd HH:mm")
                                    + "~" + EpointDateUtil.convertDate2String(appoint.getAppointmenttotime(), "HH:mm"));
                    list.add(appointJson);
                }

            }
            dataJson.put("appointdatelist", list);
            dataJson.put("totalcount", totalcount);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 删除预约取号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/deleteAppoint", method = RequestMethod.POST)
    public String deleteAppoint(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String appointguid = obj.getString("appointguid");
            JSONObject dataJson = new JSONObject();
            appointmentservice.updateStatusbyRowGuid(appointguid, QueueConstant.Appoint_Status_Delete, "", new Date(),
                    "");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 预约取号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getAPQno", method = RequestMethod.POST)
    public String getAPQno(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String appointguid = obj.getString("appointguid");
            String centerguid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();

            double alertlength = 0.0;
            int leftpaperpiece = 0;
            String operatorusermobile = "";

            AuditZnsbEquipment equipment = equipmentservice
                    .getDetailbyMacaddress(macaddress, " Alertlength,Leftpaperpiece,operatorusermobile,MACHINENO")
                    .getResult();
            if (StringUtil.isNotBlank(equipment)) {
                alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
                leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();
                operatorusermobile = equipment.getOperatorusermobile();
                // 查询中心所在辖区编号
                String belongxiaqu = "";
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("rowguid", centerguid);
                List<AuditOrgaServiceCenter> auditOrgaServiceCenters = auditorgaserivce
                        .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();
                if (auditOrgaServiceCenters != null && auditOrgaServiceCenters.size() > 0) {
                    belongxiaqu = auditOrgaServiceCenters.get(0).getBelongxiaqu();
                }
                // 发送预警短信
                if (StringUtil.isNotBlank(operatorusermobile)) {
                    if (leftpaperpiece == 1) {
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                "设备编号：" + equipment.getMachineno() + "，剩余纸张数量为0，请管理员赶紧添加纸张！", new Date(), 0, new Date(),
                                operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                belongxiaqu);
                    }
                    if (leftpaperpiece == alertlength + 1) {
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                "设备编号：" + equipment.getMachineno() + "，剩余纸张数量已达报警长度，请管理员注意！", new Date(), 0, new Date(),
                                operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                belongxiaqu);
                    }
                }
            }

            Map<String, String> msg = jxhandlequeueservice.getAPQNO(appointguid, centerguid).getResult();
            if (msg.get("msg") != null && "success".equals(msg.get("msg"))) {
                dataJson.put("qno", msg.get("qno"));
            }
            else {
                return JsonUtils.zwdtRestReturn("0", msg.get("msg"), "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 打印小票
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getQnoPrint", method = RequestMethod.POST)
    public String qhprint(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();

            // system.out.println("==============进入getQnoPrint=================qno="
            // + qno);

            // 减纸
            equipmentservice.descLeftPiece(macaddress);
            // 总打印+1
            // equipmentservice.ascAllPiece(MacAddress);
            String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum ";
            AuditQueue auditqueue = jxhandlequeueservice.getQueueDetail(fieldstr, qno, centerguid).getResult();

            if (StringUtil.isNotBlank(auditqueue)) {
                // system.out.println("==============queue不为空===============事项guid="
                // + auditqueue.getTaskguid());
                AuditQueueTasktype auditqueuetasktype = tasktypeservice
                        .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
                if (StringUtil.isNotBlank(auditqueuetasktype)) {
                    // system.out.println("==============事项类别不为空===============guid="
                    // + auditqueue.getTaskguid());
                    dataJson.put("qno", auditqueue.getQno());
                    dataJson.put("paiduiprint",
                            centerservice.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
                    dataJson.put("taskname", auditqueuetasktype.getTasktypename());
                    dataJson.put("waitnum", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult()));
                    // 重新分割窗口
                    String newindowno = "";
                    List<String> windowguids = queuewindowtaskservice
                            .getWindowListByTaskTypeGuid(auditqueuetasktype.getRowguid()).getResult();
                    if (windowguids.size() > 0) {
                        for (int i = 0; i < windowguids.size(); i++) {

                            String newwindowno = "";
                            AuditOrgaWindow window = windowservice.getWindowByWindowGuid(windowguids.get(i))
                                    .getResult();
                            /*
                             * if(!window.getWindowno().contains("B4") &&
                             * !window.getWindowno().contains("A")){
                             * StringBuffer stringBuffer = new
                             * StringBuffer(window.getWindowno());
                             * newwindowno = stringBuffer.insert(2,
                             * "区").toString();
                             * }else{
                             * newwindowno = window.getWindowno();
                             * }
                             */
                            newwindowno = window.getWindowno();
                            if (!"5".equals(window.getWindowtype())) {
                                if (i == 0) {
                                    AuditOrgaHall hall = hallservice.getAuditHallByRowguid(window.getLobbytype())
                                            .getResult();
                                    if (StringUtil.isNotBlank(hall)) {
                                        newindowno = hall.getHallname() + newwindowno + ";";
                                    }
                                }
                                else {
                                    newindowno = newindowno + newwindowno + ";";
                                }
                            }
                        }
                    }
                    dataJson.put("windowno", newindowno);
                    String barCode = code128c.getCode(auditqueue.getFlowno(), "");
                    dataJson.put("flowno", code128c.kiCode128C(barCode, 40, auditqueue.getFlowno() + ".jpg"));
                    // dataJson.put("windowno", auditqueue.getHandlewindowno());
                    dataJson.put("time", EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                    dataJson.put("flownonum", auditqueue.getFlowno());
                    dataJson.put("identitycardnum", auditqueue.getIdentitycardnum());
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getgxqQnoPrint", method = RequestMethod.POST)
    public String getgxqQnoPrint(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();

            // system.out.println("==============进入getQnoPrint=================qno="
            // + qno);

            // 减纸
            equipmentservice.descLeftPiece(macaddress);
            // 总打印+1
            // equipmentservice.ascAllPiece(MacAddress);
            String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum ";
            AuditQueue auditqueue = jxhandlequeueservice.getQueueDetail(fieldstr, qno, centerguid).getResult();

            if (StringUtil.isNotBlank(auditqueue)) {
                // system.out.println("==============queue不为空===============事项guid="
                // + auditqueue.getTaskguid());
                AuditQueueTasktype auditqueuetasktype = tasktypeservice
                        .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
                if (StringUtil.isNotBlank(auditqueuetasktype)) {
                    // system.out.println("==============事项类别不为空===============guid="
                    // + auditqueue.getTaskguid());
                    dataJson.put("qno", auditqueue.getQno());
                    dataJson.put("paiduiprint",
                            centerservice.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
                    dataJson.put("taskname", auditqueuetasktype.getTasktypename());
                    dataJson.put("waitnum", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult()));
                    // 重新分割窗口
                    String newindowno = "";
                    List<String> windowguids = queuewindowtaskservice
                            .getWindowListByTaskTypeGuid(auditqueuetasktype.getRowguid()).getResult();
                    if (windowguids.size() > 0) {
                        for (int i = 0; i < windowguids.size(); i++) {

                            String newwindowno = "";
                            AuditOrgaWindow window = windowservice.getWindowByWindowGuid(windowguids.get(i))
                                    .getResult();
                            /*
                             * if(!window.getWindowno().contains("B4") &&
                             * !window.getWindowno().contains("A")){
                             * StringBuffer stringBuffer = new
                             * StringBuffer(window.getWindowno());
                             * newwindowno = stringBuffer.insert(2,
                             * "区").toString();
                             * }else{
                             * newwindowno = window.getWindowno();
                             * }
                             */
                            newwindowno = window.getWindowno();
                            if (!"5".equals(window.getWindowtype())) {
                                if (i == 0) {
                                    AuditOrgaHall hall = hallservice.getAuditHallByRowguid(window.getLobbytype())
                                            .getResult();
                                    if (StringUtil.isNotBlank(hall)) {
                                        newindowno = hall.getHallname() + newwindowno + ";";
                                    }
                                }
                                else {
                                    newindowno = newindowno + newwindowno + ";";
                                }
                            }
                        }
                    }
                    dataJson.put("windowno", newindowno);
                    String barCode = code128c.getCode(auditqueue.getFlowno(), "");
                    dataJson.put("flowno", code128c.kiCode128C(barCode, 40, auditqueue.getFlowno() + ".jpg"));
                    // dataJson.put("windowno", auditqueue.getHandlewindowno());
                    dataJson.put("time", EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                    dataJson.put("flownonum", auditqueue.getFlowno());
                    dataJson.put("identitycardnum", auditqueue.getIdentitycardnum());
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 取号
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getQno", method = RequestMethod.POST)
    public String getQno(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String cardno = obj.getString("cardno");
            String phone = obj.getString("phone");
            String tasktypeguid = obj.getString("tasktypeguid");
            String centerguid = obj.getString("centerguid");
            String hallguid = obj.getString("hallguid");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();

            double alertlength = 0.0;
            int leftpaperpiece = 0;
            String operatorusermobile = "";

            AuditZnsbEquipment equipment = equipmentservice
                    .getDetailbyMacaddress(macaddress, " Alertlength,Leftpaperpiece,operatorusermobile,MACHINENO")
                    .getResult();
            if (StringUtil.isNotBlank(equipment)) {
                alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
                leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();
                operatorusermobile = equipment.getOperatorusermobile();
                // 查询中心所在辖区编号
                String belongxiaqu = "";
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("rowguid", centerguid);
                List<AuditOrgaServiceCenter> auditOrgaServiceCenters = auditorgaserivce
                        .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();
                if (auditOrgaServiceCenters != null && auditOrgaServiceCenters.size() > 0) {
                    belongxiaqu = auditOrgaServiceCenters.get(0).getBelongxiaqu();
                }
                // 发送预警短信
                if (StringUtil.isNotBlank(operatorusermobile)) {
                    if (leftpaperpiece == 1) {
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                "设备编号：" + equipment.getMachineno() + "，剩余纸张数量为0，请管理员赶紧添加纸张！", new Date(), 0, new Date(),
                                operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                belongxiaqu);
                    }
                    if (leftpaperpiece == alertlength + 1) {
                        messageservice.insertSmsMessage(UUID.randomUUID().toString(),
                                "设备编号：" + equipment.getMachineno() + "，剩余纸张数量已达报警长度，请管理员注意！", new Date(), 0, new Date(),
                                operatorusermobile, UUID.randomUUID().toString(), "", "", "", "", "", "", false,
                                belongxiaqu);
                    }
                }
            }

            String existqno = queueservice.existQno(cardno, tasktypeguid).getResult();
            if (StringUtil.isBlank(existqno)) {
                Map<String, String> msg = jxhandlequeueservice.getQNO(cardno, phone, tasktypeguid, centerguid, hallguid)
                        .getResult();
                if (msg.get("msg") != null && "success".equals(msg.get("msg"))) {
                    dataJson.put("qno", msg.get("qno"));
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", msg.get("msg"), "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "您有一个号码事项尚未办理，编号为" + existqno + ",请勿重复取号！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 判断是否需要人脸
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/checkFace", method = RequestMethod.POST)
    public String checkFace(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String appointguid = obj.getString("appointguid");
            JSONObject dataJson = new JSONObject();
            String needface = "0";
            AuditQueueAppointment auditQueueAppointment = appointmentservice
                    .getDetailByRowGuid(appointguid, " Apptaskguid ").getResult();
            if (StringUtil.isNotBlank(auditQueueAppointment)) {
                String isface = tasktypeaskservice
                        .getfacebyAppTaskIDandCenterGuid(auditQueueAppointment.getApptaskguid()).getResult();
                if (StringUtil.isNotBlank(isface)) {
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(isface)) {
                        needface = "1";
                    }
                }
            }
            dataJson.put("isface", needface);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 条形码
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getFlowno", method = RequestMethod.POST)
    public String getflowno(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String flowno = obj.getString("flownonum");
            JSONObject dataJson = new JSONObject();
            String barCode = code128c.getCode(flowno, "");
            dataJson.put("flowno", code128c.kiCode128C(barCode, 40, flowno + ".jpg"));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getlsQnoPrint", method = RequestMethod.POST)
    public String lsqhprint(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();

            // 减纸
            equipmentservice.descLeftPiece(macaddress);
            // 总打印+1
            // equipmentservice.ascAllPiece(MacAddress);
            String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum ";
            AuditQueue auditqueue = auditqueueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();

            if (StringUtil.isNotBlank(auditqueue)) {
                AuditQueueTasktype auditqueuetasktype = tasktypeservice
                        .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
                if (StringUtil.isNotBlank(auditqueuetasktype)) {
                    dataJson.put("qno", auditqueue.getQno());
                    dataJson.put("paiduiprint",
                            centerservice.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
                    dataJson.put("taskname", auditqueuetasktype.getTasktypename());
                    dataJson.put("waitnum", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult()));
                    String location = "";
                    if (StringUtil.isNotBlank(auditqueue.getHandlewindowno())) {
                        String[] windownos = auditqueue.getHandlewindowno().split(";");
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("centerguid", centerguid);
                        sql.eq("windowno", windownos[0]);
                        List<AuditOrgaWindow> list = windowservice.getAllWindow(sql.getMap()).getResult();
                        if (list != null && !list.isEmpty()) {
                            location = list.get(0).getStr("address");
                        }
                    }
                    dataJson.put("windowno", location + auditqueue.getHandlewindowno());
                    dataJson.put("time", EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                    dataJson.put("flownonum", auditqueue.getFlowno());
                    String barCode = code128c.getCode(auditqueue.getFlowno(), "");
                    dataJson.put("flowno", code128c.kiCode128C(barCode, 40, auditqueue.getFlowno() + ".jpg"));
                    dataJson.put("identitycardnum", auditqueue.getIdentitycardnum());
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = {"/getTaskDetailDoc" }, method = {RequestMethod.POST })
    public String getTaskDetailDoc(@RequestBody String params, @Context HttpServletRequest request) throws Exception {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");
            String macaddress = obj.getString("macaddress");

            // 减纸
            equipmentservice.descLeftPiece(macaddress);
            // 总打印+1
            // equipmentservice.ascAllPiece(MacAddress);
            String fieldstr = " Taskguid,Qno,Handlewindowno,Flowno,identitycardnum ";
            AuditQueue auditqueue = auditqueueservice.getQNODetailByQNO(fieldstr, qno, centerguid).getResult();
            String localurl = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", centerguid).getResult();
            String doctem = "";
            if (StringUtil.isNotBlank(localurl)) {

                doctem = localurl + "/jiningzwfw/individuation/jining/epointqueue/qhj/qnotemplet.docx";

            }
            else {
                doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/jiningzwfw/individuation/jining/epointqueue/qhj/qnotemplet.docx";
            }
            if (StringUtil.isNotBlank(auditqueue)) {
                AuditQueueTasktype auditqueuetasktype = tasktypeservice
                        .getAuditQueueTasktypeByRowguid(auditqueue.getTaskguid()).getResult();
                if (StringUtil.isNotBlank(auditqueuetasktype)) {

                    Document doc = new Document(doctem);
                    // system.out.println(doctem);
                    String[] fieldNames = null;
                    Object[] values = null;
                    Map<String, String> map = new HashMap<String, String>(16);
                    map.put("流水号", auditqueue.getQno());
                    map.put("办理楼层", auditqueue.getHandlewindowno());
                    map.put("办理窗口", auditqueue.getHandlewindowno().substring(0, 1));
                    map.put("等待人数", StringUtil.getNotNullString(
                            handlequeueservice.getTaskWaitNum(auditqueue.getTaskguid(), true).getResult()));
                    map.put("身份证号", auditqueue.getIdentitycardnum());
                    map.put("办理业务", auditqueuetasktype.getTasktypename());
                    map.put("取号时间", EpointDateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                    fieldNames = new String[map.size()];
                    values = new Object[map.size()];
                    int num = 0;

                    for (Entry<String, String> entry : map.entrySet()) {
                        fieldNames[num] = entry.getKey();
                        values[num] = entry.getValue();
                        num++;
                    }
                    // 替换域
                    doc.getMailMerge().execute(fieldNames, values);
                    InputStream qrCode = QrcodeUtil.getQrCode(auditqueue.getFlowno(), 150, 150);
                    if (qrCode != null) {
                        DocumentBuilder outputStream = new DocumentBuilder(doc);
                        outputStream.moveToBookmark("条形码");
                        outputStream.insertImage(qrCode);
                    }
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    doc.save(outputStream, SaveFormat.DOC);// 保存成word
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                    long size = inputStream.available();

                    // 附件信息
                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                    frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                    frameAttachInfo.setAttachFileName("小票.doc");
                    frameAttachInfo.setCliengTag("小票打印");
                    frameAttachInfo.setUploadUserGuid("");
                    frameAttachInfo.setUploadUserDisplayName("");
                    frameAttachInfo.setUploadDateTime(new Date());
                    frameAttachInfo.setContentType("application/msword");
                    frameAttachInfo.setAttachLength(size);
                    dataJson.put("taskdocattachguid",
                            attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

                    outputStream.close();
                    inputStream.close();
                    if (qrCode != null) {
                        qrCode.close();
                    }
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
