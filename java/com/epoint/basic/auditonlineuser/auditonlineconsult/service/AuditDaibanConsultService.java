package com.epoint.basic.auditonlineuser.auditonlineconsult.service;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsult;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public class AuditDaibanConsultService extends AuditCommonService
{
    /**
     * 
     */
    private static final long serialVersionUID = 6236105714707284176L;

    /**
     * 
     * 获取用户名
     */

    public Integer getAnswerCount(String askerGuid, Boolean isAsked, String cosulttype) {
        String sql = "";
        if (isAsked) {
            sql = "  select COUNT(1) from AUDIT_DAIBAN_CONSULT where ASKERUSERGUID = ?1 AND  (STATUS = 1 or STATUS = 3) and CONSULTTYPE=?2 ";
        }
        else {
            sql = "  select COUNT(1) from AUDIT_DAIBAN_CONSULT where ASKERUSERGUID = ?1 AND  (STATUS = 0 or STATUS = 2) and CONSULTTYPE=?2 ";
        }
        Integer result = commonDao.queryInt(sql, askerGuid, cosulttype);
        return result;
    }

    public PageData<AuditDaibanConsult> getAuditOnlineConsultListByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder) {
        PageData<AuditDaibanConsult> pageData = new PageData<AuditDaibanConsult>();
        SQLManageUtil sm = new SQLManageUtil();
        String order = "";
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            order = " order by " + sortField + " " + sortOrder;
        }
        String sqle = sm.buildSql(conditionMap);
        String sql = "select * from AUDIT_DAIBAN_CONSULT " + sqle + order;
        String sqlcount = sql.replace("*", "count(1)");
        List<AuditDaibanConsult> auditList = commonDao.findList(sql, first, pageSize, AuditDaibanConsult.class);
        pageData.setList(auditList);
        pageData.setRowCount(commonDao.queryInt(sqlcount));
        return pageData;
    }

}
