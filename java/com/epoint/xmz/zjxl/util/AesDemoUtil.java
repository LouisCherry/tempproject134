package com.epoint.xmz.zjxl.util;

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


public class AesDemoUtil {

	
	public final String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

	public final int HASH_ITERATIONS = 10000;
	public final int KEY_LENGTH = 128;

	public static byte[] hexStringToByteArray(String s) {

		  int len = s.length();
		  byte[] data = new byte[len / 2];

		  for (int i = 0; i < len; i += 2) {
		   data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
		     + Character.digit(s.charAt(i+1), 16));
		  }

		  return data;
		  
	}
	// 定义密钥
	
	  
	public char[] KEY = { '0', '1', 'p', 'c', 'a', '3', '5', 'D', 'p', 'W', '8', 'f', 'g', 'G', '3', 'd' };
	public byte[] salt = hexStringToByteArray("c9f1f0ca9500dbf7c77b361032bc7324"); // must save this for next

	public PBEKeySpec myKeyspec = new PBEKeySpec(KEY, salt, HASH_ITERATIONS, KEY_LENGTH);
//	public final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";
	public final String CIPHERMODEPADDING = "AES/CBC/PKCS5Padding";
	
	public final String keyValue = "a9624ED675497332";

	public SecretKeyFactory keyfactory = null;
	public SecretKey sk = null;
	public SecretKeySpec skforAES = null;
	public byte[] iv = hexStringToByteArray("2cf9f8a05026a9e1396f28bfcad17d49");

	public IvParameterSpec IV;
	
	
	public AesDemoUtil() throws Exception {
		try {
			char[] key = keyValue.toCharArray(); 
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
			byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
			String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES, IV, s), "UTF-8");
			return decrypted;
		}catch (Exception e){
			e.printStackTrace();
			return CIPHERMODEPADDING ;
		}
	}

	public byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] msg) throws Exception{
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

	public byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV, byte[] ciphertext) throws Exception{
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
		AesDemoUtil aes=null;
		try {
			aes = new AesDemoUtil();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String aaa = "test";
		char[] a1 = aaa.toCharArray();
//		
//		String encryptStr = aes.encrypt(aaa.getBytes("UTF-8"));
//		
//		System.out.println("str === " + encryptStr);
		
		String decryptStr = aes.decrypt("nPJbtEm/rBDe1IQ8d9KEShq+Xj7wkRgcjLgW/l8A16MtDfChkAV6b3j5vScupCA/MwFrfQQryhcMaRuTR3H+V6yVr5KZFNhEtX1MOS8l5jJTZUjfsd5OrA4sGC4W+Udzg2FIs1kRlhej6+XbJdUfqu0AJ4cdxecX2pwcrjHna2lXSP+VpsJv2pWwo5A+8zZWxug4HHWisjwrsKa0aIf2P1BQGYmQOiFB2xRWXhz+p/Z5bfoxE98zleFkG38bkSMYReWiNOvR+wVnO1CePkpP668UYxNUidfTKVnkjJWrenClNvU1dbdgCh+y4wPOZ5zOUuqFa9LRLL90J87jzcbfiaoQe/mXMiNrBqjUXmFZ5G0tVitwn5qz4SqeN6eAuzS8m/+g6O4XkZbyZWuCk6aHOJst1QlX4u9EGaj+crpwLbs=");
		
		System.out.println("d str === " + decryptStr);
		
		
		
	}
}
