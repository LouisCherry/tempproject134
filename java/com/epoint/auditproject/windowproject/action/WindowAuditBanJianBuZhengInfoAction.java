package com.epoint.auditproject.windowproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.domain.AuditLogisticsBasicinfo;
import com.epoint.basic.auditlogistics.auditlogisticsbasicinfo.inter.IAuditLogisticsBasicInfo;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectcharge.inter.IAuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.audittzxm.task.domain.AuditTzxmTask;
import com.epoint.basic.audittzxm.task.inter.IAuditTzxmTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

/**
 * 办件基本信息工作流页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-14 11:46:07]
 */
@RestController("windowauditbanjianbuzhenginfoaction")
@Scope("request")
public class WindowAuditBanJianBuZhengInfoAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4246072106352276268L;

    /**
     * 办件表实体对象
     */
    private AuditProject auditproject = null;
    /**
     * 事项表实体对象
     */
    private AuditTask audittask = null;
    /**
     * 办结说明
     */
    private AuditProjectUnusual auditProjectUnusual;
    // private AuditProjectUnusualService auditProjectUnusualService = new
    // AuditProjectUnusualService();
    /**
     * 事项拓展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 延期异常操作
     */
    private AuditProjectUnusual auditProjectUnusualZT;
    /**
     * 物流基本信息表实体对象
     */
    private AuditLogisticsBasicinfo auditLogistics = null;
    /**
     * 是否显示特殊办结说明
     */
    private boolean isshowunusualbanjienote = false;
    /**
     * 事项标识
     */
    private String taskguid;
    /**
     * 承诺时间
     */
    private String promise_day_gexinghua;
    /**
     * 申报方式
     */
    private String applyWay_gexinghua;
    /**
     * 证照类型
     */
    private String certtype_gexinghua;
    /**
     * 办件流水号
     */
    private String txtflowsn;
    /**
     * 办理时效
     */
    private String sparetime = "";
    /**
     * 办理用时
     */
    private String txtspendtime;
    /**
     * 办结类型
     */
    private String banjieresult;
    /**
     * 办结说明
     */
    private String lblUnusualBanjieNote = "";
    /**
     * title
     */
    private String tittle;
    /**
     * 审批结果
     */
    private String lbresult;

    /**
     * 物流信息标识
     */
    private String logisticsGuid = "";

    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectUnusual> unusualmodel;

    
    @Autowired
    private IAuditTaskResult auditResultService;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectSparetime auditProjectSpareTimeService;

    @Autowired
    private IAuditProjectUnusual auditProjectUnsualService;

    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private ICertAttachExternal certAttachService;

    @Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private IAuditLogisticsBasicInfo iAuditLogisticsBasicInfo;

    @Autowired
    private IAuditProjectCharge auditProjectChargeService;
    
    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;
    
    @Autowired
    private ICertInfoExternal certInfoExternalService;
    @Autowired
    private IAuditTzxmTask audittzzmtaskservice;
    
    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditProjectUnusual auditProjectUnusualservice;
    
    @Override
    public void pageLoad() {
        String fields = "rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,tasktype,spendtime,banjieresult,flowsn,sparetime,is_test,applydate,applyertype,certnum,certtype,address,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,contactcertnum,remark,banjiedate,If_express,legal,centerguid,is_charge,hebingshoulishuliang,Certrowguid,Certdocguid,acceptareacode,legalid,xmname,xmnum";
        String areacode = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditproject = auditProjectService.getAuditProjectByRowGuid(fields, getRequestParameter("projectguid"),
                areacode).getResult();
        //办件删除时提示
        if(auditproject == null){
            //办件可能是在其他辖区，需要查看所有的事项
            auditproject = auditProjectService.getAuditProjectByRowGuid(fields, getRequestParameter("projectguid"),
                    null).getResult();
            if(auditproject==null){
                addCallbackParam("msg", "办件已删除，待办无效");
                return;                
            }
        }
        applyWay_gexinghua = codeItemsService.getItemTextByCodeName("申请方式", auditproject.getApplyway().toString());
        if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_SFZ)) {
            certtype_gexinghua = "身份证";
        }
        else if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_GSYYZZ)) {
            certtype_gexinghua = "工商营业执照";
        }
        else if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_TYSHXYDM)) {
            certtype_gexinghua = "统一社会信用代码";
        }
        else if (auditproject.getCerttype().equals(ZwfwConstant.CERT_TYPE_ZZJGDMZ)) {
            certtype_gexinghua = "组织机构代码证";
        }
        if (applyWay_gexinghua.equals("")) {
            applyWay_gexinghua = "未知方式";
        }
        if (StringUtil.isNotBlank(getRequestParameter("ztguid"))){
            auditProjectUnusualZT = auditProjectUnsualService
                    .getAuditProjectUnusualByRowguid(getRequestParameter("ztguid")).getResult();
        }

        // 办理用时
        if (auditproject!=null &&  auditproject.getSpendtime() != null
                && !ZwfwConstant.ITEMTYPE_JBJ.equals(auditproject.getTasktype().toString())) {
            if (auditproject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL
                    && auditproject.getStatus() < ZwfwConstant.BANJIAN_STATUS_SPBTG) {
                // 正在办理, 非即办件
                AuditProjectSparetime auditProjectSparetime = auditProjectSpareTimeService
                        .getSparetimeByProjectGuid(auditproject.getRowguid()).getResult();
                if (auditProjectSparetime != null) {
                    txtspendtime = CommonUtil.getSpareTimes(auditProjectSparetime.getSpendminutes());
                }
            }
            else {
                txtspendtime = CommonUtil.getSpareTimes(auditproject.getSpendtime());
            }
        }
        if(auditproject!=null){
            banjieresult = getBanjieStatusKey(auditproject.getBanjieresult() == null ? 0 : auditproject.getBanjieresult());            
            if (auditproject.getFlowsn() == null) {
                txtflowsn = "（办件编号：）";
            }else {
                txtflowsn = "（办件编号：" + auditproject.getFlowsn() + "）";
            }
        }
        String If_express = "";
        if (auditproject != null) {
            taskguid = auditproject.getTaskguid();
            audittask = auditTaskService.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
            AuditTzxmTask tzxmtask = audittzzmtaskservice.getTzxmTaskByTaskid(audittask.getTask_id()).getResult();
            if(tzxmtask!=null&&StringUtil.isNotBlank(auditproject.getXmnum())){
                addCallbackParam("istzxm", "1");
            }
            String area = "";
            if (auditproject != null && StringUtil.isNotBlank(auditproject.getAcceptareacode())) {
                area = auditproject.getAcceptareacode();
            } else {
                area = auditproject.getAreacode();
            }
            AuditTaskDelegate delegate = auditTaskDelegateService
                    .findByTaskIDAndAreacode(audittask.getTask_id(), area).getResult();
            if (delegate != null) {
                if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                        && delegate.getUsecurrentinfo().equals(ZwfwConstant.CONSTANT_STR_ONE)) {
                    if (StringUtil.isNotBlank(delegate.getPromise_day())) {
log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
                        audittask.setPromise_day(delegate.getPromise_day());
                            log.info("update promise_day-task:"+audittask.getItem_id()+","+audittask.getPromise_day()+",user:"+userSession.getUserGuid());
                    }
                    if (StringUtil.isNotBlank(delegate.getAcceptcondition())) {
                        audittask.setAcceptcondition(delegate.getAcceptcondition());
                    }
                }
            }
            if (auditproject != null){
                auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditproject.getTaskguid(), false)
                        .getResult();                
                if (auditproject.getTasktype() == 1) {
                    promise_day_gexinghua = "0工作日";
                }
                else {
                    promise_day_gexinghua = audittask.getPromise_day() + "个工作日";
                }
                If_express = auditproject.getIf_express();
            }
        }
        // 获取物流信息
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(If_express)) {
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if("1".equals(isJSTY)){
                Map<String, String> conditionMap = new HashMap<String, String>(16);
                conditionMap.put("projectguid = ", auditproject.getRowguid());
                List<AuditLogisticsBasicinfo> list = iAuditLogisticsBasicInfo
                        .getAuditLogisticsBasicinfoList(conditionMap, null, null).getResult();
                if (list != null && list.size() > 0) {
                    for (AuditLogisticsBasicinfo logisticsBasicinfo : list) {
                        if (!"1".equals(logisticsBasicinfo.getNet_type())){
                            auditLogistics = logisticsBasicinfo;
                        }

                    }
                }
            }else{
                auditLogistics = iAuditLogisticsBasicInfo.getAuditLogisticsBasicinfoListByFlowsn(auditproject.getRowguid())
                        .getResult();
            }
            
            logisticsGuid = auditLogistics.getRowguid();
        }

        // 获取事项审批结果信息
        AuditTaskResult auditResult = auditResultService.getAuditResultByTaskGuid(taskguid, false).getResult();
        lbresult = auditResult == null ? "无"
                : codeItemsService.getItemTextByCodeName("审批结果类型", String.valueOf(auditResult.getResulttype()));
        // 异常操作，需要显示办结说明
        if (auditproject!=null && auditproject.getStatus() > ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
            isshowunusualbanjienote = true;
            auditProjectUnusual = auditProjectUnsualService.getProjectUnusualByProjectGuidAndType(
                    auditproject.getRowguid(), auditproject.getBanjieresult() == null ? "0": auditproject.getBanjieresult().toString()).getResult();
            if (auditProjectUnusual != null) {
                lblUnusualBanjieNote = auditProjectUnusual.getNote();
            }
            else {
                lblUnusualBanjieNote = "没有原因说明";

            }
        }

        // 根据系统参数判断是否显示[测试办件]
        String asneedbjtest = handleConfigService.getFrameConfig("AS_NEED_BJ_TEST", audittask.getAreacode())
                .getResult();
        if(auditproject!=null){
            addCallbackParam("pviGuid", auditproject.getPviguid());
        }
        addCallbackParam("tittle", getSparetittle());
        addCallbackParam("tasktype", audittask.getType());
        addCallbackParam("asneedbjtest", asneedbjtest);
        addCallbackParam("docs", getDocJSONArray());
        addCallbackParam("applyertype", auditproject.getApplyertype());
        addCallbackParam("If_express", auditproject.getIf_express());
        addCallbackParam("logisticsGuid", logisticsGuid);
        addCallbackParam("showfee", auditproject.getIs_charge());
        addCallbackParam("bjstatus", auditproject.getStatus());
        //是否自建系统
        addCallbackParam("isZiJianXiTong",auditTaskExtension.getIszijianxitong());
        //合并受理
        addCallbackParam("ifhebing", auditTaskExtension.getIs_allowbatchregister());
        if (auditProjectUnusualZT != null) {
            addCallbackParam("delayworkday", auditProjectUnusualZT.getDelayworkday());
            // 该待办已经处理
            if ((auditProjectUnusualZT.getAuditresult() == 1
                    && auditProjectUnusualZT.getOperatetype() == Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_YQ))
                    || auditProjectUnusualZT.getOperatetype() == Integer
                            .parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_YQTH)) {
                addCallbackParam("hasdone", "1");
            }
        }
        
        if(StringUtil.isNotBlank(auditproject.getCertdocguid())){
            addCallbackParam("certdocguid", auditproject.getCertdocguid());
        }
    }

    private JSONArray getDocJSONArray() {
        String notOpenDocs = auditTaskExtension.getIs_notopendoc();
        String asdocword = handleConfigService.getFrameConfig("AS_DOC_WORD", auditproject.getCenterguid()).getResult();
        boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;
        List<Map<String, String>> listDocs = handleProjectService.initProjectDocList(auditproject.getRowguid(),
                taskguid, auditproject.getCenterguid(), audittask.getType().toString(), notOpenDocs, isword)
                .getResult();
        JSONArray jsonArray = new JSONArray();
        String address = "";
        if (listDocs != null && listDocs.size() > 0) {
            for (Map<String, String> doc : listDocs) {
                if (doc.size() > 0) {
                    //办结且未打印的文书仍显示以供补打
                    JSONObject jsonObject = new JSONObject();
                    String param = "&projectguid=" + auditproject.getRowguid() + "&ProcessVersionInstanceGuid="
                            + auditproject.getPviguid();
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(doc.get("isPrint"))) {
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(doc.get("isWord"))) {
                            address = getRequestContextPath()
                                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + doc.get("docUrl");
                        }
                        else {
                            address = doc.get("docUrl") + param;
                            address += "&isPrint=1";
                        }
                    }
                    else {
                        address = doc.get("docUrl") + param;
                    }
                    jsonObject.put("url", address);
                    jsonObject.put("docname", doc.get("docName"));
                    jsonObject.put("isprint", doc.get("isPrint"));
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;

    }

    @SuppressWarnings("unused")
    public String modelChargeHistory() {
        String fields = " rowguid,biguid,subappguid,taskguid,taskcaseguid,status,areacode ";
        String projectGuid = getRequestParameter("projectguid");
        List<AuditProjectCharge> auditProjectCharges = auditProjectChargeService.selectChargeByProjectGuid(projectGuid)
                .getResult();
        String isPostback = getRequestParameter("isPostback");
        List<JSONObject> chargeList = new ArrayList<JSONObject>();
        JSONObject jsonChargeList = new JSONObject();
        if (auditProjectCharges == null) {

        }
        else {
        	String areacode;
            if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
            	areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
            }else{
            	areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            
            for (AuditProjectCharge auditProjectCharge : auditProjectCharges) {
                JSONObject jsoncharge = new JSONObject();
                AuditProject auditProject = auditProjectService
                        .getAuditProjectByRowGuid(projectGuid, areacode).getResult();
                String chargeNo = "";
                //办件删除时提示
                if(auditProject == null){
                    //办件可能是在其他辖区，需要查看所有的事项
                    auditProject = auditProjectService.getAuditProjectByRowGuid(fields, getRequestParameter("projectguid"),
                            null).getResult();
                    if(auditProject==null){
                        addCallbackParam("msg", "办件已删除，待办无效");
                        return "";                
                    }
                }
                if (auditProjectCharge.getChargeno() != null) {
                    int intChargeNo = auditProjectCharge.getChargeno();
                    int i_m = intChargeNo;
                    String str_m = String.valueOf(i_m);
                    String str = "00000000";
                    str_m = str.substring(0, 8 - str_m.length()) + str_m;
                    chargeNo = str_m;
                }
                Date currentTime = new Date();                
                jsoncharge.put("chargeno", chargeNo);
                if (!StringUtil.isBlank(auditProjectCharge.getCheckdate())) {
                    jsoncharge.put("checkdate", EpointDateUtil.convertDate2String(auditProjectCharge.getCheckdate(), "yyyy-MM-dd HH:mm:ss"));
                }
                if (!StringUtil.isBlank(auditProjectCharge.getFeedate())) {
                    
                    jsoncharge.put("feedate", EpointDateUtil.convertDate2String(auditProjectCharge.getFeedate(), "yyyy-MM-dd HH:mm:ss"));
                }
                jsoncharge.put("sqdate", auditProjectCharge.getCheckdate());
                String projectName = "<a href='#' onclick=\"getDetailCharge('" + auditProjectCharge.getRowguid() + "','"
                        + auditProject.getTaskguid() + "')\" > " + auditProject.getProjectname() + "</a>";
                jsoncharge.put("projectname", projectName);
                jsoncharge.put("iscancel", auditProjectCharge.getIs_cancel());
                jsoncharge.put("cancelreason", auditProjectCharge.getCancelreason());
                if (!StringUtil.isBlank(auditProjectCharge.getIs_cancel())) {
                    int cancel = auditProjectCharge.getIs_cancel();
                    if (cancel != 1) {
                        jsoncharge.put("tzsprojectguid", projectGuid);
                        int ISCHARGE = auditProjectCharge.getIscharged();
                        if (ISCHARGE == 1) {
                            jsoncharge.put("sqprojectguid", projectGuid);
                        }
                    }
                }
                chargeList.add(jsoncharge);
            }
            jsonChargeList.put("data", chargeList);
            jsonChargeList.put("total", auditProjectCharges.size());
        }
        return jsonChargeList.toString();
    }

    /**
     * 决定剩余时间的标题和显示时间内容
     * 
     * @return String
     */
    public String getSparetittle() {
        if (!ZwfwConstant.ITEMTYPE_JBJ.equals(audittask.getType().toString())) {
            if (auditproject.getStatus() < ZwfwConstant.BANJIAN_STATUS_YSL) {
                // 未受理
                tittle = "剩余办理时间";
            }
            else if (auditproject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL
                    && auditproject.getStatus() < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                // 正在办理, 非即办件
                AuditProjectSparetime auditProjectSparetime = auditProjectSpareTimeService
                        .getSparetimeByProjectGuid(auditproject.getRowguid()).getResult();
                if (auditProjectSparetime != null) {
                    tittle = auditProjectSparetime.getSpareminutes() >= 0 ? "办理剩余用时" : "办理超过用时";
                    sparetime = CommonUtil.getSpareTimes(auditProjectSparetime.getSpareminutes());
                }
            }
            else {
                tittle = "办理时效";
                if (StringUtil.isNotBlank(auditproject.getSparetime())) {
                    if (auditproject.getSparetime() > 0) {
                        sparetime = "提前" + CommonUtil.getSpareTimes(auditproject.getSparetime());
                    }
                    else if (auditproject.getSparetime() == 0) {
                        sparetime = "按时办结";
                    }
                    else {
                        sparetime = "超时" + CommonUtil.getSpareTimes(auditproject.getSparetime());
                    }
                }
                if (auditproject.getStatus() == ZwfwConstant.BANJIAN_STATUS_CXSQ
                        || auditproject.getStatus() == ZwfwConstant.BANJIAN_STATUS_BYSL
                        || auditproject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
                    sparetime = " --";
                }
            }
        }
        return tittle;
    }

    /**
     * 办结状态KEY
     * 
     * @param status
     *            办结状态
     * @return String
     */
    public static String getBanjieStatusKey(int status) {
        String stausKey = "未办结";
        if (StringUtil.isNotBlank(status)) {
            if (status == (ZwfwConstant.BANJIE_TYPE_BYSL)) {
                stausKey = "不予受理";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_ZYXK)) {
                stausKey = "准予许可";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_BYXK)) {
                stausKey = "不予许可";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_CXSQ)) {
                stausKey = "撤销申请";
            }
            if (status == (ZwfwConstant.BANJIE_TYPE_YCZZ)) {
                stausKey = "异常终止";
            }
        }
        return stausKey;
    }

    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    CertInfo cert = certInfoExternalService.getCertInfoByRowguid(auditproject.getCertrowguid());
                    List<JSONObject> list = new ArrayList<>();
                    List<JSONObject> list2 = new ArrayList<>();
                    if(cert != null){
                        if(StringUtil.isNotBlank(cert.getCertcliengguid())){
                            list = certAttachService
                                   .getAttachList(cert.getCertcliengguid(), auditproject.getAreacode());
                       }
                       /*if(StringUtil.isNotBlank(cert.getCopycertcliengguid())){
                           list2 = certAttachService
                                   .getAttachList(cert.getCopycertcliengguid(), auditproject.getAreacode());
                       }*/
                       if (sortOrder.equals("desc")) {
                           list.sort((b, a) -> EpointDateUtil.compareDateOnDay(a.getDate("uploaddatetime"),
                                   b.getDate("uploaddatetime")));
                       }
                       else {
                           list.sort((a, b) -> EpointDateUtil.compareDateOnDay(a.getDate("uploaddatetime"),
                                   b.getDate("uploaddatetime")));
                       }
                    }else{
                        List<FrameAttachInfo> listattach =  attachservice.getAttachInfoListByGuid(auditproject.getRowguid());
                        //对于存储到本地附件库的结果，返回一个值供前台切换路径
                        for (FrameAttachInfo attachInfo : listattach) {
                            attachInfo.set("source","0");
                        }
                        this.setRowCount(listattach.size());
                        return listattach;
                    }
                    List<FrameAttachInfo> frameInfos = new ArrayList<>();
                    int count = 0;
                    if (list != null && list.size() > 0) {
                        for(JSONObject json : list){
                            FrameAttachInfo info = new FrameAttachInfo();
                            info.setAttachFileName(json.getString("attachname"));
                            info.setUploadDateTime(json.getDate("uploaddatetime"));
                            info.setAttachGuid(json.getString("attachguid"));
                            frameInfos.add(info);
                        }
                    }
                    /*if (list2 != null && list2.size() > 0) {
                        for(JSONObject json : list2){
                            FrameAttachInfo info = new FrameAttachInfo();
                            info.setAttachFileName(json.getString("attachname"));
                            info.setUploadDateTime(json.getDate("uploaddatetime"));
                            info.setAttachGuid(json.getString("attachguid"));
                            frameInfos.add(info);
                        }
                    }*/
                    count = frameInfos.size();
                    this.setRowCount(count);
                    return frameInfos;
                }
            };
        }
        return model;
    }
    
    public DataGridModel<AuditProjectUnusual> getDataGrid3Data() {
        // 获得表格对象
        if (unusualmodel == null) {
            unusualmodel = new DataGridModel<AuditProjectUnusual>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<AuditProjectUnusual> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("projectguid", auditproject.getRowguid());
                    sortField="operatedate";
                    sortOrder="asc";
                    List<AuditProjectUnusual> list = auditProjectUnusualservice.getAuditProjectUnusualListByPage(sql.getMap(), 0, 0, sortField, sortOrder).getResult().getList();
                    this.setRowCount(list.size());
                    return list;
                }
            };
        }
        return unusualmodel;
    }
    
    
    public void uploadOfficalDoc(String attachGuid){
        String path = certAttachService.getAttachScan(attachGuid, auditproject.getAreacode());
        if(StringUtil.isNotBlank(path)){
            path = path.split("furl=")[1];
        }
        addCallbackParam("path", path);
    }

    public void editOfficalDoc(String attachGuid , String source) {
        FrameAttachInfo attachinfo = null;
        if(!"0".equals(source)){
            attachinfo = certAttachService.getAttachByAttachGuid(attachGuid, auditproject.getAreacode());
        }else{
            attachinfo = attachservice.getAttachInfoDetail(attachGuid);
        }
        String msg = "";
        if(StringUtil.isNotBlank(auditproject.getCertdocguid())){
            addCallbackParam("attachguid", auditproject.getCertdocguid());
            msg = "ok";
            if(attachinfo!=null){                
                addCallbackParam("filename", attachinfo.getAttachFileName());        
            }
        }
        addCallbackParam("msg", msg);
    }
    
    public void materialbuzheng(){
        String msg = "材料补交完成！";
        auditproject.set("epidemic", ZwfwConstant.CONSTANT_STR_TWO);
        auditProjectService.updateProject(auditproject);
        addCallbackParam("msg", msg);
    }

    public AuditProject getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(AuditProject auditproject) {
        this.auditproject = auditproject;
    }

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

    public AuditTaskExtension getAuditTaskExtension() {
        return auditTaskExtension;
    }

    public void setAuditTaskExtension(AuditTaskExtension auditTaskExtension) {
        this.auditTaskExtension = auditTaskExtension;
    }

    public AuditProjectUnusual getAuditProjectUnusualZT() {
        return auditProjectUnusualZT;
    }

    public void setAuditProjectUnusualZT(AuditProjectUnusual auditProjectUnusualZT) {
        this.auditProjectUnusualZT = auditProjectUnusualZT;
    }

    public String getTaskguid() {
        return taskguid;
    }

    public void setTaskguid(String taskguid) {
        this.taskguid = taskguid;
    }

    public String getTxtflowsn() {
        return txtflowsn;
    }

    public void setTxtflowsn(String txtflowsn) {
        this.txtflowsn = txtflowsn;
    }

    public String getLbresult() {
        return lbresult;
    }

    public void setLbresult(String lbresult) {
        this.lbresult = lbresult;
    }

    public String getPromise_day_gexinghua() {
        return promise_day_gexinghua;
    }

    public void setPromise_day_gexinghua(String promise_day_gexinghua) {
        this.promise_day_gexinghua = promise_day_gexinghua;
    }

    public String getApplyWay_gexinghua() {
        return applyWay_gexinghua;
    }

    public void setCerttype_gexinghua(String certtype_gexinghua) {
        this.certtype_gexinghua = certtype_gexinghua;
    }

    public String getCerttype_gexinghua() {
        return certtype_gexinghua;
    }

    public void setApplyWay_gexinghua(String applyWay_gexinghua) {
        this.applyWay_gexinghua = applyWay_gexinghua;
    }

    public String getSparetime() {
        return sparetime;
    }

    public void setSparetime(String sparetime) {
        this.sparetime = sparetime;
    }

    public String getTxtspendtime() {
        return txtspendtime;
    }

    public void setTxtspendtime(String txtspendtime) {
        this.txtspendtime = txtspendtime;
    }

    public String getLblUnusualBanjieNote() {
        return lblUnusualBanjieNote;
    }

    public void setLblUnusualBanjieNote(String lblUnusualBanjieNote) {
        this.lblUnusualBanjieNote = lblUnusualBanjieNote;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getBanjieresult() {
        return banjieresult;
    }

    public void setBanjieresult(String banjieresult) {
        this.banjieresult = banjieresult;
    }

    public boolean isIsshowunusualbanjienote() {
        return isshowunusualbanjienote;
    }

    public void setIsshowunusualbanjienote(boolean isshowunusualbanjienote) {
        this.isshowunusualbanjienote = isshowunusualbanjienote;
    }

    public AuditLogisticsBasicinfo getAuditLogistics() {
        return auditLogistics;
    }

    public void setAuditLogistics(AuditLogisticsBasicinfo auditLogistics) {
        this.auditLogistics = auditLogistics;
    }

    public String getLogisticsGuid() {
        return logisticsGuid;
    }

    public void setLogisticsGuid(String logisticsGuid) {
        this.logisticsGuid = logisticsGuid;
    }
}
