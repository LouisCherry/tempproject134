
package com.epoint.xmz.slysflowsn;

import java.lang.invoke.MethodHandles;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.xmz.api.IJnService;

@RestController
@RequestMapping("/jnslysflowsn")
public class JnSlysFlowsnController {
	
	@Autowired
	private IJnService iJnService;
	
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	 
	/**
	 * 获取电子印章系统的认证token
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getflowsn", method = RequestMethod.POST)
	public String getflowsn(@RequestBody String params) {
		try {
			log.info("=======开始调用getflowsn接口=======");
			// 1、接口的入参转化为JSON对象
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			
			String flwosn = iJnService.getZjSlFlowsn("sl");
			
			String result = Integer.parseInt(flwosn)+1+"";
			
			if(result.length() == 1) {
				result = "00000" + result;
			}
			else if (result.length() == 2) {
				result = "0000" + result;
			}
			else if (result.length() == 3) {
				result = "000" + result;
			}
			else if (result.length() == 4) {
				result = "00" + result;
			}
			else if (result.length() == 5) {
				result = "0" + result;
			}
			
			iJnService.updateZjSlFlowsn(result,"sl");
			
			
			dataJson.put("flowsn", "济审航XK"+result+"");
			
			
			return JsonUtils.zwdtRestReturn("1", "获取水路运输事项编号成功", dataJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getflowsn接口参数：params【" + params + "】=======");
			log.info("=======getflowsn异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取水路运输事项编号失败：" + e.getMessage(), "");
		}
	}
	
	
	
	
}
