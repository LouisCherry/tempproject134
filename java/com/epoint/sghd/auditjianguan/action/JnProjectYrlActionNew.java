package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import com.epoint.xmz.audittaskjn.api.IAuditTaskJnRenlingService;
import com.epoint.xmz.audittaskjn.api.entity.AuditTaskJnRenling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 部门已认领list页面对应的后台
 *应付监理-原版
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnprojectyrlactionnew")
@Scope("request")
public class JnProjectYrlActionNew extends BaseController {
    private static final long serialVersionUID = 1L;
    /**
     * 办件基本信息实体对象
     */
    private AuditProject dataBean = new AuditProject();
    /**
     * 事项基本信息实体对象
     */
    private AuditTask audittask = new AuditTask();

    /**
     * 办结类型
     */
    private List<SelectItem> banjieTypeModel = null;

    /**
     * 业务来源
     */
    private List<SelectItem> resourceModel = null;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProject> model;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTask audittaskServiec;

    /**
     * 办结时间
     */
    private String banjiedateStart;
    private String banjiedateEnd;

    @Autowired
    private IAuditTaskExtension audittaskExtensionServiec;

    // male alert sql实现service
    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Autowired
    private IAuditTaskJnRenlingService auditTaskJnRenlingService;

    private ExportModel exportModel;

    @Override
    public void pageLoad() {

    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>() {

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    sortField = " applyDate";
                    sortOrder = "desc";

                    String sqlFind = "select DISTINCT p.flowsn,p.projectname,p.rowguid,p.task_id,p.pviguid,p.ouname,p.applyername,p.banjiedate,p.banjieresult,p.renlingtime,p.taskguid from audit_project p where task_id in (";
                    String sqlNum = "select count(1) from audit_project p where task_id in (";
                    String ouGuid = userSession.getOuGuid();
                    String userguid = userSession.getUserGuid();

                    //查询此人配置过的taskids 集合
                    List<String> taskidlist = jnAuditJianGuanService.findTaskidListByUserguidAndOuguid(userguid, ouGuid);
                    StringBuilder taskids = new StringBuilder();
                    String task_ids = "";
                    if (!taskidlist.isEmpty()) {
                        for (String task_id : taskidlist) {
                            taskids.append("'").append(task_id).append("'").append(",");
                        }
                    }
                    if (taskids.length() >= 1) {
                        task_ids = taskids.substring(0, taskids.length() - 1);
                        task_ids += ")";
                    } else {
                        task_ids += "' ') ";
                    }

                    sqlFind = sqlFind + task_ids;
                    sqlNum = sqlNum + task_ids;

                    String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
                    sqlFind = sqlFind + " and p.handleareacode like '%" + handleareacode + "%' ";
                    sqlNum = sqlNum + " and p.handleareacode like '%" + handleareacode + "%' ";

                    // 如果是镇村接件
                    String area;
                    if (ZwfwUserSession.getInstance().getCitylevel() != null
                            && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    } else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sqlFind = sqlFind + " and p.areaCode = " + area + " ";
                    sqlNum = sqlNum + " and p.areaCode = " + area + " ";

                    //String key = "";

                    if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                        if ("%".equals(dataBean.getApplyername())) {
                            sqlFind = sqlFind + " and p.applyername like '%" + "\\%" + "%' ";
                            sqlNum = sqlNum + " and p.applyername like '%" + "\\%" + "%' ";
                            //key += "\\%&";
                        } else {
                            sqlFind = sqlFind + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
                            sqlNum = sqlNum + " and p.applyername like '%" + dataBean.getApplyername() + "%' ";
                            //key += dataBean.getApplyername() + "&";
                        }
                    }

                    if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                        //处理百分号
                        if ("%".equals(dataBean.getProjectname())) {
                            sqlFind = sqlFind + " and p.projectname like '%  " + "\\%" + "%' ";
                            sqlNum = sqlNum + " and p.projectname like '%  " + "\\%" + "%' ";
                            //key += "\\%&";
                        } else {
                            sqlFind = sqlFind + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                            sqlNum = sqlNum + " and p.projectname like '%" + dataBean.getProjectname() + "%' ";
                            //key += dataBean.getProjectname() + "&";
                        }
                    }
                    if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
                        //处理百分号
                        if ("%".equals(dataBean.getFlowsn())) {
                            sqlFind = sqlFind + " and p.flowsn like '%" + "\\%" + "%' ";
                            sqlNum = sqlNum + " and p.flowsn like '%" + "\\%" + "%' ";
                            //key += "\\%&";
                        } else {
                            sqlFind = sqlFind + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                            sqlNum = sqlNum + " and p.flowsn like '%" + dataBean.getFlowsn() + "%' ";
                            //key += dataBean.getFlowsn() + "&";
                        }
                    }

                    if (StringUtil.isNotBlank(dataBean.getStr("renling_username"))) {
                        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                                + dataBean.getStr("renling_username") + "%')";
                        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and r.renling_username like '%"
                                + dataBean.getStr("renling_username") + "%')";
                        //key += dataBean.getStr("renling_username") + "&";
                    }

                    if (StringUtil.isNotBlank(dataBean.get("renlingdateStart"))) {
                        Date renlingdateStart = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateStart"));
                        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                                + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
                        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) >= '"
                                + EpointDateUtil.convertDate2String(renlingdateStart) + "')";
                        //key += EpointDateUtil.convertDate2String(renlingdateStart) + "&";
                    }

                    if (StringUtil.isNotBlank(dataBean.get("renlingdateEnd"))) {
                        Date renlingdateEnd = EpointDateUtil.getBeginOfDate(dataBean.getDate("renlingdateEnd"));
                        sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                                + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
                        sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '' and date(r.renlingdate) <= '"
                                + EpointDateUtil.convertDate2String(renlingdateEnd) + "')";
                        //key += EpointDateUtil.convertDate2String(renlingdateEnd) + "&";
                    }

                    //加标识位，已认领模块
                    //key += "yrl";

                    sqlFind = sqlFind + " and p.status = 90 ";
                    sqlNum = sqlNum + " and p.status = 90 ";

                    // 认领时间不为空
                    sqlFind += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";
                    sqlNum += " and EXISTS (select 1 from audit_task_jn_renling r WHERE p.task_id = r.task_id AND p.RowGuid = r.projectguid AND r.renling_ouguid = '" + ouGuid + "' AND r.renlingdate IS NOT NULL AND r.renlingdate <> '')";

                    // 系统参数，审管互动认领时间起始
                    String sghdstarttime = ConfigUtil.getFrameConfigValue("sghdstarttime");
                    if (StringUtil.isNotBlank(sghdstarttime)) {
                        sqlFind += " and p.applyDate >= '" + sghdstarttime + "'";
                        sqlNum += " and p.applyDate >= '" + sghdstarttime + "'";
                    }

                    sqlFind = sqlFind + " order by p." + sortField + " " + sortOrder + " limit ?,? ";

                    List<AuditProject> list = new ArrayList<>();

                    /*List<AuditProject> cachelist = EHCacheUtil.get(key);
                    if (cachelist != null) {
                        list = cachelist;
                    } else {
                        list = jnAuditJianGuanService.getTaProjectWrlInfo(sqlFind, first, pageSize);
                        EHCacheUtil.put(key, list, 60000);
                    }*/

                    list = jnAuditJianGuanService.getTaProjectWrlInfo(sqlFind, first, pageSize);

                    int count = 0;
                    if (list != null) {
                        String renlingSql = "select * from audit_task_jn_renling where task_id=? and projectguid=? and renling_ouguid=? AND renlingdate IS NOT NULL AND renlingdate <> '' order by renlingdate desc LIMIT 1";
                        for (AuditProject auditProject : list) {
                            setAudittask(
                                    audittaskServiec.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult());
                            AuditTaskExtension auditTaskExtension = audittaskExtensionServiec
                                    .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                            if (auditTaskExtension != null) {
                                if (ZwfwConstant.CONSTANT_STR_ONE
                                        .equals(auditTaskExtension.getIszijianxitong().toString())) {
                                    auditProject.put("projectname", "【自建系统】" + auditProject.getProjectname());
                                }
                            }

                            // 新增字段“部门名称”的显示
                            String taskguid = auditProject.getTaskguid();
                            auditProject.put("ouname", jnAuditJianGuanService.getOuNameFromAuditTask(taskguid));
                            if (StringUtil.isBlank(auditProject.getStr("jianguantime"))) {
                                auditProject.put("flag", "true");
                            }

                            AuditTaskJnRenling auditTaskJnRenling = auditTaskJnRenlingService.find(renlingSql,
                                    new Object[]{auditProject.getTask_id(), auditProject.getRowguid(), ouGuid});
                            if (auditTaskJnRenling != null) {
                                auditProject.put("renling_username", auditTaskJnRenling.getRenling_username());
                                auditProject.set("renlingdate", EpointDateUtil.convertDate2String(
                                        auditTaskJnRenling.getRenlingdate(), EpointDateUtil.DATE_TIME_FORMAT));
                            }
                        }
                        count = jnAuditJianGuanService.getTaProjectWrlNum(sqlNum);
                    }
                    this.setRowCount(count);
                    return list;
                }
            };
        }
        return model;
    }

    /**
     * 办结类型
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<SelectItem> getBanjieTypeModel() {
        if (banjieTypeModel == null) {
            banjieTypeModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "办结类型", null, true));
        }
        return this.banjieTypeModel;
    }

    /**
     * 业务来源
     *
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<SelectItem> getResourceModel() {
        if (resourceModel == null) {
            resourceModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "业务来源", null, true));
        }
        return this.resourceModel;
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
                    "flowsn,projectname,ouname,applyername,banjiedate,renlingdate,renling_username,jianguantime",
                    "办件编号,事项名称,审批部门,申请人,办结时间,认领时间,认领人,交流发起时间");
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

    public AuditTask getAudittask() {
        return audittask;
    }

    public void setAudittask(AuditTask audittask) {
        this.audittask = audittask;
    }

}
