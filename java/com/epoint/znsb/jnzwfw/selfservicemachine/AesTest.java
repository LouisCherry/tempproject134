package com.epoint.znsb.jnzwfw.selfservicemachine;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.epoint.znsb.jnzwfw.selfservicemachine.RSASignature;
import org.apache.log4j.Logger;


public class AesTest {

	
	private final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

	private final int HASH_ITERATIONS = 10000;
	private final int KEY_LENGTH = 128;

	// 定义密钥
	
	private char[] KEY = { '0', '1', 'p', 'c', 'a', '3', '5', 'D', 'p', 'W', '8', 'f', 'g', 'G', '3', 'd' };
	private byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF }; // must save this for next

	private PBEKeySpec myKeyspec = new PBEKeySpec(KEY, salt, HASH_ITERATIONS, KEY_LENGTH);
//	private final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
	private final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";

	private SecretKeyFactory keyfactory = null;
	private SecretKey sk = null;
	private SecretKeySpec skforAES = null;
	private byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91 };

	private IvParameterSpec IV;
	
	
	public AesTest() throws Exception {
		try {
			char[] key = "492848828d7eF889".toCharArray();
			System.out.println(key);
			myKeyspec = new PBEKeySpec(key, salt, HASH_ITERATIONS, KEY_LENGTH);
			keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
			sk = keyfactory.generateSecret(myKeyspec);

		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (InvalidKeySpecException ikse) {
			ikse.printStackTrace();
		}
		byte[] skAsByteArray = sk.getEncoded();
		skforAES = new SecretKeySpec(skAsByteArray, "AES");
		IV = new IvParameterSpec(iv);
	}
	

	public String encrypt(byte[] plaintext) throws Exception{
		try{
			byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV, plaintext);
			String base64_ciphertext = Base64Encoder.encode(ciphertext);
			return base64_ciphertext;
		}catch (Exception e){
			e.printStackTrace();
			return CIPHERMODEPADDING ;
		}				
	}

	public String decrypt(String ciphertext_base64) throws Exception{
		try{
			System.out.println("#####    待解密数据： "+ciphertext_base64);
			byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
			String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s), "UTF-8");
			return decrypted;
		}catch (Exception e){
			e.printStackTrace();
			return CIPHERMODEPADDING ;
		}
	}

	private byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) throws Exception{
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.ENCRYPT_MODE, sk, IV);
			return c.doFinal(msg);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
			throw nsae;
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
			throw nspe;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw e;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) throws Exception{
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.DECRYPT_MODE, sk, IV);
			return c.doFinal(ciphertext);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
			throw nsae;
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
			throw nspe;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
			throw e;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			throw e;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		AesTest aes=null;
		try {
			aes = new AesTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String bbb = "00053700";
//		char[] a1 = aaa.toCharArray();
		


		System.out.println("str === " + aes.encrypt(bbb.getBytes("UTF-8")));

		System.out.println("d str === " + aes.decrypt(aes.encrypt(bbb.getBytes("UTF-8"))));


	}
}
