package com.epoint.union.auditunionprojectmaterial.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.union.auditunionprojectmaterial.api.entity.AuditUnionProjectMaterial;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.union.auditunionprojectmaterial.api.IAuditUnionProjectMaterialService;
/**
 * 异地通办材料信息对应的后台service实现类
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 11:18:43]
 */
@Component
@Service
public class AuditUnionProjectMaterialServiceImpl implements IAuditUnionProjectMaterialService
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditUnionProjectMaterial record) {
        return new AuditUnionProjectMaterialService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditUnionProjectMaterialService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditUnionProjectMaterial record) {
        return new AuditUnionProjectMaterialService().update(record);
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
       return new AuditUnionProjectMaterialService().find(primaryKey);
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
    public AuditUnionProjectMaterial find(String sql, Object... args) {
        return new AuditUnionProjectMaterialService().find(sql,args);
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
       return new AuditUnionProjectMaterialService().findList(sql,args);
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
       return new AuditUnionProjectMaterialService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditUnionProjectMaterial(String sql, Object... args){
        return new AuditUnionProjectMaterialService().countAuditUnionProjectMaterial(sql, args);
    }
    public Integer updateProjectMaterialStatus(String rowguid,String projectguid,String type) {
    	return new AuditUnionProjectMaterialService().updateProjectMaterialStatus(rowguid,projectguid,type);
    }
    
    public AuditUnionProjectMaterial getProjectMaterialDetail(String taskmaterialguid, String projectguid) {
    	return new AuditUnionProjectMaterialService().getProjectMaterialDetail(taskmaterialguid,projectguid);
    }

}
