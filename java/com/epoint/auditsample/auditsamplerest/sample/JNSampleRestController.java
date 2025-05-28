package com.epoint.auditsample.auditsamplerest.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbappconfig.domain.AuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbappconfig.inter.IAuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbcentertask.inter.IAuditZnsbCentertask;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbsample.domain.AuditZnsbSample;
import com.epoint.basic.auditqueue.auditznsbsample.inter.IAuditZnsbSample;
import com.epoint.basic.auditqueue.auditznsbterminalapp.domain.AuditZnsbTerminalApp;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.spring.util.SpringContextUtil;

@RestController
@RequestMapping("/jnsample")
public class JNSampleRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;


    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAuditZnsbTerminalApp appservice;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IAuditTaskMaterial taskmaterialservice;

    @Autowired
    private IAuditZnsbSample sampleservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IAuditTask taskservice;
    
    @Autowired
    private IAuditZnsbCentertask centertaskservice;

    @Autowired
    private IConfigService configservice;
    
    @Autowired
    private IAuditZnsbAppConfig appConfigService;
    @Autowired
    private IAuditTaskCase auditTaskCaseService;
    @Autowired
    private IAuditTaskMaterialCase auditTaskMaterialCaseService;
    /**
     * 初始化
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_YDJ);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipmentservice.getDetailbyMacaddress(macaddress).getResult().getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    String config = handleConfigservice.getFrameConfig("AS_IS_ENABLE_APPLOG", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("recordlog", config);
                    }
                    else {
                        // 默认不记录日志
                        dataJson.put("recordlog", "0");
                    }
                    
                    if (QueueConstant.Common_yes_String.equals(configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE"))) {
                        dataJson.put("epointdevice", "1");
                    }
                      else {
                        // 默认不启用硬件管理平台
                        dataJson.put("epointdevice", "0");
                    }
                    
                    config = handleConfigservice.getFrameConfig("AS_RabbitMQ",centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("rabbitmq", config);
                        CachingConnectionFactory conn = null;
                        if (SpringContextUtil.getApplicationContext().containsBean("connectionFactory")) {
                            conn = SpringContextUtil.getBean("connectionFactory");
                            dataJson.put("rabbitmqusername", conn.getRabbitConnectionFactory().getUsername());
                            dataJson.put("rabbitmqpassword", conn.getRabbitConnectionFactory().getPassword());
                        } 

                    }
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
     * app更新
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
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
            AuditZnsbAppConfig appConfig = appConfigService.getConfigbyMacrowguid(macrowguid,fieldstr1).getResult();

            // 判断该设备是否使用个性化的APP
            if (StringUtil.isNotBlank(centerguid)) {
                // 设备与APP关系表里是否存在
                if (StringUtil.isNotBlank(appConfig)) {
                    String fieldstr2 = "AppInfo,UpdateTime,AppType,CenterGuid,CenterName,AppAttachGuid,IsUniversal";
                    AuditZnsbTerminalApp app = appservice.getAppByGuid(appConfig.getAppguid(),fieldstr2).getResult();
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
                            .getDetailbyApptype(QueueConstant.TerminalApp_Type_Sample, centerguid).getResult();
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
     * 获取样单部门
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSampleOUList", method = RequestMethod.POST)
    public String getSampleOUList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String macaddress = obj.getString("macaddress");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");

            String centerguid = "";
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject ouJson = new JSONObject();
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {               
                       
                        PageData<String> ouguidpagedata=centertaskservice.getSampleOUPageData(centerguid, Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize)).getResult();
                        int totalcount = ouguidpagedata.getRowCount();
                        for (String ouguid : ouguidpagedata.getList()) {
                            ouJson = new JSONObject();

                            ouJson.put("ouguid", ouguid);
                            ouJson.put("ouname",
                                    StringUtil.isNotBlank(ouservice.getOuByOuGuid(ouguid).getOushortName())
                                            ? ouservice.getOuByOuGuid(ouguid).getOushortName()
                                            : ouservice.getOuByOuGuid(ouguid).getOuname());
                            list.add(ouJson);
                        }

                        dataJson.put("oulist", list);
                        dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
                  
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
     * 获取样单事项
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSampleTaskList", method = RequestMethod.POST)
    public String getSampleTaskList(@RequestBody String params) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String macaddress = obj.getString("macaddress");
            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String ouguid = obj.getString("ouguid");
            String taskname = obj.getString("taskname");

            String centerguid = "";
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject taskJson = new JSONObject();
            PageData<Record> taskpagedata = null;
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                 
                        // 常用事项
                        if (("common".equals(ouguid))) {
                            taskpagedata = centertaskservice.getCommonSampleTaskPageData(centerguid,
                                    taskname, Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                    Integer.parseInt(pageSize)).getResult();

                        }
                        else {
                            taskpagedata = centertaskservice.getSampleTaskPageData(centerguid, ouguid,
                                    taskname, Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                                    Integer.parseInt(pageSize)).getResult();
                        }
                        int totalcount = taskpagedata.getRowCount();
                        for (Record task : taskpagedata.getList()) {
                            taskJson = new JSONObject();

                            taskJson.put("taskguid", task.get("taskguid"));
                            taskJson.put("taskname", task.get("taskname"));
                            List<AuditTaskCase> auditTaskCases = auditTaskCaseService.selectTaskCaseByTaskGuid(task.get("taskguid"))
                                    .getResult();
                            if(auditTaskCases.size()>0){
                                taskJson.put("materialcases", "1"); 
                            }else{
                                taskJson.put("materialcases", "0"); 
                            }
                            list.add(taskJson);
                        }

                        dataJson.put("tasklist", list);
                        dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));

                  
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
     * 获取材料情形
     * 
     * @params params
     * @return
     * @throws Exception
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("null")
    @RequestMapping(value = "/getSampleMaterialCase", method = RequestMethod.POST)
    public String getSampleMaterialCase(@RequestBody String params, HttpServletRequest request) throws Exception {

       
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            JSONObject taskJson = new JSONObject();
            List<JSONObject> caseJsons = new ArrayList<>();
            String taskguid = obj.getString("taskguid");
            List<AuditTaskCase> auditTaskCases = auditTaskCaseService.selectTaskCaseByTaskGuid(taskguid)
                    .getResult();
            if(auditTaskCases!=null||auditTaskCases.size()>1){
                for(AuditTaskCase materialcase :auditTaskCases){
                    JSONObject caseJson = new JSONObject();
                    caseJson.put("materialcase", materialcase.getRowguid());
                    caseJson.put("casename", materialcase.getCasename());
                    caseJsons.add(caseJson);
                   
                }
                taskJson.put("materialcase", caseJsons); 
                taskJson.put("casecount", auditTaskCases.size());
            }else{
                taskJson.put("materialcase", ""); 
                taskJson.put("casecount", 0);
            }
            dataJson.put("materialcases", taskJson);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    /**
     * 获取样单材料
     * 
     * @params params
     * @return
     * @throws Exception
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSampleMaterialList", method = RequestMethod.POST)
    public String getSampleMaterialList(@RequestBody String params, HttpServletRequest request) throws Exception {

       
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();

            String currentPage = obj.getString("currentpage");
            String pageSize = obj.getString("pagesize");
            String taskguid = obj.getString("taskguid");
            String materialcase = obj.getString("materialcase");
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject materialJson = new JSONObject();      
            SqlConditionUtil sql = new SqlConditionUtil();         
            List<AuditTaskMaterialCase> auditTaskMaterialCases = auditTaskMaterialCaseService
                    .selectTaskMaterialCaseByCaseGuid(materialcase).getResult();
            String casematerialguids="";
            int p =1;
            String casematlguid="";
            for(AuditTaskMaterialCase casematerial :auditTaskMaterialCases){
                if(p==1){
                    casematerialguids +=casematerial.getMaterialguid()+"',";
                    casematlguid = casematerial.getMaterialguid();
                }else if(p<auditTaskMaterialCases.size()){
                    casematerialguids +="'"+casematerial.getMaterialguid()+"',";
                }else{
                    casematerialguids +="'"+casematerial.getMaterialguid();
                }
                p++;
            }
            sql.eq("taskguid", taskguid);
            List<String> materialguids = sampleservice.getMaterialGuidByTaskGuid(taskguid).getResult();
            if (materialguids != null) {
                if(auditTaskMaterialCases.size()>1){
                    sql.in("rowguid","'"+ casematerialguids+"'");
                }else if(auditTaskMaterialCases.size()==1){
                    sql.eq("rowguid", casematlguid);
                }
                sql.in("materialid", "'" + StringUtil.join(materialguids, "','") + "'");
            }
            else
            {
                return JsonUtils.zwdtRestReturn("0", "样单生成中！", "");
            }
            PageData<AuditTaskMaterial> getmaterialpagedata = taskmaterialservice.getAuditTaskMaterialPageData(
                    sql.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                    Integer.parseInt(pageSize), "ordernum", "desc").getResult();
            int totalcount = getmaterialpagedata.getRowCount();
            List<AuditTaskMaterial> materiallist = getmaterialpagedata.getList();
            List<AuditZnsbSample> sampleList;

            for (int i = 0; i < materiallist.size(); i++) {
                materialJson = new JSONObject();
                materialJson.put("materialguid", materiallist.get(i).getMaterialid());
                materialJson.put("materialname", materiallist.get(i).getMaterialname());
                // 第一页
                SqlConditionUtil sql2 = new SqlConditionUtil();
                sql2.eq("materialguid", materiallist.get(i).getMaterialid());
                sampleList = sampleservice.getSampleList(sql2.getMap()).getResult();

                if (sampleList != null && sampleList.size() > 0) {
                    materialJson.put("materialurl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                            + "/rest/auditattach/readAttach?attachguid="
                            + sampleList.get(0).getSampleattachguid());
               
                }
                list.add(materialJson);
            }

            dataJson.put("ouname", taskservice.getAuditTaskByGuid(taskguid, true).getResult().getOuname());
            dataJson.put("totalcount", StringUtil.getNotNullString(totalcount));
            dataJson.put("materiallist", list);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取样单
     * 
     * @params params
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getSampleList", method = RequestMethod.POST)
    public String getSampleList(@RequestBody String params,HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String materialguid = obj.getString("materialguid");
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject sampleJson = new JSONObject();
            SqlConditionUtil sql=new SqlConditionUtil();          
            sql.eq("materialguid", materialguid);
            sql.setOrderAsc("OrderNum");
            List<AuditZnsbSample> samplelist = sampleservice.getSampleList(sql.getMap()).getResult();

            for (AuditZnsbSample sample : samplelist) {
                sampleJson = new JSONObject();

                sampleJson.put("sampleurl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/rest/auditattach/readAttach?attachguid="
                        + sample.getSampleattachguid());
                sampleJson.put("sampleurlsl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/rest/auditattach/readAttach?attachguid="
                        + sample.getSampleslattachguid());
                list.add(sampleJson);
            }
            dataJson.put("samplelist", list);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
