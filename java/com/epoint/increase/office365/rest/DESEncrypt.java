package com.epoint.increase.office365.rest;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public class DESEncrypt
{
    private static final byte[] DESkey = "12345678".getBytes();// 设置密钥，略去
    private static final byte[] DESIV = "87654321".getBytes();// 设置向量，略去
    // 加密算法的参数接口，IvParameterSpec是它的一个实现
    static AlgorithmParameterSpec iv = null;
    private static Key key = null;

    public DESEncrypt() throws Exception {
        this(DESkey, DESIV);
    }

    public DESEncrypt(String DESkey, String DESIV) throws Exception {
        this(DESkey.getBytes(), DESIV.getBytes());
    }

    private DESEncrypt(byte[] DESkey, byte[] DESIV) throws Exception {
        // 设置密钥参数
        DESKeySpec keySpec = new DESKeySpec(DESkey);
        // 设置向量
        iv = new IvParameterSpec(DESIV);
        // 获得密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        key = keyFactory.generateSecret(keySpec);// 得到密钥对象
    }

    /**
     * 加密
     * @param data
     * @return
     * @throws
     */
    public String encode(String data) throws Exception {
        // 得到加密对象Cipher
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 设置工作模式为加密模式，给出密钥和向量
        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        return Base64.encodeBase64String(pasByte).replaceAll("\\+", "_").replaceAll("\\/", "*").replaceAll("\\=", "-");
    }

    /**
     * 解密
     * @param data
     * @return
     * @throws
     */
    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] pasByte = deCipher
                .doFinal(Base64.decodeBase64(data.replaceAll("_", "+").replaceAll("\\*", "/").replaceAll("-", "=")));
        return new String(pasByte, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        DESEncrypt tools = new DESEncrypt();
        String url = "https://60.211.226.110:8443/epoint-zwfwsform-web/rest/attachAction/getContent?isCommondto=true&attachGuid=1a0395cf-76f0-436b-b851-82886bf02b81";
        String encode = tools.encode(url);
    }
}
