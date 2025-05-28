package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain.AuditZnsbQuestionnaireresult;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.inter.IZCAuditZnsbQuestionnaireresultService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.service.ZCAuditZnsbQuestionnaireresultService;

/**
 * 问卷结果对应的后台service实现类
 * 
 * @author LQ
 * @version [版本号, 2021-08-05 15:25:58]
 */
@Component
@Service
public class ZCAuditZnsbQuestionnaireresultServiceImpl implements IZCAuditZnsbQuestionnaireresultService
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
    public int insert(AuditZnsbQuestionnaireresult record) {
        return new ZCAuditZnsbQuestionnaireresultService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZCAuditZnsbQuestionnaireresultService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestionnaireresult record) {
        return new ZCAuditZnsbQuestionnaireresultService().update(record);
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
        return new ZCAuditZnsbQuestionnaireresultService().find(primaryKey);
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
        return new ZCAuditZnsbQuestionnaireresultService().find(sql, args);
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
        return new ZCAuditZnsbQuestionnaireresultService().findList(sql, args);
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
        return new ZCAuditZnsbQuestionnaireresultService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditZnsbQuestionnaireresult(String sql, Object... args) {
        return new ZCAuditZnsbQuestionnaireresultService().countAuditZnsbQuestionnaireresult(sql, args);
    }

    @Override
    public AuditCommonResult<PageData<AuditZnsbQuestionnaireresult>> getAuditZnsbQuestionnaireResult(
            Map<String, String> map, int first, int pageSize, String sortField, String sortOrder) {
        AuditCommonResult<PageData<AuditZnsbQuestionnaireresult>> result = new AuditCommonResult<>();
        AuditQueueBasicService<AuditZnsbQuestionnaireresult> service = new AuditQueueBasicService<AuditZnsbQuestionnaireresult>();
        try {
            PageData<AuditZnsbQuestionnaireresult> pagedata = service
                    .getRecordPageData(AuditZnsbQuestionnaireresult.class, map, first, pageSize, sortField, sortOrder);
            result.setResult(pagedata);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public List<Record> getAllList(String name, String centerguid, int first, int pageSize) {
        return new ZCAuditZnsbQuestionnaireresultService().getAllList(name, centerguid, first, pageSize);
    }

    @Override
    public int deleteByName(String name) {
        return new ZCAuditZnsbQuestionnaireresultService().deleteByName(name);
    }

    @Override
    public Integer getAllListCount(String questionnairename) {
        return new ZCAuditZnsbQuestionnaireresultService().getAllListCount(questionnairename);
    }

}
