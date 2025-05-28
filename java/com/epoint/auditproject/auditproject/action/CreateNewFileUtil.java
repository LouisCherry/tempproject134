package com.epoint.auditproject.auditproject.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.util.StringUtils;

import sun.misc.BASE64Decoder;

public class CreateNewFileUtil {
	
	private String fileUrl;
	
	public CreateNewFileUtil() {
		//读取文件路径的配置文件
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("com/risen/base/config/fileUrl.properties");
		Properties p =new Properties();
		try {
			p.load(inputStream);
			this.fileUrl=p.getProperty("dataSource.fileUrl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	 public static void main(String[] args) throws IOException {
	    	
			
			
	    }
	

	
	 // 将 base64 转化为 file
    public static boolean base64ToFile(String base64,String path) {
    	
        byte[] buffer;
        try {
        	//用公司封装的base64Util进行解码。
            buffer = Base64.decode(base64);
            //用BASE64Decoder实现（建议使用本方法）。
            buffer = new BASE64Decoder().decodeBuffer(base64);
            //放入要输出文件的地址
            FileOutputStream out = new FileOutputStream(path);
            out.write(buffer);
            out.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("base64字符串异常或地址异常\n" + e.getMessage());
        }
    }
	
	
}
