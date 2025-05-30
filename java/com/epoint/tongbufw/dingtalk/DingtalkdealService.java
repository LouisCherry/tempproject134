package com.epoint.tongbufw.dingtalk;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

public class DingtalkdealService
{
    //获取考勤时间设置
    public static List<Record> getAuditDailyTime() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select swsbtime,swxbtime,xwsbtime,xwxbtime from audit_orga_dailytime where CENTERGUID='46db0d30-b3ea-4d9c-8a66-771731e4b33a' LIMIT 1";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();
        return list;
    }

    //获取本月当前时间之后非工作天数
    public static String getNotWorkDay(String begintime) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from audit_orga_workingday where isworkingday='0' and DATE_FORMAT(wdate,'%Y-%m-%d %H:%i')>=DATE_FORMAT('"
                + begintime
                + "','%Y-%m-%d %H:%i' ) and DATE_FORMAT(wdate,'%Y-%m-%d %H:%i')<=DATE_FORMAT(LAST_DAY(now()),'%Y-%m-%d %H:%i' )";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();
        return list.size() + "";
    }

    //获取当天是否是工作日
    public static String getWorkDay(String begintime) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select isworkingday from audit_orga_workingday where DATE_FORMAT(wdate,'%Y-%m-%d')=DATE_FORMAT('"
                + begintime + "','%Y-%m-%d' )";
        String isworkingday = dao.find(sql, String.class);
        dao.close();
        return isworkingday;
    }

    //获取所有考勤人员信息
    public static List<Record> getMember() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select userid,username,userguid,ouguid,gonghao from audit_orga_member where IFNULL(userid,'')!=''";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();
        return list;
    }

    //判断用户当前年月是否已初始化过数据，未初始化则初始化
    public static void initdata(String userguid, String year, String month, String day, String username, String ouguid,
            String gonghao, String userid, String NotWorkDay) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select RowGuid from Audit_DDKQTJ_Month where UserGuid='" + userguid + "' and year='" + year
                + "' and month='" + month + "'";
        String rowguid = dao.find(sql, String.class);
        //如果为空，则需要初始化
        if (StringUtil.isBlank(rowguid)) {
            //获取部门名称
            sql = "select oushortname from frame_ou where OUGuid='" + ouguid + "'";
            String ouname = dao.find(sql, String.class);
            sql = "insert into Audit_DDKQTJ_Month(rowguid,username,userguid,ouguid,gonghao,userid,ouname,workday,takeoffday,year,month,cdcount,cdtime,ztcount,zttime,yzcdcount,yzcdtime,kgcdcount,kgcdtime) VALUE (NEWID(),'"
                    + username + "','" + userguid + "','" + ouguid + "','" + gonghao + "','" + userid + "','" + ouname
                    + "','0','" + NotWorkDay + "','" + year + "','" + month + "',0,0,0,0,0,0,0,0) ";
            dao.executeSqlCommand(sql);
        }
        dao.close();
    }

    //更新考勤统计表数据
    public static void updatedata(String userguid, String year, String month, String day, String kqstatus, int wdk,String qjstatus) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "";
        if ("休息".equals(kqstatus) || "旷工".equals(kqstatus) || wdk == 4) {
            sql = "update Audit_DDKQTJ_Month set " + day + "='" + kqstatus + "',gcdays='" + qjstatus + "' where userguid='" + userguid
                    + "' and year='" + year + "' and month='" + month + "'";
        }
        else {
            sql = "update Audit_DDKQTJ_Month set workday=workday+1," + day + "='" + kqstatus + "',gcdays='" + qjstatus
                    + "' where userguid='" + userguid + "' and year='" + year + "' and month='" + month + "'";
        }
        dao.executeSqlCommand(sql);
        dao.close();
    }

  

    //根据开始时间，结束时间，考勤类型判断考勤情况
    public static String getKQStatus(String userguid, String year1, String month1, String begintime, String endtime,
            String checktype, String userid, String dbtime) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select userchecktime,timeresult from KaoQinRecord where userid='" + userid + "' and checktype='"
                + checktype + "'and DATE_FORMAT(userchecktime,'%Y-%m-%d %H:%i')>=DATE_FORMAT('" + begintime
                + "','%Y-%m-%d %H:%i' ) and DATE_FORMAT(userchecktime,'%Y-%m-%d %H:%i')<=DATE_FORMAT('" + endtime
                + "','%Y-%m-%d %H:%i' )";
        List<Record> list = dao.findList(sql, Record.class);
        String status = "未打卡";
        if (list.size() > 0) {
            String userchecktime = list.get(0).getStr("userchecktime");
            String timeresult = list.get(0).getStr("timeresult");
            //timeresult Normal:正常；Early早退；Late：迟到；SeriousLate：严重迟到；Absenteeism：矿工迟到；NotSigned：未打卡
            if ("NotSigned".equals(timeresult)) {
                status = "未打卡";
            }
            if ("Normal".equals(timeresult)) {
                status = "正常";
            }
            if ("Early".equals(timeresult)) {
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int datepoor = 0;
                try {
                    datepoor = getDatePoor(format1.parse(dbtime), format1.parse(userchecktime));
                }
                catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                status = "早退" + datepoor + "分钟";
                String isqj = getISQJ(userguid, userchecktime);
                if ("0".equals(isqj)) {
                    String sqlnew = "update Audit_DDKQTJ_Month set ztcount=ztcount+1,zttime=zttime+" + datepoor
                            + " where userguid='" + userguid + "' and year='" + year1 + "' and month='" + month1 + "'";
                    dao.executeSqlCommand(sqlnew);
                }
            }
            if ("Late".equals(timeresult)) {
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int datepoor = 0;
                try {
                    datepoor = getDatePoor(format1.parse(userchecktime), format1.parse(dbtime));
                }
                catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                status = "迟到" + datepoor + "分钟";
                String isqj = getISQJ(userguid, userchecktime);
                if ("0".equals(isqj)) {
                    String sqlnew = "update Audit_DDKQTJ_Month set cdcount=cdcount+1,cdtime=cdtime+" + datepoor
                            + " where userguid='" + userguid + "' and year='" + year1 + "' and month='" + month1 + "'";
                    dao.executeSqlCommand(sqlnew);
                }
            }
            if ("SeriousLate".equals(timeresult)) {
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int datepoor = 0;
                try {
                    datepoor = getDatePoor(format1.parse(userchecktime), format1.parse(dbtime));
                }
                catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                status = "严重迟到" + datepoor + "分钟";
                String isqj = getISQJ(userguid, userchecktime);
                if ("0".equals(isqj)) {
                    String sqlnew = "update Audit_DDKQTJ_Month set yzcdcount=yzcdcount+1,yzcdtime=yzcdtime+" + datepoor
                            + " where userguid='" + userguid + "' and year='" + year1 + "' and month='" + month1 + "'";
                    dao.executeSqlCommand(sqlnew);
                }
            }
            if ("Absenteeism".equals(timeresult)) {
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int datepoor = 0;
                try {
                    datepoor = getDatePoor(format1.parse(userchecktime), format1.parse(dbtime));
                }
                catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                status = "旷工迟到" + datepoor + "分钟";
                String isqj = getISQJ(userguid, userchecktime);
                if ("0".equals(isqj)) {
                    String sqlnew = "update Audit_DDKQTJ_Month set kgcdcount=kgcdcount+1,kgcdtime=kgcdtime+" + datepoor
                            + " where userguid='" + userguid + "' and year='" + year1 + "' and month='" + month1 + "'";
                    dao.executeSqlCommand(sqlnew);
                }
            }
        }
        dao.close();
        return status;
    }

    //获取请假情况
    public static String getQJStatus(String userid, String begintime,String endtime,String userguid,String accessToken) {
    	String qjstatus = "";
    	String qjtime="";
    	if (accessToken != null) {
        String recordUrlqj = "https://oapi.dingtalk.com/topapi/attendance/getleavestatus?access_token=" + accessToken;
        JSONObject jsonObjectqj = new JSONObject();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
			long start = df.parse(begintime).getTime();
			
			long end=df.parse(endtime).getTime();
			jsonObjectqj.put("start_time", start);
            jsonObjectqj.put("end_time", end);
            jsonObjectqj.put("userid_list",userid);
            jsonObjectqj.put("offset", 0);
            jsonObjectqj.put("size", 10);
        String resultqj = HttpUtils.doPost(recordUrlqj, jsonObjectqj, "utf-8");
        JSONObject resutJSONqj = JSONObject.parseObject(resultqj);
        int errcode = (int) resutJSONqj.getInteger("errcode");
        JSONObject obj = (JSONObject) resutJSONqj.get("result");
        System.out.println("==========>>>>>获取请假信息值描述"+errcode);

        	if(0==errcode){
        		 JSONArray list = obj.getJSONArray("leave_status");
        		 if(list!=null && list.size()>0){
        			 for (int i = 0; i < list.size(); i++) {  
        				 Integer duration_percent=list.getJSONObject(i).getInteger("duration_percent");
        				 String duration_unit=list.getJSONObject(i).getString("duration_unit");
        				 long start_time=list.getJSONObject(i).getLong("start_time");
        				 long end_time=list.getJSONObject(i).getLong("end_time");
        				 DecimalFormat d=new DecimalFormat("0.0");
        				    qjtime=d.format((float)duration_percent/100);
        					Date startdate=new Date(start_time);
        					Date enddate=new Date(end_time);
        					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        					String startDateString=format.format(startdate);
        					String nendDateString=format.format(enddate);
        					System.out.println(startDateString);
        					System.out.println(nendDateString);
        			
        					 if(duration_unit.equals("percent_hour")){
             					qjstatus+="请假时间："+startDateString+"--"+nendDateString+"(共"+qjtime+"小时)";
        					 }else{
              					qjstatus+="请假时间："+startDateString+"--"+nendDateString+"(共"+qjtime+"天)";
        					 }
        			        CommonDao dao = CommonDao.getInstance();
        			       String sql = "insert into audit_orga_takeoff(rowguid,UserGuid,staus,BeginTime,EndTime) VALUE (NEWID(),'"
        		                    + userguid + "','" + 2 + "','" + startDateString + "','" + nendDateString + "') ";
        		            dao.executeSqlCommand(sql);   
        			        dao.close();

        			 }

        		 }
        	}
		} catch (ParseException e) {
		
			e.printStackTrace();
		}
    }
        return qjstatus;
    }

    //判断时间是否请假
    public static String getISQJ(String userguid, String begintime) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select * from audit_orga_takeoff where staus='2' and UserGuid='" + userguid
                + "' and DATE_FORMAT('" + begintime
                + "','%Y-%m-%d %H:%i')>= DATE_FORMAT(BeginTime,'%Y-%m-%d %H:%i') and DATE_FORMAT('" + begintime
                + "','%Y-%m-%d %H:%i')<= DATE_FORMAT(EndTime,'%Y-%m-%d %H:%i')";
        List<Record> list = dao.findList(sql, Record.class);
        dao.close();
        if (list.size() > 0) {
            return "1";
        }
        else {
            return "0";
        }
    }

    public static int getDatePoor(Date endDate, Date beginDate) {
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - beginDate.getTime();
        // 计算差多少分钟
        long min = diff / 1000 / 60;
        return (int) min;
    }
    
    public static String gettoken(){
    	
        String accessTokenUrl = "https://oapi.dingtalk.com/gettoken";
        String corpid = "ding2dihezws7tl7mhae";//替换成自己的corpid
        String secret = "cL_pYSq6k8Qm41VHftdhp8TBHyd6xI8G4xLRR7iDBfVM8ySNmnySzGcb-cAOBKeG";//替换成自己的corpsecret
        //获取accessToken
        String accessToken = DingTalkService.getAccessToken(accessTokenUrl, corpid, secret);
   
        return accessToken; 
    }
    
}
