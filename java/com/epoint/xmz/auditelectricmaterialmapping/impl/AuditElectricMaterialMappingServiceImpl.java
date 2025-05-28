package com.epoint.xmz.auditelectricmaterialmapping.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.xmz.auditelectricmaterialmapping.api.IAuditElectricMaterialMappingService;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 电力材料映射表对应的后台service实现类
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:08]
 */
@Component
@Service
public class AuditElectricMaterialMappingServiceImpl implements IAuditElectricMaterialMappingService {
    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditElectricMaterialMapping record) {
        return new AuditElectricMaterialMappingService().insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditElectricMaterialMappingService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditElectricMaterialMapping record) {
        return new AuditElectricMaterialMappingService().update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditElectricMaterialMapping find(Object primaryKey) {
        return new AuditElectricMaterialMappingService().find(primaryKey);
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
    public AuditElectricMaterialMapping find(String sql, Object... args) {
        return new AuditElectricMaterialMappingService().find(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditElectricMaterialMapping> findList(String sql, Object... args) {
        return new AuditElectricMaterialMappingService().findList(sql, args);
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
    public List<AuditElectricMaterialMapping> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditElectricMaterialMappingService().findList(sql, pageNumber, pageSize, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    @Override
    public Integer countAuditElectricMaterialMapping(String sql, Object... args) {
        return new AuditElectricMaterialMappingService().countAuditElectricMaterialMapping(sql, args);
    }

    @Override
    public AuditElectricMaterialMapping getMappingDataByUuid(String uuid,String itemId) {
        return new AuditElectricMaterialMappingService().getMappingDataByUuid(uuid,itemId);
    }

    @Override
    public AuditElectricMaterialMapping getMappingDataByItemId(String itemId) {
        return new AuditElectricMaterialMappingService().getMappingDataByItemId(itemId);
    }

    @Override
    public void deleteByItemId(String itemId) {
        new AuditElectricMaterialMappingService().deleteByItemId(itemId);
    }

}
