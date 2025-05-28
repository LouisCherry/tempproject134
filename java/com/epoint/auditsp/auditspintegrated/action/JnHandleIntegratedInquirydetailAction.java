package com.epoint.auditsp.auditspintegrated.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.auditproject.api.IJnShYjsService;
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
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.audityjscsb.domain.AuditYjsCsb;
import com.epoint.basic.auditsp.audityjscsb.inter.IAuditYjsCsbService;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.BlspConstant;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.xmz.cxbus.api.ICxBusService;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnhandleintegratedinquirydetailaction")
@Scope("request")
public class JnHandleIntegratedInquirydetailAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1313301881620639339L;
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
    /**
     * 办件组合服务
     */
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;
    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private ICodeItemsService codeItemsService;
    @Autowired
    private IAuditOnlineProject iauditonlineproject;
    @Autowired
    private IAuditProject iauditproject;
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
    private IAuditYjsCsbService iaudityjscsbservice;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private ICxBusService iCxBusService;

    @Autowired
    private IJnShYjsService iJnShYjsService;

    /**
     * 项目基本信息表实体对象
     */
    private AuditSpIntegratedCompany dataBean = null;
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
     *  子申报实例标识
     */
    private String subappGuid;
    private String isaccept;

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
            addCallbackParam("subappguid", subappGuid); //
            isaccept = BlspConstant.LHSP_Status_DJJ.equals(auditSpISubapp.getStatus()) ? ZwfwConstant.CONSTANT_STR_ONE
                    : ZwfwConstant.CONSTANT_STR_ZERO;
            addCallbackParam("isaccept", isaccept);
            businessGuid = spInstance.getBusinessguid();
            AuditSpBusiness auditSpBusiness = iauditspbusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();

            if (auditSpBusiness != null) {
                addCallbackParam("newformid", auditSpBusiness.get("yjsformid"));
                if (StringUtil.isNotBlank(auditSpISubapp.getStr("optionfromid"))) {
                    addCallbackParam("formids", auditSpISubapp.get("optionfromid"));
                }
                addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
                addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
            }
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBusiness.getIsaccept())) {
                addCallbackParam("allowaccept", ZwfwConstant.CONSTANT_STR_ONE);
            }
            dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(spInstance.getYewuguid())
                    .getResult();
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
     * 
     * 预审通过
     *  
     */
    public void passyes() {
        AuditSpBusiness auditSpBusiness = iauditspbusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        String cdzYjs = configServicce.getFrameConfigValue("jnCdzYjsBusinessguid"); // 济宁充电桩一件事
        String liangshan_cdzYjs = configServicce.getFrameConfigValue("lsCdzYjsBusinessguid"); // 梁山充电桩一件事
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();

        // 如果是新能源汽车充电桩报装一件事,需要推送给第三方
        if (StringUtil.isNotBlank(businessGuid)
                && (businessGuid.equals(cdzYjs) || businessGuid.equals(liangshan_cdzYjs))) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("elecAddr", dataBean.getAddress()); // 详细地址名称,一件事“地址”
            jsonObject.put("consName", dataBean.getCompanyname()); // 传申请人名称
            jsonObject.put("consIDcard", dataBean.getZzjgdmz()); // 传申请人身份证号
            jsonObject.put("subappGuid", subappGuid);

            if (StringUtil.isNotBlank(dataBean.getZzjgdmz())) {
                jsonObject.put("countyCode", dataBean.getZzjgdmz().substring(0, 6));
            }
            else {
                jsonObject.put("countyCode", ""); // 县区编码
            }

            JSONArray jsonArray = new JSONArray();
            JSONObject contactObj = new JSONObject();
            contactObj.put("contactMode", "17");
            contactObj.put("contactName", dataBean.getContactperson());
            contactObj.put("custPhone", dataBean.getContactphone());
            contactObj.put("contactAddr", dataBean.getAddress());
            jsonArray.add(contactObj);
            jsonObject.put("custContactLists", jsonArray);// 客户联系信息结果集列表
            String sqydrl = ""; // 申请用电量
            String sfktfgfs = ""; // 是否开通峰谷分时
            String cdzazssxq = "370800"; // 充电桩安装所属县区，默认市
            if (businessGuid.equals(cdzYjs)) { // 济宁市充电桩一件事
                Record record = iCxBusService.getDzbdDetail("formtable20230321105345", subappGuid);
                if (record != null) {
                    sqydrl = record.getStr("sqydrl");
                    sfktfgfs = record.getStr("sfktfgfs");
                    cdzazssxq = record.getStr("cdzazssxq");
                }
            }

            if (businessGuid.equals(liangshan_cdzYjs)) { // 梁山
                Record record = iCxBusService.getDzbdDetail("formtable20230207102745", subappGuid);
                if (record != null) {
                    sqydrl = record.getStr("wenbk15");
                    sfktfgfs = record.getStr("danxkz1");
                    cdzazssxq = record.getStr("cdzazssxq");
                }
            }
            if (StringUtil.isNotBlank(sfktfgfs)) {
                if ("1".equals(sfktfgfs)) {
                    sfktfgfs = "是";
                }
                else {
                    sfktfgfs = "否";
                }
            }

            jsonObject.put("sqydrl", sqydrl);
            jsonObject.put("sfktfgfs", sfktfgfs);
            jsonObject.put("countyCode", cdzazssxq); // 县区编码

            JSONObject params = new JSONObject();
            params.put("params", jsonObject);
            // 推送第三方
            String zwdt_cdzyjs_resturl = configServicce.getFrameConfigValue("zwdt_cdzyjs_resturl");
            String result = HttpUtil.doPostJson(zwdt_cdzyjs_resturl, params.toString());
            if (StringUtil.isNotBlank(result)) {
                JSONObject resultJson = JSONObject.parseObject(result);
                JSONObject custom = resultJson.getJSONObject("custom");
                Integer code = custom.getInteger("code");
                if (code == ZwfwConstant.CONSTANT_INT_ONE) {
                    // 状态更新为办理中
                    //添加接件时间及人员
                    auditSpISubapp.set("acceptusername",userSession.getDisplayName());
                    auditSpISubapp.set("acceptdate",new Date());
                    auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YSJ);
                    iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                    //iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
                    // 更新外网申办的状态
                    Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                    updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTG));// 外网申报预审通过
                    SqlConditionUtil conditionsql = new SqlConditionUtil();
                    conditionsql.eq("sourceguid", biGuid);
                    iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16),
                            conditionsql.getMap());
                    // 删除待办
                    ihandleproject.delZwfwMessage("", spInstance.getAreacode(), "统一收件人员", subappGuid);
                    addCallbackParam("msg", "推送第三方预审成功！");
                }
                else {
                    addCallbackParam("msg", "推送第三方预审失败！");
                }
            }
            else {
                addCallbackParam("msg", "推送第三方预审失败！");
            }
        }
        else {

            // 开启接件模式的进入待接件，否则直接分发办件
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpBusiness.getIsaccept())) {
                auditSpISubapp.setStatus("31");
                //iAuditSpISubapp.updateSubapp(subappGuid, "31", null);
            }
            else {
                initproject();
                auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YSJ);
                //iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
            }
            //添加接件时间及人员
            auditSpISubapp.set("acceptusername",userSession.getDisplayName());
            auditSpISubapp.set("acceptdate",new Date());
            //auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YSJ);
            iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);

            // 更新外网申办的状态
            Map<String, String> updateFieldMap = new HashMap<String, String>(16);
            updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTG));// 外网申报预审通过
            SqlConditionUtil conditionsql = new SqlConditionUtil();
            conditionsql.eq("sourceguid", biGuid);
            iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16),
                    conditionsql.getMap());
            addCallbackParam("msg", "预审通过！");
            // 删除待办
            ihandleproject.delZwfwMessage("", spInstance.getAreacode(), "统一收件人员", subappGuid);
        }

        if ("公民身后一件事".equals(auditSpBusiness.getBusinessname())) {
            Record record = iCxBusService.getDzbdDetail("formtable20221112113428", subappGuid);
            if (record != null) {
                String jsname = record.getStr("jshqsxm");
                String jssex = record.getStr("xb");
                String jsphone = record.getStr("sjhm");
                String jsgx = record.getStr("yszgx");
                String jscard = record.getStr("sfzh");
                String jsaddress = record.getStr("lxdz");
                String szname = record.getStr("szxm");
                String szsex = record.getStr("xingb");
                String szage = record.getStr("nl");
                String szcard = record.getStr("wbk20");
                String szswsj = record.getStr("rqxz1");
                String szsbk = record.getStr("szsbk");
                String szkhh = record.getStr("jcrkhyx");
                String szhm = record.getStr("hum");
                String szyhkh = record.getStr("zh1");
                String szcblx = record.getStr("dxkz1");
                String szsbkzh = record.getStr("zhangh");

                Record rec = new Record();
                rec.setSql_TableName("shyjs");
                rec.set("rowguid", UUID.randomUUID().toString());
                rec.set("jsname", jsname);
                rec.set("jssex", jssex);
                rec.set("jsphone", jsphone);
                rec.set("jsgx", jsgx);
                rec.set("jscard", jscard);
                rec.set("jsaddress", jsaddress);
                rec.set("szname", szname);
                rec.set("szsex", szsex);
                rec.set("szage", szage);
                rec.set("szcard", szcard);
                rec.set("szswsj", szswsj);
                rec.set("szsbk", szsbk);
                rec.set("szkhh", szkhh);
                rec.set("szhm", szhm);
                rec.set("szyhkh", szyhkh);
                rec.set("szcblx", szcblx);
                rec.set("szsbkzh", szsbkzh);

                iJnShYjsService.inserRecord(rec);

            }
        }

    }

    public void accept() {

        initproject();

        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
        // 更新外网申办的状态
        Map<String, String> updateFieldMap = new HashMap<String, String>(16);
        updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYSTG));// 外网申报预审通过
        SqlConditionUtil conditionsql = new SqlConditionUtil();
        conditionsql.eq("sourceguid", biGuid);
        iauditonlineproject.updateOnlineProject(updateFieldMap, new HashMap<String, String>(16), conditionsql.getMap());
        addCallbackParam("msg", "处理完成！");

    }

    /**
     * 
     * 预审不通过
     *  
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
        ihandleproject.delZwfwMessage("", spInstance.getAreacode(), "统一收件人员", subappGuid);
    }

    /**
     * 
     *  初始化办件、材料
     */
    @SuppressWarnings("unchecked")
    public void initproject() {
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
        // 遍历map将其中的材料添加进materialids中
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
            // 先生成办件，再初始化材料
            String projectGuid = ihandleproject
                    .InitIntegratedProject(task.getTaskguid(), "", userSession.getDisplayName(),
                            userSession.getUserGuid(), "", "", ZwfwUserSession.getInstance().getCenterGuid(), biGuid,
                            subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_NETSBYS)
                    .getResult();
            if (StringUtil.isNotBlank(projectGuid)) {
                auditSpITaskService.updateProjectGuid(subappGuid, task.getTaskguid(), projectGuid);
                // 再进行材料初始化
                if (auditspselectedoption == null) {
                    iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
                }
                else {
                    iHandleSPIMaterial.initProjctMaterialFilterM(businessGuid, subappGuid, task.getTaskguid(),
                            projectGuid, materialids);
                }

            }
        }

        AuditSpBusiness auditSpBusiness = iauditspbusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();

        if (auditSpBusiness != null) {
            if ("我要开网约车".equals(auditSpBusiness.getBusinessname())
                    || "我要开出租车".equals(auditSpBusiness.getBusinessname())) {
                JSONObject submitString = new JSONObject();
                submitString.put("projectguid", subappGuid);
                String projectid = HttpUtil.doPostJson("http://172.16.53.22:8080/jnzwdt/rest/jnjtj/addWlyyczcjsycyzgxk",
                        submitString.toString());
                log.info("addWlyyczcjsycyzgxk返回结果：" + projectid);
            }
            else if ("我要做客车司机".equals(auditSpBusiness.getBusinessname())) {
                JSONObject submitString = new JSONObject();
                submitString.put("projectguid", subappGuid);
                String projectid = HttpUtil.doPostJson("http://172.16.53.22:8080/jnzwdt/rest/jnjtj/addKycyzgxk",
                        submitString.toString());
                log.info("addKycyzgxk返回结果：" + projectid);
            }

        }

    }

    /**
     * 
     *  初始化材料列表
     *  
     *  @param subappGuid
     *  @param biGuid
     *  @param phaseGuid
     *  @return   
     */
    @SuppressWarnings("unused")
    public String modelAuditMaterial() {
        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
        AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
        String subappguid = auditSpISubapp.getRowguid();
        String phaseGuid = auditSpISubapp.getPhaseguid();
        String initMaterial = auditSpISubapp.getInitmaterial(); // 是否初始化材料
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
            if(listMaterial!=null && listMaterial.size()>0) {
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
            }else{
                jsonMaterials.put("total", 0);
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
     * 
     *  操作纸质
     *  
     *  @param dataMaterials    
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

    public void searchCert() {
        String areacode = "";

        String handleUrl = "";
        String applyname = "";
        String certNum = "";
        String applyerType = "";

        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();

        // 关联共享材料
        AuditSpBusiness auditSpBusiness = iauditspbusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        if (auditSpBusiness != null) {
            handleUrl = auditSpBusiness.getHandleURL();
        }
        if (StringUtil.isNotBlank(handleUrl)) {
            if ("epointzwfw/auditsp/auditspbornintegrated".equals(handleUrl)) {
                AuditYjsCsb auditYjsCsb = iaudityjscsbservice.find(spInstance.getYewuguid());
                if (auditYjsCsb != null) {
                    applyname = auditYjsCsb.getFathername();
                    certNum = auditYjsCsb.getFatheridnumber();
                    applyerType = ZwfwConstant.CERTOWNERTYPE_ZRR;
                }
            }
            else {
                AuditSpIntegratedCompany auditSpIntegratedCompany = iAuditSpIntegratedCompany
                        .getAuditSpIntegratedCompanyByGuid(spInstance.getYewuguid()).getResult();
                if (auditSpIntegratedCompany != null) {
                    applyname = dataBean.getCompanyname();
                    certNum = dataBean.getZzjgdmz();
                    applyerType = ZwfwConstant.CERTOWNERTYPE_FR;
                }
            }
        }

        // 关联共享材料
        String flag = iHandleSPIMaterial.is_ExistSPCert(listMaterial, applyname, certNum, areacode, applyerType)
                .getResult();
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", applyerType);
        addCallbackParam("certnum", certNum);
        addCallbackParam("subappGuid", subappGuid);
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 2116301264299348212L;

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
                                            JSONObject params1 = new JSONObject();
                                            JSONObject param1 = new JSONObject();
                                            param1.put("ProjectGuid", auditSpITask.getProjectguid());
                                            params1.put("params", param1);
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

    public AuditSpIntegratedCompany getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpIntegratedCompany();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpIntegratedCompany dataBean) {
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
