
package com.epoint.xmz.zbxmlist;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/jnzblist")
public class JnZbListController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String zb_url= ConfigUtil.getConfigValue("epointframe", "zhaobiaourl");
	
	
	/**
	 * 查询招标企业信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getZbDetail", method = RequestMethod.POST)
	public String getZbDetail(@RequestBody String params) {
		try {
			log.info("=======开始调用getZbDetail接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("submitCompany入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			
			String TenderNum = jsonObject.getString("TenderNum");
			String TenderProjectCode = jsonObject.getString("TenderProjectCode");
			String TenderCorpName = jsonObject.getString("TenderCorpName");
			String TenderCorpCode = jsonObject.getString("TenderCorpCode");
			String BidSectionCode = jsonObject.getString("BidSectionCode");
			
			JSONObject json = new JSONObject();
			json.put("SkipCount", 0);
			json.put("MaxResultCount", 50);
			json.put("TenderNum", TenderNum);
			json.put("TenderProjectCode", TenderProjectCode);
			json.put("BidSectionCode", BidSectionCode);
			json.put("TenderCorpName", TenderCorpName);
			json.put("TenderCorpCode", TenderCorpCode);
			json.put("hasAttachmentSet", true);
			
			JSONObject dataJson1 = new JSONObject();
			dataJson1.put("userName", "jnspj_open_api");
			dataJson1.put("password", "D01E727B");
			dataJson1.put("grantType", "password");
			dataJson1.put("clientId", "jnspj_open_api");
			dataJson1.put("clientSecret", "jnspj_open_api");
			String tokenresult = HttpUtil.doPostJson(zb_url + "/api/tokenauth", dataJson1.toString(), null);
			
			JSONObject token = JSONObject.parseObject(tokenresult);
			JSONArray resultlist = new JSONArray();
			
			if (StringUtil.isBlank(token.getString("accessToken"))) {
				return JsonUtils.zwdtRestReturn("0", "用户认证登录失败！", dataJson.toString());
			}
			
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Bearer "+ token.getString("accessToken"));
			String result = HttpUtil.doPostJson(zb_url + "/api/services/app/constructionOpenData/GetResultNotificationList", json.toString(), headers);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				if ("true".equals(jsonresult.getString("success"))) {
					JSONObject results = jsonresult.getJSONObject("result");
					JSONArray items = results.getJSONArray("items");
					for (int i=0;i<items.size();i++) {
						JSONObject tttt = items.getJSONObject(i);
						JSONObject attachmentSet = tttt.getJSONObject("attachmentSet");
						JSONArray attachments =  attachmentSet.getJSONArray("attachments");
						for (int j=0;j<attachments.size();j++) {
							JSONObject JSONObject2 = new JSONObject();
							JSONObject attachment = attachments.getJSONObject(i);
							JSONObject2.put("fileName", attachment.getString("attachmentFileName"));
							JSONObject2.put("url", attachment.getString("url"));
							resultlist.add(JSONObject2);
						}
						
					}
					
					log.info("=======结束调用getZbDetail接口=======");
					return JsonUtils.zwdtRestReturn("1", "获取招标通知书信息成功", resultlist.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "获取招标通知书信息失败", resultlist.toString());
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "新增企业信息接口失败", resultlist.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getZbDetail接口参数：params【" + params + "】=======");
			log.info("=======getZbDetail异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取招标通知书信息失败：" + e.getMessage(), "");
		}
	}
	
	
	
}
