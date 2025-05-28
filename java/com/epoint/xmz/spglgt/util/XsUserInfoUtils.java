package com.epoint.xmz.spglgt.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;

public class XsUserInfoUtils {
    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static String generateKeyPair() {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException var8) {
            var8.printStackTrace();
            return null;
        }

        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(org.apache.commons.codec.binary.Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString = new String(org.apache.commons.codec.binary.Base64.encodeBase64(privateKey.getEncoded()));
        return publicKeyString + ";" + privateKeyString;
    }

    public static String getFileChecksumMD5(File file) {
        String md5 = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fileInputStream));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return md5.toUpperCase();
    }

    //定义加密方式
    public static final String KEY_RSA = "RSA";
    //RSA最大加密大小
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
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return encryptBase64(decryptedData);
        } catch (Exception e) {
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
}
