package com.epoint.auditproject.monitorsupervise.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.log.FrameLogUtil.Operation;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

import oracle.net.aso.a;

/**
 * 业务管理list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("jnauditprojectblyclistaction")
@Scope("request")
public class JNAuditprojectBlycListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> statusModel = null;
    /**
     * 是否启用下拉列表model
     */
    private List<SelectItem> ismaterialModel = null;
    /**
     * 窗口列表model
     */
    private List<SelectItem> windownameModel = null;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;
    /**
     * 是否上传材料
     */
    private int ismaterial;
    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 申请时间
     */
    private String applyDateStart;
    private String applyDateEnd;
    /**
     * 受理时间
     */
    private String acceptuserdateStart;
    private String acceptuserdateEnd;
    /**
     * 办结时间
     */
    private String banjiedateStart;
    private String banjiedateEnd;
    /**
     * 承诺办结时间
     */
    private String promiseEndDateStart;
    private String promiseEndDateEnd;

    /**
     * 窗口唯一标识
     */
    private String windowguid = "all";
    /**
     * 中心唯一标识
     */
    private String centerguid;
    /**
     * 辖区
     */
    private String areaCode;
    /**
     * 办件状态
     */
    private String status;
    /**
     * 窗口搜索
     */
    private String windowGuid_Search;
    /**
     * 办件部门
     */
    private String projectOu;
    /**
     * 监控权限，地址传参
     */

    private String is_delay;

    private String type;

    private String ouguid;

    private String iswindow;
    /**
     * 花费时间
     */
    private String spareDays;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    @Autowired
    private IAuditOrgaWindow orgaWindowService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    @Autowired
    IWFInstanceAPI9 iwf9;

    @Autowired
    IAuditProjectNumber auditProjectNumber;

    //日志
    transient Logger log = LogUtil.getLog(JNAuditprojectBlycListAction.class);

    @Override
    public void pageLoad() {
        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            windowguid = ZwfwUserSession.getInstance().getWindowGuid();
        }
        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getCenterGuid())) {
            centerguid = ZwfwUserSession.getInstance().getCenterGuid();
        }
        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getAreaCode())) {
            areaCode = ZwfwUserSession.getInstance().getAreaCode();
        }
        type = getRequestParameter("type");
        ouguid = getRequestParameter("ouguid");
        iswindow = getRequestParameter("iswindow");
        String flag = this.getRequestParameter("flag");
        if (StringUtil.isNotBlank(flag)) {
            addCallbackParam("flag", "1");
        }
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                @SuppressWarnings({"unchecked", "rawtypes" })
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    ArrayList list = new ArrayList();
                    if (StringUtil.isNotBlank(areaCode)) {
                        PageData<AuditProject> projects;
                        SqlConditionUtil conditionsql = new SqlConditionUtil();

                        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                            //过滤%，以免输入%查出全部数据
                            if ("%".equals(dataBean.getApplyername())) {
                                String applyName = "/%";
                                conditionsql.like("applyername", applyName);
                            }
                            else {
                                conditionsql.like("applyername", dataBean.getApplyername().trim());
                            }
                        }
                        // 2、办件名称
                        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                            //过滤%，以免输入%查出全部数据
                            if ("%".equals(dataBean.getProjectname())) {
                                String projectName = "/%";
                                conditionsql.like("projectname", projectName);
                            }
                            else {
                                conditionsql.like("projectname", dataBean.getProjectname().trim());
                            }
                        }
                        // 3、办件编号
                        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
                            //过滤%，以免输入%查出全部数据
                            if ("%".equals(dataBean.getFlowsn())) {
                                String flowsn = "/%";
                                conditionsql.like("flowsn", flowsn);
                            }
                            else {
                                conditionsql.like("flowsn", dataBean.getFlowsn().trim());
                            }
                        }
                        // 4、受理时间
                        if (StringUtil.isNotBlank(acceptuserdateStart)) {
                            conditionsql.ge("acceptuserdate", EpointDateUtil.getBeginOfDateStr(acceptuserdateStart));
                        }
                        if (StringUtil.isNotBlank(acceptuserdateEnd)) {
                            conditionsql.lt("acceptuserdate", EpointDateUtil.getEndOfDateStr(acceptuserdateEnd));
                        }
                        // 6、办件状态
                        if (StringUtil.isNotBlank(status)) {
                            conditionsql.eq("status", status);
                        }
                        //7、办件部门
                        if (StringUtil.isNotBlank(projectOu)) {
                            conditionsql.eq("ouguid", projectOu);
                        }
                        //8、办件窗口
                        if (StringUtil.isNotBlank(windowGuid_Search) && !("all".equals(windowGuid_Search))) {
                            conditionsql.eq("windowguid", windowGuid_Search);
                        }
                        if (StringUtil.isNotBlank(is_delay)) {
                            conditionsql.eq("is_delay", "10");
                        }
                        if (StringUtil.isNotBlank(ouguid) && !"undefined".equals(ouguid)) {
                            conditionsql.eq("ouguid", ouguid);
                        }
                        //10、承诺办结时间
                        if (StringUtil.isNotBlank(promiseEndDateStart)) {
                            conditionsql.ge("PROMISEENDDATE", EpointDateUtil.getBeginOfDateStr(promiseEndDateStart));
                        }
                        if (StringUtil.isNotBlank(promiseEndDateEnd)) {
                            conditionsql.lt("PROMISEENDDATE", EpointDateUtil.getEndOfDateStr(promiseEndDateEnd));
                        }
                        GregorianCalendar gcNew = new GregorianCalendar();
                        gcNew.add(Calendar.MONTH, -1);
                        Date dtFrom = gcNew.getTime();
                        //添加乡镇判断
                        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
                            conditionsql.eq("a.areacode", ZwfwUserSession.getInstance().getBaseAreaCode());
                        }
                        else {
                            conditionsql.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode());
                        }
                        conditionsql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                        conditionsql.lt("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
                        conditionsql.ge("applydate", dtFrom);
                        conditionsql.eq("a.centerGuid", centerguid);
                        conditionsql.setOrderAsc("spareminutes");
                        conditionsql.lt("spareminutes", "1440");
                        if (StringUtil.isNotBlank(type) && "chaoshi".equals(type)) {
                            conditionsql.lt("spareminutes", ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        if (StringUtil.isNotBlank(type) && "ljcq".equals(type)) {
                            conditionsql.ge("spareminutes", ZwfwConstant.CONSTANT_STR_ZERO);
                        }
                        if (StringUtil.isNotBlank(iswindow)) {
                            conditionsql.ge("applydate", new Date(111111));
                            conditionsql.eq("windowguid", ZwfwUserSession.getInstance().getWindowGuid());
                        }
                        if (StringUtil.isNotBlank(ouguid) && !"undefined".equals(ouguid)) {
                            conditionsql.eq("ouguid", ouguid);
                        }
                        String field = "a.rowguid rowguid,status,pviguid,projectname,windowname,flowsn,applyername,applydate,ACCEPTUSERDATE";
                        projects = auditProjectService
                                .getPagaBySpareTime(field, conditionsql.getMap(), first, pageSize, "", "").getResult();
                        if (projects != null && projects.getList().size() > 0) {
                            for (AuditProject auditProject : projects.getList()) {
                                auditProject.put("sparetime",
                                        getSpareTime(auditProject.getRowguid(), auditProject.getStatus()));
                            }
                            this.setRowCount(projects.getRowCount());
                            return projects.getList();
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

    /**
     * 获取剩余时间
     * 
     * @return String
     */
    public String getSpareTime(String rowguid, int status) {
        String result = "";
        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(status)) {
            String fields = " rowguid,taskguid,projectname,tasktype ";
            String areacode = "";
            if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ)) {
                areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
            }
            else {
                areacode = ZwfwUserSession.getInstance().getAreaCode();
            }
            AuditProject auditProject = auditProjectService.getAuditProjectByRowGuid(fields, rowguid, areacode)
                    .getResult();
            AuditProjectSparetime auditProjectSparetime = projectSparetimeService.getSparetimeByProjectGuid(rowguid)
                    .getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (auditProjectSparetime == null) {
                return "--";
            }
            else if (auditProject.getTasktype() == Integer.parseInt(ZwfwConstant.ITEMTYPE_CNJ)) {
                if ((status >= ZwfwConstant.BANJIAN_STATUS_YSL && status < ZwfwConstant.BANJIAN_STATUS_ZCBJ)
                        || (status == ZwfwConstant.BANJIAN_STATUS_YJJ
                                && ZwfwConstant.CONSTANT_INT_ONE == auditTaskExtension.getIszijianxitong()
                                && ZwfwConstant.ZIJIANMODE_JJMODE
                                        .equals(String.valueOf(auditTaskExtension.getZijian_mode())))) {
                    if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
                        int i = auditProjectSparetime.getSpareminutes();
                        if (i > 0) {
                            if (i < 1440) {
                                result = "剩余" + CommonUtil.getSpareTimes(i)
                                        + "<img src=\"../../image/light/yellowLight.gif\" />";
                            }
                            else {
                                result = "剩余" + CommonUtil.getSpareTimes(i)
                                        + "<img src=\"../../image/light/greenLight.gif\" />";
                            }
                        }
                        else {
                            i = -i;
                            result = "超时" + CommonUtil.getSpareTimes(i)
                                    + "<img src=\"../../image/light/redLight.gif\" />";
                        }
                    }
                    else {
                        return "--";
                    }
                }
                else if (status >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                    if (StringUtil.isNotBlank(auditProjectSparetime.getSpareminutes())) {
                        int i = auditProjectSparetime.getSpareminutes();
                        if (i < 0) {// 如果剩余时间为负数:办结超时
                            result = "已办结(超过" + CommonUtil.getSpareTimes(i)
                                    + "<img src=\"../../image/light/redLight.gif\" />";
                        }
                        else {
                            result = "已办结" + "<img src=\"../../image/light/greenLight.gif\" />";
                        }
                    }
                    else {// 办结用时为空：默认--
                        return "--";
                    }
                }
                else {
                    return "--";
                }
            }
            else {//非承诺件
                result = ZwfwConstant.getItemtypeKey(auditProject.getTasktype().toString());
            }
        }
        else {
            result = "--";
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, true));
            //statusModel.stream().filter(a->(Integer.parseInt(a.getValue().toString()))
            if (statusModel != null) {
                Iterator<SelectItem> it = statusModel.iterator();
                if (it.hasNext()) {
                    it.next();
                }
                while (it.hasNext()) {
                    SelectItem item = it.next();
                    // 删除列表中不需要的列表选项
                    Integer status = Integer.parseInt(item.getValue().toString());
                    if ("ljcq".equals(type)) {
                        if (status < ZwfwConstant.BANJIAN_STATUS_YSL || status >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                            it.remove();
                        }
                    }
                }
            }
        }
        return this.statusModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getIsMaterialModel() {
        if (ismaterialModel == null) {
            ismaterialModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否上传材料", null, false));
        }
        return this.ismaterialModel;
    }

    public List<SelectItem> getWindowNameModel() {
        if (windownameModel == null) {
            windownameModel = new ArrayList<SelectItem>();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());
            List<AuditOrgaWindow> list = orgaWindowService.getAllWindow(sql.getMap()).getResult();
            for (AuditOrgaWindow auditWindow : list) {
                windownameModel.add(new SelectItem(auditWindow.getRowguid(), auditWindow.getWindowname()));
            }
            windownameModel.add(0, new SelectItem("all", "所有窗口"));
        }
        return this.windownameModel;
    }

    public void deleteSelect(String ids) {
        if (StringUtil.isNotBlank(ids)) {
            String[] select = ids.split(",");
            for (String projectGuid : select) {
                //待办删除
                AuditProject projectDetail = auditProjectService
                        .getAuditProjectByRowGuid(projectGuid, ZwfwUserSession.getInstance().getAreaCode()).getResult();
                ProcessVersionInstance pvi = iwf9.getProcessVersionInstance(projectDetail.getPviguid());
                if (pvi != null) {
                    List<WorkflowWorkItem> workflowworkitems = iwf9.selectByProcessVersionInstanceGuid(pvi, 20, null,
                            null, null);
                    for (WorkflowWorkItem WorkflowWorkItem : workflowworkitems) {
                        String messageguid = WorkflowWorkItem.getWaitHandleGuid();
                        //删除其他用户名下的待办（由于分表分库原因，部分代码删除不了，用此方法）            
                        messagesCenterService.deleteMessage(messageguid, WorkflowWorkItem.getTransactor());
                    }
                }
                auditProjectService.deleteProject(projectGuid, projectDetail.getFlowsn(), projectDetail.getPviguid());
                auditProjectService.deleteProjectByGuid(projectGuid, ZwfwUserSession.getInstance().getAreaCode());
                if (projectDetail.getHebingshoulishuliang() != null) {
                    auditProjectNumber.deleteAuditProjectNumberByRelated(projectGuid);
                }
                log.debug(userSession.getDisplayName() + "于" + new Date() + "删除办件，办件编号唯一标识为：" + projectGuid);
                String OPERATOR_TYPE = "删除";
                String SUBSYSTEM_TYPE = "无效件清理";
                String content = userSession.getDisplayName() + "于" + new Date() + "删除办件，办件编号唯一标识为：" + projectGuid;
                String OperateTable = "ASP_DELETEPROJECT";
                Operation.info(getClientIP(), userSession.getLoginID(), userSession.getUserGuid(),
                        userSession.getDisplayName(), OPERATOR_TYPE, "", SUBSYSTEM_TYPE, content, "", OperateTable,
                        userSession.getBaseOUGuid(), userSession.getOuGuid(), getMacaddress(), content, new Date());
            }
            addCallbackParam("msg", "删除成功！");
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

    public List<SelectItem> getIsmaterialModel() {
        return ismaterialModel;
    }

    public void setIsmaterialModel(List<SelectItem> ismaterialModel) {
        this.ismaterialModel = ismaterialModel;
    }

    public void setStatusModel(List<SelectItem> statusModel) {
        this.statusModel = statusModel;
    }

    public int getIsmaterial() {
        return ismaterial;
    }

    public void setIsmaterial(int ismaterial) {
        this.ismaterial = ismaterial;
    }

    public String getWindowguid() {
        return windowguid;
    }

    public void setWindowguid(String windowguid) {
        this.windowguid = windowguid;
    }

    public String getCenterguid() {
        return centerguid;
    }

    public void setCenterguid(String centerguid) {
        this.centerguid = centerguid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcceptuserdateStart() {
        return acceptuserdateStart;
    }

    public void setAcceptuserdateStart(String acceptuserdateStart) {
        this.acceptuserdateStart = acceptuserdateStart;
    }

    public String getAcceptuserdateEnd() {
        return acceptuserdateEnd;
    }

    public void setAcceptuserdateEnd(String acceptuserdateEnd) {
        this.acceptuserdateEnd = acceptuserdateEnd;
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

    public String getWindowGuid_Search() {
        return windowGuid_Search;
    }

    public void setWindowGuid_Search(String windowGuid_Search) {
        this.windowGuid_Search = windowGuid_Search;
    }

    public String getProjectOu() {
        return projectOu;
    }

    public void setProjectOu(String projectOu) {
        this.projectOu = projectOu;
    }

    public String getSpareDays() {
        return spareDays;
    }

    public void setSpareDays(String spareDays) {
        this.spareDays = spareDays;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPromiseEndDateStart() {
        return promiseEndDateStart;
    }

    public void setPromiseEndDateStart(String promiseEndDateStart) {
        this.promiseEndDateStart = promiseEndDateStart;
    }

    public String getPromiseEndDateEnd() {
        return promiseEndDateEnd;
    }

    public void setPromiseEndDateEnd(String promiseEndDateEnd) {
        this.promiseEndDateEnd = promiseEndDateEnd;
    }

    public String getApplyDateStart() {
        return applyDateStart;
    }

    public void setApplyDateStart(String applyDateStart) {
        this.applyDateStart = applyDateStart;
    }

    public String getApplyDateEnd() {
        return applyDateEnd;
    }

    public void setApplyDateEnd(String applyDateEnd) {
        this.applyDateEnd = applyDateEnd;
    }

    public String getIs_delay() {
        return is_delay;
    }

    public void setIs_delay(String is_delay) {
        this.is_delay = is_delay;
    }
    
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("projectname,windowname,flowsn,applyername,applydate,ACCEPTUSERDATE,sparetime,status",
                    "事项名称,窗口名称,办件编号,申请人,申请时间,受理时间,办结剩余时间,办件状态");
        }
        return exportModel;
    }
}
