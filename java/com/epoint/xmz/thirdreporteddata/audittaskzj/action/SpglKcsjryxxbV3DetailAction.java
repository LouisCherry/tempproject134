package com.epoint.xmz.thirdreporteddata.audittaskzj.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglKcsjryxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglKcsjryxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 勘察设计人员信息表详情页面对应的后台
 *
 * @author Epoint
 * @version [版本号, 2023-11-08 18:25:06]
 */
@RightRelation(com.epoint.xmz.thirdreporteddata.audittaskzj.action.SpglKcsjryxxbV3ListAction.class)
@RestController("spglkcsjryxxbv3detailaction")
@Scope("request")
public class SpglKcsjryxxbV3DetailAction extends BaseController
{
    @Autowired
    private ISpglKcsjryxxbV3Service service;

    /**
     * 勘察设计人员信息表实体对象
     */
    private SpglKcsjryxxbV3 dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new SpglKcsjryxxbV3();
        }
    }

    public SpglKcsjryxxbV3 getDataBean() {
        return dataBean;
    }
}
