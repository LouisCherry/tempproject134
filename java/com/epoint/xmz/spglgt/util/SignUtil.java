package com.epoint.xmz.spglgt.util;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class SignUtil
{

    // 定义加密方式
    public static final String KEY_RSA = "RSA";
    // RSA最大加密大小
    private final static int MAX_ENCRYPT_BLOCK = 117;

    /**
         * 私钥加密
         *
         * @param toBeEncryptedStr
         * @param privateKeyStr
         * @return
         */
    public static String sign(String toBeEncryptedStr, String privateKeyStr) {
        try {
            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            // 获得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // 取得待加密数据
            byte[] data = toBeEncryptedStr.getBytes("UTF-8");
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            // 返回加密后由Base64编码的加密信息
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                }
                else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return encryptBase64(decryptedData);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
         * BASE64 解码
         *
         * @param key 需要Base64解码的字符串
         * @return 字节数组
         */
    public static byte[] decryptBase64(String key) {
        return Base64.getDecoder().decode(key);
    }

    /**
     * BASE64 编码
     *
     * @param key 需要Base64编码的字节数组
     * @return 字符串
     */
    public static String encryptBase64(byte[] key) {
        return new String(Base64.getEncoder().encode(key));
    }

    public static void main(String[] args) {
        String res = sign("liujingwei66",
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMBnCYiKm32X4A8Bu9di3RYCSY/EgD30DBqJ2X6QSDhdaVWXn6SPLynKEoGb5jgBfc7ZUZ3chZzIBNL40wrQCORGZ7TZCrMqvldTO+6sFguYEIs3Q9QTlYmEmEi7MXGcs9WpgyaArsoRcFD+lvN3CU+nptM9KOwuLc5oSFZYXaRPAgMBAAECgYEAt1e4gu+qOiUmssYxLI8eJKHrXV+f4OmVOxjPM8bjuypwfMquqw5yn/zaY72exRHOw/TczHReskb8KdpIqU4SmWQyuGB/yZAyKNxTRziXHiDcNBkXQnpQ/zIcWauBrkDdgLH4GQEVOYpLOKK4eMyjlOLRMOB/DI3bTu5YguFvmCECQQDzAF6jpf29MrAHfAKzxl8ipQ1ag3kr8wT2gRLdJxnsDyYW5vRKHVTKUw99YQEGtKQx006LWwultoO5MbwZ6yufAkEAyrHFovgDgXXsX7VMZ43moGk8vKUWwdNqS4eM3qEWh+Cr4rSdgmPyMv8OqIeqtMk6j+rcSrvfhV0KjqhYVCvJUQJACdoM/DOBmwcQDe2gKmuzdbMdXHsSEY8Tt266Ng8cxO5ETA9m/g48XsuJSDsDkKMIOSDpAUtaqIhCxOJYTvCy3wJAR/EzAbXfoKqXPB0gQj0GV3civJ4n5qHAP5Cb+kTYt1+SAoQyx53r0BfhPwXwG/y0UXJ9v/TQ0AgHAD1WJIHqEQJBAM7EVb40Bz3xKjOHH/rxGr6nquc90EcihWzoBJdWr/48HuJsNhgyGO4aGhhktzuWk5R1JlzKNumIaetxuc/x8uQ=");
        //system.out.println(res);

    }

}
