package com.epoint.ces.auditnotifydocattachinfo.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.ces.auditnotifydocattachinfo.api.entity.AuditNotifydocAttachinfo;

/**
 * 办件文书信息表对应的后台service
 * 
 * @author jiem
 * @version [版本号, 2022-03-15 14:02:49]
 */
public class AuditNotifydocAttachinfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditNotifydocAttachinfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditNotifydocAttachinfo record) {
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
        T t = baseDao.find(AuditNotifydocAttachinfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditNotifydocAttachinfo record) {
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
    public AuditNotifydocAttachinfo find(Object primaryKey) {
        return baseDao.find(AuditNotifydocAttachinfo.class, primaryKey);
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
    public AuditNotifydocAttachinfo find(String sql,  Object... args) {
        return baseDao.find(sql, AuditNotifydocAttachinfo.class, args);
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
    public List<AuditNotifydocAttachinfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditNotifydocAttachinfo.class, args);
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
    public List<AuditNotifydocAttachinfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditNotifydocAttachinfo.class, args);
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
    public Integer countAuditNotifydocAttachinfo(String sql, Object... args){
        sql = "select count(1) from AUDIT_NOTIFYDOC_ATTACHINFO " + sql;
        return baseDao.queryInt(sql, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql
     *            查询语句
     * @param first
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @return T extends BaseEntity
     */
    public List<AuditNotifydocAttachinfo> findPage(String sql, int first, int pageSize) {
        return baseDao.findList(sql, first, pageSize, AuditNotifydocAttachinfo.class);
    }
}
