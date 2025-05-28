package com.epoint.auditspitem.impl;

import com.epoint.auditspitem.api.entity.AuditSpItem;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

import java.util.List;
import java.util.Map;

/**
 * 项目信息表对应的后台service
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:30:48]
 */
public class AuditSpItemService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpItemService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpItem record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditSpItem.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpItem record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditSpItem find(Object primaryKey) {
        return baseDao.find(AuditSpItem.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpItem find(String sql, Object... args) {
        return baseDao.find(sql, AuditSpItem.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpItem> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpItem.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpItem> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpItem.class, args);
    }

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countAuditSpItem(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 
     * 获取项目信息集合
     * 
     * @param conditionMap
     *            查询条件
     * @return list结果
     */
    public List<AuditSpItem> getAuditSpItemListByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String allSql = sqlManageUtil.buildSqlComoplete(AuditSpItem.class, conditionMap);
        return baseDao.findList(allSql, AuditSpItem.class);
    }
    public String getlctguidByitem(String itemcode){
        String sql="select rowguid from audit_sp_instance where YEWUGUID=(select rowguid from audit_rs_item_baseinfo where itemcode='"+itemcode+"')";
        return baseDao.queryString(sql);
    };


    public List<AuditSpItem> findsyncist() {
        String sql = " SELECT * from AUDIT_SP_ITEM WHERE  sfzditem='1' and  ( issync ='0' or issync is null)  ";
        return baseDao.findList(sql, AuditSpItem.class);
    }

}
