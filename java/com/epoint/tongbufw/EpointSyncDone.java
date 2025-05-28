package com.epoint.tongbufw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.commonvalue.FWConstants;
import com.epoint.audittask.util.SyncCommonUtil;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.service.FrameAttachInfoNewService9;
import com.epoint.workflow.service.common.custom.Activity;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.config.WorkflowProcess;
import com.epoint.workflow.service.common.entity.config.WorkflowProcessVersion;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowProcessService;

/**
 * 事项同步服务
 * 
 * @author xbn
 * @version 
 */
public class EpointSyncDone
{

    transient  Logger log = LogUtil.getLog(EpointSyncDone.class);

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected  ICommonDao commonDaoTo;
    private IWorkflowProcessService iWorkflowProcessService = ContainerFactory.getContainInfo()
            .getComponent(IWorkflowProcessService.class);
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzpassword");
    private static String IS_EDITAFTERIMPORT = ConfigUtil.getConfigValue("datasyncjdbc", "is_editafterimport");


    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public EpointSyncDone() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }


    public List<Record> getQLSXnew() {
        String sql = "select * from (select DISTINCT INNER_CODE,REGION_CODE,ORG_CODE,ROWGUID from QLT_QLSX where NVL(QFSX,'0') = '0'"
                + "  and dh_state=1  order by REGION_CODE) a  where  rownum <=3";
        List<Record> r = commonDaoFrom.findList(sql, Record.class);
        //commonDaoFrom.close();
        return r;
    }

    public Record getOuInfo(String ouCode) {
        String sql = "select OUName,OUGuid,OUCODE from Frame_OU where OUCode=?";
        Record r = commonDaoTo.find(sql, Record.class, ouCode);
        return r;
    }


    /**
     * 获取前置库事项下面的子事项order by version
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param innerCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> selectRightByRowGuid(String innerCode,String ORG_CODE) {
        String sql = "select * from QLT_QLSX where dh_state=1 and NVL(QFSX,'0')<>'9' and NVL(QFSX,'0')<>'10' and inner_code='"+innerCode+"'"
                + " and ORG_CODE=? ORDER BY to_number(VERSION) DESC";
        List<Record> list = commonDaoFrom.findList(sql, Record.class,ORG_CODE);
        //commonDaoFrom.close();
        return list;
    }
    
    public AuditTaskResult getTaskResult(String taskguid){
        String sql = "SELECT * FROM audit_task_result WHERE TASKGUID =?";
        return commonDaoTo.find(sql, AuditTaskResult.class, taskguid);
    }
    
    public int updateTaskResult(String taskguid,String resultguid){
        String sql = "UPDATE audit_task_result SET TASKGUID=?1 WHERE rowguid =?2";
        return commonDaoTo.execute(sql,taskguid,resultguid);
    }

    /**
     * 新增事项
     */
    public void insertTask(Record right, Record dvOuInfo) {
        log.info("开始新增旧事项");
        //事项
        AuditTask audittask = new AuditTask();
        //事项扩展
        AuditTaskExtension audittaskex = new AuditTaskExtension();
        // 事项的基本信息添加
        audittask.setOperateusername("旧同步服务");
        audittask.setOperatedate(new Date());
        String taskguid = UUID.randomUUID().toString();
        audittask.setRowguid(taskguid);
        audittask.setIs_history(0);
        //是否启用
        audittask.setIs_enable(1);
        //是否为模板事项
        audittask.setIstemplate(0);
        //事项审核状态  4：待确认
        if("1".equals(IS_EDITAFTERIMPORT)){
            audittask.setIs_editafterimport(1);
        }else{
            audittask.setIs_editafterimport(4);
        }
        audittask.setTaskname(right.getStr("NAME"));
        audittask.setItem_id(right.getStr("INNER_CODE"));
        audittask.set("unid", right.getStr("ROWGUID"));
        String taskId = UUID.randomUUID().toString();
        audittask.setTask_id(taskId);
        String versionNew = "";
        if (StringUtil.isNotBlank(right.getStr("VERSION"))) {
            audittask.setVersion(right.getStr("VERSION"));
            versionNew = right.getStr("VERSION");
        }
        else {
            audittask.setVersion("1");
            versionNew = "1";
        }
        audittask.setVersiondate(new Date());
        audittask.setTasksource("2");
        //audittask.setQl_dept(dvOuInfo.getStr("OUName"));//受理机构
        //audittask.setDept_ql_id(dvOuInfo.getStr("OUCode"));//审核不通过意见
        //audittask.setDecision_dep(dvOuInfo.getStr("OUName"));//决定机构
        // 辖区
        //audittask.setAreacode("100001");
        audittask.setAreacode(right.getStr("REGION_CODE").substring(0, 6));
        if (dvOuInfo == null) {
            audittask.setOuguid(right.getStr("ORG_CODE"));
            audittask.setOuname(right.getStr("ORG_NAME"));
        }
        else {
            audittask.setOuguid(dvOuInfo.getStr("ouguid"));
            audittask.setOuname(dvOuInfo.getStr("ouname"));
        }

        // 审批类别和审批系统中代码项(审批结果)对应
        switch (right.getStr("TYPE")) {
            case "XK":
                audittask.setShenpilb("01");
                break;
            case "GG":
                audittask.setShenpilb("11");
                break;
            case "QT":
                audittask.setShenpilb("10");
                break;
            case "CF":
                audittask.setShenpilb("02");
                break;
            case "QZ":
                audittask.setShenpilb("03");
                break;
            case "ZJJG":
                audittask.setShenpilb("15");
                break;
            default:
                audittask.setShenpilb("10");
                break;
        }

        Integer assort = null;
        if (StringUtil.isBlank(right.getStr("ASSORT"))) {
            //大汉前置空也是即办件
            assort = 1;
        }
        else {
            if (FWConstants.STRONE.equals(right.getStr("ASSORT"))) {
                assort = 2;
            }
            else {
                if (FWConstants.STRTWO.equals(right.getStr("ASSORT"))) {
                    assort = 1;
                }
                else {
                    if (FWConstants.STRTHREE.equals(right.getStr("ASSORT"))) {
                        assort = 3;
                    }
                    else {
                        if (FWConstants.STRFOUR.equals(right.getStr("ASSORT"))) {
                            assort = 4;
                        }
                        else {
                            assort = 1;
                        }
                    }
                }
            }
        }
        //办件类型  1.即办 .....
        if(assort==1){
        	audittask.setJbjmode("1");
        }
        audittask.setType(assort);
        audittask.setQlfullid(right.getStr("ITEM_CODE"));
        audittask.set("charge_lc", "1");
        audittask.setFullid(right.getStr("ITEM_CODE"));
        
        // 大汉前置库没有相应字段，默认为个人法人
        audittask.setApplyertype("10,20");
        //承诺期限
        String ACCEPT_TIME = right.getStr("ACCEPT_TIME");
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:");
        audittask.setPromise_day(StringUtil.isBlank(ACCEPT_TIME)?1:Integer.parseInt(ACCEPT_TIME));
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:");
        //法定时限
        String LAW_TIME = right.getStr("LAW_TIME");
        audittask.setAnticipate_day(StringUtil.isBlank(LAW_TIME)?1:Integer.parseInt(LAW_TIME));
        //是否收费
        String IS_CHARGE = right.getStr("IS_CHARGE");
        audittask.setCharge_flag(StringUtil.isBlank(IS_CHARGE)?0:Integer.parseInt(IS_CHARGE));

        //-----暂不解析，后期解析----------//收费依据
        audittask.setCharge_basis(right.getStr("Charge_Basis"));
        //联系电话
        audittask.setLink_tel(right.getStr("WINDOW_PHONE"));
        //监督投诉电话
        audittask.setSupervise_tel(right.getStr("COMPLAIN_PHONE"));

        String law = right.getStr("LAWBASIS").replace("<br>", "");
        //法律依据
        audittask.setBy_law(law);
        //受理条件
        String APPLY_CONDITION = right.getStr("APPLY_CONDITION");
        if(StringUtil.isNotBlank(APPLY_CONDITION)){
            audittask.setAcceptcondition(APPLY_CONDITION);
        }else{
            audittask.setAcceptcondition("符合相关规定，提交相关申请材料");
        }
        //办理地址
        audittask.setTransact_addr(right.getStr("FWCK"));
        //audittask.setTransact_time(right.getStr("SL_Time"));//工作时间
        audittask.setTransact_time("周一至周五 上午9:00-12:00 下午13:00-17:00");
        // 外部流程图
        try {
            byte[] outimg = (byte[]) right.get("OUT_FLOW_IMG");
            String taskOutImgGuid = insertFrameAttach(outimg);
            audittask.setTaskoutimgguid(taskOutImgGuid);
        }
        catch (Exception e) {
            log.info("外部流程图同步失败"+e.getMessage());
        }

        WorkflowProcess workflowProcess = new WorkflowProcess();
        workflowProcess.setProcessName(right.getStr("NAME") + "【版本：" + versionNew + "】");
        workflowProcess.setProcessGuid(UUID.randomUUID().toString());
        WorkflowProcessVersion workflowProcessVersion = iWorkflowProcessService.addWorkflowProcess(workflowProcess,
                "同步业务服务", null);
        audittask.setPvguid(workflowProcessVersion.getProcessVersionGuid());
        audittask.setProcessguid(workflowProcessVersion.getProcessGuid());

        // 事项扩展信息表
       
        audittaskex.setOperateusername("旧同步服务");
        audittaskex.setOperatedate(new Date());
        audittaskex.setRowguid(UUID.randomUUID().toString());
        audittaskex.setTaskguid(taskguid);
        audittaskex.set("innerno", right.getStr("innerno"));
        //是否允许批量录入
        audittaskex.setIs_allowbatchregister(0);
        //audittaskex.setSubjectnature(right.getStr("ORG_PROPERTY"));// 实施主体性质
        audittaskex.setTaskadduserdisplayname("旧同步服务");
        //常见问题
        audittaskex.setQa_info(right.getStr("COMMON_QUESTION"));
        String rightClassQiyezt = right.getStr("RIGHTCLASS_QIYEZT");
        if (rightClassQiyezt.endsWith(FWConstants.SEMICOLON)) {
            //法人主题分类
            audittaskex.setTaskclass_forcompany(";" + right.getStr("RIGHTCLASS_QIYEZT"));
        }
        else {
            //法人主题分类
            audittaskex.setTaskclass_forcompany(";" + right.getStr("RIGHTCLASS_QIYEZT") + ";");
        }
        String rightClassGerensx = right.getStr("RIGHTCLASS_GERENSX");
        if (rightClassGerensx.endsWith(FWConstants.SEMICOLON)) {
            //个人主题分类
            audittaskex.setTaskclass_forpersion(";" + right.getStr("RIGHTCLASS_GERENSX"));
        }
        else {
            //个人主题分类
            audittaskex.setTaskclass_forpersion(";" + right.getStr("RIGHTCLASS_GERENSX") + ";");
        }
        audittaskex.setIs_simulation(0);
        audittaskex.setIsriskpoint(0);

        audittaskex.setIszijianxitong(0);
        //是否支持预约办理
        audittaskex.setReservationmanagement("1");
        audittaskex.setOnlinepayment("0");
        audittaskex.setIf_express("0");
        String webapplytype = right.getStr("WEBAPPLYDEPTH");
        //WEBAPPLYDEPTH 浪潮事项深度，事项公开（一级）在线预审（二级）在线申办（三级）全程网办（四级）
        //网上申报类型：不允许网上申报0；网上申报后预审1；网上申报后直接办理2
        if("2".equals(webapplytype) || "1".equals(webapplytype)){
            audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
        }else if("4".equals(webapplytype)){
            audittaskex.setWebapplytype(2);// 网上申报类型：网上申报后直接办理
        }else{
            audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
        }
        //同步事项情形信息
        audittaskex.set("CASE_SETTING_INFO", right.getStr("CASE_SETTING_INFO"));
        audittaskex.setSubjectnature("1");
        audittaskex.setUse_level("4");
        audittaskex.setNotify_ys("[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，预审已获通过，请带齐申报资料到中心窗口办理。");
        audittaskex.setNotify_nys(
                "[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，由于[#=Reason#]，预审未获通过，特此通知。");
        audittaskex.setNotify_pz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批已获批准，请尽快到中心窗口办理。");
        audittaskex.setNotify_npz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批未获批准，特此通知。");
        audittaskex.setNotify_sl("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，经审查符合受理条件，决定受理。");
        audittaskex.setNotify_nsl("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，经审查不符合受理条件，决定不予受理。");

        commonDaoTo.insert(audittaskex);
        log.info("新增旧事项信息成功");
        commonDaoTo.insert(audittask);
        log.info("新增旧事项扩展信息成功");
        commonDaoTo.commitTransaction();

        //生成默认流程
        handleDefaultFlow(audittask, audittask.getAreacode());
        //同步材料
        if(StringUtil.isNotBlank(right.getStr("MATERIAL_INFO"))){
            syncMaterial(taskguid, right.getStr("MATERIAL_INFO"));

        }else{
            log.info("材料节点为空");
        }

        //同步常见问题
        syncTaskFaq(right.getStr("COMMON_QUESTION"), taskId);
        this.log.info("同步常见问题成功");
    }

    /**
     * 
     *  [更新事项]
     *  [功能详细描述]
     *  @param right
     *  @param dvOuInfo
     *  @param task    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateTask(Record right, Record dvOuInfo, AuditTask task) {
        log.info("开始更新旧事项");
        AuditTask audittask = (AuditTask) task.clone();
        AuditTaskExtension copytaskex = getTaskEx(task.getRowguid());
        AuditTaskExtension audittaskex = (AuditTaskExtension) copytaskex.clone();
        AuditTaskResult result = getTaskResult(task.getRowguid());
        
        // 事项的基本信息添加
        audittask.setOperateusername("同步旧服务");
        audittask.setOperatedate(new Date());
        String taskguid = UUID.randomUUID().toString();
        if(result!=null){
            result.setOperateusername("同步旧服务");
            result.setOperatedate(new Date());
            result.setRowguid(UUID.randomUUID().toString());
            result.setTaskguid(taskguid);
        }
        audittask.setRowguid(taskguid);
        audittask.setIs_history(0);
        //是否启用
        audittask.setIs_enable(1);
        //是否为模板事项
        audittask.setIstemplate(0);
        //事项审核状态  4：待确认
        if("1".equals(IS_EDITAFTERIMPORT)){
            audittask.setIs_editafterimport(1);
        }else{
            audittask.setIs_editafterimport(4);
        }
        audittask.setTaskname(right.getStr("NAME"));
        audittask.setItem_id(right.getStr("INNER_CODE"));
        audittask.set("unid", right.getStr("ROWGUID"));//保存前置库事项基本信息主键
        String taskId = task.getTask_id();
        audittask.setTask_id(taskId);
        String versionNew = "";
        if (StringUtil.isNotBlank(right.getStr("VERSION"))) {
            audittask.setVersion(right.getStr("VERSION"));
            versionNew = right.getStr("VERSION");
        }
        else {
            audittask.setVersion("1");
            versionNew = "1";
        }
        audittask.setVersiondate(new Date());
        audittask.setTasksource("2");
        //audittask.setQl_dept(dvOuInfo.getStr("OUName"));//受理机构
        //audittask.setDept_ql_id(dvOuInfo.getStr("OUCode"));//审核不通过意见
        //audittask.setDecision_dep(dvOuInfo.getStr("OUName"));//决定机构
        // 辖区
        audittask.setAreacode(right.getStr("REGION_CODE").substring(0, 6));

        if (dvOuInfo == null) {
            audittask.setOuguid(right.getStr("ORG_CODE"));
            audittask.setOuname(right.getStr("ORG_NAME"));

        }
        else {
            audittask.setOuguid(dvOuInfo.getStr("ouguid"));
            audittask.setOuname(dvOuInfo.getStr("ouname"));
        }

        // 审批类别和审批系统中代码项(审批结果)对应
        switch (right.getStr("TYPE")) {
            case "XK":
                audittask.setShenpilb("01");
                break;
            case "GG":
                audittask.setShenpilb("11");
                break;
            case "QT":
                audittask.setShenpilb("10");
                break;
            case "CF":
                audittask.setShenpilb("02");
                break;
            case "QZ":
                audittask.setShenpilb("03");
                break;
            case "ZJJG":
                audittask.setShenpilb("15");
                break;
            default:
                audittask.setShenpilb("10");
                break;
        }

        Integer assort = null;
        if (StringUtil.isBlank(right.getStr("ASSORT"))) {
            //大汉前置空也是即办件
            assort = 1;
        }
        else {
            if (FWConstants.STRONE.equals(right.getStr("ASSORT"))) {
                assort = 2;
            }
            else {
                if (FWConstants.STRTWO.equals(right.getStr("ASSORT"))) {
                    assort = 1;
                }
                else {
                    if (FWConstants.STRTHREE.equals(right.getStr("ASSORT"))) {
                        assort = 3;
                    }
                    else {
                        if (FWConstants.STRFOUR.equals(right.getStr("ASSORT"))) {
                            assort = 4;
                        }
                        else {
                            assort = 1;
                        }
                    }
                }
            }
        }
        //办件类型  1.即办 .....
        audittask.setType(assort);
        if(assort==1){
        	audittask.setJbjmode("1");
         }
        audittask.set("charge_lc", "1");
        audittask.setQlfullid(right.getStr("ITEM_CODE"));
        audittask.setFullid(right.getStr("ITEM_CODE"));
        // 大汉前置库没有相应字段，默认为个人法人
        audittask.setApplyertype("10,20");
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:");
        audittask.setPromise_day(Integer.parseInt(right.getStr("ACCEPT_TIME")));
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:");
        audittask.setAnticipate_day(Integer.parseInt(right.getStr("LAW_TIME")));
        audittask.setCharge_flag(Integer.parseInt(right.getStr("IS_CHARGE")));

        //-----暂不解析，后期解析----------
        audittask.setCharge_basis(right.getStr("Charge_Basis"));

        audittask.setLink_tel(right.getStr("WINDOW_PHONE"));
        audittask.setSupervise_tel(right.getStr("COMPLAIN_PHONE"));

        String law = right.getStr("LAWBASIS").replace("<br>", "");
        audittask.setBy_law(law);
        String APPLY_CONDITION = right.getStr("APPLY_CONDITION");
        if(StringUtil.isNotBlank(APPLY_CONDITION)){
            audittask.setAcceptcondition(APPLY_CONDITION);
        }else{
            audittask.setAcceptcondition("符合相关规定，提交相关申请材料");
        }
        //办理地址
        audittask.setTransact_addr(right.getStr("FWCK"));
        //audittask.setTransact_time(right.getStr("SL_Time"));//工作时间
        audittask.setTransact_time("周一至周五 上午9:00-12:00 下午13:00-17:00");
        // 外部流程图
        try {
            byte[] outimg = (byte[]) right.get("OUT_FLOW_IMG");
            String taskOutImgGuid = insertFrameAttach(outimg);
            audittask.setTaskoutimgguid(taskOutImgGuid);
        }
        catch (Exception e) {
            log.info("外部流程图同步失败 =====>"+e.getMessage());
        }

        WorkflowProcess workflowProcess = new WorkflowProcess();
        workflowProcess.setProcessName(right.getStr("NAME") + "【版本：" + versionNew + "】");
        workflowProcess.setProcessGuid(UUID.randomUUID().toString());
        WorkflowProcessVersion workflowProcessVersion = iWorkflowProcessService.addWorkflowProcess(workflowProcess,
                "同步业务服务", null);
        audittask.setPvguid(workflowProcessVersion.getProcessVersionGuid());
        audittask.setProcessguid(workflowProcessVersion.getProcessGuid());

        // 事项扩展信息表
        audittaskex.setOperateusername("旧同步服务");
        audittaskex.setOperatedate(new Date());
        audittaskex.set("innerno", right.getStr("innerno"));
        audittaskex.setRowguid(UUID.randomUUID().toString());
        audittaskex.setTaskguid(taskguid);
        //是否允许批量录入
        audittaskex.setIs_allowbatchregister(0);
        String webapplytype = right.getStr("WEBAPPLYDEPTH");
        //WEBAPPLYDEPTH 浪潮事项深度，事项公开（一级）在线预审（二级）在线申办（三级）全程网办（四级）
        //网上申报类型：不允许网上申报0；网上申报后预审1；网上申报后直接办理2
        //网上申报类型：不允许网上申报0；网上申报后预审1；网上申报后直接办理2
        if("2".equals(webapplytype) || "1".equals(webapplytype)){
            audittaskex.setWebapplytype(1);// 网上申报类型对应：网上申报后预审
        }else if("4".equals(webapplytype)){
            audittaskex.setWebapplytype(2);// 网上申报类型：网上申报后直接办理
        }else{
            audittaskex.setWebapplytype(0);// 网上申报类型：不允许网上申报
        }
        //同步事项情形信息
        audittaskex.set("CASE_SETTING_INFO", right.getStr("CASE_SETTING_INFO"));
        //audittaskex.setWebapplytype(Integer.parseInt(right.getStr("WEBAPPLYDEPTH")));// 网上申报类型、
        audittaskex.setSubjectnature(right.getStr("ORG_PROPERTY"));// 实施主体性质
        audittaskex.setTaskadduserdisplayname("同步服务");
        audittaskex.setQa_info(right.getStr("COMMON_QUESTION"));
        String rightClassqiyezt = right.getStr("RIGHTCLASS_QIYEZT");
        if (rightClassqiyezt.endsWith(FWConstants.SEMICOLON)) {
            //法人主题分类
            audittaskex.setTaskclass_forcompany(";" + right.getStr("RIGHTCLASS_QIYEZT"));
        }
        else {
            //法人主题分类
            audittaskex.setTaskclass_forcompany(";" + right.getStr("RIGHTCLASS_QIYEZT") + ";");
        }
        String rightClassGeRensx = right.getStr("RIGHTCLASS_GERENSX");
        if (rightClassGeRensx.endsWith(FWConstants.SEMICOLON)) {
            //个人主题分类
            audittaskex.setTaskclass_forpersion(";" + right.getStr("RIGHTCLASS_GERENSX"));
        }
        else {
            //个人主题分类
            audittaskex.setTaskclass_forpersion(";" + right.getStr("RIGHTCLASS_GERENSX") + ";");
        }
        audittaskex.setIs_simulation(0);
        audittaskex.setIsriskpoint(0);

        audittaskex.setIszijianxitong(0);
        //是否支持预约办理
        audittaskex.setReservationmanagement("1");
        audittaskex.setOnlinepayment("0");
        audittaskex.setIf_express("0");
        
        audittaskex.setSubjectnature("1");
        audittaskex.setUse_level("4");
        audittaskex.setNotify_ys("[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，预审已获通过，请带齐申报资料到中心窗口办理。");
        audittaskex.setNotify_nys(
                "[#=ApplyerName#]你好，您于[#=ApplyDate#]在网上申请的“[#=ProjectName#]”，由于[#=Reason#]，预审未获通过，特此通知。");
        audittaskex.setNotify_pz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批已获批准，请尽快到中心窗口办理。");
        audittaskex.setNotify_npz("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，审批未获批准，特此通知。");
        audittaskex.setNotify_sl("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，经审查符合受理条件，决定受理。");
        audittaskex.setNotify_nsl("[#=ApplyerName#]你好，您于[#=ApplyDate#]申请的“[#=ProjectName#]”，经审查不符合受理条件，决定不予受理。");
        int count = 0;
        count += commonDaoTo.insert(audittaskex);
        this.log.info("新增旧事项信息成功");
        count += commonDaoTo.insert(audittask);
        this.log.info("新增旧事项扩展信息成功");
        if(result!=null){
            commonDaoTo.insert(result);
            this.log.info("新增事项结果信息成功");
        }
        if("1".equals(IS_EDITAFTERIMPORT) && count==2){
            deleteTaskByTaskguid(task.getRowguid());
            log.info("原事项自动置为历史版本=======>"+task.getTaskname()+"："+task.getRowguid());
        }
        commonDaoTo.commitTransaction();
        

        //生成默认流程
        handleDefaultFlow(audittask, audittask.getAreacode());
        //同步材料
        if(StringUtil.isNotBlank(right.getStr("MATERIAL_INFO"))){
            syncMaterial(taskguid, right.getStr("MATERIAL_INFO"));

        }else{
            log.info("材料节点为空");
        }

        //同步常见问题
        syncTaskFaq(right.getStr("COMMON_QUESTION"), taskId);
        log.info("同步常见问题成功=======>" );
    }

    /**
     * 
     * 是否生成默认流程
     * 
     * @param ywGuid
     * @param auditTaskNew
     * @param areaCode
     */
    public void handleDefaultFlow(AuditTask auditTaskNew, String areaCode) {
        IAuditTaskRiskpoint iAuditTaskRiskpoint = ContainerFactory.getContainInfo()
                .getComponent(IAuditTaskRiskpoint.class);
        IWorkflowActivityService iWorkflowActivityService = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowActivityService.class);
        IWorkflowProcessVersionService iWorkflowProcessVersionService = ContainerFactory.getContainInfo()
                .getComponent(IWorkflowProcessVersionService.class);
        List<AuditTaskRiskpoint> auditTaskRiskpoints = new ArrayList<AuditTaskRiskpoint>();
        // 受理
        String firstActivityGuid = UUID.randomUUID().toString();
        // 审核
        String thirdActivityGuid = UUID.randomUUID().toString();
        // 办结
        String fifthActivityGuid = UUID.randomUUID().toString();
        for (int i = 0; i < FWConstants.COMMONTHREE; i++) {
            AuditTaskRiskpoint auditTaskRiskpoint = new AuditTaskRiskpoint();
            auditTaskRiskpoint.setRowguid(UUID.randomUUID().toString());
            if (i == 0) {
                auditTaskRiskpoint.setActivityname("受理");
                auditTaskRiskpoint.setActivityguid(firstActivityGuid);
                auditTaskRiskpoint.setOrdernum(50);
            }
            else if (i == 1) {
                auditTaskRiskpoint.setActivityname("审核");
                auditTaskRiskpoint.setActivityguid(thirdActivityGuid);
                auditTaskRiskpoint.setOrdernum(30);
            }
            else if (i == 2) {
                auditTaskRiskpoint.setOrdernum(10);
                auditTaskRiskpoint.setActivityname("办结");
                auditTaskRiskpoint.setActivityguid(fifthActivityGuid);
            }
            auditTaskRiskpoint.setAdddate(new Date());
            auditTaskRiskpoint.setTaskguid(auditTaskNew.getRowguid());
            auditTaskRiskpoint.setOperatedate(new Date());
            auditTaskRiskpoint.setOperateusername("同步岗位服务");
            auditTaskRiskpoint.setRiskpointid(UUID.randomUUID().toString());
            auditTaskRiskpoints.add(auditTaskRiskpoint);
            iAuditTaskRiskpoint.addAuditTaskRiskpoint(auditTaskRiskpoint);
            if (i == 0) {
                auditTaskRiskpoint.set("nextactivityguid", thirdActivityGuid);
            }
            else if (i == 1) {
                auditTaskRiskpoint.set("nextactivityguid", fifthActivityGuid);
            }
            else if (i == 2) {
                List<WorkflowActivity> activitylist = iWorkflowActivityService
                        .selectAllByProcessVersionGuid(auditTaskNew.getPvguid(), " and activityName = '结束'");
                auditTaskRiskpoint.set("nextactivityguid", activitylist.get(0).getActivityGuid());
            }
        }
        List<Activity> activitiList = SyncCommonUtil.sortActivities(auditTaskRiskpoints, auditTaskNew);
        WorkflowParameter9 workFlowParameter9 = new WorkflowParameter9();
        workFlowParameter9.setProcessVersionGuid(auditTaskNew.getPvguid());
        // 生成流程
        iWorkflowProcessVersionService.designProcess(auditTaskNew.getPvguid(), activitiList, true);
        // 流程配置修改
        SyncCommonUtil.handleWorkFlow(auditTaskNew.getPvguid(), areaCode);
    }

    /**
     * 
     *  [插入外部流程图]
     *  [功能详细描述]
     *  @param outimg
     *  @param taskOutImgGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String insertFrameAttach(byte[] outimg) {
        String attchpath = getAttachConfig("附件库");
        String taskOutImgGuid = UUID.randomUUID().toString();
        String guid = UUID.randomUUID().toString();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(date);
        String attPath = "/opt/epoint/BigFileUpLoadStorage/temp/" + dateNowStr + "/" + guid + "/";
        File file = new File(attPath);
        if (!file.exists()) {
            file.mkdirs();
            String filename = System.currentTimeMillis() + ".jpg";
            File photo = new File(file, filename);
            try {
                if (!photo.exists()) {
                    photo.createNewFile();
                }
                FileOutputStream fout = new FileOutputStream(photo);
                //将字节写入文件
                fout.write(outimg);
                fout.close();
            }
            catch (Exception e) {
                taskOutImgGuid = "";
                log.error("同步外部流程图异常=======>" + e.getMessage());
            }
            if (!"".equals(taskOutImgGuid)) {
                FrameAttachInfoNewService9 frameAttachInfoNewService = new FrameAttachInfoNewService9();
                FrameAttachInfo frameattachinfo = new FrameAttachInfo();
                frameattachinfo.setAttachGuid(guid);
                frameattachinfo.setAttachFileName(filename);
                frameattachinfo.setContentType(".jpg");
                frameattachinfo.setAttachLength((long) outimg.length);
                frameattachinfo.setCliengGuid(taskOutImgGuid);
                frameattachinfo.setStorageType("NasShareDirectory");
                frameattachinfo.setAttachConnectionStringName("nas");
                frameattachinfo.setFilePath(attPath);
                frameattachinfo.setUploadUserDisplayName("同步服务");
                frameattachinfo.setUploadDateTime(new Date());
                frameattachinfo.setAttachStorageGuid(guid);
                frameAttachInfoNewService.addFrameAttach(frameattachinfo);

                log.info("=======同步外部流程图成功=======");
            }
        }
        return taskOutImgGuid;
    }

    /**
     * API:获取附件存储路径
     */
    public String getAttachConfig(String configname) {
        String sql = "select ATTACH_CONNECTIONSTRING from frame_attachconfig where ATTACH_CONNECTIONSTRINGNAME=?1";
        String attachConnectionString = commonDaoTo.queryString(sql, configname);
        
        return attachConnectionString;
    }

    /**
     * 
     *  [同步材料]
     *  [功能详细描述]    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public void syncMaterial(String taskguid, String material) {
      
        //默认提交电子 10提交电子文件 20提交纸质文件 35电子或纸质  40电子和纸质
        String submittype = "10";
        //提交方式10必要/20普通
        int necessity = 10;
        try {
            //解析XML
            Document document = DocumentHelper.parseText(material);
            Element root = document.getRootElement();
            Element nodeList = root.element("MATERIALS");
            List<Element> s = nodeList.elements("MATERIAL");
            for (Element p : s) {
                Element apply = p.element("BUSINESS_TYPE");
                if ("Apply".equals(apply.getStringValue())) {
                    AuditTaskMaterial auditTaskMaterial = new AuditTaskMaterial();
                    auditTaskMaterial.setTaskguid(taskguid);
                    auditTaskMaterial.setOperateusername("同步服务");
                    auditTaskMaterial.setRowguid(UUID.randomUUID().toString());
                    
                    // 材料类型 表格：10 附件 20
                    auditTaskMaterial.setType(20);
                    // 1.来源渠道:(审批系统对应代码项:1-申请人自备;2-政府部门核发;3-其他)
                    auditTaskMaterial.setFile_source("1");
                    //当前节点的所有属性的list
                    List<Element> listAttr = p.elements();
                    //获取空白表格和示例表格：
                    String blankfilename = "";
                    String blankfileid = "";
                    String exfilename = "";
                    String exfileid = "";
                    //遍历当前节点的所有属性
                    for (Element attr : listAttr) {
                        //属性名称
                        String name = attr.getName();
                        String value = attr.getStringValue();
                        switch (name) {
                            case "NAME":
                                auditTaskMaterial.setMaterialname(value);
                                break;
                            case "PUBLISHER":
                                if ("纸质;".equals(value)) {
                                    submittype = "20";
                                }
                                else if ("电子版;".equals(value)) {
                                    submittype = "10";
                                }
                                else {
                                    submittype = "35";
                                }
                                auditTaskMaterial.setSubmittype(submittype);
                                break;
                            case "MUST":
                                if ("0".equals(value)) {
                                    necessity = 20;
                                }
                                auditTaskMaterial.setNecessity(necessity);
                                break;

                            case "CODE":
                                auditTaskMaterial.setMaterialid(value);
                                break;
                            case "FILENAME":
                                exfilename = value;
                                break;
                            case "FILEID":
                                exfileid = value;
                                break;
                            case "BLANK_NAME":
                                blankfilename = value;
                                break;
                            case "BLANK":
                                blankfileid = value;
                                break;
                            default:
                                break;
                        }
                    }
                    String anotherattachguid = UUID.randomUUID().toString();
                    //空白表格
                    if(StringUtil.isNotBlank(blankfilename) && StringUtil.isNotBlank(blankfileid)){
                        log.info("空白表格开始同步+++++++");
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        auditTaskMaterial.setTemplateattachguid(anotherattachguid);
                        if(blankfileid.contains(";")){
                            blankfileid=blankfileid.substring(0, blankfileid.length()-1);
                        }
                        getmaterial(blankfilename, blankfileid, anotherattachguid, date);
                    }
                    //示例表格
                    String anotherattachguid1 = UUID.randomUUID().toString();
                    if(StringUtil.isNotBlank(exfilename) && StringUtil.isNotBlank(exfileid)){
                        log.info("示例表格开始同步++++++");
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        auditTaskMaterial.setExampleattachguid(anotherattachguid1);
                        if(exfileid.contains(";")){
                            exfileid=exfileid.substring(0,exfileid.length()-1);
                        }
                        getmaterial(exfilename, exfileid, anotherattachguid1, date);

                    }   
                    commonDaoTo.insert(auditTaskMaterial);
                    commonDaoTo.commitTransaction();
                    this.log.info("同步材料成功");
                }else {
                    this.log.info("同步材料为空");
                }
            	
            }

        }
        catch (Exception e) {
            log.error("同步材料失败=======>");
            e.printStackTrace();
        }
    }

    /**
     * API:同步常见问题
     */
    public void syncTaskFaq(String question, String taskId) {
    		List<AuditTaskFaq> list=getTaskfaq(taskId);
    		if(list!=null && list.size()>0){
    			for(AuditTaskFaq faq : list){
    				int result=commonDaoTo.delete(faq);
    				if(result>0){
    					log.info("====>>>删除常见问题成功");
    				}else{
    					log.info("====>>>删除常见问题失败");
    				}
    			}
    		}
        if (!"".equals(question) && "无".equals(question)) {
            AuditTaskFaq auditTaskFaq = new AuditTaskFaq();
            auditTaskFaq.setRowguid(UUID.randomUUID().toString());
            auditTaskFaq.setOperateusername("同步服务");
            auditTaskFaq.setTaskid(taskId);
            auditTaskFaq.setQuestion(question);
            auditTaskFaq.setOrdernum(1);
            commonDaoTo.insert(auditTaskFaq);
            commonDaoTo.commitTransaction();
        }
    }

    public void updateSyncSign(String innerCode, String ORG_CODE) {
        String sql = "Update QLT_QLSX set QFSX='1',UPDATE_DATE=SYSDATE where inner_code=?1 and ORG_CODE=?2";
        commonDaoFrom.execute(sql, innerCode, ORG_CODE);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();
    }

    public void updateSyncSignbyoucode(String orgCode) {
        String sql = "Update QLT_QLSX set QFSX='1',UPDATE_DATE=SYSDATE where ORG_CODE=?";
        commonDaoFrom.execute(sql, orgCode);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();
    }

    /**
     * API:获取事项
     */
    public List<AuditTask> getAuditTask1(String innerCode) {
        String sql = "SELECT * FROM audit_task where new_item_code=?1 and Is_editafterimport=1 "
                + " AND task_id is not null and IFNULL(IS_HISTORY,0)= 0 ORDER BY Operatedate DESC ";
        List<AuditTask> auditTask = commonDaoTo.findList(sql, AuditTask.class, innerCode);
      
        return auditTask;
    }

    /**
     * API:获取待确认事项中事项
     */
    public AuditTask getAuditTask4(String innerCode) {
        String sql = "SELECT * FROM audit_task where new_item_code=?1 and Is_editafterimport=4"
                + " AND task_id is not null and IFNULL(IS_HISTORY,0)= 0 ORDER BY Operatedate DESC LIMIT 1";
        AuditTask auditTask = commonDaoTo.find(sql, AuditTask.class, innerCode);
       
        return auditTask;
    }

    /**
     * API:更新：获取原事项扩展信息
     */
    public AuditTaskExtension getTaskEx(String taskguid) {
        String sql = "SELECT * FROM AUDIT_TASK_EXTENSION where taskguid=?1";
        AuditTaskExtension hnAuditTaskExtension = commonDaoTo.find(sql, AuditTaskExtension.class, taskguid);
       
        return hnAuditTaskExtension;
    }
    
    public int deleteTaskByTaskguid(String taskguid) {
        String sql = "update AUDIT_TASK set OperateDate = NOW(), OperateUserName = '同步服务自动置为历史版本', IS_EDITAFTERIMPORT =5, IS_HISTORY=1 where rowguid =?";
       
        return commonDaoTo.execute(sql, taskguid);
    }

    public void updateQlsxSync(String orgcode, String string) {
        String sql = "Update JI_SXFF.QLT_QLSX_HB_NEW set QFSX='" + string + "',EX_DATE=SYSDATE where ORG_CODE='" + orgcode + "'";
        commonDaoFrom.execute(sql);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();
    }
    public void updateJnQlsxSync(String rowguid, String string) {
        String sql = "Update JI_SXFF.QLT_QLSX_HB_NEW set QFSX='" + string + "',EX_DATE=SYSDATE where ROWGUID='" + rowguid + "'";
        commonDaoFrom.execute(sql);
        commonDaoFrom.commitTransaction();
        commonDaoFrom.close();

    }
    public  void getmaterial( String MNAME, String URL, String uuid, String date) {
        try {
            String dirname = "/opt/epoint/BigFileUpLoadStorage/temp/" + date + "/" + uuid + "/";
            
            File dir = new File(dirname);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            MNAME=MNAME.replaceAll(";","");
            String mtype = MNAME.substring(MNAME.indexOf("."));
            String fileLocal =dirname + MNAME;
            
            int filelength = 0;
            try {
                downloadFile(URL, fileLocal);
                filelength = (int) (new File(fileLocal).length());
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sql = "INSERT INTO frame_attachinfo VALUES (null, '" + uuid + "', '" + MNAME + "', '" + mtype + "', "
                    + filelength + ", 'nas', null, '" + uuid + "', null, 'NasShareDirectory', '" + dirname
                    + "', '济宁同步表格服务', '济宁同步表格服务', now(), '" + uuid
                    + "', null, null, null, null, null, null, null, null, null, null, null)";
            commonDaoTo.execute(sql);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public  void downloadFile(String Urlcn, String fileLocal) throws Exception {
        // 内网地址59.207.104.12:8060 外网地址是http://www.hnzwfw.gov.cn
        // String URL =
        // "http://59.207.104.12:8060/fileserver/download.jsp?filePath=";
        // String URL =
        // "http://www.hnzwfw.gov.cn/fileserver/download.jsp?filePath=";
        try {
            String FileURL ="http://59.206.96.197:8080/WebDiskServerDemo/doc?doc_id="+Urlcn;
            
            URL url = new URL(FileURL);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(6000);
            urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
            }
            // 读文件流
            DataInputStream in = new DataInputStream(urlCon.getInputStream());
            DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(new String(fileLocal.getBytes(),"utf-8")));
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            log.info("--------------附件下载异常--------");
        }
    }

    public List<Record> getQLSXMaterialnew() {
        String sql = " select * from (select distinct INNER_CODE ,REGION_CODE,ORG_CODE from QLT_QLSX where "
                + " NVL(QFSX,'0')='1'  and dh_state=1 and MATERIAL_INFO is not null ORDER BY REGION_CODE) a where rownum <= 50";
        List<Record> list = commonDaoFrom.findList(sql, Record.class);
        //commonDaoFrom.close();
        return list;
    }

    public void syncTaskMaterial(Record record) throws DocumentException {
       
            String taskrowguid = getNowTaskRowguid(record.getStr("inner_code"));
            String meterialinfo = record.getStr("MATERIAL_INFO");
            Document document = DocumentHelper.parseText(meterialinfo);
            Element root = document.getRootElement();
            Element nodeList = root.element("MATERIALS");
            List<Element> s = nodeList.elements("MATERIAL");
            for (Element p : s) {
                
                //当前节点的所有属性的list
                List<Element> listAttr = p.elements();
                //获取空白表格和示例表格：
                String blankfilename = "";
                String blankfileid = "";
                String exfilename = "";
                String exfileid = "";
                String bussinesstype = "";
                //遍历当前节点的所有属性\
                String materialid = "";
                for (Element attr : listAttr) {
                    //属性名称
                    String name = attr.getName();
                    String value = attr.getStringValue();
                    switch (name) {
                        case "CODE":
                            materialid = value;
                            break;
                        case "FILENAME":
                            exfilename = value;
                            break;
                        case "FILEID":
                            exfileid = value;
                            break;
                        case "BLANK_NAME":
                            blankfilename = value;
                            break;
                        case "BLANK":
                            blankfileid = value;
                            break;
                        case "BUSINESS_TYPE":
                            bussinesstype = value;
                            break;
                        default:
                            break;
                    }
                }
                
                AuditTaskMaterial auditTaskMaterial = getTaskMaterial(taskrowguid,materialid);
                if(StringUtil.isNotBlank(auditTaskMaterial)){
                //空白表格
                if(StringUtil.isNotBlank(blankfilename) && StringUtil.isNotBlank(blankfileid) && "Apply".equals(bussinesstype)){
                    log.info("空白表格开始同步");
                    String anotherattachguid = UUID.randomUUID().toString();
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    auditTaskMaterial.setTemplateattachguid(anotherattachguid);
                    if(blankfileid.contains(";")){
                    	blankfileid=blankfileid.substring(0, blankfileid.length()-1);
                    }
                    getmaterial(blankfilename, blankfileid, anotherattachguid, date);
                    commonDaoTo.update(auditTaskMaterial);
                    
                }
                //示例表格
                if(StringUtil.isNotBlank(exfilename) && StringUtil.isNotBlank(exfileid) && "Apply".equals(bussinesstype)){
                    log.info("示例表格开始同步");
                    String anotherattachguid = UUID.randomUUID().toString();
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    auditTaskMaterial.setExampleattachguid(anotherattachguid);
                    if(exfileid.contains(";")){
                    	exfileid=exfileid.substring(0, exfileid.length()-1);
                    }
                    getmaterial(exfilename, exfileid, anotherattachguid, date);
                    commonDaoTo.update(auditTaskMaterial);

                  } 
                }
            }
        
    }

    private AuditTaskMaterial getTaskMaterial(String taskrowguid, String materialid) {
        String sql = "select * from audit_task_material where taskguid = '"+taskrowguid+"' "
                + " and materialid = '"+materialid+"'";
        AuditTaskMaterial info = commonDaoTo.find(sql, AuditTaskMaterial.class);
        return info;
    }

    private String getNowTaskRowguid(String str) {
        String sql="select rowguid from audit_task where item_id = '"+str+"' and is_enable = 1 and is_editafterimport=1 "
                + "and ifnull(is_history,'0')='0' ";
        String rowguid = commonDaoTo.find(sql,String.class);
        return rowguid;
    }
   private List<AuditTaskFaq> getTaskfaq(String taskid){
	   String sql="select * from audit_task_faq where taskid=?";
	   List<AuditTaskFaq> list=commonDaoTo.findList(sql, AuditTaskFaq.class, taskid);
	   return list;
   }

}
