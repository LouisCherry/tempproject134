package com.epoint.hikJob;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.ces.requesthiklog.api.IRequestHikLogService;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class RequestHikAttendanceJob
        implements Job {
    transient Logger log = LogUtil.getLog(RequestHikAttendanceJob.class);

    private IRequestHikLogService requestHikLogService = (IRequestHikLogService) ContainerFactory.getContainInfo()
            .getComponent(IRequestHikLogService.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("=====================开始执行RequestHikAttendanceJob服务================");

            EpointFrameDsManager.begin(null);

            HTTPClientUtil client = new HTTPClientUtil();
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", "xt12345678");
            HTTPClientUtil.client.getState().setCredentials(AuthScope.ANY, creds);

            String[] ipList = {"10.159.103.144", "10.159.103.145", "10.159.103.146", "10.159.103.147", "10.159.103.148",
                    "10.159.103.149", "10.159.103.150", "10.159.103.151", "10.159.103.152", "10.159.103.153"};

            String date = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd");

            List<String> ghlist = requestHikLogService.getAllUserGh();
            String parmStartDate = "";
            String parmendDate = "";

            if (!timeCompare("13:00:00")) {
                parmStartDate = "T08:00:55+08:00";
                parmendDate = "T09:01:55+08:00";
            } else {
                parmStartDate = "T12:00:55+08:00";
                parmendDate = "T13:01:55+08:00";
            }

            for (String gh : ghlist) {
                boolean isdk = false;
                JSONObject param = new JSONObject();

                JSONObject hikparam = new JSONObject();
                param.put("searchID", "1");
                param.put("searchResultPosition", Integer.valueOf(0));
                param.put("maxResults", Integer.valueOf(30));
                param.put("major", Integer.valueOf(0));
                param.put("minor", Integer.valueOf(0));
                param.put("cardNo", gh);
                param.put("startTime", date + parmStartDate);
                param.put("endTime", date + parmendDate);
                hikparam.put("AcsEventCond", param);
                for (String ip : ipList) {
                    String url = "http://" + ip + ":80/ISAPI/AccessControl/AcsEvent?format=json";
                    String returnData = HTTPClientUtil.doPost(url, hikparam.toString());
                    JSONObject obj = JSONObject.parseObject(returnData);
                    if (!returnData.contains("AcsEvent"))
                        break;
                    String totalMatches = obj.getJSONObject("AcsEvent").get("totalMatches").toString();
                    if ("0".equals(totalMatches)) {
                        continue;
                    }

                    isdk = true;

                    JSONArray info = obj.getJSONObject("AcsEvent").getJSONArray("InfoList");
                    JSONObject datajson = info.getJSONObject(0);
                    Record rec = new Record();
                    rec.set("gonghao", datajson.get("cardNo"));
                    rec.set("time", datajson.get("time"));
                    rec.set("name", datajson.get("name"));
                    rec.set("date", new Date());
                    rec.set("rowguid", UUID.randomUUID().toString());
                    rec.setSql_TableName("hikrecord");
                    requestHikLogService.insert(rec);
                    requestHikLogService.CloseConnection();
                    break;
                }

                if (isdk)
                    continue;
                Record rec = new Record();
                rec.set("gonghao", gh);
                rec.set("time", null);
                rec.set("name", null);
                rec.set("date", new Date());
                rec.set("rowguid", UUID.randomUUID().toString());
                rec.setSql_TableName("hikrecord");
                requestHikLogService.insert(rec);
                requestHikLogService.CloseConnection();
            }

            log.info("================结束执行RequestHikAttendanceJob服务======================");

            EpointFrameDsManager.commit();
        } catch (Exception e) {
            log.error("================执行RequestHikAttendanceJob服务异常===================");

            EpointFrameDsManager.rollback();
            e.printStackTrace();
        } finally {
            EpointFrameDsManager.close();
        }
    }

    public boolean timeCompare(String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(time, dtf);
        return LocalTime.now().isAfter(localTime);
    }
}