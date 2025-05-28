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
import java.util.UUID;

/**
 * 首页弹窗表新增页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-11-08 15:36:45]
 */
@RightRelation(HomepageNoticeListAction.class)
@RestController("homepagenoticeaddaction")
@Scope("request")
public class HomepageNoticeAddAction extends BaseController {
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
        dataBean = new HomepageNotice();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setReleasetime(new Date());
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new HomepageNotice();
    }

    public HomepageNotice getDataBean() {
        if (dataBean == null) {
            dataBean = new HomepageNotice();
        }
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

    public void show(){
        String isshow = "0";
        //得到最新一条记录，看他是否展示
        HomepageNotice homepageNotice = service.getLatestHomepage();
        if(homepageNotice != null){
            isshow = homepageNotice.getIs_show();
        }
        addCallbackParam("isshow",isshow);
    }

}
