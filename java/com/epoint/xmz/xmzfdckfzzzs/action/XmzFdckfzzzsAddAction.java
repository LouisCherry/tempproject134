package com.epoint.xmz.xmzfdckfzzzs.action;

import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.xmz.xmzfdckfzzzs.api.entity.XmzFdckfzzzs;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.controller.BaseController;
import com.epoint.xmz.xmzfdckfzzzs.api.IXmzFdckfzzzsService;

/**
 * 房地产开发资质证书新增页面对应的后台
 * 
 * @author 86177
 * @version [版本号, 2021-05-12 09:40:37]
 */
@RightRelation(XmzFdckfzzzsListAction.class)
@RestController("xmzfdckfzzzsaddaction")
@Scope("request")
public class XmzFdckfzzzsAddAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IXmzFdckfzzzsService service;
    /**
     * 房地产开发资质证书实体对象
     */
    private XmzFdckfzzzs dataBean = null;

    public void pageLoad() {
        dataBean = new XmzFdckfzzzs();
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
        dataBean = new XmzFdckfzzzs();
    }

    public XmzFdckfzzzs getDataBean() {
        if (dataBean == null) {
            dataBean = new XmzFdckfzzzs();
        }
        return dataBean;
    }

    public void setDataBean(XmzFdckfzzzs dataBean) {
        this.dataBean = dataBean;
    }

}
