package com.epoint.xmz.auditfszldevice.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.auditfszldevice.api.entity.AuditFszlDevice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.auditfszldevice.api.IAuditFszlDeviceService;

/**
 * 放射装置表新增页面对应的后台
 *
 * @author ljh
 * @version [版本号, 2024-06-20 10:28:22]
 */
@RightRelation(AuditFszlDeviceListAction.class)
@RestController("auditfszldeviceaddaction")
@Scope("request")
public class AuditFszlDeviceAddAction extends BaseController {
    @Autowired
    private IAuditFszlDeviceService service;
    /**
     * 放射装置表实体对象
     */
    private AuditFszlDevice dataBean = null;


    public void pageLoad() {
        dataBean = new AuditFszlDevice();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setFszlguid(getRequestParameter("fszlGuid"));
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new AuditFszlDevice();
    }

    public AuditFszlDevice getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditFszlDevice();
        }
        return dataBean;
    }

    public void setDataBean(AuditFszlDevice dataBean) {
        this.dataBean = dataBean;
    }


}
