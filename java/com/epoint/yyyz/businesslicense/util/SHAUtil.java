package com.epoint.yyyz.businesslicense.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * SHA256加密
 */
public class SHAUtil
{

    /**
     * 生成网关签名
     * @param timeStamp
     * @param nonce
     * @param passToken
     * @return
     */
    public static String generateSign(String timeStamp, String nonce, String passToken) {
        return getSHA256(timeStamp + passToken + nonce + timeStamp);
    }
    public static String generateSign(String appid, String nonce, String passToken,String appkey) {
        return getSHA256(appid + passToken + nonce + appkey);
    }
    /**
     * 利用java原生的类实现SHA256加密
     * @ param str 加密后的报文
     * @ return
     */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * 将byte转为16进制
     * @ param bytes
     * @ return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 获取当前时间戳字符串
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取随机字符串
     * @return
     */
    public static String getNonceStr() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}
