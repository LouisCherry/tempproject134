package com.epoint.auditproject.auditrongquematerial.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditproject.auditrongquematerial.api.IAuditProjectMaterialRongqueService;
import com.epoint.auditproject.auditrongquematerial.api.entity.AuditProjectMaterialRongque;

/**
 * 材料容缺信息表对应的后台service实现类
 * @authory shibin
 * @version 2019年10月23日 下午7:22:32
 */

@Component
@Service
public class AuditProjectMaterialRongqueServiceImpl implements IAuditProjectMaterialRongqueService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectMaterialRongque record) {
        return new AuditProjectMaterialRongqueService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditProjectMaterialRongqueService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectMaterialRongque record) {
        return new AuditProjectMaterialRongqueService().update(record);
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
        return new AuditProjectMaterialRongqueService().find(primaryKey);
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
        return new AuditProjectMaterialRongqueService().find(args);
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
        return new AuditProjectMaterialRongqueService().findList(sql, args);
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
        return new AuditProjectMaterialRongqueService().findList(sql, pageNumber, pageSize, args);
    }

    @Override
    public List<AuditProjectMaterialRongque> findListByProject(String projectGuid) {
        return new AuditProjectMaterialRongqueService().findListByProject(projectGuid);
    }

    @Override
    public String getPromiseFile(String projectGuid) {
        return new AuditProjectMaterialRongqueService().getPromiseFile(projectGuid);
    }

}
