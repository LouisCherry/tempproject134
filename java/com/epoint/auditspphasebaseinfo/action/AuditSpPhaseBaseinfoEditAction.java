package com.epoint.auditspphasebaseinfo.action;

import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 前四阶段信息配置表修改页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@RightRelation(AuditSpPhaseBaseinfoListAction.class)
@RestController("auditspphasebaseinfoeditaction")
@Scope("request")
public class AuditSpPhaseBaseinfoEditAction extends BaseController
{

    @Autowired
    private IAuditSpPhaseBaseinfoService service;

    /**
     * 前四阶段信息配置表实体对象
     */
    private AuditSpPhaseBaseinfo dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpPhaseBaseinfo();
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

    public AuditSpPhaseBaseinfo getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

}
