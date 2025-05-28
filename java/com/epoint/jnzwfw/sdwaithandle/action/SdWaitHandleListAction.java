package com.epoint.jnzwfw.sdwaithandle.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 属地代办列表
 * @作者 17614
 * @version [版本号, 2019年5月23日]
 */
@RestController("sdwaithandlelistaction")
@Scope("request")
public class SdWaitHandleListAction extends BaseController implements Serializable
{

    private static final long serialVersionUID = 1L;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IWFInstanceAPI9 wfi;

    @Autowired
    private IWorkflowActivityService wfaService;

    @Autowired
    private IAuditTask taskService;

    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;
    /**
     * 从ZwfwUserSession中获取的windowguid
     */
    private String windowguid;
    /**
     * 申请人查询条件
     */
    private String applyerName;
    /**
     * 接件时间
     */
    private String dateStart;
    private String dateEnd;

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                   List<AuditProject> list = new ArrayList<>();
                    if (StringUtil.isNotBlank(windowguid)) {
                        Date datestart = null;
                        Date dateend = null;
                        //接件开始时间
                        if (StringUtil.isNotBlank(dateStart)) {
                            datestart = EpointDateUtil.getBeginOfDateStr(dateStart);
                        }
                        //接件结束时间
                        if (StringUtil.isNotBlank(dateEnd)) {
                            dateend = EpointDateUtil.getEndOfDateStr(dateEnd);
                        }
                        String areacode = ZwfwUserSession.getInstance().getAreaCode();
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("IS_SAMECITY", ZwfwConstant.CONSTANT_STR_ONE);
                        sqlConditionUtil.eq("areacode", areacode);
                        //已接件
                        sqlConditionUtil.eq("status", ZwfwConstant.BANJIAN_STATUS_YJJ + "");
                        if (datestart != null) {
                            sqlConditionUtil.ge("receivedate", datestart);
                        }
                        if (dateend != null) {
                            sqlConditionUtil.le("receivedate", dateend);
                        }
                        PageData<AuditProject> pageData = iAuditProject.getAuditProjectPageDataByCondition(
                                sqlConditionUtil.getMap(), first, pageSize, "receivedate", "desc").getResult();
                        if (pageData != null && pageData.getList() != null) {
                            for (AuditProject auditProject : pageData.getList()) {
                                // 返回窗口名字
                                auditProject.put("WindowNameField", auditProject.getWindowname());
                                Map<String, String> mapProjectName = handleIsMyproject(auditProject);
                                // 返回办件名称
                                auditProject.put("TaskName",
                                        mapProjectName.get("my")
                                                + "&nbsp;&nbsp;<a  href='javascript:void(0)' onclick='openUrl(\""
                                                + mapProjectName.get("url") + "\",\"" + auditProject.getRowguid()
                                                + "\")'>" + auditProject.getProjectname() + "</a>");

                                if (auditProject.getReceivedate() == null) {
                                    auditProject.put("receivedate", "--");
                                }
                            }
                            this.setRowCount(pageData.getRowCount());
                            return pageData.getList();
                        }
                        else {
                            this.setRowCount(0);
                            return list;
                        }
                    }
                    else {
                        this.setRowCount(0);
                        return list;
                    }
                }
            };
        }
        return model;
    }

    public void hasProGuid(String rowguid) {
        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(rowguid, area).getResult();
        if (auditProject == null) {
            addCallbackParam("message", "办件已删除");
        }
    }

    public Map<String, String> handleIsMyproject(AuditProject auditProject) {
        String pviGuid = auditProject.getPviguid();
        String Taskguid = auditProject.getTaskguid();
        String RowGuid = auditProject.getRowguid();
        Integer Status = auditProject.getStatus();
        String acceptuserguid = auditProject.getAcceptuserguid();

        Map<String, String> map = new HashMap<String, String>(16);
        // 1.获取活动的工作项
        List<WorkflowWorkItem> workflowWorkItems = getActivityWorkflowWorkItem(pviGuid);
        if (workflowWorkItems != null && !workflowWorkItems.isEmpty() && ZwfwConstant.BANJIAN_STATUS_YSL <= Status) {// 存在活动工作项且办件已受理
            //受理后补办显示补办页面
            if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Status) {
                map.put("url", "epointzwfw/auditbusiness/auditproject/auditprojectinfobuzheng?projectguid=" + RowGuid
                        + "&TaskGuid=" + Taskguid + "&ProcessVersionInstanceGuid=" + pviGuid);
                map.put("my", "<span class='text-danger'>窗口办件</span>");
            }
            else {
                map.put("url",
                        "epointzwfw/auditbusiness/auditwindowbusiness/businessmanage/windowauditbanjianinfo?projectguid="
                                + RowGuid + "&ProcessVersionInstanceGuid=" + pviGuid);
                map.put("my", "<span class='text-danger'>窗口办件</span>");
            }
            // 1.1工作项中的处理人是否有“我”
            for (WorkflowWorkItem workflowWorkItem : workflowWorkItems) {
                // 流转到“我”的工作流只允许“我”打开
                if (UserSession.getInstance().getUserGuid().equals(workflowWorkItem.getTransactor())) {
                    map.put("url", workflowWorkItem.getHandleUrl());
                    map.put("my", "<span class='text-danger'>我的办件</span>");
                    break;
                }
            }
        }
        else {// 不存在活动的工作项

            AuditCommonResult<AuditTask> taskResult = taskService.getAuditTaskByGuid(Taskguid, true);
            AuditTask task = taskResult.getResult();
            if (task != null) {
                // map.put("url", null);
                WorkflowActivity activity = wfaService.getFirstActivity(task.getPvguid());
                if (activity != null) {
                    map.put("url", activity.getHandleUrl() + "?processguid=" + task.getProcessguid() + "&taskguid="
                            + Taskguid + "&projectguid=" + RowGuid);
                    map.put("my", "<span class='text-info'>窗口办件</span>");
                }
                else {
                    map.put("url", "epointzwfw/auditbusiness/auditproject/auditprojectinfo?processguid="
                            + task.getProcessguid() + "&taskguid=" + Taskguid + "&projectguid=" + RowGuid);
                    map.put("my", "<span class='text-info'>窗口办件</span>");
                }
                if (UserSession.getInstance().getUserGuid().equals(acceptuserguid)) {
                    map.put("my", "<span class='text-danger'>我的办件</span>");
                }
            }
        }
        return map;

    }

    public List<WorkflowWorkItem> getActivityWorkflowWorkItem(String pviGuid) {
        List<WorkflowWorkItem> selectByPVIGuidAndStatus = null;
        if (StringUtil.isNotBlank(pviGuid)) {
            List<Integer> status = new ArrayList<Integer>();
            status.add(10);
            status.add(20);
            ProcessVersionInstance pVersionInstance = wfi.getProcessVersionInstance(pviGuid);
            if (pVersionInstance != null) {
                selectByPVIGuidAndStatus = wfi.getWorkItemListByPVIGuidAndStatus(pVersionInstance, status);
            }
        }
        return selectByPVIGuidAndStatus;
    }

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

}
