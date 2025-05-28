package com.epoint.auditqueueqhjmodule.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueueqhjmodule.api.IAuditQueueQhjmoduleService;
import com.epoint.auditqueueqhjmodule.entity.AuditQueueQhjmodule;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 取号机大厅模块配置表list页面对应的后台
 * 
 * @author Epoint
 * @version [版本号, 2024-11-14 09:51:07]
 */
@RestController("auditqueueqhjmodulelistaction")
@Scope("request")
public class AuditQueueQhjmoduleListAction extends BaseController
{
    @Autowired
    private IAuditQueueQhjmoduleService service;

    /**
     * 取号机大厅模块配置表实体对象
     */
    private AuditQueueQhjmodule dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditQueueQhjmodule> model;

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
        addCallbackParam("msg", l("成功删除！"));
    }

    public DataGridModel<AuditQueueQhjmodule> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditQueueQhjmodule>()
            {

                @Override
                public List<AuditQueueQhjmodule> fetchData(int first, int pageSize,
                        String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<AuditQueueQhjmodule> list = service
                            .findList(ListGenerator.generateSql("audit_queue_qhjmodule", conditionSql, sortField,
                                    sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditQueueQhjmodule(
                            ListGenerator.generateSql("audit_queue_qhjmodule", conditionSql), conditionList.toArray());
                    this.setRowCount(count);

                    return list;
                }

            };
        }
        return model;
    }

    public AuditQueueQhjmodule getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQueueQhjmodule();
        }
        return dataBean;
    }

    public void setDataBean(AuditQueueQhjmodule dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
