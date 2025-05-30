package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import com.alibaba.fastjson.JSONObject;

public class RSASignature
{
    public static final String headpara = "centerId,userId,userIdType,deviceType,deviceToken,currenVersion,buzType,channel,appid,appkey,appToken,clientIp,money,original,encryption,transVersion,ybmapMessage";

    public static final String RSA_PRIVATE = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAzVGENKWOHFos/ypcI9lDz+o8QPjyEfPgKMGAJdQErt3rcHQvqUhm6T4dPLJUMbiF7b9trLVG8TEKwaVTlqujYwIDAQABAkEAyBYnq5KnMjQi46vpTTo/HpCblYnFhf9PNQiVlMfQpIN6zt22QZX4EKtYQhbo5Q78jpbolXi/fDQP8Ozb7t1VwQIhAO/dGpyyhXIBu9bTIXl0AgXd0lHtelInuNLLi9PY/auDAiEA2yF6q50ro7yywpWtRQKRhmKSdI7bP94Bjzb2lDh5QqECICSS+UDVc8WbgBHUpbEIQFq2pSA67sDiL6tswAhweNWTAiArMxZz7rBDv1eedNOL303BKH2m7OLcXHACQ9uorNl7AQIhAJV4PErHWZtqdDafYMrnp7ixNLBN2QUvksMNzoAQiQn/";

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey) {
        try {
            String charset = "utf-8";
            if (privateKey == null || privateKey.length() == 0) {
                privateKey = RSA_PRIVATE;
            }
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(charset));

            byte[] signed = signature.sign();
            //System.out.println(signed.length);
            return Base64.encode(signed);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Post
    public static JSONObject httpURLConnectionPOST(String httpurl, String params) {
        try {

            URL url = new URL(httpurl);
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)  
            // 此时cnnection只是为一个连接对象,待连接中  
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)  
            connection.setDoOutput(true);
            // 设置连接输入流为true  
            connection.setDoInput(true);
            // 设置请求方式为post  
            connection.setRequestMethod("POST");
            // post请求缓存设为false  
            connection.setUseCaches(false);
            connection.setReadTimeout(100000);
            // 设置该HttpURLConnection实例是否自动执行重定向  
            connection.setInstanceFollowRedirects(true);
            connection.addRequestProperty("headpara", headpara);

            connection.addRequestProperty("headparaMD5",
                    RSASignature.sign(EncryptionByMD5.getMD5(params.getBytes()), RSASignature.RSA_PRIVATE));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)  
            connection.connect();

            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)  
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            // 将参数输出到连接  
            //dataout.writeBytes(URLEncoder.encode(params, "utf8"));
           // params = params.replaceAll("[+]","%2B");
            dataout.writeBytes(params);
            // 输出完成后刷新并关闭流  
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)   

            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)  
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据  

            // 循环读取流,若不到结尾处  
            while ((line = bf.readLine()) != null) {
                //sb.append(bf.readLine());  
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close(); // 重要且易忽略步骤 (关闭流,切记!)   
            connection.disconnect(); // 销毁连接  

            //System.out.println(sb.toString());
            return JSONObject.parseObject(sb.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static JSONObject httpURLConnectionPOST(String httpurl, String params,String newheadpara) {
        try {

            URL url = new URL(httpurl);
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            // 此时cnnection只是为一个连接对象,待连接中
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            connection.setReadTimeout(100000);
            connection.addRequestProperty("headpara", newheadpara);

            connection.addRequestProperty("headparaMD5",
                    RSASignature.sign(EncryptionByMD5.getMD5(params.getBytes()), RSASignature.RSA_PRIVATE));
            System.out.println( RSASignature.sign(EncryptionByMD5.getMD5(params.getBytes()), RSASignature.RSA_PRIVATE));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();

            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            // 将参数输出到连接
            //dataout.writeBytes(URLEncoder.encode(params, "utf8"));
            params = params.replaceAll("[+]","%2B");
            System.out.println(params);
            dataout.writeBytes(params);
            // 输出完成后刷新并关闭流
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)

            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据

            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                //sb.append(bf.readLine());
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close(); // 重要且易忽略步骤 (关闭流,切记!)
            connection.disconnect(); // 销毁连接

            //System.out.println(sb.toString());
            return JSONObject.parseObject(sb.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
