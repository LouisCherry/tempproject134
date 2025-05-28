package com.epoint.auditqueue.qhj.rest;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.util.JsonUtils;

@RestController
@RequestMapping({"/jnqhjframe"})
public class JNQhjFrame
{
    @Autowired
    private IAuditZnsbEquipment equipmentservice;
    
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @RequestMapping(value = {"/getquhallguid"}, method = {RequestMethod.POST})
    public String getQuHallguid(@RequestBody String params) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            AuditZnsbEquipment  equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            dataJson.put("quhallguid", equipment.get("quhallguid"));
            return JsonUtils.zwdtRestReturn("1", "" , dataJson);
        } catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
