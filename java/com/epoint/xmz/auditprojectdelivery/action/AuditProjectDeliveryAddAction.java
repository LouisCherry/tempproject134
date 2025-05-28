package com.epoint.xmz.auditprojectdelivery.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.auditprojectdelivery.api.IAuditProjectDeliveryService;
import com.epoint.xmz.auditprojectdelivery.api.entity.AuditProjectDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电力材料映射表新增页面对应的后台
 *
 * @author lee
 * @version [版本号, 2023-08-10 15:24:06]
 */
@RestController("auditprojectdeliveryaddaction")
@Scope("request")
public class AuditProjectDeliveryAddAction extends BaseController {
    @Autowired
    private IAuditProjectDeliveryService service;
    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 电力材料映射表实体对象
     */
    private AuditProjectDelivery dataBean = null;

    private String projectguid = "";

    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        dataBean = service.getDeliveryByProjectguid(projectguid);
        if (dataBean != null) {
            AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectguid, "").getResult();
            if (auditProject != null) {
                dataBean.setFlowsn(auditProject.getFlowsn());
            }
        } else {
            dataBean = new AuditProjectDelivery();
        }
    }

    /**
     * 保存快递单号
     */
    public void save() {
        dataBean.set("ismail", "1");
        service.update(dataBean);
        addCallbackParam("msg", "保存成功！");
    }

    public AuditProjectDelivery getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProjectDelivery();
        }
        return dataBean;
    }

    public void setDataBean(AuditProjectDelivery dataBean) {
        this.dataBean = dataBean;
    }


}
