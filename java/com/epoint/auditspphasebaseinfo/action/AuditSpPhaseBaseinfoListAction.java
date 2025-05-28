package com.epoint.auditspphasebaseinfo.action;

import com.epoint.auditspphasebaseinfo.api.IAuditSpPhaseBaseinfoService;
import com.epoint.auditspphasebaseinfo.api.entity.AuditSpPhaseBaseinfo;
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
 * 前四阶段信息配置表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@RestController("auditspphasebaseinfolistaction")
@Scope("request")
public class AuditSpPhaseBaseinfoListAction extends BaseController
{
    @Autowired
    private IAuditSpPhaseBaseinfoService service;

    /**
     * 前四阶段信息配置表实体对象
     */
    private AuditSpPhaseBaseinfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpPhaseBaseinfo> model;

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

    public DataGridModel<AuditSpPhaseBaseinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpPhaseBaseinfo>()
            {

                @Override
                public List<AuditSpPhaseBaseinfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<AuditSpPhaseBaseinfo> list = service.findList(
                            ListGenerator.generateSql("AUDIT_SP_PHASE_BASEINFO", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditSpPhaseBaseinfo(
                            ListGenerator.generateSql("AUDIT_SP_PHASE_BASEINFO", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpPhaseBaseinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPhaseBaseinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPhaseBaseinfo dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
