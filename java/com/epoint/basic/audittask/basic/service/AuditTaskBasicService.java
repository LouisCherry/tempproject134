package com.epoint.basic.audittask.basic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.domain.CacheAuditTask;
import com.epoint.basic.audittask.basic.domain.ViewAuditTaskJiangSu;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.service.AuditTaskCache;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.DbKit;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.Parameter;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;

/**
 * 
 * 事项service
 * 
 * @author Administrator
 * @version [版本号, 2016年9月29日]
 
 
 */
@SuppressWarnings("unused")
public class AuditTaskBasicService
{
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    private IDataSourceService dataSourceService = ContainerFactory.getContainInfo()
            .getComponent(IDataSourceService.class);

    public AuditTaskBasicService() {
        commonDao = CommonDao.getInstance("task");
    }

    public AuditTaskBasicService(String dataSourceName) {
        commonDao = CommonDao.getInstance(dataSourceService.getDataSourceByName(dataSourceName));
    }

    public AuditTaskBasicService(DataSourceConfig dataSource) {
        commonDao = CommonDao.getInstance(dataSource);
    }

    public AuditTaskBasicService(ICommonDao dao) {
        this.commonDao = dao;
    }

    // 从缓存里面获取数据
    private AuditTaskCache<CacheAuditTask> auditTaskCacheInit = EHCacheUtil.get(ZwfwConstant.CACHE_AUDITTASK_TABLE);

    /**
     * 逻辑删除事项，is_editafterimport置为5
     * 
     * @param taskGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteTaskByStatus(String taskGuid) {
        String sql = "update AUDIT_TASK set IS_EDITAFTERIMPORT =?1 where ROWGUID=?2";
        //String sql = "delete from  AUDIT_TASK where ROWGUID=?2";
        commonDao.execute(sql, ZwfwConstant.TASKAUDIT_STATUS_YZF, taskGuid);
    }

    public void updateEnableAuditHottaskByTaskid(String taskid) {
        String sql = "  update Audit_Task_Hottask set ENABLE = 0 where RowGuid =? ";
        commonDao.execute(sql, taskid);

    }

    /**
     * 
     * 根据taskguid获取事项
     * 
     * @param taskGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditTask getAuditTaskByGuid(String taskGuid) {
        return commonDao.find(AuditTask.class, taskGuid);
    }

    /**
     * 
     * 更新事项
     * 
     * @param auditTask
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateAuditTask(AuditTask auditTask) {
        commonDao.update(auditTask);
    }

    /**
     * 
     * 添加事项基本信息
     * 
     * @param auditTask
     *            事项实体
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addAuditTaskAndExt(AuditTask auditTask) {
        commonDao.insert(auditTask);
    }

    /**
     * 
     * 根据taskId和状态获取事项
     * 
     * @param taskId
     *            事项id
     * @param is_editafterimports
     *            状态
     * @return AuditTask
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditTask getCopyTaskByTaskId(String taskId, String... is_editafterimports) {
        AuditTask auditTask = null;
        if (is_editafterimports != null && is_editafterimports.length > 0) {
            StringBuffer sb = new StringBuffer(" and IS_EDITAFTERIMPORT in (");
            for (String is_editafterimport : is_editafterimports) {
                sb.append(is_editafterimport).append(",");
            }
            String filter = sb.substring(0, sb.length() - 1);
            filter += " )";
            // 删除该taskid下面，审核状态为is_editafterimport的所有事项
            List<AuditTask> listAuditTask = this.selectTaskByTaskId(taskId, filter);
            if (listAuditTask != null && listAuditTask.size() > 0) {
                auditTask = listAuditTask.get(0);
                auditTask.put("count", listAuditTask.size());
            }
        }
        return auditTask;
    }

    /**
     * 
     * 根据条件map生成，sql条件已经 and开头， 不支持boolean类型的value
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildQuerySql(Map<String, String> conditionMap) {
        String sql = "select a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE from AUDIT_TASK a where 1=1";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildSql(conditionMap).replace(" where 1=1", "");
        return sql;
    }

    private String buildQuerySql(Map<String, String> conditionMap, List<String> exampleList) {
        String sql = "select a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time from AUDIT_TASK a where 1=1";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildSql(conditionMap, exampleList).replace(" where 1=1", "");
        return sql;
    }

    private String buildQuerySqlForcenter(Map<String, String> conditionMap) {
        String sql = "select b.rowguid,item_id,taskname,ouname,b.ordernum,is_enable,ouguid,PROCESSGUID,TYPE,AREACODE,task_id,promise_day,anticipate_day,charge_flag,"
                + "shenpilb,link_tel,supervise_tel,IS_EDITAFTERIMPORT from AUDIT_TASK b,audit_task_extension c where b.rowguid = c.taskguid and  1=1";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildPatchSql(conditionMap);
        return sql;
    }

    private String buildQuerySqlForcenter(Map<String, String> conditionMap, List<String> exampleList) {
        String sql = "select b.rowguid,item_id,taskname,ouname,b.ordernum,is_enable,ouguid,PROCESSGUID,TYPE,AREACODE,task_id,promise_day,anticipate_day,charge_flag,"
                + "shenpilb,link_tel,supervise_tel,IS_EDITAFTERIMPORT from AUDIT_TASK b where  1=1";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildPatchSql(conditionMap, exampleList);
        return sql;
    }

    /**
     * 
     * 根据条件map生成，sql条件已经 and开头， 不支持boolean类型的value
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildQuerySqlWithDict(Map<String, String> conditionMap) {
        String sql = "select a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id from AUDIT_TASK a left join AUDIT_TASK_EXTENSION b on a.ROWGUID=b.TASKGUID LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID where (a.IS_HISTORY=0 or a.IS_HISTORY is null)";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildSql(conditionMap).replace(" where 1=1", "");
        return sql;
    }

    /**
     * 
     * 根据条件map生成，sql条件已经 and开头， 不支持boolean类型的value
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildQuerySqlWithDict(Map<String, String> conditionMap, List<String> exampleList) {
        String sql = "select a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id from AUDIT_TASK a left join AUDIT_TASK_EXTENSION b on a.ROWGUID=b.TASKGUID LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID where (a.IS_HISTORY=0 or a.IS_HISTORY is null)";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildSql(conditionMap, exampleList).replace(" where 1=1", "");
        return sql;
    }

    /**
     * 
     * 根据事项ID，状态获取事项
     * 
     * @param taskid
     *            事项id
     * @param filter
     *            过滤
     * @return List 事项集合
     */
    public List<AuditTask> selectTaskByTaskId(String taskid, String filter) {
        StringBuffer sb = new StringBuffer("select * from AUDIT_TASK where TASK_ID=? ");
        sb.append(filter);
        if (commonDao.isSqlserver()) {
            sb.append(" order by CAST(VERSION AS int) DESC");
        }
        else if (commonDao.isOracle()) {
            sb.append(" order by  to_number(VERSION) DESC");
        }
        else if (commonDao.isMySql()) {
            sb.append(" order by CONVERT(VERSION,SIGNED) DESC");
        }
        return commonDao.findList(sb.toString(), AuditTask.class,taskid);
    }

    /**
     * 
     * 删除事项，以后级联信息。包括事项扩展信息
     * 
     * @param taskGuid
     *            事项guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void deleteAuditTask(String taskGuid) {
        // 1、先删除事项扩展信息
        commonDao.execute("delete from AUDIT_TASK_EXTENSION where TASKGUID= ?1", taskGuid);
        // 2、再删除事项基本信息
        commonDao.execute("delete from AUDIT_TASK where ROWGUID=?1", taskGuid);
    }

    /**
     * 通过辖区编码获取模板事项
     * 
     * @param areaCode
     *            辖区编码
     * @return
     */
    public String getTemplateTaskGuid(String areaCode) {
        String sql = "select rowguid from AUDIT_TASK where istemplate='1' and is_editafterimport='1' and areaCode=? ";
        return commonDao.queryString(sql,areaCode);
    }

    /**
     * 
     * 判断事项编码是否正在使用
     * 
     * @param itemid
     *            事项编码
     * @return boolean 是否在使用
     */
    public boolean isItemIdExist(String itemid) {
        boolean retbol = false;

        String sql = "select count(*) from Audit_Task where item_id=? and (is_editafterimport <> '-1' and is_editafterimport <> '5') and (IS_HISTORY=0 or IS_HISTORY is null)";
        Integer count = commonDao.find(sql, Integer.class,itemid);
        if (count > 0) {
            retbol = true;
        }

        return retbol;
    }

    /**
     *  判断事项编码是否正在使用
     *  @param itemid
     *  @param RowGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isItemIdExist(String itemid, String RowGuid) {
        boolean retbol = false;
        String sql = "select count(*) from Audit_Task where item_id=? and RowGuid<>? and (is_editafterimport <> '-1' and is_editafterimport <> '5')";
        Integer count = commonDao.find(sql, Integer.class,itemid,RowGuid);
        if (count > 0) {
            retbol = true;
        }
        return retbol;
    }

    /**
     * 
     * 更新外部流程图附件
     * 
     * @param attachGuid
     *            附件guid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateTaskoutImg(String attachGuid, String taskoutInfo) {
        FrameAttachInfo info = commonDao.find(FrameAttachInfo.class, attachGuid);
        if (info != null) {
            info.setCliengInfo(taskoutInfo);
            commonDao.update(info);
        }
    }

    /**
     * 
     * 获取所有可用事项
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAllUsableAuditTask() {
        return commonDao.findList(
                "select * from AUDIT_TASK where (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1",
                AuditTask.class);
    }

    /**
     * 
     * 获取所有事项扩展信息
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTaskExtension> getAllAuditTaskExt() {
        return commonDao.findList(
                "select * from AUDIT_TASK_EXTENSION where taskguid in (select rowguid from AUDIT_TASK where (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1)",
                AuditTaskExtension.class);
    }

    /**
     * 
     * 对事项信息根据条件查询，并且分页 其中conditionMap支持事项扩展信息的查询， 如果扩展信息的话，需要key写成
     * AUDIT_TASK_EXTENSION.XXX的方式，value不变
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param firstResult
     *            起始记录数
     * @param maxResults
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditTask> getAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
        List<String> exampleList = new ArrayList<String>();
        sqlCondition.isBlankOrValue("IS_HISTORY", "0");
        // 筛选+排序的sql
        String sql = "";
        //从主题处点击进来
        if (conditionMap.containsKey(
                "dict_id" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S")) {
            //如果是外网门户网站查询的 
            if (conditionMap.containsKey("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                    + ZwfwConstant.ZWFW_SPLIT + "S")) {
                sqlCondition.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
            }
            sql = buildQuerySqlWithDict(sqlCondition.getMap(), exampleList);
        }
        // 从除了主题之外的其他地方进来
        else {
            //如果事项是在用版本并且不是外网门户网站查询的
            if (conditionMap
                    .containsKey("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S")
                    && conditionMap.get("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S").equals(ZwfwConstant.CONSTANT_STR_ONE)
                    && !conditionMap.containsKey("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S")) {
                sqlCondition.getMap().remove("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT
                        + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S");

                String extraSql = buildQuerySqlForcenter(sqlCondition.getMap(), exampleList);
                String ifnullcase = " ifnull(IS_HISTORY, '0') = '0') ";
                if (commonDao.isOracle()) {
                    ifnullcase = " nvl(IS_HISTORY, '0') = '0') ";
                }
                String extraSqlstatus = " AND (IS_EDITAFTERIMPORT ='1' OR (IS_EDITAFTERIMPORT = '-1' AND tasksource = 0))";
                sql = "select a.* from (" + extraSql + extraSqlstatus
                        + " and not EXISTS( SELECT rowguid FROM audit_Task where b.IS_EDITAFTERIMPORT='-1' and b.task_id=task_id and IS_EDITAFTERIMPORT='1' and "
                        + ifnullcase + ") a ";
            }
            //外网门户网站查询
            else {
                sqlCondition.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
                sqlCondition.setOrderDesc("ordernum");
                sqlCondition.setOrderDesc("item_id");
                sql = buildQuerySql(sqlCondition.getMap(), exampleList);
            }

        }
        Object[] paramsobject = exampleList.toArray();
        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class, paramsobject);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(sql.replace("a.*", "count(1)")
                .replaceAll("a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time", "count(1)")
                .replaceAll("a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id", "count(1)"), paramsobject));
        return pageData;
    }

    /**
     * 
     * 对事项信息根据条件查询，并且分页 其中conditionMap支持事项扩展信息的查询， 如果扩展信息的话，需要key写成
     * AUDIT_TASK_nSION.XXX的方式，value不变
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param firstResult
     *            起始记录数
     * @param maxResults
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer  getAuditTaskCount(Map<String, String> conditionMap, int firstResult,
            int maxResults, String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
        List<String> exampleList = new ArrayList<String>();
        sqlCondition.isBlankOrValue("IS_HISTORY", "0");
        // 筛选+排序的sql
        String sql = "";
        //从主题处点击进来
        if (conditionMap.containsKey(
                "dict_id" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S")) {
            //如果是外网门户网站查询的 
            if (conditionMap.containsKey("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                    + ZwfwConstant.ZWFW_SPLIT + "S")) {
                sqlCondition.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
            }
            sqlCondition.setOrderDesc("ordernum");
            sqlCondition.setOrderDesc("item_id");
            sql = buildQuerySqlWithDict(sqlCondition.getMap(), exampleList);
        }
        // 从除了主题之外的其他地方进来
        else {
            //如果事项是在用版本并且不是外网门户网站查询的
            if (conditionMap
                    .containsKey("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S")
                    && conditionMap.get("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S").equals(ZwfwConstant.CONSTANT_STR_ONE)
                    && !conditionMap.containsKey("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S")) {
                sqlCondition.getMap().remove("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT
                        + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S");

                String extraSql = buildQuerySqlForcenter(sqlCondition.getMap(), exampleList);
                String ifnullcase = " ifnull(IS_HISTORY, '0') = '0') ";
                if (commonDao.isOracle()) {
                    ifnullcase = " nvl(IS_HISTORY, '0') = '0') ";
                }
                String extraSqlstatus = " AND (IS_EDITAFTERIMPORT ='1' OR (IS_EDITAFTERIMPORT = '-1' AND tasksource = 0))";
                sql = "select a.* from (" + extraSql + extraSqlstatus
                        + " and not EXISTS( SELECT rowguid FROM audit_Task where b.IS_EDITAFTERIMPORT='-1' and b.task_id=task_id and IS_EDITAFTERIMPORT='1' and "
                        + ifnullcase + ") a ORDER BY ordernum DESC,item_id DESC";
            }
            //外网门户网站查询
            else {
                sqlCondition.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
                sqlCondition.setOrderDesc("ordernum");
                sqlCondition.setOrderDesc("item_id");
                sql = buildQuerySql(sqlCondition.getMap(), exampleList);
            }

        }
        Object[] paramsobject = exampleList.toArray();
        return commonDao.queryInt(sql.replace("a.*","count(1)"), paramsobject);
    }

  
    
    
    /**
     * 
     * 对事项信息根据条件查询，并且分页 其中conditionMap支持事项扩展信息的查询， 如果扩展信息的话，需要key写成
     * AUDIT_TASK_EXTENSION.XXX的方式，value不变
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param firstResult
     *            起始记录数
     * @param maxResults
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditTask> getAuditTaskPageDataWithDict(Map<String, String> conditionMap, int firstResult,
            int maxResults, String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        // 筛选+排序的sql
        String sql = buildQuerySqlWithDict(conditionMap);
        sql += " and (IS_HISTORY=0 or IS_HISTORY is null)";
        
        if (StringUtil.isNotBlank(sortField)) {
            sql += " order by " + DbKit.checkOrderField(sql, sortField, AuditTask.class);
        }
        if (StringUtil.isNotBlank(sortOrder)) {
            sql += " " + DbKit.checkOrderDirect(sql, sortOrder);
        }

        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(sql.replaceAll("a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id", "count(1)")));
        return pageData;
    }

    /**
     * 
     * 在内存里面根据条件进行筛选
     * 
     * @param conditionMap
     *            条件map
     * @return List<AuditTask> 符合条件的事项list
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private List<AuditTask> getAuditTaskByCondition(Map<String, Object> conditionMap) {
        List<AuditTask> auditTaskListCn = new ArrayList<AuditTask>();
        List<CacheAuditTask> auditTaskList = auditTaskCacheInit.getRecordList();
        // 条件map不为空的情况下，逐个添加
        if (conditionMap != null && conditionMap.size() > 0) {
            for (CacheAuditTask cacheAuditTask : auditTaskList) {
                boolean flag = false;
                for (Entry<String, Object> entry : conditionMap.entrySet()) {
                    Object propertyValue = null;
                    // 控制是否加入list
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (StringUtil.isNotBlank(key)) {
                        // 事项联表查询，和事项扩展表关联 key如果包含 '.' 就说明需要关联事项扩展表
                        if (key.indexOf(".") != -1) {
                            String[] fields = key.split("\\.");
                            if (fields.length == 2) {
                                if ("AUDIT_TASK_EXTENSION".equalsIgnoreCase(fields[0])) {
                                    // 获取事项扩展信息缓存
                                    propertyValue = cacheAuditTask.getAuditTaskExtension().get(fields[1]);
                                }
                            }
                        }
                        else {
                            // 利用反射获取key字段的值
                            propertyValue = cacheAuditTask.getAuditTask().get(key);
                        }

                        // 可能会抛出转化异常，比如map里面value是string，但实体属性是int
                        // 如果value是string类型的
                        if (value instanceof String) {
                            // 获取到的值是string包含value
                            if (String.valueOf(propertyValue).indexOf(value.toString()) != -1) {
                                flag = true;
                            }
                            else {
                                flag = false;
                                break;
                            }
                            // 非string类型就直接等于判断
                        }
                        else {
                            String propertyValueStr = String.valueOf(propertyValue);
                            String valueStr = String.valueOf(value);
                            if (valueStr.equals(propertyValueStr)) {
                                flag = true;
                            }
                            else {
                                flag = false;
                                break;
                            }
                        }
                    }
                }
                if (flag) {
                    auditTaskListCn.add((AuditTask) cacheAuditTask.getAuditTask().clone());
                }
            }
        }
        // 条件map为空的情况下，直接等于缓存list
        else {
            for (CacheAuditTask cacheAuditTask : auditTaskList) {
                auditTaskListCn.add((AuditTask) cacheAuditTask.getAuditTask().clone());
            }
        }
        return auditTaskListCn;
    }

    /**
     * 
     * 根据事项ID获取最新可用的事项
     * 
     * @param taskid
     *            事项id
     * @return AuditTask 事项
     */
    public AuditTask selectUsableTaskByTaskID(String taskid) {
        AuditTask audittask = null;
        String sql = "select *  FROM Audit_Task Where task_id=? and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1";
        List<AuditTask> list = commonDao.findList(sql, AuditTask.class,taskid);
        if (list != null && list.size() > 0) {
            audittask = list.get(0);
        }
        return audittask;
    }

    /**
     * 
     * 通过事项ID取出对应的最新的事项
     * 
     * @param taskid
     *            事项id
     * @return AuditTask 事项
     */
    public List<AuditTask> selectlatestByTaskId(String taskid) {
        String sql = "";
        sql = "SELECT * FROM Audit_Task Where Task_ID=? ORDER BY IS_EDITAFTERIMPORT DESC";
        List<AuditTask> list = commonDao.findList(sql, AuditTask.class,taskid);
        return list;
    }

    /**
     * 
     * 通过rowguid取出对应事项
     * 
     * @param rowguid
     *            事项id
     * @return AuditTask 事项
     */
    public AuditTask selectByRowGuid(String rowguid) {
        String sql = "";
        sql = "select * from audit_task where ROWGUID =? ";
        AuditTask auditTask = commonDao.find(sql, AuditTask.class,rowguid);
        return auditTask;
    }

    /**
     * 窗口配置事项根据条件查询事项
     * 
     * @param condition
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> selectAuditTaskByCondition(String condition, String areaCode) {
        String sql = "select ROWGUID,TASKNAME,Task_id,applyertype,item_id,businesstype from AUDIT_TASK where TASKNAME like ? and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1 and IS_ENABLE=1"
                + " and ISTEMPLATE=0 and areacode=?";
        return commonDao.findList(sql, AuditTask.class, "%" + condition + "%", areaCode);
    }

    /**
     * 
     * 该节点下面有子节点，获取该窗口部门下的所有事项
     * 
     * @param objectGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> selectAuditTaskOuByObjectGuid(String objectGuid, String areaCode) {
        String sql = "select ROWGUID,TASKNAME,Task_id,Item_id,areacode,businesstype from AUDIT_TASK where OUGUID=? and( IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and IS_ENABLE=1"
                + " and ISTEMPLATE=0 and areacode=? order by item_id asc,ordernum desc";
        return commonDao.findList(sql, AuditTask.class, objectGuid, areaCode);
    }

    /**
     * 
     * 该节点下面有子节点，获取该窗口部门下的所有事项
     * 
     * @param areaCode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> selectAuditTaskByAreacode(String areaCode) {
        String sql = "select ROWGUID,TASKNAME,Task_id,Item_id,businesstype from AUDIT_TASK where ( IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and IS_ENABLE=1"
                + " and ISTEMPLATE=0 and areacode=?1 order by item_id asc,ordernum desc";
        return commonDao.findList(sql, AuditTask.class, areaCode);
    }

    /**
     * 
     * 获取选中事项
     * 
     * @param objectGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> selectAuditTaskByObjectGuid(String objectGuid) {
        String sql = "select ROWGUID,TASKNAME,Task_id,businesstype from AUDIT_TASK where (ROWGUID=?1 or task_id=?2) and ( IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and IS_ENABLE=1";
        return commonDao.findList(sql, AuditTask.class, objectGuid, objectGuid);
    }

    /**
     * 
     * 根据taskid和is_editafterimport删除事项
     * 
     * @param taskid
     * @param is_editafterimport
     */
    public void deleteTaskByTaskIdAndStatus(String taskid, String is_editafterimport) {
        String sql = "delete from audit_Task where task_id=?1 and is_editafterimport=?2";
        commonDao.execute(sql, taskid, is_editafterimport);
    }

    /**
     * 
     * 根据区域code以及申请人类型查询部门信息
     * 
     * @param applyertype
     * @param areacode
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> selectOuByApplyertype(String applyertype, String areacode) {
        String sql = "select b.OUGUID,b.OUNAME FROM frame_ou b where b.OUGUID in (select a.ouguid from audit_task a where a.AREACODE=?1 and APPLYERTYPE LIKE ?2 AND (a.IS_HISTORY IS NULL OR a.IS_HISTORY =''OR a.IS_HISTORY ='0') AND a.IS_EDITAFTERIMPORT=1 AND a.IS_ENABLE=1 GROUP BY a.OUGUID) order by b.ordernumber desc,b.ouname desc";
        return commonDao.findList(sql, Record.class, areacode, "%" + applyertype + "%");
    }

    /**
     * [一句话功能简述] 获取事项基本信息 + 事项EXT表的 IS_ALLOWBATCHREGISTER 字段信息 [功能详细描述]
     * 
     * @param oneTaskGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditTask getTaskAndExtByTaskid(String oneTaskid) {
        String sql = " select a.* from Audit_Task a "
                + " where a.TASK_ID =?1 and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and istemplate='0'";
        AuditTask auditTask = commonDao.find(sql, AuditTask.class, oneTaskid);
        if (auditTask != null) {
            return auditTask;
        }
        return null;
    }

    /**
     * 
     *  [获取事项库中所有的部门guid]
     *  
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getTaskOuGuid() {
        String sql = "select distinct OUGuid from Audit_Task where IS_EDITAFTERIMPORT=1 and (IS_HISTORY=0 or IS_HISTORY is null) and is_enable=1";
        return commonDao.findList(sql, Record.class);
    }

    public PageData<AuditTask> getWindowTaskPageData(Map<String, String> conditionMap,
            Map<String, String> conditionMapstatus, String userGuid, int first, int pageSize, String sortField,
            String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        String extraSql = sqlManageUtil.buildSql(conditionMap);
        String extraSqlstatus = " AND (IS_EDITAFTERIMPORT NOT IN ('-1', '-3', '-5') OR (IS_EDITAFTERIMPORT = '-1' AND tasksource = 1))";
        StringBuffer sb = new StringBuffer("select a.* from (select * from AUDIT_TASK ");
        sb.append(extraSql);
        sb.append(extraSqlstatus);
        sb.append(" ) a left join (select * from AUDIT_TASK ");
        sb.append(extraSql);
        sb.append("  AND (IS_EDITAFTERIMPORT IN ('0', '2')OR (IS_EDITAFTERIMPORT = '-1' AND tasksource = 1))) b");
        sb.append(" on a.task_id=b.task_id where (a.IS_EDITAFTERIMPORT='1' and b.rowguid is null) or (a.IS_EDITAFTERIMPORT in ('-1','0','2') and a.rowguid=b.rowguid )");
        StringBuffer sb1 = new StringBuffer("select count(*) from (select * from AUDIT_TASK ");
        sb1.append(extraSql);
        sb1.append(extraSqlstatus);
        sb1.append(" ) a left join (select * from AUDIT_TASK ");
        sb1.append(extraSql);
        sb1.append("  AND (IS_EDITAFTERIMPORT IN ('0', '2')OR (IS_EDITAFTERIMPORT = '-1' AND tasksource = 1))) b");
        sb1.append(" on a.task_id=b.task_id where (a.IS_EDITAFTERIMPORT='1' and b.rowguid is null) or (a.IS_EDITAFTERIMPORT in ('-1','0','2') and a.rowguid=b.rowguid )");
                // 增加Orderby语句
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by a.");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortOrder);
            sb.append(", a.rowguid desc");

        }
//        if (StringUtil.isNotBlank(sortField)) {
//            sql += " order by " + DbKit.checkOrderField(sql, "a."+sortField, AuditTask.class);
//        }
//        if (StringUtil.isNotBlank(sortOrder)) {
//            sql += " " + DbKit.checkOrderDirect(sql, "a."+sortOrder)+" , a.rowguid desc";
//        }
        List<AuditTask> dataList = commonDao.findList(sb.toString(), first, pageSize, AuditTask.class);
        int dataCount = commonDao.queryInt(sb1.toString());
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public List<String> selectZJTaskByTaskids(List<String> taskids) {
        if (taskids != null && taskids.size() > 0) {
            String strtaskids = "'" + StringUtil.join(taskids, "','") + "'";
            String sql = "select task_id  FROM Audit_Task a inner join Audit_Task_Extension b on a.rowguid = b.taskguid Where task_id in ("
                    + strtaskids
                    + ") and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1 and b.ISZIJIANXITONG='1'";
            return commonDao.findList(sql, String.class);
        }
        else {
            return null;
        }
    }

    /**
     *  江苏通用版权利事项和业务按层级展示分页
     *  @param conditionMap
     *  @param firstResult
     *  @param maxResults
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<ViewAuditTaskJiangSu> getViewAuditTaskJiangSuPageData(Map<String, String> conditionMap,
            int firstResult, int maxResults, String sortField, String sortOrder) {
        PageData<ViewAuditTaskJiangSu> pageData = new PageData<ViewAuditTaskJiangSu>();
        // 筛选+排序的sql
        String sql = "select * from View_Audit_Task where (IS_HISTORY=0 or IS_HISTORY is null)";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildSql(conditionMap).replace(" where 1=1", "");
//        if (StringUtil.isNotBlank(sortField)) {
//            sql += " order by " + sortField + " " + sortOrder;
//        }
        if (StringUtil.isNotBlank(sortField)) {
            sql += " order by " + DbKit.checkOrderField(sql, sortField, ViewAuditTaskJiangSu.class);
        }
        if (StringUtil.isNotBlank(sortOrder)) {
            sql += " " + DbKit.checkOrderDirect(sql, sortOrder);
        }

        List<ViewAuditTaskJiangSu> auditTaskList = commonDao.findList(sql, firstResult, maxResults,
                ViewAuditTaskJiangSu.class);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.findList(sql, ViewAuditTaskJiangSu.class).size());
        return pageData;
    }

    /**
     * 
     * 对事项信息根据条件查询，并且分页 其中conditionMap支持事项扩展信息的查询， 如果扩展信息的话，需要key写成
     * AUDIT_TASK_EXTENSION.XXX的方式，value不变
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param firstResult
     *            起始记录数
     * @param maxResults
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditTask> getXzAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        //        SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        String condition = sqlManageUtil.buildSql(conditionMap);
        condition = condition.replaceAll("where 1=1", "");
        String sql = "select a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE from (select t.*,d.AREACODE delegateareacode from audit_task t, audit_task_delegate d where d.TASKID=t.TASK_ID order by d.xzorder desc) a left join AUDIT_TASK_EXTENSION b on a.ROWGUID=b.TASKGUID where 1=1"
                + condition;
//        if (StringUtil.isNotBlank(sortField)) {
//            sql += " order by " + sortField + " " + sortOrder;
//        }
        if (StringUtil.isNotBlank(sortField)) {
            sql += " order by " + DbKit.checkOrderField(sql, sortField, AuditTask.class);
        }
        if (StringUtil.isNotBlank(sortOrder)) {
            sql += " " + DbKit.checkOrderDirect(sql, sortOrder);
        }

        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.findList(sql, AuditTask.class).size());
        return pageData;
    }

    public Record getAvgPromiseAndanticipatedate(String areacode) {
        String sql = "select IFNULL(avg(promise_day),0) promise,IFNULL(avg(anticipate_day),0) anticipate  from audit_task where areacode = ?1 and ( IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and IS_ENABLE=1  and ISTEMPLATE=0 and type='2'";
        if (commonDao.isOracle()) {
            sql = sql.replace("IFNULL", "NVL");
        }
        return commonDao.find(sql, Record.class, areacode);
    }

    public PageData<AuditTask> getAuditWindowTaskPageData(String windowguid, Map<String, String> map, int first,
            int pageSize, String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SQLManageUtil sm = new SQLManageUtil("task", true);
        String sqlbuild = sm.buildPatchSql(map);
        String order = "";
        if (StringUtil.isNotBlank(sortField)) {
            order = " order by b." + sortField + " " + sortOrder;
        }
        String usebletask = " and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1 ";
        String sql = "select a.*,b.ordernum as ordernumber,b.rowguid as windowtaskguid from audit_task a,audit_orga_windowtask b where a.task_id = b.taskid and windowguid=? "
                + sqlbuild + usebletask + order;
        String countsql = sql.replace("a.*,b.ordernum as ordernumber,b.rowguid as windowtaskguid", "count(1)");
        List<AuditTask> auditTaskList = commonDao.findList(sql, first, pageSize, AuditTask.class, windowguid);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(countsql, windowguid));
        return pageData;
    }

    public PageData<AuditTask> getAuditEnableTaskPageData(String fileds, Map<String, String> map, int first,
            int pageSize, String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SQLManageUtil sm = new SQLManageUtil("task", true);
        String sqlbuild = sm.buildPatchSql(map);
        String order = "";
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            order = " order by " + sortField + " " + sortOrder;
        }
        //String sqle = sm.buildSql(map);
        String sqle = " where (IS_ENABLE = '1' AND ISTEMPLATE = '0' "
                + sqlbuild + " AND ifnull(IS_HISTORY, '0') = '0' AND IS_EDITAFTERIMPORT = 1)";
        //String sql = "select " + fileds + " from view_enabledtask " + sqle + order;
        StringBuffer sb = new StringBuffer("select ");
        sb.append(fileds);
        sb.append(" from audit_task ");
        sb.append(sqle);
        sb.append(order);
        String sqlcount = sb.toString().replace(fileds, "count(*)");
        List<AuditTask> auditTaskList = commonDao.findList(sb.toString(), first, pageSize, AuditTask.class);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(sqlcount));
        return pageData;
    }

    public List<AuditTask> getAuditEnableTaskList(String fileds, Map<String, String> map, int first, int pageSize,
            String sortField, String sortOrder) {
        SQLManageUtil sm = new SQLManageUtil("task", true);
        String sqlbuild = sm.buildPatchSql(map);
        String order = "";
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            order = " order by " + sortField + " " + sortOrder;
        }
       /* if (StringUtil.isNotBlank(sortField)) {
            order += " order by " + DbKit.checkOrderField(order, sortField, AuditTask.class);
        }
        if (StringUtil.isNotBlank(sortOrder)) {
            order += " " + DbKit.checkOrderDirect(order, sortOrder);
        }*/
        String sqle = " where (IS_ENABLE = '1' AND ISTEMPLATE = '0' "
                + sqlbuild + " AND ifnull(IS_HISTORY, '0') = '0' AND IS_EDITAFTERIMPORT = 1)";
        //String sqle = sm.buildSql(map);
        StringBuffer sb = new StringBuffer("select ");
        sb.append(fileds);
        sb.append(" from audit_task ");
        sb.append(sqle);
        sb.append(order);
        List<AuditTask> auditTaskList = commonDao.findList(sb.toString(), first, pageSize, AuditTask.class);
        return auditTaskList;
    }

    public Integer getAuditEnableTaskCount(Map<String, String> map, int first, int pageSize, String sortField,
            String sortOrder) {
        SQLManageUtil sm = new SQLManageUtil("task", true);
        String sqlbuild = sm.buildPatchSql(map);
       // String sqle = sm.buildSql(map);
        String sqle = " where (IS_ENABLE = '1' AND ISTEMPLATE = '0' "
                + sqlbuild + " AND ifnull(IS_HISTORY, '0') = '0' AND IS_EDITAFTERIMPORT = 1)";
        String sql = "select count(*) from audit_task " + sqle;
        return commonDao.queryInt(sql);
    }

    /**
     * 
     * 根据事项ID获取最新可用的事项
     * 
     * @param taskid
     *            事项id
     * @return AuditTask 事项
     */
    public List<AuditTask> selectUsableTaskItemListByItemId(String item, String itemid) {
        AuditTask audittask = null;
        StringBuffer sb = new StringBuffer("select *  FROM Audit_Task Where item_id like '");
        sb.append(item);
        sb.append("%' and ITEM_ID NOT in('");
        sb.append(itemid);
        sb.append("') and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1 and IS_ENABLE=1 ");
        List<AuditTask> list = commonDao.findList(sb.toString(), AuditTask.class);
        return list;
    }

    /**
     * 
     * 根据事项ID获取最新可用的事项
     * 
     * @param taskid
     *            事项id
     * @return AuditTask 事项
     */
    public List<AuditTask> selectUsableTaskItemListByItemId(String item, String itemid, Map<String, String> sqlmap) {
        AuditTask audittask = null;
        SQLManageUtil sm = new SQLManageUtil("task", true);
        String sqlextend = sm.buildPatchSql(sqlmap);
        List<Object> params = new ArrayList<Object>();
        String sql = "select *  FROM Audit_Task Where item_id like ? and is_enable='1' and ITEM_ID NOT in " ;
        params.add(item.replace("\\", "\\\\").replace("%", "\\%") + "%");
        Parameter pa= DbKit.splitIn(itemid);
        sql += pa.getSql();
        sql += sqlextend;
        for(Object obj : pa.getValue()){
            params.add(obj);
        }
        List<AuditTask> list = commonDao.findList(sql, AuditTask.class,params.toArray());
        return list;
    }

    /**
     * 获取所有满足条件的没有小项的大项以及满足条件的小项所对应的大项
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param firstResult
     *            起始记录数
     * @param maxResults
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditTask> getGBAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
        List<String> exampleList = new ArrayList<String>();
        sqlCondition.isBlankOrValue("IS_HISTORY", "0");
        // 筛选+排序的sql
        String sql = "";

        // 从主题处点击进来
        if (conditionMap.containsKey(
                "dict_id" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S")) {
            sql = buildGBQuerySqlWithDict(sqlCondition.getMap(), exampleList);
        }
        // 从部门或其他地方点击进来
        else {
            sql = buildGBQuerySql(sqlCondition.getMap(), exampleList);
        }
        Object[] paramsobject = exampleList.toArray();
        //system.out.println(sql);
        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class, paramsobject);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.findList(sql, AuditTask.class, paramsobject).size());
        return pageData;
    }

    /**
     * 
     * 查询与主题相关的事项信息
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildGBQuerySqlWithDict(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        //查询没有有小项的大项所返回的参数列表
        List<String> tempresultList2 = new ArrayList<String>();
        String sql1 = "";//查询有小项的大项
        String sql2 = "";//查询没有有小项的大项
        String sql3 = "";//拼接sql1和sql2
        if (commonDao.isOracle()||commonDao.isDM()) {
            sql1 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id"
                    + " FROM audit_task a WHERE  IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                    + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND substr(ITEM_ID ,-3) = '000' AND ITEM_ID  IN ("
                    + "SELECT  concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK b "
                    + "INNER JOIN AUDIT_TASK_EXTENSION d ON b.ROWGUID = d.TASKGUID LEFT JOIN audit_task_map c ON b.TASK_ID = c.TASK_ID "
                    + " WHERE ( NOT (ITEM_ID LIKE '%000'))";
            sql2 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                    + "FROM audit_task a INNER JOIN AUDIT_TASK_EXTENSION d ON a.ROWGUID = d.TASKGUID "
                    + "LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID WHERE substr(ITEM_ID ,-3) = '000' AND ITEM_ID NOT IN ("
                    + "SELECT  concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK b WHERE IS_EDITAFTERIMPORT = 1 "
                    + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )"
                    + "AND ( NOT (ITEM_ID LIKE '%000')))";
        }
        else if (commonDao.isMySql()) {
            sql1 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id"
                    + " FROM audit_task a WHERE  IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                    + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND RIGHT (ITEM_ID, 3) = '000' AND ITEM_ID  IN ("
                    + "SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK b "
                    + "INNER JOIN AUDIT_TASK_EXTENSION d ON b.ROWGUID = d.TASKGUID LEFT JOIN audit_task_map c ON b.TASK_ID = c.TASK_ID "
                    + " WHERE ( NOT (ITEM_ID LIKE '%000'))";
            sql2 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                    + "FROM audit_task a INNER JOIN AUDIT_TASK_EXTENSION d ON a.ROWGUID = d.TASKGUID "
                    + "LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID WHERE RIGHT (ITEM_ID, 3) = '000' AND ITEM_ID NOT IN ("
                    + "SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK b WHERE IS_EDITAFTERIMPORT = 1 "
                    + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )"
                    + "AND ( NOT (ITEM_ID LIKE '%000')))";
        }
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "") + " ) ";
        //拼接sql，拼接通用条件
        sql2 += sqlManageUtil.buildSql(conditionMap, tempresultList2).replace(" where 1=1", "");
        sql3 = "select * from ( " + sql1 + " UNION  " + sql2 + " ) c order by ordernum desc, item_id desc ";
        exampleList.addAll(tempresultList1);
        exampleList.addAll(tempresultList2);
        return sql3;
    }

    /**
     * 
     * 获取部门或搜索条件搜索出来的相关事项信息
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildGBQuerySql(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        //查询没有有小项的大项所返回的参数列表
        List<String> tempresultList2 = new ArrayList<String>();
        String sql1 = "";//查询有小项的大项
        String sql2 = "";//查询没有有小项的大项
        String sql3 = "";//拼接sql1和sql2
        if (commonDao.isOracle() || commonDao.isDM()) {
            sql1 = "SELECT a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                    + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,"
                    + "a.transact_addr,a.Transact_time FROM audit_task a WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                    + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND substr(ITEM_ID ,-3) = '000' AND ITEM_ID  IN ("
                    + "SELECT concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK b INNER JOIN AUDIT_TASK_EXTENSION d ON b.ROWGUID = d.TASKGUID WHERE ( NOT (ITEM_ID LIKE '%000'))";
            sql2 = "SELECT b.rowguid,b.if_entrust,b.applyertype,b.item_id,b.taskname,b.ouname,b.ordernum,b.is_enable,b.ouguid,b.PROCESSGUID,b.TYPE,"
                    + "b.AREACODE,b.task_id,b.promise_day,b.anticipate_day,b.charge_flag,b.shenpilb,b.link_tel,b.supervise_tel,b.JBJMODE,b.BY_LAW,"
                    + "transact_addr,Transact_time FROM audit_task b INNER JOIN AUDIT_TASK_EXTENSION d ON b.ROWGUID = d.TASKGUID WHERE substr(ITEM_ID ,-3) = '000' AND ITEM_ID NOT IN ("
                    + "SELECT concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK WHERE IS_EDITAFTERIMPORT = 1 "
                    + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )"
                    + "AND ( NOT (ITEM_ID LIKE '%000')))";
        }
        else if (commonDao.isMySql()) {
            sql1 = "SELECT a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                    + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,"
                    + "a.transact_addr,a.Transact_time FROM audit_task a WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                    + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND RIGHT (ITEM_ID, 3) = '000' AND ITEM_ID  IN ("
                    + "SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK b INNER JOIN AUDIT_TASK_EXTENSION d ON b.ROWGUID = d.TASKGUID WHERE ( NOT (ITEM_ID LIKE '%000'))";
            sql2 = "SELECT b.rowguid,b.if_entrust,b.applyertype,b.item_id,b.taskname,b.ouname,b.ordernum,b.is_enable,b.ouguid,b.PROCESSGUID,b.TYPE,"
                    + "b.AREACODE,b.task_id,b.promise_day,b.anticipate_day,b.charge_flag,b.shenpilb,link_tel,b.supervise_tel,b.JBJMODE,b.BY_LAW,"
                    + "b.transact_addr,b.Transact_time FROM audit_task b INNER JOIN AUDIT_TASK_EXTENSION d ON b.ROWGUID = d.TASKGUID WHERE RIGHT (ITEM_ID, 3) = '000' AND ITEM_ID NOT IN ("
                    + "SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK  WHERE IS_EDITAFTERIMPORT = 1 "
                    + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )"
                    + "AND ( NOT (ITEM_ID LIKE '%000')))";
        }
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "") + " ) ";
        //拼接sql，拼接通用条件
        sql2 += sqlManageUtil.buildSql(conditionMap, tempresultList2).replace(" where 1=1", "");
        sql3 = "select * from ( " + sql1 + " UNION ALL " + sql2 + " ) c order by ordernum desc, item_id desc ";
        exampleList.addAll(tempresultList1);
        exampleList.addAll(tempresultList2);
        return sql3;
    }

    public List<Record> getShenpilbnumByCondition(Map<String, String> sqlmap) {
        SQLManageUtil sqlm = new SQLManageUtil();
        String sqle = sqlm.buildSql(sqlmap);
        String sql = "select shenpilb,count(1) num from audit_task " + sqle + " group by shenpilb";
        List<Record> result = commonDao.findList(sql, Record.class);
        return result;
    }

    /**
     * 获取乡镇大小项事项列表
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param firstResult
     *            起始记录数
     * @param maxResults
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditTask> getXZGBAuditTaskPageData(Map<String, String> conditionMap, String XZAreaCode,
            int firstResult, int maxResults, String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
        List<String> exampleList = new ArrayList<String>();
        sqlCondition.isBlankOrValue("IS_HISTORY", "0");
        // 筛选+排序的sql
        String sql = "";
        // 从主题处点击进来
        if (conditionMap.containsKey(
                "dict_id" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S")) {
            sql = buildXZGBQuerySqlWithDict(sqlCondition.getMap(), exampleList);
        }
        // 从部门或其他地方点击进来
        else {
            sql = buildXZGBQuerySql(sqlCondition.getMap(), exampleList);
        }
        Object[] paramsobject = exampleList.toArray();
        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class, paramsobject);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.findList(sql, AuditTask.class, paramsobject).size());
        return pageData;
    }

    /**
     * 
     * 查询与主题相关的事项信息
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildXZGBQuerySqlWithDict(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        //查询没有有小项的大项所返回的参数列表
        List<String> tempresultList2 = new ArrayList<String>();
        String sql1 = "";//查询有小项的大项
        String sql2 = "";//查询没有有小项的大项
        String sql3 = "";//拼接sql1和sql2
        if (commonDao.isOracle() ||commonDao.isDM()) {
            sql1 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                    + "from audit_task a  WHERE substr(ITEM_ID ,-3) = '000' AND ITEM_ID  IN ( SELECT concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM "
                    + "AUDIT_TASK b INNER JOIN  AUDIT_TASK_EXTENSION e on b.ROWGUID=e.TASKGUID  INNER JOIN audit_task_delegate d on b.Task_id = d.TASKID "
                    + "LEFT JOIN audit_task_map c ON b.TASK_ID = c.TASK_ID WHERE ( NOT (ITEM_ID LIKE '%000')) AND d.status = 1 AND d.ISALLOWACCEPT = 1 ";
            sql2 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                    + "from audit_task a left join AUDIT_TASK_EXTENSION e on a.ROWGUID=e.TASKGUID LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID "
                    + "INNER JOIN audit_task_delegate d ON a.Task_id = d.TASKID WHERE substr(ITEM_ID ,-3) = '000' "
                    + "AND ITEM_ID NOT IN(SELECT concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK WHERE "
                    + "IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) AND ( NOT ( ITEM_ID LIKE '%000')))"
                    + " AND d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        }
        else if (commonDao.isMySql()) {
            sql1 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                    + "from audit_task a  WHERE RIGHT(ITEM_ID,3)='000' AND ITEM_ID  IN ( SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM "
                    + "AUDIT_TASK b INNER JOIN  AUDIT_TASK_EXTENSION e on b.ROWGUID=e.TASKGUID  INNER JOIN audit_task_delegate d on b.Task_id = d.TASKID "
                    + "LEFT JOIN audit_task_map c ON b.TASK_ID = c.TASK_ID WHERE ( NOT (ITEM_ID LIKE '%000')) AND d.status = 1 AND d.ISALLOWACCEPT = 1 ";
            sql2 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                    + "from audit_task a left join AUDIT_TASK_EXTENSION e on a.ROWGUID=e.TASKGUID LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID "
                    + "INNER JOIN audit_task_delegate d ON a.Task_id = d.TASKID WHERE RIGHT(ITEM_ID,3)='000' "
                    + "AND ITEM_ID NOT IN (SELECT concat( LEFT (`audit_task`.`ITEM_ID`, (length(`audit_task`.`ITEM_ID`) - 3)),'000')FROM AUDIT_TASK WHERE "
                    + "IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL) AND ( NOT ( ITEM_ID LIKE '%000')))"
                    + " AND d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        }
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "") + " ) ";
        //拼接sql，拼接通用条件
        sql1 += " AND IS_EDITAFTERIMPORT =1 AND ISTEMPLATE =0 AND IS_ENABLE =1 AND (IS_HISTORY=0 OR IS_HISTORY IS NULL)";
        sql2 += sqlManageUtil.buildSql(conditionMap, tempresultList2).replace(" where 1=1", "");
        sql3 = "select * from ( " + sql1 + " UNION " + sql2 + " ) c order by ordernum desc, item_id desc";
        exampleList.addAll(tempresultList1);
        exampleList.addAll(tempresultList2);
        return sql3;
    }

    /**
     * 
     * 获取部门或搜索条件搜索出来的乡镇相关事项信息
     * 
     * @param conditionMap
     *            条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildXZGBQuerySql(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        //查询没有有小项的大项所返回的参数列表
        List<String> tempresultList2 = new ArrayList<String>();
        String sql1 = "";//查询有小项的大项
        String sql2 = "";//查询没有有小项的大项
        String sql3 = "";//拼接sql1和sql2
        if (commonDao.isOracle() || commonDao.isDM()) {
            sql1 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,"
                    + "a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time "
                    + "from audit_task a  WHERE  substr(ITEM_ID ,-3)='000' AND ITEM_ID  IN ( SELECT  concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM "
                    + " AUDIT_TASK b INNER JOIN  AUDIT_TASK_EXTENSION e on b.ROWGUID=e.TASKGUID  INNER JOIN audit_task_delegate d on b.Task_id = d.TASKID "
                    + "WHERE ( NOT (ITEM_ID LIKE '%000')) AND d.status = 1 AND d.ISALLOWACCEPT = 1";
            sql2 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,"
                    + "a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time "
                    + "from audit_task a left join AUDIT_TASK_EXTENSION e on a.ROWGUID=e.TASKGUID INNER JOIN audit_task_delegate d on a.Task_id = d.TASKID  "
                    + "WHERE substr(ITEM_ID ,-3)='000' "
                    + "AND ITEM_ID NOT IN (SELECT concat( substr (ITEM_ID,0,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK WHERE IS_EDITAFTERIMPORT = 1 "
                    + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND ( NOT (ITEM_ID LIKE '%000'))) "
                    + "AND d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        }
        else if (commonDao.isMySql()) {
            sql1 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,"
                    + "a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time "
                    + "from audit_task a  WHERE RIGHT(ITEM_ID,3)='000' AND ITEM_ID  IN ( SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM "
                    + " AUDIT_TASK b INNER JOIN  AUDIT_TASK_EXTENSION e on b.ROWGUID=e.TASKGUID  INNER JOIN audit_task_delegate d on b.Task_id = d.TASKID "
                    + "WHERE ( NOT (ITEM_ID LIKE '%000')) AND d.status = 1 AND d.ISALLOWACCEPT = 1";
            sql2 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,"
                    + "a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time "
                    + "from audit_task a left join AUDIT_TASK_EXTENSION e on a.ROWGUID=e.TASKGUID INNER JOIN audit_task_delegate d on a.Task_id = d.TASKID  "
                    + "WHERE RIGHT(ITEM_ID,3)='000' "
                    + "AND ITEM_ID NOT IN (SELECT concat( LEFT (ITEM_ID,(length(ITEM_ID) - 3)),'000') FROM AUDIT_TASK WHERE IS_EDITAFTERIMPORT = 1 "
                    + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND ( NOT (ITEM_ID LIKE '%000'))) "
                    + "AND d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        }
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "") + " ) ";
        //拼接sql，拼接通用条件
        sql1 += " AND IS_EDITAFTERIMPORT =1 AND ISTEMPLATE =0 AND IS_ENABLE =1 AND (IS_HISTORY=0 OR IS_HISTORY IS NULL)";
        sql2 += sqlManageUtil.buildSql(conditionMap, tempresultList2).replace(" where 1=1", "");
        sql3 = "select * from ( " + sql1 + " UNION ALL " + sql2 + " ) c order by ordernum desc, item_id desc ";
        exampleList.addAll(tempresultList1);
        exampleList.addAll(tempresultList2);
        return sql3;
    }

    /**
     * 
     *  查询满足条件的乡镇事项列表（大厅全局搜索用）
     * 
     *  @param sql
     *  @param first
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     */
    public PageData<AuditTask> getXZGBpageDataByCondition(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SQLManageUtil sqlutil = new SQLManageUtil("task", true);
        String sqlbuild = sqlutil.buildPatchSql(conditionMap);
        String order = "";
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            order = " order by " + sortField + " " + sortOrder;
        }
        String sqle = sqlutil.buildSql(conditionMap).replace(" where 1=1", "");
        String sql = " select a.* from view_enabledtask a INNER JOIN audit_task_delegate d where a.TASK_ID= d.TASKID "
                + sqle + order;
        String sqlcount = sql.replace("a.*", "count(1)");
        List<AuditTask> auditTaskList = commonDao.findList(sql, first, pageSize, AuditTask.class);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(sqlcount));
        return pageData;
    }

    /**
     * 
     *  查询满足条件的乡镇事项列表（大厅全局搜索用,不区分大小项）
     * 
     *  @param sql
     *  @param first
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     */
    public PageData<AuditTask> getXZpageDataByCondition(Map<String, String> conditionMap, int first, int pageSize,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        List<String> exampleList = new ArrayList<String>();
        SqlConditionUtil sqlCondition2 = new SqlConditionUtil(conditionMap);
        sqlCondition2.getMap().remove(
                "d.areacode" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S");
        SqlConditionUtil sqlCondition3 = new SqlConditionUtil(conditionMap);
        sqlCondition3.getMap().remove(
                "taskname" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_LIKE + ZwfwConstant.ZWFW_SPLIT + "S");
        sqlCondition3.getMap().remove(
                "ISTEMPLATE" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S");
        sqlCondition3.getMap().remove("IS_EDITAFTERIMPORT" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                + ZwfwConstant.ZWFW_SPLIT + "S");
        sqlCondition3.getMap().remove(
                "IS_ENABLE" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S");
        String sql = "select a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,"
                + "a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time "
                + "from AUDIT_TASK a left join AUDIT_TASK_EXTENSION b on a.ROWGUID=b.TASKGUID where 1=1";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql += sqlManageUtil.buildSql(sqlCondition2.getMap(), exampleList).replace(" where 1=1", "");
        String sql2 = "SELECT b.* FROM audit_task_delegate d LEFT JOIN ( " + sql
                + " AND (IS_HISTORY = 0 OR IS_HISTORY IS NULL ) ) b ON d.TASKID = b.TASK_ID WHERE b.is_enable = 1 AND d.status = 1 AND d.isallowaccept = 1 "
                + sqlManageUtil.buildPatchSql(sqlCondition3.getMap());
        Object[] paramsobject = exampleList.toArray();
        List<AuditTask> auditTaskList = commonDao.findList(sql2, first, pageSize, AuditTask.class, paramsobject);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.findList(sql2, AuditTask.class, paramsobject).size());
        return pageData;
    }

    public Integer getTasknumByCondition(Map<String, String> sqlmap) {
        SQLManageUtil sqlm = new SQLManageUtil();
        String sqle = sqlm.buildSql(sqlmap);
        String sql = "select count(1) from audit_task " + sqle;
        Integer result = commonDao.queryInt(sql);
        return result;
    }

    public PageData<AuditTask> getSearchAuditEnableTaskPageData(String keyWord, int first, int pageSize,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        String sql = " SELECT RowGuid,TaskName,ordernum,applyertype,TASK_ID,ouname,Transact_addr,By_law,By_law,Shenpilb,Type,Anticipate_day,"
                + "Promise_day,Link_tel,Supervise_tel,Item_id  "
                + " FROM audit_task where TASK_ID in ( SELECT DISTINCT(B.CLIENTIDENTIFIER) FROM audit_online_tag A "
                + "INNER JOIN audit_tag_map B on A.RowGuid = B.TAGGUID and B.TAGTYPE = '1' AND A.TAGNAME LIKE ?) and IS_EDITAFTERIMPORT='1'"
                + " and ISTEMPLATE='0' and IS_ENABLE='1' UNION "
                + "select RowGuid,TaskName,ordernum,applyertype,TASK_ID,ouname,Transact_addr,By_law,By_law,Shenpilb,Type,Anticipate_day,"
                + "Promise_day,Link_tel,Supervise_tel,Item_id  "
                + " from view_enabledtask  where 1=1  and taskname like ? and IS_EDITAFTERIMPORT='1'  and "
                + "ISTEMPLATE='0' and IS_ENABLE='1' order by ordernum desc ";
        List<AuditTask> auditTaskList = commonDao.findList(sql, first, pageSize, AuditTask.class, "%" + keyWord + "%",
                "%" + keyWord + "%");
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.findList(sql, AuditTask.class, "%" + keyWord + "%", "%" + keyWord + "%").size());
        return pageData;
    }
    
    
    /**
     * 
     * 对事项信息根据条件查询，并且conditionMap支持事项扩展信息的查询， 如果扩展信息的话，需要key写成
     * AUDIT_TASK_EXTENSION.XXX的方式，value不变
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @return List<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskList(Map<String, String> conditionMap) {
        SqlConditionUtil sqlCondition = new SqlConditionUtil(conditionMap);
        List<String> exampleList = new ArrayList<String>();
        sqlCondition.isBlankOrValue("IS_HISTORY", "0");
        // 筛选+排序的sql
        String sql = "";
        //从主题处点击进来
        if (conditionMap.containsKey(
                "dict_id" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S")) {
            //如果是外网门户网站查询的 
            if (conditionMap.containsKey("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                    + ZwfwConstant.ZWFW_SPLIT + "S")) {
                sqlCondition.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
            }
            sqlCondition.setOrderDesc("ordernum");
            sqlCondition.setOrderDesc("item_id");
            sql = buildQuerySqlWithDict(sqlCondition.getMap(), exampleList);
        }
        // 从除了主题之外的其他地方进来
        else {
            //如果事项是在用版本并且不是外网门户网站查询的
            if (conditionMap
                    .containsKey("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S")
                    && conditionMap.get("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S").equals(ZwfwConstant.CONSTANT_STR_ONE)
                    && !conditionMap.containsKey("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S")) {
                sqlCondition.getMap().remove("is_editafterimport" + ZwfwConstant.ZWFW_SPLIT
                        + ZwfwConstant.SQL_OPERATOR_EQ + ZwfwConstant.ZWFW_SPLIT + "S");

                String extraSql = buildQuerySqlForcenter(sqlCondition.getMap(), exampleList);
                String ifnullcase = " ifnull(IS_HISTORY, '0') = '0') ";
                if (commonDao.isOracle()) {
                    ifnullcase = " nvl(IS_HISTORY, '0') = '0') ";
                }
                String extraSqlstatus = " AND (IS_EDITAFTERIMPORT ='1' OR (IS_EDITAFTERIMPORT = '-1' AND tasksource = 0))";
                sql = "select a.* from (" + extraSql + extraSqlstatus
                        + " and not EXISTS( SELECT rowguid FROM audit_Task where b.IS_EDITAFTERIMPORT='-1' and b.task_id=task_id and IS_EDITAFTERIMPORT='1' and "
                        + ifnullcase + ") a ORDER BY ordernum DESC,item_id DESC";
            }
            //外网门户网站查询
            else {
                sqlCondition.getMap().remove("iszwmhwz" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S");
                sqlCondition.setOrderDesc("ordernum");
                sqlCondition.setOrderDesc("item_id");
                sql = buildQuerySql(sqlCondition.getMap(), exampleList);
            }

        }
        Object[] paramsobject = exampleList.toArray();
        List<AuditTask> auditTaskList = commonDao.findList(sql, AuditTask.class, paramsobject);
        return auditTaskList;
    }

}
