package com.epoint.auditqueue.auditqueuerest.virtualstation;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowTask;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;

@RestController
@RequestMapping("/jnvirtualstation")
public class JnQueueVirtualstationController
{

    @Autowired
    private IAuditOrgaHall audithall;

    @Autowired
    private IAuditOrgaWindow windowservice;

    @Autowired
    private IAuditQueueOrgaWindow orgawindowservice;

    @Autowired
    private IAuditTask taskservice;

    @Autowired
    private IOuService ouservice;

    @Autowired
    private IUserService userservice;
    
    

    /**
     * 根据中心id获取大厅
     * 
     * @params centerguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getHall", method = RequestMethod.POST)
    public String getHall(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");

            JSONObject dataJson = new JSONObject();
            JSONObject hallJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();

            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("centerguid", centerguid);
            sql.setOrderDesc("ordernum");
            List<AuditOrgaHall> halllist = audithall.getAllHall(sql.getMap()).getResult();
            int num=1;
            for (AuditOrgaHall hall : halllist) {
                hallJson = new JSONObject();
                hallJson.put("hallname", hall.getHallname());
                hallJson.put("hallguid", hall.getRowguid());
                if(num==1){
                  hallJson.put("tbhhallguid","63a5595b-fcd0-4b5b-a060-693c671ddd2c");
                }else if(num==2){
                  hallJson.put("tbhhallguid","f155b358-22b7-40b0-9c84-d5444448502a");
                }else if(num==3){
                  hallJson.put("tbhhallguid","3f11bd9f-550e-454c-8e02-1316f1f3d23c");
                }else if(num==4){
                  hallJson.put("tbhhallguid","009da827-a73c-42ef-853b-ffde53a1311b");
                }
                num=num+1;
                list.add(hallJson);
            }

            dataJson.put("halllist", list);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 获取特定窗口细节
     * 
     * @params windowguid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getWindowdetail", method = RequestMethod.POST)
    public String getWindowdetail(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");

            String windowguid = obj.getString("windowguid");

            JSONObject dataJson = new JSONObject();
            JSONObject windowJson = new JSONObject();
            JSONObject taskJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            List<JSONObject> tasklist = new ArrayList<JSONObject>();
            AuditOrgaWindow window= windowservice.getWindowByWindowGuid(windowguid).getResult();
            windowJson.put("windowno",window.getWindowno());
            windowJson.put("windowname", window.getWindowname());
            windowJson.put("windowguid", windowguid);
            AuditQueueOrgaWindow orgawindow = orgawindowservice.getDetailbyWindowguid(windowguid).getResult();
           
            if (StringUtil.isNotBlank(orgawindow)) {

                FrameUser user = userservice.getUserByUserField("UserGuid", orgawindow.getUserguid());
                if (user != null) {
                    FrameOu ou = ouservice.getOuByOuGuid(user.getOuGuid());
                    if (ou != null) {
                        windowJson.put("ouname",
                                StringUtil.isNotBlank(ou.getOushortName()) ? ou.getOushortName() : ou.getOuname());
                    }
                }
                windowJson.put("username", orgawindow.getUsername());
                windowJson.put("workstatus", orgawindow.getWorkstatus());
                windowJson.put("userguid", orgawindow.getUserguid());
                windowJson.put("cameraIndexCode", window.getStr("cameraIndexCode"));
                windowJson.put("currenthandleqno", orgawindow.getCurrenthandleqno());
                windowJson.put("photourl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                        + "/rest/auditattach/getUserPic?userguid=" + orgawindow.getUserguid());
            }
            else {
                windowJson.put("ouname", "");
                windowJson.put("username", "");
                windowJson.put("workstatus", "0");
                windowJson.put("userguid", "");
                windowJson.put("currenthandleqno", "");
                windowJson.put("photourl", "");
                windowJson.put("cameraIndexCode", window.getStr("cameraIndexCode"));
            }

            //获取办理业务            
          
            AuditTask task = null;
            List<AuditOrgaWindowTask> windowtasklist = windowservice.getTaskByWindow(windowguid).getResult();

            for (AuditOrgaWindowTask windowtask : windowtasklist) {
                taskJson = new JSONObject();
                task = taskservice.selectUsableTaskByTaskID(windowtask.getTaskid()).getResult();
                if (StringUtil.isNotBlank(task)) {
                    //html.append("<li >"+num+"、   "+ task.getTaskname() + "</li>");
                    taskJson.put("task", task.getTaskname());
                    tasklist.add(taskJson);
                }
            }

            windowJson.put("business", tasklist);

            list.add(windowJson);
            
            dataJson.put("windowlist", list);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

    /**
     * 根据条件查询窗口
     * 
     * @params hallid windowno workstatus
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/searchWindow", method = RequestMethod.POST)
    public String searchWindow(@RequestBody String params, HttpServletRequest request) {
        try {

            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String hallid = obj.getString("hallid");
            String windowno = obj.getString("windowno");
            String workstatus = obj.getString("workstatus");

            JSONObject dataJson = new JSONObject();
            JSONObject windowJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            String [] str=hallid.split("&");
            SqlConditionUtil sql = new SqlConditionUtil();
            //sql.eq("lobbytype", hallid);
            //sql.in("lobbytype", "63a5595b-fcd0-4b5b-a060-693c671ddd2c");
            sql.in("lobbytype", "'"+str[0]+"','"+str[1]+"'");
            sql.eq("WINDOWTYPE","10");
            sql.setOrderAsc("windowno");
            List<AuditOrgaWindow> windowlist = windowservice.getAllWindow(sql.getMap()).getResult();

            for (AuditOrgaWindow window : windowlist) {
                AuditQueueOrgaWindow orgawindow = orgawindowservice.getDetailbyWindowguid(window.getRowguid())
                        .getResult();

                boolean isshow = false;
                
                switch (workstatus) {
                    case "all":
                        if (window.getWindowno().contains(windowno)) {
                            isshow = true;
                        }
                        break;
                    case "ing":
                        if (window.getWindowno().contains(windowno) && QueueConstant.Window_WorkStatus_Free.equals(orgawindow.getWorkstatus())
                                && StringUtil.isNotBlank(orgawindow.getCurrenthandleqno())) {
                            isshow = true;
                        }
                        break;
                    case "free":
                        if (window.getWindowno().contains(windowno) && QueueConstant.Window_WorkStatus_Free.equals(orgawindow.getWorkstatus())
                                && StringUtil.isBlank(orgawindow.getCurrenthandleqno())) {
                            isshow = true;
                        }
                        break;
                    case "pause":
                        if (window.getWindowno().contains(windowno) && QueueConstant.Window_WorkStatus_Pause.equals(orgawindow.getWorkstatus())) {
                            isshow = true;
                        }
                        break;
                    case "unlisted":
                        if (window.getWindowno().contains(windowno) && QueueConstant.Window_WorkStatus_NotLogin.equals(orgawindow.getWorkstatus())) {
                            isshow = true;
                        }
                        break;

                    default:
                        break;
                }
                


                if (isshow) {
                    windowJson = new JSONObject();
                    windowJson.put("windowguid", window.getRowguid());
                    windowJson.put("windowno", window.getWindowno());
                    windowJson.put("windowname", window.getWindowname());
                    if (StringUtil.isNotBlank(orgawindow)) {
                    	if(StringUtil.isNotBlank(orgawindow.getWorkstatus())){
                        FrameUser user = userservice.getUserByUserField("UserGuid", orgawindow.getUserguid());

                        if (user != null) {
                            FrameOu ou = ouservice.getOuByOuGuid(user.getOuGuid());
                            if (ou != null) {
                                windowJson.put("ouname", StringUtil.isNotBlank(ou.getOushortName())
                                        ? ou.getOushortName() : ou.getOuname());
                            }
                            
                         }
                       }
                        
                        windowJson.put("username", orgawindow.getUsername());
                        windowJson.put("workstatus", orgawindow.getWorkstatus());
                        windowJson.put("userguid", orgawindow.getUserguid());
                        windowJson.put("currenthandleqno", orgawindow.getCurrenthandleqno());
                        windowJson.put("photourl", QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
                                + "/rest/auditattach/getUserPic?userguid=" + orgawindow.getUserguid());
                    }
                    else {
                        windowJson.put("username", "坐席暂缺");
                        windowJson.put("workstatus", "0");
                        windowJson.put("userguid", "");
                        windowJson.put("currenthandleqno", "");
                        windowJson.put("photourl", "");
                    }
                    list.add(windowJson);

                }

            }

            dataJson.put("windowlist", list);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }

}
