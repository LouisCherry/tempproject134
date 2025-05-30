package com.epoint.xmz.auditspitaskcorp.api;


import com.epoint.xmz.auditspitaskcorp.api.entity.AuditSpITaskCorp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 并联审批事项实例单位信息表对应的后台service接口
 *
 * @author Epoint
 * @version [版本号, 2023-10-10 15:01:07]
 */
public interface IAuditSpITaskCorpService extends Serializable
{

    /**
     * 插入数据
     *
     * @param record
     *         BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpITaskCorp record);

    /**
     * 删除数据
     *
     * @param record
     *         BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record
     *         BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpITaskCorp record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz
     *         类<必须继承BaseEntity>
     * @param primaryKey
     *         主键
     * @return T extends BaseEntity
     */
    public AuditSpITaskCorp find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql
     *         查询语句
     * @param clazz
     *         可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args
     *         参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpITaskCorp find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql
     *         查询语句
     * @param clazz
     *         可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *         参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpITaskCorp> findList(String sql, Object... args);

    /**
     * 分页查找一个list
     *
     * @param sql
     *         查询语句
     * @param pageNumber
     *         记录行的偏移量
     * @param pageSize
     *         记录行的最大数目
     * @param clazz
     *         可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *         参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpITaskCorp> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql
     *         执行语句
     * @param args
     *         参数
     * @return Integer
     */
    public Integer countAuditSpITaskCorp(String sql, Object... args);

    /**
     * 根据subappguid和taskguid删除数据 [一句话功能简述]
     *
     * @param subappguid
     * @param taskGuid
     */
    public AuditSpITaskCorp deleteInfoBySubappguidAndTaskGuid(String subappguid, String taskGuid);

    /**
     * 根据subappguid和taskguid删除数据 [一句话功能简述]
     *
     * @param subappguid
     * @param taskGuid
     */
    public Integer countAuditSpITaskCorps(String taskguid, String subappGuid);

    public List<AuditSpITaskCorp> getAuditSpITaskCorpList(Map<String, String> map);

    /**
     * 查询已关联单位名称
     *
     * @param subappGuid
     * @param taskGuid
     * @return
     */
    public String getCorpnamesBySubappguidAndTaskguid(String subappGuid, String taskGuid);
}
