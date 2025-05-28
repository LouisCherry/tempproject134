package com.epoint.znhdt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Component
@Service
public class ZnhdtServiceImpl implements IZnhdtService
{
    public Record getProjectCount(String areacode) {
        Date nowDate = new Date();
        Date beginDate = EpointDateUtil.getBeginOfDate(nowDate);
        // 当年
        String year = EpointDateUtil.convertDate2String(nowDate, "yyyy");
        // 当月
        String month = EpointDateUtil.convertDate2String(nowDate, "yyyy-MM");
        // 前366天
        Date oldDate = EpointDateUtil.addDay(nowDate, -366);
        oldDate = EpointDateUtil.getBeginOfDate(oldDate);

        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "SELECT count(1) lj, ROUND(count(case when APPLYDATE<? and APPLYDATE>=? then 1 else null end)/365) rj, count(case when DATE_FORMAT(APPLYDATE,'%Y-%m')=? then 1 else null end) dy, count(case when DATE_FORMAT(APPLYDATE,'%Y')=? then 1 else null end) dn FROM audit_project a LEFT JOIN audit_task b ON a.TASKGUID = b.RowGuid WHERE 1 = 1 AND a. STATUS >= 12 AND a. STATUS != 20 AND ( a.is_lczj != '2' OR a.is_lczj IS NULL ) and a.AREACODE like ? ";
        }
        else {
            return new Record();
        }

        return commonDao.find(sql, Record.class, new Object[] {beginDate, oldDate, month, year, areacode });
    }

    public List<Record> getBusinessCountList(String areacode, int limitInt) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from ( SELECT d.businessname, sum( CASE WHEN b. STATUS > 10 THEN 1 ELSE 0 END ) AS sb FROM audit_sp_instance a, audit_sp_i_subapp b, audit_orga_area c,audit_sp_business d WHERE a.businesstype = '2' AND a.RowGuid = b.BIGUID and a.BUSINESSGUID=d.RowGuid AND a.areacode = c.XiaQuCode and a.areacode like ? GROUP BY a.BUSINESSGUID ) x order by sb desc";
        }
        else {
            return new ArrayList<Record>();
        }
        return commonDao.findList(sql, 0, limitInt, Record.class, new Object[] {areacode });
    }

    public List<Record> getTownMapProjectCountList(String areacode) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "SELECT o.xiaqucode,o.xiaquname,ifnull(t.allnum,0) allnum FROM ( SELECT SUBSTRING(ACCEPTAREACODE FROM 1 FOR 9) areacode, SUM( CASE WHEN (STATUS > 12 AND STATUS != 20) THEN 1 ELSE 0 END ) AS allnum FROM audit_project WHERE 1 = 1 AND STATUS > 12 AND areacode = ? GROUP BY SUBSTRING(ACCEPTAREACODE FROM 1 FOR 9) ) t RIGHT JOIN AUDIT_ORGA_AREA o ON t.areacode = o.XiaQuCode where o.baseareacode=? and o.citylevel='3'";
        }
        else {
            return new ArrayList<Record>();
        }
        return commonDao.findList(sql, Record.class, new Object[] {areacode, areacode });
    }

    public List<Record> getNowMonthWinProjectCountList(String areacode, int limitInt) {
        Date nowDate = new Date();
        // 当月
        String month = EpointDateUtil.convertDate2String(nowDate, "yyyy-MM");

        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "SELECT w.windowname, w.windowno, ifnull(t.allnum,0) allnum, ifnull(t.slnum,0) slnum, ifnull(t.sldate,0) sldate, ifnull(t.bjnum,0) bjnum FROM ( SELECT WINDOWGUID, SUM( CASE WHEN (STATUS > 12 AND STATUS != 20) THEN 1 ELSE 0 END ) AS allnum,SUM(CASE WHEN STATUS >= 30 THEN 1 ELSE 0 END ) as slnum, SUM(CASE WHEN STATUS >= 30 THEN TIMESTAMPDIFF(SECOND, ACCEPTUSERDATE, BANJIEDATE) ELSE 0 END ) as sldate, SUM( CASE WHEN STATUS >= 90 THEN 1 ELSE 0 END ) AS bjnum FROM audit_project WHERE 1 = 1 AND STATUS > 12 AND areacode = ? and DATE_FORMAT(APPLYDATE,'%Y-%m')=? GROUP BY WINDOWGUID ) t RIGHT JOIN audit_orga_window w ON t.WINDOWGUID = w.RowGuid WHERE 1=1 ORDER BY t.allnum DESC";
        }
        else {
            return new ArrayList<Record>();
        }
        return commonDao.findList(sql, 0, limitInt, Record.class, new Object[] {areacode, month });
    }

    public PageData<Record> getBusinessDataList(String areacode, int first, int pagesize) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        String countsql = "";
        if (commonDao.isMySql()) {
            sql = "select *,TIMESTAMPDIFF(MINUTE,ACCEPTUSERDATE,BANJIEDATE) as processtime from ( SELECT d.businessname,b.status as yjsstatus,GROUP_CONCAT(po.OUNAME) ouname,GROUP_CONCAT(p.PROJECTNAME) projectname,min(p.ACCEPTUSERDATE) acceptuserdate,max(p.BANJIEDATE) banjiedate FROM audit_sp_instance a inner JOIN audit_sp_i_subapp b on a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c on a.areacode = c.XiaQuCode inner join audit_sp_business d on a.BUSINESSGUID = d.RowGuid left join audit_sp_i_task t on a.RowGuid=t.BIGUID left join audit_project p on t.PROJECTGUID=p.RowGuid left join frame_ou po on p.OUGUID=po.OUGUID WHERE a.businesstype = '2' AND a.areacode LIKE ? group by a.RowGuid ) x";
            countsql = "select count(1) from ( SELECT a.rowguid FROM audit_sp_instance a inner JOIN audit_sp_i_subapp b on a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c on a.areacode = c.XiaQuCode inner join audit_sp_business d on a.BUSINESSGUID = d.RowGuid  WHERE a.businesstype = '2' AND a.areacode LIKE ? group by a.RowGuid ) x";
        }
        else {
            return new PageData<Record>();
        }
        List<Record> list = commonDao.findList(sql, first, pagesize, Record.class, new Object[] {areacode });
        if (list == null) {
            list = new ArrayList<Record>();
        }
        int count = commonDao.queryInt(countsql, new Object[] {areacode });
        return new PageData<>(list, count);
    }

    public Record getXmCount(String areacode) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "SELECT SUM(e.totalinvest) totalinvest,count(1) totalcount,count(case when e.zdxmlx='1' then 1 else null end) zdcount FROM audit_sp_instance a INNER JOIN audit_sp_i_subapp b ON a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c ON a.areacode = c.XiaQuCode INNER JOIN audit_sp_business d ON a.BUSINESSGUID = d.RowGuid INNER JOIN AUDIT_RS_ITEM_BASEINFO e on a.YEWUGUID=e.RowGuid WHERE a.businesstype = '1' AND a.areacode LIKE ? ";
        }
        else {
            return new Record();
        }

        return commonDao.find(sql, Record.class, new Object[] {areacode });
    }

    public List<Record> getXmlxCountList(String areacode, int limitInt) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from ( SELECT e.itemtype,count(1) totalcount FROM audit_sp_instance a INNER JOIN audit_sp_i_subapp b ON a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c ON a.areacode = c.XiaQuCode INNER JOIN audit_sp_business d ON a.BUSINESSGUID = d.RowGuid INNER JOIN AUDIT_RS_ITEM_BASEINFO e on a.YEWUGUID=e.RowGuid WHERE a.businesstype = '1' AND a.areacode LIKE ? group by e.ITEMTYPE ) x order by totalcount desc";
        }
        else {
            return new ArrayList<Record>();
        }
        return commonDao.findList(sql, 0, limitInt, Record.class, new Object[] {areacode });
    }

    public List<Record> getXmGchyflCountList(String areacode, int limitInt) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from ( SELECT e.gchyfl,count(1) totalcount FROM audit_sp_instance a INNER JOIN audit_sp_i_subapp b ON a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c ON a.areacode = c.XiaQuCode INNER JOIN audit_sp_business d ON a.BUSINESSGUID = d.RowGuid INNER JOIN AUDIT_RS_ITEM_BASEINFO e on a.YEWUGUID=e.RowGuid WHERE a.businesstype = '1' AND a.areacode LIKE ? group by e.gchyfl ) x order by totalcount desc";
        }
        else {
            return new ArrayList<Record>();
        }
        return commonDao.findList(sql, 0, limitInt, Record.class, new Object[] {areacode });
    }

    public List<Record> getXmLxlxCountList(String areacode) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select * from ( SELECT e.lxlx,count(1) totalcount FROM audit_sp_instance a INNER JOIN audit_sp_i_subapp b ON a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c ON a.areacode = c.XiaQuCode INNER JOIN audit_sp_business d ON a.BUSINESSGUID = d.RowGuid INNER JOIN AUDIT_RS_ITEM_BASEINFO e on a.YEWUGUID=e.RowGuid WHERE a.businesstype = '1' AND a.areacode LIKE ? group by e.lxlx ) x order by totalcount desc";
        }
        else {
            return new ArrayList<Record>();
        }
        return commonDao.findList(sql, Record.class, new Object[] {areacode });
    }

    public PageData<Record> getXmTjDataList(String areacode, int first, int pagesize) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        String countsql = "";
        if (commonDao.isMySql()) {
            sql = "select *,ROUND(ABS(TIMESTAMPDIFF(SECOND, acceptuserdate, banjiedate)) / 3600, 1) as processtime from ( SELECT a.CREATEDATE,e.itemname,b.status as yjsstatus,GROUP_CONCAT(po.OUNAME) ouname,GROUP_CONCAT(p.PROJECTNAME) projectname,min(p.ACCEPTUSERDATE) acceptuserdate,max(p.BANJIEDATE) banjiedate FROM audit_sp_instance a inner JOIN audit_sp_i_subapp b on a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c on a.areacode = c.XiaQuCode inner join audit_sp_business d on a.BUSINESSGUID = d.RowGuid INNER JOIN AUDIT_RS_ITEM_BASEINFO e on a.YEWUGUID=e.RowGuid left join audit_sp_i_task t on a.RowGuid=t.BIGUID left join audit_project p on t.PROJECTGUID=p.RowGuid left join frame_ou po on p.OUGUID=po.OUGUID WHERE a.businesstype = '1' AND a.areacode LIKE ? group by a.RowGuid ) x order by CREATEDATE desc ";
            countsql = "select count(1) from ( SELECT a.rowguid FROM audit_sp_instance a inner JOIN audit_sp_i_subapp b on a.RowGuid = b.BIGUID INNER JOIN audit_orga_area c on a.areacode = c.XiaQuCode inner join audit_sp_business d on a.BUSINESSGUID = d.RowGuid INNER JOIN AUDIT_RS_ITEM_BASEINFO e on a.YEWUGUID=e.RowGuid WHERE a.businesstype = '1' AND a.areacode LIKE ? group by a.RowGuid ) x";
        }
        else {
            return new PageData<Record>();
        }
        List<Record> list = commonDao.findList(sql, first, pagesize, Record.class, new Object[] {areacode });
        if (list == null) {
            list = new ArrayList<Record>();
        }
        int count = commonDao.queryInt(countsql, new Object[] {areacode });
        return new PageData<>(list, count);
    }
}
