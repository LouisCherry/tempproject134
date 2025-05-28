package com.epoint.ces.auditnotifydocattachinfo.action;

import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.auditnotifydocattachinfo.api.entity.AuditNotifydocAttachinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.ces.auditnotifydocattachinfo.api.IAuditNotifydocAttachinfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * 办件文书信息表详情页面对应的后台
 *
 * @author jiem
 * @version [版本号, 2022-03-15 14:02:49]
 */
@RightRelation(AuditNotifydocAttachinfoListAction.class)
@RestController("auditnotifydocattachinfodetailaction")
@Scope("request")
public class AuditNotifydocAttachinfoDetailAction extends BaseController {
    @Autowired
    private IAuditNotifydocAttachinfoService service;

    /**
     * 办件文书信息表实体对象
     */
    private AuditNotifydocAttachinfo dataBean = null;

    private FileUploadModel9 fileUploadModel;

    private String cliengguid;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditNotifydocAttachinfo();
            cliengguid = UUID.randomUUID().toString();
            dataBean.setRowguid(cliengguid);
        }else {
            cliengguid = dataBean.getRowguid();
        }
    }


    public AuditNotifydocAttachinfo getDataBean() {
        return dataBean;
    }

    /**
     * [附件上传]
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengguid,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }
}