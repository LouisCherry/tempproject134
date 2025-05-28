package com.epoint.auditqueue.auditznsbremotehelpuser.action;

import java.util.Date;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;


/**
 *  一体机绑定好视通账户add页面对应的后台
 * 
 * @author wj
 * @version [版本号, 2017-05-01 13:50:10]
 */
@RestController("auditznsbremotehelpuseraddaction")
@Scope("request")
public class AuditZnsbRemoteHelpUserAddAction extends BaseController
{
    private static final long serialVersionUID = 1690140724829267535L;
    
    @Autowired
    private IAuditZnsbRemoteHelpUserService auditZnsbAssessConfigService;

    
    /**
     * 一体机绑定好视通账户实体对象
     */
    private AuditZnsbRemoteHelpUser dataBean;


    public void pageLoad() {
        dataBean = new AuditZnsbRemoteHelpUser();
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
        int count = auditZnsbAssessConfigService.getAuditZnsbRemoteHelpUserByAccount(dataBean.getAccount());
        if(count>0){
            addCallbackParam("msg", "账户已被注册！");
        }else{
            auditZnsbAssessConfigService.insert(dataBean);
            addCallbackParam("msg", "保存成功！");
            dataBean = null; 
        }
    }
    
    /**
     * 保存并新建
     * 
     */
    public void addNew() {
        add();
        dataBean = new AuditZnsbRemoteHelpUser();
    }

    public AuditZnsbRemoteHelpUser getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbRemoteHelpUser();
        }
        return dataBean;
    }
    

    public void setDataBean(AuditZnsbRemoteHelpUser dataBean) {
        this.dataBean = dataBean;
    }


}
