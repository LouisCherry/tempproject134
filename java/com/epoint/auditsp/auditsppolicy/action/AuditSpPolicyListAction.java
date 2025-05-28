package com.epoint.auditsp.auditsppolicy.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditsppolicy.api.IAuditSpPolicyService;
import com.epoint.auditsp.auditsppolicy.api.entity.AuditSpPolicy;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 一件事政策解读list页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:34]
 */
@RestController("auditsppolicylistaction")
@Scope("request")
public class AuditSpPolicyListAction extends BaseController
{
    @Autowired
    private IAuditSpPolicyService service;

    /**
     * 一件事政策解读实体对象
     */
    private AuditSpPolicy dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpPolicy> model;

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

    public DataGridModel<AuditSpPolicy> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpPolicy>()
            {

                @Override
                public List<AuditSpPolicy> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    String businessguid = getRequestParameter("guid");
                    conditionSql += " and businessguid=?";
                    conditionList.add(businessguid);

                    List<AuditSpPolicy> list = service.findList(
                            ListGenerator.generateSql("audit_sp_policy", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countAuditSpPolicy(ListGenerator.generateSql("audit_sp_policy", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpPolicy getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPolicy();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPolicy dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
