package com.epoint.hcp.service;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.api.entity.AuditHcpAreainfo;
import com.epoint.hcp.api.entity.AuditHcpOuinfo;
import com.epoint.hcp.api.entity.Evainstance;

public class AuditLocalHcpDataCountService
{
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    /**
     * 以下是2种构造函数重载。
     */
    public AuditLocalHcpDataCountService() {
        commonDao = CommonDao.getInstance();
    }

    public void updateAuditHcpAreainfo(String fcbmynum,String mynum,String ydnum,String cpzgcount,String jbmynum,String mycount,String bmynum,String pcnum,String bjcount,String cpcount,String pjcount,String padnum,String myd,String fcmynum,String ytjnum,String zgl,String areacode) {
    	String sql = "update audit_hcp_areainfo set fcbmynum = ?1 , mynum = ?2 , ydnum = ?3 , cpzgcount = ?4 , jbmynum = ?5 , mycount = ?6 , bmynum = ?7 , pcnum = ?8 , bjcount = ?9 , cpcount = ?10 , operatedate = now() , pjcount = ?11 , padnum = ?12 , myd = ?13 , fcmynum = ?14 , ytjnum = ?15 , zgl = ?16  where areacode = ?17 ";
        commonDao.execute(sql,fcbmynum, mynum, ydnum, cpzgcount, jbmynum, mycount, bmynum, pcnum, bjcount, cpcount, pjcount, padnum, myd, fcmynum, ytjnum, zgl, areacode);
    }
    
    public void updateAuditHcpOuinfo(String bjcount,String pjcount,String cpcount,String mycount,String myd,String deptCode) {
    	String sql = "update audit_hcp_ouinfo set bjcount = ?1 , pjcount = ?2 , cpcount = ?3 , mycount = ?4 , myd = ?5   where oucode = ?6 ";
        commonDao.execute(sql,bjcount, pjcount, cpcount, mycount, myd, deptCode);
    }

    public void insert(Record r) {
        commonDao.insert(r);
    }

    /**
     *  根据辖区编码获取辖区名称
     *  @param areacode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getAreaNameByareaCode(String areacode) {
        String sql = "select xiaquname as areaname from audit_orga_area where xiaqucode = ?";
        return commonDao.queryString(sql, areacode);
    }

    public List<AuditHcpAreainfo> getAreaCountList() {
        String sql = "SELECT areaCode," + "count(DISTINCT projectno) bjcount,"
                + " SUM(CASE WHEN satisfaction THEN 1 ELSE 0 END) pjcount,"
                + " SUM(CASE WHEN satisfaction IN (1, 2) and sbsign = '1' THEN   1 ELSE 0 END) cpcount,"
                + " SUM(CASE WHEN satisfaction IN (3, 4, 5) THEN    1 ELSE 0 END) mycount,"
                + " round(SUM(case when satisfaction in (3, 4, 5) then 1 else 0 end) / SUM(case when satisfaction then 1 else 0 end) * 100, 2) myd,"
                + " SUM(CASE WHEN Evalevel in (1,2) and sbsign = '1' AND answer is NOT NULL THEN 1 ELSE 0 END) cpzgcount,"
                + " round(SUM(case when Evalevel in (1, 2) and sbsign = '1' and answer is not null then 1 else 0 end) / SUM(case when satisfaction in (1, 2) then 1 else 0 end) * 100, 2) zgl,"
                + " SUM(CASE WHEN satisfaction = 5 THEN 1 ELSE 0 END) fcmynum,"
                + " SUM(CASE WHEN satisfaction = 4 THEN 1 ELSE 0 END) mynum,"
                + " SUM(CASE WHEN satisfaction = 3 THEN 1 ELSE 0 END) jbmynum,"
                + " SUM(CASE WHEN satisfaction = 2 and sbsign = '1' THEN 1 ELSE 0 END) bmynum,"
                + " SUM(CASE WHEN satisfaction = 1 and sbsign = '1' THEN 1 ELSE 0 END) fcbmynum,"
                + " SUM(CASE WHEN pf = 1 THEN   1 ELSE 0 END) pcnum,"
                + " SUM(CASE WHEN pf IN (2, 3) THEN 1 ELSE 0 END) ydnum,"
                + " SUM(CASE WHEN pf = 4 THEN   1 ELSE 0 END) padnum,"
                + " SUM(CASE WHEN pf = 5 THEN   1 ELSE 0 END) ytjnum"
                + " FROM evainstance WHERE effectivEvalua = 1 GROUP BY areaCode";
        return commonDao.findList(sql, AuditHcpAreainfo.class);
    }

    public List<AuditHcpOuinfo> getOuCountList() {
        String sql = "SELECT rowguid,proDepart OUName,deptCode OUCODE,areaCode BelongXiaQuCode,"
                + " count(DISTINCT projectno) bjcount," + " SUM(CASE WHEN satisfaction THEN 1 ELSE 0 END) pjcount,"
                + " SUM(CASE WHEN satisfaction IN (1, 2) and sbsign = '1' and sbsign = '1' THEN 1 ELSE 0 END) cpcount,"
                + " SUM(CASE WHEN satisfaction IN (3, 4, 5) THEN    1 ELSE 0 END) mycount,"
                + " round(SUM(CASE WHEN satisfaction IN (3, 4, 5) THEN 1 ELSE 0 END) / SUM(CASE WHEN satisfaction THEN 1 ELSE 0 END) * 100, 2) myd"
                + " FROM evainstance WHERE effectivEvalua = 1 and prodepart is not null GROUP BY deptCode";
        return commonDao.findList(sql, AuditHcpOuinfo.class);
    }
    
    public List<Record> getOldEvaOuList() {
    	String sql = "select OUName,OUCODE,BelongXiaQuCode,bjcount,pjcount,cpcount,mycount,myd from old_eva_ou";
        return commonDao.findList(sql, Record.class);
    }

    public List<Evainstance> getUnSyncEvaluateList() {
        String sql = "select * from evainstance WHERE areacode is not null and areacode != '' and projectno is not null and (sync_sign is null or sync_sign = '' or sync_sign = '0') limit 1000";
        return commonDao.findList(sql, Evainstance.class);
    }

    public void updateSyncSign(Evainstance evainstance) {
        evainstance.set("sync_sign", "1");
        commonDao.update(evainstance);
    }

    public AuditHcpAreainfo findArea(String areaCode) {
        String sql = "select areaCode from Audit_Hcp_AreaInfo where areaCode = ?";
        return commonDao.find(sql, AuditHcpAreainfo.class, areaCode);
    }
    
    public AuditHcpOuinfo findOu(String ouCode) {
        String sql = "select ouCode from Audit_Hcp_OUInfo where ouCode = ?";
        return commonDao.find(sql, AuditHcpOuinfo.class, ouCode);
    }

}
