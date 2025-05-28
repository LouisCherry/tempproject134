package com.epoint.auditsp.auditsptask.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditsptask.api.IJNAuditWindowTaskService;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.core.dao.CommonDao;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增市县同体service
 *
 * @author xbn
 * @version [版本号, 2017-04-05 11:23:36]
 */
@Component
@Service
public class JNAuditWindowTaskService implements IJNAuditWindowTaskService {

    public List<AuditOrgaArea> getAllArea() {
        String sql = "select * from audit_orga_area";
        return CommonDao.getInstance().findList(sql, AuditOrgaArea.class);
    }

    public String getCenterguidBytaskguid(String guid) {
        String sql = "SELECT c.RowGuid centerguid FROM  audit_orga_servicecenter c  LEFT JOIN audit_task t ON t.AREACODE=c.BELONGXIAQU WHERE t.RowGuid = ? ";
        return CommonDao.getInstance().queryString(sql, guid);

    }
}
