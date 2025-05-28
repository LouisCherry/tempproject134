package com.epoint.zoucheng.device.infopub.infopubprogram.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.IInfopubProgramService;
import com.epoint.zoucheng.device.infopub.infopubprogram.api.entity.InfopubProgram;

/**
 * 节目表详情页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-16 15:39:32]
 */
@RestController("infopubprogramdetailaction")
@Scope("request")
public class InfopubProgramDetailAction extends BaseController
{
    private static final long serialVersionUID = 1688670205154466181L;
    @Autowired
    private IInfopubProgramService infopubProgramService;

    /**
     * 节目表实体对象
     */
    private InfopubProgram dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = infopubProgramService.find(guid);
        addCallbackParam("programguid", dataBean.getRowguid());
        if (dataBean == null) {
            dataBean = new InfopubProgram();
        }
    }

    public InfopubProgram getDataBean() {
        return dataBean;
    }
}
