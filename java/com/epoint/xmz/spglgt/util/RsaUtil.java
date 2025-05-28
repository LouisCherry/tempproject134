package com.epoint.xmz.spglgt.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

public class RsaUtil {
	public static String generateKeyPair() {
	    KeyPairGenerator keyPairGen = null;
	    try {
	        keyPairGen = KeyPairGenerator.getInstance("RSA");
	    } catch (NoSuchAlgorithmException var8) {
	        var8.printStackTrace();
	        return null;
	    }

	    keyPairGen.initialize(1024, new SecureRandom());
	    KeyPair keyPair = keyPairGen.generateKeyPair();
	    RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
	    RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
	    String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
	    String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
	    return publicKeyString + "_SPLIT_"+privateKeyString;
	}
	
	
	public static void main(String[] args) {
	    	//system.out.println(generateKeyPair());
	        
	    }
	
}
