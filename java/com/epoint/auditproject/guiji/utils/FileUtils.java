package com.epoint.auditproject.guiji.utils;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具类
 * @author 刘雨雨
 * @time 2018年8月24日下午6:52:25
 */
public class FileUtils {
	
	private static Logger logger = Logger.getLogger(FileUtils.class);
	
	/**
	 * 读取输入流中的内容到字节数组中
	 * @param in
	 * @return
	 */
	public static byte[] readBytesFromInputStream(InputStream in) {
		if (in != null) {
			try {
				byte[] buff = new byte[in.available()];
				in.read(buff);
				return buff;
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}
}
