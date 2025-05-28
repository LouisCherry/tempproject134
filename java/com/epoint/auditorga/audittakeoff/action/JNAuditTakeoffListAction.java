package com.epoint.auditorga.audittakeoff.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditorga.auditdepartment.domain.AuditOrgaDepartment;
import com.epoint.basic.auditorga.auditdepartment.inter.IAuditOrgaDepartment;
import com.epoint.basic.auditorga.auditmember.domain.AuditOrgaMember;
import com.epoint.basic.auditorga.auditmember.inter.IAuditOrgaMember;
import com.epoint.basic.auditorga.audittakeoff.domain.AuditOrgaTakeoff;
import com.epoint.basic.auditorga.audittakeoff.inter.IAuditOrgaTakeoff;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 中心请假记录表list页面对应的后台
 * 
 * @author TYX
 * @version [版本号, 2017-03-21 16:44:53]
 */
@RestController("jnaudittakeofflistaction")
@Scope("request")
public class JNAuditTakeoffListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 9162843651668250704L;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IAuditOrgaTakeoff auditto;
    @Autowired
    private IAuditOrgaMember auditMember;
    private String sqstartDate;
    private String sqendDate;
    private String qjstartDatefrom;
    private String qjendDatefrom;
    private String qjstartDateto;
    private String qjendDateto;
    @Autowired
    private IWFInstanceAPI9 wfi;
    @Autowired
    private IAuditOrgaConfig auditconfig;
    /**
     * 中心请假记录表实体对象
     */
    private AuditOrgaTakeoff dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditOrgaTakeoff> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 是否销假
     */
    private List<SelectItem> auditisXiaoJiaModal;
    /**
     * 请假类别代码项
     */
    private List<SelectItem> auditTypeModal;
    /**
     * 申请状态代码项
     */
    private List<SelectItem> auditStatusModal;
    
    @Autowired
    private IConfigService iConfigService;
    
    @Autowired
    private IAuditOrgaDepartment iAuditOrgaDepartment;

    private String centerGuid;

    @Override
    public void pageLoad() {
        dataBean = new AuditOrgaTakeoff();
        centerGuid = ZwfwUserSession.getInstance().getCenterGuid();
        addCallbackParam("AS_TakeOff_ProcesGuid", iConfigService.getFrameConfigValue("AS_TakeOff_ProcesGuid"));
        if (StringUtil.isBlank(centerGuid)) {
            //选取的人员的基本信息
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("userguid", userSession.getUserGuid());
            AuditOrgaMember member = auditMember.getAuditMember(sql.getMap()).getResult();
            String OUGuid = member.getOuguid();
            AuditOrgaDepartment department = iAuditOrgaDepartment.getAuditDepartmentByOUGuid(OUGuid).getResult();
            if (department != null) {
                centerGuid = department.getCenterguid();
            }
        }
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        int returntype = 1;
        for (String sel : select) {
            AuditOrgaTakeoff auditOrgaTakeoff = auditto.getTakeoffByRowguid(sel).getResult();
            if (ZwfwConstant.QinJia_Status_unsubmit.equals(auditOrgaTakeoff.getStaus())
                    || ZwfwConstant.QinJia_Status_back.equals(auditOrgaTakeoff.getStaus())) {
                auditto.deleteTakeoffByRowguid(sel);
            }
            else {
                returntype = 0;
            }
        }
        if (returntype == 1) {
            addCallbackParam("msg", "成功删除！");
        }
        else {
            addCallbackParam("msg", "只能删除状态为未提交或已退回的数据！");
        }
    }

    public DataGridModel<AuditOrgaTakeoff> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditOrgaTakeoff>()
            {
                @Override
                public List<AuditOrgaTakeoff> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    sortField = " sqtime";
                    sortOrder = "desc";
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", centerGuid);
                    if (StringUtil.isNotBlank(dataBean.getType())
                            && !dataBean.getType().equals(ZwfwConstant.CONSTANT_STR_ZERO)) {
                        sql.eq("Type", dataBean.getType());
                    }
                    if (StringUtil.isNotBlank(qjstartDatefrom)) {
                        sql.ge("Begintime", EpointDateUtil.getBeginOfDateStr(qjstartDatefrom));
                    }
                    if (StringUtil.isNotBlank(qjendDatefrom)) {
                        sql.lt("Begintime", EpointDateUtil.getEndOfDateStr(qjendDatefrom));
                    }
                    if (StringUtil.isNotBlank(qjstartDateto)) {
                        sql.ge("Endtime", EpointDateUtil.getBeginOfDateStr(qjstartDateto));
                    }
                    if (StringUtil.isNotBlank(qjendDateto)) {
                        sql.lt("Endtime", EpointDateUtil.getEndOfDateStr(qjendDateto));
                    }
                    if (StringUtil.isNotBlank(sqstartDate)) {
                        sql.ge("Sqtime", EpointDateUtil.getBeginOfDateStr(sqstartDate));
                    }
                    if (StringUtil.isNotBlank(sqendDate)) {
                        sql.lt("Sqtime", EpointDateUtil.getEndOfDateStr(sqendDate));
                    }
                    if (StringUtil.isNotBlank(dataBean.getStaus())) {
                        sql.eq("Staus", dataBean.getStaus().toString());
                    }
                    else { // 排除销假状态
                        sql.nq("Staus", "5");
                    }
                    if (StringUtil.isNotBlank(dataBean.getUsername())) {
                        sql.like("Username", dataBean.getUsername());
                    }
                    if (StringUtil.isNotBlank(dataBean.getIsxiaojia())) {
                        sql.eq("Isxiaojia", dataBean.getIsxiaojia().toString());
                    }
                    PageData<AuditOrgaTakeoff> pageData = auditto
                            .getAuditTakeoffPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    DecimalFormat df = new DecimalFormat("######0.00");
                    Double dbWorkhour = Double.parseDouble(getConfigValue(centerGuid, "AS_WorkHour"));
                    for (AuditOrgaTakeoff auditOrgaTakeoff : pageData.getList()) {
                        SqlConditionUtil sqlmen = new SqlConditionUtil();
                        sqlmen.eq("userguid", auditOrgaTakeoff.getUserguid());
                        AuditOrgaMember member = auditMember.getAuditMember(sqlmen.getMap()).getResult();
                        if (member == null) {
                            FrameOu ouname = ouservice.getOuByUserGuid(auditOrgaTakeoff.getUserguid());
                            if (ouname != null) {
                                auditOrgaTakeoff.put("DeptName", ouname.getOuname());
                            }
                        }
                        else {
                            auditOrgaTakeoff.put("DeptName", member.getOuname());
                        }
                        //请假天数
                        Double QJDayCount = auditOrgaTakeoff.getQjdaycount() / (dbWorkhour * 60);
                        auditOrgaTakeoff.put("QJDays", df.format(QJDayCount));
                    }
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }
            };
        }
        return model;
    }

    public String getUrl(String pviGuid) {
        String strReturn = "";
        // 1.获取活动的工作项
        List<WorkflowWorkItem> workflowWorkItems = getActivityWorkflowWorkItem(pviGuid);
        if (workflowWorkItems != null && !workflowWorkItems.isEmpty()) {// 存在活动工作项
            // 1.1工作项中的处理人是否有“我”
            for (WorkflowWorkItem workflowWorkItem : workflowWorkItems) {
                // 流转到“我”的工作流只允许“我”打开
                if (UserSession.getInstance().getUserGuid().equals(workflowWorkItem.getTransactor())) {
                    strReturn = workflowWorkItem.getHandleUrl();
                }
            }
        }
        return strReturn;
    }

    public AuditOrgaTakeoff getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditOrgaTakeoff();
        }
        return dataBean;
    }

    public void setDataBean(AuditOrgaTakeoff dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getisXiaoJiaModal() {
        if (auditisXiaoJiaModal == null) {
            auditisXiaoJiaModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "是否", null, true));
        }
        return this.auditisXiaoJiaModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getTypeModal() {
        if (auditTypeModal == null) {
            auditTypeModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("单选按钮组", "请假类别", null, false));
            auditTypeModal.add(new SelectItem("", "全部"));
        }
        return this.auditTypeModal;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getStatusModal() {
        if (auditStatusModal == null) {
            auditStatusModal = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "请假审批状态", null, true));
        }
        return this.auditStatusModal;
    }

    public String getSqstartDate() {
        return sqstartDate;
    }

    public void setSqstartDate(String sqstartDate) {
        this.sqstartDate = sqstartDate;
    }

    public String getSqendDate() {
        return sqendDate;
    }

    public void setSqendDate(String sqendDate) {
        this.sqendDate = sqendDate;
    }

    public String getQjstartDatefrom() {
        return qjstartDatefrom;
    }

    public void setQjstartDatefrom(String qjstartDatefrom) {
        this.qjstartDatefrom = qjstartDatefrom;
    }

    public String getQjendDatefrom() {
        return qjendDatefrom;
    }

    public void setQjendDatefrom(String qjendDatefrom) {
        this.qjendDatefrom = qjendDatefrom;
    }

    public String getQjstartDateto() {
        return qjstartDateto;
    }

    public void setQjstartDateto(String qjstartDateto) {
        this.qjstartDateto = qjstartDateto;
    }

    public String getQjendDateto() {
        return qjendDateto;
    }

    public void setQjendDateto(String qjendDateto) {
        this.qjendDateto = qjendDateto;
    }

    /**
     * 获取活动的工作项
     * 
     * @param pviGuid
     *            流程版本实例标识
     * @return List
     */
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

    /*
     * 根据中心标识和系统参数获取系统参数的值
     */
    public String getConfigValue(String centerguid, String configname) {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("centerguid", centerguid);
        sql.eq("configname", configname);
        AuditOrgaConfig config = auditconfig.getCenterConfig(sql.getMap()).getResult();
        if (config == null) {
            return "8";
        }
        else {
            return config.getConfigvalue();
        }
    }
}
