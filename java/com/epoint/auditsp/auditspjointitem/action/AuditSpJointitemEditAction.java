package com.epoint.auditsp.auditspjointitem.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspjointitem.api.IAuditSpJointitemService;
import com.epoint.auditsp.auditspjointitem.api.entity.AuditSpJointitem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 一件事联办事项修改页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:40]
 */
@RightRelation(AuditSpJointitemListAction.class)
@RestController("auditspjointitemeditaction")
@Scope("request")
public class AuditSpJointitemEditAction extends BaseController
{

    @Autowired
    private IAuditSpJointitemService service;

    /**
     * 一件事联办事项实体对象
     */
    private AuditSpJointitem dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpJointitem();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public AuditSpJointitem getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpJointitem dataBean) {
        this.dataBean = dataBean;
    }

}
