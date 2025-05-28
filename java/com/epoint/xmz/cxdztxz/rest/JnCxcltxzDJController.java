
package com.epoint.xmz.cxdztxz.rest;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping("/jncxcltxzdj")
public class JnCxcltxzDJController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String cx_url= ConfigUtil.getConfigValue("epointframe", "cxurl");
	
	@Autowired
	private IConfigService configService ;
	
	/**
	 * 提交成品油变更的办件数据推送到
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getcxcode", method = RequestMethod.POST)
	public String getCxCode(@RequestBody String params) {
		try {
			log.info("=======开始调用getCxCode接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			 String cxyscltxzurl = configService.getFrameConfigValue("cxyscltxzurl");
			String json = "{\"deptId\":\"370800\"}";
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("ApiKey", "767766629319704576");
        	JSONObject dataJson = new JSONObject();
        	String result = HttpUtil.doPostJson(cxyscltxzurl, json, headers);
        	log.info("超限接口调用输出result:"+result);
        	String code = "";
        	if (StringUtil.isNotBlank(result)) {
        		JSONObject jsons = JSON.parseObject(result);
        		if ("200".equals(jsons.getString("code"))) {
        			log.info("超限接口输出jsons："+jsons);
        			JSONObject data = jsons.getJSONObject("data");
        			if ("200".equals(data.getString("code"))) {
        				log.info("超限接口输出data："+data);
        				String detail = data.getString("data");
        				 //解析XML
        		        org.dom4j.Document document = null;
        		        try {
        		            document = DocumentHelper.parseText(detail);
        		        } catch (DocumentException e) {
        		           e.printStackTrace();
        		        }
        		        if (detail != null && StringUtil.isNotBlank(detail)) {
        		        	 org.dom4j.Element root = document.getRootElement();
        		        	 List<org.dom4j.Element> elements = root.elements();
        		        	 for (org.dom4j.Element element : elements) {
        		        		 //system.out.println("QualifiedName:"+element.getQualifiedName()+";StringValue:"+element.getStringValue());
        		        		 if ("DATA".equals(element.getQualifiedName())) {
        		        			 code = element.getStringValue();
        		        		 }
        		        	 }
        		        }else {
        		        	code = "";
        		        }
        			}else {
        				code = "";
        			}
        		}else {
        			code = "";
        		}
        	}
        	dataJson.put("cxcode", code);
			return JsonUtils.zwdtRestReturn("1", "获取Code成功", dataJson.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getCxCode接口参数：params【" + params + "】=======");
			log.info("=======getCxCode异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取Code失败：" + e.getMessage(), "");
		}
	}

	

}
