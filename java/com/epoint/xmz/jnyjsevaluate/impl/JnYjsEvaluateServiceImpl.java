package com.epoint.xmz.jnyjsevaluate.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;
/**
 * 一件事评价表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
@Component
@Service
public class JnYjsEvaluateServiceImpl implements IJnYjsEvaluateService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnYjsEvaluate record) {
        return new JnYjsEvaluateService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnYjsEvaluateService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnYjsEvaluate record) {
        return new JnYjsEvaluateService().update(record);
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
    public JnYjsEvaluate find(Object primaryKey) {
       return new JnYjsEvaluateService().find(primaryKey);
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
    public JnYjsEvaluate find(String sql, Object... args) {
        return new JnYjsEvaluateService().find(sql,args);
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
    public List<JnYjsEvaluate> findList(String sql, Object... args) {
       return new JnYjsEvaluateService().findList(sql,args);
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
    public List<JnYjsEvaluate> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnYjsEvaluateService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countJnYjsEvaluate(String sql, Object... args){
        return new JnYjsEvaluateService().countJnYjsEvaluate(sql, args);
    }
     
     public List<Record> findEvaluateList(int pageNumber, int pageSize,String applydateStart,String applydateEnd) {
         return new JnYjsEvaluateService().findEvaluateList(pageNumber,pageSize,applydateStart,applydateEnd);
      }
     
     public Integer countEvaluate(String applydateStart,String applydateEnd){
         return new JnYjsEvaluateService().countEvaluate(applydateStart, applydateEnd);
     }
     
     

}
