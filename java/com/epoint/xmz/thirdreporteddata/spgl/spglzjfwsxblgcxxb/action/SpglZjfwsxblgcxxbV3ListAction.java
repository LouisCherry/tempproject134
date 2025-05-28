package com.epoint.xmz.thirdreporteddata.spgl.spglzjfwsxblgcxxb.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglZjfwsxblgcxxbV3;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.inter.ISpglZjfwsxblgcxxbV3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 中介服务事项办理过程信息表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2024-04-02 15:25:43]
 */
@RestController("spglzjfwsxblgcxxbv3listaction")
@Scope("request")
public class SpglZjfwsxblgcxxbV3ListAction extends BaseController {
    @Autowired
    private ISpglZjfwsxblgcxxbV3Service service;

    /**
     * 中介服务事项办理过程信息表实体对象
     */
    private SpglZjfwsxblgcxxbV3 dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<SpglZjfwsxblgcxxbV3> model;

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

    public DataGridModel<SpglZjfwsxblgcxxbV3> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<SpglZjfwsxblgcxxbV3>() {

                @Override
                public List<SpglZjfwsxblgcxxbV3> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<SpglZjfwsxblgcxxbV3> list = service.findList(
                            ListGenerator.generateSql("SPGL_ZJFWSXBLGCXXB_V3", conditionSql, "lsh", "asc"), first, pageSize,
                            conditionList.toArray());
                    int count = service.countSpglZjfwsxblgcxxbV3(ListGenerator.generateSql("SPGL_ZJFWSXBLGCXXB_V3", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public SpglZjfwsxblgcxxbV3 getDataBean() {
        if (dataBean == null) {
            dataBean = new SpglZjfwsxblgcxxbV3();
        }
        return dataBean;
    }

    public void setDataBean(SpglZjfwsxblgcxxbV3 dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


}
