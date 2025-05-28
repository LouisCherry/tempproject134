package com.epoint.hcp.job;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

public class AuditProjectWpj {

    /**
     * [查询大于24小时并且未评价数据]
     * [功能详细描述]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static List<Record> getWpjProjectList() {
        CommonDao dao = CommonDao.getInstance();

        String sql = "SELECT\n" +
                "\tAPPLYERNAME,\n" +
                "\tcontactmobile,\n" +
                "\tPROJECTNAME,\n" +
                "\tAPPLYDATE,\n" +
                "\tFLOWSN,\n" +
                "\tROWGUID,\n" +
                "\tAREACODE,\n" +
                "\tCONTACTPHONE \n" +
                "FROM\n" +
                "\taudit_project \n" +
                "WHERE\n" +
                "\t( hcpstatus IS NULL OR hcpstatus = '0' ) \n" +
                "\tand FLOWSN is not null\n" +
                "\tand `STATUS`=90\n" +
                "\tAND LENGTH( CONTACTPHONE )= 11 \n" +
                "\tAND LENGTH( contactmobile )= 11 \n" +
                "\tAND LENGTH( APPLYERNAME )> 0 \n" +
                "\tAND is_msg IS NULL  and APPLYDATE>='2021-11-09 00:00:00'\n" +
                "\tLIMIT 200";
        List<Record> messagelist = dao.findList(sql, Record.class);
        dao.close();
        return messagelist;
    }

    public static void updateProjectMsg(String rowguid) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "update audit_project set is_msg = '1' where rowguid=?";
        dao.execute(sql, rowguid);
        dao.close();
    }


    public static List<AuditProject> getProjectList() {
        CommonDao dao = CommonDao.getInstance();
        String sql = "SELECT\n" +
                "\touguid,\n" +
                "\tareacode,\n" +
                "\tTASKGUID,\n" +
                "\tRowGuid,\n" +
                "\tflowsn,\n" +
                "\tSTATUS,\n" +
                "\touname,\n" +
                "\tApplyertype,\n" +
                "\tAcceptusername,\n" +
                "\tcertnum,\n" +
                "\tTasktype,\n" +
                "\tbanjiedate \n" +
                "FROM\n" +
                "\taudit_project \n" +
                "WHERE\n" +
                "\tCERTNUM IS NOT NULL \n" +
                "\tAND `STATUS` = 90 \n" +
                "\tAND now() > SUBDATE( banjiedate, INTERVAL - 4 HOUR ) \n" +
                "\tAND banjiedate >= '2021-11-08' \n" +
                "\tLIMIT 100 ";
        List<AuditProject> list = dao.findList(sql, AuditProject.class);
        return list;
    }

    public static void updateProjectByGuid(String rowguid, String s) {
        CommonDao dao = CommonDao.getInstance();
        try {
            dao.beginTransaction();
            String sql = "update audit_project set hcpstatus = '" + s + "' where rowguid= '" + rowguid + "' ";
            dao.execute(sql);
            dao.commitTransaction();
        } catch (Exception e) {
            dao.rollBackTransaction();

        } finally {
            dao.close();
        }
    }
}
