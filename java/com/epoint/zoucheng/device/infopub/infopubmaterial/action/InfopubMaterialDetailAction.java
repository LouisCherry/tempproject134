package com.epoint.zoucheng.device.infopub.infopubmaterial.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.zoucheng.device.infopub.infopubmaterial.api.IInfopubMaterialService;
import com.epoint.zoucheng.device.infopub.infopubmaterial.api.entity.InfopubMaterial;

/**
 * 素材表详情页面对应的后台
 * 
 * @author 15394
 * @version [版本号, 2017-08-14 16:30:56]
 */
@RestController("infopubmaterialdetailaction")
@Scope("request")
public class InfopubMaterialDetailAction extends BaseController
{
    private static final long serialVersionUID = -790811488931271292L;

    @Autowired
    private IInfopubMaterialService materialService;
    
    /**
     * 素材表实体对象
     */
    private InfopubMaterial dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = materialService.find(guid);
        if (dataBean == null) {
            dataBean = new InfopubMaterial();
        }
    }

    public InfopubMaterial getDataBean() {
        return dataBean;
    }
}
