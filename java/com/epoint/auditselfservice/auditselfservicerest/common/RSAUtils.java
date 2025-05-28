package com.epoint.auditselfservice.auditselfservicerest.common;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * RSA加密和解密工具-普通版
 */
public class RSAUtils
{

    /**
     * 数字签名，密钥算法
     */
    private static final String RSA_KEY_ALGORITHM = "RSA";

    /**
     * 公钥 key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥 key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA密钥长度，RSA算法的默认密钥长度是1024密钥长度必须是64的倍数，在512到65536位之间
     */
    private static final int KEY_SIZE = 1024;

    /**
     * 生成密钥对
     */
    private static Map<String, String> initKey() throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
        SecureRandom secrand = new SecureRandom();

        /**
         * 初始化随机产生器
         */
        secrand.setSeed("initSeed".getBytes());

        /**
         * 初始化密钥生成器
         * 设置密钥对的bit数 越大越安全
         */
        keygen.initialize(KEY_SIZE, secrand);

        KeyPair keys = keygen.genKeyPair();
        // 获取公钥
        byte[] pub_key = keys.getPublic().getEncoded();
        String publicKeyString = Base64.encodeBase64String(pub_key);

        // 获取私钥
        byte[] pri_key = keys.getPrivate().getEncoded();
        String privateKeyString = Base64.encodeBase64String(pri_key);

        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put(PUBLIC_KEY, publicKeyString);
        keyPairMap.put(PRIVATE_KEY, privateKeyString);

        return keyPairMap;
    }

    /**
     * 密钥转成字符串
     *
     * @param key
     * @return
     */
    public static String encodeBase64String(byte[] key) {
        return Base64.encodeBase64String(key);
    }

    /**
     * 密钥转成byte[]
     *
     * @param key
     * @return
     */
    public static byte[] decodeBase64(String key) {
        return Base64.decodeBase64(key);
    }

    /**
     * 公钥加密
     *
     * @param data
     *            加密前的字符串
     * @param publicKey
     *            公钥
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encryptByPubKey(String data, String publicKey) throws Exception {
        byte[] pubKey = RSAUtils.decodeBase64(publicKey);
        byte[] enSign = encryptByPubKey(data.getBytes(), pubKey);
        return Base64.encodeBase64String(enSign);
    }

    /**
     * 公钥加密
     *
     * @param data
     *            待加密数据
     * @param pubKey
     *            公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPubKey(byte[] data, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥加密
     *
     * @param text
     *            加密前的字符串
     * @param privateKey
     *            私钥
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encryptByPriKey(String text, String privateKey) {
        try {
            byte[] priKey = RSAUtils.decodeBase64(privateKey);
            byte[] enSign = encryptByPriKey(text.getBytes(), priKey);
            return Base64.encodeBase64String(enSign);
        }
        catch (Exception e) {
            throw new RuntimeException("加密字符串[" + text + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     *            待加密的数据
     * @param priKey
     *            私钥
     * @return 加密后的数据
     * @throws Exception
     */
    public static byte[] encryptByPriKey(byte[] data, byte[] priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data
     *            待解密的数据
     * @param pubKey
     *            公钥
     * @return 解密后的数据
     * @throws Exception
     */
    public static byte[] decryptByPubKey(byte[] data, byte[] pubKey) throws Exception {
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data
     *            解密前的字符串
     * @param publicKey
     *            公钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptByPubKey(String data, String publicKey) throws Exception {
        byte[] pubKey = RSAUtils.decodeBase64(publicKey);
        byte[] design = decryptByPubKey(Base64.decodeBase64(data), pubKey);
        return new String(design);
    }

    /**
     * 私钥解密
     *
     * @param data
     *            待解密的数据
     * @param priKey
     *            私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPriKey(byte[] data, byte[] priKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param secretText
     *            解密前的字符串
     * @param privateKey
     *            私钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptByPriKey(String secretText, String privateKey) {
        try {
            byte[] priKey = RSAUtils.decodeBase64(privateKey);
            byte[] design = decryptByPriKey(Base64.decodeBase64(secretText), priKey);
            return new String(design);
        }
        catch (Exception e) {
            throw new RuntimeException("解密字符串[" + secretText + "]时遇到异常", e);
        }
    }

    public static void main(String[] args) {
        try {
            Map<String, String> keyMap = initKey();
            // String publicKeyString = keyMap.get(PUBLIC_KEY);
            // String privateKeyString = keyMap.get(PRIVATE_KEY);
            // System.out.println("公钥:" + publicKeyString);
            // System.out.println("length: " + publicKeyString.length());
            // System.out.println("私钥:" + privateKeyString);
            // System.out.println("length: " + privateKeyString.length());
            // 待加密数据
            String data = "320584199805010514";
            // 公钥加密
            String encrypt = RSAUtils.encryptByPubKey(data,
                    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDSUmOXyQmYYSnZacp0btvAZCOvCNPtzixAp7eJmzmAG4mgy/VgrY/s1BDLh9qTNHIRWXepUtwMrf1kYul/A45qE/2oxIbeeq4238YDWQ7ModOVXR9ytEHsT0jpCFvoYfYXYZnnoWRrLIBylQeXzqxbLDxxBxGCs4AjoRKh5S7nNQIDAQAB");
            // 私钥解密
            String decrypt = RSAUtils.decryptByPriKey(encrypt,
                    "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANJSY5fJCZhhKdlpynRu28BkI68I0+3OLECnt4mbOYAbiaDL9WCtj+zUEMuH2pM0chFZd6lS3Ayt/WRi6X8DjmoT/ajEht56rjbfxgNZDsyh05VdH3K0QexPSOkIW+hh9hdhmeehZGssgHKVB5fOrFssPHEHEYKzgCOhEqHlLuc1AgMBAAECgYEAqTB9zWx7u4juEWd45ZEIVgw4aGXBllt0Xc6NZrTn3JZKcH+iNNNqJCm0GQaAXkqiODKwgBWXzttoK4kmLHa/6D7rXouWN8PGYXj7DHUNzyOe3IgmzYanowp/A8gu99mJQJzyhZGQ+Uo9dZXAgUDin6HAVLaxF3yWD8/yTKWN4UECQQD8Q72r7qdAfzdLMMSQl50VxRmbdhQYbo3D9FmwUw6W1gy2jhJyPXMi0JZKdKaqhxMZIT3zy4jYqw8/0zF2xc5/AkEA1W+n24Ef3ucbPgyiOu+XGwW0DNpJ9F8D3ZkEKPBgjOMojM7oqlehRwgy52hU+HaL4Toq9ghL1SwxBQPxSWCYSwJAGQUO9tKAvCDh9w8rL7wZ1GLsG0Mm0xWD8f92NcrHE6a/NAv7QGFf3gAaJ+BR92/WMRPe9SMmu3ab2JS1vzX3OQJAdN70/T8RYo8N3cYxNzBmf4d59ee5wzQb+8WD/57QX5UraR8LS+s8Bpc4uHnqvTq8kZG2YI5eZ9YQ6XwlLVbVTQJAKOSXNT+XEPWaol1YdWZDvr2m/ChbX2uwz52s8577Tey96O4Z6S/YA7V6Fr7hZEzkNF+K0LNUd79EOB6m2eQq5w==");
            System.out.println("加密前:" + data);
            System.out.println("明文length：" + data.length());
            System.out.println("加密后:" + encrypt);
            System.out.println("解密后:" + decrypt);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
