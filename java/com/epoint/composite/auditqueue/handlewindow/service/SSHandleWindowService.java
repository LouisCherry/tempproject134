package com.epoint.composite.auditqueue.handlewindow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public class SSHandleWindowService {

    private IAuditOrgaWindowYjs auditOrgaWindowService = ContainerFactory.getContainInfo()
            .getComponent(IAuditOrgaWindowYjs.class);
    private IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
    private IAuditQueue queueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);

    Logger log = Logger.getLogger(SSHandleWindowService.class);
    /**
     * 初始化窗口缓存
     * 
     */
    public void initAuditQueueOrgaWindow() {
        HashMap<String, String> conditionMap = new HashMap<String, String>(16);
        AuditQueueOrgaWindow window = null;
        List<AuditQueueOrgaWindow> windowlists = new ArrayList<AuditQueueOrgaWindow>();
        List<AuditOrgaWindow> lstRecord = auditOrgaWindowService.getAllWindow(conditionMap).getResult();
        for (AuditOrgaWindow record : lstRecord) {
            window = new AuditQueueOrgaWindow();
            window.setWindowguid(record.getRowguid());
            window.setWindowname(record.getWindowname());
            window.setOuguid(record.getOuguid());
            FrameOu ou = ouservice.getOuByOuGuid(record.getOuguid());
            if (ou != null) {
                window.setOuname(StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
            }
            window.setUserguid("");
            window.setQueuevalue("");
            window.setCurrenthandleqno(queueservice.getCurrentHandleNO(record.getRowguid(), false).getResult());
            window.setWaitnum(
                    StringUtil.getNotNullString(queueservice.getWindowWaitNum(record.getRowguid(), false).getResult()));
            window.setWorkstatus(QueueConstant.Window_WorkStatus_NotLogin);
            // 给窗口加上个参数，窗口平均叫号的处理
            window.set("waittime", "");
            window.set("loopcount", "0");
            
            window.setCreattime(new Date());
            windowlists.add(window);
        }

        ZwfwRedisCacheUtil redis = null;
        try {
            redis = new ZwfwRedisCacheUtil(false);
            redis.delPatten("AuditQueueOrgaWindow_*");
            redis.putListByHash(windowlists);
        }
        catch (Exception e) {
            throw new RuntimeException("redis执行发生了异常", e);
        }
        finally {
            if (redis != null) {
                redis.close();
            }
        }
    }

    public void initAuditQueueOrgaWindowbyWindow(String windowguid) {

        ZwfwRedisCacheUtil redis = null;
        try {
            redis = new ZwfwRedisCacheUtil(false);
            redis.del("AuditQueueOrgaWindow_" + windowguid);
            AuditQueueOrgaWindow window = new AuditQueueOrgaWindow();
            AuditOrgaWindow orgawindow = auditOrgaWindowService.getWindowByWindowGuid(windowguid).getResult();
            window.setWindowguid(orgawindow.getRowguid());
            window.setWindowname(orgawindow.getWindowname());
            window.setOuguid(orgawindow.getOuguid());
            if (ouservice.getOuByOuGuid(orgawindow.getOuguid()) != null) {
                window.setOuname(StringUtil.isNotBlank(ouservice.getOuByOuGuid(orgawindow.getOuguid()).getOushortName())
                        ? ouservice.getOuByOuGuid(orgawindow.getOuguid()).getOushortName()
                        : ouservice.getOuByOuGuid(orgawindow.getOuguid()).getOuname());
            }
            window.setUserguid("");
            window.setQueuevalue("");
            window.setCurrenthandleqno(queueservice.getCurrentHandleNO(orgawindow.getRowguid(), false).getResult());
            window.setWaitnum(StringUtil
                    .getNotNullString(queueservice.getWindowWaitNum(orgawindow.getRowguid(), false).getResult()));
            window.setWorkstatus(QueueConstant.Window_WorkStatus_NotLogin);

            // 给窗口加上个参数，窗口平均叫号的处理
            window.set("waittime", "");
            window.set("loopcount", "0");
            window.setCreattime(new Date());
            redis.putByHash(window);
        }
        catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        finally {
            if (redis != null) {
                redis.close();
            }
        }
        log.info("初始化缓存AuditQueueOrgaWindow_" + windowguid + "成功！");
    }
}
