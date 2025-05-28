package com.epoint.screen.action;

import java.lang.invoke.MethodHandles ;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditworkingday.service.AuditOrgaWorkingDayService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.screen.api.ILyAuditVisitCount;
import com.epoint.screen.api.ILyDataShow;
import com.epoint.screen.api.ILyScreen02;
import com.epoint.screen.api.IProsavetimeService;
import com.epoint.screen.impl.Prosavetime;


@RestController
@RequestMapping("/lyzwfwscreentwo")
public class LyzwfwScreen
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
    private ILyAuditVisitCount service;
    
    @Autowired
    private IProsavetimeService iProsavetimeService;
    
    private String isjd;

    @RequestMapping(value = "/getscreenno", method = RequestMethod.POST)
    public String getInformationDetailByRowguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getScreenNo2接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String[] str = params.split("&");
            String starttime = (String) jsonObject.get("starttime");
            String endtime =  (String) jsonObject.get("endtime");
           // String starttime = str[0].substring(str[0].indexOf("="), str[0].length());
           // String endtime =  str[1].substring(str[1].indexOf("="), str[1].length());
            DecimalFormat df = new DecimalFormat("######0.00");
            if(StringUtil.isNotBlank(starttime)&&!"=".equals(starttime)){
                starttime = starttime.split("=")[1];
            }else{
                starttime = "";
            }
            if(StringUtil.isNotBlank(endtime)&&!"=".equals(endtime)){
                endtime = endtime.split("=")[1];
            }else{
                endtime = "";
            }
            JSONObject dataJson = new JSONObject();
            //申请人企业行业类型统计饼图
            Integer enterprisecount = iLyScreen02.getAllCountByType(starttime, endtime, 10).getResult();
            Integer enterprisesum = enterprisecount;
            List<JSONObject> enterpriseType = new ArrayList<JSONObject>();
            List<Record> enterpriseIndustrytypeList = iLyScreen02.getEnterpriseIndustrytype(starttime, endtime, "4").getResult();
            for (Record record : enterpriseIndustrytypeList) {
                JSONObject bean = new JSONObject();
                bean.put("name", iCodeItemsService.getItemTextByCodeName("行业类型", record.get("INDUSTRYTYPE")));
                bean.put("value", record.get("count"));
               // String rate = ((Double.parseDouble((record.get("count")+""))/enterprisesum)*100+"");
                Double ratedouble = ((Double.parseDouble((record.get("count")+""))/enterprisesum)*100);
//                if(rate.length()>=5){
//                   rate = rate.substring(0, 5);
//                }
                String rate = df.format(ratedouble);
                bean.put("rate", rate);
                enterprisecount = enterprisecount - Integer.parseInt(record.get("count")+"");
                enterpriseType.add(bean);
            }
            JSONObject otherenterprise = new JSONObject();
            otherenterprise.put("name", "其他");
            otherenterprise.put("value", enterprisecount);
            if(enterprisecount == 0){
                otherenterprise.put("rate", "0.00");
            }else{    
                //String rate = (enterprisecount*1.0/enterprisesum*100+"");
                Double ratedouble = (enterprisecount*1.0/enterprisesum*100);
                String rate = df.format(ratedouble);
//                if(rate.length()>=5){
//                    rate = rate.substring(0, 5);
//                 }
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
                //String rate = ((Double.parseDouble((record.get("count")+""))/individualsum)*100+"");
                Double ratedouble = ((Double.parseDouble((record.get("count")+""))/individualsum)*100);
//                if(rate.length()>=5){
//                   rate = rate.substring(0, 5);
//                }
                String rate = df.format(ratedouble);
                bean.put("rate", rate);
                individualcount = individualcount - Integer.parseInt(record.get("count")+"");
                individualType.add(bean);
            }
            JSONObject otherindividual = new JSONObject();
            otherindividual.put("name", "其他");
            otherindividual.put("value", individualcount);
            if(individualcount ==0){
                otherindividual.put("rate", "0.00");
            }else{        
                //String rate = (individualcount*1.0/individualsum*100+"");
                Double ratedouble = (individualcount*1.0/individualsum*100);
                String rate = df.format(ratedouble);
//                if(rate.length()>=5){
//                    rate = rate.substring(0, 5);
//                 }
                otherindividual.put("rate", rate);
            }
            individualType.add(otherindividual);
            dataJson.put("individualType", individualType);
            // 企业设立统计数量分布
            String[] townList = {"三十岗乡","大杨镇","杏花村","三孝口","四里河","亳州路","双岗","逍遥津","海棠","林店","杏林","其他"};
            List<JSONObject> enterpriseNumber = new ArrayList<JSONObject>();
            int towncount = 0;
            for (String town : townList) {
                JSONObject bean = new JSONObject();
                int count = iLyScreen02.getSetupCountByType(starttime, endtime, 10, town).getResult();
                if(!"其他".equals(town)){
                	towncount += count;
                }else{
                	count = count - towncount;
                }
                bean.put("name", town);
                bean.put("value", count);
                enterpriseNumber.add(bean);
            }
            dataJson.put("enterpriseNumber", enterpriseNumber);
            List<JSONObject> individualNumber = new ArrayList<JSONObject>();
            towncount = 0;
            for (String town : townList) {
                JSONObject bean = new JSONObject();
                int count = iLyScreen02.getSetupCountByType(starttime, endtime, 20, town).getResult();
                if(!"其他".equals(town)){
                	towncount += count;
                }else{
                	count = count - towncount;
                }
                bean.put("name", town);
                bean.put("value", count);
                individualNumber.add(bean);
            }
            dataJson.put("individualNumber", individualNumber);
            // 办件人年龄段分布
            List<JSONObject> ageCount = new ArrayList<JSONObject>();
            String[] ages = { "1950", "195", "196", "197", "198", "199", "200" };
            String[] ranges = {"1950之前", "50后", "60后", "70后", "80后", "90后", "00后" };
            for (int i = 0; i < ages.length; i++) {
                JSONObject bean = new JSONObject();
                bean.put("name", ranges[i]);
                int male = iLyScreen02.getAgeGroupNum(starttime, endtime, 1, ages[i]).getResult();
                bean.put("male", male);
                int famale = iLyScreen02.getAgeGroupNum(starttime, endtime, 2, ages[i]).getResult();
                bean.put("famale", famale);
                bean.put("count", male+famale);
                ageCount.add(bean);
            }
            dataJson.put("ageCount", ageCount);
            //获取总注册资产
            Double  number1 = iLyScreen02.getRegisteredcapital(starttime, endtime, 10).getResult();
            
            
            dataJson.put("number1", df.format(number1/10000));
            Double  number2 = iLyScreen02.getRegisteredcapital(starttime, endtime, 20).getResult();
            dataJson.put("number2", df.format(number2/10000));
            
            //资金来源
            Double moneyhefei = (iLyScreen02.getRegisteredcapitalIsBelong(starttime, endtime, 1).getResult());
            Double moneybsgs = (iLyScreen02.getRegisteredcapitalIsBelong(starttime, endtime, 2).getResult());
            Double moneyshenwai = ((number1+number2)-(moneyhefei)-(moneybsgs));
            moneyhefei = moneyhefei/10000;
            moneybsgs = moneybsgs/10000;
            moneyshenwai = moneyshenwai/10000;
            dataJson.put("moneyhefei", df.format(moneyhefei)+"亿");
            dataJson.put("moneybsgs", df.format(moneybsgs)+"亿");
            dataJson.put("moneyshenwai", df.format(moneyshenwai)+"亿");
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
            
            log.info("=======结束调用getScreenNo2接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());

        }
        catch (Exception e) {
            log.info("=======getScreenNo2接口参数：params【" + params + "】=======");
            log.info("=======getScreenNo2异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
    }
    
    
    @RequestMapping(value = "/getserviceffect", method = RequestMethod.POST)
    public String getServiceEffecte(@RequestBody String params) {
        JSONObject data = new JSONObject();
        try {
            log.info("=======开始调用getServiceEffecte接口=======");
            // 1、入参转化为JSON对象
            //JSONObject jsonObject = JSONObject.parseObject(params);
            String[] str = params.split("&");
           // String starttime = (String) jsonObject.get("starttime");
           // String endtime =  (String) jsonObject.get("endtime");
            String starttime = str[0].substring(str[0].indexOf("="), str[0].length());
            String endtime =  str[1].substring(str[1].indexOf("="), str[1].length());
            isjd = str[2].substring(str[2].indexOf("=")+1, str[2].length());
            if(StringUtil.isNotBlank(starttime)&&!"=".equals(starttime)){
                starttime = starttime.split("=")[1];
            }else{
                starttime = "";
            }
            if(StringUtil.isNotBlank(endtime)&&!"=".equals(endtime)){
                endtime = endtime.split("=")[1];
            }else{
                endtime = "";
            }
            //节省工作日
            List<Record> records = iLyScreen02.getCountDaysParams(starttime, endtime);
            int days = 0;
            Prosavetime savetime = iProsavetimeService.find("00155cd5-61d6-466e-9365-14bcdee08c15");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (savetime.getOperatedate() != null) {
                String time = sdf.format(savetime.getOperatedate());
                if(savetime.getTime()!=null && time.equals(sdf.format(new Date()))){
                    data.put("days", savetime.getTime());
                }
            }else{                
                for (Record record : records) {
                    if (record.get("PROMISEENDDATE") != null && record.get("banjiedate") != null) {
                        AuditOrgaWorkingDayService service = new AuditOrgaWorkingDayService();                        
                        days += service.GetWorkingDays_Between_From_To(null, record.get("banjiedate"), record.get("PROMISEENDDATE"));
                    }
                    
                } 
                data.put("days", days);
                savetime.setTime(days);
                savetime.setOperatedate(new Date());
                iProsavetimeService.update(savetime);
            }
            //满意度
            Map<String,Integer> evaluatmap =  iLyScreen02.getEvaluatProjectCount(starttime, endtime);
            JSONObject satisfiedjson = new JSONObject();
            DecimalFormat df = new DecimalFormat("#.0");
            int all = evaluatmap.get("all");
            double satisfied = (double)evaluatmap.get("satisfied")*100/(double)all;
            double unsatisfied = (double)evaluatmap.get("unsatisfied")/(double)all*100;
            if(100 == (int)satisfied){
                satisfiedjson.put("satisfied", "100");
            }else{
                satisfiedjson.put("satisfied", df.format(satisfied));
            }
            if(100 == (int)unsatisfied){
                satisfiedjson.put("unsatisfied", "100");
            }else if(0.0 == unsatisfied){
                satisfiedjson.put("unsatisfied", "0");
            }else{
                satisfiedjson.put("unsatisfied", df.format(unsatisfied));
            }
            data.put("satisfiedjson", satisfiedjson);
            
            Map<String,Integer> map = iLyScreen02.getProjectCountByType(starttime, endtime).getResult();
            //微信关注量
            int wechartCount = iLyScreen02.getWeChartCount().getResult();
            data.put("wechartCount", wechartCount);
            
            //即办件数量
            int jiban = map.get("jiban");
            data.put("jiban", jiban);
            //承诺件数量
            int chengnuo = map.get("chengnuo");
            data.put("chengnuo", chengnuo);
            //线上办件数量
            int online = map.get("online");
            data.put("online", online);
            //线下办件数量
            int xianxia = map.get("xianxia");
            data.put("xianxia", xianxia);
            
            //承诺件部门分布
            List<Record> chengnuolist = iLyScreen02.getTaskOuTypeByType().getResult().get("chengnuo");
            int cnquzhi = chengnuolist.get(0).getInt("count");
            int cnstreet = chengnuolist.get(1).getInt("count");
            int cnshequ = chengnuolist.get(2).getInt("count");
            JSONObject cnjson = new JSONObject();
            cnjson.put("cnquzhi", cnquzhi);
            cnjson.put("cnstreet", cnstreet);
            cnjson.put("cnshequ", cnshequ);
            data.put("cnjson", cnjson);
            
            //即办件部门分布
            List<Record> jibanlist = iLyScreen02.getTaskOuTypeByType().getResult().get("jiban");
            int jbquzhi = jibanlist.get(0).getInt("count");
            int jbstreet = jibanlist.get(1).getInt("count");
            int jbshequ = jibanlist.get(2).getInt("count");
            JSONObject jbjson = new JSONObject();
            jbjson.put("jbquzhi", jbquzhi);
            jbjson.put("jbstreet", jbstreet);
            jbjson.put("jbshequ", jbshequ);
            data.put("jbjson", jbjson);
            
            JSONObject chartDatajb = new JSONObject();
            chartDatajb.put("name", "即办件");
            chartDatajb.put("value", jbquzhi+jbshequ+jbstreet);
            JSONObject chartDatacn = new JSONObject();
            chartDatacn.put("name", "承诺件");
            chartDatacn.put("value", cnquzhi+cnstreet+cnshequ);
            data.put("chartDatajb", chartDatajb);
            data.put("chartDatacn", chartDatacn);
            
            //网上用户注册量
            int registerCount = iLyScreen02.getOlineRegister(starttime, endtime).getResult();
            data.put("registerCount", registerCount);
            //网上办事量
            //int onlineProjectCount = iLyScreen02.getOlineProject(starttime, endtime).getResult();
            data.put("onlineProjectCount", online);
            //网上预约量
            int onlineQueueCount = iLyScreen02.getOlineQueue(starttime, endtime).getResult();
            data.put("onlineQueueCount", onlineQueueCount);
            //网上咨询量
            int totolvisitcount = service.findVisitcount("", "");
           // int onlineConsultCount = iLyScreen02.getOlineConsult(starttime, endtime).getResult();
            data.put("onlineConsultCount", totolvisitcount);
            
            
            //本周线上线下趋势
            List<JSONObject> serviceTrend = new ArrayList<>();
            List<Map<String,String>> sevendays = getSevenDays();
            for (int i = 6; i >= 0; i--) {
                JSONObject j = new JSONObject();
                int offlinecount = iLyScreen02.getWeekProjectTimeAndCount(sevendays.get(i).get("time1"), sevendays.get(i).get("time2"), "20",isjd).getResult();
                int onlinecount = iLyScreen02.getWeekProjectTimeAndCount(sevendays.get(i).get("time1"), sevendays.get(i).get("time2"), "",isjd).getResult();
                j.put("time", sevendays.get(i).get("time1").substring(5, 10));
                j.put("offline", offlinecount);
                j.put("online", onlinecount);
                serviceTrend.add(j);
            }
            data.put("serviceTrend", serviceTrend);
            
            //本周办件量和取号量走势
            List<JSONObject> sumList = new ArrayList<JSONObject>();
            for (int i = 6; i >= 0; i--) {
                JSONObject json = new JSONObject();
                Integer banjiancount = iLyDataShow.getBanjianCountByMonth(sevendays.get(i).get("time1").substring(0,10),isjd).getResult();
                Integer quhaocount = iLyDataShow.getQueueCountByMonth(sevendays.get(i).get("time1").substring(0,10)).getResult();
                String time = sevendays.get(i).get("time1").substring(5, sevendays.get(i).get("time1").length()-9);
                StringBuilder sb = new StringBuilder(time);
                json.put("time", sb.toString());
                json.put("matterNum", banjiancount);
                json.put("fetchNum", quhaocount);
                sumList.add(json);
            }
            data.put("weeklyDo", sumList);
            
            //根据标准获取事项数
            JSONObject taskObject = new JSONObject();
            int taskTotalCount = iLyScreen02.getTaskCountByWebLevel(0).getResult();
            int taskCountOne = iLyScreen02.getTaskCountByWebLevel(1).getResult();
            List<Record> outypelistone = iLyScreen02.getTaskCountByWebLevelAndOUtype(1).getResult();
            int quzhione = outypelistone.get(0).getInt("count");
            int streetone = outypelistone.get(1).getInt("count");
            int shequone = outypelistone.get(2).getInt("count");
            int taskCountTwo = iLyScreen02.getTaskCountByWebLevel(2).getResult();
            List<Record> outypelisttwo = iLyScreen02.getTaskCountByWebLevelAndOUtype(2).getResult();
            int quzhitwo = outypelisttwo.get(0).getInt("count");
            int streettwo = outypelisttwo.get(1).getInt("count");
            int shequtwo = outypelisttwo.get(2).getInt("count");
            int taskCountThree = iLyScreen02.getTaskCountByWebLevel(3).getResult();
            List<Record> outypelistthree = iLyScreen02.getTaskCountByWebLevelAndOUtype(3).getResult();
            int quzhithree = outypelistthree.get(0).getInt("count");
            int streetthree = outypelistthree.get(1).getInt("count");
            int shequthree = outypelistthree.get(2).getInt("count");
            int taskCountFour = iLyScreen02.getTaskCountByWebLevel(4).getResult();
            List<Record> outypelistfour = iLyScreen02.getTaskCountByWebLevelAndOUtype(4).getResult();
            int quzhifour = outypelistfour.get(0).getInt("count");
            int streetfour = outypelistfour.get(1).getInt("count");
            int shequfour = outypelistfour.get(2).getInt("count");
            double percentOne = (double)taskCountOne/(double)taskTotalCount*100;
            double percentTwo = (double)taskCountTwo/(double)taskTotalCount*100;
            double percentThree = (double)taskCountThree/(double)taskTotalCount*100;
            double percentFour = (double)taskCountFour/(double)taskTotalCount*100;
            taskObject.put("taskTotalCount", taskTotalCount);
            //一级标准数和占比
            taskObject.put("taskCountOne", taskCountOne);
            taskObject.put("percentOne", df.format(percentOne));
            taskObject.put("quzhione", quzhione);
            taskObject.put("streetone", streetone);
            taskObject.put("shequone", shequone);
            //二级标准数和占比
            taskObject.put("taskCountTwo", taskCountTwo);
            taskObject.put("percentTwo", df.format(percentTwo));
            taskObject.put("quzhitwo", quzhitwo);
            taskObject.put("streettwo", streettwo);
            taskObject.put("shequtwo", shequtwo);
            //三级标准数和占比
            taskObject.put("taskCountThree", taskCountThree);
            taskObject.put("percentThree", df.format(percentThree));
            taskObject.put("quzhithree", quzhithree);
            taskObject.put("streetthree", streetthree);
            taskObject.put("shequthree", shequthree);
            //四级标准数和占比
            taskObject.put("taskCountFour", taskCountFour);
            taskObject.put("percentFour", df.format(percentFour));
            taskObject.put("quzhifour", quzhifour);
            taskObject.put("streetfour", streetfour);
            taskObject.put("shequfour", shequfour);
            data.put("taskObject", taskObject);
            
            // 办件量
            JSONObject psumjson = new JSONObject();
            Integer projectSum = iLyDataShow.getBanjianSumByTime("", starttime, endtime).getResult();
            List<String> projectSumCount = new ArrayList<>();
            for (String projectSumStr : changeToSixNum(projectSum)) {
                projectSumCount.add(projectSumStr);
            }
            Calendar calendar = Calendar.getInstance();
            Integer projectYearSum = iLyDataShow.getBanjianSumByTime(String.valueOf(calendar.get(Calendar.YEAR)), "", "").getResult();
            Integer projectMonthSum = iLyDataShow.getBanjianSumByTime(EpointDateUtil.convertDate2String(new Date()).substring(0, 7), "", "").getResult();
            Integer projectDaySum = iLyDataShow.getBanjianSumByTime(EpointDateUtil.convertDate2String(new Date()), "", "").getResult();
            psumjson.put("projectSum", projectSumCount);// 办件量总数
            psumjson.put("projectYearSum", projectYearSum);// 本年办件量
            psumjson.put("projectMonthSum", projectMonthSum);// 本月办件量
            psumjson.put("projectDaySum", projectDaySum);// 本日办件量
            data.put("psumjson", psumjson);
            // 办结量
            JSONObject projectbanjiejson = new JSONObject();
            Integer projectBanjieSum = iLyDataShow.getBanjieSumByTime("", starttime, endtime).getResult();
            List<String> projectBanjieCount = new ArrayList<>();
            for (String projectBanjieSumStr : changeToSixNum(projectBanjieSum)) {
                projectBanjieCount.add(projectBanjieSumStr);
            }
            Integer projectBanjieYearSum = iLyDataShow.getBanjieSumByTime(String.valueOf(calendar.get(Calendar.YEAR)), "", "").getResult();
            Integer projectBanjieMonthSum = iLyDataShow.getBanjieSumByTime(EpointDateUtil.convertDate2String(new Date()).substring(0, 7), "", "").getResult();
            Integer projectBanjieDaySum = iLyDataShow.getBanjieSumByTime(EpointDateUtil.convertDate2String(new Date()), "", "").getResult();
            projectbanjiejson.put("projectBanjieCount", projectBanjieCount);// 办结量总数
            projectbanjiejson.put("projectBanjieYearSum", projectBanjieYearSum);// 本年办结量
            projectbanjiejson.put("projectBanjieMonthSum", projectBanjieMonthSum);// 本月办结量
            projectbanjiejson.put("projectBanjieDaySum", projectBanjieDaySum);// 本日办结量
            data.put("projectbanjiejson", projectbanjiejson);
            
            // 总取号
            JSONObject totalQuhaoCountJson = new JSONObject();
            Integer quhaoCount = iLyDataShow.getQueueCountByTime(starttime, endtime).getResult();
            totalQuhaoCountJson.put("quhaoCount", quhaoCount);
            // 获取当天预约数
            Integer todayappoint = iLyDataShow.getAppointTodayCount().getResult();
            totalQuhaoCountJson.put("todayappoint", todayappoint);
            // 获取当天取号数
            Integer todayquhao = iLyDataShow.getQueueCountByDate().getResult();
            totalQuhaoCountJson.put("todayquhao", todayquhao);
            // 获取当前办理人数
            Integer nowbanli = iLyDataShow.getProjectCountByDate().getResult();
            totalQuhaoCountJson.put("nowbanli", nowbanli);
            // 获取当前等待人数
            Integer nowwait = iLyDataShow.getWaitNumByDate().getResult();
            totalQuhaoCountJson.put("nowwait", nowwait);
              data.put("totalQuhaoCountJson", totalQuhaoCountJson);
            
            //hot
            List<JSONObject> hotTaskJsonList = new ArrayList<JSONObject>();
            List<AuditProject> hotTaskTop5 = iLyDataShow.getHotTaskTop5ByTime(starttime, endtime,isjd).getResult();
            int sort = 1;
            for (AuditProject auditProject : hotTaskTop5) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("sort", sort);
                jsonObject1.put("name", auditProject.getProjectname());
                jsonObject1.put("num", auditProject.get("count"));
                hotTaskJsonList.add(jsonObject1);
                sort++;
            }
            data.put("hotMatter", hotTaskJsonList);
            
            //大厅进驻率
            Integer windowTaskCount = iLyDataShow.getWindowTaskCount().getResult();
            int alltask = iLyScreen02.getTaskCount().getResult();
            DecimalFormat dformat = new DecimalFormat("######0.00"); 
            double jzl = windowTaskCount/(double)alltask*100;
            String jinzhulv = dformat.format(jzl);
            data.put("jinzhulv",jinzhulv);
            
            log.info("=======结束调用getServiceEffecte接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", data.toString());
        }
        catch (Exception e) {
            log.info("=======getServiceEffecte接口参数：params【" + params + "】=======");
            log.info("=======getServiceEffecte异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
        
    }
    
    @RequestMapping(value = "/getgeneralsituation", method = RequestMethod.POST)
    public String getGeneralSituation(@RequestBody String params) {
        JSONObject data = new JSONObject();
        try {
            log.info("=======开始调用getServiceEffecte接口=======");
            DecimalFormat df=new DecimalFormat("0.00");
            //法人库信息量
            int companyCount = iLyScreen02.getCompanyCount().getResult(); 
            data.put("companyCount", df.format((float)companyCount/10000.00));
            //个人库信息量
            int individualCount = iLyScreen02.getIndividualCount().getResult();
            data.put("individualCount", df.format((float)individualCount/10000.00));
            //网上用户注册量
            int registerCount = iLyScreen02.getOlineRegister("","").getResult();
            data.put("registerCount", df.format((float)registerCount/10000.00));
            //根据事项类别获取事项数
            //List<JSONObject> matterPie = new ArrayList<>();
            List<Record> XZQL = iLyScreen02.getTaskCountByType("01","07","XZQL").getResult();
            int quzhiXZQL = 0;
            int streetXZQL = 0;
            int shequXZQL = 0;
            for (Record record : XZQL) {
                switch (record.getStr("isjd")) {
                    case "1":
                        quzhiXZQL = record.getInt("count");
                        break;
                    case "2":
                        streetXZQL = record.getInt("count");
                        break;
                    case "3":
                        shequXZQL = 0;
                        break;
                    default:
                        break;
                }
            }
            int XZQLcount = quzhiXZQL+streetXZQL+shequXZQL;
            data.put("XZQLcount", XZQLcount);
            data.put("quzhiXZQL", quzhiXZQL);
            data.put("streetXZQL", streetXZQL);
            data.put("shequXZQL", shequXZQL);
//            JSONObject shenpiCountjson = new JSONObject();
//            shenpiCountjson.put("name", "行政权利");
//            shenpiCountjson.put("value", XZQLcount);
//            matterPie.add(shenpiCountjson);
            List<Record> GGFW = iLyScreen02.getTaskCountByType("11",null,"GGFW").getResult();
            int quzhiGGFW = 0;
            int streetGGFW = 0;
            int shequGGFW = 0;
            for (Record record : GGFW) {
                switch (record.getStr("isjd")) {
                    case "1":
                        quzhiGGFW = record.getInt("count");
                        break;
                    case "2":
                        streetGGFW = record.getInt("count");
                        break;
                    case "3":
                        shequGGFW = record.getInt("count");
                        break;
                    default:
                        break;
                }
            }
            int GGFWcount = quzhiGGFW+streetGGFW+shequGGFW;
            data.put("GGFWcount", GGFWcount);
            data.put("quzhiGGFW", quzhiGGFW);
            data.put("streetGGFW", streetGGFW);
            data.put("shequGGFW", shequGGFW);
//            JSONObject fuwuCountjson = new JSONObject();
//            fuwuCountjson.put("name", "公共服务");
//            fuwuCountjson.put("value", GGFWcount);
//            matterPie.add(fuwuCountjson);
            List<Record> QT = iLyScreen02.getTaskCountByType(null,null,"QT").getResult();
            int quzhiQT = 0;
            int streetQT = 0;
            int shequQT = 0;
            for (Record record : QT) {
                switch (record.getStr("isjd")) {
                    case "1":
                        quzhiQT = record.getInt("count");
                        break;
                    case "2":
                        streetQT = record.getInt("count");
                        break;
                    case "3":
                        shequQT = record.getInt("count");
                        break;
                    default:
                        break;
                }
            }
            int QTcount = quzhiQT+streetQT+shequQT;
            data.put("QTcount", QTcount);
            data.put("quzhiQT", quzhiQT);
            data.put("streetQT", streetQT);
            data.put("shequQT", shequQT);
//            JSONObject querenCountjson = new JSONObject();
//            querenCountjson.put("name", "其他事项");
//            querenCountjson.put("value", QTcount);
//            matterPie.add(querenCountjson);
//            data.put("matterPie", matterPie);
            //涉及部门
            List<Record> oulist = iLyScreen02.getOuCount().getResult();
            int qushiCount = 0;
            int streetCount = 0;
            int shequCount = 0;
            for (Record record : oulist) {
                switch (record.getStr("isjd")) {
                    case "1":
                        qushiCount = record.getInt("count");
                        break;
                    case "2":
                        streetCount = record.getInt("count");
                        break;
                    case "3":
                        shequCount = record.getInt("count");
                        break;
                    default:
                        break;
                }
            }
            int ouCount = qushiCount+streetCount+shequCount;
            data.put("ouCount", ouCount);
            data.put("qushiCount", qushiCount);
            data.put("streetCount", streetCount);
            data.put("shequCount", shequCount);
            //办事指南/事项清单/启用事项数
            int taskCount = iLyScreen02.getTaskCount().getResult();
            data.put("taskCount", taskCount);
            //岗位数
            int riskPointCount = iLyScreen02.getRiskPointCount().getResult();
            data.put("riskPointCount", riskPointCount);
            //表单数
            int materialCount = iLyScreen02.getMaterialCount().getResult();
            data.put("materialCount", materialCount);
            
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", data.toString());
        }catch (Exception e) {
            log.info("=======getServiceEffecte接口参数：params【" + params + "】=======");
            log.info("=======getServiceEffecte异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
    }
    
    /**
     * 将数字转换成6位字符串
     */
    public List<String> changeToSixNum(int num) {
        List<String> numlist = new ArrayList<String>();
        String numstr = String.valueOf(num);
        if (numstr.length() < 2) {
            for (int i = 0; i < 5; i++) {
                numlist.add("0");
            }
            numlist.add(numstr);
        } else if (numstr.length() < 3) {
            for (int i = 0; i < 4; i++) {
                numlist.add("0");
            }
            numlist.add(numstr.substring(0, 1));
            numlist.add(numstr.substring(1, 2));
        } else if (numstr.length() < 4) {
            for (int i = 0; i < 3; i++) {
                numlist.add("0");
            }
            numlist.add(numstr.substring(0, 1));
            numlist.add(numstr.substring(1, 2));
            numlist.add(numstr.substring(2, 3));
        } else if (numstr.length() < 5) {
            for (int i = 0; i < 2; i++) {
                numlist.add("0");
            }
            numlist.add(numstr.substring(0, 1));
            numlist.add(numstr.substring(1, 2));
            numlist.add(numstr.substring(2, 3));
            numlist.add(numstr.substring(3, 4));
        } else if (numstr.length() < 6) {
            numlist.add("0");
            numlist.add(numstr.substring(0, 1));
            numlist.add(numstr.substring(1, 2));
            numlist.add(numstr.substring(2, 3));
            numlist.add(numstr.substring(3, 4));
            numlist.add(numstr.substring(4, 5));
        } else {
            numlist.add(numstr.substring(0, 1));
            numlist.add(numstr.substring(1, 2));
            numlist.add(numstr.substring(2, 3));
            numlist.add(numstr.substring(3, 4));
            numlist.add(numstr.substring(4, 5));
            numlist.add(numstr.substring(5, 6));
        }

        return numlist;
    }
    
    /**
     * 获取今天的前7天
     */
    public List<Map<String,String>> getSevenDays() {
          List<Map<String,String>> days = new ArrayList<>();
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AuditOrgaWorkingDayService service = new AuditOrgaWorkingDayService();
        for (int i = 0; i < 7; i++) {
            Date date = service.getWorkingDayWithOfficeSet(ZwfwUserSession.getInstance().getCenterGuid(), new Date(), -i);
            Map<String,String> map = new HashMap<String, String>();
            map.put("time1", sdf.format(date)+" 00:00:00");
            map.put("time2", sdf.format(date)+" 23:59:59");
            days.add(map);
        }
        return days;
    }
}
