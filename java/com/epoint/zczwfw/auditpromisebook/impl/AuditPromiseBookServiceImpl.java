package com.epoint.zczwfw.auditpromisebook.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.zczwfw.auditpromisebook.api.entity.AuditPromiseBook;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zczwfw.auditpromisebook.api.IAuditPromiseBookService;
/**
 * 卫生许可告知书附件表对应的后台service实现类
 * 
 * @author Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
@Component
@Service
public class AuditPromiseBookServiceImpl implements IAuditPromiseBookService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditPromiseBook record) {
        return new AuditPromiseBookService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditPromiseBookService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditPromiseBook record) {
        return new AuditPromiseBookService().update(record);
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
    public AuditPromiseBook find(Object primaryKey) {
       return new AuditPromiseBookService().find(primaryKey);
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
    public AuditPromiseBook find(String sql, Object... args) {
        return new AuditPromiseBookService().find(sql,args);
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
    public List<AuditPromiseBook> findList(String sql, Object... args) {
       return new AuditPromiseBookService().findList(sql,args);
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
    public List<AuditPromiseBook> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditPromiseBookService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditPromiseBook(String sql, Object... args){
        return new AuditPromiseBookService().countAuditPromiseBook(sql, args);
    }

}
