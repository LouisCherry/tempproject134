package com.epoint.auditsp.auditsphandle.action;

import java.util.ArrayList;
import java.util.List;

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
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.zjcs.fwxminfo.bizlogic.FwxmInfoService;
import com.epoint.zjcs.fwxminfo.bizlogic.domain.FwxmInfo;


/**
 * 材料告知页面对应的后台
 * 
 * @author Administrator
 *
 */
@RestController("jnhandlematerialnotifyfwxmaction")
@Scope("request")
public class JNHandleMaterialNotifyfwxmAction extends BaseController
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
     * 申报名称
     */
    private String itemname;

    private String biGuid = "";

    private String subAppGuid = "";

    private DataGridModel<AuditSpITask> modelTask = null;


    private DataGridModel<AuditTaskMaterial> modelMaterial = null;

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
    private IAuditSpITaskfwxm auditSpITaskfwxmService;
    @Autowired
    private IHandleSPIMaterial handleSPIMaterialService;
    private String phaseGuid = "";
    private String businessGuid = "";
    private FwxmInfoService fwxminfoService=new FwxmInfoService();

    @Override
    public void pageLoad() {
        biGuid = getRequestParameter("biguid");
        subAppGuid = getRequestParameter("subappguid");
        AuditSpInstance spInstance=spInstanceService.getDetailByBIGuid(biGuid).getResult();
        String ywGuid = spInstance.getYewuguid();
        businessGuid=spInstance.getBusinessguid();
        dataBean = rsItemBaseinfoService.getAuditRsItemBaseinfoByRowguid(ywGuid).getResult();
    }

    public DataGridModel<AuditSpITask> getDataGridTask() {
        // 获得表格对象
        if (modelTask == null) {
            modelTask = new DataGridModel<AuditSpITask>()
            {
                @Override
                public List<AuditSpITask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<AuditSpITask> spITasks = auditSpITaskfwxmService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
                    if (spITasks != null && spITasks.size() > 0) {
                        for (AuditSpITask auditSpITask : spITasks) {
                        	String fwxmguid=auditSpITask.get("fwxmguid");
                            AuditTask auditTask = auditTaskService.getAuditTaskByGuid(auditSpITask.getTaskguid(), true)
                                    .getResult();
                            FwxmInfo infolist=fwxminfoService.find(FwxmInfo.class,fwxmguid);
                            if (auditTask != null) {
                                auditSpITask.set("ouname", auditTask.getOuname());
                                auditSpITask.set("fwxmmc", infolist.getFwsxmc());
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

    public DataGridModel<AuditTaskMaterial> getDataGridMaterial() {
        // 获得表格对象
        if (modelMaterial == null) {
            modelMaterial = new DataGridModel<AuditTaskMaterial>()
            {
                @Override
                public List<AuditTaskMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                   
                    List<AuditTaskMaterial> materiallist1 = new ArrayList<AuditTaskMaterial>();

                	List<AuditTaskMaterial> materiallist = new ArrayList<AuditTaskMaterial>();
                    	List<AuditSpITask> auditSpITasklist=auditSpITaskfwxmService.getTaskInstanceBySubappGuid(subAppGuid).getResult();
                       for(AuditSpITask list:auditSpITasklist){
                    	   String taskguid=list.getTaskguid();
                    	  materiallist=auditSpITaskfwxmService.getTaskInstanceBytaskguid(taskguid).getResult();
                
                    if (materiallist != null && materiallist.size() > 0) {
                        for (AuditTaskMaterial auditSpIMaterial : materiallist) {
                            auditSpIMaterial.put("necessity",
                                    ("10").equals(auditSpIMaterial.getNecessity()) ? "必需" : "非必需");
                            if (auditSpIMaterial.getSubmittype() != null) {
                                auditSpIMaterial.put("submittype",
                                        codeItemsService.getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype())
                                                .replace("同时", "").replace("提交", ""));
                            }
                        }
                    }
                    materiallist1.addAll(materiallist);
                  }
                    this.setRowCount(materiallist1 == null ? 0 : materiallist1.size());
                    return materiallist1 == null ? new ArrayList<>() : materiallist1;
                }
            };
        }
        return modelMaterial;
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
