package com.epoint.cert.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class EncryDecryUtil3
{
    private static final Logger logger = Logger.getLogger(EncryDecryUtil3.class);

    // 可以删除掉，仅测试使用
    public static void main(String[] args) {
        logger.info("开始-------------------------------------------------------------------");
        EncryDecryUtil3 test = new EncryDecryUtil3();
        JSONObject result = test.decryption("CFD0CA22008C3ACA6ECD624344F355092BDA0160201E989AF5138FDC6305486A8DBC14B923D2223E200562E1E01230CBA242C9708AB6BE5DA49CE2E757418A0F95AFBBAF618F02CEFFA644D36EF60033DBF7558AFB766DDD640B6BE24BA17513D5982A78B99D19A37AF71093817C0981DD3E639EE04FCB08B6C760EBB90BE30463395CA4F26DBE18EF25480405863A2725DA114BF43E07501A52EFAA64ACD5BEECB631AE399E25B3E376384C02BA80110350F58808C0909EC1E63C943ABD561BDD30EAAD2CE740BB1738B97547432EAFDF16E144B6D8CCD8CC84CF37D8405736CB432C437D7AFF2E1BFC1501C4592E45",
        		"",
        		"EooDVlzKVwTrmd9PbFdpRdrxBookKzfNJ4hXOC58KJ/xvZjAIWlyEn6xBmbeeN0HkoOp32IxeH51Hb2OM8JXgg==",
        		"MIIBVwIBADANBgkqhkiG9w0BAQEFAASCAUEwggE9AgEAAkEAl56HOLhFWuSifkob2vKRcCl1I8DTSap5sTsgsZX80L4OBzOSSH7T1RQzpAW9rEPtOQXowaYMph5aQM8YbrYKlQIDAQABAkEAiVVcojG3EId77+xsoruIpRHIOuRT/aveonwuNuzmnKPCvErgGRPnRY+uzEAKY01fVQangBvY6yOS0cYopmhUgQIhANvncACMGTbRjs1vsTaQh/lgKVNwc1BOiCYw5JMAQc/FAiEAsIGz+Tgsyj7tIZELusiopZ3BF81d/phx6MtRH3jrrJECIQCEIul2Krjr67f3UeoWc3qBKnsqnCNuWgINkMuIWVsyUQIhAKBN3CevVjaEuhcvRYjpbwmjYdh9Qy3URDgaV94Ok3SRAiEAzSjziF2AY9cVytAAzw9XLj6Yvqf0TjlH1srCJ2EYYLA="
        		
        		);
        logger.info("result:"+result.toString());
        logger.info("结束-------------------------------------------------------------------");
    }

    /**
     * 返回加密后的证照及企业和key
     *
     * @param certid
     *            证照的唯一标识
     * @param provincenum
     *            企业所在省份
     * @return
     */
    public JSONObject encryption() {
        JSONObject rtnObj = new JSONObject();
        try {
            logger.info("-------------------------------------------------------------------");
            logger.info("提示：下面为随机生成的公钥和私钥，可以作为加密参数保存好，然后可“将随机字符串用ras加密“注释下的代码注释,将下面的publicKeyString替换成固定的即可");
            // 32省
            for (int i = 0; i < 32; i++) {
                // 将随机字符串用ras加密
                // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
                KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
                // 初始化密钥对生成器,“密钥长度”一般只是指模值的位长度，一般是1024，2048，3072，越大加密长度越长
                // 密钥长度必须是64的倍数，在512到65536位之间
                keyPairGen.initialize(512);
                // 生成一个密钥对，保存在keyPair中
                KeyPair keyPair = keyPairGen.generateKeyPair();
                // 私钥
                RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                // 公钥
                RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                // 对应的字符串
                String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
                String privateKeyString = Base64.encodeBase64String((privateKey.getEncoded()));
                logger.info(i + 1 + "随机生成的公钥：" + publicKeyString);
                logger.info(i + 1 + "随机生成的私钥：" + privateKeyString);

            }
            logger.info("-------------------------------------------------------------------");
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);

        }
        return rtnObj;
    }

    /**
     * 解密
     *
     * @param encryCertid
     *            加密后的证照的唯一标识
     * @param encryProvincenum
     *            加密后的企业所在省份
     * @param encryKey
     *            ras加密后的key
     * @param privateKeyString
     *            私钥
     */
    public JSONObject decryption(String encryCertid, String encryProvincenum, String encryKey,
            String privateKeyString) {
        JSONObject rtnObj = new JSONObject();
        try {
            // 先将key进行ras解密
            String decryKey = RasDecrypt(encryKey, privateKeyString);

            // 生成密钥对象,如果算法是DES，那么这个构造函数不会检查key是否为8个字节长
            SecretKeySpec secretKeySpec = new SecretKeySpec(decryKey.getBytes(), "DES");
            // 获取加解密实例
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            // 初始化解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            // HexBinaryAdapter十六进制转换工具
            HexBinaryAdapter hexBinaryAdapter = new HexBinaryAdapter();
            // 解密
            byte[] ecertidResult = cipher.doFinal(hexBinaryAdapter.unmarshal(encryCertid));
            // byte[] provincenumResult =
            // cipher.doFinal(hexBinaryAdapter.unmarshal(encryProvincenum));

            rtnObj.put("certid", new String(ecertidResult));
            // rtnObj.put("provincenum", new String(provincenumResult));
            rtnObj.put("provincenum", encryProvincenum);
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rtnObj;
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     */
    public static String RasEncrypt(String str, String publicKey) throws Exception {
        // base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        // X509EncodedKeySpec该类表示公钥的ASN.1编码
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     */
    public static String RasDecrypt(String str, String privateKey) throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        // PKCS8EncodedKeySpec该类代表私有密钥的ASN.1编码
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

}
