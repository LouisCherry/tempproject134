package com.epoint.zwfw.dj.api;

import java.io.Serializable;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;

/**
 * 济宁通用service
 * @作者 zym
 * @version [版本号, 2021年8月20日]
 */
public interface IJnDjService extends Serializable
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
	 * 获取办件基本信息
	 * @param item_id 事项编码
	 * @return 返回事项基本信息
	 */
	public AuditTask getAuditBasicInfo(String item_id);

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

}
