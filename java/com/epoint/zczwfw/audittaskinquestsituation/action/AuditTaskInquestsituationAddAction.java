package com.epoint.zczwfw.audittaskinquestsituation.action;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.zczwfw.audittaskinquestsituation.api.IAuditTaskInquestsituationService;
import com.epoint.zczwfw.audittaskinquestsituation.api.entity.AuditTaskInquestsituation;

/**
 * 勘验事项情形表新增页面对应的后台
 * 
 * @author yrchan
 * @version [版本号, 2022-04-18 09:53:53]
 */
@RightRelation(AuditTaskInquestsituationListAction.class)
@RestController("audittaskinquestsituationaddaction")
@Scope("request")
public class AuditTaskInquestsituationAddAction extends BaseController
{
    private static final long serialVersionUID = 6161496073085126959L;
    @Autowired
    private IAuditTaskInquestsituationService service;
    /**
     * 勘验事项情形表实体对象
     */
    private AuditTaskInquestsituation dataBean = null;

    public void pageLoad() {
        dataBean = new AuditTaskInquestsituation();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setTaskguid(getRequestParameter("taskGuid"));
        dataBean.setCreatedate(new Date());
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
        dataBean = new AuditTaskInquestsituation();
    }

    public AuditTaskInquestsituation getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskInquestsituation();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskInquestsituation dataBean) {
        this.dataBean = dataBean;
    }

}
