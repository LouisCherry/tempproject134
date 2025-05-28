package com.epoint.jntongji.action;

import java.util.ArrayList;
import java.util.List;

import com.epoint.auditproject.auditproject.api.IJNAuditProject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.CommonUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ValidateUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.entity.EvainstanceCk;
import com.epoint.evainstanceck.api.IEvainstanceCkService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 业务管理list页面对应的后台
 *
 * @author WeiY
 * @version [版本号, 2016-09-26 11:13:04]
 */
@RestController("jnreportprojectinfobyoulistaction")
@Scope("request")
public class JNReportProjectInfoByOUList extends BaseController
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
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    private ExportModel exportModel;

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
     * 办件状态
     */
    private String status;
    /**
     * 监控权限，地址传参
     */
    private String ouguid;

    private String areacode;

    private boolean chaoshi = false;

    private String flowsn;
    private String projectname;
    private String receiveusername;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectSparetime projectSparetimeService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    IWFInstanceAPI9 iwf9;

    @Autowired
    IAuditProjectNumber auditProjectNumber;
    @Autowired
    private IAuditTask iAuditTask;
    // 评价api
    @Autowired
    private IEvainstanceCkService iEvainstanceService;
    @Autowired
    private IOuService iOuService;

    @Autowired
    private IJNAuditProject ijnAuditProjectService;

    // 日志
    transient Logger log = LogUtil.getLog(JNReportProjectInfoByOUList.class);

    @Override
    public void pageLoad() {
        ouguid = this.getRequestParameter("ouguid");
        areacode = getRequestParameter("areacode");
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>()
            {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings({"unchecked", "rawtypes", "deprecation" })
                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    ArrayList list = new ArrayList();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    // 11、申请时间
                    applyDateStart = getRequestParameter("startdate");
                    applyDateEnd = getRequestParameter("enddate");
                    if (StringUtil.isNotBlank(applyDateStart)) {
                        sql.ge("applydate", EpointDateUtil.getBeginOfDateStr(applyDateStart));
                    }
                    if (StringUtil.isNotBlank(applyDateEnd)) {
                        sql.lt("applydate", EpointDateUtil.getEndOfDateStr(applyDateEnd));
                    }
                    if (StringUtil.isNotBlank(areacode) && !"undefined".equals(areacode)) {
                        if(!"ALL".equals(areacode)){
                            sql.eq("a.areacode", areacode);
                            sql.eq("b.areacode",areacode);
                        }
                    }
                    else {
                        sql.eq("a.areacode", ZwfwUserSession.getInstance().getAreaCode());
                        sql.eq("b.areacode",ZwfwUserSession.getInstance().getAreaCode());
                    }
                    if (!"ALL".equals(ouguid) && StringUtil.isNotBlank(ouguid)) {
                        sql.eq("a.ouguid", ouguid);
                    }

                    // 新增查询条件
                    if (StringUtil.isNotBlank(flowsn)) {
                        sql.like("flowsn", flowsn);
                    }
                    if (StringUtil.isNotBlank(projectname)) {
                        sql.like("projectname", projectname);
                    }
                    if (StringUtil.isNotBlank(receiveusername)) {
                        sql.like("receiveusername", receiveusername);
                    }
                    sql.setInnerJoinTable("frame_ou_extendinfo b","a.ouguid","b.ouguid");
                    sql.eq("b.IS_WINDOWOU","1");

                    sql.setOrder(sortField, sortOrder);
                    sql.setSelectFields(
                            "a.*");
                    String type = getRequestParameter("type");
                    if (StringUtil.isNotBlank(type)) {
                        switch (type) {
                            case "allnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
                                sql.ge("status", status);
                                sql.nq("status","20");
                                break;
                            case "slnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL);
                                sql.ge("status", status);
                                break;
                            case "cqslnum":
                                sql.eq("ISOVERTIMESHOULI","1");
                                break;
                            case "bslnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL);
                                sql.eq("status", status);
                                break;
                            case "bxknum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG);
                                sql.eq("status", status);
                                break;
                            case "wwnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL);
                                sql.ge("status", status);
                                sql.nq("applyway", "20");
                                break;
                            case "bjnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
                                sql.ge("status", status);
                                break;
                            case "cqbjnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
                                sql.ge("status", status);
                                sql.isNotBlank("PROMISEENDDATE");
                                sql.gt("PROMISEENDDATE","'1800-01-01'");
                                sql.gt("banjiedate-PROMISEENDDATE","0");

                                sql.nq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL));
                                break;
                            case "aqbjnum":
                                status = String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
                                sql.eq("b.IS_WINDOWOU","1' and  !(STATUS >= 90 and status != 97 and PROMISEENDDATE is not null  and BANJIEDATE > PROMISEENDDATE and PROMISEENDDATE>'1800-01-01') and '1'='1");
                                sql.ge("status", status);
                                break;
                            default:
                                break;
                        }
                    }


                    PageData<AuditProject> pageData = ijnAuditProjectService
                            .getAuditProjectPageData(sql.getMap(), first, pageSize, "", "")
                            .getResult();
                    List<AuditProject> pagelist = new ArrayList<AuditProject>();
                    if (pageData != null) {
                        pagelist = pageData.getList();

                        // 新增显示字段
                        pagelist.forEach(x -> {
                            AuditTask task = iAuditTask.getAuditTaskByGuid(x.getTaskguid(), false).getResult();
                            x.put("item_id", task.getItem_id());
                            EvainstanceCk record = (EvainstanceCk) iEvainstanceService.findByflowsn(x.getFlowsn());
                            if (ValidateUtil.isNotNull(record)) {
                                x.put("satisfactiondate", record.getCreatedate());
                                x.put("satisfaction", record.getSatisfaction());
                            }
                            if (ValidateUtil.isNotNull(x.getReceivedate())
                                    && ValidateUtil.isNotNull(x.getBanjiedate())) {
                                x.put("processtime", EpointDateUtil.dateDiffCompare(x.getBanjiedate(),
                                        x.getReceivedate(), "min"));
                            }

                            // 查询ouname为null的用ouguid转换
                            if (x.getOuname() == null || StringUtil.isBlank(x.getOuname())) {
                                FrameOu ou = iOuService.getOuByOuGuid(x.getOuguid());
                                if (ou != null) {
                                    x.setOuname(ou.getOuname());
                                }
                            }
                        });

                        List<AuditProject> delpagelist = new ArrayList<AuditProject>();
                        int pagecount = pageData.getRowCount();
                        if (chaoshi) {
                            for (AuditProject auditProject : pagelist) {
                                String spareTime = getSpareTime(auditProject.getRowguid(),
                                        auditProject.getStatus());
                                auditProject.put("sparetime", spareTime);
                                // 方便导出
                                if (StringUtil.isNotBlank(spareTime)) {
                                    String[] split = spareTime.split("<img");
                                    auditProject.put("sysj", split[0]);
                                }
                                if (!spareTime.startsWith("超时")) {
                                    delpagelist.add(auditProject);
                                }
                            }
                            pagelist.removeAll(delpagelist);
                            pagecount = pagecount - delpagelist.size();
                        }
                        else {
                            for (AuditProject auditProject : pageData.getList()) {
                                String spareTime = getSpareTime(auditProject.getRowguid(),
                                        auditProject.getStatus());
                                auditProject.put("sparetime", spareTime);
                                // 方便导出
                                if (StringUtil.isNotBlank(spareTime)) {
                                    String[] split = spareTime.split("<img");
                                    auditProject.put("sysj", split[0]);
                                }
                            }
                        }
                        this.setRowCount(pagecount);
                    }
                    return pagelist;
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
        // 默认剩余时间
        String defaultSparaeTime = "";
        if (StringUtil.isNotBlank(rowguid) && StringUtil.isNotBlank(status)) {
            String fields = " rowguid,taskguid,projectname,tasktype ";
            AuditProject auditProject = null;
            if("ALL".equals(areacode)){
                auditProject = auditProjectService.getAuditProjectByRowGuid(fields, rowguid, null)
                        .getResult();
            }else{
                auditProject = auditProjectService.getAuditProjectByRowGuid(fields, rowguid, areacode)
                        .getResult();
            }

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
            else {// 非承诺件
                result = ZwfwConstant.getItemtypeKey(auditProject.getTasktype().toString());
            }
        }
        else {
            result = defaultSparaeTime;
        }
        return result;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "windowname,ouname,projectname,item_id,flowsn,applyername,applydate,receivedate,receiveusername,ACCEPTUSERDATE,acceptusername,banjieusername,sysj,banjiedate,processtime,status",
                    "窗口名称,办件所属部门,事项名称,事项编码,办件编号,申请人,申请时间,接件时间,接件人员,受理时间,受理人员,审核人员,办结剩余时间,办结时间,办件时长,办件状态");
        }
        return exportModel;
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

    public int getIsmaterial() {
        return ismaterial;
    }

    public void setIsmaterial(int ismaterial) {
        this.ismaterial = ismaterial;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFlowsn() {
        return flowsn;
    }

    public void setFlowsn(String flowsn) {
        this.flowsn = flowsn;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getReceiveusername() {
        return receiveusername;
    }

    public void setReceiveusername(String receiveusername) {
        this.receiveusername = receiveusername;
    }

}
