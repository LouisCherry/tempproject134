package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnycsl.utils.HttpUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/companycredit")
public class CompanyCreditRestController
{

    final static String appKey = "b97f78b8c85f419cb2da32603ea8dc68";
    final static String appsecret = "U2FsdGVkX18y2KN9pRiz5soKFbZAtoEEHEmuQrwLC1AJ";
    transient Logger log = LogUtil.getLog(CompanyCreditRestController.class);

    /**
     * 红名单企业查询
     * @param params
     * @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getredlist", method = RequestMethod.POST)
    public String getRedList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String creditCode = obj.getString("creditCode");
            JSONObject dataJson = new JSONObject();
            String httpurl = "http://59.206.96.178:9960/gateway/api/1/jnfg_hmd1";
            String ApiKey = "596738364502179840";
            Map<String, String> headerRequest = new HashMap<String, String>();
            headerRequest.put("ApiKey", ApiKey);
            Map<String, String> bodyRequest = new HashMap<String, String>();
            Map<String, Object> val = new HashMap<>();
            val.put("creditCode", creditCode);
            val.put("flag", "1");
            String sign = JnSelfservicemachineSignUtil.getSign(val, appsecret);
            String url = httpurl + "?appKey=" + appKey + "&creditCode=" + creditCode + "&flag=1&sign=" + sign;
            String str = HttpUtils.getHttp(url, bodyRequest, headerRequest);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String data = responseJsonObj.getString("data");
            responseJsonObj = (JSONObject) JSONObject.parse(data);

            dataJson.put("hhbList", responseJsonObj);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 黑名单企业查询
     * @param params
     * @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getblacklist", method = RequestMethod.POST)
    public String getBlackList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String creditCode = obj.getString("creditCode");
            JSONObject dataJson = new JSONObject();
            String httpurl = "http://59.206.96.178:9960/gateway/api/1/jnfg_hmd";
            String ApiKey = "596738138009763840";
            Map<String, String> headerRequest = new HashMap<String, String>();
            headerRequest.put("ApiKey", ApiKey);
            Map<String, String> bodyRequest = new HashMap<String, String>();
            Map<String, Object> val = new HashMap<>();
            val.put("creditCode", creditCode);
            val.put("flag", "2");
            String sign = JnSelfservicemachineSignUtil.getSign(val, appsecret);
            String url = httpurl + "?appKey=" + appKey + "&creditCode=" + creditCode + "&flag=2&sign=" + sign;
            String str = HttpUtils.getHttp(url, bodyRequest, headerRequest);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String data = responseJsonObj.getString("data");
            responseJsonObj = (JSONObject) JSONObject.parse(data);
            dataJson.put("hhbList", responseJsonObj);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 依据身份证查询个人不动产信息
     * @param params
     * @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getbdcbysfz", method = RequestMethod.POST)
    public String getBdcBySfz(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String zjhm = obj.getString("zjhm");
            JSONObject dataJson = new JSONObject();
            String httpurl = "http://59.206.96.178:9960/gateway/api/1/jnsbdc_gjsfzcxbdcxx";
            String ApiKey = "715222567178207232";
            Map<String, String> headerRequest = new HashMap<String, String>();
            headerRequest.put("ApiKey", ApiKey);
            Map<String, String> bodyRequest = new HashMap<String, String>();

            String url = httpurl + "?ApiKey=" + ApiKey + "&zjhm=" + zjhm;
            String str = HttpUtils.getHttp(url, bodyRequest, headerRequest);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String data = responseJsonObj.getString("data");
            responseJsonObj = (JSONObject) JSONObject.parse(data);
            dataJson.put("info", responseJsonObj);
            log.info("不动产查询：" + str);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }

    /**
     * 依据身份证查询个人不动产信息-市里调用
     * @param params
     * @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/getcitybdcbysfz", method = RequestMethod.POST)
    public String getCityBdcBySfz(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String zjhm = obj.getString("zjhm");
            JSONObject dataJson = new JSONObject();
            String httpurl = "http://59.206.96.178:9960/gateway/api/1/jnsbdc_gjsfzcxbdcxx";
            String ApiKey = "715222567178207232";
            Map<String, String> headerRequest = new HashMap<String, String>();
            headerRequest.put("ApiKey", ApiKey);
            Map<String, String> bodyRequest = new HashMap<String, String>();

            String url = httpurl + "?ApiKey=" + ApiKey + "&zjhm=" + zjhm;
            String str = HttpUtils.getHttp(url, bodyRequest, headerRequest);
            JSONObject responseJsonObj = new JSONObject();
            responseJsonObj = (JSONObject) JSONObject.parse(str);
            String data = responseJsonObj.getString("data");
            responseJsonObj = (JSONObject) JSONObject.parse(data);
            dataJson.put("info", responseJsonObj);

            return JsonUtils.zwdtRestReturn("1", "", dataJson);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "接口发生异常：" + e.getMessage(), "");
        }
    }


    //转发接口
    @RequestMapping(value = "/getUrlBack", method = RequestMethod.POST)
    public String getUrlBack(@RequestBody String params){

        String url = "http://59.206.96.178:9960/gateway/";
        JSONObject trueJson = new JSONObject(false);
        JSONObject jsonObject = trueJson.parseObject(params);
        String appkey =jsonObject.getString("appkey");
        String appsecert =jsonObject.getString("appsecert");
        String apikey =jsonObject.getString("apikey");
        String apiname =jsonObject.getString("apiname");
        Map<String, Object> val = new HashMap<>();
        JSONObject param = jsonObject.getJSONObject("param");
        //system.out.println(param);
        StringBuffer sb = new StringBuffer("");
        HttpURLConnection connection = null;

        StringBuffer urlBuffer = new StringBuffer();
        JSONObject dataJson = new JSONObject();
        try {
            urlBuffer.append(url);
            urlBuffer.append(apiname);
            urlBuffer.append("?appKey=" + appkey);

            if(StringUtil.isNotBlank(param)){
                Set<String> keys =  param.keySet();

                for (String key :keys) {
                    urlBuffer.append("&" + key + "=" + URLEncoder.encode(param.getString(key), "UTF-8"));
                }
            }
            String sign = new SignUtil().getSign(param, appsecert);

            urlBuffer.append("&sign=" + sign);

            Map<String,String> headermap = new HashMap<>();
            headermap.put("ApiKey",apikey);
            log.info("调用共享平台的链接：" + urlBuffer.toString());

            CloseableHttpClient httpClient;

            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL)
                    .loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSslcontext(sslContext).setSSLHostnameVerifier((x, y) -> true).build();

            String result = HTTPSClientUtil.doPost(httpClient, urlBuffer.toString(),headermap, null);
            log.info("济宁共享平台接口返回数据：:" + result);
            return  JsonUtils.zwdtRestReturn("1", "", result);

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

    
    
    


    @RequestMapping(value = "/getUrlBackByJN", method = RequestMethod.POST)
    public String getUrlBackByJN(@RequestBody String params){

            Map<String,String> headermap = new HashMap<>();
            CloseableHttpClient httpClient;

            try{
            SSLContext sslContext = SSLContextBuilder.create().useProtocol(SSLConnectionSocketFactory.SSL)
                    .loadTrustMaterial((x, y) -> true).build();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000).build();
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).setSslcontext(sslContext).setSSLHostnameVerifier((x, y) -> true).build();

            String result = HTTPSClientUtil.doPost(httpClient, "http://112.6.110.176:28080/jnzwfwznsb/rest/companycredit/getUrlBack",headermap, JSONObject.parseObject(params));
            log.info("济宁共享平台接口返回数据：:" + result);
            return  result;

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
