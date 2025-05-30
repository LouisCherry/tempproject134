package com.epoint.zwdt.zwdtrest.dhuser;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.basic.auditonlineuser.auditonlineverycode.inter.IAuditOnlineVeryCode;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdt.login.dhrsautil.AESUtil;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.util.UserEncodeUtil;

@RestController
@RequestMapping("/jnzwfwWxUser")
public class JnZwfwWxUserRestController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	/**
	 * 用户注册信息API
	 */
	@Autowired
	private IAuditOnlineRegister iAuditOnlineRegister;
	/**
	 * 法定代表人API
	 */
	@Autowired
	private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
	/**
	 * 用户个人信息API
	 */
	@Autowired
	private IAuditOnlineIndividual iAuditOnlineIndividual;
	/**
	 * 系统参数API
	 */
	@Autowired
	private IHandleConfig handleConfig;

	@Autowired
	private IAuditOnlineVeryCode iAuditOnlineVeryCode;

	/**
	 * 微信用户绑定接口
	 * 
	 * @param params 接口的入参
	 * @return
	 */
	@RequestMapping(value = "/wxNewUserBind", method = RequestMethod.POST)
	public String wxForceUserBind(@RequestBody String params) {
		try {
			log.info("=======开始调用wxNewUserBind接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取登录名（手机号码/身份证号码）
				String idnumOrMobile = obj.getString("idnumormobile");
				// 1.2、获取用户微信的唯一标识
				String openId = obj.getString("openid");
				// 1.3、获取框架加密的密码--用于江苏通用版的密码解密
				String encodepassword = obj.getString("encodepassword");
				// 1.4、获取是否使用大汉认证中心
				String isuse = ConfigUtil.getConfigValue("jnlogin", "usedhuser");
				// 1.5、获取SM2加密配置参数
				String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
				// 1.6 获取大汉的token
				String dhtoken = obj.getString("token");
				// 1.7 大汉解密私钥
				String decodekey = ConfigUtil.getConfigValue("jnlogin", "wxappkey");
				// 定义加密方法
				UserEncodeUtil userEncodeUtil = new UserEncodeUtil();
				if (ZwdtConstant.STRING_YES.equals(isuse)) {
					// encodepassword = EncodeUtil.decodeByJs(encodepassword);
					String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
					String appmark = ConfigUtil.getConfigValue("jnlogin", "wxappmark");
					String appword = ConfigUtil.getConfigValue("jnlogin", "wxappword");
					String wangzhi = ConfigUtil.getConfigValue("jnlogin", "dhrest");
					// 通过大汉提供的加密方式进行加密
					String sign = MD5Util.getMD5(appmark + appword + time);
					String urlparams = "appmark=" + appmark + "&time=" + time + "&sign=" + sign;
					Object obj2 = null;
					Object objticket = null;
					// AuthService auth = WebServiceUtil.getService(AuthService.class, wangzhi +
					// "services/WSAuth?wsdl");
					// obj2 = auth.userValidate(appmark, time, sign, idnumOrMobile, enrsapassword,
					// "1");
					// 获取票据
					objticket = DhUser_HttpUtil.getTicketByPwd(wangzhi, idnumOrMobile, encodepassword, urlparams,
							appword);
					JSONObject ticketObject = JSONObject.parseObject(objticket.toString());
					if (ticketObject.isEmpty() || !"000000".equals(ticketObject.getString("retcode"))) {
						return JsonUtils.zwdtRestReturn("0", "获取大汉票据失败！", "");
					} else {
						String datastr = ticketObject.getString("data");
						if (StringUtil.isNotBlank(datastr)) {
							dhtoken = JSON.parseObject(datastr).getString("token");
						}
					}
					// 验证票据
					obj2 = DhUser_HttpUtil.getUserinfoByDhrest(wangzhi, dhtoken, decodekey, urlparams, false);
					String[] resultdata = obj2.toString().split("#COMPANY#");
					JSONObject resultObject = JSONObject.parseObject(resultdata[0]);
					if (resultObject.isEmpty() || !"000000".equals(resultObject.getString("retcode"))) {
						return JsonUtils.zwdtRestReturn("0", "获取大汉用户失败！", "");
					}
					String userdata = resultObject.getString("data");
					resultObject = JSONObject.parseObject(userdata);
					// 获取大汉相关数据
					// 身份证
					String idcard = resultObject.getString("cardid");
					// 登录名
					String loginname = resultObject.getString("loginname");
					// 姓名
					String name = resultObject.getString("name");
					// 手机号
					String mobile = resultObject.getString("mobile");
					idcard = StringUtil.isNotBlank(idcard) ? idcard.toUpperCase() : idcard;
					// 这边需要先判断用户数据有没有存到数据库中去
					AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(mobile)
							.getResult();
					if (auditOnlineRegister == null) {
						String sm2Pwd = "";
						// 判断是否采用sm2加密
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							sm2Pwd = userEncodeUtil.encryption(MD5Util.getMD5(encodepassword), idcard);

						}
						String accountguid = UUID.randomUUID().toString();
						// 3、插入个人基本信息表
						AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
						auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
						auditOnlineIndividual.setAccountguid(accountguid);
						auditOnlineIndividual.setClientname(name);
						auditOnlineIndividual.setIdnumber(idcard);
						auditOnlineIndividual.setContactmobile(mobile);
						auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
						iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
						// 3.1、插入用户注册表
						AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
						auditonlineregister.setRowguid(UUID.randomUUID().toString());
						auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);// 默认自然人
						auditonlineregister.setAccountguid(accountguid);
						// 3.1.1、判断是否使用sm2
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							auditonlineregister.setPassword(sm2Pwd);
						} else {
							auditonlineregister.setPassword(MD5Util.getMD5(encodepassword));
						}
						auditonlineregister.setMobile(mobile);
						auditonlineregister.setUsername(name);
						auditonlineregister.setIdnumber(idcard);
						auditonlineregister.setOpenid(openId);
						auditonlineregister.setLoginid(loginname);
						iAuditOnlineRegister.addRegister(auditonlineregister);
						return JsonUtils.zwdtRestReturn("1", "绑定成功", "");
					} else {
						// 判断是否使用sm2加密
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							auditOnlineRegister.setPassword(userEncodeUtil.encryption(MD5Util.getMD5(encodepassword),
									auditOnlineRegister.getIdnumber().toUpperCase()));
						} else {
							auditOnlineRegister.setPassword(MD5Util.getMD5(encodepassword));
						}
						// 3.4、更新OPENID,不考虑已绑定的情况，因为在上一步骤已经判断过
						auditOnlineRegister.setMobile(mobile);
						auditOnlineRegister.setUsername(name);
						auditOnlineRegister.setIdnumber(idcard);
						auditOnlineRegister.setLoginid(loginname);
						auditOnlineRegister.setOpenid(openId);
						iAuditOnlineRegister.updateRegister(auditOnlineRegister);
						return JsonUtils.zwdtRestReturn("1", "绑定成功", "");
					}
				}
				// 3、用戶綁定
				AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(idnumOrMobile)
						.getResult();
				auditOnlineRegister.setOpenid(openId);
				iAuditOnlineRegister.updateRegister(auditOnlineRegister);
				log.info("=======结束调用wxNewUserBind接口=======");
				return JsonUtils.zwdtRestReturn("1", "绑定成功", "");
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======wxNewUserBind接口参数：params【" + params + "】=======");
			log.info("=======wxNewUserBind异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "微信用户绑定出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 获取小程序用户openid
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getopenid", method = RequestMethod.POST)
	public String getOpenid(@RequestBody String params, HttpServletRequest request) {
		try {
			JSONObject jsonObject = JSONObject.parseObject(params);
			// 1、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				// 1.1、微信标识
				JSONObject json = (JSONObject) jsonObject.get("params");
				String code = json.getString("code");
				String appid = ConfigUtil.getConfigValue("zcwx_appid");
				String secret = ConfigUtil.getConfigValue("zcwx_secret");
				String wxUrl = ConfigUtil.getConfigValue("zcwx_url");
				String tokenUrl = wxUrl + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code
						+ "&grant_type=authorization_code";
				// String oauthResponseText = HttpUtil.doGet(tokenUrl);
				String oauthResponseText = HttpRequestUtils.sendGet(tokenUrl, "");

				JSONObject jo = com.alibaba.fastjson.JSONObject.parseObject(oauthResponseText);
				if (StringUtil.isNotBlank(jo.get("openid")) && StringUtil.isNotBlank(jo.get("session_key"))) {
					dataJson.put("openid", jo.get("openid").toString());

					return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取信息成功！", dataJson.toString());
				} else {
					log.error("=======获取openid失败，getopenid接口参数：params【" + params + "】=======");
					log.error("=======调用微信官方的接口返回：" + oauthResponseText + "=====");
					return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "openid为空！", "");
				}
			} else {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "身份验证失败！", "");
			}
		} catch (Exception e) {
			log.error("=======getopenid接口参数：params【" + params + "】=======");
			log.error("=======getopenid异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取openid失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 邹城小程序微信用户绑定接口
	 * 
	 * @param params 接口的入参
	 * @return
	 */
	@RequestMapping(value = "/zcYkyWxNewUserBind", method = RequestMethod.POST)
	public String zcYkyWxNewUserBind(@RequestBody String params) {
		try {
			log.info("=======开始调用zcYkyWxNewUserBind接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取登录名（手机号码/身份证号码）
				String idnumOrMobile = obj.getString("idnumormobile");
				// 1.2、获取用户微信的唯一标识
				String openId = obj.getString("openid");
				// 1.3、获取框架加密的密码--用于江苏通用版的密码解密
				String encodepassword = obj.getString("encodepassword");
				// 1.4、获取是否使用大汉认证中心
				String isuse = ConfigUtil.getConfigValue("jnlogin", "usedhuser");
				// 1.5、获取SM2加密配置参数
				String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
				// 1.6 获取大汉的token
				String dhtoken = obj.getString("token");
				// 1.7 大汉解密私钥
				String decodekey = ConfigUtil.getConfigValue("jnlogin", "wxappkey");
				// 定义加密方法
				UserEncodeUtil userEncodeUtil = new UserEncodeUtil();
				if (ZwdtConstant.STRING_YES.equals(isuse)) {
					// encodepassword = EncodeUtil.decodeByJs(encodepassword);
					String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
					String appmark = ConfigUtil.getConfigValue("jnlogin", "wxappmark");
					String appword = ConfigUtil.getConfigValue("jnlogin", "wxappword");
					String wangzhi = ConfigUtil.getConfigValue("jnlogin", "dhrest");
					// 通过大汉提供的加密方式进行加密
					String sign = MD5Util.getMD5(appmark + appword + time);
					String urlparams = "appmark=" + appmark + "&time=" + time + "&sign=" + sign;
					Object obj2 = null;
					Object objticket = null;
					// AuthService auth = WebServiceUtil.getService(AuthService.class, wangzhi +
					// "services/WSAuth?wsdl");
					// obj2 = auth.userValidate(appmark, time, sign, idnumOrMobile, enrsapassword,
					// "1");
					// 获取票据
					objticket = DhUser_HttpUtil.getTicketByPwd(wangzhi, idnumOrMobile, encodepassword, urlparams,
							appword);
					JSONObject ticketObject = JSONObject.parseObject(objticket.toString());
					if (ticketObject.isEmpty() || !"000000".equals(ticketObject.getString("retcode"))) {
						return JsonUtils.zwdtRestReturn("0", "获取大汉票据失败！", "");
					} else {
						String datastr = ticketObject.getString("data");
						if (StringUtil.isNotBlank(datastr)) {
							dhtoken = JSON.parseObject(datastr).getString("token");
						}
					}
					// 验证票据
					obj2 = DhUser_HttpUtil.getUserinfoByDhrest(wangzhi, dhtoken, decodekey, urlparams, false);
					String[] resultdata = obj2.toString().split("#COMPANY#");
					JSONObject resultObject = JSONObject.parseObject(resultdata[0]);
					if (resultObject.isEmpty() || !"000000".equals(resultObject.getString("retcode"))) {
						return JsonUtils.zwdtRestReturn("0", "获取大汉用户失败！", "");
					}
					String userdata = resultObject.getString("data");
					resultObject = JSONObject.parseObject(userdata);
					// 获取大汉相关数据
					// 身份证
					String idcard = resultObject.getString("cardid");
					// 登录名
					String loginname = resultObject.getString("loginname");
					// 姓名
					String name = resultObject.getString("name");
					// 手机号
					String mobile = resultObject.getString("mobile");
					idcard = StringUtil.isNotBlank(idcard) ? idcard.toUpperCase() : idcard;
					// 这边需要先判断用户数据有没有存到数据库中去
					AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(mobile)
							.getResult();
					if (auditOnlineRegister == null) {
						String sm2Pwd = "";
						// 判断是否采用sm2加密
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							sm2Pwd = userEncodeUtil.encryption(MD5Util.getMD5(encodepassword), idcard);

						}
						String accountguid = UUID.randomUUID().toString();
						// 3、插入个人基本信息表
						AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
						auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
						auditOnlineIndividual.setAccountguid(accountguid);
						auditOnlineIndividual.setClientname(name);
						auditOnlineIndividual.setIdnumber(idcard);
						auditOnlineIndividual.setContactmobile(mobile);
						auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
						iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
						// 3.1、插入用户注册表
						AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
						auditonlineregister.setRowguid(UUID.randomUUID().toString());
						auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);// 默认自然人
						auditonlineregister.setAccountguid(accountguid);
						// 3.1.1、判断是否使用sm2
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							auditonlineregister.setPassword(sm2Pwd);
						} else {
							auditonlineregister.setPassword(MD5Util.getMD5(encodepassword));
						}
						auditonlineregister.setMobile(mobile);
						auditonlineregister.setUsername(name);
						auditonlineregister.setIdnumber(idcard);
						auditonlineregister.set("zcykyopenid", openId);
						auditonlineregister.setLoginid(loginname);
						iAuditOnlineRegister.addRegister(auditonlineregister);
						return JsonUtils.zwdtRestReturn("1", "绑定成功", "");
					} else {
						// 判断是否使用sm2加密
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							auditOnlineRegister.setPassword(userEncodeUtil.encryption(MD5Util.getMD5(encodepassword),
									auditOnlineRegister.getIdnumber().toUpperCase()));
						} else {
							auditOnlineRegister.setPassword(MD5Util.getMD5(encodepassword));
						}
						// 3.4、更新OPENID,不考虑已绑定的情况，因为在上一步骤已经判断过
						auditOnlineRegister.setMobile(mobile);
						auditOnlineRegister.setUsername(name);
						auditOnlineRegister.setIdnumber(idcard);
						auditOnlineRegister.setLoginid(loginname);
						auditOnlineRegister.set("zcykyopenid", openId);
						iAuditOnlineRegister.updateRegister(auditOnlineRegister);
						return JsonUtils.zwdtRestReturn("1", "绑定成功", "");
					}
				}
				// 3、用戶綁定
				AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(idnumOrMobile)
						.getResult();
				auditOnlineRegister.set("zcykyopenid", openId);
				iAuditOnlineRegister.updateRegister(auditOnlineRegister);
				log.info("=======结束调用zcYkyWxNewUserBind接口=======");
				return JsonUtils.zwdtRestReturn("1", "绑定成功", "");
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======zcYkyWxNewUserBind接口参数：params【" + params + "】=======");
			log.info("=======zcYkyWxNewUserBind异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "微信用户绑定出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 邹城小程序通过OPENID获取用户信息
	 * 
	 * @param params  接口的入参
	 * @param request HTTP请求
	 * @return
	 */
	@RequestMapping(value = "/zcYkyUserDetailByOpenID", method = RequestMethod.POST)
	public String zcYkyUserDetailByOpenID(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用zcYkyUserDetailByOpenID接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取用户微信唯一标识
				String openid = obj.getString("openid");
				if (StringUtil.isBlank(openid)) {
					return JsonUtils.zwdtRestReturn("0", "请传入正确的微信标识", "");
				}
				// 1.2、获取sm2加密配置参数
				String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
				// 2、根据OPENID获取用户信息
				SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
				sqlConditionUtil.eq("zcykyopenid", openid);
				List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
						.selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
				if (auditOnlineRegisters.isEmpty()) {
					return JsonUtils.zwdtRestReturn("0", "用户名不存在或者还未绑定", "");
				}
				// 3、定义返回JSON对象
				JSONObject dataJson = new JSONObject();
				AuditOnlineRegister auditOnlineRegister = auditOnlineRegisters.get(0);
				dataJson.put("username", auditOnlineRegister.getUsername()); // 用户姓名
				dataJson.put("idnum", auditOnlineRegister.getIdnumber()); // 身份证号码
				dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号码
				String refresh_token = auditOnlineRegister.get("zcykyrefreshtoken");
				String loginUrl = ""; // 请求token地址
				String loginResult = ""; // 请求token返回结果
				String urlroot = request.getRequestURL().toString();
				if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("interalIP"))) {
					urlroot = ConfigUtil.getConfigValue("interalIP");
				} else {
					urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
				}
				// 4、判断是否首次登录
				if (StringUtil.isBlank(refresh_token)) {
					// 4.1、如果为空 则请求登录接口获取token
					// 4.1.1、若采用SM2加密，则传入sm2加密标识
					if (StringUtil.isNotBlank(customPwdEncrypt)
							&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
						loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
								+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
								+ "&encrypttype=sm2" + "";
					}
					// 4.1.2、未采用sm2加密方式
					else {
						loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
								+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
								+ "";
					}
					loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
							"application/x-www-form-urlencoded");
					// 4.1.3、解析请求的返回值
					JSONObject jsonresult = JSONObject.parseObject(loginResult);
					if (jsonresult.containsKey("error")) {
						return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
					} else {
						dataJson.put("token", jsonresult.get("access_token"));
						auditOnlineRegister.set("zcykyrefreshtoken", jsonresult.get("refresh_token").toString());
						iAuditOnlineRegister.updateRegister(auditOnlineRegister);
					}
				} else {
					// 4.2、如果不为空，则用refresh_token去刷新，获取最新的access_token
					loginUrl = "/rest/oauth2/token?grant_type=refresh_token&&client_id=zwdtClient&client_secret=zwdtSecret&refresh_token="
							+ refresh_token + "";
					loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
							"application/x-www-form-urlencoded");
					// 4.2.1、解析请求的返回值
					JSONObject jsonresult = JSONObject.parseObject(loginResult);
					if (jsonresult.containsKey("error")) {
						// 4.2.1.1、如果过期的refresh_token，需要重新登录
						// 4.2.1.1.1、若采用SM2加密，则传入sm2加密标识
						if (StringUtil.isNotBlank(customPwdEncrypt)
								&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
									+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
									+ "&encrypttype=sm2" + "";
						}
						// 4.2.1.1.2、未采用sm2加密方式
						else {
							loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
									+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
									+ "";
						}
						loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
								"application/x-www-form-urlencoded");
						// 4.2.1.2、解析请求的返回值
						jsonresult = JSONObject.parseObject(loginResult);
						if (jsonresult.containsKey("error")) {
							return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
						} else {
							dataJson.put("token", jsonresult.get("access_token"));
							auditOnlineRegister.set("zcykyrefreshtoken", jsonresult.get("refresh_token").toString());
							iAuditOnlineRegister.updateRegister(auditOnlineRegister);
						}
					} else {
						dataJson.put("token", jsonresult.get("access_token"));
						auditOnlineRegister.set("zcykyrefreshtoken", jsonresult.get("refresh_token").toString());
						iAuditOnlineRegister.updateRegister(auditOnlineRegister);
					}
				}
				log.info("=======结束调用zcYkyUserDetailByOpenID接口=======");
				return JsonUtils.zwdtRestReturn("1", " 通过openid获取用户信息成功", dataJson.toString());
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======zcYkyUserDetailByOpenID接口参数：params【" + params + "】=======");
			log.info("=======zcYkyUserDetailByOpenID异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", " 通过openid获取用户信息出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 邹城小程序登录接口
	 * 
	 * @param params 接口的入参
	 * @return
	 */
	@RequestMapping(value = "/zcXcxLogin", method = RequestMethod.POST)
	public String zcXcxLogin(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用zcXcxLogin接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取登录名（手机号码/身份证号码）
				String idnumOrMobile = obj.getString("idnumormobile");

				// 1.3、获取框架加密的密码--用于江苏通用版的密码解密
				String encodepassword = obj.getString("encodepassword");
				// 1.4、获取是否使用大汉认证中心
				String isuse = ConfigUtil.getConfigValue("jnlogin", "usedhuser");
				// 1.5、获取SM2加密配置参数
				String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
				// 1.6 获取大汉的token
				String dhtoken = obj.getString("token");
				// 1.7 大汉解密私钥
				String decodekey = ConfigUtil.getConfigValue("jnlogin", "wxappkey");
				// 定义加密方法
				UserEncodeUtil userEncodeUtil = new UserEncodeUtil();
				AuditOnlineRegister auditOnlineRegister = null;
				if (ZwdtConstant.STRING_YES.equals(isuse)) {
					String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
					String appmark = ConfigUtil.getConfigValue("jnlogin", "wxappmark");
					String appword = ConfigUtil.getConfigValue("jnlogin", "wxappword");
					String wangzhi = ConfigUtil.getConfigValue("jnlogin", "dhrest");
					// 通过大汉提供的加密方式进行加密
					String sign = MD5Util.getMD5(appmark + appword + time);
					String urlparams = "appmark=" + appmark + "&time=" + time + "&sign=" + sign;
					Object obj2 = null;
					Object objticket = null;
					// 获取票据
					objticket = DhUser_HttpUtil.getTicketByPwd(wangzhi, idnumOrMobile, encodepassword, urlparams,
							appword);
					JSONObject ticketObject = JSONObject.parseObject(objticket.toString());
					if (ticketObject.isEmpty() || !"000000".equals(ticketObject.getString("retcode"))) {
						return JsonUtils.zwdtRestReturn("0", "获取大汉票据失败！", "");
					} else {
						String datastr = ticketObject.getString("data");
						if (StringUtil.isNotBlank(datastr)) {
							dhtoken = JSON.parseObject(datastr).getString("token");
						}
					}
					// 验证票据
					obj2 = DhUser_HttpUtil.getUserinfoByDhrest(wangzhi, dhtoken, decodekey, urlparams, false);
					String[] resultdata = obj2.toString().split("#COMPANY#");
					JSONObject resultObject = JSONObject.parseObject(resultdata[0]);
					if (resultObject.isEmpty() || !"000000".equals(resultObject.getString("retcode"))) {
						return JsonUtils.zwdtRestReturn("0", "获取大汉用户失败！", "");
					}
					String userdata = resultObject.getString("data");
					resultObject = JSONObject.parseObject(userdata);
					// 获取大汉相关数据
					// 身份证
					String idcard = resultObject.getString("cardid");
					// 登录名
					String loginname = resultObject.getString("loginname");
					// 姓名
					String name = resultObject.getString("name");
					// 手机号
					String mobile = resultObject.getString("mobile");
					idcard = StringUtil.isNotBlank(idcard) ? idcard.toUpperCase() : idcard;
					// 这边需要先判断用户数据有没有存到数据库中去
					auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(mobile).getResult();
					if (auditOnlineRegister == null) {
						String sm2Pwd = "";
						// 判断是否采用sm2加密
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							sm2Pwd = userEncodeUtil.encryption(MD5Util.getMD5(encodepassword), idcard);

						}
						String accountguid = UUID.randomUUID().toString();
						// 3、插入个人基本信息表
						AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
						auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
						auditOnlineIndividual.setAccountguid(accountguid);
						auditOnlineIndividual.setClientname(name);
						auditOnlineIndividual.setIdnumber(idcard);
						auditOnlineIndividual.setContactmobile(mobile);
						auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
						iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
						// 3.1、插入用户注册表
						AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
						auditonlineregister.setRowguid(UUID.randomUUID().toString());
						auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);// 默认自然人
						auditonlineregister.setAccountguid(accountguid);
						// 3.1.1、判断是否使用sm2
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							auditonlineregister.setPassword(sm2Pwd);
						} else {
							auditonlineregister.setPassword(MD5Util.getMD5(encodepassword));
						}
						auditonlineregister.setMobile(mobile);
						auditonlineregister.setUsername(name);
						auditonlineregister.setIdnumber(idcard);
						auditonlineregister.setLoginid(loginname);
						iAuditOnlineRegister.addRegister(auditonlineregister);
						auditOnlineRegister = auditonlineregister;
					} else {
						// 判断是否使用sm2加密
						if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							auditOnlineRegister.setPassword(userEncodeUtil.encryption(MD5Util.getMD5(encodepassword),
									auditOnlineRegister.getIdnumber().toUpperCase()));
						} else {
							auditOnlineRegister.setPassword(MD5Util.getMD5(encodepassword));
						}
						// 更新用户信息
						auditOnlineRegister.setMobile(mobile);
						auditOnlineRegister.setUsername(name);
						auditOnlineRegister.setIdnumber(idcard);
						auditOnlineRegister.setLoginid(loginname);
						ICommonDao dao = CommonDao.getInstance();
						dao.update(auditOnlineRegister);
						dao.commitTransaction();
					}
				} else {
					auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(idnumOrMobile).getResult();
					if (auditOnlineRegister == null) {
						return JsonUtils.zwdtRestReturn("0", "该账号不存在（未使用大汉认证中心）", "");
					}
				}
				// 根据本地账号密码获取token
				String loginUrl = ""; // 请求token地址
				String loginResult = ""; // 请求token返回结果
				String urlroot = request.getRequestURL().toString();
				urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
				if (StringUtil.isNotBlank(customPwdEncrypt)
						&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
					loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
							+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
							+ "&encrypttype=sm2" + "";
				}
				// 4.1.2、未采用sm2加密方式
				else {
					loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
							+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword() + "";
				}
				loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "", "application/x-www-form-urlencoded");
				// 4.1.3、解析请求的返回值
				JSONObject jsonresult = JSONObject.parseObject(loginResult);
				System.out.println(jsonresult);
				if (jsonresult.containsKey("error")) {
					return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
				} else {
					JSONObject dataJson = new JSONObject();
					dataJson.put("username", auditOnlineRegister.getUsername()); // 用户姓名
					dataJson.put("idnum", auditOnlineRegister.getIdnumber()); // 身份证号码
					dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号码
					dataJson.put("token", jsonresult.get("access_token"));
					return JsonUtils.zwdtRestReturn("1", "登录成功", dataJson.toString());
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======zcXcxLogin接口参数：params【" + params + "】=======");
			log.info("=======zcXcxLogin异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "邹城小程序登录出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 邹城小程序登录接口
	 * 
	 * @param params 接口的入参
	 * @return
	 */
	@RequestMapping(value = "/zcappLogin", method = RequestMethod.POST)
	public String zcappLogin(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用zcappLogin接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String key="epoint@123!@#$";
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				System.out.println(AESUtil.encrypt(jsonObject.getString("params"), key));
				JSONObject obj = JSONObject.parseObject(AESUtil.decrypt(jsonObject.getString("params"), key));
				// 身份证
				String idcard = obj.getString("idnum");
				// 登录名
				String loginname = obj.getString("loginname");
				// 姓名
				String name = obj.getString("username");
				// 手机号
				String mobile = obj.getString("mobile");
				if(StringUtil.isBlank(idcard)) {
					return JsonUtils.zwdtRestReturn("0", "身份证不能为空", "");
				}
				if(StringUtil.isBlank(name)) {
					return JsonUtils.zwdtRestReturn("0", "用户名不能为空", "");
				}
				if(StringUtil.isBlank(mobile)) {
					return JsonUtils.zwdtRestReturn("0", "手机号不能为空", "");
				}

				// 1.3、获取框架加密的密码--用于江苏通用版的密码解密
				String encodepassword = "11111";
				// 1.5、获取SM2加密配置参数
				String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();

				// 定义加密方法
				UserEncodeUtil userEncodeUtil = new UserEncodeUtil();
				AuditOnlineRegister auditOnlineRegister = null;
				idcard = StringUtil.isNotBlank(idcard) ? idcard.toUpperCase() : idcard;
				// 这边需要先判断用户数据有没有存到数据库中去
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(mobile).getResult();
				if (auditOnlineRegister == null) {
					String sm2Pwd = "";
					// 判断是否采用sm2加密
					if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
						sm2Pwd = userEncodeUtil.encryption(MD5Util.getMD5(encodepassword), idcard);

					}
					String accountguid = UUID.randomUUID().toString();
					// 3、插入个人基本信息表
					AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
					auditOnlineIndividual.setRowguid(UUID.randomUUID().toString());
					auditOnlineIndividual.setAccountguid(accountguid);
					auditOnlineIndividual.setClientname(name);
					auditOnlineIndividual.setIdnumber(idcard);
					auditOnlineIndividual.setContactmobile(mobile);
					auditOnlineIndividual.setApplyerguid(UUID.randomUUID().toString());
					iAuditOnlineIndividual.addIndividual(auditOnlineIndividual);
					// 3.1、插入用户注册表
					AuditOnlineRegister auditonlineregister = new AuditOnlineRegister();
					auditonlineregister.setRowguid(UUID.randomUUID().toString());
					auditonlineregister.setUsertype(ZwdtConstant.ONLINEUSERTYPE_PERSON);// 默认自然人
					auditonlineregister.setAccountguid(accountguid);
					// 3.1.1、判断是否使用sm2
					if (ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
						auditonlineregister.setPassword(sm2Pwd);
					} else {
						auditonlineregister.setPassword(MD5Util.getMD5(encodepassword));
					}
					auditonlineregister.setMobile(mobile);
					auditonlineregister.setUsername(name);
					auditonlineregister.setIdnumber(idcard);
					auditonlineregister.setLoginid(loginname);
					iAuditOnlineRegister.addRegister(auditonlineregister);
					auditOnlineRegister = auditonlineregister;
				} else {
					// 更新用户信息
					auditOnlineRegister.setMobile(mobile);
					auditOnlineRegister.setUsername(name);
					auditOnlineRegister.setIdnumber(idcard);
					auditOnlineRegister.setLoginid(loginname);
					ICommonDao dao = CommonDao.getInstance();
					dao.update(auditOnlineRegister);
					dao.commitTransaction();
				}

				// 根据本地账号密码获取token
				String loginUrl = ""; // 请求token地址
				String loginResult = ""; // 请求token返回结果
				String urlroot = request.getRequestURL().toString();
				urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
				if (StringUtil.isNotBlank(customPwdEncrypt)
						&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
					loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
							+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
							+ "&encrypttype=sm2" + "";
				}
				// 4.1.2、未采用sm2加密方式
				else {
					loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
							+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword() + "";
				}
				loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "", "application/x-www-form-urlencoded");
				// 4.1.3、解析请求的返回值
				JSONObject jsonresult = JSONObject.parseObject(loginResult);
				System.out.println(jsonresult);
				if (jsonresult.containsKey("error")) {
					return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
				} else {
					JSONObject dataJson = new JSONObject();
					dataJson.put("username", auditOnlineRegister.getUsername()); // 用户姓名
					dataJson.put("idnum", auditOnlineRegister.getIdnumber()); // 身份证号码
					dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号码
					dataJson.put("token", jsonresult.get("access_token"));
					dataJson.put("code", 1);
					dataJson.put("text", "登录成功");
					return JsonUtils.zwdtRestReturn("1", "登录成功", AESUtil.encrypt(dataJson.toString(), key));
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======zcappLogin接口参数：params【" + params + "】=======");
			log.info("=======zcappLogin异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "邹城小程序登录出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 鱼台登录接口
	 * 
	 * @param params 接口的入参
	 * @return
	 */
	@RequestMapping(value = "/ytXcxLogin", method = RequestMethod.POST)
	public String ytXcxLogin(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用ytXcxLogin接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取登录名（手机号码/身份证号码）
				String mobile = obj.getString("mobile");
				// 1.2、验证码
				String code = obj.getString("code");

				String customPwdEncrypt = "";

				AuditOnlineVerycode auditOnlineVerycode = null;
				if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("redisSetting"))) {
					ZwfwRedisCacheUtil redisUtil = null;
					try {
						// 如果系统使用了redis，则从redis中获取
						redisUtil = new ZwfwRedisCacheUtil(false);
						auditOnlineVerycode = redisUtil.getByHashzwdtvcode(AuditOnlineVerycode.class, mobile, "4");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (redisUtil != null) {
							redisUtil.close();
						}
					}
				} else {
					// 如果系统未使用了redis，则从数据库中获取
					auditOnlineVerycode = iAuditOnlineVeryCode.getScanVerifyCode(
							EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), mobile, "0",
							"3").getResult();
				}

				if (!code.equals(auditOnlineVerycode.getVerifycode())) {
					return JsonUtils.zwdtRestReturn("0", "验证码错误，请重新输入！", "");
				}

				AuditOnlineRegister auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(mobile)
						.getResult();
				if (auditOnlineRegister == null) {
					return JsonUtils.zwdtRestReturn("0", "该账号不存在（未使用大汉认证中心）", "");
				}

				if ("20".equals(auditOnlineRegister.getUsertype())) {
					return JsonUtils.zwdtRestReturn("0", "个人账号无法登录！", "");
				}
				// 根据本地账号密码获取token
				String loginUrl = ""; // 请求token地址
				String loginResult = ""; // 请求token返回结果
				String urlroot = request.getRequestURL().toString();
				urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
				if (StringUtil.isNotBlank(customPwdEncrypt)
						&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
					loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
							+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
							+ "&encrypttype=sm2" + "";
				}
				// 4.1.2、未采用sm2加密方式
				else {
					loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
							+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword() + "";
				}
				loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "", "application/x-www-form-urlencoded");
				// 4.1.3、解析请求的返回值
				JSONObject jsonresult = JSONObject.parseObject(loginResult);
				System.out.println(jsonresult);
				if (jsonresult.containsKey("error")) {
					return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
				} else {
					JSONObject dataJson = new JSONObject();
					dataJson.put("username", auditOnlineRegister.getUsername()); // 用户姓名
					dataJson.put("idnum", auditOnlineRegister.getIdnumber()); // 身份证号码
					dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号码
					dataJson.put("token", jsonresult.get("access_token"));
					return JsonUtils.zwdtRestReturn("1", "登录成功", dataJson.toString());
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======zcXcxLogin接口参数：params【" + params + "】=======");
			log.info("=======zcXcxLogin异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "鱼台登录出现异常：" + e.getMessage(), "");
		}
	}

	@RequestMapping(value = { "/zcYkyWxUserUnBind" }, method = { RequestMethod.POST })
	public String zcYkyWxUserUnBind(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			this.log.info("=======开始调用zcYkyWxUserUnBind接口=======");
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if ("Epoint_WebSerivce_**##0601".equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				String openid = "";
				AuditOnlineRegister register = this.getOnlineRegister(request);
				if (register != null) {
					openid = register.get("zcykyopenid");
				}

				if (StringUtil.isBlank(openid)) {
					return JsonUtils.zwdtRestReturn("0", "请传入正确的微信标识", "");
				} else {
					SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
					sqlConditionUtil.eq("zcykyopenid", openid);
					List<AuditOnlineRegister> auditOnlineRegisters = (List) this.iAuditOnlineRegister
							.selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
					if (auditOnlineRegisters.isEmpty()) {
						return JsonUtils.zwdtRestReturn("0", "用户名不存在", "");
					} else {
						AuditOnlineRegister auditOnlineRegister = (AuditOnlineRegister) auditOnlineRegisters.get(0);
						auditOnlineRegister.set("zcykyopenid", "");
						this.iAuditOnlineRegister.updateRegister(auditOnlineRegister);
						return JsonUtils.zwdtRestReturn("1", "解除绑定成功", "");
					}
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}
		} catch (Exception var11) {
			var11.printStackTrace();
			this.log.info("=======zcYkyWxUserUnBind接口参数：params【" + params + "】=======");
			this.log.info("=======zcYkyWxUserUnBind异常信息：" + var11.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "微信用户解除绑定出现异常：" + var11.getMessage(), "");
		}
	}

	/**
	 * 通过OPENID获取用户信息
	 * 
	 * @param params  接口的入参
	 * @param request HTTP请求
	 * @return
	 */
	@RequestMapping(value = "/wzUserDetailByOpenID", method = RequestMethod.POST)
	public String wzUserDetailByOpenID(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用wzUserDetailByOpenID接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {
				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取用户微信唯一标识
				String openid = obj.getString("openid");
				if (StringUtil.isBlank(openid)) {
					return JsonUtils.zwdtRestReturn("0", "请传入正确的微信标识", "");
				}
				// 1.2、获取sm2加密配置参数
				String customPwdEncrypt = handleConfig.getFrameConfig("sm2_EncryptPwd", "").getResult();
				// 2、根据OPENID获取用户信息
				SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
				sqlConditionUtil.eq("openid", openid);
				List<AuditOnlineRegister> auditOnlineRegisters = iAuditOnlineRegister
						.selectAuditOnlineRegisterList(sqlConditionUtil.getMap()).getResult();
				if (auditOnlineRegisters.isEmpty()) {
					return JsonUtils.zwdtRestReturn("0", "用户名不存在或者还未绑定", "");
				}
				// 3、定义返回JSON对象
				JSONObject dataJson = new JSONObject();
				AuditOnlineRegister auditOnlineRegister = auditOnlineRegisters.get(0);
				dataJson.put("username", auditOnlineRegister.getUsername()); // 用户姓名
				dataJson.put("idnum", auditOnlineRegister.getIdnumber()); // 身份证号码
				dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号码
				String refresh_token = auditOnlineRegister.getRefreshtoken();
				String loginUrl = ""; // 请求token地址
				String loginResult = ""; // 请求token返回结果
				String urlroot = request.getRequestURL().toString();
				if (StringUtil.isNotBlank(ConfigUtil.getConfigValue("interalIP"))) {
					urlroot = ConfigUtil.getConfigValue("interalIP");
				} else {
					urlroot = urlroot.substring(0, urlroot.indexOf("rest"));
				}
				// 4、判断是否首次登录
				if (StringUtil.isBlank(refresh_token)) {
					// 4.1、如果为空 则请求登录接口获取token
					// 4.1.1、若采用SM2加密，则传入sm2加密标识
					if (StringUtil.isNotBlank(customPwdEncrypt)
							&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
						loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
								+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
								+ "&encrypttype=sm2" + "";
					}
					// 4.1.2、未采用sm2加密方式
					else {
						loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
								+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
								+ "";
					}
					loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
							"application/x-www-form-urlencoded");
					// 4.1.3、解析请求的返回值
					JSONObject jsonresult = JSONObject.parseObject(loginResult);
					if (jsonresult.containsKey("error")) {
						return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
					} else {
						dataJson.put("token", jsonresult.get("access_token"));
						auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
						iAuditOnlineRegister.updateRegister(auditOnlineRegister);
					}
				} else {
					// 4.2、如果不为空，则用refresh_token去刷新，获取最新的access_token
					loginUrl = "/rest/oauth2/token?grant_type=refresh_token&&client_id=zwdtClient&client_secret=zwdtSecret&refresh_token="
							+ refresh_token + "";
					loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
							"application/x-www-form-urlencoded");
					// 4.2.1、解析请求的返回值
					JSONObject jsonresult = JSONObject.parseObject(loginResult);
					if (jsonresult.containsKey("error")) {
						// 4.2.1.1、如果过期的refresh_token，需要重新登录
						// 4.2.1.1.1、若采用SM2加密，则传入sm2加密标识
						if (StringUtil.isNotBlank(customPwdEncrypt)
								&& ZwdtConstant.CUSTOMPWDENCRYPT_SM2.equalsIgnoreCase(customPwdEncrypt)) {
							loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
									+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
									+ "&encrypttype=sm2" + "";
						}
						// 4.2.1.1.2、未采用sm2加密方式
						else {
							loginUrl = "/rest/oauth2/token?client_id=zwdtClient&client_secret=zwdtSecret&grant_type=password&scope=zwdt&username="
									+ auditOnlineRegister.getMobile() + "&password=" + auditOnlineRegister.getPassword()
									+ "";
						}
						loginResult = HttpRequestUtils.sendPost(urlroot + loginUrl, "",
								"application/x-www-form-urlencoded");
						// 4.2.1.2、解析请求的返回值
						jsonresult = JSONObject.parseObject(loginResult);
						if (jsonresult.containsKey("error")) {
							return JsonUtils.zwdtRestReturn("0", jsonresult.get("error_description").toString(), "");
						} else {
							dataJson.put("token", jsonresult.get("access_token"));
							auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
							iAuditOnlineRegister.updateRegister(auditOnlineRegister);
						}
					} else {
						dataJson.put("token", jsonresult.get("access_token"));
						auditOnlineRegister.setRefreshtoken(jsonresult.get("refresh_token").toString());
						iAuditOnlineRegister.updateRegister(auditOnlineRegister);
					}
				}
				log.info("=======结束调用wzUserDetailByOpenID接口=======");
				return JsonUtils.zwdtRestReturn("1", " 通过openid获取用户信息成功", dataJson.toString());
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======wzUserDetailByOpenID接口参数：params【" + params + "】=======");
			log.info("=======wzUserDetailByOpenID异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", " 通过openid获取用户信息出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 通过OPENID获取用户信息
	 * 
	 * @param params  接口的入参
	 * @param request HTTP请求
	 * @return
	 */
	@RequestMapping(value = "/wzUserDetailByIdbumber", method = RequestMethod.POST)
	public String wzUserDetailByIdbumber(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用wzUserDetailByIdbumber接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String token = jsonObject.getString("token");
			if (ZwdtConstant.SysValidateData.equals(token)) {

				AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);

				JSONObject obj = (JSONObject) jsonObject.get("params");
				// 1.1、获取用户微信唯一标识
				String applyertype = obj.getString("applyertype");
				if (auditOnlineRegister == null) {
					return JsonUtils.zwdtRestReturn("0", "用户信息不存在！", "");
				}
				// 3、定义返回JSON对象
				JSONObject dataJson = new JSONObject();

				if (auditOnlineRegister != null) {
					// 2、获取用户信息
					if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyertype)) {
						SqlConditionUtil sql = new SqlConditionUtil();
						sql.eq("ORGALEGAL_IDNUMBER", auditOnlineRegister.getIdnumber());
						List<AuditRsCompanyBaseinfo> company = iAuditRsCompanyBaseinfo
								.selectAuditRsCompanyBaseinfoByCondition(sql.getMap()).getResult();
						if (company != null && company.size() > 0) {
							AuditRsCompanyBaseinfo com = company.get(0);
							dataJson.put("companyname", com.getOrganname());
							dataJson.put("cerditcode", com.getCreditcode());
							dataJson.put("legal", com.getOrganlegal()); // 身份证号码
						}
					}
					dataJson.put("username", auditOnlineRegister.getUsername()); // 用户姓名
					dataJson.put("idnum", auditOnlineRegister.getIdnumber()); // 身份证号码
					dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号码

				}

				log.info("=======结束调用wzUserDetailByIdbumber接口=======");
				return JsonUtils.zwdtRestReturn("1", " 用户信息获取成功", dataJson.toString());
			} else {
				return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======wzUserDetailByIdbumber接口参数：params【" + params + "】=======");
			log.info("=======wzUserDetailByIdbumber异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", " 用户信息获取出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 
	 * 更新客户信息
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkUser", method = RequestMethod.POST)
	public String checkUser(@RequestBody String params, HttpServletRequest request) {
		log.info("=======开始调用checkUser接口=======");
		// 1、接口的入参转化为JSON对象
		JSONObject jsonObject = JSONObject.parseObject(params);
		String token = jsonObject.getString("token");
		if (ZwdtConstant.SysValidateData.equals(token)) {
			JSONObject obj = (JSONObject) jsonObject.get("params");
			String idnumber = obj.getString("idnumber");
			String mobile = obj.getString("mobile");
			SqlConditionUtil sql = new SqlConditionUtil();
			sql.eq("mobile", mobile);
			sql.eq("idnumber", idnumber);
			List<AuditOnlineRegister> registers = iAuditOnlineRegister.selectAuditOnlineRegisterList(sql.getMap())
					.getResult();

			if (registers.isEmpty()) {
				log.info("=======结束调用checkUser接口=======");
				return JsonUtils.zwdtRestReturn("0", " 查询用户信息成功", "error");
			} else {
				log.info("=======结束调用checkUser接口=======");
				return JsonUtils.zwdtRestReturn("1", " 查询用户信息成功", "success");
			}

		} else {
			return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
		}

	}

	/**
	 * 获取用户唯一标识
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
		AuditOnlineRegister auditOnlineRegister;
		OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
		if (oAuthCheckTokenInfo != null) {
			// 手机端
			// 通过登录名获取用户
			auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
					.getResult();
		} else {
			// PC端
			String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
			if (StringUtil.isNotBlank(accountGuid)) {
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
			} else {
				// 通过登录名获取用户
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
						.getResult();
			}
		}
		return auditOnlineRegister;
	}
}
