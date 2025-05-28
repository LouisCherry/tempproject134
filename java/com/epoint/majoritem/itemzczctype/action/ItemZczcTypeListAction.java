package com.epoint.majoritem.itemzczctype.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.majoritem.itemzczctype.api.IItemZczcTypeService;
import com.epoint.majoritem.itemzczctype.api.entity.ItemZczcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 重点项目政策支持类型表list页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:06:03]
 */
@RestController("itemzczctypelistaction")
@Scope("request")
public class ItemZczcTypeListAction extends BaseController {
    @Autowired
    private IItemZczcTypeService service;

    /**
     * 重点项目政策支持类型表实体对象
     */
    private ItemZczcType dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ItemZczcType> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;


    public void pageLoad() {
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<ItemZczcType> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ItemZczcType>() {

                @Override
                public List<ItemZczcType> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<ItemZczcType> list = service.findList(
                            ListGenerator.generateSql("item_zczc_type", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countItemZczcType(ListGenerator.generateSql("item_zczc_type", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
