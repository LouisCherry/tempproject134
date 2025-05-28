package com.epoint.jnrestforevaluat.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.entity.FrameUser;

/**
 * 
 * 
 * @author zhaoy
 * @version [版本号, 2019-01-23 16:39:23]
 */
public interface IJnEvaluatService extends Serializable
{ 
    
    public List<FrameOu> getOuinfo(String ouguid,String areacode);
    
    public PageData<AuditProject> getProjectinfo(String ouguid,String areacode,String startdate,
            String enddate,String flowsn,Integer pagesize,Integer index);
    
    public PageData<FrameUser> getUserinfo(String ouguid,String areacode,String userguid,
            Integer pagesize,Integer index);
    
    public Integer isExistProject(String projectguid);
}
