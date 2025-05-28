package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditselfservice.auditselfservicerest.common.PrintRestController;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
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
@RequestMapping(value = "/selfrs")
public class SelfRSRestController extends AuditCommonService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    transient Logger log = LogUtil.getLog(PrintRestController.class);

    /**
     * 人社总接口
     */
    @RequestMapping(value = "/getBackString", method = RequestMethod.POST)
    public String getBackString(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=====================开始调用医保人社接口返回====================" );
            JSONObject e = JSON.parseObject(params);
            JsonUtils.checkUserAuth(e.getString("token"));
            JSONObject obj = (JSONObject) e.get("params");
            String serviceName = e.getString("serviceName");
            String operationName = e.getString("operationName");
            String type =  e.getString("type");
            JSONObject dataJson = new JSONObject();
            String backString = "";
            if("YB".equals(type)){
                 backString = DwPspProxy.queryYBString(serviceName,operationName,(Map)obj);
            }else{
                  backString = DwPspProxy.queryString(serviceName,operationName,(Map)obj);
            }
            log.info("=====================调用医保人社接口返回====================" + backString);



            dataJson.put("info",backString);
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



    //转发接口
    @RequestMapping(value = "/getTest", method = RequestMethod.POST)
    public String getTest(@RequestBody String params){


        JSONObject dataJson = new JSONObject();
        try {

            GetHealthPassByUserInfo.newJzQRCode("孙超","370811199103220055");
            return  JsonUtils.zwdtRestReturn("1", "", dataJson);

        } catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
