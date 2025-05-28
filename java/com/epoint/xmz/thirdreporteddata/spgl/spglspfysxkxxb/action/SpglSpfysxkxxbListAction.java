package com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.ISpglSpfysxkxxbService;
import com.epoint.xmz.thirdreporteddata.spgl.spglspfysxkxxb.api.entity.SpglSpfysxkxxb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品房预售信息许可表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:51]
 */
@RestController("spglspfysxkxxblistaction")
@Scope("request")
public class SpglSpfysxkxxbListAction extends BaseController {
    @Autowired
    private ISpglSpfysxkxxbService service;

    /**
     * 商品房预售信息许可表实体对象
     */
    private SpglSpfysxkxxb dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglSpfysxkxxb> model;

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

    public DataGridModel<SpglSpfysxkxxb> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglSpfysxkxxb>() {

                @Override
                public List<SpglSpfysxkxxb> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<SpglSpfysxkxxb> list = service.findList(
                            ListGenerator.generateSql("SPGL_SPFYSXKXXB", conditionSql, "lsh", "asc"), first, pageSize,
                            conditionList.toArray());
                    int count = service.countSpglSpfysxkxxb(ListGenerator.generateSql("SPGL_SPFYSXKXXB", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public SpglSpfysxkxxb getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglSpfysxkxxb();
        }
        return dataBean;
    }

    public void setDataBean(SpglSpfysxkxxb dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
