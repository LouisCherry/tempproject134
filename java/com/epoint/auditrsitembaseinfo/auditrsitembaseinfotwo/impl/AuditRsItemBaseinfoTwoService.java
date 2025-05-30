package com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.impl;
import java.util.List;

import com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.api.entity.AuditRsItemBaseinfoTwo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 泰安建设项目第二阶段基本信息拓展表对应的后台service
 * 
 * @author wangxiaolong
 * @version [版本号, 2019-08-26 08:59:06]
 */
public class AuditRsItemBaseinfoTwoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditRsItemBaseinfoTwoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditRsItemBaseinfoTwo record) {
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
        T t = baseDao.find(AuditRsItemBaseinfoTwo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditRsItemBaseinfoTwo record) {
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
    public AuditRsItemBaseinfoTwo find(Object primaryKey) {
        return baseDao.find(AuditRsItemBaseinfoTwo.class, primaryKey);
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
    public AuditRsItemBaseinfoTwo find(String sql,  Object... args) {
        return baseDao.find(sql, AuditRsItemBaseinfoTwo.class, args);
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
    public List<AuditRsItemBaseinfoTwo> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditRsItemBaseinfoTwo.class, args);
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
    public List<AuditRsItemBaseinfoTwo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditRsItemBaseinfoTwo.class, args);
    }
    public AuditRsItemBaseinfoTwo getAuditRsItemBaseinfoTwoByparentidandpaseguid(String parentid,String subappguid) {
        String sql = "select * from audit_rs_item_baseinfo_two where parentid =?1 and subappguid = ?2";
        return baseDao.find(sql, AuditRsItemBaseinfoTwo.class, parentid,subappguid);
    }

}
