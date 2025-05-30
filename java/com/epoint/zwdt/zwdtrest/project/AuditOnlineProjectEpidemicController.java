
package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;

@RestController
@RequestMapping("/zwdtProjectEpidemic")
public class AuditOnlineProjectEpidemicController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
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
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
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
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;
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
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    /**
     * 事项队列分类相关api
     */
    @Autowired
    IAuditQueueTasktypeTask iAuditQueueTasktypeTask;

    /**
     * 事项队列相关api
     */
    @Autowired
    IAuditQueueTasktype iAuditQueueTasktype;
    
    /**
     *  办件提交成功后修改同意申报承诺书标志位
     */
    @RequestMapping(value = "/updateEpidemic", method = RequestMethod.POST)
    public String declareProjectNotice(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用updateEpidemic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskGuid = obj.getString("taskguid");
                String areacode = obj.getString("areacode");
                String projectguid = obj.getString("projectguid");
                // 2、获取事项基本信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    // 3、获取办件信息
                    AuditProject project = iAuditProject.getAuditProjectByRowGuid("*", projectguid, areacode).getResult();
                    if (project!=null) {
                        // 4、修改标志位
                        project.set("epidemic", ZwfwConstant.CONSTANT_STR_ONE);
                        iAuditProject.updateProject(project);
                        // 5、更新外网办件信息
                        AuditOnlineProject onlineProject=iAuditOnlineProject.getOnlineProjectByApplyerGuid(projectguid, project.getOnlineapplyerguid()).getResult();
                        if (onlineProject!=null) {
                            onlineProject.set("epidemic", ZwfwConstant.CONSTANT_STR_ONE);
                            iAuditOnlineProject.updateProject(onlineProject);
                        }
                        else {
                            log.info("=======结束调用updateEpidemic接口："+projectguid+"该办件外网信息不存在！=======");
                            return JsonUtils.zwdtRestReturn("1", "该办件外网信息不存在！", "");
                        }
                    }
                    else {
                        log.info("=======结束调用updateEpidemic接口："+projectguid+"该办件不存在！=======");
                        return JsonUtils.zwdtRestReturn("1", "该办件不存在！", "");
                    }
                    log.info("=======结束调用updateEpidemic接口=======");
                    return JsonUtils.zwdtRestReturn("1", "修改同意申报承诺书标志位成功", "");
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
            log.info("=======updateEpidemic接口参数：params【" + params + "】=======");
            log.info("=======updateEpidemic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改同意申报承诺书标志位失败：" + e.getMessage(), "");
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
                    sqlConditionUtil.eq("epidemic",ZwfwConstant.CONSTANT_STR_ONE); // 申报承诺书标志位
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
                                projectJson.put("projectform", auditTask.getStr("projectform"));
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
                                    evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
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
                                    AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                                            .getAuditOperation(projectGuid, ZwfwConstant.OPERATE_YSDH).getResult();
                                    projectJson.put("remarks",
                                            auditProjectOperation == null ? "" : auditProjectOperation.getRemarks());
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
                                evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
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
                                projectJson.put("revert", "撤回");
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
     *  添加申报承诺书标志位搜索
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
                    sqlConditionUtil.eq("epidemic", ZwfwConstant.CONSTANT_STR_ONE); // 申报承诺书标志位
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
                                    ZwdtConstant.BANJIAN_STATUS_WWWTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU);
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
                            sqlConditionUtil.eq("is_evaluat", "1");
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
                            String projectApplyerGuid = auditOnlineProject.getDeclarerguid();
                            if (auditTask != null && StringUtil.isNotBlank(projectApplyerGuid)) {
                                JSONObject projectJson = new JSONObject();
                                // 4.1.2.2、如果是企业办件，获取当前办件实际申报人
                                projectJson.put("declarerguid", projectApplyerGuid);
                                projectJson.put("declarername", auditOnlineProject.getDeclarername());
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
                                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("taskcaseguid",
                                        projectGuid, auditOnlineProject.getAreacode()).getResult();
                                projectJson.put("taskcaseguid",
                                        auditProject == null ? "" : auditProject.getTaskcaseguid()); // 办件多情形标识
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
                                    evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
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
                                            if (auditOnlineProject.getSpendtime() != null) {
                                                int spendMinute = auditOnlineProject.getSpendtime();
                                                spareTime = "办理用时：" + CommonUtil.getSpareTimes(spendMinute);
                                            }
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
                                // 获取办件是否是审批不通过
                                List<AuditProjectOperation> auditProjectOperations = iAuditProjectOperation
                                        .getOperationListForDT(projectGuid).getResult();
                                for (AuditProjectOperation auditProjectOperation : auditProjectOperations) {
                                    String operateType = auditProjectOperation.getOperateType();
                                    if (StringUtil.isNotBlank(operateType)
                                            && ZwfwConstant.OPERATE_SPBTG.equals(operateType)) {
                                        projectJson.put("isspbtg", "1");
                                    }
                                }
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
                                    SqlConditionUtil opSqlConditionUtil = new SqlConditionUtil();
                                    opSqlConditionUtil.eq("projectGuid", projectGuid);
                                    opSqlConditionUtil.eq("operatetype", ZwfwConstant.OPERATE_YSDH);
                                    AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                                            .getAuditOperationByCondition(opSqlConditionUtil.getMap()).getResult();
                                    projectJson.put("remarks",
                                            auditProjectOperation == null ? "" : auditProjectOperation.getRemarks());
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
                                        buZhengText += auditProjectMaterial.getTaskmaterial() + "；";
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
                                evaluate = iCodeItemsService.getItemTextByCodeName("满意度",
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
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid()).getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity()).getResult();
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
}
