package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.service;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain.AuditZnsbQuestionnaireresult;

/**
 * 问卷结果对应的后台service
 * 
 * @author LQ
 * @version [版本号, 2021-08-05 15:25:58]
 */
public class ZCAuditZnsbQuestionnaireresultService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZCAuditZnsbQuestionnaireresultService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbQuestionnaireresult record) {
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
        T t = baseDao.find(AuditZnsbQuestionnaireresult.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestionnaireresult record) {
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
    public AuditZnsbQuestionnaireresult find(Object primaryKey) {
        return baseDao.find(AuditZnsbQuestionnaireresult.class, primaryKey);
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
    public AuditZnsbQuestionnaireresult find(String sql, Object... args) {
        return baseDao.find(sql, AuditZnsbQuestionnaireresult.class, args);
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
    public List<AuditZnsbQuestionnaireresult> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditZnsbQuestionnaireresult.class, args);
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
    public List<AuditZnsbQuestionnaireresult> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditZnsbQuestionnaireresult.class, args);
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
    public Integer countAuditZnsbQuestionnaireresult(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public List<Record> getAllList(String name, String centerguid, int first, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                "select a.questionnairename oldName,b.questionnairename newName,a.OperateDate ,a.rowguid ,a.questionnaireguid ,b.createtime createtime,count(0) count from audit_znsb_questionnaireresult a left join audit_znsb_questionnaire b on a.questionnaireguid = b.rowguid  where a.centerguid = ? ");
        if (StringUtil.isNotBlank(name)) {
            if (name.contains("_") || name.contains("%")) {
                name = name.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
            }
            sql.append(" and b.questionnairename like " + "'%" + name + "%'");
        }
        sql.append(" group by a.questionnaireguid ");
        return baseDao.findList(sql.toString(), first, pageSize, Record.class, centerguid);
    }

    public int deleteByName(String name) {
        String sql = "delete from audit_znsb_questionnaireresult where questionnaireguid = ? ";
        return baseDao.execute(sql, name);
    }

    public Integer getAllListCount(String name) {
        StringBuffer sql = new StringBuffer();
        sql.append(
                "select count(0) from ( select b.questionnairename ,a.OperateDate ,a.rowguid ,a.questionnaireguid ,b.createtime createtime,count(0) count from audit_znsb_questionnaireresult a left join audit_znsb_questionnaire b on a.questionnaireguid = b.rowguid ");
        if (StringUtil.isNotBlank(name)) {
            if (name.contains("_") || name.contains("%")) {
                name = name.replaceAll("%", "\\\\%").replaceAll("_", "\\\\_");
            }
            sql.append("where b.questionnairename like " + "'%" + name + "%'");
        }
        sql.append(" group by a.questionnaireguid ) c ");
        return baseDao.queryInt(sql.toString());
    }
}
