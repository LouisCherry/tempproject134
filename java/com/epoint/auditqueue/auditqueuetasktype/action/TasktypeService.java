package com.epoint.auditqueue.auditqueuetasktype.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

import java.util.List;

public class TasktypeService {
    public List<AuditTask> gettasklist(String type, String params) {
        ICommonDao commondao = CommonDao.getInstance();
        String sql = "";
        if ("1".equals(type)) {
            sql = "select c.* from (select b.* ,a.WINDOWGUID from audit_orga_windowtask a LEFT JOIN audit_task b " +
                    " on a.TASKID = b.TASK_ID where b.is_enable='1' and( b.IS_HISTORY='' or b.IS_HISTORY = '0') and b.IS_EDITAFTERIMPORT ='1' " +
                    " and b.areacode = ? ) c INNER JOIN (select RowGuid from audit_orga_window where WINDOWTYPE ='10') d " +
                    " on c.WINDOWGUID = d.RowGuid ";
        } else {
            sql = "select c.* from (select b.* ,a.WINDOWGUID from audit_orga_windowtask a LEFT JOIN audit_task b " +
                    " on a.TASKID = b.TASK_ID where b.is_enable='1' and( b.IS_HISTORY='' or b.IS_HISTORY = '0') and b.IS_EDITAFTERIMPORT ='1' " +
                    " and b.OUGuid = ? ) c INNER JOIN (select RowGuid from audit_orga_window where WINDOWTYPE ='10') d " +
                    "on c.WINDOWGUID = d.RowGuid ";
        }
        return commondao.findList(sql, AuditTask.class, params);
    }
}
