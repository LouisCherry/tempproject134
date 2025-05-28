package com.epoint.sghd.auditjianguan.rlconfig.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.sghd.auditjianguan.rlconfig.api.IRlconfigSendmsg;
import com.epoint.sghd.auditjianguan.rlconfig.api.RlconfigSendmsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前四阶段信息配置表详情页面对应的后台
 *
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@RestController("rlconfigsendmsgdetailaction")
@Scope("request")
public class RlconfigSendmsgDetailAction extends BaseController {
    @Autowired
    private IRlconfigSendmsg service;

    /**
     * 前四阶段信息配置表实体对象
     */
    private RlconfigSendmsg dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new RlconfigSendmsg();
        }
    }

    public RlconfigSendmsg getDataBean() {
        return dataBean;
    }

    public void setDataBean(RlconfigSendmsg dataBean) {
        this.dataBean = dataBean;
    }
}
