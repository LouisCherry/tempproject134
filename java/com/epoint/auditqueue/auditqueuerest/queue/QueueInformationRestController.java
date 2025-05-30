package com.epoint.auditqueue.auditqueuerest.queue;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditqueue.auditqueuerest.queue.api.IHallWaitCount;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zwdt.util.TARequestUtil;

@RestController
@RequestMapping("/queueInformation")
public class QueueInformationRestController
{

	private static String ZWFWURL = ConfigUtil.getConfigValue("datasyncjdbc", "zwfwurl");
	
    @Autowired
    IAuditOrgaArea auditorgaareaservice;

    @Autowired
    IAuditOrgaServiceCenter centerservice;

    @Autowired
    IAuditOrgaHall hallservice;

    @Autowired
    IAuditOrgaWindow windowservice;

    @Autowired
    IAuditQueue queueservice;
    
    @Autowired
    IHandleQueue handlequeueservice;

    @Autowired
    IAuditProject projectservice;
    
    @Autowired
    IAuditTask taskservice;

    @Autowired
    IAuditQueueOrgaWindow queuewindowservice;

    @Autowired
    IAuditQueueTasktypeTask tasktypetaskservice;

    @Autowired
    IAuditQueueTasktype tasktypeservice;
    @Autowired
    IHallWaitCount hallWaitCountService;
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 根据辖区获取中心
     * 
     * @params params 请求参数
     * @return
     */
    @RequestMapping(value = "/getCenterByAreacode", method = RequestMethod.POST)
    public String getCenterByAreacode(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 辖区code
            String areacode = obj.getString("areacode");
            List<JSONObject> list = new ArrayList<JSONObject>();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("BELONGXIAQU", areacode);
            List<AuditOrgaServiceCenter> centerList = centerservice.getAuditOrgaServiceCenterByCondition(sql.getMap())
                    .getResult();
            for (AuditOrgaServiceCenter center : centerList) {
                JSONObject centerJson = new JSONObject();
                centerJson.put("centerguid", center.getRowguid());
                centerJson.put("centername", center.getCentername());
                list.add(centerJson);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("centerlist", list);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取大厅等待情况
     * 
     * @params params 请求参数
     * @return
     */
    @RequestMapping(value = "/getHallWaitCount", method = RequestMethod.POST)
    public String getHallWaitCount(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 中心guid
            String centerguid = obj.getString("centerguid");
            List<JSONObject> list = new ArrayList<JSONObject>();
            JSONObject dataJson = new JSONObject();
            
            if ("c3a8e6a6-94b9-4df7-be7f-e82408b599a5".equals(centerguid)) {
            	centerguid = "fb332790-c2b1-475d-b158-322c382da13b";
            	JSONObject datajson = new JSONObject();
            	datajson.put("centerguid", centerguid);
            	JSONObject submit = new JSONObject();
            	submit.put("params", datajson);
            	String resultsign = TARequestUtil.sendPostInner(ZWFWURL, submit.toJSONString(), "", "");
            	if (StringUtil.isNotBlank(resultsign)) {
                    JSONObject result = JSONObject.parseObject(resultsign);
                    if (StringUtil.isNotBlank(result.getString("custom"))) {
                    	JSONObject custom1 = result.getJSONObject("custom");
                        SqlConditionUtil sqlproject = new SqlConditionUtil();
                        sqlproject.eq("centerguid", "c3a8e6a6-94b9-4df7-be7f-e82408b599a5");
                        sqlproject.between("ACCEPTUSERDATE", EpointDateUtil.getBeginOfDate(new Date()),
                                EpointDateUtil.getEndOfDate(new Date()));
                        custom1.put("totalprojectcount",
                                projectservice.getAuditProjectCountByCondition(sqlproject.getMap()).getResult().toString());
                        return JsonUtils.zwdtRestReturn("1", "获取大厅等待情况成功！", custom1.toString());
                    }else {
                    	return JsonUtils.zwdtRestReturn("0", "查询大厅事项清单为空！", "");
                    }
                }else {
                	return JsonUtils.zwdtRestReturn("0", "查询大厅事项清单为空！", "");
                }
            }else {
            	 SqlConditionUtil sqlwindow = new SqlConditionUtil();
            	 SqlConditionUtil sql = new SqlConditionUtil();
                 sql.eq("centerguid", centerguid);
                 sql.setOrderDesc("ORDERNUM");

                 List<AuditOrgaHall> hallList = hallservice.getAllHall(sql.getMap()).getResult();
                 for (AuditOrgaHall hall : hallList) {
                     JSONObject hallJson = new JSONObject();
                     hallJson.put("hallguid", hall.getRowguid());
                     hallJson.put("hallname", hall.getHallname());
                     sqlwindow.eq("lobbytype", hall.getRowguid());
                     sqlwindow.eq("IS_USEQUEUE", QueueConstant.Common_yes_String);
                     // 大厅下面窗口数
                     hallJson.put("windowcount", windowservice.getWindowCount(sqlwindow.getMap()).getResult().toString());
                     // 大厅等待人数
                     hallJson.put("waitcount",
                             hallWaitCountService.getHallWaitCount(hall.getRowguid(), QueueConstant.Qno_Status_Init));
                     list.add(hallJson);
                 }
                
                 dataJson.put("halllist", list);

                 // 今日中心总等待人数
                 dataJson.put("totalwaitcount", queueservice
                         .getCountByCenterAndStatus(centerguid, QueueConstant.Qno_Status_Init).getResult().toString());
                 // 今日取号
                 dataJson.put("totalqueuecount",
                         queueservice.getCountByCenterAndStatus(centerguid, "").getResult().toString());
                 // 今日受理
                 SqlConditionUtil sqlproject = new SqlConditionUtil();
                 sqlproject.eq("centerguid", centerguid);
                 sqlproject.between("ACCEPTUSERDATE", EpointDateUtil.getBeginOfDate(new Date()),
                         EpointDateUtil.getEndOfDate(new Date()));
                 dataJson.put("totalprojectcount",
                         projectservice.getAuditProjectCountByCondition(sqlproject.getMap()).getResult().toString());
                 AuditOrgaServiceCenter center = centerservice.findAuditServiceCenterByGuid(centerguid).getResult();
                 if (StringUtil.isNotBlank(center)) {
                     sqlproject.eq("areacode", center.getBelongxiaqu());
                 }
                 return JsonUtils.zwdtRestReturn("1", "获取大厅等待情况成功！", dataJson.toString());
            }
           
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取窗口等待情况
     * 
     * @params params 请求参数
     * @return
     */
    @RequestMapping(value = "/getWindowWaitCount", method = RequestMethod.POST)
    public String getWindowWaitCount(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            // 大厅guid
            String hallguid = obj.getString("hallguid");
            String centerguid = obj.getString("centerguid");
            String qno = obj.getString("qno");

            List<JSONObject> list = new ArrayList<JSONObject>();
            AuditQueueOrgaWindow queuewindow;

            SqlConditionUtil sql = new SqlConditionUtil();
            // 判断qno存不存在
            if (StringUtil.isNotBlank(qno)) {
                String windows = "";
                List<String> windowlist = queueservice.getWindowListbyQno(qno, centerguid).getResult();
                if (windowlist != null && windowlist.size() > 0) {
                    windows = "'" + StringUtil.join(windowlist, "','") + "'";
                    sql.in("rowguid", windows);
                }
                else {
                    sql.eq("rowguid", "nodate");
                }
            }
            else {
                if (StringUtil.isNotBlank(hallguid)) {
                    sql.eq("lobbytype", hallguid);
                }
            }
            if (StringUtil.isNotBlank(centerguid)) {
                sql.eq("centerguid", centerguid);
            }
            sql.eq("IS_USEQUEUE", QueueConstant.Common_yes_String);

            sql.setOrderAsc("windowno");
            List<AuditOrgaWindow> windowList = windowservice.getAllWindow(sql.getMap()).getResult();
            for (AuditOrgaWindow window : windowList) {
                JSONObject windowJson = new JSONObject();
                windowJson.put("windowguid", window.getRowguid());
                windowJson.put("windowname", window.getWindowname());
                windowJson.put("windowno", window.getWindowno());

                queuewindow = queuewindowservice.getDetailbyWindowguid(window.getRowguid()).getResult();
                if (StringUtil.isNotBlank(queuewindow)) {
                    windowJson.put("currenthandleqno", queuewindow.getCurrenthandleqno());
                    windowJson.put("waitcount", queuewindow.getWaitnum());
                }
                else {
                    windowJson.put("currenthandleqno", "");
                    windowJson.put("waitcount", "0");
                }
                windowJson.put("windowno", window.getWindowno());
                list.add(windowJson);
            }

            JSONObject dataJson = new JSONObject();
            dataJson.put("windowlist", list);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项取号信息
     * 
     * @params params 请求参数
     * @params request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskQueueInfo", method = RequestMethod.POST)
    public String getTaskQueueInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskQueueInfo接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 中心guid
            String centerguid = obj.getString("centerguid");
            // 事项guid
            String taskguid = obj.getString("taskguid");

            JSONObject dataJson = new JSONObject();

            AuditTask task = taskservice.getAuditTaskByGuid(taskguid, true).getResult();
            if (task != null) {
                // 根据事项id跟中心guid获取事项类别
                String tasktypeguid = tasktypetaskservice
                        .getTaskTypeguidbyTaskIDandCenterGuid(task.getTask_id(), centerguid).getResult();
                if (StringUtil.isNotBlank(tasktypeguid)) {
                    AuditQueueTasktype tasktype = tasktypeservice.getTasktypeByguid(tasktypeguid).getResult();
                    if (StringUtil.isNotBlank(tasktype)) {

                        int leftqueuenum=-1;
                        // 判断是否存在限号
                        if (StringUtil.isNotBlank(tasktype.getXianhaonum())) {
                            leftqueuenum = Integer.parseInt(tasktype.getXianhaonum())
                                    - queueservice.getCountByTaskGuid(tasktypeguid).getResult();

                        }
                        // 等待人数
                        dataJson.put("waitnum", handlequeueservice.getTaskWaitNum(tasktypeguid, true).getResult());
                        // 剩余叫号数 -1代表没有限制
                        dataJson.put("leftqueuenum", String.valueOf(leftqueuenum));

                    } else {
                        return JsonUtils.zwdtRestReturn("0", "该事项分类不存在！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项未关联排队类别！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        } catch (JSONException e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
}
