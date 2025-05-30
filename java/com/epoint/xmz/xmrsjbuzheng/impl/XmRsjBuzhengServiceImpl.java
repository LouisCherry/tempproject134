package com.epoint.xmz.xmrsjbuzheng.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.xmrsjbuzheng.api.IXmRsjBuzhengService;
import com.epoint.xmz.xmrsjbuzheng.api.entity.XmRsjBuzheng;
/**
 * 人社局补正信息表对应的后台service实现类
 * 
 * @author LYA
 * @version [版本号, 2020-12-23 13:15:19]
 */
@Component
@Service
public class XmRsjBuzhengServiceImpl implements IXmRsjBuzhengService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmRsjBuzheng record) {
        return new XmRsjBuzhengService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new XmRsjBuzhengService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmRsjBuzheng record) {
        return new XmRsjBuzhengService().update(record);
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
    public XmRsjBuzheng find(Object primaryKey) {
       return new XmRsjBuzhengService().find(primaryKey);
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
    public XmRsjBuzheng find(String sql, Object... args) {
        return new XmRsjBuzhengService().find(sql,args);
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
    public List<XmRsjBuzheng> findList(String sql, Object... args) {
       return new XmRsjBuzhengService().findList(sql,args);
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
    public List<XmRsjBuzheng> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new XmRsjBuzhengService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countXmRsjBuzheng(String sql, Object... args){
        return new XmRsjBuzhengService().countXmRsjBuzheng(sql, args);
    }

    @Override
    public XmRsjBuzheng findXmRsjBuzhengByflowsn(String flowsn) {
        return  new XmRsjBuzhengService().findXmRsjBuzhengByflowsn(flowsn);
    }

}
