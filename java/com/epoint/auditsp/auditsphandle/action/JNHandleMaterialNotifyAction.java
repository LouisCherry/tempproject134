package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditsphandle.api.IAuditSpITaskfwxm;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.zhenggai.api.ZhenggaiService;

/**
 * 材料告知页面对应的后台
 * 
 * @author Administrator
 *
 */
@RestController("jnhandlematerialnotifyaction")
@Scope("request")
public class JNHandleMaterialNotifyAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 8713148757403132395L;

    private AuditRsItemBaseinfo dataBean;

    /**
     * 建设单位
     */
    private String applyername;
    /**
     * 是否代码项
     */
    private List<SelectItem> bbdbModal;
    
    private List<SelectItem> userModal;
    /**
     * 申报名称
     */
    private String itemname;

    private String biGuid = "";

    private String subAppGuid = "";

    private DataGridModel<AuditSpITask> modelTask = null;

    private DataGridModel<AuditSpIMaterial> modelMaterial = null;

    @Autowired
    private IAuditSpInstance spInstanceService;

    @Autowired
    private IAuditRsItemBaseinfo rsItemBaseinfoService;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditSpIMaterial spIMaterialService;

    @Autowired
    private IAuditSpITask spITaskService;

    @Autowired
    private ICodeItemsService codeItemsService;

    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;
    @Autowired
    private IAuditSpISubapp handleSpiSubappSrtvice;
    
    @Autowired
    private IAuditSpITaskfwxm iTaskfwxm;
    @Autowired
    private ZhenggaiService zhenggaiImpl;
    private String initMaterial = ZwfwConstant.CONSTANT_STR_ZERO;
    private String phaseGuid = "";
    private String businessGuid = "";
    
    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("biguid");
        subAppGuid = getRequestParameter("subappguid");
        AuditSpInstance spInstance = spInstanceService.getDetailByBIGuid(biGuid).getResult();
        String ywGuid = spInstance.getYewuguid();
        initMaterial = handleSpiSubappSrtvice.getSubappByGuid(subAppGuid).getResult().getInitmaterial();
        businessGuid = spInstance.getBusinessguid();
        dataBean = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(ywGuid).getResult();
        //system.out.println("==>>>"+dataBean.getItemlegalcerttype());
        if(dataBean.getLegalproperty().equals("其他")){
           addCallbackParam("Legalproperty", "1");

        }
        if(dataBean.getItemlegalcerttype().equals("15")){
        	dataBean.setItemlegalcerttype("16");
        }
        String getPhaseguid = iTaskfwxm.findPhaseBysubappguid(subAppGuid);
        String phasename = iTaskfwxm.findPhase(getPhaseguid);
        itemname=dataBean.getItemname()+"("+phasename+")";
        
        addCallbackParam("bbdb", dataBean.get("bbdb"));
        addCallbackParam("kc", dataBean.get("kc"));
        addCallbackParam("ps", dataBean.get("ps"));
        addCallbackParam("gg", dataBean.get("gg"));
        
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                	
                	 List<AuditSpITask> spITasks =zhenggaiImpl.getauditspitask(subAppGuid);
                	
                    if (spITasks != null && spITasks.size() > 0) {
                        for (AuditSpITask auditSpITask : spITasks) {
                            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditSpITask.getTaskguid(), true)
                                    .getResult();
                            if (auditTask != null) {
                                auditSpITask.set("ouname", auditTask.getOuname());
                            }
                        } 

                    }
                    this.setRowCount(spITasks == null ? 0 : spITasks.size());
                    return spITasks;
                }
            };
        }
        return modelTask;
    }

    public DataGridModel<AuditSpIMaterial> getDataGridMaterial() {
        // 获得表格对象
        if (modelMaterial == null) {
            modelMaterial = new DataGridModel<AuditSpIMaterial>()
            {
                @Override
                public List<AuditSpIMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpIMaterial> spIMaterials = new ArrayList<AuditSpIMaterial>();
                    if (!ZwfwConstant.CONSTANT_STR_ONE.equals(initMaterial)) {
                        spIMaterials = spIMaterialService.getSpIMaterialBySubappGuid(subAppGuid+"1").getResult();
                        if(spIMaterials.size()==0){
                            spIMaterials = handleSPIMaterialService
                                    .initSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, "", "").getResult();
                        }
                    }
                    else {
                        spIMaterials = spIMaterialService.getSpIMaterialBySubappGuid(subAppGuid+"1").getResult();
                        if(spIMaterials.size()==0){
                            spIMaterials = spIMaterialService.getSpIMaterialBySubappGuid(subAppGuid).getResult();
                        }
                    }
                    if (spIMaterials != null && spIMaterials.size() > 0) {
                    	String taskname=null;
                        for (AuditSpIMaterial auditSpIMaterial : spIMaterials) {
                            Record record = iTaskfwxm.findTaskname(auditSpIMaterial.getMaterialguid(),auditSpIMaterial.getSubappguid());
                            if(record!=null){
                                if(record.get("taskname").equals(taskname)){
                                    auditSpIMaterial.put("taskname", "");	
                                }else{
                                    auditSpIMaterial.put("taskname", record.get("taskname"));
                                }
                                taskname=record.get("taskname");
                                auditSpIMaterial.put("file_source", record.get("file_source"));
                            }
                            if (auditSpIMaterial.getSubmittype() != null) {
                                auditSpIMaterial.put("submittype",
                                        codeItemsService.getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype())
                                                .replace("同时", "").replace("提交", ""));
                            }
                        }
                    }
                    this.setRowCount(spIMaterials == null ? 0 : spIMaterials.size());
                    return spIMaterials == null ? new ArrayList<>() : spIMaterials;
                }
            };
        }
        return modelMaterial;
    }

    
    @SuppressWarnings("unchecked")
	public List<SelectItem> getbbdbModal() {
        if (bbdbModal == null) {
        	bbdbModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.bbdbModal;
    }

    public List<SelectItem> getLogTypeList() {
        if (userModal == null) {
        	userModal = new ArrayList<SelectItem>();
        	 List<Record> list=zhenggaiImpl.getbbdb();
        	
            for (Record window : list) {
            	userModal.add(new SelectItem(window.getStr("userguid"), window.getStr("username")+"             联系电话:"+window.getStr("telephoneOffice")));
            }
        }
        return this.userModal;
    }

    public void updatexiangmu(){
    	rsItemBaseinfoService.updateAuditRsItemBaseinfo(dataBean);
    	addCallbackParam("msg", "保存成功");

    }
    
    public AuditRsItemBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditRsItemBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditRsItemBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
