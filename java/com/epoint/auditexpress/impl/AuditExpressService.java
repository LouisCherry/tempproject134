package com.epoint.auditexpress.impl;

import java.util.List;

import com.epoint.auditexpress.api.entity.AuditExpress;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 快递信息表对应的后台service
 * 
 * @author Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
public class AuditExpressService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditExpressService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditExpress record) {
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
        T t = baseDao.find(AuditExpress.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditExpress record) {
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
    public AuditExpress find(Object primaryKey) {
        return baseDao.find(AuditExpress.class, primaryKey);
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
    public AuditExpress find(String sql, Object... args) {
        return baseDao.find(sql, AuditExpress.class, args);
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
    public List<AuditExpress> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditExpress.class, args);
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
    public List<AuditExpress> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditExpress.class, args);
    }

    /**
     * 根据projectguid删除记录信息
     *  @param projectguid
     *  @return    
     */
    public int deleteByProjectguid(String projectguid) {
        String sqlDelete = "DELETE from audit_express WHERE projectguid = ? ";
        return baseDao.execute(sqlDelete, projectguid);
    }

    /**
     *  根据projectguid获取记录
     *  @param projectguid
     *  @return    
     */
    public List<AuditExpress> listByProjectguid(String projectguid) {
        String sql = "select * from audit_express where projectguid = ? ";
        return baseDao.findList(sql, AuditExpress.class, projectguid);
    }

    /**
     * 
     * @Description: 保存省市区地址
     * @author male   
     * @date 2019年2月15日 下午3:14:36
     * @return int    返回类型    
     * @throws
     */
    public int updateExpressAdress(String province, String city, String region, String zwdtExpressStatus,
            String rowguid) {
        String sql = "update audit_express set province=?,city=?,region=?,zwdtExpressStatus = ? where rowguid = ?";
        return baseDao.execute(sql, province, city, region, zwdtExpressStatus, rowguid);
    }
}
