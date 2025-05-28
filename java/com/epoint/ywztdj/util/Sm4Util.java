package com.epoint.ywztdj.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

/**
 * SM4加密工具类
 *
 */
//@Slf4j
public class Sm4Util {

    /**
     * 算法名称
     */
    public static final String ALGORITHM_NAME = "SM4";

    /**
     * CBC P7填充
     */
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS7Padding";

    /**
     * 用于新秘钥生成
     */
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    /**
     * SM4加密
     * CBC P5填充解密
     *
     * @param sm4Key 密钥
     * @param data   将要加密数据
     * @return 加密结果
     */
    public static String encryptCbcPadding(String sm4Key, String data) {
        try {
            //密钥
            byte[] appSecretEncData = sm4Encrypt(padRight(sm4Key, 16, "0").getBytes(StandardCharsets.UTF_8), sm4Key.getBytes(StandardCharsets.UTF_8));
            //新秘钥串（将byte转换为16进制）
            byte[] key = Hex.toHexString(appSecretEncData).toUpperCase().substring(0, 16).getBytes(StandardCharsets.UTF_8);
            //偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，所以需要设置偏移量，一般用密钥做偏移量
            byte[] iv = padRight(sm4Key, 16, "0").getBytes(StandardCharsets.UTF_8);
            Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE,
                    key, iv);
            byte[] encryptBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptBytes);
        } catch (Exception e) {
//			log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * SM4解密
     * CBC P5填充解密
     *
     * @param sm4Key     密钥
     * @param cipherText 加密数据
     * @return 解密结果
     */
    public static String decryptCbcPadding(String sm4Key, String cipherText) {
        try {
            //密钥
            byte[] appSecretEncData = sm4Encrypt(padRight(sm4Key, 16, "0").getBytes(StandardCharsets.UTF_8), sm4Key.getBytes(StandardCharsets.UTF_8));
            //新秘钥串
            byte[] key = Hex.toHexString(appSecretEncData).toUpperCase().substring(0, 16).getBytes(StandardCharsets.UTF_8);
            //偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，所以需要设置偏移量，一般用密钥做偏移量
            byte[] iv = padRight(sm4Key, 16, "0").getBytes(StandardCharsets.UTF_8);
            Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE,
                    key, iv);
            byte[] cipherTextByte = Base64.getDecoder().decode(cipherText);
            byte[] decryptBytes = cipher.doFinal(cipherTextByte);
            return new String(decryptBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * SM4加密(用于生成新的秘钥)
     *
     */
    public static byte[] sm4Encrypt(byte[] appIdBytes, byte[] keyBytes) {
        if (appIdBytes.length != 16) {
            throw new RuntimeException("err key length");
        }
        try {
            Key key = new SecretKeySpec(appIdBytes, "SM4");
            Cipher out = Cipher.getInstance(ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
            out.init(Cipher.ENCRYPT_MODE, key);
            return out.doFinal(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String padRight(String l, int size, String pad) {
        int al = l.length();
        int a = size - al;
        StringBuilder sb = new StringBuilder();
        if (a < 0) {
            return l.substring(0, size);
        } else {
            for (int i = 0; i < a; i++) {
                sb.append(pad);
            }
        }
        return l + sb.toString();
    }

    /**
     * CBC P5填充加解密Cipher初始化
     *
     * @param algorithmName 算法名称
     * @param mode          1 加密 2解密
     * @param key           密钥
     * @param iv            偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，
     *                      所以需要设置偏移量，一般用密钥做偏移量
     * @return Cipher
     */
    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(mode, sm4Key, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
        }
        return null;
    }
}

