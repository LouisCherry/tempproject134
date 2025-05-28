package com.epoint.majoritem.itemplan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.majoritem.itemplan.api.IItemPlanService;
import com.epoint.majoritem.itemplan.api.entity.ItemPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 重点项目项目进度表详情页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:05:52]
 */
@RightRelation(ItemPlanListAction.class)
@RestController("itemplandetailaction")
@Scope("request")
public class ItemPlanDetailAction extends BaseController {
    @Autowired
    private IItemPlanService service;

    /**
     * 重点项目项目进度表实体对象
     */
    private ItemPlan dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ItemPlan();
        }
    }


    public ItemPlan getDataBean() {
        return dataBean;
    }
}