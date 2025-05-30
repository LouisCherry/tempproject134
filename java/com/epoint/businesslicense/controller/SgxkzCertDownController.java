package com.epoint.businesslicense.controller;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.zwdt.util.TARequestUtil;

/**
 * 一业一证业务接口
 * @description
 * @author shibin
 * @date  2020年5月18日 上午11:31:17
 */
@RestController
@RequestMapping("/sgxkzCert")
public class SgxkzCertDownController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 基本信息API
     */
    @Autowired
    private IBusinessLicenseBaseinfo baseinfoService;
    
    /**
     * 扩展信息API
     */
    @Autowired
    private ICertInfo certInfoService;

    @RequestMapping(value = "/getCertinfoGhxkz", method = RequestMethod.POST)
    public String getCertinfoGhxkz(@RequestBody String params) {
    	
    	log.info("=======开始调用getCertinfoGhxkz接口======="
    			+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
    	log.info("=======params=======" + params);
    	JSONObject dataJson = new JSONObject();
    	JSONObject param = new JSONObject();
    	JSONObject sumbit = new JSONObject();
    	try {
    		JSONObject jsonObject = JSONObject.parseObject(params);
    		String token = jsonObject.getString("token");
    		JSONObject obj = (JSONObject) jsonObject.get("params");
    		String certno = obj.getString("certno");
    		String xmmc = obj.getString("xmmc");
    		String creditcode = obj.getString("creditcode");
    		
    		if (ZwdtConstant.SysValidateData.equals(token)) {
    			if (StringUtil.isNotBlank(creditcode)) {
    				SqlConditionUtil sql = new SqlConditionUtil();
        			sql.eq("ISHISTORY", "0");
        			sql.eq("status", "10");
        			sql.eq("certownerno", creditcode);
        			sql.eq("certname", "建设用地规划许可证");
        			List<CertInfo> certs = certInfoService.getListByCondition(sql.getMap());
        			if (!certs.isEmpty()) {
        				CertInfo certInfo = certs.get(0);
        				param.put("certinfoguid", certInfo.getRowguid());
        				param.put("tycertcatcode", certInfo.getTycertcatcode());
            			log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
        			}
    			}
    			param.put("xmmc", xmmc);
    			param.put("certno", certno);
    			
    			sumbit.put("params", param);
    			
    			//发送请求 
    			String note = TARequestUtil.sendPost(
    					"http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionGhxkz",
    					sumbit.toJSONString(), "", "");
    			log.info("返回的证照信息："+note);
    			JSONObject object = JSONObject.parseObject(note);
    			JSONObject object2 = object.getJSONObject("custom");
    			JSONObject object3 = object2.getJSONObject("certinfoextension");
    			
    			String certrowguid = object3.getString("certinfoguid");
    			
    			if (StringUtil.isBlank(certrowguid)) {
    				return JsonUtils.zwdtRestReturn("0", "证照信息查询为空！", "");
    			}
    			
                CertInfo certInfo = certInfoService.getCertInfoByRowguid(certrowguid);
                
                if (certInfo == null) {
    				return JsonUtils.zwdtRestReturn("0", "证照不存在！", "");
    			}
                
                if ("建设用地规划许可证".equals(certInfo.getCertname())) {
                	dataJson.put("certinfoextension", object3);
                }
    			
    			
    			log.info("=======结束调用getCertinfoGhysjyxkzList接口======="
    					+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
    			return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
    		}
    		else {
    			return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
    		}
    	}
    	catch (Exception e) {
    		log.info("=======结束调用getCertinfoGhxkz接口======="
    				+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
    		log.info("=======getCertinfoGhxkz异常信息：======" + e.getMessage());
    		return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
    	}
    }
    
    
    @RequestMapping(value = "/getCertinfoSgxkz", method = RequestMethod.POST)
    public String getCertinfoSgxkz(@RequestBody String params) {
    	
    	log.info("=======开始调用getCertinfoSgxkz接口======="
    			+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
    	log.info("=======params=======" + params);
    	JSONObject dataJson = new JSONObject();
    	JSONObject param = new JSONObject();
    	JSONObject sumbit = new JSONObject();
    	try {
    		JSONObject jsonObject = JSONObject.parseObject(params);
    		String token = jsonObject.getString("token");
    		JSONObject obj = (JSONObject) jsonObject.get("params");
    		String certno = obj.getString("certno");
    		String xmmc = obj.getString("xmmc");
    		String creditcode = obj.getString("creditcode");
    		
    		if (ZwdtConstant.SysValidateData.equals(token)) {
    			if (StringUtil.isNotBlank(creditcode)) {
    				SqlConditionUtil sql = new SqlConditionUtil();
        			sql.eq("ISHISTORY", "0");
        			sql.eq("status", "10");
        			sql.eq("certownerno", creditcode);
        			sql.eq("certname", "建筑工程施工许可证");
        			List<CertInfo> certs = certInfoService.getListByCondition(sql.getMap());
        			if (!certs.isEmpty()) {
        				CertInfo certInfo = certs.get(0);
        				param.put("certinfoguid", certInfo.getRowguid());
        				param.put("tycertcatcode", certInfo.getTycertcatcode());
            			log.info("=======tycertcatcode=======" + certInfo.getTycertcatcode());
        			}
    			}
    			
    			param.put("xmmc", xmmc);
    			param.put("certno", certno);
    			sumbit.put("params", param);
    			
    			//发送请求 
    			String note = TARequestUtil.sendPost(
    					"http://172.16.53.7:8080/jnzwfw/rest/tacertInfo/getCertInfoExtensionSgxkz",
    					sumbit.toJSONString(), "", "");
    			log.info("返回的证照信息："+note);
    			JSONObject object = JSONObject.parseObject(note);
    			JSONObject object2 = object.getJSONObject("custom");
    			JSONArray object3 = object2.getJSONArray("certinfoextensions");
    			 JSONArray jsonArray = new JSONArray();
    			 
    			for (int i=0;i<object3.size();i++) {
    				JSONObject object4 = object3.getJSONObject(i);
    				String certrowguid = object4.getString("certinfoguid");
    				log.info("sgxkz:"+certrowguid);
        			if (StringUtil.isBlank(certrowguid)) {
        				continue;
        			}
        			CertInfo certInfo = certInfoService.getCertInfoByRowguid(certrowguid);
                    if (certInfo == null) {
        				continue;
        			}
                    
                    
                    log.info("certInfo:"+certInfo);
                    if ("建筑工程施工许可证".equals(certInfo.getCertname())) {
                   	 List<Record> attachlist = baseinfoService.findMaterialsAndCertSgxkz(certInfo.getCertcliengguid());
                   	 log.info("attachlist:"+attachlist);
                        for(Record rec : attachlist) {
                        	 Map<String, String> materguidurl = new HashMap<String, String>();
                           	 materguidurl.put("attachguid", rec.getStr("attachguid"));
                           	 materguidurl.put("attachname", rec.getStr("attachname"));
                           	 jsonArray.add(materguidurl);
                        }
                   }
    			}
    			dataJson.put("materials", jsonArray);
    			
    			log.info("=======结束调用getCertinfoGhysjyxkzList接口======="
    					+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
    			return JsonUtils.zwdtRestReturn("1", "获取信息成功！", dataJson.toJSONString());
    		}
    		else {
    			return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
    		}
    	}
    	catch (Exception e) {
    		log.info("=======结束调用getCertinfoSgxkz接口======="
    				+ EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
    		log.info("=======getCertinfoSgxkz异常信息：======" + e.getMessage());
    		return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
    	}
    }
    
    
}
