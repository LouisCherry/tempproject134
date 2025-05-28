package com.epoint.auditproject.auditrongquematerial.impl;

import java.util.List;

import com.epoint.auditproject.auditrongquematerial.api.entity.AuditProjectMaterialRongque;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 好差评信息表对应的后台service
 * 
 * @author shibin
 * @version [版本号, 2019-10-15 16:28:30]
 */
public class AuditProjectMaterialRongqueService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditProjectMaterialRongqueService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectMaterialRongque record) {
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
        T t = baseDao.find(AuditProjectMaterialRongque.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectMaterialRongque record) {
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
    public AuditProjectMaterialRongque find(Object primaryKey) {
        return baseDao.find(AuditProjectMaterialRongque.class, primaryKey);
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
    public AuditProjectMaterialRongque find(String sql, Object... args) {
        return baseDao.find(sql, AuditProjectMaterialRongque.class, args);
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
    public List<AuditProjectMaterialRongque> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditProjectMaterialRongque.class, args);
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
    public List<AuditProjectMaterialRongque> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditProjectMaterialRongque.class, args);
    }

    /**
     * 根据projectguid获取容缺材料
     * @authory shibin
     * @version 2019年10月24日 下午5:10:29
     * @param projectGuid
     * @return
     */
    public List<AuditProjectMaterialRongque> findListByProject(String projectGuid) {
        String sql = "select * from audit_project_material_rongque WHERE Projectguid= ? ";
        return baseDao.findList(sql, AuditProjectMaterialRongque.class, projectGuid);
    }

    /**
     * 获取附件标识
     * @authory shibin
     * @version 2019年10月25日 上午9:16:11
     * @param projectGuid
     * @return
     */
    public String getPromiseFile(String projectGuid) {
        String sql = "select  ATTACHGUID from audit_project p INNER JOIN frame_attachinfo f ON p.promisefileguid = f.CLIENGGUID WHERE p.RowGuid = ? ";
        return baseDao.queryString(sql, projectGuid);
    }

}
