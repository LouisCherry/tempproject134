package com.epoint.queue;

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
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.basic.auditorga.audithall.inter.IAuditOrgaHall;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
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
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.queue.api.IJnQueue;

@RestController
@RequestMapping("/jnqueueInformation")
public class JnQueueInformationRestController
{

    @Autowired
    IAuditOrgaArea auditorgaareaservice;

    @Autowired
    IAuditOrgaServiceCenter centerservice;

    @Autowired
    IAuditOrgaHall hallservice;

    @Autowired
    IAuditOrgaWindowYjs windowservice;

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
    IJnQueue iJnQueue;
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


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
             JSONObject obj = (JSONObject) json.get("params");
             String centerguid = obj.getString("centerguid");
            JSONObject dataJson = new JSONObject();
            List<JSONObject> list = new ArrayList<JSONObject>();
            if (StringUtil.isNotBlank(centerguid)) {
            	
            	List<AuditOrgaHall> hallList = iJnQueue.getHallList(centerguid).getResult();
            	
            	 for (AuditOrgaHall hall : hallList) {
                     JSONObject hallJson = new JSONObject();
                     hallJson.put("hallguid", hall.getRowguid());
                     hallJson.put("hallname", hall.getHallname());
                     // 大厅下面窗口数
                     hallJson.put("windowcount", iJnQueue.getWindowCount(hall.getRowguid()));
                     // 大厅等待人数
                     hallJson.put("waitcount",
                    		 iJnQueue.getWaitCount(hall.getRowguid()));
                     list.add(hallJson);
                 }
            	 
            	 dataJson.put("halllist", list);

                 // 今日中心总等待人数
                 dataJson.put("totalwaitcount", iJnQueue.getTotalWaitCount(centerguid));
                 // 今日取号
                 dataJson.put("totalqueuecount",iJnQueue.getTotalQueueCount(centerguid));
                 // 今日受理
                 dataJson.put("totalprojectcount","0");
                 return JsonUtils.zwdtRestReturn("1", "", dataJson.toString());
            }else {
            	 return JsonUtils.zwdtRestReturn("0", "中心标识为空", "");
            }
           
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
   
}
