package com.epoint.xmz.wjw;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
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
public class WjwProjectSyncJob implements Job
{

    transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

    IAuditOnlineProject iAuditOnlineProject = ContainerFactory.getContainInfo().getComponent(IAuditOnlineProject.class);
   
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
            log.info("===============开始同步微警务办结办件===============");
            getWjwProject();
            log.info("===============结束同步微警务办结办件===============");
        }
        catch (Exception e) {
            log.info("===============获取微警务办结办件出现异常===============");
        }
    }

	public void getWjwProject() {
        String sql = "select * from audit_online_project where wjwdataid is not null and ifnull(wjwsync,0) = 0 limit 10";
        @SuppressWarnings("static-access")
		List<AuditOnlineProject> projects = new CommonDao().getInstance().findList(sql, AuditOnlineProject.class);
        try {
	        for (AuditOnlineProject project : projects) {
	        	String dataid  = project.getStr("wjwdataid");
	        	String projectguid  = project.getSourceguid();
	        	JSONObject submitString = new JSONObject();
	        	submitString.put("projectguid", projectguid);
	        	submitString.put("dataid", dataid);
	        	String result = HttpUtil.doPostJson("http://112.6.110.176:25001/jnzwdt/rest/jnwjw/getReuslt", submitString.toString());
	        	log.info("wjwresult:"+result);
	        }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
