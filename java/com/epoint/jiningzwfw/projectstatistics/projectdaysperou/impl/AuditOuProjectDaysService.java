package com.epoint.jiningzwfw.projectstatistics.projectdaysperou.impl;

import com.epoint.basic.auditresource.service.AuditResourceService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.entity.AuditOuProjectDays;

import java.util.List;
import java.util.Map;

/**
 * 每日部门办件统计分析表对应的后台service
 * 
 * @author yangyi
 * @version [版本号, 2021-06-30 17:37:23]
 */
public class AuditOuProjectDaysService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditOuProjectDaysService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditOuProjectDays record) {
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
        T t = baseDao.find(AuditOuProjectDays.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditOuProjectDays record) {
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
    public AuditOuProjectDays find(Object primaryKey) {
        return baseDao.find(AuditOuProjectDays.class, primaryKey);
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
    public AuditOuProjectDays find(String sql,  Object... args) {
        return baseDao.find(sql, AuditOuProjectDays.class, args);
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
    public List<AuditOuProjectDays> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditOuProjectDays.class, args);
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
    public List<AuditOuProjectDays> findList(String sql, int pageNumber, int pageSize, Object... args) {
        //system.out.println(sql);
        return baseDao.findList(sql, pageNumber, pageSize, AuditOuProjectDays.class, args);
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
    public Integer countAuditOuProjectDays(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public AuditCommonResult<List<AuditOuProjectDays>> findListByCondition(Map<String, String> map) {
        AuditResourceService<AuditOuProjectDays> auditResourceService = new AuditResourceService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            List<AuditOuProjectDays> list = auditResourceService.getAllRecord(AuditOuProjectDays.class, map, false);
            result.setResult(list);
        } catch (Exception var5) {
            result.setSystemFail(var5.getMessage());
        }

        return result;
    }

    public List<Record> getProjectInfoByDate(String startDate, String endDate) {
        String sql = "SELECT p.APPLYDATE as SDate,p.OUGUID as OUGUID , p.AREACODE as AREACODE, p.OUNAME as OUNAME , SUM(IF(p.STATUS=90, 1, 0)) AS tocnum " +
                ", SUM(IF(p.APPLYWAY IN (10, 11) AND p.STATUS=90, 1, 0)) AS exttocnum " +
                ", SUM(IF(e.projectNo IS NOT NULL,1,0)) AS evanum " +
                ", SUM(IF(p.APPLYWAY IN (10, 11) AND p.STATUS IN (12, 16, 24, 26, 28), 1, 0)) AS extnotacceptednum " +
                ", SUM(IF(IFNULL(p.SPARETIME,0)>=0, 1, 0)) AS onnum " +
                "FROM (" +
                "SELECT APPLYDATE, OUGUID, OUNAME, STATUS, APPLYWAY, SPARETIME, TASKGUID, FLOWSN ,AREACODE " +
                "FROM audit_project " +
                "WHERE APPLYDATE >= '"+startDate+"' " +
                "AND APPLYDATE <= '"+endDate+"' " +
                "UNION ALL " +
                "SELECT APPLYDATE, OUGUID, OUNAME, STATUS, APPLYWAY, SPARETIME, TASKGUID, FLOWSN ,AREACODE " +
                "FROM lc_project " +
                "WHERE APPLYDATE >= '"+startDate+"' " +
                "AND APPLYDATE <= '"+endDate+"'" +
                ") p " +
                "LEFT JOIN audit_task t on t.RowGuid=p.TASKGUID " +
                "LEFT JOIN evaluateservice e on e.projectNo =p.FLOWSN " +
                "WHERE t.businesstype = '1' GROUP BY p.OUGUID";
        return CommonDao.getInstance().findList(sql,Record.class);
    }

    public List<Record> getInfoByDate(String startDate, String endDate,Integer mins) {
        String sql = "select a.ouguid as ouguid,a.extconnum,a.extconreplynum,b.OUNAME,a.areacode,a.ASKDATE from  (SELECT OUGUID,areacode,ASKDATE " +
                ", count(1) AS extconnum " +
                ", sum(if(STATUS = '1' AND TIMESTAMPDIFF(MINUTE, ASKDATE, ANSWERDATE) <= "+mins+", 1, 0)) AS extconreplynum " +
                "FROM AUDIT_ONLINE_CONSULT " +
                "WHERE CONSULTTYPE = 1 " +
                "AND ASKDATE >= '"+startDate+"' " +
                "AND ASKDATE <= '"+endDate+"' " +
                "GROUP BY OUGUID) as a inner join frame_ou b on a.ouguid = b.OUGUID ";
        return CommonDao.getInstance().findList(sql,Record.class);
    }

    public Integer isExists(String ouguid, String sDate) {
        String sql = "select * as totalCount from AUDIT_OU_PROJECT_DAYS where SDate='" + sDate + "' and ouguid ='" + ouguid + "'";
        return CommonDao.getInstance().queryInt(sql);
    }
}
