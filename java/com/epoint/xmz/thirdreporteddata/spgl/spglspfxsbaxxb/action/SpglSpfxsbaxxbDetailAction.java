package com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.ISpglSpfxsbaxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfxsbaxxb.api.entity.SpglSpfxsbaxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品房现售备案信息表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:55]
 */
@RightRelation(SpglSpfxsbaxxbListAction.class)
@RestController("spglspfxsbaxxbdetailaction")
@Scope("request")
public class SpglSpfxsbaxxbDetailAction extends BaseController {
    @Autowired
    private ISpglSpfxsbaxxbService service;

    /**
     * 商品房现售备案信息表实体对象
     */
    private SpglSpfxsbaxxb dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglSpfxsbaxxb();
        }
    }


    public SpglSpfxsbaxxb getDataBean() {
        return dataBean;
    }
}