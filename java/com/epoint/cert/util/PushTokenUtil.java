package com.epoint.cert.util;

import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public class PushTokenUtil {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    //获取token
    private static String tokenUrl = ConfigUtil.getConfigValue("xmzArgs", "axcerttokenurl");

    private static String appkey = ConfigUtil.getConfigValue("xmzArgs", "appkey");

    private static String appsecret = ConfigUtil.getConfigValue("xmzArgs", "appsecret");


    public static String getCertToken() {
        // 调用获取token接口
        Map<String, String> herders = new HashMap<>();
        herders.put("Content-Type", "application/x-www-form-urlencoded");
        herders.put("Cache-Control", "no-cache");
        Map<String, Object> pramMap = new HashMap<>();
        pramMap.put("client_id", appkey);
        pramMap.put("client_secret", appsecret);
        pramMap.put("grant_type", "client_credentials");
        String s = HttpUtil.doPost(tokenUrl, pramMap, herders);
        if (StringUtil.isBlank(s)) {
            return null;
        }
        log.info("===========token返回值==" + s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String custom = jsonObject.getString("custom");
        String access_token = "";
        if (StringUtil.isNotBlank(custom)) {
            access_token = JSONObject.parseObject(custom).getString("access_token");
        }
        log.info("===========token返回值access_token==" + access_token);
        return access_token;
    }

}
