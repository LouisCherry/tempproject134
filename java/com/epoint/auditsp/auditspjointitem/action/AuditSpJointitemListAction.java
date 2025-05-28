package com.epoint.auditsp.auditspjointitem.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditsp.auditspjointitem.api.IAuditSpJointitemService;
import com.epoint.auditsp.auditspjointitem.api.entity.AuditSpJointitem;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.frame.service.metadata.mis.util.ListGenerator;

/**
 * 一件事联办事项list页面对应的后台
 * 
 * @author zyq
 * @version [版本号, 2024-12-02 17:15:40]
 */
@RestController("auditspjointitemlistaction")
@Scope("request")
public class AuditSpJointitemListAction extends BaseController
{
    @Autowired
    private IAuditSpJointitemService service;

    /**
     * 一件事联办事项实体对象
     */
    private AuditSpJointitem dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpJointitem> model;

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

    public DataGridModel<AuditSpJointitem> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpJointitem>()
            {

                @Override
                public List<AuditSpJointitem> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);

                    String businessguid = getRequestParameter("guid");
                    conditionSql += " and businessguid=?2";
                    conditionList.add(businessguid);

                    List<AuditSpJointitem> list = service.findList(
                            ListGenerator.generateSql("audit_sp_jointitem", conditionSql, sortField, sortOrder), first,
                            pageSize, conditionList.toArray());
                    int count = service.countAuditSpJointitem(
                            ListGenerator.generateSql("audit_sp_jointitem", conditionSql), conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpJointitem getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpJointitem();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpJointitem dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
