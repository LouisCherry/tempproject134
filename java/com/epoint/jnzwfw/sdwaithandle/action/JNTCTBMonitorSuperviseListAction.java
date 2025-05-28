package com.epoint.jnzwfw.sdwaithandle.action;

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

import com.epoint.auditproject.monitorsupervise.api.IJNAuditProjectMonitorService;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
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
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 业务管理list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("jntctbmonitorsuperviselistaction")
@Scope("request")
public class JNTCTBMonitorSuperviseListAction extends BaseController
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
    private String is_all;

    private String is_delay;
    
    private String is_lczj;
    
    private boolean is_leader = false;

    private  boolean chaoshi = false;
    
    private  String ouguid;
    /**
     * 花费时间
     */
    private String spareDays;
    

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    @Autowired
    private IAuditOrgaWindowYjs orgaWindowService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    @Autowired
    IWFInstanceAPI9 iwf9;

    @Autowired
    IAuditProjectNumber auditProjectNumber;
    
    @Autowired
    private IJNAuditProjectMonitorService jnProjectMonitorService;

    //日志
    transient Logger log = LogUtil.getLog(JNTCTBMonitorSuperviseListAction.class);

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
        is_lczj = this.getRequestParameter("is_lczj");
        
        is_all = this.getRequestParameter("is_all");
        ouguid = this.getRequestParameter("ouguid");
        String type = this.getRequestParameter("type");
        String flag = this.getRequestParameter("flag");
        if(StringUtil.isNotBlank(flag) && "1".equals(flag)){
            addCallbackParam("flag", "1");
        }
        String speStatus = this.getRequestParameter("speStatus");
        if(StringUtil.isNotBlank(speStatus) && "no".equals(speStatus)){
            addCallbackParam("speStatus", "no");
        }
        if (StringUtil.isNotBlank(type)) {
            is_leader = true;
            switch (type) {
                case "不予受理":
                    status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL);
                    break;
                case "不予许可":
                    status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG);
                    break;
                case "延期申请":
                    is_delay="1,10";
                    addCallbackParam("is_delay", "true");
                    break;
                case "异常终止":
                    status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_YCZZ);
                    break;
                case "chaoshi":
                    chaoshi = true;
                        break;
                default:
                    break;
            }
            addCallbackParam("is_leader", "true");
        }
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {

                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                @SuppressWarnings({"unchecked", "rawtypes" })
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    ArrayList list = new ArrayList();
                    if (StringUtil.isNotBlank(areaCode)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("is_samecity", "1");
                        // 在查询办件表的时候，增加了分表需要传入辖区参数
                        if(!"370800".equals(areaCode)){
                            sql.eq("areaCode", areaCode);
                        }
                        // 1、申请人
                        if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                            //过滤%，以免输入%查出全部数据
                            if("%".equals(dataBean.getApplyername()) || "_".equals(dataBean.getApplyername())){
                                String applyName = "/" + dataBean.getApplyername();
                                sql.like("applyername", applyName);
                            }else{
                                sql.like("applyername", dataBean.getApplyername().trim());
                            }   
                        }
                        // 2、办件名称
                        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                          //过滤%，以免输入%查出全部数据
                            if("%".equals(dataBean.getProjectname()) || "_".equals(dataBean.getProjectname())){
                                String projectName = "/" + dataBean.getProjectname();
                                sql.like("projectname", projectName);
                            }else{
                                sql.like("projectname", dataBean.getProjectname().trim());
                            }     
                        }
                        // 3、办件编号
                        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
                          //过滤%，以免输入%查出全部数据
                            if("%".equals(dataBean.getFlowsn()) || "_".equals(dataBean.getFlowsn())){
                                String flowsn = "/" + dataBean.getFlowsn();
                                sql.like("flowsn", flowsn);
                            }else{
                                sql.like("flowsn", dataBean.getFlowsn().trim());
                            }  
                        }
                        // 4、受理时间
                        if (StringUtil.isNotBlank(acceptuserdateStart)) {
                            sql.ge("acceptuserdate", EpointDateUtil.getBeginOfDateStr(acceptuserdateStart));
                        }
                        if (StringUtil.isNotBlank(acceptuserdateEnd)) {
                            sql.lt("acceptuserdate", EpointDateUtil.getEndOfDateStr(acceptuserdateEnd));
                        }
                        // 5、办结时间
                        if (StringUtil.isNotBlank(banjiedateStart)) {
                            sql.ge("banjiedate", EpointDateUtil.getBeginOfDateStr(banjiedateStart));
                        }
                        if (StringUtil.isNotBlank(banjiedateEnd)) {
                            sql.lt("banjiedate", EpointDateUtil.getEndOfDateStr(banjiedateEnd));
                        }
                        // 6、办件状态
                        if (StringUtil.isNotBlank(status)) {
                            sql.eq("status", status);
                        }
                        //7、办件部门（判断是否是所有部门）
                        if (StringUtil.isNotBlank(projectOu)&& !"f9root".equals(projectOu)) {
                            if(!"f9root".equals(projectOu)){
                                sql.eq("ouguid", projectOu); 
                            }
                        }
                        //8、办件窗口
                        if (StringUtil.isNotBlank(windowGuid_Search) && !("all".equals(windowGuid_Search))) {
                            sql.eq("windowguid", windowGuid_Search);
                        }
                        if (StringUtil.isNotBlank(is_delay)) {
                            sql.in("is_delay", is_delay);
                        }
                        if(StringUtil.isNotBlank(ouguid) && !"undefined".equals(ouguid)){
                            sql.eq("ouguid", ouguid);
                        }
                        //9、剩余时间
                        //if (StringUtil.isNotBlank(spareDays)) {   
                        //}                        
                        //10、承诺办结时间
                        if (StringUtil.isNotBlank(promiseEndDateStart)) {
                            sql.ge("PROMISEENDDATE", EpointDateUtil.getBeginOfDateStr(promiseEndDateStart));
                        }
                        if (StringUtil.isNotBlank(promiseEndDateEnd)) {
                            sql.lt("PROMISEENDDATE", EpointDateUtil.getEndOfDateStr(promiseEndDateEnd));
                        }
                        // 11、申请时间
                        if (StringUtil.isNotBlank(applyDateStart)) {
                            sql.ge("applydate", EpointDateUtil.getBeginOfDateStr(applyDateStart));
                        }
                        if (StringUtil.isNotBlank(applyDateEnd)) {
                            sql.lt("applydate", EpointDateUtil.getEndOfDateStr(applyDateEnd));
                        }
                        if(is_leader){
                            GregorianCalendar gcNew = new GregorianCalendar();
                            gcNew.add(Calendar.MONTH, -1);
                            Date dtFrom = gcNew.getTime();
                            sql.ge("APPLYDATE", dtFrom);
                            sql.eq("centerguid", centerguid);
                            if ("1,10".equals(is_delay)) {
                                sql.in("is_delay", is_delay);
                            }else{
                                sql.eq("status", status);  
                            }
                        }
                        //12、个性化浪潮自建系统办件正常模块不显示
                        if(StringUtil.isBlank(is_lczj)){
                            sql.isBlank("is_lczj");;
                        }else{
                            sql.eq("is_lczj", is_lczj);
                        }
                        String ouname = request.getParameter("ouname");
                        if(StringUtil.isNotBlank(ouname)){
                            GregorianCalendar gcNew = new GregorianCalendar();
                            gcNew.set(Calendar.MONTH, gcNew.get(Calendar.MONTH) - 1);
                            Date dtFrom1 = gcNew.getTime();
                            sql.eq("ouname", ouname);
                            sql.ge("status",  String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ ));
                            sql.nq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_INIT ));
                            sql.ge("APPLYDATE", dtFrom1);
                        }else{
                            sql.ge("status",  String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ ));
                            sql.nq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_INIT ));
                           
                        }
                        String userguid = userSession.getUserGuid();
                        
                        sql.setOrder(sortField, sortOrder);
                        Map<String, String> sqlmap = sql.getMap();
                        
                        String fieldstr = " rowguid,status,pviguid,projectname,windowname,flowsn,applyername,applydate,ACCEPTUSERDATE,areaCode ";
                        PageData<AuditProject> pageData = jnProjectMonitorService.getAuditProjectPageDataMonitor(fieldstr,
                                sqlmap, userguid, ismaterial, first, pageSize, "", "").getResult();
                        List<AuditProject> pagelist =  new ArrayList<AuditProject>();
                        if(pageData!=null){
                            pagelist =  pageData.getList();
                            List<AuditProject> delpagelist = new ArrayList<AuditProject>();
                            int pagecount = pageData.getRowCount();
                            if(chaoshi){
                                for (AuditProject auditProject : pagelist) {
                                    auditProject.put("sparetime",
                                            getSpareTime(auditProject.getRowguid(), auditProject.getStatus(),auditProject.getAreacode()));
                                    if(!getSpareTime(auditProject.getRowguid(), auditProject.getStatus(),auditProject.getAreacode()).startsWith("超时")){
                                        delpagelist.add(auditProject);
                                    }
                                }
                                pagelist.removeAll(delpagelist);
                                pagecount = pagecount - delpagelist.size();
                            }else{
                                for (AuditProject auditProject : pageData.getList()) {
                                    auditProject.put("sparetime",
                                            getSpareTime(auditProject.getRowguid(), auditProject.getStatus(),auditProject.getAreacode()));
                                }
                            }
                            this.setRowCount(pagecount);  
                        }
                        return pagelist;
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
    public String getSpareTime(String rowguid, int status, String areacode) {
        String result = "";
        // 默认剩余时间
        String defaultSparaeTime = "";
        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(status)) {
            String fields = " rowguid,taskguid,projectname,tasktype ";
            AuditProject auditProject = auditProjectService
                    .getAuditProjectByRowGuid(fields, rowguid, areacode).getResult();
            AuditProjectSparetime auditProjectSparetime = projectSparetimeService.getSparetimeByProjectGuid(rowguid)
                    .getResult();
            AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
            if (auditProjectSparetime == null) {
                return defaultSparaeTime;
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
                            if (i < 1440){
                                result = "剩余" + CommonUtil.getSpareTimes(i)
                                        + "<img src=\"../../image/light/yellowLight.gif\" />";
                            }
                            else{
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
                        return defaultSparaeTime;
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
                        return defaultSparaeTime;
                    }
                }
                else {
                    return defaultSparaeTime;
                }
            }
            else {//非承诺件
                result = ZwfwConstant.getItemtypeKey(auditProject.getTasktype().toString());
            }
        }
        else {
            result = defaultSparaeTime;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModel() {
        if (statusModel == null) {
            statusModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办件状态", null, true));
            if (statusModel != null) {
                Iterator<SelectItem> it = statusModel.iterator();
                if (it.hasNext()) {
                    it.next();
                }
                while (it.hasNext()) {
                    SelectItem item = it.next();
                    // 删除列表中不需要的列表选项
                    if ((Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_DJJ)
                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWINIT)
                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWWTJ)
                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_INIT)
                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_YDJ)
                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWYSTU)
                            || (Integer.parseInt(item.getValue().toString()) == ZwfwConstant.BANJIAN_STATUS_WWYSTG)) {
                        it.remove();
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
                // 如果存在待办事宜，则删除待办
                List<MessagesCenter> messagesCenterList = messagesCenterService.queryForList(userSession.getUserGuid(), null,
                        null, "", IMessagesCenterService.MESSAGETYPE_WAIT, projectGuid, "", -1, "", null, null, 0,
                        -1);
                if (messagesCenterList != null && messagesCenterList.size() > 0) {
                    for (MessagesCenter messagescenter : messagesCenterList) {
                        messagesCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
                    }
                }
                if (projectDetail.getHebingshoulishuliang() != null) {
                    auditProjectNumber.deleteAuditProjectNumberByRelated(projectGuid);
                }
                log.debug(userSession.getDisplayName() + "于" + new Date() + "删除办件，办件编号唯一标识为：" + projectGuid);
                String OPERATOR_TYPE = "删除";
                String SUBSYSTEM_TYPE = "无效件清理";
                String content = userSession.getDisplayName() + "于" + new Date() + "删除办件，办件编号唯一标识为：" + projectGuid;
                String OperateTable = "ASP_DELETEPROJECT";
                Operation.info(getClientIP(), userSession.getLoginID(),
                        userSession.getUserGuid(), userSession.getDisplayName(), OPERATOR_TYPE, "", SUBSYSTEM_TYPE,
                        content, "", OperateTable, userSession.getBaseOUGuid(), userSession.getOuGuid(),
                        getMacaddress(), content, new Date());
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

    public String getIs_all() {
        return is_all;
    }

    public void setIs_all(String is_all) {
        this.is_all = is_all;
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

    public String getIs_lczj() {
        return is_lczj;
    }

    public void setIs_lczj(String is_lczj) {
        this.is_lczj = is_lczj;
    }
}
