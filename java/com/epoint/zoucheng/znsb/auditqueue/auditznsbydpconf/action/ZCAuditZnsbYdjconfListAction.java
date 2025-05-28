package com.epoint.zoucheng.znsb.auditqueue.auditznsbydpconf.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditqueue.auditznsbydpconf.domain.AuditZnsbYdpconf;
import com.epoint.basic.auditqueue.auditznsbydpconf.inter.IAuditZnsbYdpconfService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@RestController("zcauditznsbydjconflistaction")
@Scope("request")
public class ZCAuditZnsbYdjconfListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditZnsbYdpconfService service;

    /**
     * 引导屏页面配置表实体对象
     */
    private AuditZnsbYdpconf dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditZnsbYdpconf> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    public void pageLoad() {
        addCallbackParam("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            service.delete(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditZnsbYdpconf> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditZnsbYdpconf>()
            {

                @Override
                public List<AuditZnsbYdpconf> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
                    PageData<AuditZnsbYdpconf> pageData = service
                            .getFileByPage(sql.getMap(), first, pageSize, "OperateDate", "desc").getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public AuditZnsbYdpconf getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditZnsbYdpconf();
        }
        return dataBean;
    }

    public void setDataBean(AuditZnsbYdpconf dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

}
