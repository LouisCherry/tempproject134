package com.epoint.zwdt.zwdtrest.task.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Service
public class JnAppRestService {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    protected ICommonDao commonDao;

    public JnAppRestService() {
        commonDao = CommonDao.getInstance();
    }

    public List<Record> selectOuByApplyertype(String applyertype, String is_fugongfuchan, String areacode, String taskname, String isyc, String xzql, List<String> list) {
        String sql = "select b.OUGUID,b.OUNAME,b.OUSHORTNAME  FROM frame_ou b where b.OUGUID in "
                + "(select a.ouguid from audit_task a where a.AREACODE=?1  ";
        if (StringUtil.isNotBlank(applyertype)) {
            sql += "and APPLYERTYPE LIKE '%" + applyertype + "%' ";
        }
        sql += " and IFNULL(a.is_history,0)=0 AND a.IS_EDITAFTERIMPORT=1 ";

        if (StringUtil.isNotBlank(is_fugongfuchan)) {
            sql += "AND is_fugongfuchan ='1' ";
        }

        if ("1".equals(isyc)) {
            sql += " and a.businesstype = '1' ";
        }
        if ("1".equals(xzql)) {
            sql += " and a.shenpilb <> '11'";
        }

        String tasknames = "%";
        List<String> splitStringList = chineseSplitFunction(taskname, 3);
        if (splitStringList != null && splitStringList.size() > 0) {
            //2022/06/20 阳佳 如果list不为空，则查询task_id
            if (ValidateUtil.isNotBlankCollection(list)) {
                sql += " and task_id in (" + StringUtil.joinSql(list) + ")";
            } else {
                for (String split : splitStringList) {
                    tasknames += split + "%";
                }
                sql += "and taskname like '" + tasknames + "'";

            }
        }

        sql += "AND a.IS_ENABLE=1 AND (LENGTH(a.item_id) = 31 or LENGTH(a.item_id) = 33 ) and a.shenpilb!='15' and ifnull(a.iswtshow,'') in ('','1') GROUP BY a.OUGUID) ORDER BY ordernumber DESC";
        return commonDao.findList(sql, Record.class, areacode);
    }

    public List<Record> selectOuByApplyertypeYC(String applyertype, String Dao_xc_num, String ISPYC, String Operationscope, String If_express, String mpmb, String CHARGE_FLAG, String mashangban, String wangshangban, String sixshenpilb, String bianminfuwu, String yishenqing, String qctb, String ggfw, String xzxk, String areacode, String kstbsx, String qstb) {
        String sql = "select b.OUGUID,b.OUNAME,b.OUSHORTNAME  FROM frame_ou b join (  "
                + "select a.ouguid from audit_task a  join audit_task_extension c on a.rowguid = c.taskguid where a.AREACODE=?1 AND"
                + " IFNULL(a.is_history,0)=0 AND a.IS_EDITAFTERIMPORT=1 and a.shenpilb <> '15'";
        if (StringUtil.isNotBlank(Dao_xc_num) && "1".equals(Dao_xc_num)) {
            sql += " and a.applyermin_count in ('0','1') and a.taskcode <> '' and a.businesstype = '1' ";
        }
        if ("1".equals(kstbsx)) {
            sql += " and a.iskstb  = '1' ";
        }
        if ("1".equals(qstb)) {
            sql += " and a.isjnqstb = '1' ";
        }

        if (StringUtil.isNotBlank(ggfw)) {
            sql += " and a.shenpilb = '11'";
        }
        if (StringUtil.isNotBlank(xzxk)) {
            sql += " and a.shenpilb = '01'";
        }
        if (StringUtil.isNotBlank(mpmb) && "1".equals(mpmb)) {
            sql = sql.replace("and a.shenpilb <> '15'", "");
            sql += " and a.is_mpmb = '1' ";
        }
        if (StringUtil.isNotBlank(bianminfuwu) && "1".equals(bianminfuwu)) {
            sql = sql.replace("and a.shenpilb <> '15'", "");
            sql += " and a.is_bianminfuwu = '1' ";
        }
        if (StringUtil.isNotBlank(sixshenpilb) && "1".equals(sixshenpilb)) {
            sql += " and a.shenpilb in ('01','10','07','05','08','06','11') and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(yishenqing) && "1".equals(yishenqing)) {
            sql += " and a.businesstype = '1' and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(mashangban)) {
            sql += " and a.promise_day in ('0','1') and a.taskcode <> '' and a.businesstype = '1' ";
        }
        if (StringUtil.isNotBlank(wangshangban)) {
            sql += " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%' or a.wangbanshendu like '%2%' or a.wangbanshendu like '%3%' or a.wangbanshendu like '%4%' )  and a.businesstype = '1' and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(qctb)) {
            sql += " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%' )  and a.businesstype = '1' and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(ISPYC)) {
            sql += " and a.ISPYC = '1' and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(CHARGE_FLAG)) {
            sql += " and a.CHARGE_FLAG = '1' and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(Operationscope)) {
            sql += " and a.CROSS_SCOPE in ('1','2','3') and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(If_express)) {
            sql += " and a.IS_DELIVERY = '1' and a.taskcode <> '' ";
        }
        if (StringUtil.isNotBlank(Dao_xc_num) && "0".equals(Dao_xc_num)) {
            sql += " and (a.applyermin_count = '0' or a.wangbanshendu REGEXP '5|6|7') and a.taskcode <> '' and a.businesstype = '1' ";
        }

        if (StringUtil.isNotBlank(applyertype)) {
            sql += " and a.applyertype like '%" + applyertype + "%' ";
        }

        sql += " AND a.IS_ENABLE=1 GROUP BY a.OUGUID) a on b.ouguid = a.ouguid";
        return commonDao.findList(sql, Record.class, areacode);
    }


    public String getTaskguidByItemid(String itemid) {
        String sql = "SELECT rowguid FROM audit_task where item_id=?1 and Is_editafterimport=1 "
                + " AND task_id is not null and IFNULL(IS_HISTORY,0)= 0 and IS_ENABLE=1 ORDER BY Operatedate DESC ";
        return commonDao.queryString(sql, itemid);
    }

    public AuditTask getTaskByInnercode(String innercode) {
        String sql = "SELECT rowguid,areacode,inner_code,item_id,taskname,task_id,zj_url,is_turn FROM audit_task where inner_code=?1 and Is_editafterimport=1 "
                + " AND task_id is not null and IFNULL(IS_HISTORY,0)= 0 and IS_ENABLE=1 ORDER BY Operatedate DESC ";
        return commonDao.find(sql, AuditTask.class, innercode);
    }

    public String getTaskguidByInnercode(String inner_code) {
        String sql = "SELECT rowguid FROM audit_task where inner_code=?1 and Is_editafterimport=1 "
                + " AND task_id is not null and IFNULL(IS_HISTORY,0)= 0 and IS_ENABLE=1 ORDER BY Operatedate DESC ";
        return commonDao.queryString(sql, inner_code);
    }

    public FrameOu getAreanameByAreacode(String areacode) {
        String sql = "select * from frame_ou where ouguid = (select ouguid from audit_orga_area where xiaqucode = ?)";
        return commonDao.find(sql, FrameOu.class, areacode);
    }


    /**
     * 获取乡镇大小项事项列表
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
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
        int count = getXZGBQuerySqlCount(conditionMap, exampleList);
        pageData.setRowCount(count);      //结束之间
        //程序块运行时间
        return pageData;
    }


    /**
     * 查询与主题相关的事项信息
     *
     * @param conditionMap 条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildXZGBQuerySqlWithDict(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        String sql1 = "";//查询有小项的大项
        String sql3 = "";//拼接sql1和sql2
        sql1 = "SELECT a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id "
                + "from audit_task a left join AUDIT_TASK_EXTENSION e on a.ROWGUID=e.TASKGUID LEFT JOIN audit_task_map c ON a.TASK_ID = c.TASK_ID "
                + "INNER JOIN audit_task_delegate d ON a.Task_id = d.TASKID "
                + "WHERE LENGTH(ITEM_ID)=31 AND d.status = 1 AND d.ISALLOWACCEPT = 1 AND IS_EDITAFTERIMPORT = 1 "
                + "AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) ";
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "");
        //sql3 = "select * from ( " + sql1  + " ) c order by ordernum desc, item_id desc";
        exampleList.addAll(tempresultList1);
        return sql1;
    }


    /**
     * 获取部门或搜索条件搜索出来的乡镇相关事项信息
     *
     * @param conditionMap 条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildXZGBQuerySql(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        String mashangban = conditionMap.get("mashangban#zwfw#eq#zwfw#S");
        String wangshangban = conditionMap.get("wangshangban#zwfw#eq#zwfw#S");
        String qctb = conditionMap.get("qctb#zwfw#eq#zwfw#S");
        String isznb = conditionMap.get("isznb#zwfw#eq#zwfw#S");
        String lpt = conditionMap.get("lpt#zwfw#eq#zwfw#S");
        String sql1 = "";//查询有小项的大项
        sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,"
                + "a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.transact_addr,a.Transact_time,a.turn_type "
                + "from audit_task a JOIN audit_task_delegate d on a.Task_id = d.TASKID  "
                + "WHERE d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        if ("1".equals(mashangban)) {
            sql1 += " and a.promise_day in ('0','1') and a.taskcode <> '' and a.businesstype = '1' ";
        }
        if ("1".equals(wangshangban)) {
            sql1 += " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%' or a.wangbanshendu like '%2%' or a.wangbanshendu like '%3%' or a.wangbanshendu like '%4%') and a.businesstype = '1' ";
        }
        if ("1".equals(qctb)) {
            sql1 += " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%' ) and a.businesstype = '1' ";
        }
        if ("1".equals(isznb)) {
            sql1 += " and (a.is_mpmb = '1' or a.isbigshow = '1') ";
        }
        if ("0".equals(lpt)) {
            sql1 += " and (a.applyermin_count = '0' or a.wangbanshendu REGEXP '5|6|7') and a.taskcode <> '' and a.businesstype = '1' ";
        }
        conditionMap.remove("mashangban#zwfw#eq#zwfw#S");
        conditionMap.remove("wangshangban#zwfw#eq#zwfw#S");
        conditionMap.remove("qctb#zwfw#eq#zwfw#S");
        conditionMap.remove("isznb#zwfw#eq#zwfw#S");
        conditionMap.remove("lpt#zwfw#eq#zwfw#S");
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "");
        exampleList.addAll(tempresultList1);
        return sql1;
    }

    /**
     * 获取部门或搜索条件搜索出来的乡镇相关事项信息
     *
     * @param conditionMap 条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private int getXZGBQuerySqlCount(Map<String, String> conditionMap, List<String> exampleList) {
        String sql1 = "SELECT count(1) FROM audit_task a JOIN audit_task_delegate d ON a.Task_id = d.TASKID WHERE IS_EDITAFTERIMPORT =1 AND ISTEMPLATE =0 AND IS_ENABLE =1 AND ifnull(IS_HISTORY ,0) =0 ";
        sql1 += " AND d.STATUS = 1 AND d.ISALLOWACCEPT = 1 AND d.areacode = ? ";
        String areacode = conditionMap.get("d.areacode#zwfw#eq#zwfw#S");
        String taskname = conditionMap.get("taskname#zwfw#like#zwfw#S");

        String tasknames = "%";
        if (taskname != null) {
            List<String> splitStringList = chineseSplitFunction(taskname, 3);
            for (String split : splitStringList) {
                tasknames += split + "%";
            }
        }

        if (StringUtil.isNotBlank(taskname)) {
            sql1 += " and a.taskname like ?";
        }
        int count = commonDao.queryInt(sql1, areacode, tasknames);
        return count;
    }


    /**
     * 获取所有满足条件的没有小项的大项以及满足条件的小项所对应的大项
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
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

        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class, paramsobject);
        System.out.println("本次查询的sql语句为：" + sql);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(sql.replace("a.*", "count(1)")
                .replaceAll("a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,a.transact_addr,a.Transact_time,a.turn_type", "count(1)")
                .replaceAll("a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type", "count(1)")
                .replaceAll("a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,a.transact_addr,a.Transact_time,a.turn_type", "count(1)")
                .replaceAll("a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type", "count(1)"), paramsobject));
        return pageData;
    }

    /**
     * 查询与主题相关的事项信息
     *
     * @param conditionMap 条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildGBQuerySqlWithDict(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        String sql1 = "";//查询有小项的大项
        String sql3 = "";//拼接sql1和sql2
        String taskclass = conditionMap.get("dict_id#zwfw#eq#zwfw#S");
        conditionMap.remove("dict_id#zwfw#eq#zwfw#S");
        String applyertype = conditionMap.get("applyertype#zwfw#like#zwfw#S");
        if (StringUtil.isNotBlank(taskclass)) {
            if ("10".equals(applyertype)) {
                sql1 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type "
                        + " FROM audit_task a join audit_task_extension b on a.rowguid = b.taskguid WHERE  IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                        + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND LENGTH(ITEM_ID)=31 ";

                conditionMap.put("TASKCLASS_FORCOMPANY#zwfw#like#zwfw#S", taskclass);

            } else {
                sql1 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type "
                        + " FROM audit_task a join audit_task_extension b on a.rowguid = b.taskguid WHERE  IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                        + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND LENGTH(ITEM_ID)=31 ";
                conditionMap.put("TASKCLASS_FORPERSION#zwfw#like#zwfw#S", taskclass);
            }
        } else {
            sql1 = "SELECT a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type "
                    + " FROM audit_task a WHERE  IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                    + "AND IS_ENABLE = 1 AND (  IS_HISTORY = 0 OR IS_HISTORY IS NULL ) AND LENGTH(ITEM_ID)=31  ";

        }

        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "");
        //拼接sql，拼接通用条件
        //sql3 = "select * from ( " + sql1  + " ) c order by ordernum desc, item_id desc ";
        exampleList.addAll(tempresultList1);
        return sql1;
    }

    /**
     * 获取部门或搜索条件搜索出来的相关事项信息
     *
     * @param conditionMap 条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildGBQuerySql(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        String taskname = conditionMap.get("taskname#zwfw#like#zwfw#S");
        String mashangban = conditionMap.get("mashangban#zwfw#eq#zwfw#S");
        String wangbanshendu = conditionMap.get("wangshangban#zwfw#eq#zwfw#S");
        String qctb = conditionMap.get("qctb#zwfw#eq#zwfw#S");
        String isznb = conditionMap.get("isznb#zwfw#eq#zwfw#S");
        String lpt = conditionMap.get("lpt#zwfw#eq#zwfw#S");
        String webapplytype = conditionMap.get("webapplytype#zwfw#in#zwfw#S");
        String taskid = conditionMap.get("task_id#zwfw#in#zwfw#S");
        String itemds = "";
        String sql1 = "";//查询有小项的大项
        if (StringUtil.isNotBlank(taskname)) {
            List<String> itemids = getItemidByTaskname(taskname, conditionMap.get("a.areacode#zwfw#eq#zwfw#S"), conditionMap.get("applyertype#zwfw#like#zwfw#S"));
            if (itemids != null && itemids.size() > 0) {
                for (String str : itemids) {
                    if (str.length() == 33) {
                        if ("11370800MB28559184337012100800003".equals(str)) {
                            itemds += "'" + str + "',";
                        } else {
                            if (isExistItemId(str.substring(0, str.length() - 2)) > 0) {
                                itemds += "'" + str.substring(0, str.length() - 2) + "',";
                            } else {
                                itemds += "'" + str + "',";
                            }
                        }

                    } else if (str.length() == 31) {
                        itemds += "'" + str + "',";
                    }
                }
                itemds = itemds.substring(0, itemds.length() - 1);
            }
            if (StringUtil.isNotBlank(itemds)) {
                conditionMap.remove("taskname#zwfw#like#zwfw#S");
                if (StringUtil.isNotBlank(webapplytype)) {
                    conditionMap.remove("webapplytype#zwfw#in#zwfw#S");

                    sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                            + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,"
                            + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a join audit_task_extension b on a.rowguid = b.taskguid join frame_ou_extendinfo o on a.ouguid = o.ouguid WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                            + "AND IS_ENABLE = 1  AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )  and length(o.areacode) = 6";


                } else {
                    sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                            + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                            + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a join frame_ou_extendinfo o on a.ouguid = o.ouguid WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                            + "AND IS_ENABLE = 1  AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )  and length(o.areacode) = 6";
                }
                conditionMap.put("ITEM_ID#zwfw#in#zwfw#S", itemds);
            } else {
                return "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                        + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                        + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a where 1=2";
            }
        } else {
            conditionMap.remove("webapplytype#zwfw#in#zwfw#S");
            if (StringUtil.isNotBlank(webapplytype)) {
                sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                        + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                        + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a join audit_task_extension b on a.rowguid = b.taskguid  join frame_ou_extendinfo o on a.ouguid = o.ouguid WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                        + "AND IS_ENABLE = 1  AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )  and length(o.areacode) = 6";
                if (!"1".equals(isznb)) {
                    if (StringUtil.isNotBlank(taskid)) {

                    } else {
                        sql1 += " AND LENGTH(item_id) = 31 ";
                    }

                }
            } else {
                sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                        + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                        + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a join frame_ou_extendinfo o on a.ouguid = o.ouguid WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                        + "AND IS_ENABLE = 1  AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) and length(o.areacode) = 6";
                if (!"1".equals(isznb)) {
                    if (StringUtil.isNotBlank(taskid)) {

                    } else {
                        sql1 += " AND LENGTH(item_id) = 31 ";
                    }
                }

            }

        }

        if ("1".equals(mashangban)) {
            sql1 += " and a.promise_day in ('0','1') and a.taskcode <> '' and a.businesstype = '1' ";
        }
        if ("1".equals(wangbanshendu)) {
            sql1 += " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%' or a.wangbanshendu like '%2%' or a.wangbanshendu like '%3%' or a.wangbanshendu like '%4%') and a.businesstype = '1' ";
        }
        if ("1".equals(qctb)) {
            sql1 += " and (a.wangbanshendu like '%5%' or a.wangbanshendu like '%6%' or a.wangbanshendu like '%7%') and a.businesstype = '1' ";
        }
        if ("0".equals(lpt)) {
            sql1 += " and (a.applyermin_count = '0' or a.wangbanshendu REGEXP '5|6|7') and a.taskcode <> '' and a.businesstype = '1' ";
        }
        if ("1".equals(isznb)) {
            sql1 += " and (a.is_mpmb = '1' or a.isbigshow = '1') ";
        }
        conditionMap.remove("mashangban#zwfw#eq#zwfw#S");
        conditionMap.remove("wangshangban#zwfw#eq#zwfw#S");
        conditionMap.remove("qctb#zwfw#eq#zwfw#S");
        conditionMap.remove("isznb#zwfw#eq#zwfw#S");
        conditionMap.remove("lpt#zwfw#eq#zwfw#S");
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "");
        //sql1 += " order by a.ordernum desc";
        //sql3 = "select * from ( " + sql1 + " ) c order by ordernum desc, item_id desc ";
        exampleList.addAll(tempresultList1);
        return sql1;
    }

    /**
     * 根据taskname获取父项itemid
     */
    public Integer isExistItemId(String itemId) {
        String sql = "select count(1) from audit_task where item_id = ? ";
        return CommonDao.getInstance().find(sql, Integer.class, itemId);
    }

    /**
     * 根据taskname获取父项itemid
     */
    public List<String> getItemidByTaskname(String taskname, String areacode, String applyertype) {
        String sql = "SELECT distinct item_id FROM audit_task a WHERE IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND taskname LIKE ? ";
        sql += " AND ifnull(IS_HISTORY, 0) = 0 AND areacode = ?  ";
        String tasknames = "%";
        List<String> splitStringList = chineseSplitFunction(taskname, 3);
        for (String split : splitStringList) {
            tasknames += split + "%";
        }

        if (StringUtil.isNotBlank(applyertype)) {
            sql += " AND applyertype LIKE ?";
        }
        return CommonDao.getInstance().findList(sql, String.class, tasknames, areacode, "%" + applyertype + "%");
    }

    public static List<String> chineseSplitFunction(String src, int bytes) {
        try {
            if (src == null) {
                return null;
            }
            List<String> splitList = new ArrayList<String>();
            int startIndex = 0;    //字符串截取起始位置
            int endIndex = bytes > src.length() ? src.length() : bytes;  //字符串截取结束位置
            while (startIndex < src.length()) {
                String subString = src.substring(startIndex, endIndex);
                //截取的字符串的字节长度大于需要截取的长度时，说明包含中文字符
                //在GBK编码中，一个中文字符占2个字节，UTF-8编码格式，一个中文字符占3个字节。
                while (subString.getBytes("GBK").length > bytes) {
                    --endIndex;
                    subString = src.substring(startIndex, endIndex);
                }
                splitList.add(src.substring(startIndex, endIndex));
                startIndex = endIndex;
                //判断结束位置时要与字符串长度比较(src.length())，之前与字符串的bytes长度比较了，导致越界异常。
                endIndex = (startIndex + bytes) > src.length() ?
                        src.length() : startIndex + bytes;

            }
            return splitList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取办件编号和密码查询办件rowguid
     */
    public String getProjectGuidByFlowsnPassword(String flowsn, String password) {
        String sql = "select rowguid from audit_project where flowsn = ?1 and searchpwd = ?2";
        return CommonDao.getInstance().queryString(sql, flowsn, password);
    }

    public AuditTask getAuditTaskByTaskid(String taskid) {
        String sql = " select a.* from Audit_Task a where a.TASK_ID = ? and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1  and istemplate='0' and IS_ENABLE = '1'";
        return CommonDao.getInstance().find(sql, AuditTask.class, taskid);
    }


    public List<CodeItems> getAreaList() {
        String sql = "select * from code_items where codeid = '1016279' and itemvalue <> '370800' and itemvalue <> '370892' and itemvalue <> '370891' and itemvalue <> '370890'";
        return commonDao.findList(sql, CodeItems.class);
    }


    public List<Record> selectOuByApplyertypeNew(String applyerType, String areaCode) {
        String sql = "select b.OUGUID,b.OUNAME,b.OUSHORTNAME FROM frame_ou b where b.OUGUID in ( " +
                "select a.ouguid from audit_task a where a.AREACODE=? and APPLYERTYPE LIKE ? AND " +
                "(a.IS_HISTORY IS NULL OR a.IS_HISTORY =''OR a.IS_HISTORY ='0') AND a.IS_EDITAFTERIMPORT=1 AND a.IS_ENABLE=1 and " +
                "LENGTH(a.item_id) = 31 and a.shenpilb!='15' and ifnull(a.iswtshow,'') in ('','1') GROUP BY a.OUGUID) " +
                "order by b.ordernumber desc,b.ouname desc";
        return commonDao.findList(sql, Record.class, areaCode, "%" + applyerType + "%");
    }

    public List<FrameOu> getOuListByAreacode(String areaCode) {
        String sql = "select a.* from frame_ou a,frame_ou_extendinfo b where a.ouguid = b.ouguid and b.areacode = ? ";
        return commonDao.findList(sql, FrameOu.class, areaCode);
    }

    public PageData<AuditTask> getLnrAuditTaskPageData(int firstResultTask, int parseInt) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        List<AuditTask> list = new ArrayList<>();
        String sql = "select rowguid,taskname,ouname,task_id from audit_task where IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND " +
                " shenpilb!='15' and islnr = '1' and ifnull(iswtshow,'') in ('','1') and areacode = '370800' and IS_EDITAFTERIMPORT = '1' " +
                " and ifnull(IS_HISTORY,'0') = '0' and applyertype like '%20%' and ISTEMPLATE = '0' and IS_ENABLE = '1' ";
        String sqlcount = "select count(1) from audit_task where IS_EDITAFTERIMPORT = 1 AND ISTEMPLATE = 0 AND IS_ENABLE = 1 AND " +
                " shenpilb!='15' and islnr = '1' and ifnull(iswtshow,'') in ('','1') and areacode ='370800' and IS_EDITAFTERIMPORT = '1' " +
                " and ifnull(IS_HISTORY,'0') = '0' and applyertype like '%20%' and ISTEMPLATE = '0' and IS_ENABLE = '1' ";
        int count = 0;
        list = commonDao.findList(sql, firstResultTask, parseInt, AuditTask.class);
        count = commonDao.queryInt(sqlcount);
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    /**
     * 获取所有满足条件的没有小项的大项以及满足条件的小项所对应的大项
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param firstResult  起始记录数
     * @param maxResults   最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public PageData<AuditTask> getNewAuditTaskPageData(Map<String, String> conditionMap, int firstResult, int maxResults,
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
            sql = buildGBQuerySqlNew(sqlCondition.getMap(), exampleList);
        }
        Object[] paramsobject = exampleList.toArray();

        List<AuditTask> auditTaskList = commonDao.findList(sql, firstResult, maxResults, AuditTask.class, paramsobject);
        pageData.setList(auditTaskList);
        pageData.setRowCount(commonDao.queryInt(sql.replace("a.*", "count(1)")
                .replaceAll("a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,a.transact_addr,a.Transact_time,a.turn_type", "count(1)")
                .replaceAll("a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type", "count(1)")
                .replaceAll("a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,a.transact_addr,a.Transact_time,a.turn_type", "count(1)")
                .replaceAll("a.rowguid,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,a.AREACODE,a.task_id,a.turn_type", "count(1)"), paramsobject));
        return pageData;
    }

    /**
     * 获取部门或搜索条件搜索出来的相关事项信息
     *
     * @param conditionMap 条件map
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    private String buildGBQuerySqlNew(Map<String, String> conditionMap, List<String> exampleList) {
        //查询有小项的大项所返回的参数列表
        List<String> tempresultList1 = new ArrayList<String>();
        String taskname = conditionMap.get("taskname#zwfw#like#zwfw#S");
        String itemds = "";
        String sql1 = "";//查询有小项的大项
        if (StringUtil.isNotBlank(taskname)) {
            List<String> itemids = getItemidByTaskname(taskname, conditionMap.get("a.areacode#zwfw#eq#zwfw#S"), conditionMap.get("applyertype#zwfw#like#zwfw#S"));
            if (itemids != null && itemids.size() > 0) {
                for (String str : itemids) {
                    if (str.length() == 33) {
                        if ("11370800MB28559184337012100800003".equals(str)) {
                            itemds += "'" + str + "',";
                        } else {
                            if (isExistItemId(str.substring(0, str.length() - 2)) > 0) {
                                itemds += "'" + str.substring(0, str.length() - 2) + "',";
                            } else {
                                itemds += "'" + str + "',";
                            }
                        }

                    } else if (str.length() == 31) {
                        itemds += "'" + str + "',";
                    }
                }
                itemds = itemds.substring(0, itemds.length() - 1);
            }
            if (StringUtil.isNotBlank(itemds)) {
                conditionMap.remove("taskname#zwfw#like#zwfw#S");

                sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                        + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                        + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a join frame_ou_extendinfo o on a.ouguid = o.ouguid WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                        + "AND IS_ENABLE = 1  AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL )  and length(o.areacode) = 6";

                conditionMap.put("ITEM_ID#zwfw#in#zwfw#S", itemds);
            } else {
                return "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                        + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                        + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a where 1=2";
            }
        } else {
            conditionMap.remove("webapplytype#zwfw#in#zwfw#S");
            sql1 = "SELECT a.ywztcode,a.wzds_rowguid,a.is_mpmb,a.isbigshow,a.rowguid,a.if_entrust,a.applyertype,a.item_id,a.taskname,a.ouname,a.ordernum,a.is_enable,a.ouguid,a.PROCESSGUID,a.TYPE,"
                    + "a.AREACODE,a.task_id,a.promise_day,a.anticipate_day,a.charge_flag,a.shenpilb,a.link_tel,a.supervise_tel,a.JBJMODE,a.BY_LAW,a.turn_type,"
                    + "a.transact_addr,a.Transact_time,a.turn_type FROM audit_task a join frame_ou_extendinfo o on a.ouguid = o.ouguid WHERE IS_EDITAFTERIMPORT = 1  AND ISTEMPLATE = 0 "
                    + "AND IS_ENABLE = 1  AND ( IS_HISTORY = 0 OR IS_HISTORY IS NULL ) and length(o.areacode) = 6";
        }

        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        sql1 += sqlManageUtil.buildSql(conditionMap, tempresultList1).replace(" where 1=1", "");
        exampleList.addAll(tempresultList1);
        return sql1;
    }

}
