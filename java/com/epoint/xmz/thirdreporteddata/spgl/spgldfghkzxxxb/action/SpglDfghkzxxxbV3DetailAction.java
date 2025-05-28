package com.epoint.xmz.thirdreporteddata.spgl.spgldfghkzxxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglDfghkzxxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglDfghkzxxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地方规划控制线信息表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:47]
 */
@RightRelation(SpglDfghkzxxxbV3ListAction.class)
@RestController("spgldfghkzxxxbv3detailaction")
@Scope("request")
public class SpglDfghkzxxxbV3DetailAction extends BaseController {
    @Autowired
    private ISpglDfghkzxxxbV3Service service;

    /**
     * 地方规划控制线信息表实体对象
     */
    private SpglDfghkzxxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglDfghkzxxxbV3();
        }
    }

    public SpglDfghkzxxxbV3 getDataBean() {
        return dataBean;
    }
}