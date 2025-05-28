package com.epoint.xmz.audittaskjn.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJn;

import java.util.List;

/**
 * 事项个性化表对应的后台service
 *
 * @author Administrator
 * @version [版本号, 2023-11-01 14:01:22]
 */
public class AuditTaskJnService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditTaskJnService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskJn record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditTaskJn.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskJn record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskJn find(Object primaryKey) {
        return baseDao.find(AuditTaskJn.class, primaryKey);
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
    public AuditTaskJn find(String sql, Object... args) {
        return baseDao.find(sql, AuditTaskJn.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskJn> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTaskJn.class, args);
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
    public List<AuditTaskJn> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditTaskJn.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditTaskJn(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<FrameOu> getAllOu() {
        String sql = "select ouname,ouguid from frame_ou where parentouguid='' or PARENTOUGUID is null";
        return baseDao.findList(sql,FrameOu.class);
    }

    public void deleteAllTaskJnByTaskid(String task_id) {
        String sql = "delete from audit_task_jn where task_id = ? ";
        baseDao.execute(sql,task_id);
    }

}

