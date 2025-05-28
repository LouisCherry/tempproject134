package com.epoint.lsznsb.bigscreen.rest;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.lsznsb.bigscreen.api.LsxIznsbBigScreen;

/**
 * 
 * 大屏相关接口
 * 
 * @author jyong
 * @version [版本号, 2025年1月10日]
 */
@RestController
@RequestMapping("/lsxbigscreen")
public class LsxBigScreenRestController
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IConfigService configService;
    @Autowired
    private IAuditQueue queueService;
    @Autowired
    private LsxIznsbBigScreen bigScreenService;

    /**
     * 取号实时分析
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/realTime", method = RequestMethod.POST)
    public String realTime(@RequestBody String params) {
        try {
            JSONObject dataJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            String centerguid = configService.getFrameConfigValue("AS_ZNSB_LSXCENTERGUID");
            if (StringUtil.isNotBlank(centerguid)) {
                List<Record> queuelist = bigScreenService.getTimesQueuenums(centerguid).getResult();
                if (queuelist != null && !queuelist.isEmpty()) {
                    for (Record record : queuelist) {
                        dataJson = new JSONObject();
                        dataJson.put("name", record.getStr("name"));
                        dataJson.put("value", record.getInt("value"));
                        list.add(dataJson);
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "系统参数AS_ZNSB_LSXCENTERGUID未配置", "");
            }

            return list.toString();
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 设备概况
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/equipmentProfile", method = RequestMethod.POST)
    public String equipmentProfile(@RequestBody String params) {
        try {
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject dataJson = new JSONObject();
            int qhjcount = 0;
            int ytjcount = 0;
            int jhpadcount = 0;
            int ckpcount = 0;

            String centerguid = configService.getFrameConfigValue("AS_ZNSB_LSXCENTERGUID");
            if (StringUtil.isNotBlank(centerguid)) {
                List<Record> equiplist = bigScreenService.getEquipmentList(centerguid).getResult();
                for (Record record : equiplist) {
                    if (QueueConstant.EQUIPMENT_TYPE_QHJ.equals(record.getStr("machinetype"))) {
                        qhjcount = record.getInt("machinecount").intValue();
                    }
                    else if (QueueConstant.EQUIPMENT_TYPE_CKP.equals(record.getStr("machinetype"))) {
                        ckpcount = record.getInt("machinecount").intValue();
                    }
                    else if (QueueConstant.EQUIPMENT_TYPE_YTJ.equals(record.getStr("machinetype"))) {
                        ytjcount = record.getInt("machinecount").intValue();
                    }
                    else if (QueueConstant.EQUIPMENT_TYPE_JHPAD.equals(record.getStr("machinetype"))) {
                        jhpadcount = record.getInt("machinecount").intValue();
                    }
                }
                dataJson.put("taken", qhjcount);
                dataJson.put("aio", ytjcount);
                dataJson.put("dialing", jhpadcount);
                dataJson.put("screen", ckpcount);
                list.add(dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "系统参数AS_ZNSB_LSXCENTERGUID未配置", "");
            }

            return list.toString();
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 大厅取号情况
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/markingProfile", method = RequestMethod.POST)
    public String markingProfile(@RequestBody String params) {
        try {
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject dataJson = new JSONObject();
            int total = 0;// 年度取号总量
            int today = 0;// 今日取号量
            int history = 0;// 历史取号量
            // 男性今日数量
            int malenum = 0;
            // 女性今日数量
            int femalenum = 0;
            // 男性历史数量
            int malenumhistory = 0;
            // 女性历史数量
            int femalenumhistory = 0;
            // 男性占比
            double maleper = 50;
            // 女性占比
            double femaleper = 50;
            // 总数
            int sumcount = 0;

            Date now = new Date();
            ZwfwRedisCacheUtil redis = null;
            String redisKey = "lsxZnsbBigScreen_markingProfile";
            String centerguid = configService.getFrameConfigValue("AS_ZNSB_LSXCENTERGUID");
            if (StringUtil.isNotBlank(centerguid)) {
                try {
                    redis = new ZwfwRedisCacheUtil(false);

                    today = queueService.getNowQueueCount(centerguid, EpointDateUtil.getBeginOfDate(now),
                            EpointDateUtil.getEndOfDate(now)).getResult();

                    Record todRecordSex = bigScreenService.getSexCountToday(centerguid).getResult();
                    if (todRecordSex != null) {
                        malenum = todRecordSex.getInt("man");
                        femalenum = todRecordSex.getInt("women");
                    }

                    // 历史表固定数据存储在redis中
                    if (StringUtil.isNotBlank(redis.getByString(redisKey))) {
                        JSONObject redisData = JSONObject.parseObject(redis.getByString(redisKey));
                        history = redisData.getIntValue("history");
                        malenumhistory = redisData.getIntValue("malenumhistory");
                        femalenumhistory = redisData.getIntValue("femalenumhistory");
                    }
                    else {
                        history = queueService.getHistoryQueueCount(centerguid,
                                EpointDateUtil.getBeginOfDateStr(EpointDateUtil.getYearOfDate(now) + "-01-01"),
                                EpointDateUtil.getEndOfDate(now)).getResult();

                        Record hisRecordSex = bigScreenService.getSexCountHistory(centerguid).getResult();
                        if (hisRecordSex != null) {
                            malenumhistory = hisRecordSex.getInt("man");
                            femalenumhistory = hisRecordSex.getInt("women");
                        }
                    }

                    total = today + history;

                    malenum = malenum + malenumhistory;
                    femalenum = femalenum + femalenumhistory;
                    sumcount = malenum + femalenum;
                    if (sumcount > 0) {
                        maleper = Double.valueOf(new DecimalFormat("0.00").format(malenum / (double) (sumcount) * 100));
                        femaleper = Double
                                .valueOf(new DecimalFormat("0.00").format(femalenum / (double) sumcount * 100));
                    }

                    if (StringUtil.isBlank(redis.getByString(redisKey))) {
                        JSONObject redisObj = new JSONObject();
                        redisObj.put("history", history);
                        redisObj.put("malenumhistory", malenumhistory);
                        redisObj.put("femalenumhistory", femalenumhistory);
                        redis.putByString(redisKey, redisObj.toJSONString(), 6 * 60 * 60);
                    }

                    dataJson.put("total", total);
                    dataJson.put("today", today);
                    dataJson.put("male", malenum);
                    dataJson.put("malepercent", maleper);
                    dataJson.put("female", femalenum);
                    dataJson.put("femalepercent", femaleper);
                    list.add(dataJson);
                }
                catch (Exception e) {
                    log.error("znsbbigscreen:" + redisKey + ",redis执行发生了异常", e);
                }
                finally {
                    if (redis != null) {
                        redis.close();
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "系统参数AS_ZNSB_LSXCENTERGUID未配置", "");
            }

            return list.toString();
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 年龄分布
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/ageDistribution", method = RequestMethod.POST)
    public String ageDistribution(@RequestBody String params) {
        try {
            List<JSONObject> list = new ArrayList<JSONObject>();
            // 0-18
            int level1 = 0;
            int level1history = 0;
            // 19-30
            int level2 = 0;
            int level2history = 0;
            // 31-40
            int level3 = 0;
            int level3history = 0;
            // 41-50
            int level4 = 0;
            int level4history = 0;
            // 51-60
            int level5 = 0;
            int level5history = 0;
            // >=61
            int level6 = 0;
            int level6history = 0;

            ZwfwRedisCacheUtil redis = null;
            String redisKey = "lsxZnsbBigScreen_ageDistribution";
            String centerguid = configService.getFrameConfigValue("AS_ZNSB_LSXCENTERGUID");
            if (StringUtil.isNotBlank(centerguid)) {
                try {
                    redis = new ZwfwRedisCacheUtil(false);

                    Record toRecordAge = bigScreenService.getAgeCountToday(centerguid).getResult();
                    if (toRecordAge != null) {
                        level1 = toRecordAge.getInt("level1");
                        level2 = toRecordAge.getInt("level2");
                        level3 = toRecordAge.getInt("level3");
                        level4 = toRecordAge.getInt("level4");
                        level5 = toRecordAge.getInt("level5");
                        level6 = toRecordAge.getInt("level6");
                    }

                    // 历史表固定数据存储在redis中
                    if (StringUtil.isNotBlank(redis.getByString(redisKey))) {
                        JSONObject redisData = JSONObject.parseObject(redis.getByString(redisKey));
                        level1history = redisData.getIntValue("level1history");
                        level2history = redisData.getIntValue("level2history");
                        level3history = redisData.getIntValue("level3history");
                        level4history = redisData.getIntValue("level4history");
                        level5history = redisData.getIntValue("level5history");
                        level6history = redisData.getIntValue("level6history");
                    }
                    else {
                        Record hisRecordAge = bigScreenService.getAgeCountHistory(centerguid).getResult();
                        if (hisRecordAge != null) {
                            level1history = hisRecordAge.getInt("level1");
                            level2history = hisRecordAge.getInt("level2");
                            level3history = hisRecordAge.getInt("level3");
                            level4history = hisRecordAge.getInt("level4");
                            level5history = hisRecordAge.getInt("level5");
                            level6history = hisRecordAge.getInt("level6");
                        }
                    }

                    level1 = level1 + level1history;
                    level2 = level2 + level2history;
                    level3 = level3 + level3history;
                    level4 = level4 + level4history;
                    level5 = level5 + level5history;
                    level6 = level6 + level6history;

                    if (StringUtil.isBlank(redis.getByString(redisKey))) {
                        JSONObject redisObj = new JSONObject();
                        redisObj.put("level1history", level1history);
                        redisObj.put("level2history", level2history);
                        redisObj.put("level3history", level3history);
                        redisObj.put("level4history", level4history);
                        redisObj.put("level5history", level5history);
                        redisObj.put("level6history", level6history);
                        redis.putByString(redisKey, redisObj.toJSONString(), 6 * 60 * 60);
                    }
                    JSONObject ageObj1 = new JSONObject();
                    ageObj1.put("name", "0-18岁");
                    ageObj1.put("value", level1);
                    JSONObject ageObj2 = new JSONObject();
                    ageObj2.put("name", "19-30岁");
                    ageObj2.put("value", level2);
                    JSONObject ageObj3 = new JSONObject();
                    ageObj3.put("name", "31-40岁");
                    ageObj3.put("value", level3);
                    JSONObject ageObj4 = new JSONObject();
                    ageObj4.put("name", "41-50岁");
                    ageObj4.put("value", level4);
                    JSONObject ageObj5 = new JSONObject();
                    ageObj5.put("name", "51-60岁");
                    ageObj5.put("value", level5);
                    JSONObject ageObj6 = new JSONObject();
                    ageObj6.put("name", "60岁以上");
                    ageObj6.put("value", level6);
                    list.add(ageObj1);
                    list.add(ageObj2);
                    list.add(ageObj3);
                    list.add(ageObj4);
                    list.add(ageObj5);
                    list.add(ageObj6);
                }
                catch (Exception e) {
                    log.error("znsbbigscreen:" + redisKey + ",redis执行发生了异常", e);
                }
                finally {
                    if (redis != null) {
                        redis.close();
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "系统参数AS_ZNSB_LSXCENTERGUID未配置", "");
            }

            return list.toString();
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 高频业务办理情况
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/handlingSituation", method = RequestMethod.POST)
    public String handlingSituation(@RequestBody String params) {
        try {
            List<JSONObject> list = new ArrayList<JSONObject>();
            List<Record> TaskListToday = new ArrayList<Record>();// 当天事项类别分类列表
            List<Record> TaskListHistory = new ArrayList<Record>();// 当月历史事项类别分类列表

            // 总数
            int counts = 0;
            Date now = new Date();
            ZwfwRedisCacheUtil redis = null;
            String redisKey = "lsxZnsbBigScreen_handlingSituation";
            String centerguid = configService.getFrameConfigValue("AS_ZNSB_LSXCENTERGUID");
            if (StringUtil.isNotBlank(centerguid)) {
                try {
                    redis = new ZwfwRedisCacheUtil(false);
                    TaskListToday = bigScreenService.getTaskListToday(centerguid, EpointDateUtil.getBeginOfDate(now),
                            EpointDateUtil.getEndOfDate(now)).getResult();

                    // 历史表固定数据存储在redis中
                    if (StringUtil.isNotBlank(redis.getByString(redisKey))) {
                        JSONObject redisData = JSONObject.parseObject(redis.getByString(redisKey));
                        TaskListHistory = JSON.parseArray(redisData.getString("history"), Record.class);
                    }
                    else {
                        TaskListHistory = bigScreenService.getTaskListHistory(centerguid,
                                EpointDateUtil
                                        .getBeginOfDateStr(EpointDateUtil.convertDate2String(now, "yyyy-MM") + "-01"),
                                EpointDateUtil.getEndOfDate(now)).getResult();
                    }

                    // 今日数据与历史数据合并计算
                    TaskListToday.addAll(TaskListHistory);
                    for (Record record : TaskListToday) {
                        counts += record.getInt("taskcount").intValue();
                    }

                    List<Record> newlist = mergeAndSortTasks(TaskListToday);
                    int i = 0;
                    for (Record record : newlist) {
                        JSONObject objCord = new JSONObject();
                        objCord.put("pm", "TOP" + i);
                        objCord.put("name", record.getStr("taskName"));
                        objCord.put("qhrs", record.getInt("taskcount"));
                        objCord.put("rszb", Double.valueOf(new DecimalFormat("0.00").format(record.getInt("taskcount").intValue() / (double) (counts) * 100)));
                        list.add(objCord);
                        
                        // 只返回12个
                        if(++i >= 12 ) {
                            break;
                        }
                    }

                    if (StringUtil.isBlank(redis.getByString(redisKey))) {
                        JSONObject redisObj = new JSONObject();
                        redisObj.put("history", TaskListHistory);
                        redis.putByString(redisKey, redisObj.toJSONString(), 6 * 60 * 60);
                    }
                }
                catch (Exception e) {
                    log.error("znsbbigscreen:" + redisKey + ",redis执行发生了异常", e);
                }
                finally {
                    if (redis != null) {
                        redis.close();
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "系统参数AS_ZNSB_LSXCENTERGUID未配置", "");
            }

            return list.toString();
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 大厅人流量
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/hallTraffic", method = RequestMethod.POST)
    public String hallTraffic(@RequestBody String params) {
        try {
            int today = 0;
            String hightime = "09:00";
            int highvalue = 0;

            JSONObject dataJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            List<JSONObject> data = new ArrayList<JSONObject>();
            String centerguid = configService.getFrameConfigValue("AS_ZNSB_LSXCENTERGUID");
            if (StringUtil.isNotBlank(centerguid)) {
                Date now = new Date();
                today = queueService.getNowQueueCount(centerguid, EpointDateUtil.getBeginOfDate(now),
                        EpointDateUtil.getEndOfDate(now)).getResult();
                List<Record> queuelist = bigScreenService.getTimesQueuenums(centerguid).getResult();
                if (queuelist != null && !queuelist.isEmpty()) {
                    for (Record record : queuelist) {
                        JSONObject recordobj = new JSONObject();
                        recordobj.put("name", record.getStr("name"));
                        recordobj.put("value", record.getInt("value"));
                        if (record.getInt("value") > highvalue) {
                            highvalue = record.getInt("value");
                            hightime = record.getStr("name");
                        }
                        data.add(recordobj);
                    }
                }
                dataJson.put("traffic", today);
                dataJson.put("time", hightime);
                dataJson.put("peak", highvalue);
                dataJson.put("data", data);
                list.add(dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "系统参数AS_ZNSB_LSXCENTERGUID未配置", "");
            }

            return list.toString();
        }
        catch (JSONException e) {
            log.info(e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    // 累加并自动排序
    public static List<Record> mergeAndSortTasks(List<Record> list) {
        // 创建一个 Map 来存储每个 taskguid 对应的 count 累加值
        Map<String, Record> mergedMap = new HashMap<String, Record>();
        // 遍历 list,并根据 taskguid 累加 count
        for (Record record : list) {
            String taskguid = record.getStr("TASKGUID");
            int count = record.getInt("taskcount").intValue();
            String taskname = record.getStr("TaskTypeName");

            Record recordMap = mergedMap.getOrDefault(taskguid, new Record());
            // 累加 count 值
            if (StringUtil.isNotBlank(recordMap.getInt("taskcount"))) {
                count = recordMap.getInt("taskcount").intValue() + count;
            }
            recordMap.put("taskcount", count);
            recordMap.put("taskName", taskname);
            mergedMap.put(taskguid, recordMap);
        }

        // 创建一个新的 List 来存储合并后的结果
        List<Record> resultList = new ArrayList<>();
        for (Map.Entry<String, Record> entry : mergedMap.entrySet()) {
            Record record = new Record();
            record.put("TASKGUID", entry.getKey());
            record.put("taskcount", entry.getValue().getStr("taskcount"));
            record.put("taskName", entry.getValue().getStr("taskName"));
            resultList.add(record);
        }

        // 根据 count 字段倒序排序
        resultList.sort((task1, task2) -> Integer.compare(task2.getInt("taskcount").intValue(),
                task1.getInt("taskcount").intValue()));
        return resultList;
    }
}
