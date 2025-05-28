package com.epoint.xmz.spglgt.util;

import java.io.File;

import com.epoint.core.utils.classpath.ClassPathUtil;

public class New {
	 public static void main(String[] args) {
	        String[] key = XsUserInfoUtils.generateKeyPair().split(";");
	        String privatekey = key[1];
	        String sign = XsUserInfoUtils.sign("123456", "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJjkIdV/BrzBJDvgp1/23L7la10sH5SMEOXtuHZGiaJFY0Nngmg/7x1GrK74DrpN9wsmGHYFg9LI9qG6hrq+wmO5/kndqvmBnZg1sjhA0YSM3VdfpB6GUjNTBrx68mY7hn6mJ5Ivo0hrYaUvTFoXoEN5cL5LAxm4i+YqJdEjX5GTAgMBAAECgYBhnsAvpkSx/oxZIw8bie7WgHu16PddDLW5abiu5Ej68Fz567xrPe85/SdDLKWStTc4yiEyMkFCNAitqQBfem946VEh4PlFhsdSxG87pU4LVNcATcN6qAN1R17U+2B1kiHIdQEENd+V26mi2PmOW11gcLaZd1GRF7Fd3yWswaf0CQJBAN0uFdKwU5wbj3Plegm7RDPg1+YeH5Kz0q4jpK+m2ZzZaAGD8699hQJd+pazXyqjku8hvsNsQc6HQTP5tlGbF80CQQCw9eTabVwx48mWAJjeZhpyzSz+6VcuBMUFKAa+E/Nupiv1eiB2Rkysk5KZKG9PwTUQV/QbsfrCdrIDjgI9+y7fAkEA1gFIRkIrs+kKxRWtHgtes1+DJnnRDM4HNEzyhK5I8jV9J7I3r34gmyi5VOUbWJNKkSwyer2dcqp10Ud1O8J3pQJAIm4FM9ZZPL+0BgJhu8uj07hFy+b8dlCpQKKUBUF+eIXOQbmcPUbaPi2MNU5fseXGfEKQlR7gyhsr8XMczpBjgwJBALJ7pFNOq8vOxI0HZDjXouvxRsof0V8kllLgUbZCHi/0hmlnrZtRvjNaCxbrsGtks36DKCOgvQb0+EQIhYHeHBQ=");
//	         String abc =  decryptByPublicKey(sign,"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCY5CHVfwa8wSQ74Kdf9ty+5WtdLB+UjBDl7bh2RomiRWNDZ4JoP+8dRqyu+A66TfcLJhh2BYPSyPahuoa6vsJjuf5J3ar5gZ2YNbI4QNGEjN1XX6QehlIzUwa8evJmO4Z+pieSL6NIa2GlL0xaF6BDeXC+SwMZuIvmKiXRI1+RkwIDAQAB");
//	       //system.out.println(abc);
	        String md5 = XsUserInfoUtils.getFileChecksumMD5(new File(ClassPathUtil.getDeployWarPath() + "data/data.zip"));
	        //system.out.println(md5);
	    }

}
