package com.epoint.zoucheng.device.infopub.infopubplayprogram.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.IInfopubPlayprogramService;
import com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity.InfopubPlayprogram;

/**
 * 发布单节目列表详情页面对应的后台
 * 
 * @author 12150
 * @version [版本号, 2017-09-01 14:05:48]
 */
@RestController("infopubplayprogramdetailaction")
@Scope("request")
public class InfopubPlayprogramDetailAction extends BaseController
{
    private static final long serialVersionUID = -1124828173907811867L;
    
    @Autowired
    private IInfopubPlayprogramService service;

    /**
     * 发布单节目列表实体对象
     */
    private InfopubPlayprogram dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new InfopubPlayprogram();
        }
        addCallbackParam("programguid", dataBean.getProgramguid());
    }

    public InfopubPlayprogram getDataBean() {
        return dataBean;
    }
}
