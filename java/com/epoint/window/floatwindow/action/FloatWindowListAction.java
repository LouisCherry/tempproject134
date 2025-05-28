package com.epoint.window.floatwindow.action;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.window.floatwindow.api.entity.FloatWindow;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.window.floatwindow.api.IFloatWindowService;


/**
 * 飘窗浮窗表list页面对应的后台
 *
 * @author 86183
 * @version [版本号, 2023-07-17 10:15:10]
 */
@RestController("floatwindowlistaction")
@Scope("request")
public class FloatWindowListAction extends BaseController {
    @Autowired
    private IFloatWindowService service;

    /**
     * 飘窗浮窗表实体对象
     */
    private FloatWindow dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<FloatWindow> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 所属站点下拉列表model
     */
    private List<SelectItem> siteModel = null;
    /**
     * 功能类型下拉列表model
     */
    private List<SelectItem> typeModel = null;

    private  String name;
    private  String site;
    private  String type;


    public void pageLoad() {
    }

    public void stop() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            FloatWindow floatWindow = service.find(sel);
            if ("0".equals(floatWindow.getStart())){
                floatWindow.setStart("1");
                addCallbackParam("msg", l("成功启用！"));

            }else {
                floatWindow.setStart("0");
                addCallbackParam("msg", l("成功停用！"));
            }
            service.update(floatWindow);
        }

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

    public DataGridModel<FloatWindow> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<FloatWindow>() {

                @Override
                public List<FloatWindow> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> sqllist = new ArrayList<Object>();
                    String sql =" ";
                    if (StringUtil.isNotBlank(name)) {
                        sql +="and name like '%"+name+"%' ";
                    }
                    if (StringUtil.isNotBlank(site)) {
                        sql +="and site = '"+site+"' ";
                    }
                    if (StringUtil.isNotBlank(type)) {
                        sql +="and type = '"+type+"' ";
                    }

                    List<FloatWindow> list = service.findList(
                            ListGenerator.generateSql("float_window", sql, sortField, sortOrder), first, pageSize,
                            sqllist.toArray());
                    int count = service.countFloatWindow(ListGenerator.generateSql("float_window", sql), sqllist.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public FloatWindow getDataBean() {
        if (dataBean == null) {
            dataBean = new FloatWindow();
        }
        return dataBean;
    }

    public void setDataBean(FloatWindow dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


    public List<SelectItem> getSiteModel() {
        if (siteModel == null) {
            siteModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "所属站点", null, true));
        }
        return this.siteModel;
    }

    public List<SelectItem> getTypeModel() {
        if (typeModel == null) {
            typeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "功能类型", null, true));
        }
        return this.typeModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
