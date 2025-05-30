package com.epoint.zwdt.zwdtrest.user;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.asutils.SSOConfigEnvironment;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineverycode.domain.AuditOnlineVerycode;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;

@RestController
@RequestMapping("/jnzwdtLogin")
public class JnLoginRestController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 网上注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

 
  

    /**
     * 判断用户是否登录的接口
     * 
     * @param params 接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/isLogin", method = RequestMethod.POST)
    public String isLogin(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用isLogin接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取用户session
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 4、根据session判断用户是否登录
                if (auditOnlineRegister == null) {
                    // 4.1、未登录或者登录session过期，要求重新登录
                    return JsonUtils.zwdtRestReturn("0", "用户未登录或者登录过期！", "");
                }
                else {
                    // 4.2、若用户已登录
                    String profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/css/images/step2.png";
                    if (StringUtil.isNotBlank(auditOnlineRegister.getProfilePic())) {
                        profilePicUrl = WebUtil.getRequestCompleteUrl(request)
                                + "/rest/auditattach/readAttach?attachguid=" + auditOnlineRegister.getProfilePic();
                    }
                    dataJson.put("profilepicurl", profilePicUrl); // 用户头像地址
                    dataJson.put("mobile", auditOnlineRegister.getMobile()); // 手机号
                    dataJson.put("name", auditOnlineRegister.getUsername()); // 用户姓名
                    dataJson.put("usertype", auditOnlineRegister.getUsertype()); // 用户姓名
                    dataJson.put("accountguid", auditOnlineRegister.getAccountguid()); // 用户唯一标识
                    dataJson.put("loginouturl", SSOConfigEnvironment.LOGOUT_URL); //配置的默认登出地址
                    dataJson.put("authlevel", auditOnlineRegister.getStr("authlevel"));
                    dataJson.put("dhuuid", auditOnlineRegister.getStr("dhuuid"));
                    return JsonUtils.zwdtRestReturn("1", "用户登录成功！", dataJson.toString());
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.error("====Exception信息====" + e.getMessage());
            log.info("=======isLogin接口参数：params【" + params + "】=======");
            log.info("=======isLogin异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断用户是否登录失败：" + e.getMessage(), "");
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
        	String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }else {
            	auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
            			.getResult();
            }
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 设置验证码有效期
     * 
     * @param verycode 验证码实体
     * @param time 有效期时间分钟
     * @return
     */
    public Date setValidTime(AuditOnlineVerycode auditOnlineVerycode, int time) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(auditOnlineVerycode.getInsertime());
        instance.add(Calendar.MINUTE, time);
        return instance.getTime();
    }
}
