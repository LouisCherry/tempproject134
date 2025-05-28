package com.epoint.cert.auditcertrelation.impl;
import java.util.List;

import com.epoint.cert.auditcertrelation.api.IAuditCertRelationService;
import com.epoint.cert.auditcertrelation.api.entity.AuditCertRelation;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * 证照字段关系表对应的后台service实现类
 * 
 * @author miemieyang12128
 * @version [版本号, 2024-10-16 09:08:40]
 */
@Component
@Service
public class AuditCertRelationServiceImpl implements IAuditCertRelationService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditCertRelation record) {
        return new AuditCertRelationService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditCertRelationService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditCertRelation record) {
        return new AuditCertRelationService().update(record);
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
    public AuditCertRelation find(Object primaryKey) {
       return new AuditCertRelationService().find(primaryKey);
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
    public AuditCertRelation find(String sql, Object... args) {
        return new AuditCertRelationService().find(sql,args);
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
    public List<AuditCertRelation> findList(String sql, Object... args) {
       return new AuditCertRelationService().findList(sql,args);
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
    public List<AuditCertRelation> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditCertRelationService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditCertRelation(String sql, Object... args){
        return new AuditCertRelationService().countAuditCertRelation(sql, args);
    }

}
