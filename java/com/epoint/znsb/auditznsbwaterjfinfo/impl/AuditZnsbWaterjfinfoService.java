package com.epoint.znsb.auditznsbwaterjfinfo.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.znsb.auditznsbwaterjfinfo.api.entity.AuditZnsbWaterjfinfo;

import java.util.List;

/**
 * 水务缴费信息对应的后台service
 * 
 * @author HYF
 * @version [版本号, 2021-11-11 14:49:42]
 */
public class AuditZnsbWaterjfinfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditZnsbWaterjfinfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbWaterjfinfo record) {
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
        T t = baseDao.find(AuditZnsbWaterjfinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbWaterjfinfo record) {
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
    public AuditZnsbWaterjfinfo find(Object primaryKey) {
        return baseDao.find(AuditZnsbWaterjfinfo.class, primaryKey);
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
    public AuditZnsbWaterjfinfo find(String sql,  Object... args) {
        return baseDao.find(sql, AuditZnsbWaterjfinfo.class, args);
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
    public List<AuditZnsbWaterjfinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditZnsbWaterjfinfo.class, args);
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
    public List<AuditZnsbWaterjfinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditZnsbWaterjfinfo.class, args);
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
    public Integer countAuditZnsbWaterjfinfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public List<AuditZnsbWaterjfinfo> findListByTime(String time) {
        String sql = "select * from audit_znsb_waterjfinfo where watertime like ?";
        return baseDao.findList(sql,AuditZnsbWaterjfinfo.class,time + "%");
    }
}
