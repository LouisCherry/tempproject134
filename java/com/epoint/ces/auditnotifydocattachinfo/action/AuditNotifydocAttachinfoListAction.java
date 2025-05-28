package com.epoint.ces.auditnotifydocattachinfo.action;

import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import com.epoint.ces.auditnotifydocattachinfo.api.entity.AuditNotifydocAttachinfo;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.core.dto.model.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import com.epoint.ces.auditnotifydocattachinfo.api.IAuditNotifydocAttachinfoService;


/**
 * 办件文书信息表list页面对应的后台
 *
 * @author jiem
 * @version [版本号, 2022-03-15 14:02:49]
 */
@RestController("auditnotifydocattachinfolistaction")
@Scope("request")
public class AuditNotifydocAttachinfoListAction extends BaseController {
    @Autowired
    private IAuditNotifydocAttachinfoService service;
    @Autowired
    private IAttachService iAttachService;

    /**
     * 办件文书信息表实体对象
     */
    private AuditNotifydocAttachinfo dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditNotifydocAttachinfo> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 文书类别下拉列表model
     */
    private List<SelectItem> doctypeModel = null;


    @Override
    public void pageLoad() {
    }


    /**
     * 删除选定
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            iAttachService.deleteAttachByGuid(sel);
            service.deleteByGuid(sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditNotifydocAttachinfo> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditNotifydocAttachinfo>() {

                @Override
                public List<AuditNotifydocAttachinfo> fetchData(int first, int pageSize, String sortField, String sortOrder) {

                    SqlConditionUtil sqlUtil = new SqlConditionUtil();
                    if (dataBean != null){
                        if (StringUtil.isNotBlank(dataBean.getDocname())){
                            sqlUtil.like("docname", dataBean.getDocname());
                        }
                        if (StringUtil.isNotBlank(dataBean.getDoctype())){
                            sqlUtil.eq("doctype", dataBean.getDoctype());
                        }
                    }
                    SQLManageUtil sqlManageUtil = new SQLManageUtil();
                    List<AuditNotifydocAttachinfo> list = service.findPage(sqlManageUtil.buildSqlComoplete(AuditNotifydocAttachinfo.class, sqlUtil.getMap()),first,pageSize);
                    int count = service.countAuditNotifydocAttachinfo(sqlManageUtil.buildSql(sqlUtil.getMap()));
                    this.setRowCount(count);
                    return list;
                }

            };
        }
        return model;
    }


    public AuditNotifydocAttachinfo getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditNotifydocAttachinfo();
        }
        return dataBean;
    }

    public void setDataBean(AuditNotifydocAttachinfo dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }


    public List<SelectItem> getDoctypeModel() {
        if (doctypeModel == null) {
            doctypeModel = DataUtil.convertMap2ComboBox((List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "事项文书类别", null, true));
        }
        return this.doctypeModel;
    }

}
