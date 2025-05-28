package com.epoint.znsb.jnzwfw.module;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.znsb.jnzwfw.module.entity.AuditZnsbModule;

/**
 * 
 * 一体机模块
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 * 
 */
@Service
public interface IAuditZnsbModuleService
{
    /**
     * 
     * 根据条件获取 模块点击信息
     * 
     * @param macaddress
     *            mac地址
     * @return
     * 
     * 
     */
    public AuditCommonResult<List<AuditZnsbModule>> getAuditZnsbModuleList(Map<String, String> conditionMap);

    /**
     * 获取分页列表
     * [功能详细描述]
     * 
     * @param conditionMap
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     * 
     * 
     */
    public AuditCommonResult<PageData<AuditZnsbModule>> getAuditZnsbModuleListPageData(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public void update(AuditZnsbModule record);

    /**
     * 插入点击信息
     * 
     * @param bean
     *            点击实体类
     * @return
     */
    public void insert(AuditZnsbModule bean);

    int findMacListCountByConfigType(String macaddress, String centerguid, String moduleconfigtype);

    int findMacListCount(String macaddress, String centerguid);

    int findCenterListCount(String centerguid);

    List<AuditZnsbSelfmachinemodule> findCenterListCountAndLabel(String centerguid, String lableguid);

    List<AuditZnsbSelfmachinemodule> findMacListCountAndLabel(String macaddress, String centerguid, String lableguid);

    List<AuditZnsbSelfmachinemodule> findMacListByConfigTypeAndLabel(String macaddress, String centerguid,
            String moduleconfigtype, String lableguid);
}
