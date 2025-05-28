package com.epoint.auditqueue.auditznsbremotehelp.action;

import java.util.Date;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbremotehelp.domain.AuditZnsbRemoteHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.inter.IAuditZnsbRemoteHelpService;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;


/**
 *  一体机绑定好视通账户add页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2017-05-01 13:50:10]
 */
@RestController("auditznsbremotehelpaddaction")
@Scope("request")
public class AuditZnsbRemoteHelpAddAction extends BaseController
{
    private static final long serialVersionUID = 1690140724829267535L;
    
    @Autowired
    private IAuditZnsbRemoteHelpService auditZnsbRemoteHelpService;   
    /**
     * 一体机绑定好视通账户实体对象
     */
    private AuditZnsbRemoteHelp dataBean;


    public void pageLoad() {
        dataBean = new AuditZnsbRemoteHelp();
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
        int count = auditZnsbRemoteHelpService.getAuditZnsbRemoteHelpName(dataBean.getAccount());
        if(count > 0){
            addCallbackParam("msg", "账户已被注册！");
        }else{
            auditZnsbRemoteHelpService.insert(dataBean);
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
        dataBean = new AuditZnsbRemoteHelp();
    }

    public AuditZnsbRemoteHelp getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbRemoteHelp();
        }
        return dataBean;
    }
    

    public void setDataBean(AuditZnsbRemoteHelp dataBean) {
        this.dataBean = dataBean;
    }


}
