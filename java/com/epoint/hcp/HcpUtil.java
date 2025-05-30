package com.epoint.hcp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.config.ConfigUtil;

public class HcpUtil{
    
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    
    
    // 实现httpClient POST提交
    public static String doPost(String url, Map<String, String> params, String charset) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = null;

        // 1.定义请求类型
        HttpPost post = new HttpPost(url);
        // post.setHeader("Authorization", "Bearer cd11eeab1709d9dbc2fb5edccf34b9c5");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        // post.setConfig(requestConfig); // 定义超时时间

        // 2.判断字符集是否为null
        if (StringUtils.isEmpty(charset)) {
            charset = "UTF-8";
        }

        // 3.判断用户是否传递参数
        if (params != null) {
            // 3.2准备List集合信息
            List<NameValuePair> parameters = new ArrayList<>();

            // 3.3将数据封装到List集合中
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 3.1模拟表单提交
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, charset); // 采用u8编码
                // 3.4将实体对象封装到请求对象中
                post.setEntity(formEntity);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 4.发送请求
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            System.out.println("返回值：" + response.getEntity());
            // 4.1判断返回值状态
            if (response.getStatusLine().getStatusCode() == 200) {

                // 4.2表示请求成功
                result = EntityUtils.toString(response.getEntity(), charset);
            }
            else {
                System.out.println("获取状态码信息:" + response.getStatusLine().getStatusCode());
                throw new RuntimeException();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 发送post请求
     * @param url  路径
     * @param jsonObject  参数(json类型)
     * @param encoding 编码格式
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String send(String url, JSONObject jsonObject, String encoding) throws ParseException, IOException {
        String body = "";

        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //设置参数到请求对象中
        httpPost.setEntity(s);
        System.out.println("请求地址：" + url);
        //        System.out.println("请求参数："+nvps.toString());

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        //        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }

    public static String getUserDataZwdt(String accessToken) throws Exception {
        String userInfoUrl = ConfigUtil.getConfigValue("epointframe", "xjwwrzurl")
                + "/rest/extranetNeedLoginInterfaceapp/getUserDataZwdt";
        Map<String, String> herders = new HashMap<>();
        herders.put("Authorization", "Bearer " + accessToken);
        herders.put("Content-Type", "application/x-www-form-urlencoded");
        herders.put("Cache-Control", "no-cache");
        String result = sendPost(userInfoUrl, null, herders);
        return result;
    }
    
    public static String sendPost(String url, Map<String, String> params, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = null;

        // 1.定义请求类型
        HttpPost post = new HttpPost(url);

        // 设置通用的请求属性
        if (header!=null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 3.判断用户是否传递参数
        if (params != null) {
            // 3.2准备List集合信息
            List<NameValuePair> parameters = new ArrayList<>();
            // 3.3将数据封装到List集合中
            for (Map.Entry<String, String> entry : params.entrySet()) {
                parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 3.1模拟表单提交
            try {
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "utf-8"); // 采用u8编码
                // 3.4将实体对象封装到请求对象中
                post.setEntity(formEntity);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        // 4.发送请求
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            System.out.println("返回值：" + response.getEntity());
            // 4.1判断返回值状态
            if (response.getStatusLine().getStatusCode() == 200) {
                // 4.2表示请求成功
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            else {
                System.out.println("获取状态码信息:" + response.getStatusLine().getStatusCode());
                throw new RuntimeException();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 办件状态
     */
    public String projectStatus(String tip) {
        String code = "";
        switch (tip) {
            case "1"://待受理
                code = "待受理";
                break;
            case "2"://已受理
                code = "已受理";
                break;
            case "3"://已办结
                code = "已办结";
                break;
            default:
                break;
        }
        return code;
    }
}
