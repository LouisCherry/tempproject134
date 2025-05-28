package com.epoint.basic.auditsp.auditspitask.inter;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.common.service.AuditCommonResult;

/**
 * 
 * 主题事项接口
 * 
 * @author Administrator
 * @version [版本号, 2017年11月6日]
 */
public interface IAuditSpITask
{

    /**
     * 添加申报涉及到的事项实例
     * 
     * @param businessGuid
     *            主题标识
     * @param biGuid
     *            主题实例唯一标识
     * @param phaseGuid
     *            阶段唯一标识
     * @param taskGuid
     *            事项唯一标识
     * @param taskName
     *            事项名称
     * @param subappGuid
     *            子申报唯一标识
     * @param orderNum
     *            排序
     * @return 无
     */
    AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid, String taskGuid,
            String taskName, String subappGuid, Integer orderNum);

    /**
     * 添加申报涉及到的事项实例
     * 
     * @param businessGuid
     *            主题标识
     * @param biGuid
     *            主题实例唯一标识
     * @param phaseGuid
     *            阶段唯一标识
     * @param taskGuid
     *            事项唯一标识
     * @param taskName
     *            事项名称
     * @param subappGuid
     *            子申报唯一标识
     * @param orderNum
     *            排序
     * @param areacode
     *            辖区编码
     * @return 无
     */
    AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid, String taskGuid,
            String taskName, String subappGuid, Integer orderNum, String areacode);

    /**
     * 添加申报涉及到的事项实例
     * 
     * @param businessGuid
     *            主题标识
     * @param biGuid
     *            主题实例唯一标识
     * @param phaseGuid
     *            阶段唯一标识
     * @param taskGuid
     *            事项唯一标识
     * @param taskName
     *            事项名称
     * @param subappGuid
     *            子申报唯一标识
     * @param orderNum
     *            排序
     * @param areacode
     *            辖区编码
     * @param reviewguid
     *            征求标识
     * @return 无
     */
    AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid, String taskGuid,
            String taskName, String subappGuid, Integer orderNum, String areacode, String reviewguid);

    /**
     * 添加申报涉及到的事项实例
     * 
     * @param businessGuid
     *            主题标识
     * @param biGuid
     *            主题实例唯一标识
     * @param phaseGuid
     *            阶段唯一标识
     * @param taskGuid
     *            事项唯一标识
     * @param taskName
     *            事项名称
     * @param subappGuid
     *            子申报唯一标识
     * @param orderNum
     *            排序
     * @param areacode
     *            辖区编码
     * @param reviewguid
     *            征求标识
     * @return 无
     */
    AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid, String taskGuid,
            String taskName, String subappGuid, Integer orderNum, String areacode, String reviewguid, String sflcbsx);

    /**
     * 获取子申报中的事项集合
     * 
     * @param subappGuid
     *            子申报唯一标识
     * @return 主题事项实例集合
     */
    AuditCommonResult<List<AuditSpITask>> getTaskInstanceBySubappGuid(String subappGuid);

    /**
     * 
     * 获取要初始化生成办件的实例事项
     * 
     * @param subappGuid
     *            子申报唯一标识
     * @return 主题事项实例集合
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    AuditCommonResult<List<AuditSpITask>> getInitProjectTaskInstanceBySubappGuid(String subappGuid);

    /**
     * 更新子申报的办件标识
     * 
     * @param subappGuid
     *            子申报唯一标识
     * @param taskGuid
     *            事项唯一标识
     * @param projectGuid
     *            办件唯一标识
     * @return 无
     */
    AuditCommonResult<String> updateProjectGuid(String subappGuid, String taskGuid, String projectGuid);

    /**
     * 根据主题实例标志获取事项集合
     * 
     * @param biGuid
     *            主题实例唯一标识
     * @return 主题事项实例集合
     */
    AuditCommonResult<List<AuditSpITask>> getSpITaskByBIGuid(String biGuid);

    /**
     * 根据阶段获取事项集合
     * 
     * @param phaseGuid
     *            阶段唯一标识
     * @return 主题事项实例集合
     */
    AuditCommonResult<List<AuditSpITask>> getSpITaskByPhaseGuid(String phaseGuid);

    /**
     * 根据子申报标识获取事项id集合
     * 
     * @param subappGuid
     *            子申报唯一标识
     * @return 事项id集合
     */
    AuditCommonResult<List<String>> getTaskIDBySubappGuid(String subappGuid);

    /**
     * 根据事项征求id查询
     *
     * @param subappGuid
     *            子申报唯一标识
     * @return 事项id集合
     */
    AuditCommonResult<List<String>> getTaskIDByReviewguid(String reviewguid);

    /**
     * 根据子申报标识，获取办件rowguid标识
     * 
     * @param subappGuid
     *            子申报唯一标识
     * @return 事项id集合
     */
    AuditCommonResult<List<String>> getProjectguidsBySubappGuid(String subappGuid);

    /**
     * 根据rowguid获取实体
     * 
     * @param rowguid
     * 
     * @return
     */
    AuditCommonResult<AuditSpITask> getAuditSpITaskDetail(String rowguid);

    /**
     * 更新实体
     * 
     * @param auditspitask
     *            实体记录
     */
    void updateAuditSpITask(AuditSpITask auditspitask);

    /**
     * 根据条件获取list
     * 
     * @param condition
     * @return
     */
    public AuditCommonResult<List<AuditSpITask>> getAuditSpITaskListByCondition(Map<String, String> condition);

    /**
     * 查询某字段根据某字段
     * 
     * @param field
     *            字段
     * @param condition
     *            条件
     * @return 事项id集合
     */
    public AuditCommonResult<List<String>> getStringListByCondition(String field, Map<String, String> condition);

    /**
     * 
     * @param projectguid
     *            办件标识
     * @return
     */
    public AuditCommonResult<AuditSpITask> getAuditSpITaskByProjectGuid(String projectguid);

    /**
     * 更新办件guid
     * 
     * @param rowguid
     *            spitask唯一标识
     * @param projectGuid
     *            办件标识
     * @return
     */
    public AuditCommonResult<String> updateProjectGuid(String rowguid, String projectGuid);

    /**
     * 根据子申报标识获取事项id集合
     * 
     * @param subappGuid
     *            子申报唯一标识
     * @return 事项id集合
     */
    AuditCommonResult<Integer> deleteSpITaskBySubappGuid(String subappGuid);

    /**
     * 根据条件查询实例事项的数量
     * 
     * @param conditionMap
     *            查询条件。SqlConditionUtil.getMap
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Integer countByCondition(Map<String, String> conditionMap);

    /**
     * 添加申报涉及到的事项实例
     * 
     * @param businessGuid
     *            主题标识
     * @param biGuid
     *            主题实例唯一标识
     * @param phaseGuid
     *            阶段唯一标识
     * @param taskGuid
     *            事项唯一标识
     * @param taskName
     *            事项名称
     * @param subappGuid
     *            子申报唯一标识
     * @param orderNum
     *            排序
     * @param areacode
     *            辖区编码
     * @param reviewguid
     *            征求标识
     * @return 无
     */
    AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid, String string, String string2,
            String subAppGuid, Integer orderNum, String areacode, String reviewguid, String sflcbsx, String townshipcode);

}
