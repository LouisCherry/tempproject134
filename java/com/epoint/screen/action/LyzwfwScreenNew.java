package com.epoint.screen.action;
import java.lang.invoke.MethodHandles ;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditworkingday.service.AuditOrgaWorkingDayService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.screen.api.ILyAuditVisitCount;
import com.epoint.screen.api.ILyDataShow;
import com.epoint.screen.api.ILyFrameOu;
import com.epoint.screen.api.ILyScreen02;
import com.epoint.screen.api.IProsavetimeService;
import com.epoint.screen.impl.Prosavetime;


@RestController
@RequestMapping("/lyzwfwscreennew")
public class LyzwfwScreenNew
{

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private ILyScreen02 iLyScreen02;
    @Autowired
    private ILyDataShow iLyDataShow;
    @Autowired
    private ILyAuditVisitCount service;
    @Autowired
    private IProsavetimeService iProsavetimeService;
    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;
    @Autowired
    private ILyFrameOu iLyFrameOu;
    
    private String isjd;

    @RequestMapping(value = "/getarea", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    public String getArea(@RequestBody String params) {
        JSONObject data = new JSONObject();
        try {
            log.info("=======开始调用getarea接口=======");
            // 1、入参转化为JSON对象
            String[] str = params.split("&");
            String starttime = str[0].substring(str[0].indexOf("=")+1, str[0].length());
            String endtime =  str[1].substring(str[1].indexOf("=")+1, str[1].length());
            isjd = str[2].substring(str[2].indexOf("=")+1, str[2].length());
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
            if(100 ==(int)satisfied){
                satisfiedjson.put("satisfied", "100");
            }else{
                satisfiedjson.put("satisfied", df.format(satisfied));
            }
            if(100 ==(int) unsatisfied){
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
            
            //网上用户注册量
            int registerCount = iLyScreen02.getOlineRegister(starttime, endtime).getResult();
            data.put("registerCount", registerCount);
            //网上办事量
            data.put("onlineProjectCount", online);
            //网上预约量
            int onlineQueueCount = iLyScreen02.getOlineQueue(starttime, endtime).getResult();
            data.put("onlineQueueCount", onlineQueueCount);
            //网上咨询量
            int totolvisitcount = service.findVisitcount("", "");
            data.put("onlineConsultCount", totolvisitcount);
            
            //线下办件总量
            Integer xianxiaCount = iLyDataShow.getXianxiaCount(starttime, endtime,isjd).getResult();
            //办件总量
            Integer totalCount = iLyDataShow.getTotalCount(starttime, endtime,isjd).getResult();
            data.put("xianshangCount", totalCount - xianxiaCount);
            data.put("xianxiaCount", xianxiaCount);
            double xianshangrent = ((double) (totalCount - xianxiaCount) / (double) totalCount) * 100;
            data.put("xianshangrent", df.format(xianshangrent));
            
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
            data.put("ageCount", ageCount);
            
            //户口
            Integer hefeicount = iLyDataShow.getHomeCount("1").getResult();
            data.put("hefeicount", hefeicount);
            Integer nothefeicount = iLyDataShow.getHomeCount("2").getResult();
            data.put("nothefeicount", nothefeicount);
            
            //部门前10业务量
            List<Record> outop10list = iLyDataShow.getOuTop10ByTime(starttime, endtime,isjd).getResult();
            List<JSONObject> outop10 = new ArrayList<JSONObject>();
            if (outop10list != null && outop10list.size() > 0) {
                for (int i = 0; i < outop10list.size(); i++) {
                    JSONObject ou = new JSONObject();
                    FrameOu frameOu = iLyFrameOu.getFrameOuByOuguid(outop10list.get(i).get("ouguid").toString())
                            .getResult();
                    ou.put("indexs", i + 1);
                    ou.put("ouname", frameOu.getOuname());
                    ou.put("oucount", outop10list.get(i).get("count"));
                    outop10.add(ou);
                }
            }
            data.put("outop10list", outop10);
            
            //窗口前10业务量
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
            data.put("windowRank", windowtop10);
            
            JSONObject dataLiquid = new JSONObject();
            List<JSONObject> appointData = new ArrayList<JSONObject>();
            List<JSONObject> fetchData = new ArrayList<JSONObject>();
            List<Record> appointhourlist = iLyDataShow.getTodayAppointCountByHour().getResult();
            List<Record> projecthourlist = iLyDataShow.getBanjianTodayCountByHour().getResult();
            List<Record> jiaohaohourlist = iLyDataShow.getQueueTodayCountByHour().getResult();
            
            List<String> hourlist = new ArrayList<>();
            hourlist.add("9:00-10:00");//{"9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00","14:00-15:00", "15:00-16:30" };
            hourlist.add("10:00-11:00");
            hourlist.add("11:00-12:00");
            hourlist.add("12:00-13:00");
            hourlist.add("13:00-14:00");
            hourlist.add("14:00-15:00");
            hourlist.add("15:00-16:30");
            
            //预约号
            if(appointhourlist.size()>0){
                for (int i = 0; i < hourlist.size(); i++) {
                    JSONObject appointjson = new JSONObject();
                    if(i < appointhourlist.size()){
                    	if (hourlist.get(i).split(":")[0].equals(appointhourlist.get(i).getStr("hour"))) {
                            appointjson.put("name", hourlist.get(i));
                            appointjson.put("appointNum", appointhourlist.get(i).get("count"));
                            appointData.add(appointjson);
                        }
                        else {
                            Record record = new Record();
                            record.set("hour", hourlist.get(i).split(":")[0]);
                            record.set("count", "");
                            appointhourlist.add(i, record);
                            i--;
                        }
                    }else{
                    	Record record = new Record();
                        record.set("hour", hourlist.get(i).split(":")[0]);
                        record.set("count", "");
                        appointhourlist.add(i, record);
                        i--;
                    }
                    
                }
            }
            //办件量
            if(projecthourlist.size()>0){
                for (int i = 0; i < hourlist.size(); i++) {
                    JSONObject fetchjson = new JSONObject();
                    if(i <= projecthourlist.size()-1){
                    	if (hourlist.get(i).split(":")[0].equals(projecthourlist.get(i).getStr("hour"))) {
                            fetchjson.put("name", hourlist.get(i));
                            fetchjson.put("matterNum", projecthourlist.get(i).get("count"));
                            fetchData.add(fetchjson);
                        }
                        else {
                            Record record = new Record();
                            record.set("hour", hourlist.get(i).split(":")[0]);
                            record.set("count", "");
                            projecthourlist.add(i, record);
                            i--;
                        }
                    }else{
                    	Record record = new Record();
                        record.set("hour", hourlist.get(i).split(":")[0]);
                        record.set("count", "");
                        projecthourlist.add(i, record);
                        i--;
                    }
                    
                }
            }
            
            //叫号量
            if(jiaohaohourlist.size()>0){
                for (int i = 0; i < hourlist.size(); i++) {
                	if(i < jiaohaohourlist.size() && "8".equals(jiaohaohourlist.get(i).getStr("hour"))){
                		jiaohaohourlist.get(i+1).set("count", jiaohaohourlist.get(i+1).getInt("count")+jiaohaohourlist.get(i).getInt("count"));
                		jiaohaohourlist.remove(i);
                		i--;
                		continue;
                	}
                	if(i < jiaohaohourlist.size()){
                		//办件量
                        if (hourlist.get(i).split(":")[0].equals(jiaohaohourlist.get(i).getStr("hour"))) {
                            fetchData.get(i).put("fetchNum", jiaohaohourlist.get(i).get("count"));
                        }
                        else {
                            Record record = new Record();
                            record.set("hour", hourlist.get(i).split(":")[0]);
                            record.set("count", "");
                            jiaohaohourlist.add(i, record);
                            i--;
                        }
                	}else{
                		Record record = new Record();
                        record.set("hour", hourlist.get(i).split(":")[0]);
                        record.set("count", "");
                        jiaohaohourlist.add(i-1, record);
                        i--;
                	}
                    
                }
            }
            
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
          
            dataLiquid.put("appointData", appointData);
            dataLiquid.put("fetchData", fetchData);
            data.put("dataLiquid", dataLiquid);
            
            List<Integer> riskpoint = iLyDataShow.getRiskPointCompare().getResult();
            data.put("riskright", riskpoint.get(0));
            data.put("riskleft", riskpoint.get(1));
            
            Map<String,List<String>> riskuser = iLyDataShow.getRiskUserCompare().getResult();
            List<String> rightList = riskuser.get("rightList");
            String rightListstr = "";
            for (String string : rightList) {
            	rightListstr += string;
            }
            List<String> leftList = riskuser.get("leftList");
            String leftListstr = "";
            for (String string : leftList) {
            	leftListstr += string;
            }
            String[] spryright = rightListstr.split(";"); 
            String[] spryleft = leftListstr.split(";"); 
            data.put("spryright", spryright.length);
            data.put("spryleft", spryleft.length);
            
            //业务科室
            List<Record> oulist = iLyScreen02.getOuCount().getResult();
            int ywcsleft = 0;
            for (Record record : oulist) {
            	ywcsleft = ywcsleft + record.getInt("count");
            }
            data.put("ywcsleft", ywcsleft);
            log.info("=======结束调用getarea接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", data.toString());
        }catch (Exception e) {
            log.info("=======getarea接口参数：params【" + params + "】=======");
            e.printStackTrace();
            log.info("=======getarea异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
    }
    
    @RequestMapping(value = "/getstreet", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    public String getStreet(@RequestBody String params) {
        JSONObject data = new JSONObject();
        try {
            log.info("=======开始调用getstreet接口=======");
            // 1、入参转化为JSON对象
            String[] str = params.split("&");
            String starttime = str[0].substring(str[0].indexOf("=")+1, str[0].length());
            String endtime =  str[1].substring(str[1].indexOf("=")+1, str[1].length());
            isjd = str[2].substring(str[2].indexOf("=")+1, str[2].length());
            // 办件人年龄段分布(街道)
            String[] ages = { "1950", "195", "196", "197", "198", "199", "200" };
            String[] ranges = {"1950之前", "50后", "60后", "70后", "80后", "90后", "00后" };
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
        	data.put("ageJDCount", ageJDCount);
        	//街道户口
            Integer jdhefeicount = iLyDataShow.getJDHomeCount("1").getResult();
            data.put("jdhefeicount", jdhefeicount);
            Integer jdnothefeicount = iLyDataShow.getJDHomeCount("2").getResult();
            data.put("jdnothefeicount", jdnothefeicount);
            
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
            data.put("monthlyDo", banjianSumList);
            
            List<JSONObject> hotTaskJsonList = new ArrayList<JSONObject>();
            List<AuditProject> hotTaskTop5 = iLyDataShow.getHotTaskTop5ByTime(starttime, endtime,isjd).getResult();
            for (AuditProject auditProject : hotTaskTop5) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", auditProject.getProjectname());
                jsonObject.put("value", auditProject.get("count"));
                hotTaskJsonList.add(jsonObject);
            }
            data.put("hotMatter", hotTaskJsonList);
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
            
            //部门前10业务量
            List<Record> outop10list = iLyDataShow.getOuTop10ByTime(starttime, endtime,isjd).getResult();
            List<JSONObject> outop10 = new ArrayList<JSONObject>();
            if (outop10list != null && outop10list.size() > 0) {
                for (int i = 0; i < outop10list.size(); i++) {
                    JSONObject ou = new JSONObject();
                    FrameOu frameOu = iLyFrameOu.getFrameOuByOuguid(outop10list.get(i).get("ouguid").toString())
                            .getResult();
                    ou.put("indexs", i + 1);
                    ou.put("ouname", frameOu.getOuname());
                    ou.put("oucount", outop10list.get(i).get("count"));
                    outop10.add(ou);
                }
            }
            data.put("outop10list", outop10);
            
            //窗口前10业务量
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
            data.put("windowRank", windowtop10);
            //线下办件总量
            Integer xianxiaCount = iLyDataShow.getXianxiaCount(starttime, endtime,isjd).getResult();
            //办件总量
            Integer totalCount = iLyDataShow.getTotalCount(starttime, endtime,isjd).getResult();
            data.put("xianshangCount", totalCount - xianxiaCount);
            data.put("xianxiaCount", xianxiaCount);
            
            log.info("=======结束调用getstreet接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", data.toString());
        }catch (Exception e) {
            log.info("=======getstreet接口参数：params【" + params + "】=======");
            log.info("=======getstreet异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取信息失败：" + e.getMessage(), "");
        }
    }
    
    @RequestMapping(value = "/getservice", method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    public String getService(@RequestBody String params) {
        JSONObject data = new JSONObject();
        try {
            log.info("=======开始调用getservice接口=======");
            // 1、入参转化为JSON对象
            String[] str = params.split("&");
            String starttime = str[0].substring(str[0].indexOf("=")+1, str[0].length());
            String endtime =  str[1].substring(str[1].indexOf("=")+1, str[1].length());
            String type =  str[2].substring(str[2].indexOf("=")+1, str[2].length());
            type = URLDecoder.decode(type,"utf-8");
            List<Record> childous = iLyDataShow.getChildOuProjectCount(type, starttime, endtime).getResult();
            String name = "";
            Map<String, JSONObject> childmap = new HashMap<>();
            List<JSONObject> childouslist = new ArrayList<>();
            for (Record record : childous) {
            	JSONObject childjson;
            	if(name.equals(record.get("ouname"))){
            		childjson = childmap.get(name);
            	}else{
            		childjson = new JSONObject();
            	}
				name = record.get("ouname");
				childjson.put("name", name);
				if("10".equals(record.getStr("applyway"))){
					childjson.put("onlineNum", record.getInt("count")==null?0:record.getInt("count"));
				}else if("20".equals(record.getStr("applyway"))){
					childjson.put("offlineNum", record.getInt("count")==null?0:record.getInt("count"));
				}
				List<Record> childoutaskhot = iLyDataShow.getChildOuHotTaskTop5ByTime(starttime, endtime, record.get("ouguid")).getResult();
				List<JSONObject> tpArr = new ArrayList<>();
				int sort = 1;
				for (Record childoutask : childoutaskhot) {
					JSONObject childtaskjson = new JSONObject();
					childtaskjson.put("sort", sort);
					childtaskjson.put("name", childoutask.get("projectname"));
					childtaskjson.put("num", childoutask.get("count"));
					sort ++;
					tpArr.add(childtaskjson);
				}
				childjson.put("tpArr", tpArr);
				childmap.put(name, childjson);
            }
            int index = 0;
            int result = 0;
            for (String key : childmap.keySet()) {
            	if(key.endsWith("政府") || key.endsWith("办事处")){
            		result = index;
            	}
            	index ++;
            	childouslist.add(childmap.get(key));
			}
            Collections.swap(childouslist, result, 0);
            data.put("childouslist", childouslist);
            log.info("=======结束调用getservice接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取信息成功", data.toString());
        }catch (Exception e) {
            log.info("=======getservice接口参数：params【" + params + "】=======");
            log.info("=======getservice异常信息：" + e.getMessage() + "=======");
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
