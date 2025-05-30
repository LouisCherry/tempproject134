package com.epoint.xmz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 好差评相关接口的详细实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public class JnService
{
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;


    protected ICommonDao jnzjxtdjDao;

    /**
     * 前置库数据源
     */
    private static String jnzjxtdjurl = ConfigUtil.getConfigValue("datasyncjdbc", "jnzjxtdjurl");
    private static String jnzjxtdjusername = ConfigUtil.getConfigValue("datasyncjdbc", "jnzjxtdjusername");
    private static String jnzjxtdjpassword = ConfigUtil.getConfigValue("datasyncjdbc", "jnzjxtdjpassword");
    private DataSourceConfig jnzjxtdjDataSourceConfig = new DataSourceConfig(jnzjxtdjurl, jnzjxtdjusername, jnzjxtdjpassword);

    public JnService() {
        dao = CommonDao.getInstance();
        jnzjxtdjDao = CommonDao.getInstance(jnzjxtdjDataSourceConfig);
    }



	public int insert(Record rec) {
		return dao.insert(rec);
	}

	 public int insertbyrecord(WorkflowWorkItem workflow) {
	    return dao.insert(workflow);
	 }


    /**
     * 获取办件基本信息
     * @param taskName 事项名称
     * @param areacode 辖区编码
     * @return 返回事项基本信息
     */
    public AuditTask getAuditBasicInfo(String taskName, String areacode){
        String sql =" select * from audit_task where taskname = ? and ifnull(is_history,0)=0 and is_editafterimport = 1 and is_enable = 1 And areacode = ? ";
        return dao.find(sql,AuditTask.class,taskName,areacode);
    }

    /**
     * 获取办件基本信息
     * @param item_id 事项编码
     * @return 返回事项基本信息
     */
    public AuditTask getAuditBasicInfo(String item_id){
        String sql =" select * from audit_task where item_id = ? and ifnull(is_history,0)=0 and is_editafterimport = 1 and is_enable = 1 ";
        return dao.find(sql,AuditTask.class,item_id);
    }
    /**
     * 通过rowguid查询表中是已有否有对应数据
     * @param rowguid
     * @return
     */
    public Record getAuditProjectZjxtByRowguid(String rowguid){
        String sql = " select * from audit_project_zjxt where rowguid = ? ";
        return dao.find(sql,Record.class,rowguid);
    }

    /**
     * 通过rowguid查询表中是已有否有对应数据
     * @param rowguid
     * @return
     */
    public Record getAuditRsApplyZjxtByRowguid(String rowguid){
        String sql = " select * from AUDIT_RS_APPLY_PROCESS_ZJXT where rowguid = ? ";
        return dao.find(sql,Record.class,rowguid);
    }



    public String getAreacodeByareaname(String areaname) {
        String sql = " select ITEMVALUE from  code_items ci  where CODEID  ='1015788' and itemtext  = ? ";
        return dao.find(sql,String.class,areaname);
    }


    /**
     * 获取前置库inf_apply_baseinfo_r表中市场监管局的办件信息
     * @return
     */
    public List<Record> getSCJGJApplyBaseInfo(Date beginOfDate,Date endOfDate){
        String sql = " select * from inf_apply_baseinfo_r where OperateDate >= ? and OperateDate <=? ";
        List<Record> list = jnzjxtdjDao.findList(sql, Record.class, beginOfDate, endOfDate);
        jnzjxtdjDao.close();//防止连接泄露
        return list;
    }

    /**
     * 获取前置库inf_apply_process_r表中市场监管局的办件流程
     * @param beginOfDate 开始时间
     * @param endOfDate 结束时间
     * @return 信息列表
     */
    public List<Record> getSCJGJApplyProcess(Date beginOfDate,Date endOfDate){
        String sql = " select * from inf_apply_process_r where VERSIONTIME >= ? and VERSIONTIME <= ? ";
        List<Record> list = jnzjxtdjDao.findList(sql, Record.class, beginOfDate, endOfDate);
        jnzjxtdjDao.close();
        return list;
    }

    /**
     * 根据办件projectid查询办件流程信息
     * @param projectid
     * @return
     */
    public List<Record> getSCJGJApplyProcess(String projectid){
        String sql = "  select * from inf_apply_process_r where  projectid = ? ";
        List<Record> list = jnzjxtdjDao.findList(sql, Record.class, projectid);
        jnzjxtdjDao.close();
        return list;
    }
    /**
     * 更新前置库inf_apply_baseinfo_r表中市场监管局的办件信息的同步标志
     * @param rowguid
     */
    public void upApplyBaseInfoSign(String rowguid){
        String sql = " update inf_apply_baseinfo_r set SYNC_SIGN ='1' where RowGuid =? ";
        jnzjxtdjDao.execute(sql,rowguid);
        jnzjxtdjDao.close();
    }

    /**
     * 更新前置库inf_apply_process_r表中市场监管局的办件信息的同步标志
     * @param rowguid
     */
    public void upApplyProcessSign(String rowguid){
        String sql = " update inf_apply_process_r set SYNC_SIGN ='1' where RowGuid =? ";
        jnzjxtdjDao.execute(sql,rowguid);
        jnzjxtdjDao.close();
    }

    /**
     * 根据事项名称，和区域编码获取事项基本信息
     * @param taskName 事项名称
     * @param areacode 区域编码
     * @return 事项基本信息
     */
    public AuditTask getAuditTaskInfo(String taskName,String areacode){
        String sql = " select * from audit_task where TaskName = ? and ifnull(IS_HISTORY,0)=0 and IS_EDITAFTERIMPORT = 1 and IS_ENABLE = 1 and AREACODE = ? ";
        return dao.find(sql,AuditTask.class,taskName,areacode);
    }

    /**
     * 查询数据库当天是否已有【省外建筑业企业入鲁报送基本信息】的同步信息
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Record> getSWJZQYDataAuditProjectZjxtByTime(Date startDate,Date endDate){
        String sql = " select * from audit_project_zjxt where OperateDate >= ? and OperateDate <= ? and datasource ='006' ";
        return  dao.findList(sql,Record.class,startDate,endDate);
    }

    public AuditTask getAuditBasicInfoDetail(String taskName, String areacode){
        String sql =" select * from audit_task a join frame_ou_extendinfo b on a.ouguid = b.ouguid where a.taskname = ? and ifnull(a.is_history,0)=0 and a.is_editafterimport = 1 and a.is_enable = 1 And b.areacode = ? ";
        return dao.find(sql,AuditTask.class,taskName,areacode);
    }

    public String getZjSlFlowsn(String custom){
        String sql =" select flowsn from zj_shuilu_flowsn where custom = ?";
        return dao.find(sql,String.class,custom);
    }

    public void updateZjSlFlowsn(String flowsn,String custom){
    	String sql =" update zj_shuilu_flowsn set flowsn = ? where custom = ?";
    	dao.execute(sql, flowsn,custom);
    }

    /**
     * 获取前置库inf_apply_baseinfo_r表中市场监管局的办件信息
     * @return
     */
    public List<Record> getSCBDCRSData(Date beginOfDate,Date endOfDate){
        String sql1 = " select * from EA_JC_STEP_BASICINFO_BDC where OperateDate >= ? and OperateDate <=? ";
        List<Record> list1 = jnzjxtdjDao.findList(sql1, Record.class, beginOfDate, endOfDate);
        String sql2 = " select * from EA_JC_STEP_DONE_BDC where OperateDate >= ? and OperateDate <=? ";
        List<Record> list2 = jnzjxtdjDao.findList(sql2, Record.class, beginOfDate, endOfDate);
        String sql3 = " select * from EA_JC_STEP_PROC_BDC where OperateDate >= ? and OperateDate <=? ";
        List<Record> list3 = jnzjxtdjDao.findList(sql3, Record.class, beginOfDate, endOfDate);
        String sql4 = " select * from EA_JC_STEP_BASICINFO where OperateDate >= ? and OperateDate <=? ";
        List<Record> list4 = jnzjxtdjDao.findList(sql4, Record.class, beginOfDate, endOfDate);
        String sql5 = " select * from EA_JC_STEP_DONE where OperateDate >= ? and OperateDate <=? ";
        List<Record> list5 = jnzjxtdjDao.findList(sql5, Record.class, beginOfDate, endOfDate);
        String sql6 = " select * from EA_JC_STEP_PROC where OperateDate >= ? and OperateDate <=? ";
        List<Record> list6 = jnzjxtdjDao.findList(sql6, Record.class, beginOfDate, endOfDate);

        jnzjxtdjDao.close();//防止连接泄露
        /*Record record = new Record();
        record.setSql_TableName("EA_JC_STEP_BASICINFO_BDC");
        list1.add(record);*/

        List<Record> list = new ArrayList();
        list.addAll(list1);
        list.addAll(list2);
        list.addAll(list3);
        list.addAll(list4);
        list.addAll(list5);
        list.addAll(list6);


        return list;
    }

    /**
     * 通过name获取办件基本信息
     * @param
     * @return 返回事项基本信息
     */
    public AuditTask getAuditBasicInfoByName(String taskname){
        String sql =" select * from audit_task where taskname = ? and ifnull(is_history,0)=0 and is_editafterimport = 1 and is_enable = 1 ";
        return dao.find(sql,AuditTask.class,taskname);
    }
}
