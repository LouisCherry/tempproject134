package com.epoint.jnycsl.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.string.StringUtil;

/**
 * http请求工具类
 *
 * @author 刘雨雨
 * @time 2018年9月11日下午5:31:54
 */
public class HttpUtils {

    private static final Logger logger = Logger.getLogger(HttpUtils.class);

    /**
     * 连接超时 15秒
     */
    private static final int DEFAULT_TIME_OUT = 15 * 1000;

    /**
     * 默认编码UTF-8
     */
    private static final String DEFAULT_ENCODE = "UTF-8";

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

    public static String getHttp(String url, Map<String, String> bodyParams) throws IOException {
        return getHttp(url, bodyParams, null);
    }

    public static String postHttp(String url, Map<String, String> requestBodyParams) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }

    public static String postHttp(String url, Map<String, String> requestBodyParams, Map<String, String> headerParams) {
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
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }

    /**
     * 信用相关请求
     *
     * @param url
     * @param apikey
     */
    public static String postXinyong(String url) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody body = RequestBody.create(mediaType, "");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("ApiKey", "725356385771978752")
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .build();
        Response response = null;
        String responseData = "";
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                responseData = response.body().string();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseData;
    }

    /**
     * 访问新点网厅的http接口，会在真实参数上包装上新点的token，以符合新点的传参格式
     *
     * @param url
     * @param requestBodyParams
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String postEpointHttp(String url, JSONObject params) throws HttpException, IOException {
        if (params == null) {
            params = new JSONObject();
        }
        JSONObject requestBodyParams = new JSONObject();
        requestBodyParams.put("token", EpointTokenUtils.TOKEN);
        requestBodyParams.put("params", params);
        return postHttp(url, requestBodyParams.toJSONString(), "application/json", "UTF-8");
    }

    /**
     * 实际调用的是postHttp(url, requestBodyParams, "application/json", "UTF-8")
     *
     * @param url
     * @param requestBodyParams json格式的字符串参数
     * @return
     * @throws IOException
     * @throws HttpException
     */
    public static String postHttp(String url, String requestBodyParams) throws IOException {
        return postHttp(url, requestBodyParams, "application/json", "UTF-8");
    }

    public static String postHttp(String url, String requestBodyParams, String contentType, String charset)
            throws IOException {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(DEFAULT_ENCODE);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(DEFAULT_TIME_OUT);
        PostMethod postMethod = new PostMethod(url);
        try {
            RequestEntity requestEntity = new StringRequestEntity(requestBodyParams, contentType, charset);
            postMethod.setRequestEntity(requestEntity);
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            responseMsg = out.toString(DEFAULT_ENCODE);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }

    /**
     * 解析新点http接口的响应
     *
     * @param response
     * @return
     */
    public static JSONObject parseEpointHttpResponse(String response) {
        if (StringUtil.isNotBlank(response)) {
            JSONObject resultObj = JSONObject.parseObject(response);
            if (resultObj != null) {
                JSONObject custom = resultObj.getJSONObject("custom");
                return custom;
            }
        }
        return null;
    }


}
