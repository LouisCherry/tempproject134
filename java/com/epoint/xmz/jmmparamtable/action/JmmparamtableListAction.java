package com.epoint.xmz.jmmparamtable.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.xmz.jmmparamtable.api.IJmmparamtableService;
import com.epoint.xmz.jmmparamtable.api.entity.Jmmparamtable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 居民码市县参数配置表list页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2023-06-26 14:34:18]
 */
@RestController("jmmparamtablelistaction")
@Scope("request")
public class JmmparamtableListAction extends BaseController {
    @Autowired
    private IJmmparamtableService service;

    /**
     * 居民码市县参数配置表实体对象
     */
    private Jmmparamtable dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<Jmmparamtable> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 市县名称下拉列表model
     */
    private List<SelectItem> areanameModel = null;


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

    public DataGridModel<Jmmparamtable> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Jmmparamtable>() {

                @Override
                public List<Jmmparamtable> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(), conditionList);
                    List<Jmmparamtable> list = service.findList(
                            ListGenerator.generateSql("jmmparamtable", conditionSql, sortField, sortOrder), first, pageSize,
                            conditionList.toArray());
                    int count = service.countJmmparamtable(ListGenerator.generateSql("jmmparamtable", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public Jmmparamtable getDataBean() {
        if (dataBean == null) {
            dataBean = new Jmmparamtable();
        }
        return dataBean;
    }

    public void setDataBean(Jmmparamtable dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


    public List<SelectItem> getAreanameModel() {
        if (areanameModel == null) {
            areanameModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "辖区对应关系", null, true));
        }
        return this.areanameModel;
    }

}
