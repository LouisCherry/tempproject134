package com.epoint.auditqueue.auditznsbqueue.action;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueue.auditznsbqueue.api.IQueueList;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.sso.frame.domain.FrameUser;

/**
 * 今日取号list页面对应的后台
 * 
 * @author W J
 * @version [版本号, 2017-10-26 21:43:22]
 */
@RestController("jnqueuelistaction")
@Scope("request")
public class JNQueueListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AuditQueue dataBean;
    /**
     * 表格控件model
     */
    private DataGridModel<AuditQueue> model;

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
     * 取号人身份证
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
                    "qno,tasktypename,ouname,identitycardnum,phone,windowuser,window,windowname,getnotime,calltime,waittime,handletime,status",
                    "排队号,取号事项,部门,身份证,联系方式,窗口办理人员,办理窗口,窗口名称,取号时间,呼叫时间,等待时间,办理时间,状态");
        }
        return exportModel;
    }

    public DataGridModel<AuditQueue> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditQueue>()
            {

                @Override
                public List<AuditQueue> fetchData(int first, int pageSize, String sortField, String sortOrder) {
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
                    PageData<AuditQueue> pageData = auditqueue
                            .getQueueByPage(sql.getMap(), first, pageSize, "getnotime", sortOrder).getResult();
                    for (AuditQueue auditQueue : pageData.getList()) {

                        auditQueue.put("waittime",
                                getTimeDifference(auditQueue.getGetnotime(), auditQueue.getCalltime()));
                        auditQueue.put("handletime",
                                getTimeDifference(auditQueue.getCalltime(), auditQueue.getDate("finishtime")));
                        // 获取窗口人员名字
                        if (StringUtil.isNotBlank(auditQueue.getHandleuserguid())) {
                            auditQueue.put("windowuser",
                                    frameuser.getUserNameByUserGuid(auditQueue.getHandleuserguid()));
                        }
                        else {
                            auditQueue.put("windowuser", "");
                        }

                        // 获取办理窗口
                        if (StringUtil.isNotBlank(auditQueue.getHandlewindowguid())) {
                            AuditOrgaWindow auditOrgaWindow = windowservice
                                    .getWindowByWindowGuid(auditQueue.getHandlewindowguid()).getResult();
                            if (auditOrgaWindow != null) {
                                auditQueue.put("window", auditOrgaWindow.getWindowno());
                                auditQueue.put("windowname", auditOrgaWindow.getWindowname());
                            }
                            else {
                                auditQueue.put("window", "");
                                auditQueue.put("windowname", "");
                            }
                        }
                        else {
                            auditQueue.put("window", "");
                            auditQueue.put("windowname", "");
                        }

                        // 获取事项分类
                        if (StringUtil.isNotBlank(auditQueue.getTaskguid())) {
                            AuditQueueTasktype auditQueueTasktype = tasktypeservice
                                    .getAuditQueueTasktypeByRowguid(auditQueue.getTaskguid()).getResult();
                            if (StringUtil.isNotBlank(auditQueueTasktype)) {
                                auditQueue.put("tasktypename", auditQueueTasktype.getTasktypename());
                                FrameOu ou = ouservice.getOuByOuGuid(auditQueueTasktype.getOuguid());
                                if (ou != null) {
                                    auditQueue.put("ouname", ou.getOuname());
                                }
                                else {
                                    auditQueue.put("ouname", "");
                                }
                            }
                            else {
                                auditQueue.put("tasktypename", "");
                            }
                        }
                        else {
                            auditQueue.put("tasktypename", "");
                        }
                    }

                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public AuditQueue getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditQueue();
        }
        return dataBean;
    }

    public void setDataBean(AuditQueue dataBean) {
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
