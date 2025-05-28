package com.epoint.jnycsl.dao;

import java.util.List;

import com.epoint.core.BaseEntity;

/**
 * 基础dao
 * @author 刘雨雨
 * @time 2018年9月12日下午3:35:58
 */
public interface TaianBaseDao {

	/**
	 * 根据一个字段查询实体对象
	 * @param entity
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public <T extends BaseEntity> T findEntity(Class<T> entity, String columnName, Object columnValue);

	/**
	 * 根据一个字段查询实体对象
	 * @param entity
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public <T extends BaseEntity> List<T> findEntities(Class<T> entity, String columnName, Object columnValue);

	/**
	 * 根据主键查询实体对象
	 * @param entity
	 * @param id 主键值
	 * @return
	 */
	public <T extends BaseEntity> T findEntity(Class<T> entity, String id);

	/**
	 * 删除一行或者多行记录
	 * @param entity 实体类
	 * @param rowguids 主键数组
	 * @return
	 */
	public <T extends BaseEntity> int deleteEntities(Class<T> entity, String[] rowguids);
	
	/**
	 * 删除一行或者多行记录
	 * @param entity 实体类
	 * @param rowguids 主键集合
	 * @return
	 */
	public <T extends BaseEntity> int deleteEntities(Class<T> entity, List<String> rowguids);


	public <T extends BaseEntity> int insert(T t);

	public <T extends BaseEntity> int update(T t);

	/**
	 * 删除columnName=columnValue的记录
	 * @param entity
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public <T extends BaseEntity> int deleteEntities(Class<T> entity, String columnName, Object columnValue);

	public <T extends BaseEntity> List<T> findList(String sql, Class<T> clazz, Object... args);

	public <T extends BaseEntity> List<T> findList(String sql, int startIndex, int pageSize, Class<T> clazz, Object... args);

	public int queryInt(String sql, Object... args);

	public String queryString(String sql, Object... args);
}
