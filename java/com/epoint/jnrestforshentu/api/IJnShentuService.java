package com.epoint.jnrestforshentu.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.grammar.Record;
import com.epoint.jnrestforshentu.api.entity.AuditThreeFirst;

/**
 * 
 * 
 * @author zhaoy
 * @version [版本号, 2019-01-23 16:39:23]
 */
public interface IJnShentuService extends Serializable
{ 
    public Integer isExistEvaluate(String projectguid);
    
    public Integer isExistProject(String projectguid);
    
    public List<AuditRsItemBaseinfo> getTsProjectInfo(String flowsn, String name, String creditcode);
    
    public Integer addTsOpinion(Record opinion);
    
    public Record getTsProjectInfoByRowguid(String rowguid);
    
    public AuditRsItemBaseinfo getTsClientguidByRowguid(String rowguid);
    
    public List<Record> getThreeFirstByAttachguid(String type);
    
    public int insert(AuditThreeFirst record);
}
