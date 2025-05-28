package com.epoint.xmz.auditfszldevice.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 放射装置表详情页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:22]
 */
@RightRelation(AuditFszlDeviceListAction.class)
@RestController("auditfszldevicedetailaction")
@Scope("request")
public class AuditFszlDeviceDetailAction extends BaseController {
    @Autowired
    private IAuditFszlDeviceService service;

    /**
     * 放射装置表实体对象
     */
    private AuditFszlDevice dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditFszlDevice();
        }
    }


    public AuditFszlDevice getDataBean() {
        return dataBean;
    }
}