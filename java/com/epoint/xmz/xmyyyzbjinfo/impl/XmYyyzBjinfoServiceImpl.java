package com.epoint.xmz.xmyyyzbjinfo.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.xmyyyzbjinfo.api.IXmYyyzBjinfoService;
import com.epoint.xmz.xmyyyzbjinfo.api.entity.XmYyyzBjinfo;
/**
 * 泰安一业一证对接办结信息表对应的后台service实现类
 * 
 * @author LYA
 * @version [版本号, 2020-07-16 15:17:24]
 */
@Component
@Service
public class XmYyyzBjinfoServiceImpl implements IXmYyyzBjinfoService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmYyyzBjinfo record) {
        return new XmYyyzBjinfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new XmYyyzBjinfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmYyyzBjinfo record) {
        return new XmYyyzBjinfoService().update(record);
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
    public XmYyyzBjinfo find(Object primaryKey) {
       return new XmYyyzBjinfoService().find(primaryKey);
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
    public XmYyyzBjinfo find(String sql, Object... args) {
        return new XmYyyzBjinfoService().find(sql,args);
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
    public List<XmYyyzBjinfo> findList(String sql, Object... args) {
       return new XmYyyzBjinfoService().findList(sql,args);
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
    public List<XmYyyzBjinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new XmYyyzBjinfoService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countXmYyyzBjinfo(String sql, Object... args){
        return new XmYyyzBjinfoService().countXmYyyzBjinfo(sql, args);
    }

    @Override
    public XmYyyzBjinfo findXmYyyzBjinfoByFlowsn(String flowsn) {
        return new XmYyyzBjinfoService().findXmYyyzBjinfoByFlowsn(flowsn);
    }

}
