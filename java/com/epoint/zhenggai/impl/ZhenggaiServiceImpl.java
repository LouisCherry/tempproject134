package com.epoint.zhenggai.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zhenggai.api.ZhenggaiService;

@Component
@Service
public class ZhenggaiServiceImpl implements ZhenggaiService{

	@Override
	public String getunidbyTaskid(String taskid) {
		String sql="select unid from audit_task where TASK_ID=? AND IFNULL(IS_HISTORY,0)=0"
	              +" AND IS_EDITAFTERIMPORT = 1 AND IS_ENABLE = 1 AND ISTEMPLATE = 0";
		return CommonDao.getInstance().queryString(sql,taskid);
	}

	@Override
	public String getchargelc(String taskguid) {
        String sql="select charge_lc from audit_task where rowguid=?";
		return CommonDao.getInstance().queryString(sql,taskguid);
	}

	@Override
	public List<Record> getjbjworkflows(String rowguid) {
    	String sql="select * from workflow_workitem where projectguid=? order by createdate";
		return CommonDao.getInstance().findList(sql, Record.class, rowguid);
	}

	@Override
	public List<AuditSpITask> getauditspitask(String subAppGuid) {
    	String sql="select a.* from audit_sp_i_task a left join audit_task b on a.taskguid=b.rowguid where subappguid=? order by b.areacode asc";
		return  CommonDao.getInstance().findList(sql, AuditSpITask.class, subAppGuid);
	}

	@Override
	public List<Record> getbbdb() {
        String sql ="SELECT fu.USERGUID userguid,u.DISPLAYNAME username,u.telephoneOffice telephoneOffice FROM frame_userrolerelation fu LEFT JOIN frame_role fr ON fu.ROLEGUID=fr.roleguid LEFT JOIN frame_user u ON fu.USERGUID=u.USERGUID WHERE ROLENAME='帮办待办';";
		return CommonDao.getInstance().findList(sql, Record.class);
	}

	@Override
	public AuditTaskExtension getAuditExtbytaskguid(String taskGuid) {
		String sql = "select qssxjhm from AUDIT_TASK_EXTENSION where taskGuid = ?";
		return CommonDao.getInstance().find(sql, AuditTaskExtension.class,taskGuid);
	}

	@Override
	public List<String> getwindowbyguid(String equmentguid) {
	    String sql="SELECT WINDOWGUID from audit_queue_window where equipmentguid=?1";
	    return CommonDao.getInstance().findList(sql, String.class, equmentguid);
	}

	@Override
	public void insert(Record record) {
		CommonDao.getInstance().insert(record);
		
	}

	@Override
	public String getuseridbyid(String userguid) {
	    String sqluserid = "select userid from audit_orga_member where UserGuid=?";
		return CommonDao.getInstance().queryString(sqluserid, userguid);
	}

	@Override
	public void getupdatebyrowguid(String guid) {
		 String sql = "update zjcs_agencycert set status='审批通过' where rowguid=?";
		 CommonDao.getInstance().execute(sql, guid);
	}

	@Override
	public void getupdatestatus(String guid) {
        String sql1 = "update agencyservice set status='1' where rowguid=?";
        CommonDao.getInstance().execute(sql1, guid);

	}

	@Override
	public void getdatestatuss(String guid) {
        String sql = "update zjcs_agencycert set status='审批不通过' where rowguid=?";

        CommonDao.getInstance().execute(sql, guid);

	}

	@Override
	public void getdatestatusss(String guid) {
		 String sql1 = "update agencyservice set status='2' where rowguid=?";
	     CommonDao.getInstance().execute(sql1, guid);
		
	}

	@Override
	public void getupdatestatusrowguid(String guid) {
		String sql = "update zjcs_agencycert set status='1' where rowguid=?";
		CommonDao.getInstance().execute(sql, guid);
		
	}

	@Override
	public void updatebycert(String guid) {
		String sql = "update zjcs_agencycert set status='2' where rowguid=?";
		CommonDao.getInstance().execute(sql, guid);		
	}

	@Override
	public void updatezjcsdispute(String replycontent, Date replyDate, String guid) {
		   String sql = "update zjcs_dispute set status='1',replycontent=? , ReplyDate=? where rowguid=?";
	       CommonDao.getInstance().execute(sql, replycontent, replyDate, guid);
		
	}
      
}
