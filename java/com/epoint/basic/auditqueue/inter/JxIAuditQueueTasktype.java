package com.epoint.basic.auditqueue.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 
 * 事项分类api
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 * 
 */
@Service
public interface JxIAuditQueueTasktype
{
    /**
     * 查找 通过主键获取实体
     * 
     * @param conditionMap
     *            条件Map
     */
    public AuditCommonResult<List<AuditQueueTasktype>> getAllTasktype(Map<String, String> conditionMap);

    /**
     * 查找 通过主键获取实体
     * 
     * @param rowguid
     *            主键
     */
    public AuditCommonResult<AuditQueueTasktype> getAuditQueueTasktypeByRowguid(String rowguid);

    public Integer getAuditQueueTasktypeCount(Map<String, String> conditionMap, String fields);

    public AuditCommonResult<Integer> getCountbyOUGuid(String ouguid, String centerguid);

    public AuditCommonResult<String> delete(String RowGuid);

    public AuditCommonResult<String> insertTasktype(AuditQueueTasktype auditQueueTasktype);

    public AuditCommonResult<String> updateAuditQueueTasktype(AuditQueueTasktype auditQueueTasktype);

    public AuditCommonResult<AuditQueueTasktype> getTasktypeByguid(String rowguid);

    public AuditCommonResult<List<Record>> getAuditQueueTasktypeByPage(String ouguid, String centerguid, int first,
            int pageSize);

    public AuditCommonResult<List<AuditQueueTasktype>> getAuditQueueTasktypeByPage(Map<String, String> conditionMap,
            int first, int pagesize, String fields);

    public AuditCommonResult<PageData<AuditQueueTasktype>> getAuditQueueTasktypeByPage(String fieldstr,
            Map<String, String> conditionMap, int first, int pagesize, String sortField, String sortOrder);

    public AuditCommonResult<Integer> getAuditQueueTasktypeCount(String TaskTypeName, String OUGuid, String centerguid);

    /**
     * 
     * 获取选中事项分类
     * 
     * @param RowGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<List<AuditQueueTasktype>> selectTaskTypeByGuid(String RowGuid);

    public AuditCommonResult<String> deletebyqueuevalue(String queuevalue);

    public AuditCommonResult<String> updateTaskTypeQueueValue(String queuevalue, String rowguid);

    /**
     * 根据条件查询事项分类
     * 
     * @param condition
     *            条件
     * @param areaCode
     *            区域标识
     * @return 事项实体集合
     */
    public AuditCommonResult<List<AuditQueueTasktype>> selectAuditQueueTasktypeByCondition(String condition);

    /**
     * 
     * 该节点下面有子节点，获取该窗口部门下的所有事项分类
     * 
     * @param objectGuid
     *            部门标识
     * @param areaCode
     *            区域标识
     * @return 事项实体集合
     */
    public AuditCommonResult<List<AuditQueueTasktype>> selectAuditQueueTasktypeOuByOuGuid(String ouGuid);

    public AuditCommonResult<List<AuditQueueTasktype>> getTasktypebyLargeTasktypeGuid(String largetasktypeguid);

    public AuditCommonResult<String> getLargeTasktypeGuidbyRowguidandCenterGuid(String rowguid, String centerguid);

    public AuditCommonResult<String> emptyLargeTasktypeGuidbyGuid(String rowguid);

    public AuditCommonResult<String> deleteLargeTasktypeGuidbyLargeTasktypeGuid(String largetasktypeguid);

    public AuditCommonResult<String> updateTasktypeByRowguid(String rowguid, String largetasktypeguid);

    int gettasktypesfzCount(String tasktypeguid, String sfz);

}
