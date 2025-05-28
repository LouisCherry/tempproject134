package com.epoint.auditsp.auditsppolicy.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditsppolicy.api.IAuditSpPolicyService;
import com.epoint.auditsp.auditsppolicy.api.entity.AuditSpPolicy;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 一件事政策解读详情页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:34]
 */
@RightRelation(AuditSpPolicyListAction.class)
@RestController("auditsppolicydetailaction")
@Scope("request")
public class AuditSpPolicyDetailAction extends BaseController
{
    @Autowired
    private IAuditSpPolicyService service;

    /**
     * 一件事政策解读实体对象
     */
    private AuditSpPolicy dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPolicy();
        }
    }

    public AuditSpPolicy getDataBean() {
        return dataBean;
    }
}
