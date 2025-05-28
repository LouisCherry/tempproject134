package com.epoint.union.auditunionproject.action;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController("unionauditattachlistconfirmaction")
@Scope("request")
public class UnionAuditAttachlistConfirmAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 6196380024743989243L;

    /*
     * 操作类型
     */
    private String type;


    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;
    
    @Autowired
    private IAttachService frameAttachInfoService;
    
    @Autowired
    private IAuditProjectMaterial auditMaterialService;
    
    @Autowired
    private IAuditSpIMaterial spIShareMaterialService;
    
    @Autowired
    private IAuditProject iAuditProject; 
    
    @Autowired
    private IAuditTaskMaterial iaudittaskmaterial;
    
    private String materialinstanceguid;
    
    private String projectguid;
    
    private String clientid;
    
    
    @Override
    public void pageLoad() {
        materialinstanceguid = getRequestParameter("materialinstanceguid");
        projectguid = getRequestParameter("projectguid");
        AuditProject project = iAuditProject.getAuditProjectByRowGuid(projectguid, ZwfwUserSession.getInstance().getAreaCode()).getResult();
        AuditProjectMaterial auditProjectMaterial =  auditMaterialService.getProjectMaterialDetail(materialinstanceguid, projectguid).getResult();
        String materialGuid = auditProjectMaterial.getSharematerialiguid();
        if (StringUtil.isBlank(materialGuid)) {
            AuditTaskMaterial auditTaskMaterial = iaudittaskmaterial.getAuditTaskMaterialByRowguid(auditProjectMaterial.getTaskmaterialguid()).getResult();
            if(auditTaskMaterial!=null){
                 materialGuid = auditTaskMaterial.getMaterialid();
            }
        }
        
        AuditSpIMaterial auditSpiMaterial = spIShareMaterialService
                .getSpIMaterialByMaterialGuid(project.getSubappguid(), materialGuid).getResult();
        if(auditSpiMaterial!=null && StringUtil.isNotBlank(auditSpiMaterial.getCliengguid())){
            clientid = auditSpiMaterial.getCliengguid();            
        }
    }

   
    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<FrameAttachInfo> list = getAttachList(clientid);
                    int count = list.size();
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }

    public void doChange(){
        try {
        	//复制办件
            AuditProjectMaterial auditProjectMaterial =  auditMaterialService.getProjectMaterialDetail(materialinstanceguid, projectguid).getResult();
         	//删除附件
            List<FrameAttachInfo> list = frameAttachInfoService.getAttachInfoListByGuid(auditProjectMaterial.getCliengguid());
            for (FrameAttachInfo frameAttachInfo : list) {
            	frameAttachInfoService.deleteAttach(frameAttachInfo);				
			}
            frameAttachInfoService.copyAttachByClientGuid(clientid, null, null,
            		auditProjectMaterial.getCliengguid());
            Integer count = frameAttachInfoService.getAttachCountByClientGuid(auditProjectMaterial.getCliengguid());
            if(count>0){
            	//有附件，再判断之前是否有纸质，有的话设为电子和纸质
            	if(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == auditProjectMaterial.getStatus() || ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC == auditProjectMaterial.getStatus()){
            		auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC);
                }else{
                	auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
                }
            }else{
            	if(ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC == auditProjectMaterial.getStatus()){
            		auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT);
            	}else if(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC == auditProjectMaterial.getStatus()){
            		auditProjectMaterial.setStatus(ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER);
            	}
            }
            
            auditMaterialService.updateProjectMaterial(auditProjectMaterial);
        }
        catch (Exception e) {
            addCallbackParam("msg","修改失败!");
            return;
        }
        addCallbackParam("msg","替换成功!");
    }
    
    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        List<FrameAttachInfo> list = frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
        return list;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
