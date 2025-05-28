package com.epoint.xmz.spglgt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public class TaMd5Util {
	public static String getFileChecksumMD5(File file) {
	    String md5 = null;
	    try (FileInputStream fileInputStream = new FileInputStream(file)) {
	        md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fileInputStream));
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	    return md5.toUpperCase();
	}
	
	 public static void main(String[] args) {
		    File file = new File("C:\\Users\\1\\Desktop\\tadata\\data.zip");
	    	//system.out.println(getFileChecksumMD5(file));
	        
	    }
}
