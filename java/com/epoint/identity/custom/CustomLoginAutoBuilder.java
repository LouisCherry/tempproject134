package com.epoint.identity.custom;

import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.identity.Identity;
import com.epoint.basic.shiro.login.IdentityBuilderWrapper.LoginAutoBuilder;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2ClientUtil;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class CustomLoginAutoBuilder extends LoginAutoBuilder
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
            //saveSession(request, response);
            redirectWithAjax(page, servletResponse);
            // 标记流程走完
            return true;
        }
        else {
            // 未登录，返回用于加密的字符串
            Map<String, String> params = new HashMap<String, String>();
            params.put("publicExponent",
                    "00be7347f0bbb16d0975b3997c7132aff1c482102af1902fabcfaf42197cc2fa84a7dd28e93948866ef2bb54fb0d0938be13cb3125fae36e6a921a21e54e63fe8ad8e9b6afb7411b901a18c911991a2c787e5c4a32fa05b1521663c522588c038394832d038ee5f8e86c4759cbd939ecdd75f75ceb6b3a9fe05ea50b2ff9e8e7fd");
            params.put("modulus", "010001");
            params.put("sm2PublicKey", SM2ClientUtil.getInstance().getSm2PubKey());
            addCallBackMessageWithAjax(params, servletResponse);
            // 标记流程走完
            return true;
        }
    }

}
