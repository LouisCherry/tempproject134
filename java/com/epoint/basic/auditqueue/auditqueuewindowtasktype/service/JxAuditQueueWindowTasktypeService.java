package com.epoint.basic.auditqueue.auditqueuewindowtasktype.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.string.StringUtil;

import java.util.List;

public class JxAuditQueueWindowTasktypeService {

    /**
     * 通用dao操作
     */
    private CommonDao commonDao;

    public JxAuditQueueWindowTasktypeService() {
        commonDao = CommonDao.getInstance();
    }

    public void deletebyTaskTypeGuid(String TaskTypeGuid) {
        String sql = "delete from AUDIT_QUEUE_WINDOW_TASKTYPE where TaskTypeGuid=?1";
        commonDao.execute(sql, TaskTypeGuid);
    }

    public void deletebyWindowguid(String windowguid) {
        String sql = "delete from AUDIT_QUEUE_WINDOW_TASKTYPE where windowguid=?1";
        commonDao.execute(sql, windowguid);
    }

    public List<String> getWindowListByTaskTypeGuid(String TaskTypeGuid) {
        String sql = "select a.windowguid from Audit_Queue_Window_TaskType a,audit_orga_window b where a.windowguid = b.rowguid and a.tasktypeguid = ? ORDER BY b.ordernum";
        List<String> windowguids = commonDao.findList(sql, String.class, TaskTypeGuid);
        return windowguids;
    }

    public boolean IsSameQueuevalue(String windowguid) {

        String sql = "select distinct QueueValue from audit_queue_tasktype,audit_queue_window_tasktype where audit_queue_tasktype.RowGuid=audit_queue_window_tasktype.tasktypeguid and windowguid=?1";

        List<String> QueueValues = commonDao.findList(sql, String.class, windowguid);
        if (QueueValues != null && QueueValues.size() > 0) {
            if (QueueValues.size() > 1) {
                return false;
            } else {
                if (StringUtil.isNotBlank(QueueValues.get(0))) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return true;
        }
    }

}
