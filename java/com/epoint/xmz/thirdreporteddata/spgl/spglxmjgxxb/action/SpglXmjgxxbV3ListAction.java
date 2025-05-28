package com.epoint.xmz.thirdreporteddata.spgl.spglxmjgxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjgxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglXmjgxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目监管信息表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:32]
 */
@RestController("spglxmjgxxbv3listaction")
@Scope("request")
public class SpglXmjgxxbV3ListAction extends BaseController {
    @Autowired
    private ISpglXmjgxxbV3Service service;

    /**
     * 项目监管信息表实体对象
     */
    private SpglXmjgxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglXmjgxxbV3> model;

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

    public DataGridModel<SpglXmjgxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglXmjgxxbV3>() {

                @Override
                public List<SpglXmjgxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<SpglXmjgxxbV3> list = service.findList(
                            ListGenerator.generateSql("SPGL_XMJGXXB_V3", conditionSql, "lsh", "asc"), first, pageSize,
                            conditionList.toArray());
                    int count = service.countSpglXmjgxxbV3(ListGenerator.generateSql("SPGL_XMJGXXB_V3", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public SpglXmjgxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglXmjgxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglXmjgxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("lsh", "流水号");
        }
        return exportModel;
    }


}
