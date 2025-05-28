package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.inter.IGxhAuditJianguan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部门已认领list页面对应的后台
 *应付监理
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnprojectyrlaction")
@Scope("request")
public class JnProjectYrlAction extends BaseController {

    private static final long serialVersionUID = 1L;

    /**
     * 表格控件model
     */
    private DataGridModel<Record> model;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IGxhAuditJianguan gxhAuditJianguan;

    private ExportModel exportModel;

    private String applyername;
    private String projectname;
    private String renlingusername;
    private String renlingdateStart;
    private String renlingdateEnd;

    @Override
    public void pageLoad() {

    }

    public DataGridModel<Record> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<Record>() {

                @Override
                public List<Record> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    // 临时模块
                    Record record = new Record();
                    record.set("applyername", applyername);
                    record.set("projectname", projectname);
                    record.set("projectname", renlingdateStart);
                    record.set("projectname", renlingdateEnd);
                    record.set("renlingtype", "1");
                    PageData<Record> pageData = gxhAuditJianguan.getAuditJianguanPageData(record, first, pageSize, sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }


    public void hasProGuid(String rowguid) {
        String areacode;
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        } else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "flowsn,projectname,ouname,applyername,banjiedate,renlingdate,renling_username",
                    "办件编号,事项名称,审批部门,申请人,办结时间,认领时间,认领人");
        }
        return exportModel;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getRenlingusername() {
        return renlingusername;
    }

    public void setRenlingusername(String renlingusername) {
        this.renlingusername = renlingusername;
    }

    public String getRenlingdateStart() {
        return renlingdateStart;
    }

    public void setRenlingdateStart(String renlingdateStart) {
        this.renlingdateStart = renlingdateStart;
    }

    public String getRenlingdateEnd() {
        return renlingdateEnd;
    }

    public void setRenlingdateEnd(String renlingdateEnd) {
        this.renlingdateEnd = renlingdateEnd;
    }
}
