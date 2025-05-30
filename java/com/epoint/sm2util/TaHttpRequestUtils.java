package com.epoint.sm2util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

public class TaHttpRequestUtils {
    transient static Logger log = LogUtil.getLog(TaHttpRequestUtils.class);

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
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
                } else {
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
        } catch (Exception e) {
            log.error("发送GET请求出现异常！" + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String contentType, String token) {
        Logger log = LogUtil.getLog(TaHttpRequestUtils.class);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            if (StringUtil.isNotBlank(contentType)) {
                connection.setRequestProperty("Content-Type", contentType);
            } else {
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
            // out = new PrintWriter(connection.getOutputStream());
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
                if (result.length() > 1024 * 1024) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public static String sendposturl(String url, String request) {
        PostMethod postMethod = new PostMethod(url);
        JSONObject jsonObj = JSON.parseObject(request);
        // jsonObj.put("request", request);
        Set<Entry<String, Object>> set = jsonObj.entrySet();
        for (Entry<String, Object> entry : set) {
            postMethod.setParameter(entry.getKey(), String.valueOf(entry.getValue()));
        }

        String result = "";
        try {
            HttpClient httpClient = new HttpClient();
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            httpClient.setConnectionTimeout(30000);
            httpClient.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

}
