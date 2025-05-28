package com.epoint.auditproject.guiji.utils;

import com.epoint.core.utils.string.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HttpClient工具类，使用http-client包实现，原先的common-httpclient已经淘汰
 * 
 * @作者 ko
 * @version [版本号, 2017年10月18日]
 */
public class HttpUtil
{
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 7000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        // 在提交请求之前 测试连接是否可用
        connMgr.setValidateAfterInactivity(1);

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        requestConfig = configBuilder.build();
    }

    /**
     * 发送 GET请求
     * 
     * @param apiUrl
     *            API接口URL
     * @return String 响应内容
     */
    public static String doGet(String apiUrl, Map<String, String> headers) {
        return doHttp(apiUrl, null, "get", 2, false, headers);
    }

    /**
     * 发送 GET请求(SSL)
     * 
     * @param apiUrl
     *            API接口URL
     * @return String 响应内容
     */
    public static String doGetSSL(String apiUrl, Map<String, String> headers) {
        return doHttp(apiUrl, null, "get", 2, true, headers);
    }

    /**
     * 发送POST请求
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            K-V参数
     * @return String 响应内容
     */
    public static String doPost(String apiUrl, Map<String, Object> params, Map<String, String> headers) {
        return doHttp(apiUrl, params, "get", 2, false, headers);
    }

    /**
     * 发送POST请求(SSL)
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            K-V参数
     * @return String 响应内容
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params, Map<String, String> headers) {
        return doHttp(apiUrl, params, "post", 2, true, headers);
    }

    /**
     * 发送POST请求
     * 
     * @param apiUrl
     *            API接口URL
     * @param json
     *            json参数
     * @return String 响应内容
     */
    public static String doPostJson(String apiUrl, String json, Map<String, String> headers) {
        return doHttp(apiUrl, json, "post", 2, false, headers);
    }

    /**
     * 发送POST请求(SSL)
     * 
     * @param apiUrl
     *            API接口URL
     * @param json
     *            json参数
     * @return String 响应内容
     */
    public static String doPostJsonSSL(String apiUrl, String json, Map<String, String> headers) {
        return doHttp(apiUrl, json, "post", 2, true, headers);
    }

    /**
     * 发送 http 请求
     * 
     * @param apiUrl
     *            API接口URL
     * @param params
     *            {Map<String, Object> K-V形式、json字符串}
     * @param method
     *            {null、或者post:POST请求、patch:PATCH请求、delete:DELETE请求、get:GET请求}
     * @param type
     *            {1:请求返回stream(此时流需要在外部手动关闭),其余:请求返回字符串}
     * @param ssl
     * @return {InputStream或者String}
     */
    @SuppressWarnings("unchecked")
    public static <T> T doHttp(String apiUrl, Object params, String method, int type, boolean ssl,
            Map<String, String> headers) {
        CloseableHttpClient httpClient = null;
        if (ssl) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
                    .setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        }
        else {
            httpClient = HttpClients.createDefault();
        }

        HttpRequestBase httpPost = null;
        if (StringUtil.isNotBlank(method)) {
            if ("patch".equalsIgnoreCase(method)) {
                httpPost = new HttpPatch(apiUrl);
            }
            else if ("delete".equalsIgnoreCase(method)) {
                httpPost = new HttpDelete(apiUrl);
            }
            else if ("get".equalsIgnoreCase(method)) {
                httpPost = new HttpGet(apiUrl);
            }
            else if ("post".equalsIgnoreCase(method)) {
                httpPost = new HttpPost(apiUrl);
            }
        }
        else {
            httpPost = new HttpPost(apiUrl);
        }
        CloseableHttpResponse response = null;

        try {
            if (ssl) {
                httpPost.setConfig(requestConfig);
            }
            // 参数不为null、要处理参数
            if (params != null) {
                // get请求拼接在url后面
                if (httpPost instanceof HttpGet) {
                    StringBuffer param = new StringBuffer();
                    if (params instanceof Map) {
                        Map<String, Object> paramsConvert = (Map<String, Object>) params;
                        int i = 0;
                        for (String key : paramsConvert.keySet()) {
                            if (i == 0)
                                param.append("?");
                            else
                                param.append("&");
                            param.append(key).append("=").append(paramsConvert.get(key));
                            i++;
                        }
                    }
                    else {
                        param.append("?" + params.toString());
                    }
                }
                // delete请求暂不处理
                else if (!(httpPost instanceof HttpDelete)) {
                    // K-V形式
                    if (params instanceof Map) {
                        Map<String, Object> paramsConvert = (Map<String, Object>) params;

                        List<NameValuePair> pairList = new ArrayList<>(paramsConvert.size());
                        for (Map.Entry<String, Object> entry : paramsConvert.entrySet()) {
                            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                            pairList.add(pair);
                        }
                        ((HttpEntityEnclosingRequestBase) httpPost)
                                .setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
                    }
                    // json格式
                    else {
                        StringEntity stringEntity = new StringEntity(params.toString(), "UTF-8");
                        stringEntity.setContentEncoding("UTF-8");
                        stringEntity.setContentType("application/json");
                        ((HttpEntityEnclosingRequestBase) httpPost).setEntity(stringEntity);
                    }
                }
            }
            // 拼接header
            if (headers != null) {
                for (String key : headers.keySet()) {
                    System.out.println(key + "===========" + headers.get(key));
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            System.out.println("httpPost:"+httpPost);
            response = httpClient.execute(httpPost);
            // int statusCode = response.getStatusLine().getStatusCode();
            // if (statusCode != HttpStatus.SC_OK) {
            // return null;
            // }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                if (type == 1) {
                    return (T) entity.getContent();
                }
                else {
                    return (T) EntityUtils.toString(entity, "UTF-8");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String doPostJson(String apiUrl, String params, Map<String, String> headers, RequestConfig customConfig) {
        CloseableHttpClient httpClient = null;
        boolean ssl = false;
        if (customConfig == null) {
            customConfig = RequestConfig.custom().setSocketTimeout(3000).setSocketTimeout(3000).setConnectionRequestTimeout(3000).build();
        }
        if (ssl) {
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
                    .setConnectionManager(connMgr).setDefaultRequestConfig(customConfig).build();
        }
        else {
            httpClient = HttpClients.createDefault();
        }
        HttpRequestBase httpPost = null;
        httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(params.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            ((HttpEntityEnclosingRequestBase) httpPost).setEntity(stringEntity);
            // 拼接header
            if (headers != null) {
                for (String key : headers.keySet()) {
                    System.out.println(key + "===========" + headers.get(key));
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "UTF-8");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
            {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier()
            {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        }
        catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

}
