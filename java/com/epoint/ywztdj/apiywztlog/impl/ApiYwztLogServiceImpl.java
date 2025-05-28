package com.epoint.ywztdj.apiywztlog.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.ywztdj.apiywztlog.api.entity.ApiYwztLog;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.ywztdj.apiywztlog.api.IApiYwztLogService;
/**
 * 业务中台接口日志表对应的后台service实现类
 * 
 * @author 15056
 * @version [版本号, 2024-01-08 14:41:09]
 */
@Component
@Service
public class ApiYwztLogServiceImpl implements IApiYwztLogService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ApiYwztLog record) {
        return new ApiYwztLogService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ApiYwztLogService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ApiYwztLog record) {
        return new ApiYwztLogService().update(record);
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
    public ApiYwztLog find(Object primaryKey) {
       return new ApiYwztLogService().find(primaryKey);
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
    public ApiYwztLog find(String sql, Object... args) {
        return new ApiYwztLogService().find(sql,args);
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
    public List<ApiYwztLog> findList(String sql, Object... args) {
       return new ApiYwztLogService().findList(sql,args);
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
    public List<ApiYwztLog> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new ApiYwztLogService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countApiYwztLog(String sql, Object... args){
        return new ApiYwztLogService().countApiYwztLog(sql, args);
    }

}
