package com.epoint.xmz.homepagenotice.action;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.xmz.homepagenotice.api.entity.HomepageNotice;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.xmz.homepagenotice.api.IHomepageNoticeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 首页弹窗表详情页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-11-08 15:36:46]
 */
@RightRelation(HomepageNoticeListAction.class)
@RestController("homepagenoticedetailaction")
@Scope("request")
public class HomepageNoticeDetailAction extends BaseController {
    @Autowired
    private IHomepageNoticeService service;

    /**
     * 首页弹窗表实体对象
     */
    private HomepageNotice dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new HomepageNotice();
        }
    }


    public HomepageNotice getDataBean() {
        return dataBean;
    }
}