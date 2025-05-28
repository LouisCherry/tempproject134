package com.epoint.auditsprelationtask.action;

import com.epoint.auditsprelationtask.api.IAuditSpRelationTaskService;
import com.epoint.auditsprelationtask.api.entity.AuditSpRelationTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 前四阶段事项关系表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:13:27]
 */
@RestController("auditsprelationtasklistaction")
@Scope("request")
public class AuditSpRelationTaskListAction extends BaseController
{
    @Autowired
    private IAuditSpRelationTaskService service;

    /**
     * 前四阶段事项关系表实体对象
     */
    private AuditSpRelationTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpRelationTask> model;

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

    public DataGridModel<AuditSpRelationTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpRelationTask>()
            {

                @Override
                public List<AuditSpRelationTask> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<AuditSpRelationTask> list = service.findList(
                            ListGenerator.generateSql("AUDIT_SP_RELATION_TASK", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditSpRelationTask(
                            ListGenerator.generateSql("AUDIT_SP_RELATION_TASK", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpRelationTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpRelationTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpRelationTask dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
