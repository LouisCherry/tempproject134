package com.epoint.xmz.thirdreporteddata.spgl.spglzjfwsxblgcxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZjfwsxblgcxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZjfwsxblgcxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 中介服务事项办理过程信息表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:43]
 */
@RightRelation(SpglZjfwsxblgcxxbV3ListAction.class)
@RestController("spglzjfwsxblgcxxbv3detailaction")
@Scope("request")
public class SpglZjfwsxblgcxxbV3DetailAction extends BaseController {
    @Autowired
    private ISpglZjfwsxblgcxxbV3Service service;

    /**
     * 中介服务事项办理过程信息表实体对象
     */
    private SpglZjfwsxblgcxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglZjfwsxblgcxxbV3();
        }
    }


    public SpglZjfwsxblgcxxbV3 getDataBean() {
        return dataBean;
    }
}