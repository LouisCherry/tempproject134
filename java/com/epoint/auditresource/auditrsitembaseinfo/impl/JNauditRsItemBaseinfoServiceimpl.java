package com.epoint.auditresource.auditrsitembaseinfo.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditresource.auditrsitembaseinfo.api.IJNauditRsItemBaseinfoservice;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
@Component
@Service
public class JNauditRsItemBaseinfoServiceimpl implements IJNauditRsItemBaseinfoservice {

	@Override
	public List<AuditSpPhase> getphaseNamebybusinessguid(String bigguid) {
		String sql="SELECT a.* FROM audit_sp_phase a LEFT JOIN audit_sp_instance b ON a.BUSINEDSSGUID=b.BUSINESSGUID  WHERE b.RowGuid=?";
		return CommonDao.getInstance().findList(sql, AuditSpPhase.class,bigguid);
	}

	@Override
	public List<AuditSpITask> gettaskbybigguid(String xiangmuguid, String phaseguid) {
		String sql="SELECT a.TASKNAME,b.ouguid,b.OUNAME,a.TASKGUID,a.BIGUID,a.PROJECTGUID,d.STATUS,f.SPAREMINUTES,d.promise_day,d.RECEIVEDATE,d.BANJIEDATE FROM audit_sp_i_task a LEFT JOIN audit_task b ON a.TASKGUID = b.RowGuid LEFT JOIN audit_rs_item_baseinfo c on a.BIGUID=c.BIGUID JOIN audit_project d ON d.rowguid=a.PROJECTGUID LEFT JOIN audit_project_sparetime f on d.RowGuid=f.PROJECTGUID WHERE c.RowGuid =? AND PHASEGUID =?";
		return CommonDao.getInstance().findList(sql, AuditSpITask.class,xiangmuguid,phaseguid);
	}
	
	@Override
	public List<AuditSpITask> gettaskbybigguidOld(String xiangmuguid, String phaseguid) {
		String sql="SELECT a.TASKNAME,b.ouguid,b.OUNAME,a.TASKGUID,a.BIGUID,a.PROJECTGUID,d.STATUS,f.SPAREMINUTES,d.promise_day,d.RECEIVEDATE,d.BANJIEDATE FROM audit_sp_i_task a LEFT JOIN audit_task b ON a.TASKGUID = b.RowGuid LEFT JOIN audit_rs_item_baseinfo c on a.BIGUID=c.BIGUID JOIN audit_project_old d ON d.rowguid=a.PROJECTGUID LEFT JOIN audit_project_sparetime f on d.RowGuid=f.PROJECTGUID WHERE c.RowGuid =? AND PHASEGUID =?";
		return CommonDao.getInstance().findList(sql, AuditSpITask.class,xiangmuguid,phaseguid);
	}
	

	@Override
	public int gettaskbytotal(String phaseguid, String bigguid) {
       String sql="SELECT count(1) FROM audit_sp_i_task a LEFT JOIN audit_task b ON a.TASKGUID = b.RowGuid WHERE BIGUID =? AND PHASEGUID =?";
       return  CommonDao.getInstance().queryInt(sql, bigguid,phaseguid);
	}
	
	@Override
	public Record getAuditProjectbyrowguid(String rowguid) {
		String sql="select b.TaskName,a.PROMISE_DAY,a.WINDOWNAME,a.ACCEPTUSERDATE,a.BANJIEDATE,a.`STATUS` STATUS ,c.SPAREMINUTES from audit_project a LEFT JOIN audit_task b ON a.TASKGUID=b.RowGuid LEFT JOIN audit_project_sparetime c ON a.RowGuid=c.PROJECTGUID WHERE a.RowGuid=?";
		
       return  CommonDao.getInstance().find(sql,Record.class,rowguid);
	}

	@Override
	public List<Record> getphase(String guid) {
		String sql="SELECT a.PHASENAME,a.RowGuid,count(a.RowGuid) AS cum,c.CREATEDATE,c.FINISHDATE,d.itemname,b.BUSINESSNAME "
		        + " FROM audit_sp_phase a LEFT JOIN audit_sp_i_subapp c ON c.PHASEGUID=a.RowGuid "
		        + " LEFT JOIN audit_rs_item_baseinfo d ON d.BIGUID=c.BIGUID"
		        + " LEFT JOIN audit_sp_business b ON c.BUSINESSGUID = b.RowGuid"
		        + " WHERE d.rowguid='"+guid+"' GROUP BY a.RowGuid ORDER BY a.ORDERNUMBER desc";
		return  CommonDao.getInstance().findList(sql, Record.class);
	}

	@Override
	public String getstatusnamebyguid(String status) {
		String sql="select b.ITEMTEXT from code_main a LEFT JOIN code_items b ON a.CODEID=b.CODEID WHERE a.CODENAME='办件状态' AND b.ITEMLEVCODE=?";
		return CommonDao.getInstance().queryString(sql, status);
	}

	public AuditProject getProjectByTaskguid(String taskguid,String biguid){
	    String sql = "select p.rowguid,p.receivedate,ta.ouname,p.projectname,p.applyername,p.BANJIEDATE from audit_project p inner join"
	            + " audit_sp_i_task t on t.PROJECTGUID=p.RowGuid"
	            + " INNER JOIN audit_task ta  on t.taskguid = ta.rowguid where t.TASKGUID=?1 and t.BIGUID=?2";
	    return CommonDao.getInstance().find(sql, AuditProject.class, taskguid, biguid);
	}

    @Override
    public Record getSumOfInstance(String areacode,String type) {
        String sql = "select SUM(1) as total,SUM(date_format(CREATEDATE,'%Y-%m')=date_format(CURDATE(),'%Y-%m')) AS curmonthsum,"
                 +" SUM(date_format(CREATEDATE,'%Y-%m')=SUBSTR(date_add(date_format(CURDATE(),'%Y-%m-%d'),INTERVAL -1 YEAR_MONTH),1,7)) AS backmonthsum "
                 +" from audit_sp_instance where businesstype=?";
        if(StringUtil.isNotBlank(areacode)){
            sql += " and areacode = '"+areacode+"'";
        }
        return CommonDao.getInstance().find(sql,Record.class,type);
    }
	
}
