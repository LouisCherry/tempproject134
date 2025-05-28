package com.epoint.lsznsb.auditqueue.handlecommon.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.lsznsb.auditqueue.handlecommon.inter.LsIHandleCommon;
import com.epoint.lsznsb.auditqueue.handlecommon.service.LsHandleCommonService;

@Component
@Service
public class LsHandleCommonImpl implements LsIHandleCommon
{

    @Override
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
            String WindowNo) {
        LsHandleCommonService handlecommonservice = new LsHandleCommonService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            handlecommonservice.SendQueueMessage(QNO, WindowGuid, CenterGuid, WindowNo);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> sendLEDMQ(String content, String WindowNo, String CenterGuid) {
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        LsHandleCommonService handlecommonservice = new LsHandleCommonService();
        try {
            handlecommonservice.sendLEDMQ(content, WindowNo, CenterGuid);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> tiqianSendMessage(String WindowGuid, String CenterGuid, String WindowNo,
            String UserGuid) {
        LsHandleCommonService handlecommonservice = new LsHandleCommonService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            handlecommonservice.tiqianSendMessage(WindowGuid, CenterGuid, WindowNo, UserGuid);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
