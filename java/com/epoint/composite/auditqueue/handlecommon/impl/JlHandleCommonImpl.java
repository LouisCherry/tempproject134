package com.epoint.composite.auditqueue.handlecommon.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.handlecommon.inter.IHandleCommon;
import com.epoint.composite.auditqueue.handlecommon.service.HandleCommonService;
import com.epoint.composite.auditqueue.handlecommon.service.JlHandleCommonService;

@Component
@Service
@Primary
public class JlHandleCommonImpl implements IHandleCommon
{
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
            String WindowNo) {
        JlHandleCommonService handlecommonservice = new JlHandleCommonService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            handlecommonservice.SendQueueMessage(QNO, WindowGuid, CenterGuid, WindowNo);
        }
        catch (Exception var8) {
            result.setSystemFail(var8.toString());
        }

        return result;
    }

    public AuditCommonResult<String> sendLEDMQ(String content, String WindowNo, String CenterGuid) {
        AuditCommonResult<String> result = new AuditCommonResult();
        JlHandleCommonService handlecommonservice = new JlHandleCommonService();

        try {
            handlecommonservice.sendLEDMQ(content, WindowNo, CenterGuid);
        }
        catch (Exception var7) {
            result.setSystemFail(var7.getMessage());
        }

        return result;
    }

    public AuditCommonResult<String> tiqianSendMessage(String WindowGuid, String CenterGuid, String WindowNo,
            String UserGuid) {
        HandleCommonService handlecommonservice = new HandleCommonService();
        AuditCommonResult result = new AuditCommonResult();

        try {
            handlecommonservice.tiqianSendMessage(WindowGuid, CenterGuid, WindowNo, UserGuid);
        }
        catch (Exception var8) {
            result.setSystemFail(var8.toString());
        }

        return result;
    }
}
