package com.epoint.auditsp.auditsphandle.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController("jnauditsharematerialattachlistaction")
@Scope("request")
public class JNAuditShareMaterialAttachlistAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 6196380024743989243L;

    /*
     * 操作类型
     */
    private String type;

    private int attachCount;
    
    private String submittype;
    
    private String spimaterial;
    
    /**
     * 表格控件model
     */
    private DataGridModel<FrameAttachInfo> model;

    @Autowired
    private IAttachService frameAttachInfoService;

    /**
     * 附件上传model
     */
    private FileUploadModel9 attachUploadModel;

    @Autowired
    private IHandleSPIMaterial handleSpiMaterialService;

    @Autowired
    private IAuditSpIMaterial iauditspimaterial;
    
    

    @Override
    public void pageLoad() {
        submittype = getRequestParameter("submittype");
        spimaterial = getRequestParameter("materialInstanceGuid");
        addCallbackParam("materialInstanceGuid", spimaterial);
        addCallbackParam("submittype", submittype);
        addCallbackParam("cliengGuid", getRequestParameter("cliengGuid"));
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            String cliengGuid = getRequestParameter("cliengGuid");
            List<FrameAttachInfo> list = getAttachList(cliengGuid);
            int count = list.size();
            attachCount = count;
            String rtnType = "add";
            if (count > 0){
                rtnType = "addanddel";
            }
            addCallbackParam("type", rtnType);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    AttachHandler9 handler = new AttachHandler9()
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void afterSaveAttachToDB(Object attach) {

            attachUploadModel.getExtraDatas().put("msg", "上传成功");
            String materialInstanceGuid = getRequestParameter("materialInstanceGuid");
            //如果原本为0，那么需要更新状态为电子提交或电子或纸质提交
            if (attachCount == 0) {
                handleSpiMaterialService.updateMaterialStatus(materialInstanceGuid, 10);
            }
            /*  String auditstatus = getRequestParameter("auditStatus");
            //未提交更新为审核通过
            if(auditstatus.equals(ZwfwConstant.Material_AuditStatus_WTJ)){
            	auditMaterialService.updateAuditStatus(materialInstanceGuid, ZwfwConstant.Material_AuditStatus_SHTG);
            }
            //待补正更新为待审核
            else if(auditstatus.equals(ZwfwConstant.Material_AuditStatus_DBZ)){
            	auditMaterialService.updateAuditStatus(materialInstanceGuid, ZwfwConstant.Material_AuditStatus_YBZ);
            }*/
        }

        @Override
        public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {
            return true;
        }

    };

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            String cliengGuid = getRequestParameter("cliengGuid");
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid, null, null, handler,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }

    public DataGridModel<FrameAttachInfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FrameAttachInfo>()
            {
                @Override
                public List<FrameAttachInfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String cliengGuid = getRequestParameter("cliengGuid");
                    List<FrameAttachInfo> list = getAttachList(cliengGuid);
                    int count = list.size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public void delete() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            frameAttachInfoService.deleteAttachByAttachGuid(sel);
        }
        String materialInstanceGuid = getRequestParameter("materialInstanceGuid");
        //如果删除的附件和当前所有的附件一致，那就说明已经没有电子件了，需要更新材料提交状态
        if (attachCount == select.size()) {
            handleSpiMaterialService.updateMaterialStatus(materialInstanceGuid, -10);
        }
        /*  String auditstatus = getRequestParameter("auditStatus");
        //待补正更新为待审核
        if(auditstatus.equals(ZwfwConstant.Material_AuditStatus_DBZ)){
        	auditMaterialService.updateAuditStatus(materialInstanceGuid, ZwfwConstant.Material_AuditStatus_YBZ);
        }*/
        addCallbackParam("msg", "成功删除！");
    }

    public List<FrameAttachInfo> getAttachList(String cliengGuid) {
        List<FrameAttachInfo> list = frameAttachInfoService.getAttachInfoListByGuid(cliengGuid);
        return list;
    }

    public void pass(){
        String msg = "";
        if(StringUtil.isNotBlank(spimaterial)){
           AuditSpIMaterial auditspimaterial =  iauditspimaterial.getSpIMaterialByrowguid(spimaterial).getResult();
           if(auditspimaterial !=null){
               auditspimaterial.setIsbuzheng(ZwfwConstant.CONSTANT_STR_THREE);
               iauditspimaterial.updateSpIMaterial(auditspimaterial);
               msg = "审核成功！";
           }else{
               msg = "未获取到材料！";
           }
        }else{
            msg = "未获取到材料标识！";
        }
        addCallbackParam("msg", msg);
    }
    
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
}
