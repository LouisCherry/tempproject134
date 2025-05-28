package com.epoint.zoucheng.device.infopub.infopubled.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.zoucheng.device.infopub.infopubled.api.IInfopubLedService;
import com.epoint.zoucheng.device.infopub.infopubled.api.entity.InfopubLed;

/**
 * LED大屏详情页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-22 15:26:57]
 */
@RestController("infopubleddetailaction")
@Scope("request")
public class InfopubLedDetailAction extends BaseController
{
    private static final long serialVersionUID = 1055849654447593454L;
    @Autowired
    private IInfopubLedService service;

    /**
     * LED大屏实体对象
     */
    private InfopubLed dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new InfopubLed();
        }
    }

    public InfopubLed getDataBean() {
        return dataBean;
    }
}
