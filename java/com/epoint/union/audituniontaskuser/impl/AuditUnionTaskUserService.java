package com.epoint.union.audituniontaskuser.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.union.audituniontaskuser.api.entity.AuditUnionTaskUser;

/**
 * 异地通办事项人员关联表对应的后台service
 * 
 * @author zhaoyan
 * @version [版本号, 2020-03-22 22:39:40]
 */
public class AuditUnionTaskUserService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditUnionTaskUserService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditUnionTaskUser record) {
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
        T t = baseDao.find(AuditUnionTaskUser.class, guid);
        return baseDao.delete(t);
    }
    
    /**
     * 删除事项人员配置
     * @param taskid
     * @return
     */
    public int deleteByTaskid(String taskid) {
    	String sql = "delete from audit_union_task_user where task_id =?";
    	return baseDao.execute(sql, taskid);
    }
    
    /**
     * 获取事项人员配置
     * @param taskid
     * @return
     */
    public List<Record> getUserBytaskid(String taskid) {
    	String sql = "select userguid,username from audit_union_task_user where task_id =?";
    	return baseDao.findList(sql, Record.class, taskid);
    }
    
    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditUnionTaskUser record) {
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
    public AuditUnionTaskUser find(Object primaryKey) {
        return baseDao.find(AuditUnionTaskUser.class, primaryKey);
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
    public AuditUnionTaskUser find(String sql,  Object... args) {
        return baseDao.find(sql, AuditUnionTaskUser.class, args);
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
    public List<AuditUnionTaskUser> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditUnionTaskUser.class, args);
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
    public List<AuditUnionTaskUser> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditUnionTaskUser.class, args);
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
    public Integer countAuditUnionTaskUser(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
}
