package com.epoint.xmz.util;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

public class TaHttpRequestUtils
{
    transient static Logger log = LogUtil.getLog(TaHttpRequestUtils.class);
    
    /**
     * 连接超时 15秒
     */
    private static final int DEFAULT_TIME_OUT = 15 * 1000;

    /**
     * 默认编码UTF-8
     */
    private static final String DEFAULT_ENCODE = "UTF-8";
    
    public static String sendPostRequest(String url,String paramJson, String ApiKey) {
       // String url = "http://59.206.26.31:8066/api/Proxy/HandleByKey";
        PostMethod postMethod = null;
        postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
        NameValuePair[] data = {new NameValuePair("APIKEY", ApiKey), new NameValuePair("ParamJson", paramJson)};
        postMethod.setRequestBody(data);
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        String result = null;
        try {
            int response = httpClient.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    

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
            String urlNameString =url;
            if(StringUtil.isNotBlank(param)){
                if(url.indexOf("?")!=-1){
                    urlNameString = url + "&" + param;
                }else{
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
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
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
        Logger log = LogUtil.getLog(TaHttpRequestUtils.class);
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
                connection.setRequestProperty("Content-Type", "application/json");
            }
            // 在Authentication设置 Bearer+空格+AccessToken
            if (StringUtil.isNotBlank(token)) {
                connection.setRequestProperty("Authorization", "Bearer " + token);
            }
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(connection.getOutputStream());
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "utf-8"));
            //out.write(param.getBytes("UTF-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
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
    public static String postHttp(String url, Map<String, String> requestBodyParams,Map<String, String> headerParams) {
//        String responseMsg = "";
//        HttpClient httpClient = new HttpClient();
//        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
//        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
//        PostMethod postMethod = new PostMethod(url);
//        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        if (headerParams != null) {
//            for (String key : headerParams.keySet()) {
//                postMethod.addRequestHeader(key, headerParams.get(key));
//            }
//        }
//        for (String key : requestBodyParams.keySet()) {
//            postMethod.addParameter(key, requestBodyParams.get(key));
//        
//        }
//       // postMethod.
//        try {
//            httpClient.executeMethod(postMethod);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            InputStream in = postMethod.getResponseBodyAsStream();
//            int len = 0;
//            byte[] buf = new byte[1024];
//            while ((len = in.read(buf)) != -1) {
//                out.write(buf, 0, len);
//            }
//            responseMsg = out.toString(DEFAULT_ENCODE);
//        } catch (HttpException e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException(e);
//        } finally {
//            postMethod.releaseConnection();
//        }
//        return responseMsg;
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (headerParams != null) {
            for (String key : headerParams.keySet()) {
                postMethod.addRequestHeader(key, headerParams.get(key));
            }
        }
        for (String key : requestBodyParams.keySet()) {
            postMethod.addParameter(key, requestBodyParams.get(key));
        
        }
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            responseMsg = out.toString(DEFAULT_ENCODE);
        } catch (HttpException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }
    
    
    
    public static String postHttp(String url,JSONObject jsonObject,Map<String, String> headerParams) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (headerParams != null) {
            for (String key : headerParams.keySet()) {
                postMethod.addRequestHeader(key, headerParams.get(key));
            }
        }
        String  toJson = jsonObject.toString();
        RequestEntity se;
        try {
            se = new StringRequestEntity (toJson ,"application/json" ,"UTF-8");
            postMethod.setRequestEntity(se);
        }
        catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


       
       // postMethod.
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            responseMsg = out.toString(DEFAULT_ENCODE);
        } catch (HttpException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }
    
    public static String postHttp1(String url,JSONObject jsonObject,Map<String, String> headerParams) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/json");
        if (headerParams != null) {
            for (String key : headerParams.keySet()) {
                postMethod.addRequestHeader(key, headerParams.get(key));
            }
        }
        String  toJson = jsonObject.toString();
        RequestEntity se;
        try {
            se = new StringRequestEntity (toJson ,"application/json" ,"UTF-8");
            postMethod.setRequestEntity(se);
        }
        catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


       
       // postMethod.
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            responseMsg = out.toString(DEFAULT_ENCODE);
        } catch (HttpException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }
    
    public static String postHttp2(String url,JSONObject jsonObject,Map<String, String> headerParams) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "multipart/form-data");
        if (headerParams != null) {
            for (String key : headerParams.keySet()) {
                postMethod.addRequestHeader(key, headerParams.get(key));
            }
        }
        String  toJson = jsonObject.toString();
        RequestEntity se;
        try {
            se = new StringRequestEntity (toJson ,"application/json" ,"UTF-8");
            postMethod.setRequestEntity(se);
        }
        catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


       
       // postMethod.
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            responseMsg = out.toString(DEFAULT_ENCODE);
        } catch (HttpException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }
    
    public static String getHttp(String url, Map<String, String> bodayParams, Map<String, String> headerParams)
            throws IOException {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        if (headerParams != null) {
            for (String key : headerParams.keySet()) {
                getMethod.addRequestHeader(key, headerParams.get(key));
            }
        }
        HttpMethodParams httpMethodParams = new HttpMethodParams();
        if (bodayParams != null) {
            for (String key : bodayParams.keySet()) {
                httpMethodParams.setParameter(key, bodayParams.get(key));
            }
        }

        getMethod.setParams(httpMethodParams);
        try {
            httpClient.executeMethod(getMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = getMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            responseMsg = out.toString(DEFAULT_ENCODE);
        } finally {
            getMethod.releaseConnection();
        }
        return responseMsg;
    }
    
    /**
     * 
     *  [调用接口推送办理过程信息] 
     *  @param paramJson
     *  @param newUrl
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static String sendPostRequest(String paramJson,String newUrl) {
        String url = newUrl;
        PostMethod postMethod = null;
        postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        //参数设置，需要注意的就是里边不能传NULL，要传空字符串
        NameValuePair[] data = { new NameValuePair("data", paramJson)};
        postMethod.setRequestBody(data);
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        String result = null;
        try {
            int response = httpClient.executeMethod(postMethod);
            result = postMethod.getResponseBodyAsString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
