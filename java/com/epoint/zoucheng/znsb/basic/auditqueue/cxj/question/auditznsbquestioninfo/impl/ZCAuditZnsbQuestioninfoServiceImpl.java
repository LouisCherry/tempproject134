package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain.AuditZnsbQuestioninfo;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.inter.IZCAuditZnsbQuestioninfoService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.service.ZCAuditZnsbQuestioninfoService;

/**
 * 问卷调查-问题对应的后台service实现类
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:17]
 */
@Component
@Service
public class ZCAuditZnsbQuestioninfoServiceImpl implements IZCAuditZnsbQuestioninfoService
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
    public int insert(AuditZnsbQuestioninfo record) {
        return new ZCAuditZnsbQuestioninfoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZCAuditZnsbQuestioninfoService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestioninfo record) {
        return new ZCAuditZnsbQuestioninfoService().update(record);
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
    public AuditZnsbQuestioninfo find(Object primaryKey) {
        return new ZCAuditZnsbQuestioninfoService().find(primaryKey);
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
    public AuditZnsbQuestioninfo find(String sql, Object... args) {
        return new ZCAuditZnsbQuestioninfoService().find(sql, args);
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
    public List<AuditZnsbQuestioninfo> findList(String sql, Object... args) {
        return new ZCAuditZnsbQuestioninfoService().findList(sql, args);
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
    public List<AuditZnsbQuestioninfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ZCAuditZnsbQuestioninfoService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditZnsbQuestioninfo(String sql, Object... args) {
        return new ZCAuditZnsbQuestioninfoService().countAuditZnsbQuestioninfo(sql, args);
    }

    @Override
    public AuditCommonResult<PageData<AuditZnsbQuestioninfo>> findListByQuestionnairerowguid(
            Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<AuditZnsbQuestioninfo> auditqueueService = new AuditQueueBasicService<AuditZnsbQuestioninfo>();
        AuditCommonResult<PageData<AuditZnsbQuestioninfo>> result = new AuditCommonResult<PageData<AuditZnsbQuestioninfo>>();
        try {
            PageData<AuditZnsbQuestioninfo> page = auditqueueService.getRecordPageData(AuditZnsbQuestioninfo.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(page);
        }
        catch (Exception e) {
            log.error(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public Integer getCountByName(String question, String questionnaireguid) {
        return new ZCAuditZnsbQuestioninfoService().getCountByName(question, questionnaireguid);
    }

    @Override
    public List<AuditZnsbQuestioninfo> findListByQuestion(String question, String centerguid) {
        return new ZCAuditZnsbQuestioninfoService().findListByQuestion(question, centerguid);
    }

    @Override
    public List<AuditZnsbQuestioninfo> findListByQuestionType(String questiontype, String centerguid) {
        return new ZCAuditZnsbQuestioninfoService().findListByQuestionType(questiontype, centerguid);
    }
}
