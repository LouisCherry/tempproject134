package com.epoint.expert.experttask.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.expert.experttask.api.IExpertTaskService;
import com.epoint.expert.experttask.api.entity.ExpertTask;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 专家关联事项表list页面对应的后台
 * 
 * @author cqsong
 * @version [版本号, 2019-08-21 16:36:57]
 */
@RestController("experttasklistaction")
@Scope("request")
public class ExpertTaskListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = -859180190244662150L;

    @Autowired
    private IExpertTaskService service;

    /**
     * 专家关联事项表实体对象
     */
    private ExpertTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<ExpertTask> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

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

    public DataGridModel<ExpertTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<ExpertTask>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = -946096408481403349L;

                @Override
                public List<ExpertTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<ExpertTask> list = service.findList(
                            ListGenerator.generateSql("Expert_Task", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service
                            .findList(ListGenerator.generateSql("Expert_Task", conditionSql, sortField, sortOrder),
                                    conditionList.toArray())
                            .size();
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public ExpertTask getDataBean() {
        if (dataBean == null) {
            dataBean = new ExpertTask();
        }
        return dataBean;
    }

    public void setDataBean(ExpertTask dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
