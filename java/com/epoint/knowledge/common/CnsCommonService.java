
package com.epoint.knowledge.common;


import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import java.util.List;
import java.util.Map;

public abstract class CnsCommonService<T extends Record> {
	protected abstract ICnsCommon<T> getICnsCommon();

	public CnsCommonService() {
		this.getICnsCommon();
	}

	public List<T> getRecordList(String selectField, Map<String, String> conditionMap, String sortField,
			String sortOrder) {
		return this.getICnsCommon().getRecordList(selectField, conditionMap, sortField, sortOrder);
	}

	public Integer getRecordCount(Map<String, String> conditionMap) {
		return this.getICnsCommon().getRecordCount(conditionMap);
	}

	public List<T> getRecordPage(String selectField, Map<String, String> conditionMap, Integer first, Integer pageSize,
			String sortField, String sortOrder) {
		return this.getICnsCommon().getRecordByPage(selectField, conditionMap, first, pageSize, sortField, sortOrder);
	}

	public PageData<T> getRecordPageBean(String selectField, Map<String, String> conditionMap, Integer first,
			Integer pageSize, String sortField, String sortOrder) {
		return this.getICnsCommon().getRecordPageBean(selectField, conditionMap, first, pageSize, sortField, sortOrder);
	}

	public void addRecord(T t) {
		this.getICnsCommon().addRecord(t);
	}

	public void updateRecord(T t) {
		this.getICnsCommon().updateRecord(t);
	}

	public void delete(T t) {
		this.getICnsCommon().delete(t);
	}

	public void deleteRecord(String rowguid) {
		this.getICnsCommon().deleteRecod(rowguid);
	}

	public void deleteRecords(String key, String value) {
		this.getICnsCommon().deleteRecords(key, value);
	}

	public T getBeanByOneField(String key, String value) {
		return this.getICnsCommon().getBeanByOneField(key, value);
	}

	public T getBeanByGuid(String rowguid) {
		return this.getICnsCommon().getBeanByGuid(rowguid);
	}

	public List<T> getListByOneField(String key, String value) {
		return this.getICnsCommon().getListByOneField(key, value);
	}

	public List<T> getListByOneField(String key, String value, String sortField, String sortOrder) {
		return this.getICnsCommon().getListByOneField(key, value, sortField, sortOrder);
	}

	public T getSelectRecordByField(String selectField, String key, String value) {
		return this.getICnsCommon().getSelectBeanByOneField(selectField, key, value);
	}

	public List<T> getSelectRecordList(String selectField, String key, String value) {
		return this.getICnsCommon().getSelectListByOneField(selectField, key, value);
	}
}