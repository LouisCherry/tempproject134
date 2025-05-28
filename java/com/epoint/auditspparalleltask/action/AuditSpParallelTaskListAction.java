package com.epoint.auditspparalleltask.action;

import com.epoint.auditspparalleltask.api.IAuditSpParallelTaskService;
import com.epoint.auditspparalleltask.api.entity.AuditSpParallelTask;
import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.StringUtil;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 并行阶段阶段事项配置表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:18:40]
 */
@RestController("auditspparalleltasklistaction")
@Scope("request")
public class AuditSpParallelTaskListAction extends BaseController
{
    @Autowired
    private IAuditSpParallelTaskService service;
    @Autowired
    private IAuditSpPhaseBaseinfoService iAuditSpPhaseBaseinfoService;

    /**
     * 并行阶段阶段事项配置表实体对象
     */
    private AuditSpParallelTask dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpParallelTask> model;

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

    public DataGridModel<AuditSpParallelTask> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpParallelTask>()
            {

                @Override
                public List<AuditSpParallelTask> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<AuditSpParallelTask> list = service.findList(
                            ListGenerator.generateSql("AUDIT_SP_PARALLEL_TASK", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditSpParallelTask(
                            ListGenerator.generateSql("AUDIT_SP_PARALLEL_TASK", conditionSql), conditionList.toArray());

                    for (AuditSpParallelTask auditSpParallelTask : list) {
                        String[] phaseguids = auditSpParallelTask.getPhaseguid().split(",");
                        List<String> arrList = Arrays.asList(phaseguids);
                        List<String> phasename = new ArrayList<>();
                        if (!arrList.isEmpty()) {
                            for (String string : arrList) {
                                AuditSpPhaseBaseinfo find = iAuditSpPhaseBaseinfoService.find(string);
                                if (find != null) {
                                    phasename.add(find.getPhasename());
                                }
                            }
                        }
                        auditSpParallelTask.setPhaseguid(StringUtil.join(phasename, ","));

                    }

                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpParallelTask getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpParallelTask();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpParallelTask dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
