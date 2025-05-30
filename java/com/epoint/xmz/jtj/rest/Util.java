package com.epoint.xmz.jtj.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Util {
	private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
	 
	/**
	 * 
	 * @param params
	 *            传递的普通参数
	 * @param uploadFile
	 *            需要上传的文件名
	 * @param fileFormName
	 *            需要上传文件表单中的名字
	 * @param newFileName
	 *            上传的文件名称，不填写将为uploadFile的名称
	 * @param urlStr
	 *            上传的服务器的路径
	 * @throws IOException
	 */
	public String uploadForm(Map<String, String> params, String fileFormName,
			InputStream inputStream, String newFileName, String urlStr,String size)
			throws IOException {
 
		StringBuilder sb = new StringBuilder();
		/**
		 * 普通的表单数据
		 */
		for (String key : params.keySet()) {
			sb.append("--" + BOUNDARY + "\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + key + "\""
					+ "\r\n");
			sb.append("\r\n");
			sb.append(params.get(key) + "\r\n");
		}
		/**
		 * 上传文件的头
		 */
		sb.append("--" + BOUNDARY + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"" + fileFormName
				+ "\"; filename=\"" + newFileName + "\"" + "\r\n");
		sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType
		sb.append("\r\n");
 
		byte[] headerInfo = sb.toString().getBytes("UTF-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");
		System.out.println(sb.toString());
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty("Content-Length", String
				.valueOf(headerInfo.length + size
						+ endInfo.length));
		conn.setDoOutput(true);
 
		OutputStream out = conn.getOutputStream();
		InputStream in = inputStream;
		out.write(headerInfo);
 
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) != -1)
			out.write(buf, 0, len);
 
		out.write(endInfo);
		in.close();
		out.close();
		
		if (conn.getResponseCode() == 200) {
			System.out.println("上传成功");
		}
		
		   // 读取返回数据  
        StringBuffer strBuf = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
        		conn.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            strBuf.append(line).append("\n");
        }
        String res = strBuf.toString();
        reader.close();
        
		return res;
 
	}
}
