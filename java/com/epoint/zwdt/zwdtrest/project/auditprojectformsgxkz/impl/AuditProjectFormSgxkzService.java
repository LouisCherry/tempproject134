package com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.impl;

import java.util.List;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.zwdt.zwdtrest.project.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

/**
 * 施工许可证表单对应的后台service
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
public class AuditProjectFormSgxkzService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditProjectFormSgxkzService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectFormSgxkz record) {
        return baseDao.insert(record);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectFormSgxkzDanti record) {
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
        T t = baseDao.find(AuditProjectFormSgxkz.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectFormSgxkz record) {
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
    public AuditProjectFormSgxkz find(String projectguid) {
        String sql = "select * from audit_project_form_sgxkz where projectguid = ?";
        return baseDao.find(sql,AuditProjectFormSgxkz.class, projectguid);
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
    public AuditProjectFormSgxkz find(String sql, Object... args) {
        return baseDao.find(sql, AuditProjectFormSgxkz.class, args);
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
    public List<AuditProjectFormSgxkz> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditProjectFormSgxkz.class, args);
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
    public List<AuditProjectFormSgxkz> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditProjectFormSgxkz.class, args);
    }
    
    
    public AuditProjectFormSgxkz getRecordBy(String projectguid){
        String sql = "select * from audit_project_form_sgxkz where projectguid = ?";
        return baseDao.find(sql, AuditProjectFormSgxkz.class, projectguid);
    }
    
    public AuditProjectFormSgxkzDanti getDantiByRowguid(String rowguid){
        String sql = "select * from audit_project_form_sgxkz_danti where rowguid = ?";
        return baseDao.find(sql, AuditProjectFormSgxkzDanti.class, rowguid);
    }

    public List<AuditProjectFormSgxkzDanti> findDantiList(String projectguid) {
        String sql = "select * from audit_project_form_sgxkz_danti where projectguid = ?";
        return baseDao.findList(sql,AuditProjectFormSgxkzDanti.class,projectguid);
    }
    
    public Integer queryInt(String projectguid) {
        String sql = "select count(1) from audit_project_form_sgxkz_danti where projectguid = ?";
        return baseDao.queryInt(sql,projectguid);
    }
}
