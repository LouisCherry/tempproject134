package com.epoint.auditproject.auditproject.action;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@RestController("jnauditattachlistaction")
@Scope("request")
public class JNAuditAttachlistAction extends BaseController
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
    
    private String projectGuid;

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
    

    @Override
    public void pageLoad() {
    	projectGuid = getRequestParameter("projectGuid");
        addCallbackParam("submittype", ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC);
        try {
            // 这里需要判断一下当前的附件数，如果大于0个，需要控制显示删除按钮
            String cliengGuid = projectGuid;
            List<FrameAttachInfo> list = getAttachList(cliengGuid);
            int count = list.size();
            setAttachCount(count);
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

        }

        @SuppressWarnings("unused")
        public boolean beforeSaveAttachToDB(Object attach) {
            return true;
        }

        @Override
        public boolean beforeSaveAttachToDB(AttachStorage arg0) {
            // TODO Auto-generated method stub
            return false;
        }

    };

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {
            String cliengGuid = getRequestParameter("projectGuid");
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid, null,null, null,
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
                    String cliengGuid = getRequestParameter("projectGuid");
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
        addCallbackParam("msg", "成功删除！");
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

    public int getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(int attachCount) {
        this.attachCount = attachCount;
    }
}
