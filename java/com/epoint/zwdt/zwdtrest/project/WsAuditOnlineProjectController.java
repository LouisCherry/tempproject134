
package com.epoint.zwdt.zwdtrest.project;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.TextSimilarity;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibrary;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.onlinecloud.cloudfiles.domain.AuditOnlineCloudFiles;
import com.epoint.cert.basic.onlinecloud.cloudfiles.inter.IAuditOnlineCloudFiles;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.security.crypto.ParamEncryptUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.hcp.api.IAuditHcpShow;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.jnzwdt.auditproject.jnauditprojectrest.api.IJNAuditProjectRestService;
import com.epoint.utils.WavePushInterfaceUtils;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.zwdt.auditsp.handleproject.impl.TAHandleProjectImpl;
import com.epoint.zwdt.util.TARequestUtil;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;

@RestController
@RequestMapping("/wszwdtProject")
public class WsAuditOnlineProjectController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");
    
    private static String HCPINSTANCE = ConfigUtil.getConfigValue("hcp", "HcpInatanceUrl");
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 系统参数
     */
    @Autowired
    private IConfigService iConfigService;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 办件API
     */
    @Autowired
    private IJNAuditProjectRestService iJNAuditProjectRestService;
    /**
     * 办件材料API
     */
    @Autowired
    private IAuditProjectMaterial iAuditProjectMaterial;
    /**
     * 事项基本信息API
     */
    @Autowired
    private IAuditTask iAuditTask;
    /**
     * 事项扩展信息API
     */
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    /**
     * 事项多情形API
     */
    @Autowired
    private IAuditTaskCase iAuditTaskCase;
    /**
     * 事项情形与材料关系API
     */
    @Autowired
    private IAuditTaskMaterialCase iAuditTaskMaterialCase;
    /**
     * 事项审批结果API
     */
    @Autowired
    private IAuditTaskResult iAuditTaskResult;
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    /**
     * 办件操作API
     */
    @Autowired
    private IHandleProject iHandleProject;
    /**
     * 办件评价API
     */
    @Autowired
    private IAuditOnlineEvaluat iAuditOnlineEvaluat;
    /**
     * 办件剩余时间API
     */
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;
    /**
     * 办件特殊操作API
     */
    @Autowired
    private IAuditProjectUnusual iAuditProjectUnusual;
    /**
     * 办件文书快照API
     */
    @Autowired
    private IAuditProjectDocSnap iAuditProjectDocSnap;
    
    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    
    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;
    
    /**
     * 项目基本信息API
     */
    @Autowired
    private IAuditRsItemBaseinfo iAuditRsItemBaseinfo;
    /**
     * 主题实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    /**
     * 主题实例材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    /**
     * 网上用户注册API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 政务云盘文件API
     */
    @Autowired
    private IAuditOnlineCloudFiles iAuditOnlineCloudFiles;
    @Autowired
   	private ISpaceAcceptService iSpaceAcceptService;
    /**
     * 证照基本信息API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;
    /**
     * 事项授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;
    /**
     * 法定代表人API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
    /**
     * 事项基础窗口API
     */
    @Autowired
    private IAuditOrgaWindow iAuditOrgaWindow;
    
    @Autowired
    private IHandleConfig handleConfigService;
    
    @Autowired
    private IAuditHcpShow iAuditHcpShow;
    
    @Autowired
    private IJnProjectService iJnProjectService;
    
    /**
     * 历史材料信息API
     */
    @Autowired
    private IAuditMaterialLibrary iAuditMaterialLibrary;
    

    /**
     *  申报须知接口(办件申报须知调用)
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/declareProjectNotice", method = RequestMethod.POST)
    public String declareProjectNotice(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用declareProjectNotice接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskGuid = obj.getString("taskguid");
                // 2、获取事项基本信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    // 3、获取事项扩展信息
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 5、设置需要返回的事项基本信息
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id()); // 事项业务唯一标识
                    dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                    dataJson.put("taskicon", WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码图标内容
                    JSONObject taskElementJson = new JSONObject();
                    String applytype = String.valueOf(auditTaskExtension.getWebapplytype());
                    taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT.equals(applytype) ? "0" : "1");// 是否可以网上申报
                    taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                    dataJson.put("taskelement", taskElementJson);
                    dataJson.put("spcondition", auditTask.getAcceptcondition()); // 审批条件
                    // 6、获取事项多情形数据
                    List<AuditTaskCase> auditTaskCases = iAuditTaskCase.selectTaskCaseByTaskGuid(taskGuid).getResult();
                    List<JSONObject> caseJsonList = new ArrayList<JSONObject>();
                    if (auditTaskCases != null && auditTaskCases.size() > 0) {
                        for (AuditTaskCase auditTaskCase : auditTaskCases) {
                            JSONObject caseJson = new JSONObject();
                            caseJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                            caseJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                            caseJsonList.add(caseJson);
                        }
                    }
                    dataJson.put("taskcasecount", caseJsonList.size());
                    dataJson.put("casecondition", caseJsonList);
                    if (caseJsonList.size() == 0) {
                        // 7、没有多情形的情况下需要返回事项配置的材料，多情形情况下会调用下面接口返回事项材料数据
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
                        // 7.1、对事项材料进行排序
                        Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>()
                        {
                            @Override
                            public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                // 8.1.1、优先对比材料必要性（必要在前）
                                int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                int ret = comNecessity;
                                // 8.1.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                    Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                    ret = ordernum2.compareTo(ordernum1);
                                }
                                return ret;
                            }
                        });
                        List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject materialJson = new JSONObject();
                            materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            materialJsonList.add(materialJson);
                        }
                        dataJson.put("taskmateriallist", materialJsonList);
                    }
                    log.info("=======结束调用declareProjectNotice接口=======");
                    return JsonUtils.zwdtRestReturn("1", "申报须知信息获取成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取多情形材料列表(办件申报须知多情形下调用)
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getTaskCaseMaterialsByCaseguid", method = RequestMethod.POST)
    public String getTaskCaseMaterialsByCaseguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskCaseMaterialsByCaseguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.2、获取事项标识
                // String taskGuid = obj.getString("taskguid");
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                // 2、获取多情形材料列表
                if (StringUtil.isNotBlank(taskCaseGuid)) {
                    AuditTaskCase auditTaskCase = iAuditTaskCase.getAuditTaskCaseByRowguid(taskCaseGuid).getResult();
                    String taskGuid = "";
                    if (auditTaskCase != null) {
                        taskGuid = auditTaskCase.getTaskguid();
                    }
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(taskGuid)) {
                        sql.eq("taskGuid", taskGuid);
                    }
                    sql.eq("necessity", ZwfwConstant.NECESSITY_SET_YES);
                    // 获取事项材料表中的必要材料
                    List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                            .selectMaterialListByCondition(sql.getMap()).getResult();
                    for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                        if (auditTaskMaterial != null) {
                            materialJsonList.add(this.getMaterialJson(auditTaskMaterial));
                        }
                    }
                    // 获取情形材料（情形表中仅有非必要材料）
                    List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                            .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        if (auditTaskMaterialCase != null) {
                            // int necessity = auditTaskMaterialCase.getNecessity();
                            // 3、当内网不勾选材料情形时，默认不显示该材料
                            if (ZwdtConstant.INT_NO == auditTaskMaterialCase.getNecessity()) {
                                continue;
                            }
                            // 4、获取情形与材料关系获取事项材料信息
                            AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                    .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid()).getResult();
                            if (auditTaskMaterial != null) {
                                // 兼容旧版，情形材料中的必要材料不返回
                                if (ZwfwConstant.NECESSITY_SET_NO.equals(auditTaskMaterial.getNecessity().toString())) {
                                    materialJsonList.add(this.getMaterialJson(auditTaskMaterial));
                                }
                            }
                        }
                    }
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("taskmateriallist", materialJsonList);
                log.info("=======结束调用getTaskCaseMaterialsByCaseguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "多情形材料信息获取成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取办件基本信息（办件申报信息调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/getProjectAndTaskBasic", method = RequestMethod.POST)
    public String getProjectAndTaskBasic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectAndTaskBasic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.3、获取区域标识
                String areaCode = obj.getString("areacode");
                // 1.4、获取中心标识
                String centerguid = obj.getString("centerguid");
                // 1.5、获取用户基本信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    JSONObject applyerJson = new JSONObject();
                    String workingDayGuid = handleConfigService.getFrameConfig("Public_AcceptWorkingDay", centerguid).getResult();
                    dataJson.put("workingDayGuid", workingDayGuid);
                    // 5、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("department", auditTask.getOuname());// 部门名称
                        dataJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        dataJson.put("taskicon", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码内容
                        areaCode = auditTask.getAreacode();
                        dataJson.put("areacode", areaCode);// 区域编码
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                            dataJson.put("promiseday", "1");// 即办件显示1天
                        }
                        else {
                            dataJson.put("promiseday", auditTask.getPromise_day());
                        }
                        JSONObject taskElementJson = new JSONObject();
                        taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype())) ? "0" : "1");// 是否可以网上申报
                        taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                        dataJson.put("taskelement", taskElementJson);
                        // 6、获取用户个人信息
                        AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                        if (auditOnlineRegister != null && auditOnlineIndividual != null) {
                            // 7、获取申请信息
                            applyerJson.put("declarername", auditOnlineIndividual.getClientname()); // 实际申请人（企业用）
                            applyerJson.put("applyername", auditOnlineIndividual.getClientname());//申报人
                            applyerJson.put("contactperson", auditOnlineIndividual.getClientname());//联系人
                            applyerJson.put("applyercard", auditOnlineIndividual.getIdnumber());//申报人身份证
                            applyerJson.put("linkphone", auditOnlineRegister.getMobile());//联系人手机
                            applyerJson.put("contactphone", auditOnlineRegister.getMobile());//联系人电话
                            applyerJson.put("contactidnum", auditOnlineIndividual.getIdnumber());//联系人身份证
                            if (StringUtil.isNotBlank(projectGuid)) {
                                AuditProject auditProject = iJNAuditProjectRestService.getAuditProjectDetail(projectGuid);
                                if (auditProject != null) {
                                    applyerJson.put("applyername", auditProject.getApplyername()); // 办件申报人
                                    applyerJson.put("contactperson", auditProject.getContactperson());// 联系人
                                    applyerJson.put("linkphone", auditProject.getContactmobile());// 联系人手机
                                    applyerJson.put("contactidnum", auditProject.getContactcertnum());// 联系人证照编号
                                    applyerJson.put("contactphone", auditProject.getContactphone()); // 联系人电话
                                    applyerJson.put("postcode", auditProject.getContactpostcode()); // 邮编
                                    applyerJson.put("address", auditProject.getAddress()); // 地址
                                    applyerJson.put("remark", auditProject.getRemark()); // 备注
                                    applyerJson.put("taskcaseguid", auditProject.getTaskcaseguid()); // 事项多情形标识
                                    applyerJson.put("email", auditProject.getContactemail());// 电子邮件
                                    applyerJson.put("fax", auditProject.getContactfax()); // 传真
                                    applyerJson.put("certtype", auditProject.getCerttype());
                                    applyerJson.put("legal", auditProject.getLegal());
                                    applyerJson.put("orgalegalidnumber", auditProject.getLegalid());
                                    applyerJson.put("certnum", auditProject.getCertnum());
                                    applyerJson.put("applyerguid", auditProject.getApplyeruserguid()); // 办件申报人
                                    // 8、企业的applyerguid为企业的companyid
                                    if (ZwfwConstant.APPLAYERTYPE_QY
                                            .equals(String.valueOf(auditProject.getApplyertype()))) {
                                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                                .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid())
                                                .getResult();
                                        if (auditRsCompanyBaseinfo != null) {
                                            applyerJson.put("applyerguid", auditRsCompanyBaseinfo.getCompanyid()); // 办件申报人
                                        }
                                    }
                                }
                            }
                        }
                        applyerJson.put("ifexpress", auditTaskExtension.getIf_express());// 是否使用物流
                        dataJson.put("applerinfo", applyerJson);
                        log.info("=======结束调用getProjectAndTaskBasic接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取办件基本信息成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectAndTaskBasic接口参数：params【" + params + "】=======");
            log.info("=======getProjectAndTaskBasic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "办件申报获取办件基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     *  办件初始化（办件申报页面初始化调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/private/initProjectReturnMaterials", method = RequestMethod.POST)
    public String initProjectReturnMaterials(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initProjectReturnMaterials接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取区域标识
                String areacode = "370830";
                // 1.3、获取事项情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.4、获取中心标识
                String centerGuid = obj.getString("centerguid");
                // 1.5、获取申请人类型（10:企业 20:个人）
                String applyerType = obj.getString("applyertype");
                // 1.6、申请人类型如果为空则默认为个人
                applyerType = StringUtil.isBlank(applyerType) ? ZwfwConstant.APPLAYERTYPE_GR : applyerType;
                // 1.7、是否为手机申报
                String isMobile = obj.getString("ismobile");
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                if (auditTask == null) {
                    return JsonUtils.zwdtRestReturn("0", "查询事项失败", "");
                }
                else {
                    if (StringUtil.isBlank(auditTask.getAreacode())) {
                        return JsonUtils.zwdtRestReturn("0", "配置事项辖区错误", "");
                    }
                    else {
                        areacode = auditTask.getAreacode();
                        // 1.7、获取用户注册信息
                        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                        if (auditOnlineRegister != null) {
                            // 2、获取用户信息
                            AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                    .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                            // 2.1、设置办件标识
                            String projectGuid = UUID.randomUUID().toString();
                            // 2.2、设置申请人标识(个人类型默认为申请人Applyerguid，企业默认为空)
                            String applyerGuid = "";
                            // 2.3、设置申请人姓名（申请人类型为个人则申请人姓名为个人信息中的姓名，申请人类型为企业则为空）
                            String applyerUserName = "";
                            // 2.4、设置申请人证照编号（申请人类型为个人则申请人证照编号为个人信息中的身份证，申请人类型为企业则为空）
                            String certNum = "";
                            List<AuditProjectMaterial> auditProjectMaterials = null;
                            // 2.5、如果是手机提交申报的办件
                            if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                                applyerGuid = auditOnlineIndividual.getApplyerguid();
                                applyerUserName = ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)
                                        ? auditOnlineIndividual.getClientname() : "";
                                certNum = ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)
                                        ? auditOnlineIndividual.getIdnumber() : "";
                                auditProjectMaterials = iHandleProject
                                        .InitOnlineProjectReturnMaterials(taskGuid, centerGuid, areacode, applyerGuid,
                                                applyerUserName, certNum, projectGuid, taskCaseGuid, applyerType)
                                        .getResult();
                            }
                            // 2.6、如果为PC端申报的办件
                            else {
                                if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                                    applyerUserName = auditOnlineIndividual.getClientname();
                                    applyerGuid = auditOnlineIndividual.getApplyerguid();
                                    certNum = auditOnlineIndividual.getIdnumber();
                                    auditProjectMaterials = iHandleProject.InitOnlineProjectReturnMaterials(taskGuid,
                                            centerGuid, areacode, applyerGuid, applyerUserName, certNum, projectGuid,
                                            taskCaseGuid, applyerType).getResult();
                                }
                                else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                                    // 2.7、设置企业申请人姓名
                                    String declarerName = auditOnlineIndividual.getClientname();
                                    // 2.8、设置企业申请人guid
                                    String declarerGuid = auditOnlineIndividual.getApplyerguid();
                                    // 3、初始化办件数据并且返回办件材料数据
                                    auditProjectMaterials = iHandleProject
                                            .InitOnlineCompanyProjectReturnMaterials(taskGuid, centerGuid, areacode,
                                                    applyerGuid, applyerUserName, certNum, projectGuid, taskCaseGuid,
                                                    applyerType, declarerName, declarerGuid)
                                            .getResult();
                                }
                            }
                            // 4、定义返回JSON对象
                            JSONObject dataJson = new JSONObject();
                            List<JSONObject> materialJsonlist = new ArrayList<JSONObject>();
                            for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                        .getResult();
                                JSONObject materialJson = new JSONObject();
                                // 4.1、获取办件材料基本信息返回JSON
                                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 办件材料名称
                                materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                materialJson.put("sharematerialiguid", auditProjectMaterial.getSharematerialiguid());// 办件材料关联共享材料实例标识
                                materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
                                materialJson.put("type", auditTaskMaterial.getType());// 材料类型
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
                                // 4.2、返回材料来源，默认为申请人自备
                                String materialSource;
                                if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                    materialSource = "申请人自备";
                                }
                                else {
                                    materialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource",
                                        StringUtil.isBlank(materialSource) ? "申请人自备" : materialSource);
                                // 4.3、返回材料必要性（默认为事项材料中设置，如果有多情形需要按照多情形中材料必要性）
                                int necessity = auditTaskMaterial.getNecessity();
                                if (StringUtil.isNotBlank(taskCaseGuid)) {
                                    AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                            .selectTaskMaterialCaseByGuid(taskCaseGuid, auditTaskMaterial.getRowguid())
                                            .getResult();
                                    if (auditTaskMaterialCase  != null) {
                                        necessity = auditTaskMaterialCase.getNecessity();
                                    }
                                }
                                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                    materialJson.put("necessary", "0");
                                }
                                else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                    materialJson.put("necessary", "1");
                                }
                                // 4.4、返回空白表格的附件标识
                                if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                    int templateAttachCount = iAttachService
                                            .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                    if (templateAttachCount > 0) {
                                        materialJson.put("templateattachguid",
                                                auditTaskMaterial.getTemplateattachguid());
                                    }
                                }
                                // 4.4、返回填报示例的附件标识
                                if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                    int exampleAttachCount = iAttachService
                                            .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                    if (exampleAttachCount > 0) {
                                        materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                    }
                                }
                                // 4.5、返回办件材料对应的附件业务标识
                                String clientGuid = auditProjectMaterial.getCliengguid();
                                materialJson.put("clientguid", StringUtil.isNotBlank(clientGuid) ? clientGuid : "");
                                // 4.6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                                int count = iAttachService.getAttachCountByClientGuid(clientGuid); // 获取办件材料对应的附件数量
                                int showButton = 0; // 按钮显示方式         
                                String needLoad = ZwdtConstant.CERTLEVEL_C
                                        .equals(auditProjectMaterial.get("materialLevel")) ? "1" : "0"; // 是否需要上传到证照库  0:不需要 、1:需要                  
                                if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                    // 4.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                    if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                        // 4.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                        showButton = count > 0 ? 4 : 3;
                                    }
                                    else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                        // 4.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                        showButton = count > 0 ? 1 : 0;
                                    }
                                }
                                else {
                                    // 4.6.2、如果关联了证照库
                                    if (count > 0) {
                                        // 4.6.2.1、如果有附件，则标识为已引用证照库
                                        showButton = 2;
                                    }
                                    else {
                                        // 4.6.2.2、如果没有附件，则标识为未上传
                                        showButton = 0;

                                    }
                                }
                                materialJson.put("needload", needLoad);
                                materialJson.put("showbutton", showButton);
                                materialJsonlist.add(materialJson);
                            }
                            // 5、对返回的材料列表进行排序
                            Collections.sort(materialJsonlist, new Comparator<JSONObject>()
                            {
                                @Override
                                public int compare(JSONObject b1, JSONObject b2) {
                                    Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                                    Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                                    // 5.1、优先对比材料必要性（必要在前）
                                    int comNecessity = necessaryb2.compareTo(necessaryb1);
                                    int ret = comNecessity;
                                    // 5.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                    if (comNecessity == 0) {
                                        Integer ordernumb1 = b1.get("ordernum") == null ? 0
                                                : (Integer) b1.get("ordernum");
                                        Integer ordernumb2 = b2.get("ordernum") == null ? 0
                                                : (Integer) b2.get("ordernum");
                                        ret = ordernumb2.compareTo(ordernumb1);
                                    }
                                    return ret;
                                }
                            });
                            dataJson.put("materiallist", materialJsonlist);
                            dataJson.put("projectguid", projectGuid);
                            log.info("=======结束调用initProjectReturnMaterials接口=======");
                            return JsonUtils.zwdtRestReturn("1", "初始化办件成功", dataJson.toString());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                        }
                    }
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======initProjectReturnMaterials接口参数：params【" + params + "】=======");
            log.info("=======initProjectReturnMaterials异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "初始化办件失败：" + e.getMessage(), "");
        }
    }

    /**
     *  保存办件的接口（办件申报保存调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/saveProjectInfo", method = RequestMethod.POST)
    public String saveProjectInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveProjectInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取法人代表
                String legal = obj.getString("legal");
                // 1.3、获取申请人姓名
                String applyerName = obj.getString("applyername");
                // 1.4、获取申请人guid(如果为企业为企业的companyid)
                String applyerGuid = obj.getString("applyerguid");
                // 1.5、获取申请人证照编号
                String idCard = obj.getString("idcard");
                // 1.6、获取证照类型
                String certType = obj.getString("certtype");
                // 1.7、获取联系人姓名
                String contactName = obj.getString("contactname");
                // 1.8、获取联系人手机
                String contactMobile = obj.getString("contactmobile");
                // 1.9、获取联系人电话
                String contactPhone = obj.getString("contactphone");
                // 1.10、获取联系人身份证号码
                String contactIdnum = obj.getString("contactidnum");
                // 1.11、获取邮编
                String postCode = obj.getString("postcode");
                // 1.12、获取电子邮件
                String email = obj.getString("email");
                // 1.13、获取传真
                String fax = obj.getString("fax");
                // 1.14、获取地址
                String address = obj.getString("address");
                // 1.15、获取备注
                String remark = obj.getString("remark");
                // 1.16、获取是否快递
                String ifExpress = obj.getString("if_express");
                // 1.17、获取申请人类型
                String applyType = obj.getString("applyertype");
                // 1.18、获取申请人类型
                String areaCode = obj.getString("areacode");
                // 1.19、获取当前企业的主键标识  companyguid
                String companyrowguid = obj.getString("companyrowguid");
                // 1.20、手机端申报参数
                String isMobile = obj.getString("ismobile");
                // 1.21、法人代表身份证
                String orgalegalidnumber = obj.getString("orgalegalidnumber");
                // 2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、更新初始化的办件的数据
                    AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectGuid, areaCode)
                            .getResult();
                    if (auditProject == null) {
                        return JsonUtils.zwdtRestReturn("0", "获取办件信息失败！", "");
                    }
                    // 在做保存操作前，获取办件原来的申请人Guid(企业为企业RowGuid,个人为ApplyerGuid)
                    String originApplyerGuid = auditProject.getApplyeruserguid();
                    auditProject.setRowguid(projectGuid);// 办件标识
                    auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 保存时办件状态：外网申报未提交
                    auditProject.setOperatedate(new Date());
                    auditProject.setCertnum(idCard);
                    auditProject.setApplyername(applyerName);
                    // 如果为PC端申报企业办事，则需要设置申请人guid
                    if (StringUtil.isBlank(isMobile)) {
                        // 类型如果为企业则申请人guid为企业的companyguid
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyType)) {
                            auditProject.setApplyeruserguid(companyrowguid);
                        }
                    }
                    auditProject.setContactperson(contactName);
                    auditProject.setContactmobile(contactMobile);
                    auditProject.setContactphone(contactPhone);
                    auditProject.setContactpostcode(postCode);
                    auditProject.setContactcertnum(contactIdnum);
                    auditProject.setRemark(remark);
                    auditProject.setAddress(address);
                    auditProject.setLegal(legal);
                    auditProject.setContactemail(email);
                    auditProject.setContactfax(fax);
                    auditProject.setIf_express(ifExpress);
                    auditProject.setLegalid(orgalegalidnumber);
                    // 3.2、设置办件证照类型
                    if (StringUtil.isBlank(certType)) {
                        // 3.2.1、如果证照类型为空，需要根据申请人类型设置默认值
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 3.2.1.1、若申请人类型为个人，则默认设置证照类型为身份证
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_SFZ);
                        }
                        else if (ZwfwConstant.APPLAYERTYPE_QY.equals(String.valueOf(auditProject.getApplyertype()))) {
                            // 3.2.1.2、若申请人类型为企业，则默认设置证照类型为统一信用代码
                            auditProject.setCerttype(ZwfwConstant.CERT_TYPE_TYSHXYDM);
                        }
                    }
                    else {
                        // 3.2.2、如果证照类型不为空，则获取传递的证照类型
                        auditProject.setCerttype(certType);
                    }
                    iAuditProject.updateProject(auditProject);
                    // 4、判断是否是手机申报
                    if (StringUtil.isNotBlank(isMobile) && ZwdtConstant.STRING_YES.equals(isMobile)) {
                        Map<String, String> updateFieldMap = new HashMap<>(16);
                        updateFieldMap.put("applyername=", applyerName);
                        updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
                        Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                        updateDateFieldMap.put("applydate=",
                                EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("sourceguid", projectGuid);
                        sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
                        iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                sqlConditionUtil.getMap());
                    }
                    // 5、PC端申报
                    else {
                        // 企业办件则初始化OnlinePrject表
                        if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyType)) {
                            String declarerguid = auditOnlineIndividual.getApplyerguid();
                            String declarername = auditOnlineIndividual.getClientname();
                            // 5.1、查看AuditOnlineProject办件数据是否存在,
                            String originCompanyid = "";
                            if (StringUtil.isNotBlank(originApplyerGuid)) {
                                AuditRsCompanyBaseinfo originAuditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                        .getAuditRsCompanyBaseinfoByRowguid(originApplyerGuid).getResult();
                                originCompanyid = originAuditRsCompanyBaseinfo.getCompanyid();
                            }
                            // 5.2、第一次申报为为null，则新增网厅办件数据 
                            AuditOnlineProject currentAuditOnlineProject = iAuditOnlineProject
                                    .getOnlineProjectByApplyerGuid(projectGuid, originCompanyid).getResult();
                            // 5.3、获取当前申报的企业基本信息
                            AuditRsCompanyBaseinfo originAuditRsCompanyBaseinfoQY = iAuditRsCompanyBaseinfo
                                    .getAuditRsCompanyBaseinfoByRowguid(companyrowguid).getResult();
                            // 5.4、获取当前用户授权基本信息
                            AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                                    .getGrantByBsqUserGuidAndCompanyId(originAuditRsCompanyBaseinfoQY.getCompanyid(),
                                            auditOnlineRegister.getAccountguid())
                                    .getResult();
                            String sqGuid = auditOnlineCompanyGrant != null ? auditOnlineCompanyGrant.getRowguid() : "";
                            if (currentAuditOnlineProject != null) {
                                // 5.5、如果已有该办件，判断当前的企业是否与之前的相同
                                if (originCompanyid.equals(applyerGuid)) {
                                    currentAuditOnlineProject.setApplydate(new Date());
                                    // 5.5.1、判断当前用户的授权信息与原办件是否相同，如果不同则需要更新
                                    if (!sqGuid.equals(currentAuditOnlineProject.getSqGuid())) {
                                        // 5.5.1.1、更新授权信息
                                        currentAuditOnlineProject.setSqGuid(sqGuid);
                                    }
                                    iAuditOnlineProject.updateProject(currentAuditOnlineProject);
                                }
                                else {
                                    // 5.5.2、如果企业数据与之前的不同，则需要删除原有企业
                                    iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid, originCompanyid);
                                    // 新增企业
                                    iHandleProject.addCompanyOnlineProject(auditProject, declarerguid, declarername,
                                            applyerGuid, applyerName, companyrowguid, sqGuid);
                                }
                            }
                            else {
                                // 初始化新增OnlineProject办件
                                iHandleProject.addCompanyOnlineProject(auditProject, declarerguid, declarername,
                                        applyerGuid, applyerName, companyrowguid, sqGuid);
                            }
                        }
                        // 个人办事逻辑，此处不改变原有逻辑
                        else {
                            Map<String, String> updateFieldMap = new HashMap<>();
                            updateFieldMap.put("applyername=", applyerName);
                            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
                            Map<String, String> updateDateFieldMap = new HashMap<String, String>();
                            updateDateFieldMap.put("applydate=",
                                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("sourceguid", projectGuid);
                            sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
                            iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                    sqlConditionUtil.getMap());
                        }
                    }
                    log.info("=======结束调用saveProjectInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveProjectInfo接口参数：params【" + params + "】=======");
            log.info("=======saveProjectInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkAllMaterialIsSubmit", method = RequestMethod.POST)
    public String checkAllMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkAllMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的提交状态
                String taskMaterialStatusArr = obj.getString("statusarray");
                // 1.2、获取材料的标识
                String taskMaterialGuidsArr = obj.getString("taskmaterialarray");
                // 1.3、获取多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                int noSubmitNum = 0;// 必要材料没有提交的个数
                int materialCount = 0;
                if (StringUtil.isNotBlank(taskMaterialGuidsArr) && StringUtil.isNotBlank(taskMaterialStatusArr)) {
                    // 2、将传递的材料标识和材料状态的字符串首尾的[]去除，然后组合成数组
                    taskMaterialStatusArr = taskMaterialStatusArr.replace("[", "").replace("]", "");
                    taskMaterialGuidsArr = taskMaterialGuidsArr.replace("[", "").replace("]", "");
                    String[] taskMaterialGuids = taskMaterialGuidsArr.split(","); // 材料标识数组
                    String[] taskMaterialStatus = taskMaterialStatusArr.split(","); // 材料状态数组
                    materialCount = taskMaterialGuids.length;
                    for (int i = 0; i < materialCount; i++) {
                        String materialGuid = taskMaterialGuids[i];
                        String materialStatus = taskMaterialStatus[i];
                        materialGuid = materialGuid.replaceAll("\"", "");
                        materialStatus = materialStatus.replaceAll("\"", "");
                        // 3、获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(materialGuid).getResult();
                        if (auditTaskMaterial != null) {
                            int status = Integer.parseInt(materialStatus); // 办件材料表当前材料提交的状态
                            int isRongque = auditTaskMaterial.getIs_rongque(); // 材料容缺状态
                            String paperUnnecrsstity = iConfigService.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
                            paperUnnecrsstity = StringUtil.isBlank(paperUnnecrsstity) ? ZwfwConstant.CONSTANT_STR_ZERO
                                    : paperUnnecrsstity;
                            // 4、根据材料的提交状态及材料设置的必要性判断材料是否已提交
                            // 5、材料必要性默认为事项材料配置的必要性
                            int necessity = auditTaskMaterial.getNecessity();
                            // 6、如果事项配置了多情形，则所有材料都需要提交
                            if (StringUtil.isNotBlank(taskCaseGuid)) {
                                // 若材料为未提交，则未提交数量+1
                                if (status == ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT) {
                                    noSubmitNum++;
                                    continue;
                                }
                            }
                            // 7、如果材料必要非容缺未提交
                            if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)
                                    && isRongque != ZwfwConstant.CONSTANT_INT_ONE
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                // 7.1、如果外网不需要提交纸质材料，需要判断材料提交方式
                                if (ZwfwConstant.CONSTANT_STR_ONE.equals(paperUnnecrsstity)) {
                                    // 如果材料提交方式是“提交电子文件”，或是“提交电子或纸质文件”：
                                    if ("10".equals(auditTaskMaterial.getSubmittype())
                                            || "40".equals(auditTaskMaterial.getSubmittype())) {
                                        noSubmitNum++;
                                    }
                                }
                                // 7.2、如果外网需要提交纸质材料
                                else {
                                    noSubmitNum++;
                                }
                            }
                        }
                    }
                }

                // 8、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkAllMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     *  提交办件的接口（办件申报提交调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/submitProjectByTaskguid", method = RequestMethod.POST)
    public String submitProjectByTaskguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitProjectByTaskguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.3、获取区域编码
                // String areaCode = obj.getString("areacode");
                // 1.4、获取法人代表
                String legal = obj.getString("legal");
                // 1.4、获取法人代表身份证
                String legalid = obj.getString("legalid");
                // 1.5、获取申请人姓名
                String applyerName = obj.getString("applyername");
                // 1.6、获取申请人证照编号
                String certNum = obj.getString("idcard");
                // 1.7、获取证照类型
                String certType = obj.getString("certtype");
                // 1.8、获取申请人类型
                String applyerType = obj.getString("applyertype");
                // 1.9、获取联系人姓名
                String contactName = obj.getString("contactname");
                // 1.10、获取联系人手机号
                String contactMobile = obj.getString("contactmobile");
                // 1.11、获取联系人电话
                String contactPhone = obj.getString("contactphone");
                // 1.12、获取联系人身份证号码
                String contactIdnum = obj.getString("contactidnum");
                // 1.14、获取通讯地址
                String address = obj.getString("address");
                // 1.17、获取传真
                AuditProject auditproject = new AuditProject();
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                if (auditTask == null) {
                   return JsonUtils.zwdtRestReturn("0", "该事项未查询到！", "");
                }
                
                if (!"370830".equals(auditTask.getAreacode())){
                	 return JsonUtils.zwdtRestReturn("0", "该事项未查询到！", "");
                }
                  auditproject.setOperatedate(new Date());
                  auditproject.setRowguid(UUID.randomUUID().toString());
                  auditproject.setIs_test(0);
                  auditproject.setLegal(legal);
                  auditproject.setAddress(address);
//                  auditproject.setIs_delay(20);
                  auditproject.setIs_charge(0);
                  auditproject.setApplyway(20);
                  auditproject.setContactmobile(contactMobile);
                  auditproject.setContactphone(contactPhone);
                  auditproject.setContactperson(contactName);
                  auditproject.setApplydate(new Date());
                  auditproject.setCertnum(certNum);
                  auditproject.setCerttype(certType);
                  auditproject.setApplyeruserguid(UUID.randomUUID().toString());
                  auditproject.setApplyername(applyerName);
                  auditproject.setApplyertype(Integer.parseInt(applyerType));
                  auditproject.setStatus(12);
                  auditproject.setPromise_day(auditTask.getPromise_day());
                  auditproject.setTasktype(auditTask.getType());
                  auditproject.setOuguid(auditTask.getOuguid());
                  auditproject.setTaskguid(auditTask.getRowguid());
                  auditproject.setProjectname(auditTask.getTaskname());
                  auditproject.setInsertdate(new Date());
                  auditproject.setLegalid(legalid);
                  auditproject.setTask_id(auditTask.getTask_id());
                  auditproject.setTaskid(auditTask.getTask_id());
                  auditproject.setOuname(auditTask.getOuname());
                  auditproject.setAreacode(auditTask.getAreacode());
                  auditproject.setCenterguid("");
                  auditproject.setContactcertnum(certNum);
                  auditproject.setIf_express("0");
                  auditproject.setIf_jz_hall("1");
                  auditproject.setXmname("汶上卓繁");
                  
                  //个性化办件流水号，以及搜索密码
/*                  TAHandleProjectImpl  jnproject=new TAHandleProjectImpl();
                  Record record= jnproject.getFlowsn(auditTask, auditproject);
                  String flowsn= record.getStr("receiveNum");
                  String searchpwd= record.getStr("searchpwd");*/
                String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());

                auditproject.setFlowsn(flowsn);
//                  auditproject.set("searchpwd", searchpwd);
                  iAuditProject.addProject((auditproject));
          

                    log.info("=======结束调用submitProjectByTaskguid接口=======");
                    return JsonUtils.zwdtRestReturn("1", "提交办件成功", "");
            }else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitProject接口参数：params【" + params + "】=======");
            log.info("=======submitProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    
    /**
     *  删除办件的接口(待提交页面中放弃申报调用)
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/private/deleteProject", method = RequestMethod.POST)
    public String deleteProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用deleteProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.3、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 1.4、 获取事项guid
                String taskGuid = obj.getString("taskguid");
                // 1.5、 获取公司id
                String companyid = obj.getString("companyid");
                if (StringUtil.isNotBlank(taskGuid)) {
                    //如果传了taskguid，以事项的areacode为准，如果没传则用前台传来的areacode
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                    if (auditTask != null) {
                        if (StringUtil.isNotBlank(auditTask.getAreacode())) {
                            areaCode = auditTask.getAreacode();
                        }
                    }
                }
                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 2、删除AUDIT_PROJECT表数据
                    iAuditProject.deleteProjectByGuid(projectGuid, areaCode);
                    // 3、删除AUDIT_ONLINE_PROJECT表数据，如果传递了companyid，则为企业办件，需要根据该guid删除办件
                    if (StringUtil.isNotBlank(companyid)) {
                        iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid, companyid);
                    }
                    else {
                        iAuditOnlineProject.deleteOnlineProjectByGuid(projectGuid,
                                auditOnlineIndividual.getApplyerguid());
                    }
                    log.info("=======结束调用deleteProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "删除办件成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======deleteProject接口参数：params【" + params + "】=======");
            log.info("=======deleteProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "删除办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取材料合格标准的接口（点开上传按钮后页面调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getMaterialstandardByGuid", method = RequestMethod.POST)
    public String getMaterialstandardByGuid(@RequestBody String params) {
        try {
            log.info("=======开始调用getMaterialstandardByGuid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项材料标识
                String taskMaterialGuid = obj.getString("taskmaterialguid");
                // 1.2、获取办件材料标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskMaterialGuid)
                        .getResult();
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (auditTaskMaterial != null) {
                    dataJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
                    dataJson.put("projectmaterialguid", projectMaterialGuid);// 办件材料主键
                    dataJson.put("materialstantard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                            : auditTaskMaterial.getStandard());// 材料合格标准
                }
                log.info("=======结束调用getMaterialstandardByGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取材料合格标准成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialstandardByGuid接口参数：params【" + params + "】=======");
            log.info("=======getMaterialstandardByGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取材料合格标准异常：" + e.getMessage(), "");
        }
    }

    /**
     *  获取材料对应的附件列表接口（点开上传按钮后页面调用）
     * 
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/getMaterialAttachListbyClientguid", method = RequestMethod.POST)
    public String getMaterialAttachListbyClientguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMaterialAttachListbyClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料对应附件的业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取上传方式（0:云盘 1:本地上传 2:手机上传 为空显示所有）
                String uploadType = obj.getString("uploadtype");
                List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
                // 2、获取办件材料对应的附件
                List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientGuid);
                for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                    String cliengTag = frameAttachInfo.getCliengTag();
                    if (StringUtil.isBlank(uploadType) || uploadType.equals(cliengTag)) {
                        JSONObject attachJson = new JSONObject();
                        FrameAttachStorage frameAttachStorage = iAttachService
                                .getAttach(frameAttachInfo.getAttachGuid());
                        if (frameAttachStorage != null) {
                            // 3、设置附件信息
                            attachJson.put("attachicon", WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/auditattach/readAttach?attachguid=" + frameAttachStorage.getAttachGuid());// 附件图标
                            attachJson.put("attachname", frameAttachInfo.getAttachFileName()); // 附件名称
                            attachJson.put("attachsize", AttachUtil.convertFileSize(frameAttachInfo.getAttachLength()));// 附件大小
                            attachJson.put("attachsource", frameAttachInfo.getCliengTag());// 附件来源
                            attachJson.put("attachguid", frameAttachInfo.getAttachGuid());// 附件唯一标识
                            attachJsonList.add(attachJson);
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("attachlist", attachJsonList);
                log.info("=======结束调用getMaterialAttachListbyClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取上传的附件列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialAttachListbyClientguid接口参数：params【" + params + "】=======");
            log.info("=======getMaterialAttachListbyClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取上传的附件列表异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断材料是否上传的接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkMaterialIsUploadByClientguid", method = RequestMethod.POST)
    public String checkMaterialIsUploadByClientguid(@RequestBody String params) {
        try {
            log.info("=======开始调用checkMaterialIsUploadByClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件材料附件业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取办件材料标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                // 1.3、获取共享材料实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.4、获取材料提交状态
                String status = obj.getString("status");
                // 1.5、获取材料类型
                String type = obj.getString("type");
                // 1.6、获取材料对应附件是否需要上传到证照库（只有C级证照才需要）
                String needLoad = obj.getString("needload");
                // 1.7、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 2、获取办件材料对应的附件数量
                int attachCount = iAttachService.getAttachCountByClientGuid(clientGuid);
                int intStatus = Integer.parseInt(status); // 材料提交状态
                // 3、根据材料是否有附件及材料的提交状态更新材料的提交状态
                if (attachCount > 0) {
                    // 3.1、若材料有对应的附件，说明电子材料已提交
                    switch (intStatus) {
                        // 3.1.1、材料原先的状态为未提交更新为电子已提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC;
                            break;
                        // 3.1.2、材料原先的状态为纸质已提交更新为电子和纸质都提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                            break;
                        default:
                            break;
                    }
                }
                else {
                    // 3.2、若材料没有对应的附件，说明电子材料没有提交
                    switch (intStatus) {
                        // 3.2.1、材料原先的状态为电子已提交更新为未提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT;
                            break;
                        // 3.2.2、材料原先的状态为电子和纸质都提交更新为纸质已提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                            intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER;
                            break;
                        default:
                            break;
                    }
                }
                // 4、更新办件材料数据
                AuditProjectMaterial auditProjectMaterial = new AuditProjectMaterial();
                auditProjectMaterial.setStatus(intStatus);
                auditProjectMaterial.setCliengguid(clientGuid);
                auditProjectMaterial.setRowguid(projectMaterialGuid);
                if (StringUtil.isNotBlank(projectGuid)) {
                    auditProjectMaterial.setProjectguid(projectGuid);
                }
                iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                // 5、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                int showButton = 0; // 按钮显示方式
                if (StringUtil.isBlank(shareMaterialIGuid)) {
                    // 5.1、如果没有从证照库引用附件，则为普通附件及填表
                    if (String.valueOf(WorkflowKeyNames9.MaterialType_Form).equals(type)) {
                        // 5.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                        showButton = attachCount > 0 ? 4 : 3;
                    }
                    else {
                        // 5.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                        showButton = attachCount > 0 ? 1 : 0;
                    }
                }
                else {
                    // 5.2、如果关联了证照库
                    if (ZwdtConstant.NEEDLOAD_Y.equals(needLoad)) {
                        // 5.2.1、如果需要更新证照库，有附件则显示已上传，没有附件则显示未未上传
                        showButton = attachCount > 0 ? 1 : 0;
                        // 5.2.2、更新证照库版本
                        CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                        int countRs = iAttachService.getAttachCountByClientGuid(certInfo.getCertcliengguid());
                        // 5.2.2.1、数量不一致
                        if (attachCount > 0 && attachCount != countRs) {
                            certInfo.setCertcliengguid(clientGuid);
                            shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null, clientGuid,
                                    ZwdtConstant.MATERIALTYPE_CERT);
                            //5.2.2.1.1、关联到办件材料
                            auditProjectMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                            iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                        }
                        else if (attachCount == countRs) {
                            // 5.2.2.2、数量一致继续比较
                            List<FrameAttachInfo> rsInfos = iAttachService
                                    .getAttachInfoListByGuid(certInfo.getCertcliengguid());
                            List<FrameAttachInfo> projectInfos = iAttachService
                                    .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                            rsInfos.sort((a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                            projectInfos.sort((a, b) -> a.getAttachStorageGuid().compareTo(b.getAttachStorageGuid()));
                            boolean isModify = false;
                            for (int i = 0; i < rsInfos.size(); i++) {
                                // 5.2.2.2.1、AttachStorageGuid不同说明附件改动过
                                if (!rsInfos.get(i).getAttachStorageGuid()
                                        .equals(projectInfos.get(i).getAttachStorageGuid())) {
                                    isModify = true;
                                    break;
                                }
                            }
                            if (isModify) {
                                certInfo.setCertcliengguid(clientGuid);
                                shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null, clientGuid,
                                        ZwdtConstant.MATERIALTYPE_CERT);
                                //5.2.2.2.2、关联到办件材料          
                                auditProjectMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                iAuditProjectMaterial.updateProjectMaterial(auditProjectMaterial);
                            }
                        }
                    }
                    else {
                        // 5.2.3、如果不需要更新证照库，有附件则显示已引用证照库，没有附件则显示未未上传
                        showButton = attachCount > 0 ? 2 : 0;
                    }
                }
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("showbutton", showButton);
                dataJson.put("needload", needLoad);
                dataJson.put("status", intStatus);
                dataJson.put("sharematerialiguid", shareMaterialIGuid);
                log.info("=======结束调用checkMaterialIsUploadByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkMaterialIsUploadByClientguid接口参数：params【" + params + "】=======");
            log.info("=======checkMaterialIsUploadByClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     *  获取我的办件的接口（办件中心各模块调用）
     * 
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/private/getMyProject", method = RequestMethod.POST)
    public String getMyProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.3、获取搜索内容
                String keyWord = obj.getString("keyword");
                // 1.4、获取办件的状态（0:全部 1:待提交 2:待补正  3:待提交原件 4:待缴费 5:审批中 6:待签收 7:待评价 8：已办结 9:其他）
                String status = obj.getString("status");
                // 1.5、获取区域编码
                // String areaCode = obj.getString("areacode");
                // 1.6、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、查询各个状态的办件
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1、办件通用的检索条件
                    sqlConditionUtil.eq("applyerguid", auditOnlineIndividual.getApplyerguid()); // 申请人标识
                    //sqlConditionUtil.eq("epidemic",ZwfwConstant.CONSTANT_STR_ONE); // 申报承诺书标志位
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("projectname", keyWord); // 事项名称
                    }
                    // 3.2、根据各种办件的状态拼接不同的SQL语句
                    int intStatus = Integer.parseInt(status);
                    switch (intStatus) {
                        case 0:
                            // 全部办件（外网申报已提交、外网申报预审退回、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过、正常办结、不予受理、撤销申请、异常终止）
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTU + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + "," + ZwdtConstant.BANJIAN_STATUS_YJJ
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DBB + "," + ZwdtConstant.BANJIAN_STATUS_YSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_ZCBJ + "," + ZwdtConstant.BANJIAN_STATUS_BYSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        case 1:
                            // 待提交(外网申报未提交和预审不通过的办件)
                            sqlConditionUtil.in("status",
                                    ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU + ",200");
                            break;
                        case 2:
                            // 待补正(待补办的办件)
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_DBB));
                            break;
                        case 3:
                            // 待原件提交（功能暂无）TODO
                            break;
                        case 4:
                            // 待缴费(需要缴费并且已核价未收讫的办件)
                            sqlConditionUtil.eq("is_charge", "1"); // 需要缴费
                            sqlConditionUtil.eq("is_check", "1"); // 已核价
                            sqlConditionUtil.eq("is_fee", "0"); // 未收讫
                            break;
                        case 5:
                            // 审批中(外网申报已提交、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_YSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG);
                            break;
                        case 6:
                            // 待签收（功能暂无） TODO
                            break;
                        case 7:
                            // 待评价(未评价且正常办结的办件)
                            sqlConditionUtil.eq("is_evaluat", "0");
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 8:
                            // 已办结(已评价且正常办结的办件)
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 9:
                            // 其他(不予受理、撤销申请、异常终止)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_BYSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        case 10:
                            // 已办结
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        default:
                            break;
                    }
                    // 3.3、根据条件查询到办件数据
                    PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "applydate", "desc").getResult();
                    int totalCount = pageData.getRowCount(); // 办件总数
                    List<AuditOnlineProject> auditOnlineProjectList = pageData.getList(); // 办件数据集合
                    // 4、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    List<JSONObject> projectJsonList = new ArrayList<JSONObject>();
                    for (AuditOnlineProject auditOnlineProject : auditOnlineProjectList) {
                        String type = auditOnlineProject.getType();
                        // 4.1、如果是普通办件，返回普通办件的JSON数据
                        if (ZwdtConstant.ONLINEPROJECTTYPE_PROJECT.equals(type)) {
                            String projectGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus()); // 办件状态
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            String taskType = auditOnlineProject.getTasktype(); // 事项类型
                            // 4.1.1、获取事项基本信息
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditOnlineProject.getTaskguid(), true)
                                    .getResult();
                            if (auditTask != null) {
                                JSONObject projectJson = new JSONObject();
                                // 4.1.2、获取办件基本的信息拼接JSON
                                // 4.1.2.1、获取办件的基本信息拼接JSON
                                projectJson.put("projectname", auditOnlineProject.getProjectname()); // 事项名称
                                projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                                projectJson.put("is_evaluat", auditOnlineProject.getIs_evaluat());// 评价状态
                                projectJson.put("flowsn", auditOnlineProject.getFlowsn());// 办件流水号
                                projectJson.put("areacode", auditOnlineProject.getAreacode());// 辖区编码
                                projectJson.put("projectform", auditTask.getStr("projectform"));
                                projectJson.put("projectguid", projectGuid); // 办件标识
                                projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 事项标识
                                projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                                projectJson.put("type", Integer.parseInt(auditOnlineProject.getType())); // 类型 0：普通办件 1：套餐
                                projectJson.put("flowsn", auditOnlineProject.getFlowsn()); // 办件编号
                                projectJson.put("projectstatus", ZwdtConstant
                                        .getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus()))); // 办件状态
                                projectJson.put("ouname", auditOnlineProject.getOuname()); // 办件受理部门
                                projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT)); // 保存时间
                                projectJson.put("applydate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT)); // 办件申请时间
                                projectJson.put("applyertype", auditOnlineProject.getApplyertype()); // 办件申请人类型
                                projectJson.put("taskcaseguid", auditOnlineProject.getTaskcaseguid()); // 办件多情形标识
                                //projectJson.put("certnum", auditProject.get("certnum")); //普通办件情况下返回信用代码
                                // 4.1.2.2、获取办件办结时间
                                String banJieData = "";
                                if (auditOnlineProject.getBanjiedate() != null) {
                                    banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                            EpointDateUtil.DATE_FORMAT);
                                }
                                projectJson.put("banjiedate", banJieData);
                                // 4.1.3、获取办件评价内容
                                String evaluate = "无评价";
                                AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                        .selectEvaluatByClientIdentifier(auditOnlineProject.getSourceguid())
                                        .getResult();
                                if (auditOnlineEvaluat != null
                                        && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                    evaluate = iCodeItemsService.getItemTextByCodeName("好差评满意度层级",
                                            auditOnlineEvaluat.getSatisfied());
                                }
                                projectJson.put("evaluate", evaluate);
                                // 4.1.4、获取办件剩余时间
                                String spareTime = "";
                                AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                        .getSparetimeByProjectGuid(projectGuid).getResult();
                                if (auditProjectSparetime != null) {
                                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                        spareTime = "即办件";
                                    }
                                    else {
                                        int spareMinutes = auditProjectSparetime.getSpareminutes();
                                        if (spareMinutes >= 0) {
                                            spareTime = "距承诺办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                        else {
                                            spareTime = "已超过办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                    }
                                }
                                else {
                                    if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                        spareTime = "待预审";
                                        projectJson.put("chehui", "撤回");
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTG) {
                                        spareTime = "预审通过";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            spareTime = "已接件";
                                        }
                                    }
                                    else if ((projectStatus == ZwfwConstant.BANJIAN_STATUS_SPTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_SPBTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ)) {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            int spendMinute = 0;
                                            if ( auditOnlineProject.getSpendtime() != null ) {
                                                spendMinute = auditOnlineProject.getSpendtime();
                                            }
                                            spareTime = "办理用时：" + CommonUtil.getSpareTimes(spendMinute);
                                        }
                                    }
                                    else {
                                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                            spareTime = "即办件";
                                        }
                                        else {
                                            spareTime = "距承诺办结期限：--个工作日";
                                        }
                                    }
                                }
                                projectJson.put("sparetime", spareTime);
                                // 4.1.5、获取办件状态
                                if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                    // 4.1.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                    projectJson.put("currentstatus", "待提交");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                    // 4.1.5.2、外网申报预审退回（预审退回状态）
                                    projectJson.put("currentstatus", "预审退回");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                                    // 4.1.5.2.1、获取预审退回原因
                                    /*SqlConditionUtil opSqlConditionUtil = new SqlConditionUtil();
                                    opSqlConditionUtil.eq("projectGuid", projectGuid);
                                    opSqlConditionUtil.eq("operatetype", ZwfwConstant.OPERATE_YSDH);*/
                                    projectJson.put("remarks",StringUtil.isBlank(auditOnlineProject.getBackreason()) ? "" : auditOnlineProject.getBackreason());
                                }
                                else if ("200".equals(String.valueOf(projectStatus))) {
                                    projectJson.put("currentstatus", "撤销办件");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【撤销办件】</span>");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                    // 4.1.5.3、待补办、受理后待补办（待补正状态）
                                    projectJson.put("currentstatus", "待补正");
                                    String buZhengText = "";// 补正材料名称
                                    int buZhengCount = 0; // 补正材料数量
                                    SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();// 补正材料检索条件
                                    materialSqlConditionUtil.eq("projectguid", projectGuid);
                                    materialSqlConditionUtil.in("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                                    List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                            .selectProjectMaterialByCondition(materialSqlConditionUtil.getMap())
                                            .getResult();
                                    for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                        buZhengText += auditProjectMaterial.getTaskmaterial() + "、";
                                        buZhengCount++;
                                    }
                                    if (StringUtil.isNotBlank(buZhengText)) {
                                        buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                                + buZhengCount + "份材料）";
                                    }
                                    projectJson.put("buzhengtext",
                                            StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);// 补正材料
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                    // 4.1.5.4、正常办结且未评价（待评价状态）
                                    projectJson.put("currentstatus", "待评价");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                    // 4.1.5.5、正常办结且已评价（待评价状态）
                                    projectJson.put("currentstatus", "已办结");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_BYSL
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_CXSQ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
                                    // 4.1.5.6、不予受理、撤销申请、异常终止（其他状态）
                                    projectJson.put("currentstatus", "其他");
                                }
                                else {
                                    // 4.1.5.7、除去之前列出的状态外的办件（审批中状态）
                                    projectJson.put("currentstatus", "审批中");
                                }
                                projectJsonList.add(projectJson);
                            }
                        }
                        else if (ZwdtConstant.ONLINEPROJECTTYPE_BUSINESS.equals(auditOnlineProject.getType())) {
                            // 4.2、如果是套餐，返回套餐的JSON数据
                            String biGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus());
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            JSONObject projectJson = new JSONObject();
                            // 4.2.1、获取套餐的基本信息拼接JSON
                            projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                            projectJson.put("projectname", auditOnlineProject.getProjectname());// 主题名称
                            projectJson.put("flowsn", auditOnlineProject.getFlowsn());// 套餐申报编号
                            projectJson.put("biguid", biGuid); // 主题实例标识
                            projectJson.put("projectguid", biGuid); // 主题实例标识
                            projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 主题标识
                            projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                            projectJson.put("type", Integer.parseInt(auditOnlineProject.getType())); // 类型 0：普通办件 1：套餐
                            
                         // 如果是套餐要返回一下个性化文件夹目录
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditOnlineProject.getType())) {
                                Record auditSpBusiness = iAuditSpBusiness
                                        .getAuditSpBusinessByRowguid(auditOnlineProject.getTaskguid()).getResult();
                                // 返回表单页面目录层级
                                String folderStr = auditSpBusiness.getStr("handleurl");
                                String folder = "myspace";
                                if (StringUtil.isNotBlank(folderStr)) {
                                    if (folderStr.lastIndexOf("/") != -1) {
                                        folderStr = folderStr.substring(folderStr.lastIndexOf("/") + 1);
                                        if (!"auditspintegrated".equals(folderStr)) {
                                            folder = folderStr;
                                        }
                                    }
                                }
                                projectJson.put("folder", folder);
                            }
                            
                            
                            projectJson.put("projectstatus",
                                    ZwdtConstant.getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus())));// 办件状态
                            projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                    auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT));// 保存时间
                            projectJson.put("applydate", EpointDateUtil
                                    .convertDate2String(auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT));// 办件申请时间
                            // 4.2.2、获取套餐办结时间
                            String banJieData = "";
                            if (auditOnlineProject.getBanjiedate() != null) {
                                banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                        EpointDateUtil.DATE_FORMAT);
                            }
                            projectJson.put("banjiedate", banJieData);
                            // 4.2.3、获取套餐评价内容
                            String evaluate = "无评价";
                            AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                    .selectEvaluatByClientIdentifier(biGuid).getResult();
                            if (auditOnlineEvaluat != null
                                    && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                evaluate = iCodeItemsService.getItemTextByCodeName("好差评满意度层级",
                                        auditOnlineEvaluat.getSatisfied());
                            }
                            projectJson.put("evaluate", evaluate);
                            // 4.2.4、获取业务标识、子申报标识、阶段标识
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                            projectJson.put("yewuguid", auditSpInstance == null ? "" : auditSpInstance.getYewuguid());
                            List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                    .getResult();
                            String subAppGuid = "";
                            if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
                                AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                                subAppGuid = auditSpISubapp.getRowguid();
                                projectJson.put("subappguid", auditSpISubapp.getRowguid());
                                projectJson.put("phaseguid", auditSpISubapp.getPhaseguid());
                            }
                            // 4.2.5、获取套餐状态
                            if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                // 4.2.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                projectJson.put("currentstatus", "待提交");
                                projectJson.put("sparetime", "待提交");
                                projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                // 4.2.5.2、预审退回（预审退回状态）
                                projectJson.put("currentstatus", "预审退回");
                                projectJson.put("remarks", auditOnlineProject.getBackreason());// 套餐退回原因
                                projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                // 4.2.5.3、待补办、受理后待补办（预审退回状态）
                                projectJson.put("currentstatus", "待补正");
                                projectJson.put("sparetime", "待补正");

                                String buZhengText = "";// 补正材料名称
                                int buZhengCount = 0;// 补正材料数量
                                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                                        .getSpIPatchMaterialBySubappGuid(subAppGuid).getResult();
                                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                                    buZhengText += auditSpIMaterial.getMaterialname() + "、";
                                    buZhengCount++;
                                }
                                if (StringUtil.isNotBlank(buZhengText)) {
                                    buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                            + buZhengCount + "份材料）";
                                }
                                projectJson.put("buzhengtext",
                                        StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                // 4.2.5.4、正常办结且未评价（待评价状态）
                                projectJson.put("currentstatus", "待评价");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                // 4.2.5.5、正常办结且已评价（已办结状态）
                                projectJson.put("currentstatus", "已办结");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                //4.2.5.6、待预审（外网申报已提交状态）
                                projectJson.put("currentstatus", "待预审");
                                projectJson.put("sparetime", "待预审");
                            }
                            else {
                                // 4.2.5.7、除去之前列出的状态外的办件（已办结状态）
                                projectJson.put("currentstatus", "审批中");
                                projectJson.put("sparetime", "审批中");
                            }
                            projectJsonList.add(projectJson);
                        }
                    }
                    dataJson.put("projectlist", projectJsonList);
                    dataJson.put("totalcount", totalCount);
                    dataJson.put("projectcount", totalCount);// 我的空间（我的申请使用）
                    log.info("=======结束调用getMyProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我的办件成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyProject异常信息：" + e.getMessage() + "=======");
            log.info("=======getMyProject接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的办件异常：" + e.getMessage(), "");
        }
    }

    /**
     *  获取我的企业办件的接口（办件中心各模块调用）
     * 
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/private/getMyCompanyProject", method = RequestMethod.POST)
    public String getMyCompanyProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyCompanyProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.3、获取搜索内容
                String keyWord = obj.getString("keyword");
                // 1.4、获取办件的状态（0:全部 1:待提交 2:待补正  3:待提交原件 4:待缴费 5:审批中 6:待签收 7:待评价 8：已办结 9:其他）
                String status = obj.getString("status");
                // 1.5、获取企业id
                String compamyId = obj.getString("companyid");
                // 1.6、用户guid
                String userguid = obj.getString("userguid");
                // 1.6、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取企业用户角色
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    String userLevel = this.getCompanyUserLevel(auditOnlineRegister.getAccountguid(),
                            auditOnlineRegister.getIdnumber(), compamyId);
                    if (userLevel == null) {
                        return JsonUtils.zwdtRestReturn("0", "请确保用户为企业用户", "");
                    }
                    // 3、查询各个状态的办件
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1、办件通用的检索条件，如果用户是法人或者管理者，可以看到企业所有办件
                    sqlConditionUtil.eq("applyerguid", compamyId);
                    //3.2、 如果当前用户是经办人，则只能看到自己所申请的办件
                    if (ZwdtConstant.GRANT_AGENT.equals(userLevel)) {
                        sqlConditionUtil.eq("declarerguid", auditOnlineIndividual.getApplyerguid());
                    }
                    //3.3、如果传递了userguid，则代表为查看某个人所有的办件，则显示该userguid下所有的办件
                    if (StringUtil.isNotBlank(userguid)) {
                        AuditOnlineIndividual dbrAuditOnlineIndividual = iAuditOnlineIndividual
                                .getIndividualByAccountGuid(userguid).getResult();
                        sqlConditionUtil.eq("declarerguid", dbrAuditOnlineIndividual.getApplyerguid());
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("projectname", keyWord); // 事项名称
                    }
                    // 3.2、根据各种办件的状态拼接不同的SQL语句
                    int intStatus = Integer.parseInt(status);
                    switch (intStatus) {
                        case 0:
                            // 全部办件（外网申报已提交、外网申报预审退回、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过、正常办结、不予受理、撤销申请、异常终止）
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTU + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + "," + ZwdtConstant.BANJIAN_STATUS_YJJ
                                    + "," + ZwdtConstant.BANJIAN_STATUS_DBB + "," + ZwdtConstant.BANJIAN_STATUS_YSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG
                                    + "," + ZwdtConstant.BANJIAN_STATUS_ZCBJ + "," + ZwdtConstant.BANJIAN_STATUS_BYSL
                                    + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        case 1:
                            // 待提交(外网申报未提交和预审不通过的办件)
                            sqlConditionUtil.in("status",
                                    ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU + ",200");
                            break;
                        case 2:
                            // 待补正(待补办的办件)
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_DBB));
                            break;
                        case 3:
                            // 待原件提交（功能暂无）TODO
                            break;
                        case 4:
                            // 待缴费(需要缴费并且已核价未收讫的办件)
                            sqlConditionUtil.eq("is_charge", "1"); // 需要缴费
                            sqlConditionUtil.eq("is_check", "1"); // 已核价
                            sqlConditionUtil.eq("is_fee", "0"); // 未收讫
                            break;
                        case 5:
                            // 审批中(外网申报已提交、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_WWYTJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                                    + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_YSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_SPBTG + "," + ZwdtConstant.BANJIAN_STATUS_SPTG);
                            break;
                        case 6:
                            // 待签收（功能暂无） TODO
                            break;
                        case 7:
                            // 待评价(未评价且正常办结的办件)
                            sqlConditionUtil.eq("is_evaluat", "0");
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 8:
                            // 已办结(已评价且正常办结的办件)
                            sqlConditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_ZCBJ));
                            break;
                        case 9:
                            // 其他(不予受理、撤销申请、异常终止)
                            sqlConditionUtil.in("status", ZwdtConstant.BANJIAN_STATUS_BYSL + ","
                                    + ZwdtConstant.BANJIAN_STATUS_CXSQ + "," + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                            break;
                        default:
                            break;
                    }
                    // 3.3、根据条件查询到办件数据
                    PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "applydate", "desc").getResult();
                    int totalCount = pageData.getRowCount(); // 办件总数   
                    //3.4、获取近一个月的办件数量
                    sqlConditionUtil.between("operatedate", EpointDateUtil.getDateBefore(new Date(), 30), new Date());
                    PageData<AuditOnlineProject> recentpageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                            sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "applydate", "desc").getResult();
                    int recentcount = recentpageData.getRowCount(); // 近一个月办件总数
                    List<AuditOnlineProject> auditOnlineProjectList = pageData.getList(); // 办件数据集合
                    // 4、定义返回JSON对象
                    List<JSONObject> projectJsonList = new ArrayList<JSONObject>();
                    for (AuditOnlineProject auditOnlineProject : auditOnlineProjectList) {
                        String type = auditOnlineProject.getType();
                        // 4.1、如果是普通办件，返回普通办件的JSON数据
                        if (ZwdtConstant.ONLINEPROJECTTYPE_PROJECT.equals(type)) {
                            String projectGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus()); // 办件状态
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            String taskType = auditOnlineProject.getTasktype(); // 事项类型
                            // 4.1.1、获取事项基本信息
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditOnlineProject.getTaskguid(), true)
                                    .getResult();
                            if (auditTask != null) {
                                JSONObject projectJson = new JSONObject();
                                // 4.1.2.2、如果是企业办件，获取当前办件实际申报人
                                String projectApplyerGuid = auditOnlineProject.getDeclarerguid();
                                projectJson.put("declarerguid", projectApplyerGuid);
                                projectJson.put("declarername", auditOnlineProject.getDeclarername());
                                projectJson.put("projectform", auditTask.getStr("projectform"));
                                // 4.1.2.3、如果是此办件的申报人和当前用户不同
                                if (!projectApplyerGuid.equals(auditOnlineIndividual.getApplyerguid())) {
                                    projectJson.put("watchmode", 1);
                                    // 4.1.2.4、获取提交办件人信息
                                    AuditOnlineIndividual applyerAuditOnlineIndividual = iAuditOnlineIndividual
                                            .getIndividualByApplyerGuid(projectApplyerGuid).getResult();
                                    // 4.1.2.5、获取提交办件人的等级,
                                    String projectUserLevel = this.getCompanyUserLevel(
                                            applyerAuditOnlineIndividual.getAccountguid(),
                                            applyerAuditOnlineIndividual.getIdnumber(), compamyId);
                                    // 4.1.2.6、返回提交用户等级
                                    if (ZwdtConstant.GRANT_LEGEL_PERSION.equals(projectUserLevel)) {
                                        projectJson.put("declarerlevel", "法人");
                                    }
                                    else {
                                        // 4.1.2.7、如果不为法人，则通过sqGuid去获取授权等级
                                        String sqGuid = auditOnlineProject.getSqGuid();
                                        AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                                                .getAuditOnlineCompanyGrantByGrantguid(sqGuid).getResult();
                                        String myuserLevel = auditOnlineCompanyGrant != null
                                                ? auditOnlineCompanyGrant.getBsqlevel() : "";
                                        // 4.1.2.8、判断用户授权等级
                                        if (ZwdtConstant.GRANT_MANAGER.equals(myuserLevel)) {
                                            projectJson.put("declarerlevel", "管理者");
                                        }
                                        else if (ZwdtConstant.GRANT_AGENT.equals(myuserLevel)) {
                                            projectJson.put("declarerlevel", "代办人");
                                        }
                                    }
                                }
                                // 4.1.2.1、获取办件的基本信息拼接JSON
                                projectJson.put("projectname", auditOnlineProject.getProjectname()); // 事项名称
                                projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                                projectJson.put("projectguid", projectGuid); // 办件标识
                                projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 事项标识
                                projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                                projectJson.put("type", auditOnlineProject.getType()); // 类型 0：普通办件 1：套餐
                                projectJson.put("flowsn", auditOnlineProject.getFlowsn()); // 办件编号
                                projectJson.put("projectstatus", ZwdtConstant
                                        .getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus()))); // 办件状态
                                projectJson.put("ouname", auditOnlineProject.getOuname()); // 办件受理部门
                                projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT)); // 保存时间
                                projectJson.put("applydate", EpointDateUtil.convertDate2String(
                                        auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT)); // 办件申请时间
                                projectJson.put("applyertype", auditOnlineProject.getApplyertype()); // 办件申请人类型
                                projectJson.put("taskcaseguid", auditOnlineProject.getTaskcaseguid()); // 办件多情形标识
                                // 4.1.2.2、获取办件办结时间
                                String banJieData = "";
                                if (auditOnlineProject.getBanjiedate() != null) {
                                    banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                            EpointDateUtil.DATE_FORMAT);
                                }
                                projectJson.put("banjiedate", banJieData);
                                // 4.1.3、获取办件评价内容
                                String evaluate = "无评价";
                                AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                        .selectEvaluatByClientIdentifier(auditOnlineProject.getSourceguid())
                                        .getResult();
                                if (auditOnlineEvaluat != null
                                        && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                    evaluate = iCodeItemsService.getItemTextByCodeName("好差评满意度层级",
                                            auditOnlineEvaluat.getSatisfied());
                                }
                                projectJson.put("evaluate", evaluate);
                                // 4.1.4、获取办件剩余时间
                                String spareTime = "";
                                AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                        .getSparetimeByProjectGuid(projectGuid).getResult();
                                if (auditProjectSparetime != null) {
                                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                        spareTime = "即办件";
                                    }
                                    else {
                                        int spareMinutes = auditProjectSparetime.getSpareminutes();
                                        if (spareMinutes >= 0) {
                                            spareTime = "距承诺办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                        else {
                                            spareTime = "已超过办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                        }
                                    }
                                }
                                else {
                                    if (ZwfwConstant.ITEMTYPE_JBJ.equals(taskType)) {
                                        spareTime = "即办件";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                        spareTime = "待预审";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTG) {
                                        spareTime = "预审通过";
                                    }
                                    else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                        spareTime = "已接件";
                                    }
                                    else if ((projectStatus == ZwfwConstant.BANJIAN_STATUS_SPTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_SPBTG)
                                            || (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ)) {
                                        int spendMinute = 0;
                                        if (StringUtil.isNotBlank(auditOnlineProject.getSpendtime())) {
                                        	spendMinute = auditOnlineProject.getSpendtime();
                                        }
                                        spareTime = "办理用时：" + CommonUtil.getSpareTimes(spendMinute);
                                    }
                                    else {
                                        spareTime = "距承诺办结期限：--个工作日";
                                    }
                                }
                                projectJson.put("sparetime", spareTime);
                                // 4.1.5、获取办件状态
                                if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                    // 4.1.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                    projectJson.put("currentstatus", "待提交");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                    // 4.1.5.2、外网申报预审退回（预审退回状态）
                                    projectJson.put("currentstatus", "预审退回");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                                    // 4.1.5.2.1、获取预审退回原因
//                                    SqlConditionUtil opSqlConditionUtil = new SqlConditionUtil();
//                                    opSqlConditionUtil.eq("projectGuid", projectGuid);
//                                    opSqlConditionUtil.eq("operatetype", ZwfwConstant.OPERATE_YSDH);
//                                    AuditProjectOperation auditProjectOperation = iAuditProjectOperation
//                                            .getAuditOperationByCondition(opSqlConditionUtil.getMap()).getResult();
                                    projectJson.put("remarks",StringUtil.isBlank(auditOnlineProject.getBackreason()) ? "" : auditOnlineProject.getBackreason());
                                }
                                else if ("200".equals(String.valueOf(projectStatus))) {
                                    projectJson.put("currentstatus", "撤销办件");
                                    projectJson.put("currentstatusredtitle", "<span class='red'>【撤销办件】</span>");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                    // 4.1.5.3、待补办、受理后待补办（待补正状态）
                                    projectJson.put("currentstatus", "待补正");
                                    String buZhengText = "";// 补正材料名称
                                    int buZhengCount = 0; // 补正材料数量
                                    SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();// 补正材料检索条件
                                    materialSqlConditionUtil.eq("projectguid", projectGuid);
                                    materialSqlConditionUtil.in("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                                    List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                            .selectProjectMaterialByCondition(materialSqlConditionUtil.getMap())
                                            .getResult();
                                    for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                                        buZhengText += auditProjectMaterial.getTaskmaterial() + "、";
                                        buZhengCount++;
                                    }
                                    if (StringUtil.isNotBlank(buZhengText)) {
                                        buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                                + buZhengCount + "份材料）";
                                    }
                                    projectJson.put("buzhengtext",
                                            StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);// 补正材料
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                    // 4.1.5.4、正常办结且未评价（待评价状态）
                                    projectJson.put("currentstatus", "待评价");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                    // 4.1.5.5、正常办结且已评价（待评价状态）
                                    projectJson.put("currentstatus", "已办结");
                                }
                                else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_BYSL
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_CXSQ
                                        || projectStatus == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
                                    // 4.1.5.6、不予受理、撤销申请、异常终止（其他状态）
                                    projectJson.put("currentstatus", "其他");
                                }
                                else {
                                    // 4.1.5.7、除去之前列出的状态外的办件（审批中状态）
                                    projectJson.put("currentstatus", "审批中");
                                }
                                projectJsonList.add(projectJson);
                            }
                        }
                        else if (ZwdtConstant.ONLINEPROJECTTYPE_BUSINESS.equals(auditOnlineProject.getType())) {
                            // 4.2、如果是套餐，返回套餐的JSON数据
                            String biGuid = auditOnlineProject.getSourceguid();
                            int projectStatus = Integer.parseInt(auditOnlineProject.getStatus());
                            int isEvaluat = auditOnlineProject.getIs_evaluat(); // 是否已评价
                            JSONObject projectJson = new JSONObject();
                            // 4.2.1、获取套餐的基本信息拼接JSON
                            projectJson.put("applyername", auditOnlineProject.getApplyername());// 申请人
                            projectJson.put("projectname", auditOnlineProject.getProjectname());// 主题名称
                            projectJson.put("flowsn", auditOnlineProject.getFlowsn());// 套餐申报编号
                            projectJson.put("biguid", biGuid); // 主题实例标识
                            projectJson.put("projectguid", biGuid); // 主题实例标识
                            projectJson.put("taskguid", auditOnlineProject.getTaskguid()); // 主题标识
                            projectJson.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                            projectJson.put("type", auditOnlineProject.getType()); // 类型 0：普通办件 1：套餐
                            projectJson.put("projectstatus",
                                    ZwdtConstant.getBanjanStatusKey(String.valueOf(auditOnlineProject.getStatus())));// 办件状态
                            projectJson.put("savedate", EpointDateUtil.convertDate2String(
                                    auditOnlineProject.getOperatedate(), EpointDateUtil.DATE_FORMAT));// 保存时间
                            projectJson.put("applydate", EpointDateUtil
                                    .convertDate2String(auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT));// 办件申请时间
                            // 4.2.2、获取套餐办结时间
                            String banJieData = "";
                            int days = 0;
                            if (auditOnlineProject.getBanjiedate() != null) {
                                banJieData = EpointDateUtil.convertDate2String(auditOnlineProject.getBanjiedate(),
                                        EpointDateUtil.DATE_FORMAT);
                                 days = (int) ((new Date().getTime() - auditOnlineProject.getBanjiedate().getTime()) / (1000*3600*24));
                            }
                            projectJson.put("banjiedate", banJieData);
                            // 4.2.3、获取套餐评价内容
                            String evaluate = "无评价";
                            AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                    .selectEvaluatByClientIdentifier(biGuid).getResult();
                            if (auditOnlineEvaluat != null
                                    && StringUtil.isNotBlank(auditOnlineEvaluat.getSatisfied())) {
                                evaluate = iCodeItemsService.getItemTextByCodeName("好差评满意度层级",
                                        auditOnlineEvaluat.getSatisfied());
                            }
                           
                            if (days > 5) {
                                evaluate = "满意";
                            }
                            projectJson.put("evaluate", evaluate);
                            // 4.2.4、获取业务标识、子申报标识、阶段标识
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                            projectJson.put("yewuguid", auditSpInstance == null ? "" : auditSpInstance.getYewuguid());
                            List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                    .getResult();
                            String subAppGuid = "";
                            if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
                                AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                                subAppGuid = auditSpISubapp.getRowguid();
                                projectJson.put("subappguid", auditSpISubapp.getRowguid());
                                projectJson.put("phaseguid", auditSpISubapp.getPhaseguid());
                            }
                            // 4.2.5、获取套餐状态
                            if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWWTJ
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
                                // 4.2.5.1、外网申报未提交、外网申报初始化（待提交状态）
                                projectJson.put("currentstatus", "待提交");
                                projectJson.put("sparetime", "待提交");
                                projectJson.put("currentstatusredtitle", "<span class='red'>【待提交】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
                                // 4.2.5.2、预审退回（预审退回状态）
                                projectJson.put("currentstatus", "预审退回");
                                projectJson.put("remarks", auditOnlineProject.getBackreason());// 套餐退回原因
                                projectJson.put("currentstatusredtitle", "<span class='red'>【预审退回】</span>");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_DBB
                                    || projectStatus == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                                // 4.2.5.3、待补办、受理后待补办（预审退回状态）
                                projectJson.put("currentstatus", "待补正");
                                projectJson.put("sparetime", "待补正");

                                String buZhengText = "";// 补正材料名称
                                int buZhengCount = 0;// 补正材料数量
                                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                                        .getSpIPatchMaterialBySubappGuid(subAppGuid).getResult();
                                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                                    buZhengText += auditSpIMaterial.getMaterialname() + "、";
                                    buZhengCount++;
                                }
                                if (StringUtil.isNotBlank(buZhengText)) {
                                    buZhengText = buZhengText.substring(0, buZhengText.length() - 1) + "（共"
                                            + buZhengCount + "份材料）";
                                }
                                projectJson.put("buzhengtext",
                                        StringUtil.isBlank(buZhengText) ? "材料已补正成功，请等待审核！" : buZhengText);
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 0) {
                                // 4.2.5.4、正常办结且未评价（待评价状态）
                                projectJson.put("currentstatus", "待评价");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ && isEvaluat == 1) {
                                // 4.2.5.5、正常办结且已评价（已办结状态）
                                projectJson.put("currentstatus", "已办结");
                                projectJson.put("sparetime", "已办结");
                            }
                            else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                //4.2.5.6、待预审（外网申报已提交状态）
                                projectJson.put("currentstatus", "待预审");
                                projectJson.put("sparetime", "待预审");
                            }
                            else {
                                // 4.2.5.7、除去之前列出的状态外的办件（已办结状态）
                                projectJson.put("currentstatus", "审批中");
                                projectJson.put("sparetime", "审批中");
                            }
                            projectJsonList.add(projectJson);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("projectlist", projectJsonList);
                    dataJson.put("compamyid", compamyId);
                    dataJson.put("totalcount", totalCount);
                    dataJson.put("recentcount", recentcount);// 近期办件（目前默认显示近一个月）
                    log.info("=======结束调用getMyCompanyProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取我的企业办件成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyCompanyProject异常信息：" + e.getMessage() + "=======");
            log.info("=======getMyCompanyProject接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的企业办件异常：" + e.getMessage(), "");
        }
    }

    /**
     * 申报办件获取用户所在企业列表接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getUserCompanyList", method = RequestMethod.POST)
    public String getCompanyList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getUserCompanyList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 3、根据用户guid获取法人信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("orgalegal_idnumber", auditOnlineRegister.getIdnumber());
                    sqlConditionUtil.isBlankOrValue("is_history", "0");
                    sqlConditionUtil.eq("isactivated", "1");
                    // 3.1、根据条件查询出法人信息列表
                    List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfoList = iAuditRsCompanyBaseinfo
                            .selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();
                    int companyCount = 0;
                    // 3.1.1、添加默认选择列，请选择
                    List<JSONObject> companyJsonList = new ArrayList<JSONObject>();
                    JSONObject exampleJson = new JSONObject();
                    exampleJson.put("companyguid", "");
                    exampleJson.put("companyname", "请选择");
                    companyJsonList.add(exampleJson);
                    // 3.2、列表加入是法人的公司列表
                    for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfoList) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("companyid", auditRsCompanyBaseinfo.getCompanyid());
                        dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                        dataJson.put("companyguid", auditRsCompanyBaseinfo.getRowguid());
                        companyJsonList.add(dataJson);
                        companyCount += 1;
                    }
                    // 3.3、列表加入是管理者代办人的公司列表
                    SqlConditionUtil agentconditionUtil = new SqlConditionUtil();
                    agentconditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
                    agentconditionUtil.eq("bsqidnum", auditOnlineRegister.getIdnumber());
                    agentconditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权： 0-否，1-是
                    agentconditionUtil.eq("m_isactive", "1"); // 是否已激活： 1 已激活
                    List<AuditOnlineCompanyGrant> auditOnlineCompanyGrants = iAuditOnlineCompanyGrant
                            .selectCompanyGrantByConditionMap(agentconditionUtil.getMap()).getResult();
                    for (AuditOnlineCompanyGrant auditOnlineCompanyGrant : auditOnlineCompanyGrants) {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("companyid", auditOnlineCompanyGrant.getCompanyid());
                        // 获取企业名称
                        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                .getCompanyByCompanyId(auditOnlineCompanyGrant.getCompanyid()).getResult();
                        dataJson.put("companyname", auditRsCompanyBaseinfo.getOrganname());
                        dataJson.put("companyguid", auditRsCompanyBaseinfo.getRowguid());
                        companyJsonList.add(dataJson);
                        companyCount += 1;
                    }
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("companycount", companyCount);
                    dataJson.put("companylist", companyJsonList);
                    log.info("=======结束调用getUserCompanyList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办用户所在企业列表成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getUserCompanyList接口参数：params【" + params + "】=======");
            log.info("=======getUserCompanyList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户所在企业列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取我的企业所属证照信息接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/getMyCompanyCertInfo", method = RequestMethod.POST)
    public String getMyCompanyCertInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMyCompanyCertInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String companyId = obj.getString("companyid");
                // 2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                            .getCompanyByCompanyId(companyId).getResult();
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 证照类型：1 统一社会信用代码（默认） 2 组织机构代码证
                    String certtype = "";
                    String centnum = "";
                    // 4、获取统一信用代码，如果不为空则返回，如果为空返回企业组织机构代码
                    if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                        centnum = auditRsCompanyBaseinfo.getCreditcode();
                        certtype = ZwfwConstant.CERT_TYPE_TYSHXYDM;
                    }
                    else {
                        // 4.1、返回组织机构代码
                        if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                            certtype = ZwfwConstant.CERT_TYPE_ZZJGDMZ;
                            centnum = auditRsCompanyBaseinfo.getOrgancode();
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "获取企业信息异常！", "");
                        }
                    }
                    // 5 、企业证照列表
                    List<JSONObject> companycertlist = new ArrayList<JSONObject>();
                    dataJson.put("certtype", certtype);
                    dataJson.put("centnum", centnum);
                    
                    dataJson.put("organlegal",
                            auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getOrganlegal());
                    dataJson.put("address",
                            auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getRegisteraddress());
                    dataJson.put("companyrowguid",
                            auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getRowguid());
                    dataJson.put("orgalegalidnumber",
                            auditRsCompanyBaseinfo == null ? "" : auditRsCompanyBaseinfo.getOrgalegal_idnumber());
                    dataJson.put("companycertlist", companycertlist);
                    log.info("=======结束调用getMyCompanyCertInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办用户所在企业列表成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMyCompanyCertInfo接口参数：params【" + params + "】=======");
            log.info("=======getMyCompanyCertInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取我的企业所属证照信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件材料信息的接口（继续申报至办件申报页面调用、办件详细页面调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getInitMaterialList", method = RequestMethod.POST)
    public String getInitMaterialList(@RequestBody String params) {
        try {
            log.info("=======开始调用getInitMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取所要获取材料的类型（0:未提交的材料、1：需要补正材料、2:原件提交、空:办件所有材料）
                String type = obj.getString("type");
                // 1.3、获取辖区编码
                // String areaCode = obj.getString("areacode");
                // 1.4、获取事项多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 2、拼接办件材料条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("projectguid", projectGuid);
                if ("0".equals(type)) {
                    // 2.1、材料提交状态：未提交
                    sqlConditionUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_WTJ);
                }
                else if ("1".equals(type)) {
                    // 2.2、材料提交状态：待补正
                    sqlConditionUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                }
                // 3、获取办件的材料
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                        // 3.1、如果事项配置了材料多情形情况，获取该情形下的材料列表
                        List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                                .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                        for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                            if (auditProjectMaterial.getTaskmaterialguid()
                                    .equals(auditTaskMaterialCase.getMaterialguid())) {
                                // 3.1.1、获取多情形中配置的材料基本信息
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    JSONObject materialJson = new JSONObject();
                                    // 3.1.2、获取办件材料基本信息
                                    materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());//办件材料标识
                                    materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                    materialJson.put("status", auditProjectMaterial.getStatus()); // 办件材料提交状态
                                    materialJson.put("taskmaterialguid", auditProjectMaterial.getTaskmaterialguid()); // 事项材料标识
                                    materialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());// 材料关联的共享材料标识
                                    materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                    materialJson.put("clientguid", auditProjectMaterial.getCliengguid()); // 办件材料对应的附件标识
                                    int necessity = auditTaskMaterialCase.getNecessity(); // 材料是否必需
                                    if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                        materialJson.put("necessary", "0");
                                    }
                                    else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                        materialJson.put("necessary", "1");
                                    }
                                    // 3.1.3、获取材料提交方式
                                    materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                            String.valueOf(auditTaskMaterial.getSubmittype())));
                                    // 3.1.4、获取材料来源渠道
                                    String materialsource;
                                    if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                        materialsource = "申请人自备";
                                    }
                                    else {
                                        materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                                auditTaskMaterial.getFile_source());
                                    }
                                    materialJson.put("materialsource",
                                            StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource); // 材料来源渠道
                                    // 3.1.5、获取材料空白表格标识
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                        int templateAttachCount = iAttachService
                                                .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                        if (templateAttachCount > 0) {
                                            materialJson.put("templateattachguid",
                                                    auditTaskMaterial.getTemplateattachguid());
                                        }
                                    }
                                    // 3.1.6、获取材料填报示例标识
                                    if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                        int exampleAttachCount = iAttachService
                                                .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                        if (exampleAttachCount > 0) {
                                            materialJson.put("exampleattachguid",
                                                    auditTaskMaterial.getExampleattachguid());
                                        }
                                    }
                                    // 3.1.7、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                                    int count = iAttachService
                                            .getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                                    int showButton = 0; // 按钮显示方式
                                    if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                        // 3.1.7.1、如果没有从证照库引用附件，则为普通附件及填表
                                        if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                            // 3.1.7.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                            showButton = count > 0 ? 4 : 3;
                                        }
                                        else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                            // 3.1.7.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                            showButton = count > 0 ? 1 : 0;
                                        }
                                    }
                                    else {
                                        // 3.1.7.2、如果关联了证照库，有附件则显示已引用证照库，没有则显示未上传
                                        showButton = count > 0 ? 2 : 0;
                                    }
                                    materialJson.put("showbutton", showButton);
                                    materialJsonList.add(materialJson);
                                }
                            }
                        }
                    }
                    else {
                        // 4.2、如果事项配置了材料没有配置多情形情况
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            JSONObject materialJson = new JSONObject();
                            // 4.2.1、获取办件材料基本信息
                            materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());//办件材料标识
                            materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                            materialJson.put("status", auditProjectMaterial.getStatus()); // 办件材料提交状态
                            materialJson.put("taskmaterialguid", auditProjectMaterial.getTaskmaterialguid()); // 事项材料标识
                            materialJson.put("sharematerialiguid", auditTaskMaterial.getSharematerialguid());// 材料关联的共享材料标识
                            materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                            materialJson.put("clientguid", auditProjectMaterial.getCliengguid()); // 办件材料对应的附件标识
                            int necessity = auditTaskMaterial.getNecessity(); // 材料是否必需
                            if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                materialJson.put("necessary", "0");
                            }
                            else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                materialJson.put("necessary", "1");
                            }
                            // 4.2.2、获取材料提交方式
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditTaskMaterial.getSubmittype())));
                            // 4.2.3、获取材料来源渠道
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            }
                            else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource",
                                    StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource); // 材料来源渠道
                            // 4.2.4、获取材料空白表格标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int templateAttachAcount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (templateAttachAcount > 0) {
                                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
                                }
                            }
                            // 4.2.5、获取材料填报示例标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachAcount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachAcount > 0) {
                                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                }
                            }
                            // 4.2.6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                            int count = iAttachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()); // 获取办件材料对应的附件数量
                            int showButton = 0; // 按钮显示方式
                            if (StringUtil.isBlank(auditProjectMaterial.getSharematerialiguid())) {
                                // 4.2.6.1、如果没有从证照库引用附件，则为普通附件及填表
                                if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                    // 4.2.6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                }
                                else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                    // 4.2.6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                            else {
                                // 4.2.6.2、如果关联了证照库，有附件则显示已引用证照库，没有则显示未上传
                                showButton = count > 0 ? 2 : 0;
                            }
                            materialJson.put("showbutton", showButton);
                            materialJsonList.add(materialJson);
                        }
                    }
                }
                // 5、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                        Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                        // 5.1、优先对比材料必要性（必要在前）
                        int comNecessity = necessaryb2.compareTo(necessaryb1);
                        int ret = comNecessity;
                        // 5.2、材料必要性一致的情况下对比排序号（排序号降序排）
                        if (comNecessity == 0) {
                            Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                            Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                            ret = ordernumb2.compareTo(ordernumb1);
                        }
                        return ret;
                    }
                });
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getInitMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getInitMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getInitMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查补正材料是否都提交的接口（补正详细页面提交材料前调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkBZMaterialIsSubmit", method = RequestMethod.POST)
    public String checkBZMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkBZMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料信息
                String materialList = obj.getString("materiallist");
                int noSubmitNum = 0;// 补正的材料没有提交的个数
                // 2、将材料信息转换为弱类型的实体集合
                List<Record> records = JsonUtil.jsonToList(materialList, Record.class);
                for (Record record : records) {
                    // 2.1、获取办件材料的提交状态
                    String status = record.getStr("status");
                    // 2.2、获取事项材料标识
                    String taskMaterialGuid = record.getStr("taskmaterialguid");
                    // 2.3、获取材料信息
                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(taskMaterialGuid).getResult();
                    if (auditTaskMaterial != null) {
                        int isRongque = auditTaskMaterial.getIs_rongque();  // 是否容缺
                        int necessity = auditTaskMaterial.getNecessity();   // 是否必要
                        int intStatus = Integer.parseInt(status);   // 上传状态
                        String paperUnnecrsstity = iConfigService.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
                        paperUnnecrsstity = StringUtil.isBlank(paperUnnecrsstity) ? ZwfwConstant.CONSTANT_STR_ZERO
                                : paperUnnecrsstity;
                        // 2.4、如果材料必要非容缺未提交
                        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)
                                && isRongque != ZwfwConstant.CONSTANT_INT_ONE
                                && intStatus != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                && intStatus != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                            // 2.4.1、如果外网不需要提交纸质材料，需要判断材料提交方式
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(paperUnnecrsstity)) {
                                // 如果材料提交方式是“提交电子文件”，或是“提交电子或纸质文件”：
                                if ("10".equals(auditTaskMaterial.getSubmittype())
                                        || "40".equals(auditTaskMaterial.getSubmittype())) {
                                    noSubmitNum++;
                                }
                            }
                            // 2.4.2、如果外网需要提交纸质材料
                            else {
                                noSubmitNum++;
                            }
                        }
                    }
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkBZMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查补正材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkBZMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkBZMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查补正材料是否都提交失败：" + e.getMessage(), "");
        }
    }
    /**
     * 提交补正材料接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String submitMaterial(@RequestBody String params) {
        try {
            log.info("=======开始调用submitMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取提交材料列表信息
                String materialList = obj.getString("materiallist");
                List<Record> records = JsonUtil.jsonToList(materialList, Record.class);
                for (Record record : records) {
                    String projectMaterialGuid = record.getStr("projectmaterialguid"); // 办件材料标识
                    // 2、更新办件材料审核状态为已补正
                    iAuditProjectMaterial.updateProjectMaterialAuditStatus(projectMaterialGuid,
                            Integer.parseInt(ZwfwConstant.Material_AuditStatus_YBZ), projectGuid);
                }
                // 3定义返回JSON对象
                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "提交材料成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitMaterial接口参数：params【" + params + "】=======");
            log.info("=======submitMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 云盘文件选择接口（办件申报云盘选择调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/submitMaterialsByCloud", method = RequestMethod.POST)
    public String submitMaterialsByCloud(@RequestBody String params) {
        try {
            log.info("=======开始调用submitMaterialsByCloud接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取云盘中已选文件的标识
                String selectedGuids = obj.getString("curguids");
                // 1.2、获取办件材料对应的附件标识
                String clientGuid = obj.getString("clientguid");
                String[] selectedGuidsArr = null; // 云盘中已选文件标识数组
                int size = 0;
                if (StringUtil.isNotBlank(selectedGuids)) {
                    selectedGuids = selectedGuids.replace("[", "").replace("]", "");
                    selectedGuidsArr = selectedGuids.split(",");
                    size = selectedGuidsArr.length;
                }
                // 2、需要清除原先从云盘选择并且关联到材料的附件
                List<FrameAttachInfo> frameAttachInfolist = iAttachService.getAttachInfoListByGuid(clientGuid);
                for (FrameAttachInfo frameAttachInfo : frameAttachInfolist) {
                    if ("云盘上传".equals(frameAttachInfo.getCliengTag())) {
                        iAttachService.deleteAttach(frameAttachInfo);
                    }
                }
                // 3、将已选择的云盘文件对应的附件关联到办件材料
                for (int i = 0; i < size; i++) {
                    String selectedGuid = selectedGuidsArr[i];
                    selectedGuid = selectedGuid.replaceAll("\"", "");
                    // 3.1、获取已选的云盘文件
                    AuditOnlineCloudFiles auditOnlineCloudFiles = iAuditOnlineCloudFiles
                            .getAuditOnlineCloudFilesByRowguid(selectedGuid);
                    if (auditOnlineCloudFiles != null) {
                        // 3.2、将已选的云盘文件关联到材料附件，需要复制云盘文件对应的附件进行关联（云盘文件clientGuid关联了attachGuid）
                        FrameAttachInfo frameAttachInfoCloudFiles = iAttachService
                                .getAttachInfoDetail(auditOnlineCloudFiles.getClientguid());
                        if (frameAttachInfoCloudFiles != null) {
                            // 3.3、复制云盘文件
                            iAttachService.copyAttachByClientGuid(frameAttachInfoCloudFiles.getCliengGuid(), "云盘上传",
                                    auditOnlineCloudFiles.getClientguid(), clientGuid);
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(clientGuid);
                            // 3.4、更新附件上传时间
                            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                frameAttachInfo.setUploadDateTime(new Date());
                                iAttachService.updateAttach(frameAttachInfo, null);
                            }
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                log.info("=======结束调用submitMaterialsByCloud接口=======");
                return JsonUtils.zwdtRestReturn("1", "提交云盘选中材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitMaterialsByCloud接口参数：params【" + params + "】=======");
            log.info("=======submitMaterialsByCloud异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交云盘选中材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取已提交材料列表接口（办件详细页面调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getSubmitMaterialList", method = RequestMethod.POST)
    public String getSubmitMaterialList(@RequestBody String params) {
        try {
            log.info("=======开始调用getSubmitMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取事项材料情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.3、获取事项标识
                // String taskGuid = obj.getString("taskguid");
                // 2、获取已提交的材料数据（排除未提交状态的办件材料）
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("projectguid", projectGuid);
                sqlConditionUtil.nq("status", String.valueOf(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT));
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    // 2.1、获取事项材料信息
                    AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                            .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                    if (auditTaskMaterial != null) {
                        JSONObject materialJson = new JSONObject();
                        materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid()); // 办件材料标识
                        materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial()); // 办件材料名称
                        int necessary = auditTaskMaterial.getNecessity(); // 是否必要(默认设置为事项材料的必要性)
                        if (StringUtil.isNotBlank(taskCaseGuid)) {
                            // 2.1.1、如果事项材料存在多情形则需要获取多情形中材料的必要性
                            AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                    .selectTaskMaterialCaseByGuid(taskCaseGuid,
                                            auditProjectMaterial.getTaskmaterialguid())
                                    .getResult();
                            necessary = auditTaskMaterialCase.getNecessity();
                        }
                        materialJson.put("necessary",
                                necessary == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES) ? 1 : 0); // 是否必要
                        materialJson.put("ordernumber", auditTaskMaterial.getOrdernum()); // 排序
                        String materialsource;
                        if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                            materialsource = "申请人自备";
                        }
                        else {
                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                    auditTaskMaterial.getFile_source());
                        }
                        materialJson.put("materialsource",
                                StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 材料来源
                        materialJsonList.add(materialJson);
                    }
                }
                // 3、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                        Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                        // 3.1、优先对比材料必要性（必要在前）
                        int comNecessity = necessaryb2.compareTo(necessaryb1);
                        int ret = comNecessity;
                        // 3.2、材料必要性一致的情况下对比排序号（排序号降序排）
                        if (comNecessity == 0) {
                            Integer ordernumb1 = b1.get("ordernumber") == null ? 0 : (Integer) b1.get("ordernumber");
                            Integer ordernumb2 = b2.get("ordernumber") == null ? 0 : (Integer) b2.get("ordernumber");
                            ret = ordernumb2.compareTo(ordernumb1);
                        }
                        return ret;
                    }
                });
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                dataJson.put("materialcount", materialJsonList.size());
                log.info("=======结束调用getSubmitMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取已提交材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSubmitMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getSubmitMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取已提交材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     *  获取办件中所有纸质材料接口（申报告知页面调用）
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getPaperMaterial", method = RequestMethod.POST)
    public String getPaperMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPaperMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                //  String areaCode = obj.getString("areacode");
                // 1.3、获取事项材料多情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 2、如果存在事项材料配置了多情形，则需要获取情形与材料对应关系
                List<AuditTaskMaterialCase> auditTaskMaterialCases = null;
                if (StringUtil.isNotBlank(taskCaseGuid)) {
                    auditTaskMaterialCases = iAuditTaskMaterialCase.selectTaskMaterialCaseByCaseGuid(taskCaseGuid)
                            .getResult();
                }
                // 2、获取办件材料信息
                List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                        .selectProjectMaterial(projectGuid).getResult();
                List<JSONObject> materialJsonlist = new ArrayList<JSONObject>();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    if (StringUtil.isNotBlank(taskCaseGuid)) {
                        // 2.1、如果是配置了多情形的情况，需要展示多情形对应的纸质材料数据
                        for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                            if (auditProjectMaterial.getTaskmaterialguid()
                                    .equals(auditTaskMaterialCase.getMaterialguid())) {
                                // 2.1.2、获取事项材料信息
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    String submitType = auditTaskMaterial.getSubmittype();
                                    // 2.1.3、需要返回的是提交方式包含纸质方式的材料
                                    if (!String.valueOf(WorkflowKeyNames9.SubmitType_Submit).equals(submitType)) {
                                        JSONObject materialJson = new JSONObject();
                                        // 2.1.4、获取办件材料基本信息
                                        materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                        materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                        materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                        materialJson.put("sharematerialiguid",
                                                auditProjectMaterial.getSharematerialiguid()); // 办件材料关联共享材料标识
                                        materialJson.put("status", auditProjectMaterial.getStatus()); // 材料提交状态
                                        materialJson.put("type", auditTaskMaterial.getType()); // 材料类型
                                        materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                                String.valueOf(auditTaskMaterial.getSubmittype()))); // 提交方式
                                        materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                        // 2.1.5、获取材料来源渠道
                                        String materialsource;
                                        if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                            materialsource = "申请人自备";
                                        }
                                        else {
                                            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                                    auditTaskMaterial.getFile_source());
                                        }
                                        materialJson.put("materialsource",
                                                StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 来源渠道
                                        // 2.1.6、获取材料必要性
                                        int necessity = auditTaskMaterialCase.getNecessity();
                                        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                            materialJson.put("necessary", "0");
                                        }
                                        else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                            materialJson.put("necessary", "1");
                                        }
                                        // 2.1.6、获取材料空白表格附件标识
                                        if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                            int templateAttachCount = iAttachService.getAttachCountByClientGuid(
                                                    auditTaskMaterial.getTemplateattachguid());
                                            if (templateAttachCount > 0) {
                                                materialJson.put("templateattachguid",
                                                        auditTaskMaterial.getTemplateattachguid());
                                            }
                                        }
                                        // 2.1.7、获取材料填报示例附件标识
                                        if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                            int exampleAttachCount = iAttachService.getAttachCountByClientGuid(
                                                    auditTaskMaterial.getExampleattachguid());
                                            if (exampleAttachCount > 0) {
                                                materialJson.put("exampleattachguid",
                                                        auditTaskMaterial.getExampleattachguid());
                                            }
                                        }
                                        materialJsonlist.add(materialJson);
                                    }
                                }
                            }
                        }
                    }
                    else {
                        // 2.2、如果是没有配置多情形的情况下，需要展示办件材料中的包含纸质的材料
                        // 2.2.2、获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            String submitType = auditTaskMaterial.getSubmittype();
                            // 2.2.3、需要返回的是提交方式包含纸质方式的材料
                            if (!String.valueOf(WorkflowKeyNames9.SubmitType_Submit).equals(submitType)) {
                                JSONObject materialJson = new JSONObject();
                                // 2.2.4、获取材料基本信息
                                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 办件材料标识
                                materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());// 办件材料名称
                                materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                                materialJson.put("sharematerialiguid", auditProjectMaterial.getSharematerialiguid()); // 办件材料关联共享材料标识
                                materialJson.put("status", auditProjectMaterial.getStatus()); // 材料提交状态
                                materialJson.put("type", auditTaskMaterial.getType()); // 材料类型
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype()))); // 提交方式
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum()); // 事项材料排序
                                // 2.2.5、获取材料来源渠道
                                String materialsource;
                                if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                    materialsource = "申请人自备";
                                }
                                else {
                                    materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource",
                                        StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 来源渠道
                                // 2.2.6、获取材料必要性
                                int necessity = auditTaskMaterial.getNecessity();
                                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                                    materialJson.put("necessary", "0");
                                }
                                else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                                    materialJson.put("necessary", "1");
                                }
                                // 2.2.7、获取材料空白模板附件标识
                                String templateAttachGuid = "";
                                if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                    List<FrameAttachInfo> tempFrameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditTaskMaterial.getTemplateattachguid());
                                    if (tempFrameAttachInfos != null && tempFrameAttachInfos.size() > 0) {
                                        templateAttachGuid = tempFrameAttachInfos.get(0).getAttachGuid();
                                    }
                                }
                                materialJson.put("templateattachguid", templateAttachGuid);
                                // 2.2.8、获取材料填报示例附件标识
                                String exampleAttachGuid = "";
                                if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                    List<FrameAttachInfo> examFrameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditTaskMaterial.getExampleattachguid());
                                    if (examFrameAttachInfos != null && examFrameAttachInfos.size() > 0) {
                                        exampleAttachGuid = examFrameAttachInfos.get(0).getAttachGuid();
                                    }
                                }
                                materialJson.put("exampleattachguid", exampleAttachGuid);
                                materialJsonlist.add(materialJson);
                            }
                        }
                    }
                }
                // 3、对返回的材料列表进行排序
                Collections.sort(materialJsonlist, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject b1, JSONObject b2) {
                        Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                        Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                        // 3.1、优先对比材料必要性（必要在前）
                        int comNecessity = necessaryb2.compareTo(necessaryb1);
                        int ret = comNecessity;
                        // 3.2、材料必要性一致的情况下对比排序号（排序号降序排）
                        if (comNecessity == 0) {
                            Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                            Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                            ret = ordernumb2.compareTo(ordernumb1);
                        }
                        return ret;
                    }
                });
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonlist);
                dataJson.put("projectguid", projectGuid);// 办件标识
                log.info("=======结束调用getPaperMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取纸质材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPaperMaterial接口参数：params【" + params + "】=======");
            log.info("=======getPaperMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取纸质材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 办件公示的接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
    public String getProjectList(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取搜索关键字（办件流水号、申请人姓名）
                String keyWord = obj.getString("keyword");
                // 1.2、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.3、获取每页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.4、获取区域编码
                String areacode = obj.getString("areacode");
                // 2、获取公示需要展示的办件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("areacode", areacode);
                // 2.1、办件状态：外网申报已提交\外网申报预审退回\外网申报预审通过\待接件\已接件\待补办\已受理\审批不通过\审批通过\正常办结\不予受理\撤销申请\异常终止）
                sqlConditionUtil.in("status",
                        ZwdtConstant.BANJIAN_STATUS_WWYTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU + ","
                                + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                                + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_DBB + ","
                                + ZwdtConstant.BANJIAN_STATUS_YSL + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG + ","
                                + ZwdtConstant.BANJIAN_STATUS_SPTG + "," + ZwdtConstant.BANJIAN_STATUS_ZCBJ + ","
                                + ZwdtConstant.BANJIAN_STATUS_BYSL + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + ","
                                + ZwdtConstant.BANJIAN_STATUS_YCZZ);
                // 2.2、查询需要的办件字段
                String fields = "  rowguid,taskguid,projectname,applyername,status,receivedate,ouname,acceptuserdate,flowsn,tasktype,applyway,yushendate,applydate ";
                
                sqlConditionUtil.ge("applydate", "'2021-09-01'");
                
                
                PageData<AuditProject> pageData = iAuditProject.getAuditProjectPageDataByCondition(fields,
                        sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                        Integer.parseInt(pageSize), "applydate", "desc", keyWord).getResult();
                int totalCount = pageData.getRowCount();
                List<AuditProject> auditProjects = pageData.getList();
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                List<JSONObject> projectJsonList = new ArrayList<>();
                for (AuditProject auditProject : auditProjects) {
                    if (auditProject != null) {
                        JSONObject projectJson = new JSONObject();
                        projectJson.put("projectguid", auditProject.getRowguid()); // 办件标识
                        projectJson.put("projectname", auditProject.getProjectname());// 办件名称
                        projectJson.put("flowsn", auditProject.getFlowsn());// 办件流水号
                        projectJson.put("ouname", auditProject.getOuname());// 部门名称
                        projectJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                        projectJson.put("status",
                                ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus())));// 办件状态
                        Integer tasktype = auditProject.getTasktype(); // 取出事项类别 1：即办件，2：承诺件
                        Integer applyway = auditProject.getApplyway(); // 取出申报类型 10：网上申报预审，11：网上直接申报
                        // 3.1、默认取出受理时间
                        String date = StringUtil.isNotBlank(auditProject.getAcceptuserdate()) ? EpointDateUtil
                                .convertDate2String(auditProject.getAcceptuserdate(), EpointDateUtil.DATE_FORMAT) : "--";
                        // 3.2、即办件
                        if(tasktype!= null && applyway != null) {
                        	if (tasktype == Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                        		// 3.2.1、(申报预审),先判断是否有接件时间，没有在判断是否有受理时间，没有取预审时间
                        		if (applyway == Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS)) {
                        			if (auditProject.getStatus() == ZwdtConstant.BANJIAN_STATUS_YJJ) {
                        				date = EpointDateUtil.convertDate2String(auditProject.getYushendate(),
                        						EpointDateUtil.DATE_FORMAT);// 预审时间
                        			}
                        			if (StringUtil.isNotBlank(auditProject.getReceivedate())) {
                        				date = EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
                        						EpointDateUtil.DATE_FORMAT);// 接件时间
                        			}
                        		}
                        		// 3.2.2、(直接受理),判断是否有受理时间，没有取申请时间
                        		else if (applyway == Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {
                        			if ("--".equals(date)) {
                        				date = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                        						EpointDateUtil.DATE_FORMAT);// 申请时间
                        			}
                        		}
                        		// 3.2.3、(窗口登记)
                        		else if (applyway == Integer.parseInt(ZwfwConstant.APPLY_WAY_CKDJ)) {
                        			if (auditProject.getStatus() == ZwdtConstant.BANJIAN_STATUS_YJJ) {
                        				if ("--".equals(date)) {
                        					date = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                        							EpointDateUtil.DATE_FORMAT);// 申请时间
                        				}
                        			}
                        			if (StringUtil.isNotBlank(auditProject.getReceivedate())) {
                        				date = EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
                        						EpointDateUtil.DATE_FORMAT);// 接件时间
                        			}
                        		}
                        	}
                        	// 3.3、 承诺件
                        	else if (tasktype == Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ)) {
                        		// 3.3.1、直接受理
                        		if (applyway == Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB)) {
                        			if ("--".equals(date)) {
                        				date = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                        						EpointDateUtil.DATE_FORMAT);// 申请时间
                        			}
                        		}
                        		if (StringUtil.isNotBlank(auditProject.getReceivedate())) {
                        			date = EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
                        					EpointDateUtil.DATE_FORMAT);// 接件时间
                        		}
                        	}
                        }
                        projectJson.put("receivedate", date);// 接件时间
                        projectJsonList.add(projectJson);
                    }
                }
                dataJson.put("totalcount", totalCount);
                dataJson.put("projectlist", projectJsonList);
                log.info("=======结束调用getProjectList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件公示成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectList异常信息：" + e.getMessage() + "=======");
            log.info("=======getProjectList接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件公示异常：" + e.getMessage(), "");
        }
    }

    /**
     *  办件公示详情的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/getPublicProjectDetail", method = RequestMethod.POST)
    public String getPublicProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPublicProjectDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyername,pviguid,status,ouname,receiveusername,receivedate,flowsn,acceptusername,acceptuserdate ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    // 4、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        dataJson.put("taskname", auditProject.getProjectname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("receivename", auditProject.getOuname());// 收件单位
                        dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位            
                        dataJson.put("flowsn", auditProject.getFlowsn());// 办件编号
                        // 4.1、获取经办人与操作时间（优先取接件时间和接件人，没有的话取受理时间和受理人，否则显示--）
                        String userName = "--";
                        String date = "--";
                        userName = StringUtil.isNotBlank(auditProject.getAcceptusername())
                                ? auditProject.getAcceptusername() : userName;
                        date = StringUtil.isNotBlank(auditProject.getAcceptuserdate()) ? EpointDateUtil
                                .convertDate2String(auditProject.getAcceptuserdate(), EpointDateUtil.DATE_TIME_FORMAT)
                                : date;
                        userName = StringUtil.isNotBlank(auditProject.getReceiveusername())
                                ? auditProject.getReceiveusername() : userName;
                        dataJson.put("receiveusername", userName);// 经办人
                        dataJson.put("receivedate", date);// 接件时间                 
                        dataJson.put("status",
                                ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus())));// 办件状态
                    }
                }
                log.info("=======结束调用getPublicProjectDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPublicProjectDetail异常信息：" + e.getMessage() + "=======");
            log.info("=======getPublicProjectDetail接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 办件详情的接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = "370830";
                // 1.3、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isNotBlank(taskGuid)) {
                    // 2.1、辖区编码以事项所在辖区为准（考虑到站点切换后查看个人申请的办件信息）
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {
                        if (StringUtil.isNotBlank(auditTask.getAreacode())) {
                            areaCode = auditTask.getAreacode();
                        }
                    }
                }
                // 3、获取办件信息
                AuditProject auditProject = iJNAuditProjectRestService.getAuditProjectDetail(projectGuid);
                if (auditProject != null) {
                    int status = auditProject.getStatus(); // 办件状态
                    int isCheck = auditProject.getIs_check() == null ? 0 : auditProject.getIs_check(); // 是否已核价
                    int isFee = auditProject.getIs_fee() == null ? 0 : auditProject.getIs_fee(); // 是否已收费
                    int isEvaluat = auditProject.getIs_evaluat() == null ? 0 : auditProject.getIs_evaluat(); // 办件是否已评价
                    // 4、获取事项基本信息拼接JSON
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
                    // 4.1、获取办件相关基本信息
                    dataJson.put("taskname", auditProject.getProjectname()); // 事项名称
                    dataJson.put("projectguid", auditProject.getRowguid()); // 项目标示
                    dataJson.put("flowsn", auditProject.getFlowsn()); // 办件编号
                    dataJson.put("unid", auditTask.getStr("unid")); // 对应浪潮itemId
                    dataJson.put("itemid", auditTask.getItem_id()); // 事项编码
                    dataJson.put("type", auditTask.getType()); // 办理类型
                    dataJson.put("promiseday", auditTask.getPromise_day());// 承诺时间
                    dataJson.put("applyertype", auditProject.getApplyertype()); // 办件申请人类型
                    dataJson.put("applyway", iCodeItemsService.getItemTextByCodeName("申请方式",
                            String.valueOf(auditProject.getApplyway())));// 申请方式
                    dataJson.put("applydate", EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 申请时间
                    dataJson.put("savedate", EpointDateUtil.convertDate2String(auditProject.getOperatedate(),
                            EpointDateUtil.DATE_TIME_FORMAT));// 保存时间
                    dataJson.put("projectname", auditProject.getProjectname()); // 办件名称
                    dataJson.put("applyername", auditProject.getApplyername()); // 申请人/申请单位
                    dataJson.put("applyercertnum", auditProject.getCertnum()); // 申请人身份证
                    dataJson.put("contactperson", auditProject.getContactperson()); // 联系人
                    dataJson.put("contactmobile", auditProject.getContactmobile()); // 联系人手机
                    dataJson.put("contactphone", auditProject.getContactphone()); // 联系人电话
                    dataJson.put("contactcertnum", auditProject.getContactcertnum()); // 联系人身份证
                    dataJson.put("contactpostcode", auditProject.getContactpostcode()); // 邮编
                    dataJson.put("address", auditProject.getAddress());// 地址
                    dataJson.put("windowname", auditProject.getWindowname());// 办理部门
                    dataJson.put("remark", auditProject.getRemark());// 备注
                    dataJson.put("receivename", auditProject.getOuname());// 收件单位
                    dataJson.put("receiveusername", auditProject.getReceiveusername());// 经办人
                    dataJson.put("email", auditProject.getContactemail());// 邮箱
                    dataJson.put("fax", auditProject.getContactfax());// 传真
                    dataJson.put("legal", auditProject.getLegal()); // 法人代表
                    dataJson.put("orgalegalidnumber", auditProject.getLegalid()); // 法人代表身份证
                    dataJson.put("ispause", auditProject.getIs_pause()); // 是否暂停
                    dataJson.put("ouname", auditProject.getOuname()); // 部门名称
                    dataJson.put("receivedate", EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
                            EpointDateUtil.DATE_TIME_FORMAT)); // 接件时间
                    dataJson.put("status", ZwdtConstant.getBanjanStatusKey(String.valueOf(status)));// 办件状态
                    dataJson.put("projectstatus", ZwdtConstant.getBanjanStatusKey(String.valueOf(status)));// 办件状态
                    dataJson.put("certtype",
                            iCodeItemsService.getItemTextByCodeName("申请人用来唯一标识的证照类型", auditProject.getCerttype())); // 办件证照类型
                    dataJson.put("qrcontent", WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + auditTask.getRowguid()); // 二维码内容
                    dataJson.put("areacode", areaCode); // 辖区编码
                    
                    List<FrameAttachInfo> resultlist = iAttachService.getAttachInfoListByGuid(auditProject.getRowguid());
                    dataJson.put("resultlist", resultlist); // 结果材料清单
                    AuditOnlineProject auditonlineproject = null;
                    // 4.1.1 网上办件表中的declarername字段一起返回
                    if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditProject.getApplyertype().toString())) {
                        AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                                .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                        if (auditrscompanybaseinfo != null) {
                            String applyerUserGuid = auditrscompanybaseinfo.getCompanyid();
                            auditonlineproject = iAuditOnlineProject
                                    .getOnlineProjectByApplyerGuid(auditProject.getRowguid(), applyerUserGuid)
                                    .getResult();
                            if (auditonlineproject != null) {
                            	 // 企业办件申请人姓名
                                dataJson.put("declarername", auditonlineproject.getDeclarername()); // 申报人姓名(企业)
                                dataJson.put("companyid", auditonlineproject.getApplyerguid()); // 申报人姓名(企业)
                            }
                        }
                    }
                    // 4.2、获取事项是否可以网上预约和申报
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTaskExtension != null) {
                        String webApplyType = String.valueOf(auditTaskExtension.getWebapplytype());
                        String onlinehandle = "0"; // 是否在线办理：默认否
                        if (StringUtil.isNotBlank(webApplyType)) {
                            onlinehandle = ZwfwConstant.WEB_APPLY_TYPE_NOT.equals(webApplyType) ? "0" : "1";
                        }
                        dataJson.put("onlinehandle", onlinehandle);
                        dataJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                    }
                    // 4.3、获取事项配置的审批结果
                    AuditTaskResult auditResult = iAuditTaskResult
                            .getAuditResultByTaskGuid(auditProject.getTaskguid(), false).getResult();
                    dataJson.put("resultname", auditResult == null ? "无" : auditResult.getResultname());
                    // 4.4、办件处于收费状态（已核价未收讫）
                    if (isCheck == ZwfwConstant.CONSTANT_INT_ONE && isFee == ZwfwConstant.CONSTANT_INT_ZERO) {
                        dataJson.put("pausereason", "收费中");
                    }
                    // 4.5、获取办件满意度
                    if (status == ZwfwConstant.BANJIAN_STATUS_ZCBJ || isEvaluat == ZwfwConstant.CONSTANT_INT_ONE) {
                        AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat
                                .selectEvaluatByClientIdentifier(projectGuid).getResult();
                        if (auditOnlineEvaluat != null) {
                                String gradeinfo = auditOnlineEvaluat.getStr("hcpevaluate");
                                List<Record> list = new ArrayList<Record>();
                                String[] split = null;
                                if(gradeinfo != null) {
                                    split = gradeinfo.split(",");
                                }
                                if (split != null && split.length > 0) {
                                    for (String info : split) {
                                    	Record record = iSpaceAcceptService.getHcpStati(info);
                                        if (record != null) {
                                            list.add(record);
                                        }
                                    }
                                }
                                dataJson.put("evacontextlist", list);
                        	
                            String satisfied = auditOnlineEvaluat.getSatisfied(); //满意度
                            String evaluateContent = StringUtil.isBlank(auditOnlineEvaluat.getEvaluatecontent()) ? ""
                                    : auditOnlineEvaluat.getEvaluatecontent(); //评价内容
                            dataJson.put("satisfied", iCodeItemsService.getItemTextByCodeName("满意度", satisfied));
                            dataJson.put("evaluatecontent", evaluateContent);
                        }
                    }
                    // 4.6、获取办件暂停原因及暂停的时间
                    int is_pause = auditProject.getIs_pause() == null ? 0 : auditProject.getIs_pause();
                    if (is_pause == ZwfwConstant.CONSTANT_INT_ONE) {
                        AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual
                                .getProjectUnusualByProjectGuidAndType(projectGuid, ZwdtConstant.BANJIANOPERATE_TYPE_ZT)
                                .getResult();
                        if (auditProjectUnusual != null) {
                            dataJson.put("pausereason", auditProjectUnusual.getNote()); // 暂停原因
                        }
                    }
                    // 4.7、获取办件计时
                    String spareTime = "";
                    if (!ZwfwConstant.ITEMTYPE_JBJ.equals(auditTask.getType())) {
                        AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                                .getSparetimeByProjectGuid(projectGuid).getResult();
                        if (auditProjectSparetime != null) {
                            if (ZwfwConstant.ITEMTYPE_JBJ.equals(auditTask.getType())) {
                                spareTime = "即办件";
                            }
                            else {
                                int spareMinutes = auditProjectSparetime.getSpareminutes();
                                if (spareMinutes >= 0) {
                                    spareTime = "距承诺办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                }
                                else {
                                    spareTime = "已超过办结期限：" + CommonUtil.getSpareTimes(spareMinutes);
                                }
                            }
                        }
                        else {
                            if (status == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
                                spareTime = "待预审";
                            }
                            else if (status == ZwfwConstant.BANJIAN_STATUS_WWYSTG) {
                                spareTime = "预审通过";
                            }
                            else if (status == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                spareTime = "已接件";
                            }
                            else if ((status == ZwfwConstant.BANJIAN_STATUS_SPTG)
                                    || (status == ZwfwConstant.BANJIAN_STATUS_SPBTG)) {
                                int spendMinutes = auditProject.getSpendtime();
                                spareTime = "办理用时 : " + CommonUtil.getSpareTimes(spendMinutes);
                            }
                            else {
                                if (ZwfwConstant.ITEMTYPE_JBJ.equals(auditTask.getType())) {
                                    spareTime = "即办件";
                                }
                                else {
                                    spareTime = "距承诺办结期限：--个工作日";
                                }
                            }
                        }
                        dataJson.put("spendtime", spareTime);

                    }
                    // 4.8、获取办件审批结果的附件(提供给微信使用)
                    List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
                    // 4.8.1、办件审批的状态为审批通过或者正常办结需要查看办件的审批结果
                    if (status == ZwfwConstant.BANJIAN_STATUS_SPTG || status == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                        List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(projectGuid);
                        if (frameAttachInfos != null) {
                            for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                                JSONObject attachJson = new JSONObject();
                                FrameAttachStorage frameAttachStorage = iAttachService
                                        .getAttach(frameAttachInfo.getAttachGuid());
                                if (frameAttachStorage != null) {
                                    attachJson.put("attachicon",
                                            WebUtil.getRequestCompleteUrl(request)
                                                    + "/rest/auditattach/readAttach?attachguid="
                                                    + frameAttachStorage.getAttachGuid());// 附件图标
                                    attachJson.put("attachname", frameAttachInfo.getAttachFileName()); // 附件名称
                                    attachJson.put("attachsize",
                                            AttachUtil.convertFileSize(frameAttachInfo.getAttachLength()));// 附件大小
                                    attachJson.put("attachsource", frameAttachInfo.getCliengTag());// 附件来源
                                    attachJson.put("attachguid", frameAttachInfo.getAttachGuid());// 附件唯一标识
                                    attachJsonList.add(attachJson);
                                }
                            }
                        }
                    }
                    dataJson.put("attachlist", attachJsonList);
                    dataJson.put("attachcount", attachJsonList.size());
                    // 4.9、办件材料补正状态 1:补正待审核(无需显示提交按钮) 0:待补正(需要显示提交按钮)
                    String materialstatus = "1"; // 默认无需显示提交按钮(考虑到大厅页面没有关闭，审批系统直接受理情况)
                    if (status == ZwfwConstant.BANJIAN_STATUS_DBB || status == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("projectguid", projectGuid);
                        sqlConditionUtil.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
                        List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
                                .selectProjectMaterialByCondition(sqlConditionUtil.getMap()).getResult();
                        if (auditProjectMaterials != null && auditProjectMaterials.size() > 0) {
                            materialstatus = "0"; // 如果办件材料中存在待补正的材料，则需要显示提交按钮
                        }
                    }
                    dataJson.put("materialstatus", materialstatus);
                    log.info("=======结束调用getProjectDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectDetail接口参数：params【" + params + "】=======");
            log.info("=======getProjectDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 根据办件流水号获取办件信息的接口（APP用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectDetailByFlowSN", method = RequestMethod.POST)
    public String getProjectDetailByFlowSN(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectDetailByFlowSN接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、获取办件的流水号
                String flowsn = obj.getString("flowsn");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 2、获取办件
                SqlConditionUtil conditionMapsql = new SqlConditionUtil();
                JSONObject dataJson = new JSONObject();
                conditionMapsql.eq("areacode", areaCode);
                // 3、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,applydate,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,windowname,ouname,receiveusername,receivedate,is_pause,applyertype,certtype,legal,is_fee,is_check ";
                AuditProject auditProject = iAuditProject.getAuditProjectByFlowsn(fields, flowsn, areaCode).getResult();
                if (auditProject != null) {
                    // 4、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        dataJson.put("taskname", auditProject.getProjectname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                        dataJson.put("receivename", auditProject.getOuname());// 收件单位
                        dataJson.put("flowsn", auditProject.getFlowsn());// 办件编号
                        dataJson.put("receiveusername", auditProject.getReceiveusername());// 经办人
                        dataJson.put("receivedate", EpointDateUtil.convertDate2String(auditProject.getReceivedate(),
                                EpointDateUtil.DATE_TIME_FORMAT));// 接件时间
                        dataJson.put("status",
                                ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus())));// 办件状态
                        log.info("=======结束调用getProjectDetailByFlowSN接口=======");
                        return JsonUtils.zwdtRestReturn("1", "根据办件流水号获取办件信息成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectDetailByFlowSN接口参数：params【" + params + "】=======");
            log.info("=======getProjectDetailByFlowSN异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "根据办件流水号获取办件信息的接口异常：" + e.getMessage(), "");
        }
    }

    /**
     *  评价接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/private/evaluateProject", method = RequestMethod.POST)
    public String evaluateProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用evaluateProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取评价等级
                String evaluateLevel = obj.getString("evaluatelevel");
                // 1.2、获取好差评评价登记
                String hcpevaluate = obj.getString("hcpevaluate");
                // 1.3、获取评价内容
                String evaluateNote = obj.getString("evaluatenote");
                // 1.4、获取申请人类型
                String companyid = obj.getString("companyid");
                // 1.5、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取用户信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 2.1、新增在线评价数据
                    AuditOnlineEvaluat auditOnlineEvaluat = new AuditOnlineEvaluat();
                    auditOnlineEvaluat.setRowguid(UUID.randomUUID().toString());
                    auditOnlineEvaluat.setClientidentifier(projectGuid); // 业务标识
                    auditOnlineEvaluat.setClienttype(ZwdtConstant.EVALUATEOBJECT_BUSINESS); // 评价对象(10：办件、20：事项)
                    auditOnlineEvaluat.setSatisfied(evaluateLevel); // 满意度级别
                    auditOnlineEvaluat.set("taskguid", taskGuid); // 绑定事项ID
                    auditOnlineEvaluat.set("hcpevaluate", hcpevaluate); // 好差评满意度级别
                    auditOnlineEvaluat.setEvaluatecontent(evaluateNote); // 满意度内容
                    auditOnlineEvaluat.setEvaluatedate(new Date()); // 评价时间
                    auditOnlineEvaluat.setEvaluatetype(ZwdtConstant.EVALUATETYPE_ONLINE); // 评价方式(10：网上申报 20：短信 30：评价器)
                    auditOnlineEvaluat.setEvaluateuserguid(auditOnlineRegister.getAccountguid()); // 评价用户标识
                    auditOnlineEvaluat.setEvaluateusername(auditOnlineIndividual.getClientname());// 评价用户名称
                    iAuditOnlineEvaluat.addAuditOnineEvaluat(auditOnlineEvaluat);
                    // 2.2、更新AUDIT_ONLINE_PROJECT为已评价
                    // 获取外网办件表，如果是企业办件，则首先需要获取companyid
                    AuditOnlineProject auditOnlineProject = null;
                    if (StringUtil.isNotBlank(companyid)) {
                        auditOnlineProject = iAuditOnlineProject.getOnlineProjectByApplyerGuid(projectGuid, companyid)
                                .getResult();
                    }
                    else {
                        auditOnlineProject = iAuditOnlineProject
                                .getOnlineProjectByApplyerGuid(projectGuid, auditOnlineIndividual.getApplyerguid())
                                .getResult();
                    }

                    auditOnlineProject.setIs_evaluat(1);
                    iAuditOnlineProject.updateProject(auditOnlineProject);
                    // 2.3、更新AUDIT_PROJECT为已评价
                    AuditProject auditProject = new AuditProject();
                    auditProject.setRowguid(projectGuid);
                    auditProject.setAreacode(auditOnlineProject.getAreacode());
                    auditProject.setIs_evaluat(1);
                    iAuditProject.updateProject(auditProject);
                    // 2、获取事项基本信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {
                     	String Projectno = auditOnlineProject.getFlowsn();
                     	String Projectname = auditOnlineProject.getProjectname();
                    	String Areacode = auditOnlineProject.getAreacode();
                    	String Taskname = auditTask.getTaskname();
                    	String Taskcode = auditTask.getItem_id();
                    	String Tasktype = auditTask.getType()+"";
                    	String Promisetime = auditTask.getPromise_day()+"";
                    	String Prodepart = auditOnlineProject.getOuname();
                    	String orgcode = auditOnlineProject.getOuguid();
                    	String projecttype = auditOnlineProject.getType();
                    	String Userprop = auditOnlineProject.getApplyertype();
                    	String Username = auditOnlineProject.getApplyername();
                    	String Certkey = auditOnlineIndividual.getIdnumber();
                    	String Certkeygov = auditOnlineIndividual.getIdnumber();
                    	String Mobile = auditOnlineRegister.getMobile();
                    	String Acceptdate = EpointDateUtil.convertDate2String(auditOnlineProject.getApplydate(), "yyyy-MM-dd HH:mm:ss");
                    	int status = Integer.parseInt(auditOnlineProject.getStatus());
                    	if (status < 30) {
                    		status = 1;
                    	}else if (status >= 30 && status< 90) {
                    		status = 2;
                    	}else if (status >= 90) {
                    		status = 3;
                    	}else {
                    		status = 1;
                    	}
                    		JSONObject json = new JSONObject();
                            json.put("taskCode", Taskcode);
                            json.put("areaCode", Areacode);
                            json.put("taskName", Taskname);
                            json.put("projectNo", Projectno);
                            json.put("proStatus", status);
                            json.put("orgcode", orgcode);
                            json.put("orgName", Prodepart);
                            json.put("acceptDate", Acceptdate);
                            json.put("userProp", Userprop);
                            json.put("userName", Username);
                            json.put("userPageType", "111");
                            json.put("certKey", Certkey);
                            json.put("certKeyGOV", Certkeygov);
                            json.put("serviceName", "线上评价推送办件数据");
                            json.put("serviceNumber", "1");
                            json.put("serviceTime", Acceptdate);
                            json.put("projectType", projecttype);
                            if (3 == status) {
                            json.put("resultDate", Acceptdate);
                            }
                            json.put("tasktype", Tasktype);
                            json.put("mobile", Mobile);
                            json.put("deptCode", orgcode);
                            json.put("projectName", Projectname);
                            json.put("creditNum", Certkey);
                            //默认证照类型为身份证
                            json.put("creditType", "111");
                            json.put("promiseDay", Promisetime);
                            //默认办结时间单位为工作日
                            json.put("anticipateDay", "1");
                            //线上评价为1
                            json.put("proChannel", "1");
                            json.put("promiseTime", Tasktype);
                            
                            log.info("办件数据加密前入参：" + json.toString());
                            JSONObject submit = new JSONObject();
                            submit.put("params", json);
                            String  resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
                            if (StringUtil.isNotBlank(resultsign)) {
                            	JSONObject result = JSONObject.parseObject(resultsign);
                            	if ("success".equals(result.getString("custom"))) {
                            		JSONObject json1 = new JSONObject();
                            		json1.put("projectno", Projectno);
                            		json1.put("pf", "1");
                            		json1.put("satisfaction", evaluateLevel);
                            		json1.put("assessNumber", "1");
                            		json1.put("evalDetail", hcpevaluate);
                            		json1.put("evaluateLevel", evaluateLevel);
                            		json1.put("Taskname", Taskname);
                            		json1.put("Taskcode", Taskcode);
                            		json1.put("Promisetime", Promisetime);
                            		json1.put("Deptcode", orgcode);
                            		json1.put("Prodepart", Prodepart);
                            		json1.put("Username", Username);
                            		json1.put("Applicant", Username);
                            		json1.put("Certkey", Certkey);
                            		json1.put("Certkeygov", Certkeygov);
                            		json1.put("Mobile", Mobile);
                            		json1.put("Acceptdate", Acceptdate);
                            		json1.put("writingEvaluation", evaluateNote);
                            		
                            		 JSONObject submit1 = new JSONObject();
                            		 submit1.put("params", json);
                                     String  instanceresult = TARequestUtil.sendPostInner(HCPINSTANCE, submit1.toJSONString(), "", "");
                                     if (StringUtil.isNotBlank(instanceresult)) {
                                    	 JSONObject results = JSONObject.parseObject(instanceresult);
                                     	if ("success".equals(results.getString("custom"))) {
                                     		 return JsonUtils.zwdtRestReturn("1", "推送省好差评成功", "");
                                     	}else {
                                     		 return JsonUtils.zwdtRestReturn("0", "推送省好差评失败", "");
                                     	}
                                     }else {
                                    	 return JsonUtils.zwdtRestReturn("0", "推送省好差评失败", "");
                                     }
                            	}else {
                            		 return JsonUtils.zwdtRestReturn("0", "保存办件服务评价失败", "");
                            	}
                            }else {
                            	 return JsonUtils.zwdtRestReturn("0", "保存办件服务失败", "");
                            }
                            
                    }
                    log.info("=======结束调用evaluateProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "评价成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======evaluateProject接口参数：params【" + params + "】=======");
            log.info("=======evaluateProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "评价失败：" + e.getMessage(), "");
        }
    }

    
    /**
     *  评价接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return 
     */
    @RequestMapping(value = "/private/evaluateProjectlist1", method = RequestMethod.POST)
    public String evaluateProjectlist1(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用evaluateProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String time = obj.getString("time");
                List<Record> noevaluates = iJnProjectService.getNoEvaluteProject(time);
                if (noevaluates != null && noevaluates.size() > 0) {
                	for (Record record : noevaluates ) {
                		 String projectGuid = record.getStr("rowguid");
                		 String taskguid = record.getStr("taskguid");
                		 AuditTask audittask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                		 if (audittask != null ) {
                				 String areacode = record.getStr("areacode");
                       		  // 1.2、获取评价等级
                                String evaluateLevel = "5";
                                // 1.2、获取好差评评价登记
                                String hcpevaluate = "512,513";
                                // 1.3、获取评价内容
                                String evaluateNote = "";
                                AuditProject project = null;
                                	project = iAuditProject.getAuditProjectByRowGuid(projectGuid, areacode).getResult();
                                	if (project != null) {
                                       	 Evainstance evainstance = new Evainstance();
                                            evainstance.setRowguid(UUID.randomUUID().toString());
                                            evainstance.setCreatedate(new Date());
                                            evainstance.setOperatedate(new Date());
                                            evainstance.setFlag("I");
                                            evainstance.setAppstatus(0);
                                            evainstance.setDatasource("165");
                                            evainstance.setAssessnumber(1);
                                            evainstance.set("isdefault", "0");
                                            evainstance.setEffectivEvalua("1");
                                            evainstance.setProjectno(project.getFlowsn());
                                            evainstance.setAreacode(project.getAreacode());
                                            evainstance.setPf("1");
                                            evainstance.setProstatus("3");
                                            evainstance.setSatisfaction(evaluateLevel);
                                            evainstance.setEvalevel(evaluateLevel);
                                            evainstance.setEvaldetail(hcpevaluate);
                                            evainstance.setEvacontant(evaluateNote);
                                            evainstance.setWritingevaluation(evaluateNote);
                                            evainstance.setTaskname(audittask.getTaskname());
                                            evainstance.setTaskcode(audittask.getItem_id());
                                            evainstance.setTaskhandleitem(audittask.getItem_id());
                                            evainstance.setSubmatter("");
                                            evainstance.setPromisetime(audittask.getPromise_day());
                                            evainstance.setDeptcode(audittask.getOuguid());
                                            evainstance.setProdepart(project.getOuname());
                                            evainstance.setUserprop(project.getApplyertype()+"");
                                            evainstance.setUsername(project.getApplyername());
                                            evainstance.setApplicant(project.getApplyername());
                                            evainstance.setAcceptdate(EpointDateUtil.convertDate2String(project.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                                            evainstance.set("sync_sign","0");
                                            evainstance.set("answerStatus", "0");
                                            iAuditHcpShow.addEvainstance(evainstance);
                			 }
                             }
                	}
                }
              
                    log.info("=======结束调用evaluateProject接口=======");
                    return JsonUtils.zwdtRestReturn("1", "评价成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======evaluateProject接口参数：params【" + params + "】=======");
            log.info("=======evaluateProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "评价失败：" + e.getMessage(), "");
        }
    }
    
    
    /**
     * 获取办件各个状态具体数量以及申请总量（手机端用）
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/private/getProjectCountList", method = RequestMethod.POST)
    public String getappcountlist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getprojectcountlist接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String areacode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 返回手机端中各个统计数量
                AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                        .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                // 3、拼接我的办件的检索条件
                SqlConditionUtil conditionsqlall = new SqlConditionUtil();
                SqlConditionUtil conditionsqldtj = new SqlConditionUtil();
                SqlConditionUtil conditionsqldbz = new SqlConditionUtil();
                SqlConditionUtil conditionsqldjf = new SqlConditionUtil();
                SqlConditionUtil conditionsqlspz = new SqlConditionUtil();
                SqlConditionUtil conditionsqldpj = new SqlConditionUtil();
                SqlConditionUtil conditionsqlybj = new SqlConditionUtil();
                SqlConditionUtil conditionsqlqt = new SqlConditionUtil();
                String applyerguid = auditOnlineIndividual.getApplyerguid();
                // 3.2、拼接办件状态
                SqlConditionUtil conditionAll = handleProjectSqlCondition(conditionsqlall, 0, areacode, applyerguid);
                SqlConditionUtil conditionDtj = handleProjectSqlCondition(conditionsqldtj, 1, areacode, applyerguid);
                SqlConditionUtil conditionDbz = handleProjectSqlCondition(conditionsqldbz, 2, areacode, applyerguid);
                SqlConditionUtil conditionDjf = handleProjectSqlCondition(conditionsqldjf, 4, areacode, applyerguid);
                SqlConditionUtil conditionSpz = handleProjectSqlCondition(conditionsqlspz, 5, areacode, applyerguid);
                SqlConditionUtil conditionDpj = handleProjectSqlCondition(conditionsqldpj, 7, areacode, applyerguid);
                SqlConditionUtil conditionYbj = handleProjectSqlCondition(conditionsqlybj, 8, areacode, applyerguid);
                SqlConditionUtil conditionQt = handleProjectSqlCondition(conditionsqlqt, 9, areacode, applyerguid);
                // 查询办件各个状态数量
                int allProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionAll.getMap()).getResult();
                dataJson.put("allprojectcount", allProjectCount);
                int dtjProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDtj.getMap()).getResult();
                dataJson.put("dtjprojectcount", dtjProjectCount);
                int dbzProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDbz.getMap()).getResult();
                dataJson.put("dbzprojectcount", dbzProjectCount);
                int djfProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDjf.getMap()).getResult();
                dataJson.put("djfprojectcount", djfProjectCount);
                int spzProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionSpz.getMap()).getResult();
                dataJson.put("spzprojectcount", spzProjectCount);
                int dpjProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionDpj.getMap()).getResult();
                dataJson.put("dpjprojectcount", dpjProjectCount);
                int ybjProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionYbj.getMap()).getResult();
                dataJson.put("ybjprojectcount", ybjProjectCount);
                int qtProjectCount = iAuditOnlineProject.getAllOnlineProjectCount(conditionQt.getMap()).getResult();
                dataJson.put("qtprojectcount", qtProjectCount);
                log.info("=======结束调用getprojectcountlist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取统计数量成功", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "获取统计数量失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getprojectcountlist接口参数：params【" + params + "】=======");
            log.info("=======getprojectcountlist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取统计数量失败：" + e.getMessage(), "");
        }
    }

    private SqlConditionUtil handleProjectSqlCondition(SqlConditionUtil conditionsql, int intstatus, String areacode,
            String applyerguid) {
        conditionsql.eq("APPLYERGUID", applyerguid);
        // 3.1、拼接通用的申请人、区域
        if (StringUtil.isNotBlank(areacode)) {
            conditionsql.eq("AREACODE", areacode);
        }
        switch (intstatus) {
            case 0:// 我的申请全部办件信息 TODO
                conditionsql.in("status", "12,16,24,26,30,40,50");
                break;
            case 1:// 待提交(外网申报未提交、预审不通过)
                conditionsql.in("status", ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU);
                break;
            case 2:// 待补正(待补办)
                conditionsql.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_DBB));
                break;
            case 3:// 待原件提交 TODO
                conditionsql.eq("status", String.valueOf(100));// 暂时不显示
                break;
            case 4:// 待缴费(需要收费、已核价未收讫)
                conditionsql.eq("is_charge", "1"); // 办件需要缴费
                conditionsql.eq("is_check", "1"); // 办件已核价
                conditionsql.eq("is_fee", "0"); // 办件未收讫
                break;
            case 5:// 审批中(外网申报已提交、外网申报预审通过、待接件、已接件、已受理、审批不通过、审批通过)
                conditionsql.in("status", "12,16,24,26,30,40,50");
                break;
            case 6:// 待签收 TODO
                break;
            case 7:// 待评价(正常办结、未评价)
                conditionsql.eq("is_evaluat", "0");
                conditionsql.eq("status", "90");
                break;
            case 8:// 已办结(正常办结、已评价)
                conditionsql.eq("status", "90");
                break;
            case 9:// 其他(不予受理、撤销申请、异常终止)
                conditionsql.in("status", "97,98,99");
                break;
            default:
                break;
        }
        return conditionsql;
    }

    /**
     * 获取办件流转信息接口(网厅小圆球展示)
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectFlowlist", method = RequestMethod.POST)
    public String getProjectFlowlist(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectFlowlist接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                //2、获取传入参数值
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String projectGuid = obj.getString("projectguid");
                String areaCode = obj.getString("areacode");
                //3、根据办件GUID获取办件的一些基本信息，是否收费等
                AuditProject auditProject = iJNAuditProjectRestService.getAuditProjectDetail(projectGuid);
                if (auditProject != null) {
                    // 4、获取该办件所对应的事项信息，是否收费、何时收费。
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    int chargeFlag = auditTask.getCharge_flag() == null ? 0 : auditTask.getCharge_flag();
                    int chargeWhen = auditProject.getCharge_when() == null ? 0 : auditProject.getCharge_when();
                    // 5、下面的六个对象对应了详情页面上的六个圆球
                    JSONObject wssqJson = new JSONObject(); // 网上申请（从外网提交到接件前）
                    JSONObject clbzJson = new JSONObject(); // 材料补正
                    JSONObject slJson = new JSONObject();// 受理 
                    JSONObject blJson = new JSONObject();// 办理 步骤是从受理到批准中间的步骤都属于办理
                    JSONObject bjJson = new JSONObject();// 办结，评价后属于办结
                    JSONObject sfJson = new JSONObject();// 收费
                    // 6、默认初始化信息，除了网上申请，默认都是未办理
                    // 网上申请
                    wssqJson.put("nodename", "网上申请");
                    // 网上申请圆球设置为正在处理状态 0:已完成 1：正在处理 2：未处理 （各个状态同下）
                    wssqJson.put("nodestatus", "1");// 
                    wssqJson.put("order", 1);
                    //补正
                    clbzJson.put("nodename", "材料补正");
                    clbzJson.put("nodestatus", "2");
                    // 受理、最大状态为补正后属于受理中，预审通过也属于受理中
                    slJson.put("nodename", "受理");
                    slJson.put("nodestatus", "2");
                    // 办理、 受理完毕后批准前属于办理
                    blJson.put("nodename", "办理");
                    blJson.put("nodestatus", "2");
                    // 办结、 评价完毕以后属于办结
                    bjJson.put("nodename", "办结");
                    bjJson.put("nodestatus", "2");
                    // 收费、 收费分受理前收费和办结前收费
                    sfJson.put("nodename", "收费");
                    sfJson.put("nodestatus", "2");
                    // 7、获取办件操作日志来设置圆球状态
                    List<AuditProjectOperation> auditProjectOperations = new ArrayList<AuditProjectOperation>();
                    auditProjectOperations = iAuditProjectOperation.getOperationListForDT(projectGuid).getResult();
                    Boolean isHasSF = false; //查看是否已收费
                    // 补正的显示有以下几种情况，0:不显示补正,1：存在补正，2：受理前收费情况下，先补正后收费，3：受理前收费情况下，先收费再补正
                    int bzDisplay = 0;
                    // 8、返回的操作日志按时间顺序排序输出，只筛选指定的节点步骤，每输出一次，假定当前输出步骤为最后步骤，覆盖前一步骤圆球状态
                    List<JSONObject> nodelist = new ArrayList<JSONObject>();
                    for (int i = 0; i < auditProjectOperations.size(); i++) {
                        String operateType = auditProjectOperations.get(i).getOperateType();
                        switch (operateType) {
                            // 8.1、接件节点
                            case ZwfwConstant.OPERATE_JJ:
                                //8.1.1、节点状态，下同： 0:已完成 1：正在处理 2：未处理 
                                wssqJson.put("nodestatus", "0");
                                //8.1.2、如果存在接件步骤默认设置受理为正在处理，如果存在收费，则设置收费为待处理，受理为未处理
                                slJson.put("nodestatus", "1");
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    slJson.put("nodestatus", "2");
                                    sfJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.2、补正节点（有补正节点，则补正为处理中）
                            case ZwfwConstant.OPERATE_BZ:
                                bzDisplay = ZwdtConstant.NOFEE_BZ;
                                clbzJson.put("nodestatus", "1");
                                // 8.2.1、补正默认现在有3种情况，没有收费的补正，收费前补正、收费后补正
                                // 1 受理前收费的补正
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    // 1.1 收费前补正，设置收费正在处理
                                    if (isHasSF == false) {
                                        bzDisplay = ZwdtConstant.BEFORE_FEE_BZ;
                                        sfJson.put("nodestatus", "2");
                                    }
                                    // 1.2 收费后补正，设置受理状态为未处理
                                    else {
                                        bzDisplay = ZwdtConstant.AFTER_FEE_BZ;
                                        slJson.put("nodestatus", "2");
                                    }
                                }
                                else {
                                    slJson.put("nodestatus", "2");
                                }
                                break;
                            // 8.3、办件收费节点，如果是受理前收费，则需要修改补正状态为已处理
                            case ZwfwConstant.OPERATE_BJSF:
                                // 8.3.1、设置收费状态为待处理
                                sfJson.put("nodestatus", "1");
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    // 1、 如果收费状态为收费前补正，则需要把补正状态改为已处理
                                    if (bzDisplay == ZwdtConstant.BEFORE_FEE_BZ) {
                                        clbzJson.put("nodestatus", "0");
                                    }
                                    // 2、如果收费状态为收费后补正，则需要把收费改为待处理
                                    else if (bzDisplay == ZwdtConstant.AFTER_FEE_BZ) {
                                        clbzJson.put("nodestatus", "0");
                                        sfJson.put("nodestatus", "1");
                                    }
                                }
                                else if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                                    blJson.put("nodestatus", "0");
                                    sfJson.put("nodestatus", "1");
                                }
                                break;
                            case ZwfwConstant.OPERATE_SL:
                                // 受理表示补正状态已结束，设置补正为已处理
                                clbzJson.put("nodestatus", "0");
                                slJson.put("nodestatus", "0");
                                blJson.put("nodestatus", "1");
                                break;
                            // 8.4、收讫节点，收讫后存在2种情况，一种是受理前收费，一种是办结前
                            case ZwfwConstant.OPERATE_SQ:
                                isHasSF = true;
                                // 8.4.1 设置收费已完成
                                sfJson.put("nodestatus", "0");
                                // 8.4.2 受理前收费、设置受理为正在处理
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    slJson.put("nodestatus", "1");
                                }
                                // 8.4.3 办结前收费、 设置办理为已处理 （因目前已经不存在批准按钮，以审批通过为节点判断）
                                else if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                                    blJson.put("nodestatus", "0");
                                    bjJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.5 审批通过节点、办理设置为已处理，办结设置为正在处理
                            case ZwfwConstant.OPERATE_SPTG:
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                                    blJson.put("nodestatus", "0");
                                    sfJson.put("nodestatus", "1");
                                    bjJson.put("nodestatus", "2");
                                }
                                else {
                                    blJson.put("nodestatus", "0");
                                    bjJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.6 办结节点
                            case ZwfwConstant.OPERATE_BJ:
                                blJson.put("nodestatus", "0");
                                bjJson.put("nodestatus", "0");
                                break;
                            // 8.7 外网申报提交节点
                            case ZwfwConstant.OPERATE_WWYTJ:
                                wssqJson.put("nodestatus", "0");
                                // 受理前收费
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    slJson.put("nodestatus", "2");
                                    sfJson.put("nodestatus", "1");
                                }
                                else {
                                    slJson.put("nodestatus", "1");
                                }
                                break;
                            // 8.8 预审退回节点
                            case ZwfwConstant.OPERATE_YSDH:
                                wssqJson.put("nodestatus", "1");
                                // 受理前收费
                                if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                                    sfJson.put("nodestatus", "2");
                                }
                                else {
                                    slJson.put("nodestatus", "2");
                                }
                            default:
                                break;
                        }
                    }
                    // 9、设置圆球显示顺序
                    nodelist.add(wssqJson);
                    // 9.1 网上申请默认显示在第一个
                    wssqJson.put("order", 1);
                    // 9.2 判定补正显示的顺序
                    boolean IsShowSF = false; // 是否已显示收费环节
                    // 不存在收费后补正操作的情况
                    if (bzDisplay > 0) {
                        // 补正在收费之后
                        if (bzDisplay == ZwdtConstant.AFTER_FEE_BZ) {
                            IsShowSF = true;
                            nodelist.add(sfJson);
                            sfJson.put("order", nodelist.size());
                        }
                        nodelist.add(clbzJson);
                        clbzJson.put("order", nodelist.size());
                    }
                    // 9.3 受理前收费按钮显示，如果收费在补正前，这边就不显示了
                    if (chargeFlag == ZwfwConstant.CONSTANT_INT_ONE && IsShowSF == false) {
                        if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_SLQ)) {
                            nodelist.add(sfJson);
                            sfJson.put("order", nodelist.size());
                        }
                    }
                    nodelist.add(slJson);
                    // 9.4 添加受理
                    slJson.put("order", nodelist.size());
                    nodelist.add(blJson);
                    // 9.5 添加办理
                    blJson.put("order", nodelist.size());
                    // 9.6 添加办结前收费
                    if (chargeFlag == ZwfwConstant.CONSTANT_INT_ONE) {
                        if (chargeWhen == Integer.parseInt(ZwfwConstant.CHARGE_WHEN_BJQ)) {
                            nodelist.add(sfJson);
                            sfJson.put("order", nodelist.size());
                        }
                    }
                    nodelist.add(bjJson);
                    bjJson.put("order", nodelist.size());
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("nodelist", nodelist);
                    log.info("=======结束调用getProjectFlowlist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件流转信息成功", dataJson.toString());
                }
                else {
                    JSONObject dataJson = new JSONObject();
                    log.info("=======结束调用getProjectFlowlist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "办件信息不存在！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectFlowlist接口参数：params【" + params + "】=======");
            log.info("=======getProjectFlowlist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件流转信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 
     *  获取办件过程信息（所有操作步骤都会列出）
     * 
     *  @param params 接口的入参
     *  @return
     */
    @RequestMapping(value = "/getProjectProcesslist", method = RequestMethod.POST)
    public String getProjectProcesslist(@RequestBody String params) {
        try {
            log.info("=======开始调用getProjectProcesslist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、获取办件基本信息
                AuditProject auditProject = iJNAuditProjectRestService.getAuditProjectDetail(projectGuid);
                if (auditProject != null) {
                    List<JSONObject> nodeJsonList = new ArrayList<JSONObject>();
                    // 根据办件日志填写对应内容(按照时间顺序排列)
                    List<AuditProjectOperation> auditProjectOperations = new ArrayList<AuditProjectOperation>();
                    auditProjectOperations = iAuditProjectOperation.getOperationListForDT(projectGuid).getResult();
                    // 这里为每一个步骤添加开始时间
                    String beginTime = "";
                    for (int i = 0; i < auditProjectOperations.size(); i++) {
                        if (i > 0) {
                            // 添加上一个节点的操作时间
                            beginTime = EpointDateUtil.convertDate2String(
                                    auditProjectOperations.get(i - 1).getOperatedate(),
                                    EpointDateUtil.DATE_TIME_FORMAT);
                        }
                        else {
                            beginTime = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                    EpointDateUtil.DATE_TIME_FORMAT);
                        }
                        // 手机端要求返回操作类型中文
                        String operateType = auditProjectOperations.get(i).getOperateType();
                        String operateTypeName = ZwdtConstant.getOperateTypeKey(operateType);
                        JSONObject tempJson = new JSONObject();
                        tempJson.put("nodename", operateTypeName);
                        tempJson.put("nodestatus", "0"); // 默认展示
                        tempJson.put("endtime", EpointDateUtil.convertDate2String(
                                auditProjectOperations.get(i).getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
                        tempJson.put("begintime", beginTime);
                        tempJson.put("operateusername", auditProjectOperations.get(i).getOperateusername());
                        tempJson.put("remark", auditProjectOperations.get(i).getRemarks());
                        nodeJsonList.add(tempJson);
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("nodelist", nodeJsonList);
                    log.info("=======结束调用getProjectProcesslist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件过程信息成功", dataJson.toString());
                }
                else {
                    log.info("=======结束调用getProjectProcesslist接口=======");
                    return JsonUtils.zwdtRestReturn("0", "办件过程信息不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectProcesslist接口参数：params【" + params + "】=======");
            log.info("=======getProjectProcesslist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件过程信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取流程中产生文书的接口
     * 
     * @param params 接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getProjectDoclist", method = RequestMethod.POST)
    public String getProjectDoclist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectDoclist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");// 办件标识，audit_project的主键
                // 2、获取办件文书快照信息
                List<AuditProjectDocSnap> auditProjectDocSnaps = iAuditProjectDocSnap
                        .selectDocSnapByProjectGuid(projectGuid).getResult();
                List<JSONObject> docJsonList = new ArrayList<JSONObject>();
                for (AuditProjectDocSnap auditProjectDocSnap : auditProjectDocSnaps) {
                    JSONObject docJson = new JSONObject();
                    // 2、文书名称默认不需要事项名称
                    String docName = iCodeItemsService.getItemTextByCodeName("文书类型",
                            String.valueOf(auditProjectDocSnap.getDoctype()));
                    docJson.put("docname", docName);
                    // 3、判断使用NTKO插件还是HTML页面形式
                    if (StringUtil.isBlank(auditProjectDocSnap.getDocattachguid())) {
                        // 3.1、如果是HTML页面形式，返回全路径
                        docJson.put("doccontent", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/myspace/doc?rowguid=" + auditProjectDocSnap.getRowguid());
                        docJson.put("type", 1);
                    }
                    else {
                        // 3.2、如果是NTKO形式
                        String url = ConfigUtil.getConfigValue("officeweb365url");
                        FrameAttachStorage frameAttachStorage = iAttachService
                                .getAttach(auditProjectDocSnap.getDocattachguid());
                        String attachguid = "";
                        if (frameAttachStorage != null) {
                            attachguid = frameAttachStorage.getAttachGuid();
                        }
                        docJson.put("doccontent",
                                url + WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                        + attachguid);
                        docJson.put("type", 0);
                    }
                    docJsonList.add(docJson);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("doclist", docJsonList);
                int totalcount = docJsonList == null ? 0 : docJsonList.size();
                dataJson.put("totalcount", totalcount);
                log.info("=======结束调用getProjectDoclist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取流程中产生文书成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectDoclist接口参数：params【" + params + "】=======");
            log.info("=======getProjectDoclist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取流程中产生文书失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取待预审办件接口（自检页面调用）
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getDYSProjectList", method = RequestMethod.POST)
    public String getDYSProjectList(@RequestBody String params) {
        try {
            log.info("=======开始获取getDYSProjectList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页
                String currentPage = obj.getString("pageindex");
                // 1.2、获取每页数量
                String pageSize = obj.getString("pagesize");
                // 2、获取外网待预审办件列表
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                SqlConditionUtil conditionUtil = new SqlConditionUtil();
                conditionUtil.eq("status", String.valueOf(ZwdtConstant.BANJIAN_STATUS_WWYTJ)); //待预审状态：12                  
                PageData<AuditOnlineProject> pageData = iAuditOnlineProject.getAuditOnlineProjectPageData(
                        conditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                        Integer.parseInt(pageSize), "ouname,applydate", "desc").getResult();
                // 2.1、获取外网提交预审办件列表数量
                int count = pageData.getRowCount();
                List<AuditOnlineProject> list = pageData.getList();
                for (AuditOnlineProject auditOnlineProject : list) {
                    JSONObject recordJson = new JSONObject();
                    recordJson.put("projectname", auditOnlineProject.getProjectname());
                    recordJson.put("ouname", auditOnlineProject.getOuname());
                    recordJson.put("applyername", auditOnlineProject.getApplyername());
                    recordJson.put("applydate", EpointDateUtil.convertDate2String(auditOnlineProject.getApplydate(),
                            EpointDateUtil.DATE_FORMAT));
                    // 2.2、查出办件对应的窗口
                    AuditTask auditTask = iAuditTask.selectTaskByRowGuid(auditOnlineProject.getTaskguid()).getResult();
                    if (auditTask != null) {
                        List<AuditOrgaWindow> auditOrgaWindowList = iAuditOrgaWindow
                                .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                        String windowname = "";
                        if (auditOrgaWindowList != null && auditOrgaWindowList.size() > 0) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindowList) {
                                if (StringUtil.isBlank(auditOrgaWindow.getWindowname())) {
                                    windowname = "无对应窗口";
                                }
                                else {
                                    windowname = windowname + auditOrgaWindow.getWindowname() + ",";
                                }
                            }
                            String name = auditOnlineProject.getOuname() + "【" + windowname;
                            recordJson.put("projectname", name.substring(0, name.length() - 1) + "】");
                        }
                        else {
                            recordJson.put("projectname", auditOnlineProject.getOuname() + "【无对应窗口】");
                        }
                    }
                    jsonList.add(recordJson);
                }
                // 3、获取每个部门以及部门待预审的办件数量
                List<JSONObject> groupJsonList = new ArrayList<JSONObject>();
                List<Record> auditOnlineProjects = iAuditOnlineProject.getDYSProjectListByGroup().getResult();
                for (Record record : auditOnlineProjects) {
                    JSONObject groupJson = new JSONObject();
                    groupJson.put("groupOuname", record.get("ouname"));
                    groupJson.put("groupCount", record.get("count"));
                    if (StringUtil.isBlank(record.get("ouname"))) {
                        groupJson.put("groupOuname", "其他");
                    }
                    groupJsonList.add(groupJson);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("jsonList", jsonList);
                dataJson.put("count", count);
                dataJson.put("groupJsonList", groupJsonList);
                log.info("=======结束调用getDYSProjectList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取待预审办件成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getDYSProjectList接口参数：params【" + params + "】=======");
            log.info("=======getDYSProjectList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取待预审办件异常：" + e.getMessage(), "");
        }
    }

    /******************************************暂时无用接口*********************************************/
    /**
     * 获取办件基本信息接口
     * 
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectBaseInfo", method = RequestMethod.POST)
    public String getProjectBaseInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectBaseInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取办件编号
                String areaCode = obj.getString("areacode");
                // 2、获取办件基本信息
                String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark ";
                AuditProject auditProject = iJNAuditProjectRestService.getAuditProjectDetail(projectGuid);
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if (auditProject != null) {
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                        dataJson.put("taskicon",
                                WebUtil.getRequestCompleteUrl(request)
                                        + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid="
                                        + auditTask.getRowguid());// 二维码内容
                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("department", auditTask.getOuname());// 部门名称
                        dataJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        // 即办件承诺日期默认为 1天
                        if (ZwfwConstant.ITEMTYPE_JBJ.equals(String.valueOf(auditTask.getType()))) {
                            dataJson.put("promiseday", "1");// 承诺日期
                        }
                        else {
                            dataJson.put("promiseday", auditTask.getPromise_day());// 承诺日期
                        }
                        JSONObject taskElementJson = new JSONObject();
                        taskElementJson.put("onlinehandle", ZwfwConstant.WEB_APPLY_TYPE_NOT
                                .equals(String.valueOf(auditTaskExtension.getWebapplytype())) ? "0" : "1");// 是否可以网上申报
                        taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());// 是否可以网上预约
                        dataJson.put("taskelement", taskElementJson);
                        JSONObject applyerJson = new JSONObject();
                        applyerJson.put("applyername", auditProject.getApplyername());
                        applyerJson.put("applyercard", auditProject.getCertnum());
                        applyerJson.put("contactname", auditProject.getContactperson());
                        applyerJson.put("contactmobile", auditProject.getContactmobile());
                        applyerJson.put("contactphone", auditProject.getContactphone());
                        applyerJson.put("contactidnum", auditProject.getContactcertnum());
                        applyerJson.put("postcode", auditProject.getContactpostcode());
                        applyerJson.put("address", auditProject.getAddress());
                        applyerJson.put("remark", auditProject.getRemark());
                        dataJson.put("applerinfo", applyerJson);
                    }
                }
                log.info("=======结束调用getProjectBaseInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件基本信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectBaseInfo接口参数：params【" + params + "】=======");
            log.info("=======getProjectBaseInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件基本信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
    * 
    *  判断用户在某企业中的身份
    *  
    *  @param accountGuid 用户标识
    *  @param idnum 用户身份证
    *  @param companyId 公司标识
    *  @return
    */
    private String getCompanyUserLevel(String accountGuid, String idnum, String companyId) {
        // 默认用户类型为0,  1：法人，2：管理者，3：代办人
        String userLevel = null;
        // 1、根据用户身份证和公司标识获取企业基本信息
        AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                .getCompanyByCompanyIdAndIdnum(companyId, idnum).getResult();
        if (auditRsCompanyBaseinfo != null) {
            // 2、如果个人有对应的企业信息，则为法人
            userLevel = ZwdtConstant.GRANT_LEGEL_PERSION;
        }
        else {
            // 3、获取用户授权基本信息
            AuditOnlineCompanyGrant auditOnlineCompanyGrant = iAuditOnlineCompanyGrant
                    .getGrantByBsqUserGuidAndCompanyId(companyId, accountGuid).getResult();
            if (auditOnlineCompanyGrant != null) {
                userLevel = auditOnlineCompanyGrant.getBsqlevel();
            }
        }
        return userLevel;
    }
    
    public JSONObject getMaterialJson(AuditTaskMaterial auditTaskMaterial) {
        JSONObject materialJson = new JSONObject();
        materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
        materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
        // 4.1、获取填报示例对应附件标识
        if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
            int exampleAttachCount = iAttachService
                    .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
            if (exampleAttachCount > 0) {
                materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
            }
        }
        // 4.2、获取空白模板对应附件标识
        if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
            int templateAttachCount = iAttachService
                    .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
            if (templateAttachCount > 0) {
                materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
            }
        }
        // 4.3、获取材料来源渠道
        String materialsource;
        if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
            materialsource = "申请人自备";
        }
        else {
            materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", auditTaskMaterial.getFile_source());
        }
        materialJson.put("materialsource", materialsource);
        // 4.4、获取事项材料必要性
        String necessary = "0";
        if (auditTaskMaterial != null) {
            necessary = ZwfwConstant.NECESSITY_SET_YES.equals(String.valueOf(auditTaskMaterial.getNecessity())) ? "1"
                    : "0";// 是否必需
        }
        materialJson.put("necessary", necessary);
        materialJson.put("pagenum",
                StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0" : auditTaskMaterial.getPage_num()); //份数
        materialJson.put("standard",
                StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无" : auditTaskMaterial.getStandard());//受理标准
        materialJson.put("fileexplain",
                StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无" : auditTaskMaterial.getFile_explian());//填报须知
        return materialJson;
    }
    

    /**
     *  查询办件历史材料的接口
     *  
     *  @param params 接口的入参
     *  @param request HTTP请求
     *  @return
     */
    @RequestMapping(value = "/getHistoryDisk", method = RequestMethod.POST)
    public String getHistoryDisk(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getHistoryDisk接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、 接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 获取办件标识
                String projectguid = obj.getString("projectguid");
                // 1.3、 获取材料标识
                String taskMaterialGuid = obj.getString("taskmaterialguid");
                // 1.4、 办件材料标识
                String projectmaterialguid = obj.getString("projectmaterialguid");
                // 1.5、搜索
                String keyword = obj.getString("keyword");
                // 1.6、项目标识
                String itemguid = obj.getString("itemguid");
                // 1.7、申请人标识
                String applyerType = obj.getString("applyertype");
                // 1.8、企业ID
                String companyId = obj.getString("companyid");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    String certNum = "";
                    int filesCount = 0;
                    List<JSONObject> records = new ArrayList<JSONObject>();
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        certNum = "'" + auditOnlineRegister.getIdnumber() + "'";
                    }
                    else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        if (StringUtil.isNotBlank(itemguid)) {
                            //并联审批
                            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                            if (auditRsItemBaseinfo != null) {
                                //判断当前项目的法人证件类型，取出对应的证件号码
                                if ("14".equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                    //组织机构代码证
                                    certNum = "'" + auditRsItemBaseinfo.getItemlegalcertnum() + "'";
                                }
                                else if ("16".equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                    //社会信用代码
                                    certNum = "'" + auditRsItemBaseinfo.getItemlegalcertnum() + "'";
                                }
                                else {
                                    //如果法人的证件类型匹配不到，默认项目所属的社会信用代码
                                    certNum = "'" + (StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum()) + "'";
                                }
                            }
                        }
                        else {
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByCompanyId(companyId).getResult();
                            if (auditRsCompanyBaseinfo != null) {
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                                    certNum += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                                }
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                                    certNum += "'" + auditRsCompanyBaseinfo.getOrgancode() + "',";
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotBlank(certNum)) {
                        if (certNum.endsWith(",")) {
                            certNum = certNum.substring(0, certNum.length() - 1);
                        }
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.isBlankOrValue("firstflag", "0");
                        sql.eq("projectguid", projectguid);
                        sql.eq("projectmaterialguid", projectmaterialguid);
                        sql.isNotBlank("copyfrom");
                        PageData<AuditMaterialLibrary> pagedata = iAuditMaterialLibrary
                                .getAuditMaterialLibraryListByPage(sql.getMap(), 0, 0, "", "").getResult();
                        // 2.1、 把当前办件或者套餐选中的历史材料保存起来，用于后面遍历
                        Map<String, String> map = new HashMap<>();
                        for (AuditMaterialLibrary auditMaterialLibrary : pagedata.getList()) {
                            map.put(auditMaterialLibrary.getRowguid() + "," + auditMaterialLibrary.getAttachguid(),
                                    auditMaterialLibrary.getCopyfrom());
                        }
                        // 3、 从事项的材料中获取材料名称，用于计算相似度
                        String materialName = "";
                        if (StringUtil.isNotBlank(taskMaterialGuid)) {
                            AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                    .getAuditTaskMaterialByRowguid(taskMaterialGuid).getResult();
                            if (auditTaskMaterial != null) {
                                materialName = auditTaskMaterial.getMaterialname();
                            }
                            else {
                                AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                        .getSpIMaterialByrowguid(projectmaterialguid).getResult();
                                if (auditSpIMaterial != null) {
                                    materialName = auditSpIMaterial.getMaterialname();
                                }
                            }
                        }
                        // 4、 查询当前登陆人首次提交的非历史材料
                        SqlConditionUtil sqlc = new SqlConditionUtil();
                        sqlc.in("ownerno", certNum);
                        sqlc.eq("firstflag", ZwfwConstant.CONSTANT_STR_ONE);
                        sqlc.nq("projectguid", projectguid);
                        PageData<AuditMaterialLibrary> pagedataLibrary = iAuditMaterialLibrary
                                .getAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "", "").getResult();
                        List<Record> list2 = new ArrayList<Record>();
                        TextSimilarity similarity = new CosineSimilarity();
                        DecimalFormat df = new DecimalFormat("0.0000");
                        // 5、 遍历所有首次提交的材料，计算相似度
                   
                        for (Record record : pagedataLibrary.getList()) {
                            try {
                                Double double2 = similarity.getSimilarity(materialName, record.getStr("MaterialName"));
                                record.put("Similarity", double2 != 0 ? df.format(double2) : "0.0000");
                                record.set("materialname", record.get("materialname"));
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                record.put("Similarity", 0);
                            }
                            list2.add(record);
                        }
                        // 根据Collections.sort重载方法来实现倒序
                        // 6、 按照材料名称的相似度排序，相似度大的在上面
                        Collections.sort(list2, new Comparator<Record>()
                        {
                            @Override
                            public int compare(Record b1, Record b2) {
                                return b2.getStr("Similarity").compareTo(b1.getStr("Similarity"));
                            }
                        });
                        // 7、 返回json
                        filesCount = list2.size();
                        for (Record record : list2) {
                            JSONObject fileJson = new JSONObject();
                            // 搜索匹配附件名称
                            if (StringUtil.isNotBlank(keyword)) {
                                if (!record.getStr("materialname").contains(keyword)) {
                                    continue;
                                }
                            }
                            fileJson.put("guid", record.get("rowguid").toString());
                            fileJson.put("name", record.getStr("materialname"));
                            fileJson.put("filetype", record.getStr("filetype"));
                            fileJson.put("size", convertFileSize(record.getInt("count")));
                            fileJson.put("uploaddate", EpointDateUtil.convertDate2String(record.getDate("OperateDate"),
                                    EpointDateUtil.DATE_TIME_FORMAT));
                            fileJson.put("attachguid", record.getStr("attachguid"));
                            OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(request);
                            String sysLoginId = "";
                            if (oAuthCheckTokenInfo != null) {
                                sysLoginId = oAuthCheckTokenInfo.getLoginid();
                            }
                            String frameAttachGuid = ParamEncryptUtil.encrypt(record.getStr("attachguid"), sysLoginId,
                                    null);
                            fileJson.put("frameAttachGuid", frameAttachGuid);
                            DecimalFormat dfDouble = new DecimalFormat("######0.00");
                            fileJson.put("similarity",
                                    dfDouble.format((Double.parseDouble(record.getStr("similarity"))) * 100));
                            // 判断材料是否选中
                            String rowguid = record.get("rowguid").toString();
                            if (map.containsValue(rowguid)) {
                                for (String s : map.keySet()) {
                                    if (rowguid.equals(map.get(s))) {
                                        String attachguid = s.split(",")[1];
                                        //如果删除了附件，但是引用关系可能没删，这里判断有没有附件
                                        FrameAttachInfo frameAttachInfo = iAttachService
                                                .getAttachInfoDetail(attachguid);
                                        if (frameAttachInfo != null) {
                                            fileJson.put("isselected", 1);
                                        }
                                    }
                                }

                            }
                            else {
                                fileJson.put("isselected", 0);
                            }
                            records.add(fileJson);

                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("records", records);
                    dataJson.put("totoalcount", filesCount);
                    log.info("=======结束调用getHistoryDisk接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取历史材料信息成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getHistoryDisk接口参数：params【" + params + "】=======");
            log.info("=======getHistoryDisk异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取历史材料失败：" + e.getMessage(), "");
        }
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        }
        else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        }
        else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        }
        else {
            return String.format("%d B", size);
        }
    }
    
    /**
     *  确认复用接口
     *  
     *  @param params 接口的入参
     *  @return 
     */
    @RequestMapping(value = "/sureHistoryFile", method = RequestMethod.POST)
    public String sureHistoryFile(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用sureHistoryFile接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、 接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、 选中的历史材料记录
                String libraryguids = obj.getString("libraryguids");
                // 1.2、 材料附件标识
                String cliengguid = obj.getString("clientguid");
                // 1.3、 事项材料唯一标识
                String taskmaterialguid = obj.getString("taskmaterialguid");
                // 1.4、 事项唯一标识
                String taskguid = obj.getString("taskguid");
                // 1.5、 办件唯一标识
                String projectguid = obj.getString("projectguid");
                // 1.6、 办件材料
                String projectmaterialguid = obj.getString("projectmaterialguid");
                // 1.7、 申请人类型
                String applyerType = obj.getString("applyertype");
                // 1.8、 企业ID
                String companyId = obj.getString("companyid");
                // 1.9、 项目标识
                String itemguid = obj.getString("itemguid");

                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    String ownerid = "";
                    String certNum = "";
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        //如果是个人办件或套餐
                        certNum = "'" + auditOnlineRegister.getIdnumber() + "'";
                        ownerid = auditOnlineRegister.getIdnumber();
                    }
                    else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        if (StringUtil.isNotBlank(itemguid)) {
                            //并联审批
                            AuditRsItemBaseinfo auditRsItemBaseinfo = iAuditRsItemBaseinfo
                                    .getAuditRsItemBaseinfoByRowguid(itemguid).getResult();
                            if (auditRsItemBaseinfo != null) {
                                //判断当前项目的法人证件类型，取出对应的证件号码
                                if ("14".equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                    //组织机构代码证
                                    certNum = "'" + auditRsItemBaseinfo.getItemlegalcertnum() + "'";
                                    ownerid = auditRsItemBaseinfo.getItemlegalcertnum();
                                }
                                else if ("16".equals(auditRsItemBaseinfo.getItemlegalcerttype())) {
                                    //社会信用代码
                                    certNum = "'" + auditRsItemBaseinfo.getItemlegalcertnum() + "'";
                                    ownerid = auditRsItemBaseinfo.getItemlegalcertnum();
                                }
                                else {
                                    //如果法人的证件类型匹配不到，默认项目所属的社会信用代码
                                    certNum = "'" + (StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum()) + "'";
                                    ownerid = StringUtils.isNotBlank(auditRsItemBaseinfo.getItemlegalcreditcode())?auditRsItemBaseinfo.getItemlegalcreditcode():auditRsItemBaseinfo.getItemlegalcertnum();
                                }
                            }
                        }
                        else {
                            //如果是法人办件
                            AuditRsCompanyBaseinfo auditRsCompanyBaseinfo = iAuditRsCompanyBaseinfo
                                    .getCompanyByCompanyId(companyId).getResult();
                            if (auditRsCompanyBaseinfo != null) {
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getOrgancode())) {
                                    certNum += "'" + auditRsCompanyBaseinfo.getOrgancode() + "',";
                                    ownerid = auditRsCompanyBaseinfo.getOrgancode();
                                }
                                if (StringUtil.isNotBlank(auditRsCompanyBaseinfo.getCreditcode())) {
                                    certNum += "'" + auditRsCompanyBaseinfo.getCreditcode() + "',";
                                    ownerid = auditRsCompanyBaseinfo.getCreditcode();
                                }
                            }
                        }
                    }
                    if (StringUtil.isNotBlank(certNum)) {
                        if (certNum.endsWith(",")) {
                            certNum = certNum.substring(0, certNum.length() - 1);
                        }
                        if (StringUtil.isNotBlank(libraryguids)) {
                            libraryguids = libraryguids.replace("[", "").replace("]", "").replace("\"", "");
                            // 2、 多次选择把之前选择的删掉，重新新增
                            SqlConditionUtil sqlDelete = new SqlConditionUtil();
                            sqlDelete.in("ownerno", certNum);
                            sqlDelete.isBlank("firstflag");
                            // 3、 判断提交的是办件还是套餐
                            sqlDelete.eq("projectguid", projectguid);
                            sqlDelete.eq("projectmaterialguid", projectmaterialguid);
                            // 4、 把当前的办件或者套餐提交的材料全部删除，然后重新插入材料（重新选择材料）
                            PageData<AuditMaterialLibrary> pagedataLibrary = iAuditMaterialLibrary
                                    .getAuditMaterialLibraryListByPage(sqlDelete.getMap(), 0, 0, "", "").getResult();
                            for (AuditMaterialLibrary auditMaterialLibrary : pagedataLibrary.getList()) {
                                iAuditMaterialLibrary
                                        .deleteAuditMaterialLibraryByAttach(auditMaterialLibrary.getAttachguid());
                                iAttachService.deleteAttachByAttachGuid(auditMaterialLibrary.getAttachguid());
                            }
                            // 5、 分割出选中的材料遍历插入
                            String[] strLibraryArr = libraryguids.split(",");
                            String strLibrary = "";
                            for (int i = 0; i < strLibraryArr.length; i++) {
                                if (!strLibrary.contains(strLibraryArr[i])) {
                                    strLibrary += strLibraryArr[i] + ",";
                                }
                            }
                            String itemId = "";
                            String materialid = "";
                            String materialname = "";
                            String taskid = "";
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                            String projectName = "";
                            if (auditTask != null) {
                                itemId = auditTask.getItem_id();
                                taskid = auditTask.getTask_id();
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(taskmaterialguid).getResult();
                                if (auditTaskMaterial != null) {
                                    materialid = auditTaskMaterial.getMaterialid();
                                    materialname = auditTaskMaterial.getMaterialname();
                                }
                                projectName = auditTask.getTaskname();
                            }
                            else {
                                AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial
                                        .getSpIMaterialByrowguid(projectmaterialguid).getResult();
                                if (auditSpIMaterial != null) {
                                    materialid = auditSpIMaterial.getMaterialguid();
                                    materialname = auditSpIMaterial.getMaterialname();
                                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                            .getAuditSpBusinessByRowguid(auditSpIMaterial.getBusinessguid())
                                            .getResult();
                                    if (auditSpBusiness != null) {
                                        projectName = auditSpBusiness.getBusinessname();
                                    }
                                }
                            }
                            String[] libraryArr = strLibrary.split(",");
                            for (String s : libraryArr) {
                                if (StringUtil.isNotBlank(s)) {
                                    AuditMaterialLibrary auditMaterialLibrary = iAuditMaterialLibrary.find(s);
                                    if (auditMaterialLibrary != null) {
                                        if (StringUtil.isNotBlank(auditMaterialLibrary.getAttachguid())) {
                                            FrameAttachStorage frameAttachStorage = iAttachService
                                                    .getAttach(auditMaterialLibrary.getAttachguid());
                                            FrameAttachInfo attachInfo = iAttachService
                                                    .getAttachInfoDetail(auditMaterialLibrary.getAttachguid());
                                            if (attachInfo != null) {
                                                String attachguid = UUID.randomUUID().toString();
                                                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                                                frameAttachInfo.setAttachGuid(attachguid);
                                                frameAttachInfo.setCliengGuid(cliengguid);
                                                frameAttachInfo.setCliengTag("历史附件引用");
                                                frameAttachInfo.setUploadDateTime(new Date());
                                                frameAttachInfo.setAttachFileName(attachInfo.getAttachFileName());
                                                frameAttachInfo.setAttachLength(attachInfo.getAttachLength());
                                                frameAttachInfo.setFilePath(attachInfo.getFilePath());
                                                InputStream content = null;
                                                if (frameAttachStorage != null
                                                        && frameAttachStorage.getContent() != null) {
                                                    content = frameAttachStorage.getContent();

                                                }
                                                iAttachService.addAttach(frameAttachInfo, content);
                                                AuditMaterialLibrary auditMaterialLibraryNew = new AuditMaterialLibrary();
                                                auditMaterialLibraryNew.setRowguid(UUID.randomUUID().toString());
                                                auditMaterialLibraryNew.setAttachguid(attachguid);
                                                auditMaterialLibraryNew.setCopyfrom(auditMaterialLibrary.getRowguid());
                                                auditMaterialLibraryNew.setCount(0);
                                                auditMaterialLibraryNew.setItemcode(itemId);
                                                auditMaterialLibraryNew.setMaterialid(materialid);
                                                auditMaterialLibraryNew.setMaterialname(materialname);
                                                auditMaterialLibraryNew.setTaskid(taskid);
                                                auditMaterialLibraryNew.setProjectguid(projectguid);
                                                auditMaterialLibraryNew.setOuname("政务门户网站");
                                                auditMaterialLibraryNew.setProjectname(projectName);
                                                auditMaterialLibraryNew
                                                        .setOperateusername(auditOnlineRegister.getUsername());
                                                auditMaterialLibraryNew.setOperatedate(new Date());
                                                auditMaterialLibraryNew.setOwnerno(ownerid);
                                                auditMaterialLibraryNew.set("projectmaterialguid", projectmaterialguid);
                                                iAuditMaterialLibrary.insert(auditMaterialLibraryNew);
                                                auditMaterialLibrary.setCount(auditMaterialLibrary.getCount() + 1);
                                                iAuditMaterialLibrary.update(auditMaterialLibrary);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    log.info("=======结束调用sureHistoryFile接口=======");
                    return JsonUtils.zwdtRestReturn("1", "文件引用成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (

        Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "文件引用失败：" + e.getMessage(), "");
        }
    }


}
