package com.epoint.jn.selfservicemachine.rest;

import java.util.UUID;

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
import com.epoint.core.utils.string.StringUtil;

@RestController
@RequestMapping("/jnzwfwmaterial")
public class JnzwfwMaterialRestController
{
    
    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @RequestMapping(value = "/getclientguid", method = RequestMethod.POST)
    public String getClientguid() throws Exception {

        try {
            JSONObject dataJson = new JSONObject();
            UUID uuid = UUID.randomUUID();
            dataJson.put("clientguid", uuid);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }

    }
    
    @RequestMapping(value = {"/getA4" }, method = {RequestMethod.POST })
    public String getA4(@RequestBody String params) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";
            AuditZnsbEquipment equipment;

            equipment = (AuditZnsbEquipment) this.equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    dataJson.put("A4Device", equipment.getStr("A4Device"));
                    return JsonUtils.zwdtRestReturn("1", "", dataJson);
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册！", "");
            }
        }
        catch (JSONException arg8) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + arg8.getMessage(), "");
        }
    }
}
