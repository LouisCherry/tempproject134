package com.epoint.common.zwdt.login.dhrsautil;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSONObject;

/**
 * AES 加密方法，是对称的密码算法(加密与解密的密钥一致)，这里使用最大的 256 位的密钥
 */
public class AESUtil
{

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    /**
     * 获得一个 密钥长度为 256 位的 AES 密钥，
     * @return 返回经 BASE64 处理之后的密钥字符串
     */
    public static String getStrKeyAES() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes("utf-8"));
        // 这里可以是 128、192、256、越大越安全
        keyGen.init(256, secureRandom);
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     *  将使用 Base64 加密后的字符串类型的 secretKey 转为 SecretKey
     * @param strKey
     * @return SecretKey
     */
    public static SecretKey strKey2SecretKey(String strKey) {
        byte[] bytes = Base64.getDecoder().decode(strKey);
        SecretKeySpec secretKey = new SecretKeySpec(bytes, "AES");
        return secretKey;
    }

    /**
     * 加密
     * @param content 待加密内容
     * @param secretKey 加密使用的 AES 密钥
     * @return 加密后的密文 byte[]
     */
    public static byte[] encryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    /**
     * 解密
     * @param content 待解密内容
     * @param secretKey 解密使用的 AES 密钥
     * @return 解密后的明文 byte[]
     */
    public static byte[] decryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    /**
     * 字节数组转Base64编码
     * @param bytes
     * @return
     */
    public static String byte2Base64(byte[] bytes) {
        return BASE64_ENCODER.encodeToString(bytes);
    }

    /**
     * Base64编码转字节数组
     * @param base64Key
     * @return
     */
    public static byte[] base642Byte(String base64Key) {
        return BASE64_DECODER.decode(base64Key);
    }

    /*********************************以下方法为大汉统一身份认证提供******************************************/

    /**
     * 密钥如超过16位，截至16位，不足16位，补/000至16位
     * 
     * @param key原密钥
     * @return 新密钥
     */
    public static String secureBytes(String key) {
        if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        else if (key.length() < 16) {
            for (int i = (key.length() - 1); i < 15; i++) {
                key += "\000";
            }
        }
        return key;
    }

    /**
     * AES解密 用于数据库储存
     * 
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decryptCode(String sSrc, String key) {

        String sKey = secureBytes(key);

        try {
            // 判断Key是否正确
            if (sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey);
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "GBK");
                return originalString;
            }
            catch (Exception e) {
                return null;
            }
        }
        catch (Exception ex) {
            return null;
        }

    }

    /**
     * AES解密 用于数据库储存
     * 
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String key) {

        String sKey = secureBytes(key);

        try {
            // 判断Key是否正确
            if (sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey);
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);

                String originalString = new String(original, "utf-8");
                // String originalString = new String(original, "GBK");

                return originalString;
            }
            catch (Exception e) {
                return null;
            }
        }
        catch (Exception ex) {
            return null;
        }

    }

    public static String encrypt4Contacts(String sSrc) {
        return sSrc;
    }

    /**
     * AES加密
     * 
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String key) {

        String sKey = secureBytes(key);
        try {
            if (sSrc == null || sKey == null) {
                // LogUtil.d("AesUtil", "Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                // LogUtil.d("AesUtil", "Key长度不是16位");
                sKey = secureBytes(sKey);
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return byte2hex(encrypted).toLowerCase();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param strhex
     * @return
     */
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 != 0) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            }
            else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

}
