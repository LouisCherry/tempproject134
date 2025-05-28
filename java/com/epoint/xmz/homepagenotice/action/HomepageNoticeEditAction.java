package com.epoint.xmz.homepagenotice.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.xmz.homepagenotice.api.IHomepageNoticeService;
import com.epoint.xmz.homepagenotice.api.entity.HomepageNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 首页弹窗表修改页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-11-08 15:36:46]
 */
@RightRelation(HomepageNoticeListAction.class)
@RestController("homepagenoticeeditaction")
@Scope("request")
public class HomepageNoticeEditAction extends BaseController {

    @Autowired
    private IHomepageNoticeService service;

    /**
     * 首页弹窗表实体对象
     */
    private HomepageNotice dataBean = null;

    /**
     * 是否展示单选按钮组model
     */
    private List<SelectItem> is_showModel = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new HomepageNotice();
        }
    }

    /**
     * 保存修改
     */
    public void save() {
        dataBean.setOperatedate(new Date());
        service.update(dataBean);
        addCallbackParam("msg", l("修改成功") + "！");
    }

    public HomepageNotice getDataBean() {
        return dataBean;
    }

    public void setDataBean(HomepageNotice dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getIs_showModel() {
        if (is_showModel == null) {
            is_showModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否", null, false));
        }
        return this.is_showModel;
    }

}
