package com.epoint.majoritem.itemplan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.majoritem.itemplan.api.IItemPlanService;
import com.epoint.majoritem.itemplan.api.entity.ItemPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 重点项目项目进度表新增页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:05:52]
 */
@RightRelation(ItemPlanListAction.class)
@RestController("itemplanaddaction")
@Scope("request")
public class ItemPlanAddAction extends BaseController {
    @Autowired
    private IItemPlanService service;
    /**
     * 重点项目项目进度表实体对象
     */
    private ItemPlan dataBean = null;
    private String itemguid = "";

    /**
     * 项目进度下拉列表model
     */
    private List<SelectItem> itemplanModel = null;


    public void pageLoad() {
        itemguid = getRequestParameter("itemguid");
        dataBean = new ItemPlan();
    }

    /**
     * 保存并关闭
     */
    public void add() {
        dataBean.setRowguid(UUID.randomUUID().toString());
        dataBean.setOperatedate(new Date());
        dataBean.setOperateusername(userSession.getDisplayName());
        dataBean.setItemguid(itemguid);
        service.insert(dataBean);
        addCallbackParam("msg", l("保存成功！"));
        dataBean = null;
    }

    /**
     * 保存并新建
     */
    public void addNew() {
        add();
        dataBean = new ItemPlan();
    }

    public ItemPlan getDataBean() {
        if (dataBean == null) {
            dataBean = new ItemPlan();
        }
        return dataBean;
    }

    public void setDataBean(ItemPlan dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getItemplanModel() {
        if (itemplanModel == null) {
            itemplanModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目项目进度", null, false));
        }
        return this.itemplanModel;
    }

}
