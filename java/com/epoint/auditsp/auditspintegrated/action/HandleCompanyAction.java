package com.epoint.auditsp.auditspintegrated.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
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
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.audityjscsb.domain.AuditYjsCsb;
import com.epoint.basic.auditsp.audityjscsb.inter.IAuditYjsCsbService;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 套餐式开公司申报页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("handlecompanyaction")
@Scope("request")
public class HandleCompanyAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1313301881620639339L;
    /**
     * 套餐式开公司API
     */
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;
    /**
     * API
     */
    @Autowired
    private IAuditYjsCsbService iAuditYjsCsbService;
    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
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
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;
    /**
     * 阶段事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;
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
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    @Autowired
    private IAuditTask iaudittask;

    @Autowired
    private IAuditTaskElementService iaudittaskelementservice;

    @Autowired
    private IHandleSPIMaterial ihandlespimaterial;

    @Autowired
    private IHandleProject ihandleproject;

    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;

    @Autowired
    private IAuditSpElementService iauditspelementservice;
    
    @Autowired
    private IAuditSpOptionService iauditspoptionservice;
    
    @Autowired
    private IAuditOrgaArea iauditorgaarea;

    @Autowired
    private IAuditSpSelectedOptionService iauditspselectedoptionservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;
    
    @Autowired
    private IConfigService configServicce;
    /**
     * 经费来源下拉列表model
     */
    private List<SelectItem> fundssourceModel = null;
    /**
     * 法人证照类型下拉列表model
     */
    private List<SelectItem> legalpersonidtypeModel = null;
    /**
     * 办税人证照类型下拉列表model
     */
    private List<SelectItem> taxationidtypeModel = null;
    /**
     * 财务负责人证照类型下拉列表model
     */
    private List<SelectItem> treasureridtypeModel = null;
    /**
     * 经办人证照类型下拉列表model
     */
    private List<SelectItem> operatoridtypeModel = null;
    /**
     * 法人职务下拉列表model
     */
    private List<SelectItem> legalpersondutyModel = null;
    /**
     * 参保险种下拉列表model
     */
    private List<SelectItem> insurancetypeModel = null;
    /**
     * 机构类型下拉列表model
     */
    private List<SelectItem> organizationtypeModel = null;
    /**
     * 设立方式下拉列表model
     */
    private List<SelectItem> foundtypeModel = null;
    /**
     * 企业类型下拉列表model
     */
    private List<SelectItem> companytypeModel = null;
    /**
     * 辖区列表model
     */
    private List<SelectItem> areacodeModel = null;
    /**
     * 套餐式开公司实体对象
     */
    private AuditSpIntegratedCompany dataBean = null;
    /**
     * 主题标识
     */
    private String businessGuid = "";
    /**
     * 主题标识
     */
    private String areacodeselsect = "";
    /**
     * 主题实例标识
     */
    private String biGuid = "";
    
    private String subappGuid = "";

    @Override
    public void pageLoad() {
        businessGuid = getRequestParameter("businessGuid");
        biGuid = getRequestParameter("biGuid");
        
        String codetemp = getRequestParameter("areacodeselsect");
       
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        if (auditSpInstance != null) {
            dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid())
                    .getResult();
        }
        else {
            dataBean = new AuditSpIntegratedCompany();
        }
        
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        
        if (auditSpBusiness != null) {
        	 addCallbackParam("newformid", auditSpBusiness.get("yjsformid")); 
             addCallbackParam("eformCommonPage",configServicce.getFrameConfigValue("eformCommonPage"));
             addCallbackParam("epointUrl",configServicce.getFrameConfigValue("epointsformurl"));
             JSONObject commonData = new JSONObject();
             if(dataBean!=null){
                 commonData.put("jshqsxm", dataBean.getCompanyname());
                 commonData.put("sjhm", dataBean.getContactphone());
                 commonData.put("sfzh", dataBean.getZzjgdmz());
             }else{
                 //对之前没有生成AuditSpIntegratedCompany 的数据进行处理
                 if(StringUtils.isNotBlank(biGuid) && auditSpInstance!=null){
                     AuditSpIntegratedCompany auditSpIntegratedCompany = new AuditSpIntegratedCompany();
                     auditSpIntegratedCompany.setRowguid(auditSpInstance.getYewuguid());
                     auditSpIntegratedCompany.setOperatedate(new Date());
                     auditSpIntegratedCompany.setOperateusername(userSession.getDisplayName());
                     iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(auditSpIntegratedCompany);
                 }
             }
             addCallbackParam("commonData",commonData.toString());
        }
        
        
        
        
        AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
        if (area != null) {
            addCallbackParam("citylevel", area.getCitylevel());
        }
        if(StringUtil.isNotBlank(codetemp)){
            areacodeselsect =  codetemp;
        }
        boolean ishaselement = iauditspelementservice.checkByBusinessguid(businessGuid).getResult();
        addCallbackParam("flag", ishaselement);
    }

    /**
     * 
     *  保存基本信息
     *    
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(dataBean);
        this.addCallbackParam("msg", "保存成功！");
    }

    /**
     * 
     *  登记基本信息
     *    
     */
    public void addComplete() {
        // 1、保存基本信息
        String yewuGuid = UUID.randomUUID().toString();
        dataBean.setRowguid(yewuGuid);
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(dataBean);

        //将所选的辖区传到前端
        if(StringUtil.isNotBlank(areacodeselsect)){
            addCallbackParam("areacodeselsect", areacodeselsect);
        }
        // 2、初始化实例信息
        // 2.1、生成主题实例信息
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        String biguid = iAuditSpInstance.addBusinessInstance(businessGuid, yewuGuid, "", dataBean.getCompanyname(), "",
                auditSpBusiness.getBusinessname(), ZwfwUserSession.getInstance().getAreaCode(),
                auditSpBusiness.getBusinesstype()).getResult();

        String phaseGuid = "";
        List<AuditSpPhase> listPhase = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
        if (listPhase != null && !listPhase.isEmpty()) {
            phaseGuid = listPhase.get(0).getRowguid();
            // 2.2、生成子申报信息
            String subappGuid = iAuditSpISubapp.addSubapp(businessGuid, biguid, phaseGuid, dataBean.getCompanyname(),
                    dataBean.getCompanyname(), "", ZwfwConstant.APPLY_WAY_CKDJ).getResult();
            boolean ishaselement = iauditspelementservice.checkByBusinessguid(businessGuid).getResult();
            if(!ishaselement){
                //查找是否存在必填事项
                SqlConditionUtil sql = new SqlConditionUtil();
                sql.eq("businessguid", businessGuid);
                sql.eq("elementguid", "root");
                List<AuditSpOption> optionlist = iauditspoptionservice.findListByCondition(sql.getMap()).getResult();
                if(optionlist.size()>0){
                    //查找每个事项是否存在问题
                    String taskids = optionlist.get(0).getTaskid();
                    if(StringUtil.isNotBlank(taskids)){                        
                        String[] taskidarr = taskids.split(";");
                        for (String string : taskidarr) {
                            if(StringUtil.isNotBlank(string)){
                                if(iaudittaskelementservice.checkBytaskid(string).getResult()){
                                    ishaselement = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (ishaselement) {
                this.addCallbackParam("flag", "2");
            }
            else {
                // 2.3、生成事项实例信息
                List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
                SqlConditionUtil sqlc = new SqlConditionUtil();
                for (AuditSpTask auditSpTask : auditSpTasks) {
                    sqlc.clear();
                    //套餐去掉标准事项的配置，判断下是否存在标准事项
                    if (StringUtil.isBlank(auditSpTask.getBasetaskguid())) {
                        AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(auditSpTask.getTaskid()).getResult();
                        iAuditSpITask.addTaskInstance(businessGuid, biguid, phaseGuid, auditTask.getRowguid(),
                                auditTask.getTaskname(), subappGuid,
                                auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(), auditTask.getAreacode());
                    }
                    else {
                        sqlc.eq("basetaskguid", auditSpTask.getBasetaskguid());
                        sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                        List<AuditSpBasetaskR> listr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap())
                                .getResult();
                        for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                            AuditTask auditTask = iaudittask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                    .getResult();
                            if(auditTask!=null){                                
                                iAuditSpITask.addTaskInstance(businessGuid, biguid, phaseGuid, auditTask.getRowguid(),
                                        auditTask.getTaskname(), subappGuid,
                                        auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(),
                                                auditTask.getAreacode());
                            }
                        }
                    }
                }
                // 2.4、生成材料实例信息
                iHandleSPIMaterial.initTcSubappMaterial(subappGuid, businessGuid, biguid, phaseGuid, "", "");
                this.addCallbackParam("flag", "1");
            }
            this.addCallbackParam("msg", "登记成功！");
            this.addCallbackParam("subappGuid", subappGuid);
            this.addCallbackParam("biGuid", biguid);
            this.addCallbackParam("phaseGuid", phaseGuid);
            this.addCallbackParam("businessGuid", businessGuid);
        }
        else {
            this.addCallbackParam("msg", "请先配置主题阶段！");
        }

    }

    public void searchCert() {
        String subappGuid = getRequestParameter("subappGuid");
        String biguid = getRequestParameter("biGuid");
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biguid).getResult();
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
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        if (auditSpBusiness != null) {
            handleUrl = auditSpBusiness.getHandleURL();
        }
        if (StringUtil.isNotBlank(handleUrl)) {
            if ("epointzwfw/auditsp/auditspbornintegrated".equals(handleUrl)) {
                AuditYjsCsb auditYjsCsb = iAuditYjsCsbService.find(auditSpInstance.getYewuguid());
                if (auditYjsCsb != null) {
                    applyname = auditYjsCsb.getFathername();
                    certNum = auditYjsCsb.getFatheridnumber();
                    applyerType = ZwfwConstant.CERTOWNERTYPE_ZRR;
                }
            }
            else {
                AuditSpIntegratedCompany auditSpIntegratedCompany = iAuditSpIntegratedCompany
                        .getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid()).getResult();
                if (auditSpIntegratedCompany != null) {
                    applyname = dataBean.getCompanyname();
                    certNum = dataBean.getZzjgdmz();
                    applyerType = ZwfwConstant.CERTOWNERTYPE_FR;
                }
            }
        }
        String flag = iHandleSPIMaterial.is_ExistSPCert(listMaterial, applyname, certNum, areacode,applyerType).getResult();
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", applyerType);
        addCallbackParam("certnum", certNum);
        addCallbackParam("subappGuid", subappGuid);
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
    public String modelAuditMaterial(String subappGuid, String biGuid, String phaseGuid) {
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
        String initMaterial = auditSpISubapp.getInitmaterial(); //是否初始化材料
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        // 如果材料尚未初始化，那就要先初始化一下材料
        if (!ZwfwConstant.CONSTANT_STR_ONE.equals(initMaterial)) {
            listMaterial = ihandlespimaterial.initNewIntegratedMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "")
                    .getResult();
        }
        else {
            listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();
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
            jsonMaterials.put("total", listMaterial != null ? listMaterial.size() : 0);

            if (listMaterial != null && !listMaterial.isEmpty()) {

                for (int i = 0; i < listMaterial.size(); i++) {
                    JSONObject jsonMaterial = new JSONObject();
                    int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                    String submittype = listMaterial.get(i).get("SUBMITTYPE");
                    jsonMaterial.put("status", listMaterial.get(i).getStatus());
                    jsonMaterial.put("materialname", listMaterial.get(i).get("MATERIALNAME"));
                    jsonMaterial.put("rowguid", listMaterial.get(i).getRowguid());
                    jsonMaterial.put("necessity", listMaterial.get(i).get("NECESSITY"));

                    String is_rongque = "";
                    if ("1".equals(listMaterial.get(i).get("NECESSITY").toString())
                            || "10".equals(listMaterial.get(i).get("NECESSITY").toString())) {
                        jsonMaterial.put("isnecessity", "必需");
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(listMaterial.get(i).get("ALLOWRONGQUE").toString())) {
                            is_rongque = "1";
                            rongqueCount++;
                        }
                        else {
                            is_rongque = "0";
                            buRongqueCount++;
                        }
                    }
                    else {
                        jsonMaterial.put("isnecessity", "非必需");
                    }

                    jsonMaterial.put("submittype", submittype);
                    jsonMaterial.put("cliengguid", listMaterial.get(i).get("CLIENGGUID"));
                    jsonMaterial.put("is_rongque", is_rongque);
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

    /**
     * 
     *  收件完成操作
     * 
     *  @param subappGuid
     *  @param biGuid  
     *  @param businessGuid
     */
    public void accept(String subappGuid, String biGuid, String businessGuid) {
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappGuid).getResult();
        auditSpInstance.setApplyername(dataBean.getCompanyname());
        iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
        iAuditSpIntegratedCompany.updateAuditSpIntegratedCompany(dataBean);
        List<AuditSpITask> listTask = iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();
        
        // 查找情形匹配的材料
        AuditSpSelectedOption auditspselectedoption = iauditspselectedoptionservice.getSelectedoptionsBySubappGuid(subappGuid).getResult();
        List<String> opguids = new ArrayList<>();
        List<AuditTaskOption> optionlist = new ArrayList<>();
        List<String> materialids = new ArrayList<>();
        if(auditspselectedoption != null && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())){
            JSONObject jsonObject = JSONObject.parseObject(auditspselectedoption.getSelectedoptions());
            List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObject.get("selectedoptions");
            for (Map<String, Object> map : jsona) {
                List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                for (Map<String, Object> map2 : maplist) {
                    opguids.add(String.valueOf(map2.get("optionguid")));
                }
            }
        }
        if(opguids.size()>0){
            SqlConditionUtil sqlc = new SqlConditionUtil();
            sqlc.in("rowguid", StringUtil.joinSql(opguids));
            optionlist = iaudittaskoptionservice.findListByCondition(sqlc.getMap()).getResult();
        }
        //遍历map将其中的材料添加进materialids中
        for (AuditTaskOption audittaskoption : optionlist) {
            String material = audittaskoption.getMaterialids();
            if(StringUtil.isBlank(material)){
                continue;
            }
            String[] materials = material.split(";");
            for (String string : materials) {
                if(!materialids.contains(string)){
                    materialids.add(string);
                }
            }
        }
        
        for (AuditSpITask task : listTask) {
            //先生成办件，再初始化材料
            String projectGuid = ihandleproject
                    .InitIntegratedProject(task.getTaskguid(), "", userSession.getDisplayName(),
                            userSession.getUserGuid(), "", "", ZwfwUserSession.getInstance().getCenterGuid(), biGuid,
                            subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_CKDJ)
                    .getResult();
            if (StringUtil.isNotBlank(projectGuid)) {
                iAuditSpITask.updateProjectGuid(subappGuid, task.getTaskguid(), projectGuid);
                //再进行材料初始化
                if(auditspselectedoption == null){                    
                    iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
                }else{
                    iHandleSPIMaterial.initProjctMaterialFilterM(businessGuid, subappGuid, task.getTaskguid(), projectGuid,materialids);
                }
            }
        }

        //添加接件时间及人员
        auditSpISubapp.set("acceptusername",userSession.getDisplayName());
        auditSpISubapp.set("acceptdate",new Date());
        auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_YSJ);
        iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
        //iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
        addCallbackParam("msg", "收件完成！");
    }

    public AuditSpIntegratedCompany getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpIntegratedCompany dataBean) {
        this.dataBean = dataBean;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFundssourceModel() {
        if (fundssourceModel == null) {
            fundssourceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "资金来源", null, false));
        }
        return this.fundssourceModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getLegalpersonidtypeModel() {
        if (legalpersonidtypeModel == null) {
            legalpersonidtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.legalpersonidtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTaxationidtypeModel() {
        if (taxationidtypeModel == null) {
            taxationidtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.taxationidtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTreasureridtypeModel() {
        if (treasureridtypeModel == null) {
            treasureridtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.treasureridtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getOperatoridtypeModel() {
        if (operatoridtypeModel == null) {
            operatoridtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.operatoridtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getLegalpersondutyModel() {
        if (legalpersondutyModel == null) {
            legalpersondutyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "法人职务", null, false));
        }
        return this.legalpersondutyModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getInsurancetypeModel() {
        if (insurancetypeModel == null) {
            insurancetypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.insurancetypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getOrganizationtypeModel() {
        if (organizationtypeModel == null) {
            organizationtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.organizationtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getFoundtypeModel() {
        if (foundtypeModel == null) {
            foundtypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否允许批量录入", null, false));
        }
        return this.foundtypeModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getCompanytypeModel() {
        if (companytypeModel == null) {
            companytypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "企业类型代码", null, false));
        }
        return this.companytypeModel;
    }
    
    public List<SelectItem> areacodeSelsectModel() {
        if (areacodeModel == null) {
            areacodeModel = new ArrayList<>();
            AuditOrgaArea area = iauditorgaarea.getAreaByAreacode(ZwfwUserSession.getInstance().getAreaCode()).getResult();
            if(area!=null){
                //如果是县级，查找市级主题
                if(ZwfwConstant.CONSTANT_STR_TWO.equals(area.getCitylevel())){
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.clear();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_TWO);
                    //查找市级辖区
                    List<AuditOrgaArea> sjarealist =iauditorgaarea.selectAuditAreaList(sqlc.getMap()).getResult();
                    if(ValidateUtil.isNotBlankCollection(sjarealist)){
                        for (AuditOrgaArea auditOrgaArea : sjarealist) {
                            areacodeModel.add(new SelectItem(auditOrgaArea.getXiaqucode(), auditOrgaArea.getXiaquname()));
                        }
                    }
                }else{
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.clear();
                    sqlc.eq("citylevel", ZwfwConstant.CONSTANT_STR_TWO);
                    //查找市级辖区
                    List<AuditOrgaArea> sjarealist =iauditorgaarea.selectAuditAreaList(sqlc.getMap()).getResult();
                    if(ValidateUtil.isNotBlankCollection(sjarealist)){
                        for (AuditOrgaArea auditOrgaArea : sjarealist) {
                            areacodeModel.add(new SelectItem(auditOrgaArea.getXiaqucode(), auditOrgaArea.getXiaquname()));
                        }
                    }
                    areacodeModel.add(new SelectItem(area.getXiaqucode(), area.getXiaquname()));
                }
           }
        }
        return this.areacodeModel;
    }

    public void saveForm() {
        String msg = "保存成功";
        iAuditSpIntegratedCompany.updateAuditSpIntegratedCompany(dataBean);
        addCallbackParam("msg", msg);
    }
    
    
    public String getAreacodeselsect() {
        return areacodeselsect;
    }

    public void setAreacodeselsect(String areacodeselsect) {
        this.areacodeselsect = areacodeselsect;
    }
}
