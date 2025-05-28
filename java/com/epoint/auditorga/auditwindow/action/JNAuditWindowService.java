package com.epoint.auditorga.auditwindow.action;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class JNAuditWindowService {
    public void Updateaudiywindow(String rowguid, String indicating, String childindicating) {
        ICommonDao commondao = CommonDao.getInstance();
        String sql = "update audit_orga_window set indicating = ? ,childindicating = ? where RowGuid = ? ";
        commondao.execute(sql, indicating, childindicating, rowguid);
    }

    public Record getauditwindow(String rowguid) {
        ICommonDao commondao = CommonDao.getInstance();
        String sql = "select * from audit_orga_window where RowGuid = ?";
        return commondao.find(sql, Record.class, rowguid);
    }

    public AuditOrgaWindow getauditwindowdetail(String rowguid) {
        ICommonDao commondao = CommonDao.getInstance();
        String sql = "select windowbh from audit_orga_window where RowGuid = ?";
        return commondao.find(sql, AuditOrgaWindow.class, rowguid);
    }
}
