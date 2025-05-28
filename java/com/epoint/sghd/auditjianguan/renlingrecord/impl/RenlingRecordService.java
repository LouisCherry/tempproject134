package com.epoint.sghd.auditjianguan.renlingrecord.impl;

import com.epoint.basic.authentication.UserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;

import java.util.List;

/**
 * 认领记录表对应的后台service
 *
 * @author lizhenjie
 * @version [版本号, 2022-11-04 19:24:23]
 */
public class RenlingRecordService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public RenlingRecordService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RenlingRecord record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(RenlingRecord.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(RenlingRecord record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public RenlingRecord find(Object primaryKey) {
        return baseDao.find(RenlingRecord.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public RenlingRecord find(String sql, Object... args) {
        return baseDao.find(sql, RenlingRecord.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<RenlingRecord> findList(String sql, Object... args) {
        return baseDao.findList(sql, RenlingRecord.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<RenlingRecord> findList(String sql, int pageNumber, int pageSize, Object... args) {
        sql += " and userguid= '" + UserSession.getInstance().getUserGuid() + "' ";
        return baseDao.findList(sql, pageNumber, pageSize, RenlingRecord.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countRenlingRecord(String sql, Object... args) {
        sql += " and userguid= '" + UserSession.getInstance().getUserGuid() + "' ";
        return baseDao.queryInt(sql, args);
    }
}
