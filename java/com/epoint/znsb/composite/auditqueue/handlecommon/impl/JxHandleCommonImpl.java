package com.epoint.znsb.composite.auditqueue.handlecommon.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.znsb.composite.auditqueue.handlecommon.inter.JxIHandleCommon;
import com.epoint.znsb.composite.auditqueue.handlecommon.service.JxHandleCommonService;

@Component
@Service
public class JxHandleCommonImpl implements JxIHandleCommon
{


    @Override
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
            String WindowNo) {
        JxHandleCommonService JxHandleCommonService = new JxHandleCommonService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            JxHandleCommonService.SendQueueMessage(QNO, WindowGuid, CenterGuid, WindowNo);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> sendLEDMQ(String content, String WindowNo, String CenterGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();

        JxHandleCommonService JxHandleCommonService = new JxHandleCommonService();
        try {
            JxHandleCommonService.sendLEDMQ(content, WindowNo, CenterGuid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> tiqianSendMessage(String WindowGuid, String CenterGuid, String WindowNo,
            String UserGuid) {
        JxHandleCommonService JxHandleCommonService = new JxHandleCommonService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            JxHandleCommonService.tiqianSendMessage(WindowGuid, CenterGuid, WindowNo, UserGuid);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

}
