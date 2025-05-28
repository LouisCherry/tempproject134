package com.epoint.auditresource.auditrsitembaseinfo.api;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.core.grammar.Record;

public interface IJNauditRsItemBaseinfoservice
{

    //根据主题实列bigguid获取阶段名称
    List<AuditSpPhase> getphaseNamebybusinessguid(String bigguid);

    //根据阶段guid和主题实列bigguid获取对应项目的阶段事项
    List<AuditSpITask> gettaskbybigguid(String xiangmuguid, String phaseguid);
    
    List<AuditSpITask> gettaskbybigguidOld(String xiangmuguid, String phaseguid);

    int gettaskbytotal(String phaseguid, String bigguid);

    List<Record> getphase(String guid);

    Record getAuditProjectbyrowguid(String guid);

    String getstatusnamebyguid(String status);
    
    AuditProject getProjectByTaskguid(String taskguid,String biguid);
    
    Record getSumOfInstance(String areacode,String type);

	String findMaxItemCodeByParentId(String rowguid);
    
    //List<AuditSpInstance> getSpInstansList(String areacode);
}
