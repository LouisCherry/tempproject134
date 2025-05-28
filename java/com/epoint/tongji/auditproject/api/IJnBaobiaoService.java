package com.epoint.tongji.auditproject.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.tongji.auditproject.domain.JnAuditProjectTJ;


public interface IJnBaobiaoService extends Serializable
{  
	public List<JnAuditProjectTJ> getTjs(String areacode);
    

}
