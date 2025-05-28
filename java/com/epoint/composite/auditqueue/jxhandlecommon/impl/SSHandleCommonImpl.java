package com.epoint.composite.auditqueue.jxhandlecommon.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.jxhandlecommon.inter.IJXHandleCommon;
import com.epoint.composite.auditqueue.jxhandlecommon.inter.ISSHandleCommon;
import com.epoint.composite.auditqueue.jxhandlecommon.service.JXHandleCommonService;
import com.epoint.composite.auditqueue.jxhandlecommon.service.SSHandleCommonService;
import org.springframework.stereotype.Component;

@Component
@Service
public class SSHandleCommonImpl implements ISSHandleCommon
{


    @Override
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
            String WindowNo) {
        SSHandleCommonService handlecommonservice = new  SSHandleCommonService();
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

        SSHandleCommonService handlecommonservice = new  SSHandleCommonService();
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
        SSHandleCommonService handlecommonservice = new  SSHandleCommonService();
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
