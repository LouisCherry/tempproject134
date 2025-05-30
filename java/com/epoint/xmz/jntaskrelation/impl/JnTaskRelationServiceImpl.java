package com.epoint.xmz.jntaskrelation.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.jntaskrelation.api.entity.JnTaskRelation;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.jntaskrelation.api.IJnTaskRelationService;
/**
 * 事项关联乡镇表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-10-09 16:26:57]
 */
@Component
@Service
public class JnTaskRelationServiceImpl implements IJnTaskRelationService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnTaskRelation record) {
        return new JnTaskRelationService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnTaskRelationService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnTaskRelation record) {
        return new JnTaskRelationService().update(record);
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
    public JnTaskRelation find(Object primaryKey) {
       return new JnTaskRelationService().find(primaryKey);
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
    public JnTaskRelation find(String sql, Object... args) {
        return new JnTaskRelationService().find(sql,args);
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
    public List<JnTaskRelation> findList(String sql, Object... args) {
       return new JnTaskRelationService().findList(sql,args);
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
    public List<JnTaskRelation> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnTaskRelationService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countJnTaskRelation(String sql, Object... args){
        return new JnTaskRelationService().countJnTaskRelation(sql, args);
    }

}
