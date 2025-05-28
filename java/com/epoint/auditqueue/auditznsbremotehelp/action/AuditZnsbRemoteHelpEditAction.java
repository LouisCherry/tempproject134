package com.epoint.auditqueue.auditznsbremotehelp.action;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbremotehelp.domain.AuditZnsbRemoteHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.inter.IAuditZnsbRemoteHelpService;
import com.epoint.basic.controller.BaseController;

/**
 * 一体机绑定好视通账户修改页面对应的后台
 * 
 * @author wangjie
 * @version [版本号, 2017-05-01 13:50:10]
 */
@RestController("auditznsbremotehelpeditaction")
@Scope("request")
public class AuditZnsbRemoteHelpEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -3199393796565245392L;

    @Autowired
    private IAuditZnsbRemoteHelpService auditZnsbRemoteHelpService;   
    /**
     * 一体机绑定好视通账户实体对象
     */
    private AuditZnsbRemoteHelp dataBean;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = auditZnsbRemoteHelpService.getDetail(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditZnsbRemoteHelp();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        auditZnsbRemoteHelpService.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditZnsbRemoteHelp getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbRemoteHelp dataBean) {
        this.dataBean = dataBean;
    }

}
