package com.epoint.yyyz.businesslicense.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SafetyUtil
{

    public static String doHttpPost(String url, String appKey) {
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            // 与http一致
            URL serverUrl = new URL(url);
            HttpURLConnection http = (HttpURLConnection) serverUrl.openConnection();
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");// 不能为驼峰格式
            http.setRequestProperty("Charset", "utf-8");
            String xaeptimeStamp = SHAUtil.getTimeStamp();
            String xaepnonce = SHAUtil.getNonceStr();
            http.setRequestProperty("x-aep-timestamp", xaeptimeStamp);
            http.setRequestProperty("x-aep-nonce", xaepnonce);
            http.setRequestProperty("x-aep-appkey", appKey);
            http.setRequestProperty("x-aep-signature", SHAUtil.generateSign(xaeptimeStamp, xaepnonce, appKey));

            http.setConnectTimeout(30 * 1000);
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setUseCaches(false);
            // 建立连接
            http.connect();
            OutputStream ops = http.getOutputStream();
            ops.flush(); //刷空输出流，并输出所有被缓存的字节
            int code = http.getResponseCode(); // 获取所有响应头字段
            if (code != 200) {
                return "HTTP POST访问接口失败  " + code;
            }
            in = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8")); // 读取响应
            String line = null;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            http.disconnect();
        }
        catch (Exception e) {
            //system.out.println(e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
