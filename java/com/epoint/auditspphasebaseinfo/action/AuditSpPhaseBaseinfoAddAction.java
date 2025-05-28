package com.epoint.auditspphasebaseinfo.action;

import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 前四阶段信息配置表新增页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@RightRelation(AuditSpPhaseBaseinfoListAction.class)
@RestController("auditspphasebaseinfoaddaction")
@Scope("request")
public class AuditSpPhaseBaseinfoAddAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseBaseinfoService service;
    /**
     * 前四阶段信息配置表实体对象
     */
    private AuditSpPhaseBaseinfo dataBean = null;

    public void pageLoad() {
        dataBean = new AuditSpPhaseBaseinfo();
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
        dataBean = new AuditSpPhaseBaseinfo();
    }

    public AuditSpPhaseBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPhaseBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

}
