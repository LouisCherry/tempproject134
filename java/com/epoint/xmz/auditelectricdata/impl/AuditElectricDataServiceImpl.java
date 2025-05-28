package com.epoint.xmz.auditelectricdata.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.xmz.auditelectricdata.api.IAuditElectricDataService;
import com.epoint.xmz.auditelectricdata.api.entity.AuditElectricData;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电力事项信息表对应的后台service实现类
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:23:54]
 */
@Component
@Service
public class AuditElectricDataServiceImpl implements IAuditElectricDataService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditElectricData record) {
        return new AuditElectricDataService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditElectricDataService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditElectricData record) {
        return new AuditElectricDataService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditElectricData find(Object primaryKey) {
        return new AuditElectricDataService().find(primaryKey);
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
        return new AuditElectricDataService().find(sql, args);
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
        return new AuditElectricDataService().findList(sql, args);
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
        return new AuditElectricDataService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditElectricData(String sql, Object... args) {
        return new AuditElectricDataService().countAuditElectricData(sql, args);
    }

    @Override
    public AuditTask getTaskByItemId(String itemId) {
        return new AuditElectricDataService().getTaskByItemId(itemId);
    }

    @Override
    public List<AuditElectricData> getElectricDataList() {
        return new AuditElectricDataService().getElectricDataList();
    }

    @Override
    public int countDataByProjectGuid(String projectGuid) {
        return new AuditElectricDataService().countDataByProjectGuid(projectGuid);
    }

    @Override
    public String getDlFlowSnByProjectGuid(String projectID) {
        return new AuditElectricDataService().getDlFlowSnByProjectGuid(projectID);
    }

}
