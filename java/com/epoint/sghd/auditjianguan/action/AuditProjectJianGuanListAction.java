package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmonitor.domain.AuditProjectMonitor;
import com.epoint.basic.auditproject.auditprojectmonitor.inter.IAuditProjectMonitor;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 部门办件监管页面对应的action
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("auditprojectjianguanlistaction")
@Scope("request")
public class AuditProjectJianGuanListAction extends BaseController {

    private static final long serialVersionUID = -4046499456177644472L;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectMonitor> model = null;

    /**
     * 督办时间
     */
    private String supervisedateStart;
    private String supervisedateEnd;
    private String searchProjectname;
    private String isAll = "1";

    private String isMyMonitor;

    private String monitorPerson;

    @Autowired
    private IAuditProjectMonitor monitorService;

    @Autowired
    private IAuditProject projectService;

    private List<SelectItem> alerttypeModel = null;

    private String alertType;

    @Override
    public void pageLoad() {
        isMyMonitor = getRequestParameter("isMyMonitor");

    }

    public DataGridModel<AuditProjectMonitor> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectMonitor>() {

                @Override
                public List<AuditProjectMonitor> fetchData(int first, int pageSize, String sortField,
                                                           String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(supervisedateStart)) {
                        sql.ge("supervise_date", EpointDateUtil.getBeginOfDateStr(supervisedateStart));
                    }
                    if (StringUtil.isNotBlank(searchProjectname)) {
                        sql.like("projectname", searchProjectname);
                    }
                    if (StringUtil.isNotBlank(supervisedateEnd)) {
                        sql.le("supervise_date", EpointDateUtil.getEndOfDateStr(supervisedateEnd));
                    }

                    if (!"1".equals(isAll)) {
                        sql.eq("SUPERVISE_ID", userSession.getUserGuid());
                    }

                    if ("1".equals(isMyMonitor)) {
                        sql.eq("monitor_id", userSession.getUserGuid());
                    }

                    if (StringUtil.isNotBlank(monitorPerson)) {
                        sql.like("monitor_person", monitorPerson.trim());
                    }

                    //部门编码
                    String ouguid = userSession.getBaseOUGuid();
                    if (StringUtil.isNotBlank(ouguid)) {
                        sql.eq("MONITOR_ORG_ID", ouguid);
                    }

                    //已办结
                    if ("03".equals(alertType)) {
                        sql.isNotBlank("end_date");
                    }
                    //已反馈
                    if ("02".equals(alertType)) {
                        sql.isBlank("end_date");
                        sql.isNotBlank("firstreplydate");
                    }
                    //未反馈
                    if ("01".equals(alertType)) {
                        sql.isBlank("firstreplydate");
                    }
                    sql.eq("AREACODE", ZwfwUserSession.getInstance().getAreaCode());
                    sortField = "supervise_date";
                    sortOrder = "desc";
                    PageData<AuditProjectMonitor> pageData = monitorService
                            .getMonitorPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();

                    for (AuditProjectMonitor projectMonitor : pageData.getList()) {
                        String fields = "  rowguid,taskguid,projectname,pviguid,applyername,ouname";
                        AuditProject auditProject = projectService.getAuditProjectByFlowsn(fields,
                                projectMonitor.getBj_no(), projectMonitor.getAreacode()).getResult();
                        if (projectMonitor.getFirstreplydate() == null) {
                            projectMonitor.put("resultType", "未反馈");
                        }
                        if (projectMonitor.getEnd_date() != null) {
                            projectMonitor.put("resultType", "已办结");
                        }
                        if (projectMonitor.getFirstreplydate() != null && projectMonitor.getEnd_date() == null) {
                            projectMonitor.put("resultType", "已反馈");
                        }
                        if (auditProject != null) {
                            projectMonitor.put("projectguid", auditProject.getRowguid());
                            projectMonitor.put("projectname", auditProject.getProjectname());
                            projectMonitor.put("pviguid", auditProject.getPviguid());
                            projectMonitor.put("applyername", auditProject.getApplyername());
                            projectMonitor.put("ouname", auditProject.getOuname());
                        }
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getAlerttypeModel() {
        if (this.alerttypeModel == null) {
            this.alerttypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "反馈状态", (String) null, true));
        }

        return this.alerttypeModel;
    }

    public String getSupervisedateStart() {
        return supervisedateStart;
    }

    public void setSupervisedateStart(String supervisedateStart) {
        this.supervisedateStart = supervisedateStart;
    }

    public String getSupervisedateEnd() {
        return supervisedateEnd;
    }

    public void setSupervisedateEnd(String supervisedateEnd) {
        this.supervisedateEnd = supervisedateEnd;
    }

    public String getIsAll() {
        return isAll;
    }

    public void setIsAll(String isAll) {
        this.isAll = isAll;
    }

    public String getMonitorPerson() {
        return monitorPerson;
    }

    public void setMonitorPerson(String monitorPerson) {
        this.monitorPerson = monitorPerson;
    }

    public String getSearchProjectname() {
        return searchProjectname;
    }

    public void setSearchProjectname(String searchProjectname) {
        this.searchProjectname = searchProjectname;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

}
