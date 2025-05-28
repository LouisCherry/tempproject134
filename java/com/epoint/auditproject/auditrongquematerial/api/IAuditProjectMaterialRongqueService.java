package com.epoint.auditproject.auditrongquematerial.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.auditproject.auditrongquematerial.api.entity.AuditProjectMaterialRongque;

/**
 * 材料容缺信息表对应的后台service接口
 * @authory shibin
 * @version 2019年10月23日 下午7:17:17
 */
public interface IAuditProjectMaterialRongqueService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectMaterialRongque record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectMaterialRongque record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditProjectMaterialRongque find(Object primaryKey);

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
    public AuditProjectMaterialRongque find(String sql, Object... args);

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
    public List<AuditProjectMaterialRongque> findList(String sql, Object... args);

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
    public List<AuditProjectMaterialRongque> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 根据projectguid获取容缺材料
     * @authory shibin
     * @version 2019年10月24日 下午5:10:29
     * @param projectGuid
     * @return
     */
    public List<AuditProjectMaterialRongque> findListByProject(String projectGuid);

    /**
     * 获取附件标识
     * @authory shibin
     * @version 2019年10月25日 上午9:15:36
     * @param projectGuid
     * @return
     */
    public String getPromiseFile(String projectGuid);

}
