package com.epoint.tongbufw.dingtalk;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.authentication.UserSession;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

public class DingtalkdealJob implements Job
{
    transient Logger log = LogUtil.getLog(DingtalkdealJob.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        // TODO Auto-generated method stub

        try {
            log.info("开始处理考勤数据 =====>时间："
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            EpointFrameDsManager.begin(null);

            Calendar now = Calendar.getInstance();
            //获取当前年月日
            String year1 = now.get(Calendar.YEAR) + "";
            int mm = now.get(Calendar.MONTH) + 1;
            String month1 = mm + "";
            String day1 = now.get(Calendar.DAY_OF_MONTH)+ "";
            //获取考勤时间设置
            String swsbtime = "";
            String swxbtime = "";
            String xwsbtime = "";
            String xwxbtime = "";
            
            List<Record> AuditDailyTimeList = DingtalkdealService.getAuditDailyTime();
            if (AuditDailyTimeList.size() > 0) {
                swsbtime = AuditDailyTimeList.get(0).getStr("swsbtime");
                swxbtime = AuditDailyTimeList.get(0).getStr("swxbtime");
                xwsbtime = AuditDailyTimeList.get(0).getStr("xwsbtime");
                xwxbtime = AuditDailyTimeList.get(0).getStr("xwxbtime");
            }
            if (StringUtil.isBlank(swsbtime)) {
                swsbtime = "9:00";
            }
            if (StringUtil.isBlank(swxbtime)) {
                swxbtime = "12:00";
            }
            if (StringUtil.isBlank(xwsbtime)) {
                xwsbtime = "14:00";
            }
            if (StringUtil.isBlank(xwxbtime)) {
                xwxbtime = "17:00";
            }
            String swsb = year1 + "-" + month1 + "-" + day1 + " " + swsbtime + ":00";
            String swxb = year1 + "-" + month1 + "-" + day1 + " " + swxbtime + ":00";
            String xwsb = year1 + "-" + month1 + "-" + day1 + " " + xwsbtime + ":00";
            String xwxb = year1 + "-" + month1 + "-" + day1 + " " + xwxbtime + ":00";
            String begintime = year1 + "-" + month1 + "-" + day1 + " 00:00:00";
            String midtime = year1 + "-" + month1 + "-" + day1 + " 12:30:00";
            String endtime = year1 + "-" + month1 + "-" + day1 + " 21:00:00";
            

            //获取本月还剩余的休息时间
            String NotWorkDay = DingtalkdealService.getNotWorkDay(begintime);
            //获取所有考勤人员信息
            List<Record> MemberList = DingtalkdealService.getMember();
            String token=DingtalkdealService.gettoken();
            for (int i = 0; i < MemberList.size(); i++) {
                //根据userguid，year，month，判断是否初始化过数据，未初始化，则初始化一次
                String userguid = MemberList.get(i).getStr("userguid");
                String username = MemberList.get(i).getStr("username");
                String ouguid = MemberList.get(i).getStr("ouguid");
                String gonghao = MemberList.get(i).getStr("gonghao");
                String userid = MemberList.get(i).getStr("userid");
                DingtalkdealService.initdata(userguid, year1, month1, day1, username, ouguid, gonghao, userid,
                        NotWorkDay);

                String isWorkDay = DingtalkdealService.getWorkDay(begintime);
                String kqstatus = "";
                //请假信息
                String qjstatus = "";
                int wdk = 0;

                if ("1".equals(isWorkDay)) {
                    //未打卡次数，如果有四次未打卡，则说明当天未出勤
                    qjstatus = DingtalkdealService.getQJStatus(userid, begintime,endtime,userguid,token);
              
                    String status = "";
                    //判断上午上班情况
                    status = DingtalkdealService.getKQStatus(userguid, year1, month1,begintime, midtime, "OnDuty", userid, swsb);
                    if ("未打卡".equals(status)) {
                        wdk++;
                        kqstatus = kqstatus + "上午上班" + status + ";";
                    }
                    else {
                        if (!"正常".equals(status)) {
                            kqstatus = kqstatus + "上午上班" + status + ";";
                        }
                    }
                    //判断上午下班情况
                    status = DingtalkdealService.getKQStatus(userguid, year1, month1,begintime, midtime, "OffDuty", userid, swxb);
                    if ("未打卡".equals(status)) {
                        wdk++;
                        kqstatus = kqstatus + "上午下班" + status + ";";
                    }
                    else {
                        if (!"正常".equals(status)) {
                            kqstatus = kqstatus + "上午下班" + status + ";";
                        }
                    }
                    //判断下午上班情况
                    status = DingtalkdealService.getKQStatus(userguid, year1, month1,midtime, endtime, "OnDuty", userid, xwsb);
                    if ("未打卡".equals(status)) {
                        wdk++;
                        kqstatus = kqstatus + "下午上班" + status + ";";
                    }
                    else {
                        if (!"正常".equals(status)) {
                            kqstatus = kqstatus + "下午上班" + status + ";";
                        }
                    }
                    //判断下午下班情况
                    status = DingtalkdealService.getKQStatus(userguid, year1, month1,midtime, endtime, "OffDuty", userid, xwxb);
                    if ("未打卡".equals(status)) {
                        wdk++;
                        kqstatus = kqstatus + "下午下班" + status + ";";
                    }
                    else {
                        if (!"正常".equals(status)) {
                            kqstatus = kqstatus + "下午下班" + status + ";";
                        }
                    }
                    //判断当天请假情况
                    if (StringUtil.isNotBlank(qjstatus)) {
                        kqstatus = kqstatus+"("+qjstatus+")";
                    }
                    else {
                        if (wdk == 4) {
                            kqstatus = "旷工";
                        }
                        else {
                            if (StringUtil.isBlank(kqstatus)) {
                                kqstatus = "正常";
                            }
                        }
                    }
                }
                else {
                    kqstatus = "休息";
                }
                //更新考勤统计表数据
                DingtalkdealService.updatedata(userguid, year1, month1, "day" + day1, kqstatus, wdk,qjstatus);
            }
            log.info("结束处理考勤数据 =====>时间："
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

}
