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

/**
 * 重点项目项目进度表修改页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:05:52]
 */
@RightRelation(ItemPlanListAction.class)
@RestController("itemplaneditaction")
@Scope("request")
public class ItemPlanEditAction extends BaseController {

    @Autowired
    private IItemPlanService service;

    /**
     * 重点项目项目进度表实体对象
     */
    private ItemPlan dataBean = null;

    /**
     * 项目进度下拉列表model
     */
    private List<SelectItem> itemplanModel = null;


    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ItemPlan();
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

    public ItemPlan getDataBean() {
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
