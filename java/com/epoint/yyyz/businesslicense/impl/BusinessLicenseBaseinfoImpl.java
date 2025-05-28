package com.epoint.yyyz.businesslicense.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证基本信息实现类 
 * @description
 * @author shibin
 * @date  2020年5月19日 下午2:52:35
 */
@Component
@Service
public class BusinessLicenseBaseinfoImpl implements IBusinessLicenseBaseinfo
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(BusinessLicenseBaseinfo record) {
        return new BusinessLicenseBaseinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new BusinessLicenseBaseinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(BusinessLicenseBaseinfo record) {
        return new BusinessLicenseBaseinfoService().update(record);
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
    public BusinessLicenseBaseinfo find(Object primaryKey) {
        return new BusinessLicenseBaseinfoService().find(primaryKey);
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
    public BusinessLicenseBaseinfo find(String sql, Object... args) {
        return new BusinessLicenseBaseinfoService().find(sql, args);
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
    public List<BusinessLicenseBaseinfo> findList(String sql, Object... args) {
        return new BusinessLicenseBaseinfoService().findList(sql, args);
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
    public List<BusinessLicenseBaseinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new BusinessLicenseBaseinfoService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public Record getBusinessBaseinfoByBiguid(String biGuid) {
        return new BusinessLicenseBaseinfoService().getBusinessBaseinfoByBiguid(biGuid);
    }

    @Override
    public List<Record> getTaskidsByBusinessguid(String businessguid) {
        return new BusinessLicenseBaseinfoService().getTaskidsByBusinessguid(businessguid);
    }

    public BusinessLicenseExtension getBusinessBaseinfoByBiguidAndBuesiness(String biGuid, String businessguid) {
        return new BusinessLicenseBaseinfoService().getBusinessBaseinfoByBiguidAndBuesiness(biGuid, businessguid);
    }

    @Override
    public BusinessLicenseBaseinfo getBaseinfoByBiguid(String biguid) {
        return new BusinessLicenseBaseinfoService().getBaseinfoByBiguid(biguid);
    }

    @Override
    public BusinessLicenseBaseinfo getBaseinfoByCertguid(String guid) {
        return new BusinessLicenseBaseinfoService().getBaseinfoByCertguid(guid);
    }
    
    @Override
    public FrameAttachInfo getAttachByYyyzCliengguid(String yyyzcliengguid, String yyyztype){
        return new BusinessLicenseBaseinfoService().getAttachByYyyzCliengguid(yyyzcliengguid,yyyztype);
    }

    @Override
    public List<CertCatalog> findCertCatalogByOuguid(String ouguid) {
        return new BusinessLicenseBaseinfoService().findCertCatalogByOuguid(ouguid);
    }
    
    @Override
    public List<CertCatalog> findCertCatalogZzlbByOuguid(String ouguid) {
        return new BusinessLicenseBaseinfoService().findCertCatalogZzlbByOuguid(ouguid);
    }

}
