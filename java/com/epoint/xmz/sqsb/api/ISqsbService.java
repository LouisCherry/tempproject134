package com.epoint.xmz.sqsb.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;


/**
 * 双全双百对应的后台service接口
 * 
 * @author LYA
 * @version [版本号, 2020-08-16 19:20:32]
 */
public interface ISqsbService extends Serializable
{ 
   

    /**
     * API:根据国脉前置库获取的主题id查询主题配置表
     */
    public AuditSpBusiness getAuditSpBusinessByOriginid(String originid);

    /**
     * 
     *  [根据basetaskguid获取清单配置事项列表] 
     *  @param basetaskguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditSpBasetaskR> getAuditSpBasetaskrByBaseTaskGuid(String basetaskguid);
    
    /**
     * 
     *  [根据事项的taskid查找在用版本的，非编辑的，网办深度是非事项公开的事项] 
     *  @param taskid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditTask getAuditTaskByTaskId(String taskid);

    /**
     * 
     *  [根据事项的事项名称查找在用版本的，非编辑的，网办深度是非事项公开的事项] 
     *  @param taskid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<AuditTask> getAuditTaskListByTaskName(String taskname);
    
    public List<AuditSpBasetaskR> getTaskNameBySqsb(String issqlb,String areacode);

}
