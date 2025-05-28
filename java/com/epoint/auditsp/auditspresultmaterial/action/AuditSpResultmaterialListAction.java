package com.epoint.auditsp.auditspresultmaterial.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspresultmaterial.api.IAuditSpResultmaterialService;
import com.epoint.auditsp.auditspresultmaterial.api.entity.AuditSpResultmaterial;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 一件事结果材料list页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:53]
 */
@RestController("auditspresultmateriallistaction")
@Scope("request")
public class AuditSpResultmaterialListAction extends BaseController
{
    @Autowired
    private IAuditSpResultmaterialService service;

    /**
     * 一件事结果材料实体对象
     */
    private AuditSpResultmaterial dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpResultmaterial> model;

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

    public DataGridModel<AuditSpResultmaterial> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpResultmaterial>()
            {

                @Override
                public List<AuditSpResultmaterial> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    String businessguid = getRequestParameter("guid");
                    conditionSql += " and businessguid=?";
                    conditionList.add(businessguid);

                    List<AuditSpResultmaterial> list = service.findList(
                            ListGenerator.generateSql("audit_sp_resultmaterial", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditSpResultmaterial(
                            ListGenerator.generateSql("audit_sp_resultmaterial", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpResultmaterial getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpResultmaterial();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpResultmaterial dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
