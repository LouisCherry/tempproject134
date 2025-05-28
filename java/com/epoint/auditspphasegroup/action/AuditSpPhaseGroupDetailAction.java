package com.epoint.auditspphasegroup.action;

import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前四阶段分组配置表详情页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:01:54]
 */
@RightRelation(AuditSpPhaseGroupListAction.class)
@RestController("auditspphasegroupdetailaction")
@Scope("request")
public class AuditSpPhaseGroupDetailAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseGroupService service;

    /**
     * 前四阶段分组配置表实体对象
     */
    private AuditSpPhaseGroup dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPhaseGroup();
        }
    }

    public AuditSpPhaseGroup getDataBean() {
        return dataBean;
    }
}
