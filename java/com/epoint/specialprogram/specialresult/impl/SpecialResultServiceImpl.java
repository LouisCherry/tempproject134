package com.epoint.specialprogram.specialresult.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.specialprogram.specialresult.api.entity.SpecialResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.specialprogram.specialresult.api.ISpecialResultService;
/**
 * 特别程序结果对应的后台service实现类
 * 
 * @author lizhenjie
 * @version [版本号, 2020-12-23 19:34:13]
 */
@Component
@Service
public class SpecialResultServiceImpl implements ISpecialResultService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(SpecialResult record) {
        return new SpecialResultService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new SpecialResultService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(SpecialResult record) {
        return new SpecialResultService().update(record);
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
    public SpecialResult find(Object primaryKey) {
       return new SpecialResultService().find(primaryKey);
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
    public SpecialResult find(String sql, Object... args) {
        return new SpecialResultService().find(sql,args);
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
    public List<SpecialResult> findList(String sql, Object... args) {
       return new SpecialResultService().findList(sql,args);
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
    public List<SpecialResult> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new SpecialResultService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countSpecialResult(String sql, Object... args){
        return new SpecialResultService().countSpecialResult(sql, args);
    }

}
