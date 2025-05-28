package com.epoint.xmz.audittaskpopinfo.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.grammar.Record;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;
import com.epoint.xmz.audittaskpopinfo.api.entity.AuditTaskPopInfo;

/**
 * 弹窗信息维护对应的后台service
 * 
 * @author dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
public class AuditTaskPopInfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditTaskPopInfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskPopInfo record) {
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
        T t = baseDao.find(AuditTaskPopInfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskPopInfo record) {
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
    public AuditTaskPopInfo find(Object primaryKey) {
        return baseDao.find(AuditTaskPopInfo.class, primaryKey);
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
    public AuditTaskPopInfo find(String sql,  Object... args) {
        return baseDao.find(sql, AuditTaskPopInfo.class, args);
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
    public List<AuditTaskPopInfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTaskPopInfo.class, args);
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
    public List<AuditTaskPopInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditTaskPopInfo.class, args);
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
    public Integer countAuditTaskPopInfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public AuditTaskPopInfo find(Map<String, String> conditionMap) {
        String sqlCondition = new SQLManageUtil().buildPatchSql(conditionMap);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("select * from ").append("audit_task_pop_info").append(" where 1=1 ").append(sqlCondition);
        return baseDao.find(sqlBuilder.toString(),AuditTaskPopInfo.class);
    }
}
