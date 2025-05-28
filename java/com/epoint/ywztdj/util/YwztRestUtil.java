package com.epoint.ywztdj.util;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.ywztdj.apiywztlog.api.IApiYwztLogService;
import com.epoint.ywztdj.apiywztlog.api.entity.ApiYwztLog;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.http.MediaType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * @title: YwztRestUtil
 * @Author 成都研发4部-付荣煜
 * @Date: 2024/1/8 15:48
 */
public class YwztRestUtil {
    private static Logger log = Logger.getLogger(YwztRestUtil.class);

    /**
     * getHeaderMap 封装请求头
     *
     * @return Map<String>
     * @author 成都研发4部-付荣煜
     * @date 2024/1/8 15:49
     */
    public static Map<String, String> getHeaderMap() {
        //获取配置文件参数
        String appkey = ConfigUtil.getConfigValue("ywztrest", "x-aep-appkey");
        String appsecret = ConfigUtil.getConfigValue("ywztrest", "x-aep-appsecret");

        String timestamp = System.currentTimeMillis() + "";
        String nonce = UUID.randomUUID().toString();

        //构造请求头
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("x-aep-timestamp", timestamp);
        headerMap.put("x-aep-appkey", appkey); //获取系统参数
        headerMap.put("x-aep-nonce", nonce);

        try {
            //封装签名
            String signature = hmacSha256("x-aep-appkey=" + appkey + "&x-aep-nonce=" + nonce + "&x-aep-timestamp=" + timestamp, appsecret);
            headerMap.put("x-aep-signature", signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return headerMap;
    }

    public static String parsexml(String xmlString) {
        JSONObject rootJson = new JSONObject();
        try {
            Document document = DocumentHelper.parseText(xmlString);
            List<Attribute> sAttrList = null;
            Element root = document.getRootElement();
            List<Element> sElementList = root.elements("APPROVEDATAINFO");
            if (!sElementList.isEmpty()) {
                for (Element sElement : sElementList) {
                    sAttrList = sElement.attributes();
                    for (Attribute attribute : sAttrList) {
                        rootJson.put(attribute.getName(), attribute.getValue());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootJson.toJSONString();
    }

    private static final char[] DIGITS;

    public static String hmacSha256(final String secret, final String data) throws Exception {
        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            final SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), mac.getAlgorithm());
            mac.init(signingKey);
            return encodeHex(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new Exception("Fail to generate HMAC-SHA256 signature");
        }
    }

    private static String encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        int i = 0;
        int j = 0;
        while (i < l) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0xF & data[i]];
            ++i;
        }
        return new String(out);
    }

    static {
        DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    }

    /**
     * ywztRestXmlReturn 接口固定返回
     *
     * @param code
     * @param text
     * @param custom
     * @return String
     * @author 成都研发4部-付荣煜
     * @date 2024/1/15 14:15
     */
    public static String ywztRestXmlReturn(String code, String text, String custom) {
        JSONObject jsonReturn = new JSONObject();

        //封装三方接口入参
        Document document = DocumentHelper.createDocument();
        //设置编码
        document.setXMLEncoding("utf-8");
        //创建根节点
        Element result = document.addElement("RESULT");
        //在根节点加入子节点
        Element status = result.addElement("STATUS");
        status.setText(code);
        Element desc = result.addElement("DESC");
        desc.setText(text);
        Element time = result.addElement("TIME");
        time.setText(System.currentTimeMillis() + "");
        Element data = result.addElement("data");
        try {
            JSONObject object;
            if (StringUtil.isNotBlank(custom)) {
                object = JSONObject.parseObject(custom);
                for (Map.Entry<String, Object> entry : object.entrySet()) {
                    Element element = data.addElement(entry.getKey());
                    element.setText(entry.getValue().toString());
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            jsonReturn.put("data", var7.getMessage());
        }
        jsonReturn.put("data", document.asXML());
        jsonReturn.put("status", code);
        return jsonReturn.toString();
    }

    /**
     * insertApiYwztLog 记录日志
     *
     * @param url            接口地址
     * @param requestXml     入参
     * @param callresultid   返回结果
     * @param callresultexpl 报错内容
     * @param interfaceName  接口名称
     * @author 成都研发4部-付荣煜
     * @date 2024/1/9 15:24
     */
    public static void insertApiYwztLog(String url, String requestXml, int callresultid, String callresultexpl, String interfaceName) {
        IApiYwztLogService iApiYwztLog = ContainerFactory.getContainInfo().getComponent(IApiYwztLogService.class);
        Date now = new Date();
        ApiYwztLog apiYwztLog = new ApiYwztLog();
        apiYwztLog.setRowguid(UUID.randomUUID().toString());
        apiYwztLog.setCreated_time(now);
        apiYwztLog.setOperatedate(now);
        apiYwztLog.setBusinessid("业务中台对接");
        apiYwztLog.setBusinessname("业务中台对接");
        apiYwztLog.setInterfacename(interfaceName);
        apiYwztLog.setInterfaceuri(url);
        apiYwztLog.setParams(requestXml);
        apiYwztLog.setCallresultid(callresultid);
        apiYwztLog.setCallresultexpl(callresultexpl);
        iApiYwztLog.insert(apiYwztLog);
    }

    /**
     * 请求ID参数(唯一参数,使用UUID生成即可)
     **/
    public static final String AEP_NONCE = "x-aep-nonce";
    /**
     * appkey参数(应用appkey)
     */
    public static final String AEP_APPKEY = "x-aep-appkey";
    /**
     * 时间戳参数
     */
    public static final String AEP_TIMESTAMP = "x-aep-timestamp";
    /**
     * 签名参数
     **/
    public static final String AEP_SIGNATURE = "x-aep-signature";
    /**
     * 是否加密(默认false)
     */
    public static final String AEP_ENCRYPTION = "x-aep-encryption";

    /**
     * APP_KEY Value
     */
    final static String appKeyValue = ConfigUtil.getConfigValue("ywztrest", "x-aep-appkey");
    ;
    /**
     * APP_SECRET Value
     */
    final static String appSecretValue = ConfigUtil.getConfigValue("ywztrest", "x-aep-appsecret");
    ;

    public static String post(String url, JSONObject jsonObject) {
        try {

//            OkHttpClient client = new OkHttpClient();
//
//            String uuid = UUID.randomUUID().toString();
//            String timestamp = String.valueOf(System.currentTimeMillis());
//            MediaType mediaType = MediaType.parse("application/json");
//            RequestBody body = RequestBody.create(mediaType, jsonObject.toJSONString());
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .addHeader("x-aep-nonce", uuid)
//                    .addHeader("x-aep-appkey", appKeyValue)
//                    .addHeader("x-aep-timestamp", timestamp)
//                    .addHeader("x-aep-signature", hmacSha256(appSecretValue, getBody(uuid, timestamp)))
//                    .addHeader("Content-Type", "application/json")
//                    .addHeader("User-Agent", "PostmanRuntime/7.15.0")
//                    .addHeader("Accept", "*/*")
//                    .addHeader("Cache-Control", "no-cache")
//                    .addHeader("Postman-Token", "da8cbc0e-2b10-4b30-a633-a7e001b27a55,21d58b53-f676-478d-b9ec-a039c306da6d")
//                    .addHeader("Host", "172.20.240.160:31905")
//                    .addHeader("accept-encoding", "gzip, deflate")
//                    .addHeader("content-length", "889")
//                    .addHeader("Connection", "keep-alive")
//                    .addHeader("cache-control", "no-cache")
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            return response.body().string();

            String uuid = UUID.randomUUID().toString();
            String timestamp = String.valueOf(System.currentTimeMillis());
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader(AEP_NONCE, uuid);
            httpPost.addHeader(AEP_APPKEY, appKeyValue);
            httpPost.addHeader(AEP_TIMESTAMP, timestamp);
            httpPost.addHeader(AEP_SIGNATURE, hmacSha256(appSecretValue, getBody(uuid, timestamp)));
            // 解决中文乱码问题
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
            stringEntity.setContentEncoding("utf-8");
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                params.add(pair);
            }
            stringEntity.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            System.out.println(entity.toString());
            if (entity != null) {
                return EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBody(String uuid, String time) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put(AEP_NONCE, uuid);
        objectMap.put(AEP_APPKEY, appKeyValue);
        objectMap.put(AEP_TIMESTAMP, time);
        return getSignContent(objectMap);
    }

    public static String getSignContent(Map<String, String> sortedParams) {
        final StringBuffer content = new StringBuffer();
        final List<String> keys = new ArrayList<>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        int size = keys.size();
        for (int i = 0; i < size; ++i) {
            final String key = keys.get(i);
            final String value = sortedParams.get(key);
            if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(value)) {
                content.append(((index == 0) ? "" : "&") + key + "=" + value);
                ++index;
            }
        }
        return content.toString();
    }

    /**
     * decodeXmlData 解析三方接口返回
     *
     * @param result
     * @return JSONObject
     * @author 成都研发4部-付荣煜
     * @date 2024/2/18 15:58
     */
    public static JSONObject decodeXmlData(String result) {
        JSONObject resultObj = new JSONObject();
        //解析xml
        try {
            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            //加密数据
            String data = root.elementText("data");
            //解密数据
            String sm4Key = ConfigUtil.getConfigValue("ywztrest", "sm4Key");
            String jsonStr = Sm4Util.decryptCbcPadding(sm4Key, data);
            if (StringUtil.isNotBlank(jsonStr)) {
                resultObj = JSONObject.parseObject(jsonStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj;
    }

    public static JSONObject decodeJSONData(String result) {
        JSONObject resultObj = JSONObject.parseObject(result);
        //解析xml
        try {
            //加密数据
            String data = resultObj.getString("data");
            //解密数据
            String sm4Key = ConfigUtil.getConfigValue("ywztrest", "sm4Key");
            String jsonStr = Sm4Util.decryptCbcPadding(sm4Key, data);
            if (StringUtil.isNotBlank(jsonStr)) {
                resultObj = JSONObject.parseObject(jsonStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObj;
    }
}
