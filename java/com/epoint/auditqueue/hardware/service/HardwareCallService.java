package com.epoint.auditqueue.hardware.service;


import java.util.List;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;

public class HardwareCallService
{
   public AuditOrgaWindow getWindowByMac(String hardno){
       ICommonDao commonDao = CommonDao.getInstance();
       String sql="select * from audit_orga_window where hardwaremac=?1";
       AuditOrgaWindow window = commonDao.find(sql, AuditOrgaWindow.class, hardno);
    return window;
       
   }
   public String getQno(String handlewindowguid) {
       String sql = "select qno from audit_queue where HANDLEWINDOWGUID = ?1 and status=1";
       ICommonDao commonDao = CommonDao.getInstance();
       String qno = commonDao.queryString(sql, handlewindowguid);
       if (StringUtil.isBlank(qno)) {
           return "无办理人员！";
       }
       return qno;
   }
   
   public List<AuditOrgaWindowUser> getWindowUser(String windowguid){
       ICommonDao commonDao = CommonDao.getInstance();
       String sql = "select * from audit_orga_windowuser where windowguid=?1";
       List<AuditOrgaWindowUser> list = commonDao.findList(sql, AuditOrgaWindowUser.class, windowguid);
    return list;
   }
}
