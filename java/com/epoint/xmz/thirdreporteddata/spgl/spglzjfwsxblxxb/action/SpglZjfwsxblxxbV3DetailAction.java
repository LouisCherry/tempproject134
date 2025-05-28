package com.epoint.xmz.thirdreporteddata.spgl.spglzjfwsxblxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZjfwsxblxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZjfwsxblxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 中介服务事项办理信息表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:38]
 */
@RightRelation(SpglZjfwsxblxxbV3ListAction.class)
@RestController("spglzjfwsxblxxbv3detailaction")
@Scope("request")
public class SpglZjfwsxblxxbV3DetailAction extends BaseController {
    @Autowired
    private ISpglZjfwsxblxxbV3Service service;

    /**
     * 中介服务事项办理信息表实体对象
     */
    private SpglZjfwsxblxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglZjfwsxblxxbV3();
        }
    }


    public SpglZjfwsxblxxbV3 getDataBean() {
        return dataBean;
    }
}