/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.knowledge.common;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import java.util.List;
import java.util.Map;

public interface ICnsCommon<T extends Record> {
	int addRecord(T arg0);

	int updateRecord(T arg0);

	int delete(T arg0);

	void deleteRecod(String arg0);

	void deleteRecords(String arg0, String arg1);

	T getBeanByGuid(String arg0);

	T getBeanByOneField(String arg0, String arg1);

	List<T> getListByOneField(String arg0, String arg1);

	List<T> getListByOneField(String arg0, String arg1, String arg2, String arg3);

	T getSelectBeanByOneField(String arg0, String arg1, String arg2);

	List<T> getSelectListByOneField(String arg0, String arg1, String arg2);

	List<T> getRecordList(String arg0, Map<String, String> arg1, String arg2, String arg3);

	Integer getRecordCount(Map<String, String> arg0);

	List<T> getRecordByPage(String arg0, Map<String, String> arg1, Integer arg2, Integer arg3, String arg4,
			String arg5);

	PageData<T> getRecordPageBean(String arg0, Map<String, String> arg1, Integer arg2, Integer arg3, String arg4,
			String arg5);
}