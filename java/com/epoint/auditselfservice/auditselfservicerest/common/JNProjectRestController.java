package com.epoint.auditselfservice.auditselfservicerest.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
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
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/jnselfserviceproject"})
public class JNProjectRestController {
    @Autowired
    private IAuditOrgaServiceCenter servicecenterservice;
    @Autowired
    private IJNProjectpvi pviservice;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private IAuditProject projectservice;
    @Autowired
    private IAuditOrgaWindowYjs windowservice;
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
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
   
    @RequestMapping(value = {"/submitProject"}, method = {RequestMethod.POST})
    public String submitProject(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
          
            Map<String, String> updateFieldMap = new HashMap(16);
            Map<String, String> updateDateFieldMap = new HashMap(16);
            SqlConditionUtil conditionMap = new SqlConditionUtil();
            JSONObject dataJson = new JSONObject();
            AuditProject project = projectservice.getAuditProjectByRowGuid(projectGuid, areacode)
                    .getResult();
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) this.taskExtensionservice
                    .getTaskExtensionByTaskGuid(project.getTaskguid(), true).getResult();
            if ("2".equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                updateFieldMap.put("status=", String.valueOf(26));
                project.setStatus(26);
                project.setApplyway(Integer.parseInt("50"));
            } else {
                updateFieldMap.put("status=", String.valueOf(12));
                project.setStatus(12);
            }
            if(StringUtil.isNotBlank(project.getTaskguid())){
                AuditTask task = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                if(task!=null ){
                    areacode = task.getAreacode();
                }
               
            }
            dataJson.put("areacode", areacode);
          //在浪潮库生成办件 
            // 来源（外网还是其他系统）
            ICommonDao baseDao=CommonDao.getInstance();
            AuditTask auditTask = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
            String resource = "1";
            String sql="select unid from audit_task where rowguid=?";
            String unid=baseDao.queryString(sql, project.getTaskguid());                   
            String receiveNum=project.getFlowsn();
            log.info("========================>修改前办件编号"+receiveNum);
            if(StringUtil.isNotBlank(unid)){
    			// 请求接口获取受理编码
    			if (StringUtil.isNotBlank(unid)) {
    				String result = FlowsnUtil.createReceiveNum(unid,auditTask.getRowguid());
    				if (!"error".equals(result)) {
    					log.info("========================>获取受理编码成功！" + result);
    					receiveNum = result;
    				} else {
    					log.info("========================>获取受理编码失败！");
    				}
    			}
            	
            	
                /* String item_Id = unid;
                    // 构造获取受理编码的请求入参
                    String params2Get = "?itemId=" + item_Id + "&applyFrom=" + resource+ "&is_lczj=6";
                    String pwd="";
                    // 请求接口获取受理编码
                    try {
                        JSONObject jsonObj = WavePushInterfaceUtils.createReceiveNum(params2Get, auditTask.getShenpilb());
                        
                        if (jsonObj != null && "200".equals(jsonObj.getString("state"))) {
                            log.info("========================>获取受理编码成功！" + jsonObj.getString("receiveNum") + "#####"
                                    + jsonObj.getString("password"));
                            pwd=jsonObj.getString("password");
                            receiveNum=jsonObj.getString("receiveNum");                         
                        }else{      
                            log.info("========================>获取受理编码失败！");
                        }
                    } catch (IOException e) {
                        log.info("接口请求报错！========================>" + e.getMessage());
                        e.printStackTrace();
                    }
                    project.set("pwd", pwd);*/
            }
            updateDateFieldMap.put("applydate=", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            conditionMap.eq("sourceguid", projectGuid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionMap.getMap());
            project.setFlowsn(receiveNum);
         
            project.setApplydate(new Date());
            project.setAreacode(areacode);
            project.setRowguid(projectGuid);
            projectservice.updateProject(project);
            AuditZnsbSelfmachineproject auditZnsbSelfmachineproject = new AuditZnsbSelfmachineproject();
            auditZnsbSelfmachineproject.setRowguid(UUID.randomUUID().toString());
            auditZnsbSelfmachineproject.setProjectguid(projectGuid);
            auditZnsbSelfmachineproject.setApplydate(new Date());
            
            return JsonUtils.zwdtRestReturn("1", "提交办件成功", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
        }
    }
    @RequestMapping(value = {"/aisubmitProject"}, method = {RequestMethod.POST})
    public String saveProjectInfo(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(jsonObject.getString("token"));
            JSONObject obj = (JSONObject) jsonObject.get("params");
            String projectGuid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
          
            Map<String, String> updateFieldMap = new HashMap(16);
            Map<String, String> updateDateFieldMap = new HashMap(16);
            SqlConditionUtil conditionMap = new SqlConditionUtil();
            JSONObject dataJson = new JSONObject();
            AuditProject project = projectservice.getAuditProjectByRowGuid(projectGuid, areacode)
                    .getResult();
            AuditTaskExtension auditTaskExtension = (AuditTaskExtension) this.taskExtensionservice
                    .getTaskExtensionByTaskGuid(project.getTaskguid(), true).getResult();
            if ("2".equals(String.valueOf(auditTaskExtension.getWebapplytype()))) {
                updateFieldMap.put("status=", String.valueOf(26));
                project.setStatus(26);
                project.setApplyway(Integer.parseInt("50"));
            } else {
                updateFieldMap.put("status=", String.valueOf(12));
                project.setStatus(12);
            }
            if(StringUtil.isNotBlank(project.getTaskguid())){
                AuditTask task = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
                if(task!=null ){
                    areacode = task.getAreacode();
                }
               
            }
            dataJson.put("areacode", areacode);
          //在浪潮库生成办件 
            // 来源（外网还是其他系统）
            ICommonDao baseDao=CommonDao.getInstance();
            AuditTask auditTask = taskservice.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
            String resource = "1";
            String sql="select unid from audit_task where rowguid=?";
            String unid=baseDao.queryString(sql, project.getTaskguid());                   
            String receiveNum=project.getFlowsn();
            log.info("========================>修改前办件编号"+receiveNum);
            if(StringUtil.isNotBlank(unid)){
            	
    			// 请求接口获取受理编码
    			if (StringUtil.isNotBlank(unid)) {
    				String result = FlowsnUtil.createReceiveNum(unid,auditTask.getRowguid());
    				if (!"error".equals(result)) {
    					receiveNum = result;
    					
    					log.info("========================>获取受理编码成功！" + result);
    				} else {
    					log.info("========================>获取受理编码失败！");
    				}
    			}
            	
                 /*String item_Id = unid;
                    // 构造获取受理编码的请求入参
                    String params2Get = "?itemId=" + item_Id + "&applyFrom=" + resource+ "&is_lczj=6";
                    String pwd="";
                    // 请求接口获取受理编码
                    try {
                        JSONObject jsonObj = WavePushInterfaceUtils.createReceiveNum(params2Get, auditTask.getShenpilb());
                        
                        if (jsonObj != null && "200".equals(jsonObj.getString("state"))) {
                            log.info("========================>获取受理编码成功！" + jsonObj.getString("receiveNum") + "#####"
                                    + jsonObj.getString("password"));
                            pwd=jsonObj.getString("password");
                            receiveNum=jsonObj.getString("receiveNum");                         
                        }else{      
                            log.info("========================>获取受理编码失败！");
                        }
                    } catch (IOException e) {
                        log.info("接口请求报错！========================>" + e.getMessage());
                        e.printStackTrace();
                    }
                    project.set("pwd", pwd);*/
            }
            updateDateFieldMap.put("applydate=", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            conditionMap.eq("sourceguid", projectGuid);
            onlineProjectservice.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionMap.getMap());
            project.setFlowsn(receiveNum);
         
            project.setApplydate(new Date());
            project.setAreacode(areacode);
            project.setRowguid(projectGuid);
            project.setStatus(90);
            project.setBanjieresult(40);
            project.setProjectname("<AI审批>" + auditTask.getTaskname());
            projectservice.updateProject(project);
            AuditZnsbSelfmachineproject auditZnsbSelfmachineproject = new AuditZnsbSelfmachineproject();
            auditZnsbSelfmachineproject.setRowguid(UUID.randomUUID().toString());
            auditZnsbSelfmachineproject.setProjectguid(projectGuid);
            auditZnsbSelfmachineproject.setApplydate(new Date());
            
            return JsonUtils.zwdtRestReturn("1", "提交办件成功", dataJson);
        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "提交办件失败：" + e.getMessage(), "");
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
                    List<WorkflowWorkItem> WorkflowWorkItemlist = new ArrayList<>();
                    if(pvi == null){
                        WorkflowWorkItemlist = pviservice.getworkflowbypvi(project.getPviguid());
                    }else{
                        ArrayList<Integer> statuslist = new ArrayList<>();
                        WorkflowWorkItemlist = wfinstanceapi.getWorkItemListByPVIGuidAndStatus(pvi,
                                statuslist);
                    }
                  
                    for (WorkflowWorkItem workflowWorkItem : WorkflowWorkItemlist) {
                        if(workflowWorkItem!=null){
                           
                            
                                    workitemJson = new JSONObject();
                                    workitemJson.put("activityname", workflowWorkItem.getActivityName());
                                    if(workflowWorkItem.getOperationDate()!=null){
                                        workitemJson.put("operationdate", EpointDateUtil
                                                .convertDate2String(workflowWorkItem.getOperationDate(), "yyyy-MM-dd HH:mm"));
                                    }else{
                                        workitemJson.put("operationdate","");
                         
                                    }
                                  
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
    @RequestMapping(value = {"/getProjectDetail"}, method = {RequestMethod.POST})
    public String getProjectDetail(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            SimpleDateFormat myFmt1=new SimpleDateFormat("yy年MM月dd日 HH:mm");
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,flowsn,ouname,promise_day,applydate,applyway,windowguid,certnum,contactmobile,contactphone,address,contactemail,contactpostcode,remark,contactcertnum,contactperson,centerguid,legal,legalid,certtype,acceptuserdate,banjiedate";
            AuditProject project = (AuditProject) this.projectservice
                    .getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
            if (project != null) {
                dataJson.put("projectguid", project.getRowguid());
                dataJson.put("projectname", project.getProjectname());
                dataJson.put("flowsn", project.getFlowsn());
                dataJson.put("ouname", project.getOuname());
                dataJson.put("promiseday", project.getPromise_day() + "个工作日");
                dataJson.put("applydate", EpointDateUtil.convertDate2String(project.getApplydate()));
                dataJson.put("applyway", project.getApplyway());
                dataJson.put("applywaytext",
                        this.codeitemsservice.getItemTextByCodeName("申请方式", String.valueOf(project.getApplyway())));
                dataJson.put("status", project.getStatus());
                dataJson.put("statustext",
                        this.codeitemsservice.getItemTextByCodeName("办件状态", String.valueOf(project.getStatus())));
                AuditOrgaWindow window = (AuditOrgaWindow) this.windowservice
                        .getWindowByWindowGuid(project.getWindowguid()).getResult();
                if (window != null) {
                    dataJson.put("windowtel", window.getTel());
                } else {
                    dataJson.put("windowtel", "");
                }

                dataJson.put("applyername", project.getApplyername());
                dataJson.put("certnum", project.getCertnum());
                dataJson.put("contactphone", project.getContactphone());
                if ("8".equals(project.getStatus().toString()) && StringUtil.isNotBlank(project.getCertnum())) {
                    AuditQueueUserinfo auditQueueUserinfo = (AuditQueueUserinfo) this.userinfoservice
                            .getUserinfo(project.getCertnum()).getResult();
                    AuditOnlineRegister register = (AuditOnlineRegister) this.registerservice
                            .getRegisterByIdorMobile(project.getCertnum()).getResult();
                    if (StringUtil.isNotBlank(auditQueueUserinfo)) {
                        dataJson.put("address", auditQueueUserinfo.getAddress());
                    } else {
                        dataJson.put("address", "");
                    }

                    if (StringUtil.isNotBlank(register)) {
                        dataJson.put("contactmobile", register.getMobile());
                    } else {
                        dataJson.put("contactmobile", "");
                    }
                } else {
                    dataJson.put("contactmobile", project.getContactmobile());
                    dataJson.put("address", project.getAddress());
                }
               
                dataJson.put("banjiedate",  myFmt1.format(project.getBanjiedate()));
                dataJson.put("acceptuserdate", myFmt1.format(project.getAcceptuserdate()));
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
                AuditOrgaConfig config = (AuditOrgaConfig) this.auditconfigservice.getCenterConfig(sql.getMap())
                        .getResult();
                if (StringUtil.isNotBlank(config)) {
                    dataJson.put("projecturl", config.getConfigvalue());
                } else {
                    dataJson.put("projecturl", "");
                }

                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("projectguid", projectguid);
                sql2.in("status ", "'15','20','25'");
                List<AuditProjectMaterial> auditProjectMaterials = (List) this.projectMaterialservice
                        .selectProjectMaterialByCondition(sql2.getMap()).getResult();
                List<Map<String, Object>> dataList = new ArrayList();
                Map<String, Object> data1 = null;
                if (auditProjectMaterials.size() != 0) {
                    for (int i = 0; i < auditProjectMaterials.size(); ++i) {
                        data1 = new HashMap(16);
                        data1.put("taskmaterial",
                                i + 1 + "、" + ((AuditProjectMaterial) auditProjectMaterials.get(i)).getTaskmaterial());
                        dataList.add(data1);
                    }
                }

                dataJson.put("material", dataList);
                return JsonUtils.zwdtRestReturn("1", "", dataJson);
            } else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }
        } catch (JSONException var17) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + var17.getMessage(), "");
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
                        .selectTaskCaseByTaskGuid(project.getTaskguid(),"1").getResult();

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
            } else {
                return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
            }

        } catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取办件多情形失败：" + e.getMessage(), "");
        }
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
            } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
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
            AuditTask task = taskservice.selectTaskByRowGuid(taskGuid).getResult();
           if(StringUtil.isNotBlank(task)){
               dataJson.put("taskid", task.getTask_id());
           }
            return JsonUtils.zwdtRestReturn("1", "初始化办件成功", dataJson);

        } catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "初始化办件失败：" + e.getMessage(), "");
        }
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
            } else if ("1".equals(type)) {
                sql.eq("auditstatus", ZwfwConstant.Material_AuditStatus_DBZ);
            } else if ("2".equals(type)) {
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
                } else if (necessity == Integer.parseInt(ZwfwConstant.NECESSITY_SET_YES)) {
                    // 不考虑纸质必填的
                    if (WorkflowKeyNames9.SubmitType_PaperSubmit == Integer
                            .parseInt(auditTaskMaterial.getSubmittype())) {
                        materialJson.put("necessary", "0");
                    } else {
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
                } else {
                    materialJson.put("attachguid", "");
                }
                // 获取排序值用于排序
                materialJson.put("ordernum",
                        StringUtil.isBlank(auditTaskMaterial.getOrdernum()) ? 0 : auditTaskMaterial.getOrdernum());
                if (StringUtil.isBlank(usestore)
                        || ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT == auditProjectMaterial.getStatus()) {
                    projectJsonlist.add(materialJson);
                } else {
                    totalcount--;
                }
            }
            // 先排序
            Collections.sort(projectJsonlist, new JNProjectRestController.MyComparator());
            // 再分页
            List<JSONObject> pageJsonlist = Page(projectJsonlist, Integer.parseInt(pageSize),
                    Integer.parseInt(currentPage));
            dataJson.put("materiallist", pageJsonlist);
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson);

        } catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }
    // 排序
    public class MyComparator implements Comparator<JSONObject> {
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


}