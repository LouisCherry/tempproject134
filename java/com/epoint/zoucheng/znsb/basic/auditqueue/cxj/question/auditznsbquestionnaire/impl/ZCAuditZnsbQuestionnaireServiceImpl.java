package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain.AuditZnsbQuestionnaire;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.inter.IZCAuditZnsbQuestionnaireService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.service.ZCAuditZnsbQuestionnaireService;

/**
 * 问卷调查-问卷对应的后台service实现类
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
@Component
@Service
public class ZCAuditZnsbQuestionnaireServiceImpl implements IZCAuditZnsbQuestionnaireService
{

    private static final long serialVersionUID = 1L;
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditZnsbQuestionnaire record) {
        return new ZCAuditZnsbQuestionnaireService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZCAuditZnsbQuestionnaireService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestionnaire record) {
        return new ZCAuditZnsbQuestionnaireService().update(record);
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
        return new ZCAuditZnsbQuestionnaireService().find(primaryKey);
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
        return new ZCAuditZnsbQuestionnaireService().find(sql, args);
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
        return new ZCAuditZnsbQuestionnaireService().findList(sql, args);
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
        return new ZCAuditZnsbQuestionnaireService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditZnsbQuestionnaire(String sql, Object... args) {
        return new ZCAuditZnsbQuestionnaireService().countAuditZnsbQuestionnaire(sql, args);
    }

    @Override
    public AuditCommonResult<PageData<AuditZnsbQuestionnaire>> getQuestionnaireListPageData(
            Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditZnsbQuestionnaire>> result = new AuditCommonResult<>();
        AuditQueueBasicService<AuditZnsbQuestionnaire> service = new AuditQueueBasicService<AuditZnsbQuestionnaire>();
        try {
            PageData<AuditZnsbQuestionnaire> pagedata = service.getRecordPageData(AuditZnsbQuestionnaire.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(pagedata);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public Integer getCountByName(String questionnairename) {
        return new ZCAuditZnsbQuestionnaireService().getCountByName(questionnairename);
    }

}
