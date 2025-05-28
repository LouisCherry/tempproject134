package com.epoint.zzlb.auditspintegrated;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditresource.auditrssharemaerial.domain.AuditRsShareMetadata;
import com.epoint.basic.auditresource.auditrssharemaerial.inter.IAuditRsShareMetadata;
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
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.lsyc.common.util.LsZwfwBjsbConstant;
import com.epoint.yyyz.auditspiyyyzmaterial.api.IAuditSpIYyyzMaterialService;
import com.epoint.yyyz.auditspiyyyzmaterial.api.entity.AuditSpIYyyzMaterial;
import com.epoint.yyyz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseResult;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 套餐式开公司申报页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("zzlbhandlecompanyaction")
@Scope("request")
public class ZzlbHandleCompanyAction extends BaseController
{
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
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
     * 一业一证材料API
     */
    @Autowired
    private IAuditSpIYyyzMaterialService  auditSpIYyyzMaterialService;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;
    
    @Autowired
    private IAuditSpBasetaskR iauditspbasetaskr;
    
    @Autowired
    private  IBusinessLicenseBaseinfo iBusinessLicenseBaseinfo;
    
    @Autowired
    private IAuditTaskMaterial iaudittaskmaterial;
    
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
    
    @Autowired
    private IAuditSpISubapp auditSpISubappService;
    
    @Autowired
    private IAuditTaskCase iAuditTaskCase;
    
    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;
    
    @Autowired
    private IAuditRsShareMetadata iAuditRsShareMetadata;
    
    @Autowired
    private IBusinessLicenseExtension iBusinessLicenseExtension;
    
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
    
    @Autowired
    private ICodeItemsService codeItemService;
    
    @Autowired
    private IConfigService configServicce;
    
    @Autowired
    private IHandleConfig handleConfigService;
    //代办接口
    @Autowired
    private IMessagesCenterService messagesCenterService;
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private IConfigService configService;
    
    @Autowired
    private IBusinessLicenseResult businessLicenseResult;
    
    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;
    
    @Autowired
    private IAuditSpYyyzMaterialService auditSpYyyzMaterialService;
    
    
    
    private Record otherInfo = new Record();
    
    public Record getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Record otherInfo) {
        this.otherInfo = otherInfo;
    }
    
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
     * 行业类别下拉列表model
     */
    private List<SelectItem> industrycategoryModel = null;
    
    /**
     * 许可项目下拉列表model
     */
    private List<SelectItem> licenseditemsModel = null;
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
    
    
    /**
     * 业务标识
     */
    private String yewuGuid;
    
    
    //一业一证行业相关方法
    
    private DataGridModel<AuditSpITask> modelTask = null;
    
    @Override
    public void pageLoad() {
    	yewuGuid = UUID.randomUUID().toString();
        businessGuid = getRequestParameter("businessGuid");
        biGuid = getRequestParameter("biGuid");
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        if (auditSpInstance != null) {
            dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid())
                    .getResult();
            yewuGuid = dataBean.getRowguid();
            addCallbackParam("yewuGuid", dataBean.getRowguid());
        }
        else {
            dataBean = new AuditSpIntegratedCompany();
            dataBean.setRowguid(yewuGuid);
            addCallbackParam("yewuGuid", yewuGuid);
        }
        if (business != null) {
        	 addCallbackParam("formid", business.getStr("formid"));
        }
    }
    
    public String getControls(String subAppGuid, String biGuid, String taskid){
        if(StringUtil.isBlank(taskid)) {
            addCallbackParam("msg", false);
            return  "";
        }
        JSONObject result = new JSONObject();
        String [] taskids = null;
        String formids = "" ;
        if (taskid != null) {
            taskids = taskid.split(";");
            if (taskids != null && taskids.length > 0) {
               for (String taskiddetail : taskids) {
            	   Record record = iBusinessLicenseExtension.getTaianInfoByTaskId(taskiddetail);
            	   if (record != null) {
            		   formids +=  record.getStr("formid") + ",";
            	   }
               }
               if (formids.endsWith(",")) {
            	   formids = formids.substring(0, formids.length() - 1);
               }
            	  
            }
        }
        
        result.put("formids", formids);
        result.put("yewuGuid", yewuGuid);
        result.put("eformCommonPage",configServicce.getFrameConfigValue("eformCommonPage"));
        result.put("epointUrl",configServicce.getFrameConfigValue("epointsformurl"));
        return result.toString();
    }
    
    /**
     * 
     *  [需办理事项中，行业中主题配置的可申报事项] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public DataGridModel<AuditSpITask> getDataGridTask() {
        //spTasks = iAuditSpTask.getAllAuditSpTaskByBusinessGuid(businessGuid).getResult();
        List<AuditSpITask> auditSpITask= iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
                    if (auditSpITask != null && auditSpITask.size() > 0) {
                        for (AuditSpITask task : auditSpITask) {
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(task.getTaskguid(),false).getResult();
                            task.set("businessname", business.getBusinessname());
                            if(StringUtil.isNotBlank(auditTask)) {
                                task.set("task_id", auditTask.getTask_id());
                            }
                        }
                    }
                    this.setRowCount(auditSpITask.size());
                    return auditSpITask;
                }
            };
        }
        return modelTask;
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
        
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(dataBean);

        // 2、初始化实例信息
        // 2.1、生成主题实例信息
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        String biGuid = iAuditSpInstance.addBusinessInstance(businessGuid, dataBean.getRowguid(), "", dataBean.getCompanyname(), "",
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
            SqlConditionUtil sqlc = new SqlConditionUtil();
            for (AuditSpTask auditSpTask : auditSpTasks) {
                sqlc.clear();
                //套餐去掉标准事项的配置，判断下是否存在标准事项
                if(StringUtil.isBlank(auditSpTask.getBasetaskguid())){
                    AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpTask.getTaskid()).getResult();
                    iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.getRowguid(),
                            auditTask.getTaskname(), subappGuid, auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(),auditTask.getAreacode());
                }else{
                    sqlc.eq("basetaskguid", auditSpTask.getBasetaskguid());
                    sqlc.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
                    List<AuditSpBasetaskR> listr = iauditspbasetaskr.getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                    for (AuditSpBasetaskR auditSpBasetaskR : listr) {                    
                        AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                        iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.getRowguid(),
                                auditTask.getTaskname(), subappGuid, auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum(),auditTask.getAreacode());
                    }
                }
            }

            // 2.4、生成材料实例信息
           /* 
            iHandleSPIMaterial.initTcSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "");
            */
            List<AuditSpYyyzMaterial> auditSpYyyzMaterialList = auditSpYyyzMaterialService.findListBybusinessGuid(businessGuid);
            auditSpIYyyzMaterialService.initTcSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, auditSpYyyzMaterialList);
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
    public String modelAuditMaterial(String subappGuid, String biGuid, String phaseGuid,String selectids) {
        List<JSONObject> materialList = new ArrayList<JSONObject>();
        List<AuditSpIYyyzMaterial> listMaterial = new ArrayList<AuditSpIYyyzMaterial>();
        //获取一页一证材料数据
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        
   	 	BusinessLicenseExtension extension =  iBusinessLicenseBaseinfo.getBusinessBaseinfoByBiguidAndBuesiness(biGuid, auditSpInstance.getBusinessguid());
   	 	
   	 	String yyyzclengguid = "";
   	 	if (extension != null) {
   	 		yyyzclengguid = extension.getBaseinfoGuid();
   	 	}
   	 	
   	 	if (StringUtil.isBlank(businessGuid)) {
   	 		businessGuid = auditSpInstance.getBusinessguid();
   	 	}
   	 	
        listMaterial = auditSpIYyyzMaterialService.getSpIMaterialBySubappGuid(subappGuid);
        // 如果材料尚未初始化，那就要先初始化一下材料
        if (StringUtil.isBlank(listMaterial) || listMaterial.size() == 0) {
            List<AuditSpYyyzMaterial> auditSpYyyzMaterialList = auditSpYyyzMaterialService.findListBybusinessGuid(businessGuid);
            listMaterial = auditSpIYyyzMaterialService.initTcSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, auditSpYyyzMaterialList);
        }
        String[] taskIds = null;
        if (StringUtil.isNotBlank(selectids)) {
            taskIds = selectids.split(";");
            
        }
        //勾选通用事项时，其个性化材料绑定的专项事项的taskid
        List<String> taskids = new ArrayList<String>();
        
//        if (extension != null) {
        	 // 根据事项过滤材料
            if (taskIds != null && taskIds.length > 0 ) {
                for(String taskId : taskIds) {
                    taskids.add(taskId);
                    
                }
                List<String> materilids = new ArrayList<>();
                if(StringUtil.isNotBlank(taskids) && taskids.size()>0) {
                    String taskid = StringUtil.join(taskids, "','");
                    List<AuditSpIYyyzMaterial> listm = auditSpIYyyzMaterialService.getYyyzMaterialByTaskId(biGuid,taskid);
                    for (AuditSpIYyyzMaterial auditSpIYyyzMaterial : listm) {
                        materilids.add(auditSpIYyyzMaterial.getRowguid());
                    }
                }
                listMaterial = listMaterial.stream()
                        .filter(auditspimaterial -> materilids.contains(auditspimaterial.getRowguid()))
                        .collect(Collectors.toList());
            }
            else {
                //如果没勾选事项，则加载通用事项
                String taskid = "";
                listMaterial = auditSpIYyyzMaterialService.getYyyzMaterialByTaskId(biGuid,taskid);
            }
//        }
        
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
            int listMaterialCount = listMaterial != null ? listMaterial.size() : 0;
            jsonMaterials.put("total", listMaterialCount);
            //专项事项配置的材料
            if (listMaterial != null && listMaterial.size() > 0) {

                for (int i = 0; i < listMaterial.size(); i++) {
                    JSONObject jsonMaterial = new JSONObject();
                    
                    String yyyztype = listMaterial.get(i).getStr("yyyztype");
                    if (StringUtil.isNotBlank(yyyztype) && StringUtil.isNotBlank(yyyzclengguid)) {
                    	FrameAttachInfo attachinfo = iBusinessLicenseBaseinfo.getAttachByYyyzCliengguid(yyyzclengguid, yyyztype);
                    	if (attachinfo != null) {
                    		jsonMaterial.put("status", "20");
                    		jsonMaterial.put("cliengguid", attachinfo.getCliengGuid());
                    	}else {
                    		jsonMaterial.put("status", listMaterial.get(i).getStatus());
                    		jsonMaterial.put("cliengguid", listMaterial.get(i).getCliengguid());
                    	}
                    }else {
                    	jsonMaterial.put("status", listMaterial.get(i).getStatus());
                		jsonMaterial.put("cliengguid", listMaterial.get(i).getCliengguid());
                    }
                    
                    int materialstatus = Integer.parseInt(listMaterial.get(i).get("STATUS").toString());
                    String submittype = listMaterial.get(i).get("SUBMITTYPE");
                    
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
                    jsonMaterial.put("is_rongque", is_rongque);
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
            log.info("========Exception信息========" + e.getMessage());
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
                //iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, 5);
                auditSpIYyyzMaterialService.updateMaterialStatus(materialInstanceGuid, 5);
            }
            else{
                //iHandleSPIMaterial.updateMaterialStatus(materialInstanceGuid, -5);
                auditSpIYyyzMaterialService.updateMaterialStatus(materialInstanceGuid, -5);
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
    public void accept(String subappGuid, String biGuid, String businessGuid,String selectTaskInfos) {
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
        auditSpInstance.setApplyername(dataBean.getCompanyname());
        iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
        iAuditSpIntegratedCompany.updateAuditSpIntegratedCompany(dataBean);
        List<AuditSpITask> listTask = iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();
        List<String> taskInfosList = Arrays.asList(selectTaskInfos.split(";"));
        //事项标识的集合
        List<String> taskGuidList = new ArrayList<String>();
        Map<String,String> taskGuidAndTaskId = new HashMap<String,String>();
        if(StringUtil.isNotBlank(taskInfosList) && taskInfosList.size()>0) {
            for (String taskInfos : taskInfosList) {
                taskGuidAndTaskId.put(taskInfos.split("_spl_")[0], taskInfos.split("_spl_")[1]);
                taskGuidList.add(taskInfos.split("_spl_")[0]);
            }
        }
        listTask = listTask.stream()
                .filter(auditSpITask -> taskGuidList.contains(auditSpITask.getTaskguid()))
                .collect(Collectors.toList());
        for (AuditSpITask task : listTask) {
            //先生成办件，再初始化材料
            String projectGuid = iHandleProject
                    .InitIntegratedProject(task.getTaskguid(), "", userSession.getDisplayName(),
                            userSession.getUserGuid(), "", "", ZwfwUserSession.getInstance().getCenterGuid(), biGuid,
                            subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_CKDJ)
                    .getResult();
            iAuditSpITask.updateProjectGuid(task.getRowguid(), projectGuid);
            //再进行材料初始化
            //iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
        }
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
        String itemName = "";
        if(iBusinessLicenseExtension.getItemNameByBiGuid(biGuid) != null) {
            itemName = iBusinessLicenseExtension.getItemNameByBiGuid(biGuid).getStr("itemname");
        }
        String title = "【受理】"+itemName;
        String openUrl =WebUtil.getRequestRootUrl(request)+"/epointzwfw/auditbusiness/auditproject/auditprojectinfo?";
        List<Record> taskInfoList = iBusinessLicenseExtension.getTaskInfoByBiGuid(biGuid);
        if(StringUtil.isNotBlank(taskInfoList)) {
            for (Record taskInfo : taskInfoList) {
                String taskGuid = taskInfo.getStr("taskguid");
                String projectGuid = taskInfo.getStr("projectguid");
                String sendUrl = openUrl +"taskguid="+taskGuid+"&projectguid="+projectGuid+"&biguid="+biGuid;
                List<Record> windowInfoList = iBusinessLicenseExtension.getWindowGuidByTaskGuid(taskInfo.getStr("taskguid"));
                List<String> windowGuidList = new ArrayList<String>();
                if(StringUtil.isNotBlank(windowInfoList) && windowInfoList.size()>0) {
                    for (Record windowInfo : windowInfoList) {
                            windowGuidList.add(windowInfo.getStr("windowguid"));
                    }
                }
                List<Record> userGuidList = iBusinessLicenseExtension.getUserGuidByWindowGuidList(windowGuidList);
                if(StringUtil.isNotBlank(userGuidList) && userGuidList.size()>0) {
                    for (Record userGuid : userGuidList) {
                        String acceptUserGuid = userGuid.getStr("userguid");
                        messagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title, IMessagesCenterService.MESSAGETYPE_WAIT, acceptUserGuid, "", UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                                "", sendUrl, UserSession.getInstance().getOuGuid(),  UserSession.getInstance().getBaseOUGuid(), 1, null, "", projectGuid, null, new Date(), null, UserSession.getInstance().getUserGuid(), null, "");
                    }
                }
            }
        }
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
    @SuppressWarnings("unchecked")
    public List<SelectItem> getIndustrycategoryModel() {
        String licensedText = configService.getFrameConfigValue("industryName");
        if (industrycategoryModel == null) {
            industrycategoryModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", licensedText, null, false));
        }
        return this.industrycategoryModel;
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getLicensedModel() {
        String filter = this.getRequestParameter("filter");
        String licensedText = "";
        if (StringUtil.isBlank(filter)) {
            filter = "";
        }
        List<CodeItems> industryList = codeItemService.listCodeItemsByCodeName(configService.getFrameConfigValue("industryName"));
        if(StringUtil.isNotBlank(industryList)) {
            for (CodeItems codeItems : industryList) {
                if(filter.equals(codeItems.getItemValue())) {
                    licensedText = codeItems.getItemText();
                }
            }
        }
        if (licenseditemsModel == null) {
            licenseditemsModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", licensedText, null, false));

        }
        return licenseditemsModel;
    }
    /**
     * 
     * [生成控件] [功能详细描述]
     * 
     * @param auditRsShareMetadataList 控件元数据
     * @param isdetail
     * @param otherInfoReference       用于区分变更后的与已经走流程的办件（间接区分不同版本）
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Map<String, Object> generateMap(List<AuditRsShareMetadata> auditRsShareMetadataList, boolean isdetail,
            Record otherInfoReference,String baseinfo) {
        Set<String> keys = new HashSet<String>();
        if (otherInfoReference != null) {
            keys = otherInfoReference.keySet();
        }
        boolean isKeysEmpty = keys.isEmpty();
        if (auditRsShareMetadataList.size() == 0 || auditRsShareMetadataList == null) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> recordList = new ArrayList<Map<String, Object>>();
        for (AuditRsShareMetadata auditRsShareMetadata : auditRsShareMetadataList) {
            Map<String, Object> recordMap = new HashMap<String, Object>();
            if (isKeysEmpty || (!isKeysEmpty && (keys.contains(auditRsShareMetadata.getFieldname().toUpperCase()) ||  keys.contains(auditRsShareMetadata.getFieldname().toLowerCase())))) {
                // 详情页面处理
                if (isdetail) {
                    if ("webuploader".equals(auditRsShareMetadata.getFielddisplaytype())
                            || "webeditor".equals(auditRsShareMetadata.getFielddisplaytype())) {
                        recordMap.put("type", auditRsShareMetadata.getFielddisplaytype());
                    }
                    else {
                        recordMap.put("type", "outputtext");
                    }
                    String value=LsZwfwBjsbConstant.CONSTANT_STR_NULL;
                    if (StringUtil.isNotBlank(auditRsShareMetadata.getDatasource_codename())) {
//                        recordMap.put("dataOptions","{code : '" + auditRsShareMetadata.getDatasource_codename() + "'}");
                        
                     String message =  otherInfoReference.getStr(auditRsShareMetadata.getFieldname());
                     
                     if("combobox".equals(auditRsShareMetadata.getFielddisplaytype()) && !"xcsm".equals(auditRsShareMetadata.getFieldname())){                         
                         value=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), message);
                     }
                     
                     if ("xcsm".equals(auditRsShareMetadata.getFieldname())) {
                         value = message;
                     }
                     
                     if("radiobuttonlist".equals(auditRsShareMetadata.getFielddisplaytype())){                         
                         value=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), message);
                     }
                    /* if("checkboxlist".equals(auditRsShareMetadata.getFielddisplaytype())){
                         if(10==auditproject.getApplyway()){
                             if(message.contains("[")){                                 
                                 com.alibaba.fastjson.JSONArray jsonarr = JSONObject.parseArray(message);
                                 for (Object object : jsonarr) {
                                     value+=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), object.toString())+";"; 
                                 }
                             }else{
                                 String[] arr = message.split(",");
                                 for (String string : arr) {
                                     value+=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), string.toString())+";"; 
                                }  
                             }
                         }else{
                             String[] arr = message.split(",");
                             for (String string : arr) {
                                 value+=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), string.toString())+";"; 
                            }
                             
                         }
                        //value=codeItemService.getItemTextByCodeName(auditRsShareMetadata.getDatasource_codename(), message);
                     }*/
                        
                    } else if ("datepicker".equals(auditRsShareMetadata.getFielddisplaytype())&&StringUtil.isNotBlank(auditRsShareMetadata.getDateformat())) {// 日期控件
                        value=EpointDateUtil.convertDate2String(otherInfoReference.getDate(auditRsShareMetadata.getFieldname()), auditRsShareMetadata.getDateformat());
                    } else {
                        value=otherInfoReference.getStr(auditRsShareMetadata.getFieldname());
                    }
                        recordMap.put("bind", value);
                    
                }
                else {
                    recordMap.put("type", auditRsShareMetadata.getFielddisplaytype());
                    recordMap.put("required", "1".equals(auditRsShareMetadata.getNotnull()) ? true : false);
                    recordMap.put("bind", baseinfo+"." + auditRsShareMetadata.getFieldname());
                }
                // 数据类型在表单中的校验
                if (auditRsShareMetadata.getFieldtype() != null) {
                    // int型数据校验
                    if (auditRsShareMetadata.getFieldtype().contains("int")) {
                        recordMap.put("vType", "int");
                    }
                    // numeric型数据校验
                    else if (auditRsShareMetadata.getFieldtype().contains("numeric")) {
                        recordMap.put("vType", "float");
                    }
                }
                // 代码项值渲染
                if (StringUtil.isNotBlank(auditRsShareMetadata.getDatasource_codename())) {
                    recordMap.put("dataData", getCodeData(auditRsShareMetadata.getDatasource_codename()));
                }
                if (auditRsShareMetadata.getControlwidth() == 1) {
                    recordMap.put("width", 1);
                }
                if (auditRsShareMetadata.getControlwidth() == 2) {
                    recordMap.put("width", 2);
                }
                recordMap.put("fieldName", auditRsShareMetadata.getFieldname());
                recordMap.put("label", auditRsShareMetadata.getFieldchinesename());
                recordList.add(recordMap);
            }
        }
        map.put("data", recordList);
        return map;
    }
    
    /**
     * 根据代码项名，获取字符串形式的代码项
     * 
     * @param codeName
     * @return
     */
    public String getCodeData(String codeName) {
        StringBuffer rtnString = new StringBuffer("[");
        List<CodeItems> codeItemList = codeItemService.listCodeItemsByCodeName(codeName);
        if (ValidateUtil.isBlankCollection(codeItemList)) {
            return "";
        }
        else {
            for (CodeItems codeItems : codeItemList) {
                rtnString.append("{id:'" + codeItems.getItemValue() + "',text:'" + codeItems.getItemText() + "'},");
            }
            rtnString = new StringBuffer(rtnString.substring(0, rtnString.length() - 1));
            rtnString.append("]");
            return rtnString.toString();
        }
    }

    

}
