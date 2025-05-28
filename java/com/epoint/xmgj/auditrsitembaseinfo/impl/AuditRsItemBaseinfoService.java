package com.epoint.xmgj.auditrsitembaseinfo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.xmgj.auditrsitembaseinfo.api.entity.AuditRsItemBaseinfo;

/**
 * 项目表对应的后台service
 * 
 * @author pansh
 * @version [版本号, 2025-02-12 17:31:53]
 */
public class AuditRsItemBaseinfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditRsItemBaseinfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditRsItemBaseinfo record) {
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
        T t = baseDao.find(AuditRsItemBaseinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditRsItemBaseinfo record) {
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
    public AuditRsItemBaseinfo find(Object primaryKey) {
        return baseDao.find(AuditRsItemBaseinfo.class, primaryKey);
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
    public AuditRsItemBaseinfo find(String sql,  Object... args) {
        return baseDao.find(sql, AuditRsItemBaseinfo.class, args);
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
    public List<AuditRsItemBaseinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditRsItemBaseinfo.class, args);
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
    public List<AuditRsItemBaseinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditRsItemBaseinfo.class, args);
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
    public Integer countAuditRsItemBaseinfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public List<String> getProjectnamesByProjectid(String BIGUID) {
        String sql = "select PROJECTNAME from audit_project where RowGuid in (select PROJECTGUID from audit_sp_i_task where BIGUID=?) and status=90";
        return baseDao.findList(sql, String.class, BIGUID);
    }

    public List<AuditRsItemBaseinfo> findList(Map<String, Object> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        List<Object> params = new ArrayList<>();
        return baseDao.findList(new SqlHelper().getSqlComplete(AuditRsItemBaseinfo.class, conditionMap, params), first,
                pageSize,
                AuditRsItemBaseinfo.class, params.toArray());
    }
}
