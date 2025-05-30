package com.epoint.businesslicense.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证扩展信息实现类 
 * @description
 * @author shibin
 * @date  2020年5月19日 下午2:53:29
 */
@Component
@Service
public class BusinessLicenseExtensionImpl implements IBusinessLicenseExtension
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseExtension record) {
        return new BusinessLicenseExtensionService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new BusinessLicenseExtensionService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(BusinessLicenseExtension record) {
        return new BusinessLicenseExtensionService().update(record);
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
    public BusinessLicenseExtension find(Object primaryKey) {
        return new BusinessLicenseExtensionService().find(primaryKey);
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
    public BusinessLicenseExtension find(String sql, Object... args) {
        return new BusinessLicenseExtensionService().find(sql, args);
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
    public List<BusinessLicenseExtension> findList(String sql, Object... args) {
        return new BusinessLicenseExtensionService().findList(sql, args);
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
    public List<BusinessLicenseExtension> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new BusinessLicenseExtensionService().findList(sql, pageNumber, pageSize, args);
    }
}
