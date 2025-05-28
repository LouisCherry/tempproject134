package com.epoint.xmz.thirdreporteddata.spgl.spglxmjgxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjgxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目监管信息表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:32]
 */
@RightRelation(SpglXmjgxxbV3ListAction.class)
@RestController("spglxmjgxxbv3detailaction")
@Scope("request")
public class SpglXmjgxxbV3DetailAction extends BaseController {
    @Autowired
    private ISpglXmjgxxbV3Service service;

    /**
     * 项目监管信息表实体对象
     */
    private SpglXmjgxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglXmjgxxbV3();
        }
    }


    public SpglXmjgxxbV3 getDataBean() {
        return dataBean;
    }
}