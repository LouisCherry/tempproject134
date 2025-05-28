package com.epoint.majoritem.itemzczctype.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.majoritem.itemzczctype.api.IItemZczcTypeService;
import com.epoint.majoritem.itemzczctype.api.entity.ItemZczcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 重点项目政策支持类型表新增页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:06:03]
 */
@RestController("itemzczctypeaddaction")
@Scope("request")
public class ItemZczcTypeAddAction extends BaseController {
    @Autowired
    private IItemZczcTypeService service;
    /**
     * 重点项目政策支持类型表实体对象
     */
    private ItemZczcType dataBean = null;

    /**
     * 政策支持类型下拉列表model
     */
    private List<SelectItem> zczclxModel = null;
    /**
     * 统计年份下拉列表model
     */
    private List<SelectItem> tjyearModel = null;
    private String itemguid = "";

    public void pageLoad() {
        itemguid = getRequestParameter("itemguid");
        dataBean = new ItemZczcType();
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
        dataBean = new ItemZczcType();
    }

    public ItemZczcType getDataBean() {
        if (dataBean == null) {
            dataBean = new ItemZczcType();
        }
        return dataBean;
    }

    public void setDataBean(ItemZczcType dataBean) {
        this.dataBean = dataBean;
    }

    public List<SelectItem> getZczclxModel() {
        if (zczclxModel == null) {
            zczclxModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目政策支持类型", null, false));
        }
        return this.zczclxModel;
    }

    public List<SelectItem> getTjyearModel() {
        if (tjyearModel == null) {
            tjyearModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "重点项目年份", null, false));
        }
        return this.tjyearModel;
    }

}
