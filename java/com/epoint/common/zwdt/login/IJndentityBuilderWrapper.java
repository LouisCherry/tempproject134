
package com.epoint.common.zwdt.login;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.asutils.SSOConfigEnvironment;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.identity.Identity;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlinevisitlog.domain.AuditOnlineVisitLog;
import com.epoint.basic.auditonlineuser.auditonlinevisitlog.inter.IAuditOnlineVisitLog;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.dto.Component;
import com.epoint.core.dto.DtoTransfer;
import com.epoint.core.utils.code.AESEncDec;
import com.epoint.core.utils.code.DES;
import com.epoint.core.utils.code.MD5Util;
import com.epoint.core.utils.code.RSAUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.security.TokenUtils;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.zwdt.zwdtrest.dhuser.DhUser_HttpUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

/**
 * 框架不同登录方式前端信息的解析类集合，主要是解析前端数据格式，封装用户身份对象，解密用户名和密码
 *
 * @author sjw
 * @version [版本号, 2017年1月3日]
 */
public class IJndentityBuilderWrapper {

    transient static Logger log = LogUtil.getLog(com.epoint.common.zwdt.login.IJndentityBuilderWrapper.class);
    /**
     * 用户名和密码---拆分成手机号码+动态密码；手机号码/身份证号码+密码
     */
    public static class LoginByUsernameAndPasswordBuilder extends FrametIdentityBuilder {

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            String username = request.getParameter("username");
            String password = request.getParameter("code");
            String loadType = request.getParameter("logintype");
            String url = request.getParameter("url");//登录成功需要跳转的地址
            if ("".equals(loadType)) {
                // 默认用户名密码登录
                loadType = ZwdtConstant.LoginIdAndPassWord;
            }
            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");
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
            }
            // 封装请求参数
            Identity identity = createCommonToken(username, password, loadType, request, response);
            identity.setName(ZwdtConstant.LoginidAndPasswordlogin);
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", url);
            identity.setExtvalue(map);
            return identity;
        }
    }

    //扫描登录
    public static class LoginByScanBuilder extends FrametIdentityBuilder {

        @SuppressWarnings("unused")
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            String qrguid = "";//二维码标识
            String jessionid = "";//游览器标识
            qrguid = request.getParameter("qrguid");
            jessionid = request.getParameter("jessionid");
            String url = request.getParameter("url");//登录成功需要跳转的地址
            // 扫描登录
            String loadType = ZwdtConstant.LoginByScanType;
            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                qrguid = format(arr[0]);
                jessionid = format(arr[1]);
            }

            Identity identity = null;
            // 封装请求参数
            identity = createCommonToken(qrguid, "", loadType, request, response);
            identity.setName(ZwdtConstant.Scanlogin);
            Map<String, String> map = new HashMap<String, String>();
            map.put("url", url);
            return identity;
        }
    }

    //第三方对接登录
    public static class LoginByThirdBuilder extends FrametIdentityBuilder {

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            //第三方标识guid
            String thirdguid = "";
            // 第三方登录的标志
            String loadType = "";
            //不同第三方登录的标志，微信，扣扣，微博
            String thirdtype = "";
            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");

            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                thirdguid = format(arr[0]);
                thirdtype = format(arr[1]);
                loadType = format(arr[2]);
            }
            Identity identity = null;
            // 封装请求参数
            identity = createCommonToken(thirdguid, thirdtype, loadType, request, response);
            identity.setName(ZwdtConstant.Thirdlogin);
            return identity;
        }
    }

    public static class LoginByIdBuilder extends FrametIdentityBuilder {

        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            // loginByid的方式，需要额外传入token值
            String token = getRequestParameter(servletRequest, "token");
            Identity identity = null;
            try {
                if (TokenUtils.validateToken(token)) {
                    String loginid = getRequestParameter(servletRequest, "loginid");
                    // 当启用了用户名加密功能时，就需要对其进行解密
                    String encryptLoginId = ConfigUtil.getConfigValue("encryptLoginId");
                    if (StringUtil.isNotBlank(encryptLoginId) && encryptLoginId.equalsIgnoreCase("1")) {
                        loginid = DES.decrypt(loginid);
                    }
                    identity = createCommonToken(loginid, loginid, "loginbyid", request, response);
                } else {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("systemMessages", "token" + "验证失败");
                    addCallBackMessageWithAjax(params, servletResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return identity;
        }
    }

    public static class LoginByUserinfoBuilder extends FrametIdentityBuilder {
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
                    String[] decUserInfoList = AESEncDec.decrypt(userinfo).split("&");
                    if (decUserInfoList.length < 2) {
                        params.put("systemMessages", "userinfo" + "验证失败");
                        addCallBackMessageWithAjax(params, servletResponse);
                    } else {
                        identity = createCommonToken(decUserInfoList[0], decUserInfoList[1], "0", servletRequest,
                                servletResponse);
                    }
                } else {
                    params.put("systemMessages", "userinfo" + "验证失败");
                    addCallBackMessageWithAjax(params, servletResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return identity;
        }
    }

    public static class LoginByADBuilder extends FrametIdentityBuilder {
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
                        username = DES.decrypt(username);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                identity = createCommonToken(username, username, "loginbyid", servletRequest, servletResponse);
            }
            return identity;
        }
    }

    public static class LoginByValidateCodeBuilder extends FrametIdentityBuilder {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            Identity identity = null;

            String username = "";
            String password = "";
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
                username = format(arr[0]);
                password = format(arr[1]);
                if (arr.length > 2) {
                    loadType = format(arr[2]);
                }
            }

            String commonDto = servletRequest.getParameter("commonDto");
            JSONArray array = null;
            try {
                array = JSONArray.parseArray(commonDto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<Component> components = new DtoTransfer().transfer(array);
            String validateCode = "";
            String inputCode = "";
            for (Component component : components) {
                if (component.getId().equals("validateCode")) {
                    validateCode = component.getValue().toString();
                }
                if (component.getId().equals("inputCode")) {
                    inputCode = component.getValue().toString();
                }
            }

            if (StringUtil.isBlank(validateCode) || StringUtil.isBlank(inputCode)
                    || !MD5Util.getMD5(inputCode.toUpperCase()).equals(validateCode)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("systemMessages", "你输入的验证码不正确,请重新输入");
                addCallBackMessageWithAjax(params, servletResponse);
            } else {
                identity = createCommonToken(username, password, loadType, servletRequest, servletResponse);
            }
            return identity;
        }
    }

    public static class LoginSecondBuilder extends FrametIdentityBuilder {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            Identity identity = null;

            String username = "";
            String password = "";
            String ouguid = "";
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
            }

            identity = createCommonToken(username, password, loadType, servletRequest, servletResponse);
            identity.getExtvalue().put("checkSecondLogin", "false");
            identity.getExtvalue().put("ouguid", ouguid);
            return identity;
        }
    }

    public static class LoginAutoBuilder extends FrametIdentityBuilder {
        @Override
        public boolean preBuild(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            // 判断是否登录
            if (Authenticator.isAuthenticated(servletRequest)) {
                // 已登录，跳转到首页 
                redirectWithAjax(SSOConfigEnvironment.SUCCESS_URL, servletResponse);
                // 标记流程走完
                return true;
            } else {
                // 未登录，返回用于加密的字符串
                RSAPublicKey publicKey = RSAUtils.getRSAPublicKey();
                String publicExponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
                String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
                Map<String, String> params = new HashMap<String, String>();
                params.put("publicExponent", publicExponent);
                params.put("modulus", modulus);
                addCallBackMessageWithAjax(params, servletResponse);
                // 标记流程走完
                return true;
            }
        }
    }

    public static class LogoutBuilder extends FrametIdentityBuilder {
        @Override
        public boolean preBuild(ServletRequest request, ServletResponse response) {
            HttpServletRequest servletRequest = WebUtils.toHttp(request);
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            IAuditOnlineRegister iAuditOnlineRegister = (IAuditOnlineRegister) ContainerFactory.getContainInfo().getComponent(IAuditOnlineRegister.class);
            String accountguid = ZwdtUserSession.getInstance("").getAccountGuid();
            AuditOnlineRegister onlineRegister = (AuditOnlineRegister) iAuditOnlineRegister.getRegisterByAccountguid(accountguid).getResult();
            IAuditOnlineVisitLog iAuditOnlineVisitLog = (IAuditOnlineVisitLog) ContainerFactory.getContainInfo().getComponent(IAuditOnlineVisitLog.class);
            AuditOnlineVisitLog auditOnlineVisitLog = new AuditOnlineVisitLog();
            auditOnlineVisitLog.setRowguid(UUID.randomUUID().toString());
            auditOnlineVisitLog.setVisitid(onlineRegister.getMobile());
            auditOnlineVisitLog.setVisitstate("登出系统");
            auditOnlineVisitLog.setVisitname(onlineRegister.getUsername());
            auditOnlineVisitLog.setVisituserguid(onlineRegister.getAccountguid());
            auditOnlineVisitLog.setVisitdate(new Date());
            auditOnlineVisitLog.setLoginmodel("0");
            iAuditOnlineVisitLog.addAuditOnlineVisitLog(auditOnlineVisitLog);
            if (Authenticator.isAuthenticated(new HttpServletRequest[]{servletRequest})) {
                Authenticator.logout();
                String url = SSOConfigEnvironment.LOGOUT_URL;
                //调用省网注销接口
                IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
                String AS_ISENABLE_DHNEWSSO = configservice.getFrameConfigValue("AS_ISENABLE_DHNEWSSO");
                if (StringUtil.isNotBlank(AS_ISENABLE_DHNEWSSO) && ZwfwConstant.CONSTANT_STR_ONE.equals(AS_ISENABLE_DHNEWSSO)) {
                    String areasign = request.getServerName().replace(".sd.gov.cn", "");
                    if ("localhost".equals(areasign)) {
                        areasign = "jizwfw";
                    }
                    String areaurl = ConfigUtil.getConfigValue("jnlogin", areasign);
                    areasign = StringUtil.isNotBlank(areaurl) ? areasign + "_" : "wt";
                    String appmark = ConfigUtil.getConfigValue("jnlogin1", areasign + "appmark");
                    url = com.epoint.common.zwdt.login.DhUser_HttpUtil.logout(appmark,areaurl + "/jnzwdt/" + url);
                }
                try {
                    servletResponse.sendRedirect(WebUtil.getRequestCompleteUrl(servletRequest, url));
                } catch (IOException var12) {
                    var12.printStackTrace();
                }

                return true;
            } else {
                return true;
            }
        }
    }

    /**
     * 大汉单点登录
     */
    public static class DHLoginBuilder extends FrametIdentityBuilder {
        @Override
        public Identity build(ServletRequest request, ServletResponse response) {
            HttpServletResponse servletResponse = WebUtils.toHttp(response);
            String ticket = request.getParameter("ticket");
            System.out.println("大汉票据ticket：" + ticket);
            String loginname = "";// 登录账号
            String name = "";// 用户名
            String mobile = "";// 手机号
            String cardid = "";// 证件号码
            String dhuuid = "";// 证件号码
            String authlevel = "";// 证件号码
            String corrole = "";// 办事人企业角色（0:经办人  1:管理员，2：法人）
            String corname = ""; // 企业名称
            String cornumber = ""; // 企业统一社会信用代码
            String corusercardid = "";
            String corusername = "";

            String areasign = request.getServerName().replace(".sd.gov.cn", "");
            System.out.println("登录域名：" + request.getServerName() + ">>区域标识：" + areasign);
            String areaurl = ConfigUtil.getConfigValue("jnlogin", areasign);
            areasign = StringUtil.isNotBlank(areaurl) ? areasign + "_" : "wt";
            String password = "";
            String loadType = ZwdtConstant.Dhlogin;

            String dhtoken = "";
            String usertype = "";
            String[] resultdata = null;
            JSONObject userbasic = null;

            IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            String AS_ISENABLE_DHNEWSSO = configservice.getFrameConfigValue("AS_ISENABLE_DHNEWSSO");
            log.info("AS_ISENABLE_DHNEWSSO"+AS_ISENABLE_DHNEWSSO);
            if (StringUtil.isNotBlank(AS_ISENABLE_DHNEWSSO) && ZwfwConstant.CONSTANT_STR_ONE.equals(AS_ISENABLE_DHNEWSSO)) {
                String decodekey = ConfigUtil.getConfigValue("jnlogin1", areasign + "appkey");
                String appmark = ConfigUtil.getConfigValue("jnlogin1", areasign + "appmark");
                JSONObject tokenrtn = com.epoint.common.zwdt.login.DhUser_HttpUtil.getTicketvalidate(appmark, decodekey, ticket, "0");
                if (tokenrtn != null) {
                    System.out.println("tokenrtn:"+tokenrtn.toJSONString());
                    log.info("tokenrtn:"+tokenrtn.toJSONString());
                    dhtoken = tokenrtn.getString("token");
                    usertype = tokenrtn.getString("usertype");
                    if (StringUtil.isNotBlank(dhtoken)) {
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(usertype)) {
                            userbasic = com.epoint.common.zwdt.login.DhUser_HttpUtil.getUserInfo(appmark, decodekey, "0", dhtoken);
                            System.out.println("userbasic:"+userbasic.toJSONString());
                            log.info("userbasic:"+userbasic.toJSONString());
                            cardid = userbasic.getString("certNo");
                            authlevel = userbasic.getString("authLevel");
                        } else {
                            userbasic = com.epoint.common.zwdt.login.DhUser_HttpUtil.getCoruserInfo(appmark, decodekey, "0", dhtoken);
                             System.out.println("userbasic:"+userbasic.toJSONString());
                            log.info("userbasic:"+userbasic.toJSONString());
                            cardid = userbasic.getString("certNo");//登录人身份证号码
                            corrole = userbasic.getString("corUserRole");//账号角色
                            corname = userbasic.getString("corporationName");//企业名称
                            cornumber = userbasic.getString("creditCode");//统一社会信用代码
                            corusercardid = userbasic.getString("corUserCertNo");//法定代表人证件号
                            corusername = userbasic.getString("corUserName");//法定代表人姓名
                            if (StringUtil.isNotBlank(cardid)) {
                                authlevel = "1";
                            }
                        }
                        loginname = userbasic.getString("loginName");
                        mobile = userbasic.getString("mobile");
                        name = userbasic.getString("name");
                        dhuuid = userbasic.getString("uuid");
                    }
                }
            } else {
                String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
                String decodekey = ConfigUtil.getConfigValue("jnlogin", areasign + "appkey");
                String appmark = ConfigUtil.getConfigValue("jnlogin", areasign + "appmark");
                String appword = ConfigUtil.getConfigValue("jnlogin", areasign + "appword");
                String wangzhi = ConfigUtil.getConfigValue("jnlogin", "dhrest");
                // 通过大汉提供的加密方式进行加密
                String sign = MD5Util.getMD5(appmark + appword + time);
                String urlparams = "appmark=" + appmark + "&time=" + time + "&sign=" + sign;
                Object obj2 = null;
                JSONObject resultObject = new JSONObject();
                //验证票据
                try {
                    dhtoken = DhUser_HttpUtil.getDHtoken(wangzhi, ticket, decodekey, urlparams);
                     System.out.println("dhtoken:"+dhtoken);
                    log.info("dhtoken:"+dhtoken);
                    if (dhtoken.contains("errormsg>>")) {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("systemMessages", dhtoken);
                        addCallBackMessageWithAjax(params, servletResponse);
                        return null;
                    }
                    obj2 = DhUser_HttpUtil.getUserinfoByDhrest(wangzhi, dhtoken, decodekey, urlparams, false);
                    System.out.println("obj2:"+obj2.toString());
                    log.info("obj2:"+obj2.toString());
                    resultdata = obj2.toString().split("#COMPANY#");
                    resultObject = JSONObject.parseObject(resultdata[0]);
                    System.out.println("resultObject:"+resultObject.toJSONString());
                    log.info("resultObject:"+resultObject.toJSONString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (resultObject.isEmpty() || !"000000".equals(resultObject.getString("retcode"))) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("systemMessages", resultObject.toJSONString());
                    addCallBackMessageWithAjax(params, servletResponse);
                    return null;
                } else {
                    String userdata = resultObject.getString("data");
                    if (StringUtil.isNotBlank(userdata)) {
                        JSONObject userobj = JSONObject.parseObject(userdata);
                        if (userobj.containsKey("loginname")) {// 登录名
                            loginname = userobj.getString("loginname");
                            mobile = userobj.getString("mobile");
                            cardid = userobj.getString("cardid");
                            name = userobj.getString("name");
                            authlevel = userobj.getString("authlevel");
                            dhuuid = userobj.getString("uuid");
                        }

                    }
                }

            }


            String cmdparams = WebUtils.getCleanParam(request, "cmdParams");
            if (StringUtil.isNotBlank(cmdparams)) {
                if (cmdparams.startsWith("[")) {
                    cmdparams = cmdparams.substring(1);
                }
                if (cmdparams.endsWith("]")) {
                    cmdparams = cmdparams.substring(0, cmdparams.length() - 1);
                }
                String[] arr = cmdparams.split(",");
                loginname = format(arr[0]);
                password = format(arr[1]);
                if (arr.length > 2) {
                    loadType = format(arr[2]);
                }
            }
            System.out.println("loginname:"+loginname+","+password+","+loadType+",");
            // 封装请求参数
            Identity identity = createCommonToken(loginname, password, loadType, request, response);
            identity.setName("zwdtjnlogin");
            identity.put("name", name);
            identity.put("mobile", mobile);
            identity.put("cardid", cardid);
            identity.put("dhtoken", dhtoken);
            identity.put("dhuuid", dhuuid);
            identity.put("authlevel", authlevel);
            identity.put("corrole", corrole);
            identity.put("corname", corname);
            identity.put("cornumber", cornumber);
            identity.put("corusercardid", corusercardid);
            identity.put("corusername", corusername);
            identity.put("areasign", areasign);
            identity.put("usertype", usertype);
            log.info("identity"+identity.toString());
            if (resultdata != null && resultdata.length == 2 && StringUtil.isNotBlank(resultdata[1])) {
                JSONObject compobj = JSONObject.parseObject(resultdata[1]);
                System.out.println("compobj:"+compobj.toJSONString());
                if ("000000".equals(compobj.getString("retcode"))) {// 登录名
                    JSONArray compobjdata = compobj.getJSONArray("data");
                    if (compobjdata != null && compobjdata.size() > 0) {
                        identity.put("compobjdata", compobjdata);
                        identity.put("hascomp", "1");
                    }
//                	if(compobjdata.containsKey("creditcode")) {
//                		identity.put("creditcode", compobjdata.getString("creditcode"));
//                		identity.put("regorg", compobjdata.getString("regorg"));
//                		identity.put("cardnumber", compobjdata.getString("cardnumber"));
//                		identity.put("regaddress", compobjdata.getString("regaddress"));
//                		identity.put("realname", compobjdata.getString("realname"));
//                		identity.put("corname", compobjdata.getString("corname"));
//                	}
                }
            }

            return identity;
        }

    }

}
