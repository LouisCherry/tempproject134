
package com.epoint.xmz.jnaicpy.rest;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditqueue.auditznsbsample.inter.IAuditZnsbSample;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.jnzwdt.auditproject.jnaicpy.api.IJnAiCpyService;
import com.epoint.jnzwdt.auditproject.jnbuildpart.api.IJnBuildPartService;
import com.epoint.newshow2.api.Newshow2Service;
import com.epoint.wechat.project.api.IWeiChatProjectService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;

@RestController
@RequestMapping("/jnprojectaicpy")
public class JnProjectAiCpyController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	
	/**
	 * 提交成品油变更的办件数据推送到
	 * 
	 * @param params
	 *            接口的入参
	 */
	@RequestMapping(value = "/submitJnAiCpy", method = RequestMethod.POST)
	public String submitJnAiCpy(@RequestBody String params) {
		try {
			log.info("=======开始调用submitJnAiCpy接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject paramss = JSONObject.parseObject(params);
			JSONObject jsonObject = paramss.getJSONObject("params");
			log.info("提交成品油入参为："+jsonObject.toString());
			String bghitemname = jsonObject.getString("bghitemname");
			String bghlegal = jsonObject.getString("bghlegal");
			String bgqitemname = jsonObject.getString("bgqitemname");
			String bgqlegal = jsonObject.getString("bgqlegal");
			String certnum = jsonObject.getString("certnum");
			String areacode = jsonObject.getString("areacode");
			String address = jsonObject.getString("address");
			JSONObject json1 = new JSONObject();
			json1.put("username", "api_jining");
			json1.put("password", "12345678");
			String result1 = HttpUtil.doPostJson("http://oil.sdcom.gov.cn:80/api/v1/admin/auth/login", json1.toString());
			if (StringUtil.isNotBlank(result1)) {
				JSONObject jsons = JSON.parseObject(result1);
				String code = jsons.getString("code");
				if ("200".equals(code)) {
					JSONObject data = jsons.getJSONObject("data");
					String tokens = data.getString("token");
					JSONObject json = new JSONObject();
					json.put("address", address);
		      	    json.put("addressCode", areacode);
		      	    json.put("changeLegal", bghlegal);
		      	    json.put("changeName", bghitemname);
		      	    json.put("originalLegal", bgqlegal);
		      	    json.put("originalName", bgqitemname);
		      	    json.put("regCode", certnum);
					Map<String,String> headers = new HashMap<String,String>();
					headers.put("Authorization", tokens);
					String result = com.epoint.cert.commonutils.HttpUtil.doPostJson("http://oil.sdcom.gov.cn:80/api/v4/report/change", json.toString(), headers);
					JSONObject jsont = JSON.parseObject(result);
					log.info("提交成品油变更事项接口成功："+jsont.toString());
					if ("200".equals(jsont.getString("code"))) {
						return JsonUtils.zwdtRestReturn("1", "提交成品油变更事项成功", result);
					}else {
						return JsonUtils.zwdtRestReturn("0", "提交成品油变更事项失败", result);
					}
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "提交成品油变更事项失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======submitJnAiCpy接口参数：params【" + params + "】=======");
			log.info("=======submitJnAiCpy异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "提交成品油变更事项失败：" + e.getMessage(), "");
		}
		return params;
	}

	

}
