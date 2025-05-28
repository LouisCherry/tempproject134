package com.epoint.auditspoptiontownship.impl;

import java.util.List;
import java.util.Map;

import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.core.grammar.Record;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

/**
 * 情形引导选项乡镇延伸对应的后台service
 * 
 * @author xzkui
 * @version [版本号, 2020-10-16 15:53:19]
 */
public class AuditSpOptiontownshipService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpOptiontownshipService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpOptiontownship record) {
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
        T t = baseDao.find(AuditSpOptiontownship.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpOptiontownship record) {
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
    public AuditSpOptiontownship find(Object primaryKey) {
        return baseDao.find(AuditSpOptiontownship.class, primaryKey);
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
    public AuditSpOptiontownship find(String sql, Object... args) {
        return baseDao.find(sql, AuditSpOptiontownship.class, args);
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
    public List<AuditSpOptiontownship> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpOptiontownship.class, args);
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
    public List<AuditSpOptiontownship> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpOptiontownship.class, args);
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
    public Integer countAuditSpOptiontownship(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public int deleteByBusinessGuid(String businessguid) {
        String sql = "delete from audit_sp_optiontownship where businessguid = ?";
        return baseDao.execute(sql, businessguid);
    }

    public List<AuditSpOptiontownship> findListByCondition(Map<String, String> condition) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        return sqlManageUtil.getListByCondition(AuditSpOptiontownship.class, condition);
    }

    public Integer deleteByTaskid(String taskid) {
        String sql = "delete from audit_sp_optiontownship where taskid = ?";
        return baseDao.execute(sql, taskid);
    }
}
