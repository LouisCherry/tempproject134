package com.epoint.feishuiduijie.utils;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public class FeishuiduijieUtil {

    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 加密
     * @param str
     * @param key
     * @return
     */
    public static String sm4Encryption(String str, String key){
        SM4 sm41 = SmUtil.sm4(key.getBytes());
        String base64Pass = sm41.encryptBase64(str);
        return base64Pass;
    }

    /**
     * 解密
     * @param base64Pass
     * @param key
     * @return
     */
    public String sm4Decryption(String base64Pass, String key) {
        SM4 sm41 = SmUtil.sm4(key.getBytes());
        // base64解密
        String s2 = sm41.decryptStr(base64Pass);
        return s2;
    }

    /**
     * 获取token接口
     * @return
     */
    public static JSONObject getToken(){
        log.info("========== 开始非税对接获取token接口 ==========");
        try {
            JSONObject result = new JSONObject();
            String feishuiduijieurl = ConfigUtil.getConfigValue("feishuiduijie","feishuiduijieurl");
            if (StringUtil.isBlank(feishuiduijieurl)) {
                log.info("========== 调用非税对接获取token接口异常 ========= 未配置 feishuiduijieurl");
                return null;
            }
            String client_id = ConfigUtil.getConfigValue("feishuiduijie","client_id");
            String client_secret = ConfigUtil.getConfigValue("feishuiduijie","client_secret");
            String grant_type = ConfigUtil.getConfigValue("feishuiduijie","grant_type");

            String gettokenurl = feishuiduijieurl +"rest/oauth2/token";
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "application/x-www-form-urlencoded");
            Map<String, Object> map = new HashMap<>();
            map.put("client_id", client_id);
            map.put("client_secret", client_secret);
            map.put("grant_type", grant_type);
            log.info("=======地址======="+gettokenurl+" 参数："+map);
            //String resultStr = HttpUtil.doHttp(gettokenurl, headerMap, jsonObject.toJSONString(), "post", HttpUtil.RTN_TYPE_STRING);
            String resultStr = HttpUtil.doPost(gettokenurl, map);
            log.info("=======结束调用非税对接获取token接口 resultStr======="+resultStr);
            if (resultStr != null) {
                result = JSONObject.parseObject(resultStr);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            log.info("调用非税对接获取token接口异常");
            return null;
        }
    }

    /**
     * 水保 防空接口
     */
    public static JSONObject sbfkfydata(String MethodName,String areacode,JSONObject data){
        log.info("========== 开始调用sbfkfydata接口 ==========");
        try {
            JSONObject result = new JSONObject();
            String feishuiduijieurl = ConfigUtil.getConfigValue("feishuiduijie","feishuiduijieurl");
            String key = ConfigUtil.getConfigValue("feishuiduijie","key");
            log.info("=======key======="+key);
            if (StringUtil.isBlank(feishuiduijieurl)) {
                log.info("========== 调用非税对接获取token接口异常 ========= 未配置 feishuiduijieurl");
                return null;
            }
            if(StringUtil.isBlank(key)){
                log.info("========== 调用非税对接获取token接口异常 ========= 未配置 key");
                return null;
            }
            // 获取token
            String access_token = "";
            JSONObject token = getToken();
            if(token == null){
                log.info("========== 调用非税对接获取token接口异常 ========= 未获取到 token");
                return null;
            }
            if(token.containsKey("custom")){
                access_token = token.getJSONObject("custom").getString("access_token");
            }
            if(StringUtil.isBlank(access_token)){
                log.info("========== 调用非税对接获取token接口异常 ========= 未获取到 access_token");
                return null;
            }

            String requesturl = feishuiduijieurl +"rest/sbfkfydatarest/sbfkfydata";
            Map<String, String> headerMap = new HashMap<>();
            //headerMap.put("Content-Type", "application/x-www-form-urlencoded");
            headerMap.put("Authorization", "Bearer "+access_token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MethodName", MethodName);
            JSONObject jsonParams = new JSONObject();
            // 上传的地市区划编码
            jsonParams.put("citynum", areacode);
            // 记录集合加密字符串
            log.info("data加密前："+data);
            String dataStr = FeishuiduijieUtil.sm4Encryption(data.toJSONString(),key);
            log.info("data加密后："+dataStr);
            jsonParams.put("data", dataStr);
            jsonObject.put("params", jsonParams);
            log.info("=======地址======="+requesturl+" 参数："+jsonObject.toJSONString());
            String resultStr = HttpUtil.doHttp(requesturl, headerMap, jsonObject.toJSONString(), "post", HttpUtil.RTN_TYPE_STRING);
            log.info("=======结束调用sbfkfydata接口 resultStr======="+resultStr);
            if (resultStr != null) {
                result = JSONObject.parseObject(resultStr);
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            log.info("调用sbfkfydata接口异常");
            return null;
        }
    }

}
