package com.epoint.auditsp.auditspapplymaterial.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspapplymaterial.api.IAuditSpApplymaterialService;
import com.epoint.auditsp.auditspapplymaterial.api.entity.AuditSpApplymaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 一件事申报材料list页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:47]
 */
@RestController("auditspapplymateriallistaction")
@Scope("request")
public class AuditSpApplymaterialListAction extends BaseController
{
    @Autowired
    private IAuditSpApplymaterialService service;

    /**
     * 一件事申报材料实体对象
     */
    private AuditSpApplymaterial dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpApplymaterial> model;

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

    public DataGridModel<AuditSpApplymaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpApplymaterial>()
            {

                @Override
                public List<AuditSpApplymaterial> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    String businessguid = getRequestParameter("guid");
                    conditionSql += " and businessguid=?";
                    conditionList.add(businessguid);

                    List<AuditSpApplymaterial> list = service.findList(
                            ListGenerator.generateSql("audit_sp_applymaterial", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditSpApplymaterial(
                            ListGenerator.generateSql("audit_sp_applymaterial", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpApplymaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpApplymaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpApplymaterial dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
