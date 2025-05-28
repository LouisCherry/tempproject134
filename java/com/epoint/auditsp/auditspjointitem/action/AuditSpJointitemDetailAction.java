package com.epoint.auditsp.auditspjointitem.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspjointitem.api.IAuditSpJointitemService;
import com.epoint.auditsp.auditspjointitem.api.entity.AuditSpJointitem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 一件事联办事项详情页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:40]
 */
@RightRelation(AuditSpJointitemListAction.class)
@RestController("auditspjointitemdetailaction")
@Scope("request")
public class AuditSpJointitemDetailAction extends BaseController
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

    public AuditSpJointitem getDataBean() {
        return dataBean;
    }
}
