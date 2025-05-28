package com.epoint.auditevaluation.auditevaluationrest.evaluation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.FileItem;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbappconfig.domain.AuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbappconfig.inter.IAuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbinformation.domain.AuditZnsbInformation;
import com.epoint.basic.auditqueue.auditznsbinformation.inter.IAuditZnsbInformation;
import com.epoint.basic.auditqueue.auditznsblogoconfig.domain.AuditZnsbLogoconfig;
import com.epoint.basic.auditqueue.auditznsblogoconfig.inter.IAuditZnsbLogoconfigService;
import com.epoint.basic.auditqueue.auditznsbmachinelogo.domain.AuditZnsbMachineLogo;
import com.epoint.basic.auditqueue.auditznsbmachinelogo.inter.IAuditZnsbMachineLogoService;
import com.epoint.basic.auditqueue.auditznsbterminalapp.domain.AuditZnsbTerminalApp;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.faces.fileupload.MultipartRequest;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import com.epoint.frame.spring.util.SpringContextUtil;

@RestController
@RequestMapping("/jnevaluation")
public class JNEvaluationRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IAuditQueueOrgaWindow queuewindowservice;

    @Autowired
    private IUserService userservice;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IHandleTask handleTaskService;

    /*
     * @Autowired
     * private IAuditTaskExtension audittaskextension;
     */

    @Autowired
    private ICodeItemsService codeitemsservice;

    @Autowired
    private IAuditTaskMaterial audittaskmaterialservice;

    @Autowired
    private IAuditProject auditprojectservice;

    @Autowired
    private IAuditProjectMaterial auditprojectmaterialservice;

    @Autowired
    private IAuditOnlineEvaluat evaluateservice;

    @Autowired
    private IAuditZnsbInformation znsbinformationservice;

    @Autowired
    private IAuditZnsbTerminalApp appservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditProjectDocSnap auditprojectdocsnapservice;

    @Autowired
    private IConfigService configservice;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAuditZnsbAppConfig appConfigService;

    @Autowired
    private IAuditOrgaWindowYjs windowService;
    @Autowired
    private IAuditZnsbMachineLogoService machineLogoService;

    @Autowired
    private IAuditZnsbLogoconfigService logoConfigService;

    @Autowired
    private IAttachService attachService;

    /**
     * 获取人员信息
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public String getUserInfo(@RequestBody String params, HttpServletRequest request) throws IOException {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String macaddress = obj.getString("macaddress");
            String windowguid = "";
            String displayname = "";
            String ouname = "";
            String gonghao = "";
            String userguid = "";
            String photourl = "";
            String windowlabel = "";

            JSONObject dataJson = new JSONObject();
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();

            if (StringUtil.isNotBlank(equipment)) {
                windowguid = equipment.getWindowguid();
                if (StringUtil.isNotBlank(windowguid)) {
                    AuditOrgaWindow window = windowService.getWindowByWindowGuid(windowguid).getResult();
                    if (StringUtil.isNotBlank(window)) {
                        windowlabel = window.getStr("windowlabel");
                    }
                    AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
                    if (StringUtil.isNotBlank(queuewindow)) {
                        userguid = queuewindow.getUserguid();
                        if (StringUtil.isNotBlank(userguid)) {
                            FrameUser user = userservice.getUserByUserField("UserGuid", userguid);

                            if (user != null) {
                                displayname = user.getDisplayName();
                                FrameUserExtendInfo userextendinfo = userservice.getUserExtendInfoByUserGuid(userguid);
                                if (userextendinfo != null) {
                                    gonghao = userextendinfo.getShortMobile();
                                }
                                FrameOu ou = ouservice.getOuByOuGuid(user.getOuGuid());
                                if (ou != null) {
                                    ouname = StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName()
                                            : ou.getOuname();
                                }
                                photourl = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/getUserPic?userguid=" + userguid;
                                /*
                                 * String attachGuid = userguid; String prepath
                                 * = QueueCommonUtil.getUrlPath(request.
                                 * getRequestURL().toString()) +
                                 * "/AttachStorage/" + attachGuid + "/"; String
                                 * filepath = ClassPathUtil.getDeployWarPath() +
                                 * "AttachStorage/" + attachGuid + "/";
                                 * 
                                 * // 创建文件夹 File file1 = new File(filepath); if
                                 * (!file1.exists()) { file1.mkdirs(); } //
                                 * 创建文件夹下目录 String filename = "loginpic.jpg";
                                 * 
                                 * File file2 = new File(file1, filename); if
                                 * (!file2.exists()) {
                                 * 
                                 * byte[] mypic =
                                 * userservice.getUserPic(userguid); if (mypic
                                 * != null) { file2.createNewFile();
                                 * 
                                 * FileOutputStream fileOutputStream = new
                                 * FileOutputStream(file2); // 读文件 InputStream
                                 * in = new ByteArrayInputStream(mypic); byte[]
                                 * b = new byte[1024]; int len = 0; // 写文件 while
                                 * ((len = in.read(b)) != -1) {
                                 * fileOutputStream.write(b, 0, len); }
                                 * fileOutputStream.flush();
                                 * fileOutputStream.close(); in.close();
                                 * photourl = prepath + filename; } } else {
                                 * photourl = prepath + filename; }
                                 */

                            }

                        }
                    }
                }
                else {

                    return JsonUtils.zwdtRestReturn("0", "该PAD未配置窗口！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            dataJson.put("displayname", displayname);
            dataJson.put("ouname", ouname);
            dataJson.put("gonghao", gonghao);
            dataJson.put("photourl", photourl);
            dataJson.put("windowlabel", windowlabel);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取窗口事项
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getWindowTask", method = RequestMethod.POST)
    public String getWindowTask(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String macaddress = obj.getString("macaddress");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String taskname = obj.getString("taskname");

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject taskJson = new JSONObject();

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {

                if (StringUtil.isNotBlank(equipment.getWindowguid())) {
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(taskname)) {
                        conditionsql.like("taskname", taskname);
                    }

                    PageData<AuditTask> audittaskpagedata = handleTaskService
                            .getAuditTaskPageDataByWindow(equipment.getWindowguid(), conditionsql.getMap(),
                                    Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                    Integer.parseInt(pageSize), "ordernum", "desc")
                            .getResult();
                    int totalcount = audittaskpagedata.getRowCount();
                    for (AuditTask audittask : audittaskpagedata.getList()) {
                        taskJson = new JSONObject();
                        taskJson.put("taskname", audittask.getTaskname());
                        taskJson.put("taskguid", audittask.getRowguid());

                        list.add(taskJson);
                    }

                    dataJson.put("tasklist", list);
                    dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
                }
                else {

                    return JsonUtils.zwdtRestReturn("0", "该PAD未配置窗口！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取事项基本信息
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getTaskDetail", method = RequestMethod.POST)
    public String getTaskDetail(@RequestBody String params) {
        try {

            JSONObject json = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));

            JSONObject obj = (JSONObject) json.get("params");
            String taskGuid = obj.getString("taskguid");

            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, false).getResult();
            if (auditTask == null) {
                auditTask = new AuditTask();
            }

            /*
             * AuditTaskExtension auditTaskExtension =
             * audittaskextension.getTaskExtensionByTaskGuid(taskGuid, false)
             * .getResult();
             * if (auditTaskExtension == null) {
             * auditTaskExtension = new AuditTaskExtension();
             * }
             */

            // 3、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("taskguid", taskGuid);// 事项guid
            dataJson.put("taskname", auditTask.getTaskname());// 事项名称
            dataJson.put("itemid", auditTask.getItem_id());// 权利编码
            String applyertype = auditTask.getApplyertype();
            String managementobj = "";
            if (StringUtil.isNotBlank(applyertype)) {
                String[] applyertypearr = applyertype.split(";");
                for (int i = 0; i < applyertypearr.length; i++) {
                    managementobj = codeitemsservice.getItemTextByCodeName("申请人类型", applyertypearr[i]) + ";";
                }
                managementobj = managementobj.substring(0, managementobj.length() - 1);
            }
            dataJson.put("applyertype", managementobj);// 办理对象
            dataJson.put("type", codeitemsservice.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办理类型
            dataJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");// 法定期限
            dataJson.put("promiseday", auditTask.getPromise_day() + "工作日");// 承诺期限
            dataJson.put("linktel", auditTask.getLink_tel());// 咨询电话
            dataJson.put("supervisetel", auditTask.getSupervise_tel());// 投诉电话
            dataJson.put("acceptcondition", auditTask.getAcceptcondition());// 受理条件
            dataJson.put("bylaw", auditTask.getBy_law());// 设定依据
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(String.valueOf(auditTask.getCharge_flag()))) {
                dataJson.put("chargeflag", "收费");
            }
            else {
                dataJson.put("chargeflag", "不收费");
            }
            dataJson.put("shenpilb",
                    codeitemsservice.getItemTextByCodeName("审批类别", String.valueOf(auditTask.getShenpilb())));// 审批类别

            List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
            List<AuditTaskMaterial> auditTaskMaterials = audittaskmaterialservice
                    .selectTaskMaterialListByTaskGuid(taskGuid, false).getResult();
            for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                JSONObject materialJson = new JSONObject();
                materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                taskMaterialList.add(materialJson);
            }
            dataJson.put("taskmaterials", taskMaterialList);
            dataJson = JSONObject.parseObject(dataJson.toJSONString().replaceAll("&nbsp;", "  "));
            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {

            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取办件详情
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
    public String getProjectDetail(@RequestBody String params) {
        try {

            JSONObject json = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String projectguid = obj.getString("projectguid");
            String areacode = obj.getString("areacode");
            JSONObject dataJson = new JSONObject();

            String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,certnum,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,remark,certtype,applyertype,legal ";
            AuditProject auditProject = auditprojectservice.getAuditProjectByRowGuid(fields, projectguid, areacode)
                    .getResult();
            if (auditProject != null) {
                dataJson.put("projectname", auditProject.getProjectname());// 办件名称
                dataJson.put("applyername", auditProject.getApplyername());// 申请人/申请单位
                dataJson.put("applyercertnum", auditProject.getCertnum());// 申请人证照编号
                dataJson.put("legal", auditProject.getLegal());// 法人代表
                dataJson.put("contactperson", auditProject.getContactperson());// 联系人
                dataJson.put("contactmobile", auditProject.getContactmobile());// 联系人手机
                dataJson.put("contactphone", auditProject.getContactphone());// 联系人电话
                dataJson.put("contactcertnum", auditProject.getContactcertnum());// 联系人身份证
                dataJson.put("contactpostcode", auditProject.getContactpostcode());// 邮编
                dataJson.put("address", auditProject.getAddress());// 地址
                dataJson.put("contactfax", auditProject.getContactfax());// 传真
                dataJson.put("contactemail", auditProject.getContactemail());// 电子邮件
                dataJson.put("certtype", codeitemsservice.getItemTextByCodeName("申请人用来唯一标识的证照类型",
                        String.valueOf(auditProject.getCerttype())));// 证照类型
                dataJson.put("applyertype",
                        codeitemsservice.getItemTextByCodeName("申请人类型", String.valueOf(auditProject.getApplyertype())));// 申请人类型
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                conditionsql.eq("projectguid", projectguid);
                List<JSONObject> materialList = new ArrayList<JSONObject>();
                List<AuditProjectMaterial> auditProjectMaterials = auditprojectmaterialservice
                        .selectProjectMaterialByCondition(conditionsql.getMap()).getResult();
                for (AuditProjectMaterial auditProjectMaterial : auditProjectMaterials) {
                    JSONObject materialJson = new JSONObject();
                    materialJson.put("projectmaterialguid", auditProjectMaterial.getRowguid());
                    materialJson.put("projectmaterialname", auditProjectMaterial.getTaskmaterial());
                    materialJson.put("projectmaterialstatus", codeitemsservice.getItemTextByCodeName("材料提交状态",
                            String.valueOf(auditProjectMaterial.getStatus())));// 材料提交状态
                    materialList.add(materialJson);
                }

                dataJson.put("materiallist", materialList);
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 评价
     * 
     * @params params
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/evaluate", method = RequestMethod.POST)
    public String evaluate(@RequestBody String params) {
        try {

            JSONObject json = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String clientidentifier = obj.getString("clientidentifier");
            String clienttype = obj.getString("clienttype");// 评价对象（10：办件；20：事项；30：排队号）
            String macaddress = obj.getString("macaddress");
            String satisfied = obj.getString("satisfied");// 满意度
            JSONObject dataJson = new JSONObject();

            if (!evaluateservice.isExistEvaluate(clientidentifier).getResult()) {
                AuditOnlineEvaluat evaluat = new AuditOnlineEvaluat();
                evaluat.setRowguid(UUID.randomUUID().toString());
                evaluat.setEvaluatetype(ZwfwConstant.Evaluate_type_PJQ);
                evaluat.setClientidentifier(clientidentifier);
                evaluat.setClienttype(clienttype);
                evaluat.setEvaluatedate(new Date());
                evaluat.setSatisfied(satisfied);
                evaluateservice.addAuditOnineEvaluat(evaluat);
            }
            else {
                AuditOnlineEvaluat evaluat = evaluateservice.selectEvaluatByClientIdentifier(clientidentifier)
                        .getResult();
                if (evaluat != null) {
                    evaluat.setEvaluatetype(ZwfwConstant.Evaluate_type_PJQ);
                    evaluat.setClientidentifier(clientidentifier);
                    evaluat.setClienttype(clienttype);
                    evaluat.setEvaluatedate(new Date());
                    evaluat.setSatisfied(satisfied);
                    evaluateservice.updateAuditOnineEvaluat(evaluat);
                }

            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);

        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取信息
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/getInformation", method = RequestMethod.POST)
    public String getInformation(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String macaddress = obj.getString("macaddress");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String type = obj.getString("type");// 10 代表服务承诺 20代表岗位职责

            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject informationJson = new JSONObject();

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                if (StringUtil.isNotBlank(equipment.getWindowguid())) {
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(type)) {
                        conditionsql.eq("type", type);
                    }
                    PageData<AuditZnsbInformation> informationpagedata = znsbinformationservice.getInformationPageData(
                            conditionsql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                            Integer.parseInt(pageSize), "ordernum", "desc").getResult();
                    int totalcount = informationpagedata.getRowCount();
                    for (AuditZnsbInformation information : informationpagedata.getList()) {
                        informationJson = new JSONObject();
                        informationJson.put("information", information.getInformation());

                        list.add(informationJson);
                    }

                    dataJson.put("informationlist", list);
                    dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
                }
                else {

                    return JsonUtils.zwdtRestReturn("0", "该PAD未配置窗口！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * app更新
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/updateApp", method = RequestMethod.POST)
    public String updateApp(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String macaddress = obj.getString("macaddress");
            String info = obj.getString("info");

            String apppath = "";
            String centerguid = "";
            String macrowguid = "";
            JSONObject dataJson = new JSONObject();

            // 根据macaddress获取中心guid
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress, "centerguid,rowguid")
                    .getResult();
            centerguid = equipment.getCenterguid();
            macrowguid = equipment.getRowguid();
            String fieldstr1 = "centerguid,macrowguid,appguid,equipmentname";
            AuditZnsbAppConfig appConfig = appConfigService.getConfigbyMacrowguid(macrowguid, fieldstr1).getResult();

            // 判断该设备是否使用个性化的APP
            if (StringUtil.isNotBlank(centerguid)) {
                // 设备与APP关系表里是否存在
                if (StringUtil.isNotBlank(appConfig)) {
                    String fieldstr2 = "AppInfo,UpdateTime,AppType,CenterGuid,CenterName,AppAttachGuid,IsUniversal";
                    AuditZnsbTerminalApp app = appservice.getAppByGuid(appConfig.getAppguid(), fieldstr2).getResult();
                    if (StringUtil.isNotBlank(app.getAppattachguid())) {
                        if (Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))) {

                            List<FrameAttachInfo> attachinfolist = attachservice
                                    .getAttachInfoListByGuid(app.getAppattachguid());
                            if (attachinfolist != null && attachinfolist.size() > 0) {
                                apppath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid="
                                        + attachinfolist.get(0).getAttachGuid();
                            }
                        }
                    }

                }
                else {
                    AuditZnsbTerminalApp app = appservice
                            .getDetailbyApptype(QueueConstant.TerminalApp_Type_Evaluation, centerguid).getResult();
                    if (StringUtil.isNotBlank(app)) {
                        if (Integer.parseInt(QueueCommonUtil.padRight(info.replace(".", ""), 3, '0')) < Integer
                                .parseInt(QueueCommonUtil.padRight(app.getAppinfo().replace(".", ""), 3, '0'))) {

                            if (StringUtil.isNotBlank(app.getAppattachguid())) {
                                List<FrameAttachInfo> attachinfolist = attachservice
                                        .getAttachInfoListByGuid(app.getAppattachguid());
                                if (attachinfolist != null && attachinfolist.size() > 0) {
                                    apppath = QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                            + "/rest/auditattach/readAttach?attachguid="
                                            + attachinfolist.get(0).getAttachGuid();
                                }

                            }
                        }

                    }
                }
            }

            dataJson.put("apppath", apppath);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 初始化
     * 
     * @params params
     * @return
     * 
     * 
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params, @Context HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String logomachinetype = obj.getString("logomachinetype");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_PJPAD);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    dataJson.put("centerguid", centerguid);
                    String config = handleConfigservice.getFrameConfig("AS_RabbitMQ", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("rabbitmq", config);
                        CachingConnectionFactory conn = null;
                        if (SpringContextUtil.getApplicationContext().containsBean("connectionFactory")) {
                            conn = SpringContextUtil.getBean("connectionFactory");
                            dataJson.put("rabbitmqusername", conn.getRabbitConnectionFactory().getUsername());
                            dataJson.put("rabbitmqpassword", conn.getRabbitConnectionFactory().getPassword());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "RabbitMQ未配置！", "");
                        }

                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "系统参数AS_RabbitMQ未配置！", "");

                    }
                    config = handleConfigservice.getFrameConfig("AS_IS_ENABLE_APPLOG", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("recordlog", config);
                    }
                    else {
                        // 默认不记录日志
                        dataJson.put("recordlog", "0");
                    }

                    config = configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE");
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("epointdevice", config);

                    }
                    else {
                        // 默认不启用硬件管理平台
                        dataJson.put("epointdevice", "0");
                    }
                    // logo后台可配置开始
                    List<String> logourllist = new ArrayList<String>();
                    // 获取个性化的logo，就不再查询通用logo地址
                    String fields = "logoguid";
                    boolean getCommon = false;
                    // 首先通过设备rowguid查询logo个性化表记录，如果有，则表示改设备被个性化了
                    AuditZnsbLogoconfig logoconfig = logoConfigService
                            .getConfigbyMacinerowguid(equipment.getRowguid(), fields).getResult();
                    if (StringUtil.isNotBlank(logoconfig)) {
                        String logoguid = logoconfig.getLogoguid();
                        AuditZnsbMachineLogo machineLogo = machineLogoService.find(logoguid);
                        // 通过获取到的设备logo，判断当前设备个性化的类型是不是和当前设备logo类型一致，一致则获取logo，不一致，就不获取该个性化，进而继续判断改设备有无通用。
                        if (StringUtil.isNotBlank(machineLogo)
                                && QueueConstant.CONSTANT_STR_ONE.equals(machineLogo.getIsuniversal())
                                && machineLogo.getMachinetype().equals(logomachinetype)) {
                            List<FrameAttachInfo> logoattachlist = attachService
                                    .getAttachInfoListByGuid(machineLogo.getLogocliengguid());
                            for (FrameAttachInfo frameAttachInfo : logoattachlist) {
                                logourllist.add(QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid());
                            }
                        }
                        else {
                            getCommon = true;
                        }

                    }
                    else {
                        getCommon = true;
                    }
                    if (getCommon) {
                        // 获取通用的logo
                        AuditZnsbMachineLogo commonMachineLogo = machineLogoService
                                .getLogoByMachineTypeAndCenterGuid(logomachinetype, centerguid).getResult();
                        if (StringUtil.isNotBlank(commonMachineLogo)
                                && QueueConstant.CONSTANT_STR_ZERO.equals(commonMachineLogo.getIsuniversal())) {
                            List<FrameAttachInfo> commonlogoattachlist = attachService
                                    .getAttachInfoListByGuid(commonMachineLogo.getLogocliengguid());
                            for (FrameAttachInfo frameAttachInfo : commonlogoattachlist) {
                                logourllist.add(QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                        + "/rest/auditattach/readAttach?attachguid=" + frameAttachInfo.getAttachGuid());
                            }
                        }
                    }
                    dataJson.put("logourllist", logourllist);
                    // logo后台可配置结束
                    // 判断是否启用
                    if (QueueConstant.EQUIPMENT_STATUS_OFFLINE.equals(equipment.getStatus())) {
                        return JsonUtils.zwdtRestReturn("0", "设备已离线！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 手写签批pdf上传接口
     * 
     * @params params
     * @return
     */
    @RequestMapping(value = "/uploadhandwrite", method = RequestMethod.POST)
    public String uploadhandwrite(HttpServletRequest request) {
        try {
            // 从multipartRequest获取POST的流文件并保存到数据库
            request = (MultipartRequest) request;
            String docsnapguid = request.getParameter("docsnapguid");
            savehandwriteAttach((MultipartRequest) request, docsnapguid);
            return JsonUtils.zwdtRestReturn("1", "上传成功", "");
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "上传失败：" + e.getMessage(), "");
        }
    }

    public void savehandwriteAttach(MultipartRequest multipartRequest, String docsnapguid) throws IOException {
        FrameAttachInfo frameAttachInfo = null;
        Map<String, List<FileItem>> map = multipartRequest.getFileParams();
        for (Map.Entry<String, List<FileItem>> en : map.entrySet()) {
            List<FileItem> fileItems = en.getValue();
            if (fileItems != null && !fileItems.isEmpty()) {
                for (FileItem fileItem : fileItems) {
                    if (!fileItem.isFormField()) {// 是文件流而不是表单数据
                        String fileName;
                        // 从文件流中获取文件名
                        fileName = fileItem.getName();

                        int index = fileName.lastIndexOf("\\");
                        fileName = fileName.substring(++index);
                        // 从文件流中获取文件类型
                        String contentType = fileItem.getContentType();
                        // 获取流大小
                        long size = fileItem.getSize();
                        // 获取流
                        InputStream inputStream = fileItem.getInputStream();

                        // 附件信息
                        frameAttachInfo = new FrameAttachInfo();
                        frameAttachInfo.setCliengGuid(UUID.randomUUID().toString());
                        frameAttachInfo.setAttachFileName(fileName);
                        frameAttachInfo.setCliengTag("手写签批");
                        frameAttachInfo.setUploadUserGuid("");
                        frameAttachInfo.setUploadUserDisplayName("");
                        frameAttachInfo.setUploadDateTime(new Date());
                        frameAttachInfo.setContentType(contentType);
                        frameAttachInfo.setAttachLength(size);
                        FrameAttachInfo frameAttachInfopdf = attachservice.addAttach(frameAttachInfo, inputStream);

                        // 更新通知书pdf附件
                        AuditProjectDocSnap auditProjectDocSnap = auditprojectdocsnapservice
                                .selectDocSnapByRowGuid(docsnapguid).getResult();
                        if (auditProjectDocSnap != null) {
                            auditProjectDocSnap.setPdfattachguid(frameAttachInfopdf.getAttachGuid());
                            auditprojectdocsnapservice.updateDocSnap(auditProjectDocSnap);
                        }
                    }
                }
            }
        }

    }

}
