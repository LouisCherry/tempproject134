package com.epoint.auditspphasetask.action;

import com.epoint.auditspphasegroup.api.IAuditSpPhaseGroupService;
import com.epoint.auditspphasegroup.api.entity.AuditSpPhaseGroup;
import com.epoint.auditspphasetask.api.IAuditSpPhaseTaskService;
import com.epoint.auditspphasetask.api.entity.AuditSpPhaseTask;
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
 * 前四阶段事项配置表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:06:22]
 */
@RestController("auditspphasetasklistaction")
@Scope("request")
public class AuditSpPhaseTaskListAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseTaskService service;

    @Autowired
    private IAuditSpPhaseGroupService iAuditSpPhaseGroupService;

    /**
     * 前四阶段事项配置表实体对象
     */
    private AuditSpPhaseTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpPhaseTask> model;

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

    public DataGridModel<AuditSpPhaseTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpPhaseTask>()
            {

                @Override
                public List<AuditSpPhaseTask> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<AuditSpPhaseTask> list = service.findList(
                            ListGenerator.generateSql("AUDIT_SP_PHASE_TASK", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countAuditSpPhaseTask(
                            ListGenerator.generateSql("AUDIT_SP_PHASE_TASK", conditionSql), conditionList.toArray());
                    for (AuditSpPhaseTask auditSpPhaseTask : list) {
                        AuditSpPhaseGroup find = iAuditSpPhaseGroupService.find(auditSpPhaseTask.getGroupguid());
                        if (find != null) {
                            auditSpPhaseTask.setGroupguid(find.getGroupname());
                        }
                    }
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpPhaseTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPhaseTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseTask dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
