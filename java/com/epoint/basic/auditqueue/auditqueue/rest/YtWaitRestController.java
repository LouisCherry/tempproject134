package com.epoint.basic.auditqueue.auditqueue.rest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;

@RestController
@RequestMapping("/ytSendLed")
public class YtWaitRestController {


    @RequestMapping(value="/sendLEDMQ",method = RequestMethod.POST)
    public String sendLEDMQ(@RequestBody String params){
        try {
            JSONObject e=JSONObject.parseObject(params);
            JSONObject obj=e.getJSONObject("params");
            String token = e.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
            	String content=obj.getString("content");
                String WindowNo=obj.getString("WindowNo");
                String CenterGuid=obj.getString("CenterGuid");
    		    JSONObject dataJson = new JSONObject();
    		    dataJson.put("regionName", WindowNo);
    		    dataJson.put("content", content);
    		    ProducerMQ.send("QueueLed_" + CenterGuid, "[" + dataJson.toString() + "]");
                return JsonUtils.zwdtRestReturn("1","接口调用成功!","");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
            
        } catch (Exception ex) {
            return JsonUtils.zwdtRestReturn("0","接口出现异常："+ ex.getMessage(),"");
        }
    }
    
}
