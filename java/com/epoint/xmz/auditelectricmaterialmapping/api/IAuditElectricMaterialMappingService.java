package com.epoint.xmz.auditelectricmaterialmapping.api;

import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;

import java.io.Serializable;
import java.util.List;

/**
 * 电力材料映射表对应的后台service接口
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:08]
 */
public interface IAuditElectricMaterialMappingService extends Serializable {

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditElectricMaterialMapping record);

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditElectricMaterialMapping record);

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditElectricMaterialMapping find(Object primaryKey);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditElectricMaterialMapping find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditElectricMaterialMapping> findList(String sql, Object... args);

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
    public List<AuditElectricMaterialMapping> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditElectricMaterialMapping(String sql, Object... args);

    /**
     *
     * @param uuid 材料来源编码
     * @param itemId 事项编码
     * @return mapping表实体
     */
    AuditElectricMaterialMapping getMappingDataByUuid(String uuid,String itemId);

    AuditElectricMaterialMapping getMappingDataByItemId(String itemId);

    /**
     * 根据itemId删除数据
     * @param itemId itemId
     */
    void deleteByItemId(String itemId);
}
