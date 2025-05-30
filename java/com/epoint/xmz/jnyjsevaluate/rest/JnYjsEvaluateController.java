	
package com.epoint.xmz.jnyjsevaluate.rest;

import java.lang.invoke.MethodHandles;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.xmz.jnyjsevaluate.api.IJnYjsEvaluateService;
import com.epoint.xmz.jnyjsevaluate.api.entity.JnYjsEvaluate;

import cn.hutool.core.lang.UUID;

@RestController
@RequestMapping("/jnyjsevaluate")
public class JnYjsEvaluateController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private IAuditSpBusiness iAuditSpBusiness;
	
	@Autowired
	private IJnYjsEvaluateService iJnYjsEvaluateService;
	
	/**
	 * 新增申请单接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addevaluate", method = RequestMethod.POST)
	public String addEvaluate(@RequestBody String params) {
        try {
            log.info("=======开始调用addEvaluate接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String businessguid = obj.getString("businessguid");
                
                String popupitem = obj.getString("popupitem");
                String conditionitems = obj.getString("conditionitems");
                String reasontext = obj.getString("reasontext");
                String areacode = obj.getString("areacode");
                // 2、获取主题基本信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                        .getResult();
                if (auditSpBusiness != null) {
                	JnYjsEvaluate evaluate = new JnYjsEvaluate();
                	evaluate.setRowguid(UUID.randomUUID().toString());
                	evaluate.setOperatedate(new Date());
                	evaluate.setBusinessguid(businessguid);
                	evaluate.setBusinessname(auditSpBusiness.getBusinessname());
                	evaluate.setAreacode(areacode);
                	evaluate.setLabel(conditionitems);
                	evaluate.setReason(reasontext);
                	evaluate.setSatisfaction(popupitem);
                	iJnYjsEvaluateService.insert(evaluate);
                	return JsonUtils.zwdtRestReturn("1", "评价成功！", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "主题不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======addEvaluate接口参数：params【" + params + "】=======");
            log.info("=======addEvaluate异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "主题获取失败：" + e.getMessage(), "");
        }
    }
	
	
	
	

}
