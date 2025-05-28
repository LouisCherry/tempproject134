package com.epoint.zoucheng.znsb.auditznsbmodule.service;

import java.util.List;

import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

/**
 * 
 * 
 * @author JackLove
 * @version [版本号, 2018-03-30 11:11:00]
 */
public class ZCAuditZnsbModuleService extends AuditCommonService
{
    private static final long serialVersionUID = 1L;

    /**
     * 查询登录数量
     * 
     */
    public Integer getRecordCount() {
        String sql = "select count(rowguid) from AUDIT_ZNSB_MODULE";
        return commonDao.queryInt(sql, Integer.class);
    }

    public Integer getRecordCountByMacaddress(String macaddress) {
        String sql = "select count(rowguid) from AUDIT_ZNSB_MODULE where Macaddress=?1";
        return commonDao.queryInt(sql, macaddress);
    }

    public Integer getRecordCountByCenterguidAndMacaddress(String centerguid, String macaddress) {
        String sql = "select count(rowguid) from AUDIT_ZNSB_MODULE where centerguid=?1 and Macaddress=?2";
        return commonDao.queryInt(sql, centerguid, macaddress);
    }

    public Integer getRecordCountByCenterguid(String centerguid) {
        String sql = "select count(rowguid) from AUDIT_ZNSB_MODULE where centerguid=?1 ";
        return commonDao.queryInt(sql, centerguid);
    }

    public List<Record> getModuleClickByCenterguid(String centerguid) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 group by modulename order by count(1) desc limit 8";
        }
        else if (commonDao.isOracle()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and rownum <= 8 group by modulename order by count(1) desc";
        }
        else if (commonDao.isDM()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and rownum <= 8 group by modulename order by count(1) desc";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and rownum <= 8 group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class, centerguid);
    }

    public Integer getRecordCount(String macaddress, String from, String to) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select count(1) from audit_znsb_module where Macaddress=?1  and DATE(onclicktime)>=?2 and  DATE(onclicktime)<=?3";
        }
        else if (commonDao.isOracle()) {
            sql = "select count(1) from audit_znsb_module where Macaddress=?1  and onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        else if (commonDao.isDM()) {
            sql = "select count(1) from audit_znsb_module where Macaddress=?1  and onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select count(1) from audit_znsb_module where Macaddress=?1  and onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }

        return commonDao.queryInt(sql, macaddress, from, to);
    }

    public Integer getRecordCountByCenterguidAndTime(String centerguid, String from, String to) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select count(1) from audit_znsb_module where  DATE(onclicktime)>=?2 and  DATE(onclicktime)<=?3";
        }
        else if (commonDao.isOracle()) {
            sql = "select count(1) from audit_znsb_module where  onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        else if (commonDao.isDM()) {
            sql = "select count(1) from audit_znsb_module where  onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select count(1) from audit_znsb_module where  onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        if (StringUtil.isNotBlank(centerguid)) {
            sql += " and centerguid=?1 ";
        }

        return commonDao.queryInt(sql, centerguid, from, to);
    }

    public List<Record> getRecordByCenterguidAndTime(String centerguid, String from, String to) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select Macaddress,count(1) as count from audit_znsb_module where  DATE(onclicktime)>=?2 and  DATE(onclicktime)<=?3";
        }
        else if (commonDao.isOracle()) {
            sql = "select Macaddress,count(1) as count from audit_znsb_module where  onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        else if (commonDao.isDM()) {
            sql = "select Macaddress,count(1) as count from audit_znsb_module where  onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select Macaddress,count(1) as count from audit_znsb_module where  onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd')";
        }
        if (StringUtil.isNotBlank(centerguid)) {
            sql += " and centerguid=?1 ";
        }
        sql += " group by Macaddress ";
        return commonDao.findList(sql, Record.class,centerguid, from, to);
    }
    
    public List<Record> getModuleClick(String centerguid) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 group by modulename order by count(1) desc limit 10";
        }
        else if (commonDao.isOracle()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and rownum<= 10 group by modulename order by count(1) desc";
        }
        else if (commonDao.isDM()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and rownum<= 10 group by modulename order by count(1) desc";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and rownum<= 10 group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class, centerguid);
    }

    public List<Record> getModuleClick() {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  group by modulename order by count(1) desc limit 10";
        }
        else if (commonDao.isOracle()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where rownum<=10 group by modulename order by count(1) desc";
        }
        else if (commonDao.isDM()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where rownum<=10 group by modulename order by count(1) desc";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where rownum<=10 group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class);
    }

    public List<Record> getModuleClick(String centerguid, String startime, String endtime) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and DATE(onclicktime)>=?2 and  DATE(onclicktime)<=?3 group by modulename order by count(1)  desc limit 10";
        }
        else if (commonDao.isOracle()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd') and rownum<= 10 group by modulename order by count(1) desc";
        }
        else if (commonDao.isDM()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd') and rownum<= 10 group by modulename order by count(1) desc";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=?1 and onclicktime >= TO_DATE(?2,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?3,'yyyy-MM-dd') and rownum<= 10 group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class, centerguid, startime, endtime);
    }

    public List<Record> getModuleClick(String startime, String endtime) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where  DATE(onclicktime)>=?1 and  DATE(onclicktime)<=?2 group by modulename order by count(1) desc limit 10";
        }
        else if (commonDao.isOracle()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where onclicktime >= TO_DATE(?1,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?2,'yyyy-MM-dd') and rownum<=10 group by modulename order by count(1) desc";
        }
        else if (commonDao.isDM()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where onclicktime >= TO_DATE(?1,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?2,'yyyy-MM-dd') and rownum<=10 group by modulename order by count(1) desc";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where onclicktime >= TO_DATE(?1,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?2,'yyyy-MM-dd') and rownum<=10 group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class, startime, endtime);
    }

    public int getModuleDayClick(String centerguid, String modulename, String startime, String endtime) {
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select count(1) clicknum from audit_znsb_module where modulename=?2 and DATE(onclicktime)>=?3 and  DATE(onclicktime)<?4 ";
        }
        else if (commonDao.isOracle()) {
            sql = "select count(1) clicknum from audit_znsb_module where modulename=?2 onclicktime >= TO_DATE(?3,'yyyy-MM-dd') and  onclicktime < TO_DATE(?4,'yyyy-MM-dd')";
        }
        else if (commonDao.isDM()) {
            sql = "select count(1) clicknum from audit_znsb_module where modulename=?2 onclicktime >= TO_DATE(?3,'yyyy-MM-dd') and  onclicktime < TO_DATE(?4,'yyyy-MM-dd')";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql = "select count(1) clicknum from audit_znsb_module where modulename=?2 onclicktime >= TO_DATE(?3,'yyyy-MM-dd') and  onclicktime < TO_DATE(?4,'yyyy-MM-dd')";
        }
        if (StringUtil.isNotBlank(centerguid)) {
            sql += " and centerguid=?1 ";
        }
        return commonDao.queryInt(sql, centerguid, modulename, startime, endtime);
    }

    public List<Record> getCenterClick() {
        String sql = "select centerguid,count(1) clickcount  from audit_znsb_module GROUP BY centerguid ORDER BY count(1) desc limit 10";

        return commonDao.findList(sql, Record.class);
    }

    public List<Record> getModuleClickByMac(String macaddress) {
        String sql = "select modulename,count(1) clicknum from audit_znsb_module  where   macaddress=? group by modulename order by count(1) desc ";

        return commonDao.findList(sql, Record.class, macaddress);
    }

    public List<Record> getModuleClickWithoutMac(String macaddress, String centerguid) {
        String sql = "select modulename,count(1) clicknum from audit_znsb_module  where centerguid=? and   macaddress not in "
                + macaddress + " group by modulename order by count(1) desc ";

        return commonDao.findList(sql, Record.class, centerguid);
    }

    public Integer updateByMacaddress(String oldmacaddress, String newmacaddress) {
        String sql = " update audit_znsb_module set macaddress = ? where  macaddress = ? ";
        return commonDao.execute(sql, newmacaddress, oldmacaddress);
    }
}
