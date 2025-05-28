package com.epoint.auditresource.auditspisubappopinion.action;

import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;

import com.epoint.auditresource.auditspisubappopinion.api.IAuditSpISubappOpinionService;
import com.epoint.auditresource.auditspisubappopinion.api.entity.AuditSpISubappOpinion;
import com.epoint.basic.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 并联审批意见修改页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-04-28 10:42:04]
 */
@RestController("auditspisubappopinioneditaction")
@Scope("request")
public class AuditSpISubappOpinionEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditSpISubappOpinionService service;

    /**
     * 并联审批意见实体对象
     */
    private AuditSpISubappOpinion dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new AuditSpISubappOpinion();
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

    public AuditSpISubappOpinion getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditSpISubappOpinion dataBean) {
        this.dataBean = dataBean;
    }

}
