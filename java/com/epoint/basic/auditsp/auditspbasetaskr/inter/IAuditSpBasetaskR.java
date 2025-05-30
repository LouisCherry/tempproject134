package com.epoint.basic.auditsp.auditspbasetaskr.inter;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

/**
 * 
 * 主题接口
 * 
 * @author Administrator
 * @version [版本号, 2017年11月6日]
 */
public interface IAuditSpBasetaskR
{

    /**
     * 增加 add 实体通用方法
     * 
     * @param basetaskr
     *            实体
     * @return 无
     */
    public AuditCommonResult<Void> addBasetaskR(AuditSpBasetaskR basetaskr);

    /**
     * 更新 update 实体通用方法
     * 
     * @param basetaskr
     *            实体
     * @return 无
     */
    public AuditCommonResult<Void> updateBasetaskR(AuditSpBasetaskR basetaskr);

    /**
     * 通过主键获取实体
     * 
     * @param rowGuid
     *            主键
     * @return 主题配置表实体
     */
    public AuditCommonResult<AuditSpBasetaskR> getAuditSpBasetaskRByrowguid(String rowGuid);

    /**
     * 通过主键删除实体
     * 
     * @param rowGuid
     * 
     * @return 主题配置表实体
     */
    public AuditCommonResult<Void> delAuditSpBasetaskR(String rowGuid);

    /**
     * 通过 审批事项标识删除关系
     * 
     * @param basetaskguid
     * 
     * @return
     */
    public AuditCommonResult<Void> delByBasetaskguid(String basetaskguid);

    /**
     * 
     * 获取审批事项关系列表
     * 
     * @param conditionMap
     *            查询条件
     * @return 列表
     */
    public AuditCommonResult<List<AuditSpBasetaskR>> getAuditSpBasetaskrByCondition(Map<String, String> conditionMap);

    /**
     * 
     * 获取审批事项关系列表
     * 
     * @param conditionMap
     *            查询条件
     * @return 列表
     */
    public AuditCommonResult<List<AuditSpBasetaskR>> getAuditSpBasetaskAndRByCondition(
            Map<String, String> conditionMap);

    /**
     * 
     * 获取审批事项关系列表
     * 
     * @param conditionMap
     *            查询条件
     * @return 列表
     */
    public AuditCommonResult<Integer> getAuditSpBasetaskrCountByCondition(Map<String, String> conditionMap);

    /**
     * 获取列表
     * 
     * @param ouname
     *            审批事项部门
     * @param basetaskguids
     *            审批事项范围
     * @return 需要征求的部门
     */
    public AuditCommonResult<List<Record>> getTaskidlistbyBasetaskOuname(String ouname, List<String> basetaskguids);

    /**
     * 根据阶段获取所有的事项标识
     * 
     * @param phaseid
     *            阶段的id
     * @return
     */
    public AuditCommonResult<List<String>> getTaskidlistbyPhaseid(String phaseid);

    /**
     * 根据阶段获取所有的事项标识
     * 
     * @param phaseid
     *            阶段的id
     * @return
     */
    public AuditCommonResult<List<String>> getTaskidlistbySpecialPhaseid(String phaseid);

    /**
     * 根据事项的id获取标准事项标识
     */
    public AuditCommonResult<List<String>> getBasetaskguidsBytaskids(List<String> taskids);

    /**
     * 根据主题获取当前主题的数据
     */
    public AuditCommonResult<List<String>> gettaskidsByBusinedssguid(String businedssguid, String phaseid);

    /**
     * 根据事项id下去编码获取所有的事项
     * 
     * @param taskid
     *            事项唯一标识
     * @param areacodeList
     *            辖区列表
     * @return
     */
    public AuditCommonResult<List<Record>> getBasetaskByAreacodelistandTaskid(String taskid, List<String> areacodeList);

    /**
     * 根据事项id下去编码获取所有的事项(包含目录清单guid)
     * 
     * @param taskid
     *            事项唯一标识
     * @param areacodeList
     *            辖区列表
     * @return
     */
    public AuditCommonResult<List<Record>> getBasetaskByAreacodelistandTaskid(String taskid, List<String> areacodeList,
            List<String> basetaskList);

    /**
     * 根据事项id主题类型查询所有事项关联关系
     * 
     * @param taskid
     *            事项id
     * @param businessType
     *            主题类型
     * @return
     */
    public AuditCommonResult<List<Record>> getBasetaskrByTaskIdAndBusinessType(String taskid, String businessType);

    /**
     * 根据事项id获取所有的事项
     * 
     * @param taskid
     *            事项id
     * @return
     */
    public AuditCommonResult<List<Record>> getBasetaskByTaskid(String taskid);

    /**
     * 根据事项id下去编码获取所有的事项(新)
     * 
     * @param taskid
     *            事项唯一标识
     * @param areacodeList
     *            辖区列表
     * @return
     */
    public AuditCommonResult<List<Record>> getBasetaskByAreacodelistandTaskidNew(String taskid,
            List<String> areacodeList, String type);

    /**
     * 查询以关联的事项
     * 
     * @param businesstype
     * @return
     */
    public List<String> getAlreadyUsedTaskid(String businesstype);
}
