package com.epoint.xmz.job.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.core.grammar.Record;

/**
 * 好差评相关接口
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public interface IGsProjectService extends Serializable
{

	List<AuditProject> getWaitEvaluateSbList();
	
	List<AuditRsItemBaseinfo> getWaitItemList();
	
	List<AuditRsItemBaseinfo> getSwWaitItemList();

	List<Record> getSpglSgxkzList();

	List<Record> getSpglSgxkzXkList();

	List<Record> getSpglSgxkzDantiList();

	List<Record> getHpDataList();

	void insert(Record rec);
	
	void insertQzk(Record rec);
	
	void updateQzk(Record rec);

	Record getSpglJgYsInfoBySubappguid(String projectguid);

	Record getSpglSgxkInfoBySubappguid(String projectguid);

	List<Record> getSpglParticipnListBySubappguid(String projectguid);

	Record getSpglDantiInfoBySubappguid(String projectguid);

	Record getSpglSgxkIteminfoBySubappguid(String projectguid);

	Record getProjectBasicInfoByRowguid(String id);
	
	Record getProjectUnitInfoByRowguid(String id);

	Record getSgxkzInfoByPorjectId(String id);

	AuditSpSpSgxk getSpSgxkByRowguid(String rowguid);

	Record getProjectCorpInfoByRowguid(String id);

	Record getProjectFinishByRowguid(String id);

	Record getProjectLicenceByRowguid(String id);

}
