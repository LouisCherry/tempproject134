package com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter;
import java.io.Serializable;
import java.util.Map;

import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 一体机绑定好视通账户对应的后台service接口
 * 
 * @author JackLove
 * @version [版本号, 2018-04-12 15:24:50]
 */
public interface IAuditZnsbRemoteHelpUserService extends Serializable
{ 
   
    /**
     * 分页查询列表
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param conditionMap
     *  @param first
     *  @param pageSize
     *  @param sortField
     *  @param sortOrder
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<PageData<AuditZnsbRemoteHelpUser>> getFileByPage(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder);
    /**
     * 
     * 插入数据
     * 
     * @param AuditQueueWindowExtendinfo实体
     * 
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<String> insert(AuditZnsbRemoteHelpUser auditZnsbAssessconfig);
    
    /**
     * 根据rowguid删除
     * @param RowGuid
     * @return
     */
    public AuditCommonResult<String> deletebyRowGuid(String RowGuid);
    
    /**
     * 获取详情
     * @param RowGuid
     * @return
     */
    public AuditCommonResult<AuditZnsbRemoteHelpUser> getDetail(String RowGuid);
    
    /**
     * 获取详情
     * @param RowGuid
     * @return
     */
    public AuditCommonResult<AuditZnsbRemoteHelpUser> getDetailByUserguid(String userguid);
    
    /**
     * 更新数据
     * @param dataBean
     * @return
     */
    public AuditCommonResult<String> update(AuditZnsbRemoteHelpUser dataBean);

    public Integer getAuditZnsbRemoteHelpUserByAccount(String account);
    
    public Integer getAuditZnsbRemoteHelpUserByUserguid(String userguid);

}
