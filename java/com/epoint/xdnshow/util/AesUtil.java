package com.epoint.xdnshow.util;


import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @Description :  AES加密
 *
 */
public class AesUtil {

    private static String ivParameter = "0392039203920300";


    // 加密
    public static byte[] encrypt(String sSrc, String sKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return encrypted;// 此处使用BASE64做转码。
    }

    // 解密
    public static byte[] decrypt(byte[] content,String sKey) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(content);
            return original;
        }
        catch (RuntimeException ex) {
            return null;
        }
    }
    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }


    public static void main(String[] args) {
        try {
            String  skey = "D6d4C30f208e40ad";  //服务方提供的秘钥

            /**
             * 加密示例 -- 对token对应的值进行AES加密
             */
            JSONObject params = new JSONObject();
            params.put("flag", "1");
            //system.out.println("加密前的明文："+params.toString());
            String token = AesUtil.parseByte2HexStr(AesUtil.encrypt(params.toJSONString(), skey));
            //system.out.println("加密后的密文："+token);

            /**
             * 解密示例
             */
            String str = new String(AesUtil.decrypt(AesUtil.parseHexStr2Byte(token), skey), "utf-8");
            //system.out.println("解密得到的明文："+str);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
