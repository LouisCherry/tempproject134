package com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.ISpglSpfysxkxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity.SpglSpfysxkxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品房预售信息许可表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:51]
 */
@RightRelation(SpglSpfysxkxxbListAction.class)
@RestController("spglspfysxkxxbdetailaction")
@Scope("request")
public class SpglSpfysxkxxbDetailAction extends BaseController {
    @Autowired
    private ISpglSpfysxkxxbService service;

    /**
     * 商品房预售信息许可表实体对象
     */
    private SpglSpfysxkxxb dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglSpfysxkxxb();
        }
    }


    public SpglSpfysxkxxb getDataBean() {
        return dataBean;
    }
}