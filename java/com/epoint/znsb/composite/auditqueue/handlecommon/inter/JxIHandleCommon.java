/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.znsb.composite.auditqueue.handlecommon.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface JxIHandleCommon {
    AuditCommonResult<String> sendQueueMessage(String arg0, String arg1, String arg2, String arg3);

    AuditCommonResult<String> tiqianSendMessage(String arg0, String arg1, String arg2, String arg3);

    AuditCommonResult<String> sendLEDMQ(String arg0, String arg1, String arg2);
}