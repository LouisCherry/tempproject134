package com.epoint.auditsppolicybasicinfo.action;

import com.epoint.auditsppolicybasicinfo.api.IAuditSpPolicyBasicinfoService;
import com.epoint.auditsppolicybasicinfo.api.entity.AuditSpPolicyBasicinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 政策信息表详情页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:26:17]
 */
@RightRelation(AuditSpPolicyBasicinfoListAction.class)
@RestController("auditsppolicybasicinfodetailaction")
@Scope("request")
public class AuditSpPolicyBasicinfoDetailAction extends BaseController
{
    @Autowired
    private IAuditSpPolicyBasicinfoService service;

    /**
     * 政策信息表实体对象
     */
    private AuditSpPolicyBasicinfo dataBean = null;

    private FileUploadModel9 fileUploadModel;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPolicyBasicinfo();
        }
        if (StringUtil.isBlank(dataBean.getClientguid())) {
            dataBean.setClientguid(UUID.randomUUID().toString());
        }

    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getClientguid(), null,
                    null, null, userSession.getUserGuid(), userSession.getDisplayName()));
        }

        return fileUploadModel;
    }

    public AuditSpPolicyBasicinfo getDataBean() {
        return dataBean;
    }
}
