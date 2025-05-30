package com.epoint.xmz.jncertrecord.impl;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.jncertrecord.api.entity.JnCertRecord;

import java.util.List;

/**
 * 证照调用次数统计表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2022-08-22 16:53:37]
 */
public class JnCertRecordService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnCertRecordService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(JnCertRecord record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(JnCertRecord.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(JnCertRecord record) {
        return baseDao.update(record);
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
        return baseDao.find(JnCertRecord.class, primaryKey);
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
    public JnCertRecord find(String sql,  Object... args) {
        return baseDao.find(sql, JnCertRecord.class, args);
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
        return baseDao.findList(sql, JnCertRecord.class, args);
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
        return baseDao.findList(sql, pageNumber, pageSize, JnCertRecord.class, args);
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
    public Integer countJnCertRecord(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public JnCertRecord getTotalByAreacode(String areacode) {
    	String sql = "select * from jn_cert_record where areacode = ?";
        return baseDao.find(sql, JnCertRecord.class, areacode);
    }

    public int getCountByIdnumber(String idnumber) {
        String sql = "select count(1) from jn_cert_record where idnumber = ? and to_days(operateDate)= to_days(now()) ";
        return baseDao.queryInt(sql,idnumber);
    }

}
