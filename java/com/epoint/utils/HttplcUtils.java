package com.epoint.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;

public class HttplcUtils
{
    /**
     * 
     *  Get工具类
     *  @param url
     *  @param params
     *  @param headerParams
     *  @return    
     * @throws IOException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject getHttp(String url, String params2Get) throws IOException {
        JSONObject responseJsonObj = new JSONObject();
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("UTF-8");
        System.out.println(url + params2Get);
        GetMethod getMethod = new GetMethod(url + params2Get);
        httpClient.executeMethod(getMethod);
        responseJsonObj =  (JSONObject) JSONObject.parse(getMethod.getResponseBodyAsString());
        return responseJsonObj;
    }
    
    /**
     * 
     *  post工具类
     *  @param url
     *  @param params
     *  @return    
     * @throws IOException 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject postHttp(String url,Map<String, Object> params) throws IOException {
        JSONObject responseJsonObj = new JSONObject();
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("UTF-8"); 
        System.out.println(url);
        PostMethod postMethod = new PostMethod(url);
        //设置Http Post数据    
        if (params != null) {   
            for (Map.Entry<String, Object> entry : params.entrySet()) {      
                postMethod.setParameter(entry.getKey(), entry.getValue().toString());   
            }   
        }
        httpClient.executeMethod(postMethod);
        InputStream stream = postMethod.getResponseBodyAsStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));  
            StringBuffer stringBuffer = new StringBuffer(); 
            String str1= "";  
            while((str1 = br.readLine()) !=null){  
              stringBuffer.append(str1);  
            }  
           String sb=stringBuffer.toString();
           //按照utf-8格式输出响应体
         String result = new String(sb.getBytes(),"UTF-8");
        responseJsonObj = (JSONObject) JSONObject.parse(result);
        return responseJsonObj;
    }
}
