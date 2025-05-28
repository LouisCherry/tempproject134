package com.epoint.jntongji.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.bizlogic.mis.CommonService;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

@RestController("jxquhaomanyiduaction")
@Scope("request")
public class JXquhaomanyiduAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    private DataGridModel<AuditProject> modelall;
    private ExportModel exportModel;
    private String windowguid;
    private String satisfied;
    private String WindowList;
    private String manyiList;
    // 窗口查询
    private List<SelectItem> windownameModel = null;
    private List<SelectItem> manyiModel = null;
    private CommonService commservice = new CommonService();

    public List<SelectItem> getWindowNameModel() {
        if (windownameModel == null) {
            windownameModel = new ArrayList<SelectItem>();
            String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
            String sql = "select RowGuid,WINDOWNAME,WINDOWNO from audit_orga_window " + " where CenterGuid=?"
                    + " order by WINDOWNO desc";
            List<AuditOrgaWindow> list = commservice.findList(sql, AuditOrgaWindow.class, centerguid);
            for (AuditOrgaWindow window : list) {
                windownameModel.add(new SelectItem(window.getRowguid(), window.getWindowname()));
            }
            windownameModel.add(0, new SelectItem("all", "所有窗口"));
        }
        return this.windownameModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getmanyiModel() {
        if (manyiModel == null) {
            manyiModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "满意度", null, false));
        }
        manyiModel.add(0, new SelectItem("all", "所有状态"));
        return this.manyiModel;
    }

    @Override
    public void pageLoad() {

        this.WindowList = getRequestParameter("WindowList");
        this.manyiList = this.getRequestParameter("manyiList");
    }

    @SuppressWarnings("serial")
    public DataGridModel<AuditProject> getDataGridDataTask() {
        // 获得表格对象
        if (modelall == null) {
            modelall = new DataGridModel<AuditProject>()
            {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    String centerguid = ZwfwUserSession.getInstance().getCenterGuid();
                    String strsql = " select (case when b.satisfied = '0' THEN 5 else satisfied END) satisfied,c.WINDOWNO,d.displayname,c.WINDOWNAME,e.TaskTypeName,a.centerguid,f.OUNAME,b.Evaluatedate from audit_queue_history a,audit_online_evaluat b,"
                            + " audit_orga_window c,frame_user d,audit_queue_tasktype e,frame_ou f"
                            + " where a.rowguid = b.ClientIdentifier  and a.HANDLEWINDOWGUID = c.rowguid"
                            + " and a.handleuserguid = d.userguid and a.TASKGUID = e.RowGuid AND d.OUGUID=f.OUGUID and a.CenterGuid='"
                            + centerguid + "'";

                    if (StringUtil.isNotBlank(WindowList) && !"all".equals(WindowList)) {
                        strsql += " and c.RowGuid='" + WindowList + "'";
                    }

                    if (StringUtil.isNotBlank(manyiList) && !"all".equals(manyiList)) {
                        if (manyiList.equals("2")) {
                            strsql += " and b.satisfied in (0,2)";

                        }
                        else {
                            strsql += " and b.satisfied='" + manyiList + "'";
                        }
                    }
                    strsql += " ORDER BY b.Evaluatedate DESC";

                    List<AuditProject> list = commservice.findList(strsql, first, pageSize, AuditProject.class);
                    List<AuditProject> list1 = commservice.findList(strsql, AuditProject.class);
                    this.setRowCount(list1.size());
                    return list;

                }
            };
        }
        return modelall;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("WINDOWNO,TaskTypeName,displayname,WINDOWNAME,OUNAME,Evaluatedate,satisfied",
                    "窗口编码,办理业务,被评价人,被评价窗口,部门,评价时间,满意度");
        }
        return exportModel;
    }

    public String getWindowguid() {
        return windowguid;
    }

    public void setWindowguid(String windowguid) {
        this.windowguid = windowguid;
    }

    public String getSatisfied() {
        return satisfied;
    }

    public void setSatisfied(String satisfied) {
        this.satisfied = satisfied;
    }

    public String getWindowList() {
        return WindowList;
    }

    public void setWindowList(String windowList) {
        WindowList = windowList;
    }

    public String getManyiList() {
        return manyiList;
    }

    public void setManyiList(String manyiList) {
        this.manyiList = manyiList;
    }

}
