package com.epoint.xmz.shebao;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.epoint.core.utils.classpath.ClassPathUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

public class SbUtil {
	
	public static void main(String[] args) {
    	
		String res = doJnryPost("");
		//system.out.println(res);
    }

	
	public static String doSbPost (String biz_content) {
		String result ="";
		//业务入参（不同服务，入参字段和格式不一样，参见各个服务的请求示例）
        biz_content = "{\"zjhm\":\"370811199103220055\",\"ksxm\":\"孙超\"}";
		//接入系统标识，由人社部分配
        String access_key = "SDS_GGZYJYZX";
		//返回参数格式 xml/json
        String format = "json";
		//版本号，以各个服务请求示例为准
        String version = "1.0";
		//流水号
        String request_id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		//请求时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
		//mock服务
        String mock = "";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("biz_content", biz_content);
        paramMap.put("access_key", access_key); 
        paramMap.put("format", format);
        paramMap.put("request_id", request_id);
        paramMap.put("timestamp", timestamp);
        paramMap.put("version", version);
        paramMap.put("mock", mock);
        String source = generateSignSource(paramMap);
		//签名串（由人社部提供私钥）
        String sign;
        OutputStreamWriter out = null;
        BufferedReader in = null;
        try {
        	
			sign = sign(source, "D://privateKey.txt");
			//原生http调用签名串中的+需要转义为%2B (如果使用httpclient、 okhttp等组件不需要)
	        sign = sign.replaceAll("[+]", "%2B");
			//服务地址（不同服务请求地址不一样），参见具体服务的请求示例
	        String address = "http://172.20.58.176/gateway/api/1/jnsdsjzx_rsb_zyjszgzsxx";
			//参数
	        String paramStr = source + "&sign=" + sign;
	        URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("apiKey", "888106319838445568");
			//post传参
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(paramStr);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder resultBuilder = new StringBuilder();
            while ((line = in.readLine()) != null) {
                resultBuilder.append(line);
            } 
            //结果输出
            result = resultBuilder.toString();
		}  catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
        return result;
	}
	
	
	public static String generateSignSource(Map params) {
        Set<String> keySet = params.keySet();
        List<String> keys = new ArrayList<>();
        for (String key : keySet) {
            if (params.get(key) != null && StringUtils.isNotBlank(params.get(key).toString())) {
                keys.add(key);
            }
        }
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = keys.size(); i < size; i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            builder.append(key);
            builder.append("=");
            builder.append(value); 
            if (i != size - 1) {
                builder.append("&");
            }
        }
        return builder.toString();
    }

    public static String sign(String source, String keyFile) throws Exception {
		//读取解析私钥（解析完成的PrivateKey对象建议缓存起来）
        InputStream in = new FileInputStream(keyFile);
        PrivateKey privateKey = null;
        String sign = null;
        try {
            byte[] keyBytes = IOUtils.toByteArray(in);
            byte[] encodedKey = Base64.getDecoder().decode(keyBytes);
            KeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(keySpec);
        } finally {
            IOUtils.closeQuietly(in);
        }
        if(privateKey != null) {
		//签名
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(source.getBytes());
            byte[] signed = signature.sign();
		//取base64， 得到签名串
            sign = Base64.getEncoder().encodeToString(signed);
        }
        return sign;
    }
    
    public static String doJnryPost(String biz_content ) {
    	String result = "";
    	// XM:姓名 SFZH : 身份证号  ZSBH:证书编号
//        String biz_content = "{\"XM\":\"赵XX\",\"SFZH\":\"3723XXXXXXXXX\",\"ZSBH\":\"\"}";
    	biz_content = "{\"zjhm\":\"370811199103220055\",\"ksxm\":\"孙超\"}";
        //接入系统标识，由人社部分配
        String access_key = "SDS_GGZYJYZX";
        //返回参数格式 xml/json
        String format = "json";
        //版本号，以各个服务请求示例为准
        String version = "1.0";
        //流水号
        String request_id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        //请求时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        //mock服务
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("biz_content", biz_content);
        paramMap.put("access_key", access_key);
        paramMap.put("format", format);
        paramMap.put("request_id", request_id);
        paramMap.put("timestamp", timestamp);
        paramMap.put("version", version);
            //用来生成签名
        String source = generateSignSource(paramMap);
        //system.out.println(source);
        //签名串（由人社部提供私钥)  依据 传入参数  和  私钥生成
        String sign;
		try {
//			String licenseName = ClassPathUtil.getClassesPath() + "privateKey.txt";
			
			sign = sign(source, "D://privateKey.txt");
			//system.out.println(sign);
			 //原生http调用签名串中的+需要转义为%2B (如果使用httpclient、 okhttp等组件不需要)  本用例使用的是 HttpClient
	        //sign = sign.replaceAll("[+]", "%2B");
	        //服务地址
	        String address = "http://172.20.58.176/gateway/api/1/jnsdsjzx_rsb_jnrydjcx";
	        //system.out.println("接口地址: " + address);
	        NameValuePair[] param = {
	                new NameValuePair("biz_content",biz_content),
	                new NameValuePair("access_key",access_key),
	                new NameValuePair("request_id",request_id),
	                new NameValuePair("timestamp",timestamp),
	                new NameValuePair("sign",sign)
	        };
	        //system.out.println(param);
	        try {
	            PostMethod postMethod = new PostMethod(address);
	            postMethod.setRequestHeader("ApiKey","888106319838445568");
	            postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8;");
	            postMethod.setRequestBody(param);
	            HttpClient httpClient = new HttpClient();
	            int i = httpClient.executeMethod(postMethod);
	            result = postMethod.getResponseBodyAsString();
	            //system.out.println("接口返回内容: " +result);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return result;
    }

}
