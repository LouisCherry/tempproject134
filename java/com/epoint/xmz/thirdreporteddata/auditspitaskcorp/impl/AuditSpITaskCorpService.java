package com.epoint.xmz.thirdreporteddata.auditspitaskcorp.impl;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.thirdreporteddata.auditspitaskcorp.api.entity.AuditSpITaskCorp;

import java.util.List;
import java.util.Map;

/**
 * 并联审批事项实例单位信息表对应的后台service
 *
 * @author Epoint
 * @version [版本号, 2023-10-10 15:01:07]
 */
public class AuditSpITaskCorpService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpITaskCorpService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpITaskCorp record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditSpITaskCorp.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpITaskCorp record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditSpITaskCorp find(Object primaryKey) {
        return baseDao.find(AuditSpITaskCorp.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditSpITaskCorp find(String sql, Object... args) {
        return baseDao.find(sql, AuditSpITaskCorp.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpITaskCorp> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpITaskCorp.class, args);
    }

    /**
     * 分页查找一个list
     *
     * @param sql        查询语句
     * @param pageNumber 记录行的偏移量
     * @param pageSize   记录行的最大数目
     * @param clazz      可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args       参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditSpITaskCorp> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpITaskCorp.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditSpITaskCorp(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    /**
     * 根据subappguid和taskguid删除数据 [一句话功能简述]
     *
     * @param subappguid
     * @param taskGuid
     */
    public AuditSpITaskCorp deleteInfoBySubappguidAndTaskGuid(String subappguid, String taskGuid) {
        String sql = "select *  from AUDIT_SP_I_TASK_CORP where subappguid = ? and taskguid = ?";
        return baseDao.find(sql, AuditSpITaskCorp.class, subappguid, taskGuid);
    }

    public Integer countAuditSpITaskCorps(String taskguid, String subappGuid) {
        String sql = "select count(1)  from AUDIT_SP_I_TASK_CORP where subappguid = ? and taskguid = ?";
        return baseDao.queryInt(sql, subappGuid, taskguid);
    }

    public List<AuditSpITaskCorp> getAuditSpITaskCorpList(Map<String, String> map) {
        String sql = "select * from audit_sp_i_task_corp ";
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        sql += sqlManageUtil.buildSql(map);
        return baseDao.findList(sql, AuditSpITaskCorp.class);
    }

    public String getCorpnamesBySubappguidAndTaskguid(String subappGuid, String taskGuid) {
        String sql = "select group_concat(concat(corpname, '')) from participants_info pii join (select corpguid from audit_sp_i_task_corp where subappguid = ? and taskguid = ? ) asitc on pii.rowguid = asitc.corpguid";
        return baseDao.queryString(sql, subappGuid, taskGuid);
    }
}
