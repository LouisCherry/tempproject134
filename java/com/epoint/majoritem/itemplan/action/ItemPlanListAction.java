package com.epoint.majoritem.itemplan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.majoritem.itemplan.api.IItemPlanService;
import com.epoint.majoritem.itemplan.api.entity.ItemPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 重点项目项目进度表list页面对应的后台
 *
 * @author 19273
 * @version [版本号, 2024-07-09 15:05:52]
 */
@RestController("itemplanlistaction")
@Scope("request")
public class ItemPlanListAction extends BaseController {
    @Autowired
    private IItemPlanService service;

    /**
     * 重点项目项目进度表实体对象
     */
    private ItemPlan dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ItemPlan> model;

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

    public DataGridModel<ItemPlan> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ItemPlan>() {

                @Override
                public List<ItemPlan> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<ItemPlan> list = service.findList(
                            ListGenerator.generateSql("item_plan", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countItemPlan(ListGenerator.generateSql("item_plan", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
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

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
