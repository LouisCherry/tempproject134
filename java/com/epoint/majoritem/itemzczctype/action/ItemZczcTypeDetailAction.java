package com.epoint.majoritem.itemzczctype.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.controller.RightRelation;
import com.epoint.majoritem.itemzczctype.api.IItemZczcTypeService;
import com.epoint.majoritem.itemzczctype.api.entity.ItemZczcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

/**
 * 重点项目政策支持类型表详情页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:06:03]
 */
@RightRelation(ItemZczcTypeListAction.class)
@RestController("itemzczctypedetailaction")
@Scope("request")
public class ItemZczcTypeDetailAction extends BaseController {
    @Autowired
    private IItemZczcTypeService service;

    /**
     * 重点项目政策支持类型表实体对象
     */
    private ItemZczcType dataBean = null;

    public void pageLoad() {
        String guid = getRequestParameter("guid");
        dataBean = service.find(guid);
        if (dataBean == null) {
            dataBean = new ItemZczcType();
        }
    }


    public ItemZczcType getDataBean() {
        return dataBean;
    }
}