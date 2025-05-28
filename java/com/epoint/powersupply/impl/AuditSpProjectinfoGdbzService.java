package com.epoint.powersupply.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.powersupply.entity.AuditSpProjectinfoGdbz;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AuditSpProjectinfoGdbzService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public int insert(AuditSpProjectinfoGdbz auditSpProjectinfoGdbz) {
        ICommonDao commonDao = CommonDao.getInstance();
        return commonDao.insert(auditSpProjectinfoGdbz);
    }

    public int update(AuditSpProjectinfoGdbz auditSpProjectinfoGdbz) {
        ICommonDao commonDao = CommonDao.getInstance();
        return commonDao.update(auditSpProjectinfoGdbz);
    }

    public AuditSpProjectinfoGdbz getBySubAppGuid(String subAppGuid) {
        ICommonDao commonDao = CommonDao.getInstance();
        // 因为供电报装办件表会有多个相同subappGuid的数据，所以查询的时候，只查询没有设置projectGuid的数据
        String sql = "select * from audit_sp_projectinfo_gdbz where subappGuid = '" + subAppGuid + "' and projectguid is null";
        log.info("根据subAppGuid查询供电报装办件sql--->" + sql);
        return commonDao.find(sql, AuditSpProjectinfoGdbz.class);
    }
}
