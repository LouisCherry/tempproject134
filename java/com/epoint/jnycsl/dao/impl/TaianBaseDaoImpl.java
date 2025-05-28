package com.epoint.jnycsl.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnycsl.dao.TaianBaseDao;
import com.epoint.jnycsl.utils.TaianStringUtils;


/**
 * 基础dao
 * @author 刘雨雨
 * @time 2018年9月12日下午3:36:24
 */
@Repository
public class TaianBaseDaoImpl implements TaianBaseDao {

	@Override
	public <T extends BaseEntity> T findEntity(Class<T> entity, String id) {
		String primaryKey = entity.getAnnotation(Entity.class).id()[0];
		return findEntity(entity, primaryKey, id);
	}

	@Override
	public <T extends BaseEntity> T findEntity(Class<T> entity, String columnName, Object columnValue) {
		List<T> entities = findEntities(entity, columnName, columnValue);
		if (entities != null && entities.size() > 0) {
			return entities.get(0);
		} else {
			return null;
		}
	}

	@Override
	public <T extends BaseEntity> int deleteEntities(Class<T> entity, String[] rowguids) {
		if (StringUtil.isBlank(rowguids)) {
			return 0;
		}
		ICommonDao commonDao = CommonDao.getInstance();
		Entity entityAnnotation = entity.getAnnotation(Entity.class);
		String tableName = entityAnnotation.table();
		String primaryKey = entityAnnotation.id()[0]; // 主键一般只有一个
		String sql = "delete from " + tableName + " where " + primaryKey + " in ("
				+ TaianStringUtils.formatStr(rowguids) + ")";
		int affectRows = commonDao.execute(sql);
		return affectRows;
	}

	@Override
	public <T extends BaseEntity> List<T> findEntities(Class<T> entity, String columnName, Object columnValue) {
		if (StringUtil.isBlank(columnName)) {
			return Collections.emptyList();
		}
		ICommonDao commonDao = CommonDao.getInstance();
		if (entity.isAnnotationPresent(Entity.class)) {
			Entity entityAnnotation = entity.getAnnotation(Entity.class);
			String tableName = entityAnnotation.table();
			String sql = "select * from " + tableName;
			if (columnValue == null) {
				sql += " where " + columnName + " is ?";
			} else {
				sql += " where " + columnName + " = ?";
			}
			return commonDao.findList(sql, entity, columnValue);
		}
		return Collections.emptyList();
	}

	@Override
	public <T extends BaseEntity> int insert(T record) {
		return CommonDao.getInstance().insert(record);
	}

	@Override
	public <T extends BaseEntity> int update(T record) {
		return CommonDao.getInstance().update(record);
	}

	@Override
	public <T extends BaseEntity> int deleteEntities(Class<T> entity, String columnName, Object columnValue) {
		if (StringUtil.isBlank(columnName)) {
			return 0;
		}
		String tableName = entity.getAnnotation(Entity.class).table();
		String sql = "delete from " + tableName + " where " + columnName + "";
		List<Object> params = new ArrayList<>(1);
		if (columnValue == null) {
			sql+= " is null";
		} else {
			sql+= " = ?";
			params.add(columnValue);
		}
		return CommonDao.getInstance().execute(sql, params.toArray(new Object[params.size()]));
	}

	@Override
	public <T extends BaseEntity> List<T> findList(String sql, Class<T> clazz, Object... args) {
		return CommonDao.getInstance().findList(sql, clazz, args);
	}

	@Override
	public <T extends BaseEntity> List<T> findList(String sql, int startIndex, int pageSize, Class<T> clazz,
			Object... args) {
		return CommonDao.getInstance().findList(sql, startIndex, pageSize, clazz, args);
	}

	@Override
	public int queryInt(String sql, Object... args) {
		return CommonDao.getInstance().queryInt(sql, args);
	}

	@Override
	public String queryString(String sql, Object... args) {
		return CommonDao.getInstance().queryString(sql, args);
	}

	@Override
	public <T extends BaseEntity> int deleteEntities(Class<T> entity, List<String> rowguids) {
		String[] rowguidArray = new String[rowguids.size()];
		rowguidArray = rowguids.toArray(rowguidArray);
		return deleteEntities(entity, rowguidArray);
	}
	
}
