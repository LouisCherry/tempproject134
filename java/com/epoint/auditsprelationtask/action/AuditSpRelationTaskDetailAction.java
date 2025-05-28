package com.epoint.auditsprelationtask.action;

import com.epoint.auditsprelationtask.api.IAuditSpRelationTaskService;
import com.epoint.auditsprelationtask.api.entity.AuditSpRelationTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前四阶段事项关系表详情页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:13:27]
 */
@RightRelation(AuditSpRelationTaskListAction.class)
@RestController("auditsprelationtaskdetailaction")
@Scope("request")
public class AuditSpRelationTaskDetailAction extends BaseController
{
    @Autowired
    private IAuditSpRelationTaskService service;

    /**
     * 前四阶段事项关系表实体对象
     */
    private AuditSpRelationTask dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpRelationTask();
        }
    }

    public AuditSpRelationTask getDataBean() {
        return dataBean;
    }
}
