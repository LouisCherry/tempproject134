package com.epoint.jn.inproject.impl;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogtnew;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegtnew;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgtnew;
import com.epoint.jn.inproject.api.entity.lcproject;

/**
 * 竣工信息表对应的后台service
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
public class InprojectService
{
	  /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;
    
    //社保数据库连接
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzpassword");
    
    private static String SccsURL = ConfigUtil.getConfigValue("datasyncjdbc", "sccsqzurl");
    private static String SccsNAME = ConfigUtil.getConfigValue("datasyncjdbc", "sccszusername");
    private static String SccsPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "sccspassword");
    

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    
    private DataSourceConfig SccsdataSourceConfig = new DataSourceConfig(SccsURL, SccsNAME, SccsPASSWORD);
    

    public InprojectService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance(SccsdataSourceConfig);
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }
   
    
    public void insertQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt) {
    	String sql = "declare v_clob clob :='"+eajcstepbasicinfogt.getStr("ACCEPTLIST")+"'; begin "; 
        sql += "insert into DHQZK.ea_jc_step_basicinfo_gtnew(PROJPWD,VALIDITY_FLAG,Dataver,Stdver,Approvaltype,Promisetimelimit,Promisetimeunit,Timelimit";
    	sql += ",Submit,Occurtime,Maketime,Acceptdeptcode1,Acceptdeptcode2,Itemregionid,ORGBUSNO,PROJID,Itemno,Itemname,Projectname,Applicant,Acceptdeptid,Region_id,Acceptdeptname,applicantcardtype,applicantcardcode,applicanttel,ACCEPTLIST,ITEMTYPE,CATALOGCODE,TASKCODE,APPLYERTYPE,Acceptdeptcode)";
    	sql += " values('11111',1,1,1,'2',0,1,'0','1',sysdate,sysdate,'无','无',";
    	sql += "'" + eajcstepbasicinfogt.getItemregionid() + "',";
    	sql += "'" + eajcstepbasicinfogt.getOrgbusno() + "',";
    	sql += "'" + eajcstepbasicinfogt.getProjid() + "',";
    	sql += "'" + eajcstepbasicinfogt.getItemno() + "',";
    	sql += "'" + eajcstepbasicinfogt.getItemname() + "',";
    	sql += "'" + eajcstepbasicinfogt.getProjectname() + "',";
    	sql += "'" + eajcstepbasicinfogt.getApplicant() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptid() + "',";
    	sql += "'" + eajcstepbasicinfogt.getRegion_id() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptname() + "',";
    	sql += "'" + eajcstepbasicinfogt.getApplicantCardtype() + "',";
    	sql += "'" + eajcstepbasicinfogt.getApplicantCardCode() + "',";
    	sql += "'" + eajcstepbasicinfogt.getApplicanttel() + "',";
    	sql += "v_clob,";
    	sql += "'" + eajcstepbasicinfogt.getStr("ITEMTYPE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("CATALOGCODE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("TASKCODE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("APPLYERTYPE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode() + "'); end;";
    	commonDaoFrom.execute(sql);
    }
    
    public void insertQzkProcess(eajcstepprocgt eajcstepprocgt) {
    	String sql = "insert into DHQZK.ea_jc_step_proc_gtnew(Validity_flag,Dataver,Stdver,Nodestate,Occurtime";
    	sql += ",Maketime,Signstate,Sn,Noderesult,Itemregionid,Nodeprocerarea,Region_id,Nodename,Nodecode,Nodetype,Nodeprocer,Nodeprocername,Procunit,Procunitname,Nodestarttime,Nodeendtime,ORGBUSNO,PROJID)";
    	sql += "VALUES(1,1,1,'02',sysdate,sysdate,'0',";
    	sql += "'" + eajcstepprocgt.getSn() + "',";
    	sql += "'" + eajcstepprocgt.getNoderesult() + "',";
    	sql += "'" + eajcstepprocgt.getItemregionid() + "',";
    	sql += "'" + eajcstepprocgt.getNodeprocerarea() + "',";
    	sql += "'" + eajcstepprocgt.getRegion_id() + "',";
    	sql += "'" + eajcstepprocgt.getNodename() + "',";
    	sql += "'" + eajcstepprocgt.getNodecode() + "',";
    	sql += "'" + eajcstepprocgt.getNodetype() + "',";
    	sql += "'" + eajcstepprocgt.getNodeprocer() + "',";
    	sql += "'" + eajcstepprocgt.getNodeprocername() + "',";
    	sql += "'" + eajcstepprocgt.getProcunit() + "',";
    	sql += "'" + eajcstepprocgt.getProcunitname() + "',";
    	sql += "to_date('" + eajcstepprocgt.getNodestarttime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "to_date('" + eajcstepprocgt.getNodeendtime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepprocgt.getOrgbusno() + "',";
    	sql += "'" + eajcstepprocgt.getProjid() + "')";
    	commonDaoFrom.execute(sql);
    }
    
    public void insertQzkDone(eajcstepdonegt eajcstepdonegt) {
    	String sql = "insert into DHQZK.ea_jc_step_donenew(Validity_flag,Stdver,Dataver,Doneresult,Approvallimit,Certificatenam,Isfee,Occurtime,Maketime,Signstate,Region_id,Itemregionid";
    	sql += ",Orgbusno,Projid,Certificateno,Transactor) values(1,1,'1',0,sysdate,11,'0',sysdate,sysdate,'0',";
    	sql += "'" + eajcstepdonegt.getRegion_id() + "',";
    	sql += "'" + eajcstepdonegt.getItemregionid() + "',";
    	sql += "'" + eajcstepdonegt.getOrgbusno() + "',";
    	sql += "'" + eajcstepdonegt.getProjid() + "',";
    	sql += "'" + eajcstepdonegt.getCertificateno() + "',";
    	sql += "'" + eajcstepdonegt.getTransactor() + "')";
    	commonDaoFrom.execute(sql);
    }
    
    
    public void insertQzkBaseInfonew(eajcstepbasicinfogtnew eajcstepbasicinfogt) {
    	String sql = "insert into DHQZK.ea_jc_step_basicinfo_gt(Orgbusno,Projid,Projpwd,Validity_flag,Dataver,Stdver,Itemno,Itemname,Projectname";
    	sql += ",Applicant,Acceptlist,Acceptdeptid,Region_id,Acceptdeptname,Approvaltype,Promisetimelimit,Promisetimeunit,Timelimit,Itemregionid,Submit,Occurtime,Maketime,TBSTATE,Acceptdeptcode,Acceptdeptcode1,Acceptdeptcode2";
    	sql += ",APPLICANTTEL,APPLICANTCARDCODE,APPLICANTCARDTYPE,ITEMTYPE,CATALOGCODE,TASKCODE,APPLYERTYPE,APPLICANTTYPE)";
    	sql += " values(";
    	sql += "'" + eajcstepbasicinfogt.getOrgbusno() + "',";
    	sql += "'" + eajcstepbasicinfogt.getProjid() + "',";
    	sql += "'" + eajcstepbasicinfogt.getProjpwd() + "',";
    	sql += "'" + eajcstepbasicinfogt.getValidity_flag() + "',";
    	sql += "'" + eajcstepbasicinfogt.getDataver() + "',";
    	sql += "'" + eajcstepbasicinfogt.getStdver() + "',";
    	sql += "'" + eajcstepbasicinfogt.getItemno() + "',";
    	sql += "'" + eajcstepbasicinfogt.getItemname() + "',";
    	sql += "'" + eajcstepbasicinfogt.getProjectname() + "',";
    	sql += "'" + eajcstepbasicinfogt.getApplicant() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptlist() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptid() + "',";
    	sql += "'" + eajcstepbasicinfogt.getRegion_id() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptname() + "',";
    	sql += "'" + eajcstepbasicinfogt.getApprovaltype() + "',";
    	sql += "'" + eajcstepbasicinfogt.getPromisetimelimit() + "',";
    	sql += "'" + eajcstepbasicinfogt.getPromisetimeunit() + "',";
    	sql += "'" + eajcstepbasicinfogt.getTimelimit() + "',";
    	sql += "'" + eajcstepbasicinfogt.getItemregionid() + "',";
    	sql += "'" + eajcstepbasicinfogt.getSubmit() + "',";
    	sql += "to_date('" + eajcstepbasicinfogt.getOccurtime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "to_date('" + eajcstepbasicinfogt.getMaketime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepbasicinfogt.getStr("TBSTATE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode1() + "',";
    	sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode2() + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("APPLICANTTEL") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("APPLICANTCARDCODE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("APPLICANTCARDTYPE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("ITEMTYPE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("CATALOGCODE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("TASKCODE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("APPLYERTYPE") + "',";
    	sql += "'" + eajcstepbasicinfogt.getStr("APPLICANTTYPE") + "')";
    	commonDaoFrom.execute(sql);
    }
    
    public void insertQzkProcessnew(eajcstepprocgtnew eajcstepprocgt) {
    	String sql = "insert into DHQZK.ea_jc_step_proc_gtnew(Orgbusno,Projid,Validity_flag,Dataver,Stdver,SN,Nodename,Nodecode,Nodetype";
    	sql += ",Nodeprocer,Nodeprocername,Nodeprocerarea,Region_id,Procunit,Procunitname,Nodestate,Nodestarttime,Nodeendtime,Noderesult,Occurtime,Maketime,Signstate,Itemregionid)";
    	sql += "VALUES(";
    	sql += "'" + eajcstepprocgt.getOrgbusno() + "',";
    	sql += "'" + eajcstepprocgt.getProjid() + "',";
    	sql += "'" + eajcstepprocgt.getValidity_flag() + "',";
    	sql += "'" + eajcstepprocgt.getDataver() + "',";
    	sql += "'" + eajcstepprocgt.getStdver() + "',";
    	sql += "'" + eajcstepprocgt.getSn() + "',";
    	sql += "'" + eajcstepprocgt.getNodename() + "',";
    	sql += "'" + eajcstepprocgt.getNodecode() + "',";
    	sql += "'" + eajcstepprocgt.getNodetype() + "',";
    	sql += "'" + eajcstepprocgt.getNodeprocer() + "',";
    	sql += "'" + eajcstepprocgt.getNodeprocername() + "',";
    	sql += "'" + eajcstepprocgt.getNodeprocerarea() + "',";
    	sql += "'" + eajcstepprocgt.getRegion_id() + "',";
    	sql += "'" + eajcstepprocgt.getProcunit() + "',";
    	sql += "'" + eajcstepprocgt.getProcunitname() + "',";
    	sql += "'" + eajcstepprocgt.getNodestate() + "',";
    	sql += "to_date('" + eajcstepprocgt.getNodestarttime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "to_date('" + eajcstepprocgt.getNodeendtime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepprocgt.getNoderesult() + "',";
    	sql += "to_date('" + eajcstepprocgt.getOccurtime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "to_date('" + eajcstepprocgt.getMaketime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepprocgt.getSignstate() + "',";
    	sql += "'" + eajcstepprocgt.getItemregionid() + "')";
    	commonDaoFrom.execute(sql);
    }
    
    public void insertQzkDonenew(eajcstepdonegtnew eajcstepdonegt) {
    	String sql = "insert into DHQZK.ea_jc_step_donenew(Orgbusno,Projid,Validity_flag,Stdver,Region_id,Dataver,Doneresult,Approvallimit,Certificatenam";
    	sql += ",Certificateno,Isfee,Occurtime,Transactor,Maketime,Signstate,Itemregionid) values(";
    	sql += "'" + eajcstepdonegt.getOrgbusno() + "',";
    	sql += "'" + eajcstepdonegt.getProjid() + "',";
    	sql += "'" + eajcstepdonegt.getValidity_flag() + "',";
    	sql += "'" + eajcstepdonegt.getStdver() + "',";
    	sql += "'" + eajcstepdonegt.getRegion_id() + "',";
    	sql += "'" + eajcstepdonegt.getDataver() + "',";
    	sql += "'" + eajcstepdonegt.getDoneresult() + "',";
    	sql += "to_date('" + eajcstepdonegt.getApprovallimit()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepdonegt.getCertificatenam() + "',";
    	sql += "'" + eajcstepdonegt.getCertificateno() + "',";
    	sql += "'" + eajcstepdonegt.getIsfee() + "',";
    	sql += "to_date('" + eajcstepdonegt.getOccurtime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepdonegt.getTransactor() + "',";
    	sql += "to_date('" + eajcstepdonegt.getMaketime()+"','yyyy-MM-dd hh24:mi:ss'),";
    	sql += "'" + eajcstepdonegt.getSignstate() + "',";
    	sql += "'" + eajcstepdonegt.getItemregionid() + "')";
    	commonDaoFrom.execute(sql);
    }
    
	public AuditTask getAuditTaskByTaskname(String taskname, String areacode,String ouguid) {
		String sql = "select * from audit_task where taskname = ?1 and is_enable = '1' and IFNULL(is_history,0) = 0 and IS_EDITAFTERIMPORT = '1' and areacode = ?2 and ouguid = ?3";
		return commonDaoTo.find(sql, AuditTask.class, taskname,areacode,ouguid);
	}

	public FrameOu getFrameOuByOuname(String ouname) {
		String sql = "select ouname,oucode,ouguid from frame_ou where ouname = ?";
		return commonDaoTo.find(sql, FrameOu.class, ouname);
	}
	
	public List<Record> finList() {
        String sql = "select * from lc_project where DATE_FORMAT(banjiedate,'%Y-%m') = '2021-09' and IFNULL(YearFlag,0) = 0 limit 200 ";
        return commonDaoTo.findList(sql, Record.class);
    }
	
	public void updatezjprojectByrowguid(String syncdone,String rowguid) {
		   String sql = "update lc_project set YearFlag=?1 where rowguid=?2";
	       commonDaoTo.execute(sql,syncdone, rowguid);
		}
    
}
