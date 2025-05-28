package com.epoint.jiningzwfw.importstatistics.service;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportService {

    private ICommonDao baseDao;

    public ImportService(){
//        baseDao = CommonDao.getInstance();
    }




    public List<AuditOrgaArea> getAreaWithLevel(int level, String XiaQuCode) {
        String sql = "select * from AUDIT_ORGA_AREA where CityLevel=? ";
        CommonDao dao = CommonDao.getInstance();
        List<AuditOrgaArea> list;
        if (StringUtil.isNotBlank(XiaQuCode)){
            sql += "and xiaqucode like ?";
            list = dao.findList(sql, AuditOrgaArea.class, level, XiaQuCode + "___");
            dao.close();
            return list;
        }
        list = dao.findList(sql, AuditOrgaArea.class, level);
        dao.close();
        return list;
    }

    public String getAreaCode(String ouguid) {
        String sql = "select xiaqucode from AUDIT_ORGA_AREA where ouguid=? ";
        CommonDao dao = CommonDao.getInstance();
        String areacode = dao.queryString(sql, ouguid);
        dao.close();
        return areacode;
    }

    public Record findProjectStatistics(Map<String, String> map) {

        CommonDao dao = CommonDao.getInstance();
        String table = map.get("table");
        if (StringUtil.isBlank(table)){
            return new Record();
        }
        String ouGuid = map.get("ouGuid");
        String areacode = map.get("areacode");
        String start = map.get("start");
        String end = map.get("end");
        String sql = "select count(1) count from " + table + " where 1=1";
        if (StringUtil.isNotBlank(ouGuid)){
            ouGuid.replaceAll(",","','");
            sql += " and ouguid in ('" + ouGuid + "')";
        }
        if (StringUtil.isNotBlank(areacode)){
            sql += " and areacode='" + areacode + "'";
        }
        if (StringUtil.isNotBlank(start)){
            sql += " and APPLYDATE between '" + start + "' and '" + end + "'";
        }
        //system.out.println(sql);
        Record record = dao.find(sql,Record.class);
        dao.close();
        return record;
    }

    public Record findProjectStatistics1(Map<String, String> map) {

        CommonDao dao = CommonDao.getInstance();
        String table = map.get("table");
        if (StringUtil.isBlank(table)){
            return new Record();
        }
        String ouGuid = map.get("ouGuid");
        String start = map.get("start");
        String end = map.get("end");
        StringBuilder sb = new StringBuilder();
        sb.append("select SUM(COUNT) count  from ( select count(1) count from  external_project_info_1 where 1=1");
        if (StringUtil.isNotBlank(ouGuid)){
            ouGuid = ouGuid.replaceAll(",","','");
            sb.append(" and accept_ou_guid in ('");
            sb.append(ouGuid);
            sb.append("')");
        }
        if (StringUtil.isNotBlank(start)){
            sb.append(" and accept_date between '");
            sb.append(start);
            sb.append("' and '");
            sb.append(end);
            sb.append("'");
        }

        for (int i = 1; i < 12; i++) {

            sb.append(" union all ");
            sb.append(" select count(1) count from  external_project_info_" + (i+1));
            sb.append(" where 1=1");
            if (StringUtil.isNotBlank(ouGuid)){
                sb.append(" and accept_ou_guid in ('");
                sb.append(ouGuid);
                sb.append("')");
            }
            if (StringUtil.isNotBlank(start)){
                sb.append(" and accept_date between '");
                sb.append(start);
                sb.append("' and '");
                sb.append(end);
                sb.append("'");
            }
            if (i == 11){
                continue;
            }
        }
        sb.append(") a");

        //system.out.println(sb.toString());
        Record record = dao.find(sb.toString(),Record.class);
        dao.close();
        return record;
    }


    public Record findEvaInstanceStatistics(HashMap<String, String> map) {

        CommonDao dao = CommonDao.getInstance();
        String table = map.get("table");
        if (StringUtil.isBlank(table)){
            return new Record();
        }
        String areacode = map.get("areacode");
        String start = map.get("start");
        String end = map.get("end");
        String sql = "select count(1) total,ifnull(sum(if(sbsign=1,1,0)),0) success,ifnull(sum(if(sbsign=99,1,0)),0) fail, ifnull(sum(if(sbsign=1,1,0))/count(1),0) rate from " + table + " where 1=1";
        if (StringUtil.isNotBlank(areacode)){
            sql += " and areacode in " + areacode;
        }
        if (StringUtil.isNotBlank(start)){
            sql += " and createdate between '" + start + "' and '" + end + "'";
        }
        //system.out.println(sql);
        Record record = dao.find(sql, Record.class);
        dao.close();
        return record;
    }
}
