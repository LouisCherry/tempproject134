package com.epoint.wechat.task.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.grammar.Record;
import com.epoint.wechat.task.api.IWeiChatTaskService;

/**
 * 快递信息表对应的后台service实现类
 * 
 * @author Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
@Component
@Service
public class WeiChatTaskServiceImpl implements IWeiChatTaskService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int insert(AuditTaskHottask record) {
        return new WeiChatTaskService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int deleteByGuid(String guid) {
        return new WeiChatTaskService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public int update(AuditTaskHottask record) {
        return new WeiChatTaskService().update(record);
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
    @Override
    public AuditTaskHottask find(Object primaryKey) {
        return new WeiChatTaskService().find(primaryKey);
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
    @Override
    public AuditTaskHottask find(String sql, Object... args) {
        return new WeiChatTaskService().find(args);
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
    @Override
    public List<AuditTaskHottask> findList(String sql, Object... args) {
        return new WeiChatTaskService().findList(sql, args);
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
    @Override
    public List<AuditTaskHottask> findListByPage(SqlConditionUtil sqlConditionUtil, int pageNumber, int pageSize,
            Object... args) {
        return new WeiChatTaskService().findListByPage(sqlConditionUtil, pageNumber, pageSize, args);
    }

    @Override
    public int getTotalNum(SqlConditionUtil sqlConditionUtil) {
        return new WeiChatTaskService().getTotalNum(sqlConditionUtil);
    }

    @Override
    public String getQrCodeUrl(String taskid) {
        return new WeiChatTaskService().getQrCodeUrl(taskid);
    }

    @Override
    public String getVideoDemo(String taskid) {
        return new WeiChatTaskService().getVideoDemo(taskid);
    }

    @Override
    public Record findTaskIconByTaskid(String taskid) {
        return new WeiChatTaskService().findTaskIconByTaskid(taskid);
    }

    @Override
    public AuditTask getTaskByTaskid(String taskid) {
        return new WeiChatTaskService().getTaskByTaskid(taskid);
    }

    @Override
    public void updateProjectMaterialStatus(String projectGuid) {
        new WeiChatTaskService().updateProjectMaterialStatus(projectGuid);
    }
}
