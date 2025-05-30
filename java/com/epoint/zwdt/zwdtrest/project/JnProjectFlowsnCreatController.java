package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

@RestController
@RequestMapping("/jnflowsn")
public class JnProjectFlowsnCreatController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
    private IJNAuditProject iJNAuditProject;
	
	

	@RequestMapping(value = "/createReceiveNum", method = RequestMethod.POST)
	public String createReceiveNum(@RequestBody String params) {
		try {
			log.info("=======开始调用createReceiveNum接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String token = jsonObject.getString("token");
			JSONObject datajson = new JSONObject();
			if (ZwdtConstant.SysValidateData.equals(token)) {
				String unid = obj.getString("unid");
				AuditTask auditTask = iJNAuditProject.getAuditTaskByUnid(unid);
				if (auditTask != null) {
					String shenpilb = auditTask.getShenpilb();
					if (StringUtil.isBlank(shenpilb)) {
						shenpilb = "99";
					}
					
					Record rec = iJNAuditProject.getMaxZjNum("jnprojectnum");
		     		Integer maxnum = rec.getInt("maxnum");
		     		maxnum = maxnum + 1;
		     		iJNAuditProject.UpdateMaxZjNum(String.valueOf(maxnum), "jnprojectnum");
		     		String num = String.valueOf(maxnum);
		     		if (num.length() == 1) {
		     			num = "000000" + num;
		     		}
		     		else if (num.length() == 2) {
		     			num = "00000" + num;
		     		}
		     		else if (num.length() == 3) {
		     			num = "0000" + num;
		     		}
		     		else if (num.length() == 4) {
		     			num = "000" + num;
		     		}
		     		else if (num.length() == 5) {
		     			num = "00" + num;
		     		}
		     		else if (num.length() == 6) {
		     			num = "0" + num;
		     		}
		     		
		     		String flowsn = "0808"+ shenpilb+EpointDateUtil.convertDate2String(new Date(), "yy")+num;
		     		datajson.put("flowsn", flowsn);
		     		
		     		return JsonUtils.zwdtRestReturn("1", "证照编号获取成功" , datajson.toString());
				}else {
					return JsonUtils.zwdtRestReturn("0", "未获取到对应的事项信息" , "");
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "未获取token信息" , "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======createReceiveNum接口参数：params【" + params + "】=======");
			log.info("=======createReceiveNum异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取证照列表失败：" + e.getMessage(), "");
		}
	}
	
	

}
