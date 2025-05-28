package com.epoint.auditqueue.auditznsbremotehelpuser.action;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.basic.controller.BaseController;


/**
 * 一体机绑定好视通账户修改页面对应的后台
 * 
 * @author wangjie
 * @version [版本号, 2017-05-01 13:50:10]
 */
@RestController("auditznsbremotehelpusereditaction")
@Scope("request")
public class AuditZnsbRemoteHelpUserEditAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -3199393796565245392L;

    @Autowired
    private IAuditZnsbRemoteHelpUserService auditZnsbAssessConfigService;

    /**
     * 一体机绑定好视通账户实体对象
     */
    private AuditZnsbRemoteHelpUser dataBean;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = auditZnsbAssessConfigService.getDetail(guid).getResult();
        if (dataBean == null) {
            dataBean = new AuditZnsbRemoteHelpUser();
        }
    }

    /**
     * 保存修改
     * 
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        auditZnsbAssessConfigService.update(dataBean);
        addCallbackParam("msg", "修改成功！");
    }

    public AuditZnsbRemoteHelpUser getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditZnsbRemoteHelpUser dataBean) {
        this.dataBean = dataBean;
    }

}
