package com.epoint.auditsp.auditspbasetaskp.inter;

import com.epoint.auditsp.auditspbasetaskp.domain.AuditSpBasetaskP;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.List;
import java.util.Map;

/**
 * 主题接口
 *
 * @author Administrator
 * @version [版本号, 2017年11月6日]
 */
public interface IAuditSpBasetaskP {

    /**
     * 增加 add 实体通用方法
     *
     * @param basetask 实体
     * @return 无
     */
    public AuditCommonResult<Void> addBasetask(AuditSpBasetaskP basetask);

    /**
     * 更新 update 实体通用方法
     *
     * @param basetask 实体
     * @return 无
     */
    public AuditCommonResult<Void> updateBasetask(AuditSpBasetaskP basetask);

    /**
     * 查找 通过主键获取实体
     *
     * @param rowGuid 主键
     * @return 审批事项表实体
     */
    public AuditCommonResult<AuditSpBasetaskP> getAuditSpBasetaskPByrowguid(String rowGuid);

    /**
     * 通过主键删除实体
     *
     * @param rowGuid 主键
     * @return
     */
    public AuditCommonResult<Void> delAuditSpBasetaskPByrowguid(String rowGuid);

    /**
     * 获取审批事项分页
     *
     * @param conditionMap 查询条件
     * @param first        分页起始值
     * @param pageSize     每页数量
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return 审批事项
     */
    public AuditCommonResult<PageData<AuditSpBasetaskP>> getAuditSpBasetaskPByPage(Map<String, String> conditionMap,
                                                                                   int first, int pageSize, String sortField, String sortOrder);

    /**
     * 获取审批事项列表
     *
     * @param conditionMap 查询条件
     * @return 列表
     */
    public AuditCommonResult<List<AuditSpBasetaskP>> getAuditSpBasetaskPByCondition(Map<String, String> conditionMap);

    /**
     * 根据条件查询倒记录的数量
     *
     * @param condition 条件的map
     * @return
     */
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> condition);

    /**
     * 获取所有有子标准事项的父标准事项和单独的子标准事项
     *
     * @return
     */
    public AuditCommonResult<List<Record>> getParentBaseTaskList(int pageNumber, int pageSize, String taskname,
                                                                 String ouname, String phaseid);

    /**
     * 获取所有有子标准事项的父标准事项和单独的子标准事项的数量
     *
     * @return
     */
    public AuditCommonResult<Integer> getParentBaseTaskCount(String taskname, String ouname, String phaseid);

}
