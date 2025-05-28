package com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.core.grammar.Record;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity.AuditZnsbQuestionnaireQuestion;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.core.BaseEntity;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.dao.CommonDao;

/**
 * 试卷和题目的关系表对应的后台service
 * 
 * @author TJX
 * @version [版本号, 2022-07-08 16:34:55]
 */
public class AuditZnsbQuestionnaireQuestionService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditZnsbQuestionnaireQuestionService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbQuestionnaireQuestion record) {
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
        T t = baseDao.find(AuditZnsbQuestionnaireQuestion.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestionnaireQuestion record) {
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
    public AuditZnsbQuestionnaireQuestion find(Object primaryKey) {
        return baseDao.find(AuditZnsbQuestionnaireQuestion.class, primaryKey);
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
    public AuditZnsbQuestionnaireQuestion find(String sql,  Object... args) {
        return baseDao.find(sql, AuditZnsbQuestionnaireQuestion.class, args);
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
    public List<AuditZnsbQuestionnaireQuestion> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditZnsbQuestionnaireQuestion.class, args);
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
    public List<AuditZnsbQuestionnaireQuestion> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditZnsbQuestionnaireQuestion.class, args);
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
    public Integer countAuditZnsbQuestionnaireQuestion(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    public int getCountByQuestiontype(String questionnaireguid, String questiontype) {
        String sql = "select count(*) from audit_znsb_questionnaire_question a left join audit_znsb_questioninfo b on a.question=b.rowguid where a.questionnaireguid=? and b.questiontype=?";
        return baseDao.queryInt(sql, questionnaireguid, questiontype);
    }
    public List<AuditZnsbQuestionnaireQuestion> findListByQuestionNaireGuid(String questionnaireguid) {
        String sql = "select * from audit_znsb_questionnaire_question where questionnaireguid=?";
        return baseDao.findList(sql, AuditZnsbQuestionnaireQuestion.class, questionnaireguid);
    }
    public int deleteByQuestionNaireGuid(String questionnaireguid) {
        String sql = "delete from audit_znsb_questionnaire_question where questionnaireguid=?";
        return baseDao.execute(sql, questionnaireguid);
    }
    public List<AuditZnsbQuestioninfo> findQuestionInfo(String questionnairerowguid) {
        String sql = "select b.rowguid,b.question,b.questiontype from audit_znsb_questionnaire_question a left join audit_znsb_questioninfo b on a.question=b.rowguid where a.questionnaireguid=? order by questiontype";
        return baseDao.findList(sql, AuditZnsbQuestioninfo.class, questionnairerowguid);
    }
}
