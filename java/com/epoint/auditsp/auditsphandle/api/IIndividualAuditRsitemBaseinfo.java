package com.epoint.auditsp.auditsphandle.api;

import java.util.List;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;


public interface IIndividualAuditRsitemBaseinfo extends IAuditRsItemBaseinfo {
	
	
	//通过项目代码获取项目信息
	public AuditRsItemBaseinfo getAuditRsItemBaseinfobyitemcode(String itemcode);

	public List<AuditRsItemBaseinfo> selectIndividualByLikeIDNumber(String query,String xiaqucode);

	public List<AuditRsItemBaseinfo> selectIndividualByLikeIDname(String query,String xiaqucode);



}
