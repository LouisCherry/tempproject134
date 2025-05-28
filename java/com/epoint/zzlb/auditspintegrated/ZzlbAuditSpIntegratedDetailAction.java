package com.epoint.zzlb.auditspintegrated;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditresource.auditcert.inter.IAuditCert;
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
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.yyyz.auditspiyyyzmaterial.api.IAuditSpIYyyzMaterialService;
import com.epoint.yyyz.auditspiyyyzmaterial.api.entity.AuditSpIYyyzMaterial;
import com.epoint.yyyz.auditspyyyzmaterial.api.IAuditSpYyyzMaterialService;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseResult;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseResult;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("zzlbauditspintegrateddetailaction")
@Scope("request")
public class ZzlbAuditSpIntegratedDetailAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1313301881620639333L;
    /**
     * 项目库API
     */
    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel;
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
    /**
     * 附件的guid
     */
    private String uploadguid = "";
    
    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;

    private List<AuditSpITask> spITasks = null;

    private DataGridModel<AuditSpITask> modelTask = null;

    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;

    @Autowired
    private IAuditSpInstance auditSpInstanceService;

    @Autowired
    private IAuditSpITask auditSpITaskService;
    
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private IAuditProject projectService;
    
    @Autowired
    private IAttachService attachService;
    
    
    @Autowired
    private IAuditTaskResult auditTaskResultService;
    
    @Autowired
    private IAuditSpYyyzMaterialService auditSpYyyzMaterialService;
    
    @Autowired
    private IAuditSpIYyyzMaterialService auditSpIYyyzMaterialService;
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IAuditProjectSparetime sparetimeService;
    
    @Autowired
    private IAuditProject iAuditProject;
    
    /**
     * 证照api
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;
    
    @Autowired
    private IBusinessLicenseResult businessLicenseResult;
    
    @Autowired
    private IBusinessLicenseExtension businessLicenseExtensionService;
    
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    
    @Autowired
    private IConfigService configServicce;


    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("guid");
        spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
            dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(spInstance.getYewuguid())
                    .getResult();
            AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(spInstance.getBusinessguid()).getResult();
            String yewuGuid = dataBean.getRowguid();
            if (business != null) {
            	addCallbackParam("formid", business.getStr("formid"));
            	 addCallbackParam("yewuGuid", yewuGuid);
            }
            addCallbackParam("eformCommonPage",configServicce.getFrameConfigValue("eformCommonPage"));
            addCallbackParam("epointUrl",configServicce.getFrameConfigValue("epointsformurl"));
            createdate = spInstance.getCreatedate();
            String certguid = spInstance.getStr("certrowguid");
            CertInfo cert = iCertInfoExternal.getCertInfoByRowguid(certguid);
            if (cert != null && cert.getIshistory() == 0) {
            	addCallbackParam("hascert", "1");
            }
        }
        spITasks = auditSpYyyzMaterialService.getSpITaskByBIGuid(biGuid);
        List<String> taskguids = new ArrayList<String>();
      //事项名称
        List<String> taskNames = new ArrayList<String>();
        if(StringUtil.isNotBlank(spITasks) && spITasks.size()>0) {
            for (AuditSpITask auditSpITask : spITasks) {
                taskguids.add(auditSpITask.getTaskguid());
                taskNames.add(auditSpITask.getTaskname());
            }
        }
        String taskguid = StringUtil.join(taskguids,"','");
        List<AuditProject> auditProjectList =  auditSpYyyzMaterialService.getProjectListByBiGuidAndTaskguids(biGuid,taskguid);
        List<String> taskids = new ArrayList<String>();
        if(StringUtil.isNotBlank(auditProjectList) && auditProjectList.size()>0) {
            for (AuditProject auditProject : auditProjectList) {
                taskids.add(auditProject.getTask_id());
            }
        }
        String taskid = StringUtil.join(taskids,"','");
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
        List<AuditSpIYyyzMaterial> listMaterial = auditSpIYyyzMaterialService.getYyyzMaterialByTaskId(biGuid,taskid);
        if (listMaterial != null && listMaterial.size()>0) {
            materialCount = String.valueOf(listMaterial.size());
            for (AuditSpIYyyzMaterial auditSpIYyyzMaterial : listMaterial) {
                if ("10".equals(auditSpIYyyzMaterial.getSubmittype())) {
                    zzMaterial++;
                }
                else if ("20".equals(auditSpIYyyzMaterial.getSubmittype())) {
                    dzMaterial++;
                }
                else if ("40".equals(auditSpIYyyzMaterial.getSubmittype())) {
                    zzMaterial++;
                    dzMaterial++;
                }
                if ("1".equals(auditSpIYyyzMaterial.getAllowrongque())) {
                    rqMaterial++;
                }
                if ("1".equals(auditSpIYyyzMaterial.getIsbuzheng())) {
                    bzMaterial++;
                }
            }
        }
        String taskName =StringUtil.join(taskNames,"、");
        materialdesc = "包括"+taskName+"在内的共" + taskCount + "个事项，" + materialCount + "份材料";
        materialsubmitdesc = "提交电子材料" + String.valueOf(dzMaterial) + "份 提交纸质材料" + String.valueOf(zzMaterial)
                + "份 容缺（暂无）" + String.valueOf(rqMaterial) + "份 需整改" + String.valueOf(bzMaterial) + "份";
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> auditSpITasks = new ArrayList<>();
                    if (spITasks != null && spITasks.size() > 0) {
                        for (AuditSpITask auditSpITask : spITasks) {
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,certrowguid,is_pause,acceptuserdate,is_cert";
                                AuditProject auditProject = projectService.getAuditProjectByRowGuid(fields,
                                        auditSpITask.getProjectguid(), ZwfwUserSession.getInstance().getAreaCode())
                                        .getResult();
                                
                                String sql = "select * from BusinessLicense_result where biguid=?1 and projectguid=?2 ";
                                BusinessLicenseResult result = businessLicenseResult.find(sql, auditSpITask.getBiguid(), auditSpITask.getProjectguid());
                                String cliengguid = "";
                                if(result == null) {
                                    result = new BusinessLicenseResult();
                                    cliengguid = auditSpITask.getProjectguid();
                                    result.setOperatedate(new Date());
                                    result.setOperateusername(userSession.getDisplayName());
                                    result.setRowguid(UUID.randomUUID().toString());
                                    result.setBiGuid(auditSpITask.getBiguid());
                                    result.setProjectguid(cliengguid);
                                    result.setClientguid(cliengguid);
//                                    businessLicenseResult.insert(result);
                                }else {
                                    cliengguid = result.getClientguid();
                                }
                                
                                if (auditProject != null) {
                                    int status = auditProject.getStatus();
                                    auditSpITask.put("status",
                                            codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)) == null ?"--" : codeItemsService.getItemTextByCodeName("办件状态", String.valueOf(status)));
                                    auditSpITask.put("accptdate",auditProject.getAcceptuserdate());
                                    auditSpITask.put("iscert", auditProject.getIs_cert());
                                    auditSpITask.put("uploadguid",cliengguid);
                                    //通过事项guid获取事项结果表的信息
                                    AuditTaskResult taskResult = auditTaskResultService.getAuditResultByTaskGuid(auditProject.getTaskguid(), true).getResult();
                                    //通过办件rowguid查看附件表里是否有附件
                                    Record frameAttachInfo = businessLicenseExtensionService.getFrameAttachInfoByCliengguid(cliengguid);
                                    String certname = "";
                                    String certinfoname = "";
                                    if(taskResult != null) {
                                       certinfoname = taskResult.getResultname();
                                    }
                                    auditSpITask.put("certinfoname",certinfoname);
                                    if(frameAttachInfo != null) {
                                        if(StringUtil.isNotBlank(frameAttachInfo)) {
                                            certname = frameAttachInfo.getStr("ATTACHFILENAME").substring(0, frameAttachInfo.getStr("ATTACHFILENAME").indexOf("."));
                                        }
                                    }
                                    auditSpITask.put("certname", certname);
                                    String sparetime = "--";
                                    if (status >= ZwfwConstant.BANJIAN_STATUS_YSL
                                            && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ && !ZwfwConstant.ITEMTYPE_JBJ
                                                    .equals(auditProject.getTasktype().toString())) {
                                        if(StringUtil.isNotBlank(auditProject.getIs_pause())) {
                                            if (ZwfwConstant.CONSTANT_INT_ONE == auditProject.getIs_pause()) {
                                                sparetime = "暂停计时";// 办件处于暂停计时状态
                                            }
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
    
    
    /**
     * 
     *  检测是否所有的办件是否已办结
     * 
     *  @param biGuid   
     */
    public void checkAllProjectStatus(String biGuid) {
        int count = 0;
        IAuditSpInstance auditSpInstanceService = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        Boolean isPutCert = false;
        if(StringUtil.isNotBlank(auditSpInstance.get("certrowguid"))) {
            isPutCert = true;
        }
        SqlConditionUtil conditionsql = new SqlConditionUtil();
        conditionsql.eq("biguid", biGuid);
        conditionsql.eq("areacode", ZwfwUserSession.getInstance().getAreaCode());
        List<AuditProject> auditProjects = iAuditProject.getAuditProjectListByCondition(conditionsql.getMap())
                .getResult();
        if (auditProjects != null && auditProjects.size() > 0) {
            for (AuditProject auditProject : auditProjects) {
                if (auditProject.getStatus() < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                    count++;
                }
            }
        }
        addCallbackParam("msg", count);
        addCallbackParam("isPutCert", isPutCert);
    }
    
    public void checkCertStauts(String guid) {
    	if(spInstance!=null) {
    		String certrowguid = spInstance.getStr("certrowguid");
    		addCallbackParam("certrowguid", certrowguid);
    	}
    }
    /**
     * 
     *  套餐办结
     *  
     *  @param biGuid  
     */
    public void cancelIntegrate(String biGuid) {
        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
        if (auditSpISubapps != null && auditSpISubapps.size() > 0) {
            for (AuditSpISubapp auditSpISubapp : auditSpISubapps) {
                iAuditSpISubapp.updateSubapp(auditSpISubapp.getRowguid(), ZwfwConstant.LHSP_Status_YBJ, new Date());
                // 同时更新外网onlineproject，正常办结
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);//要更新的时间类型字段
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                updateFieldMap.put("is_evaluat=", String.valueOf(ZwfwConstant.CONSTANT_STR_ZERO));
                updateDateFieldMap.put("banjiedate=",
                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                conditionsql.eq("sourceguid", auditSpISubapp.getBiguid());
                conditionsql.eq("taskguid", auditSpISubapp.getBusinessguid());
                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, conditionsql.getMap());  
            }
        }
    }
    
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9("",
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }
    
    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<FrameAttachInfo> list = attachService.getAttachInfoListByGuid("");
                    int count = 0;
                    if (list != null && list.size() > 0) {
                        count = list.size();
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
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
