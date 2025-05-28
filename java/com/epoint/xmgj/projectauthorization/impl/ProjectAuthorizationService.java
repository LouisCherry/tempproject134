package com.epoint.xmgj.projectauthorization.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmgj.projectauthorization.api.entity.ProjectAuthorization;

/**
 * 项目授权表对应的后台service
 * 
 * @author pansh
 * @version [版本号, 2025-02-13 14:58:47]
 */
public class ProjectAuthorizationService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ProjectAuthorizationService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectAuthorization record) {
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
        T t = baseDao.find(ProjectAuthorization.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ProjectAuthorization record) {
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
    public ProjectAuthorization find(Object primaryKey) {
        return baseDao.find(ProjectAuthorization.class, primaryKey);
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
    public ProjectAuthorization find(String sql,  Object... args) {
        return baseDao.find(sql, ProjectAuthorization.class, args);
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
    public List<ProjectAuthorization> findList(String sql, Object... args) {
        return baseDao.findList(sql, ProjectAuthorization.class, args);
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
    public List<ProjectAuthorization> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ProjectAuthorization.class, args);
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
    public Integer countProjectAuthorization(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public int deleteByProjectId(String projectId) {
        String sql= "delete from project_authorization where ProjectID = ?";
        return baseDao.execute(sql,projectId);
    }

    public List<String> getUsersByProjectId(String projectId) {
        String sql = "select userid from project_authorization where ProjectID = ?";
        return baseDao.findList(sql,String.class, projectId);
    }

    public List<String> getUserNamesByProjectId(String projectId) {
        String sql = "select b.DISPLAYNAME from project_authorization a,frame_user b where a.ProjectID = ? and a.UserID=b.USERGUID";
        return baseDao.findList(sql, String.class, projectId);
    }
}
