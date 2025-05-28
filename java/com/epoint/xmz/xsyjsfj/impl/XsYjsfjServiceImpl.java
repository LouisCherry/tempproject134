package com.epoint.xmz.xsyjsfj.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.xsyjsfj.api.entity.XsYjsfj;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.xsyjsfj.api.IXsYjsfjService;
/**
 * 国土上传预审批复函对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-10-06 17:34:42]
 */
@Component
@Service
public class XsYjsfjServiceImpl implements IXsYjsfjService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XsYjsfj record) {
        return new XsYjsfjService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new XsYjsfjService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XsYjsfj record) {
        return new XsYjsfjService().update(record);
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
    public XsYjsfj find(Object primaryKey) {
       return new XsYjsfjService().find(primaryKey);
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
    public XsYjsfj find(String sql, Object... args) {
        return new XsYjsfjService().find(sql,args);
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
    public List<XsYjsfj> findList(String sql, Object... args) {
       return new XsYjsfjService().findList(sql,args);
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
    public List<XsYjsfj> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new XsYjsfjService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countXsYjsfj(String sql, Object... args){
        return new XsYjsfjService().countXsYjsfj(sql, args);
    }

}
