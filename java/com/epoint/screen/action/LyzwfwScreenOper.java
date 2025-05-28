package com.epoint.screen.action;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.screen.api.ILyDataShow;
import com.epoint.screen.api.ILyFrameOu;
import com.epoint.screen.api.ILyScreen02;


@RestController
@RequestMapping("/lyzwfwscreenoper")
public class LyzwfwScreenOper
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private ILyScreen02 iLyScreen02;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private ILyDataShow iLyDataShow;
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;

    @Autowired
    private ILyFrameOu iLyFrameOu;

    private DecimalFormat df = new DecimalFormat("######0.00");

    @RequestMapping(value = "/getscreenoper", method = RequestMethod.POST)
    public String getInformationDetailByRowguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getscreenoper接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String[] str = params.split("&");
             String starttime = (String) jsonObject.get("starttime");
            String endtime =  (String) jsonObject.get("endtime");
            //String starttime = str[0].substring(str[0].indexOf("="), str[0].length());
            //String endtime = str[1].substring(str[1].indexOf("="), str[1].length());
            String isjd = str[2].substring(str[2].indexOf("=")+1, str[2].length());
            if (StringUtil.isNotBlank(starttime) && !"=".equals(starttime)) {
                starttime = starttime.split("=")[1];
            }
            else {
                starttime = "";
            }
            if (StringUtil.isNotBlank(endtime) && !"=".equals(endtime)) {
                endtime = endtime.split("=")[1];
            }
            else {
                endtime = "";
            }
            JSONObject dataJson = new JSONObject();
            //申请人企业行业类型统计饼图
            Integer enterprisecount = iLyScreen02.getAllCountByType(starttime, endtime, 10).getResult();
            Integer enterprisesum = enterprisecount;
            List<JSONObject> enterpriseType = new ArrayList<JSONObject>();
            List<Record> enterpriseIndustrytypeList = iLyScreen02.getEnterpriseIndustrytype(starttime, endtime, "4")
                    .getResult();
            for (Record record : enterpriseIndustrytypeList) {
                JSONObject bean = new JSONObject();
                bean.put("name", iCodeItemsService.getItemTextByCodeName("行业类型", record.get("INDUSTRYTYPE")));
                bean.put("value", record.get("count"));
                String rate = ((Double.parseDouble((record.get("count") + "")) / enterprisesum) * 100 + "");
                if (rate.length() >= 5) {
                    rate = rate.substring(0, 5);
                }
                bean.put("rate", rate);
                enterprisecount = enterprisecount - Integer.parseInt(record.get("count") + "");
                enterpriseType.add(bean);
            }
            JSONObject otherenterprise = new JSONObject();
            otherenterprise.put("name", "其他");
            otherenterprise.put("value", enterprisecount);
            if (enterprisecount == 0) {
                otherenterprise.put("rate", "0.00");
            }
            else {
                String rate = (enterprisecount * 1.0 / enterprisesum * 100 + "");
                if (rate.length() >= 5) {
                    rate = rate.substring(0, 5);
                }
                otherenterprise.put("rate", rate);
            }
            enterpriseType.add(otherenterprise);
            dataJson.put("enterpriseType", enterpriseType);
            // 申请人个体工商户行业类型统计
            Integer individualcount = iLyScreen02.getAllCountByType(starttime, endtime, 20).getResult();
            Integer individualsum = individualcount;
            List<JSONObject> individualType = new ArrayList<JSONObject>();
            List<Record> individualTypeList = iLyScreen02.getindividualType(starttime, endtime, "4").getResult();
            for (Record record : individualTypeList) {
                JSONObject bean = new JSONObject();
                bean.put("name", iCodeItemsService.getItemTextByCodeName("行业类型", record.get("INDUSTRYTYPE")));
                bean.put("value", record.get("count"));
                String rate = ((Double.parseDouble((record.get("count") + "")) / individualsum) * 100 + "");
                if (rate.length() >= 5) {
                    rate = rate.substring(0, 5);
                }
                bean.put("rate", rate);
                individualcount = individualcount - Integer.parseInt(record.get("count") + "");
                individualType.add(bean);
            }
            JSONObject otherindividual = new JSONObject();
            otherindividual.put("name", "其他");
            otherindividual.put("value", individualcount);
            if (individualcount == 0) {
                otherindividual.put("rate", "0.00");
            }
            else {
                String rate = (individualcount * 1.0 / individualsum * 100 + "");
                if (rate.length() >= 5) {
                    rate = rate.substring(0, 5);
                }
                otherindividual.put("rate", rate);
            }
            individualType.add(otherindividual);
            dataJson.put("individualType", individualType);
            // 企业设立统计数量分布
            String[] townList = {"三十岗乡", "大杨镇", "杏花村", "三孝口", "四里河", "亳州路", "双岗", "逍遥津", "海棠", "林店", "杏林" };
            List<JSONObject> enterpriseNumber = new ArrayList<JSONObject>();
            for (String town : townList) {
                JSONObject bean = new JSONObject();
                bean.put("name", town);
                bean.put("value", iLyScreen02.getSetupCountByType(starttime, endtime, 10, town).getResult());
                enterpriseNumber.add(bean);
            }
            dataJson.put("enterpriseNumber", enterpriseNumber);
            List<JSONObject> individualNumber = new ArrayList<JSONObject>();
            for (String town : townList) {
                JSONObject bean = new JSONObject();
                bean.put("name", town);
                bean.put("value", iLyScreen02.getSetupCountByType(starttime, endtime, 20, town).getResult());
                individualNumber.add(bean);
            }
            dataJson.put("individualNumber", individualNumber);
            // 办件人年龄段分布
            List<JSONObject> ageCount = new ArrayList<JSONObject>();
            String[] ages = {"1950", "195", "196", "197", "198", "199", "200" };
            String[] ranges = {"1950之前", "50后", "60后", "70后", "80后", "90后", "00后" };
            for (int i = 0; i < ages.length; i++) {
                JSONObject bean = new JSONObject();
                bean.put("name", ranges[i]);
                int male = iLyScreen02.getAgeGroupNum(starttime, endtime, 1, ages[i]).getResult();
                bean.put("male", male);
                int famale = iLyScreen02.getAgeGroupNum(starttime, endtime, 2, ages[i]).getResult();
                bean.put("famale", famale);
                bean.put("count", male + famale);
                ageCount.add(bean);
            }
            dataJson.put("ageCount", ageCount);
            
            if(isjd.equals("true")){
            	// 办件人年龄段分布(街道)
            	List<JSONObject> ageJDCount = new ArrayList<JSONObject>();
            	for (int i = 0; i < ages.length; i++) {
            		JSONObject bean = new JSONObject();
            		bean.put("name", ranges[i]);
            		int male = iLyScreen02.getJDAgeGroupNum(starttime, endtime, 1, ages[i]).getResult();
            		bean.put("male", male);
            		int famale = iLyScreen02.getJDAgeGroupNum(starttime, endtime, 2, ages[i]).getResult();
            		bean.put("famale", famale);
            		bean.put("total", male + famale);
            		ageJDCount.add(bean);
            	}
            	dataJson.put("ageJDCount", ageJDCount);
            	//街道户口
                Integer jdhefeicount = iLyDataShow.getJDHomeCount("1").getResult();
                dataJson.put("jdhefeicount", jdhefeicount);
                Integer jdnothefeicount = iLyDataShow.getJDHomeCount("2").getResult();
                dataJson.put("jdnothefeicount", jdnothefeicount);
            }

            //获取总注册资产
            Double number1 = iLyScreen02.getRegisteredcapital(starttime, endtime, 10).getResult();
            dataJson.put("number1", number1);
            Double number2 = iLyScreen02.getRegisteredcapital(starttime, endtime, 20).getResult();
            dataJson.put("number2", number2);
            //企业设立数量
            int set1 = iLyScreen02.getSetupSumByType(starttime, endtime, 10).getResult();
            dataJson.put("set1", set1);
            int set2 = iLyScreen02.getSetupSumByType(starttime, endtime, 20).getResult();
            dataJson.put("set2", set2);
            //全区招牌数
            int signcount = iLyScreen02.getSignCount(starttime, endtime).getResult();
            dataJson.put("signcount", signcount);
            //户口
            Integer hefeicount = iLyDataShow.getHomeCount("1").getResult();
            dataJson.put("hefeicount", hefeicount);
            Integer nothefeicount = iLyDataShow.getHomeCount("2").getResult();
            dataJson.put("nothefeicount", nothefeicount);

            Integer bsgscont = iLyDataShow.isBelongTo("3").getResult();
            dataJson.put("bsgscont", bsgscont);
            Integer shenneicount = iLyDataShow.isBelongTo("1").getResult();
            dataJson.put("shenneicount", shenneicount);
            Integer count = iLyDataShow.isBelongTo("2").getResult();
            Integer shenwaicount = count - shenneicount;
            dataJson.put("shenwaicount", shenwaicount);

            List<JSONObject> hotTaskJsonList = new ArrayList<JSONObject>();
            List<AuditProject> hotTaskTop5 = iLyDataShow.getHotTaskTop5ByTime(starttime, endtime,isjd).getResult();
            for (AuditProject auditProject : hotTaskTop5) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", auditProject.getProjectname());
                jsonObject1.put("value", auditProject.get("count"));
                hotTaskJsonList.add(jsonObject1);
            }
            dataJson.put("hotMatter", hotTaskJsonList);
            // 近12月办件量走势
            List<String> monthList = getMonthList(
                    EpointDateUtil.convertDate2String(EpointDateUtil.addYear(new Date(), -1)),
                    EpointDateUtil.convertDate2String(EpointDateUtil.addDay(new Date(), 1)));
            List<JSONObject> banjianSumList = new ArrayList<JSONObject>();
            for (String month : monthList) {
                JSONObject banjianJson = new JSONObject();
                Integer banjianList = iLyDataShow.getBanjianCountByMonth(month,isjd).getResult();
                String time = month.substring(2, month.length()).replace("-", "/");
                StringBuilder sb = new StringBuilder(time);
                if ("0".equals(time.charAt(3) + "")) {
                    sb.replace(3, 4, "");
                }
                banjianJson.put("time", sb.toString());
                banjianJson.put("value", banjianList);
                banjianSumList.add(banjianJson);
            }
            dataJson.put("monthlyDo", banjianSumList);
            //取号 量
            List<JSONObject> queueList = new ArrayList<JSONObject>();
            for (String month : monthList) {
                JSONObject queueJson = new JSONObject();
                Integer banjianList = iLyDataShow.getQueueCountByMonth(month).getResult();
                String time = month.substring(2, month.length()).replace("-", "/");
                StringBuilder sb = new StringBuilder(time);
                if ("0".equals(time.charAt(3) + "")) {
                    sb.replace(3, 4, "");
                }
                queueJson.put("time", sb.toString());
                queueJson.put("value", banjianList);
                queueList.add(queueJson);
            }
            dataJson.put("monthlyTake", queueList);
            
            List<Record> windowtop10list = iLyDataShow.getWindowTop10ByTime(starttime, endtime,isjd).getResult();
            List<JSONObject> windowtop9 = new ArrayList<JSONObject>();
            List<JSONObject> windowtop10 = new ArrayList<JSONObject>();
            int a = 1;
            if (windowtop10list.size() < 9) {
                for (int i = 0; i < windowtop10list.size(); i++) {
                    JSONObject window = new JSONObject();
                    AuditOrgaWindow auditOrgaWindow = iAuditOrgaWindow
                            .getWindowByWindowGuid(windowtop10list.get(i).getStr("windowguid")).getResult();
                    window.put("window", auditOrgaWindow.getWindowname() + auditOrgaWindow.getWindowno());
                    window.put("value", windowtop10list.get(i).get("count"));
                    window.put("order", i + 1);
                    windowtop9.add(window);
                }
                for (int i = windowtop10list.size(); i < 9; i++) {
                    JSONObject window = new JSONObject();
                    window.put("window", "");
                    window.put("value", "");
                    window.put("order", i + 1);
                    windowtop9.add(window);
                }

            }
            else {
                for (int i = 0; i < 9; i++) {
                    JSONObject window = new JSONObject();
                    AuditOrgaWindow auditOrgaWindow = iAuditOrgaWindow
                            .getWindowByWindowGuid(windowtop10list.get(i).getStr("windowguid")).getResult();
                    window.put("window", auditOrgaWindow.getWindowname() + auditOrgaWindow.getWindowno());
                    window.put("value", windowtop10list.get(i).get("count"));
                    window.put("order", i + 1);
                    windowtop9.add(window);
                }
            }
            if (windowtop10list.size() < 10) {
                for (int i = 0; i < windowtop10list.size(); i++) {
                    JSONObject window = new JSONObject();
                    AuditOrgaWindow auditOrgaWindow = iAuditOrgaWindow
                            .getWindowByWindowGuid(windowtop10list.get(i).getStr("windowguid")).getResult();
                    window.put("window", auditOrgaWindow.getWindowname() + auditOrgaWindow.getWindowno());
                    window.put("value", windowtop10list.get(i).get("count"));
                    window.put("order", i + 1);
                    windowtop10.add(window);
                }
                for (int i = windowtop10list.size(); i < 10; i++) {
                    JSONObject window = new JSONObject();
                    window.put("window", "");
                    window.put("value", "");
                    window.put("order", i + 1);
                    windowtop10.add(window);
                }
            }
            else {
                for (Record record : windowtop10list) {
                    JSONObject window = new JSONObject();
                    AuditOrgaWindow auditOrgaWindow = iAuditOrgaWindow.getWindowByWindowGuid(record.get("windowguid"))
                            .getResult();
                    window.put("window", auditOrgaWindow.getWindowname() + auditOrgaWindow.getWindowno());
                    window.put("value", record.get("count"));
                    window.put("order", a);
                    windowtop10.add(window);
                    a++;
                }
            }
            dataJson.put("windowRank", windowtop10);

            //部门前10业务量
            List<Record> outop10list = iLyDataShow.getOuTop10ByTime(starttime, endtime,isjd).getResult();
            List<JSONObject> outop10 = new ArrayList<JSONObject>();
            double maxOuCount = 0;
            if (outop10list != null && outop10list.size() > 0) {
                maxOuCount = Double.parseDouble(outop10list.get(0).get("count").toString());
                for (int i = 0; i < outop10list.size(); i++) {
                    JSONObject ou = new JSONObject();
                    FrameOu frameOu = iLyFrameOu.getFrameOuByOuguid(outop10list.get(i).get("ouguid").toString())
                            .getResult();
                    ou.put("indexs", i + 1);
                    ou.put("ouname", frameOu.getOuname());
                    ou.put("oucount", outop10list.get(i).get("count"));
                    //ou.put("percent", df.format((Double.parseDouble(outop10list.get(i).get("count").toString()) / maxOuCount) * 100));
                    outop10.add(ou);
                }
            }

            dataJson.put("outop10list", outop10);

            //线下办件总量
            Integer xianxiaCount = iLyDataShow.getXianxiaCount(starttime, endtime,isjd).getResult();
            //办件总量
            Integer totalCount = iLyDataShow.getTotalCount(starttime, endtime,isjd).getResult();
            dataJson.put("xianshangCount", totalCount - xianxiaCount);
            dataJson.put("xianxiaCount", xianxiaCount);
            double xianshangrent = ((double) (totalCount - xianxiaCount) / (double) totalCount) * 100;
            DecimalFormat df = new DecimalFormat("#.0");
            dataJson.put("xianshangrent", df.format(xianshangrent));

            Integer shenpiTask = iLyDataShow.getTaskCountByShenpilb("01", "07", "XZQL").getResult(); // 审批
            Integer fuwuTask = iLyDataShow.getTaskCountByShenpilb("11", null, "GGFW").getResult();// 服务
            Integer jibanTask = iLyDataShow.getTaskCountByType("1").getResult();// 即办件
            Integer chengnuoTask = iLyDataShow.getTaskCountByType("2").getResult();// 承诺件

            dataJson.put("shenpiTask", shenpiTask);
            dataJson.put("fuwuTask", fuwuTask);
            dataJson.put("jibanTask", jibanTask);
            dataJson.put("chengnuoTask", chengnuoTask);
            dataJson.put("taskcount", jibanTask + chengnuoTask);

            List<JSONObject> matterPie = new ArrayList<JSONObject>();
            double jibanTaskprent = (double) jibanTask / (double) (jibanTask + chengnuoTask) * 100;
            JSONObject chengnuo = new JSONObject();
            chengnuo.put("name", "承诺件");
            chengnuo.put("value", 100 - (int) jibanTaskprent);
            matterPie.add(chengnuo);
            JSONObject jiban = new JSONObject();
            jiban.put("name", "即办件");
            jiban.put("value", (int) jibanTaskprent);
            matterPie.add(jiban);
            dataJson.put("matterPie", matterPie);

            //大厅进驻率
            //            Integer windowTaskCount = iLyDataShow.getWindowTaskCount().getResult();
            //            List<AuditTask> allUseableTask = iAuditTask.getAllUsableAuditTask().getResult();
            //            DecimalFormat dformat = new DecimalFormat("######0.00"); 
            //            double jzl = windowTaskCount/(double)allUseableTask.size()*100;
            //            String jinzhulv = dformat.format(jzl);
            //            dataJson.put("jinzhulv",jinzhulv);
            log.info("=======结束调用getscreenoper接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());

        }
        catch (Exception e) {
            log.info("=======getscreenoper接口参数：params【" + params + "】=======");
            log.info("=======getscreenoper异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
    }

    public List<String> getMonthList(String dateFirst, String dateSecond) {
        ArrayList<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月，用户前端展示
        try {

            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();

            min.setTime(sdf.parse(dateFirst));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH) + 1, 1);

            max.setTime(sdf.parse(dateSecond));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            Calendar curr = min;
            while (curr.before(max)) {
                String time = sdf.format(curr.getTime());
                result.add(time);
                curr.add(Calendar.MONTH, 1);
            }
        }
        catch (Exception e) {

        }

        return result;
    }

    @RequestMapping(value = "/getnumber", method = RequestMethod.POST)
    public String getNumber(@RequestBody String params) {
        try {
            log.info(
                    "==============================================================================开始调用getNumber接口=======");
            // 1、入参转化为JSON对象
            JSONObject dataJson = new JSONObject();
            //            String cangshu = "{}";
            //            String retString = HttpClientUtil.postBody("http://192.168.0.166:8080/IDRIProject/rest/getFGSNumber/getNumber", cangshu);
            //            //system.out.println("--------------------------------"+retString);
            //            JSONObject json = JSONObject.parseObject(retString);
            //            //system.out.println("--------------------------------"+json);
            //            JSONObject obj = (JSONObject) json.get("result");
            //            //system.out.println("--------------------------------"+obj);
            //            int count = (Integer)obj.get("count");
            //    //system.out.println("--------------------------------"+count);
            dataJson.put("count", "1");
            log.info("=======结束调用getNumber接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());

        }
        catch (Exception e) {
            log.info("=======getNumber接口参数：params【" + params + "】=======");
            log.info("=======getNumber异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
    }

}
