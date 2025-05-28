package com.epoint.wsshow.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.ces.requesthiklog.api.IRequestHikLogService;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.wsxznsb.datascreen.api.IdataScreen;

@RestController
@RequestMapping("/wsshow")
public class WsShowRestController
{
    @Autowired
    private IdataScreen screenService;
    @Autowired
    private IRequestHikLogService hikService;
    @Autowired
    private IAuditOrgaServiceCenter centerService;
    @Autowired
    private IAuditOrgaWindowYjs windowService;
    @Autowired
    private IOuService ouService;
    // 辖区编码(汶上县)
    private String xiaquCode = ConfigUtil.getConfigValue("bigshow", "wensxiaqucode");
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /***
     * 
     * [汶上为民服务中心]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/servecenter", method = RequestMethod.POST)
    public String servecenter(@RequestBody String params, HttpServletRequest request) {
        try {
            // 取号总量
            int total = 0;
            // 当前等待量
            int waiting = 0;
            // 今日取号量
            int today = 0;
            // 今日办理量
            int processing = 0;
            JSONObject dataJson = new JSONObject();
            String centerGuid = getCenterGuid();
            if (StringUtil.isNotBlank(centerGuid)) {
                total = screenService.getHistoryQueueCount(centerGuid, "1").getResult();
                Record record = screenService.getCurrentDayQueue(centerGuid).getResult();
                if (record != null) {
                    today = record.getInt("qhcount");
                    total = total + today;
                    waiting = record.getInt("ddcount");
                    processing = record.getInt("blcount");
                }
            }

            dataJson.put("processing", processing);
            dataJson.put("today", today);
            dataJson.put("total", total);
            dataJson.put("waiting", waiting);

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            log.info("servecenter异常:" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     * [办件满意度]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/satisfaction", method = RequestMethod.POST)
    public String satisfaction(@RequestBody String params, HttpServletRequest request) {
        JSONObject dataJson = new JSONObject();
        try {
            // 总数量
            int sums = 0;
            // 非常满意
            int verysatisfied = 0;
            // 满意
            int satisfied = 0;
            // 基本满意
            int basically = 0;
            // 不满意
            int dissatisfied = 0;
            // 非常不满意
            int verydissatisfied = 0;
            // 综合满意度
            double total = 100d;
            String centerGuid = getCenterGuid();
            if (StringUtil.isNotBlank(centerGuid)) {
                Record record = screenService.getCurrentDayEvate(centerGuid).getResult();
                if (record != null) {
                    sums = record.getInt("counts");
                    verysatisfied = record.getInt("fcmy");
                    verysatisfied = verysatisfied + record.getInt("mrmy");
                    satisfied = record.getInt("my");
                    basically = record.getInt("jbmy");
                    dissatisfied = record.getInt("bmy");
                    verydissatisfied = record.getInt("fcbmy");
                    if (sums > 0) {
                        total = Double.valueOf(new DecimalFormat("0.00")
                                .format((verysatisfied + satisfied + basically) / (double) (sums) * 100));
                    }
                }
            }
            dataJson.put("basically", basically);
            dataJson.put("dissatisfied", dissatisfied);
            dataJson.put("satisfied", satisfied);
            dataJson.put("total", total);
            dataJson.put("verydissatisfied", verydissatisfied);
            dataJson.put("verysatisfied", verysatisfied);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (JSONException e) {
            log.info("satisfaction异常：" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /***
     * 
     * [每日考勤]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/attendance", method = RequestMethod.POST)
    public String attendance(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            List<Record> list1 = new ArrayList<>();
            List<Record> list2 = new ArrayList<>();
            List<Record> userlist = hikService.getAllUser();
            int index = 1;
            for (Record om : userlist) {
                Record rec = new Record();
                rec.set("name", om.get("username"));
                if (StringUtil.isNotBlank(om.get("time"))) {
                    rec.set("type", "出勤");
                    rec.set("typetip", 1);
                    if (index % 2 == 0) {
                        list2.add(rec);
                    }
                    else {
                        list1.add(rec);
                    }
                }
                // else {
                // rec.set("type", "缺勤");
                // rec.set("typetip", 1);
                // }

                index++;
            }
            dataJson.put("list1", list1);
            dataJson.put("list2", list2);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            log.info(e);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     * [中间部分]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/centerlist", method = RequestMethod.POST)
    public String centerlist(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            List<Record> departments = new ArrayList<>();
            List<Record> windows = new ArrayList<>();
            String centerGuid = getCenterGuid();
            if (StringUtil.isNotBlank(centerGuid)) {
                // 获取今日窗口取号列表
                List<Record> windowTodayQueueList = screenService.getWindowTodayQueueMonth(centerGuid).getResult();
                // 获取历史窗口取号列表
                List<Record> windowHistoryQueueList = screenService.getWindowHistoryQueueMonth(centerGuid).getResult();
                // 窗口月度办件情况统计
                SqlConditionUtil sqlCondition = new SqlConditionUtil();
                sqlCondition.eq("CenterGuid", centerGuid);
                sqlCondition.eq("IS_USEQUEUE", QueueConstant.Common_yes_String);
                List<AuditOrgaWindow> windowlist = windowService.getAllWindow(sqlCondition.getMap()).getResult();
                if (windowlist != null && !windowlist.isEmpty()) {
                    // 记录窗口数量
                    int singleWindowCount = 0;
                    // 遍历该中心窗口,今日与历史取号列表同一个窗口进行相加
                    for (AuditOrgaWindow orgawindow : windowlist) {
                        singleWindowCount = 0;
                        // 统计历史数据
                        if (windowHistoryQueueList != null && !windowHistoryQueueList.isEmpty()) {
                            for (Record hisRecord : windowHistoryQueueList) {
                                if (orgawindow.getRowguid().equals(hisRecord.get("HANDLEWINDOWGUID"))) {
                                    singleWindowCount = hisRecord.getInt("jhcount");
                                    break;
                                }
                            }
                        }
                        // 统计今日数据
                        if (windowTodayQueueList != null && !windowTodayQueueList.isEmpty()) {
                            for (Record todRecord : windowTodayQueueList) {
                                if (orgawindow.getRowguid().equals(todRecord.get("HANDLEWINDOWGUID"))) {
                                    singleWindowCount = singleWindowCount + todRecord.getInt("jhcount");
                                    break;
                                }
                            }
                        }
                        Record winobj = new Record();
                        winobj.set("name", orgawindow.getWindowname());
                        winobj.set("value", singleWindowCount);
                        windows.add(winobj);
                    }
                }

                // 部门月度办件统计
                // List<String> oustrList =
                // windowService.getoulistBycenterguid(centerGuid).getResult();
                List<String> oustrList = new ArrayList<String>();
                oustrList.addAll(windowService.getOUbyHall(centerGuid, "all").getResult());
                if (oustrList != null && !oustrList.isEmpty()) {
                    // 部门历史统计办件列表
                    List<Record> ouHistoryQueueList = screenService.getOuHistoryQueueMonth(centerGuid).getResult();
                    // 部门今日统计办件列表
                    List<Record> ouTodayQueueList = screenService.getOuTodayQueueMonth(centerGuid).getResult();
                    // 记录部门数量
                    int singleOuCount = 0;
                    FrameOu ou = null;
                    for (String oustr : oustrList) {
                        singleOuCount = 0;
                        // 统计部门历史数据
                        if (ouHistoryQueueList != null && !ouHistoryQueueList.isEmpty()) {
                            for (Record ouhisRecord : ouHistoryQueueList) {
                                if (oustr.equals(ouhisRecord.get("OUGUID"))) {
                                    singleOuCount = ouhisRecord.getInt("oujhcount");
                                    break;
                                }
                            }
                        }
                        // 统计部门今日数据
                        if (ouTodayQueueList != null && !ouTodayQueueList.isEmpty()) {
                            for (Record outodRecord : ouTodayQueueList) {
                                if (oustr.equals(outodRecord.get("OUGUID"))) {
                                    singleOuCount = singleOuCount + outodRecord.getInt("oujhcount");
                                    break;
                                }
                            }
                        }
                        Record ouobj = new Record();
                        ou = ouService.getOuByOuGuid(oustr);
                        if (ou != null) {
                            ouobj.set("name",
                                    StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                            ouobj.set("value", singleOuCount);
                            departments.add(ouobj);
                        }
                    }
                }
            }

            // 对窗口列表和部门列表进行按数量排序
            Collections.sort(windows, new Comparator<Record>()
            {
                @Override
                public int compare(Record o1, Record o2) {
                    try {
                        int projectcount1 = o1.get("value");
                        int projectcount2 = o2.get("value");
                        if (projectcount1 < projectcount2) {
                            return 1;
                        }
                        else if (projectcount1 > projectcount2) {
                            return -1;
                        }
                        else {
                            return 0;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });

            Collections.sort(departments, new Comparator<Record>()
            {
                @Override
                public int compare(Record o1, Record o2) {
                    try {
                        int projectcount1 = o1.get("value");
                        int projectcount2 = o2.get("value");
                        if (projectcount1 < projectcount2) {
                            return 1;
                        }
                        else if (projectcount1 > projectcount2) {
                            return -1;
                        }
                        else {
                            return 0;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });

            // 本年度取号总量统计
            Record record = screenService.getProjectTotal().getResult();
            // 今日办理量
            int total = 0;
            int today2 = 0;
            if (StringUtil.isNotBlank(centerGuid)) {
                total = screenService.getHistoryQueueCount(centerGuid, "1").getResult();
                Record record2 = screenService.getCurrentDayQueue(centerGuid).getResult();
                if (record2 != null) {
                    today2 = record2.getInt("qhcount");
                    total = total + today2;
                }
            }

            if (record != null) {
                dataJson.put("total", record.getInt("total") + total);
            }

            dataJson.put("department", departments);
            dataJson.put("window", windows);
            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            log.info("centerlist异常：" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     * [性别比例]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/sexratio", method = RequestMethod.POST)
    public String sexratio(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject dataJson = new JSONObject();
            // 男性数量
            int malenum = 0;
            // 女性数量
            int femalenum = 0;
            // 男性占比
            double maleper = 50;
            // 女性占比
            double femaleper = 50;
            // 总数
            int sumcount = 0;
            String centerGuid = getCenterGuid();
            // centerGuid = "25b4169f-a79d-4721-8cc6-7e578b6aa936";
            if (StringUtil.isNotBlank(centerGuid)) {
                Record todRecord = screenService.getSexCountToday(centerGuid).getResult();
                Record hisRecord = screenService.getSexCountHistory(centerGuid).getResult();
                if (todRecord != null) {
                    sumcount = todRecord.getInt("counts");
                    malenum = todRecord.getInt("man");
                    femalenum = todRecord.getInt("women");
                    if (hisRecord != null) {
                        sumcount = sumcount + hisRecord.getInt("counts");
                        malenum = malenum + hisRecord.getInt("man");
                        femalenum = femalenum + hisRecord.getInt("women");
                    }
                    if (sumcount > 0) {
                        maleper = Double.valueOf(new DecimalFormat("0.00").format(malenum / (double) (sumcount) * 100));
                        femaleper = Double
                                .valueOf(new DecimalFormat("0.00").format(femalenum / (double) sumcount * 100));
                    }
                }
            }

            dataJson.put("femalenum", femalenum);
            dataJson.put("femaleper", femaleper);
            dataJson.put("malenum", malenum);
            dataJson.put("maleper", maleper);

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            log.info("sexratio异常:" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     * [年龄分析]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/ageanalysis", method = RequestMethod.POST)
    public String ageanalysis(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONArray dataJson = new JSONArray();
            // 15-30
            int level1 = 0;
            // 31-40
            int level2 = 0;
            // 41-50
            int level3 = 0;
            // 51-60
            int level4 = 0;
            // 61-70
            int level5 = 0;
            // >=71
            int level6 = 0;
            // <15
            // int level7 = 0;
            // 总数
            int counts = 0;
            String centerGuid = getCenterGuid();
            // centerGuid = "25b4169f-a79d-4721-8cc6-7e578b6aa936";
            if (StringUtil.isNotBlank(centerGuid)) {
                Record toRecord = screenService.getAgeCountToday(centerGuid).getResult();
                Record hisRecord = screenService.getAgeCountHistory(centerGuid).getResult();
                if (toRecord != null) {
                    level1 = toRecord.getInt("level1");
                    level2 = toRecord.getInt("level2");
                    level3 = toRecord.getInt("level3");
                    level4 = toRecord.getInt("level4");
                    level5 = toRecord.getInt("level5");
                    level6 = toRecord.getInt("level6");
                    // level7 = toRecord.getInt("level7");
                    // counts = toRecord.getInt("counts");
                    if (hisRecord != null) {
                        level1 = level1 + hisRecord.getInt("level1");
                        level2 = level2 + hisRecord.getInt("level2");
                        level3 = level3 + hisRecord.getInt("level3");
                        level4 = level4 + hisRecord.getInt("level4");
                        level5 = level5 + hisRecord.getInt("level5");
                        level6 = level6 + hisRecord.getInt("level6");
                        // level7 = level6 + hisRecord.getInt("level7");
                        // counts = hisRecord.getInt("counts");
                    }
                    counts = level1 + level2 + level3 + level4 + level5 + level6;
                    Record rec1 = new Record();
                    rec1.set("color", "#46ff94");
                    rec1.set("name", "15~30岁");
                    rec1.set("per", 14.2);
                    rec1.set("value", level1);
                    Record rec2 = new Record();
                    rec2.set("color", "#46b3ff");
                    rec2.set("name", "31~40岁");
                    rec2.set("per", 14.2);
                    rec2.set("value", level2);
                    Record rec3 = new Record();
                    rec3.set("color", "#46f0ff");
                    rec3.set("name", "41~50岁");
                    rec3.set("per", 14.2);
                    rec3.set("value", level3);
                    Record rec4 = new Record();
                    rec4.set("color", "#fcc204");
                    rec4.set("name", "51~60岁");
                    rec4.set("per", 14.2);
                    rec4.set("value", level4);
                    Record rec5 = new Record();
                    rec5.set("color", "#ff7070");
                    rec5.set("name", "61~70岁");
                    rec5.set("per", 14.2);
                    rec5.set("value", level5);
                    Record rec6 = new Record();
                    rec6.set("color", "#ff7070");
                    rec6.set("name", ">70岁");
                    rec6.set("per", 14.2);
                    rec6.set("value", level6);
                    if (counts > 0) {
                        rec1.set("per", new DecimalFormat("0.00").format(level1 / (double) (counts) * 100));
                        rec2.set("per", new DecimalFormat("0.00").format(level2 / (double) (counts) * 100));
                        rec3.set("per", new DecimalFormat("0.00").format(level3 / (double) (counts) * 100));
                        rec4.set("per", new DecimalFormat("0.00").format(level4 / (double) (counts) * 100));
                        rec5.set("per", new DecimalFormat("0.00").format(level5 / (double) (counts) * 100));
                        rec6.set("per", new DecimalFormat("0.00").format(level6 / (double) (counts) * 100));
                    }
                    dataJson.add(rec1);
                    dataJson.add(rec2);
                    dataJson.add(rec3);
                    dataJson.add(rec4);
                    dataJson.add(rec5);
                    dataJson.add(rec6);
                }
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            log.info("ageanalysis异常：" + e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     * [服务渠道]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/servicechannel", method = RequestMethod.POST)
    public String servicechannel(@RequestBody String params, HttpServletRequest request) {
        try {
            // 大厅月度取号总量
            int hallcount = 0;
            // 微信
            int wechat = 0;
            String centerGuid = getCenterGuid();
            if (StringUtil.isNotBlank(centerGuid)) {
                hallcount = screenService.getHistoryQueueCount(centerGuid, "0").getResult();
                Record record = screenService.getCurrentDayQueue(centerGuid).getResult();
                if (record != null) {
                    hallcount = hallcount + record.getInt("qhcount");
                }
                // 3:微信
                wechat = screenService.getYuYueWechatCount(centerGuid, "3").getResult();
            }
            //微信（写死）
            wechat = 424;
            // 自助服务终端（写死）
            int selfmachieCount = 892;

            // 政务服务网
            int zwfwnet = 404;
            // 四者总数
            int total = hallcount + selfmachieCount + wechat + zwfwnet;
            JSONObject dataJson = new JSONObject();
            dataJson.put("autonum", zwfwnet);
            dataJson.put("autoper", new DecimalFormat("0.00").format((zwfwnet) / (double) (total) * 100));
            dataJson.put("hallnum", wechat);
            dataJson.put("hallper", new DecimalFormat("0.00").format((wechat) / (double) (total) * 100));
            dataJson.put("servernum", hallcount);
            dataJson.put("serverper", new DecimalFormat("0.00").format((hallcount) / (double) (total) * 100));
            dataJson.put("weixnum", selfmachieCount);
            dataJson.put("weixper", new DecimalFormat("0.00").format((selfmachieCount) / (double) (total) * 100));

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());

        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /***
     * 
     * [地图]
     * [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/map", method = RequestMethod.POST)
    public String map(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONArray dataJson = new JSONArray();
            List<Record> list = screenService.getProjectCount().getResult();
            
            for (Record rec : list) {
                if ("汶上县".equals(rec.getStr("name"))) {
                    Record rec1 = new Record();
                    rec1.set("month", rec.getInt("month"));
                    rec1.set("year", rec.getInt("year"));
                    rec1.set("name", "中都街道");
                    dataJson.add(rec1);
                }
                else if ("军屯乡".equals(rec.getStr("name"))) {
                    Record rec2 = new Record();
                    rec2.set("month", rec.getInt("month"));
                    rec2.set("year", rec.getInt("year"));
                    rec2.set("name", "军屯乡");
                    dataJson.add(rec2);
                }
                else if ("杨店镇".equals(rec.getStr("name"))) {
                    Record rec3 = new Record();
                    rec3.set("month", rec.getInt("month"));
                    rec3.set("year", rec.getInt("year"));
                    rec3.set("name", "杨店镇");
                    dataJson.add(rec3);
                }
                else if ("白石镇".equals(rec.getStr("name"))) {
                    Record rec4 = new Record();
                    rec4.set("month", rec.getInt("month"));
                    rec4.set("year", rec.getInt("year"));
                    rec4.set("name", "白石镇");
                    dataJson.add(rec4);
                }
                else if ("郭仓镇".equals(rec.getStr("name"))) {
                    Record rec5 = new Record();
                    rec5.set("month", rec.getInt("month"));
                    rec5.set("year", rec.getInt("year"));
                    rec5.set("name", "郭仓镇");
                    dataJson.add(rec5);
                }
                else if ("郭楼镇".equals(rec.getStr("name"))) {
                    Record rec6 = new Record();
                    rec6.set("month", rec.getInt("month"));
                    rec6.set("year", rec.getInt("year"));
                    rec6.set("name", "郭楼镇");
                    dataJson.add(rec6);
                }
                else if ("汶上街道".equals(rec.getStr("name"))) {
                    Record rec7 = new Record();
                    rec7.set("month", rec.getInt("month"));
                    rec7.set("year", rec.getInt("year"));
                    rec7.set("name", "汶上街道");
                    dataJson.add(rec7);
                }
                else if ("苑庄镇".equals(rec.getStr("name"))) {
                    Record rec8 = new Record();
                    rec8.set("month", rec.getInt("month"));
                    rec8.set("year", rec.getInt("year"));
                    rec8.set("name", "苑庄镇");
                    dataJson.add(rec8);
                }
                else if ("寅寺镇".equals(rec.getStr("name"))) {
                    Record rec9 = new Record();
                    rec9.set("month", rec.getInt("month"));
                    rec9.set("year", rec.getInt("year"));
                    rec9.set("name", "寅寺镇");
                    dataJson.add(rec9);
                }
                else if ("义桥镇".equals(rec.getStr("name"))) {
                    Record rec10 = new Record();
                    rec10.set("month", rec.getInt("month"));
                    rec10.set("year", rec.getInt("year"));
                    rec10.set("name", "义桥镇");
                    dataJson.add(rec10);
                }
                else if ("次邱镇".equals(rec.getStr("name"))) {
                    Record rec11 = new Record();
                    rec11.set("month", rec.getInt("month"));
                    rec11.set("year", rec.getInt("year"));
                    rec11.set("name", "次邱镇");
                    dataJson.add(rec11);
                }
                else if ("南站镇".equals(rec.getStr("name"))) {
                    Record rec12 = new Record();
                    rec12.set("month", rec.getInt("month"));
                    rec12.set("year", rec.getInt("year"));
                    rec12.set("name", "南站镇");
                    dataJson.add(rec12);
                }
                else if ("刘楼镇".equals(rec.getStr("name"))) {
                    Record rec13 = new Record();
                    rec13.set("month", rec.getInt("month"));
                    rec13.set("year", rec.getInt("year"));
                    rec13.set("name", "刘楼镇");
                    dataJson.add(rec13);
                }
                else if ("南旺镇".equals(rec.getStr("name"))) {

                    Record rec14 = new Record();
                    rec14.set("month", rec.getInt("month"));
                    rec14.set("year", rec.getInt("year"));
                    rec14.set("name", "南旺镇");
                    dataJson.add(rec14);
                }
                else if ("康驿镇".equals(rec.getStr("name"))) {
                    Record rec15 = new Record();
                    rec15.set("month", rec.getInt("month"));
                    rec15.set("year", rec.getInt("year"));
                    rec15.set("name", "康驿镇");
                    dataJson.add(rec15);
                }
            }

            if (!dataJson.contains("军屯乡")) {
                Record rec11 = new Record();
                rec11.set("month", 0);
                rec11.set("year", 0);
                rec11.set("name", "军屯乡");
                dataJson.add(rec11);

            }
            log.info("wsshowmap:"+dataJson.toString());

            return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
        }
        catch (Exception e) {
            log.info(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 
     * 天气
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getWeather", method = RequestMethod.POST)
    public String getWeather(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("=======开始调用getWeather接口=======");
        try {
            // 1.1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 默认获取石家庄市天气
                URL url = new URL("http://www.weather.com.cn/data/cityinfo/101090101.html");
                InputStreamReader isReader = new InputStreamReader(url.openStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isReader);// 采用缓冲式读入
                String str = null;
                String strReturn = null;
                while ((str = br.readLine()) != null) {
                    strReturn = str;
                }
                br.close();// 网上资源使用结束后，数据流及时关闭
                isReader.close();
                // 获取总数
                JSONObject dataJson = new JSONObject();
                dataJson.put("weather", strReturn);
                log.info("=======结束调用getWeather接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取天气成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getWeather接口=======");
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }
        }
        catch (Exception e) {
            log.info("=======getWeather初始化数据接口调用异常=======");
            e.printStackTrace();
        }
        log.info("=======结束调用getWeather接口=======");
        return JsonUtils.zwdtRestReturn("0", "获取天气失败：", "");
    }

    public String getCenterGuid() {
        String centerGuid = "141b93a6-6532-4462-82b1-f7e3051e9796";
        AuditOrgaServiceCenter center = centerService.getAuditServiceCenterByBelongXiaqu(xiaquCode);
        if (center != null) {
            centerGuid = center.getRowguid();
        }
        return centerGuid;
    }
}
