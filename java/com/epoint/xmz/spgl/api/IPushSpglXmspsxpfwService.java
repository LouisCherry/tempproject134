package com.epoint.xmz.spgl.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.auditproject.monitorsupervise.api.auditprojectold;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;


public interface IPushSpglXmspsxpfwService extends Serializable
{
	 /**
     * 查询数据
     * 
     * @return List<AuditProject>
     */
    List<AuditProject> findProjectList();
    
    List<AuditProject> findYthProjectList();
    
    List<auditprojectold> findOldYthProjectList();

   
    
    
    

}
