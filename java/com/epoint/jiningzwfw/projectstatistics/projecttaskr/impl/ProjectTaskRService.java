package com.epoint.jiningzwfw.projectstatistics.projecttaskr.impl;

import com.epoint.basic.auditresource.service.AuditResourceService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.entity.ProjectTaskR;

import java.util.List;
import java.util.Map;

/**
 * 每日办件事项记录表对应的后台service
 * 
 * @author yangyi
 * @version [版本号, 2021-07-01 09:24:03]
 */
public class ProjectTaskRService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ProjectTaskRService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ProjectTaskR record) {
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
        T t = baseDao.find(ProjectTaskR.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(ProjectTaskR record) {
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
    public ProjectTaskR find(Object primaryKey) {
        return baseDao.find(ProjectTaskR.class, primaryKey);
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
    public ProjectTaskR find(String sql,  Object... args) {
        return baseDao.find(sql, ProjectTaskR.class, args);
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
    public List<ProjectTaskR> findList(String sql, Object... args) {
        return baseDao.findList(sql, ProjectTaskR.class, args);
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
    public List<ProjectTaskR> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, ProjectTaskR.class, args);
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
    public Integer countProjectTaskR(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public AuditCommonResult<List<ProjectTaskR>> findListByCondition(Map<String, String> map) {
        AuditResourceService<ProjectTaskR> auditResourceService = new AuditResourceService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            List<ProjectTaskR> list = auditResourceService.getAllRecord(ProjectTaskR.class, map, false);
            result.setResult(list);
        } catch (Exception var5) {
            result.setSystemFail(var5.getMessage());
        }

        return result;
    }

    public String getMinDateFromTables() {
        String sql = "SELECT MIN(APPLYDATE) AS APPLYDATE FROM (SELECT MIN(APPLYDATE) AS APPLYDATE FROM audit_project UNION all SELECT MIN(APPLYDATE) AS APPLYDATE FROM lc_project) p";
        return CommonDao.getInstance().queryString(sql);
    }

    public List<Record> getTASKInfoByDate(String startDate,String endDate) {
        String sql = "SELECT * FROM (SELECT APPLYDATE, TASKGUID, AREACODE, OUGUID, OUNAME FROM audit_project WHERE APPLYDATE >= '"+startDate+"' AND APPLYDATE <= '"+endDate+"' UNION all SELECT APPLYDATE, TASKGUID, AREACODE, OUGUID, OUNAME FROM lc_project WHERE APPLYDATE >= '"+startDate+"' AND APPLYDATE <= '"+endDate+"') p GROUP BY TASKGUID";
        return CommonDao.getInstance().findList(sql,Record.class);
    }

    public Integer getProjectCountByOU(String ouguid, String startDate, String endDate) {
        String sql = "select count(*) from  (select TASK_ID from project_task_r where OUGUID ='"+ouguid+"' and SDate <='"+endDate+"' and SDate >='"+startDate+"'  group by  TASK_ID)a inner join " +
                "(select TASK_ID from audit_task_on_temp atot where  businesstype='1') b on a.TASK_ID= b.TASK_ID ";
        return CommonDao.getInstance().queryInt(sql);
    }
}
