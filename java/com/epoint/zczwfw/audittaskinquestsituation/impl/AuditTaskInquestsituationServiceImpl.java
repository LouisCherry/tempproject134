package com.epoint.zczwfw.audittaskinquestsituation.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.zczwfw.audittaskinquestsituation.api.IAuditTaskInquestsituationService;
import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;

/**
 * 勘验事项情形表对应的后台service实现类
 * 
 * @author yrchan
 * @version [版本号, 2022-04-18 09:53:53]
 */
@Component
@Service
public class AuditTaskInquestsituationServiceImpl implements IAuditTaskInquestsituationService
{
    private static final long serialVersionUID = -2589101059925436997L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskInquestsituation record) {
        return new AuditTaskInquestsituationService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditTaskInquestsituationService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskInquestsituation record) {
        return new AuditTaskInquestsituationService().update(record);
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
    public AuditTaskInquestsituation find(Object primaryKey) {
        return new AuditTaskInquestsituationService().find(primaryKey);
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
    public AuditTaskInquestsituation find(String sql, Object... args) {
        return new AuditTaskInquestsituationService().find(sql, args);
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
    public List<AuditTaskInquestsituation> findList(String sql, Object... args) {
        return new AuditTaskInquestsituationService().findList(sql, args);
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
    public List<AuditTaskInquestsituation> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditTaskInquestsituationService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditTaskInquestsituation(String sql, Object... args) {
        return new AuditTaskInquestsituationService().countAuditTaskInquestsituation(sql, args);
    }

    /**
     * 
     * 根据taskGuid删除勘验事项情形数据
     * 
     * @param taskGuid
     *            事项唯一标识
     */
    @Override
    public void deleteByTaskGuid(String taskGuid) {
        new AuditTaskInquestsituationService().deleteByTaskGuid(taskGuid);
    }

    /**
     * 
     * 根据taskGuid查询勘验事项情形数据
     * 
     * @param fields
     *            查询的字段
     * @param taskGuid
     *            事项唯一标识
     * @return
     */
    @Override
    public List<AuditTaskInquestsituation> listAuditTaskInquestsituationByTaskGuid(String fields, String taskGuid) {
        return new AuditTaskInquestsituationService().listAuditTaskInquestsituationByTaskGuid(fields, taskGuid);
    }

    /**
     * 
     * 把旧的taskguid,替换成新的taskguid
     * 
     * @param oldTaskGuid
     *            旧的事项标识
     * @param newtaskGuid
     *            新的事项标识
     */
    @Override
    public void updateNewTaskGuidByOldTaskGuid(String oldTaskGuid, String newtaskGuid) {
        new AuditTaskInquestsituationService().updateNewTaskGuidByOldTaskGuid(oldTaskGuid, newtaskGuid);
    }
}
