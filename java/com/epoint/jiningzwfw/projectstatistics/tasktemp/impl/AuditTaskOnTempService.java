package com.epoint.jiningzwfw.projectstatistics.tasktemp.impl;

import com.epoint.basic.auditresource.service.AuditResourceService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.projectstatistics.tasktemp.api.entity.AuditTaskOnTemp;

import java.util.List;
import java.util.Map;

/**
 * 1对应的后台service
 * 
 * @author yangyi
 * @version [版本号, 2021-06-30 16:53:22]
 */
public class AuditTaskOnTempService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditTaskOnTempService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditTaskOnTemp record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditTaskOnTemp.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditTaskOnTemp record) {
        return baseDao.update(record);
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
    public AuditTaskOnTemp find(Object primaryKey) {
        return baseDao.find(AuditTaskOnTemp.class, primaryKey);
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
    public AuditTaskOnTemp find(String sql,  Object... args) {
        return baseDao.find(sql, AuditTaskOnTemp.class, args);
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
    public List<AuditTaskOnTemp> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditTaskOnTemp.class, args);
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
    public List<AuditTaskOnTemp> findList(String sql, int pageNumber, int pageSize, Object... args) {
        //system.out.println(sql);
        return baseDao.findList(sql, pageNumber, pageSize, AuditTaskOnTemp.class, args);
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
    public Integer countAuditTaskOnTemp(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }

    public AuditCommonResult<List<AuditTaskOnTemp>> findListByCondition(Map<String, String> map) {
        AuditResourceService<AuditTaskOnTemp> auditResourceService = new AuditResourceService();
        AuditCommonResult result = new AuditCommonResult();
        try {
            List<AuditTaskOnTemp> list = auditResourceService.getAllRecord(AuditTaskOnTemp.class, map, false);
            result.setResult(list);
        }
        catch (Exception var5) {
            result.setSystemFail(var5.getMessage());
        } return result;
    }

    public void doProcedure() {
        CommonDao.getInstance().executeProcudureWithoutReturn("proc_audit_task_on_temp");
    }
}
