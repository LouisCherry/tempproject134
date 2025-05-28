package com.epoint.basic.auditonlineuser.impl;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditonlineuser.inter.IAuditDaibanUser;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;

public class AuditDaibanUserDbImpl<T> implements IAuditDaibanUser<T>
{

    /**
     * 通用dao
     */
    private ICommonDao commonDao;
    private IDataSourceService dataSourceService = ContainerFactory.getContainInfo()
            .getComponent(IDataSourceService.class);

    public AuditDaibanUserDbImpl() {
        commonDao = CommonDao.getInstance();
    }

    public AuditDaibanUserDbImpl(String dataSourceName) {
        commonDao = CommonDao.getInstance(dataSourceService.getDataSourceByName(dataSourceName));
    }

    public AuditDaibanUserDbImpl(DataSourceConfig dataSource) {
        commonDao = CommonDao.getInstance(dataSource);
    }

    public AuditDaibanUserDbImpl(ICommonDao dao) {
        this.commonDao = dao;
    }

    @Override
    public List<T> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        return new SQLManageUtil().getListByCondition(baseClass, conditionMap);
    }

    @Override
    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        return new SQLManageUtil(baseClass).getListCount(baseClass, conditionMap);
    }

    @Override
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.insert(record);
        }
    }

    @Override
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.update(record);
        }
    }

    @Override
    public void updateRecord(Class<? extends BaseEntity> baseClass, String rowGuid, String key, String value) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接查询语句进行处理
            sql = "update " + en.table() + " set " + key + "= '" + value + "' where rowguid= ?1 ";
        }
        if (StringUtil.isNotBlank(sql)) {
            commonDao.execute(sql, rowGuid);
        }
    }

    @Override
    public void deleteRecod(Class<? extends BaseEntity> baseClass, Record record, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.delete(record);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getDetail(Class<? extends BaseEntity> baseClass, String rowGuid, String key) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接查询语句进行处理
            sql = "select * from " + en.table() + " where " + key + "=?1";
        }
        if (StringUtil.isNotBlank(sql)) {
            return (T) commonDao.find(sql, baseClass, rowGuid);
        }
        else {
            return null;
        }
    }

    @Override
    public List<T> getAllRecordByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        return new AuditCommonService().getListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
    }

    @Override
    public void deleteRecords(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接删除语句进行处理
            String sql = "delete from " + en.table() + " where " + key + "=?1";
            commonDao.execute(sql, keyValue);
        }

    }

}
