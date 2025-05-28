package com.epoint.auditqueue.hardware.api;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;

@Service
public interface IHardwarecall
{
  public AuditOrgaWindow getWindowByMac(String hardno);
  public  String getQno(String handlewindowguid);
  public List<AuditOrgaWindowUser> getWindowOrgaWindowUserByWindowguid(String windowguid);
}
