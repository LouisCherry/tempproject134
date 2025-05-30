package com.epoint.common.filedownload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.core.utils.code.Base64Util;

@RestController
@RequestMapping("/attachloadservicecontroller")
public class AttachLoadServiceController
{

	/**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 下载地址
     * @param response
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/downloadfile", method = RequestMethod.POST)
    public String downloadfile(HttpServletResponse response, @RequestBody String params, HttpServletRequest request) {
    	try {
    		log.info(">>>>>>>>>>>>开始调用downloadfile接口：" + params);
			JSONObject paramobj = JSONObject.parseObject(params);
			String fileurl = paramobj.getString("fileurl");
			// 下载文件
            URL url = new URL(fileurl);
            // 附件存储地址
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(60000);
            urlCon.setReadTimeout(60000);
            // 设定请求的方法，默认是GET
            urlCon.setRequestMethod("GET");
            // 设置字符编码
            urlCon.setRequestProperty("Charset", "UTF-8");
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
            }
            // 读文件流
            InputStream in = urlCon.getInputStream();
            byte[] bytes = toByteArray(in);
            String filebase64 = Base64Util.encode(bytes);
            JSONObject returnobject = new JSONObject();
            returnobject.put("content", filebase64);
            return returnobject.toJSONString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }

    public byte[] toByteArray(InputStream input) throws IOException{

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
        output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
