package com.epoint.auditsp.auditsphandle.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditsphandle.api.IJnAuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class IJnAuditSpInstaceimpl implements IJnAuditSpInstance
{

    @Override
    public void updateSpInstance(AuditSpInstance ins) {
        JnAuditSpInstaceService service = new JnAuditSpInstaceService();
        service.update(ins);
    }

	@Override
	public List<AuditSpInstance> getAuditSpInstanceByPage(int first, int pageSize, String applyername,String itemname) {
		return new JnAuditSpInstaceService().getAuditSpInstanceByPage(first,pageSize,applyername,itemname);
	}
	
	@Override
	public Integer getAuditSpInstanceCount(String applyername,String itemname) {
		return new JnAuditSpInstaceService().getAuditSpInstanceCount(applyername,itemname);
	}

	@Override
	public List<AuditSpInstance> getAuditSpInstanceListBySearch(int first, int pageSize, String itemcode, String itemname, String areacode) {
		return new JnAuditSpInstaceService().getAuditSpInstanceListBySearch(first,pageSize,itemcode,itemname,areacode);
	}

	@Override
	public int getAuditSpInstanceCountNew(String itemcode, String itemname, String areacode) {
		return new JnAuditSpInstaceService().getAuditSpInstanceCountNew(itemcode,itemname,areacode);
	}

}
