package com.epoint.zoucheng.znsb.auditznsbcentertask.inter;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Service
public interface IZCAuditZnsbCentertask
{
    /**
     * 
     *  分页获取中心事项列表
     *  @param conditionMap 查询条件
     *  @param first 起始位置
     *  @param pageSize 每页数量
     *  @param sortField [排序字段
     *  @param sortOrder 排序方式
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<AuditZnsbCentertask>> getCenterTaskPageData(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);

    /**
     * 根据rowguid删除
     * @param RowGuid 中心事项guid
     * @return
     */
    public AuditCommonResult<String> deletebyRowGuid(String RowGuid);
    
    /**
     * 根据CenterGuid删除
     * @param CenterGuid 中心guid
     * @return
     */
    public AuditCommonResult<String> deletebyCenterGuid(String CenterGuid);
    
    /**
     * 根据TaskId和中心guid删除
     * @param TaskId 事项id
     * @param CenterGuid 中心guid
     * @return
     */
    public AuditCommonResult<String> deletebyTaskId(String TaskId,String CenterGuid);

    /**
     * 插入数据
     * @param dataBean 中心事项实体
     * @return
     */
    public AuditCommonResult<String> insert(AuditZnsbCentertask dataBean);

    /**
     * 更新数据
     * @param dataBean 中心事项实体
     * @return
     */
    public AuditCommonResult<String> update(AuditZnsbCentertask dataBean);

    /**
     * 获取详情
     * @param RowGuid 中心事项guid
     * @return
     */
    public AuditCommonResult<AuditZnsbCentertask> getDetail(String RowGuid);
    
    /**
     * 
     *  根据事项guid获取详情
     *  @param TaskGuid 事项guid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<AuditZnsbCentertask> getDetailbyTaskGuid(String TaskGuid);

    /**
     * 根据TaskId判断是否存在
     * @param TaskId 事项id
     * @param centerguid 中心guid
     * @return
     */
    public AuditCommonResult<Boolean> ISExistbyTaskId(String TaskId, String centerguid);

    /**
     * 
     *  更新
     *  @param edit
     *  @param centerguid
     *  @return
     */
    public AuditCommonResult<String> updateedit(String edit, String centerguid);
    /**
     * 
     *  更新是否编辑状态
     *  @param edit 是否编辑状态
     *  @param taskid 事项id
     *  @param centerguid 中心guid
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> updateeditbytaskid(String edit, String taskid, String centerguid);

    /**
     * 删除编辑中数据
     * @param edit 
     * @param centerguid 中心guid
     * @return
     */
    public AuditCommonResult<String> deletebyedit(String edit, String centerguid);

    /**
     * 删除
     * @param taskguid 中心事项guid
     * @return
     */
    public AuditCommonResult<String> deletebytaskguid(String taskguid);

    /**
     * 获取中心事项列表
     * @param conditionMap 查询条件
     * @return
     */
    public AuditCommonResult<List<AuditZnsbCentertask>> getCenterTaskList(Map<String, String> conditionMap);
    /**
     * 
     *  分页获取事项部门列表
     *  @param centerguid 中心guid
     *  @param applyertype 申请人类型
     *  @param first 起始位置
     *  @param pageSize 每页数量
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<String>> getTaskOUListPageData(String centerguid, String applyertype, int first,
            int pageSize);
    /**
     * 
     *  获取事项部门列表
     *  @param centerguid 中心guid
     *  @param applyertype 申请人类型
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<String>> getTaskOUList(String centerguid, String applyertype);
    /**
     * 
     *  获取事项部门列表
     *  @param centerguid 中心guid
     *  @param applyertype 申请人类型
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<List<String>> getTaskOUListByOuName(String centerguid, String applyertype, String ouname);
    /**
     * 
     *  分页获取部门guid(不重复的)
     *  @param centerguid 中心guid
     *  @param first 起始位置
     *  @param pageSize 每页数量
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<String>> getSampleOUPageData(String centerguid, int first,
            int pageSize);
    /**
     * 
     *  分页获取事项名称和guid
     *  @param centerguid 中心guid
     *  @param ouguid 部门guid
     *  @param taskname 事项名称
     *  @param first 起始位置
     *  @param pageSize 每页数量
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<Record>> getSampleTaskPageData(String centerguid, String ouguid, String taskname,
            int first, int pageSize);
    /**
     * 
     *  分页获取热门中心事项
     *  @param centerguid 中心guid
     *  @param taskname 事项名称
     *  @param first 起始位置
     *  @param pageSize 每页数量
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<PageData<Record>> getCommonSampleTaskPageData(String centerguid, String taskname, int first,
            int pageSize);
    /**
     * 
     *  更新热门事项
     *  @param areacode 辖区编号
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> updatenohottask(String areacode);

    /**
     * 
     *  更新热门事项
     *  @param taskid 事项id
     *  @param ishottask 是否热门事项
     *  @param hottaskordernum 热门事项排序
     *  @return    
     * 
     * 
     */
    public AuditCommonResult<String> updatehottask(String taskid, String ishottask, Integer hottaskordernum);
}
