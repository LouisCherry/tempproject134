package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api;

import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjrInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 残疾人一件事表单对应的后台service接口
 * 
 * @author ez
 * @version [版本号, 2021-04-09 13:50:05]
 */
public interface IAuditYjsCjrService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Record record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Record record);

    /**
     * 根据ID查找单个实体
     *
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditYjsCjr find(Object primaryKey);

    /**
     * 查找单个实体
     *
     * @param baseClass baseClass
     * @param primary   primary
     * @param <T>       类型
     * @return 查找单个实体
     */
    <T extends BaseEntity> T find(Class<T> baseClass, String primary);

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
    public AuditYjsCjr find(String sql,Object... args);

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
    public List<AuditYjsCjr> findList(String areacode);

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
    public List<AuditYjsCjr> findList(String sql, int pageNumber, int pageSize, Object... args);

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
    public <T extends BaseEntity> List<T> findList(Class<T> baseClass, String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer count(String sql, Object... args);

    /**
     * 查找实体列表
     * @param map 条件
     * @param baseClass baseClass
     * @param <T> 类型
     * @return 实体列表
     */
    <T extends BaseEntity> List<T> findList(Map<String, Object> map, Class<T> baseClass);
    
    public AuditYjsCjrInfo getCjrInfoDetailByRowguid(String rowguid);
    
    public Record getMzAreacodeByCjrAreacode(String CjrAreacode);
    
}
