package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain.AuditZnsbQuestiondetail;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.inter.IZCAuditZnsbQuestiondetailService;
import com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.service.ZCAuditZnsbQuestiondetailService;

/**
 * 问卷调查-问题详情对应的后台service实现类
 * 
 * @author LQ
 * @version [版本号, 2021-08-04 10:35:10]
 */
@Component
@Service
public class ZCAuditZnsbQuestiondetailServiceImpl implements IZCAuditZnsbQuestiondetailService
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
    public int insert(AuditZnsbQuestiondetail record) {
        return new ZCAuditZnsbQuestiondetailService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new ZCAuditZnsbQuestiondetailService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditZnsbQuestiondetail record) {
        return new ZCAuditZnsbQuestiondetailService().update(record);
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
    public AuditZnsbQuestiondetail find(Object primaryKey) {
        return new ZCAuditZnsbQuestiondetailService().find(primaryKey);
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
    public AuditZnsbQuestiondetail find(String sql, Object... args) {
        return new ZCAuditZnsbQuestiondetailService().find(sql, args);
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
    public List<AuditZnsbQuestiondetail> findList(String sql, Object... args) {
        return new ZCAuditZnsbQuestiondetailService().findList(sql, args);
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
    public List<AuditZnsbQuestiondetail> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new ZCAuditZnsbQuestiondetailService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditZnsbQuestiondetail(String sql, Object... args) {
        return new ZCAuditZnsbQuestiondetailService().countAuditZnsbQuestiondetail(sql, args);
    }

    @Override
    public void deleteByQuestionGuid(String questionguid) {
        AuditQueueBasicService<AuditZnsbQuestiondetail> service = new AuditQueueBasicService<AuditZnsbQuestiondetail>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            service.deleteRecords(AuditZnsbQuestiondetail.class, questionguid, "questionguid");
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.toString());
        }

    }

    @Override
    public AuditCommonResult<PageData<AuditZnsbQuestiondetail>> findListByQuestionInfoguid(
            Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<AuditZnsbQuestiondetail> auditqueueService = new AuditQueueBasicService<AuditZnsbQuestiondetail>();
        AuditCommonResult<PageData<AuditZnsbQuestiondetail>> result = new AuditCommonResult<>();
        try {
            PageData<AuditZnsbQuestiondetail> page = auditqueueService.getRecordPageData(AuditZnsbQuestiondetail.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(page);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public Integer getCountByGuid(String rowguid) {
        return new ZCAuditZnsbQuestiondetailService().getCountByGuid(rowguid);
    }

    @Override
    public Integer getCountByName(String name, String questionguid, String rowguid) {
        return new ZCAuditZnsbQuestiondetailService().getCountByName(name, questionguid, rowguid);
    }

    @Override
    public List<AuditZnsbQuestiondetail> findListByQuestionGuid(String questionguid) {
        return new ZCAuditZnsbQuestiondetailService().findListByQuestionGuid(questionguid);
    }

    @Override
    public List<String> findRightListByQuestionGuid(String questionguid) {
        return new ZCAuditZnsbQuestiondetailService().findRightListByQuestionGuid(questionguid);
    }

}
