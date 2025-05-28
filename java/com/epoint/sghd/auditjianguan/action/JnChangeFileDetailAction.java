package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 变更意见附件详情页面action
 *
 * @author 许烨斌
 * @time 2019年2月11日下午10:11:54
 */
@RestController("jnchangefiledetailaction")
@Scope("request")
public class JnChangeFileDetailAction extends BaseController {
    private static final long serialVersionUID = 1L;

    private AuditProjectPermissionChange dataBean;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    /**
     * 附件service
     */
    @Autowired
    private IAttachService attachService;

    @Override
    public void pageLoad() {
        String guid = this.getRequestParameter("guid");
        dataBean = jnAuditJianGuanService.find(guid);
        if (StringUtil.isNotBlank(dataBean.getFileclientguid())) {
            List<FrameAttachInfo> frameAttachInfoList = attachService
                    .getAttachInfoListByGuid(dataBean.getFileclientguid());
            addCallbackParam("list", frameAttachInfoList);
        }

    }

    public AuditProjectPermissionChange getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectPermissionChange dataBean) {
        this.dataBean = dataBean;
    }

}
