package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfo;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
/**
 * 残疾人一件事表单对应的后台service实现类
 * 
 * @author ez
 * @version [版本号, 2021-04-09 13:50:05]
 */
@Component
@Service
public class AuditYjsCjrServiceImpl implements IAuditYjsCjrService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(Record record) {
        return new AuditYjsCjrService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param guid BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new AuditYjsCjrService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(Record record) {
        return new AuditYjsCjrService().update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    @Override
    public AuditYjsCjr find(Object primaryKey) {
       return new AuditYjsCjrService().find(primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public AuditYjsCjr find(String sql, Object... args) {
        return new AuditYjsCjrService().find(sql, args);
    }

    /**
     * 查找单个实体
     *
     * @param baseClass baseClass
     * @param primary   primary
     * @param <T>       类型
     * @return 查找单个实体
     */
    @Override
    public <T extends BaseEntity> T find(Class<T> baseClass, String primary) {
        return new AuditYjsCjrService().find(baseClass, primary);
    }

    /**
     * 查找一个list
     *
     * @param sql  查询语句
     * @param args 参数值数组
     * @return T extends BaseEntity
     */
    @Override
    public List<AuditYjsCjr> findList(String areacode) {
        return new AuditYjsCjrService().findList(areacode);
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
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    @Override
    public List<AuditYjsCjr> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditYjsCjrService().findList(sql,pageNumber,pageSize,args);
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
     public Integer count(String sql, Object... args) {
         return new AuditYjsCjrService().countAuditYjsCjr(sql, args);
     }


    /**
     * 查找实体列表
     *
     * @param map       条件
     * @param baseClass baseClass
     * @param <T>       类型
     * @return 实体列表
     */
    @Override
    public <T extends BaseEntity> List<T> findList(Map<String, Object> map, Class<T> baseClass) {
        return new AuditYjsCjrService().findlist(map, baseClass);
    }

    /**
     * 分页查找一个list
     *
     * @param baseClass
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    @Override
    public <T extends BaseEntity> List<T> findList(Class<T> baseClass, String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditYjsCjrService().findList(baseClass,sql,pageNumber,pageSize,args);
    }
    
    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public AuditYjsCjrInfo getCjrInfoDetailByRowguid(String rowguid){
        return new AuditYjsCjrService().getCjrInfoDetailByRowguid(rowguid);
    }
    
    @Override
    public Record getMzAreacodeByCjrAreacode(String CjrAreacode){
        return new AuditYjsCjrService().getMzAreacodeByCjrAreacode(CjrAreacode);
    }
    
}
