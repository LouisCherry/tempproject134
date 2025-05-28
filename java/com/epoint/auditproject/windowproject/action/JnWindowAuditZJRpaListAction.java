package com.epoint.auditproject.windowproject.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 业务管理list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("jnwindowauditzjrpalistaction")
@Scope("request")
public class JnWindowAuditZJRpaListAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    @Autowired
    private IAuditProjectSparetime auditProjectSpareTimeService;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditOrgaWindow windowService;
    
    @Autowired
    private IAuditTask taskService;
    
    @Autowired
    private IWFInstanceAPI9 wfi;

    @Autowired
    private IWorkflowActivityService wfaService;
    
    @Autowired
    private IAuditProject iAuditProject;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 3808213696459481888L;

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    sortField = "applydate";
                    SqlConditionUtil sql = new SqlConditionUtil();
                    String taskids = "";
                    List<String> taskidList = windowService.getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid()).getResult();
                    if(taskidList!=null && !taskidList.isEmpty()){
                    	taskids = "'" + StringUtil.join(taskidList,"','") + "'";
                    }else{
                    	this.setRowCount(0);
                    	return new ArrayList<>();
                    }
                    sql.in("task_id", taskids);
                    String areacode;
                    if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
                    	areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
                    }else{
                    	areacode = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sql.eq("is_rpa", "1");
                    sql.eq("areacode", areacode);
                    sql.in("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));                  
                    if (!dataBean.isEmpty()) {
                        if (!dataBean.getProjectname().isEmpty()) {
                        	sql.like("projectname", dataBean.getProjectname());
                        }
                        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                        	sql.like("applyername", dataBean.getApplyername());
                        }
                    }
                    sql.setSelectFields("rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,tasktype,flowsn,applydate,acceptuserguid");
                    PageData<AuditProject> pageProject = auditProjectService
                            .getAuditProjectPageData(sql.getMap(), first, pageSize, sortField, sortOrder)
                            .getResult();
                    if (pageProject != null) {
                        for (AuditProject auditProject : pageProject.getList()) {
                            Map<String, String> mapProjectName = handleIsMyproject(auditProject);
                            // 返回办件名称
                               auditProject.put("taskname",
                                       mapProjectName.get("my") + "&nbsp;&nbsp;<a  href='javascript:void(0)' onclick='openUrl(\"" + mapProjectName.get("url")
                                               + "\")'>" + auditProject.getProjectname() + "</a>"
                                              );                              
                            AuditProjectSparetime auditProjectSparetime = auditProjectSpareTimeService
                                    .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();                                
                            if (auditProjectSparetime != null) {
                                auditProject.put("sparetime", getSpareTime(auditProjectSparetime.getSpareminutes(),
                                        auditProject.getTasktype()));
                            }
                            else {
                                auditProject.put("sparetime", "--");
                            }
                        }
                        this.setRowCount(pageProject.getRowCount());
                        return pageProject.getList();
                    }else{
                        this.setRowCount(0);
                        return new  ArrayList<AuditProject>();
                    }
                }
            };
        }
        return model;
    }

    /**
     * 获取剩余时间
     * 
     * @return String
     */
    public String getSpareTime(int minutes, int taskType) {
        String result = "";
        if (StringUtil.isNotBlank(taskType)) {
            if (taskType != Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                if (minutes > 0) {
                    if (minutes < 1440){
                        result = "剩余" + CommonUtil.getSpareTimes(minutes)
                                + "<img src=\"../../../image/light/yellowLight.gif\"/>";
                    }
                    else{
                        result = "剩余" + CommonUtil.getSpareTimes(minutes)
                                + "<img src=\"../../../image/light/greenLight.gif\"/>";
                    }
                }
                else {
                    minutes = -minutes;
                    result = "超时" + CommonUtil.getSpareTimes(minutes)
                            + "<img src=\"../../../image/light/redLight.gif\"/>";
                }
            }
            else {
                result = "--";
            }
        }
        else {
            result = "--";
        }
        return result;
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

	
	public Map<String, String> handleIsMyproject(AuditProject auditProject) {
		String pviGuid = auditProject.getPviguid();
		String Taskguid = auditProject.getTaskguid();
		String RowGuid = auditProject.getRowguid();
		Integer Status = auditProject.getStatus();
		String acceptuserguid  = auditProject.getAcceptuserguid();

        Map<String, String> map = new HashMap<String, String>(16);
        // 1.获取活动的工作项
        List<WorkflowWorkItem> workflowWorkItems = getActivityWorkflowWorkItem(pviGuid);
        if (workflowWorkItems != null && !workflowWorkItems.isEmpty() && ZwfwConstant.BANJIAN_STATUS_YSL<=Status) {// 存在活动工作项且办件已受理
        	//受理后补办显示补办页面
        	if(ZwfwConstant.BANJIAN_STATUS_YSLDBB == Status){
        		map.put("url",
        				"epointzwfw/auditbusiness/auditproject/auditprojectinfobuzheng?projectguid=" + RowGuid + "&TaskGuid="
                                + Taskguid + "&ProcessVersionInstanceGuid=" + pviGuid);
                map.put("my", "<span class='text-danger'>窗口办件</span>");
        	}else{
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
                }else{
                	map.put("url", "epointzwfw/auditbusiness/auditproject/auditprojectinfo?processguid=" + task.getProcessguid() + "&taskguid="
                            + Taskguid + "&projectguid=" + RowGuid);
                    map.put("my", "<span class='text-info'>窗口办件</span>");
                }
                if(UserSession.getInstance().getUserGuid().equals(acceptuserguid)){
                    map.put("my", "<span class='text-danger'>我的办件</span>");
                }
            }
        }
        return map;	
	}
    
    public void hasProGuid(String rowguid){
        String areacode;
        if (ZwfwUserSession.getInstance().getCitylevel()!=null&&(Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer.parseInt(ZwfwConstant.AREA_TYPE_XQJ))){
        	areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }else{
        	areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(rowguid, areacode).getResult();
        if(auditProject==null){
            addCallbackParam("message","办件已删除");
        }
     }
    
    public AuditProject getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditProject();
        }
        return dataBean;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
    }
}
