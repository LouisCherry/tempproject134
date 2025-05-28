
package com.epoint.knowledge.common;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.epoint.basic.bizlogic.sysconf.datasource.service.DataSourceService9;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public abstract class CnsCommonImpl<T extends Record> implements ICnsCommon<T> {
	private Class<T> clazz = this.getClazzByChild();
	protected ICommonDao commonDao;
	
	public CnsCommonImpl() {
		this.commonDao = CommonDao.getInstance();
	}

	public CnsCommonImpl(String dataSourceName) {
		//this.commonDao = CommonDao.getInstance(DataSourceService9.initDataSourceConfig(dataSourceName));
	    this.commonDao = CommonDao.getInstance();
	}

	public CnsCommonImpl(DataSourceConfig dataSource) {
		this.commonDao = CommonDao.getInstance(dataSource);
	}

	public CnsCommonImpl(ICommonDao commonDao) {
		this.commonDao = commonDao;
	}

	@Override
    public int addRecord(T t) {
		return this.commonDao.insert(t);
	}

	@Override
    public int updateRecord(T t) {
		return this.commonDao.update(t);
	}

	@Override
    public int delete(T t) {
		return this.commonDao.delete(t);
	}

	@Override
    public void deleteRecod(String rowguid) {
		if (StringUtil.isNotBlank(rowguid)) {
			Record record = this.getBeanByGuid(rowguid);
			this.commonDao.delete(record);
		}

	}

	@Override
    public void deleteRecords(String key, String value) {
		Entity entity = (Entity) this.clazz.getAnnotation(Entity.class);
		if (StringUtil.isNotBlank(entity.table())) {
			String sql = "delete from " + entity.table() + " where " + key + "=?1";
			this.commonDao.execute(sql, new Object[]{value});
		}

	}

	@Override
    public T getBeanByGuid(String rowguid) {
		return this.getSelectBeanByOneField("*", "ROWGUID", rowguid);
	}

	@Override
    public T getBeanByOneField(String key, String value) {
		return this.getSelectBeanByOneField("*", key, value);
	}

	@Override
    public List<T> getListByOneField(String key, String value) {
		return this.getSelectListByOneField("*", key, value);
	}

	@Override
    public List<T> getListByOneField(String key, String value, String sortField, String sortOrder) {
		return this.getSelectListByOneField("*", key, value, sortField, sortOrder);
	}

	@Override
    public T getSelectBeanByOneField(String selectField, String key, String value) {
		String sql = "";
		Entity entity = (Entity) this.clazz.getAnnotation(Entity.class);
		if (StringUtil.isNotBlank(key)) {
			sql = "select " + selectField + " from  " + entity.table() + " where " + key + "=?1";
			return this.commonDao.find(sql, this.clazz, new Object[]{value});
		} else {
			return null;
		}
	}

	@Override
    public List<T> getSelectListByOneField(String selectField, String key, String value) {
		String sql = "";
		Entity entity = (Entity) this.clazz.getAnnotation(Entity.class);
		if (StringUtil.isNotBlank(key)) {
			sql = "select " + selectField + " from  " + entity.table() + " where " + key + "=?1";
			return this.commonDao.findList(sql, this.clazz, new Object[]{value});
		} else {
			return null;
		}
	}

	public List<T> getSelectListByOneField(String selectField, String key, String value, String sortField,
			String sortOrder) {
		String sql = "";
		Entity entity = (Entity) this.clazz.getAnnotation(Entity.class);
		if (StringUtil.isNotBlank(key)) {
			sql = "select " + selectField + " from  " + entity.table() + " where " + key + "=?1";
			if (StringUtil.isNotBlank(sortField)) {
				sql = sql + " order by " + sortField + " " + sortOrder;
			}

			return this.commonDao.findList(sql, this.clazz, new Object[]{value});
		} else {
			return null;
		}
	}

	@Override
    public List<T> getRecordList(String selectField, Map<String, String> conditionMap, String sortField,
			String sortOrder) {
		return this.getListByCondition(this.clazz, selectField, conditionMap, sortField, sortOrder);
	}

	@Override
    public Integer getRecordCount(Map<String, String> conditionMap) {
		Integer count = Integer.valueOf(0);
		List list = this.getListByCondition(this.clazz, "count(1) as count", conditionMap, "", "");
		if (list != null && list.size() > 0) {
			count = ((Record) list.get(0)).getInt("count");
		}

		return count;
	}

	@Override
    public List<T> getRecordByPage(String selectField, Map<String, String> conditionMap, Integer first,
			Integer pageSize, String sortField, String sortOrder) {
		return this.getListByConditionPage(this.clazz, selectField, conditionMap, first, pageSize, sortField,
				sortOrder);
	}

	@Override
    public PageData<T> getRecordPageBean(String selectField, Map<String, String> conditionMap, Integer first,
			Integer pageSize, String sortField, String sortOrder) {
		PageData pageData = new PageData();
		pageData.setList(this.getRecordByPage(selectField, conditionMap, first, pageSize, sortField, sortOrder));
		pageData.setRowCount(this.getRecordCount(conditionMap).intValue());
		return pageData;
	}

	private Class<T> getClazzByChild() {
		return (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	private List<T> getListByConditionPage(Class<T> baseClass, String fields, Map<String, String> conditionMap,
			Integer first, Integer pageSize, String sortField, String sortOrder) {
		Entity entity = (Entity) baseClass.getAnnotation(Entity.class);
		StringBuffer sb = new StringBuffer(" where 1=1");
		if (conditionMap != null && conditionMap.size() > 0) {
			Iterator sql = conditionMap.entrySet().iterator();

			label66 : while (true) {
				while (true) {
					if (!sql.hasNext()) {
						break label66;
					}

					Entry dataList = (Entry) sql.next();
					String fieldName = ((String) dataList.getKey()).trim();
					if (!fieldName.endsWith("=") && !fieldName.endsWith(">") && !fieldName.endsWith("<")) {
						fieldName = fieldName.toUpperCase();
					}

					if (!fieldName.endsWith("=") && !fieldName.endsWith(">") && !fieldName.endsWith("<")) {
						if (fieldName.endsWith("LIKE")) {
							fieldName = fieldName.substring(0, fieldName.length() - 4);
							if (((String) dataList.getValue()).startsWith("%")) {
								sb.append(" and " + fieldName + " like \'" + (String) dataList.getValue() + "\'");
							} else {
								sb.append(" and " + fieldName + " like \'%" + (String) dataList.getValue() + "%\'");
							}
						} else if (fieldName.endsWith("IN")) {
							fieldName = fieldName.substring(0, fieldName.length() - 2);
							sb.append(" and " + fieldName + " in (" + (String) dataList.getValue() + ")");
						} else if (fieldName.endsWith("GT")) {
							fieldName = fieldName.substring(0, fieldName.length() - 2);
							sb.append(" and " + fieldName + "> \'" + (String) dataList.getValue() + "\'");
						} else if (fieldName.endsWith("GTE")) {
							fieldName = fieldName.substring(0, fieldName.length() - 3);
							sb.append(" and " + fieldName + ">= \'" + (String) dataList.getValue() + "\'");
						} else if (fieldName.endsWith("LT")) {
							fieldName = fieldName.substring(0, fieldName.length() - 2);
							sb.append(" and " + fieldName + "< \'" + (String) dataList.getValue() + "\'");
						} else if (fieldName.endsWith("LTE")) {
							fieldName = fieldName.substring(0, fieldName.length() - 3);
							sb.append(" and " + fieldName + "<= \'" + (String) dataList.getValue() + "\'");
						} else {
							sb.append(" and " + fieldName + " " + (String) dataList.getValue());
						}
					} else if (((String) dataList.getValue()).toLowerCase().startsWith("to_date")) {
						sb.append(" and " + fieldName + " " + (String) dataList.getValue() + " ");
					} else {
						sb.append(" and " + fieldName + "\'" + (String) dataList.getValue() + "\'");
					}
				}
			}
		}

		if (StringUtil.isNotBlank(sortField)) {
			sb.append(" order by " + sortField + " " + sortOrder);
		}

		String sql1 = "select " + fields + " from " + entity.table() + sb.toString();
		//new ArrayList();
		List dataList1;
		if (pageSize.intValue() != 0) {
			dataList1 = this.commonDao.findList(sql1, first.intValue(), pageSize.intValue(), baseClass, new Object[0]);
		} else {
			dataList1 = this.commonDao.findList(sql1, baseClass, new Object[0]);
		}

		return dataList1;
	}

	private List<T> getListByCondition(Class<T> baseClass, String fields, Map<String, String> conditionMap,
			String sortField, String sortOrder) {
		return this.getListByConditionPage(baseClass, fields, conditionMap, Integer.valueOf(0), Integer.valueOf(0),
				sortField, sortOrder);
	}
}