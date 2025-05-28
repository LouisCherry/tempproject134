package com.epoint.common.services;

import java.util.List;
import java.util.Map;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;
import com.epoint.util.CommonUtil;

public class DBServcie {

	/**
	 * 通用dao
	 */
	private ICommonDao commonDao;

	/**
	 * 普通构造
	 */
	private DBServcie() {
	    commonDao = CommonDao.getInstance();
	}
	
	/**
	 * 分表构造
	 */
	private DBServcie(Class<? extends BaseEntity> baseClass) {
	    String tablename = CommonUtil.getTablenameByClass(baseClass);
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(tablename);
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        }
        else {
            commonDao = CommonDao.getInstance();
        }
    }

	/**
	 *  初始化dbservice
	 *  @return    
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static DBServcie getInstance() {
	    return new DBServcie();
	}
	
	/**
	 *  初始化分表dbservice
	 *  @param baseClass
	 *  @return    
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static DBServcie getInstance(Class<? extends BaseEntity> baseClass) {
	    return new DBServcie(baseClass);
	}
	
	/**
     * 新增记录
     *
     * @param t    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public <T extends Record> int add(T t) {
        return commonDao.insert(t);
    }

    /**
     * 更新记录
     *
     * @param t    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public <T extends Record> int update(T t) {
        return commonDao.update(t);
    }

    /**
     * 删除记录
     *
     * @param t    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public <T extends Record>  int delete(T t) {
        return commonDao.delete(t);
    }
    
    /**
     *  根据主键删除记录
     *  @param clazz
     *  @param guid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public <T extends BaseEntity> int deleteByGuid(Class<T> clazz, String guid) {
        T t = commonDao.find(clazz, guid);
        return delete(t);
    }
	
	/**
	 * 根据条件查询list
	 *
	 * @param baseClass
	 * @param conditionMap
	 * @return
	 */
	public <T> List<T> getListByCondition(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
		return new SqlParserService(commonDao).getListByCondition(baseClass, conditionMap);
	}

	/**
	 * 根据条件查询数目
	 *
	 * @param baseClass
	 * @param conditionMap
	 * @return
	 */
	public Integer getListCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
		return new SqlParserService(commonDao).getListCount(baseClass, conditionMap);
	}

	/**
	 * 查询单条数据
	 *
	 * @param baseClass
	 * @param rowGuid
	 * @param key
	 * @return
	 */
    public <T extends BaseEntity, R> R getDetail(Class<T> baseClass, Class<R> returnClass, String keyName,
            String keyValue) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接查询语句进行处理
            sql = "select * from " + en.table() + " where " + keyName + "=?";
        }
        if (StringUtil.isNotBlank(sql)) {
            return commonDao.find(sql, returnClass, keyValue);
        }
        else {
            return null;
        }
    }
    
    /**
     *  查询单条数据
     *  
     *  @param baseClass
     *  @param keyName
     *  @param keyValue
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public <T extends BaseEntity> T getDetail(Class<T> baseClass, String keyName,
            String keyValue) {
        return getDetail(baseClass, baseClass, keyName, keyValue);
    }

	/**
	 * 分页查询
	 *
	 * @param baseClass
	 * @param conditionMap
	 * @param first
	 * @param pageSize
	 * @param sortField
	 * @param sortOrder
	 * @return
	 */
	public <T> PageData<T> getListByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
			Integer first, Integer pageSize, String sortField, String sortOrder) {
		return new SqlParserService(commonDao).getListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
	}
    
    public ICommonDao getDao(){
    	return commonDao;
    }
}
