package com.epoint.zhenggai.api;

import java.util.Date;
import java.util.List;

import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.core.grammar.Record;

public interface ZhenggaiService {

	String getunidbyTaskid(String taskid);

	String getchargelc(String taskguid);

	List<Record> getjbjworkflows(String rowguid);

	List<AuditSpITask> getauditspitask(String subAppGuid);

	List<Record> getbbdb();

	AuditTaskExtension getAuditExtbytaskguid(String taskGuid);

	List<String> getwindowbyguid(String equmentguid);

	void insert(Record record);

	String getuseridbyid(String userguid);

	void getupdatebyrowguid(String guid);

	void getupdatestatus(String guid);

	void getdatestatuss(String guid);

	void getdatestatusss(String guid);

	void getupdatestatusrowguid(String guid);

	void updatebycert(String guid);

	void updatezjcsdispute(String replycontent, Date replyDate, String guid);

}
