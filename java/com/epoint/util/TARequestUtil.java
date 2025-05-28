package com.epoint.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

public class TARequestUtil
{
    transient static Logger log = LogUtil.getLog(TARequestUtil.class);

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String token) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if (StringUtil.isNotBlank(param)) {
                if (url.indexOf("?") != -1) {
                    urlNameString = url + "&" + param;
                }
                else {
                    urlNameString = url + "?" + param;
                }
            }
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            // 在Authentication设置 Bearer+空格+AccessToken
            if (StringUtil.isNotBlank(token)) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                log.debug(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            log.error("发送GET请求出现异常！" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String contentType, String token) {
        Logger log = LogUtil.getLog(TARequestUtil.class);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            if (StringUtil.isNotBlank(contentType)) {
                connection.setRequestProperty("Content-Type", contentType);
            }
            else {
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            }
            // 在Authentication设置 Bearer+空格+AccessToken
            if (StringUtil.isNotBlank(token)) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            //out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            log.error("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPostInner(String url, String param, String contentType, String token) {
        Logger log = LogUtil.getLog(TARequestUtil.class);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("connection", "Keep-Alive");
            if (StringUtil.isNotBlank(contentType)) {
                connection.setRequestProperty("Content-Type", contentType);
            }
            else {
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            }
            // 在Authentication设置 Bearer+空格+AccessToken
            if (StringUtil.isNotBlank(token)) {
                connection.setRequestProperty("Authorization", token);
            }
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            //out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            log.error("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 
     * @Description: 封装网厅json传输格式  
     * @author male   
     * @date 2019年4月1日 上午8:55:59
     * @return JSONObject    返回类型    
     * @throws
     */
    public static JSONObject zwdtSumbit(String param) {
        JSONObject sumbit = new JSONObject();
        sumbit.put("token", "Epoint_WebSerivce_**##0601");
        sumbit.put("params", param);
        return sumbit;
    }

    /**
     * 
     * @Description: 以form表单形式提交数据，发送post请求
     * @author male   
     * @date 2019年4月3日 上午10:31:23
     * @explain 
     *   1.请求头：httppost.setHeader("Content-Type","application/x-www-form-urlencoded")
     *   2.提交的数据格式：key1=value1&key2=value2...
     * @param url 请求地址
     * @param paramsMap 具体数据
     * @return 服务器返回数据
     */
    public static String httpPostWithForm(String url, Map<String, String> paramsMap) {
        // 用于接收返回的结果
        String resultData = "";
        try {
            HttpPost post = new HttpPost(url);

            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            // 迭代Map-->取出key,value放到BasicNameValuePair对象中-->添加到list中
            for (String key : paramsMap.keySet()) {
                pairList.add(new BasicNameValuePair(key, paramsMap.get(key)));
            }
            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairList, "utf-8");
            post.setEntity(uefe);
            // 创建一个http客户端
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            // 发送post请求
            HttpResponse response = httpClient.execute(post);
            response.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            // 状态码为：200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 返回数据：
                resultData = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            else {
                throw new RuntimeException("接口连接失败！");
            }
        }
        catch (Exception e) {
            throw new RuntimeException("接口连接失败！");
        }
        return resultData;
    }

}
