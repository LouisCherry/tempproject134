package com.epoint.jnzwdt.audityjscjr.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.BaseEntity;
import com.epoint.jnzwdt.audityjscjr.api.IAuditYjsCjrService;
import com.epoint.jnzwdt.audityjscjr.api.entity.AuditYjsCjr;
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
    public int insert(AuditYjsCjr record) {
        return new AuditYjsCjrService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
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
    public int update(AuditYjsCjr record) {
        return new AuditYjsCjrService().update(record);
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
    public AuditYjsCjr find(Object primaryKey) {
       return new AuditYjsCjrService().find(primaryKey);
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
    public AuditYjsCjr find(String sql, Object... args) {
        return new AuditYjsCjrService().find(sql,args);
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
    public List<AuditYjsCjr> findList(String sql, Object... args) {
       return new AuditYjsCjrService().findList(sql,args);
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
    public Integer countAuditYjsCjr(String sql, Object... args){
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


}
