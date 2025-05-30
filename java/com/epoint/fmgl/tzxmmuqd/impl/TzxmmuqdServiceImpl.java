package com.epoint.fmgl.tzxmmuqd.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.fmgl.tzxmmuqd.api.entity.Tzxmmuqd;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.fmgl.tzxmmuqd.api.ITzxmmuqdService;
/**
 * 投资项目目录清单对应的后台service实现类
 * 
 * @author Administrator
 * @version [版本号, 2020-09-26 12:07:55]
 */
@Component
@Service
public class TzxmmuqdServiceImpl implements ITzxmmuqdService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Tzxmmuqd record) {
        return new TzxmmuqdService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new TzxmmuqdService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Tzxmmuqd record) {
        return new TzxmmuqdService().update(record);
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
    public Tzxmmuqd find(Object primaryKey) {
       return new TzxmmuqdService().find(primaryKey);
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
    public Tzxmmuqd find(String sql, Object... args) {
        return new TzxmmuqdService().find(sql,args);
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
    public List<Tzxmmuqd> findList(String sql, Object... args) {
       return new TzxmmuqdService().findList(sql,args);
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
    public List<Tzxmmuqd> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new TzxmmuqdService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countTzxmmuqd(String sql, Object... args){
        return new TzxmmuqdService().countTzxmmuqd(sql, args);
    }

    @Override
    public Tzxmmuqd selectTzxmmuqd(String xzqhdm, String permitindustry, String projecttype) {
        return new TzxmmuqdService().selectTzxmmuqd(xzqhdm, permitindustry,projecttype);
    }

}
