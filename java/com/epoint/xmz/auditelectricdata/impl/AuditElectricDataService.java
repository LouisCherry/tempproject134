package com.epoint.xmz.auditelectricdata.impl;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.auditelectricdata.api.entity.AuditElectricData;

import java.util.List;

/**
 * 电力事项信息表对应的后台service
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:23:54]
 */
public class AuditElectricDataService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditElectricDataService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditElectricData record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditElectricData.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditElectricData record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditElectricData find(Object primaryKey) {
        return baseDao.find(AuditElectricData.class, primaryKey);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditElectricData find(String sql, Object... args) {
        return baseDao.find(sql, AuditElectricData.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditElectricData> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditElectricData.class, args);
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
    public List<AuditElectricData> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditElectricData.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditElectricData(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public AuditTask getTaskByItemId(String itemId) {
        String sql = "select *  FROM Audit_Task Where item_id = ?" +
                " and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1 and IS_ENABLE=1 ";
        return baseDao.find(sql, AuditTask.class, itemId);
    }

    /**
     * 获取电力登记数据
     *
     * @return 每次10条
     */
    public List<AuditElectricData> getElectricDataList() {
        String sql = "select RowGuid,`type`,params,`result`,projectguid,updatetime,error,flowsn from audit_electric_data " +
                " where projectguid is null order by updatetime limit 10";
        return baseDao.findList(sql, AuditElectricData.class);
    }

    public int countDataByProjectGuid(String projectGuid) {
        String sql = "select count(*) from audit_electric_data where projectGuid = ?";
        return baseDao.queryInt(sql, projectGuid);
    }

    public String getDlFlowSnByProjectGuid(String projectID) {
        String sql = "select dlFlowSn from audit_electric_data where projectGuid = ? and type = '登记'";
        return baseDao.queryString(sql,projectID);
    }
}
