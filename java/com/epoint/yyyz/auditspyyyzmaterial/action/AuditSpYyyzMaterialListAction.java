package com.epoint.yyyz.auditspyyyzmaterial.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.yyyz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.yyyz.auditspyyyzmaterial.api.entity.AuditSpYyyzMaterial;

/**
 * 一业一证专项材料表list页面对应的后台
 * 
 *@author lyunang
 * @version [版本号, 2020-06-19 09:34:42]
 */
@RestController("auditspyyyzmateriallistaction")
@Scope("request")
public class AuditSpYyyzMaterialListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8142587666517725536L;



    /**
     * 主题专项材料表实体对象
     */
    private AuditSpYyyzMaterial dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpYyyzMaterial> model;
    /**
     * 是否必须model
     */
    private List<SelectItem> necessityModel = null;
    /**
     * 是否启用model
     */
    private List<SelectItem> statusModel = null;
    /**
     * 作用范围model
     */
    private List<SelectItem>  effictiverangeModel = null;
   
    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 主题guid
     */
    private String businessGuid;

    private String materialname;

    /**
     * 主题共享材料接口的实现类
     */
    @Autowired
    private IAuditSpYyyzMaterialService spYyyzMaterialService;
    
    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditSpShareMaterialRelation spShareMaterialRelationService;
    
    @Autowired
    private IAuditSpTask auditSpTaskService;
    
    
    @Override
    public void pageLoad() {
        businessGuid = getRequestParameter("guid");
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelectYyyzMaterial() {
        List<String> select = getDataGridData().getSelectKeys();
        
        for (String sel : select) {
            spYyyzMaterialService.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 保存修改
     */
    public void saveInfo() {
        List<AuditSpYyyzMaterial> materialList = getDataGridData().getWrappedData();
        
        
        for (AuditSpYyyzMaterial auditSpyyyzMaterial : materialList) {
            if (auditSpyyyzMaterial.getOrdernumber() == null) {// null会跑到最前
                auditSpyyyzMaterial.setOrdernumber(0);
            }
            auditSpyyyzMaterial.keep("rowguid","ordernumber");
            spYyyzMaterialService.update(auditSpyyyzMaterial);
        }
        String msg = "保存成功！";
        
        addCallbackParam("msg", msg);
    }

    public DataGridModel<AuditSpYyyzMaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpYyyzMaterial>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = -39022630956666810L;

                @Override
                public List<AuditSpYyyzMaterial> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    conditionSql +=" and businessguid = ?";
                    conditionList.add(businessGuid);
                    if(StringUtil.isNotBlank(materialname)) {
                        conditionSql +=" and materialname like ?";
                        conditionList.add(businessGuid+"%");
                    }
                    if (StringUtils.isNotBlank(dataBean.getNecessity())) {
                        conditionSql +=" and necessity = ?";
                        conditionList.add(dataBean.getNecessity());
                    }
                    if (StringUtils.isNotBlank(dataBean.getStatus())) {
                        conditionSql +=" and status = ?";
                        conditionList.add(dataBean.getStatus());
                    }
                    sortField = "ordernumber";
                    sortOrder = "desc";

                    List<AuditSpYyyzMaterial> list = spYyyzMaterialService.findList(
                            ListGenerator.generateSql("audit_sp_yyyz_material", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    List<AuditSpTask> taskList = auditSpTaskService.getAllAuditSpTaskByBusinessGuid(businessGuid).getResult();
                    Map<String,String> taskidToTaskName = new HashMap<String,String>();
                    if(taskList != null && taskList.size()>0) {
                        for(AuditSpTask auditSpTask : taskList) {
                            taskidToTaskName.put(auditSpTask.getTaskid(), auditSpTask.getTaskname());
                        }
                    }
                    for(AuditSpYyyzMaterial assm : list){
                        if("20".equals(assm.getNecessity())){
                            assm.setRongque("--");
                        }else if("1".equals(assm.getRongque())){
                            assm.setRongque("是");
                        }else{
                            assm.setRongque("否");
                        }
                        String taskName = taskidToTaskName.get(assm.getTask_id());
                        assm.setTask_id(taskName);
                    }
                    int count = spYyyzMaterialService.findCountByCondition(conditionSql,conditionList.toArray());
                    
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    /**
     * 保存修改
     */
    public void initTaskRelation() {
        String biguid = getRequestParameter("biguid");
        if(StringUtil.isNotBlank(biguid)){
            JSONObject rtnjson = new JSONObject();
            rtnjson.put("class", "go.GraphLinksModel");
            rtnjson.put("linkFromPortIdProperty", "fromPort");
            rtnjson.put("linkToPortIdProperty", "toPort");
            JSONArray nodeDataArray = new JSONArray();
            JSONArray linkDataArray = new JSONArray();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("businessguid", biguid);
            sql.eq("materialtype", "20");
            Map<String,List<Map<String,String>>> relationMap = new HashMap<>();
            List<String> taskidlist = new ArrayList<String>();
            //查询所有 【审批结果】 材料
            List<AuditSpShareMaterialRelation> materialRlist = spShareMaterialRelationService.getAuditSpShareMaterialByMap(sql.getMap()).getResult();
            if(materialRlist!=null&&!materialRlist.isEmpty()){
                for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : materialRlist) {
                    
//                    AuditTask auditTask = auditTaskService.selectUsableTaskByTaskID(auditSpShareMaterialRelation.getTaskid())
//                            .getResult();
                    //查询审批结果对应的共享材料
                    sql.clear();
                    sql.eq("sharematerialguid", auditSpShareMaterialRelation.getSharematerialguid());
                    sql.eq("businessguid", biguid);
                    sql.eq("materialtype", "10");
                    List<AuditSpShareMaterialRelation> marRelationlist = spShareMaterialRelationService.getAuditSpShareMaterialByMap(sql.getMap()).getResult();
                    if(marRelationlist!=null && !marRelationlist.isEmpty()){
                        AuditTask houtask = auditTaskService.selectUsableTaskByTaskID(auditSpShareMaterialRelation.getTaskid())
                                .getResult();
                        if(StringUtil.isNotBlank(relationMap.get(houtask.getTask_id()))){
                            boolean ishas = true;
                            for(int i =0;i<relationMap.get(houtask.getTask_id()).size();i++){
                                Map<String,String> indexmap = relationMap.get(houtask.getTask_id()).get(i);
                                if(indexmap.get("materialid").equals(auditSpShareMaterialRelation.getMaterialid())){
                                    ishas = false;
                                }
                            }
                            if(ishas){
                                List<Map<String,String>> materiallist = relationMap.get(houtask.getTask_id());
                                Map<String,String> materialMap = new HashMap<String,String>();
                                materialMap.put("materialid", auditSpShareMaterialRelation.getMaterialid());
                                materialMap.put("materialname",auditSpShareMaterialRelation.getMaterialname());
                                materialMap.put("materialtype",auditSpShareMaterialRelation.getMaterialtype());
                                materiallist.add(materialMap);
                                relationMap.put(houtask.getTask_id(), materiallist);

                            }
                        }else{
                            List<Map<String,String>> materiallist = new ArrayList<Map<String,String>>();
                            Map<String,String> materialMap = new HashMap<String,String>();
                            materialMap.put("materialid", auditSpShareMaterialRelation.getMaterialid());
                            materialMap.put("materialname",auditSpShareMaterialRelation.getMaterialname());
                            materialMap.put("materialtype",auditSpShareMaterialRelation.getMaterialtype());
                            materiallist.add(materialMap);
                            relationMap.put(houtask.getTask_id(), materiallist);
                        }
                        if(!taskidlist.contains(houtask.getTask_id())){
                            taskidlist.add(houtask.getTask_id());
                        }
                        //添加后置事项信息
                        for (AuditSpShareMaterialRelation auditSpShareMaterialR : marRelationlist) {
                            if(auditSpShareMaterialR.getTaskid().equals(houtask.getTask_id())){
                                continue;
                            }
                            AuditTask qiantask = auditTaskService.selectUsableTaskByTaskID(auditSpShareMaterialR.getTaskid())
                                    .getResult();
                            if(StringUtil.isNotBlank(relationMap.get(qiantask.getTask_id()))){
                                boolean ishas = true;
                                for(int i =0;i<relationMap.get(qiantask.getTask_id()).size();i++){
                                    Map<String,String> indexmap = relationMap.get(qiantask.getTask_id()).get(i);
                                    if(indexmap.get("materialid").equals(auditSpShareMaterialR.getMaterialid())){
                                        ishas = false;
                                        
                                    }
                                }
                                if(ishas){
                                    List<Map<String,String>> materiallist = relationMap.get(qiantask.getTask_id());
                                    Map<String,String> materialMap = new HashMap<String,String>();
                                    materialMap.put("materialid", auditSpShareMaterialR.getMaterialid());
                                    materialMap.put("materialname",auditSpShareMaterialR.getMaterialname());
                                    materialMap.put("materialtype",auditSpShareMaterialR.getMaterialtype());
                                    materiallist.add(materialMap);
                                    relationMap.put(qiantask.getTask_id(), materiallist);
                                }
                            }else{
                                List<Map<String,String>> materiallist = new ArrayList<Map<String,String>>();
                                Map<String,String> materialMap = new HashMap<String,String>();
                                materialMap.put("materialid", auditSpShareMaterialR.getMaterialid());
                                materialMap.put("materialname",auditSpShareMaterialR.getMaterialname());
                                materialMap.put("materialtype",auditSpShareMaterialR.getMaterialtype());
                                materiallist.add(materialMap);
                                relationMap.put(qiantask.getTask_id(), materiallist);
                            }
                            if(!taskidlist.contains(qiantask.getTask_id())){
                                taskidlist.add(qiantask.getTask_id());
                            }
                            
                            JSONObject linkData = new JSONObject();
                            linkData.put("from", qiantask.getTaskname());
                            linkData.put("fromPort", auditSpShareMaterialR.getMaterialname());
                            linkData.put("to", houtask.getTaskname());
                            linkData.put("toPort", auditSpShareMaterialRelation.getMaterialname());
                            linkDataArray.add(linkData);
                        }
                    }
                }
                //遍历完成，。添加信息到array 
                if(taskidlist!=null && !taskidlist.isEmpty()){
                    for (String task_id : taskidlist) {
                        AuditTask task = auditTaskService.selectUsableTaskByTaskID(task_id)
                                .getResult();
                        JSONObject nodedata = new JSONObject();
                        nodedata.put("key",task.getTaskname());
                        JSONArray field = new JSONArray();
                        if(relationMap.get(task_id)!=null&&!relationMap.get(task_id).isEmpty()){
                            for(int i = 0;i<relationMap.get(task_id).size();i++){
                                Map<String,String> indexmap = relationMap.get(task_id).get(i);
                                if("20".equals(indexmap.get("materialtype"))){
                                    JSONObject fielddata = new JSONObject();
                                    fielddata.put("name",relationMap.get(task_id).get(i).get("materialname"));
                                    fielddata.put("info", "");
                                    fielddata.put("color", "#F25022");
                                    fielddata.put("figure", "Rectangle");
                                    field.add(fielddata);
                                }else{
                                    JSONObject fielddata = new JSONObject();
                                    fielddata.put("name",relationMap.get(task_id).get(i).get("materialname"));
                                    fielddata.put("info", "");
                                    fielddata.put("color", "#FFB900");
                                    fielddata.put("figure", "Diamond");
                                    field.add(fielddata);
                                }
                            }
                        }
                        nodedata.put("fields", field);
                        nodedata.put("loc","300 100");
                        nodeDataArray.add(nodedata);
                    }
                }
                rtnjson.put("nodeDataArray", nodeDataArray);
                rtnjson.put("linkDataArray", linkDataArray);
                
                addCallbackParam("data",rtnjson);
            }else{
                addCallbackParam("msg", "网络异常！");
            }
        }else{
            addCallbackParam("msg", "网络异常！");
        }
        addCallbackParam("msg", "");
    }
    
    public AuditSpYyyzMaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpYyyzMaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpYyyzMaterial dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public String getBusinessGuid() {
        return businessGuid;
    }

    public void setBusinessGuid(String businessGuid) {
        this.businessGuid = businessGuid;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getNecessityModel() {
        if (necessityModel == null) {
            necessityModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否必需", null, false));
        }
        return necessityModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, false));
        }
        return statusModel;
    }
    @SuppressWarnings("unchecked")
    public List<SelectItem> getEffictiverangeModel() {
        if (effictiverangeModel == null) {
            effictiverangeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "主题共享材料作用范围", null, false));
        }
        return effictiverangeModel;
    }
}
