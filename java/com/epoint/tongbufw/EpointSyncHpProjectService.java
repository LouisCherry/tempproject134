package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;

public class EpointSyncHpProjectService
{
    transient Logger log = LogUtil.getLog(EpointSyncDone.class);
    private IAuditTaskExtension auditTaskExtensionService = ContainerFactory.getContainInfo()
            .getComponent(IAuditTaskExtension.class);;
    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "hpqzkurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "hpqzkusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "hpqzkpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public EpointSyncHpProjectService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public List<Record> getPhaseList(String proid) {
        String sql = "select * from pre_phase where sysnid='I' and proId=?";
        return commonDaoFrom.findList(sql, Record.class, proid);
    }

    public void insertPhase(Record record2) {
        record2.setSql_TableName("pre_phase");
        commonDaoTo.insert(record2);
    }

    public List<Record> getUpdateList() {
        String sql = "select * from pre_phase where sysnid='U'";
        return commonDaoFrom.findList(sql, Record.class);
    }

    public List<Record> getProjectList() {
        String sql = "select b.* From t_WORKFLOW_GZLCSL a join t_XMSP_SP_JBXX b on a.YWXTBH = b.xh where YWZT = 'Finished' and extract(year from KSSJ)= '2021' limit 20";
        return commonDaoFrom.findList(sql, Record.class);
    }

    public List<Record> getProjectDetail(String xh) {
        String sql = "select * from  T_XMSP_ZWFW_JBXX where xh = ?";
        return commonDaoFrom.findList(sql, Record.class,xh);
    }

    public void insertProject(Record record2) {
        record2.setSql_TableName("pre_project");
        commonDaoTo.insert(record2);
    }

    public void updatePhase(Record record2) {
        commonDaoTo.update(record2);
    }

    public Record select(Object object) {
        String sql = "select * from pre_phase where UNID=?";
        return commonDaoTo.find(sql, Record.class, object);
    }

    public List<Record> getDeleteList() {
        String sql = "select * from pre_phase where sysnid='D'";
        return commonDaoFrom.findList(sql, Record.class);
    }

    public void delete(Record record2) {
        commonDaoTo.delete(record2);
    }

    public void addProject(AuditProject auditproject, AuditTask auditTask) {
        AuditTaskExtension auditTaskExtension = (AuditTaskExtension) auditTaskExtensionService
                .getTaskExtensionByTaskGuid(auditTask.getRowguid(), false).getResult();
        if (auditTaskExtension == null) {
            auditTaskExtension = new AuditTaskExtension();
        }
        //原办件使用的是操作人姓名为济宁管理员
        //auditproject.setOperateusername("济宁管理员");
        auditproject.setOperateusername("济宁市自建系统同步办件");
        auditproject.setOperatedate(new Date());
        auditproject.setTask_id(auditTask.getTask_id());
        auditproject.setTaskguid(auditTask.getRowguid());
        auditproject.setPromise_day(auditTask.getPromise_day());

        auditproject.setIs_delay(Integer.valueOf(20));
        auditproject.setProjectname(auditTask.getTaskname());
        auditproject.setInsertdate(new Date());
        auditproject.setIs_charge(auditTask.getCharge_flag());
        auditproject.setCharge_when(auditTaskExtension.getCharge_when());
        auditproject.setTasktype(auditTask.getType());
//        Integer applyWay = auditTaskExtension.getWebapplytype();
//        if (ZwfwConstant.WEB_APPLY_TYPE_NOT.equals(applyWay)) {
//            auditproject.setApplyway(Integer.valueOf(Integer.parseInt("11")));
//        }
//        else if (ZwfwConstant.WEB_APPLY_TYPE_YS.equals(applyWay)) {
//            auditproject.setApplyway(Integer.valueOf(Integer.parseInt("10")));
//        }
        auditproject.setApplyway(Integer.valueOf(ZwfwConstant.APPLY_WAY_NETSBYS));
        auditproject.setOuguid(auditTask.getOuguid());
        auditproject.setOuname(auditTask.getOuname());
        if(StringUtil.isBlank(auditproject.getAreacode())){
            auditproject.setAreacode(auditTask.getAreacode());
        }
        //is_lczj null,0：一窗受理系统办件，1，浪潮对接自建办件，2：新点对接济宁市自建办件
        auditproject.set("is_lczj", "2");
        auditproject.setTaskid(auditTask.getTask_id());
        auditproject.setTask_id(auditTask.getTask_id());
        auditproject.setIf_jz_hall(auditTaskExtension.getIf_jz_hall());
        IAuditProject auditProjectService = (IAuditProject) ContainerFactory.getContainInfo()
                .getComponent(IAuditProject.class);
        auditProjectService.addProject(auditproject);
    }

    public AuditTask gettaskbydeptask(String deptask,String areacode) {
        String sql = "SELECT b.* from zj_deptask a LEFT JOIN audit_task b ON a.itemid=b.item_id WHERE a.deptask=?1"
                + " and IFNULL(IS_HISTORY,0)=0 and IS_EDITAFTERIMPORT=1 and IS_ENABLE=1 and areacode = ?2";
        return CommonDao.getInstance().find(sql, AuditTask.class, deptask,areacode);
    }

    public String getcenterbyarea(String code) {
        CommonDao dao = CommonDao.getInstance();
        String sql = "select rowguid from audit_orga_servicecenter where belongxiaqu=?";
        String result = dao.queryString(sql, code);
        return result;
    }

    public int insertbyrecord(WorkflowWorkItemHistory workflow) {
        int result = commonDaoTo.insert(workflow);
        return result;
    }

    public int updateflag(String unid,String sync) {
        String sql = "update pre_phase set sysnId='"+sync+"' where unid=?";
        int result = commonDaoFrom.execute(sql, unid);
        commonDaoFrom.close();
        return result;
    }

    public int updatesysid(String proid) {
        String sql = "update pre_project set synid='S' where unid=?";
        int result = commonDaoFrom.execute(sql, proid);
        commonDaoFrom.close();
        return result;
    }

    public void updateProject(String name2, String time, String projectguid) {
        String sql = "update audit_project set ACCEPTUSERNAME='" + name2
                + "' WHERE RowGuid='" + projectguid + "'";
        
        if (StringUtil.isNotBlank(time)) {
        	sql = "update audit_project set ACCEPTUSERDATE='" + time + "', ACCEPTUSERNAME='" + name2
                    + "' WHERE RowGuid='" + projectguid + "'";
        }
        commonDaoTo.execute(sql);
    }

    public void updatebanjieProject(String name2, String time, String projectguid) {
        String sql = "update audit_project set BANJIEUSERNAME=?,BANJIEDATE=? WHERE RowGuid='" + projectguid + "'";
        commonDaoTo.execute(sql, name2, time);

    }

    public int updateonesysid(String proid) {
        String sql = "update pre_project set synid='U' where unid=?";
        int result = commonDaoFrom.execute(sql, proid);
        commonDaoFrom.close();
        return result;
    }
    
    public Record getBaseinfoByXmdm(String xmdm){
    	String sql = "select * from audit_rs_item_baseinfo where itemcode = ? limit 1";
    	return commonDaoTo.find(sql, Record.class, xmdm);
    }
}
