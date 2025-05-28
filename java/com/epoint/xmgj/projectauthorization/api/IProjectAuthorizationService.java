package com.epoint.xmgj.projectauthorization.api;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmgj.projectauthorization.api.entity.ProjectAuthorization;

/**
 * 项目授权表对应的后台service接口
 * 
 * @author pansh
 * @version [版本号, 2025-02-13 14:58:47]
 */
public interface IProjectAuthorizationService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectAuthorization record);

    /**
     * 删除数据
     *
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
    public int update(ProjectAuthorization record);

    /**
     * 根据ID查找单个实体
     *
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public ProjectAuthorization find(Object primaryKey);

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public ProjectAuthorization find(String sql,Object... args);

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<ProjectAuthorization> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<ProjectAuthorization> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countProjectAuthorization(String sql, Object... args);

     public int deleteByProjectId(String projectId);

    public List<String> getUsersByProjectId(String projectId);


    public PageData<ProjectAuthorization> getListPage(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder);

    public List<String> getUserNamesByProjectId(String projectId);
}
