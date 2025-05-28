package com.epoint.auditsppolicybasicinfo.action;

import com.epoint.auditsppolicybasicinfo.api.IAuditSpPolicyBasicinfoService;
import com.epoint.auditsppolicybasicinfo.api.entity.AuditSpPolicyBasicinfo;
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
 * 政策信息表list页面对应的后台
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:26:17]
 */
@RestController("auditsppolicybasicinfolistaction")
@Scope("request")
public class AuditSpPolicyBasicinfoListAction extends BaseController
{
    @Autowired
    private IAuditSpPolicyBasicinfoService service;

    /**
     * 政策信息表实体对象
     */
    private AuditSpPolicyBasicinfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditSpPolicyBasicinfo> model;

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

    public DataGridModel<AuditSpPolicyBasicinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditSpPolicyBasicinfo>()
            {

                @Override
                public List<AuditSpPolicyBasicinfo> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    List<Object> conditionList = new ArrayList<Object>();
                    String conditionSql = ListGenerator.getSearchSql(getRequestContext().getComponents(),
                            conditionList);
                    List<AuditSpPolicyBasicinfo> list = service.findList(
                            ListGenerator.generateSql("AUDIT_SP_POLICY_BASICINFO", conditionSql, sortField, sortOrder),
                            first, pageSize, conditionList.toArray());
                    int count = service.countAuditSpPolicyBasicinfo(
                            ListGenerator.generateSql("AUDIT_SP_POLICY_BASICINFO", conditionSql),
                            conditionList.toArray());
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }

    public AuditSpPolicyBasicinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditSpPolicyBasicinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditSpPolicyBasicinfo dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
