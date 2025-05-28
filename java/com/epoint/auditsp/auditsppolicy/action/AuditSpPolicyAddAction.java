package com.epoint.auditsp.auditsppolicy.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditsppolicy.api.IAuditSpPolicyService;
import com.epoint.auditsp.auditsppolicy.api.entity.AuditSpPolicy;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 一件事政策解读新增页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:34]
 */
@RightRelation(AuditSpPolicyListAction.class)
@RestController("auditsppolicyaddaction")
@Scope("request")
public class AuditSpPolicyAddAction extends BaseController
{
    @Autowired
    private IAuditSpPolicyService service;
    /**
     * 一件事政策解读实体对象
     */
    private AuditSpPolicy dataBean = null;

    public void pageLoad() {
        dataBean = new AuditSpPolicy();
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
        dataBean = new AuditSpPolicy();
    }

    public AuditSpPolicy getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPolicy();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPolicy dataBean) {
        this.dataBean = dataBean;
    }

}
