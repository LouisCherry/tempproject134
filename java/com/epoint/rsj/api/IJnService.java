package com.epoint.rsj.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;

/**
 * 济宁通用service
 * @作者 zym
 * @version [版本号, 2021年8月20日]
 */
public interface IJnService extends Serializable
{


    public int insert(Record rec);

	/**
	 * 获取办件基本信息
	 * @param taskName 事项名称
	 * @param areacode 辖区编码
	 * @return 返回事项基本信息
	 */
	public AuditTask getAuditBasicInfo(String taskName, String areacode);

	/**
	 * 通过rowguid查询表中是已有否有对应数据
	 * @param rowguid
	 * @return
	 */
	public Record getAuditProjectZjxtByRowguid(String rowguid);

	/**
	 * 通过rowguid查询表中是已有否有对应数据
	 * @param rowguid
	 * @return
	 */
	public Record getAuditRsApplyZjxtByRowguid(String rowguid);

    public String getAreacodeByareaname(String areaname);

	public List<Record> getGxbysjyProjectList();
	public List<Record> getSjglProjectList();
	public List<Record> getRskszxProjectList();
	public List<Record> getZyjsryProjectList();
	
	public List<Record> getZyjnjdProjectList();
	
	public List<Record> getKjjProjectList();

	public List<Record> getHjProjectList(String oRGBUSNO);

	public Record getIfSyncByProid(String str);

	public int updateByProid(String str,String status);
}
