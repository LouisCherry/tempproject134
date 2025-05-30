package com.epoint.basic.auditproject.auditproject.inter;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;

/**
 * 
 * 办件相关接口定义
 * @author Administrator
 * @version [版本号, 2017年11月6日]
 */
public interface IJNAuditProject {
    
    public Record getMaxZjNum(String name);
    
    public void UpdateMaxZjNum(String maxnum,String name);
    
    public AuditTask getAuditTaskByUnid(String unid);
    
    
}
