package com.epoint.auditproject.windowproject.action;

import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.cache.util.ZwfwCommonCacheUtil;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 办理工作台页面对应的后台
 *
 * @author Administrator
 * @version [版本号, 2017年5月22日]
 */
@RestController("windowworkpaneldshaction")
@Scope("request")
public class WindowWorkPanelDshAction extends BaseController {
    private static final long serialVersionUID = 2714262579121582382L;


    /**
     * 实体bean
     */
    private AuditProject dataBean = new AuditProject();
    /**
     * 办件剩余时间
     */
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;
    /**
     * 从ZwfwUserSession中获取的windowguid
     */
    private String windowguid;
    /**
     * 督办时间
     */
    private String dateStart;
    private String dateEnd;
    private Integer jzcount = 0;


    /**
     * 申请人查询条件
     */
    private String applyerName;

    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IWFInstanceAPI9 wfi;

    @Autowired
    private IWorkflowActivityService wfaService;
    /**
     * frame_ou对应微服务接口
     */
    @Autowired
    private IOuService ouService;

    @Autowired
    private IAuditOrgaArea auditOrgaArea;

    @Autowired
    private IAuditTask taskService;
    private static ZwfwCommonCacheUtil cacheUtil = new ZwfwCommonCacheUtil(1800);

    @Override
    public void pageLoad() {
        windowguid = ZwfwUserSession.getInstance().getWindowGuid();
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 5452702001563539075L;

                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    if (StringUtil.isNotBlank(windowguid)){
                        ArrayList list = new ArrayList();
                        String fieldstr = "rowguid,areacode,pviguid,taskguid,projectname,promiseenddate,tasktype,status,sparetime,applyername,bubandate,applydate,acceptuserdate,WindowName,receivedate,yushendate,acceptuserguid ";
                        Date datestart = null;
                        Date dateend = null;
                        if (StringUtil.isNotBlank(dateStart)) {
                            datestart = EpointDateUtil.getBeginOfDateStr(dateStart);
                        }
                        if (StringUtil.isNotBlank(dateEnd)) {
                            dateend = EpointDateUtil.getEndOfDateStr(dateEnd);
                        }
                        String area = "";
                        // 如果是镇村接件
                        if (ZwfwUserSession.getInstance().getCitylevel() != null && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                                .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                            area = ZwfwUserSession.getInstance().getBaseAreaCode();
                        } else {
                            area = ZwfwUserSession.getInstance().getAreaCode();
                        }
                        String areacode = ZwfwUserSession.getInstance().getAreaCode();
                        SqlConditionUtil sql = new SqlConditionUtil();
                        if (StringUtil.isNotBlank(applyerName)) {
                            sql.like("applyername", applyerName);
                        }
                        if (StringUtil.isNotBlank(datestart)) {
                            sql.ge("acceptuserdate", datestart);
                        }
                        if (StringUtil.isNotBlank(dateend)) {
                            sql.le("acceptuserdate", dateend);
                        }
                        //状态为待审核
                        sql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                        //areacode
                        sql.isBlankOrValue("acceptareacode", areacode);
                        //中心guid
                        sql.isBlankOrValue("centerGuid", ZwfwUserSession.getInstance().getCenterGuid());
                        //2022/05/27 阳佳 加上windowguid的筛选
                        sql.eq("windowguid",windowguid);
                        PageData<AuditProject> pageData = iAuditProject.getAuditProjectPageData(fieldstr, sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                        if (pageData != null && pageData.getList() != null) {
                            for (AuditProject auditProject : pageData.getList()) {
                                // 返回窗口名字
                                auditProject.put("WindowNameField", auditProject.getWindowname());
                                Map<String, String> mapProjectName = handleIsMyproject(auditProject);
                                // 返回办件名称
                                auditProject.put("TaskName",
                                        mapProjectName.get("my") + "&nbsp;&nbsp;<a  href='javascript:void(0)' onclick='openUrl(\"" + mapProjectName.get("url")
                                                + "\")'>" + auditProject.getProjectname() + "</a>"
                                );
                                // 返回承诺办结时间
                                auditProject.put("promiseenddate", getTime(auditProject.getPromiseenddate(),
                                        auditProject.getTasktype().toString()));
                                // 返回剩余时间
                                auditProject.put("computeSpareTime",
                                        getSpareTime(auditProject, auditProject.getStatus()));

                                if (auditProject.getReceivedate() == null) {
                                    auditProject.put("receivedate", "--");
                                }
                            }
                            this.setRowCount(pageData.getRowCount());
                            return pageData.getList();
                        } else {
                            this.setRowCount(0);
                            return list;
                        }
                    }
                    else{
                        this.setRowCount(0);
                        return new ArrayList<>();
                    }
                }
            };
        }
        return model;
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
            } else {
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
        } else {// 不存在活动的工作项

            AuditCommonResult<AuditTask> taskResult = taskService.getAuditTaskByGuid(Taskguid, true);
            AuditTask task = taskResult.getResult();
            if (task != null) {
                // map.put("url", null);
                WorkflowActivity activity = wfaService.getFirstActivity(task.getPvguid());
                if (activity != null) {
                    map.put("url", activity.getHandleUrl() + "?processguid=" + task.getProcessguid() + "&taskguid="
                            + Taskguid + "&projectguid=" + RowGuid);
                    map.put("my", "<span class='text-info'>窗口办件</span>");
                } else {
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

    /**
     * 获取承诺时间
     */
    public Object getTime(Date time, String tasktype) {
        Object result = null;
        if (ZwfwConstant.ITEMTYPE_JBJ.equals(tasktype)) {
            result = "即办件";
        } else {
            result = time;
        }
        return result;
    }

    /**
     * 获取剩余时间
     *
     * @return String
     */
    public String getSpareTime(AuditProject auditProject, int status) {
        String result = "";
        if (auditProject != null && StringUtil.isNotBlank(status)) {
            if (!(auditProject.getTasktype().equals(Integer.parseInt(ZwfwConstant.ITEMTYPE_JBJ))) || status == ZwfwConstant.BANJIAN_STATUS_SPTG || status == ZwfwConstant.BANJIAN_STATUS_SPBTG) {
                AuditProjectSparetime auditProjectSparetime = iAuditProjectSparetime
                        .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                int i = auditProject.getSparetime() == null ? 0 : auditProject.getSparetime();
                if (status < 90) {
                    i = auditProjectSparetime == null ? 0 : auditProjectSparetime.getSpareminutes();
                }
                if (i > 0) {
                    if (i < 1440) {
                        result = "剩余" + CommonUtil.getSpareTimes(i)
                                + "<img src=\"../../../image/light/yellowLight.gif\"/>";
                    } else {
                        result = "剩余" + CommonUtil.getSpareTimes(i)
                                + "<img src=\"../../../image/light/greenLight.gif\"/>";
                    }
                } else if (i == 0) {
                    if (auditProject.getSparetime() == null || auditProjectSparetime == null) {
                        result = "--";
                    } else {
                        result = "剩余" + CommonUtil.getSpareTimes(i)
                                + "<img src=\"../../../image/light/greenLight.gif\"/>";
                    }
                } else {
                    i = -i;
                    result = "超时" + CommonUtil.getSpareTimes(i)
                            + "<img src=\"../../../image/light/redLight.gif\"/>";
                }
            } else {
                result = "--";
            }
        } else {
            result = "--";
        }
        return result;
    }

    public void setDataBean(AuditProject dataBean) {
        this.dataBean = dataBean;
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

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

}
