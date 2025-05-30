package com.epoint.xmz.apimanager;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.apimanage.event.basic.api.IEventDispatchService;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;

@RestController
@RequestMapping("/testApiManager")
public class TestApiManagerController {

	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	 private String accessId = "DASHUJU_YICHUANGSHOULI";
	 private String accessToken = "B5BA2AAB58EE49D5A860BA129468719E";
	    
	
	 @Autowired
	 private IEventDispatchService iEventDispatchService;
	 
	 
	/**
	 * 测试apimanager接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
	public String getAccessToken(@RequestBody String params) {
		try {
			log.info("=======开始调用getAccessToken接口=======");
			JSONObject dataJson = new JSONObject();
			JSONObject data = JSONObject.parseObject(params);
			String qrcode = data.getString("qrcode");
			// apimanager地址
            String apimanagerurl = "http://112.6.110.176:8080/epoint-jkjk";
            //apimanager流水号标识
            String apino = "JN20220826001";
            // 转发请求
            try {
            	
            	 String postreason = "{\"head\":{\"accessId\":\"" + accessId + "\",\"accessToken\":\"" + accessToken
                         + "\"},\"data\":{\"qrcode\":\"" + qrcode + "\", \"useCause\":\"济宁政务服务\"}}";
            	 
                Record result = iEventDispatchService.execute(apimanagerurl, apino, postreason);
                JSONObject resultjson = new JSONObject();
                if (result != null) {
                    Set<String> keyset = result.keySet();
                    for (String key : keyset) {
                        resultjson.put(key, result.get(key));
                    }
                }
                return resultjson.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
                return JsonUtils.zwdtRestReturn("0", "获取token信息失败", dataJson.toString());
            }
            
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getAccessToken接口参数：params【" + params + "】=======");
			log.info("=======getAccessToken异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败：" + e.getMessage(), "");
		}
	}
	
	 public String getParamJson (ServletRequest request) {
	        InputStream inputStream = null;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        String params = "";
	        try {
	            inputStream = request.getInputStream();
	            int len = -1;
	            byte[] buf = new byte[128];
	            while ((len = inputStream.read(buf)) != -1) {
	                out.write(buf, 0, len);
	            }
	            params = new String(out.toByteArray(), "UTF-8");
	        }
	        catch (Exception e) {
	            e.printStackTrace();

	        }
	        finally {
	            try {
	                inputStream.close();
	                out.close();
	            }
	            catch (Exception e) {

	            }
	        }
	        return params;
	    }
	 
}
