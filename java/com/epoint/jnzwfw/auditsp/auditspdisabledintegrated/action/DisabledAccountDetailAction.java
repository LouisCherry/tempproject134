package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.IAuditYjsCjrService;
import com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity.CjrApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 残联账号表详情页面对应的后台
 *
 * @author ez
 * @version [版本号, 2021-04-15 16:18:31]
 */
@RightRelation(DisabledAccountListAction.class)
@RestController("disabledaccountdetailaction")
@Scope("request")
public class DisabledAccountDetailAction extends BaseController {
    @Autowired
    private IAuditYjsCjrService service;

    /**
     * 残联账号表实体对象
     */
    private CjrApiConfig dataBean = null;

    @Override
    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(CjrApiConfig.class, guid);
        if (dataBean == null) {
            dataBean = new CjrApiConfig();
        }
    }


    public CjrApiConfig getDataBean() {
        return dataBean;
    }
}
