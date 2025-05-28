package com.epoint.consultbyou.impl;

import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditOnlineConsult;
import com.epoint.basic.authentication.UserSession;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import java.util.ArrayList;
import java.util.List;

public class ConsultByouImpl {


    public List<AuditOnlineConsult> getConsultByou(String startDate, String endDate, String areacode, String ouguid, String type, String status) {

        String userguid = ConfigUtil.getConfigValue("userguid");

        String userguid2 = UserSession.getInstance().getUserGuid();

        List<AuditOnlineConsult> list = new ArrayList<>();

        if (userguid2.equals(userguid)) {
            String sql = "SELECT a.RowGuid,d.XIAQUNAME,b.OUNAME,DATE_FORMAT(a.ASKDATE,'%Y-%m-%d') as ASKDATE,DATE_FORMAT(a.ANSWERDATE,'%Y-%m-%d') as ANSWERDATE,a.title,DATE_FORMAT(c.QADATE,'%Y-%m-%d') as QADATE from audit_online_consult a "
                    + " join frame_ou b on a.OUGUID=b.OUGUID LEFT JOIN audit_online_consult_ext c on a.RowGuid=c.CONSULTGUID"
                    + "  join audit_orga_area d on a.areacode=d.XiaQuCode  where 1=1 ";
            if (StringUtil.isNotBlank(ouguid)) {
                sql += " and (";
                String[] ouguids = ouguid.split(",");
                for (int i = 0; i < ouguids.length; i++) {
                    sql += "  a.ouguid='" + ouguids[i] + "'";
                    if ((i + 1) != ouguids.length) {
                        sql += " or ";
                    }
                }
                sql += " ) ";
            }

            if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
                sql += " and DATE_FORMAT(a.ASKDATE,'%Y-%m-%d')>='" + startDate + "'"
                        + " and DATE_FORMAT(a.ASKDATE,'%Y-%m-%d')<='" + endDate + "'";
            }
            if (StringUtil.isNotBlank(type)) {
                if (!"0".equals(type)) {
                    sql += " and a.CONSULTTYPE=?1";
                }
            }
            if (StringUtil.isNotBlank(status)) {
                sql += " and a.status=?2";
            }
            sql += " and a.status != 9 ";
            sql += " ORDER BY areacode,a.ASKDATE ";
            list = CommonDao.getInstance().findList(sql, AuditOnlineConsult.class, type, status);
        } else {
            String sql = "SELECT a.RowGuid,d.XIAQUNAME,b.OUNAME,DATE_FORMAT(a.ASKDATE,'%Y-%m-%d') as ASKDATE,DATE_FORMAT(a.ANSWERDATE,'%Y-%m-%d') as ANSWERDATE,a.title,DATE_FORMAT(c.QADATE,'%Y-%m-%d') as QADATE from audit_online_consult a "
                    + " join frame_ou b on a.OUGUID=b.OUGUID LEFT JOIN audit_online_consult_ext c on a.RowGuid=c.CONSULTGUID "
                    + "  join audit_orga_area d on a.areacode=d.XiaQuCode  where 1=1 and areacode=?1";
            if (StringUtil.isNotBlank(ouguid)) {
                sql += " and (";
                String[] ouguids = ouguid.split(",");
                for (int i = 0; i < ouguids.length; i++) {
                    sql += "  a.ouguid='" + ouguids[i] + "'";
                    if ((i + 1) != ouguids.length) {
                        sql += " or ";
                    }
                }
                sql += " ) ";
            }
            if (StringUtil.isNotBlank(startDate) && StringUtil.isNotBlank(endDate)) {
                sql += " and DATE_FORMAT(a.ASKDATE,'%Y-%m-%d')>='" + startDate + "'"
                        + " and DATE_FORMAT(a.ASKDATE,'%Y-%m-%d')<='" + endDate + "'";
            }
            if (StringUtil.isNotBlank(type)) {
                if (!"0".equals(type)) {
                    sql += " and a.CONSULTTYPE=?2";
                }
            }
            if (StringUtil.isNotBlank(status)) {
                sql += " and a.status=?3";
            }
            sql += " and a.status != 9 ";
            sql += " ORDER BY a.ASKDATE ";
            list = CommonDao.getInstance().findList(sql, AuditOnlineConsult.class, areacode, type, status);
        }


        return list;
    }

    public List<FrameOu> getOUList(String areaCode) {
        String sql = "select * from frame_ou where OUCODE like '%" + areaCode + "%'";
        return CommonDao.getInstance().findList(sql, FrameOu.class);
    }

}
