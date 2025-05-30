package com.epoint.xmz.job.util;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/****
 * Token的生成和验证
 * 
 * @author Administrator
 * 
 */
public class TokenUtil
{

    /**
     * 调用账号
     */
    private static final Map<String, String> map = new HashMap<String, String>();

    /**
     * 超时时间
     */
    private static int timeOut = 60;

    /**
     * 初始化参数
     * 
     */
    static {
        String appKey = getConfigValue("AppKey");
        String appSecret = getConfigValue("AppSecret");
        if (isNotBlank(appKey)) {
            String[] keys = appKey.split(";");
            String[] values = appSecret.split(";");
            int i = 0;
            for (String key : keys) {
                map.put(key, values[i]);
                i++;
            }
        }
        String timeout = getConfigValue("AppTimeOut");
        if (isNotBlank(timeout)) {
            timeOut = Integer.parseInt(timeout);
        }
    }

    /***
     * Token生成
     * 
     * @param AppKey
     *            生成token的账号
     * @return 生成的token
     * @throws Exception
     */
    public static String createToken(String AppKey) throws Exception {
        String result = null;
        String appSecret = map.get(AppKey);
        if (isNotBlank(appSecret)) {
            // 在调用该方法之后的5分钟内，生成的token有效
            Integer num = (int) ((new Date().getTime() / 1000) + timeOut);
            String P1 = num.toString();
            // UrlEncode，并Base64编码
            String str = URLEncoder.encode(P1, "UTF-8");
            String P2 = encode(str);
            // 对P2加密
            byte[] b = hmacSha1(P2, appSecret);
            String P4 = encode(b).replace("+", "-").replace("/", "_");
            result = AppKey + "@" + P4 + "@" + P2;
        }
        return result;
    }

    /***
     * 验证token
     * 
     * @param token
     *            token值
     * @return 是否验证成功
     * @throws Exception
     */
    public static Boolean validateToken(String token) {
        boolean result = false;
        if (isNotBlank(token)) {
            String[] p = null;
            if (token.indexOf("@") > 0) {
                // 将token拆分成3部分p1,p2,p3
                p = token.split("@");
            }
            String AppKey = p[0];
            String p2 = p[1];
            String p3 = p[2];
            if (map.containsKey(AppKey)) {
                // 通过key获取secret
                String AppSecret = map.get(AppKey);
                // 将p3进行加密并Base64编码
                byte[] b = hmacSha1(p3, AppSecret);
                String P4 = encode(b).replace("+", "-").replace("/", "_");
                if (P4.equals(p2)) {
                    // 验证成功，接着下一步验证,将P3Base64反编译得到时间戳
                    Integer time = Integer.parseInt(decode(p3));
                    Integer now = (int) (new Date().getTime() / 1000);
                    if (time >= now) {
                        // token未过期，验证成功
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 供外部来操作账号容器
     * 
     * @return Map<String, String>：账号密码键值对
     */
    public static Map<String, String> getMap() {
        return map;
    }

    /**
    * 生成签名数据
    * 
    * @param data
    *            待加密的数据
    * @param key
    *            加密使用的key
    * @return hmacsha1加密的字符串
    */
    public static byte[] hmacSha1(String data, String key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return mac.doFinal(data.getBytes("UTF-8"));
        }
        catch (Exception e) {
            return new byte[0];
        }
    }

    /**
    * 将 s 进行 BASE64 编码
    * 
    * @param s
    *            要编码的字符串
    * @return base64编码后的字符串
    */
    public static String encode(String s) {
        String result = null;
        if (isNotBlank(s)) {
            result = encode(s.getBytes());
        }
        return result;
    }

    /**
    * 将 s 进行 BASE64 编码
    * 
    * @param s
    *            要编码的byte[]
    * @return base64编码后的字符串
    */
    public static String encode(byte[] s) {
        String result = null;
        if (s != null) {
            result = (new BASE64Encoder()).encode(s);
        }
        return result;
    }

    /**
    * 将 BASE64 编码的字符串 s 进行解码
    * 
    * @param s
    *            base64编码的字符串
    * @return 解码后的字符串
    */
    public static byte[] decodeBuffer(String s) {
        byte[] result = null;
        if (isNotBlank(s)) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                result = decoder.decodeBuffer(s);
            }
            catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    /**
     * 将 BASE64 编码的字符串 s 进行解码
     * 
     * @param s
     *            base64编码的字符串
     * @return 解码后的字符串
     */
    public static String decode(String s) {
        String result = null;
        byte[] b = decodeBuffer(s);
        if (b != null) {
            result = new String(b);
        }
        return result;
    }

    /**
    * 判断字符串是否是空的(判断依据:字符串为null,或者截取空格后长度为0)
    * 
    * @param string
    *            要判断的字符串
    * @return 检查结果 true:空;false:不空
    */
    public static boolean isBlank(final String string) {
        return string == null || string.trim().length() == 0 || string.equalsIgnoreCase("null");
    }

    /**
     * 判断字符串是否非空(判断依据:字符串不为null,并且截取空格后长度不为0)
     * 
     * @param string
     *            要判断的字符串
     * @return 检查结果 true:不空;false:空
     */
    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }

    /**
    * 获取epointframe.properties中的配置参数
    * 
    * @param configname
    *            配置项名称
    * @return String 配置项值
    * 
    */
    public static String getConfigValue(String configname) {
        String file = "epointframe";
        java.util.ResourceBundle resBundle = null;
        String result = "";
        try {
            resBundle = java.util.ResourceBundle.getBundle(file);
            result = resBundle.getString(configname);
            result = new String(result.getBytes("ISO-8859-1"), "utf-8");
            return result;
        }
        catch (Exception e) {
            return null;
        }
    }
}
