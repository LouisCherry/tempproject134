/**************************************************************************************
 *
 * @(#) V2AuthUtil.java
 * @Package com.crcgas.bizonline.openapi.common.utils
 *
 * 注意：本内容为华润燃气集团机密信息，仅限内部传阅，未经本公司书面同意，禁止外传以及用于其他商业目的
 * Copyright © China Resources Gas Group Limited. All rights reserved.
 *
 **************************************************************************************/
package com.epoint.gassupply.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author lsmingyux
 * @Description:
 * @date 2022/3/31
 */
@Component
public class V2AuthUtil {

	public static void main(String[] args) throws Exception {
		// 生成密码
//		System.out.println(UidUtils.randomUUIDReplace());
		//生成公钥和私钥
//		genKeyPair();

		/**
		 * 此处需替换为微网厅签发的秘钥信息
		 */
		String USER_NAME = "jiningOWA";
		String PASSWORD = "7912df06448144e3be59d1365d680b00";
		String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK6AilURfFEi6imBvJSnob/I3Ae/9Hw6dKJJ4Xmu4zp5+S7dBqc2nc2E0nlT+TdkPh0gvFapvLAwgvc3WjUFk/sCAwEAAQ==";
		String PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAkEZAGgs9Ywtexb9IekxU9lIngM+aag+0GwpzBhZ0hslQNzvjqiyR36bMROzHFE2OVOmL4frBtG36dGQdF9la0QIDAQABAkBvKtJFvxdj8+TotkVghuSIkDEEHfM7ejIrC9gpfq/3EotRnP9orynLxQg2/6a03Hi0IP2MX4BWzdWhYTzgr6/hAiEAxGcugS6aUX5dzPrVzgUIWNqQd5EM/CnAWTRIiu+AfdUCIQC8DafELHlTzTiNRBFeFQsqTTscLVXlTkJLyuqjhBebDQIgC0btaad2XuWFl0Zzzk+FqixqjSL7/uKa08Rnwep8kz0CIQCrX5ZfxFfc8sxykHFzV6IRLsiLaRU6HxNzlrw1v47zkQIgdUb17BtV7W2T4m3ENunujms90RBjtjoBGIo6/mBV5xk=";
		/*
		 * 生成签名字符串param
		 */
		String param = getParam(USER_NAME, PASSWORD, PUBLIC_KEY);
		System.out.println("生成的签名字符串-->"+param);
		/**
		 * 解密签名字符串param
		 */
		byte[] dataByte = java.util.Base64.getDecoder().decode(param);
		String paramStr = new String(dataByte, "utf-8");
		JSONObject jsonObject = JSONObject.parseObject(paramStr);
		String username = jsonObject.getString("USER");
		String pwd = jsonObject.getString("PWD");
		System.out.println("解密后的签名字符串-->"+paramStr);
		System.out.println("解密后的PWD字符串-->"+decrypt(pwd,PRIVATE_KEY));

	}

	/**
	 * 随机生成密钥对
	 * @throws NoSuchAlgorithmException
	 */
	public static void genKeyPair() throws NoSuchAlgorithmException {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为96-512位
		keyPairGen.initialize(512, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
		// 得到私钥字符串
		String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
		// 将公钥和私钥保存到Map
		System.out.println("随机生成的公钥为:" + publicKeyString);
		System.out.println("随机生成的私钥为:" + privateKeyString);
	}

	/**
	 * 生成签名信息
	 * @param user
	 * @param password
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String getParam(String user, String password, String publicKey) throws Exception {
		String encrypt = encrypt(password +"#"+ System.currentTimeMillis(), publicKey);
		String PARAM = "{\"USER\":\"%s\",\"PWD\":\"%s\"}";
		return new String(Base64.encodeBase64(String.format(PARAM, user, encrypt).getBytes()));
	}

	/**
	 * RSA公钥加密
	 *
	 * @param str
	 *            加密字符串
	 * @param publicKey
	 *            公钥
	 * @return 密文
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static String encrypt(String str, String publicKey) throws Exception {
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("utf-8")));
		return outStr;
	}

	/**
	 * RSA私钥解密
	 *
	 * @param str
	 *            加密字符串
	 * @param privateKey
	 *            私钥
	 * @return 铭文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static String decrypt(String str, String privateKey) throws Exception {
		//64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
}
