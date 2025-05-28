package com.epoint.auditselfservice.auditselfservicerest.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditqueue.handlerabbitmq.inter.IHandleRabbitMQ;
import com.epoint.core.utils.container.ContainerFactory;

@RestController
@RequestMapping("/jnqueuepadlb")
public class JNPadQueueRestController
{
    @Autowired
    private IHandleRabbitMQ handlemqservice;
    @Autowired
    private IAuditQueueOrgaWindow queuewindowservice;
    IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo()
            .getComponent(IAuditQueueOrgaWindow.class);
    @RequestMapping(value = "/setlianban", method = RequestMethod.POST)
    public String setLianBan(@RequestBody String params) {
        try{
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String windowguid = obj.getString("windowguid");
            JSONObject dataJson = new JSONObject();
            AuditQueueOrgaWindow queuewindow = queuewindowservice.getDetailbyWindowguid(windowguid).getResult();
            dataJson.put("status", queuewindow.getWorkstatus());
            queuewindowservice.updateWindowWorkStatus(windowguid, "10");
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "接口调用失败"+e.getMessage(), "");
        }
       
    }
    @RequestMapping(value = "/droplianban", method = RequestMethod.POST)
    public String dropLianBan(@RequestBody String params) {
        try{
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String windowguid = obj.getString("windowguid");
            String status = obj.getString("status");
            JSONObject dataJson = new JSONObject();
            queuewindowservice.updateWindowWorkStatus(windowguid, status);
            handlemqservice.sendMQJSbyWindow(windowguid, "refresh()");
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }catch (Exception e) {
            // TODO: handle exception
            return JsonUtils.zwdtRestReturn("0", "接口调用失败", "");
        }
       
    }
}
