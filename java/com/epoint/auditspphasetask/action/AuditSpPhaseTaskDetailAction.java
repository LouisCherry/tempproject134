package com.epoint.auditspphasetask.action;

import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.auditspphasetask.api.IAuditSpPhaseTaskService;
import com.epoint.auditspphasetask.api.entity.AuditSpPhaseTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前四阶段事项配置表详情页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:06:22]
 */
@RightRelation(AuditSpPhaseTaskListAction.class)
@RestController("auditspphasetaskdetailaction")
@Scope("request")
public class AuditSpPhaseTaskDetailAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseTaskService service;

    @Autowired
    private IAuditSpPhaseGroupService iAuditSpPhaseGroupService;
    /**
     * 前四阶段事项配置表实体对象
     */
    private AuditSpPhaseTask dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPhaseTask();
        }

        AuditSpPhaseGroup find = iAuditSpPhaseGroupService.find(dataBean.getGroupguid());
        if (find != null) {
            dataBean.setGroupguid(find.getGroupname());
        }

    }

    public AuditSpPhaseTask getDataBean() {
        return dataBean;
    }
}
