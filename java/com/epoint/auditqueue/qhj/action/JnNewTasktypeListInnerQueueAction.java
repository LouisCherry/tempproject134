package com.epoint.auditqueue.qhj.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.domain.AuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditqueuexianhaotime.inter.IAuditQueueXianhaotime;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;

@RestController("jnnewtasktypelistinnerqueueaction")
@Scope("request")
public class JnNewTasktypeListInnerQueueAction extends BaseController
{
    private static final long serialVersionUID = 1L;

    @Autowired
    private IAuditQueueTasktype tasktypeservice;

    @Autowired
    private IAuditOrgaWindow windowservice;

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

    private String ouguid;

    private String centerguid;

    private String hallguid;

    private String hallname;

    private static final int pagesize = 5;

    private Calendar c = Calendar.getInstance();

    @Override
    public void pageLoad() {
        ouguid = getRequestParameter("ouguid");
        centerguid = getRequestParameter("Centerguid");
        hallguid = getRequestParameter("hallguid");

        if (StringUtil.isNotBlank(ouguid)) {
            Integer total = tasktypeservice.getCountbyOUGuid(ouguid, centerguid).getResult();
            int pages = total % pagesize == 0 ? (total / pagesize) : (total / pagesize + 1);
            addCallbackParam("pages", pages);
            addCallbackParam("hallname", hallname);
        }
        else {
            addCallbackParam("pages", getTaskTypePagesbyHallguid(hallguid, centerguid));
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
        if (StringUtil.isNotBlank(ouguid)) {
            addCallbackParam("html", getTaskTypePagesDatabyOuguid("department", ouguid, centerguid,
                    Integer.parseInt(getRequestParameter("cur"))));
        }
        else {
            addCallbackParam("html", getTaskTypePagesDatabyHallguid("Centerguid", hallguid, centerguid,
                    Integer.parseInt(getRequestParameter("cur"))));
        }
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
            return JsonUtil.listToJson(lists);

        }
    }

    public String getTaskTypePagesDatabyOuguid(String cate, String ouguid, String centerguid, int curPage) {
        String taskwaitnum = "0";
        if ("theme".equals(cate)) {
            return null;
        }
        else {
            List<Record> lists = tasktypeservice
                    .getAuditQueueTasktypeByPage(ouguid, centerguid, Integer.valueOf(curPage) * pagesize, pagesize)
                    .getResult();

            for (Record list : lists) {
                taskwaitnum = StringUtil
                        .getNotNullString(handlequeueservice.getTaskWaitNum(list.get("rowguid"), true).getResult());
                list.put("taskwaitnum", taskwaitnum);
            }
            String json = JsonUtil.listToJson(lists);
            return json;
        }
    }

    public void checkIsclick(String taskname, String taskguid, String waitCount, String macAddress) {
        String msg = "";
        int leftpaperpiece = Integer.valueOf(getleftPiece(macAddress));
        if (leftpaperpiece <= 0) {
            msg = ("剩余纸张数量为0，请联系管理员");
        }
        else if (StringUtil.isNotBlank(taskguid)) {
            int week = c.get(Calendar.DAY_OF_WEEK) - 1;

            // 判断当前事项,当前星期是否有限号
            AuditQueueXianhaotime xianhaotime = null;
            xianhaotime = xianhaoservice.getDetailbyTasktypeguidandweek(String.valueOf(week), taskguid, centerguid)
                    .getResult();
            if (xianhaotime != null) {
                msg = xianhaoBytime(xianhaotime, taskguid);
            }
            else {
                xianhaotime = xianhaoservice
                        .getDetailbyTasktypeguidandweek(String.valueOf(week), "commonguid", centerguid).getResult();
                if (xianhaotime != null) {

                    msg = xianhaoBytime(xianhaotime, taskguid);
                }
                else {
                    AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(taskguid).getResult();
                    if (tasktype != null && StringUtil.isNotBlank(tasktype.getXianhaonum())) {
                        if (queueservice.getCountByTaskGuid(taskguid).getResult() >= Integer
                                .parseInt(tasktype.getXianhaonum())) {
                            msg = "该事项今日取号数已达上限，无法取号！";
                        }
                    }
                }
            }

        }
        addCallbackParam("msg", msg);
        addCallbackParam("taskname", taskname);
        addCallbackParam("taskguid", taskguid);
        addCallbackParam("waitCount", waitCount);
    }

    public void getOUName(String ouGuid) {
        addCallbackParam("ouname",
                StringUtil.isNotBlank(ouservice.getOuByOuGuid(ouGuid).getOushortName())
                        ? ouservice.getOuByOuGuid(ouGuid).getOushortName()
                        : ouservice.getOuByOuGuid(ouGuid).getOuname());
    }

    public int getleftPiece(String macAddress) {
        int leftpaperpiece = 0;
        AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macAddress, " leftpaperpiece ")
                .getResult();
        if (StringUtil.isNotBlank(equipment)) {
            leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();

        }
        return leftpaperpiece;
    }

    public Map<String, String> getPiece(String macAddress) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        double alertlength = 0.0;
        int leftpaperpiece = 0;
        String isBlink = "no";

        AuditZnsbEquipment equipment = equipmentservice
                .getDetailbyMacaddress(macAddress, " Alertlength,Leftpaperpiece ").getResult();
        if (StringUtil.isNotBlank(equipment)) {
            alertlength = StringUtil.isBlank(equipment.getAlertlength()) ? 0.0 : equipment.getAlertlength();
            leftpaperpiece = StringUtil.isBlank(equipment.getLeftpaperpiece()) ? 0 : equipment.getLeftpaperpiece();

            if (alertlength >= leftpaperpiece) {
                isBlink = "is";
            }
            if (leftpaperpiece == 0) {
                isBlink = "red";
            }
            rtnMap.put("leftpaperpiece", String.valueOf(leftpaperpiece));
            rtnMap.put("alertlength", String.valueOf((int) alertlength));
            rtnMap.put("isBlink", isBlink);
        }
        return rtnMap;
    }

    public String xianhaoBytime(AuditQueueXianhaotime xianhaotime, String taskguid) {
        String msg = "";
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        String amstart = year + "-" + month + "-" + date + " " + xianhaotime.getAmstart();
        String amend = year + "-" + month + "-" + date + " " + xianhaotime.getAmend();
        String pmstart = year + "-" + month + "-" + date + " " + xianhaotime.getPmstart();
        String pmend = year + "-" + month + "-" + date + " " + xianhaotime.getPmend();
        try {
            long ams = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(amstart).getTime();
            long ame = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(amend).getTime();
            long pms = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pmstart).getTime();
            long pme = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pmend).getTime();

            long now = System.currentTimeMillis();

            if (now > ams && now < ame || now > pms && now < pme) {
                if (StringUtil.isNotBlank(taskguid)) {
                    AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(taskguid).getResult();
                    if (tasktype != null && StringUtil.isNotBlank(tasktype.getXianhaonum())) {
                        if (queueservice.getCountByTaskGuid(taskguid).getResult() >= Integer
                                .parseInt(tasktype.getXianhaonum())) {
                            msg = "该事项今日取号数已达上限，无法取号！";
                        }
                    }
                }
            }
            else {
                msg = "时间段" + xianhaotime.getAmstart() + "至" + xianhaotime.getAmend() + "以及" + xianhaotime.getPmstart()
                        + "至" + xianhaotime.getPmend() + "外无法取号";
            }
        }
        catch (ParseException e) {
            msg = "请检查相关限号时间格式是否正确！";
        }

        return msg;
    }

}
