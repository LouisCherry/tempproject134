package com.epoint.auditqueue.auditznsbvideopic.action;


import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditqueue.auditqueuetimeset.inter.IAuditQueueTimeset;
import com.epoint.basic.auditqueue.auditznsbappconfig.inter.IAuditZnsbAppConfig;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbterminalapp.inter.IAuditZnsbTerminalApp;
import com.epoint.ces.auditznsbwaitvedio.api.IAuditZnsbWaitvedioService;
import com.epoint.ces.auditznsbwaitvedio.api.entity.AuditZnsbWaitvedio;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.QueueCommonUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.spring.util.SpringContextUtil;

@RestController
@RequestMapping("/jnqueueApp")
public class JNQueueAppRestController
{

    @Autowired
    private IAuditZnsbEquipment equipmentservice;



    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IAttachService attachservice;

    @Autowired
    private IConfigService configservice;

    /**
     * 日志
     */
    @SuppressWarnings("unused")
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IAuditZnsbWaitvedioService service;

    @RequestMapping(value = "/getvideoguid", method = RequestMethod.POST)
    public String getvideoguid(@RequestBody String params) {
     
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String centerguid = obj.getString("centerguid");
            String sql="";
            if(StringUtil.isNotBlank(centerguid)){
                sql+="  and centerguid = '"+centerguid+"'";
            }
           
            AuditZnsbWaitvedio video =    service.findfirst(sql);
            JSONObject dataJson = new JSONObject();
        
           String videoguid =  video.getVideoguid();
           List<FrameAttachInfo> attachlist =    attachservice.getAttachInfoListByGuid(videoguid);
           if(attachlist.size()>0){
               dataJson.put("videoguid", attachlist.get(0).getAttachGuid());
           }else{
               return JsonUtils.zwdtRestReturn("0", "视频已删除", "");  
           }
           
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.info("【getvideoguid异常】" + params);
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "getvideoguid接口调用失败", "");  
        }
    
       
    }
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            String ip = obj.getString("ip");
            String networkmask = obj.getString("networkmask");
            String gateway = obj.getString("gateway");
            String dns = obj.getString("dns");

            JSONObject dataJson = new JSONObject();
            String centerguid = "";

            // 根据Macaddress判断是否存在，不存在自动插入一条记录
            if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
                AuditZnsbEquipment equipment = new AuditZnsbEquipment();
                equipment.setMacaddress(macaddress);
                equipment.setIpaddress(ip);
                equipment.setNetworkmask(networkmask);
                equipment.setGateway(gateway);
                equipment.setDns(dns);
                equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
                equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_CKP);
                equipment.setRowguid(UUID.randomUUID().toString());
                equipment.setOperatedate(new Date());
                equipmentservice.insertEquipment(equipment);
            }

            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                // 根据macaddress获取中心guid
                centerguid = equipment.getCenterguid();
                if (StringUtil.isNotBlank(centerguid)) {
                    String config = handleConfigservice.getFrameConfig("AS_RabbitMQ", centerguid).getResult();
                   if (StringUtil.isNotBlank(config)) {
                        dataJson.put("rabbitmq", config);
                        CachingConnectionFactory conn = null;
                        if (SpringContextUtil.getApplicationContext().containsBean("connectionFactory")) {
                            conn = SpringContextUtil.getBean("connectionFactory");
                            dataJson.put("rabbitmqusername", conn.getRabbitConnectionFactory().getUsername());
                            dataJson.put("rabbitmqpassword", conn.getRabbitConnectionFactory().getPassword());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "RabbitMQ未配置！", "");
                        }

                    }
                  else {
                        return JsonUtils.zwdtRestReturn("0", "系统参数AS_RabbitMQ未配置！", "");
                    }
                    config = handleConfigservice.getFrameConfig("AS_IS_ENABLE_APPLOG", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("recordlog", config);
                    }
                    else {
                        // 默认不记录日志
                        dataJson.put("recordlog", "0");
                    }
                    config = handleConfigservice.getFrameConfig("AS_IS_USE_COMPANYSTB", centerguid).getResult();
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("companystb", config);
                    }
                    else {
                        // 默认公司盒子
                        dataJson.put("companystb", "1");
                    }

                    config = configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE");                 
                    if (StringUtil.isNotBlank(config)) {
                        dataJson.put("epointdevice", config);
                    }
                    else {
                        // 默认不启用硬件管理平台
                        dataJson.put("epointdevice", "0");
                    }

                    // 首页地址
                    if (StringUtil.isNotBlank(equipment.getUrl())) {
                        dataJson.put("homepageurl", equipment.getUrl());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "首页地址未配置！", "");
                    }

                    // 判断是否启用
                    if (QueueConstant.EQUIPMENT_STATUS_OFFLINE.equals(equipment.getStatus())) {
                        return JsonUtils.zwdtRestReturn("0", "设备已离线！", "");
                    }

                    //服务器时间用于校准机顶盒时间
                    dataJson.put("machinetime", sdf.format(new Date()));
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "该设备未注册，请重新启动apk！", "");
            }
            String conditionsql="";
            if(StringUtil.isNotBlank(centerguid)){
                conditionsql+="  and centerguid = '"+centerguid+"'";
            }
           
            AuditZnsbWaitvedio video =    service.findfirst(conditionsql);

            String videoguid =  video.getVideoguid();
            List<FrameAttachInfo> attachlist =    attachservice.getAttachInfoListByGuid(videoguid);
            if(attachlist.size()>0){
               String videoattachguid = attachlist.get(0).getAttachGuid();
             String videourl =   QueueCommonUtil.getUrlPath(request.getRequestURL().toString())
               + "/rest/auditattach/readAttach?attachguid="+videoattachguid;
             dataJson.put("videourl", videourl);
            }
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (JSONException e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }
}
