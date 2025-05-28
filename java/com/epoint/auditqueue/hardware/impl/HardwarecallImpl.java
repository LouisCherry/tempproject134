package com.epoint.auditqueue.hardware.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditqueue.hardware.api.IHardwarecall;
import com.epoint.auditqueue.hardware.service.HardwareCallService;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;


@Component
@Service
public class HardwarecallImpl implements IHardwarecall
{

    @Override
    public AuditOrgaWindow getWindowByMac(String hardno) {
        HardwareCallService service = new HardwareCallService();
        return service.getWindowByMac(hardno);
    }

    @Override
    public List<AuditOrgaWindowUser> getWindowOrgaWindowUserByWindowguid(String windowguid) {
        HardwareCallService service = new HardwareCallService();
        return service.getWindowUser(windowguid);
    }

	@Override
	public String getQno(String handlewindowguid) {
		HardwareCallService service = new HardwareCallService();
        return service.getQno(handlewindowguid);
	}

   

}
