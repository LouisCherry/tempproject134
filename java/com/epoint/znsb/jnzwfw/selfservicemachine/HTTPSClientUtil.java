package com.epoint.znsb.jnzwfw.selfservicemachine;

import com.alibaba.fastjson.JSON;
import com.epoint.core.utils.string.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HTTPSClientUtil { 
    private static final String DEFAULT_CHARSET = "UTF-8"; 
    
    private HTTPSClientUtil(){ 
        
    } 

    public static String doPost( String url, 
            Map<String, Object> paramBody) throws Exception { 
    HttpClient httpClient = HttpClients.createDefault(); 
    //设置请求头数据 
         Map<String, String> paramHeader = new HashMap<>(); 
         //希望接受xml格式的数据 
         paramHeader.put("Content-Type", "application/json"); 
         //设置编码格式 
         paramHeader.put("Accept-Charset", "utf-8"); 
        return doPost(httpClient, url, paramHeader, paramBody, DEFAULT_CHARSET); 
    }


    public static String doFPost( String apiUrl,
                                 Map<String, Object> paramBody) throws Exception {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            // conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (paramBody != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = paramBody.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /** 
     * post方法，默认UTF-8格式 
     *  [一句话功能简述] 
     *  [功能详细描述] 
     *  @param httpClient 
     *  @param url 
     *  @param paramHeader 
     *  @param paramBody 
     *  @return 
     *  @throws Exception    
     * @exception/throws [违例类型] [违例说明] 
     * @see [类、类#方法、类#成员] 
     */ 
    public static String doPost(HttpClient httpClient, String url, Map<String, String> paramHeader, 
            Map<String, Object> paramBody) throws Exception { 
        return doPost(httpClient, url, paramHeader, paramBody, DEFAULT_CHARSET); 
    } 

    /** 
     * 
     *  post方法 
     *  [功能详细描述] 
     *  @param httpClient 
     *  @param url 接口地址 
     *  @param paramHeader 请求头参数 
     *  @param paramBody 请求体参数 
     *  @param charset 编码格式 
     *  @return 
     *  @throws Exception    
     * @exception/throws [违例类型] [违例说明] 
     * @see [类、类#方法、类#成员] 
     */ 
    public static String doPost(HttpClient httpClient, String url, Map<String, String> paramHeader, 
            Map<String, Object> paramBody, String charset) throws Exception { 

        String result = null; 
        HttpPost httpPost = new HttpPost(url); 
        setHeader(httpPost, paramHeader); 
        if (StringUtil.isNotBlank(paramHeader.get("Content-Type"))&&paramHeader.get("Content-Type").startsWith("application/x-www-form-urlencoded")) {
            seturlencodedBody(httpPost, paramBody, charset); 
        }else{
            setBody(httpPost, paramBody, charset); 
        }
        

        HttpResponse response = httpClient.execute(httpPost); 
        if (response != null) { 
            HttpEntity resEntity = response.getEntity(); 
            if (resEntity != null) { 
                result = EntityUtils.toString(resEntity, charset); 
            } 
        } 

        return result; 
    } 
    
    public static String doGet(HttpClient httpClient, String url, Map<String, String> paramHeader) throws Exception { 
        return doGet(httpClient, url, paramHeader, DEFAULT_CHARSET); 
    } 

    public static String doGet(HttpClient httpClient, String url, Map<String, String> paramHeader, String charset) throws Exception { 

        String result = null; 
        HttpGet httpGet = new HttpGet(url); 
        setHeader(httpGet, paramHeader); 

        HttpResponse response = httpClient.execute(httpGet); 
        if (response != null) { 
            HttpEntity resEntity = response.getEntity(); 
            if (resEntity != null) { 
                result = EntityUtils.toString(resEntity, charset); 
            } 
        } 

        return result; 
    } 

    /** 
     * 设置头参数 
     *  [一句话功能简述] 
     *  [功能详细描述] 
     *  @param request 
     *  @param paramHeader    
     * @exception/throws [违例类型] [违例说明] 
     * @see [类、类#方法、类#成员] 
     */ 
    private static void setHeader(HttpRequestBase request, Map<String, String> paramHeader) { 
        // 设置Header 
        if (paramHeader != null) { 
            Set<String> keySet = paramHeader.keySet(); 
            for (String key : keySet) { 
                request.addHeader(key, paramHeader.get(key)); 
            } 
        } 
    } 

    /** 
     * 设置参数；json格式 
     *  [一句话功能简述] 
     *  [功能详细描述] 
     *  @param httpPost 
     *  @param paramBody 
     *  @param charset 
     *  @throws UnsupportedEncodingException    
     * @exception/throws [违例类型] [违例说明] 
     * @see [类、类#方法、类#成员] 
     */ 
    private static void setBody(HttpPost httpPost, Map<String, Object> paramBody, String charset) throws UnsupportedEncodingException   { 
        // 设置参数 
        if (paramBody != null) { 
            StringEntity entity = new StringEntity(JSON.toJSONString(paramBody)); 
//            StringEntity entity = new StringEntity("grant_type=client_credentials&client_id=41820bd3401342ef8c48ffa7e7120408&client_secret=412b9be1e46a4587ab2ab35cbf6527a4&scope=default"); 
            httpPost.setEntity(entity); 
        } 
    }     

    /** 
     * 设置参数；x-www-form-urlencoded格式 
     *  [一句话功能简述] 
     *  [功能详细描述] 
     *  @param httpPost 
     *  @param paramBody 
     *  @param charset 
     *  @throws UnsupportedEncodingException    
     * @exception/throws [违例类型] [违例说明] 
     * @see [类、类#方法、类#成员] 
     */ 
    private static void seturlencodedBody(HttpPost httpPost, Map<String, Object> paramBody, String charset) throws UnsupportedEncodingException   { 
        // 设置参数 
        if (paramBody != null) { 
            String sendbody="";
            for (Map.Entry<String, Object> entry : paramBody.entrySet()) {
                sendbody+=entry.getKey()+"="+entry.getValue()+"&";               
              }
            sendbody=sendbody.substring(0,sendbody.length()-1);
            StringEntity entity = new StringEntity(sendbody); 
            httpPost.setEntity(entity); 
        } 
    }    
}


