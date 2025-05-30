package com.epoint.zwdt.zwdtrest;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 加密方法，是对称的密码算法(加密与解密的密钥一致)，这里使用最大的 256 位的密钥
 */
public class AESUtil {

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
    public static SecretKey strKey2SecretKey(String strKey){
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
    public static byte[] encryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
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
    public static byte[] decryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    /**
     * 字节数组转Base64编码
     * @param bytes
     * @return
     */
    public static String byte2Base64(byte[] bytes){
        //避免在jdk7以下出现换行问题
        return BASE64_ENCODER.encodeToString(bytes).replaceAll("\r|\n", "");
    }

    /**
     * Base64编码转字节数组
     * @param base64Key
     * @return
     */
    public static byte[] base642Byte(String base64Key) {
        return BASE64_DECODER.decode(base64Key);
    }

    public static void main(String[] args) {
        try {
            // 返回经 BASE64 处理之后的密钥字符串
//            String strKeyAES = getStrKeyAES();
            String strKeyAES = "epoint@123!@#$";
                    //TODO: 填入文档中的秘钥
            //String strKeyAES = "";
            System.out.println("密钥：" + strKeyAES);
            // 将使用 Base64 加密后的字符串类型的 secretKey 转为 SecretKey
            SecretKey secretKey = com.epoint.common.zwdt.login.dhrsautil.AESUtil.strKey2SecretKey(strKeyAES);

            // 要加密的内容
            // ======================加密开始===================================
/*            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("itemCode", "test123");
            jsonObject.put("itemName", "某办件");
            jsonObject.put("deptName", "某部门");
            jsonObject.put("userId", "123456789");
            jsonObject.put("userType", 1);
            jsonObject.put("projectTarget", "张三");
            jsonObject.put("licenseNo", "310212190011111234");
            jsonObject.put("mobile", "18888888888");
            String content = jsonObject.toJSONString();*/
//            String content = "{\"taskguid\":\"45cf2f67-8512-42e5-95b8-aa2a9e39297d\",\"projectguid\":\"976b1d27-de42-4cef-8df0-48792f395b62\",\"applyertype\":\"20\",\"applyername\":\"董振华\",\"applyermobile\":\"17854119957\",\"idcard\":\"370322199704031331\",\"certtype\":\"身份证\",\"address\":\"测试\",\"contactname\":\"\",\"contactmobile\":\"\",\"cliengguid\":\"e5174e4f-f417-4a0e-a5fd-c1ba7fcb4c59\",\"promisecliengguid\":\"\"}";
//            System.out.println("加密的内容：" + content);
//            //避免出现中文乱码，此处务必指定编码
//            String publicEncryptStr = com.epoint.common.zwdt.login.dhrsautil.AESUtil.encrypt(content, strKeyAES);
//            System.out.println("AES加密后：" + publicEncryptStr);
            // ======================加密结束===================================

            // ======================解密开始===================================
             String publicEncryptStr = "";
            byte[] publicEncryptByte = AESUtil.decryptAES(AESUtil.base642Byte(publicEncryptStr), secretKey);
            String decodeContent = new String(publicEncryptByte);
            System.out.println("AES解密：" + decodeContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}