package com.epoint.auditsp.auditsppolicy.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditsppolicy.api.IAuditSpPolicyService;
import com.epoint.auditsp.auditsppolicy.api.entity.AuditSpPolicy;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;

/**
 * 一件事政策解读修改页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:34]
 */
@RightRelation(AuditSpPolicyListAction.class)
@RestController("auditsppolicyeditaction")
@Scope("request")
public class AuditSpPolicyEditAction extends BaseController
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

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public AuditSpPolicy getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpPolicy dataBean) {
        this.dataBean = dataBean;
    }

}
