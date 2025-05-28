package com.epoint.auditsp.auditspintegrated.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

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
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("auditspintegrateddetailaction")
@Scope("request")
public class AuditSpIntegratedDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1313301881620639339L;
    /**
     * 项目库API
     */

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
     * biguid
     */
    private String biGuid = "";
    /**
     * 主题实例
     */
    private AuditSpInstance spInstance;

    private List<AuditSpITask> spITasks = null;

    private DataGridModel<AuditSpITask> modelTask = null;

    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;

    @Autowired
    private IAuditSpInstance auditSpInstanceService;
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private IAuditProject projectService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditProjectSparetime sparetimeService;
    
    @Autowired
    private IConfigService configServicce;
    
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    
    
    private String businessGuid = "";
    

    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("guid");
        businessGuid = getRequestParameter("businessguid");
        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
        List<AuditSpISubapp> subapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
        if (auditSpBusiness != null) {
    	  if (!subapps.isEmpty()) {
    		  AuditSpISubapp subapp = subapps.get(0);
              addCallbackParam("subappguid", subapp.getRowguid()); 
              if (StringUtil.isNotBlank(subapp.getStr("optionfromid"))) {
 	    		 addCallbackParam("formids", subapp.get("optionfromid"));
 	       	 }
          }
       	    addCallbackParam("newformid", auditSpBusiness.get("yjsformid")); //
            addCallbackParam("eformCommonPage",configServicce.getFrameConfigValue("eformCommonPage"));
            addCallbackParam("epointUrl",configServicce.getFrameConfigValue("epointsformurl"));
       }
        spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
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
        List<AuditSpIMaterial> auditSpIMaterials = auditSpIMaterialService.getSpIMaterialByBIGuid(biGuid).getResult();
        //去除结果材料
        auditSpIMaterials = auditSpIMaterials.stream().filter(a->ZwfwConstant.CONSTANT_STR_ZERO.equals(a.getResult())).collect(Collectors.toList());
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
        materialsubmitdesc = "提交电子材料" + dzMaterial + "份 提交纸质材料" + zzMaterial
                + "份 容缺（暂无）" + rqMaterial + "份 需整改" + bzMaterial + "份";
        
        
        
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                /**
                 * 
                 */
                private static final long serialVersionUID = 3001336374849369331L;

                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = new ArrayList<>();
                    if (spITasks != null && !spITasks.isEmpty()) {
                        for (AuditSpITask auditSpITask : spITasks) {
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,is_pause,acceptuserdate ";
                                AuditProject auditProject = projectService.getAuditProjectByRowGuid(fields,
                                        auditSpITask.getProjectguid(), auditSpITask.getAreacode())
                                        .getResult();
                                if (auditProject != null) {
                                    int status = auditProject.getStatus();
                                    auditSpITask.put("status",
                                            codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));
                                    auditSpITask.put("accptdate",auditProject.getAcceptuserdate());
                                    String sparetime = "--";
                                    if (status >= ZwfwConstant.BANJIAN_STATUS_YSL
                                            && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ && !ZwfwConstant.ITEMTYPE_JBJ
                                                    .equals(auditProject.getTasktype().toString())) {
                                        if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_pause()) {
                                            sparetime = "暂停计时";// 办件处于暂停计时状态
                                        }
                                        else {
                                            AuditProjectSparetime auditprojectsparetime = sparetimeService
                                                    .getSparetimeByProjectGuid(auditSpITask.getProjectguid())
                                                    .getResult();
                                            if (auditprojectsparetime != null) {
                                                sparetime = CommonUtil
                                                        .getSpareTimes(auditprojectsparetime.getSpareminutes());
                                                if(auditprojectsparetime.getSpareminutes()<0){
                                                    sparetime = "超时"+sparetime;
                                                }
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
                    auditSpITasks.sort((a,b)->{
                     
                       if(b.getDate("accptdate") == null){
                            return -1;
                       }else if(a.getDate("accptdate") == null){
                           return 1;
                       }
                       if(a.getDate("accptdate").getTime()-b.getDate("accptdate").getTime()>0){
                           return 1;
                       }else{
                           return -1;
                       }
                    });
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
