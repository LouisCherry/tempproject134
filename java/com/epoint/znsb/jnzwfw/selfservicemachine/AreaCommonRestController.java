package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

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
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping("/selfserviceareacommon")
public class AreaCommonRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private IAuditZnsbEquipment equipmentservice;

    @Autowired
    private IHandleConfig handleConfigservice;

    @Autowired
    private IConfigService configService;

    /**
     * 初始化
     *
     * @return
     * @params params
     */
    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject json = JSON.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = (JSONObject) json.get("params");
            String macaddress = obj.getString("macaddress");
            JSONObject dataJson = new JSONObject();
            String centerguid = "";
            AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
            if (StringUtil.isNotBlank(equipment)) {
                centerguid = equipment.getCenterguid();
            }
            // 区域一体化相关参数
            // currentareacode如果为7.10智能化系统，则该行设置currentareacode的代码请删除，7.10之前的智能化系统请保留
            String currentareacode = handleConfigservice.getFrameConfig("AS_ZNSB_AREACODE", centerguid).getResult();
            dataJson.put("currentareacode", currentareacode);
            String areaurl = configService.getFrameConfigValue("AS_ZNSB_AREAURL");
            dataJson.put("areaurl", areaurl);
            String appkey = handleConfigservice.getFrameConfig("AS_ZNSB_APPKEY", centerguid).getResult();
            dataJson.put("appkey", appkey);
            String appsecret = handleConfigservice.getFrameConfig("AS_ZNSB_APPSECRET", centerguid).getResult();
            dataJson.put("appsecret", appsecret);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
