package com.epoint.zwdt.zwdtrest.task.audittaskpopinfo.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.epoint.zwdt.zwdtrest.task.audittaskpopinfo.api.IAuditTaskPopInfoService;
import com.epoint.zwdt.zwdtrest.task.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
/**
 * 弹窗信息维护对应的后台service实现类
 * 
 * @author dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
@Component
@Service
public class AuditTaskPopInfoServiceImpl implements IAuditTaskPopInfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskPopInfo record) {
        return new AuditTaskPopInfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditTaskPopInfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskPopInfo record) {
        return new AuditTaskPopInfoService().update(record);
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
    public AuditTaskPopInfo find(Object primaryKey) {
       return new AuditTaskPopInfoService().find(primaryKey);
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
    public AuditTaskPopInfo find(String sql, Object... args) {
        return new AuditTaskPopInfoService().find(sql,args);
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
    public List<AuditTaskPopInfo> findList(String sql, Object... args) {
       return new AuditTaskPopInfoService().findList(sql,args);
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
    public List<AuditTaskPopInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditTaskPopInfoService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditTaskPopInfo(String sql, Object... args){
        return new AuditTaskPopInfoService().countAuditTaskPopInfo(sql, args);
    }
    @Override
    public AuditTaskPopInfo find(Map<String, String> map) {
        return new AuditTaskPopInfoService().find(map);
    }
}
