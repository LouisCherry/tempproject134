package com.epoint.cert.impl;

import com.epoint.cert.domain.XmzCertPushLog;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class XmzCertPushLogService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public XmzCertPushLogService() {
        baseDao = CommonDao.getInstance();
    }

    public int addXmzCertPushLog(XmzCertPushLog xmzCertPushLog) {
        return baseDao.insert(xmzCertPushLog);
    }
}
