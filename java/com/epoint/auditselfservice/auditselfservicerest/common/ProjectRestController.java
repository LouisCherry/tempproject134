package com.epoint.auditselfservice.auditselfservicerest.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueuserinfo.inter.IAuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditznsbselfmachineproject.domain.AuditZnsbSelfmachineproject;
import com.epoint.basic.auditqueue.auditznsbselfmachineproject.inter.IAuditZnsbSelfmachineproject;
import com.epoint.basic.auditqueue.auditznsbzzsbdemo.domain.AuditZnsbZzsbDemo;
import com.epoint.basic.auditqueue.auditznsbzzsbdemo.inter.IAuditZnsbZzsbDemoService;
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
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QrcodeUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

@RestController
@RequestMapping("/selfserviceproject")
public class ProjectRestController
{
    @Autowired
    private IAuditOrgaServiceCenter servicecenterservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditProject projectservice;

    @Autowired
    private IAuditOrgaWindow windowservice;

    @Autowired
    private ICodeItemsService codeitemsservice;

    @Autowired
    private IAuditOnlineIndividual individualservice;

    @Autowired
    private IHandleProject handleProjectservice;

    @Autowired
    private IAuditTask taskservice;

    @Autowired
    private IAuditOnlineProject onlineProjectservice;

    @Autowired
    private IAuditProjectMaterial projectMaterialservice;

    @Autowired
    private IAuditTaskExtension taskExtensionservice;

    @Autowired
    private IAuditTaskMaterial taskMaterialservice;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IWFInstanceAPI9 wfinstanceapi;

    @Autowired
    private IAuditTaskResult taskresultservice;

    @Autowired
    private IAuditOrgaConfig auditconfigservice;

    @Autowired
    private IAuditTaskCase auditTaskCaseService;

    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseService;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAuditZnsbSelfmachineproject auditZnsbSelfmachineprojectService;

    @Autowired
    private IAuditZnsbZzsbDemoService auditZnsbZzsbDemoService;

    @Autowired
    private IAuditQueueUserinfo userinfoservice;

    @Autowired
    private IAuditOnlineRegister registerservice;

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IUserService userservice;

    @Autowired
    private IRoleService roleservice;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

    /**
     * 根据流水号获取办件
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectByFlowSN", method = RequestMethod.POST)
    public String getProjectByFlowSN(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String flowsn = obj.getString("flowsn");
            String areacode = obj.getString("areacode");
            String fields = "  rowguid ";
            AuditProject project = projectservice.getAuditProjectByFlowsn(fields, flowsn, areacode).getResult();

            if (project != null) {
                dataJson.put("projectguid", project.getRowguid());

            }
            else {
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("flowsn", flowsn);
                sql.eq("areacode", areacode);
                sql.setSelectFields(" rowguid,projectname,status,applydate,sourceguid,type,taskguid,flowsn ");
                List<AuditOnlineProject> list = onlineProjectservice.getAuditOnlineProjectPageData(sql.getMap(),
                        QueueConstant.CONSTANT_INT_ZERO, QueueConstant.CONSTANT_INT_ONE, "", "").getResult().getList();
                if (!list.isEmpty()) {
                    AuditOnlineProject onlineproject = list.get(0);
                    dataJson.put("projectguid", onlineproject.getSourceguid());
                    dataJson.put("businessguid", onlineproject.getTaskguid());
                    dataJson.put("projectname", onlineproject.getProjectname());
                    dataJson.put("applydate", EpointDateUtil.convertDate2String(onlineproject.getApplydate()));
                    dataJson.put("status", onlineproject.getStatus());
                    dataJson.put("statustext", setStatusText(Integer.parseInt(onlineproject.getStatus())));
                    dataJson.put("type", onlineproject.getType());
                    dataJson.put("flowsn", onlineproject.getFlowsn()); // 办件编号
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "您查询的办件编号不存在！", "");
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取流水号前缀
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectPre", method = RequestMethod.POST)
    public String getProjectPre(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String centerguid = obj.getString("centerguid");
            String config = handleConfigservice.getFrameConfig("AS_FLOWSN_PRE", centerguid).getResult();
            if (StringUtil.isNotBlank(config)) {
                dataJson.put("projectpre", config);
            }
            else {
                dataJson.put("projectpre", "STD");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 根据二维码获取办件
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectByCode", method = RequestMethod.POST)
    public String getProjectByCode(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String code = obj.getString("code");
            String areacode = obj.getString("areacode");
            String fields = "  rowguid ";
            AuditProject project;

            if (code.contains("projectguid=")) {
                code = code.substring(code.indexOf("projectguid="));
                code = code.replace("projectguid=", "");
                project = projectservice.getAuditProjectByRowGuid(fields, code, areacode).getResult();

            }
            else if (code.contains("flowsn=")) {
                code = code.substring(code.indexOf("flowsn="));
                code = code.replace("flowsn=", "");
                project = projectservice.getAuditProjectByFlowsn(fields, code, areacode).getResult();
            }
            else {

                project = null;
            }
            if (project != null) {
                dataJson.put("projectguid", project.getRowguid());

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "您查询的办件不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 根据获取办件详情
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouname,promise_day,applydate,applyway,windowguid,certnum,contactmobile,contactphone,address,contactemail,contactpostcode,remark,contactcertnum,contactperson,centerguid,legal,legalid,certtype";
            AuditProject project = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            if (project != null) {

                dataJson.put("projectguid", project.getRowguid());
                dataJson.put("projectname", project.getProjectname());
                dataJson.put("flowsn", project.getFlowsn());
                dataJson.put("ouname", project.getOuname());

                dataJson.put("promiseday", project.getPromise_day() + "个工作日");

                dataJson.put("applydate", EpointDateUtil.convertDate2String(project.getApplydate()));

                dataJson.put("applyway", project.getApplyway());
                dataJson.put("applywaytext",
                        codeitemsservice.getItemTextByCodeName("申请方式", String.valueOf(project.getApplyway())));
                dataJson.put("status", project.getStatus());
                dataJson.put("statustext",
                        codeitemsservice.getItemTextByCodeName("办件状态", String.valueOf(project.getStatus())));
                AuditOrgaWindow window = windowservice.getWindowByWindowGuid(project.getWindowguid()).getResult();
                if (window != null) {
                    dataJson.put("windowtel", window.getTel());
                }
                else {
                    dataJson.put("windowtel", "");
                }
                dataJson.put("applyername", project.getApplyername());
                dataJson.put("certnum", project.getCertnum());
                dataJson.put("contactphone", project.getContactphone());
                if ("8".equals(project.getStatus().toString()) && StringUtil.isNotBlank(project.getCertnum())) {
                    AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(project.getCertnum())
                            .getResult();
                    AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(project.getCertnum())
                            .getResult();
                    if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                        dataJson.put("address", auditQueueUserinfo.getAddress());
                    }
                    else {
                        dataJson.put("address", "");
                    }
                    if (StringUtil.isNotBlank(register)) {
                        dataJson.put("contactmobile", register.getMobile());
                    }
                    else {
                        dataJson.put("contactmobile", "");
                    }

                }
                else {
                    dataJson.put("contactmobile", project.getContactmobile());
                    dataJson.put("address", project.getAddress());
                }

                dataJson.put("contactemail", project.getContactemail());
                dataJson.put("contactperson", project.getContactperson());

                dataJson.put("taskguid", project.getTaskguid());
                dataJson.put("contactpostcode", project.getContactpostcode());
                dataJson.put("contactidnum", project.getContactcertnum());
                dataJson.put("remark", project.getRemark());
                dataJson.put("legal", project.getLegal());
                dataJson.put("legalid", project.getLegalid());
                dataJson.put("certtype", project.getCerttype());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", project.getCenterguid());
                sql.eq("configname", "AS_PROJECT_URL");
                AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    dataJson.put("projecturl", config.getConfigvalue());
                }
                else {
                    dataJson.put("projecturl", "");
                }

                // 查询办件已提交的材料
                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("projectguid", projectguid);
                sql2.in("status ",
                        "'" + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC + "'");
                List<AuditProjectMaterial> auditProjectMaterials = projectMaterialservice
                        .selectProjectMaterialByCondition(sql2.getMap()).getResult();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                Map<String, Object> data1 = null;

                if (!auditProjectMaterials.isEmpty()) {
                    for (int i = 0; i < auditProjectMaterials.size(); i++) {
                        data1 = new HashMap<String, Object>(16);
                        data1.put("taskmaterial", (i + 1) + "、" + auditProjectMaterials.get(i).getTaskmaterial());
                        dataList.add(data1);
                    }
                }
                dataJson.put("material", dataList);

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 
     * 获取办件过程信息（所有操作步骤都会列出）
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectProcesslist", method = RequestMethod.POST)
    public String getProjectProcesslist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectProcesslist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取办件标识
            String projectGuid = obj.getString("projectguid");
            // 1.2、获取辖区编码
            String areaCode = obj.getString("areacode");
            // 2、获取办件基本信息
            String fields = " rowguid,taskguid,projectname,status,charge_when,operatedate,applydate,applyertype,applyername,applyeruserguid ";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectGuid, areaCode)
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
                                auditProjectOperations.get(i - 1).getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT);
                    }
                    else {
                        beginTime = EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                EpointDateUtil.DATE_TIME_FORMAT);
                    }
                    // 手机端要求返回操作类型中文
                    String operateType = auditProjectOperations.get(i).getOperateType();
                    String operateTypeName = ZwdtConstant.getOperateTypeKey(operateType);
                    JSONObject tempJson = new JSONObject();
                    tempJson.put("activityname", operateTypeName);
                    tempJson.put("nodestatus", "0"); // 默认展示
                    tempJson.put("operationdate", EpointDateUtil
                            .convertDate2String(auditProjectOperations.get(i).getOperatedate(), "yyyy-MM-dd HH:mm"));
                    tempJson.put("begintime", beginTime);
                    tempJson.put("operateusername", auditProjectOperations.get(i).getOperateusername());
                    tempJson.put("remark", auditProjectOperations.get(i).getRemarks());
                    // 去除重复步骤
                    boolean issame = false;
                    for (JSONObject nodeJson : nodeJsonList) {
                        if (StringUtil.isNotBlank(operateTypeName)
                                && operateTypeName.equals(nodeJson.getString("activityname"))) {
                            issame = true;
                            break;
                        }
                    }
                    if (!issame) {
                        nodeJsonList.add(tempJson);
                    }

                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("workitemlist", nodeJsonList);
                log.info("=======结束调用getProjectProcesslist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件过程信息成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getProjectProcesslist接口=======");
                return JsonUtils.zwdtRestReturn("0", "办件过程信息不存在！", "");
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
     * 根据获取办件详情
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectDetail_en", method = RequestMethod.POST)
    public String getProjectDetail_en(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouname,promise_day,applydate,applyway,windowguid,certnum,contactmobile,contactphone,address,contactemail,contactpostcode,remark,contactcertnum,contactperson,centerguid,legal,legalid,certtype";
            AuditProject project = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            if (project != null) {

                dataJson.put("projectguid", project.getRowguid());
                dataJson.put("projectname", project.getProjectname());
                dataJson.put("flowsn", project.getFlowsn());
                dataJson.put("ouname", project.getOuname());

                dataJson.put("promiseday", project.getPromise_day() + "Workday");

                dataJson.put("applydate", EpointDateUtil.convertDate2String(project.getApplydate()));

                dataJson.put("applyway", project.getApplyway());
                // 英文版直接修改了代码项对应的值
                dataJson.put("applywaytext",
                        codeitemsservice.getItemTextByCodeName("申请方式", String.valueOf(project.getApplyway())));

                dataJson.put("status", project.getStatus());
                dataJson.put("statustext",
                        codeitemsservice.getItemTextByCodeName("办件状态", String.valueOf(project.getStatus())));
                AuditOrgaWindow window = windowservice.getWindowByWindowGuid(project.getWindowguid()).getResult();
                if (window != null) {
                    dataJson.put("windowtel", window.getTel());
                }
                else {
                    dataJson.put("windowtel", "");
                }
                dataJson.put("applyername", project.getApplyername());
                dataJson.put("certnum", project.getCertnum());
                dataJson.put("contactphone", project.getContactphone());
                if ("8".equals(project.getStatus().toString()) && StringUtil.isNotBlank(project.getCertnum())) {
                    AuditQueueUserinfo auditQueueUserinfo = userinfoservice.getUserinfo(project.getCertnum())
                            .getResult();
                    AuditOnlineRegister register = registerservice.getRegisterByIdorMobile(project.getCertnum())
                            .getResult();
                    if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                        dataJson.put("address", auditQueueUserinfo.getAddress());
                    }
                    else {
                        dataJson.put("address", "");
                    }
                    if (StringUtil.isNotBlank(register)) {
                        dataJson.put("contactmobile", register.getMobile());
                    }
                    else {
                        dataJson.put("contactmobile", "");
                    }

                }
                else {
                    dataJson.put("contactmobile", project.getContactmobile());
                    dataJson.put("address", project.getAddress());
                }

                dataJson.put("contactemail", project.getContactemail());
                dataJson.put("contactperson", project.getContactperson());

                dataJson.put("taskguid", project.getTaskguid());
                dataJson.put("contactpostcode", project.getContactpostcode());
                dataJson.put("contactidnum", project.getContactcertnum());
                dataJson.put("remark", project.getRemark());
                dataJson.put("legal", project.getLegal());
                dataJson.put("legalid", project.getLegalid());
                dataJson.put("certtype", project.getCerttype());
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", project.getCenterguid());
                sql.eq("configname", "AS_PROJECT_URL");
                AuditOrgaConfig config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    dataJson.put("projecturl", config.getConfigvalue());
                }
                else {
                    dataJson.put("projecturl", "");
                }

                // 查询办件已提交的材料
                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("projectguid", projectguid);
                sql2.in("status ",
                        "'" + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC + "'");
                List<AuditProjectMaterial> auditProjectMaterials = projectMaterialservice
                        .selectProjectMaterialByCondition(sql2.getMap()).getResult();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                Map<String, Object> data1 = null;

                if (!auditProjectMaterials.isEmpty()) {
                    for (int i = 0; i < auditProjectMaterials.size(); i++) {
                        data1 = new HashMap<String, Object>(16);
                        data1.put("taskmaterial", (i + 1) + "、" + auditProjectMaterials.get(i).getTaskmaterial());
                        dataList.add(data1);
                    }
                }
                dataJson.put("material", dataList);

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取办件列表
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
    public String getProjectList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String areacode = obj.getString("areacode");
            String certnum = obj.getString("certnum");
            String accountguid = obj.getString("accountguid");

            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject projectJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            if (StringUtil.isNotBlank(areacode)) {
                sql.eq("areacode", areacode);
            }
            if (StringUtil.isNotBlank(accountguid)) {
                AuditOnlineIndividual auditOnlineIndividual = individualservice.getIndividualByAccountGuid(accountguid)
                        .getResult();
                sql.eq("applyerguid", auditOnlineIndividual.getApplyerguid());
            }
            else {
                sql.eq("certnum", certnum);
            }

            // sql.in("status ", "12,14,16,24,26,28,30,40,50,90,97,98,99");
            sql.in("status",
                    ZwdtConstant.BANJIAN_STATUS_WWYTJ + "," + ZwdtConstant.BANJIAN_STATUS_WWYSTU + ","
                            + ZwdtConstant.BANJIAN_STATUS_WWYSTG + "," + ZwdtConstant.BANJIAN_STATUS_DJJ + ","
                            + ZwdtConstant.BANJIAN_STATUS_YJJ + "," + ZwdtConstant.BANJIAN_STATUS_DBB + ","
                            + ZwdtConstant.BANJIAN_STATUS_YSL + "," + ZwdtConstant.BANJIAN_STATUS_SPBTG + ","
                            + ZwdtConstant.BANJIAN_STATUS_SPTG + "," + ZwdtConstant.BANJIAN_STATUS_ZCBJ + ","
                            + ZwdtConstant.BANJIAN_STATUS_BYSL + "," + ZwdtConstant.BANJIAN_STATUS_CXSQ + ","
                            + ZwdtConstant.BANJIAN_STATUS_YCZZ);
            String fields = "rowguid,taskguid,projectname,status,applydate,sourceguid,type,taskguid,flowsn";
            sql.setSelectFields(fields);
            PageData<AuditOnlineProject> projectpagedata = onlineProjectservice.getAuditOnlineProjectPageData(
                    sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                    Integer.parseInt(pageSize), "applydate", "desc").getResult();
            // 非空判断
            if (StringUtil.isBlank(projectpagedata)) {
                dataJson.put("projectlist", list);
                dataJson.put("totalcount", 0);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            }
            int totalcount = projectpagedata.getRowCount();
            for (AuditOnlineProject onlineproject : projectpagedata.getList()) {
                projectJson = new JSONObject();
                projectJson.put("projectguid", onlineproject.getSourceguid());
                projectJson.put("businessguid", onlineproject.getTaskguid());
                projectJson.put("projectname", onlineproject.getProjectname());
                projectJson.put("applydate", EpointDateUtil.convertDate2String(onlineproject.getApplydate()));
                projectJson.put("status", onlineproject.getStatus());
                projectJson.put("statustext", setStatusText(Integer.parseInt(onlineproject.getStatus())));
                // peojectJson.put("statustext",
                // codeitemsservice.getItemTextByCodeName("办件状态",
                // String.valueOf(project.getStatus())));
                projectJson.put("type", onlineproject.getType());
                projectJson.put("flowsn", onlineproject.getFlowsn()); // 办件编号
                list.add(projectJson);
            }

            dataJson.put("projectlist", list);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    public String setStatusText(Integer status) {
        String statustext = "";
        if (status == ZwfwConstant.BANJIAN_STATUS_WWWTJ || status == ZwfwConstant.BANJIAN_STATUS_WWINIT) {
            // 4.2.5.1、外网申报未提交、外网申报初始化（待提交状态）
            statustext = "待提交";
        }
        else if (status == ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
            // 4.2.5.2、预审退回（预审退回状态）
            statustext = "预审退回";
        }
        else if (status == ZwfwConstant.BANJIAN_STATUS_DBB || status == ZwfwConstant.BANJIAN_STATUS_YSLDBB) {
            // 4.2.5.3、待补办、受理后待补办（预审退回状态）
            statustext = "待补正";
        }
        else if (status == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
            // 4.2.5.4、正常办结且未评价（待评价状态）
            statustext = "已办结";
        }
        else if (status == ZwfwConstant.BANJIAN_STATUS_WWYTJ) {
            // 4.2.5.6、待预审（外网申报已提交状态）
            statustext = "待预审";
        }
        else {
            // 4.2.5.7、除去之前列出的状态外的办件（已办结状态）
            statustext = "审批中";
        }

        return statustext;
    }

    /**
     * 申报（初始化办件和材料）接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/initProject", method = RequestMethod.POST)
    public String initProject(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String taskGuid = obj.getString("taskguid");
            String accountGuid = obj.getString("accountguid");
            String areacode = obj.getString("areacode");
            String centerGuid = obj.getString("centerguid");
            // 获取事项情形标识
            String taskCaseGuid = obj.getString("taskcaseguid");
            // 获取申请人类型（10:企业 20:个人）
            String applyerType = obj.getString("applyertype");
            // 申请人类型如果为空则默认为个人
            applyerType = StringUtil.isBlank(applyerType) ? ZwfwConstant.APPLAYERTYPE_GR : applyerType;

            AuditOnlineIndividual auditOnlineIndividual = individualservice.getIndividualByAccountGuid(accountGuid)
                    .getResult();

            // 设置办件标识
            String projectGuid = UUID.randomUUID().toString();
            // 设置申请人标识(个人类型默认为申请人Applyerguid，企业默认为空)
            String applyerGuid = "";
            // 设置申请人姓名（申请人类型为个人则申请人姓名为个人信息中的姓名，申请人类型为企业则为空）
            String applyerUserName = "";
            // 设置申请人证照编号（申请人类型为个人则申请人证照编号为个人信息中的身份证，申请人类型为企业则为空）
            String certNum = "";

            List<AuditProjectMaterial> auditProjectMaterials = null;
            if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                applyerUserName = auditOnlineIndividual.getClientname();
                applyerGuid = auditOnlineIndividual.getApplyerguid();
                certNum = auditOnlineIndividual.getIdnumber();
                auditProjectMaterials = handleProjectservice.InitOnlineProjectReturnMaterials(taskGuid, centerGuid,
                        areacode, applyerGuid, applyerUserName, certNum, projectGuid, taskCaseGuid, applyerType)
                        .getResult();
            }
            else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                // 设置企业申请人姓名
                String declarerName = auditOnlineIndividual.getClientname();
                // 设置企业申请人guid
                String declarerGuid = auditOnlineIndividual.getApplyerguid();
                // 初始化办件数据并且返回办件材料数据
                auditProjectMaterials = handleProjectservice.InitOnlineCompanyProjectReturnMaterials(taskGuid,
                        centerGuid, areacode, applyerGuid, applyerUserName, certNum, projectGuid, taskCaseGuid,
                        applyerType, declarerName, declarerGuid).getResult();
            }

            // 5、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("projectguid", projectGuid);// 办件标识

            return JsonUtils.zwdtRestReturn("1", "初始化办件成功", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "初始化办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 保存办件的接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/saveProjectInfo", method = RequestMethod.POST)
    public String saveProjectInfo(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("projectguid");
            // String applyerguid = obj.getString("applyerguid");
            String contactname = obj.getString("contactname");// 联系人姓名
            String contactmobile = obj.getString("contactmobile");// 联系人手机
            String contactphone = obj.getString("contactphone");// 联系人电话
            String contactidnum = obj.getString("contactidnum");// 联系人身份证号码
            String postcode = obj.getString("postcode");// 邮编
            String address = obj.getString("address");// 地址
            String remark = obj.getString("remark");// 备注
            String areacode = obj.getString("areacode");// 区域表中的IndividuationFold
            // 08-29新增法人相关字段
            // 获取申请人类型（10:企业 20:个人）
            String applyerType = obj.getString("applyertype");
            // 证照类型 统一社会信用代码 16 组织机构代码 14 身份证 22
            String certtype = obj.getString("certtype");
            // 证照编号
            String certnum = obj.getString("certnum");
            // 法人代表身份证
            String legalid = obj.getString("legalid");
            // 法人代表
            String legal = obj.getString("legal");
            // 申请人名称(公司名称)
            String applyername = obj.getString("applyername");

            // 判断办件是否已经提交
            String fields = " rowguid,status";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            // 对象判空
            if (StringUtil.isNotBlank(auditProject)) {
                if (auditProject.getStatus() >= 12) {
                    return JsonUtils.zwdtRestReturn("0", "已提交的办件无法继续修改", "");
                }
            }

            // 2、获取初始化的办件
            AuditProject auditproject = new AuditProject();
            if (StringUtil.isBlank(applyerType) || ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                auditproject.setAreacode(areacode);
                auditproject.setRowguid(projectguid);// 主键
                auditproject.setContactperson(contactname);
                auditproject.setContactmobile(contactmobile);
                auditproject.setContactphone(contactphone);
                auditproject.setContactpostcode(postcode);
                auditproject.setContactcertnum(contactidnum);
                auditproject.setRemark(remark);
                auditproject.setAddress(address);
                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 外网申报未提交
                auditproject.setOperatedate(new Date()); // 待提交 保存时间
                auditproject.setIs_test(0);
            }
            else {
                auditproject.setAreacode(areacode);
                auditproject.setRowguid(projectguid);// 主键
                auditproject.setContactperson(contactname);
                auditproject.setContactmobile(contactmobile);
                auditproject.setContactphone(contactphone);
                auditproject.setContactpostcode(postcode);
                auditproject.setContactcertnum(contactidnum);
                auditproject.setRemark(remark);
                auditproject.setAddress(address);

                auditproject.setCerttype(certtype);
                auditproject.setCertnum(certnum);
                auditproject.setLegalid(legalid);
                auditproject.setLegal(legal);
                auditproject.setApplyername(applyername);
                auditproject.setIs_test(0);

                auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 外网申报未提交
                auditproject.setOperatedate(new Date()); // 待提交 保存时间
            }

            projectservice.updateProject(auditproject);

            // 更新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            sqlConditionUtil.eq("sourceguid", projectguid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());

            return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    // 11-19个性化申报 公积金(离退休)
    @RequestMapping(value = "/saveProjectInfoDemo_GJJ", method = RequestMethod.POST)
    public String saveProjectInfoDemo(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("projectguid");
            String contactphone = obj.getString("contactphone");// 联系人电话
            String remark = obj.getString("remark");// 备注
            String certnum = obj.getString("certnum");
            String applyername = obj.getString("applyername");
            String applypickreason = obj.getString("applypickreason");
            String applypickmoney = obj.getString("applypickmoney");
            String pickmethod = obj.getString("pickmethod");
            String marriagestatus = obj.getString("marriagestatus");
            String areacode = obj.getString("areacode");

            // 判断办件是否已经提交
            String fields = " rowguid,status";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (StringUtil.isNotBlank(auditProject)) {
                if (auditProject.getStatus() >= 12) {
                    return JsonUtils.zwdtRestReturn("0", "已提交的办件无法继续修改", "");
                }
            }
            // 2、获取初始化的办件
            AuditProject auditproject = new AuditProject();
            auditproject.setAreacode(areacode);
            auditproject.setRowguid(projectguid);// 主键
            auditproject.setContactphone(contactphone);
            auditProject.setApplyername(applyername);
            auditproject.setRemark(remark);
            auditproject.setCertnum(certnum);
            auditProject.set("marriagestatus", marriagestatus);
            auditproject.set("applypickreason", applypickreason);
            auditproject.set("applypickmoney", applypickmoney);
            auditProject.set("pickmethod", pickmethod);
            if ("1".equals(pickmethod)) {
                auditproject.set("banknameforgr", obj.getString("banknameforgr"));
                auditproject.set("cardnum", obj.getString("cardnum"));
            }
            else {
                auditproject.set("payee", obj.getString("payee"));
                auditproject.set("banknameforother", obj.getString("banknameforother"));
                auditproject.set("cardnumforother", obj.getString("cardnumforother"));
            }

            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 外网申报未提交
            auditproject.setOperatedate(new Date()); // 待提交 保存时间

            projectservice.updateProject(auditproject);

            // 更新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            sqlConditionUtil.eq("sourceguid", projectguid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());

            return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    // 11-19个性化申报 不动产
    @RequestMapping(value = "/saveProjectInfoDemo_BDC", method = RequestMethod.POST)
    public String saveProjectInfoDemo_BDC(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("projectguid");
            String applyreasonone = obj.getString("applyreasonone");
            String applyreasontwo = obj.getString("applyreasontwo");
            String certnum = obj.getString("certnum");
            String applyername = obj.getString("applyername");
            String certtype = obj.getString("certtype");
            String proxyperson = obj.getString("proxyperson");
            String proxyaddress = obj.getString("proxyaddress");
            String dutypersonname = obj.getString("dutypersonname");
            String areacode = obj.getString("areacode");
            String dutypersoncerttype = obj.getString("dutypersoncerttype"); // 责任人证件类型
            String dutypersoncertnum = obj.getString("dutypersoncertnum");// 责任人证件号码
            String dutypersontel = obj.getString("dutypersontel");
            String dutypersonaddress = obj.getString("dutypersonaddress");
            String locations = obj.getString("locations");
            String originalcertificateno = obj.getString("originalcertificateno");// 原始证书编号
            String housinguse = obj.getString("housinguse");
            String housingarea = obj.getString("housingarea");
            String landuse = obj.getString("landuse");
            String landarea = obj.getString("landarea");
            String diyascope = obj.getString("diyascope");// 抵押范围
            String diyaarea = obj.getString("diyaarea");// 抵押面积
            String protocolvalue = obj.getString("protocolvalue");// 协议固执
            String amountofdebt = obj.getString("amountofdebt");// 债务总额
            String fromdate = obj.getString("fromdate");
            String enddate = obj.getString("enddate");
            String guaranteescope = obj.getString("guaranteescope");// 担保范围
            String contactphone = obj.getString("contactphone");
            String registertype = obj.getString("registertype");
            String applyholdcert = obj.getString("applyholdcert");
            String registerremark = obj.getString("registerremark");
            String isrealmeanfordiya = obj.getString("isrealmeanfordiya");
            String commonsituation = obj.getString("commonsituation");// 共有类型
            String isrealmeanforyiwu = obj.getString("isrealmeanforyiwu");
            // 判断办件是否已经提交
            String fields = " rowguid,status";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (StringUtil.isNotBlank(auditProject)) {
                if (auditProject.getStatus() >= 12) {
                    return JsonUtils.zwdtRestReturn("0", "已提交的办件无法继续修改", "");
                }
            }
            // 2、获取初始化的办件
            AuditProject auditproject = new AuditProject();
            auditproject.setAreacode(areacode);
            auditproject.setRowguid(projectguid);// 主键
            auditproject.setContactphone(contactphone);
            auditProject.setApplyername(applyername);
            auditProject.setCerttype(certtype);
            auditproject.setCertnum(certnum);
            auditProject.set("applyreasonone", applyreasonone);
            auditProject.set("applyreasontwo", applyreasontwo);
            auditProject.set("proxyperson", proxyperson);
            auditProject.set("proxyaddress", proxyaddress);
            auditProject.set("dutypersonname", dutypersonname);
            auditProject.set("dutypersoncerttype", dutypersoncerttype);
            auditProject.set("dutypersoncertnum", dutypersoncertnum);
            auditProject.set("dutypersontel", dutypersontel);
            auditProject.set("dutypersonaddress", dutypersonaddress);
            auditProject.set("locations", locations);
            auditProject.set("originalcertificateno", originalcertificateno);
            auditProject.set("housinguse", housinguse);
            auditProject.set("housingarea", housingarea);
            auditProject.set("landuse", landuse);
            auditProject.set("landarea", landarea);
            auditProject.set("diyascope", diyascope);
            auditProject.set("diyaarea", diyaarea);
            auditProject.set("protocolvalue", protocolvalue);
            auditProject.set("amountofdebt", amountofdebt);
            auditProject.set("fromdate", fromdate);
            auditProject.set("enddate", enddate);
            auditProject.set("registertype", registertype);
            auditProject.set("applyholdcert", applyholdcert);
            auditProject.set("registerremark", registerremark);
            auditProject.set("isrealmeanfordiya", isrealmeanfordiya);
            auditProject.set("commonsituation", commonsituation);
            auditProject.set("isrealmeanforyiwu", isrealmeanforyiwu);
            auditProject.set("guaranteescope", guaranteescope);

            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 外网申报未提交
            auditproject.setOperatedate(new Date()); // 待提交 保存时间

            projectservice.updateProject(auditproject);

            // 更新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            sqlConditionUtil.eq("sourceguid", projectguid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());

            return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    // 11-20个性化申报 食品经营
    @RequestMapping(value = "/saveProjectInfoDemo_SPJY", method = RequestMethod.POST)
    public String saveProjectInfoDemo_SPJY(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String applyername = obj.getString("applyername");
            String certnum = obj.getString("certnum");
            String liveaddress = obj.getString("liveaddress");
            String runaddress = obj.getString("runaddress");
            String issubstance = obj.getString("issubstance");
            String storehouse = obj.getString("storehouse");
            String manageuser = obj.getString("manageuser");
            String runprojectname = obj.getString("runprojectname");
            String applycopynum = obj.getString("applycopynum");
            String validtime = obj.getString("validtime");
            String economicproperty = obj.getString("economicproperty");// 经济性质
            String empnum = obj.getString("empnum");
            String physicaltestnum = obj.getString("physicaltestnum"); // 药品经营许可证
            String contactpostcode = obj.getString("contactpostcode");
            String contactemail = obj.getString("contactemail");
            // 判断办件是否已经提交
            String fields = " rowguid,status";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (StringUtil.isNotBlank(auditProject)) {
                if (auditProject.getStatus() >= 12) {
                    return JsonUtils.zwdtRestReturn("0", "已提交的办件无法继续修改", "");
                }
            }
            // 2、获取初始化的办件
            AuditProject auditproject = new AuditProject();
            auditproject.setAreacode(areacode);
            auditproject.setRowguid(projectguid);// 主键
            auditProject.setApplyername(applyername);
            auditproject.setCertnum(certnum);
            auditProject.setContactpostcode(contactpostcode);
            auditproject.setContactemail(contactemail);
            auditProject.set("liveaddress", liveaddress);
            auditProject.set("runaddress", runaddress);
            auditproject.set("issubstance", issubstance);
            auditproject.set("storehouse", storehouse);
            auditProject.set("manageuser", manageuser);
            auditProject.set("runprojectname", runprojectname);
            auditProject.set("applycopynum", applycopynum);
            auditProject.set("validtime", validtime);
            auditProject.set("economicproperty", economicproperty);
            auditProject.set("empnum", empnum);
            auditProject.set("physicaltestnum", physicaltestnum);

            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 外网申报未提交
            auditproject.setOperatedate(new Date()); // 待提交 保存时间

            projectservice.updateProject(auditproject);

            // 更新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            sqlConditionUtil.eq("sourceguid", projectguid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());

            return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    // 11-26 申请住房公积金贷款
    @RequestMapping(value = "/saveProjectInfoDemo_GJJLoan", method = RequestMethod.POST)
    public String saveProjectInfoDemo_GJJLoan(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("projectguid");
            String contactphone = obj.getString("contactphone");// 联系人电话
            String certnum = obj.getString("certnum");
            String applyername = obj.getString("applyername");
            String workplace = obj.getString("workplace");
            String companytel = obj.getString("companytel");
            String nowaddress = obj.getString("nowaddress");
            String personalfundnum = obj.getString("personalfundnum");// 个人资金
            String monthincome = obj.getString("monthincome");
            String buyaddress = obj.getString("buyaddress");
            String areaforbuy = obj.getString("areaforbuy");
            String houseprice = obj.getString("houseprice");
            String housetype = obj.getString("housetype");
            String loanmoney = obj.getString("loanmoney");
            String repaymethod = obj.getString("repaymethod");// 还款方式
            String areacode = obj.getString("areacode");

            // 判断办件是否已经提交
            String fields = " rowguid,status";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (StringUtil.isNotBlank(auditProject)) {
                if (auditProject.getStatus() >= 12) {
                    return JsonUtils.zwdtRestReturn("0", "已提交的办件无法继续修改", "");
                }
            }

            // 2、获取初始化的办件
            AuditProject auditproject = new AuditProject();
            auditproject.setAreacode(areacode);
            auditproject.setRowguid(projectguid);// 主键
            auditproject.setContactphone(contactphone);
            auditProject.setApplyername(applyername);
            auditproject.setCertnum(certnum);
            auditProject.set("workplace", workplace);
            auditproject.set("companytel", companytel);
            auditproject.set("nowaddress", nowaddress);
            auditProject.set("personalfundnum", personalfundnum);
            auditProject.set("monthincome ", monthincome);
            auditproject.set("buyaddress", buyaddress);
            auditproject.set("areaforbuy", areaforbuy);
            auditProject.set("houseprice", houseprice);
            auditproject.set("housetype", housetype);
            auditproject.set("loanmoney", loanmoney);
            auditProject.set("repaymethod", repaymethod);
            auditproject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWWTJ);// 外网申报未提交
            auditproject.setOperatedate(new Date()); // 待提交 保存时间

            projectservice.updateProject(auditproject);

            // 更新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));// 办件状态更新为：外网申报未提交
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            sqlConditionUtil.eq("sourceguid", projectguid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());

            return JsonUtils.zwdtRestReturn("1", "保存成功！", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "办件基本信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取多情形材料列表(办件申报须知多情形下调用)
     * 
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskCaseMaterialsByCaseguid", method = RequestMethod.POST)
    public String getTaskCaseMaterialsByCaseguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskCaseMaterialsByCaseguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");

            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项多情形标识
            String taskCaseGuid = obj.getString("faqcaseguid");
            String projectguid = obj.getString("projectguid");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            // 1.2、获取事项标识
            // String taskGuid = obj.getString("taskguid");
            List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
            // 2、获取多情形材料列表
            if (StringUtil.isNotBlank(taskCaseGuid)) {
                AuditTaskCase auditTaskCase = auditTaskCaseService.getAuditTaskCaseByRowguid(taskCaseGuid).getResult();
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
                List<AuditTaskMaterial> auditTaskMaterials = taskMaterialservice
                        .selectMaterialListByCondition(sql.getMap()).getResult();
                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                    if (auditTaskMaterial != null) {
                        materialJsonList.add(this.getMaterialJson(auditTaskMaterial, projectguid));
                    }
                }
                // 获取情形材料（情形表中仅有非必要材料）
                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(taskCaseGuid).getResult();
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    if (auditTaskMaterialCase != null) {
                        // int necessity = auditTaskMaterialCase.getNecessity();
                        // 3、当内网不勾选材料情形时，默认不显示该材料
                        if (ZwdtConstant.INT_NO == auditTaskMaterialCase.getNecessity()) {
                            continue;
                        }
                        // 4、获取情形与材料关系获取事项材料信息
                        AuditTaskMaterial auditTaskMaterial = taskMaterialservice
                                .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid()).getResult();
                        if (auditTaskMaterial != null) {
                            // 兼容旧版，情形材料中的必要材料不返回
                            if (ZwfwConstant.NECESSITY_SET_NO.equals(auditTaskMaterial.getNecessity().toString())) {
                                materialJsonList.add(this.getMaterialJson(auditTaskMaterial, projectguid));
                            }
                        }
                    }
                }
            }
            // 5、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            List<String> taskmaterialarray = new ArrayList<String>();
            List<String> statusarray = new ArrayList<String>();
            // 截取对应页面的部门list数据
            if (StringUtil.isNotBlank(currentPage) && StringUtil.isNotBlank(pageSize)) {
                int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
                int endint = (firstint + Integer.parseInt(pageSize)) >= materialJsonList.size()
                        ? materialJsonList.size() : (firstint + Integer.parseInt(pageSize));
                List<JSONObject> rtnlist = materialJsonList.subList(firstint, endint);
                dataJson.put("materiallist", rtnlist);
                dataJson.put("totalcount", StringUtil.getNotNullString(materialJsonList.size()));
                // 拼接所有材料guid和材料状态
                for (JSONObject json : materialJsonList) {
                    taskmaterialarray.add(json.getString("taskmaterialguid"));
                    statusarray.add(json.getString("status"));
                }
                if (!materialJsonList.isEmpty()) {

                    dataJson.put("taskmaterialarray", taskmaterialarray);
                    dataJson.put("statusarray", statusarray);
                }
            }
            else {
                dataJson.put("materiallist", materialJsonList);
            }
            log.info("=======结束调用getTaskCaseMaterialsByCaseguid接口=======");
            return JsonUtils.zwdtRestReturn("1", "多情形材料信息获取成功", dataJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======declareProjectNotice接口参数：params【" + params + "】=======");
            log.info("=======declareProjectNotice异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    public JSONObject getMaterialJson(AuditTaskMaterial auditTaskMaterial, String projectguid) {
        JSONObject materialJson = new JSONObject();
        AuditProjectMaterial auditProjectMaterial = projectMaterialservice
                .selectProjectMaterialByMaterialGuid(projectguid, auditTaskMaterial.getRowguid()).getResult();
        materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 材料标识
        materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
        materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
        materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
        materialJson.put("materialid", auditTaskMaterial.getMaterialid());// 7.10版本新增返回值:
                                                                          // 事项材料ID
        int necessity = auditTaskMaterial.getNecessity();
        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
            materialJson.put("necessary", "0");// 是否必须
        }
        else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
            // 不考虑纸质必填的
            if (WorkflowKeyNames9.SubmitType_PaperSubmit == Integer.parseInt(auditTaskMaterial.getSubmittype())) {
                materialJson.put("necessary", "0");
            }
            else {
                materialJson.put("necessary", "1");// 是否必须
            }
        }
        // 首先判断情形，若存在情形，则所有材料为必须材料
        materialJson.put("necessary", "1");

        materialJson.put("submittype", auditTaskMaterial.getSubmittype());
        materialJson.put("clientguid", auditProjectMaterial.getCliengguid());
        materialJson.put("submitcount", attachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()));
        // 获取每个材料对应的附件guid
        List<FrameAttachInfo> attachInfoList = attachService
                .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
        List<String> attachguidList = new ArrayList<String>();
        for (FrameAttachInfo attachinfo : attachInfoList) {
            attachguidList.add(attachinfo.getAttachGuid());
        }
        if (!attachguidList.isEmpty()) {
            materialJson.put("attachguid", StringUtil.join(attachguidList, ";"));
        }
        else {
            materialJson.put("attachguid", "");
        }
        // 获取排序值用于排序
        materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
        return materialJson;
    }

    /**
     * 获取办件材料信息的接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getMaterialList", method = RequestMethod.POST)
    public String getInitMaterialList(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String type = obj.getString("type"); // 0:办件所有材料 1：需要补正材料 2:原件提交
            String caseguid = obj.getString("caseguid");
            String areacode = obj.getString("areacode");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            // 添加参数
            String usestore = obj.getString("usestore");

            String taskmaterialguids = "";
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid", projectGuid);

            if ("0".equals(type)) {
                sql.eq("auditstatus", ZwfwConstant.Material_AuditStatus_WTJ);
            }
            else if ("1".equals(type)) {
                sql.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
            }
            else if ("2".equals(type)) {
                //
            }

            // 如果情形不为空，判断当前办件的情形跟传入的是否一致，不一致则更新
            if (StringUtil.isNotBlank(caseguid)) {
                String fields = " rowguid,areacode,taskcaseguid";
                AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectGuid, areacode)
                        .getResult();
                if (auditProject != null) {
                    if (!caseguid.equals(auditProject.getTaskcaseguid())) {
                        auditProject.setTaskcaseguid(caseguid);
                        projectservice.updateProject(auditProject);

                    }
                }

                List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                        .selectTaskMaterialCaseByCaseGuid(caseguid).getResult();
                for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                    taskmaterialguids += "'" + auditTaskMaterialCase.getMaterialguid() + "',";
                }
                if (StringUtil.isNotBlank(taskmaterialguids)) {
                    sql.in("taskmaterialguid", taskmaterialguids.substring(0, taskmaterialguids.length() - 1));
                }
            }
            // 3、获取办件材料
            List<AuditProjectMaterial> ProjectMateriallist = projectMaterialservice
                    .selectProjectMaterialByCondition(sql.getMap()).getResult();
            int totalcount = ProjectMateriallist.size();
            List<JSONObject> projectJsonlist = new ArrayList<JSONObject>();
            for (AuditProjectMaterial auditProjectMaterial : ProjectMateriallist) {
                AuditTaskMaterial auditTaskMaterial = taskMaterialservice
                        .getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();

                JSONObject materialJson = new JSONObject();
                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());// 材料标识
                materialJson.put("projectmaterialname", auditTaskMaterial.getMaterialname());// 材料名称
                materialJson.put("status", auditProjectMaterial.getStatus());// 材料状态
                materialJson.put("taskmaterialguid", auditTaskMaterial.getRowguid());// 事项材料主键
                materialJson.put("materialid", auditTaskMaterial.getMaterialid());// 7.10版本新增返回值:
                                                                                  // 事项材料ID
                int necessity = auditTaskMaterial.getNecessity();
                if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_NO)) {
                    materialJson.put("necessary", "0");// 是否必须
                }
                else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                    // 不考虑纸质必填的
                    if (WorkflowKeyNames9.SubmitType_PaperSubmit == Integer
                            .parseInt(auditTaskMaterial.getSubmittype())) {
                        materialJson.put("necessary", "0");
                    }
                    else {
                        materialJson.put("necessary", "1");// 是否必须
                    }
                }
                materialJson.put("submittype", auditTaskMaterial.getSubmittype());
                materialJson.put("clientguid", auditProjectMaterial.getCliengguid());
                materialJson.put("submitcount",
                        attachService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid()));
                // 获取每个材料对应的附件guid
                List<FrameAttachInfo> attachInfoList = attachService
                        .getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
                List<String> attachguidList = new ArrayList<String>();
                for (FrameAttachInfo attachinfo : attachInfoList) {
                    attachguidList.add(attachinfo.getAttachGuid());
                }
                if (!attachguidList.isEmpty()) {
                    materialJson.put("attachguid", StringUtil.join(attachguidList, ";"));
                }
                else {
                    materialJson.put("attachguid", "");
                }
                // 获取排序值用于排序
                materialJson.put("ordernum",
                        StringUtil.isBlank(auditTaskMaterial.getOrdernum()) ? 0 : auditTaskMaterial.getOrdernum());
                if (StringUtil.isBlank(usestore)
                        || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == auditProjectMaterial.getStatus()) {
                    projectJsonlist.add(materialJson);
                }
                else {
                    totalcount--;
                }
            }
            // 先排序
            Collections.sort(projectJsonlist, new MyComparator());
            // 再分页
            List<JSONObject> pageJsonlist = Page(projectJsonlist, Integer.parseInt(pageSize),
                    Integer.parseInt(currentPage));
            dataJson.put("materiallist", pageJsonlist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }

    // 排序
    public class MyComparator implements Comparator<JSONObject>
    {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            String key1 = o1.getString("ordernum");
            String key2 = o2.getString("ordernum");
            int int1 = Integer.parseInt(key1);
            int int2 = Integer.parseInt(key2);

            return int2 - int1;
        }
    }

    // 分页
    public static List<JSONObject> Page(List<JSONObject> dataList, int pageSize, int currentPage) {
        List<JSONObject> currentPageList = new ArrayList<>();
        if (dataList != null && !dataList.isEmpty()) {
            int currIdx = (currentPage >= 1 ? currentPage * pageSize : 0);
            for (int i = 0; i < pageSize && i < dataList.size() - currIdx; i++) {
                JSONObject data = dataList.get(currIdx + i);
                currentPageList.add(data);
            }
        }
        return currentPageList;
    }

    /**
     * 二维码扫描时，获取办件某个材料信息的接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getScanMaterialList", method = RequestMethod.POST)
    public String getScanMaterialList(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("clientidentifier");
            String projectmaterialguid = obj.getString("projectmaterialguid");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> projectJsonlist = new ArrayList<JSONObject>();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid", projectguid);
            AuditProjectMaterial projectMaterialDetail = projectMaterialservice
                    .getProjectMaterialDetail(projectmaterialguid, projectguid).getResult();
            if (StringUtil.isNotBlank(projectMaterialDetail)) {
                JSONObject materialJson = new JSONObject();
                materialJson.put("clientguid", projectMaterialDetail.getCliengguid());
                materialJson.put("projectmaterialguid", projectMaterialDetail.getRowguid());
                materialJson.put("projectmaterialname", projectMaterialDetail.getTaskmaterial());
                materialJson.put("status", projectMaterialDetail.getStatus());
                materialJson.put("taskmaterialguid", projectMaterialDetail.getTaskmaterialguid());
                projectJsonlist.add(materialJson);
            }
            dataJson.put("materiallist", projectJsonlist);
            return JsonUtils.zwdtRestReturn("1", "获取材料列表成功", dataJson);
        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件多情形
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getProjectCaseList", method = RequestMethod.POST)
    public String getProjectCaseList(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");

            JSONObject dataJson = new JSONObject();
            String fields = "taskguid,taskcaseguid";
            AuditProject project = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();

            if (project != null) {
                List<AuditTaskCase> auditTaskCases = auditTaskCaseService
                        .selectTaskCaseByTaskGuid(project.getTaskguid()).getResult();

                List<JSONObject> caseconditionList = new ArrayList<JSONObject>();
                JSONObject conditionJson = new JSONObject();
                if (!auditTaskCases.isEmpty()) {
                    for (AuditTaskCase auditTaskCase : auditTaskCases) {
                        conditionJson = new JSONObject();
                        conditionJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                        conditionJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                        caseconditionList.add(conditionJson);
                    }
                }
                dataJson.put("caselist", caseconditionList);
                dataJson.put("taskcaseguid", project.getTaskcaseguid());
                return JsonUtils.zwdtRestReturn("1", "获取办件多情形成功！", dataJson);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取办件多情形失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取已提交材料列表接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getSubmitMaterialList", method = RequestMethod.POST)
    public String getSubmitMaterialList(@RequestBody String params) {
        try {
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("projectguid", projectGuid);
            sql.in("status ",
                    "'" + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER + "','"
                            + ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC + "','"
                            + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC + "'");
            List<JSONObject> materialList = new ArrayList<JSONObject>();
            List<AuditProjectMaterial> auditProjectMaterials = projectMaterialservice
                    .selectProjectMaterialByCondition(sql.getMap()).getResult();
            for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                JSONObject materialJson = new JSONObject();
                materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());
                materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());
                materialList.add(materialJson);
            }
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("materiallist", materialList);
            dataJson.put("materialcount", materialList.size());

            return JsonUtils.zwdtRestReturn("1", "获取已提交材料列表成功", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取已提交材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都提交的接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/checkMaterialSubmit", method = RequestMethod.POST)
    public String checkMaterialSubmit(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");// 办件标识
            String areacode = obj.getString("areacode");
            String type = obj.getString("type");
            String fields = " rowguid,taskguid,taskcaseguid ";
            String taskmaterialguids = "";
            AuditProject auditProject = projectservice.getAuditProjectByRowGuid(fields, projectGuid, areacode)
                    .getResult();

            List<AuditTaskMaterial> auditTaskMaterials = taskMaterialservice
                    .selectTaskMaterialListByTaskGuid(auditProject.getTaskguid(), true).getResult();
            int count = 0;
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                if (StringUtil.isBlank(taskmaterialguids) || (StringUtil.isNotBlank(taskmaterialguids)
                        && taskmaterialguids.contains(auditTaskMaterial.getRowguid()))) {
                    AuditProjectMaterial auditProjectMaterial = projectMaterialservice
                            .selectProjectMaterialByMaterialGuid(projectGuid, auditTaskMaterial.getRowguid())
                            .getResult();
                    if (StringUtil.isNotBlank(auditProject.getTaskcaseguid())) {
                        auditTaskMaterial.setNecessity(Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES));
                    }
                    // 材料必填并且是附件
                    if (auditTaskMaterial.getNecessity() == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)
                            && auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                        String submitType = auditTaskMaterial.getSubmittype();
                        int status = auditProjectMaterial.getStatus();
                        // 提交方式：电子
                        if (WorkflowKeyNames9.SubmitType_Submit == Integer.parseInt(submitType)) {
                            if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                count++;
                            }
                        }
                        // 提交方式：电子和纸质
                        else if (WorkflowKeyNames9.SubmitType_Submit_And_PaperSubmit == Integer.parseInt(submitType)) {
                            if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                count++;
                            }
                        }
                        // 提交方式：电子或纸质
                        else if (WorkflowKeyNames9.SubmitType_Submit_Or_PaperSubmit == Integer.parseInt(submitType)) {
                            if (status == ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT
                                    && !QueueConstant.Common_yes_String.equals(type)) {
                                count++;
                            }
                        }
                        if (StringUtil.isNotBlank(auditProject.getTaskcaseguid())) {
                            // 多情形判断纸质材料必须，普通条件下不判断。
                            if (WorkflowKeyNames9.SubmitType_PaperSubmit == Integer.parseInt(submitType)) {
                                if (status == ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT
                                        && !QueueConstant.Common_yes_String.equals(type)) {
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("nosubmitnum", count);

            return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 附件上传接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/attachSubmit", method = RequestMethod.POST)
    public String attachSubmit(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String attachName = obj.getString("attachname");
            String clientGuid = obj.getString("clientguid");
            String accountGuid = obj.getString("accountguid");
            String attachGuid = obj.getString("attachguid");
            String imgbase64 = obj.getString("imgbase64");
            String mode = obj.getString("mode");
            // 重新扫描，需先删除clientguid对应附件
            if ("2".equals(mode)) {
                attachService.deleteAttachByGuid(clientGuid);
            }
            if (StringUtil.isBlank(attachGuid)) {
                attachGuid = UUID.randomUUID().toString();
            }
            byte[] pic = Base64Util.decodeBuffer(imgbase64);
            if (pic.length > 0) {
                // 附件信息
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setAttachGuid(attachGuid);
                frameAttachInfo.setCliengGuid(clientGuid);
                frameAttachInfo.setAttachFileName(attachName);
                frameAttachInfo.setCliengTag("一体机申报材料");
                frameAttachInfo.setUploadUserGuid(accountGuid);
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("image/jpeg");
                frameAttachInfo.setAttachLength((long) pic.length);
                InputStream input = new ByteArrayInputStream(pic);
                attachService.addAttach(frameAttachInfo, input);
                EpointFrameDsManager.begin(null);
                input.close();
            }
            dataJson.put("attachguid", attachGuid);

            return JsonUtils.zwdtRestReturn("1", "上传成功", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }
    }

    /**
     * 重新扫描删除附件方法
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/attachReScan", method = RequestMethod.POST)
    public String attachReScan(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String clientGuid = obj.getString("clientguid");
            String mode = obj.getString("mode");
            // 重新扫描，需先删除clientguid对应附件
            if ("2".equals(mode)) {
                attachService.deleteAttachByGuid(clientGuid);
                return JsonUtils.zwdtRestReturn("1", "重新扫描删除成功", dataJson);
            }

            return JsonUtils.zwdtRestReturn("1", "补充扫描无需删除", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "删除失败：" + e.getMessage(), "");
        }
    }

    /**
     * 删除扫描图片接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/deletescanpic", method = RequestMethod.POST)
    public String deleteScanPic(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String attachGuid = obj.getString("attachguid");
            attachService.deleteAttachByAttachGuid(attachGuid);

            return JsonUtils.zwdtRestReturn("1", "上传成功", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }
    }

    /**
     * 材料提交
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String checkMaterialIsUploadByClientguid(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String clientguid = obj.getString("clientguid");// 附件guid
            String projectmaterialguid = obj.getString("projectmaterialguid");// 办件材料主键
            String projectguid = obj.getString("projectguid");
            // 2、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            int count = attachService.getAttachCountByClientGuid(clientguid);
            // 3、更新材料状态
            if (count > 0) {
                projectMaterialservice.updateStatus(projectmaterialguid, projectguid,
                        ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
            }
            else {
                projectMaterialservice.updateStatus(projectmaterialguid, projectguid,
                        ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT);
            }
            return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交办件的接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/submitProject", method = RequestMethod.POST)
    public String submitProject(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String accountGuid = obj.getString("accountguid");
            String isfinish = obj.getString("isfinish");
            AuditProject auditProject = new AuditProject();

            // 跟新onlineproject
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
            SqlConditionUtil conditionMap = new SqlConditionUtil();
            long startTime = System.currentTimeMillis();
            AuditProject project = projectservice.getAuditProjectByRowGuid("taskguid", projectGuid, areacode)
                    .getResult();
            EpointFrameDsManager.commit();

            // 结束之间
            long endTime = System.currentTimeMillis();
            // 程序块运行时间
            log.info("Time运行时间为：" + (endTime - startTime) / 1000 + "秒");
            // 开始时间
            long startTime1 = System.currentTimeMillis();

            // 获取事项详情
            AuditTaskExtension auditTaskExtension = taskExtensionservice
                    .getTaskExtensionByTaskGuid(project.getTaskguid(), true).getResult();
            // 结束之间
            long endTime1 = System.currentTimeMillis();
            // 程序块运行时间
            log.info("Time1运行时间为：" + (endTime1 - startTime1) / 1000 + "秒");
            if (ZwfwConstant.WEB_APPLY_TYPE_SL.equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                // 网上申报后直接受理
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_DJJ));// 代接件
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_DJJ);
                // 修改办件表中申请方式
                auditProject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB));// 网上直接申报

            }
            else {
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ));// 外网申报已提交
                auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
            }
            updateDateFieldMap.put("applydate=",
                    EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            conditionMap.eq("sourceguid", projectGuid);
            long startTime2 = System.currentTimeMillis();
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionMap.getMap());
            // 结束之间
            long endTime2 = System.currentTimeMillis();
            // 程序块运行时间
            log.info("Time2运行时间为：" + (endTime2 - startTime2) / 1000 + "秒");

            if (StringUtil.isNotBlank(accountGuid)) {
                AuditOnlineIndividual auditOnlineIndividual = individualservice.getIndividualByAccountGuid(accountGuid)
                        .getResult();
                auditProject.setOnlineapplyerguid(auditOnlineIndividual.getApplyerguid());// 插入外网申请人guid
                                                                                          // 便于后期查询
            }

            // 更新办件表
            auditProject.setApplydate(new Date());
            auditProject.setAreacode(areacode);
            auditProject.setRowguid(projectGuid);
            long startTime3 = System.currentTimeMillis();
            projectservice.updateProject(auditProject);
            EpointFrameDsManager.commit();

            // 结束之间
            long endTime3 = System.currentTimeMillis();
            // 程序块运行时间
            log.info("Time3运行时间为：" + (endTime3 - startTime3) / 1000 + "秒");

            // 新增自助服务办件统计表记录
            AuditZnsbSelfmachineproject auditZnsbSelfmachineproject = new AuditZnsbSelfmachineproject();
            auditZnsbSelfmachineproject.setRowguid(UUID.randomUUID().toString());
            auditZnsbSelfmachineproject.setProjectguid(projectGuid);
            auditZnsbSelfmachineproject.setApplydate(new Date());
            long startTime4 = System.currentTimeMillis();

            // 向指定用户发送待办
            auditProject = projectservice
                    .getAuditProjectByRowGuid("rowguid,areacode,applyername,task_id,currentareacode,centerguid ",
                            projectGuid, areacode)
                    .getResult();
            AuditTask auditTask = taskservice.getAuditTaskByGuid(project.getTaskguid(), false).getResult();

            // 结束之间
            long endTime4 = System.currentTimeMillis();
            // 程序块运行时间
            log.info("Time4运行时间为：" + (endTime4 - startTime4) / 1000 + "秒");

            String handleUrl = "/epointzwfw/auditbusiness/auditproject/auditprojectinfo?taskguid="
                    + project.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
            String currentareacode = auditProject.getCurrentareacode();
            String rolename = "窗口人员";
            areacode = auditProject.getAreacode();
            // currentareacode 不为空代表是乡镇
            if (StringUtil.isNotBlank(currentareacode)) {
                areacode = currentareacode;
                if (currentareacode.length() == 9) {
                    rolename = "乡镇窗口人员";
                }
                else if (currentareacode.length() == 12) {
                    rolename = "村（社区）窗口人员 ";
                }
            }
            else {
                currentareacode = auditProject.getAreacode();
            }
            String title = "【办件预审】" + auditTask.getTaskname() + "(" + auditProject.getApplyername() + ")";
            List<FrameUser> frameUsers = windowservice.getFrameUserByTaskID(auditProject.getTask_id()).getResult();
            // 乡镇智能化,筛选用户
            List<FrameUser> frameRoleUsers = new ArrayList<FrameUser>();
            for (FrameUser frameUser : frameUsers) {
                if (roleservice.isExistUserRoleName(frameUser.getUserGuid(), rolename)) {
                    frameRoleUsers.add(frameUser);
                }
            }
            if (!frameRoleUsers.isEmpty()) {
                frameUsers = frameRoleUsers;
            }

            long startTime5 = System.currentTimeMillis();

            // 3、发送待办给审核人员 全流程审批通过后 不发送待办
            /*
             * if (StringUtil.isBlank(isfinish) || (!"1".equals(isfinish))) {
             * for (FrameUser frameuser : frameUsers) {
             * if (frameuser != null) {//
             * 第一次是list内部frameuser(这个实体只有userguid需要反查)
             * frameuser = userservice.getUserByUserField("userguid",
             * frameuser.getUserGuid());
             * if (frameuser != null) {// 第二次是复查frameuser
             * String messageItemGuid = UUID.randomUUID().toString();
             * messageCenterService.insertWaitHandleMessage(messageItemGuid,
             * title,
             * IMessagesCenterService.MESSAGETYPE_WAIT, frameuser.getUserGuid(),
             * frameuser.getDisplayName(), frameuser.getUserGuid(),
             * frameuser.getDisplayName(), "",
             * handleUrl, frameuser.getOuGuid(), "",
             * ZwfwConstant.CONSTANT_INT_ONE, "", "",
             * auditProject.getRowguid(), auditProject.getRowguid().substring(0,
             * 1), new Date(),
             * auditProject.getRowguid(), frameuser.getUserGuid(), "", "");
             * }
             * }
             * }
             * }
             */

            // 结束之间
            long endTime5 = System.currentTimeMillis();
            // 程序块运行时间
            log.info("Time5运行时间为：" + (endTime5 - startTime5) / 1000 + "秒");

            return JsonUtils.zwdtRestReturn("1", "提交办件成功", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 新增办件统计表记录
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/statisticProject", method = RequestMethod.POST)
    public String statisticProject(@RequestBody String params) {
        try {

            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String centerGuid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            String areaCode = obj.getString("areacode");

            // 新增自助服务办件统计表记录
            AuditZnsbSelfmachineproject auditZnsbSelfmachineproject = new AuditZnsbSelfmachineproject();
            auditZnsbSelfmachineproject.setRowguid(UUID.randomUUID().toString());
            auditZnsbSelfmachineproject.setCenterguid(centerGuid);
            auditZnsbSelfmachineproject.setMacaddres(macaddress);
            auditZnsbSelfmachineproject.setProjectguid(projectGuid);
            auditZnsbSelfmachineproject.setApplydate(new Date());
            if (StringUtil.isNotBlank(areaCode)) {
                AuditProject project = projectservice
                        .getAuditProjectByRowGuid("rowguid,taskguid", projectGuid, areaCode).getResult();
                AuditTask task = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                // taskname长度可以大于50字，大于50需要只保留前50
                if (StringUtil.isNotBlank(task.getTaskname()) && task.getTaskname().length() > 50) {
                    task.setTaskname(task.getTaskname().substring(0, 47) + "...");
                }
                auditZnsbSelfmachineproject.setTaskname(task.getTaskname());
                auditZnsbSelfmachineproject.setItem_id(task.getItem_id());
                auditZnsbSelfmachineproject.setAreacode(areaCode);
                auditZnsbSelfmachineproject.setOuguid(task.getOuguid());
            }

            auditZnsbSelfmachineprojectService.insert(auditZnsbSelfmachineproject);
            return JsonUtils.zwdtRestReturn("1", "统计办件成功", "");

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "统计办件失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取流程
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getProjectFlowlist", method = RequestMethod.POST)
    public String getProjectFlowlist(@RequestBody String params) {
        try {
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            JSONObject dataJson = new JSONObject();
            String projectGuid = obj.getString("projectguid");
            String areaCode = obj.getString("areacode");
            AuditProject project = projectservice
                    .getAuditProjectByRowGuid("pviguid,applydate,status,banjiedate", projectGuid, areaCode).getResult();
            if (project != null) {
                List<JSONObject> workItemList = new ArrayList<JSONObject>();
                JSONObject workitemJson = new JSONObject();

                // 申请时间
                workitemJson = new JSONObject();
                workitemJson.put("activityname", "申请");
                workitemJson.put("operationdate",
                        EpointDateUtil.convertDate2String(project.getApplydate(), "yyyy-MM-dd HH:mm"));
                workItemList.add(workitemJson);

                if (StringUtil.isNotBlank(project.getPviguid())) {
                    ProcessVersionInstance pvi = wfinstanceapi.getProcessVersionInstance(project.getPviguid());
                    ArrayList<Integer> statuslist = new ArrayList<>();
                    List<WorkflowWorkItem> WorkflowWorkItemlist = wfinstanceapi.getWorkItemListByPVIGuidAndStatus(pvi,
                            statuslist);
                    for (WorkflowWorkItem workflowWorkItem : WorkflowWorkItemlist) {
                        if (workflowWorkItem.getStatus() == 80) {
                            workitemJson = new JSONObject();
                            workitemJson.put("activityname", workflowWorkItem.getActivityName());
                            workitemJson.put("operationdate", EpointDateUtil
                                    .convertDate2String(workflowWorkItem.getOperationDate(), "yyyy-MM-dd HH:mm"));
                            workItemList.add(workitemJson);
                        }

                    }

                }
                else if (StringUtil.isNotBlank(project.getStatus()) && project.getStatus() >= 90) {
                    String lastactivityname = "";
                    switch (project.getStatus()) {
                        case 90:
                            lastactivityname = "正常办结";
                            break;
                        case 97:
                            lastactivityname = "不予受理";
                            break;
                        case 98:
                            lastactivityname = "撤销申请";
                            break;
                        case 99:
                            lastactivityname = "异常终止";
                            break;
                        default:
                            break;
                    }
                    workitemJson = new JSONObject();
                    workitemJson.put("activityname", lastactivityname);
                    workitemJson.put("operationdate",
                            EpointDateUtil.convertDate2String(project.getBanjiedate(), "yyyy-MM-dd HH:mm"));
                    workItemList.add(workitemJson);
                }
                dataJson.put("workitemlist", workItemList);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取流程
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getProjectFlowlist_en", method = RequestMethod.POST)
    public String getProjectFlowlist_en(@RequestBody String params) {
        try {
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            JSONObject dataJson = new JSONObject();
            String projectGuid = obj.getString("projectguid");
            String areaCode = obj.getString("areacode");
            AuditProject project = projectservice
                    .getAuditProjectByRowGuid("pviguid,applydate,status,banjiedate", projectGuid, areaCode).getResult();
            if (project != null) {
                List<JSONObject> workItemList = new ArrayList<JSONObject>();
                JSONObject workitemJson = new JSONObject();

                // 申请时间
                workitemJson = new JSONObject();
                workitemJson.put("activityname", "Apply");
                workitemJson.put("operationdate",
                        EpointDateUtil.convertDate2String(project.getApplydate(), "yyyy-MM-dd HH:mm"));
                workItemList.add(workitemJson);

                if (StringUtil.isNotBlank(project.getPviguid())) {
                    ProcessVersionInstance pvi = wfinstanceapi.getProcessVersionInstance(project.getPviguid());
                    ArrayList<Integer> statuslist = new ArrayList<>();
                    List<WorkflowWorkItem> WorkflowWorkItemlist = wfinstanceapi.getWorkItemListByPVIGuidAndStatus(pvi,
                            statuslist);
                    for (WorkflowWorkItem workflowWorkItem : WorkflowWorkItemlist) {
                        if (workflowWorkItem.getStatus() == 80) {
                            workitemJson = new JSONObject();
                            String activityname = "";
                            switch (workflowWorkItem.getActivityName()) {
                                case "受理":
                                    activityname = "Accepted";
                                    break;
                                case "审核":
                                    activityname = "Audited";
                                    break;
                                case "办结":
                                    activityname = "Handled";
                                    break;
                                default:
                                    activityname = workflowWorkItem.getActivityName();
                            }
                            workitemJson.put("activityname", activityname);
                            workitemJson.put("operationdate", EpointDateUtil
                                    .convertDate2String(workflowWorkItem.getOperationDate(), "yyyy-MM-dd HH:mm"));
                            workItemList.add(workitemJson);
                        }

                    }

                }
                else if (StringUtil.isNotBlank(project.getStatus()) && project.getStatus() >= 90) {
                    String lastactivityname = "";
                    switch (project.getStatus()) {
                        case 90:
                            lastactivityname = "Handled";
                            break;
                        case 97:
                            lastactivityname = "Not Accepted";
                            break;
                        case 98:
                            lastactivityname = "Withdrawn";
                            break;
                        case 99:
                            lastactivityname = "Abnomally Stopped";
                            break;
                        default:
                            break;
                    }
                    workitemJson = new JSONObject();
                    workitemJson.put("activityname", lastactivityname);
                    workitemJson.put("operationdate",
                            EpointDateUtil.convertDate2String(project.getBanjiedate(), "yyyy-MM-dd HH:mm"));
                    workItemList.add(workitemJson);
                }
                dataJson.put("workitemlist", workItemList);
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件详情Doc
     * 
     * @params params
     * @return
     * @throws Exception
     * 
     * 
     */
    @RequestMapping(value = "/getProjectDetailDoc", method = RequestMethod.POST)
    public String getProjectDetailDoc(@RequestBody String params, @Context HttpServletRequest request)
            throws Exception {

        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouname,promise_day,applydate,applyway,windowguid,certnum,contactmobile,contactphone,address,contactemail,contactpostcode,remark,contactcertnum,contactperson,centerguid";
            AuditProject project = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            if (project != null) {

                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                new License().setLicense(licenseName);// TODO
                String doctem = "";
                String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", project.getCenterguid())
                        .getResult();
                if (StringUtil.isNotBlank(downconfig)) {
                    doctem = downconfig + "/znsb/equipmentdisplay/selfservicemachine/print/projecttemplet.docx";
                }
                else {
                    doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/znsb/equipmentdisplay/selfservicemachine/print/projecttemplet.docx";
                }
                Document doc = new Document(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                // 获取域与对应的值
                Map<String, String> map = new HashMap<String, String>(16);
                map.put("ApplyName", project.getApplyername());
                map.put("IDNUM", project.getCertnum());
                map.put("CONTACTMOBILE", project.getContactmobile());
                map.put("Contactperson", project.getContactperson());
                map.put("CONTACTPHONE", project.getContactphone());
                map.put("Address", project.getAddress());

                AuditOrgaServiceCenter auditCenter = servicecenterservice
                        .findAuditServiceCenterByGuid(project.getCenterguid()).getResult();
                if (StringUtil.isNotBlank(auditCenter)) {
                    map.put("centername", auditCenter.getCentername());
                }
                else {
                    map.put("centername", "");
                }
                AuditOrgaConfig config = null;
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", project.getCenterguid());
                sql.eq("configname", "AS_CENTER_MOBILE");
                config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    map.put("centermobile", config.getConfigvalue());
                }
                else {
                    map.put("centermobile", "");
                }
                SqlConditionUtil sql1 = new SqlConditionUtil();
                sql1.eq("centerguid", project.getCenterguid());
                sql1.eq("configname", "AS_CENTER_WEB");
                config = auditconfigservice.getCenterConfig(sql1.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    map.put("centerweb", config.getConfigvalue());
                }
                else {
                    map.put("centerweb", "");
                }

                AuditTask task = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                if (task != null) {
                    map.put("taskname", task.getTaskname());
                    map.put("itemid", task.getItem_id());
                    map.put("typetext", codeitemsservice.getItemTextByCodeName("事项类型", String.valueOf(task.getType())));
                    map.put("applyertype", task.getApplyertype());
                    if ("10;".equals(task.getApplyertype())) {
                        map.put("applyertypetext", "企业");
                    }
                    else if ("20;".equals(task.getApplyertype())) {
                        map.put("applyertypetext", "个人");
                    }
                    else {
                        map.put("applyertypetext", "个人、企业");
                    }
                }
                AuditTaskResult taskresult = taskresultservice.getAuditResultByTaskGuid(project.getTaskguid(), true)
                        .getResult();
                if (taskresult != null) {
                    if (taskresult.getResulttype() == ZwfwConstant.LHSP_RESULTTYPE_ZZ) {
                        map.put("iscerttext", "是");
                    }
                    else {
                        map.put("iscerttext", "否");
                    }
                }
                else {
                    map.put("iscerttext", "否");
                }
                map.put("chargeflagtext",
                        codeitemsservice.getItemTextByCodeName("是否", String.valueOf(task.getCharge_flag())));
                map.put("condition", task.getAcceptcondition());

                fieldNames = new String[map.size()];
                values = new Object[map.size()];
                int num = 0;

                for (Entry<String, String> entry : map.entrySet()) {
                    fieldNames[num] = entry.getKey();
                    values[num] = entry.getValue();
                    num++;
                }
                // 替换域
                doc.getMailMerge().execute(fieldNames, values);
                // 替换表格--实际是替换材料
                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("projectguid", projectguid);
                sql2.in("status ",
                        "'" + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC + "'");

                List<AuditProjectMaterial> auditProjectMaterials = projectMaterialservice
                        .selectProjectMaterialByCondition(sql2.getMap()).getResult();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                Map<String, Object> data1 = null;

                if (auditProjectMaterials.isEmpty()) {
                    data1 = new HashMap<String, Object>(16);
                    data1.put("TASKMATERIAL", "无");
                    dataList.add(data1);
                }
                else {
                    for (int i = 0; i < auditProjectMaterials.size(); i++) {
                        data1 = new HashMap<String, Object>(16);
                        data1.put("TASKMATERIAL", (i + 1) + "、" + auditProjectMaterials.get(i).getTaskmaterial());
                        dataList.add(data1);
                    }
                }

                doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(dataList, "Material"));

                String projecturl = "";
                if (StringUtil.isNotBlank(project.getCenterguid())) {
                    SqlConditionUtil sql3 = new SqlConditionUtil();
                    sql3.eq("centerguid", project.getCenterguid());
                    sql3.eq("configname", "AS_PROJECT_URL");
                    AuditOrgaConfig config2 = auditconfigservice.getCenterConfig(sql3.getMap()).getResult();
                    if (StringUtil.isNotBlank(config2)) {
                        projecturl = config.getConfigvalue();
                    }
                    else {
                        projecturl = "";
                    }
                }

                // 添加二维码
                InputStream qrCode = QrcodeUtil.getQrCode(projecturl + "?projectguid=" + project.getRowguid(), 100,
                        100);

                if (qrCode != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    // 指定对应的书签
                    build.moveToBookmark("EWM");
                    // 插入附件流信息
                    build.insertImage(qrCode);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, SaveFormat.DOC);// 保存成word
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                long size = inputStream.available();

                // 附件信息
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("办件.doc");
                frameAttachInfo.setCliengTag("办件打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(size);
                dataJson.put("projectdocattachguid",
                        attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

                outputStream.close();
                inputStream.close();
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取办件详情Doc_en
     * 
     * @params params
     * @return
     * @throws Exception
     * 
     * 
     */
    @RequestMapping(value = "/getProjectDetailDoc_en", method = RequestMethod.POST)
    public String getProjectDetailDoc_en(@RequestBody String params, @Context HttpServletRequest request)
            throws Exception {

        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouname,promise_day,applydate,applyway,windowguid,certnum,contactmobile,contactphone,address,contactemail,contactpostcode,remark,contactcertnum,contactperson,centerguid";
            AuditProject project = projectservice.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            if (project != null) {

                String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                new License().setLicense(licenseName);// TODO
                String doctem = "";
                String downconfig = handleConfigservice.getFrameConfig("AS_LOCAL_DOWNLOAD_URL", project.getCenterguid())
                        .getResult();
                if (StringUtil.isNotBlank(downconfig)) {
                    doctem = downconfig + "/znsb/equipmentdisplay/selfservicemachine_en/print/projecttemplet.docx";
                }
                else {
                    doctem = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/znsb/equipmentdisplay/selfservicemachine_en/print/projecttemplet.docx";
                }
                Document doc = new Document(doctem);
                String[] fieldNames = null;
                Object[] values = null;
                // 获取域与对应的值
                Map<String, String> map = new HashMap<String, String>(16);
                map.put("ApplyName", project.getApplyername());
                map.put("IDNUM", project.getCertnum());
                map.put("CONTACTMOBILE", project.getContactmobile());
                map.put("Contactperson", project.getContactperson());
                map.put("CONTACTPHONE", project.getContactphone());
                map.put("Address", project.getAddress());

                AuditOrgaServiceCenter auditCenter = servicecenterservice
                        .findAuditServiceCenterByGuid(project.getCenterguid()).getResult();
                if (StringUtil.isNotBlank(auditCenter)) {
                    map.put("centername", auditCenter.getCentername());
                }
                else {
                    map.put("centername", "");
                }
                AuditOrgaConfig config = null;
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("centerguid", project.getCenterguid());
                sql.eq("configname", "AS_CENTER_MOBILE");
                config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    map.put("centermobile", config.getConfigvalue());
                }
                else {
                    map.put("centermobile", "");
                }
                SqlConditionUtil sql1 = new SqlConditionUtil();
                sql1.eq("centerguid", project.getCenterguid());
                sql1.eq("configname", "AS_CENTER_WEB");
                config = auditconfigservice.getCenterConfig(sql1.getMap()).getResult();
                if (StringUtil.isNotBlank(config)) {
                    map.put("centerweb", config.getConfigvalue());
                }
                else {
                    map.put("centerweb", "");
                }

                AuditTask task = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                if (task != null) {
                    map.put("taskname", task.getTaskname());
                    map.put("itemid", task.getItem_id());
                    map.put("typetext", codeitemsservice.getItemTextByCodeName("事项类型", String.valueOf(task.getType())));
                    map.put("applyertype", task.getApplyertype());
                    if ("10;".equals(task.getApplyertype())) {
                        map.put("applyertypetext", "Enterprise");
                    }
                    else if ("20;".equals(task.getApplyertype())) {
                        map.put("applyertypetext", "Individual");
                    }
                    else {
                        map.put("applyertypetext", "Individual,Enterprise");
                    }
                }
                AuditTaskResult taskresult = taskresultservice.getAuditResultByTaskGuid(project.getTaskguid(), true)
                        .getResult();
                if (taskresult != null) {
                    if (taskresult.getResulttype() == ZwfwConstant.LHSP_RESULTTYPE_ZZ) {
                        map.put("iscerttext", "Yes");
                    }
                    else {
                        map.put("iscerttext", "No");
                    }
                }
                else {
                    map.put("iscerttext", "No");
                }
                String flag = codeitemsservice.getItemTextByCodeName("是否", String.valueOf(task.getCharge_flag()));
                map.put("chargeflagtext", "是".equals(flag) ? "Yes" : "No");
                map.put("condition", task.getAcceptcondition());

                fieldNames = new String[map.size()];
                values = new Object[map.size()];
                int num = 0;

                for (Entry<String, String> entry : map.entrySet()) {
                    fieldNames[num] = entry.getKey();
                    values[num] = entry.getValue();
                    num++;
                }
                // 替换域
                doc.getMailMerge().execute(fieldNames, values);
                // 替换表格--实际是替换材料
                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("projectguid", projectguid);
                sql2.in("status ",
                        "'" + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC + "','"
                                + ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC + "'");

                List<AuditProjectMaterial> auditProjectMaterials = projectMaterialservice
                        .selectProjectMaterialByCondition(sql2.getMap()).getResult();
                List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                Map<String, Object> data1 = null;

                if (auditProjectMaterials.isEmpty()) {
                    data1 = new HashMap<String, Object>(16);
                    data1.put("TASKMATERIAL", "None");
                    dataList.add(data1);
                }
                else {
                    for (int i = 0; i < auditProjectMaterials.size(); i++) {
                        data1 = new HashMap<String, Object>(16);
                        data1.put("TASKMATERIAL", (i + 1) + "、" + auditProjectMaterials.get(i).getTaskmaterial());
                        dataList.add(data1);
                    }
                }

                doc.getMailMerge().executeWithRegions(new MapMailMergeDataSource(dataList, "Material"));

                String projecturl = "";
                if (StringUtil.isNotBlank(project.getCenterguid())) {
                    SqlConditionUtil sql3 = new SqlConditionUtil();
                    sql3.eq("centerguid", project.getCenterguid());
                    sql3.eq("configname", "AS_PROJECT_URL");
                    AuditOrgaConfig config2 = auditconfigservice.getCenterConfig(sql3.getMap()).getResult();
                    if (StringUtil.isNotBlank(config2)) {
                        projecturl = config.getConfigvalue();
                    }
                    else {
                        projecturl = "";
                    }
                }

                // 添加二维码
                InputStream qrCode = QrcodeUtil.getQrCode(projecturl + "?projectguid=" + project.getRowguid(), 100,
                        100);

                if (qrCode != null) {
                    DocumentBuilder build = new DocumentBuilder(doc);
                    // 指定对应的书签
                    build.moveToBookmark("EWM");
                    // 插入附件流信息
                    build.insertImage(qrCode);
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                doc.save(outputStream, SaveFormat.DOC);// 保存成word
                ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                long size = inputStream.available();

                // 附件信息
                FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
                frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                frameAttachInfo.setAttachFileName("办件.doc");
                frameAttachInfo.setCliengTag("办件打印");
                frameAttachInfo.setUploadUserGuid("");
                frameAttachInfo.setUploadUserDisplayName("");
                frameAttachInfo.setUploadDateTime(new Date());
                frameAttachInfo.setContentType("application/msword");
                frameAttachInfo.setAttachLength(size);
                dataJson.put("projectdocattachguid",
                        attachservice.addAttach(frameAttachInfo, inputStream).getAttachGuid());

                outputStream.close();
                inputStream.close();
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 自助申报演示接口
     */
    @RequestMapping(value = "/insertzzsbdemo", method = RequestMethod.POST)
    public String insertzzsbdemo(@RequestBody String params) throws Exception {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String projectguid = obj.getString("projectguid");
            String inputcompname = obj.getString("inputcompname");
            String cz = obj.getString("cz");
            String sqrzzbh = obj.getString("sqrzzbh");
            String sqrzzlx = obj.getString("sqrzzlx");
            String sqrlx = obj.getString("sqrlx");
            String tzxxm = obj.getString("tzxxm");
            String jkhtbh = obj.getString("jkhtbh");
            String inputzipcode = obj.getString("inputzipcode");
            String inputcreditcode = obj.getString("inputcreditcode");
            String inputocode = obj.getString("inputocode");
            String inputprincipal = obj.getString("inputprincipal");
            String inputpphone = obj.getString("inputpphone");
            String inputapplicant = obj.getString("inputapplicant");
            String inputapplicantphone = obj.getString("inputapplicantphone");
            String economiccode = obj.getString("economiccode");
            String inputidcard = obj.getString("inputidcard");
            String cardtype = obj.getString("cardtype");
            String comptype = obj.getString("comptype");
            String projecttype = obj.getString("projecttype");
            String placetype = obj.getString("placetype");
            String inputtotalstaff = obj.getString("inputtotalstaff");
            String inputtotalemployees = obj.getString("inputtotalemployees");
            String inputtotal_health_cert = obj.getString("inputtotal_health_cert");
            String inputtrain_pass_emps = obj.getString("inputtrain_pass_emps");
            String disinfect_facility = obj.getString("disinfect_facility");
            String purifier_facility = obj.getString("purifier_facility");
            String central_air_sys = obj.getString("central_air_sys");
            String dete_result = obj.getString("dete_result");
            String operation_status = obj.getString("operation_status");
            String water_type = obj.getString("water_type");
            String inputregister_person = obj.getString("inputregister_person");
            String inputregister_time = obj.getString("inputregister_time");
            String inputtotal_area = obj.getString("inputtotal_area");
            String inputactual_use_area = obj.getString("inputactual_use_area");
            JSONObject datajson = new JSONObject();
            AuditZnsbZzsbDemo auditznsbzzsbdemo = new AuditZnsbZzsbDemo();
            if (StringUtil.isNotBlank(inputcompname)) {
                auditznsbzzsbdemo.setComp_name(inputcompname);
            }
            if (StringUtil.isNotBlank(jkhtbh)) {
                auditznsbzzsbdemo.setProject_level_0125(jkhtbh);
            }

            if (StringUtil.isNotBlank(cz)) {
                auditznsbzzsbdemo.setCz(cz);
            }

            if (StringUtil.isNotBlank(inputregister_time)) {
                auditznsbzzsbdemo.setRegister_time(inputregister_time);
            }
            if (StringUtil.isNotBlank(inputtotal_area)) {
                auditznsbzzsbdemo.setTotal_area(Double.parseDouble(inputtotal_area));
            }

            if (StringUtil.isNotBlank(inputactual_use_area)) {
                auditznsbzzsbdemo.setActual_use_area(Double.parseDouble(inputactual_use_area));
            }
            if (StringUtil.isNotBlank(cz)) {
                auditznsbzzsbdemo.setCz(cz);
            }
            if (StringUtil.isNotBlank(sqrzzbh)) {
                auditznsbzzsbdemo.setSqrzzbh(sqrzzbh);
            }
            if (StringUtil.isNotBlank(sqrlx)) {
                auditznsbzzsbdemo.setSqrlx(sqrlx);
            }
            if (StringUtil.isNotBlank(tzxxm)) {
                auditznsbzzsbdemo.setTzxxm(tzxxm);
            }
            if (StringUtil.isNotBlank(sqrzzlx)) {
                auditznsbzzsbdemo.setSqrzzlx(sqrzzlx);
            }
            if (StringUtil.isNotBlank(inputzipcode)) {
                auditznsbzzsbdemo.setZipcode(inputzipcode);
            }
            if (StringUtil.isNotBlank(inputcreditcode)) {
                auditznsbzzsbdemo.setCredit_code(inputcreditcode);
            }
            if (StringUtil.isNotBlank(inputocode)) {
                auditznsbzzsbdemo.setOcode(inputocode);
            }
            if (StringUtil.isNotBlank(inputprincipal)) {
                auditznsbzzsbdemo.setPrincipal(inputprincipal);
            }
            if (StringUtil.isNotBlank(inputpphone)) {
                auditznsbzzsbdemo.setPphone(inputpphone);
            }
            if (StringUtil.isNotBlank(inputapplicant)) {
                auditznsbzzsbdemo.setApplicant(inputapplicant);
            }
            if (StringUtil.isNotBlank(inputapplicantphone)) {
                auditznsbzzsbdemo.setApplicant_phone(inputapplicantphone);
            }
            if (StringUtil.isNotBlank(economiccode)) {
                auditznsbzzsbdemo.setEconomic_code(economiccode);
            }
            if (StringUtil.isNotBlank(inputidcard)) {
                auditznsbzzsbdemo.setIdcard(inputidcard);
            }
            if (StringUtil.isNotBlank(cardtype)) {
                auditznsbzzsbdemo.setCardtype(cardtype);
            }
            if (StringUtil.isNotBlank(comptype)) {
                auditznsbzzsbdemo.setComp_type(comptype);
            }
            if (StringUtil.isNotBlank(projecttype)) {
                auditznsbzzsbdemo.setProject_type(projecttype);
            }
            if (StringUtil.isNotBlank(placetype)) {
                auditznsbzzsbdemo.setPlace_type(placetype);
            }
            if (StringUtil.isNotBlank(inputtotalstaff)) {
                auditznsbzzsbdemo.setTotal_staff(inputtotalstaff);
            }
            if (StringUtil.isNotBlank(inputtotalemployees)) {
                auditznsbzzsbdemo.setTotal_employees(inputtotalemployees);
            }
            if (StringUtil.isNotBlank(inputtotal_health_cert)) {
                auditznsbzzsbdemo.setTotal_health_cert(inputtotal_health_cert);
            }
            if (StringUtil.isNotBlank(inputtrain_pass_emps)) {
                auditznsbzzsbdemo.setTrain_pass_emps(inputtrain_pass_emps);
            }
            if (StringUtil.isNotBlank(disinfect_facility)) {
                auditznsbzzsbdemo.setDisinfect_facility(disinfect_facility);
            }
            if (StringUtil.isNotBlank(purifier_facility)) {
                auditznsbzzsbdemo.setPurifier_facility(purifier_facility);
            }
            if (StringUtil.isNotBlank(central_air_sys)) {
                auditznsbzzsbdemo.setCentral_air_sys(central_air_sys);
            }
            if (StringUtil.isNotBlank(dete_result)) {
                auditznsbzzsbdemo.setDete_result(dete_result);
            }
            if (StringUtil.isNotBlank(operation_status)) {
                auditznsbzzsbdemo.setOperation_status(operation_status);
            }
            if (StringUtil.isNotBlank(water_type)) {
                auditznsbzzsbdemo.setWater_type(water_type);
            }
            if (StringUtil.isNotBlank(inputregister_person)) {
                auditznsbzzsbdemo.setRegister_person(inputregister_person);
            }
            if (StringUtil.isNotBlank(projectguid)) {
                AuditZnsbZzsbDemo auditznsbzzsbdemo2 = auditZnsbZzsbDemoService.getDetailByProjectGuid(projectguid)
                        .getResult();
                if (StringUtil.isNotBlank(auditznsbzzsbdemo2)) {
                    if (StringUtil.isNotBlank(inputcompname)) {
                        auditznsbzzsbdemo2.setComp_name(inputcompname);
                    }
                    if (StringUtil.isNotBlank(cz)) {
                        auditznsbzzsbdemo2.setCz(cz);
                    }
                    if (StringUtil.isNotBlank(jkhtbh)) {
                        auditznsbzzsbdemo2.setProject_level_0125(jkhtbh);
                    }
                    if (StringUtil.isNotBlank(inputregister_time)) {
                        auditznsbzzsbdemo2.setRegister_time(inputregister_time);
                    }
                    if (StringUtil.isNotBlank(sqrzzbh)) {
                        auditznsbzzsbdemo2.setSqrzzbh(sqrzzbh);
                    }
                    if (StringUtil.isNotBlank(sqrlx)) {
                        auditznsbzzsbdemo2.setSqrlx(sqrlx);
                    }
                    if (StringUtil.isNotBlank(tzxxm)) {
                        auditznsbzzsbdemo2.setTzxxm(tzxxm);
                    }
                    if (StringUtil.isNotBlank(sqrzzlx)) {
                        auditznsbzzsbdemo2.setSqrzzlx(sqrzzlx);
                    }
                    if (StringUtil.isNotBlank(inputzipcode)) {
                        auditznsbzzsbdemo2.setZipcode(inputzipcode);
                    }
                    if (StringUtil.isNotBlank(inputcreditcode)) {
                        auditznsbzzsbdemo2.setCredit_code(inputcreditcode);
                    }
                    if (StringUtil.isNotBlank(inputocode)) {
                        auditznsbzzsbdemo2.setOcode(inputocode);
                    }
                    if (StringUtil.isNotBlank(inputtotal_area)) {
                        auditznsbzzsbdemo2.setTotal_area(Double.parseDouble(inputtotal_area));
                    }
                    if (StringUtil.isNotBlank(inputactual_use_area)) {
                        auditznsbzzsbdemo2.setActual_use_area(Double.parseDouble(inputactual_use_area));
                    }
                    if (StringUtil.isNotBlank(inputprincipal)) {
                        auditznsbzzsbdemo2.setPrincipal(inputprincipal);
                    }
                    if (StringUtil.isNotBlank(inputpphone)) {
                        auditznsbzzsbdemo2.setPphone(inputpphone);
                    }
                    if (StringUtil.isNotBlank(inputapplicant)) {
                        auditznsbzzsbdemo2.setApplicant(inputapplicant);
                    }
                    if (StringUtil.isNotBlank(inputapplicantphone)) {
                        auditznsbzzsbdemo2.setApplicant_phone(inputapplicantphone);
                    }
                    if (StringUtil.isNotBlank(economiccode)) {
                        auditznsbzzsbdemo2.setEconomic_code(economiccode);
                    }
                    if (StringUtil.isNotBlank(inputidcard)) {
                        auditznsbzzsbdemo2.setIdcard(inputidcard);
                    }
                    if (StringUtil.isNotBlank(cardtype)) {
                        auditznsbzzsbdemo2.setCardtype(cardtype);
                    }
                    if (StringUtil.isNotBlank(comptype)) {
                        auditznsbzzsbdemo2.setComp_type(comptype);
                    }
                    if (StringUtil.isNotBlank(projecttype)) {
                        auditznsbzzsbdemo2.setProject_type(projecttype);
                    }
                    if (StringUtil.isNotBlank(placetype)) {
                        auditznsbzzsbdemo2.setPlace_type(placetype);
                    }
                    if (StringUtil.isNotBlank(inputtotalstaff)) {
                        auditznsbzzsbdemo2.setTotal_staff(inputtotalstaff);
                    }
                    if (StringUtil.isNotBlank(inputtotalemployees)) {
                        auditznsbzzsbdemo2.setTotal_employees(inputtotalemployees);
                    }
                    if (StringUtil.isNotBlank(inputtotal_health_cert)) {
                        auditznsbzzsbdemo2.setTotal_health_cert(inputtotal_health_cert);
                    }
                    if (StringUtil.isNotBlank(inputtrain_pass_emps)) {
                        auditznsbzzsbdemo2.setTrain_pass_emps(inputtrain_pass_emps);
                    }
                    if (StringUtil.isNotBlank(disinfect_facility)) {
                        auditznsbzzsbdemo2.setDisinfect_facility(disinfect_facility);
                    }
                    if (StringUtil.isNotBlank(purifier_facility)) {
                        auditznsbzzsbdemo2.setPurifier_facility(purifier_facility);
                    }
                    if (StringUtil.isNotBlank(central_air_sys)) {
                        auditznsbzzsbdemo2.setCentral_air_sys(central_air_sys);
                    }
                    if (StringUtil.isNotBlank(dete_result)) {
                        auditznsbzzsbdemo2.setDete_result(dete_result);
                    }
                    if (StringUtil.isNotBlank(operation_status)) {
                        auditznsbzzsbdemo2.setOperation_status(operation_status);
                    }
                    if (StringUtil.isNotBlank(water_type)) {
                        auditznsbzzsbdemo2.setWater_type(water_type);
                    }
                    if (StringUtil.isNotBlank(inputregister_person)) {
                        auditznsbzzsbdemo2.setRegister_person(inputregister_person);
                    }
                    auditznsbzzsbdemo2.setOperatedate(new Date());
                    auditZnsbZzsbDemoService.update(auditznsbzzsbdemo2); // 已存在更新
                }
                else {
                    auditznsbzzsbdemo.setRowguid(UUID.randomUUID().toString());
                    auditznsbzzsbdemo.setOperatedate(new Date());
                    auditznsbzzsbdemo.setProjectguid(projectguid);
                    auditZnsbZzsbDemoService.insert(auditznsbzzsbdemo); // 不存在新增
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", datajson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 自助申报演示接口
     */
    @RequestMapping(value = "/getProjectDetaildemo", method = RequestMethod.POST)
    public String getProjectDetaildemo(@RequestBody String params) throws Exception {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String projectguid = obj.getString("projectguid");

            JSONObject datajson = new JSONObject();

            if (StringUtil.isNotBlank(projectguid)) {
                AuditZnsbZzsbDemo auditznsbzzsbdemo = auditZnsbZzsbDemoService.getDetailByProjectGuid(projectguid)
                        .getResult();
                if (StringUtil.isNotBlank(auditznsbzzsbdemo)) {
                    datajson.put("data", auditznsbzzsbdemo);
                }
                else {
                    datajson.put("data", "未查询到相关办件信息");
                }
            }
            return JsonUtils.zwdtRestReturn("1", "", datajson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（多情形办件申报提交时调用）
     * 
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkAllMaterialIsSubmit", method = RequestMethod.POST)
    public String checkAllMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkAllMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
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
                    AuditTaskMaterial auditTaskMaterial = taskMaterialservice
                            .getAuditTaskMaterialByRowguid(materialGuid).getResult();
                    if (auditTaskMaterial != null) {
                        int status = Integer.parseInt(materialStatus); // 办件材料表当前材料提交的状态
                        int isRongque = auditTaskMaterial.getIs_rongque(); // 材料容缺状态
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
                        // 7、如果材料设置了必填，则需要通过材料的提交方式进行判断（外网纸质材料不予判断）
                        if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)
                                && isRongque != ZwfwConstant.CONSTANT_INT_ONE) {
                            // 必要材料如果未提交，则数量+1
                            if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                    && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
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
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

}
