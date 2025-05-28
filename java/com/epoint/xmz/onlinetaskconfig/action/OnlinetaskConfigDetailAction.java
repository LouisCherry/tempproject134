package com.epoint.xmz.onlinetaskconfig.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.onlinetaskconfig.api.entity.OnlinetaskConfig;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.onlinetaskconfig.api.IOnlinetaskConfigService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 居民办事配置详情页面对应的后台
 *
 * @author RaoShaoliang
 * @version [版本号, 2023-10-17 15:38:09]
 */
@RightRelation(OnlinetaskConfigListAction.class)
@RestController("onlinetaskconfigdetailaction")
@Scope("request")
public class OnlinetaskConfigDetailAction extends BaseController {
    @Autowired
    private IOnlinetaskConfigService service;

    /**
     * 居民办事配置实体对象
     */
    private OnlinetaskConfig dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new OnlinetaskConfig();
        }
    }


    public OnlinetaskConfig getDataBean() {
        return dataBean;
    }
}