package com.epoint.auditsp.auditsphandle.impl;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditsphandle.api.IIndividualAuditRsitemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.impl.AuditRsItemBaseinfoImpl;
import com.epoint.core.dao.CommonDao;

@Component
@Service
@Primary
public class IndividualAuditRsitemBaseinfoImpl extends AuditRsItemBaseinfoImpl implements IIndividualAuditRsitemBaseinfo{

	
	
	@Override
	public AuditRsItemBaseinfo getAuditRsItemBaseinfobyitemcode(String itemcode) {
		CommonDao commonDao=new CommonDao();
		String sql="select * from audit_rs_item_baseinfo where itemcode=? and dlguid is null";
		AuditRsItemBaseinfo auditRsItemBaseinfo=commonDao.find(sql, AuditRsItemBaseinfo.class, itemcode);
		return auditRsItemBaseinfo;
	}

	@Override
	public List<AuditRsItemBaseinfo> selectIndividualByLikeIDNumber(String query,String xiaqucode) {
		CommonDao commonDao=new CommonDao();
		String sql="select * from audit_rs_item_baseinfo where itemcode like '%"+query+"%' and dlguid is null and BelongXiaQuCode=?";
		List<AuditRsItemBaseinfo> auditRsItemBaseinfolist =commonDao.findList(sql, AuditRsItemBaseinfo.class,xiaqucode);
		return auditRsItemBaseinfolist;
	}

	@Override
	public List<AuditRsItemBaseinfo> selectIndividualByLikeIDname(String query,String xiaqucode) {
		CommonDao commonDao=new CommonDao();
		String sql="select * from audit_rs_item_baseinfo where itemname like '%"+query+"%' and BelongXiaQuCode=? and dlguid is null";
		List<AuditRsItemBaseinfo> auditRsItemBaseinfolist =commonDao.findList(sql, AuditRsItemBaseinfo.class,xiaqucode);
		return auditRsItemBaseinfolist;
	}

}
