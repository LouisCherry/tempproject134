package com.epoint.xmz.jncertrecord.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.jncertrecord.api.IJnCertRecordService;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 证照调用次数统计表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2022-08-22 16:53:37]
 */
@Component
@Service
public class JnCertRecordServiceImpl implements IJnCertRecordService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnCertRecord record) {
        return new JnCertRecordService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new JnCertRecordService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnCertRecord record) {
        return new JnCertRecordService().update(record);
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
    public JnCertRecord find(Object primaryKey) {
       return new JnCertRecordService().find(primaryKey);
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
    public JnCertRecord find(String sql, Object... args) {
        return new JnCertRecordService().find(sql,args);
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
    public List<JnCertRecord> findList(String sql, Object... args) {
       return new JnCertRecordService().findList(sql,args);
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
    public List<JnCertRecord> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new JnCertRecordService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countJnCertRecord(String sql, Object... args){
        return new JnCertRecordService().countJnCertRecord(sql, args);
    }
     
     public JnCertRecord getTotalByAreacode(String areacode) {
         return new JnCertRecordService().getTotalByAreacode(areacode);
     }

    @Override
    public int getCountByIdnumber(String idnumber) {
        return new JnCertRecordService().getCountByIdnumber(idnumber);
    }


}
