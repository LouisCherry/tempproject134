package com.epoint.xmz.taskmiddle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditproject.auditrongquematerial.api.IAuditProjectMaterialRongqueService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 承诺材料上传后台
 * @authory shibin
 * @version 2019年10月23日 下午5:39:33
 */
@RestController("ywztfileaction")
@Scope("request")
public class YwztFileAction extends BaseController
{
    private static final long serialVersionUID = 1L;



    @Autowired
    private IAuditProjectMaterialRongqueService rongqueService;
    @Autowired
    private IAttachService iAttachService;

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;



    private String clientguid = "";

    @Override
    public void pageLoad() {
        clientguid = getViewData("clientguid");
        if (StringUtil.isBlank(clientguid)) {
            clientguid = getRequestParameter("clientguid");
        }
        addViewData("clientguid", clientguid);
    }

    /**
     * 更新附件字段
     * @authory shibin
     * @version 2019年10月23日 下午6:36:36
     */
    public void add() {
        // 获取本次上传的附件 传递给子页面
        List<FrameAttachInfo> attachInfos = iAttachService.getAttachInfoListByGuid(clientguid);
        if (attachInfos != null && attachInfos.size() > 0) {
            FrameAttachInfo frameAttachInfo = attachInfos.get(0);
            addCallbackParam("filename", frameAttachInfo.getAttachFileName());
            addCallbackParam("filepath", frameAttachInfo.getAttachGuid());
            addCallbackParam("code", "1");
        }
        else {
            addCallbackParam("code", "0");
        }
        return;
    }

    /**
     * 附件上传
     * @authory shibin
     * @version 2019年10月23日 下午5:46:19
     * @return
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(clientguid,
                    null, null, attachHandler9, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    /**
     * 是否上传承诺书
     * @authory shibin
     * @version 2019年10月24日 下午3:01:44
     * @return
     */
    public String isUploadFile(String projectGuid) {
        String attachguid = null;
        if (projectGuid != null) {
            attachguid = rongqueService.getPromiseFile(projectGuid);
        }
        addCallbackParam("attachguid", attachguid);
        return attachguid;
    }


    public void setClientguid(String clientguid) {
        this.clientguid = clientguid;
    }

    public String getClientguid() {
        return clientguid;
    }

}
