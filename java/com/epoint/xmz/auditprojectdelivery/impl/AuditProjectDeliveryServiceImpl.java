package com.epoint.xmz.auditprojectdelivery.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.auditprojectdelivery.api.IAuditProjectDeliveryService;
import com.epoint.xmz.auditprojectdelivery.api.entity.AuditProjectDelivery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 快递表对应的后台service实现类
 *
 * @author Administrator
 * @version [版本号, 2024-01-15 20:11:12]
 */
@Component
@Service
public class AuditProjectDeliveryServiceImpl implements IAuditProjectDeliveryService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectDelivery record) {
        return new AuditProjectDeliveryService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditProjectDeliveryService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectDelivery record) {
        return new AuditProjectDeliveryService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditProjectDelivery find(Object primaryKey) {
        return new AuditProjectDeliveryService().find(primaryKey);
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
    public AuditProjectDelivery find(String sql, Object... args) {
        return new AuditProjectDeliveryService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditProjectDelivery> findList(String sql, Object... args) {
        return new AuditProjectDeliveryService().findList(sql, args);
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
    public List<AuditProjectDelivery> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditProjectDeliveryService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditProjectDelivery(String sql, Object... args) {
        return new AuditProjectDeliveryService().countAuditProjectDelivery(sql, args);
    }

    @Override
    public AuditProjectDelivery getDeliveryByprojectguid(String projectguid) {
        return new AuditProjectDeliveryService().getDeliveryByprojectguid(projectguid);
    }

}
