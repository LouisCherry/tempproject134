package com.epoint.auditsprelationtask.action;

import com.epoint.auditsprelationtask.api.IAuditSpRelationTaskService;
import com.epoint.auditsprelationtask.api.entity.AuditSpRelationTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 前四阶段事项关系表新增页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:13:27]
 */
@RightRelation(AuditSpRelationTaskListAction.class)
@RestController("auditsprelationtaskaddaction")
@Scope("request")
public class AuditSpRelationTaskAddAction extends BaseController
{
    @Autowired
    private IAuditSpRelationTaskService service;
    /**
     * 前四阶段事项关系表实体对象
     */
    private AuditSpRelationTask dataBean = null;

    public void pageLoad() {
        dataBean = new AuditSpRelationTask();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        service.insert(dataBean);
        addCallbackParam("msg", "保存成功！");
        dataBean = null;
    }

    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditSpRelationTask();
    }

    public AuditSpRelationTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpRelationTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpRelationTask dataBean) {
        this.dataBean = dataBean;
    }

}
