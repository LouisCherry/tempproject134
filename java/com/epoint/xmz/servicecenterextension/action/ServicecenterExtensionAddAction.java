package com.epoint.xmz.servicecenterextension.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.servicecenterextension.api.IServicecenterExtensionService;
import com.epoint.xmz.servicecenterextension.api.entity.ServicecenterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 中心配置信息表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-09 14:14:08]
 */
@RestController("servicecenterextensionaddaction")
@Scope("request")
public class ServicecenterExtensionAddAction extends BaseController {
    @Autowired
    private IServicecenterExtensionService service;
    /**
     * 中心配置信息表实体对象
     */
    private ServicecenterExtension dataBean = null;

    private String centerguid;

    public void pageLoad() {
        centerguid = getRequestParameter("centerguid");
        dataBean = service.getInfoByCenterguid(centerguid);
        if (dataBean == null) {
            dataBean = new ServicecenterExtension();
        }
    }

    /**
     * 保存并关闭
     */
    public void add() {
        if (StringUtil.isNotBlank(dataBean.getRowguid())) {
            service.update(dataBean);
        } else {
            dataBean.setRowguid(UUID.randomUUID().toString());
            dataBean.setOperatedate(new Date());
            dataBean.setOperateusername(userSession.getDisplayName());
            dataBean.setCenterguid(centerguid);
            service.insert(dataBean);
        }
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new ServicecenterExtension();
    }

    public ServicecenterExtension getDataBean() {
        if (dataBean == null) {
            dataBean = new ServicecenterExtension();
        }
        return dataBean;
    }

    public void setDataBean(ServicecenterExtension dataBean) {
        this.dataBean = dataBean;
    }


}
