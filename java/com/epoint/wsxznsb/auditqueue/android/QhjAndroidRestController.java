package com.epoint.wsxznsb.auditqueue.android;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.wsxznsb.util.WsxQueueConstant;

@RestController
@RequestMapping("/androidqhj")
public class QhjAndroidRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params, HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(WsxQueueConstant.EQUIPMENT_TYPE_AZQHJ);
                equipment.setXiaodayindevice("w80");
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    // 首页地址
                    if (StringUtil.isNotBlank(equipment.getUrl())) {
                        dataJson.put("homepageurl", equipment.getUrl());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "首页地址未配置！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
