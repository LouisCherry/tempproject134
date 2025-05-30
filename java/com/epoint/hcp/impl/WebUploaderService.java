package com.epoint.hcp.impl;
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
import com.epoint.hcp.api.entity.eajcstepbasicinfogt;
import com.epoint.hcp.api.entity.eajcstepbasicinfogtnew;
import com.epoint.hcp.api.entity.eajcstepdonegt;
import com.epoint.hcp.api.entity.eajcstepdonegtnew;
import com.epoint.hcp.api.entity.eajcstepprocgt;
import com.epoint.hcp.api.entity.eajcstepprocgtnew;
import com.epoint.hcp.api.entity.lcproject;
import com.epoint.hcp.api.entity.lcprojecteight;
import com.epoint.hcp.api.entity.lcprojectfive;
import com.epoint.hcp.api.entity.lcprojectfour;
import com.epoint.hcp.api.entity.lcprojectnine;
import com.epoint.hcp.api.entity.lcprojectseven;
import com.epoint.hcp.api.entity.lcprojectsix;
import com.epoint.hcp.api.entity.lcprojectten;
import com.epoint.hcp.api.entity.lcprojectthree;
import com.epoint.hcp.api.entity.lcprojecttwo;
import com.epoint.xmz.yjsczcapplyer.api.entity.YjsCzcApplyer;

/**
 * 竣工信息表对应的后台service
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
public class WebUploaderService
{
	  /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;
    
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public WebUploaderService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    
    public <T extends Record> int deleteByGuid(String guid) {
        T t = commonDaoTo.find(YjsCzcApplyer.class, guid);
        return commonDaoTo.delete(t);
    }
    
    
    public List<Record> finList(int first, int pagesize, String ouguid,String areacode) {
        String sql = "select * from lc_project where 1=1  and is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
        	sql += " and ouguid = ? ";
        }
        if (StringUtil.isNotBlank(areacode)) {
        	sql += " and areacode = '"+areacode+"'";
        }
        if((first!=0&&pagesize!=0)||(first==0&&pagesize!=0)){
            sql+=" order by operatedate desc limit "+first+","+pagesize;
        }
        List<Record> list = commonDaoTo.findList(sql, Record.class, ouguid);
        return list;
    }
    
    public Integer finTotal(String ouguid,String areacode) {
        String sql = "select count(1) from lc_project where is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
        	sql += " and ouguid = ? ";
        }
        if (StringUtil.isNotBlank(areacode)) {
        	sql += " and areacode = '"+areacode+"'";
        }
        int i= commonDaoTo.queryInt(sql, ouguid);
        return i;
    }
    
    public void updatezjproject(String rowguid,String username,String userguid) {
	   String sql = "update jnzj_project set banjietime=now(),status='90',sparetime='-',banjieuserguid=?1,banjieusername=?2 where rowguid=?3"
	   		+ " and status<>'90'";
       commonDaoTo.execute(sql,userguid,username, rowguid);
	}
    
    public void insert(lcproject record) {
		commonDaoTo.insert(record);
	}
    public void insert(lcprojecttwo record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectthree record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectfour record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectfive record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectsix record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectseven record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojecteight record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectnine record) {
    	commonDaoTo.insert(record);
    }
    
    public void insert(lcprojectten record) {
    	commonDaoTo.insert(record);
    }
    
    public void update(lcprojectten record) {
    	commonDaoTo.update(record);
    }
    
    public void insertQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt) {
    	String sql = "declare v_clob clob :='"+eajcstepbasicinfogt.getStr("ACCEPTLIST")+"'; begin "; 
        sql += "insert into JNING_XZXKSC.ea_jc_step_basicinfo(PROJPWD,VALIDITY_FLAG,Dataver,Stdver,Approvaltype,Promisetimelimit,Promisetimeunit,Timelimit";
    	sql += ",Submit,Occurtime,Maketime,Acceptdeptcode1,Acceptdeptcode2,Itemregionid,ORGBUSNO,PROJID,Itemno,Itemname,Projectname,Applicant,Acceptdeptid,Region_id,Acceptdeptname,applicanttype,applicantcode,applicanttel,ACCEPTLIST,ITEMTYPE,CATALOGCODE,TASKCODE,APPLYERTYPE,Acceptdeptcode)";
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
    	String sql = "insert into JNING_XZXKSC.ea_jc_step_proc(Validity_flag,Dataver,Stdver,Nodestate,Occurtime";
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
    	String sql = "insert into JNING_XZXKSC.ea_jc_step_done(Validity_flag,Stdver,Dataver,Doneresult,Approvallimit,Certificatenam,Isfee,Occurtime,Maketime,Signstate,Region_id,Itemregionid";
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
    	String sql = "insert into JNING_XZXKSC.ea_jc_step_basicinfo(Orgbusno,Projid,Projpwd,Validity_flag,Dataver,Stdver,Itemno,Itemname,Projectname";
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
    	String sql = "insert into JNING_XZXKSC.ea_jc_step_proc(Orgbusno,Projid,Validity_flag,Dataver,Stdver,SN,Nodename,Nodecode,Nodetype";
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
    	String sql = "insert into JNING_XZXKSC.ea_jc_step_done(Orgbusno,Projid,Validity_flag,Stdver,Region_id,Dataver,Doneresult,Approvallimit,Certificatenam";
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

	public FrameOu getFrameOuByOunameNew(String ouname,String areacode) {
		String sql = "select a.ouname,a.oucode,a.ouguid from frame_ou a join frame_ou_extendinfo b on a.ouguid = b.ouguid where ouname = ?1 and SUBSTR(b.areacode,1,6)  = ?2 and a.oucode like 'JN%'";
		return commonDaoTo.find(sql, FrameOu.class, ouname,areacode);
	}
	
	public FrameOu getFrameOuByOuname(String ouname) {
		String sql = "select a.ouname,a.oucode,a.ouguid from frame_ou a where a.ouname = ? and (a.oucode is not null and a.oucode != '')";
		return commonDaoTo.find(sql, FrameOu.class, ouname);
	}
	
	public Record getTotalPorjectByNow() {
		String sql = "select count(1) as total,acceptuserdate from lc_project where DATE_FORMAT(Acceptuserdate,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')";
		return commonDaoTo.find(sql, Record.class);
	}
	
	public Record getSizeInPorject() {
		String sql = "select * from in_project_record where DATE_FORMAT(todaytime,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') limit 1";
		return commonDaoTo.find(sql, Record.class);
	}
	
	public Record getJnzjProjectByRowguid(String rowguid) {
		String sql = "select * from jnzj_project where rowguid = ? and IFNULL(syncdone,0) = 0";
		return commonDaoTo.find(sql, Record.class, rowguid);
	}
	
	public void updatezjprojectByrowguid(String syncdone,String rowguid) {
	   String sql = "update jnzj_project set syncdone=?1 where rowguid=?2";
       commonDaoTo.execute(sql,syncdone, rowguid);
	}
	
	public void deletejprojectByguid(String rowguid) {
	   String sql = "delete from audit_project where rowguid=?";
       commonDaoTo.execute(sql, rowguid);
	}



	public int getNewInCountByAreacode(String areacode) {
		String sql ="select sum(count) areacount from in_exter_project where  areacode = ? and DATE_FORMAT( indate,'%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')";
		String count = commonDaoTo.queryString(sql, areacode);
		if(StringUtil.isBlank(count)){
			return 0 ;
		}
		return Integer.parseInt(count);
	}
	
	public int getInCountByAreacode(String areacode) {
		String sql ="select sum(count) areacount from in_lc_project where  areacode = ? and DATE_FORMAT( indate,'%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')";
		 String count = commonDaoTo.queryString(sql, areacode);
		if(StringUtil.isBlank(count)){
			return 0 ;
		}
		return Integer.parseInt(count);
	}

	public void insertLcprojectRecord(String areacode, int count) {
		Record rec = new Record();
		rec.setPrimaryKeys("rowguid");
		rec.set("rowguid", UUID.randomUUID().toString());
		rec.set("count",count);
		rec.set("areacode",areacode);
		rec.set("indate",new Date());
		rec.setSql_TableName("in_lc_project");
		commonDaoTo.insert(rec);
	}
	
	public void insertExterRecord(String areacode, int count) {
		Record rec = new Record();
		rec.setPrimaryKeys("rowguid");
		rec.set("rowguid", UUID.randomUUID().toString());
		rec.set("count",count);
		rec.set("areacode",areacode);
		rec.set("indate",new Date());
		rec.setSql_TableName("in_exter_project");
		commonDaoTo.insert(rec);
	}

    public void deleteQzkBaseInfo(String guid) {
        String sql = "delete from JNING_XZXKSC.ea_jc_step_basicinfo where rowguid = ?";
        commonDaoFrom.execute(sql,guid);
        
    }

    public void deleteQzkProcess(String guid) {
        String sql = "delete from JNING_XZXKSC.ea_jc_step_proc where rowguid = ?";
        commonDaoFrom.execute(sql,guid);        
    }

    public void deleteQzkDone(String guid) {
        String sql = "delete from JNING_XZXKSC.ea_jc_step_done where rowguid = ?";
        commonDaoFrom.execute(sql,guid);        
    }
	
    
}
