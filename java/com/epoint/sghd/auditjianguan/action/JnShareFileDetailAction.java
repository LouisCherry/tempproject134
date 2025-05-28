package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.sghd.auditjianguan.domain.AuditTaskShareFile;
import com.epoint.sghd.auditjianguan.inter.IAuditTaskShareFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 2018年9月19日
 *
 * @author swy
 */
@RestController("jnsharefiledetailaction")
@Scope("request")
public class JnShareFileDetailAction extends BaseController {
    private static final long serialVersionUID = 1L;

    private AuditTaskShareFile dataBean;

    @Autowired
    private IAuditTaskShareFile shareService;

    /**
     * 附件service
     */
    @Autowired
    private IAttachService attachService;

    @Override
    public void pageLoad() {
        String guid = this.getRequestParameter("rowguid");
        this.dataBean = this.shareService.getShareFileByGuid(guid).getResult();
        if (this.dataBean == null) {
            this.dataBean = new AuditTaskShareFile();
        }
        if (StringUtil.isNotBlank(dataBean.getFileclientguid())) {
            List<FrameAttachInfo> frameAttachInfoList = attachService
                    .getAttachInfoListByGuid(dataBean.getFileclientguid());
            addCallbackParam("list", frameAttachInfoList);
        }

    }

    public AuditTaskShareFile getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditTaskShareFile dataBean) {
        this.dataBean = dataBean;
    }

}
