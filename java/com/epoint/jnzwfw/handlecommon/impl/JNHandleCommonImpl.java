package com.epoint.jnzwfw.handlecommon.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.handlecommon.impl.HandleCommonImpl;
import com.epoint.jnzwfw.handlecommon.api.IJNHandleCommon;
import com.epoint.jnzwfw.handlecommon.service.JNHandleCommonService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Date;

@Component
@Service
public class JNHandleCommonImpl extends HandleCommonImpl implements IJNHandleCommon {

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public AuditCommonResult<String> sendQueueMessage(String QNO, String WindowGuid, String CenterGuid,
                                                      String WindowNo) {
        JNHandleCommonService handlecommonservice = new JNHandleCommonService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            handlecommonservice.SendQueueMessage(QNO, WindowGuid, CenterGuid, WindowNo);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

}
