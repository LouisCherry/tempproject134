package com.epoint.expert.expertisms.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.expert.expertisms.api.IExpertISmsService;
import com.epoint.expert.expertisms.api.entity.ExpertISms;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 专家抽取短信发送表list页面对应的后台
 * 
 * @author Lee
 * @version [版本号, 2019-08-21 15:42:10]
 */
@RestController("expertismslistaction")
@Scope("request")
public class ExpertISmsListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -2640329518268838121L;

    @Autowired
    private IExpertISmsService service;

    /**
     * 专家抽取短信发送表实体对象
     */
    private ExpertISms dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertISms> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 专家抽取实例guid
     */
    private String instanceGuid = null;

    public void pageLoad() {
        instanceGuid = request.getParameter("instanceGuid");
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

    public DataGridModel<ExpertISms> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertISms>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public List<ExpertISms> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    // 查询条件
                    conditionSql = conditionSql + " and InstanceGuid=?";
                    conditionList.add(instanceGuid);

                    List<ExpertISms> list = service.findList(
                            ListGenerator.generateSql("Expert_I_SMS", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service
                            .findList(ListGenerator.generateSql("Expert_I_SMS", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public ExpertISms getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertISms();
        }
        return dataBean;
    }

    public void setDataBean(ExpertISms dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
