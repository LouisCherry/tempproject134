package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.service;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;

/**
 * 问卷调查-问卷对应的后台service
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
public class ZCAuditZnsbQuestionnaireService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZCAuditZnsbQuestionnaireService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbQuestionnaire record) {
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
        T t = baseDao.find(AuditZnsbQuestionnaire.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestionnaire record) {
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
    public AuditZnsbQuestionnaire find(Object primaryKey) {
        return baseDao.find(AuditZnsbQuestionnaire.class, primaryKey);
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
    public AuditZnsbQuestionnaire find(String sql, Object... args) {
        return baseDao.find(sql, AuditZnsbQuestionnaire.class, args);
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
    public List<AuditZnsbQuestionnaire> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditZnsbQuestionnaire.class, args);
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
    public List<AuditZnsbQuestionnaire> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditZnsbQuestionnaire.class, args);
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
    public Integer countAuditZnsbQuestionnaire(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public Integer getCountByName(String questionnairename) {
        String sql = "select count(0) from audit_znsb_questionnaire where questionnairename = ?";
        return baseDao.queryInt(sql, questionnairename);
    }
}
