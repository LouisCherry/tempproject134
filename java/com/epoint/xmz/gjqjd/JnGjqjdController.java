
package com.epoint.xmz.gjqjd;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.utils.config.ConfigUtil;

@RestController
@RequestMapping("/jngjqjd")
public class JnGjqjdController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	
	
	public static final String token_url= ConfigUtil.getConfigValue("hcp", "gjtokenurl");
	public static final String share_url= ConfigUtil.getConfigValue("hcp", "gjshareurl");
	public static final String grant_type = ConfigUtil.getConfigValue("hcp", "gjgranttype");
	public static final String client_id = ConfigUtil.getConfigValue("hcp", "gjclientid");
	public static final String client_secret = ConfigUtil.getConfigValue("hcp", "gjclientsecret");

	
	/**
	 * 获取大数据应用的token
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/gettoken", method = RequestMethod.POST)
	public String getToken(@RequestBody String params) {
		try {
			log.info("=======开始调用大数据getToken接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject result = new JSONObject();
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				Map<String, String> bodyRequest = new HashMap<String, String>();
				bodyRequest.put("grant_type", grant_type);
				bodyRequest.put("client_id", client_id);
				bodyRequest.put("client_secret", client_secret);
				String res = HttpUtils.postHttp(token_url, bodyRequest);
				result.put("result",  JSONObject.parseObject(res));
				log.info("=======结束调用token接口=======");
				return JsonUtils.zwdtRestReturn("1", "获取token成功", result.toString());
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getToken接口参数：params【" + params + "】=======");
			log.info("=======getToken异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取token失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 工业设计中心企业名单目录信息查询
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getsharegysjzx", method = RequestMethod.POST)
	public String getsharegysjzx(@RequestBody String params) {
		try {
			log.info("=======开始调用大数据getsharegysjzx接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			JSONObject result = new JSONObject();
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
                String accesstoken = obj.getString("accesstoken");
                String urlname = obj.getString("urlname");
                String pageindex = obj.getString("pageindex");
				Map<String, String> bodyRequest = new HashMap<String, String>();
				if (pageindex == null) {
					pageindex = "1";
				}
				bodyRequest.put("pageIndex", pageindex);
//				bodyRequest.put("client_id", client_id);
//				bodyRequest.put("client_secret", client_secret);
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Authorization", "Bearer " + accesstoken);
				
				String res = HttpUtils.postHttp(share_url+urlname, bodyRequest, headers);
				if ("gysjzx".equals(urlname)) {
					res = res.replace("序号", "xuhao").replace("批复时间", "pifushijian")
							.replace("批复机关", "pifujiguan").replace("所在地", "suozaidi").replace("平台名称", "pingtaimingcheng").replace("所在单位", "suoziadanwei");
				}else if("JNSBWGYLB".equals(urlname)){
					res = res.replace("概述", "xuhao").replace("是否免费开放", "pifushijian")
							.replace("联系（预约）电话", "pifujiguan").replace("博物馆名称", "suozaidi").replace("地址", "pingtaimingcheng").replace("性质", "suoziadanwei");
					
				}else if("jnscgjcfxx".equals(urlname)){
				res = res.replace("地方编码", "xuhao").replace("处罚名称", "pifushijian")
						.replace("处罚机关", "pifujiguan").replace("处罚决定日期", "suozaidi").replace("处罚依据", "pingtaimingcheng").replace("处罚文书号", "suoziadanwei");
				
				}
				else if("dxtyydxhxx".equals(urlname)){
					res = res.replace("序号", "xuhao").replace("协会名称", "pifushijian")
							.replace("注册法人", "pifujiguan").replace("地址", "suozaidi").replace("联系电话", "pingtaimingcheng").replace("INSERT_TIME", "suoziadanwei");
					
				}
				else if("gcjlqyxx".equals(urlname)){
					res = res.replace("企业名称", "xuhao").replace("统一社会信用代码", "pifushijian")
							.replace("资质证书编号", "pifujiguan").replace("证书核发机关", "suozaidi").replace("资质等级", "pingtaimingcheng").replace("证书状态", "suoziadanwei");
					
				}
				else if("jzyqyzzsp".equals(urlname)){
					res = res.replace("项目名称", "xuhao").replace("统一社会信用代码", "pifushijian").replace("法定代表人", "pifujiguan")
							.replace("许可内容", "suozaidi").replace("许可决定日期", "pingtaimingcheng").replace("许可截止日期", "suoziadanwei");
					
				}
				else if("lxsmdjlxx".equals(urlname)){
					res = res.replace("旅行社名称", "xuhao").replace("经营场所", "pifushijian").replace("法定代表人", "pifujiguan")
							.replace("经营范围", "suozaidi").replace("成立日期", "pingtaimingcheng");
					
				}
				else if("JNSMKJBXXB".equals(urlname)){
					res = res.replace("企业名称", "xuhao").replace("企业驻地", "pifushijian").replace("煤矿划分", "pifujiguan")
							.replace("所属集团", "suozaidi").replace("投产时间", "pingtaimingcheng").replace("核定/设计生产能力", "suoziadanwei");
					
				}
				else if("njwxxyglwxwdxx".equals(urlname)){
					res = res.replace("经营者名称", "xuhao").replace("证书号", "pifushijian").replace("维修类别与等级", "pifujiguan")
							.replace("地址", "suozaidi").replace("维修范围", "pingtaimingcheng").replace("到期时间", "suoziadanwei");
					
				}
				else if("szzcxjcgzhjh".equals(urlname)){
					res = res.replace("项目名称", "xuhao").replace("申报年份", "pifushijian").replace("依托单位", "pifujiguan");
					
				}
				else if("MYZYYYMD".equals(urlname)){
					res = res.replace("医院名称", "suozaidi");
					
				}
				else if("spjyxkzxx".equals(urlname)){
					res = res.replace("经营者名称", "suozaidi").replace("经营类别", "pingtaimingcheng").replace("经营场所", "suoziadanwei")
							.replace("经营项目", "pifushijian").replace("有效期至", "pifujiguan");
				}
				else if("SYJYQYXX".equals(urlname)){
					res = res.replace("企业名称", "suozaidi").replace("法人代表", "pingtaimingcheng").replace("经营地址", "suoziadanwei")
							.replace("经营范围", "pifushijian").replace("兽药经营许可证号码", "pifujiguan");
				}
				else if("GJJHBDDYYXX".equals(urlname)){
					res = res.replace("单位名称", "suozaidi").replace("地址", "pingtaimingcheng").replace("电话", "suoziadanwei").replace("手机", "pifujiguan");
					
				}
				else if("GXSQJKZXXX".equals(urlname)){
					res = res.replace("单位", "suozaidi").replace("值班电话", "pingtaimingcheng").replace("传真", "suoziadanwei").replace("地址", "pifushijian");
					
				}
				else if("glyymlxx".equals(urlname)){
					res = res.replace("公立医院名称", "suozaidi").replace("INSERT_TIME", "pingtaimingcheng").replace("所属县（市、区）", "suoziadanwei");
					
				}
				else if("JYZJYQYYLB".equals(urlname)){
					res = res.replace("单位名称", "suozaidi").replace("负责人", "pingtaimingcheng").replace("地址", "suoziadanwei").replace("属地", "pifushijian");
					
				}
				else if("JKFPDD".equals(urlname)){
					res = res.replace("医疗机构名称", "suozaidi").replace("县市区", "pingtaimingcheng");
					
				}
				else if("SQZXMD".equals(urlname)){
					res = res.replace("医疗机构名称", "suozaidi").replace("县市区", "pingtaimingcheng");
					
				}
				else if("sxbzxrxx".equals(urlname)){
					res = res.replace("名称", "suozaidi").replace("执行法院", "pingtaimingcheng").replace("执行依据文号", "suoziadanwei")
							.replace("失信被执行人具体情形", "pifushijian").replace("发布时间", "pifujiguan");
				}
				else if("sgnqhxx".equals(urlname)){
					res = res.replace("河流", "suozaidi").replace("起始断面", "pingtaimingcheng").replace("水功能区名称", "suoziadanwei")
							.replace("水质目标", "pifushijian").replace("终止断面", "pifujiguan");
				}
				else if("jnstylmbfqydwxx".equals(urlname)){
					res = res.replace("单位名称", "suozaidi").replace("办公地址", "pingtaimingcheng").replace("联系人", "suoziadanwei")
							.replace("联系电话", "pifushijian").replace("备注", "pifujiguan");
				}
				else if("tsgmlxx".equals(urlname)){
					res = res.replace("名称", "suozaidi").replace("县区", "pingtaimingcheng").replace("INSERT_TIME", "suoziadanwei");
				}
				else if("WSYMD".equals(urlname)){
					res = res.replace("卫生院名称", "suozaidi").replace("县市区", "pingtaimingcheng");
				}
				else if("shwhstxx".equals(urlname)){
					res = res.replace("队伍名称", "suozaidi").replace("注册组织", "pingtaimingcheng").replace("注册编号", "suoziadanwei")
							.replace("人数", "pifushijian");
				}
				else if("WGHQYXX".equals(urlname)){
					res = res.replace("申请人全称", "suozaidi").replace("产品名称", "pingtaimingcheng").replace("产地地址", "suoziadanwei")
							.replace("生产规模", "pifushijian").replace("证书有效期", "pifujiguan");
				}
				else if("JNSYHBZPFJYQY".equals(urlname)){
					res = res.replace("单位名称", "suozaidi").replace("经营许可范围", "pingtaimingcheng").replace("注册地址", "suoziadanwei")
							.replace("证书编号", "pifushijian").replace("有效期", "pifujiguan");
				}
				else if("yljgjbxx".equals(urlname)){
					res = res.replace("机构名称", "suozaidi").replace("县市区", "pingtaimingcheng").replace("地址", "suoziadanwei")
							.replace("INSERT_TIME", "pifushijian").replace("备注", "pifujiguan");
				}
				else if("yjbncsxx".equals(urlname)){
					res = res.replace("避难场所名称", "suozaidi").replace("地址", "pingtaimingcheng").replace("经度", "suoziadanwei")
							.replace("纬度", "pifushijian").replace("面积（万平方米）", "pifujiguan");
				}
				else if("yeyxx".equals(urlname)){
					res = res.replace("幼儿园名称", "suozaidi").replace("统一社会信用代码（合格证号码）", "pingtaimingcheng").replace("法定代表人（负责人）", "suoziadanwei")
							.replace("地址（住所）", "pifushijian").replace("备注", "pifujiguan");
				}
				
				result.put("result", JSONArray.parseArray(res));
				log.info("=======结束调用getsharegysjzx接口=======");
				return JsonUtils.zwdtRestReturn("1", "获取工业设计中心企业名单目录信息成功", result.toJSONString());
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getsharegysjzx接口参数：params【" + params + "】=======");
			log.info("=======getsharegysjzx异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取工业设计中心企业名单目录信息失败：" + e.getMessage(), "");
		}
	}

	

}
