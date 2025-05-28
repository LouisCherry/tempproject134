package com.epoint.auditperformance.auditperformancedetail.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditperformance.auditperformancedetail.doman.AuditPerformanceDetail;
import com.epoint.basic.auditperformance.auditperformancedetail.inter.IAuditPerformanceDetail;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.utils.string.StringUtil;

/**
 * 考评明细新增页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2018-01-09 10:31:38]
 */
@RestController("auditperformancedetaildetailaction")
@Scope("request")
public class AuditPerformanceDetailDetailAction extends BaseController
{
    private static final long serialVersionUID = -3873903876058473434L;
    @Autowired
    private IAuditPerformanceDetail service;

    private AuditPerformanceDetail dataBean=null;

    private FileUploadModel9 fileUploadModel = null;

    public AuditPerformanceDetail getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditPerformanceDetail dataBean) {
        this.dataBean = dataBean;
    }

    @Override
    public void pageLoad() {
        if (StringUtil.isNotBlank(this.getRequestParameter("guid"))
                && service.findDetailByRowguid(this.getRequestParameter("guid")) != null) {
            dataBean = service.findDetailByRowguid(this.getRequestParameter("guid")).getResult();
            if (StringUtil.isBlank(dataBean.getUpdaterecord())) {
                addCallbackParam("updaterecord", "true");
            }
            else {
                addCallbackParam("updaterecord", "false");
            }
        }
    }
    
    public FileUploadModel9 getUploadmodel() {
        if (fileUploadModel == null) {
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(dataBean.getGradecliengguid(), null, null,
                    null, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

}
