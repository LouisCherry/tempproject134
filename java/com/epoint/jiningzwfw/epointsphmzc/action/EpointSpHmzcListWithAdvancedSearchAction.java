package com.epoint.jiningzwfw.epointsphmzc.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.epoint.jiningzwfw.epointsphmzc.api.entity.EpointSpHmzc;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.jiningzwfw.epointsphmzc.api.IEpointSpHmzcService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.export.ExportModel;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.basic.faces.util.DataUtil;

/**
 * 惠企政策库list页面对应的后台
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:46]
 */
@RestController("epointsphmzclistwithadvancedsearchaction")
@Scope("request")
public class EpointSpHmzcListWithAdvancedSearchAction extends BaseController
{
    @Autowired
    private IEpointSpHmzcService service;

    /**
     * 惠企政策库实体对象
     */
    private EpointSpHmzc dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<EpointSpHmzc> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
    * 行业规模下拉列表model
    */
    private List<SelectItem> jnhygmModel = null;
    /**
     * 企业标签复选框组model
     */
    private List<SelectItem> qybqModel = null;
    /**
     * 是否四新企业单选按钮组model
     */
    private List<SelectItem> sfsxqyModel = null;
    /**
     * 所属部门下拉列表model
     */
    private List<SelectItem> ssbmModel = null;
    /**
     * 生命周期下拉列表model
     */
    private List<SelectItem> wwsmzqModel = null;

    public void pageLoad() {
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<EpointSpHmzc> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<EpointSpHmzc>()
            {

                @Override
                public List<EpointSpHmzc> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<EpointSpHmzc> list = service.findList(
                            ListGenerator.generateSql("epoint_sp_hmzc", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.findList(ListGenerator.generateSql("epoint_sp_hmzc", conditionSql),
                            conditionList.toArray()).size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public EpointSpHmzc getDataBean() {
        if (dataBean == null) {
            dataBean = new EpointSpHmzc();
        }
        return dataBean;
    }

    public void setDataBean(EpointSpHmzc dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("jnhygm,qybq,sfsxqy,ssbm,wwsmzq,zcmc,zcnr,zczn",
                    "行业规模,企业标签,是否四新企业,所属部门,生命周期,政策名称,政策内容,政策指南");
        }
        return exportModel;
    }

    public List<SelectItem> getJnhygmModel() {
        if (jnhygmModel == null) {
            jnhygmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "行业规模", null, true));
        }
        return this.jnhygmModel;
    }

    public List<SelectItem> getQybqModel() {
        if (qybqModel == null) {
            qybqModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("复选框组", "行业分类", null, true));
        }
        return this.qybqModel;
    }

    public List<SelectItem> getSfsxqyModel() {
        if (sfsxqyModel == null) {
            sfsxqyModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "是否四新企业", null, true));
        }
        return this.sfsxqyModel;
    }

    public List<SelectItem> getSsbmModel() {
        if (ssbmModel == null) {
            ssbmModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "null", null, true));
        }
        return this.ssbmModel;
    }

    public List<SelectItem> getWwsmzqModel() {
        if (wwsmzqModel == null) {
            wwsmzqModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "生命周期", null, true));
        }
        return this.wwsmzqModel;
    }

}
