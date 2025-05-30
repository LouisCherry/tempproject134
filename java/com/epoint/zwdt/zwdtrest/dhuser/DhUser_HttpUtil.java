package com.epoint.zwdt.zwdtrest.dhuser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.hanweb.common.util.security.SecurityUtil;

/**
 * @作者 zhaoy
 * @version [版本号, 2019年1月10日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DhUser_HttpUtil {
	/**
	 * 数据库操作DAO
	 */
	// 审批库
	protected ICommonDao commonDao;

	transient static Logger log = LogUtil.getLog(DhUser_HttpUtil.class);

	/**
	 * 
	 * [获取大汉get接口数据，封装成JSONObject返回] [功能详细描述]
	 * 
	 * @param url
	 * @return
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static Object getInfoByGet(String url, String params, String jsonparam) {
		Object rtnjson = new Object();
		try {
			HttpClient client = new HttpClient();
			client.getParams().setContentCharset("utf-8");
			url = url + "?" + params + URLEncoder.encode(jsonparam, "utf-8");
			log.info("【获取get接口数据】>>>>" + url);
			GetMethod getMethod = new GetMethod(url);
			client.executeMethod(getMethod);
			InputStream inputStream = getMethod.getResponseBodyAsStream();
			rtnjson = JSON.parse(inputStream2String(inputStream));
			return rtnjson;
		} catch (Exception e) {
			log.info("【获取get接口数据异常】>>>>" + url + e.getMessage());
		}
		return null;
	}

	public static Object getTicketByPwd(String url, String loginname, String password, String urlparams,
			String appword) {
		Object obj1 = null;
		JSONObject params = new JSONObject();
		params.put("loginname", loginname);
		params.put("password", password);
		String strparam = SecurityUtil.AESEncode(params.toJSONString(), appword);
		obj1 = getInfoByGet(url, urlparams + "&servicename=userValidate&params=", strparam);
		JSONObject tokenObject = JSONObject.parseObject(obj1.toString());
		// 大汉用户认证之后如若帐号密码有问题则需要返回相关提示
		if (!tokenObject.isEmpty() && !"000000".equals(tokenObject.getString("retcode"))) {
			String errormsg = tokenObject.getString("msg");
			return JsonUtils.zwdtRestReturn("0", "errormsg>>ticketValidate" + errormsg, "");
		}
		log.info("obj1>>>" + tokenObject.toJSONString());
		return obj1;
	}

	public static String getDHtoken(String url, String ticket, String decodekey, String urlparams) {
		String token = "";
		if (StringUtil.isNotBlank(ticket)) {
			Object obj1 = null;
			JSONObject params = new JSONObject();
			params.put("ticket", ticket);
			obj1 = getInfoByGet(url, urlparams + "&servicename=ticketValidate&params=", params.toJSONString());
			JSONObject tokenObject = JSONObject.parseObject(obj1.toString());
			// 大汉用户认证之后如若帐号密码有问题则需要返回相关提示
			if (!tokenObject.isEmpty() && !"000000".equals(tokenObject.getString("retcode"))) {
				String errormsg = tokenObject.getString("msg");
				return "errormsg>>ticketValidate" + errormsg;
			}
			log.info("obj2>>>" + tokenObject.toJSONString());
			String datatoken = tokenObject.getString("data");
			tokenObject = JSONObject.parseObject(datatoken);
			token = tokenObject.getString("token");
		}
		return token;
	}

	public static Object getUserinfoByDhrest(String url, String token, String decodekey, String urlparams,
			boolean hasticket) throws UnsupportedEncodingException {
		Object obj1 = null;
		JSONObject params = new JSONObject();
		// 大汉用户认证之后如若帐号密码有问题则需要返回相关提示
		if (hasticket) {
			params.put("ticket", token);
			obj1 = getInfoByGet(url, urlparams + "&servicename=ticketValidate&params=", params.toJSONString());
			JSONObject tokenObject = JSONObject.parseObject(obj1.toString());
			if (!tokenObject.isEmpty() && !"000000".equals(tokenObject.getString("retcode"))) {
				String errormsg = tokenObject.getString("msg");
				return JsonUtils.zwdtRestReturn("0", "errormsg>>ticketValidate" + errormsg, "");
			}
			log.info("obj2>>>" + tokenObject.toJSONString());
			token = tokenObject.getString("data");
			tokenObject = JSONObject.parseObject(token);
			token = tokenObject.getString("token");
			if (StringUtil.isBlank(token)) {
				return JsonUtils.zwdtRestReturn("0", "errormsg>>ticketValidate", tokenObject.toJSONString());
			}
		}
		// 令牌获取用户uuid返回值
		Object obj2 = null;
		// 获取用户详情
		String dhusertoken = "{'token':'" + token + "'}";
		obj2 = getInfoByGet(url, urlparams + "&servicename=findUserByToken&params=", dhusertoken);
		// 用户uuid解密数据
		Object obj3 = null;
		JSONObject userObject = JSONObject.parseObject(obj2.toString());
		if (!userObject.isEmpty() && !"000000".equals(userObject.getString("retcode"))) {
			String errormsg = userObject.getString("msg");
			return JsonUtils.zwdtRestReturn("0", "errormsg>>findUserByToken" + errormsg, "");
		}
		log.info("obj3>>>" + userObject.toJSONString());
		String userdata = userObject.getString("data");
		String dhSM2decodetoken = "{'decodekey':'" + decodekey + "','decodetext':'" + userdata + "'}";
		obj3 = getInfoByGet(url, urlparams + "&servicename=SM2decode&params=", dhSM2decodetoken);
		// 解密uuid
		Object obj4 = null;
		userObject = JSONObject.parseObject(obj3.toString());
		if (!userObject.isEmpty() && !"000000".equals(userObject.getString("retcode"))) {
			String errormsg = userObject.getString("msg");
			return JsonUtils.zwdtRestReturn("0", "errormsg>>SM2decode" + errormsg, "");
		}
		log.info("obj4>>>" + userObject.toJSONString());
		String useruuid = userObject.getString("data");
		userObject = JSONObject.parseObject(useruuid);
		dhusertoken = "{'token':'" + token + "','uuid':'" + userObject.getString("uuid") + "'}";
		obj4 = getInfoByGet(url, urlparams + "&servicename=findUserByTokenAndUuid&params=", dhusertoken);
		// 用户详细解密数据
		Object obj5 = null;
		userObject = JSONObject.parseObject(obj4.toString());
		if (!userObject.isEmpty() && !"000000".equals(userObject.getString("retcode"))) {
			String errormsg = userObject.getString("msg");
			return JsonUtils.zwdtRestReturn("0", "errormsg>>indUserByTokenAndUuid" + errormsg, "");
		}
		log.info("obj5>>>" + userObject.toJSONString());
		userdata = userObject.getString("data");

		dhSM2decodetoken = "{'decodekey':'" + decodekey + "','decodetext':'" + userdata + "'}";
		obj5 = getInfoByGet(url, urlparams + "&servicename=SM2decode&params=", dhSM2decodetoken);

		String companydata = "";
		JSONObject companyjson = new JSONObject();
		Object obj6 = getInfoByGet(url, urlparams + "&servicename=findAllCorporationByTokenAndUuid&params=",
				dhusertoken);
		Object obj7 = null;
		if (obj6 != null) {
			companyjson = JSONObject.parseObject(obj6.toString());
			if (!companyjson.isEmpty() && !"000000".equals(companyjson.getString("retcode"))) {
				String errormsg = companyjson.getString("msg");
				return JsonUtils.zwdtRestReturn("0", "errormsg>>findCorporationByTokenAndUuid" + errormsg, "");
			}
			// log.info("obj7>>>"+companyjson.toJSONString());
			int length = companyjson.toJSONString().length();
			log.info("obj7>>>企业返回数据大小：" + length);
			companydata = companyjson.getString("data");
			dhSM2decodetoken = "{'decodekey':'" + decodekey + "','decodetext':'" + companydata + "'}";
			try {
				obj7 = getInfoByGet(url, urlparams+"&servicename=SM2decode&params=",dhSM2decodetoken);
				//obj7 = new String(SM2Utils.decrypt(Util.hexToByte(decodekey), Util.hexToByte(companydata)));
				//obj7 = new String(decrypt(Util.hexToByte(decodekey), Util.hexToByte(companydata)));
				System.err.println(obj7.toString());
			} catch (Exception e) {
				e.printStackTrace();
				log.info("obj7>>>数据量过大" + companyjson.toJSONString());
			}finally {
				log.info("decrypt" + companyjson.toJSONString());
			}
		}
		if (obj7 != null) {
			return obj5 + "#COMPANY#" + obj7;
		}
		return obj5;
	}
	

	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString("UTF-8");
	}

	public static String getTicketByRefreshToken(String url, String urlparams, String proxyapp) {
		// 2 初始化session
		ZwdtUserSession zwdtusersession = ZwdtUserSession.getInstance("");
		String token = zwdtusersession.getDhtoken();
		// 根据令牌获取第三方接口资源
		Object obj2 = null;
		String dhusertoken = "{'token':'" + token + "'," + "'proxyapp':'" + proxyapp + "'}";
		obj2 = getInfoByGet(url, urlparams + "&servicename=generateTicket&params=", dhusertoken);
		JSONObject userObject = JSONObject.parseObject(obj2.toString());
		if (!userObject.isEmpty() && !"000000".equals(userObject.getString("retcode"))) {
			String errormsg = userObject.getString("msg");
			return JsonUtils.zwdtRestReturn("0", "errormsg>>generateTicket" + errormsg, "");
		}
		log.info("generateTicket>>>" + userObject.toJSONString());
		String userdata = userObject.getString("data");

		return userdata;
	}

}
