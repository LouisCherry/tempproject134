package com.epoint.yjs.yjszn.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 一件事指南配置对应的后台service实现类
 * 
 * @author panshunxing
 * @version [版本号, 2024-10-08 15:22:37]
 */
@Component
@Service
public class YjsZnServiceImpl implements IYjsZnService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(YjsZn record) {
        return new YjsZnService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new YjsZnService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(YjsZn record) {
        return new YjsZnService().update(record);
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
    public YjsZn find(Object primaryKey) {
       return new YjsZnService().find(primaryKey);
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
    public YjsZn find(String sql, Object... args) {
        return new YjsZnService().find(sql,args);
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
    public List<YjsZn> findList(String sql, Object... args) {
       return new YjsZnService().findList(sql,args);
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
    public List<YjsZn> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new YjsZnService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countYjsZn(String sql, Object... args){
        return new YjsZnService().countYjsZn(sql, args);
    }

    @Override
    public List<YjsZn> findList(Map<String, Object> conditionMap) {
        return new YjsZnService().findList(conditionMap);
    }

    /**
     * 查找一个list
     *
     */
    @Override
    public List<YjsZn> findList(Map<String, Object> conditionMap,int pageNumber, int pageSize){
        return new YjsZnService().findList(conditionMap,pageNumber,pageSize);
    }

}
