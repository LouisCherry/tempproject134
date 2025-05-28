package com.epoint.province.hcp.rest;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.StringUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.province.hcp.util.HcpSm2Util;

@RestController
@RequestMapping("/provincehcp")
public class ProvinceHcpRestController
{

    private static String HcpEvaluateHtmlUrl = ConfigUtil.getConfigValue("hcp", "HcpEvaluateHtmlUrl");

    private static String AreaId = ConfigUtil.getConfigValue("hcp", "AreaId");

    private static String AppId = ConfigUtil.getConfigValue("hcp", "AppId");

    private static String AppSecret = ConfigUtil.getConfigValue("hcp", "AppSecret");

    private static String PublicKey = ConfigUtil.getConfigValue("hcp", "PublicKey");

    private static String PrivateKey = ConfigUtil.getConfigValue("hcp", "PrivateKey");

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @RequestMapping(value = "/getEvaluateHtml", method = RequestMethod.POST)
    public String getEvaluateHtml(@RequestBody String params) {
        try {
            JSONObject json = JSONObject.parseObject(params);
            JsonUtils.checkUserAuth(json.getString("token"));
            JSONObject obj = json.getJSONObject("params");
            JSONObject dataJson = new JSONObject();

            String projectId = "37" + obj.getString("projectId");
            String serviceName = "业务办结";
            String serviceNumber = "1";
            String pf = obj.getString("pf");

            if (StringUtil.isBlank(projectId)) {
                return JsonUtils.zwdtRestReturn("0", "projectId 参数错误，请检查参数", "");
            }
            if (StringUtil.isBlank(pf)) {
                return JsonUtils.zwdtRestReturn("0", "pf 参数错误，请检查参数", "");
            }
            String timestamp = String.valueOf(System.currentTimeMillis());
            String key = String.valueOf(timestamp) + AppId + AppSecret;
            String sign = HcpSm2Util.encrypt(key, PublicKey);
            String url = String.valueOf(HcpEvaluateHtmlUrl) + "?areaId=" + AreaId + "&projectId=" + projectId
                    + "&serviceName=" + serviceName + "&serviceNumber=" + serviceNumber + "&pf=" + pf + "&sign=" + sign
                    + "&timestamp=" + timestamp + "&appId=" + AppId;
            dataJson.put("backUrl", url);
            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "接口出现异常：" + e, "");
        }
    }

}
