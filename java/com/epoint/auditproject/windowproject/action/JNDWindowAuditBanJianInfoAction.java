package com.epoint.auditproject.windowproject.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.monitorsupervise.api.IJNAuditProjectMonitorService;
import com.epoint.auditproject.monitorsupervise.api.auditprojectold;
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
import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.basic.auditresource.auditrssharemaerial.inter.IAuditRsShareMetadata;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonutils.ValidateUtil;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.ces.jnbuildpart.api.IJnBuildPartService;
import com.epoint.ces.jnbuildpart.api.entity.JnBuildPart;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handlersmaterial.inter.IHandleRSMaterial;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.cpy.jnaicpy.api.IJnAiCpyService;
import com.epoint.cpy.jnaicpy.api.entity.JnAiCpy;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
import com.epoint.jnzwfw.sdwaithandle.api.IAuditProjectSamecityService;
import com.epoint.jnzwfw.sdwaithandle.api.entity.AuditProjectSamecity;
import com.epoint.lsyc.common.util.LsZwfwBjsbConstant;
import com.epoint.zhenggai.api.ZhenggaiService;

/**
 * 办件基本信息工作流页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-14 11:46:07]
 */
@RestController("jndwindowauditbanjianinfoaction")
@Scope("request")
public class JNDWindowAuditBanJianInfoAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -4246072106352276268L;

    /**
     * 办件查询字段
     */
    private String fields = " xiangmubm,xiangmuname,AREAALL,AREABUILD,INVESTMENT,PROJECTCONTENT,PROJECTALLOWEDNO,ACCEPTUSERGUID,rowguid,taskguid,applyername,applyeruserguid,areacode,status,centerguid,applyway,taskcaseguid,pviguid,projectname,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,flowsn,tasktype,ouname,ouguid,hebingshoulishuliang,remark,windowguid,biguid,banjiedate,is_pause,is_test,if_express,spendtime,subappguid,businessguid,applydate,currentareacode,handleareacode,acceptareacode,legalid,task_id,IS_SAMECITY,dataObj_baseinfo,dataObj_baseinfo_other";

    
    /**
     * 办件表实体对象
     */
    private auditprojectold auditproject = null;
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
     * 办件标识
     */
    private String projectguid;
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
    private DataGridModel<Record> workmodel;
    
    private Record otherInfo = new Record();
    
    public Record getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Record otherInfo) {
        this.otherInfo = otherInfo;
    }
    
    
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectFormSgxkzDanti> dantimodel;

    private List<SelectItem> suozaishengfenModel = null;
    
    private List<SelectItem> suozaichengshiModel = null;
    
    private List<SelectItem> suozaiquxianModel = null;
    
    private List<SelectItem> jianshexingzhiModel = null;
    
    private List<SelectItem> xiangmufenleiModel = null;
    
    private AuditProjectFormSgxkz auditProjectFormSgxkz;
    
    /**
     * 审批结果
     */
    private AuditTaskResult auditResult = null;

    @Autowired
    private IAuditTaskResult auditResultService;

    @Autowired
    private IJNAuditProjectMonitorService auditProjectService;

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
    private ZhenggaiService zhenggaiImpl;
    
    @Autowired
    private IAttachService attachService;
    
    @Autowired
    private IHandleRSMaterial handleRSMaterialService;
    
    @Autowired
    private IHandleSPIMaterial handleSpiMaterialService;
    
    @Autowired
    private IAuditSpShareMaterialRelation relationService;
    
    @Autowired
    private IAuditProjectFormSgxkzService iAuditProjectFormSgxkzService;
    
    @Autowired
    private IAuditProjectFormSgxkzDantiService iAuditProjectFormSgxkzDantiService;
    
    @Autowired
    private IJnBuildPartService iJnBuildPartService;
    
    @Autowired
    private ICodeItemsService codeItemService;
    
    @Autowired
    private IJnAiCpyService iJnAiCpyService;
    
    @Autowired
    private IAuditRsShareMetadata iAuditRsShareMetadata;
    
    /**
     * 同城通办信息表api
     */
    @Autowired
    private IAuditProjectSamecityService iAuditProjectSamecityService;
    /**
     * 同城通办实体
     */
    private AuditProjectSamecity auditProjectSamecity;

    @Override
    public void pageLoad() {
        String areacode = "";
        projectguid = getRequestParameter("projectguid");
        String tctbarea = getRequestParameter("areacode");
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else if(StringUtil.isNotBlank(tctbarea)){
            areacode = tctbarea;
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditproject = auditProjectService.getAuditProjectByRowGuid(getRequestParameter("projectguid"));
        //办件删除时提示
        if(auditproject == null){
            addCallbackParam("msg", "办件已删除，待办无效");
            return;
        }
        
        if (auditproject.getProjectname().contains("超限事项许可证") && StringUtil.isNotBlank(auditproject.getStr("dataObj_baseinfo"))) {
        	 String cataloguid = handleConfigService.getFrameConfig("AS_CERT_CXCL", auditproject.getCenterguid()).getResult();
        	 addCallbackParam("cataloguid", cataloguid);
        	 addCallbackParam("taskguid", auditproject.getTaskguid());
        	 addCallbackParam("cxystxz", "1");
        }
        auditProjectSamecity = iAuditProjectSamecityService
                .getProjectSamecityByProjectguid(getRequestParameter("projectguid"));
        if(auditProjectSamecity != null){
            addCallbackParam("is_samecity", "1");
        }else{
            auditProjectSamecity = new AuditProjectSamecity();
        }
        auditProjectFormSgxkz = iAuditProjectFormSgxkzService.getRecordBy(projectguid);
        if(auditProjectFormSgxkz != null){
            addCallbackParam("hassgxk", "1");
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
        if (auditproject.getSpendtime() != null
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

        banjieresult = getBanjieStatusKey(auditproject.getBanjieresult() == null ? 0 : auditproject.getBanjieresult());

        if (auditproject.getFlowsn() == null) {
            txtflowsn = "（办件编号：）";
        }
        else {
            txtflowsn = "（办件编号：" + auditproject.getFlowsn() + "）";
        }
        if (auditproject != null) {
            taskguid = auditproject.getTaskguid();
            //个性化添加审批结果
            auditResult = auditResultService.getAuditResultByTaskGuid(taskguid, true).getResult();
            audittask = auditTaskService.getAuditTaskByGuid(auditproject.getTaskguid(), false).getResult();
            String area = "";
            if (auditproject != null && StringUtil.isNotBlank(auditproject.getAcceptareacode())) {
                area = auditproject.getAcceptareacode();
            } else {
                area = ZwfwUserSession.getInstance().getAreaCode();
            }
            AuditTaskDelegate delegate = auditTaskDelegateService
                    .findByTaskIDAndAreacode(audittask.getTask_id(), area).getResult();
            if (delegate != null) {
                if (StringUtil.isNotBlank(delegate.getUsecurrentinfo())
                        && delegate.getUsecurrentinfo().equals("是")) {
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
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditproject.getTaskguid(), false)
                    .getResult();
            if (auditproject.getTasktype() == 1) {
                promise_day_gexinghua = "0工作日";
            }
            else {
                promise_day_gexinghua = audittask.getPromise_day() + "个工作日";
            }
        }

        // 获取物流信息
        String If_express = auditproject.getIf_express();
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
        if (auditproject.getStatus() > ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
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

        addCallbackParam("pviGuid", auditproject.getPviguid());
        addCallbackParam("tittle", getSparetittle());
        addCallbackParam("tasktype", audittask.getType());
        addCallbackParam("asneedbjtest", asneedbjtest);
        addCallbackParam("docs", getDocJSONArray());
        addCallbackParam("applyertype", auditproject.getApplyertype());
        addCallbackParam("If_express", auditproject.getIf_express());
        addCallbackParam("logisticsGuid", logisticsGuid);
        addCallbackParam("showfee", auditproject.getIs_charge());
        addCallbackParam("lcflag", auditproject.get("ISSYNACWAVE"));

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
        String certnum = auditproject.getCertnum();
        if (StringUtil.isNotBlank(certnum) && auditproject.getProjectname().contains("建筑业企业资质简单变更")) {
            List<JnBuildPart> list = iJnBuildPartService.getJnBuildPartByCode(certnum);
            if (list != null && list.size() > 0) {
                JnBuildPart gspart = list.get(0);
                JnBuildPart qyqpart = list.get(1);
                if (gspart != null && qyqpart != null) {
                    addCallbackParam("hasqyzzk","1");
                    addCallbackParam("gsitemname",gspart.getStr("itemname"));
                    addCallbackParam("gsaddress",gspart.getStr("address"));
                    addCallbackParam("gslegal",gspart.getStr("legal"));
                    addCallbackParam("gszczb",gspart.getRegister());
                    addCallbackParam("gsjjxz",gspart.getEcon());
                    addCallbackParam("qyitemname",qyqpart.getStr("itemname"));
                    addCallbackParam("qyaddress",qyqpart.getStr("address"));
                    addCallbackParam("qylegal",qyqpart.getStr("legal"));
                    addCallbackParam("qyzczb",qyqpart.getRegister());
                    addCallbackParam("qyjjxz",qyqpart.getEcon());
                }
            }
        } else if ((StringUtil.isNotBlank(certnum)) && (auditproject.getProjectname().contains("变更成品油零售经营批准证书企业名称或法定代表人"))) {
            List<JnAiCpy> list = iJnAiCpyService.getJnAiCpyByCode(certnum);
            if ((list != null) && (list.size() > 0)) {
              JnAiCpy gspart = list.get(0);
              JnAiCpy qyqpart = list.get(1);
              if ((gspart != null) && (qyqpart != null)) {
                addCallbackParam("hasqyzzk", "2");
                addCallbackParam("gsitemname", gspart.getName());
                addCallbackParam("gslegal", gspart.getStr("legal"));
                addCallbackParam("qyitemname", qyqpart.getName());
                addCallbackParam("qylegal", qyqpart.getStr("legal"));
              }
            }
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
                    //如果已经办结了，那么没有打印的文书就不用显示出来了
                    if (!(auditproject.getStatus() >= 90
                            && ZwfwConstant.CONSTANT_STR_ZERO.equals(doc.get("isPrint")))) {
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
                auditprojectold auditProject = auditProjectService
                        .getAuditProjectByRowGuid(projectGuid);
                String chargeNo = "";
                if (auditProjectCharge.getChargeno() != null) {
                    int intChargeNo = auditProjectCharge.getChargeno();
                    int i_m = intChargeNo;
                    String str_m = String.valueOf(i_m);
                    String str = "00000000";
                    str_m = str.substring(0, 8 - str_m.length()) + str_m;
                    chargeNo = str_m;
                }
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                jsoncharge.put("chargeno", chargeNo);
                if (!StringUtil.isBlank(auditProjectCharge.getCheckdate())) {
                    jsoncharge.put("checkdate", formatter.format(auditProjectCharge.getCheckdate()));
                }
                if (!StringUtil.isBlank(auditProjectCharge.getFeedate())) {
                    jsoncharge.put("feedate", formatter.format(auditProjectCharge.getFeedate()));
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
                       if(StringUtil.isNotBlank(cert.getCopycertcliengguid())){
                           list2 = certAttachService
                                   .getAttachList(cert.getCopycertcliengguid(), auditproject.getAreacode());
                       }
                       if (sortOrder.equals("desc")) {
                           list.sort((b, a) -> EpointDateUtil.compareDateOnDay(a.getDate("uploaddatetime"),
                                   b.getDate("uploaddatetime")));
                       }
                       else {
                           list.sort((a, b) -> EpointDateUtil.compareDateOnDay(a.getDate("uploaddatetime"),
                                   b.getDate("uploaddatetime")));
                       }
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
                    if (list2 != null && list2.size() > 0) {
                        for(JSONObject json : list2){
                            FrameAttachInfo info = new FrameAttachInfo();
                            info.setAttachFileName(json.getString("attachname"));
                            info.setUploadDateTime(json.getDate("uploaddatetime"));
                            info.setAttachGuid(json.getString("attachguid"));
                            frameInfos.add(info);
                        }
                    }
                    count = frameInfos.size();
                    this.setRowCount(count);
                    return frameInfos;
                }
            };
        }
        return model;
    }
    public DataGridModel<Record> getWorkDataGridData() {
        // 获得表格对象
        if (workmodel == null) {
            workmodel = new DataGridModel<Record>()
            {
                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                   
                	List<Record> jbjworkflows=zhenggaiImpl.getjbjworkflows(auditproject.getRowguid());
                    if(jbjworkflows!=null){
                        return jbjworkflows;
                    }
                    return null;
                }
            };
        }

        return workmodel;
    }
    public void uploadOfficalDoc(String attachGuid){
        String path = certAttachService.getAttachScan(attachGuid, auditproject.getAreacode());
        if(StringUtil.isNotBlank(path)){
            path = path.split("furl=")[1];
        }
        addCallbackParam("path", path);
    }

    public void editOfficalDoc(String attachGuid) {
        FrameAttachInfo attachinfo = certAttachService.getAttachByAttachGuid(attachGuid, auditproject.getAreacode());
        String msg = "";
        if(StringUtil.isNotBlank(auditproject.getCertdocguid())){
            msg = "ok";
        }
        addCallbackParam("msg", msg);
        addCallbackParam("filename", attachinfo.getAttachFileName());        
        addCallbackParam("attachguid", auditproject.getCertdocguid());
    }
    
    
    
    /**
     * 
     * 生成共享材料实例
     * 
     * @param shareMaterialGuid
     * @param ownerGuid
     * @param oldCliengGuid
     * @param newCliengGuid
     */
    public void createAuditShareMaterialI(String shareMaterialGuid, String certnum, String oldCliengGuid,
            String newCliengGuid,String applyername,String certownertype , String certownercerttype,String certrowguid) {
        String areacode="";
        // 如果是镇村接件
        if (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        handleRSMaterialService.createShareMaterialI(auditResult.getSharematerialguid(), certnum,
                projectguid, UUID.randomUUID().toString(), null, ZwfwConstant.ZHENGZHAOADD_WAY_BJFZ, userSession.getUserGuid(),userSession.getDisplayName(),applyername,certownertype,certownercerttype,certrowguid,areacode);
    }
    
    public void getControls(){
        String projectguid = getRequestParameter("projectguid");
        String areacode ="";
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditTask audittask1 = null;
        auditprojectold auditProject1 = null;
        if (StringUtil.isNotBlank(projectguid)) {
            auditProject1  = auditProjectService.getAuditProjectByRowGuid(projectguid);
            if (auditProject1 != null ) {
            	  audittask1 = auditTaskService.getAuditTaskByGuid(auditProject1.getTaskguid(), false).getResult();
            }
        }
            
        boolean hasJosnData = false;
        
        if (auditProject1 != null && StringUtil.isNotBlank(auditProject1.get("dataObj_baseinfo"))) {
            JSONObject otherInfoJson = JSONObject.parseObject(auditProject1.getStr("dataObj_baseinfo"));
            for (Map.Entry<String, Object> en : otherInfoJson.entrySet()) {
                String value = String.valueOf(en.getValue());
                String key = String.valueOf(en.getKey());
                String result = "";
                otherInfo.put(en.getKey(), en.getValue());
            }
            hasJosnData = true;
        }
        
        String task_id = audittask1.getTask_id();
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("MATERIALGUID", task_id);
//        sql.eq("dispinadd", type);
        sql.setOrderDesc("dispinadd");
        sql.setOrderAsc("ordernum");
        
        String otherInfoJson = "";
        if (auditProject1 != null) {
            otherInfoJson = auditProject1.get("otherinfo");
        }
        if (StringUtil.isNotBlank(otherInfoJson)) {
            addCallbackParam("otherInfoJson", otherInfoJson);
        }
        
       List<AuditRsShareMetadata> auditRsShareMetadataList = iAuditRsShareMetadata.selectAuditRsShareMetadataByCondition(sql.getMap()).getResult();
       boolean isContainInfo = auditRsShareMetadataList != null && auditRsShareMetadataList.size() > 0;
       if (isContainInfo) {
           Map<String, Object> generateMap = null;
           if(hasJosnData){                
                generateMap = generateMap(auditRsShareMetadataList, true, otherInfo,"otherInfo");
           }else{
                generateMap = generateMap(auditRsShareMetadataList, false, otherInfo,"otherInfo");
           }
           List<Map<String, Object>> fieldList = (List<Map<String, Object>>) generateMap.get("data");
           addCallbackParam("controls", fieldList.isEmpty() ? "" : generateMap);
           addCallbackParam("showcontrol", "1");
       }else {
           addCallbackParam("showcontrol", "0");
       }
    }
    
    /**
     * 
     * [生成控件] [功能详细描述]
     * 
     * @param auditRsShareMetadataList 控件元数据
     * @param isdetail
     * @param otherInfoReference       用于区分变更后的与已经走流程的办件（间接区分不同版本）
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> generateMap(List<AuditRsShareMetadata> auditRsShareMetadataList, boolean isdetail,
            Record otherInfoReference,String baseinfo) {
        Set<String> keys = new HashSet<String>();
        if (otherInfoReference != null) {
            keys = otherInfoReference.keySet();
        }
        boolean isKeysEmpty = keys.isEmpty();
        if (auditRsShareMetadataList.size() == 0 || auditRsShareMetadataList == null) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
        for (AuditRsShareMetadata auditRsShareMetadata : auditRsShareMetadataList) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            if (isKeysEmpty || (!isKeysEmpty && keys.contains(auditRsShareMetadata.getFieldname()))) {
                // 详情页面处理
                if (isdetail) {
                    if ("webuploader".equals(auditRsShareMetadata.getFielddisplaytype())
                            || "webeditor".equals(auditRsShareMetadata.getFielddisplaytype())) {
                        recordMap.put("type", auditRsShareMetadata.getFielddisplaytype());
                    }
                    else {
                        recordMap.put("type", "outputtext");
                    }
                    String value=LsZwfwBjsbConstant.CONSTANT_STR_NULL;
                    if (StringUtil.isNotBlank(auditRsShareMetadata.getDatasource_codename())) {
//                        recordMap.put("dataOptions","{code : '" + auditRsShareMetadata.getDatasource_codename() + "'}");
                        
                     String message =  otherInfoReference.getStr(auditRsShareMetadata.getFieldname());
                     
                     if("combobox".equals(auditRsShareMetadata.getFielddisplaytype()) && !"xcsm".equals(auditRsShareMetadata.getFieldname())){                         
                         value=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), message);
                     }
                     
                     if ("xcsm".equals(auditRsShareMetadata.getFieldname())) {
                         value = message;
                     }
                     
                     if("radiobuttonlist".equals(auditRsShareMetadata.getFielddisplaytype())){                         
                         value=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), message);
                     }
                     if("checkboxlist".equals(auditRsShareMetadata.getFielddisplaytype()) && !"txxl".equals(auditRsShareMetadata.getFieldname())){
                         if(10==auditproject.getApplyway()){
                             if(message.contains("[")){                                 
                                 com.alibaba.fastjson.JSONArray jsonarr = JSONObject.parseArray(message);
                                 for (Object object : jsonarr) {
                                     value+=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), object.toString())+";"; 
                                 }
                             }else{
                                 String[] arr = message.split(",");
                                 for (String string : arr) {
                                     value+=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), string.toString())+";"; 
                                }  
                             }
                         }else{
                             String[] arr = message.split(",");
                             for (String string : arr) {
                                 value+=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), string.toString())+";"; 
                            }
                             
                         }
                        //value=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), message);
                     }else if("txxl".equals(auditRsShareMetadata.getFieldname())){
                    	 value = message.replace("[", "").replace("]", "").replace("\"", "");
                     }
                        
                    } else if ("datepicker".equals(auditRsShareMetadata.getFielddisplaytype())&&StringUtil.isNotBlank(auditRsShareMetadata.getDateformat())) {// 日期控件
                        value=EpointDateUtil.convertDate2String(otherInfoReference.getDate(auditRsShareMetadata.getFieldname()), auditRsShareMetadata.getDateformat());
                    } else {
                        value=otherInfoReference.getStr(auditRsShareMetadata.getFieldname());
                    }
                        recordMap.put("bind", value);
                    
                }
                else {
                    recordMap.put("type", auditRsShareMetadata.getFielddisplaytype());
                    recordMap.put("required", "1".equals(auditRsShareMetadata.getNotnull()) ? true : false);
                    recordMap.put("bind", baseinfo+"." + auditRsShareMetadata.getFieldname());
                }
                // 数据类型在表单中的校验
                if (auditRsShareMetadata.getFieldtype() != null) {
                    // int型数据校验
                    if (auditRsShareMetadata.getFieldtype().contains("int")) {
                        recordMap.put("vType", "int");
                    }
                    // numeric型数据校验
                    else if (auditRsShareMetadata.getFieldtype().contains("numeric")) {
                        recordMap.put("vType", "float");
                    }
                }
                // 代码项值渲染
                if (StringUtil.isNotBlank(auditRsShareMetadata.getDatasource_codename())) {
                    recordMap.put("dataData", getCodeData(auditRsShareMetadata.getDatasource_codename()));
                }
                if (auditRsShareMetadata.getControlwidth() == 1) {
                    recordMap.put("width", 1);
                }
                if (auditRsShareMetadata.getControlwidth() == 2) {
                    recordMap.put("width", 2);
                }
                recordMap.put("fieldName", auditRsShareMetadata.getFieldname());
                recordMap.put("label", auditRsShareMetadata.getFieldchinesename());
                recordList.add(recordMap);
            }
        }
        map.put("data", recordList);
        return map;
    }
    
    /**
     * 根据代码项名，获取字符串形式的代码项
     * 
     * @param codeName
     * @return
     */
    public String getCodeData(String codeName) {
        StringBuffer rtnString = new StringBuffer("[");
        List<CodeItems> codeItemList = codeItemService.listCodeItemsByCodeName(codeName);
        if (ValidateUtil.isBlankCollection(codeItemList)) {
            return "";
        }
        else {
            for (CodeItems codeItems : codeItemList) {
                rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
            }
            rtnString = new StringBuffer(rtnString.substring(0, rtnString.length() - 1));
            rtnString.append("]");
            return rtnString.toString();
        }
    }
    

    public auditprojectold getAuditproject() {
        return auditproject;
    }

    public void setAuditproject(auditprojectold auditproject) {
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
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getXiangmufenleiModel() {
        if (xiangmufenleiModel == null) {
            xiangmufenleiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目分类", null, false));
        }
        return this.xiangmufenleiModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSuozaishengfenModel() {
        if (suozaishengfenModel == null) {
            suozaishengfenModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所在省份", null, false));
        }
        return this.suozaishengfenModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSuozaichengshiModel() {
        if (suozaichengshiModel == null) {
            suozaichengshiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所在城市", null, false));
        }
        return this.suozaichengshiModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getSuozaiquxianModel() {
        if (suozaiquxianModel == null) {
            suozaiquxianModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所在区县", null, false));
        }
        return this.suozaiquxianModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getJianshexingzhiModel() {
        if (jianshexingzhiModel == null) {
            jianshexingzhiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "建设性质", null, false));
        }
        return this.jianshexingzhiModel;
    }
    
    public DataGridModel<AuditProjectFormSgxkzDanti> getDataGridDanti() {
        // 获得表格对象
        if (dantimodel == null) {
            dantimodel = new DataGridModel<AuditProjectFormSgxkzDanti>()
            {
                @Override
                public List<AuditProjectFormSgxkzDanti> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditProjectFormSgxkzDanti> list = iAuditProjectFormSgxkzService.findDantiList(first*pageSize, pageSize, auditproject.getRowguid());
                    this.setRowCount(iAuditProjectFormSgxkzService.queryInt(auditproject.getRowguid()));
                    return list;
                }

            };
        }
        return dantimodel;
    }

    public AuditProjectFormSgxkz getAuditProjectFormSgxkz() {
        return auditProjectFormSgxkz;
    }

    public void setAuditProjectFormSgxkz(AuditProjectFormSgxkz auditProjectFormSgxkz) {
        this.auditProjectFormSgxkz = auditProjectFormSgxkz;
    }

    public AuditProjectSamecity getAuditProjectSamecity() {
        return auditProjectSamecity;
    }

    public void setAuditProjectSamecity(AuditProjectSamecity auditProjectSamecity) {
        this.auditProjectSamecity = auditProjectSamecity;
    }
/*
    public String getXiaqucode() {
        return xiaqucode;
    }

    public void setXiaqucode(String xiaqucode) {
        this.xiaqucode = xiaqucode;
    }*/
    
}
