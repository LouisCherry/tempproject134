
package com.epoint.yjs.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.xmz.sqsb.api.ISqsbService;
import com.epoint.yjs.yjszn.api.IYjsZnService;
import com.epoint.yjs.yjszn.api.entity.YjsZn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/yjs")
public class YjsController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	 @Autowired
	 private IAttachService attachService;
	 
	 @Autowired
     private IAuditSpBusiness auditSpBusiness;
	 
	 @Autowired
     private IAuditSpTask auditSpTaskService;
	 
	 @Autowired
     private IAuditSpBasetaskR auditSpBasetaskRService;
	 
	 @Autowired
     private IAuditSpBasetask auditSpBasetaskService;
	 /**
	  * 双全双百service
	  */
	 @Autowired
	 private ISqsbService sqsbService;

	 @Autowired
	 private IYjsZnService iYjsZnService;
	 
	/**
	 * 高效办成一件事重点事项列表
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getBusinessListAsFourty", method = RequestMethod.POST)
	public String getBusinessListAsFourty(@RequestBody String params) {
		try {
			log.info("=======开始调用getBusinessListAsFourty接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getBusinessListAsFourty入参："+jsonObject);
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			String areacode = jsonObject1.getString("areacode");
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("issqlb", "40");
			if(StringUtils.isNotBlank(areacode)){
				sql.eq("areacode", areacode);
			}
            String orderCloumn = "ordernumber";
            String ordertype ="asc";
			sql.setSelectFields("rowguid,businessname,logoattachguid,ywlabel");
            List<AuditSpBusiness> auditSpBusinessList = auditSpBusiness.getAuditSpBusinessPageData(sql.getMap(), 0, 100, orderCloumn, ordertype).getResult().getList();
            JSONObject result = new JSONObject();
            result.put("list", auditSpBusinessList);
            log.info("=======结束调用getBusinessListAsFourty接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取主题信息成功" , result.toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getAccessToken异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 根据辖区以及指南类型查找指南清单
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getYjsZhinanByAreacodeAndType", method = RequestMethod.POST)
	public String getYjsZhinanByAreacodeAndType(@RequestBody String params) {
		try {
			log.info("=======开始调用getYjsZhinanByAreacodeAndType接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getYjsZhinanByAreacodeAndType入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			String areacode = jsonObject1.getString("areacode");
			String type = jsonObject1.getString("type");
			JSONObject result = new JSONObject();
			if(StringUtil.isNotBlank(areacode) && StringUtil.isNotBlank(type)) {
				com.epoint.core.utils.sql.SqlConditionUtil sql = new com.epoint.core.utils.sql.SqlConditionUtil();
            	sql.eq("type", type);
				sql.eq("areacode", areacode);
				sql.setOrderDesc("ordernum");
				List<YjsZn> yjszns =iYjsZnService.findList(sql.getMap());
			    result.put("list", yjszns);
				log.info("=======结束调用getYjsZhinanByAreacodeAndType=======");
				return JsonUtils.zwdtRestReturn("1", "获取成功", result.toString());
			}
			else {
			    return JsonUtils.zwdtRestReturn("0", "入参缺少！" , "");
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getYjsZhinanByAreacodeAndType接口参数：params【" + params + "】=======");
			log.info("=======getYjsZhinanByAreacodeAndType异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询指南信息接口失败：" + e.getMessage(), "");
		}
	}


	/**
	 * 根据辖区以及指南类型查找指南清单
	 *
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getYjsZhinanByAreacodeAndType1", method = RequestMethod.POST)
	public String getYjsZhinanByAreacodeAndType1(@RequestBody String params) {
		try {
			log.info("=======开始调用getYjsZhinanByAreacodeAndType接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getYjsZhinanByAreacodeAndType入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			JSONObject jsonObject1 = jsonObject.getJSONObject("params");
			String pagesize = jsonObject1.getString("pagesize");
			String currentpage = jsonObject1.getString("currentpage");
			String areacode = jsonObject1.getString("areacode");
			String type = jsonObject1.getString("type");
			JSONObject result = new JSONObject();
			if(StringUtil.isNotBlank(areacode) && StringUtil.isNotBlank(type)) {
				com.epoint.core.utils.sql.SqlConditionUtil sql = new com.epoint.core.utils.sql.SqlConditionUtil();
				sql.eq("type", type);
				sql.eq("areacode", areacode);
				sql.setOrderDesc("ordernum");
				int firstResult = -1;
				int maxResults = -1;
				if (StringUtil.isNotBlank(pagesize) && StringUtil.isNotBlank(currentpage)) {
					firstResult = Integer.parseInt(pagesize) * Integer.parseInt(currentpage);
					maxResults = Integer.parseInt(pagesize);
				}
				List<YjsZn> yjszns =iYjsZnService.findList(sql.getMap(),firstResult, maxResults);
				List<Object> params1 = new ArrayList<>();
				sql.setSelectFields("count(1)");
				String sqlcount = new SqlHelper().getSqlComplete(YjsZn.class, sql.getMap(), params1);
				int  total= iYjsZnService.countYjsZn(sqlcount, params1.toArray());
				result.put("list", yjszns);
				result.put("total", total);
				log.info("=======结束调用getYjsZhinanByAreacodeAndType=======");
				return JsonUtils.zwdtRestReturn("1", "获取成功", result.toString());
			}
			else {
				return JsonUtils.zwdtRestReturn("0", "入参缺少！" , "");
			}


		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getYjsZhinanByAreacodeAndType接口参数：params【" + params + "】=======");
			log.info("=======getYjsZhinanByAreacodeAndType异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询指南信息接口失败：" + e.getMessage(), "");
		}
	}
	

	
}
