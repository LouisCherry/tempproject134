package com.epoint.xmz.job;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.xmz.api.IJnService;

import net.sf.json.JSONArray;

//调用司法厅接口，将办件同步到济宁办件库
@DisallowConcurrentExecution
public class SfDataSyncJob implements Job
{

    transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

    IJnService ijnService = ContainerFactory.getContainInfo().getComponent(IJnService.class);
    IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
            .getComponent(IAuditRsItemBaseinfo.class);
    IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
    IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
    IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() {
        try {
            log.info("===============开始获取司法办件数据===============");
            getSfDate();
            log.info("===============结束获取司法办件数据===============");
        }
        catch (Exception e) {
            log.info("===============获取司法办件数据出现异常===============");
        }
    }

    public void getSfDate() {
        String url = "http://117.73.254.121:81/app/assistanceDataInterface/list";
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject bizparams = new JSONObject();
        	Date dNow = new Date();   //当前时间
        	Date dBefore = new Date();
        	Calendar calendar = Calendar.getInstance(); //得到日历
        	calendar.setTime(dNow);//把当前时间赋给日历
        	calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        	dBefore = calendar.getTime();   //得到前一天的时间
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00"); //设置时间格式
        	String bdate = sdf.format(dBefore);    //格式化前一天
        	String edate = sdf.format(dNow); //格式化当前时间
            bizparams.put("shi", "济宁市");
            bizparams.put("appUser", "FE9F9E8F4C624DEA801D1A2B4431B559");
            bizparams.put("onlySign", "1D3E7412F51D7BB20592162F366E9ABA");
            bizparams.put("step", "60");
            bizparams.put("bdate", bdate);
            bizparams.put("edate", edate);
            //调用司法的接口获取到对应的办件数据
            String rtnstr = HttpUtil.doPostJson(url, bizparams.toString());
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("1".equals(rtnjson.getString("result"))) {
                    JSONArray jsonarray = JSONArray.fromObject(rtnjson.getString("data"));
                    List<Record> list = (List) JSONArray.toCollection(jsonarray, Record.class);
                    for (Record data : list) {
                        String area = data.get("area");
                        String[] areatemp = area.split(",");
                        String areacode = "";
                        if (areatemp.length > 2) {
                            areacode = ijnService.getAreacodeByareaname(areatemp[2]);
                        }
                        else {
                            areacode = "370800";
                        }
                        //去查询系统自身事项信息
                        sql.clear();
                        sql.eq("taskname", "法律援助实施");
                        sql.isBlankOrValue("is_history", "0");
                        sql.eq("IS_EDITAFTERIMPORT", "1");
                        sql.eq("IS_ENABLE", "1");
                        sql.eq("ISTEMPLATE", "0");
                        sql.eq("AREACODE", areacode);

                        List<AuditTask> tasklist = iAuditTask.getAuditTaskList(sql.getMap()).getResult();
                        if (tasklist == null || StringUtil.isBlank(tasklist)) {
                            log.error("===============司法事项数据未查询到===============" + areacode);
                            continue;
                        }
                        //维护办件信息插入
                        Record rec = new Record();
                        rec.setSql_TableName("audit_project_zjxt");
                        rec.set("rowguid", UUID.randomUUID().toString());
                        rec.set("OperateUserName", "司法数据同步服务");
                        rec.set("OperateDate", new Date());
                        rec.set("IS_TEST", "0");
                        rec.set("BANJIEDATE", data.get("sj"));
                        rec.set("BANJIERESULT", "40");
                        //                        rec.set("ACCEPTUSERDATE", baseinfo.get("SLRQ"));
                        rec.set("ACCEPTUSERNAME", data.get("department"));
                        rec.set("APPLYERNAME", data.get("xm"));
                        rec.set("flowsn", data.get("id"));
                        //                        rec.set("CONTACTMOBILE", baseinfo.get("BLRLXSJ"));
                        //                        rec.set("CONTACTPHONE", baseinfo.get("BLRLXSJ"));
                        //                        rec.set("CONTACTPERSON", baseinfo.get("BLR"));
                        rec.set("APPLYDATE", data.get("sj"));

                        //                        rec.set("CERTNUM", baseinfo.get("SQRZJHM"));

                        //                        if ("111".equals(baseinfo.get("SQRZJLX"))) {
                        //                            rec.set("CERTTYPE", "16");
                        //                        }
                        //                        if ("02".equals(baseinfo.get("SQRZJLX"))) {
                        //                            rec.set("CERTTYPE", "22");
                        //                        }
                        rec.set("STATUS", "90");
                        //                        rec.set("TASKTYPE", "1");
                        // 窗口信息先空着
                        rec.set("WINDOWNAME", "");
                        rec.set("WINDOWGUID", "");

                        rec.set("OUGUID", tasklist.get(0).getOuguid());
                        rec.set("OUname", tasklist.get(0).getOuname());
                        rec.set("TASKGUID", tasklist.get(0).getRowguid());
                        rec.set("PROJECTNAME", tasklist.get(0).getTaskname());
                        rec.set("TASKID", tasklist.get(0).getTask_id());

                        rec.set("AREACODE", areacode);
                        // 中心guid 待议
                        //   rec.set("CENTERGUID", baseinfo.get("ORGID"));
                        //   rec.set("CONTACTCERTNUM", baseinfo.get("SQRZJHM"));
                        // 数据来源
                        rec.set("datasource", "003");
                        int i = ijnService.insert(rec);
                        if (i > 0) {
                            //调用办理环节接口
                        	getSfHjDate(data.get("id"));
                        }
                    }
                }
                else {
                }
            }
            else {
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSfHjDate(String id) {
        String url = "http://117.73.254.121:81/app/assistanceDataInterface/process";
        JSONObject rtnjson = new JSONObject();
        try {
            JSONObject params = new JSONObject();
            params.put("id",id);
            params.put("appUser", "FE9F9E8F4C624DEA801D1A2B4431B559");
            params.put("onlySign", "1D3E7412F51D7BB20592162F366E9ABA");
            String rtnstr = HttpUtil.doPostJson(url, params.toString());
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(rtnstr)) {
                rtnjson = JSON.parseObject(rtnstr);
                if ("1".equals(rtnjson.getString("result"))) {
                    JSONArray jsonarray = JSONArray.fromObject(rtnjson.getString("data"));
                    List<Record> list = (List) JSONArray.toCollection(jsonarray, Record.class);
                    for (Record data : list) {
                        Record rec = new Record();
                        rec.setSql_TableName("AUDIT_RS_APPLY_PROCESS_ZJXT");
                        rec.set("rowguid", UUID.randomUUID().toString());
                        rec.set("NODENAME", data.get("step"));
                        rec.set("NEXTNODENAME", data.get("nextStep"));
                        rec.set("PROJECTID", data.get("hid"));
                        ijnService.insert(rec);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
