package com.epoint.auditsprelationtask.action;

import com.epoint.auditsprelationtask.api.IAuditSpRelationTaskService;
import com.epoint.auditsprelationtask.api.entity.AuditSpRelationTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 前四阶段事项关系表修改页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:13:27]
 */
@RightRelation(AuditSpRelationTaskListAction.class)
@RestController("auditsprelationtaskeditaction")
@Scope("request")
public class AuditSpRelationTaskEditAction extends BaseController
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

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditSpRelationTask getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpRelationTask dataBean) {
        this.dataBean = dataBean;
    }

}
