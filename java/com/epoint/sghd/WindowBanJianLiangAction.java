package com.epoint.sghd;

import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;
import com.epoint.sghd.commonutil.api.IJnSghdCommonutil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 登记页面的办件量页面action
 *
 * @version [版本号, 2018年10月15日]
 * @作者 shibin
 */
@SuppressWarnings("unchecked")
@RestController("windowbanjianliangaction")
@Scope("request")
public class WindowBanJianLiangAction extends BaseController {
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

    @Autowired
    private IAuditOrgaWindow windowService;

    @Autowired
    private IJnSghdCommonutil sghdService;

    /**
     * 办结时间
     */
    private String banjiedateStart;
    private String banjiedateEnd;

    @Autowired
    private IAuditTaskExtension audittaskExtensionServiec;

    @Override
    public void pageLoad() {
    }

    public DataGridModel<AuditProject> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProject>() {

                @Override
                public List<AuditProject> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    sortField = " banjiedate";
                    sortOrder = "desc";

                    String userGuid = "";
                    SqlConditionUtil sql = new SqlConditionUtil();

                    // 窗口人员登陆时，可以获得窗口的windowguid
                    String windowguid = ZwfwUserSession.getInstance().getWindowGuid();

                    String areaCode = ZwfwUserSession.getInstance().getAreaCode();

                    String ouGuid = userSession.getOuGuid();

                    String handleareacode = ZwfwUserSession.getInstance().getAreaCode();
                    sql.like("handleareacode", handleareacode + ",");
                    // 如果是镇村接件
                    String area;
                    if (ZwfwUserSession.getInstance().getCitylevel() != null
                            && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                            .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                        area = ZwfwUserSession.getInstance().getBaseAreaCode();
                    } else {
                        area = ZwfwUserSession.getInstance().getAreaCode();
                    }
                    sql.eq("areacode", area);

                    if (StringUtil.isNotBlank(dataBean.getProjectname())) {
                        sql.like("projectname", dataBean.getProjectname());
                    }
                    if (StringUtil.isNotBlank(dataBean.getApplyername())) {
                        sql.like("applyername", dataBean.getApplyername());
                    }
                    if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
                        sql.like("flowsn", dataBean.getFlowsn());
                    }

                    if (dataBean.getBanjieresult() != null) {
                        sql.eq("banjieresult", dataBean.getBanjieresult().toString());
                    }
                    if (StringUtil.isNotBlank(banjiedateStart)) {
                        sql.ge("banjiedate", EpointDateUtil.getBeginOfDateStr(banjiedateStart));
                    }
                    if (StringUtil.isNotBlank(banjiedateEnd)) {
                        sql.le("banjiedate", EpointDateUtil.getEndOfDateStr(banjiedateEnd));
                    }

                    sql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));

                    String taskids = "";
                    List<String> taskidList = new ArrayList<>();

                    if (StringUtil.isNotBlank(windowguid)) {

                        taskidList = windowService.getTaskidsByWindow(ZwfwUserSession.getInstance().getWindowGuid())
                                .getResult();
                        // 窗口人员登录，查看本窗口事项
                        sql.eq("windowguid", windowguid);
                        userGuid = userSession.getUserGuid();
                        //sql.eq("ouguid", userSession.getOuGuid());
                    } else {
                        // 部门负责人登录，查看本部门的事项
                        sql.eq("ouguid", ouGuid);
                        String sqlProject = "SELECT task_id from audit_project WHERE AREACODE = ? and ouguid = ?";
                        List<String> projectList = sghdService.findList(sqlProject, String.class, areaCode, ouGuid);
                        for (String str : projectList) {
                            taskidList.add(str);
                        }
                    }

                    String fields = "rowguid,taskguid,projectname,applyername,pviguid,flowsn,banjieresult,banjiedate";
                    PageData<AuditProject> pageProject = sghdService.getAuditProjectPageData(fields,
                            sql.getMap(), first, pageSize, sortField, sortOrder, userGuid).getResult();
                    List<AuditProject> list = new ArrayList<>();
                    int count = 0;
                    if (pageProject != null) {
                        list = pageProject.getList();
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
                            String sqlPromise = "SELECT ouname FROM audit_task WHERE rowguid = ? ";
                            String ouname = sghdService.queryString(sqlPromise, taskguid);
                            auditProject.put("ouname", ouname);

                        }
                        count = pageProject.getRowCount();
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
