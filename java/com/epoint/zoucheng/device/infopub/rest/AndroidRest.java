package com.epoint.zoucheng.device.infopub.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditqueue.auditqueuetimeset.domain.AuditQueueTimeset;
import com.epoint.basic.auditqueue.auditqueuetimeset.inter.IAuditQueueTimeset;
import com.epoint.basic.auditqueue.auditznsbappconfig.domain.AuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbappconfig.inter.IAuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbterminalapp.domain.AuditZnsbTerminalApp;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.spring.util.SpringContextUtil;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.impl.InfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayterminal.impl.InfopubPlayterminalService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.ProgramMaterial;

@RestController
@RequestMapping("/androidRestMP")
public class AndroidRest
{
    @Autowired
    private IInfopubProgramService infopubProgramService;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditZnsbTerminalApp appservice;
    @Autowired
    private IAuditQueueTimeset timesetservice;
    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private IConfigService configservice;
    @Autowired
    private IAuditZnsbAppConfig appConfigService;
    @Autowired
    private IAuditOrgaWorkingDay workingdayService;
   
    /**
     * app更新
     * 
     * @params params
     * @return
     * 
     * 
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
            AuditZnsbAppConfig appConfig = appConfigService.getConfigbyMacrowguid(macrowguid, fieldstr1).getResult();

            // 判断该设备是否使用个性化的APP
            if (StringUtil.isNotBlank(centerguid)) {
                // 设备与APP关系表里是否存在
                if (StringUtil.isNotBlank(appConfig)) {
                    String fieldstr2 = "AppInfo,UpdateTime,AppType,CenterGuid,CenterName,AppAttachGuid,IsUniversal";
                    AuditZnsbTerminalApp app = appservice.getAppByGuid(appConfig.getAppguid(), fieldstr2).getResult();
                    if (StringUtil.isNotBlank(app.getAppattachguid())) {
                        if (Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))) {

                            List<FrameAttachInfo> attachinfolist = attachservice
                                    .getAttachInfoListByGuid(app.getAppattachguid());
                            if (attachinfolist != null && !attachinfolist.isEmpty()) {
                                apppath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid="
                                        + attachinfolist.get(0).getAttachGuid();
                            }
                        }
                    }
                }
                else {
                    AuditZnsbTerminalApp app = appservice
                            .getDetailbyApptype(QueueConstant.TerminalApp_Type_Window, centerguid).getResult();
                    if (StringUtil.isNotBlank(app)) {
                        if (Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))) {

                            if (StringUtil.isNotBlank(app.getAppattachguid())) {
                                List<FrameAttachInfo> attachinfolist = attachservice
                                        .getAttachInfoListByGuid(app.getAppattachguid());
                                if (attachinfolist != null && !attachinfolist.isEmpty()) {
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
     * 自动开关机设置
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/timeset", method = RequestMethod.POST)
    public String queueTimeSet(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String offhour = "";
            String offminute = "";
            String onhour = "";
            String onminute = "";
            String hallguid = "";
            int ts = 0;
            int tsStart = 0;
            JSONObject dataJson = new JSONObject();
            // 根据macaddress获取hallguid
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                hallguid = equipmentservice.getDetailbyMacaddress(macaddress).getResult().getHallguid();
                if (StringUtil.isNotBlank(hallguid)) {
                    AuditQueueTimeset timeset = timesetservice.getDetailByHallguid(hallguid).getResult();
                    if (StringUtil.isNotBlank(timeset)) {
                        Calendar cal = Calendar.getInstance();
                        // 计算分钟
                        ts = timeset.getEndtime_h() * 60 + timeset.getEndtime_m() - (cal.get(Calendar.HOUR_OF_DAY)) * 60
                                - (cal.get(Calendar.MINUTE));
                        if (ts <= 5) {
                            offhour = "";
                            offminute = "";
                            onhour = "";
                            onminute = "";
                        }
                        else {
                            offhour = StringUtil.getNotNullString(ts / 60);
                            offminute = StringUtil.getNotNullString(ts - (ts / 60) * 60);
                            // 这边加入判断工作日的逻辑
                            boolean flag = true;
                            flag = workingdayService
                                        .isWorkingDay(equipment.getCenterguid(), EpointDateUtil
                                                .convertString2DateAuto(EpointDateUtil.getCurrentDate("yyyy-MM-dd")))
                                        .getResult();
                            // 把时间计算出来
                            boolean behindflag = true;
                            int sumday = 0;
                            for (int i = 0; i < 10; i++) {
                                behindflag = workingdayService
                                        .isWorkingDay(equipment.getCenterguid(),
                                                EpointDateUtil.convertString2DateAuto(getFetureDate(i + 1)))
                                        .getResult();
                                if (!behindflag) {
                                    sumday++;
                                }
                                else {
                                    break;
                                }
                            }
                            if (!flag) {
                                // 如果不是工作日,立马关机
                                offhour = "0";
                                offminute = "0";
                               
                                tsStart = (sumday + 1) * 24 * 60 + timeset.getStarttime_h() * 60 + timeset.getStarttime_m()
                                - cal.get(Calendar.HOUR_OF_DAY) * 60 - cal.get(Calendar.MINUTE);
                            }
                            else{
                                tsStart = (sumday + 1) * 24 * 60 + timeset.getStarttime_h() * 60 + timeset.getStarttime_m()
                                - timeset.getEndtime_h() * 60 - timeset.getEndtime_m();
                            }
                            onhour = StringUtil.getNotNullString(tsStart / 60);
                            onminute = StringUtil.getNotNullString(tsStart - (tsStart / 60) * 60);
                        }
                    }

                }
            }
            dataJson.put("offhour", offhour);
            dataJson.put("offminute", offminute);
            dataJson.put("onhour", onhour);
            dataJson.put("onminute", onminute);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        return EpointDateUtil.convertDate2String(today, "yyyy-MM-dd");
    }

    /**
     * 初始化
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params, HttpServletRequest request) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String ip = obj.getString("ip");
            String networkmask = obj.getString("networkmask");
            String gateway = obj.getString("gateway");
            String dns = obj.getString("dns");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setIpaddress(ip);
                equipment.setNetworkmask(networkmask);
                equipment.setGateway(gateway);
                equipment.setDns(dns);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_CKP);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    String config = handleConfigservice.getFrameConfig("AS_RabbitMQ", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("rabbitmq", config);
                        CachingConnectionFactory conn = null;
                        if (SpringContextUtil.getApplicationContext().containsBean("connectionFactory")) {
                            conn = SpringContextUtil.getBean("connectionFactory");
                            dataJson.put("rabbitmquser", conn.getRabbitConnectionFactory().getUsername());
                            dataJson.put("rabbitmqpassword", conn.getRabbitConnectionFactory().getPassword());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "RabbitMQ未配置！", "");
                        }

                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "系统参数AS_RabbitMQ未配置！", "");
                    }
                    config = handleConfigservice.getFrameConfig("AS_IS_ENABLE_APPLOG", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("recordlog", config);
                    }
                    else {
                        // 默认不记录日志
                        dataJson.put("recordlog", "0");
                    }
                    config = handleConfigservice.getFrameConfig("AS_IS_USE_COMPANYSTB", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("companystb", config);
                    }
                    else {
                        // 默认公司盒子
                        dataJson.put("companystb", "0");
                    }

                    config = configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE");
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("epointdevice", config);
                    }
                    else {
                        // 默认不启用硬件管理平台
                        dataJson.put("epointdevice", "0");
                    }
                    
                    if(StringUtil.isNotBlank(equipment.getStr("ispublish"))){
                        dataJson.put("ispublish", equipment.getStr("ispublish"));
                    }else{
                        dataJson.put("ispublish", 0);
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
   
                    dataJson.put("machinetime", sdf.format(new Date()));
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

    /*
     * 信息发布接口
     */
    @RequestMapping(value = "/programlist", method = RequestMethod.POST)
    public String programlist(@RequestBody String params) {
        List<JSONObject> infolist = new ArrayList<JSONObject>();
        List<JSONObject> programlist = new ArrayList<JSONObject>();
        JSONObject dataJson = new JSONObject();
        JSONObject infolistJson = new JSONObject();
        JSONObject programlistJson = new JSONObject();
        JSONObject json = JSON.parseObject(params);
        JsonUtils.checkUserAuth(json.getString("token"));
        JSONObject obj = (JSONObject) json.get("params");
        String macaddress = obj.getString("macaddress");
        String playguid = new InfopubPlayterminalService().getPlayGuid(macaddress);
        String startHour = "";
        String startMinute = "";
        String endHour = "";
        String endMinute = "";
        String fileNameGuid = "";
        //获取当前节目单下的所有节目
        List<InfopubPlayprogram> infopubPlayprogramList = new InfopubPlayprogramService().getAll(playguid);
        //获取出所有的开关机时间
        int[] array = new int[infopubPlayprogramList.size() * 2];
        for (int i = 0; i < infopubPlayprogramList.size(); i++) {
            int startTime = infopubPlayprogramList.get(i).getStarttimehour() * 60
                    + infopubPlayprogramList.get(i).getStarttimeminute();
            int endTime = infopubPlayprogramList.get(i).getEndtimehour() * 60
                    + infopubPlayprogramList.get(i).getEndtimeminute();
            array[i] = startTime;
            array[i + infopubPlayprogramList.size()] = endTime;
        }
        //对数组进行去重
        //用来记录去除重复之后的数组长度和给临时数组作为下标索引
        int t = 0;
        int[] tempArr = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            boolean isTrue = true;
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] == array[j]) {
                    isTrue = false;
                    break;
                }
            }
            if (isTrue) {
                tempArr[t] = array[i];
                t++;
            }
        }
        int[] newArr = new int[t];
        //用arraycopy方法将刚才去重的数组拷贝到新数组并返回
        System.arraycopy(tempArr, 0, newArr, 0, t);
        Arrays.sort(newArr);
        //将各个时间段的节目遍历出来，传送出去给移动端
        for (int i = 0; i < newArr.length - 1; i++) {
            infolistJson = new JSONObject();
            programlist = new ArrayList<JSONObject>();
            if (newArr[i] / 60 < 10) {
                startHour = "0" + newArr[i] / 60;
            }
            else {
                startHour = newArr[i] / 60 + "";
            }

            if (newArr[i + 1] / 60 < 10) {
                endHour = "0" + newArr[i + 1] / 60;
            }
            else {
                endHour = newArr[i + 1] / 60 + "";
            }
            if (newArr[i] % 60 < 10) {
                startMinute = "0" + newArr[i] % 60;
            }
            else {
                startMinute = newArr[i] % 60 + "";
            }
            if (newArr[i + 1] % 60 < 10) {
                endMinute = "0" + newArr[i + 1] % 60;
            }
            else {
                endMinute = newArr[i + 1] % 60 + "";
            }
            infolistJson.put("start", startHour + ":" + startMinute);
            infolistJson.put("end", endHour + ":" + endMinute);

            for (InfopubPlayprogram info : infopubPlayprogramList) {
                programlistJson = new JSONObject();
                InfopubProgram infopubPrograms = infopubProgramService.find(info.getProgramguid());
                List<ProgramMaterial> materialList = JSON.parseArray(infopubPrograms.getContent(),
                        ProgramMaterial.class);
                //获取视频guid
                String contentMp4 = materialList.get(0).getContent();
                fileNameGuid = contentMp4.substring(contentMp4.lastIndexOf('/') + 1);
                String isMp4 = fileNameGuid.substring(fileNameGuid.length() - 3, fileNameGuid.length());
                int starttime1 = info.getStarttimehour() * 60 + info.getStarttimeminute();
                int endtime1 = info.getEndtimehour() * 60 + info.getEndtimeminute();
                String prefixon = infopubPrograms.getPath().split("ylzwfw")[0];
                String onlinePath = prefixon + "ylzwfw/auditznsbcontrol/infopub/file/" + infopubPrograms.getRowguid() + "/"
                        + infopubPrograms.getRowguid();
                String contentMp4Path = infopubPrograms.getPath().split(".zip")[0] + "/" + fileNameGuid;
                if (starttime1 <= newArr[i] && endtime1 >= newArr[i + 1] && (i + 1) < newArr.length) {
                    programlistJson.put("name", infopubPrograms.getProgramname());
                    programlistJson.put("guid", infopubPrograms.getRowguid());
                    programlistJson.put("showtime", info.getShowtime());
                    infolistJson.put("vol", info.getVol());
                    programlistJson.put("onlinePath", onlinePath);
                    programlistJson.put("MD5", infopubPrograms.getMd5());
                    if ("mp4".equals(isMp4)) {
                        programlistJson.put("path", contentMp4Path);
                        programlistJson.put("isMp4", "1");
                    }
                    else {
                        programlistJson.put("path", infopubPrograms.getPath());
                        programlistJson.put("isMp4", "0");
                    }
                    programlist.add(programlistJson);
                }
            }
            infolistJson.put("programlist", programlist);
            infolist.add(infolistJson);
        }
        dataJson.put("infolist", infolist);
        return JsonUtils.zwdtRestReturn("1", "", dataJson);

    }
}
