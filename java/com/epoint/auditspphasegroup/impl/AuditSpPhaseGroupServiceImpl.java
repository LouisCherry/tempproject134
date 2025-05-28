package com.epoint.auditspphasegroup.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 前四阶段分组配置表对应的后台service实现类
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:01:54]
 */
@Component
@Service
public class AuditSpPhaseGroupServiceImpl implements IAuditSpPhaseGroupService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpPhaseGroup record) {
        return new AuditSpPhaseGroupService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditSpPhaseGroupService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpPhaseGroup record) {
        return new AuditSpPhaseGroupService().update(record);
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
    public AuditSpPhaseGroup find(Object primaryKey) {
        return new AuditSpPhaseGroupService().find(primaryKey);
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
    public AuditSpPhaseGroup find(String sql, Object... args) {
        return new AuditSpPhaseGroupService().find(sql, args);
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
    public List<AuditSpPhaseGroup> findList(String sql, Object... args) {
        return new AuditSpPhaseGroupService().findList(sql, args);
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
    public List<AuditSpPhaseGroup> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditSpPhaseGroupService().findList(sql, pageNumber, pageSize, args);
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
    public Integer countAuditSpPhaseGroup(String sql, Object... args) {
        return new AuditSpPhaseGroupService().countAuditSpPhaseGroup(sql, args);
    }

    /**
     * 
     * 获取阶段分组信息集合
     * 
     * @param conditionMap
     *            查询条件
     * @return list结果
     */
    @Override
    public List<AuditSpPhaseGroup> getAuditSpPhaseGroupListByCondition(Map<String, String> conditionMap) {
        return new AuditSpPhaseGroupService().getAuditSpItemListByCondition(conditionMap);
    }

    @Override
    public List<AuditSpPhaseGroup> getAllPhaseGroup() {
        return new AuditSpPhaseGroupService().getAllPhaseGroup();
    }

    @Override
    public PageData<AuditSpPhaseGroup> getDbListByPage(Map<String, String> map, int first, int pageSize,
            String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(AuditSpPhaseGroup.class, map, first, pageSize, sortField, sortOrder);
    }

}
