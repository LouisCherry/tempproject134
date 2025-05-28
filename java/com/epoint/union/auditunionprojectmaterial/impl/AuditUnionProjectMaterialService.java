package com.epoint.union.auditunionprojectmaterial.impl;
import java.util.List;
import com.epoint.core.grammar.Record;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;

/**
 * 异地通办材料信息对应的后台service
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:43]
 */
public class AuditUnionProjectMaterialService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditUnionProjectMaterialService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditUnionProjectMaterial record) {
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
        T t = baseDao.find(AuditUnionProjectMaterial.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditUnionProjectMaterial record) {
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
    public AuditUnionProjectMaterial find(Object primaryKey) {
        return baseDao.find(AuditUnionProjectMaterial.class, primaryKey);
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
    public AuditUnionProjectMaterial find(String sql,  Object... args) {
        return baseDao.find(sql, AuditUnionProjectMaterial.class, args);
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
    public List<AuditUnionProjectMaterial> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditUnionProjectMaterial.class, args);
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
    public List<AuditUnionProjectMaterial> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditUnionProjectMaterial.class, args);
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
    public Integer countAuditUnionProjectMaterial(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    public Integer updateProjectMaterialStatus(String rowguid,String projectguid,String type) {
    	String sql = "update audit_union_project_material set STATUS = ?1 , OperateDate=now() where rowguid=?2 and unionprojectguid=?3";
    	return baseDao.execute(sql, new Object[]{type,rowguid,projectguid});
    }
    
    public AuditUnionProjectMaterial getProjectMaterialDetail(String taskmaterialguid, String projectguid) {
    	String sql = "select * from audit_union_project_material where taskmaterialguid=?1 and unionprojectguid =?2 ";
    	return baseDao.find(sql, AuditUnionProjectMaterial.class, new Object[]{taskmaterialguid,projectguid});
    }
}
