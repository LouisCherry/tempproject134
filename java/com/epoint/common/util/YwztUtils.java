package com.epoint.common.util;

import cn.hutool.crypto.SmUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditauth.auditonlineappregister.inter.IAuditOnineAppregister;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务中台相关工具类
 */
public class YwztUtils {

    private static IAuditTask iAuditTask = (IAuditTask) ContainerFactory
            .getContainInfo().getComponent(IAuditTask.class);;
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 测试apimanager接口
     *
     * @return
     */
    public static String getWeburl(String taskguid) {
        try {
            String messagescenterurl = "http://172.20.58.176/gateway/api/1/web/getItemAddressInfo";
            String appkey = "888106319838445568";
            JSONObject dataJson = new JSONObject();
            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
            if (auditTask != null) {
                //首先判断是否跳转业务中台
                if ("1".equals(auditTask.getStr("ywztcode"))) {
                    messagescenterurl += "?itemCode=" + auditTask.getStr("NEW_ITEM_CODE");
                    long timeStamp = System.currentTimeMillis();
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("ApiKey", appkey);
                    headers.put("X-BizId", String.valueOf(timeStamp));
                    String result = HttpUtil.doPostJson(messagescenterurl, null, headers);
                    if (StringUtil.isNotBlank(result)) {
                        result = StringEscapeUtils.unescapeJavaScript(result);
                        String strj = result.replace("\"[", "[");
                        String strjs = strj.replace("]\"", "]");
                        String strjso = strjs.replace("\"{", "{");
                        String strjsons = strjso.replace("}\"", "}");
                        String strjsons1 = strjsons.replace("\\", "");
                        log.info("业务中台跳转接口转义之后返回值：" + strjsons1);
                        String data1 = JSONObject.parseObject(strjsons1).getString("data");
                        String data2 = JSONObject.parseObject(data1).getString("data");
                        JSONArray data3 = JSONObject.parseObject(data2).getJSONArray("data");
                        if (data3.isEmpty()) {
                            //数组为空，说明不跳转
                            return JsonUtils.zwdtRestReturn("0", "该事项不跳转！", dataJson.toString());
                        } else {
                            String weburl = "";
                            for (Object jsonObject : data3) {
                                JSONObject object = (JSONObject) jsonObject;
                                String ADAPTERTERMINAL = object.getString("ADAPTERTERMINAL");
                                if ("pc".equals(ADAPTERTERMINAL)) {
                                    weburl = object.getString("WEBURL");
                                    weburl = getUrl(weburl);
                                }
                            }
                            dataJson.put("weburl", weburl);
                            return JsonUtils.zwdtRestReturn("1", "获取跳转地址成功！", dataJson.toString());
                        }
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "该事项不跳转！", dataJson.toString());
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项配置为不跳转！", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "获取事项信息失败！", dataJson.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getWeburl异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败：" + e.getMessage(), "");
        }
    }

    public static String getUrl(String weburl) {
        String url = "";
        try {
            if (StringUtil.isNotBlank(weburl)) {
                String key = "370808555808073"; // 应用调用方 Key
                String secret = "E260598CEE0A467B86DA6C0AA683457F"; // 应用调用方 Secret
                String time = String.valueOf(System.currentTimeMillis());
                String data = key + "&" + time + "&" + secret;
                String sign = sm3Sign(data);
                String format = weburl + "?x-h5-key=%s&x-h5-time=%s&x-h5-sign=%s";
                url = String.format(format, key, time, sign);
                url = url.replace(".cnt",".cn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("第三方拼接后的地址：" + url);
        return url;
    }

    public static String sm3Sign(String data) {
        String sm3Sign = SmUtil.sm3(data);
        return sm3Sign;
    }

}
