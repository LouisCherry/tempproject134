package com.epoint.auditqueue.auditznsbqueue.action;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueue.auditznsbqueue.api.IQueueList;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuehostory.domain.AuditQueueHistory;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.sso.frame.domain.FrameUser;

/**
 * 历史取号list页面对应的后台
 * 
 * @author wangjie
 * @version [版本号, 2017-10-26 21:43:22]
 */
@RestController("jnhistoryqueuelistaction")
@Scope("request")
public class JNHistoryQueueListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = 4962586378866551098L;
    private AuditQueueHistory dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditQueueHistory> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 办理号
     */
    private String qno;
    /**
     * 办事人联系方式
     */
    private String phone;
    /**
     * 取号时间
     */
    private String qhdateStart;
    private String qhdateEnd;
    /**
     * 窗口人员
     */
    private String useridcard;

    @Autowired
    private IAuditQueue auditqueue;
    @Autowired
    private IUserService frameuser;
    @Autowired
    private IAuditOrgaWindow windowservice;
    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IQueueList queuelistservice;
    private String tasktypename;
    private String ouguid;
    private String windowname;
    // 窗口人员姓名
    private String handleusername;
    private String calltimeStart;
    private String calltimeEnd;

    public void pageLoad() {

    }

    // 导出
    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel(
                    "qno,tasktypename,identitycardnum,phone,windowuser,window,getnotime,calltime,status",
                    "排队号,取号事项,身份证,联系方式,窗口办理人员,办理窗口,取号时间,呼叫时间,状态");
        }
        return exportModel;
    }

    public DataGridModel<AuditQueueHistory> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditQueueHistory>()
            {

                @Override
                public List<AuditQueueHistory> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("centerguid", ZwfwUserSession.getInstance().getCenterGuid());

                    // 查询办理号
                    if (StringUtil.isNotBlank(qno)) {
                        sql.like("qno", qno);
                    }
                    // 查询联系方式
                    if (StringUtil.isNotBlank(phone)) {
                        sql.like("phone", phone);
                    }
                    // 取号时间
                    if (StringUtil.isNotBlank(qhdateStart)) {
                        sql.ge("getnotime", EpointDateUtil.getBeginOfDateStr(qhdateStart));
                    }
                    if (StringUtil.isNotBlank(qhdateEnd)) {
                        sql.lt("getnotime", EpointDateUtil.getEndOfDateStr(qhdateEnd));
                    }
                    // 叫号时间
                    if (StringUtil.isNotBlank(calltimeStart)) {
                        sql.ge("calltime", EpointDateUtil.getBeginOfDateStr(calltimeStart));
                    }
                    if (StringUtil.isNotBlank(calltimeEnd)) {
                        sql.lt("calltime", EpointDateUtil.getEndOfDateStr(calltimeEnd));
                    }
                    // 取号人身份证
                    if (StringUtil.isNotBlank(useridcard)) {
                        sql.like("IDENTITYCARDNUM", useridcard);
                    }
                    // 查询事项名称
                    if (StringUtil.isNotBlank(tasktypename)) {
                        SqlConditionUtil tasktypenamesql = new SqlConditionUtil();
                        tasktypenamesql.like("tasktypename", tasktypename);
                        List<AuditQueueTasktype> tasktypelist = tasktypeservice.getAllTasktype(tasktypenamesql.getMap())
                                .getResult();
                        if (!tasktypelist.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (AuditQueueTasktype tasktype : tasktypelist) {
                                sb.append(tasktype.getRowguid()).append(",");
                            }
                            sql.in("taskguid", "'" + sb.substring(0, sb.length() - 1).replace(",", "','") + "'");
                        }
                        else {
                            sql.eq("taskguid", "none");
                        }
                    }
                    // 查询窗口
                    if (StringUtil.isNotBlank(windowname)) {
                        SqlConditionUtil windowsql = new SqlConditionUtil();
                        windowsql.like("windowname", windowname);
                        List<AuditOrgaWindow> windowlist = windowservice.getAllWindow(windowsql.getMap()).getResult();
                        if (!windowlist.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (AuditOrgaWindow window : windowlist) {
                                sb.append(window.getRowguid()).append(",");
                            }
                            sql.in("handlewindowguid",
                                    "'" + sb.substring(0, sb.length() - 1).replace(",", "','") + "'");
                        }
                        else {
                            sql.eq("handlewindowguid", "none");
                        }
                    }
                    // 查询部门
                    if (StringUtil.isNotBlank(ouguid)) {
                        SqlConditionUtil ouguidsql = new SqlConditionUtil();
                        ouguidsql.eq("ouguid", ouguid);

                        List<AuditQueueTasktype> tasktypelist = tasktypeservice.getAllTasktype(ouguidsql.getMap())
                                .getResult();
                        if (!tasktypelist.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (AuditQueueTasktype tasktype : tasktypelist) {
                                sb.append(tasktype.getRowguid()).append(",");
                            }
                            sql.in("taskguid", "'" + sb.substring(0, sb.length() - 1).replace(",", "','") + "'");
                        }
                        else {
                            sql.eq("taskguid", "none");
                        }
                    }
                    // 查询窗口人员姓名
                    if (StringUtil.isNotBlank(handleusername)) {
                        SqlConditionUtil handleusersql = new SqlConditionUtil();
                        handleusersql.like("displayname", handleusername);
                        List<FrameUser> frameuserlist = queuelistservice.getZnsbQueueHandleUser(handleusersql.getMap())
                                .getResult();
                        if (!frameuserlist.isEmpty()) {
                            StringBuilder sb = new StringBuilder();
                            for (FrameUser user : frameuserlist) {
                                sb.append(user.getUserGuid()).append(",");
                            }
                            sql.in("handleuserguid", "'" + sb.substring(0, sb.length() - 1).replace(",", "','") + "'");
                        }
                        else {
                            sql.eq("handleuserguid", "none");
                        }
                    }
                    PageData<AuditQueueHistory> pageData = auditqueue
                            .getQueueHistoryByPage(sql.getMap(), first, pageSize, "getnotime", sortOrder).getResult();
                    for (AuditQueueHistory auditQueueHistory : pageData.getList()) {
                        auditQueueHistory.put("waittime",
                                getTimeDifference(auditQueueHistory.getGetnotime(), auditQueueHistory.getCalltime()));
                        auditQueueHistory.put("handletime", getTimeDifference(auditQueueHistory.getCalltime(),
                                auditQueueHistory.getDate("finishtime")));
                        // 获取窗口人员名字
                        if (StringUtil.isNotBlank(auditQueueHistory)) {
                            auditQueueHistory.put("windowuser",
                                    frameuser.getUserNameByUserGuid(auditQueueHistory.getHandleUserGuid()));
                        }
                        else {
                            auditQueueHistory.put("windowuser", "");
                        }

                        // 获取办理窗口
                        if (StringUtil.isNotBlank(auditQueueHistory.getHandlewindowguid())) {
                            AuditOrgaWindow auditOrgaWindow = windowservice
                                    .getWindowByWindowGuid(auditQueueHistory.getHandlewindowguid()).getResult();
                            if (StringUtil.isNotBlank(auditOrgaWindow)) {
                                auditQueueHistory.put("window", auditOrgaWindow.getWindowno());
                                auditQueueHistory.put("windowname", auditOrgaWindow.getWindowname());
                            }
                            else {
                                auditQueueHistory.put("window", "");
                                auditQueueHistory.put("windowname", "");
                            }
                        }
                        else {
                            auditQueueHistory.put("window", "");
                        }
                        // 获取事项分类
                        if (StringUtil.isNotBlank(auditQueueHistory.getTaskguid())) {
                            AuditQueueTasktype auditQueueTasktype = tasktypeservice
                                    .getAuditQueueTasktypeByRowguid(auditQueueHistory.getTaskguid()).getResult();
                            if (StringUtil.isNotBlank(auditQueueTasktype)) {
                                auditQueueHistory.put("tasktypename", auditQueueTasktype.getTasktypename());
                                FrameOu ou = ouservice.getOuByOuGuid(auditQueueTasktype.getOuguid());
                                if (ou != null) {
                                    auditQueueHistory.put("ouname", ou.getOuname());
                                }
                                else {
                                    auditQueueHistory.put("ouname", "");
                                }
                            }
                            else {
                                auditQueueHistory.put("tasktypename", "");
                            }
                        }
                        else {
                            auditQueueHistory.put("tasktypename", "");
                        }
                    }

                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public AuditQueueHistory getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQueueHistory();
        }
        return dataBean;
    }

    public void setDataBean(AuditQueueHistory dataBean) {
        this.dataBean = dataBean;
    }

    public String getQno() {
        return qno;
    }

    public void setQno(String qno) {
        this.qno = qno;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQhdateStart() {
        return qhdateStart;
    }

    public void setQhdateStart(String qhdateStart) {
        this.qhdateStart = qhdateStart;
    }

    public String getQhdateEnd() {
        return qhdateEnd;
    }

    public void setQhdateEnd(String qhdateEnd) {
        this.qhdateEnd = qhdateEnd;
    }

    public String getUseridcard() {
        return useridcard;
    }

    public void setUseridcard(String useridcard) {
        this.useridcard = useridcard;
    }

    public String getTasktypename() {
        return tasktypename;
    }

    public void setTasktypename(String tasktypename) {
        this.tasktypename = tasktypename;
    }

    public String getOuguid() {
        return ouguid;
    }

    public void setOuguid(String ouguid) {
        this.ouguid = ouguid;
    }

    public String getWindowname() {
        return windowname;
    }

    public void setWindowname(String windowname) {
        this.windowname = windowname;
    }

    public String getHandleusername() {
        return handleusername;
    }

    public void setHandleusername(String handleusername) {
        this.handleusername = handleusername;
    };

    public String getCalltimeStart() {
        return calltimeStart;
    }

    public void setCalltimeStart(String calltimeStart) {
        this.calltimeStart = calltimeStart;
    }

    public String getCalltimeEnd() {
        return calltimeEnd;
    }

    public void setCalltimeEnd(String calltimeEnd) {
        this.calltimeEnd = calltimeEnd;
    }

    public String getTimeDifference(Date startDate, Date endDate) {
        // 计算时间差（单位：毫秒）
        if (startDate == null || endDate == null) {
            return "";
        }
        long timeDifference = endDate.getTime() - startDate.getTime();

        // 将毫秒转换为分钟和秒数
        long seconds = timeDifference / 1000 % 60; // 计算秒数
        long minutes = timeDifference / (60 * 1000) % 60; // 计算分钟数
        long hours = timeDifference / (60 * 60 * 1000); // 计算小时数
        String time = "";
        if (hours == 0) {
            if (minutes == 0) {
                time = seconds + " 秒";
            }
            else {

                time = minutes + " 分钟 " + seconds + " 秒";
            }
        }
        else {
            time = hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒";
        }
        return time;
    }
}
