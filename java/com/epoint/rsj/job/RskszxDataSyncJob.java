package com.epoint.rsj.job;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.rsj.api.IJnService;


@DisallowConcurrentExecution
public class RskszxDataSyncJob implements Job
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
            log.info("===============开始获取人事考试中心"+EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd")+"当天的办件数据===============");
            getSfDate();
            log.info("===============结束获取人事考试中心"+EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd")+"当天的办件数据===============");
        }
        catch (Exception e) {
        	e.printStackTrace();
            log.info("===============获取人事考试中心"+EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd")+"当天的办件数据出现异常===============");

        }
        
        
       
    }

    public void getSfDate() {
    	//开始获取人事考试中心当天事项所关联起来的办件信息
    	List<Record> baseinfos = ijnService.getRskszxProjectList();
    	log.info(EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd")+"==============人事考试中心当天办件数据量为：==============="+baseinfos.size());
		if (baseinfos == null || baseinfos.size() == 0) {
			log.info("===============无需要推送人事考试中心当天办件数据，结束服务===============");
		} else {
			SqlConditionUtil sql = new SqlConditionUtil();
			for(Record record : baseinfos) {
				// String area = data.get("area");
                 String areacode = "";
                 //查询办件是否存在对应的事项信息
                 sql.clear();
                 sql.eq("taskname", record.getStr("ITEMNAME"));
                 sql.isBlankOrValue("is_history", "0");
                 sql.eq("IS_EDITAFTERIMPORT", "1");
                 sql.eq("IS_ENABLE", "1");
                 sql.eq("ISTEMPLATE", "0");
                 sql.eq("AREACODE", "370800");
                 //判断当前数据库是否存在当前办件
                 Record projectByproid = ijnService.getIfSyncByProid(record.getStr("projid"));
                 if (projectByproid != null ) {
                     log.info("===============人事考试中心该办件已同步，办件标识为===============" + record.getStr("projid"));
                     //修改标识位
                     ijnService.updateByProid(record.getStr("ORGBUSNO"),"90");
                     continue;
                 }
                 List<AuditTask> tasklists = iAuditTask.getAuditTaskList(sql.getMap()).getResult();
                 if (tasklists == null || tasklists.size() == 0) {
                     log.error("===============人事考试中心事项数据未查询到===============" + record.getStr("REGION_ID"));
                     //修改标识位
                     ijnService.updateByProid(record.getStr("ORGBUSNO"),"99");
                     continue;
                 }
                 
                 AuditTask tasklist = tasklists.get(0);
                 
                 if(tasklist == null) {
                	 //修改标识位
                     ijnService.updateByProid(record.getStr("ORGBUSNO"),"90");
                	 log.info("事项未找到");
                	 continue;
                 }
                 
                 //办件信息插入
                 Record rec = new Record();
                 rec.setSql_TableName("audit_project_zjxt");
                 rec.set("rowguid", UUID.randomUUID().toString());
                 rec.set("OperateUserName", "人事考试中心数据同步服务");
                 rec.set("OperateDate", new Date());
                 rec.set("IS_TEST", 0);
                 List<Record> baseinf = ijnService.getHjProjectList(record.getStr("ORGBUSNO"));
                 if(!baseinf.isEmpty()) {
                	    rec.set("BANJIEDATE", 
                	    		EpointDateUtil.convertString2Date(baseinf.get(0).getStr("occurtime"), "yyyy-MM-dd HH:mm:ss"));
                 }
                 rec.set("BANJIERESULT", 40);
                 //                        rec.set("ACCEPTUSERDATE", baseinfo.get("SLRQ"));
                 rec.set("ACCEPTUSERNAME", record.getStr("TRANSACTOR"));
                 rec.set("acceptuserdate", EpointDateUtil.convertString2Date(record.getStr("occurtime"), "yyyy-MM-dd HH:mm:ss"));
                 rec.set("applydate", EpointDateUtil.convertString2Date(record.getStr("occurtime"), "yyyy-MM-dd HH:mm:ss"));
                 rec.set("APPLYERNAME", record.getStr("APPLICANT"));//申请人名称
                 rec.set("APPLYERTYPE", "20");//申请人类型
                 rec.set("certnum", record.getStr("APPLYERPAGECODE"));//申请人证照编号
                 rec.set("CERTTYPE", record.getStr("APPLYERPAGETYPE"));//申请人证照类型
                 rec.set("tasktype",tasklist.getType());//事项类型
                 rec.set("flowsn", record.getStr("projid"));
                
                 rec.set("STATUS", 90);
                 rec.set("WINDOWNAME", "");
                 rec.set("WINDOWGUID", "");

                 rec.set("OUGUID", tasklist.getOuguid());
                 rec.set("OUname", tasklist.getOuname());
                 rec.set("TASKGUID", tasklist.getRowguid());
                 rec.set("PROJECTNAME", tasklist.getTaskname());
                 rec.set("TASKID", tasklist.getTask_id());

                 rec.set("AREACODE", tasklist.getAreacode());
                 // 数据来源
                 rec.set("datasource", "005");
                 //插入到本地办件库中
                 int i = ijnService.insert(rec);
                 if (i > 0) {
                     //调用办理环节接口
                 	getSfHjDate(record.getStr("ORGBUSNO"));
                 }
                 //办件同步成功，修改标识位
                 ijnService.updateByProid(record.getStr("ORGBUSNO"),"1");

			}
		}
    }

    public void getSfHjDate(String ORGBUSNO) {
    	
        try {
        	List<Record> baseinfos = ijnService.getHjProjectList(ORGBUSNO);
    		if (baseinfos == null || baseinfos.size() == 0) {
    			log.info("===============无需要推送人事考试中心当天办件流程信息，对应的前置库标识为："+ORGBUSNO);
    		} else {
    			 for (Record data : baseinfos) {
                     Record rec = new Record();
                     rec.setSql_TableName("AUDIT_RS_APPLY_PROCESS_ZJXT");
                     rec.set("rowguid", UUID.randomUUID().toString());
                     rec.set("NODENAME", data.get("NODEADV"));
                   //  rec.set("NEXTNODENAME", data.get("nextStep"));
                     rec.set("PROJECTID", data.getStr("projid"));
                     rec.set("OperateDate", new Date());
                     ijnService.insert(rec);
                 }
    		}
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
