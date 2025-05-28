package com.epoint.yyyz.auditspintegrated;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.epoint.basic.controller.BaseController;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 项目基本信息表新增页面对应的后台
 * 
 * @author Sonl
 * @version [版本号, 2017-04-05 11:23:36]
 */
@RestController("yzauditspintegrateddetailaction")
@Scope("request")
public class YzAuditSpIntegratedDetailAction extends BaseController
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
     * 主题标识
     */
    private String businessGuid = "";
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
    private IAuditSpITask auditSpITaskService;
    
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    @Autowired
    private IAuditSpIMaterial auditSpIMaterialService;

    @Autowired
    private IAuditProject projectService;
    @Autowired
   	private IBusinessLicenseBaseinfo iBusinessLicenseBaseinfo;
    
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
    @Autowired
   	private IAuditSpBusiness iAuditSpBusiness;
    @Autowired
   	private IConfigService configServicce;
    @Autowired
    private ICodeItemsService codeItemService;
       
    /**
     * 证照api
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;


    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("guid");
        spInstance = auditSpInstanceService.getDetailByBIGuid(biGuid).getResult();
        if (spInstance != null) {
            dataBean = iAuditSpIntegratedCompany.getAuditSpIntegratedCompanyByGuid(spInstance.getYewuguid())
                    .getResult();
        	businessGuid = spInstance.getBusinessguid();
            createdate = spInstance.getCreatedate();
            AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(spInstance.getBusinessguid())
					.getResult();
            String yewuGuid = dataBean.getRowguid();
			if (business != null) {
				addCallbackParam("formid", business.getStr("formid"));
				addCallbackParam("yewuGuid", yewuGuid);
				BusinessLicenseExtension extension = iBusinessLicenseBaseinfo
						.getBusinessBaseinfoByBiguidAndBuesiness(biGuid, businessGuid);
				JSONObject commondata = new JSONObject();
				JSONArray subdatalist = new JSONArray();
				if (extension != null) {
					String formsinfo = extension.getFormsInfo();
					if (StringUtil.isNotBlank(formsinfo)) {
						JSONObject formsinfojson = JSON.parseObject(formsinfo);
						jsonLoop(formsinfojson, commondata, subdatalist);
					}
				}
				addCallbackParam("commondata", commondata.toString());
				addCallbackParam("subdatalist", subdatalist.toString());
			}
			addCallbackParam("eformCommonPage", configServicce.getFrameConfigValue("eformCommonPage"));
			addCallbackParam("epointUrl", configServicce.getFrameConfigValue("epointsformurl"));
            
            
            String certguid = spInstance.getStr("certrowguid");
            CertInfo cert = iCertInfoExternal.getCertInfoByRowguid(certguid);
            if (cert != null && cert.getIshistory() == 0) {
            	addCallbackParam("hascert", "1");
            }
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
                                String fields = " rowguid,taskguid,projectname,pviguid,status,tasktype,is_pause,acceptuserdate ";
                                AuditProject auditProject = projectService.getAuditProjectByRowGuid(fields,
                                        auditSpITask.getProjectguid(), ZwfwUserSession.getInstance().getAreaCode())
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
    
    public void jsonLoop(Object object, JSONObject oooo, JSONArray bbbb) {

		if (object instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) object;
			for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
				String key = entry.getKey();
				if (key.contains("_LIST") || "DC_YYYZ_QSXK_XSGC".equals(key) || "DC_YYYZ_FSYZL_TWS_MF".equals(key) || "DC_YYYZ_XQJY_JXSS".equals(key)
						|| "DC_YYYZ_XQJY_JZG".equals(key) || "DC_RES_SAFE_HR".equals(key) || "DC_YYYZ_YSGC".equals(key) || "DC_SUB".equals(key)
						|| "DC_YYYZ_QSXK_SJGC".equals(key) || "DC_YYYZ_YSJYXK_SB".equals(key) || "DC_YYYZ_FSYZL_TWS".equals(key) || "DC_YYYZ_QSXK_TSGC".equals(key) || "DC_YYYZ_YLJGXK_YQSB".equals(key)) {
					String formid = codeItemService.getItemValueByCodeID("1016099", key); //系统1016199
					JSONArray array = jsonObject.getJSONArray(key);
					if(StringUtil.isNotBlank(formid)) {
					    String [] formids = formid.split(",");
					    for (String id : formids) {
					        JSONArray parentdata = new JSONArray();
					        JSONObject parent = new JSONObject();
					        for (int i = 0; i < array.size(); i++) {
					            JSONObject sondata = array.getJSONObject(i);
					            JSONObject newdata = new JSONObject();
					            for (Map.Entry<String, Object> entry2 : sondata.entrySet()) {
					                newdata.put(entry2.getKey().replace("_", ""), entry2.getValue().toString());
					            }
					            parentdata.add(newdata);
					        }
					        parent.put("subtableid", id);
					        parent.put("datalist", parentdata);
					        bbbb.add(parent);
                        }
					}
				} else {
					JSONObject json = jsonObject.getJSONObject(key);
					for (Map.Entry<String, Object> entry1 : json.entrySet()) {
						oooo.put(entry1.getKey().replace("_", ""), entry1.getValue().toString());
					}
				}
			}
		}
	}
}
