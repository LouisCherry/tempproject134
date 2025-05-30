package com.epoint.zwdt.zwdtrest.sghd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zwdt.zwdtrest.sghd.api.AuditTaskJnRenling;
import com.epoint.zwdt.zwdtrest.sghd.api.IAuditTaskJnRenlingService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认领记录表对应的后台service实现类
 * 
 * @author zyq
 * @version [版本号, 2024-01-29 13:38:21]
 */
@Component
@Service
public class AuditTaskJnRenlingServiceImpl implements IAuditTaskJnRenlingService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskJnRenling record) {
        return new AuditTaskJnRenlingService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditTaskJnRenlingService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskJnRenling record) {
        return new AuditTaskJnRenlingService().update(record);
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
    public AuditTaskJnRenling find(Object primaryKey) {
        return new AuditTaskJnRenlingService().find(primaryKey);
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
    public AuditTaskJnRenling find(String sql, Object... args) {
        return new AuditTaskJnRenlingService().find(sql, args);
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
    public List<AuditTaskJnRenling> findList(String sql, Object... args) {
        return new AuditTaskJnRenlingService().findList(sql, args);
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
    public List<AuditTaskJnRenling> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditTaskJnRenlingService().findList(sql, pageNumber, pageSize, args);
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
    @Override
    public Integer countAuditTaskJnRenling(String sql, Object... args) {
        return new AuditTaskJnRenlingService().countAuditTaskJnRenling(sql, args);
    }

}
