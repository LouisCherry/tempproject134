/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.znsb.composite.auditqueue.handlequeue.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import java.util.Date;
import java.util.Map;

@Service
public interface JxIHandleQueue {
    AuditCommonResult<Map<String, String>> getQNO(String arg0, String arg1, String arg2, String arg3, String arg4);

    AuditCommonResult<Map<String, String>> getAPQNO(String arg0, String arg1);

    AuditCommonResult<Integer> getTaskWaitNum(String arg0, Boolean arg1);

    AuditCommonResult<Map<String, String>> getTurnWindowQNO(String arg0, String arg1, String arg2, String arg3);

    AuditCommonResult<Map<String, String>> getTurnTaskQNO(String arg0, String arg1, String arg2, String arg3);

    AuditCommonResult<String> getNextQNO(String arg0, String arg1, String arg2, String arg3, Boolean arg4);

    AuditCommonResult<Integer> getQueueCount(String arg0, Date arg1, Date arg2);
}