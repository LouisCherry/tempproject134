
package com.epoint.zwdt.zwdtrest.project;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditqueue.auditznsbsample.domain.AuditZnsbSample;
import com.epoint.basic.auditqueue.auditznsbsample.inter.IAuditZnsbSample;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.intermediary.sendmaterials.api.ISendMaterials;
import com.epoint.jnzwdt.auditproject.jnaicpy.api.IJnAiCpyService;
import com.epoint.jnzwdt.auditproject.jnaicpy.api.entity.JnAiCpy;
import com.epoint.jnzwdt.auditproject.jnbuildpart.api.IJnBuildPartService;
import com.epoint.jnzwdt.auditproject.jnbuildpart.api.entity.JnBuildPart;
import com.epoint.newshow2.api.Newshow2Service;
import com.epoint.utils.HttplcUtils;
import com.epoint.wechat.project.api.IWeiChatProjectService;
import com.epoint.zwdt.teacherhealthreport.api.ITeacherHealthReportService;
import com.epoint.zwdt.teacherhealthreport.api.entity.TeacherHealthReport;
import com.epoint.zwdt.zwdtrest.project.api.IZjZcsspService;
import com.epoint.zwdt.zwdtrest.project.api.entity.ZjZcssp;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/jnzwdtProject")
public class JnAuditOnlineProjectController {
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    @Autowired
    private ICodeItemsService iCodeItemsService;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IZjZcsspService izjZcsspService;

    @Autowired
    private IJnBuildPartService iJnBuildPartService;

    @Autowired
    private IJnAiCpyService iJnAiCpyService;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private ISendMaterials sendMaterials;

    @Autowired
    private ITeacherHealthReportService iTeacherHealthReportService;

    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

    /*
     * @Autowired private IAuditProjectCharge chargeservice;
     *
     * @Autowired private IHandleChargeDetail chargeDetailService;
     */
    /**
     * 网上用户注册API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

    @Autowired
    private IAuditTask taskservice;
    /**
     * 事项情形与材料关系API
     */
    @Autowired
    private IAuditTaskMaterialCase iAuditTaskMaterialCase;

    @Autowired
    private IAuditZnsbSample sampleservice;

    @Autowired
    private IAuditProject auditProjectService;

    /**
     * 证照基本信息API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;

    /**
     * 证照库附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;
    /**
     * 系统参数
     */
    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IAttachService attachService;

    /*
     * @Autowired private JnProjectService jnProjectService;
     */

    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;
    @Autowired
    private IWeiChatProjectService weiChatProjectService;

    /**
     * 济宁个性化接口
     */
    @Autowired
    private IJnAppRestService iJnAppRestService;

    @Autowired
    private Newshow2Service newshow2Service;

    @Autowired
    private IAuditOnlineProject auditOnlineProjectService;

    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private ISpaceAcceptService iSpaceAcceptService;

    @Autowired
    private IAuditTaskCase iAuditTaskCase;

    @Resource
    private IAuditTaskOptionService optionService;
    @Resource
    private IAuditProjectUnusual iAuditProjectUnusual;

    /**
     * 事项审批结果API
     */

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
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取多情形材料列表(办件申报须知多情形下调用)
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
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
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取一页显示数量
                String pageSize = obj.getString("pagesize");
                // 1.2、获取事项标识
                String taskguid = obj.getString("taskguid");

                // String taskGuid = obj.getString("taskguid");
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                // 2、获取多情形材料列表
                int totalcount = 0;
                if (StringUtil.isNotBlank(taskCaseGuid)) {
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1、办件通用的检索条件
                    sqlConditionUtil.eq("taskcaseguid", taskCaseGuid); // 申请人标识

                    PageData<AuditTaskMaterialCase> auditTaskMaterialCasess = iAuditTaskMaterialCase
                            .getAuditTaskMaterialCasePageData(sqlConditionUtil.getMap(),
                                    Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                    Integer.parseInt(pageSize), "operatedate", "desc")
                            .getResult();
                    totalcount = auditTaskMaterialCasess.getRowCount();
                    // List<AuditTaskMaterialCase> auditTaskMaterialCases =
                    // iAuditTaskMaterialCase
                    // .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                    // 2.1、材料排序（必要的在前）
                    Collections.sort(auditTaskMaterialCasess.getList(), new Comparator<AuditTaskMaterialCase>() {
                        @Override
                        public int compare(AuditTaskMaterialCase b1, AuditTaskMaterialCase b2) {
                            return b1.getNecessity().compareTo(b2.getNecessity());
                        }
                    });
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCasess.getList()) {
                        // 3、获取情形与材料关系获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid()).getResult();
                        JSONObject materialJson = new JSONObject();
                        materialJson.put("materialurl", "");// 材料url
                        materialJson.put("materialguid", auditTaskMaterial.getMaterialid());// 材料标识
                        materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("materialguid", auditTaskMaterial.getMaterialid());
                        sql.setOrderAsc("OrderNum");
                        List<AuditZnsbSample> samplelist = sampleservice.getSampleList(sql.getMap()).getResult();
                        if (samplelist.size() > 0) {
                            if (StringUtil.isNotBlank(samplelist.get(0).getSampleattachguid())) {
                                materialJson.put("havexampleattach", "1");
                            } else {
                                materialJson.put("havexampleattach", "0");
                            }
                        } else {
                            materialJson.put("havexampleattach", "0");
                        }
                        materialJsonList.add(materialJson);
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("totalcount", totalcount);
                dataJson.put("taskmateriallist", materialJsonList);
                dataJson.put("ouname", taskservice.getAuditTaskByGuid(taskguid, true).getResult().getOuname());
                log.info("=======结束调用getTaskCaseMaterialsByCaseguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "多情形材料信息获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getUIPath", method = RequestMethod.POST)
    public String getUIPath(@RequestBody String params) {
        try {
            log.info("=======开始调用getUIPath接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String Uipath = "http://59.206.96.198:8030/cform/getFormUI?formId=ShiGongXuKeZhengNEW&name=lichao&age=15"; // 获取UI的rest接口
            JSONObject jsonobject = HttplcUtils.postHttp(Uipath, null);
            JSONObject custom = jsonobject.getJSONObject("custom");
            String text = custom.getString("text").replace("获取表单失败：syntax error, pos 1, line 1, column 2", "");
            JSONObject dataJson = new JSONObject();
            dataJson.put("text", text);
            System.out.println("jsonobject:" + jsonobject);
            log.info("=======结束调用getUIPath接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取施工许可证表单成功", dataJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getUIPath接口参数：params【" + params + "】=======");
            log.info("=======getUIPath异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取表单失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    public String saveData(@RequestBody String params) {
        try {
            log.info("=======开始调用saveData接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String Uipath = "http://59.206.96.198:8030/cform/saveData?formId=ShiGongXuKeZhengNEW&name=lichao&age=2"; // 获取UI的rest接口
            System.out.println("jsonObject:" + jsonObject);
            String jsonobject = HttpUtil.doPostJson(Uipath, jsonObject.toString());
            // JSONObject jsonobject = HttplcUtils.postHttp(Uipath, map);
            log.info("=======结束调用getUIPath接口=======");
            JSONObject datajson = JSON.parseObject(jsonobject);
            return JsonUtils.zwdtRestReturn("1", "获取施工许可证表单成功", datajson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======saveData接口参数：params【" + params + "】=======");
            log.info("=======saveData异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取表单失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/loadBindData", method = RequestMethod.POST)
    public String loadBindData(@RequestBody String params) {
        try {
            log.info("=======开始调用loadBindData接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String Uipath = "http://59.206.96.198:8030/cform/getDataBind?formId=ShiGongXuKeZhengNEW&tag=1&name=2"; // 获取UI的rest接口
            JSONObject jsonobject = HttplcUtils.postHttp(Uipath, null);
            System.out.println("jsonobject:" + jsonobject);
            log.info("=======结束调用loadBindData接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取施工许可证表单成功", jsonobject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======loadBindData接口参数：params【" + params + "】=======");
            log.info("=======loadBindData异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取表单失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（办件申报提交时调用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/loadData", method = RequestMethod.POST)
    public String loadData(@RequestBody String params) {
        try {
            log.info("=======开始调用loadData接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String projectguid = jsonObject.getString("projectguid");
            String taskguid = jsonObject.getString("taskguid");
            AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            AuditProject project = auditProjectService.getAuditProjectByRowGuid(projectguid, task.getAreacode())
                    .getResult();
            String dataid = project.getStr("dataid");
            if (StringUtil.isNotBlank(dataid)) {
                String Uipath = "http://59.206.96.198:8030/cform/getFormStandardData?formId=ShiGongXuKeZhengNEW&formDataId="
                        + dataid; // 获取UI的rest接口
                JSONObject jsonobject = HttplcUtils.postHttp(Uipath, null);
                System.out.println("jsonobject:" + jsonobject);
                log.info("=======结束调用loadData接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取施工许可证表单成功", jsonobject.toString());
            } else {
                return JsonUtils.zwdtRestReturn("1", "获取施工许可证表单为空", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======loadData接口参数：params【" + params + "】=======");
            log.info("=======loadData异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取表单失败：" + e.getMessage(), "");
        }
    }

    /**
     * 办件公示详情的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
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

                String flowsn = obj.getString("flowsn");

                String password = obj.getString("password");

                // 1.1、获取办件标识
                String projectGuid = iJnAppRestService.getProjectGuidByFlowsnPassword(flowsn, password).getResult();

                // 1.2、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取办件基本信息
                String fields = " applydate,rowguid,taskguid,projectname,applyername,pviguid,status,ouname,receiveusername,receivedate,flowsn,acceptusername,acceptuserdate ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    // 4、获取事项基本信息
                    AuditTask auditTask = taskservice.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
                    if (auditTask != null) {
                        dataJson.put("taskname", auditProject.getProjectname());// 事项名称
                        Date applydate = auditProject.getApplydate();
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        dataJson.put("applydate", sd.format(applydate));
                        dataJson.put("itemid", auditTask.getItem_id());// 事项编码
                        dataJson.put("receivename", auditProject.getOuname());// 收件单位
                        dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                        dataJson.put("flowsn", auditProject.getFlowsn());// 办件编号
                        // 4.1、获取经办人与操作时间（优先取接件时间和接件人，没有的话取受理时间和受理人，否则显示--）
                        String userName = "--";
                        String date = "--";
                        userName = StringUtil.isNotBlank(auditProject.getAcceptusername())
                                ? auditProject.getAcceptusername()
                                : userName;
                        date = StringUtil.isNotBlank(auditProject.getAcceptuserdate())
                                ? EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(),
                                EpointDateUtil.DATE_TIME_FORMAT)
                                : date;
                        userName = StringUtil.isNotBlank(auditProject.getReceiveusername())
                                ? auditProject.getReceiveusername()
                                : userName;
                        dataJson.put("receiveusername", userName);// 经办人
                        dataJson.put("receivedate", date);// 接件时间
                        dataJson.put("status",
                                ZwdtConstant.getBanjanStatusKey(String.valueOf(auditProject.getStatus())));// 办件状态
                    }
                    log.info("=======结束调用getPublicProjectDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "暂未查到相关记录，请检查您输入的办件编码和查询密码!", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPublicProjectDetail异常信息：" + e.getMessage() + "=======");
            log.info("=======getPublicProjectDetail接口参数：params【" + params + "】=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取企业营业执照信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessLicense", method = RequestMethod.POST)
    public String getBusinessLicense(@RequestBody String params) {
        try {
            log.info("=======开始调用getBusinessLicense接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            JSONObject result = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certnum = obj.getString("certnum");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("ApiKey", "571374208735510528");
                String url = "http://59.206.96.178:9960/gateway/api/6/gsj_qydjbaxx?q=" + certnum;
                String results = com.epoint.cert.commonutils.HttpUtil.doGet(url, headers);
                if (results == null) {
                    return JsonUtils.zwdtRestReturn("0", "企业信息未获取到，请重新申报", "");
                }
                JSONObject res = JSONObject.parseObject(results);
                JSONObject data = res.getJSONObject("data");
                JSONObject data1 = data.getJSONObject("data");
                JSONObject data2 = data1.getJSONObject("data");
                if (data2 == null) {
                    return JsonUtils.zwdtRestReturn("0", "企业信息核验不通过", "");
                } else {
                    JSONObject baseinfo = data2.getJSONObject("baseinfo");
                    JSONArray priPerson = data2.getJSONArray("priPerson");
                    result.put("baseinfo", baseinfo);
                    result.put("priPerson", priPerson);
                    log.info("=======结束调用getBusinessLicense接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取营业执照信息成功", result.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessLicense接口参数：params【" + params + "】=======");
            log.info("=======getBusinessLicense异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取营业执照信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取个人营业执照信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getPersionLicense", method = RequestMethod.POST)
    public String getPersionLicense(@RequestBody String params) {
        try {
            log.info("=======开始调用getPersionLicense接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            JSONObject result = new JSONObject();
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String certnum = obj.getString("certnum");
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("ApiKey", "576417450254401536");
                String url = "http://59.206.96.178:9960/gateway/api/3/gsjgtdjxxcxjk?q=" + certnum;
                String results = com.epoint.cert.commonutils.HttpUtil.doGet(url, headers);
                if (results == null) {
                    return JsonUtils.zwdtRestReturn("0", "个人单位信息未获取到，请重新申报", "");
                }
                JSONObject res = JSONObject.parseObject(results);
                JSONObject data = res.getJSONObject("data");
                JSONObject data1 = data.getJSONObject("data");
                JSONObject data2 = data1.getJSONObject("data");
                if (data2 == null) {
                    return JsonUtils.zwdtRestReturn("0", "个人单位信息核验不通过", "");
                } else {
                    JSONObject baseinfo = data2.getJSONObject("baseinfo");
                    JSONObject operator = data2.getJSONObject("operator");
                    result.put("baseinfo", baseinfo);
                    result.put("operator", operator);
                    log.info("=======结束调用getPersionLicense接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取个人营业执照信息成功", result.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPersionLicense接口参数：params【" + params + "】=======");
            log.info("=======getPersionLicense异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取个人营业执照信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessDetail", method = RequestMethod.POST)
    public String getBusinessDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getBusinessDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String certnum = obj.getString("certnum");
                Record record = newshow2Service.getBusinessDetail(certnum);
                if (record != null) {
                    result.put("itemname", record.getStr("itemname"));
                    result.put("address", record.getStr("address"));
                    result.put("legal", record.getStr("legal"));
                    result.put("register", record.getStr("register"));
                    result.put("econ", record.getStr("econ"));
                    log.info("=======结束调用getBusinessDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取建筑业企业资质信息成功", result.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取建筑业企业资质信息失败", "");

                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessDetail接口参数：params【" + params + "】=======");
            log.info("=======getBusinessDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建筑业企业资质信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 修改建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/updateBusinessDetail", method = RequestMethod.POST)
    public String updateBusinessDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用updateBusinessDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String bgqitemname = obj.getString("bgqitemname");
                String bgqaddress = obj.getString("bgqaddress");
                String bgqlegal = obj.getString("bgqlegal");
                String bgqzczb = obj.getString("bgqzczb");
                String bgqjjxz = obj.getString("bgqjjxz");
                String itemcode = obj.getString("itemcode");
                Record record = newshow2Service.getBusinessDetail(itemcode);
                newshow2Service.updateBusinessDetailStatus(itemcode);
                JnBuildPart part = new JnBuildPart();
                part.setRowguid(UUID.randomUUID().toString());
                part.setOperatedate(new Date());
                part.setItemname(bgqitemname);
                part.setAddress(bgqaddress);
                part.setLegal(bgqlegal);
                part.setRegister(bgqzczb);
                part.setEcon(bgqjjxz);
                part.setCode(record.getStr("code"));
                part.setGrade(record.getStr("grade"));
                part.setItemcode(record.getStr("certificate"));
                part.setCertificate(record.getStr("certificate"));
                part.setCertdate(record.getDate("certdate"));
                part.setValiditytime(record.getDate("validitytime"));
                part.set("version", record.getInt("version") + 1);
                part.set("is_enable", "1");
                iJnBuildPartService.insert(part);
                log.info("=======结束调用updateBusinessDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "插入建筑业企业资质信息成功", result.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessDetail接口参数：params【" + params + "】=======");
            log.info("=======getBusinessDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建筑业企业资质信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params
     *            接口的入参
     * @return
     */
    /*
     * @RequestMapping(value = "/updateChargeStatus", method =
     * RequestMethod.POST) public String updateChargeStatus(@RequestBody String
     * params) { try { log.info("=======开始调用updateChargeStatus接口======="); //
     * 1、接口的入参转化为JSON对象 JSONObject jsonObject = JSONObject.parseObject(params);
     * JSONObject obj = (JSONObject) jsonObject.get("params"); String token =
     * jsonObject.getString("token"); if
     * (ZwdtConstant.SysValidateData.equals(token)) { JSONObject result = new
     * JSONObject(); String projectGuid = obj.getString("projectguid"); String
     * taskGuid = obj.getString("taskguid"); // 获取办件核价并且不是作废的记录
     * List<AuditProjectCharge> chargelist =
     * chargeservice.selectChargeByProjectAndIscancel(projectGuid).getResult();
     * if (chargelist != null && chargelist.size() > 0) { for
     * (AuditProjectCharge info : chargelist) { List<Record> list =
     * chargeDetailService.getShouQiChargeDetailDatePage(taskGuid,
     * info.getRowguid()).getResult(); if(list.size()>0 && list != null){
     * for(Record record : list){
     * jnProjectService.updateChargeDetail(record.get("rowguid")); } } } }
     * List<AuditProjectOperation> auditProjectOperations
     * =iAuditProjectOperation.getOperationListForDT(projectGuid).getResult();
     * if (auditProjectOperations != null && auditProjectOperations.size() > 0)
     * { for (AuditProjectOperation info1 : auditProjectOperations) { if
     * ("30".equals(info1.getOperateType())) { info1.set("chargestatus", "1");
     * iAuditProjectOperation.updateAuditProjectOperation(info1); } } }
     *
     * log.info("=======结束调用updateChargeStatus接口======="); return
     * JsonUtils.zwdtRestReturn("1", "修改成功", result.toString()); }else { return
     * JsonUtils.zwdtRestReturn("0", "身份验证失败！", ""); }
     *
     * } catch (Exception e) { e.printStackTrace();
     * log.info("=======updateChargeStatus接口参数：params【" + params + "】=======");
     * log.info("=======updateChargeStatus异常信息：" + e.getMessage() + "=======");
     * return JsonUtils.zwdtRestReturn("0", "修改收费信息状态失败：" + e.getMessage(), "");
     * } }
     */

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/cancelproject", method = RequestMethod.POST)
    public String cancelproject(@RequestBody String params) {
        try {
            log.info("=======开始调用cancelproject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String projectGuid = obj.getString("projectguid");
                String areacode = obj.getString("areacode");

                AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(projectGuid, areacode)
                        .getResult();
                //判断当前办件状态
                if(auditProject.getStatus()!=12){
                    return JsonUtils.zwdtRestReturn("0", "当前办件状态已发生改变，请刷新页面", "");
                }

                auditProject.setStatus(200);
                auditProjectService.updateProject(auditProject);

                String applyerguid = null;
                if (auditProject.getApplyertype().toString().equals(ZwfwConstant.APPLAYERTYPE_QY)) {
                    AuditRsCompanyBaseinfo auditrscompanybaseinfo = iAuditRsCompanyBaseinfo
                            .getAuditRsCompanyBaseinfoByRowguid(auditProject.getApplyeruserguid()).getResult();
                    if (auditrscompanybaseinfo != null) {
                        applyerguid = auditrscompanybaseinfo.getCompanyid();
                    }
                }
                Map<String, String> updateFieldMap = new HashMap<>();
                updateFieldMap.put("status=", "200");// 修改状态到外网预审退回
                Map<String, String> conditionMap = new HashMap<>();
                conditionMap.put("sourceguid=", auditProject.getRowguid());
                // if
                // (StringUtil.isNotBlank(auditProject.getOnlineapplyerguid()))
                // {
                // conditionMap.put("applyerguid=",
                // auditProject.getOnlineapplyerguid());
                // }
                auditOnlineProjectService.updateOnlineProjectJS(updateFieldMap, conditionMap);
                log.info("=======结束调用cancelproject接口=======");
                return JsonUtils.zwdtRestReturn("1", "修改成功", result.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======cancelproject接口参数：params【" + params + "】=======");
            log.info("=======cancelproject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "取消办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件过程信息（所有操作步骤都会列出）
     *
     * @param params 接口的入参
     * @return
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
                String fields = " rowguid,taskguid,projectname,status,charge_when,operatedate,applydate ";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
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
                        } else {
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
                        tempJson.put("projectguid", projectGuid);
                        String chargestatus = auditProjectOperations.get(i).getStr("chargestatus");
                        if ("30".equals(operateType)) {
                            if (StringUtil.isBlank(chargestatus)) {
                                chargestatus = "0";
                            }
                            if ("0".equals(chargestatus)) {
                                chargestatus = "未缴费";
                                tempJson.put("charge", "缴费");
                            } else if ("1".equals(chargestatus)) {
                                chargestatus = "已缴费";
                            }
                            tempJson.put("chargestatus", chargestatus);
                        }
                        if (ZwfwConstant.OPERATE_ZTJS.equals(operateType)) {
                            String remark = "";
                            AuditProjectUnusual unusual = iAuditProjectUnusual.getProjectUnusualByProjectGuidAndType(
                                    auditProject.getRowguid(), ZwfwConstant.BANJIANOPERATE_TYPE_ZT).getResult();
                            if (unusual != null) {
                                String pauseReason = unusual.getPauseReason();
                                remark += iCodeItemsService.getItemTextByCodeName("项目暂停原因", pauseReason) + ";";
                            }
                            remark += auditProjectOperations.get(i).getRemarks();
                            tempJson.put("remark", remark);
                        }
                        if (ZwfwConstant.OPERATE_BJYQ.equals(operateType)) {
                            String remark = "";
                            AuditProjectUnusual unusual = iAuditProjectUnusual.getProjectUnusualByProjectGuidAndType(
                                    auditProject.getRowguid(), ZwfwConstant.BANJIANOPERATE_TYPE_YQ).getResult();
                            if (unusual != null) {
                                remark += unusual.getDelayworkday() + "个工作日;";
                            }
                            remark += auditProjectOperations.get(i).getRemarks();
                            tempJson.put("remark", remark);
                        }
                        nodeJsonList.add(tempJson);
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("nodelist", nodeJsonList);
                    log.info("=======结束调用getProjectProcesslist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件过程信息成功", dataJson.toString());
                } else {
                    log.info("=======结束调用getProjectProcesslist接口=======");
                    return JsonUtils.zwdtRestReturn("0", "办件过程信息不存在！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectProcesslist接口参数：params【" + params + "】=======");
            log.info("=======getProjectProcesslist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件过程信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取建筑业企业资信信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getRoadpassDetail", method = RequestMethod.POST)
    public String getRoadpassDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getRoadpassDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String projectGuid = obj.getString("projectguid");
                AuditProject auditproject = weiChatProjectService.getAuditProjectByRowGuid(projectGuid);
                JSONObject results = new JSONObject();
                if (auditproject != null && StringUtil.isNotBlank(auditproject.get("dataObj_baseinfo"))) {
                    if ("90".equals(auditproject.getStr("status"))) {
                        JSONObject otherInfoJson = JSONObject.parseObject(auditproject.getStr("dataObj_baseinfo"));
                        for (Map.Entry<String, Object> en : otherInfoJson.entrySet()) {
                            results.put(en.getKey(), en.getValue());
                        }
                        log.info("=======结束调用getRoadpassDetail接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取道路通行证信息成功", results.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("1", "该办件未办理完结", results.toString());
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取道路通行证信息为空", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getRoadpassDetail接口参数：params【" + params + "】=======");
            log.info("=======getRoadpassDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取道路通行证信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取成品油企业资质信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getJnAiCpyDetail", method = RequestMethod.POST)
    public String getJnAiCpyDetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getJnAiCpyDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String certnum = obj.getString("certnum");
                Record record = iJnAiCpyService.getJnAiCpyDetail(certnum);
                if (record != null) {
                    result.put("itemname", record.getStr("name"));
                    result.put("legal", record.getStr("legal"));
                    String validitytime = record.getStr("validitytime");
                    if (StringUtil.isNotBlank(validitytime)) {
                        validitytime = validitytime.split("-")[1];
                    }
                    result.put("zzyxq", validitytime);
                    result.put("aracode", record.getStr("areacode"));
                    result.put("address", record.getStr("address"));
                    log.info("=======结束调用getBusinessDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取成品油企业资质信息成功", result.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取成品油企业资质信息失败", "");

                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getJnAiCpyDetail接口参数：params【" + params + "】=======");
            log.info("=======getJnAiCpyDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取成品油企业资质信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 修改成品油企业资质信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/updateJnAiCpyByCode", method = RequestMethod.POST)
    public String updateJnAiCpyByCode(@RequestBody String params) {
        try {
            log.info("=======开始调用updateJnAiCpyByCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String bgqitemname = obj.getString("bgqitemname");
                String bgqlegal = obj.getString("bgqlegal");
                String itemcode = obj.getString("itemcode");
                Record record = iJnAiCpyService.getJnAiCpyDetail(itemcode);
                iJnAiCpyService.updateJnAiCpyByCode(itemcode);
                JnAiCpy part = new JnAiCpy();
                part.setRowguid(UUID.randomUUID().toString());
                part.setOperatedate(new Date());
                part.setName(bgqitemname);
                part.setLegal(bgqlegal);
                part.setAddress(record.getStr("address"));
                part.setCode(record.getStr("code"));
                part.setCertno(record.getStr("certno"));
                part.set("areacode", record.getStr("areacode"));
                part.setCertificate(record.getStr("certificate"));
                part.setCertdate(record.getDate("certdate"));
                part.setValiditytime(record.getStr("validitytime"));
                Date fzrq = EpointDateUtil.addYear(new Date(), 5);
                part.setCertdate(new Date());
                part.setValiditytime(EpointDateUtil.convertDate2String(new Date(), "yyyyMMdd") + "-"
                        + EpointDateUtil.convertDate2String(fzrq, "yyyyMMdd"));
                part.set("version", record.getInt("version") + 1);
                part.set("is_enable", "1");
                iJnAiCpyService.insert(part);
                log.info("=======结束调用updateJnAiCpyByCode接口=======");
                return JsonUtils.zwdtRestReturn("1", "插入成品油企业资质信息成功", result.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateJnAiCpyByCode接口参数：params【" + params + "】=======");
            log.info("=======updateJnAiCpyByCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取成品油企业资质信息失败：" + e.getMessage(), "");
        }
    }

    // 评价
    // 我的办件评价
    @RequestMapping(value = "/getgradeinfo", method = RequestMethod.POST)
    public String getgradeinfo(@RequestBody String params) {
        try {

            JSONObject jsonObject = JSONObject.parseObject(params);

            JSONObject object = (JSONObject) jsonObject.get("params");
            if (object == null) {
                return JsonUtils.zwdtRestReturn("0", "参数结构不正确，请检查参数结构", "");
            }

            // 分数
            String grade = object.getString("grade");

            List<Record> list = iSpaceAcceptService.getGradinfo(grade);
            JSONObject dataJson = new JSONObject();
            dataJson.put("items", list);
            return JsonUtils.zwdtRestReturn("1", "获取评价信息成功", dataJson.toJSONString());

        } catch (Exception e) {
            log.debug("=======getgradeinfo接口参数：params【" + params + "】=======");
            log.debug("=======getgradeinfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "评价失败" + e.getMessage(), "");
        }
    }

    /**
     * 新增咨询投诉接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/addZcSsp", method = RequestMethod.POST)
    public String addZcSsp(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用addZcSsp接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 1.2、获取接口的入参
                    String address = obj.getString("address");// 地理位置
                    String areacode = obj.getString("areacode");// 辖区编码
                    String attachguid = obj.getString("attachguid");// 图片内容
                    String content = obj.getString("content");// 问题描述
                    String phone = obj.getString("phone");// 手机号
                    String username = obj.getString("username");// 用户姓名
                    String videoattachguid = obj.getString("videoattachguid");// 视频内容

                    ZjZcssp zcssp = new ZjZcssp();

                    String consultGuid = UUID.randomUUID().toString();
                    if (StringUtil.isBlank(attachguid)) {
                        attachguid = "0";
                    }
                    if (StringUtil.isBlank(videoattachguid)) {
                        videoattachguid = "0";
                    }
                    zcssp.setRowguid(consultGuid);
                    zcssp.setUsername(username);
                    zcssp.setPhone(phone);
                    zcssp.setAddress(address);
                    zcssp.setAreacode(areacode);
                    zcssp.setAttachguid(attachguid);
                    zcssp.set("videoattachguid", videoattachguid);
                    zcssp.setContent(content);
                    zcssp.setCreatetime(new Date());

                    izjZcsspService.insert(zcssp);

                    JSONObject dataJson = new JSONObject();
                    dataJson.put("rowguid", consultGuid);

                    log.info("=======结束调用addConsult接口=======");
                    return JsonUtils.zwdtRestReturn("1", "新增成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======addConsult接口参数：params【" + params + "】=======");
            log.info("=======addConsult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "新增咨询投诉失败：" + e.getMessage(), "");
        }
    }

    /**
     * 新增咨询投诉接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getZcSspDetail", method = RequestMethod.POST)
    public String getZcSspDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getZcSspDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.2、获取接口的入参
                String rowguid = obj.getString("rowguid");
                ZjZcssp zcssp = izjZcsspService.find(rowguid);
                if (zcssp != null) {
                    List<Record> itemresultphotos = new ArrayList<Record>();
                    String attachguid = zcssp.getAttachguid();// 图片内容
                    String videoattachguid = zcssp.getStr("videoattachguid");// 视频内容
                    if (!"0".equals(attachguid) && StringUtil.isNotBlank(attachguid)) {
                        List<FrameAttachInfo> attach = attachService.getAttachInfoListByGuid(attachguid);
                        if (attach != null && attach.size() > 0) {
                            for (FrameAttachInfo attachinfo : attach) {
                                Record record = new Record();
                                record.set("photoname", attachinfo.getAttachFileName());
                                record.set("photourl",
                                        "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachinfo.getAttachGuid());
                                itemresultphotos.add(record);
                            }
                        }
                    }
                    zcssp.set("photo", itemresultphotos);

                    if (!"0".equals(videoattachguid) && StringUtil.isNotBlank(videoattachguid)) {
                        List<FrameAttachInfo> attach = attachService.getAttachInfoListByGuid(videoattachguid);
                        if (attach != null && attach.size() > 0) {
                            zcssp.set("videoname", attach.get(0).getAttachFileName());
                            zcssp.set("videourl",
                                    "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attach.get(0).getAttachGuid());
                        }
                    }
                    zcssp.set("createtime",
                            EpointDateUtil.convertDate2String(zcssp.getCreatetime(), "yyyy-MM-dd HH:mm"));
                    dataJson.put("detail", zcssp);
                    log.info("=======结束调用getZcSspDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取随手拍详情成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取随手拍详情失败", "");
                }

            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getZcSspDetail接口参数：params【" + params + "】=======");
            log.info("=======getZcSspDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取随手拍详情失败：" + e.getMessage(), "");
        }
    }

    @PostMapping("getCaseMaterialsByCaseguid")
    public String getCaseMaterialsByCaseguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCaseMaterialsByCaseguid接口=======");
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
                    List<AuditTaskOption> options = null;
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (auditTaskCase != null) {
                        taskGuid = auditTaskCase.getTaskguid();
                        String selectedoptions = auditTaskCase.getSelectedoptions();
                        if (StringUtil.isNotBlank(selectedoptions) && !selectedoptions.startsWith("{")
                                && selectedoptions.contains(";")) {
                            String[] split = selectedoptions.split(";");
                            sql.in("rowguid", StringUtil.joinSql(split));
                            sql.isNotBlank("MATERIALIDS");
                            options = optionService.findListByCondition(sql.getMap()).getResult();
                        }
                    }
                    // 获取情形材料（情形表中仅有非必要材料）
                    List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                            .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                    String attachurl = "http://59.206.96.143:25990/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
                    String officeweb365url = ConfigUtil.getConfigValue("officeweb365url");
                    for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                        if (auditTaskMaterialCase != null) {
                            // int necessity =
                            // auditTaskMaterialCase.getNecessity();
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
                                    JSONObject materialJson = this.getMaterialJson(auditTaskMaterial, attachurl,
                                            officeweb365url);
                                    if (options != null && !options.isEmpty()) {
                                        for (AuditTaskOption option : options) {
                                            if (StringUtil.isNotBlank(option.getMaterialids()) && option
                                                    .getMaterialids().contains(auditTaskMaterial.getMaterialid())) {
                                                materialJson.put("remark",
                                                        StringUtil.isNotBlank(option.getOptionnote())
                                                                ? option.getOptionnote()
                                                                : "无");
                                                break;
                                            }
                                        }
                                    }
                                    materialJsonList.add(materialJson);
                                }
                            }
                        }
                    }
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("taskmateriallist", materialJsonList);
                log.info("=======结束调用getCaseMaterialsByCaseguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "多情形材料信息获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseMaterialsByCaseguid接口参数：params【" + params + "】=======");
            log.info("=======getCaseMaterialsByCaseguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "多情形材料信息获取失败：" + e.getMessage(), "");
        }
    }

    public JSONObject getMaterialJson(AuditTaskMaterial auditTaskMaterial, String attachurl, String office365url) {
        JSONObject materialJson = new JSONObject();
        materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
        materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
        // 4.1、获取填报示例对应附件标识
        if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
            int exampleAttachCount = attachService.getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
            if (exampleAttachCount > 0) {
                materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
            }
        }
        // 4.2、获取空白模板对应附件标识
        if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
            int templateAttachCount = attachService
                    .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
            if (templateAttachCount > 0) {
                materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
            }
        }
        // 4.3、获取材料来源渠道
        String materialsource;
        if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
            materialsource = "申请人自备";
        } else {
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
                StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0" : auditTaskMaterial.getPage_num()); // 份数
        materialJson.put("standard",
                StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无" : auditTaskMaterial.getStandard());// 受理标准
        materialJson.put("fileexplain",
                StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无" : auditTaskMaterial.getFile_explian());// 填报须知
        // 查询材料图例
        if (StringUtil.isNotBlank(auditTaskMaterial.getStr("egpic_clengguid"))) {
            List<FrameAttachInfo> egpicattachList = attachService
                    .getAttachInfoListByGuid(auditTaskMaterial.getStr("egpic_clengguid"));
            if (egpicattachList != null && !egpicattachList.isEmpty()) {
                FrameAttachInfo attachInfo = egpicattachList.get(0);
                String url = office365url + OfficeWebUrlEncryptUtil
                        .getEncryptUrl(attachurl + attachInfo.getAttachGuid(), attachInfo.getContentType());
                materialJson.put("egpicattachurl", url);
            }
        }

        return materialJson;
    }

    /**
     * 办件结果详情的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectResult", method = RequestMethod.POST)
    public String getProjectResult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectResult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 获取用户注册信息
                String jsonuserstr = jsonObject.getString("auditonlineregister");
                // JSONObject jsonuser = null;
                // if (StringUtil.isNotBlank(jsonuserstr)) {
                // jsonuser = JSONObject.parseObject(jsonuserstr);
                // }
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request, jsonuser);
                // if (auditOnlineRegister != null) {
                // ZwdtUserAuthValid zwdtUserAuthValid = new
                // ZwdtUserAuthValid();
                // if (!zwdtUserAuthValid.userValid(auditOnlineRegister,
                // projectGuid,
                // ZwdtConstant.USERVALID_SPI_INS)) {
                // return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                // }
                // } else {
                // return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                // }
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                // if (auditOnlineRegister != null) {
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                List<JSONObject> resultlist = new ArrayList<JSONObject>();
                // 3、获取办件
                String fields = "rowguid,projectname,task_id,taskguid,centerguid,flowsn,certrowguid,status";
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
                        .getResult();
                if (auditProject != null) {
                    if (auditProject.getStatus() == 90) {
                        String certrowugid = auditProject.getCertrowguid();
                        if (StringUtil.isNotBlank(certrowugid)) {
                            if (certrowugid.contains(";")) {
                                String[] guids = certrowugid.split(";");
                                for (String guid : guids) {
                                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(guid);
                                    /***
                                     * 建筑注销事项证照不展示证照在网厅
                                     */
                                    if (certInfo != null && !"建筑注销事项证照".equals(certInfo.getRemark())) {
                                        // 5、获取结果对应的附件
                                        List<JSONObject> attachList = iCertAttachExternal
                                                .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                        if (attachList != null && !attachList.isEmpty()) {
                                            for (JSONObject attachJson : attachList) {
                                                JSONObject resultJson = new JSONObject();
                                                resultJson.put("resultattachname", attachJson.get("attachname"));
                                                resultJson.put("resultattachurl",
                                                        WebUtil.getRequestCompleteUrl(request)
                                                                + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                                + attachJson.getString("attachguid") + "&filename="
                                                                + attachJson.get("attachname"));// 附件图标
                                                resultlist.add(resultJson);
                                            }
                                        }
                                    }
                                }
                                /*List<FrameAttachInfo> listattach = attachService
                                        .getAttachInfoListByGuid(auditProject.getRowguid());
                                // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                                if (listattach != null && !listattach.isEmpty()) {
                                    for (FrameAttachInfo attachInfo : listattach) {
                                        JSONObject resultJson = new JSONObject();
                                        resultJson.put("resultattachname", attachInfo.getAttachFileName());
                                        resultJson.put("resultattachurl",
                                                "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                        + attachInfo.getAttachGuid() + "&filename="
                                                        + attachInfo.getAttachFileName());// 附件图标
                                        resultlist.add(resultJson);
                                    }
                                }*/
                            } else {
                                CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(certrowugid);
                                /***
                                 * 建筑注销事项证照不展示证照在网厅
                                 */
                                if (certInfo != null && !"建筑注销事项证照".equals(certInfo.getRemark())) {
                                    // 5、获取结果对应的附件
                                    List<JSONObject> attachList = iCertAttachExternal
                                            .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                    if (attachList != null && !attachList.isEmpty()) {
                                        for (JSONObject attachJson : attachList) {
                                            JSONObject resultJson = new JSONObject();
                                            resultJson.put("resultattachname", attachJson.get("attachname"));
                                            resultJson.put("resultattachurl",
                                                    WebUtil.getRequestCompleteUrl(request)
                                                            + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                            + attachJson.getString("attachguid") + "&filename="
                                                            + attachJson.get("attachname"));// 附件图标
                                            resultlist.add(resultJson);
                                        }
                                    }
                                }

                                List<FrameAttachInfo> listattach = attachService
                                        .getAttachInfoListByGuid(auditProject.getRowguid());
                                if (listattach != null && !listattach.isEmpty()) {
                                    // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                                    for (FrameAttachInfo attachInfo : listattach) {
                                        JSONObject resultJson = new JSONObject();
                                        resultJson.put("resultattachname", attachInfo.getAttachFileName());
                                        resultJson.put("resultattachurl",
                                                "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                        + attachInfo.getAttachGuid() + "&filename="
                                                        + attachInfo.getAttachFileName());// 附件图标
                                        resultlist.add(resultJson);
                                    }
                                }
                            }
                        } else {
                            List<FrameAttachInfo> listattach = attachService
                                    .getAttachInfoListByGuid(auditProject.getRowguid());
                            // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                            for (FrameAttachInfo attachInfo : listattach) {
                                JSONObject resultJson = new JSONObject();
                                resultJson.put("resultattachname", attachInfo.getAttachFileName());
                                resultJson.put("resultattachurl",
                                        "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                                + attachInfo.getAttachGuid() + "&filename="
                                                + attachInfo.getAttachFileName());// 附件图标
                                resultlist.add(resultJson);
                            }
                        }
                    }
                    dataJson.put("resultlist", resultlist);
                }
                log.info("=======结束调用getProjectResult接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件结果详情成功", dataJson.toString());

            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
            }
            // } else {
            // return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            // }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectResult接口参数：params【" + params + "】=======");
            log.info("=======getProjectResult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件结果详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取电子表单信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getEformInfo", method = RequestMethod.POST)
    public String getEformInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getEformInfo接口=======");
            // 1、接口传入参数转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskguid = obj.getString("taskguid");
                String proejctguid = obj.getString("projectguid");
                JSONObject formInfo = new JSONObject();
                AuditTaskExtension task = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskguid, true).getResult();
                if (task != null) {
                    formInfo.put("formid", task.getStr("formid"));
                }
                if (StringUtil.isBlank(proejctguid)) {
                    formInfo.put("yewuGuid", UUID.randomUUID().toString());
                } else {
                    formInfo.put("yewuGuid", proejctguid);
                }
                formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                formInfo.put("epointUrl", configServicce.getFrameConfigValue("epointsformurlwt"));

                AuditProject proejct = iAuditProject.getAuditProjectByRowGuid(proejctguid, null).getResult();

                // if (proejct == null) {
                // return JsonUtils.zwdtRestReturn("0", "办件信息未获取到！", "");
                // }

                if (task != null && "463".equals(task.getStr("formid")) && proejct != null) {
                    String certnum = proejct.getCertnum();
                    String sql = "select * from teacher_health_report where sfz = ?";
                    TeacherHealthReport teacher = iTeacherHealthReportService.find(sql, certnum);
                    log.info("teacher:" + teacher);
                    if (teacher != null) {
                        JSONObject commondata = new JSONObject();
                        commondata.put("xm", teacher.getName());
                        commondata.put("hjssd", teacher.getCounty());
                        commondata.put("sfzh", teacher.getSfz());
                        commondata.put("tjsj",
                                EpointDateUtil.convertDate2String(teacher.getTjdate(), EpointDateUtil.DATE_FORMAT));
                        commondata.put("yymc", teacher.getHospitalname());
                        commondata.put("wbk10", teacher.getIspass());
                        formInfo.put("commondata", commondata);
                    }
                }

                JSONObject dataJson = new JSONObject();
                String iscreatemateria = task.getStr("iscreatemateria");
                dataJson.put("iscreatemateria", iscreatemateria);
                dataJson.put("formInfo", formInfo);
                log.info(dataJson);
                return JsonUtils.zwdtRestReturn("1", "获取电子表单信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getEformInfo接口参数：params【" + params + "】=======");
            log.info("=======getEformInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取电子表单信息失败：" + e.getMessage(), "");
        }

    }

    /**
     * 获取电子表单信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYjsEformInfo", method = RequestMethod.POST)
    public String getYjsEformInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getYjsEformInfo接口=======");
            // 1、接口传入参数转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String businessGuid = obj.getString("businessguid");
                JSONObject formInfo = new JSONObject();
                // 2、获取主题信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();

                if (auditSpBusiness != null) {
                    // formInfo.put("formid",
                    // auditSpBusiness.getStr("yjsformid"));
                    formInfo.put("formid", auditSpBusiness.getStr("yjsformid"));
                }

                formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                formInfo.put("epointUrl", configServicce.getFrameConfigValue("epointsformurlwt"));

                JSONObject dataJson = new JSONObject();
                dataJson.put("formInfo", formInfo);
                return JsonUtils.zwdtRestReturn("1", "获取电子表单信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYjsEformInfo接口参数：params【" + params + "】=======");
            log.info("=======getYjsEformInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取电子表单信息失败：" + e.getMessage(), "");
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
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /*----------------------建设工程质量检测-------------------*/
    @RequestMapping(value = "/getJsgczljc", method = RequestMethod.POST)
    public String getJsgczljc(@RequestBody String params) {
        try {
            log.info("=======开始调用getJsgczljc接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String certnum = obj.getString("certnum");
                Record record = iJnAiCpyService.getJsgczljc(certnum);
                if (record != null) {
                    result.put("bgqjsfzr", record.getStr("jsfzr"));
                    result.put("bgqfr", record.getStr("fddbr"));
                    result.put("bgqdz", record.getStr("xxdz"));
                    result.put("bgqqymc", record.getStr("qymc"));

                    log.info("=======结束调用getJsgczljc接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取建设工程质量检测信息成功", result.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取建设工程质量检测信息失败", "");

                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getJsgczljc接口参数：params【" + params + "】=======");
            log.info("=======getJsgczljc异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建设工程质量检测信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * [修改建设工程质量检测信息]
     *
     * @param params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/updateJsgczljc", method = RequestMethod.POST)
    public String updateJsgczljc(@RequestBody String params) {
        try {
            log.info("=======开始调用updateJsgczljc接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String bghjsfzr = obj.getString("bghjsfzr");
                String itemcode = obj.getString("itemcode");
                String bghqymc = obj.getString("bghqymc");
                String bghfr = obj.getString("bghfr");
                String bghdz = obj.getString("bghdz");
                String projectguid = obj.getString("projectguid");
                AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);
                if (auditProject != null) {
                    String projectname = auditProject.getProjectname();
                    System.out.println("updateJsgczljc中projectname：" + projectname);
                    if (projectname.contains("技术负责人变更")) {
                        iJnAiCpyService.updateJsgczljc(itemcode, bghjsfzr);
                    } else if (projectname.contains("地址变更")) {
                        iJnAiCpyService.updateJsgczljcbghdz(itemcode, bghdz);
                    } else if (projectname.contains("法定代表人变更")) {
                        iJnAiCpyService.updateJsgczljcbghfr(itemcode, bghfr);
                    } else if (projectname.contains("名称变更")) {
                        iJnAiCpyService.updateJsgczljcbghqymc(itemcode, bghqymc);
                    }
                }
                log.info("=======结束调用updateJsgczljc接口=======");
                return JsonUtils.zwdtRestReturn("1", "插入建设工程质量检测信息成功", result.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateJnAiCpyByCode接口参数：params【" + params + "】=======");
            log.info("=======updateJnAiCpyByCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建设工程质量检测信息失败：" + e.getMessage(), "");
        }
    }

    /*-----------------房地产-------------------*/
    @RequestMapping(value = "/getFdczzzs", method = RequestMethod.POST)
    public String getFdczzzs(@RequestBody String params) {
        try {
            log.info("=======开始调用getFdczzzs接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String certnum = obj.getString("certnum");
                Record record = iJnAiCpyService.getFdczzzs(certnum);
                if (record != null) {
                    result.put("bgqssxq", record.getStr("qhdm"));
                    result.put("gxqzczb", record.getStr("zczb"));
                    result.put("gxqqydz", record.getStr("dz"));
                    result.put("gxqqyfr", record.getStr("fddbr"));
                    result.put("gxqqym", record.getStr("qymc"));

                    log.info("=======结束调用getFdczzzs接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取房地产信息成功", result.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取房地产信息失败", "");

                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getFdczzzs接口参数：params【" + params + "】=======");
            log.info("=======getFdczzzs异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取房地产信息失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/updateFdczzzs", method = RequestMethod.POST)
    public String updateFdczzzs(@RequestBody String params) {
        try {
            log.info("=======开始调用updateJsgczljc接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject result = new JSONObject();
                String bghssxq = obj.getString("bghssxq");
                String itemcode = obj.getString("itemcode");
                String gxhzczb = obj.getString("gxhzczb");
                String gxhqydz = obj.getString("gxhqydz");
                String gxhqyfr = obj.getString("gxhqyfr");
                String gxhqym = obj.getString("gxhqym");

                iJnAiCpyService.updateFdczzzs(itemcode, bghssxq, gxhzczb, gxhqydz, gxhqyfr, gxhqym);

                log.info("=======结束调用updateJsgczljc接口=======");
                return JsonUtils.zwdtRestReturn("1", "插入建设工程质量检测信息成功", result.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======updateJnAiCpyByCode接口参数：params【" + params + "】=======");
            log.info("=======updateJnAiCpyByCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建设工程质量检测信息失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getCheckBodyDetail", method = RequestMethod.POST)
    public String getCheckBodyDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getCheckBodyDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String token = jsonObject.getString("token");
            AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
            if (auditOnlineRegister != null) {
                if (ZwdtConstant.SysValidateData.equals(token)) {
                    String certnum = auditOnlineRegister.getIdnumber();
                    String sql = "select * from teacher_health_report where sfz = ?";
                    TeacherHealthReport teacher = iTeacherHealthReportService.find(sql, certnum);
                    log.info("teacher:" + teacher);
                    JSONObject commondata = new JSONObject();
                    if (teacher != null) {
                        commondata.put("name", teacher.getName());
                        String areacode = teacher.getCounty();
                        commondata.put("areacode", areacode);
                        String areaname = iCodeItemsService.getItemTextByCodeName("好差评辖区", areacode);
                        if (StringUtil.isNotBlank(areaname)) {
                            commondata.put("areaname", areaname);
                        }
                        commondata.put("sfz", teacher.getSfz());
                        commondata.put("tjdate",
                                EpointDateUtil.convertDate2String(teacher.getTjdate(), EpointDateUtil.DATE_FORMAT));
                        commondata.put("hospitalname", teacher.getHospitalname());
                        commondata.put("ispass", teacher.getIspass());
                        log.info("=======结束调用getCheckBodyDetail接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取体检信息成功", commondata.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "未查询到体检信息", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCheckBodyDetail接口参数：params【" + params + "】=======");
            log.info("=======getCheckBodyDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取建设工程质量检测信息失败：" + e.getMessage(), "");
        }
    }
}
