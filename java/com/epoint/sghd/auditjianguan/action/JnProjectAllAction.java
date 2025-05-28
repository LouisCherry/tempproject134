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
 * 部门监管展示列表
 *
 * @author swy
 * @version [版本号, 2018年9月20日]
 */
@RestController("jnprojectallaction")
@Scope("request")
public class JnProjectAllAction extends BaseController {

    private static final long serialVersionUID = 1L;

    /**
     * 表格控件model
     * 应付监理
     */
    private DataGridModel<Record> model;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IGxhAuditJianguan gxhAuditJianguan;

    /**
     * 办结时间
     */
    private String banjiedateStart;

    private String banjiedateEnd;
    private String flowsn;
    private String ouname;
    private String applyername;
    private String projectname;
    private String renlingtype;
    private String renlingusername;
    private String renlingdateStart;
    private String renlingdateEnd;

    private ExportModel exportModel;

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
                    record.set("banjiedateStart", banjiedateStart);
                    record.set("banjiedateEnd", banjiedateEnd);
                    record.set("flowsn", flowsn);
                    record.set("ouname", ouname);
                    record.set("applyername", applyername);
                    record.set("projectname", projectname);
                    record.set("renlingtype", renlingtype);
                    record.set("renlingusername", renlingusername);
                    record.set("renlingdateStart", renlingdateStart);
                    record.set("renlingdateEnd", renlingdateEnd);
                    PageData<Record> pageData = gxhAuditJianguan.getAuditJianguanPageData(record, first, pageSize, sortField, sortOrder);
                    List<Record> list = pageData.getList();
                    if (!list.isEmpty()) {
                        for (Record auditjianguan : list) {
                            if ("0".equals(auditjianguan.getStr("renlingtype"))) {
                                auditjianguan.set("renlingtype", "未认领");
                            } else if ("1".equals(auditjianguan.getStr("renlingtype"))) {
                                auditjianguan.set("renlingtype", "已认领");
                            }
                        }
                    }

                    setRowCount(pageData.getRowCount());
                    return list;
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
                    "flowsn,projectname,ouname,applyername,banjiedate,renlingtype,renlingdate,renlingusername",
                    "办件编号,事项名称,审批部门,申请人,办结时间,认领状态,认领时间,认领人");
        }
        return exportModel;
    }

    public String getBanjiedateStart() {
        return banjiedateStart;
    }

    public void setBanjiedateStart(String banjiedateStart) {
        this.banjiedateStart = banjiedateStart;
    }

    public String getBanjiedateEnd() {
        return banjiedateEnd;
    }

    public void setBanjiedateEnd(String banjiedateEnd) {
        this.banjiedateEnd = banjiedateEnd;
    }

    public String getFlowsn() {
        return flowsn;
    }

    public void setFlowsn(String flowsn) {
        this.flowsn = flowsn;
    }

    public String getOuname() {
        return ouname;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
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

    public String getRenlingtype() {
        return renlingtype;
    }

    public void setRenlingtype(String renlingtype) {
        this.renlingtype = renlingtype;
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
