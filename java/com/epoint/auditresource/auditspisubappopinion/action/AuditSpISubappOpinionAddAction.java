package com.epoint.auditresource.auditspisubappopinion.action;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.auditresource.auditspisubappopinion.api.IAuditSpISubappOpinionService;
import com.epoint.auditresource.auditspisubappopinion.api.entity.AuditSpISubappOpinion;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.ZwfwConstant;

/**
 * 并联审批意见新增页面对应的后台
 * 
 * @author zhaoy
 * @version [版本号, 2019-04-28 10:42:04]
 */
@RestController("auditspisubappopinionaddaction")
@Scope("request")
public class AuditSpISubappOpinionAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditSpISubappOpinionService service;
    
    private String biguid;
    /**
     * 并联审批意见实体对象
     */
    private AuditSpISubappOpinion dataBean = null;

    public void pageLoad() {
        dataBean = new AuditSpISubappOpinion();
        biguid = getRequestParameter("guid");
    }

    /**
     * 保存并关闭
     * 
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setCreatedate(new Date());
        dataBean.setBiguid(biguid);
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setFaqansweruser(userSession.getDisplayName());
        dataBean.setFaqansweruserguid(userSession.getUserGuid());
        dataBean.setType(ZwfwConstant.CONSTANT_STR_TWO);
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
        dataBean = new AuditSpISubappOpinion();
    }

    public AuditSpISubappOpinion getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpISubappOpinion();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpISubappOpinion dataBean) {
        this.dataBean = dataBean;
    }

    public String getBiguid() {
        return biguid;
    }

    public void setBiguid(String biguid) {
        this.biguid = biguid;
    }

}
