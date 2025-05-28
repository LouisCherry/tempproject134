package com.epoint.province.hcp.util;

import java.nio.charset.StandardCharsets;

import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;

public class HcpSm2Util
{

    /**
     * 广州 SM2 加密
     * 
     * @param publicKey
     * @param text
     * @return
     */
    public static String encrypt(String text, String publicKey) {
        if (publicKey.length() == 130) {
            // 这里需要去掉开始第一个字节 第一个字节表示标记
            publicKey = publicKey.substring(2);
        }
        String xhex = publicKey.substring(0, 64);
        String yhex = publicKey.substring(64, 128);
        ECPublicKeyParameters ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        // 创建sm2 对象
        SM2 sm2 = new SM2(null, ecPublicKeyParameters);

        // 公钥加密，私钥解密
        String encryptStr = sm2.encryptBcd(text, KeyType.PublicKey, StandardCharsets.UTF_8);
        return encryptStr;
    }

    /**
     * 广州 SM2 解密
     * 
     * @param privateKey
     * @param text
     * @return
     */
    public static String decrypt(String text, String privateKey) {
        // 创建sm2 对象
        ECPrivateKeyParameters privateKeyParameters = BCUtil.toSm2Params(privateKey);
        SM2 sm2 = new SM2(privateKeyParameters, null);
        // 公钥加密，私钥解密
        String decryptStr = sm2.decryptStrFromBcd(text, KeyType.PrivateKey, StandardCharsets.UTF_8);
        return decryptStr;
    }

}
