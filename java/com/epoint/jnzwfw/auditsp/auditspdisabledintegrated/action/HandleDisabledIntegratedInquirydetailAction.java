package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.AuditYjsCjr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 项目基本信息表新增页面对应的后台
 *
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("handledisabledintegratedinquirydetailaction")
@Scope("request")
public class HandleDisabledIntegratedInquirydetailAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = -6032319360335941356L;

    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    /**
     * 材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    @Autowired
    private IAuditYjsCjrService iAuditYjsCjrService;
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditTask iaudittask;
    @Autowired
    private IAuditTaskExtension iaudittaskextension;
    @Autowired
    private IAuditProject iauditproject;
    @Autowired
    private IAuditOnlineProject iauditonlineproject;
    @Autowired
    private IHandleConfig ihandleconfig;
    @Autowired
    private IHandleFlowSn ihandleflowsn;
    @Autowired
    private IHandleProject ihandleproject;
    @Autowired
    private IAuditProjectSparetime iauditprojectsparetime;
    @Autowired
    private IAuditSpSelectedOptionService iauditspselectedoptionservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;
    
    @Autowired
    private IAuditSpBusiness iauditspbusiness;
    
    @Autowired
    private IConfigService configServicce;
    

    /**
     * 项目基本信息表实体对象
     */
    private AuditYjsCjr dataBean = null;
    /**
     * 项目申报日期
     */
    private Date createdate;
    /**
     * 事项与材料情况
     */
    private String materialdesc = "";
    /**
     * 材料提交情况
     */
    private String materialsubmitdesc = "";
    /**
     * 主题实例标识
     */
    private String biGuid = "";
    /**
     * 主题标识
     */
    private String businessGuid = "";
    /**
     * 主题实例
     */
    private AuditSpInstance spInstance;
    /**
     * 子申报实例标识
     */
    private String subappGuid;

    private List<AuditSpITask> spITasks = null;

    private DataGridModel<AuditSpITask> modelTask = null;

    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("biGuid");
        spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
            List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
            AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
            subappGuid = auditSpISubapp.getRowguid();
            businessGuid = spInstance.getBusinessguid();
            dataBean = iAuditYjsCjrService.find(spInstance.getYewuguid());
            
            AuditSpBusiness auditSpBusiness = iauditspbusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
            
            if (auditSpBusiness != null) {
            	 addCallbackParam("newformid", auditSpBusiness.get("yjsformid")); 
            	 if (StringUtil.isNotBlank(auditSpISubapp.getStr("optionfromid"))) {
            		 addCallbackParam("formids", auditSpISubapp.get("optionfromid"));
            	 }
                 addCallbackParam("eformCommonPage",configServicce.getFrameConfigValue("eformCommonPage"));
                 addCallbackParam("epointUrl",configServicce.getFrameConfigValue("epointsformurl"));
            }
            if(ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBusiness.getIsaccept())){  
                addCallbackParam("allowaccept", ZwfwConstant.CONSTANT_STR_ONE);
            }
            
            
            createdate = spInstance.getCreatedate();
        }
        spITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid).getResult();
        String taskCount = "0";
        String materialCount = "0";
        // 电子材料
        int dzMaterial = 0;
        // 纸质材料
        int zzMaterial = 0;
        // 容缺材料
        int rqMaterial = 0;
        // 补正材料
        int bzMaterial = 0;
        if (spITasks != null) {
            taskCount = String.valueOf(spITasks.size());
        }
        List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialByBIGuid(biGuid).getResult();
        if (auditSpIMaterials != null) {
            materialCount = String.valueOf(auditSpIMaterials.size());
            for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                if ("15".equals(auditSpIMaterial.getStatus())) {
                    zzMaterial++;
                }
                else if ("20".equals(auditSpIMaterial.getStatus())) {
                    dzMaterial++;
                }
                else if ("25".equals(auditSpIMaterial.getStatus())) {
                    zzMaterial++;
                    dzMaterial++;
                }
                if ("1".equals(auditSpIMaterial.getAllowrongque())) {
                    rqMaterial++;
                }
                if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                    bzMaterial++;
                }
            }
        }
        materialdesc = "共" + taskCount + "个事项，" + materialCount + "份材料";
        materialsubmitdesc = "提交电子材料" + dzMaterial + "份 提交纸质材料" + zzMaterial + "份 容缺（暂无）" + rqMaterial + "份 需整改"
                + bzMaterial + "份";
    }

    /**
     * 预审通过
     */
    public void passyes() {
        List<AuditSpITask> listTask = auditSpITaskService.getTaskInstanceBySubappGuid(subappGuid).getResult();
        // 查找情形匹配的材料
        AuditSpSelectedOption auditspselectedoption = iauditspselectedoptionservice
                .getSelectedoptionsBySubappGuid(subappGuid).getResult();
        List<String> opguids = new ArrayList<>();
        List<AuditTaskOption> optionlist = new ArrayList<>();
        List<String> materialids = new ArrayList<>();
        if (auditspselectedoption != null && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())) {
            JSONObject jsonObject = JSONObject.parseObject(auditspselectedoption.getSelectedoptions());
            List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
            for (Map<String, Object> map : jsona) {
                List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                for (Map<String, Object> map2 : maplist) {
                    opguids.add(String.valueOf(map2.get("optionguid")));
                }
            }
        }
        if (!opguids.isEmpty()) {
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("rowguid", StringUtil.joinSql(opguids));
            optionlist = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
        }
        //遍历map将其中的材料添加进materialids中
        for (AuditTaskOption audittaskoption : optionlist) {
            String material = audittaskoption.getMaterialids();
            if (StringUtil.isBlank(material)) {
                continue;
            }
            String[] materials = material.split(";");
            for (String string : materials) {
                if (!materialids.contains(string)) {
                    materialids.add(string);
                }
            }
        }

        for (AuditSpITask task : listTask) {
            //先生成办件，再初始化材料
            AuditProject auditproject = new AuditProject();
            String rowGuid = UUID.randomUUID().toString();
            String projectGuid = rowGuid;
            // 1、窗口刚打开页面尚未保存,初始化数据
            auditproject.set("operateusername", userSession.getDisplayName());
            auditproject.set("operatedate", new Date());
            auditproject.set("rowguid", rowGuid);
            AuditTask auditTask = iaudittask.getAuditTaskByGuid(task.getTaskguid(), false).getResult();
            if (auditTask != null) {
                auditproject.set("task_id", auditTask.get("task_id"));
                auditproject.set("taskguid", auditTask.get("rowguid"));
                auditproject.set("status", ZwfwConstant.BANJIAN_STATUS_YJJ);// 办件状态：初始化
                auditproject.set("ouguid", auditTask.get("ouguid"));
                auditproject.set("projectname", auditTask.get("taskname"));
                auditproject.set("is_charge", auditTask.get("charge_flag"));
                //auditproject.set("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                auditproject.set("areacode", auditTask.get("areacode"));
                AuditTaskExtension auditTaskExtension = iaudittaskextension
                        .getTaskExtensionByTaskGuid(task.getTaskguid(), false).getResult();
                if (auditTaskExtension != null) {
                    auditproject.set("charge_when", auditTaskExtension.get("charge_when"));
                }
                auditproject.set("tasktype", auditTask.get("type"));
                auditproject.set("promise_day", auditTask.get("promise_day"));
            }
            auditproject.set("applyertype", Integer.parseInt(ZwfwConstant.APPLAYERTYPE_GR));// 申请人类型：个人
            auditproject.set("applydate", new Date());// 办件申请时间
            auditproject.set("applyway", ZwfwConstant.APPLY_WAY_NETSBYS);// 办件申请方式：窗口申请
            auditproject.set("certtype", ZwfwConstant.CERT_TYPE_SFZ);// 申请人证照类型：身份证
            auditproject.set("biguid", biGuid);
            auditproject.set("subappguid", subappGuid);
            auditproject.set("receivedate", new Date());
            auditproject.set("receiveuserguid", userSession.getUserGuid());
            auditproject.set("receiveusername", userSession.getDisplayName());
            auditproject.set("is_test", Integer.parseInt(ZwfwConstant.CONSTANT_STR_ZERO));
            auditproject.set("is_delay", 20);// 是否延期

            auditproject.set("applyername", dataBean.getName());
            SqlConditionUtil sUtil = new SqlConditionUtil();
            sUtil.eq("sourceguid", biGuid);
            sUtil.eq("taskguid", businessGuid);
            List<AuditOnlineProject> list = iauditonlineproject
                    .getAuditOnlineProjectPageData(sUtil.getMap(), 0, 0, null, null).getResult().getList();
            AuditOnlineProject auditOnlineProject = new AuditOnlineProject();
            if (list != null && !list.isEmpty()) {
                auditOnlineProject = list.get(0);
            }
            // TODO 出生 个人的改造
            auditproject.set("applyeruserguid", auditOnlineProject.get("applyerguid"));
            auditproject.set("certnum", dataBean.getIdcard());// 身份证号
            auditproject.set("address", dataBean.getResidence_area());
            auditproject.set("legal", "");
            auditproject.set("contactperson", dataBean.getName());
            auditproject.set("contactcertnum", dataBean.getIdcard());
            auditproject.set("contactphone", dataBean.getCon_phone());
            auditproject.set("contactpostcode", "");
            auditproject.set("contactemail", "");

            /*String numberFlag = ihandleconfig
                    .getFrameConfig(ZwfwConstant.AS_FLOWSN_PRE, ZwfwUserSession.getInstance().getCenterGuid())
                    .getResult();
            if (StringUtil.isBlank(numberFlag)) {
                numberFlag = ZwfwConstant.AS_FLOWSN_PRE_DEFAULT;
            }
            String flowsn = ihandleflowsn.getFlowsn("numberFlag", numberFlag).getResult();*/
            String flowsn= FlowsnUtil.createReceiveNum(auditTask.getStr("unid"), auditTask.getRowguid());

            auditproject.set("flowsn", flowsn);
            auditproject.set("businessguid", businessGuid);

            if (auditTask != null && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTask.get("is_enable").toString())) {

                iauditproject.addProject(auditproject);
            }
            if (StringUtil.isNotBlank(projectGuid)) {
                auditSpITaskService.updateProjectGuid(subappGuid, task.getTaskguid(), projectGuid);
                //再进行材料初始化
                if (auditspselectedoption == null) {
                    iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
                }
                else {
                    iHandleSPIMaterial.initProjctMaterialFilterM(businessGuid, subappGuid, task.getTaskguid(),
                            projectGuid, materialids);
                }
            }
        }
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);

        // 更新外网申办的状态
        Map<String, String> updateFieldMap = new HashMap<String, String>(16);
        updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTG));//外网申报预审通过
        SqlConditionUtil conditionsql = new SqlConditionUtil();
        conditionsql.eq("sourceguid", biGuid);
        iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16), conditionsql.getMap());
        addCallbackParam("msg", "预审通过！");
        //删除待办
        ihandleproject.delZwfwMessage("", spInstance.getAreacode(), "统一收件人员", subappGuid);

    }

    /**
     * 预审不通过
     */
    public void passno() {
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSTH, null);
        // 更新外网申办的状态
        Map<String, String> updateFieldMap = new HashMap<String, String>(16);
        // 预审不通过，状态改为10
        updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWWTJ));
        SqlConditionUtil conditionsql = new SqlConditionUtil();
        conditionsql.eq("sourceguid", biGuid);
        iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16), conditionsql.getMap());
        addCallbackParam("msg", "预审不通过！");
        //删除待办
        ihandleproject.delZwfwMessage("", spInstance.getAreacode(), "统一收件人员", subappGuid);
    }

    /**
     * 初始化材料列表
     *
     * @return
     */
    @SuppressWarnings("unused")
    public String modelAuditMaterial() {
        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
        AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
        String subappguid = auditSpISubapp.getRowguid();
        String phaseGuid = auditSpISubapp.getPhaseguid();
        String initMaterial = auditSpISubapp.getInitmaterial(); //是否初始化材料
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        // 如果材料尚未初始化，那就要先初始化一下材料
        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(initMaterial)) {
            listMaterial = iHandleSPIMaterial
                    .initIntegratedMaterial(subappguid, businessGuid, biGuid, phaseGuid, "", "").getResult();
        }
        else {
            listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappguid).getResult();
        }
        JSONObject jsonMaterials = new JSONObject();
        try {
            // 这里需要定义几个变量
            int submitedMaterialCount = 0;
            int shouldPaperCount = 0;
            int submitedPaperCount = 0;
            int shouldAttachCount = 0;
            int submitedAttachCount = 0;
            int rongqueCount = 0;
            int buRongqueCount = 0;
            jsonMaterials.put("total", listMaterial.size());

            for (int i = 0; i < listMaterial.size(); i++) {
                JSONObject jsonMaterial = new JSONObject();
                int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                String submittype = listMaterial.get(i).get("SUBMITTYPE");
                jsonMaterial.put("status", listMaterial.get(i).getStatus());
                jsonMaterial.put("materialname", listMaterial.get(i).get("MATERIALNAME"));
                jsonMaterial.put("rowguid", listMaterial.get(i).getRowguid());
                jsonMaterial.put("necessity", listMaterial.get(i).get("NECESSITY"));
                jsonMaterial.put("is_rongque", listMaterial.get(i).getAllowrongque());
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(listMaterial.get(i).getAllowrongque())) {
                    rongqueCount++;
                }
                jsonMaterial.put("submittype", submittype);
                jsonMaterial.put("cliengguid", listMaterial.get(i).get("CLIENGGUID"));
                materialList.add(jsonMaterial);
                // 这里对材料信息进行判断
                // 材料的提交情况
                if (materialstatus > 10) {
                    submitedMaterialCount++;
                    if (materialstatus == 20) {
                        submitedAttachCount++;
                    }
                    else if (materialstatus == 15) {
                        submitedPaperCount++;
                    }
                    else {
                        submitedAttachCount++;
                        submitedPaperCount++;
                    }
                }
                // 材料的应提交情况
                if (!"20".equals(submittype)) {
                    shouldAttachCount++;
                }
                if (!"10".equals(submittype)) {
                    shouldPaperCount++;
                }
            }
            jsonMaterials.put("isAllPaper", shouldPaperCount > submitedPaperCount ? 0 : 1);
            jsonMaterials.put("data", materialList);
            jsonMaterials.put("materialSummary", "已标记" + submitedMaterialCount + "件材料，其中纸质" + submitedPaperCount
                    + "件，电子文档" + submitedAttachCount + "件；容缺" + rongqueCount + "件；不容缺" + buRongqueCount + "件。");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonMaterials.toString();
    }

    /**
     * 操作纸质
     *
     * @param dataMaterials
     */
    public void paperOperate(Map<String, String> dataMaterials) {
        Map<String, String> params = getRequestParametersMap();
        String param = params.get("params");
        JSONArray jsonParam = JSONObject.parseArray(param);
        jsonParam.forEach(jsonPram -> {
            Map<String, Object> map = JsonUtil.jsonToMap(jsonPram.toString());
            String operateType = String.valueOf(map.get("operateType"));
            String materialInstanceGuid = String.valueOf(map.get("materialInstanceGuid"));
            if ("submit".equals(operateType)) {
                iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, 5);
            }
            else {
                iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, -5);
            }
        });

        // 处理返回数据
        Record rtnInfo = new Record();
        rtnInfo.put("customer", "success");
        sendRespose(JsonUtil.objectToJson(rtnInfo));
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 989314303534177844L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = new ArrayList<>();
                    if (spITasks != null && !spITasks.isEmpty()) {
                        for (AuditSpITask auditSpITask : spITasks) {
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,is_pause ";
                                AuditProject auditProject = iauditproject.getAuditProjectByRowGuid(fields,
                                        auditSpITask.getProjectguid(), auditSpITask.getAreacode()).getResult();
                                if (auditProject != null) {
                                    int status = ZwfwUtil.isNull(auditProject.getInt("status"));
                                    auditSpITask.put("status",
                                            codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));

                                    String sparetime = "--";
                                    if (status >= ZwfwConstant.BANJIAN_STATUS_YSL
                                            && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ && !ZwfwConstant.ITEMTYPE_JBJ
                                            .equals(auditProject.get("tasktype").toString())) {
                                        if (ZwfwConstant.CONSTANT_INT_ONE == ZwfwUtil
                                                .isNull(auditProject.getInt("is_pause"))) {
                                            sparetime = "暂停计时";// 办件处于暂停计时状态
                                        }
                                        else {
                                            AuditProjectSparetime auditprojectsparetime = iauditprojectsparetime
                                                    .getSparetimeByProjectGuid(auditSpITask.getProjectguid())
                                                    .getResult();
                                            if (auditprojectsparetime != null) {
                                                sparetime = CommonUtil
                                                        .getSpareTimes(auditprojectsparetime.get("spareminutes"));
                                            }
                                        }
                                    }
                                    auditSpITask.put("sparetime", sparetime);
                                    auditSpITask.put("businessname", spInstance.getItemname());
                                }
                                auditSpITasks.add(auditSpITask);
                            }
                        }
                    }
                    this.setRowCount(auditSpITasks.size());
                    return auditSpITasks;
                }
            };
        }
        return modelTask;
    }

    /**
     * 受理提交
     * @throws UnsupportedEncodingException 
     */
    public void accpetSubmit() throws UnsupportedEncodingException {
        iAuditYjsCjrService.update(dataBean);
        CjrApiUtil instance = CjrApiUtil.getInstance(userSession.getUserGuid());
        if (instance != null) {
        	String isAcceptSuccess = instance.acceptSubmit(dataBean);
            if (!"success".equals(isAcceptSuccess)) {
                addCallbackParam("msg", isAcceptSuccess);
            }
        }
    }

    public AuditYjsCjr getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditYjsCjr();
        }
        return dataBean;
    }

    public void setDataBean(AuditYjsCjr dataBean) {
        this.dataBean = dataBean;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getMaterialdesc() {
        return materialdesc;
    }

    public void setMaterialdesc(String materialdesc) {
        this.materialdesc = materialdesc;
    }

    public String getMaterialsubmitdesc() {
        return materialsubmitdesc;
    }

    public void setMaterialsubmitdesc(String materialsubmitdesc) {
        this.materialsubmitdesc = materialsubmitdesc;
    }
}
