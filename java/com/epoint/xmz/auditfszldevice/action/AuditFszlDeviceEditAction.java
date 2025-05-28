package com.epoint.xmz.auditfszldevice.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 放射装置表修改页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:22]
 */
@RightRelation(AuditFszlDeviceListAction.class)
@RestController("auditfszldeviceeditaction")
@Scope("request")
public class AuditFszlDeviceEditAction extends BaseController {

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

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public AuditFszlDevice getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditFszlDevice dataBean) {
        this.dataBean = dataBean;
    }

}
