package com.epoint.zczwfw.audittaskinquestsituation.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;

/**
 * 勘验事项情形表对应的后台service接口
 * 
 * @author yrchan
 * @version [版本号, 2022-04-18 09:53:53]
 */
public interface IAuditTaskInquestsituationService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskInquestsituation record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskInquestsituation record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public AuditTaskInquestsituation find(Object primaryKey);

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
    public AuditTaskInquestsituation find(String sql, Object... args);

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
    public List<AuditTaskInquestsituation> findList(String sql, Object... args);

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
    public List<AuditTaskInquestsituation> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countAuditTaskInquestsituation(String sql, Object... args);

    /**
     * 
     * 根据taskGuid删除勘验事项情形数据
     * 
     * @param taskGuid
     *            事项唯一标识
     */
    public void deleteByTaskGuid(String taskGuid);

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
    public List<AuditTaskInquestsituation> listAuditTaskInquestsituationByTaskGuid(String fields, String taskGuid);

    /**
     * 
     * 把旧的taskguid,替换成新的taskguid
     * 
     * @param oldTaskGuid
     *            旧的事项标识
     * @param newtaskGuid
     *            新的事项标识
     */
    public void updateNewTaskGuidByOldTaskGuid(String oldTaskGuid, String newtaskGuid);
}
