package com.epoint.common.zwdt.login.dhsignutil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SignatureUtil {

    /**
     * 获取签名
     * 
     * @param params
     * @param privateKey
     * @param charset
     * @param signType
     * @return
     */
    public static String rsaSign(Map<String, String> params, String privateKey, String charset, String signType) {
        String content = getSignCheckContentV2(params);
        return rsaSign(content, privateKey, charset, signType);
    }

    /**
     * 获取签名认证的参数内容
     * 
     * @param params
     * @return
     */
    public static String getSignCheckContentV2(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            params.remove("sign");
            return getSignContent(params);
        }
    }

    /**
     * 获取参数内容
     * 
     * @param sortedParams
     * @return
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;

        for (int i = 0; i < keys.size(); ++i) {
            String key = (String)keys.get(i);
            String value = (String)sortedParams.get(key);
            if (areNotEmpty(new String[] {key, value})) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                ++index;
            }
        }

        return content.toString();
    }

    public static boolean areNotEmpty(String[] values) {
        boolean result = true;
        if ((values == null) || (values.length == 0)) {
            result = false;
        } else {
            for (String value : values) {
                result &= !(isEmpty(value));
            }
        }
        return result;
    }

    public static boolean isEmpty(String value) {
        int strLen;
        if ((value == null) || ((strLen = value.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < strLen; ++i) {
            if (!(Character.isWhitespace(value.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取签名
     * 
     * @param content
     * @param privateKey
     * @param charset
     * @param signType
     * @return
     */
    public static String rsaSign(String content, String privateKey, String charset, String signType) {
        if ("RSA".equals(signType)) {
            return rsaSignSingle(content, privateKey, charset);
        } else {
            System.out.println("加密方式错误");
        }
        return null;
    }

    /**
     * MD5RSA签名获取
     * 
     * @param content
     * @param privateKey
     * @param charset
     * @return
     */
    public static String rsaSignSingle(String content, String privateKey, String charset) {
        try {
            PrivateKey priKey = getPrivateKeyFromPkcs8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(priKey);
            if (isEmpty(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(BaseUtil.encodeBase64(signed));
        } catch (InvalidKeySpecException ex) {
            System.out.println("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 获取私钥对象
     * 
     * @param algorithm
     * @param ins
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPkcs8(String algorithm, InputStream ins) throws Exception {
        if (ins != null && !isEmpty(algorithm)) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            byte[] encodedKey = StreamUtil.readText(ins).getBytes();
            encodedKey = BaseUtil.decodeBase64(encodedKey);
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        }
        return null;
    }
}
