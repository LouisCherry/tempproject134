package com.epoint.basic.auditonlineuser.service;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.inter.IAuditDaibanUser;
import com.epoint.basic.cache.Cache;
import com.epoint.basic.cache.CacheService9;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;

@Cache
public class AuditDaibanUserService<T> extends CacheService9
{

    /**
     * 
     */
    private static final long serialVersionUID = -694683902782753162L;

    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    private IDataSourceService dataSourceService = ContainerFactory.getContainInfo()
            .getComponent(IDataSourceService.class);

    public AuditDaibanUserService() {
        setCommonDao(CommonDao.getInstance());
    }

    public AuditDaibanUserService(String dataSourceName) {
        setCommonDao(CommonDao.getInstance(dataSourceService.getDataSourceByName(dataSourceName)));
    }

    public AuditDaibanUserService(DataSourceConfig dataSource) {
        setCommonDao(CommonDao.getInstance(dataSource));
    }

    public AuditDaibanUserService(ICommonDao dao) {
        this.setCommonDao(dao);
    }

    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     * @param baseClass
     * @param useCache;
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            boolean useCache) {
        List<T> result = null;
        // 如果有缓存，并且确定需要用缓存的时候，可以去走缓存
        if (getMemService() != null && useCache) {
            result = ((IAuditDaibanUser<T>) getMemService()).getAllRecord(baseClass, conditionMap);
        }
        else {
            result = ((IAuditDaibanUser<T>) getInstanceService()).getAllRecord(baseClass, conditionMap);
        }
        return result;
    }

    /**
     * 获取所有记录，如果没有缓存数据，就从数据库中获取
     * @param baseClass
     * @param useCache;
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllRecordWithSort(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            boolean useCache, String sortField, String sortOrder) {
        List<T> result = null;
        // 如果有缓存，并且确定需要用缓存的时候，可以去走缓存
        if (getMemService() != null && useCache) {
            result = ((IAuditDaibanUser<T>) getMemService()).getAllRecord(baseClass, conditionMap);
        }
        else {
            result = ((IAuditDaibanUser<T>) getInstanceService()).getAllRecord(baseClass, conditionMap);
        }
        return result;
    }

    // 默认值的重载
    public List<T> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        return getAllRecord(baseClass, conditionMap, true);
    }

    @SuppressWarnings("unchecked")
    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            boolean useCache) {
        Integer result = null;
        // 如果有缓存，并且确定需要用缓存的时候，可以去走缓存
        if (getMemService() != null && useCache) {
            result = ((IAuditDaibanUser<T>) getMemService()).getAllRecordCount(baseClass, conditionMap);
        }
        else {
            result = ((IAuditDaibanUser<T>) getInstanceService()).getAllRecordCount(baseClass, conditionMap);
        }
        return result;
    }

    /**
     * 获取分页数据
     * @param baseClass
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    @SuppressWarnings("hiding")
    public <T> PageData<T> getAllRecordByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        PageData<T> pageData = new PageData<T>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        pageData = sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
        return pageData;
    }

    /**
     * 
     *  新增某条记录
     *  @param baseClass
     *  @param record
     *  @param useCache    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"unchecked", "unused" })
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record, boolean useCache) {
        if (getMemService() != null && useCache) {
            ((IAuditDaibanUser<T>) getMemService()).addRecord(baseClass, record);
        }
        if (instanceActive) {
            Entity en = baseClass.getAnnotation(Entity.class);
            if (StringUtil.isNotBlank(en.table())) {
                String constant = en.table().toLowerCase().replace("_", "") + "_CacheFlag";
                // 如果需要执行实例
                ((IAuditDaibanUser<T>) getInstanceService()).addRecord(baseClass, record);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("addRecord", new Object[] {record });
            }
        }
    }

    /**
     * 
     *  更新某条记录
     *  @param baseClass
     *  @param record
     *  @param key
     *  @param useCache    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"unchecked", "unused" })
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record, String key, boolean useCache) {
        if (getMemService() != null && useCache) {
            ((IAuditDaibanUser<T>) getMemService()).updateRecord(baseClass, record, key);
        }
        if (instanceActive) {
            Entity en = baseClass.getAnnotation(Entity.class);
            if (StringUtil.isNotBlank(en.table())) {
                String constant = en.table().toLowerCase().replace("_", "") + "_CacheFlag";
                // 如果需要执行实例
                ((IAuditDaibanUser<T>) getInstanceService()).updateRecord(baseClass, record, key);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("updateRecord", new Object[] {record });
            }
        }
    }

    /**
     * 
     * 更新某条记录
     * 
     * @param baseClass
     * @param record
     * @param key
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.update(record);
        }
    }

    /**
     * 
     *  更新某条记录
     *  @param baseClass
     *  @param rowGuid
     *  @param key
     *  @param value
     */
    @SuppressWarnings("unchecked")
    public void updateRecord(Class<? extends BaseEntity> baseClass, String rowGuid, String key, String value) {
        ((IAuditDaibanUser<T>) getInstanceService()).updateRecord(baseClass, rowGuid, key, value);
    }

    /**
     * 
     *  删除某条记录
     *  @param baseClass
     *  @param record
     *  @param key
     *  @param useCache    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"unchecked", "unused" })
    public void deleteRecod(Class<? extends BaseEntity> baseClass, Record record, String key, boolean useCache) {
        if (getMemService() != null && useCache) {
            ((IAuditDaibanUser<T>) getMemService()).deleteRecod(baseClass, record, key);
        }
        if (instanceActive) {
            Entity en = baseClass.getAnnotation(Entity.class);
            if (StringUtil.isNotBlank(en.table())) {
                String constant = en.table().toLowerCase().replace("_", "") + "_CacheFlag";
                // 如果需要执行实例
                ((IAuditDaibanUser<T>) getInstanceService()).deleteRecod(baseClass, record, key);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("deleteRecod", new Object[] {record });
            }
        }
    }

    @SuppressWarnings({"unchecked", "unused" })
    public void deleteRecods(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        if (getMemService() != null) {
            ((IAuditDaibanUser<T>) getMemService()).deleteRecords(baseClass, keyValue, key);
        }
        if (instanceActive) {
            Entity en = baseClass.getAnnotation(Entity.class);
            if (StringUtil.isNotBlank(en.table())) {
                String constant = en.table().toLowerCase().replace("_", "") + "_CacheFlag";
                // 如果需要执行实例
                ((IAuditDaibanUser<T>) getInstanceService()).deleteRecords(baseClass, keyValue, key);
                // 同步其他服务器,添加一个updateFlag方法调用，将当前类，当前方法名字，参数值数组，当前缓存服务的tag打包进去
                updateFlag("deleteRecod", new Object[] {keyValue });
            }
        }
    }

    /**
     * 
     *  获取某条记录
     *  @param baseClass
     *  @param rowGuid
     *  @param key
     *  @param useCache
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public T getDetail(Class<? extends BaseEntity> baseClass, String rowGuid, String key, boolean useCache) {
        if (getMemService() != null && useCache) {
            return ((IAuditDaibanUser<T>) getMemService()).getDetail(baseClass, rowGuid, key);
        }
        else {
            return ((IAuditDaibanUser<T>) getInstanceService()).getDetail(baseClass, rowGuid, key);
        }
    }

    /**
     * 
     *  获取用户名
     */
    public AuditOnlineRegister getAuditOnlineRegister(String username) {
        String sql = "select * from  audit_online_register where idnumber=?1 UNION all select * from  audit_online_register where mobile=?2 "
                + " UNION all select * from  audit_online_register where loginid=?3";
        if("zenith".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select *, LOCKED as \"LOCK\" from  audit_online_register where idnumber=?1 UNION all select *, LOCKED as \"LOCK\" from  audit_online_register where mobile=?2 "
                    + " UNION all select *, LOCKED as \"LOCK\" from  audit_online_register where loginid=?3";
        }
        AuditOnlineRegister result = commonDao.find(sql, AuditOnlineRegister.class, username, username, username);
        commonDao.close();
        return result;
    }

    /**
     * 
     * 获取已经呼叫快递的外网办件
     */

    @SuppressWarnings("hiding")
    public <T> PageData<T> getProjectIsCallByPage(Class<? extends BaseEntity> baseClass, String sqlRecord,
            String sqlCount, int first, int pageSize, String sortField, String sortOrder) {
        PageData<T> pageData = new PageData<T>();
        @SuppressWarnings("unchecked")
        List<T> dataList = (List<T>) commonDao.findList(sqlRecord, first, pageSize, baseClass);
        int dataCount = commonDao.queryInt(sqlCount);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public ICommonDao getCommonDao() {
        return commonDao;
    }

    public void setCommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @Override
    public void init(String arg0, String arg1, String arg2) {
        // TODO 缓存处理，尚未实现

    }

}
