package com.epoint.auditsp.auditspintegrated.action;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditsp.auditsptask.api.IJNAuditWindowTaskService;
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
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

/**
 * 套餐式开公司申报页面对应的后台
 * 个性化市县同体统一接件
 * @author Sonl，xbn
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("jnhandlecompanyaction")
@Scope("request")
public class JnHandleCompanyAction extends BaseController
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
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;
    /**
     * 办件组合服务
     */
    @Autowired
    private IHandleProject iHandleProject;
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
     * 套餐式开公司实体对象
     */
    private AuditSpIntegratedCompany dataBean = null;
    /**
     * 主题标识
     */
    private String businessGuid = "";
    /**
     * 主题实例标识
     */
    private String biGuid = "";
    @Autowired
	private IJNAuditWindowTaskService service;
    @Override
    public void pageLoad() {
        businessGuid = getRequestParameter("businessGuid");
        biGuid = getRequestParameter("biGuid");
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        if (auditSpInstance != null) {
            dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid())
                    .getResult();
        }
        else {
            dataBean = new AuditSpIntegratedCompany();
        }
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

        // 2、初始化实例信息
        // 2.1、生成主题实例信息
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        String biGuid = iAuditSpInstance.addBusinessInstance(businessGuid, yewuGuid, "", dataBean.getCompanyname(), "",
                auditSpBusiness.getBusinessname(), ZwfwUserSession.getInstance().getAreaCode(),
                auditSpBusiness.getBusinesstype()).getResult();

        String phaseGuid = "";
        List<AuditSpPhase> listPhase = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
        if (listPhase != null && listPhase.size() > 0) {
            phaseGuid = listPhase.get(0).getRowguid();
            // 2.2、生成子申报信息
            String subappGuid = iAuditSpISubapp.addSubapp(businessGuid, biGuid, phaseGuid, dataBean.getCompanyname(),
                    dataBean.getCompanyname(), "", ZwfwConstant.APPLY_WAY_CKDJ).getResult();

            // 2.3、生成事项实例信息
            List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
            for (AuditSpTask auditSpTask : auditSpTasks) {
                AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpTask.getTaskid()).getResult();
                iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.getRowguid(),
                        auditTask.getTaskname(), subappGuid, auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum());
            }

            // 2.4、生成材料实例信息
            iHandleSPIMaterial.initSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "");

            this.addCallbackParam("msg", "登记成功！");
            this.addCallbackParam("subappGuid", subappGuid);
            this.addCallbackParam("biGuid", biGuid);
            this.addCallbackParam("phaseGuid", phaseGuid);
            this.addCallbackParam("businessGuid", businessGuid);
        }else{
            this.addCallbackParam("msg", "请先配置主题阶段！");
        }

    }

    public void searchCert(){
        String subappGuid = getRequestParameter("subappGuid");
        String areacode ="";
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ)){
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        List<AuditSpIMaterial> listMaterial = new ArrayList<AuditSpIMaterial>();
        listMaterial = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappGuid).getResult();
        // 关联共享材料
        String flag=iHandleSPIMaterial.is_ExistSPCert(listMaterial,dataBean.getCompanyname(), dataBean.getZzjgdmz(),areacode).getResult();
        addCallbackParam("flag", flag);
        addCallbackParam("applyerType", ZwfwConstant.CERTOWNERTYPE_FR);
        addCallbackParam("certnum", dataBean.getZzjgdmz());
        addCallbackParam("subappGuid",subappGuid);
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
            listMaterial = iHandleSPIMaterial.initSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "")
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

            if (listMaterial != null && listMaterial.size() > 0) {

                for (int i = 0; i < listMaterial.size(); i++) {
                    JSONObject jsonMaterial = new JSONObject();
                    int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                    String submittype = listMaterial.get(i).get("SUBMITTYPE");
                    jsonMaterial.put("status", listMaterial.get(i).getStatus());
                    jsonMaterial.put("materialname", listMaterial.get(i).get("MATERIALNAME"));
                    jsonMaterial.put("rowguid", listMaterial.get(i).getRowguid());
                    jsonMaterial.put("necessity", listMaterial.get(i).get("NECESSITY"));

                    String isRongque = "";
                    if ("1".equals(listMaterial.get(i).get("NECESSITY").toString())
                            || "10".equals(listMaterial.get(i).get("NECESSITY").toString())) {
                        jsonMaterial.put("isnecessity", "必需");
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(listMaterial.get(i).get("ALLOWRONGQUE").toString())) {
                        	isRongque = "1";
                            rongqueCount++;
                        }
                        else {
                        	isRongque = "0";
                            buRongqueCount++;
                        }
                    }
                    else {
                        jsonMaterial.put("isnecessity", "非必需");
                    }

                    jsonMaterial.put("submittype", submittype);
                    jsonMaterial.put("cliengguid", listMaterial.get(i).get("CLIENGGUID"));
                    jsonMaterial.put("is_rongque", isRongque);
                    materialList.add(jsonMaterial);
                    // 这里对材料信息进行判断
                    // 材料的提交情况
                    if (materialstatus > 10) {
                        submitedMaterialCount++;
                        if (materialstatus == 20){
                            submitedAttachCount++;
                        }
                        else if (materialstatus == 15){
                            submitedPaperCount++;
                        }
                        else {
                            submitedAttachCount++;
                            submitedPaperCount++;
                        }
                    }
                    // 材料的应提交情况
                    if (!"20".equals(submittype)){
                        shouldAttachCount++;
                    }
                    if (!"10".equals(submittype)){
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
            if ("submit".equals(operateType)){
                iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, 5);
            }
            else{
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
        auditSpInstance.setApplyername(dataBean.getCompanyname());
        iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
        iAuditSpIntegratedCompany.updateAuditSpIntegratedCompany(dataBean);
        List<AuditSpITask> listTask = iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();
        for (AuditSpITask task : listTask) {
        	//获取并联审批不同市县的不同中心
        	String centerguid=service.getCenterguidBytaskguid(task.getTaskguid());
            //先生成办件，再初始化材料
            String projectGuid = iHandleProject
                    .InitIntegratedProject(task.getTaskguid(), "", userSession.getDisplayName(),
                            userSession.getUserGuid(), "", "", centerguid, biGuid,
                            subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_CKDJ)
                    .getResult();
            iAuditSpITask.updateProjectGuid(subappGuid, task.getTaskguid(), projectGuid);
            //再进行材料初始化
            iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
        }
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
        /*AuditHandleControlAction audit=new AuditHandleControlAction();
        audit.acceptProject();*/
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

}
