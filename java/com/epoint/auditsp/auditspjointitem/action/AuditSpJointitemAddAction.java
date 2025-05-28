package com.epoint.auditsp.auditspjointitem.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspjointitem.api.IAuditSpJointitemService;
import com.epoint.auditsp.auditspjointitem.api.entity.AuditSpJointitem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 一件事联办事项新增页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:40]
 */
@RightRelation(AuditSpJointitemListAction.class)
@RestController("auditspjointitemaddaction")
@Scope("request")
public class AuditSpJointitemAddAction extends BaseController
{
    @Autowired
    private IAuditSpJointitemService service;
    /**
     * 一件事联办事项实体对象
     */
    private AuditSpJointitem dataBean = null;

    public void pageLoad() {
        dataBean = new AuditSpJointitem();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setBusinessguid(getRequestParameter("businessguid"));

        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditSpJointitem();
    }

    public AuditSpJointitem getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpJointitem();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpJointitem dataBean) {
        this.dataBean = dataBean;
    }

}
