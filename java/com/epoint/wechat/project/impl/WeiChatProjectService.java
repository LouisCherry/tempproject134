package com.epoint.wechat.project.impl;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 快递信息表对应的后台service
 * 
 * @author Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
public class WeiChatProjectService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public WeiChatProjectService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProject record) {
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
        T t = baseDao.find(AuditProject.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProject record) {
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
    public AuditProject find(Object primaryKey) {
        return baseDao.find(AuditProject.class, primaryKey);
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
    public AuditProject find(String sql, Object... args) {
        return baseDao.find(sql, AuditProject.class, args);
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
    public List<AuditProject> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditProject.class, args);
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
    public List<AuditProject> findListByPage(SqlConditionUtil sqlConditionUtil, int pageNumber, int pageSize,
            Object... args) {

        String fields = " select rowguid,projectname,applyername,taskguid,centerguid,flowsn,status,OperateDate,APPLYDATE,APPLYERTYPE,BANJIEDATE,areacode,pviguid,banjieresult,ACCEPTUSERDATE,ACCEPTUSERNAME,ouname,ouguid from audit_project ";

        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        StringBuffer sb = new StringBuffer();

        sb.append(sqlManageUtil.buildSql(sqlConditionUtil.getMap()));
        fields = fields + sb.toString() + " ORDER BY APPLYDATE DESC ";

        String sql = "select * from (" + fields + ") a LIMIT " + pageNumber + "," + pageSize + "";
        return baseDao.findList(sql, AuditProject.class);
    }

    public int getTotalNum(SqlConditionUtil sqlConditionUtil) {
        String fields = " select count(1) from audit_project ";

        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        StringBuffer sb = new StringBuffer();

        sb.append(sqlManageUtil.buildSql(sqlConditionUtil.getMap()));
        fields = fields + sb.toString();

        return baseDao.queryInt(fields, AuditProject.class);
    }

    public AuditProject getAuditProjectByRowGuid(String projectGuid) {
        String sql = "select * from audit_project WHERE RowGuid = ? ";
        return baseDao.find(sql, AuditProject.class, projectGuid);
    }
}
