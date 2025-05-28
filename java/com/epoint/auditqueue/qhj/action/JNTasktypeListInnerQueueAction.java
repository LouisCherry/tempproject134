package com.epoint.auditqueue.qhj.action;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditqueue.qhj.api.IJNTasktypeList;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.inter.IAuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;

@RestController("jntasktypelistinnerqueueaction")
@Scope("request")
public class JNTasktypeListInnerQueueAction extends BaseController
{
    private static final long serialVersionUID = 1L;
    @Autowired
    private IAuditQueueTasktype tasktypeservice;
    @Autowired
    private IAuditOrgaWindowYjs windowservice;
    @Autowired
    private IAuditQueueWindowTasktype queuewindowservice;
    @Autowired
    private IHandleQueue handlequeueservice;
    @Autowired
    private IAuditQueueXianhaotime xianhaoservice;
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    @Autowired
    private IAuditQueue queueservice;
    @Autowired
    private IOuService ouservice;
    @Autowired
    private IJNTasktypeList jntasktypeservice;
    private String ouguid;
    private String Centerguid;
    private String hallguid;
    private String largeTasktypeGuid;
    private final int pagesize = 10;

    private Calendar c = Calendar.getInstance();

    @Override
    public void pageLoad() {
        ouguid = getRequestParameter("ouguid");
        Centerguid = getRequestParameter("Centerguid");
        hallguid = getRequestParameter("hallguid");
        largeTasktypeGuid = getRequestParameter("largetasktypeguid");
        if (StringUtil.isNotBlank(largeTasktypeGuid)) {
            Integer total = tasktypeservice.getCountbyLargeTasktypeGuid(largeTasktypeGuid).getResult();
            int pages = total % pagesize == 0 ? (total / pagesize) : (total / pagesize + 1);
            addCallbackParam("pages", pages);
        }
        else if (StringUtil.isNotBlank(ouguid)) {
            Integer total = jntasktypeservice.getWindowLinkedTskTypeCount(Centerguid, ouguid);
            int pages = total % pagesize == 0 ? (total / pagesize) : (total / pagesize + 1);
            addCallbackParam("pages", pages);
        }
        else {
            addCallbackParam("pages", getTaskTypePagesbyHallguid(hallguid, Centerguid));
        }

    }

    public int getTaskTypePagesbyHallguid(String hallguid, String centerguid) {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("centerguid", centerguid);
        if (!"all".equals(hallguid)) {
            sql.eq("lobbytype", hallguid);
        }
        sql.eq("IS_USEQUEUE", QueueConstant.Common_yes_String);
        // 根据（lobbytype和centerguid、是否窗口部门来）查出auditorgawindow里面的数据
        List<AuditOrgaWindow> windows = windowservice.getAllWindow(sql.getMap()).getResult();

        String windowguids = "";
        for (AuditOrgaWindow auditOrgaWindow : windows) {
            windowguids += auditOrgaWindow.getRowguid() + "','";
        }
        windowguids = "'" + windowguids + "'";

        SqlConditionUtil sql1 = new SqlConditionUtil();
        sql1.in("windowguid", windowguids);
        // auditorgawindow.rowguid用in来找出auditqueuewindowtasktype里面的数据
        List<AuditQueueWindowTasktype> queuewindows = queuewindowservice.getAllWindowTasktype(sql1.getMap())
                .getResult();

        String tasktypeguids = "";
        for (AuditQueueWindowTasktype auditQueueWindowTasktype : queuewindows) {
            tasktypeguids += auditQueueWindowTasktype.getTasktypeguid() + "','";
        }
        tasktypeguids = "'" + tasktypeguids + "'";

        SqlConditionUtil sql2 = new SqlConditionUtil();
        sql2.in("rowguid", tasktypeguids);
        sql2.eq("centerguid", centerguid);
        Integer total = tasktypeservice.getAuditQueueTasktypeCount(sql2.getMap(), " rowguid,TaskTypeName");
        return total % pagesize == 0 ? (total / pagesize) : (total / pagesize + 1);
    }

    public void getDataJson() {
        if (StringUtil.isNotBlank(largeTasktypeGuid)) {
            addCallbackParam("html",
                    getTaskTypebyLargeTaskGuid(largeTasktypeGuid, Integer.parseInt(getRequestParameter("cur"))));
        }
        else if (StringUtil.isNotBlank(ouguid)) {
            addCallbackParam("html", getTaskTypePagesDatabyOuguid("department", ouguid, Centerguid,
                    Integer.parseInt(getRequestParameter("cur"))));
        }
        else {
            addCallbackParam("html", getTaskTypePagesDatabyHallguid("Centerguid", hallguid, Centerguid,
                    Integer.parseInt(getRequestParameter("cur"))));
        }

    }

    private String getTaskTypebyLargeTaskGuid(String largeTasktypeGuid, int curPage) {
        String taskwaitnum = "0";
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("largetasktypeguid", largeTasktypeGuid);
        List<AuditQueueTasktype> lists = tasktypeservice.getAuditQueueTasktypeByPage("rowguid,tasktypename",
                sql.getMap(), Integer.valueOf(curPage) * pagesize, pagesize, "ordernum", "desc").getResult().getList();
        if (null != lists && !lists.isEmpty()) {
            for (AuditQueueTasktype tasktype : lists) {
                taskwaitnum = StringUtil
                        .getNotNullString(handlequeueservice.getTaskWaitNum(tasktype.getRowguid(), true).getResult());
                tasktype.put("taskwaitnum", taskwaitnum);
            }
        }

        return JsonUtil.listToJson(lists);
    }

    public String getTaskTypePagesDatabyHallguid(String cate, String hallguid, String centerguid, int curPage) {
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("centerguid", centerguid);
        if (!"all".equals(hallguid)) {
            sql.eq("lobbytype", hallguid);
        }
        sql.eq("IS_USEQUEUE", QueueConstant.Common_yes_String);
        // 根据（lobbytype和centerguid、是否窗口部门来）查出auditorgawindow里面的数据
        List<AuditOrgaWindow> windows = windowservice.getAllWindow(sql.getMap()).getResult();

        String windowguids = "";
        for (AuditOrgaWindow auditOrgaWindow : windows) {
            windowguids += auditOrgaWindow.getRowguid() + "','";
        }
        windowguids = "'" + windowguids + "'";

        SqlConditionUtil sql1 = new SqlConditionUtil();
        sql1.in("windowguid", windowguids);
        // auditorgawindow.rowguid用in来找出auditqueuewindowtasktype里面的数据
        List<AuditQueueWindowTasktype> queuewindows = queuewindowservice.getAllWindowTasktype(sql1.getMap())
                .getResult();

        String tasktypeguids = "";
        for (AuditQueueWindowTasktype auditQueueWindowTasktype : queuewindows) {
            tasktypeguids += auditQueueWindowTasktype.getTasktypeguid() + "','";
        }
        tasktypeguids = "'" + tasktypeguids + "'";

        SqlConditionUtil sql2 = new SqlConditionUtil();
        sql2.in("rowguid", tasktypeguids);
        sql2.eq("centerguid", centerguid);

        String taskwaitnum = "0";
        if ("theme".equals(cate)) {
            return null;
        }
        else {
            List<AuditQueueTasktype> lists = tasktypeservice.getAuditQueueTasktypeByPage("rowguid,TaskTypeName",
                    sql2.getMap(), Integer.valueOf(curPage) * pagesize, pagesize, "ordernum", "desc").getResult()
                    .getList();

            for (AuditQueueTasktype list : lists) {
                taskwaitnum = StringUtil
                        .getNotNullString(handlequeueservice.getTaskWaitNum(list.getRowguid(), true).getResult());
                list.set("taskwaitnum", taskwaitnum);
            }
            String json = JsonUtil.listToJson(lists);
            return json;
        }
    }

    public String getTaskTypePagesDatabyOuguid(String cate, String ouguid, String centerguid, int curPage) {
        String taskwaitnum = "0";
        if ("theme".equals(cate)) {
            return null;
        }
        else {
            /*
             * List<Record> lists = tasktypeservice
             * .getAuditQueueTasktypeByPage(ouguid, centerguid,
             * Integer.valueOf(curPage) * pagesize, pagesize)
             * .getResult();
             */
            List<Record> lists = jntasktypeservice.getWindowLinkedTskType(Centerguid, ouguid,
                    Integer.valueOf(curPage) * pagesize, pagesize);
            for (Record list : lists) {
                taskwaitnum = StringUtil
                        .getNotNullString(handlequeueservice.getTaskWaitNum(list.get("rowguid"), true).getResult());
                list.put("taskwaitnum", taskwaitnum);
            }
            String json = JsonUtil.listToJson(lists);
            return json;
        }
    }

    public void getOUName(String OUGuid) {
        addCallbackParam("ouname", StringUtil.isNotBlank(ouservice.getOuByOuGuid(OUGuid).getOushortName())
                ? ouservice.getOuByOuGuid(OUGuid).getOushortName() : ouservice.getOuByOuGuid(OUGuid).getOuname());
    }

}
