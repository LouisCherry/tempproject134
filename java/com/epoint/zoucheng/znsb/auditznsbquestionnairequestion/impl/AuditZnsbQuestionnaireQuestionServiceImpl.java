package com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.IAuditZnsbQuestionnaireQuestionService;
import com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity.AuditZnsbQuestionnaireQuestion;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.alibaba.dubbo.config.annotation.Service;
/**
 * 试卷和题目的关系表对应的后台service实现类
 * 
 * @author TJX
 * @version [版本号, 2022-07-08 16:34:55]
 */
@Component
@Service
public class AuditZnsbQuestionnaireQuestionServiceImpl implements IAuditZnsbQuestionnaireQuestionService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbQuestionnaireQuestion record) {
        return new AuditZnsbQuestionnaireQuestionService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditZnsbQuestionnaireQuestionService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestionnaireQuestion record) {
        return new AuditZnsbQuestionnaireQuestionService().update(record);
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
       return new AuditZnsbQuestionnaireQuestionService().find(primaryKey);
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
    public AuditZnsbQuestionnaireQuestion find(String sql, Object... args) {
        return new AuditZnsbQuestionnaireQuestionService().find(sql,args);
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
       return new AuditZnsbQuestionnaireQuestionService().findList(sql,args);
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
       return new AuditZnsbQuestionnaireQuestionService().findList(sql,pageNumber,pageSize,args);
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
     @Override
    public Integer countAuditZnsbQuestionnaireQuestion(String sql, Object... args){
        return new AuditZnsbQuestionnaireQuestionService().countAuditZnsbQuestionnaireQuestion(sql, args);
    }

    @Override
    public int getCountByQuestiontype(String questionnaireguid, String questiontype) {
        return new AuditZnsbQuestionnaireQuestionService().getCountByQuestiontype(questionnaireguid, questiontype);
    }

    @Override
    public List<AuditZnsbQuestionnaireQuestion> findListByQuestionNaireGuid(String questionnaireguid) {
        return new AuditZnsbQuestionnaireQuestionService().findListByQuestionNaireGuid(questionnaireguid);
    }

    @Override
    public int deleteByQuestionNaireGuid(String questionnaireguid) {
        return new AuditZnsbQuestionnaireQuestionService().deleteByQuestionNaireGuid(questionnaireguid);
    }

    @Override
    public List<AuditZnsbQuestioninfo> findQuestionInfo(String questionnairerowguid) {
        return new AuditZnsbQuestionnaireQuestionService().findQuestionInfo(questionnairerowguid);
    }

}
