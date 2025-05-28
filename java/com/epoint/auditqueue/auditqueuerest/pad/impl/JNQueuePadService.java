package com.epoint.auditqueue.auditqueuerest.pad.impl;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

public class JNQueuePadService extends AuditCommonService {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
    IAuditQueueWindowTasktype windowtasktypeservice = ContainerFactory.getContainInfo()
            .getComponent(IAuditQueueWindowTasktype.class);
    IAuditQueueTasktype tasktypeservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueTasktype.class);
    IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueOrgaWindow.class);
    Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public String selectqno(String qno, String WindowGuid, String WindowNo, String CenterGuid, String UserGuid) {

        String NextQueueNO = "";
        String TaskGuid = "";
        String QueueTableName = "Audit_Queue_Instance";
        String Queuevalue = "";
        String sql = "UPDATE audit_queue set `STATUS`='2' where HANDLEWINDOWGUID=? AND `STATUS` ='1'";
        commonDao.execute(sql, WindowGuid);
        AuditQueue queue = queueservice.getQNODetailByQNO(" QNO,taskGuid,turnqnotype,rowguid ", qno, CenterGuid)
                .getResult();
        if (StringUtil.isNotBlank(queue)) {
            NextQueueNO = queue.getQno();
            TaskGuid = queue.getTaskguid();
            AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(TaskGuid).getResult();
            if (StringUtil.isNotBlank(tasktype) && StringUtil.isNotBlank(tasktype.getQueuevalue())) {
                Queuevalue = tasktype.getQueuevalue();
                QueueTableName += "_" + Queuevalue;
                // 删除实例表数据
                commonDao.execute("delete from " + QueueTableName + " where qno='" + NextQueueNO + "'");
                commonDao.commitTransaction();
                // 更新排队状态
                updateQNOStatusandHandleWindow(QueueConstant.Qno_Status_Processing, queue.getRowguid(), WindowGuid,
                        UserGuid, new Date());
                ZwfwRedisCacheUtil redis = null;
                try {
                    redis = new ZwfwRedisCacheUtil(false);
                    List<String> windowguids = windowtasktypeservice.getWindowListByTaskTypeGuid(TaskGuid).getResult();
                    for (String para : windowguids) {
                        // redis 计数-1
                        redis.hincrBy("AuditQueueOrgaWindow_" + para, "waitnum", -1);
                        windowservice.updateCurrentHandleNO(WindowGuid, NextQueueNO);
                    }
                } finally {
                    if (redis != null) {
                        redis.close();
                    }
                }
            } else {
                NextQueueNO = "";
            }
        }

        if (StringUtil.isNotBlank(NextQueueNO)) {
            qno = NextQueueNO;

        } else {
            // led推送（空闲）
            qno = QueueConstant.Current_None;
        }

        return qno;
    }

    public void updateQNOStatusandHandleWindow(String Status, String queueguid, String Windowguid,
                                               String handleuserguid, Date CallTime) {
        String sql = "update AUDIT_QUEUE set Status=?,HANDLEWINDOWGUID=?,handleuserguid=?,CallTime=? where rowguid=? ";

        if (commonDao.isSqlserver()) {
            sql += " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111) ";
        } else if (commonDao.isOracle()) {
            sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
        } else if (commonDao.isDM()) {
            sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
        } else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD')";
        } else {
            sql += " and  date(GETNOTIME) = curdate() ";
        }
        commonDao.execute(sql, Status, Windowguid, handleuserguid, CallTime, queueguid);
    }

    public AuditQueue getqnodatailbyflownno(String flowno) {
        String sql = "select * from  audit_queue where Flowno =?";
        return commonDao.find(sql, AuditQueue.class, flowno);
    }
}
