package com.epoint.xmz.xmzfdckfzzzs.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.xmzfdckfzzzs.api.entity.XmzFdckfzzzs;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.xmzfdckfzzzs.api.IXmzFdckfzzzsService;
/**
 * 房地产开发资质证书对应的后台service实现类
 * 
 * @author 86177
 * @version [版本号, 2021-05-12 09:40:37]
 */
@Component
@Service
public class XmzFdckfzzzsServiceImpl implements IXmzFdckfzzzsService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(XmzFdckfzzzs record) {
        return new XmzFdckfzzzsService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new XmzFdckfzzzsService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(XmzFdckfzzzs record) {
        return new XmzFdckfzzzsService().update(record);
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
    public XmzFdckfzzzs find(Object primaryKey) {
       return new XmzFdckfzzzsService().find(primaryKey);
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
    public XmzFdckfzzzs find(String sql, Object... args) {
        return new XmzFdckfzzzsService().find(sql,args);
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
    public List<XmzFdckfzzzs> findList(String sql, Object... args) {
       return new XmzFdckfzzzsService().findList(sql,args);
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
    public List<XmzFdckfzzzs> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new XmzFdckfzzzsService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countXmzFdckfzzzs(String sql, Object... args){
        return new XmzFdckfzzzsService().countXmzFdckfzzzs(sql, args);
    }

}
