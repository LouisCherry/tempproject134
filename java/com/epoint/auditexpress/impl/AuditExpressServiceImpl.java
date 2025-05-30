package com.epoint.auditexpress.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditexpress.api.IAuditExpressService;
import com.epoint.auditexpress.api.entity.AuditExpress;

/**
 * 快递信息表对应的后台service实现类
 * 
 * @author Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
@Component
@Service
public class AuditExpressServiceImpl implements IAuditExpressService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(AuditExpress record) {
        return new AuditExpressService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new AuditExpressService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(AuditExpress record) {
        return new AuditExpressService().update(record);
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
    @Override
    public AuditExpress find(Object primaryKey) {
        return new AuditExpressService().find(primaryKey);
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
    @Override
    public AuditExpress find(String sql, Object... args) {
        return new AuditExpressService().find(args);
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
    @Override
    public List<AuditExpress> findList(String sql, Object... args) {
        return new AuditExpressService().findList(sql, args);
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
    @Override
    public List<AuditExpress> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditExpressService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     *  根据projectguid删除记录信息
     */
    @Override
    public int deleteByProjectguid(String projectguid) {
        return new AuditExpressService().deleteByProjectguid(projectguid);
    }

    @Override
    public List<AuditExpress> listByProjectguid(String projectguid) {

        return new AuditExpressService().listByProjectguid(projectguid);
    }

    @Override
    public int updateExpressAdress(String province, String city, String region, String zwdtExpressStatus,
            String rowguid) {
        return new AuditExpressService().updateExpressAdress(province, city, region, zwdtExpressStatus, rowguid);
    }

}
