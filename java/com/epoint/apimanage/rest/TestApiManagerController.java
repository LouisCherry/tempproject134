package com.epoint.apimanage.rest;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;

@RestController
@RequestMapping("/testApiManager")
public class TestApiManagerController {

	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	 
	/**
	 * 过滤apimanager提示接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/returnMessage", method = RequestMethod.POST)
	public String returnMessage(@RequestBody String params) {
		try {
			log.info("=======开始调用returnMessage接口=======");
			JSONObject dataJson = new JSONObject();
			JSONObject resultjson = new JSONObject();
			resultjson.put("result", "success");
			return JsonUtils.zwdtRestReturn("1", "信息查询成功！", resultjson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonUtils.zwdtRestReturn("0", "信息查询失败", "");
		}
	}
	
}
