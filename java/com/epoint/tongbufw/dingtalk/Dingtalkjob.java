package com.epoint.tongbufw.dingtalk;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

public class Dingtalkjob implements Job
{

    Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        // TODO Auto-generated method stub
       
        try {
            log.info("========================>开始执行钉钉考勤数据同步服务！");
            EpointFrameDsManager.begin(null);
            Calendar now = Calendar.getInstance();
            int year1 = now.get(Calendar.YEAR);
            int month1 = now.get(Calendar.MONTH) + 1;
            int day1 = now.get(Calendar.DAY_OF_MONTH);
           String workDateFrom = year1 + "-" + month1 + "-" + day1 + " 00:01:00";
            String workDateTo = year1 + "-" + month1 + "-" + day1 + " 23:59:00";
           /* 
            String workDateFrom="2019-02-14 : 00:01:00";
            String workDateTo="2019-02-14 : 00:01:00";*/
            String offset = "0";//分页获取数据，0表示第一页
            String limit = "10";//每页10条数据
            String accessTokenUrl = "https://oapi.dingtalk.com/gettoken";
            String corpid = "ding2dihezws7tl7mhae";//替换成自己的corpid
            String secret = "cL_pYSq6k8Qm41VHftdhp8TBHyd6xI8G4xLRR7iDBfVM8ySNmnySzGcb-cAOBKeG";//替换成自己的corpsecret
            //获取accessToken
            String accessToken = DingTalkService.getAccessToken(accessTokenUrl, corpid, secret);
            if (accessToken != null) {
                String sqlmember = "select userid from audit_orga_member where IFNULL(userid,'')!=''";
                CommonDao dao = CommonDao.getInstance();
                List<Record> listmm = dao.findList(sqlmember, Record.class);
                dao.close();
                for (int ii = 0; ii < listmm.size(); ii++) {
                    String userid = listmm.get(ii).getStr("userid");
                    // hasMore = "true";//判断是否还存在未同步数据
                    DingTalkService.getCardList(accessToken, workDateFrom, workDateTo, offset, limit, userid);
                    /*                    while ("true".equals(hasMore)) {
                        hasMore = DingTalkService.getCardList(accessToken, workDateFrom, workDateTo, offset, limit,userid);
                        offset = String.valueOf(Integer.parseInt(offset) + Integer.parseInt(limit));
                    }*/
                }
            }
            log.info("========================>结束执行钉钉考勤数据同步服务！");
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
