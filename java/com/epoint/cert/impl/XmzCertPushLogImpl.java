package com.epoint.cert.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.cert.api.IXmzCertPushLog;
import com.epoint.cert.domain.XmzCertPushLog;
import org.springframework.stereotype.Component;

@Component
@Service
public class XmzCertPushLogImpl implements IXmzCertPushLog {

    @Override
    public int addXmzCertPushLog(XmzCertPushLog xmzCertPushLog) {
        return new XmzCertPushLogService().addXmzCertPushLog(xmzCertPushLog);
    }
}
