package com.epoint.znsb.epointqueue.rest;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuewindow.domain.AuditQueueWindow;
import com.epoint.basic.auditqueue.auditqueuewindow.inter.IAuditQueueWindow;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.IAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * Description:
 *
 * @Date: 2020/8/18 16:56
 * @Author 沈家成
 */

@RestController
@RequestMapping("/jnqueue")
public class JnQueueRestController
{
    @Autowired
    private IAuditOrgaWindowYjs windowservice;

    @Autowired
    private IAuditQueueOrgaWindow queueOrgaWindowService;
    @Autowired
    private IAuditQueue queueservice;

    @Autowired
    private IAuditQueueTasktype tasktypeService;

    @Autowired
    private IAuditQueueWindow queueWindowService;

    @Autowired
    private IAuditZnsbEquipment equipmentService;

    @Autowired
    private IAuditQueueWindowTasktype windowTasktypeService;
    /**
     * 日志
     */
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 获取大厅等待情况
     *
     * @return
     * @params params
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getWindowQueue", method = RequestMethod.POST)
    public String getHallQueue(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            JSONObject dataJson = new JSONObject();
            String hallguid = obj.getString("hallguid");
            String centerguid = obj.getString("centerguid");
            String macaddress = obj.getString("macaddress");
            AuditZnsbEquipment result = equipmentService.getDetailbyMacaddress(macaddress).getResult();
            if (result == null) {
                return JsonUtils.zwdtRestReturn("0", "macaddress有误", "");
            }
            String rowguid = result.getRowguid();
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("equipmentguid", rowguid);

            List<AuditQueueWindow> queueWindowList = queueWindowService
                    .getQueueWindowByEquipmentguids(sqlConditionUtil.getMap()).getResult();

            String windowguidList = "'";
            if (queueWindowList == null || queueWindowList.size() == 0) {
                return JsonUtils.zwdtRestReturn("0", "当前等待屏未配置窗口", "");
            }
            else {
                List<String> windowguids = queueWindowList.stream().map(AuditQueueWindow::getWindowguid)
                        .collect(Collectors.toList());
                windowguidList += StringUtil.join(windowguids, "','");
                windowguidList += "'";
            }

            List<JSONObject> list = new ArrayList<>();
            List<String> waitnolist = new ArrayList<>();
            JSONObject queueJson;

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("LOBBYTYPE", hallguid);
            sql.eq("IS_USEQUEUE", "1");
            sql.in("rowguid", windowguidList);

            List<AuditOrgaWindow> lstRecord = new ArrayList<>();
            List<AuditOrgaWindow> windowList = windowservice.getAllWindow(sql.getMap()).getResult();
            if (windowList == null || windowList.size() == 0) {
                return JsonUtils.zwdtRestReturn("0", "当前等待屏配置的窗口没有关联队列", "");
            }
            AuditQueueOrgaWindow queuewindow;
            List<String> windownoList = new ArrayList<>();
            for (AuditOrgaWindow window : windowList) {
                queuewindow = queueOrgaWindowService.getDetailbyWindowguid(window.getRowguid()).getResult();
                if (queuewindow != null) {
                    if (StringUtil.isNotBlank(queuewindow.getCurrenthandleqno())) {
                        lstRecord.add(window);
                    }
                }
            }
            Collections.sort(lstRecord, (o1, o2) -> {
                if (o1.getWindowno() == null) {
                    return -1;
                }
                else if (o2.getWindowno() == null) {
                    return 1;
                }
                else {
                    return o1.getWindowno().compareTo(o2.getWindowno());
                }
            });
            String waitcount = "0";
            String currno = "";
            for (AuditOrgaWindow Record : lstRecord) {
                queueJson = new JSONObject();
                currno = queueservice.getCurrentHandleNO(Record.getRowguid(), true).getResult();
                queueJson.put("currentno", StringUtil.getNotNullString(currno));
                queueJson.put("windowno", Record.getWindowno());
                windownoList.add(Record.getWindowno());
                waitcount = StringUtil
                        .getNotNullString(queueservice.getWindowWaitNum(Record.getRowguid(), true).getResult());
                queueJson.put("waitcount", waitcount);
                AuditQueue currQno = queueservice.getQNODetailByQNO(" * ", currno, centerguid).getResult();
                if (currQno != null) {
                    queueJson.put("calltime", currQno.getCalltime());
                }
                // log.info("等待屏名字:"+result.getMachinename()+",窗口号："+Record.getWindowno()+",当前办理号："+currno);
                list.add(queueJson);
            }

            // 对当前叫号的所有qno进行叫号时间排序，最新叫号的在最前面
            if (list.size() > 1) {
                Collections.sort(list, (o1, o2) -> {
                    String c1 = o1.getString("calltime");
                    String c2 = o2.getString("calltime");
                    if (StringUtil.isNotBlank(c1) && StringUtil.isNotBlank(c2)) {
                        Date date1 = new Date(o1.get("calltime").toString());
                        Date date2 = new Date(o2.get("calltime").toString());
                        return date2.compareTo(date1);
                    }
                    else {
                        return 1;
                    }
                });
            }
            dataJson.put("queuelist", list);

            // 等待qno获取：获取配置的窗口guid，查询对应的事项分类guid，再到audit_queue表里面把事项分类一致的查出来
            SqlConditionUtil sqlMap = new SqlConditionUtil();
            sqlMap.in("windowguid", windowguidList);
            List<AuditQueueWindowTasktype> windowTasktypeList = windowTasktypeService
                    .getAllWindowTasktype(sqlMap.getMap()).getResult();
            String tasktypeguidlist = "'";
            if (windowTasktypeList == null || windowTasktypeList.size() == 0) {
                return JsonUtils.zwdtRestReturn("0", "当前等待屏配置的窗口列表未配置事项分类", "");
            }
            else {
                List<String> tasktypeguids = windowTasktypeList.stream().map(AuditQueueWindowTasktype::getTasktypeguid)
                        .collect(Collectors.toList());
                tasktypeguidlist += StringUtil.join(tasktypeguids, "','");
                tasktypeguidlist += "'";
            }
            SqlConditionUtil queueSqlMap = new SqlConditionUtil();
            queueSqlMap.eq("centerguid", centerguid);
            queueSqlMap.eq("status", QueueConstant.Qno_Status_Init);
            queueSqlMap.in("taskguid", tasktypeguidlist);
            PageData<AuditQueue> waitQueuePage = queueservice
                    .getQueueByPage(queueSqlMap.getMap(), 0, 6, " getnotime ", " asc ").getResult();
            if (waitQueuePage != null && waitQueuePage.getList() != null && waitQueuePage.getList().size() > 0) {
                for (AuditQueue queueInfo : waitQueuePage.getList()) {
                    waitnolist.add(queueInfo.getQno());
                }
            }
            dataJson.put("waitnolist", waitnolist);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
