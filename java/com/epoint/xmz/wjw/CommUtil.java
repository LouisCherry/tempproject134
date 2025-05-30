package com.epoint.xmz.wjw;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 
 * 
 */
@Slf4j
public class CommUtil {
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    private static final String KEY_ALGORITHM = "AES";
    
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64.getEncoder().encodeToString(result);//通过Base64转码返回
        } catch (Exception ex) {
        	log.error("aes加密错误,{}", ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content  待解密内容
     * @param password 加密密码
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content.toString().replace("\r\n", "")));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            log.error("aes解密错误,{}", ex);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(password.getBytes());
            //AES 要求密钥长度为 128
            //  kg.init(128, new SecureRandom(password.getBytes()));
            kg.init(128, secureRandom);
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            log.error("生成加密密钥错误,{}", ex);
        }

        return null;
    }

    /**
     * @Auther: yaohongan
     * @Description //上传文件
     * @Date： 2019/12/17 8:54
     */
    public static Map<String, String> sendMultipartFilePost( String url, InputStream inputStream,
                                                             Map<String, Object> params, String fileName,int timeout) {
        Map<String, String> resultMap = new HashMap<String, String>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String result = "";
        try {
        	  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
              byte[] buff = new byte[100];
              int rc = 0;
              while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                  byteArrayOutputStream.write(buff, 0, rc);
              }
    	  
            HttpPost httpPost = new HttpPost(url);
            trustAllHttpsCertificates();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(java.nio.charset.Charset.forName("UTF-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addBinaryBody("file", byteArrayOutputStream.toByteArray(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            //解决中文乱码
//            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if(entry.getValue() == null) {
                    continue;
                }
                // 类似浏览器表单提交，对应input的name和value
                builder.addTextBody(entry.getKey(), entry.getValue().toString());
            }
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);// 执行提交
 
            // 设置连接超时时间
//            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
//                    .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
//            httpPost.setConfig(requestConfig);
 
            HttpEntity responseEntity = response.getEntity();
            resultMap.put("scode", String.valueOf(response.getStatusLine().getStatusCode()));
            resultMap.put("data", "");
            if (responseEntity != null) {
                // 将响应内容转换为字符串
 
                result = EntityUtils.toString(responseEntity, java.nio.charset.Charset.forName("UTF-8"));
                resultMap.put("data", result);
                ((CloseableHttpResponse) response).close();
            }
            httpPost.releaseConnection();
            httpClient.close();
        } catch (Exception e) {
            resultMap.put("scode", "error");
            resultMap.put("data", "HTTP请求出现异常: " + e.getMessage());
            Writer w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
        }
 
        return resultMap;
    }
    
    public static String uploadAttach(String sendurl,InputStream inputStream,String filename) {
    	try {
    		File file = new File("D://ds.doc");
    		
    		RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
    		
    	// TODO Auto-generated method stub
    	        String BOUNDARY = "WebKitFormBoundary7MA4YWxkTrZu0gW";
    	        URL url = new URL(sendurl);
    	        trustAllHttpsCertificates();
    	        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
    	        httpConn.setConnectTimeout(30000); // 设置发起连接的等待时间，3s
    	        httpConn.setReadTimeout(30000); // 设置数据读取超时的时间，30s
    	        httpConn.setRequestMethod("POST");
    	        
    	        
    	        httpConn.setRequestProperty("Connection", "Keep-Alive");
    	        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
    	        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
    	        OutputStream os = httpConn.getOutputStream();
    	        BufferedOutputStream bos = new BufferedOutputStream(os);
    	        
    	        String content = "--" + BOUNDARY + "\r\n";
    	        content       += "Content-Disposition: form-data; name=\"file\"; filename=\""+filename+"\"\r\n";
    	        bos.write(content.getBytes());
    	        
    	        // 开始写出文件的二进制数据
    	        BufferedInputStream bfi = new BufferedInputStream(inputStream);
    	        byte[] buffer = new byte[4096];
    	        int bytes = bfi.read(buffer, 0, buffer.length);
    	        while (bytes != -1) {
    	            bos.write(buffer, 0, bytes);
    	            bytes = bfi.read(buffer, 0, buffer.length);
    	        }
    	        bfi.close();
    	        bos.write(("\r\n--" + BOUNDARY).getBytes());
    	        bos.flush();
    	        bos.close();
    	        os.close();
    	        
    	         // 读取返回数据  
    	        StringBuffer strBuf = new StringBuffer();
    	        BufferedReader reader = new BufferedReader(new InputStreamReader(
    	                httpConn.getInputStream()));
    	        String line = null;
    	        while ((line = reader.readLine()) != null) {
    	            strBuf.append(line).append("\n");
    	        }
    	        String res = strBuf.toString();
    	        reader.close();
    	        httpConn.disconnect();
    	        return res;
    
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
    }
    
    /**
     * 设置 https 请求
     * @throws Exception
     */
    @SuppressWarnings("unused")
	private static void trustAllHttpsCertificates() throws Exception {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String str, SSLSession session) {
                return true;
            }
        });
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }
    
    //设置 https 请求证书
    static class miTM implements javax.net.ssl.TrustManager,javax.net.ssl.X509TrustManager {

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
	
    /**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param uploadFile
	 *            需要上传的文件名
	 * @param fileFormName
	 *            需要上传文件表单中的名字
	 * @param newFileName
	 *            上传的文件名称，不填写将为uploadFile的名称
	 * @param urlStr
	 *            上传的服务器的路径
     * @throws Exception 
	 */
	public static String uploadForm(String fileFormName,
			InputStream inputStream, String newFileName, String urlStr,String size)
			throws Exception {
 
		StringBuilder sb = new StringBuilder();
		/**
		 * 普通的表单数据
		 */
		/*for (String key : params.keySet()) {
			sb.append("--" + BOUNDARY + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + key + "\""
					+ "\r\n");
			sb.append("\r\n");
			sb.append(params.get(key) + "\r\n");
		}*/
		/**
		 * 上传文件的头
		 */
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName
				+ "\"; filename=\"" + newFileName + "\"" + "\r\n");
		sb.append("\r\n");
 
		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		System.out.println(sb.toString());
		URL url = new URL(urlStr);
		trustAllHttpsCertificates();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty("Content-Length", String
				.valueOf(headerInfo.length + size
						+ endInfo.length));
		conn.setDoOutput(true);
 
		OutputStream out = conn.getOutputStream();
		InputStream in = inputStream;
		out.write(headerInfo);
 
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
 
		out.write(endInfo);
		in.close();
		out.close();
		
		if (conn.getResponseCode() == 200) {
			System.out.println("上传成功");
		}
		
		   // 读取返回数据  
        StringBuffer strBuf = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
        		conn.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            strBuf.append(line).append("\n");
        }
        String res = strBuf.toString();
        reader.close();
        
		return res;
 
	}
	
}
