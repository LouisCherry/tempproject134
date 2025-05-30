package com.epoint.xmz.action;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.departdocking.provinceselfbuild.api.IProviceSelfBuild;


@RestController
@RequestMapping("/rsjrestcontroller")
public class RsjRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private IProviceSelfBuild proviceSelfBuildService;

    @RequestMapping(value = "/getRsjData", method = RequestMethod.POST)
    public String getRsjData(@RequestBody String params) {
        try {
            log.info("=======开始通用getRsjData接口=======" + new Date());
            JSONObject jsonObject = JSONObject.parseObject(params);
            String apiKey= jsonObject.getString("APIKEY");
            String paramJson = jsonObject.getString("ParamJson");
            String result = sendPost(paramJson,apiKey);
            log.info("============"+jsonObject+"==========");
            return JsonUtils.zwdtRestReturn("1", "调用成功：", result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "调用异常：", "");
        }
    } 
    
    @RequestMapping(value = "/wwGetRsjData", method = RequestMethod.POST)
    public String wwGetRsjData(@RequestBody String params) {
        try {
            log.info("=======开始通用wwGetRsjData接口=======" + new Date());
            JSONObject jsonObject = JSONObject.parseObject(params);
            log.info("======jsonObject======"+jsonObject+"==========");
            String param = jsonObject.getString("params");
            JSONObject oneJsonObject = JSONObject.parseObject(param);
            log.info("======oneJsonObject======"+oneJsonObject+"==========");
            String apiKey= oneJsonObject.getString("APIKEY");
            String paramJson = oneJsonObject.getString("ParamJson");
            String result = sendPost(paramJson,apiKey);
            return JsonUtils.zwdtRestReturn("1", "调用成功：", result);
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "调用异常：", "");
        }
    } 
    /**
     * 请求方法获取人社接口返回信息
     * @description
     * @author lya
     * @date  2020年12月4日 下午2:08:20
     */
    public static String sendPost(String paramJson, String ApiKey) {
        String url = "http://59.206.31.193:8066/api/Proxy/HandleByKey";
        PostMethod postMethod = null;
        postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
        NameValuePair[] data = {new NameValuePair("APIKEY", ApiKey), new NameValuePair("ParamJson", paramJson) };
        postMethod.setRequestBody(data);
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        String result = null;
        try {
        int response = httpClient.executeMethod(postMethod);
        result = postMethod.getResponseBodyAsString();
//        JSONObject paramObj = JSONObject.parseObject(result);
//        String accessTokenData = paramObj.getString("data");
//        if (StringUtil.isNotBlank(accessTokenData)) {
//            Document document = DocumentHelper.parseText(accessTokenData);
//            Element rootElement = document.getRootElement();
//            accessToken = rootElement.elementText("TOKEN"); // 申办流水号
//        }
//
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
