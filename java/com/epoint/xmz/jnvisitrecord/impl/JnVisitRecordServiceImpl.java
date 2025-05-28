package com.epoint.xmz.jnvisitrecord.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.xmz.jnvisitrecord.api.entity.JnVisitRecord;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.jnvisitrecord.api.IJnVisitRecordService;
/**
 * 网厅访问量统计表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-09-14 14:54:42]
 */
@Component
@Service
public class JnVisitRecordServiceImpl implements IJnVisitRecordService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnVisitRecord record) {
        return new JnVisitRecordService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnVisitRecordService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnVisitRecord record) {
        return new JnVisitRecordService().update(record);
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
    public JnVisitRecord find(Object primaryKey) {
       return new JnVisitRecordService().find(primaryKey);
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
    public JnVisitRecord find(String sql, Object... args) {
        return new JnVisitRecordService().find(sql,args);
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
    public List<JnVisitRecord> findList(String sql, Object... args) {
       return new JnVisitRecordService().findList(sql,args);
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
    public List<JnVisitRecord> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnVisitRecordService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countJnVisitRecord(String sql, Object... args){
        return new JnVisitRecordService().countJnVisitRecord(sql, args);
    }

}
