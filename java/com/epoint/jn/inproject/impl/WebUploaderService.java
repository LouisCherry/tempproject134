package com.epoint.jn.inproject.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.entity.*;
import com.epoint.jn.inproject.api.entity.lcproject;
import com.epoint.jn.inproject.api.entity.*;
import com.epoint.xmz.yjsczcapplyer.api.entity.YjsCzcApplyer;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 竣工信息表对应的后台service
 *
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
public class WebUploaderService {
    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;

    // 社保数据库连接
    protected ICommonDao sbDao;
    // 医保数据库连接
    protected ICommonDao ybDao;
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "gjjqzpassword");

    private static String sbURL = ConfigUtil.getConfigValue("datasyncjdbc", "sburl");
    private static String sbNAME = ConfigUtil.getConfigValue("datasyncjdbc", "sbusername");
    private static String sbPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "sbpassword");

    private static String ybURL = ConfigUtil.getConfigValue("datasyncjdbc", "yburl");
    private static String ybNAME = ConfigUtil.getConfigValue("datasyncjdbc", "ybusername");
    private static String ybPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "ybpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    private DataSourceConfig sbdataSourceConfig = new DataSourceConfig(sbURL, sbNAME, sbPASSWORD);
    private DataSourceConfig ybdataSourceConfig = new DataSourceConfig(ybURL, ybNAME, ybPASSWORD);

    public WebUploaderService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
//        sbDao = CommonDao.getInstance(sbdataSourceConfig);
//        ybDao = CommonDao.getInstance(ybdataSourceConfig);
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public <T extends Record> int deleteByGuid(String guid) {
        T t = commonDaoTo.find(YjsCzcApplyer.class, guid);
        int result = commonDaoTo.delete(t);
        commonDaoTo.close();
        return result;
    }

    public List<Record> finList(int first, int pagesize, String ouguid, String areacode) {
        String sql = "select * from lc_project_ten where 1=1  and is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = ? ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        if ((first != 0 && pagesize != 0) || (first == 0 && pagesize != 0)) {
            sql += " order by operatedate desc limit " + first + "," + pagesize;
        }
        List<Record>  records= commonDaoTo.findList(sql, Record.class, ouguid);
        commonDaoTo.close();
        return records;
    }

    /**
     *
     * @param first
     * @param pagesize
     * @param ouguid
     * @param areacode
     * @param startDate
     * @param endDate
     * @param certnum
     * @return
     */
    public List<Record> finListTwo(int first, int pagesize, String ouguid, String areacode, String startDate,
                                   String endDate, String certnum) {
        String sql = "select * from lc_project_two where 1=1  and is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = ? ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        if (StringUtil.isNotBlank(startDate)) {
            sql += " and banjiedate >= '" + startDate.substring(0, 10) + " 00:00:00'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and banjiedate <= '" + endDate.substring(0, 10) + " 23:59:59'";
        }
        if (StringUtil.isNotBlank(certnum)) {
            sql += " and certnum = '" + certnum + "'";
        }

        if ((first != 0 && pagesize != 0) || (first == 0 && pagesize != 0)) {
            sql += " order by operatedate desc limit " + first + "," + pagesize;
        }

        List<Record> list = commonDaoTo.findList(sql, Record.class, ouguid);
        commonDaoTo.close();
        return list;
    }

    public Integer finTotal(String ouguid, String areacode) {
        String sql = "select count(1) from lc_project where is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = ? ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        int i = commonDaoTo.queryInt(sql, ouguid);
        commonDaoTo.close();
        return i;
    }

    public Integer finTotalTwo(String ouguid, String areacode, String startDate, String endDate, String certnum) {
        String sql = "select count(1) from lc_project_two where is_lczj = '9' ";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid = ? ";
        }
        if (StringUtil.isNotBlank(areacode)) {
            sql += " and areacode = '" + areacode + "'";
        }
        if (StringUtil.isNotBlank(startDate)) {
            sql += " and banjiedate >= '" + startDate.substring(0, 10) + " 00:00:00'";
        }
        if (StringUtil.isNotBlank(endDate)) {
            sql += " and banjiedate <= '" + endDate.substring(0, 10) + " 23:59:59'";
        }
        if (StringUtil.isNotBlank(certnum)) {
            sql += " and certnum = '" + certnum + "'";
        }

        int i = commonDaoTo.queryInt(sql, ouguid);
        commonDaoTo.close();
        return i;
    }

    public void updatezjproject(String rowguid, String username, String userguid) {
        String sql = "update jnzj_project set banjietime=now(),status='90',sparetime='-',banjieuserguid=?1,banjieusername=?2 where rowguid=?3"
                + " and status<>'90'";
        commonDaoTo.execute(sql, userguid, username, rowguid);
        commonDaoTo.close();
    }

    public void insert(lcproject record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojecttwo record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectthree record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectfour record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectfive record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectsix record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectseven record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojecteight record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectnine record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insert(lcprojectten record) {
        commonDaoTo.insert(record);
        commonDaoTo.close();
    }

    public void insertQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt) {
        //
        String sql = "declare v_clob clob :='" + eajcstepbasicinfogt.getStr("ACCEPTLIST") + "'; begin ";
        sql += "insert into JNING_XZXKSC.ea_jc_step_basicinfo(PROJPWD,VALIDITY_FLAG,Dataver,Stdver,Approvaltype,Promisetimelimit,Promisetimeunit,Timelimit";
        sql += ",Submit,Occurtime,Maketime,Acceptdeptcode1,Acceptdeptcode2,Itemregionid,ORGBUSNO,PROJID,Itemno,Itemname,Projectname,Applicant,Acceptdeptid,Region_id,Acceptdeptname,applicanttype,applicantcode,applicanttel," +
                "ACCEPTLIST,ITEMTYPE,CATALOGCODE,TASKCODE,APPLYERTYPE,ApplyerPageType,ApplyerPageCode,Acceptdeptcode)";
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
        sql += "'" + eajcstepbasicinfogt.getStr("ApplyerPageType") + "',";
        sql += "'" + eajcstepbasicinfogt.getStr("ApplyerPageCode") + "',";
        sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode() + "'); end;";
        commonDaoFrom.execute(sql);
        commonDaoFrom.close();
    }

    public int insertRestQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt) {
        String sql = "declare v_clob clob :='" + eajcstepbasicinfogt.getStr("ACCEPTLIST") + "'; begin ";
        sql += "insert into JNING_XZXKSC.ea_jc_step_basicinfo(PROJPWD,VALIDITY_FLAG,Dataver,Stdver,Approvaltype,Promisetimelimit,Promisetimeunit,Timelimit";
        sql += ",Submit,Occurtime,Maketime,Acceptdeptcode1,Acceptdeptcode2,Itemregionid,ORGBUSNO,PROJID,Itemno,Itemname," +
                "Projectname,Applicant,Acceptdeptid,Region_id,Acceptdeptname,applicanttype,applicantcode,applicanttel," +
                "ACCEPTLIST,ITEMTYPE,CATALOGCODE,TASKCODE,APPLYERTYPE,ApplyerPageType,ApplyerPageCode,Acceptdeptcode)";
        sql += " values(";
        sql += "'" + eajcstepbasicinfogt.getProjpwd() + "',";
        sql += "" + eajcstepbasicinfogt.getValidity_flag() + ",";
        sql += "" + eajcstepbasicinfogt.getDataver() + ",";
        sql += "" + eajcstepbasicinfogt.getStdver() + ",";
        sql += "'" + eajcstepbasicinfogt.getApprovaltype() + "',";
        sql += "" + eajcstepbasicinfogt.getPromisetimelimit() + ",";
        sql += "" + eajcstepbasicinfogt.getPromisetimeunit() + ",";
        sql += "'" + eajcstepbasicinfogt.getTimelimit() + "',";
        sql += "'" + eajcstepbasicinfogt.getSubmit() + "',";
        sql += "to_date("+eajcstepbasicinfogt.getOccurtime()+",'yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date("+eajcstepbasicinfogt.getMaketime()+",'yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode1() + "',";
        sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode2() + "',";
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
        sql += "'" + eajcstepbasicinfogt.getStr("ApplyerPageType") + "',";
        sql += "'" + eajcstepbasicinfogt.getStr("ApplyerPageCode") + "',";
        sql += "'" + eajcstepbasicinfogt.getAcceptdeptcode() + "'); end;";
        int result= commonDaoFrom.execute(sql);
        commonDaoFrom.close();
        return result;
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
        sql += "to_date('" + eajcstepprocgt.getNodestarttime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date('" + eajcstepprocgt.getNodeendtime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepprocgt.getOrgbusno() + "',";
        sql += "'" + eajcstepprocgt.getProjid() + "')";
        commonDaoFrom.execute(sql);
        commonDaoFrom.close();
    }

    public int insertRestQzkProcess(eajcstepprocgt eajcstepprocgt) {
        String sql = "insert into JNING_XZXKSC.ea_jc_step_proc(Validity_flag,Dataver,Stdver,Nodestate,Occurtime";
        sql += ",Maketime,Signstate,Sn,Nodeadv,Noderesult,Itemregionid,Nodeprocerarea,Region_id,Nodename,Nodecode,Nodetype," +
                "Nodeprocer,Nodeprocername,Procunit,Procunitname,Nodestarttime,Nodeendtime,ORGBUSNO,PROJID)";
        sql += "VALUES(";
        sql += "" + eajcstepprocgt.getValidity_flag() + ",";
        sql += "" + eajcstepprocgt.getDataver() + ",";
        sql += "" + eajcstepprocgt.getStdver() + ",";
        sql += "'" + eajcstepprocgt.getNodestate() + "',";
        sql += "to_date("+eajcstepprocgt.getOccurtime()+",'yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date("+eajcstepprocgt.getMaketime()+",'yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepprocgt.getSignstate() + "',";
        sql += "" + eajcstepprocgt.getSn() + ",";
        sql += "'" + eajcstepprocgt.getNodeadv() + "',";
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
        sql += "to_date('" + eajcstepprocgt.getNodestarttime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date('" + eajcstepprocgt.getNodeendtime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepprocgt.getOrgbusno() + "',";
        sql += "'" + eajcstepprocgt.getProjid() + "')";
        int result = commonDaoFrom.execute(sql);
        commonDaoFrom.close();
        return result;
    }

    public void insertQzkDone(eajcstepdonegt eajcstepdonegt) {
        String sql = "insert into JNING_XZXKSC.ea_jc_step_done(Validity_flag,Stdver,Dataver,Doneresult,Approvallimit," +
                "Certificatenam,Isfee,Occurtime,Maketime,Signstate,Region_id,Itemregionid";
        sql += ",Orgbusno,Projid,Certificateno,Transactor) values(1,1,'1',0,sysdate,11,'0',sysdate,sysdate,'0',";
        sql += "'" + eajcstepdonegt.getRegion_id() + "',";
        sql += "'" + eajcstepdonegt.getItemregionid() + "',";
        sql += "'" + eajcstepdonegt.getOrgbusno() + "',";
        sql += "'" + eajcstepdonegt.getProjid() + "',";
        sql += "'" + eajcstepdonegt.getCertificateno() + "',";
        sql += "'" + eajcstepdonegt.getTransactor() + "')";
        commonDaoFrom.execute(sql);
        commonDaoFrom.close();
    }

    public int insertRestQzkDone(eajcstepdonegt eajcstepdonegt) {
        String sql = "insert into JNING_XZXKSC.ea_jc_step_done(Validity_flag,Stdver,Dataver,Doneresult," +
                "Certificatenam,Certificateno,Certificatelimit,Publisher,Isfee,Fee,Feestandaccord," +
                "Paypersonname,Payperidcard,Payermobile,Payertel," +
                "Occurtime,Maketime,Signstate,Region_id,Itemregionid," +
                "Orgbusno,Projid,Transactor) values(";
        sql += "" + eajcstepdonegt.getValidity_flag() + ",";
        sql += "" + eajcstepdonegt.getStdver() + ",";
        sql += "" + eajcstepdonegt.getDataver() + ",";
        sql += "" + eajcstepdonegt.getDoneresult() + ",";
        sql += "" + eajcstepdonegt.getCertificatenam() + ",";
        sql += "'" + eajcstepdonegt.getCertificateno() + "',";
        sql += "'" + eajcstepdonegt.getCertificatelimit() + "',";
        sql += "'" + eajcstepdonegt.getPublisher() + "',";
        sql += "'" + eajcstepdonegt.getIsfee() + "',";
        sql += "'" + eajcstepdonegt.getFee() + "',";
        sql += "'" + eajcstepdonegt.getFeestandaccord() + "',";
        sql += "'" + eajcstepdonegt.getPaypersonname() + "',";
        sql += "'" + eajcstepdonegt.getPayperidcard() + "',";
        sql += "'" + eajcstepdonegt.getPayermobile() + "',";
        sql += "'" + eajcstepdonegt.getPayertel() + "',";
        sql += "to_date("+eajcstepdonegt.getOccurtime()+",'yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date("+eajcstepdonegt.getMaketime()+",'yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepdonegt.getSignstate() + "',";
        sql += "'" + eajcstepdonegt.getRegion_id() + "',";
        sql += "'" + eajcstepdonegt.getItemregionid() + "',";
        sql += "'" + eajcstepdonegt.getOrgbusno() + "',";
        sql += "'" + eajcstepdonegt.getProjid() + "',";
        sql += "'" + eajcstepdonegt.getTransactor() + "')";
        int result= commonDaoFrom.execute(sql);
        commonDaoFrom.close();
        return result;
    }

    public void insertQzkSpecialnode(eajcstepspecialnode specialnode) {
        String sql = "insert into JNING_XZXKSC.ea_jc_step_specialnode(orgbusno,Projid,Region_id,Itemregionid,Stdver,Dataver,Procunitid,Procunitname,Nodeprocadv,Nodeprocaccord,Lists";
        sql += ",Noderesult,Maketime,SignState,Nodename,Sn,Procerid,Procername,Nodestarttime,Nodeendtime,Nodeprocaddr,Nodetime) values(";
        sql += "'" + specialnode.getOrgbusno() + "',";
        sql += "'" + specialnode.getProjid() + "',";
        sql += "'" + specialnode.getRegion_id() + "',";
        sql += "'" + specialnode.getItemregionid() + "',";
        sql += "'" + specialnode.getStdver() + "',";
        sql += "'" + specialnode.getDataver() + "',";
        sql += "'" + specialnode.getProcunitid() + "',";
        sql += "'" + specialnode.getProcunitname() + "',";
        sql += "'" + specialnode.getNodeprocadv() + "',";
        sql += "'" + specialnode.getNodeprocaccord() + "',";
        sql += "'" + specialnode.getLists() + "',";
        sql += "'" + specialnode.getNoderesult() + "',";

        if (specialnode.getMaketime() != null) {
            sql += "to_date('" + specialnode.getMaketime() + "','yyyy-MM-dd hh24:mi:ss'),";
        } else {
            sql += "NULL,";
        }

        sql += "'0',";

        sql += "'" + specialnode.getNodename() + "',";
        sql += "'" + specialnode.getSn() + "',";
        sql += "'" + specialnode.getProcerid() + "',";
        sql += "'" + specialnode.getProcername() + "',";

        if (specialnode.getNodestarttime() != null) {
            String starttime = EpointDateUtil.convertDate2String(specialnode.getNodestarttime(),
                    EpointDateUtil.DATE_TIME_FORMAT);
            sql += "to_date('" + starttime + "','yyyy-MM-dd hh24:mi:ss'),";

        } else {
            sql += " NULL,";
        }

        if (specialnode.getNodeendtime() != null) {
            String endtime = EpointDateUtil.convertDate2String(specialnode.getNodeendtime(),
                    EpointDateUtil.DATE_TIME_FORMAT);
            sql += "to_date('" + endtime + "','yyyy-MM-dd hh24:mi:ss'),";
        } else {
            sql += "NULL,";
        }

        sql += "'" + specialnode.getNodeprocaddr() + "',";

        if (specialnode.getNodetime() != null) {
            String nodeTime = EpointDateUtil.convertDate2String(specialnode.getNodetime(),
                    EpointDateUtil.DATE_TIME_FORMAT);
            sql += "to_date('" + nodeTime + "','yyyy-MM-dd hh24:mi:ss'))";
        } else {
            sql += "NULL)";
        }

        // Log.info("insertQzkSpecialnode=" + sql);
        commonDaoFrom.execute(sql);
        commonDaoFrom.close();
    }

    @Deprecated
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
        sql += "to_date('" + eajcstepbasicinfogt.getOccurtime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date('" + eajcstepbasicinfogt.getMaketime() + "','yyyy-MM-dd hh24:mi:ss'),";
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
        commonDaoFrom.close();
    }

    @Deprecated
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
        sql += "to_date('" + eajcstepprocgt.getNodestarttime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date('" + eajcstepprocgt.getNodeendtime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepprocgt.getNoderesult() + "',";
        sql += "to_date('" + eajcstepprocgt.getOccurtime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "to_date('" + eajcstepprocgt.getMaketime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepprocgt.getSignstate() + "',";
        sql += "'" + eajcstepprocgt.getItemregionid() + "')";
        commonDaoFrom.execute(sql);
        commonDaoFrom.close();
    }

    @Deprecated
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
        sql += "to_date('" + eajcstepdonegt.getApprovallimit() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepdonegt.getCertificatenam() + "',";
        sql += "'" + eajcstepdonegt.getCertificateno() + "',";
        sql += "'" + eajcstepdonegt.getIsfee() + "',";
        sql += "to_date('" + eajcstepdonegt.getOccurtime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepdonegt.getTransactor() + "',";
        sql += "to_date('" + eajcstepdonegt.getMaketime() + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + eajcstepdonegt.getSignstate() + "',";
        sql += "'" + eajcstepdonegt.getItemregionid() + "')";
        commonDaoFrom.execute(sql);
        commonDaoFrom.close();
    }

    public AuditTask getAuditTaskByTaskname(String taskname, String areacode, String ouguid) {
        String sql = "select * from audit_task where taskname = ?1 and is_enable = '1' and IFNULL(is_history,0) = 0 and IS_EDITAFTERIMPORT = '1' and areacode = ?2 and ouguid = ?3";
        AuditTask auditTask= commonDaoTo.find(sql, AuditTask.class, taskname, areacode, ouguid);
        commonDaoFrom.close();
        return auditTask;
    }

    public FrameOu getFrameOuByOunameNew(String ouname, String areacode) {
        String sql = "select a.ouname,a.oucode,a.ouguid from frame_ou a join frame_ou_extendinfo b on a.ouguid = b.ouguid where ouname = ?1 and SUBSTR(b.areacode,1,6)  = ?2 and a.oucode like 'JN%'";
        FrameOu frameOu= commonDaoTo.find(sql, FrameOu.class, ouname, areacode);
        commonDaoTo.close();
        return frameOu;
    }

    public FrameOu getFrameOuByOuname(String ouname) {
        String sql = "select a.ouname,a.oucode,a.ouguid from frame_ou a where a.ouname = ? and (a.oucode is not null and a.oucode != '')";
        FrameOu frameOu= commonDaoTo.find(sql, FrameOu.class, ouname);
        commonDaoTo.close();
        return frameOu;
    }

    public Record getTotalPorjectByNow() {
        String sql = "select count(1) as total,acceptuserdate from lc_project where DATE_FORMAT(Acceptuserdate,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d')";
        Record record = commonDaoTo.find(sql, Record.class);
        commonDaoTo.close();
        return record;
    }

    public Record getSizeInPorject() {
        String sql = "select * from in_project_record where DATE_FORMAT(todaytime,'%Y-%m-%d') = DATE_FORMAT(now(),'%Y-%m-%d') limit 1";
        Record record = commonDaoTo.find(sql, Record.class);
        commonDaoTo.close();
        return record;
    }

    public Record getJnzjProjectByRowguid(String rowguid) {
        String sql = "select * from jnzj_project where rowguid = ? and IFNULL(syncdone,0) = 0";
        Record record = commonDaoTo.find(sql, Record.class,rowguid);
        commonDaoTo.close();
        return record;
    }

    public void updatezjprojectByrowguid(String syncdone, String rowguid) {
        String sql = "update jnzj_project set syncdone=?1 where rowguid=?2";
        commonDaoTo.execute(sql, syncdone, rowguid);
        commonDaoTo.close();
    }

    public void deletejprojectByguid(String rowguid) {
        String sql = "delete from audit_project where rowguid=?";
        commonDaoTo.execute(sql, rowguid);
        commonDaoTo.close();
    }

    public void insertSbDate(Record record) {
        String sql = "insert into CJR.PERSON(xm,sfzhm,n,xb,shbzhm,cjlx,cjdj,ffsj,cjrzh";
        sql += ",ffyh,yhzh,dbzh,lxdh,jtzz ) values(";
        sql += "'" + record.get("xm") + "',";
        sql += "'" + record.get("sfzhm") + "',";
        sql += "'" + record.get("n") + "',";
        sql += "'" + record.get("xb") + "',";
        sql += "'" + record.get("shbzhm") + "',";
        sql += "'" + record.get("cjlx") + "',";
        sql += "'" + record.get("cjdj") + "',";
        sql += "to_date('" + record.get("ffsj") + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + record.get("cjrzh") + "',";
        sql += "'" + record.get("ffyh") + "',";
        sql += "'" + record.get("yhzh") + "',";
        sql += "'" + record.get("dbzh") + "',";
        sql += "'" + record.get("lxdh") + "',";
        sql += "'" + record.get("jtzz") + "')";
        sbDao.execute(sql);
        sbDao.close();
    }

    public void insertYbDate(Record record) {
        String sql = "insert into CJR.PERSON(xm,sfzhm,n,xb,shbzhm,cjlx,cjdj,ffsj,cjrzh";
        sql += ",ffyh,yhzh,dbzh,lxdh ) values(";
        sql += "'" + record.get("xm") + "',";
        sql += "'" + record.get("sfzhm") + "',";
        sql += "'" + record.get("n") + "',";
        sql += "'" + record.get("xb") + "',";
        sql += "'" + record.get("shbzhm") + "',";
        sql += "'" + record.get("cjlx") + "',";
        sql += "'" + record.get("cjdj") + "',";
        sql += "to_date('" + record.get("ffsj") + "','yyyy-MM-dd hh24:mi:ss'),";
        sql += "'" + record.get("cjrzh") + "',";
        sql += "'" + record.get("ffyh") + "',";
        sql += "'" + record.get("yhzh") + "',";
        sql += "'" + record.get("dbzh") + "',";
        sql += "'" + record.get("lxdh") + "')";
        ybDao.execute(sql);
        ybDao.close();
    }

    public int getNewInCountByAreacode(String areacode) {
        String sql = "select sum(count) areacount from in_exter_project where  areacode = ? and DATE_FORMAT( indate,'%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')";
        String count = commonDaoTo.queryString(sql, areacode);
        commonDaoTo.close();
        if (StringUtil.isBlank(count)) {
            return 0;
        }
        return Integer.parseInt(count);
    }

    public int getInCountByAreacode(String areacode) {
        String sql = "select sum(count) areacount from in_lc_project where  areacode = ? and DATE_FORMAT( indate,'%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')";
        String count = commonDaoTo.queryString(sql, areacode);
        commonDaoTo.close();
        if (StringUtil.isBlank(count)) {
            return 0;
        }
        return Integer.parseInt(count);
    }

    public void insertLcprojectRecord(String areacode, int count) {
        Record rec = new Record();
        rec.setPrimaryKeys("rowguid");
        rec.set("rowguid", UUID.randomUUID().toString());
        rec.set("count", count);
        rec.set("areacode", areacode);
        rec.set("indate", new Date());
        rec.setSql_TableName("in_lc_project");
        commonDaoTo.insert(rec);
        commonDaoTo.close();
    }

    public void insertExterRecord(String areacode, int count) {
        Record rec = new Record();
        rec.setPrimaryKeys("rowguid");
        rec.set("rowguid", UUID.randomUUID().toString());
        rec.set("count", count);
        rec.set("areacode", areacode);
        rec.set("indate", new Date());
        rec.setSql_TableName("in_exter_project");
        commonDaoTo.insert(rec);
        commonDaoTo.close();
    }

    public void deleteQzkBaseInfo(String guid) {
        String sql = "delete from JNING_XZXKSC.ea_jc_step_basicinfo_gt where rowguid = ?";
        commonDaoFrom.execute(sql, guid);
        commonDaoFrom.close();
    }

    public void deleteQzkProcess(String guid) {
        String sql = "delete from JNING_XZXKSC.ea_jc_step_proc_gt where rowguid = ?";
        commonDaoFrom.execute(sql, guid);
        commonDaoFrom.close();
    }

    public void deleteQzkDone(String guid) {
        String sql = "delete from JNING_XZXKSC.ea_jc_step_done_gt where rowguid = ?";
        commonDaoFrom.execute(sql, guid);
        commonDaoFrom.close();
    }

    public eajcstepbasicinfogt getQzkBaseInfo(String flowsn) {
        String sql = "select * from JNING_XZXKSC.ea_jc_step_basicinfo where PROJID = ?";
        eajcstepbasicinfogt eajcstepbasicinfogt1= commonDaoFrom.find(sql, eajcstepbasicinfogt.class, flowsn);
        commonDaoFrom.close();
        return eajcstepbasicinfogt1;
    }

    public eajcstepbasicinfogt getQzkBaseInfoByOrgbusno(String orgbusno) {
        String sql = "select * from JNING_XZXKSC.ea_jc_step_basicinfo where orgbusno = ?";
        eajcstepbasicinfogt eajcstepbasicinfogt1= commonDaoFrom.find(sql, eajcstepbasicinfogt.class, orgbusno);
        commonDaoFrom.close();
        return eajcstepbasicinfogt1;
    }

    public Record getQzkStepInfo(String orgbusno, String sn) {
        String sql = "select * from JNING_XZXKSC.ea_jc_step_proc where orgbusno = ? and sn = ?";
        Record eajcstepbasicinfogt1= commonDaoFrom.find(sql, Record.class, orgbusno, sn);
        commonDaoFrom.close();
        return eajcstepbasicinfogt1;
    }

    public Record getQzkBanjieInfo(String orgbusno) {
        String sql = "select * from JNING_XZXKSC.ea_jc_step_done where orgbusno = ?";
        Record eajcstepbasicinfogt1= commonDaoFrom.find(sql, Record.class, orgbusno);
        commonDaoFrom.close();
        return eajcstepbasicinfogt1;
    }

    public int insertProject(AuditProject auditProject) {
        int result = commonDaoTo.insert(auditProject);
        commonDaoTo.close();
        return result;
    }

    public AuditProject getProjectByFlowsn(String flowsn) {
        String sql = "select * from AUDIT_PROJECT where flowsn = ?";
        AuditProject auditProject= commonDaoTo.find(sql, AuditProject.class, flowsn);
        commonDaoTo.close();
        return auditProject;
    }

}
