package com.epoint.composite.auditqueue.handlewindow.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface ISSHandleWindow
{

    /**
     * 初始化所有窗口
     * @return
     */
    public AuditCommonResult<String> initAuditQueueOrgaWindow();

    /**
     * 初始化单个窗口
     * @param windowguid 窗口guid
     * @return
     */
    public AuditCommonResult<String> initAuditQueueOrgaWindowbyWindow(String windowguid);

}
