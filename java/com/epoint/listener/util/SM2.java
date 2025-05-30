package com.epoint.listener.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

public class SM2
{
    public static String encrypt(String publicKey, String data) {
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(),
                sm2ECParameters.getN());
        // 提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);

        SM2Engine.Mode c1c2c3 = SM2Engine.Mode.C1C2C3;

        SM2Engine sm2Engine = new SM2Engine(c1c2c3);
        // 设置sm2为加密模式
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));

        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes();
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        }
        catch (Exception e) {
            System.out.println("SM2加密时出现异常:" + e.getMessage());
        }
        return Hex.toHexString(arrayOfBytes);
    }

    /**
     * SM2解密算法
     *
     * @param priKeyHexString
     *            私钥（16进制字符串）
     * @param cipherData
     *            密文数据
     * @return
     */
    public static String decrypt(String priKeyHexString, String cipherData) {

        // 使用BC库加解密时密文以04开头，传入的密文前面没有04则补上
        if (!cipherData.startsWith("04")) {
            cipherData = "04" + cipherData;
        }
        byte[] cipherDataByte = Hex.decode(cipherData);

        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造domain参数
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(),
                sm2ECParameters.getN());

        BigInteger privateKeyD = new BigInteger(priKeyHexString, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        // 设置sm2为解密模式
        sm2Engine.init(false, privateKeyParameters);
        String result = "";
        try {
            byte[] arrayOfBytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
            return new String(arrayOfBytes);
        }
        catch (Exception e) {
            System.out.println("SM2解密时出现异常:" + e.getMessage());
        }
        return result;
    }
}
