package com.epoint.zwdt.zwdtrest.task.keyword.impl;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.zwdtrest.task.keyword.api.entity.AuditTaskKeyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 事项关键字关系表对应的后台service
 *
 * @author yangyi
 * @version [版本号, 2022-06-17 10:47:41]
 */
public class AuditTaskKeywordService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditTaskKeywordService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskKeyword record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditTaskKeyword.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskKeyword record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditTaskKeyword find(Object primaryKey) {
        return baseDao.find(AuditTaskKeyword.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditTaskKeyword find(String sql, Object... args) {
        return baseDao.find(sql, AuditTaskKeyword.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskKeyword> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTaskKeyword.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditTaskKeyword> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditTaskKeyword.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditTaskKeyword(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }


    public void deleteByTaskid(String taskid) {
        String sql = "delete from audit_task_keyword where taskid = ?";
        baseDao.execute(sql, taskid);
    }

    public List<String> findListByKeyWord(String keyWord, String areacode) {
        String sql = "select taskid from audit_task_keyword where keywordname like ? and areacode = ? ";
        return baseDao.findList(sql, String.class, "%" + keyWord + "%", areacode);
    }

    public List<String> findListBytaskname(String keyWord, String areaCode) {
        String sql = "select task_id from audit_task where IS_EDITAFTERIMPORT = '1' and IS_ENABLE = '1' " +
                " and IFNULL(IS_HISTORY,'0') = '0' and taskname like ? and areacode = ? ";
        return baseDao.findList(sql, String.class, "%" + keyWord + "%", areaCode);
    }

    public PageData<AuditTask> getTaskListByTaskName(Record record, int pageInt, int pageSize, boolean fromKeyWord) {
        PageData<AuditTask> pageData = new PageData<>();
        List<AuditTask> list = new ArrayList<>();
        int count = 0;
        List<String> params = new ArrayList<>();
        String sql = "select taskname,areacode,count(AREACODE = '370800' or null) as iscity from audit_task where ISTEMPLATE = '0' and IS_EDITAFTERIMPORT = '1' and IS_ENABLE = '1' and ifnull(IS_HISTORY,'0') = '0' " +
                "and ifnull(iswtshow,'') in ('','1')  and shenpilb!='15' ";
        String sqlcount = "select count(1) from (select TaskName from audit_task where ISTEMPLATE = '0' and IS_EDITAFTERIMPORT = '1' and IS_ENABLE = '1' and ifnull(IS_HISTORY,'0') = '0' " +
                "and ifnull(iswtshow,'') in ('','1')  and shenpilb!='15' ";
        if (StringUtil.isNotBlank(record.getStr("applyertype"))) {
            sql += " and applyertype like ? ";
            sqlcount += " and applyertype like ? ";
            params.add("%" + record.getStr("applyertype") + "%");
        }

        //说明有关键词
        if (fromKeyWord) {
            sql += " and (taskname like ? or task_id in (?)) ";
            sqlcount += " and (taskname like ? or task_id in (?)) ";
            params.add("%" + record.getStr("taskname") + "%");
            List<String> taskids = Arrays.asList(record.getStr("taskids").split(","));
            StringBuilder taskstr = new StringBuilder();
            for (int i = 0; i < taskids.size(); i++) {
                if (i == taskids.size() - 1) {
                    taskstr.append("'").append(taskids.get(0)).append("'");
                } else {
                    taskstr.append("'").append(taskids.get(0)).append("',");
                }
            }
            params.add("%" + record.getStr("taskname") + "%");
            params.add(taskstr.toString());
        } else {
            sql += " and taskname like ? ";
            sqlcount += " and taskname like ? ";
            params.add("%" + record.getStr("taskname") + "%");
        }

        sql += " group by taskname ";
        sqlcount += " group by taskname) a ";
        list = baseDao.findList(sql, pageInt, pageSize, AuditTask.class, params.toArray());
        count = baseDao.queryInt(sqlcount, params.toArray());
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    public AuditTask getTaskcityByTaskname(String taskname) {
        String sql = "select rowguid,task_id,taskname,shenpilb,item_id,ouname,type,link_tel,anticipate_day,ANTICIPATE_TYPE,promise_day,promise_type " +
                "from audit_task where ISTEMPLATE = '0' and IS_EDITAFTERIMPORT = '1' and IS_ENABLE = '1' and ifnull(IS_HISTORY,'0') = '0' " +
                "and ifnull(iswtshow,'') in ('','1')  and shenpilb!='15' and taskname = ? and areacode = '370800' ";
        return baseDao.find(sql, AuditTask.class, taskname);
    }

    public boolean Ishighlight(String itemValue, String taskname) {
        boolean ishighlight = false;
        //先查区一级（包含乡镇事项）
        String sql1 = "select count(1) from audit_task where ISTEMPLATE = '0' and IS_EDITAFTERIMPORT = '1' and IS_ENABLE = '1' and ifnull(IS_HISTORY,'0') = '0' " +
                "and ifnull(iswtshow,'') in ('','1')  and shenpilb!='15' and taskname = ? and areacode = ? ";
        int count = baseDao.queryInt(sql1, taskname, itemValue);
        if (count > 0) {
            ishighlight = true;
        }
        return ishighlight;
    }


    public List<AuditTask> getTaskDistrictByTaskname(String taskname, String areacode) {
        String sql = "select a.rowguid,a.task_id,a.taskname,a.ouname,a.APPLYERTYPE from audit_task a inner join frame_ou_extendinfo o on a.ouguid = o.ouguid " +
                " and a.ISTEMPLATE = '0' and a.IS_EDITAFTERIMPORT = '1' and a.IS_ENABLE = '1' and ifnull(a.IS_HISTORY,'0') = '0' and " +
                " ifnull(a.iswtshow,'') in ('','1')  and a.shenpilb!='15' and a.taskname = ? and a.areacode = ? and length(o.AREACODE) = 6 ";
        return baseDao.findList(sql, AuditTask.class, taskname, areacode);
    }
    public List<AuditTask> getTaskByTaskname(String taskname, String areacode) {
        String sql = "select a.rowguid,a.promise_type,a.promise_day,a.ANTICIPATE_TYPE,a.anticipate_day,a.task_id,a.link_tel,a.item_id,a.shenpilb,a.taskname,a.ouname,a.APPLYERTYPE from audit_task a inner join frame_ou_extendinfo o on a.ouguid = o.ouguid " +
                " and a.ISTEMPLATE = '0' and a.IS_EDITAFTERIMPORT = '1' and a.IS_ENABLE = '1' and ifnull(a.IS_HISTORY,'0') = '0' and " +
                " ifnull(a.iswtshow,'') in ('','1')  and a.shenpilb!='15' and a.taskname = ?  and length(o.AREACODE) = 6 ";
        if(StringUtil.isNotBlank(areacode)){
            sql+="and a.areacode in ("+areacode+")";
        }
        return baseDao.findList(sql, AuditTask.class, taskname);
    }
    public List<AuditTask> getTaskTownByTaskname(String taskname, String areacode) {
        String sql = "SELECT a.rowguid,a.task_id,a.taskname,a.OUNAME,a.APPLYERTYPE,d.areacode from audit_task a JOIN audit_task_delegate d on a.Task_id = d.TASKID WHERE " +
                " ifnull(a.iswtshow,'') in ('','1')  and a.shenpilb!='15' and a.ISTEMPLATE = '0' and a.IS_EDITAFTERIMPORT = '1'" +
                " and a.IS_ENABLE = '1' and ifnull(a.IS_HISTORY,'0') = '0' and a.taskname = ? " +
                " and d.areacode = ? and d.status = 1 AND d.ISALLOWACCEPT = 1";
        return baseDao.findList(sql, AuditTask.class, taskname, areacode);
    }

    public List<AuditOrgaArea> getOrgaAreaListByAreacodeAndType(String areacode, String s) {
        String sql = "";
        if ("1".equals(s)) {
            sql = "select XiaQuCode,XiaQuName from audit_orga_area where XiaQuCode like ? and length(XiaQuCode) = '9' ";
        } else if ("2".equals(s)) {
            sql = "select XiaQuCode,XiaQuName from audit_orga_area where XiaQuCode like ? and length(XiaQuCode) = '12' ";
        }
        return baseDao.findList(sql, AuditOrgaArea.class, areacode + "%");
    }

    public boolean IshighlightTown(String xiaqucode, String taskname) {
        boolean ishighlight = false;
        String sql = "SELECT count(1) from audit_task a JOIN audit_task_delegate d on a.Task_id = d.TASKID WHERE " +
                " ifnull(a.iswtshow,'') in ('','1')  and a.shenpilb!='15' and a.ISTEMPLATE = '0' and a.IS_EDITAFTERIMPORT = '1'" +
                " and a.IS_ENABLE = '1' and ifnull(a.IS_HISTORY,'0') = '0' and a.taskname = ? " +
                " and d.areacode = ? and d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        int count = baseDao.queryInt(sql, taskname, xiaqucode);
        if (count > 0) {
            ishighlight = true;
        }
        return ishighlight;
    }
    public boolean IsTownTask(String xiaqucode, String taskname) {
        boolean ishighlight = false;
        String sql = "SELECT count(1) from audit_task a JOIN audit_task_delegate d on a.Task_id = d.TASKID WHERE " +
                " ifnull(a.iswtshow,'') in ('','1')  and a.shenpilb!='15' and a.ISTEMPLATE = '0' and a.IS_EDITAFTERIMPORT = '1'" +
                " and a.IS_ENABLE = '1' and ifnull(a.IS_HISTORY,'0') = '0' and a.taskname = ? " +
                " and d.areacode like ? and d.status = 1 AND d.ISALLOWACCEPT = 1 ";
        int count = baseDao.queryInt(sql, taskname, xiaqucode+ "%");
        if (count > 0) {
            ishighlight = true;
        }
        return ishighlight;
    }
    public List<Record> highlightareacode(String tasknames) {
        boolean ishighlight = false;
        //先查区一级（包含乡镇事项）
        String sql1 = "select distinct taskname,areacode from audit_task where ISTEMPLATE = '0' and IS_EDITAFTERIMPORT = '1' and IS_ENABLE = '1' and ifnull(IS_HISTORY,'0') = '0' " +
                "and ifnull(iswtshow,'') in ('','1')  and shenpilb!='15' and taskname in ('"+tasknames+"')";
        List<Record> records = baseDao.findList(sql1,Record.class);
        return records;
    }
}
