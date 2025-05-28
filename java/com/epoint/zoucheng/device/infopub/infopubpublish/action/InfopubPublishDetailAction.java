package com.epoint.zoucheng.device.infopub.infopubpublish.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.IInfopubPublishService;
import com.epoint.zoucheng.device.infopub.infopubpublish.api.entity.InfopubPublish;

/**
 * 节目发布表详情页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-23 08:42:26]
 */
@RestController("infopubpublishdetailaction")
@Scope("request")
public class InfopubPublishDetailAction extends BaseController
{
    private static final long serialVersionUID = -1651233785326608242L;

    @Autowired
    private IInfopubPublishService service;

    /**
     * 节目发布表实体对象
     */
    private InfopubPublish dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new InfopubPublish();
        }
        addCallbackParam("programguid", dataBean.getProgramguid());
    }

    public InfopubPublish getDataBean() {
        return dataBean;
    }

}
