package com.epoint.xmz.auditelectricmaterialmapping.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.auditelectricmaterialmapping.api.entity.AuditElectricMaterialMapping;

import java.util.List;

/**
 * 电力材料映射表对应的后台service
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:08]
 */
public class AuditElectricMaterialMappingService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditElectricMaterialMappingService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditElectricMaterialMapping record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(AuditElectricMaterialMapping.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditElectricMaterialMapping record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     *
     * @param clazz      类<必须继承BaseEntity>
     * @param primaryKey 主键
     * @return T extends BaseEntity
     */
    public AuditElectricMaterialMapping find(Object primaryKey) {
        return baseDao.find(AuditElectricMaterialMapping.class, primaryKey);
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
        return baseDao.find(sql, AuditElectricMaterialMapping.class, args);
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
        return baseDao.findList(sql, AuditElectricMaterialMapping.class, args);
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
        return baseDao.findList(sql, pageNumber, pageSize, AuditElectricMaterialMapping.class, args);
    }

    /**
     * 查询数量
     *
     * @param sql  执行语句
     * @param args 参数
     * @return Integer
     */
    public Integer countAuditElectricMaterialMapping(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public AuditElectricMaterialMapping getMappingDataByUuid(String uuid, String itemId) {
        String sql = "select * from audit_electric_material_mapping where uuid = ? and itemId = ?";
        return baseDao.find(sql, AuditElectricMaterialMapping.class, uuid, itemId);
    }

    public AuditElectricMaterialMapping getMappingDataByItemId(String itemId) {
        String sql = "select RowGuid,materialId,CLLYDM,itemId,taskName,ouName,updateTime from audit_electric_material_mapping where itemId = ?";
        return baseDao.find(sql, AuditElectricMaterialMapping.class, itemId);
    }

    public void deleteByItemId(String itemId) {
        String sql = "delete from audit_electric_material_mapping where itemId = ?";
        baseDao.execute(sql, itemId);
    }
}
