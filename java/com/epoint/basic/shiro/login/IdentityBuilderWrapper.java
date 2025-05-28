package com.epoint.basic.shiro.login;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.asutils.SSOConfigEnvironment;
import com.epoint.authenticator.constants.ConstantsOverview;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.identity.Identity;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.dto.CoreUtil;
import com.epoint.core.dto.RequestContext;
import com.epoint.core.utils.code.AESEncDec;
import com.epoint.core.utils.code.DES;
import com.epoint.core.utils.code.EncodeUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.security.DataEncryptionUtil;
import com.epoint.core.utils.security.TokenUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.esotericsoftware.minlog.Log;

/**
 * 框架不同登录方式前端信息的解析类集合，主要是解析前端数据格式，封装用户身份对象，解密用户名和密码
 * 
 * @作者 sjw
 * @version [版本号, 2017年1月3日]
 */
public class IdentityBuilderWrapper
{
    private static DataEncryptionUtil impl = DataEncryptionUtil.getInstanceReversible();

    public static class LoginByUsernameAndPasswordBuilder extends FrametIdentityBuilder
    {
        @Override
        public boolean preBuild(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            // 判断是否登录
            if (Authenticator.isAuthenticated(servletRequest)) {
                Identity identity = new Identity();
                identity.setPrincipal(Authenticator.getCurrentIdentity());
                identity.setClientIp(getClientIP(servletRequest));
                identity.setHost(getHost(servletRequest));
                HttpSession session = servletRequest.getSession(false);
                String loadType = session.getAttribute("_loadType_") == null ? ""
                        : session.getAttribute("_loadType_").toString();
                identity.setLoadType(loadType);
                Map<String, String> extValue = getRequestParametersMap(servletRequest);
                extValue.put("ClientIp", getClientIP(servletRequest));
                identity.setExtvalue(extValue);

                String page = afterSuccessLogin(identity, request);
                redirectWithAjax(page, servletResponse);
                // 标记流程走完
                return true;
            }
            return false;
        }

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            String username = "";
            String password = "";
            // 默认用户名密码登录
            String loadType = "0";
            RequestContext context = new RequestContext((HttpServletRequest) request, null);
            String cmdparams = CoreUtil.getCmdParams(context);
            String multiParam = "";
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                username = format(arr[0]);
                password = format(arr[1]);
                if (arr.length > 2) {
                    loadType = format(arr[2]);
                }
                if (arr.length > 3) {
                    multiParam = format(arr[3]);
                }
            }

            Map<String, String> extendMap = new HashMap<String, String>();
            if (StringUtil.isNotBlank(multiParam.trim())) {
                // 动态口令登录
                if ("77".equals(loadType) || "66".equals(loadType)) {
                    extendMap.put("otp", multiParam);
                }
                // 手机验证码登录
                if ("31".equals(loadType) || "30".equals(loadType)) {
                    extendMap.put("messageauthenticationcode", multiParam);
                }
                // 验证码(验证码目前只存在用户密码登录和usb登录)
                if ("0".equals(loadType) || "10".equals(loadType)) {
                    extendMap.put("verifycode", multiParam);
                }
            }

            // 增加手机验证码传递回来的值
            if (request.getParameterMap().get(ConstantsOverview.LoginStrategy.ISMOBILEVERIFICATION) != null) {
                extendMap.put(ConstantsOverview.LoginStrategy.ISMOBILEVERIFICATION,
                        request.getParameterMap().get(ConstantsOverview.LoginStrategy.ISMOBILEVERIFICATION)[0].toString());
            }

            // 封装请求参数
            return createCommonToken(username, password, extendMap, loadType, request, response);
        }
    }

    public static class LoginByIdBuilder extends FrametIdentityBuilder
    {
        @Override
        public boolean preBuild(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            // 判断是否登录
            if (Authenticator.isAuthenticated()) {
                Identity identity = new Identity();
                identity.setPrincipal(Authenticator.getCurrentIdentity());
                identity.setClientIp(getClientIP(servletRequest));
                identity.setHost(getHost(servletRequest));
                HttpSession session = servletRequest.getSession(false);
                String loadType = session.getAttribute("_loadType_") == null ? ""
                        : session.getAttribute("_loadType_").toString();
                identity.setLoadType(loadType);
                Map<String, String> extValue = getRequestParametersMap(servletRequest);
                extValue.put("ClientIp", getClientIP(servletRequest));
                identity.setExtvalue(extValue);

                String page = afterSuccessLogin(identity, request);
                redirectWithAjax(page, servletResponse);
                // 标记流程走完
                return true;
            }
            return false;
        }

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            // loginByid的方式，需要额外传入token值
            String token = WebUtil.getRequestParameterStr(servletRequest, "token");
            Identity identity = null;
            try {
                if (TokenUtils.validateToken(token)) {
                    String loginid = getRequestParameter(servletRequest, "loginid");
                    // 当启用了用户名加密功能时，就需要对其进行解密
                    String encryptLoginId = ConfigUtil.getConfigValue("encryptLoginId");
                    if (StringUtil.isNotBlank(encryptLoginId) && encryptLoginId.equalsIgnoreCase("1")) {
                        if (impl.isUseEncrypt(loginid)) {
                            loginid = impl.decryption(loginid);
                        }
                        else {
                            loginid = DES.decrypt(loginid);
                        }
                    }
                    identity = createCommonToken(loginid, loginid, "loginbyid", request, response);
                    identity.put("LoginByID", "true");
                    // 支持直接跳转
                    if (servletRequest.getMethod().equalsIgnoreCase("get")) {
                        identity.put("Jump straight", "true");
                    }
                }
                else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("systemMessages", "token" + "验证失败");
                    addCallBackMessageWithAjax(params, servletResponse);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return identity;
        }
    }

    public static class LoginByUserinfoBuilder extends FrametIdentityBuilder
    {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            Map<String, String> params = new HashMap<String, String>();
            Identity identity = null;
            // 获取url参数，加密后的用户信息
            String userinfo = getRequestParameter(servletRequest, "userinfo");
            try {
                if (StringUtil.isNotBlank(userinfo)) {
                    // 解密，约定的原文格式为“用户名&密码&时间戳”
                    String[] decUserInfoList;
                    if (impl.isUseEncrypt(userinfo)) {
                        decUserInfoList = impl.decryption(userinfo).split("&");
                    }
                    else {
                        decUserInfoList = AESEncDec.decrypt(userinfo).split("&");
                    }
                    if (decUserInfoList.length < 2) {
                        params.put("systemMessages", "userinfo" + "验证失败");
                        addCallBackMessageWithAjax(params, servletResponse);
                    }
                    else {
                        identity = createCommonToken(decUserInfoList[0], decUserInfoList[1], "0", servletRequest,
                                servletResponse);
                        // 支持直接跳转
                        if (servletRequest.getMethod().equalsIgnoreCase("get")) {
                            identity.put("Jump straight", "true");
                        }
                    }
                }
                else {
                    params.put("systemMessages", "userinfo" + "验证失败");
                    addCallBackMessageWithAjax(params, servletResponse);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return identity;
        }
    }

    public static class LoginByADBuilder extends FrametIdentityBuilder
    {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            Identity identity = null;

            // 检查是否已经登录域,如果已经登陆,那么跳转
            String principal = servletRequest.getSession().getAttribute("principal").toString();
            if (StringUtil.isNotBlank(principal)) {
                // 解析用户名
                String[] vals = principal.split("@");
                String username = vals[0];
                // 当启用了用户名加密功能时，就需要对其进行解密
                String encryptLoginId = ConfigUtil.getConfigValue("encryptLoginId");
                if (StringUtil.isNotBlank(encryptLoginId) && encryptLoginId.equalsIgnoreCase("1")) {
                    try {
                        if (impl.isUseEncrypt(username)) {
                            username = impl.decryption(username);
                        }
                        else {
                            username = DES.decrypt(username);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                identity = createCommonToken(username, username, "loginbyid", servletRequest, servletResponse);
            }
            return identity;
        }
    }

    public static class LoginByValidateCodeBuilder extends FrametIdentityBuilder
    {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            Identity identity = null;

            String username = "";
            String password = "";
            String inPutCode = "";
            // 默认用户名密码登录
            String loadType = "0";
            RequestContext context = new RequestContext(servletRequest, null);
            String cmdparams = CoreUtil.getCmdParams(context);
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                username = format(arr[0]);
                password = format(arr[1]);
                if (arr.length > 2) {
                    loadType = format(arr[2]);
                }
                if (arr.length > 3) {
                    inPutCode = format(arr[3]);
                }
            }

            String validateCode = CoreUtil.getComponentAttr(context, "validateCode", "value");
            String inputCode = CoreUtil.getComponentAttr(context, "inputCode", "value");

            if (StringUtil.isBlank(inputCode)) {
                inputCode = inPutCode;
            }
            // 验证码使用不可逆算法生成，此处需使用不可逆算法校验
            DataEncryptionUtil util = DataEncryptionUtil.getInstance();
            if (StringUtil.isBlank(validateCode) || StringUtil.isBlank(inputCode)
                    || !util.matchs(StringUtil.toUpperCase(inputCode), validateCode)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("systemMessages", "你输入的验证码不正确,请重新输入");
                addCallBackMessageWithAjax(params, servletResponse);
            }
            else {
                // 添加对于自定义加密的支持，用于替换rsa解密性能低下问题 edit by ko 2017-5-23
                username = EncodeUtil.decodeByJs(username);
                password = EncodeUtil.decodeByJs(password);
                identity = createCommonToken(username, password, loadType, servletRequest, servletResponse);
            }
            return identity;
        }
    }

    public static class LoginSecondBuilder extends FrametIdentityBuilder
    {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            Identity identity = null;

            String username = "";
            String password = "";
            String ouguid = "";
            String multiParam = "";
            // 默认用户名密码登录
            String loadType = "0";
            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                loadType = format(arr[0]);
                ouguid = format(arr[2]);
                username = format(arr[3]);
                password = format(arr[4]);

                if (arr.length > 5) {
                    multiParam = format(arr[5]);
                }
            }
            Map<String, String> extendMap = new HashMap<String, String>();
            if (StringUtil.isNotBlank(multiParam.trim())) {
                // 验证码
                if ("0".equals(loadType) || "10".equals(loadType)) {
                    extendMap.put("verifycode", multiParam);
                }
            }

            // 添加对于自定义加密的支持，用于替换rsa解密性能低下问题 edit by ko 2017-5-23
            username = EncodeUtil.decodeByJs(username);
            password = EncodeUtil.decodeByJs(password);
            identity = createCommonToken(username, password, extendMap, loadType, servletRequest, servletResponse);
            identity.getExtvalue().put("checkSecondLogin", "false");
            identity.getExtvalue().put("ouguid", ouguid);
            return identity;
        }
    }

    public static class LoginAutoBuilder extends FrametIdentityBuilder
    {
        @Override
        public boolean preBuild(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            // 判断是否登录
            if (Authenticator.isAuthenticated(servletRequest)) {
                Identity identity = new Identity();
                identity.setPrincipal(Authenticator.getCurrentIdentity());
                identity.setClientIp(getClientIP(servletRequest));
                identity.setHost(getHost(servletRequest));
                HttpSession session = servletRequest.getSession(false);
                String loadType = session.getAttribute("_loadType_") == null ? ""
                        : session.getAttribute("_loadType_").toString();
                identity.setLoadType(loadType);
                Map<String, String> extValue = getRequestParametersMap(servletRequest);
                extValue.put("ClientIp", getClientIP(servletRequest));
                identity.setExtvalue(extValue);

                String page = afterSuccessLogin(identity, request);
                redirectWithAjax(page, servletResponse);
                // 标记流程走完
                return true;
            }
            // 标记流程走完
            return true;
        }
    }

    public static class LogoutBuilder extends FrametIdentityBuilder
    {
        @Override
        public boolean preBuild(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            //system.out.println("用户进行登出");
            // 判断是否登录
            if (Authenticator.isAuthenticated(servletRequest)) {
                // 进行跳转
                String url = SSOConfigEnvironment.LOGOUT_URL;

                // 判断是否有特殊的回掉地址
                String login_uri = WebUtil.getRequestParameterStr(servletRequest, "login_uri");
                // 解析login_uri，参数都编码一下，防止后面重定向有中文问题
                if (StringUtil.isNotBlank(login_uri) && login_uri.indexOf("?") > -1) {
                    String[] params = login_uri.substring(login_uri.indexOf("?")).split("&");
                    if (params != null && params.length > 0) {
                        for (int i = 0; i < params.length; i++) {
                            try {
                                login_uri = login_uri.replace(params[i].split("=")[1],
                                        URLEncoder.encode(params[i].split("=")[1], "utf-8"));
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    url = login_uri;
                }

                // 对于租户用户，登出后要跳回登录地址
                UserSession userSession = UserSession.getInstance();
                String tenantSysName = userSession.getTenantSysName();
                if (StringUtil.isNotBlank(tenantSysName)) {
                    url += "?tenantSysName=" + tenantSysName;
                }
                
                try {
                	//system.out.println("LED触发注销");
	                String content="LED屏注销";
	                String WindowNo=ZwfwUserSession.getInstance().getWindowNo();
	                String CenterGuid=ZwfwUserSession.getInstance().getCenterGuid();
	    		    JSONObject dataJson = new JSONObject();
	    		    dataJson.put("regionName", WindowNo);
	    		    dataJson.put("content", content+",centerGuid:"+CenterGuid);
	    		    //system.out.println("LED触发注销发送消息");
					ProducerMQ.send("QueueLed_" + CenterGuid, "[" + dataJson.toString() + "]");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                
                
                // 进行登出
                Authenticator.logout();
                try {
                    servletResponse.sendRedirect(WebUtil.getRequestCompleteUrl(servletRequest, url));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                //system.out.println("LED触发注销1");
                // 标记流程走完
                return true;
            }
            else {
            	//system.out.println("LED触发注销2");
            	
                // 进行跳转
                String url = SSOConfigEnvironment.LOGOUT_URL;
                // 未登录直接跳转到登录页面
                try {
                    servletResponse.sendRedirect(WebUtil.getRequestCompleteUrl(servletRequest, url));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

    }
}
