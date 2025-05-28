package com.epoint.yutai.queuewindow.impl;
import com.epoint.common.service.AuditCommonService;

import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;

import java.util.*;

public class YTHandleQueueService extends AuditCommonService
{
    /**
     * 
     */
    Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final long serialVersionUID = 1L;


    public List<String> getWaitQno(String queuevalue, String windowguid) {
        if(StringUtil.isBlank(queuevalue)){
            String getqueuevalue = "select queuevalue  from audit_queue_tasktype where rowguid in (select tasktypeguid from audit_queue_window_tasktype where windowguid = ?) limit 1";
            queuevalue = commonDao.queryString(getqueuevalue,windowguid);
        }
        String newsql = "select qno from audit_queue_instance_" + queuevalue + " where handlewindows like ? order by getnotime,queueweight desc";
        List<String> qnolist = commonDao.findList(newsql,String.class,"%"+windowguid+"%");
        return qnolist;
    }
}
