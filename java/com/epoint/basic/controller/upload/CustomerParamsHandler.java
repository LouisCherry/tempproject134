package com.epoint.basic.controller.upload;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import com.epoint.authenticator.Constants;
import com.epoint.authenticator.asutils.SSOConfigEnvironment;
import com.epoint.authenticator.asutils.SSOWebUtils;
import com.epoint.authenticator.params.DefaultOAuth2ParamsHandler;
import com.epoint.authenticator.params.OAuth2ParamsBuilder;

public class CustomerParamsHandler extends DefaultOAuth2ParamsHandler
{
    @Override
    public Map<String, String> buildCodeParams(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURL().toString();
        String uri = req.getRequestURI();
        String path = url.substring(0, url.indexOf(uri));
        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");

        int index = 0;

        for (int i = 0; i < redirectUrls.length; i++) {
            String item = redirectUrls[i];
            if (item.startsWith(path)) {
                index = i;
            }
        }

        String redirectUrl = redirectUrls[index];

        String newRedirectUrl = "";

        // 判断是否是ajax请求，如果是，则回掉路径是配置的回掉路径
        String requestType = (String) req.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(requestType) && requestType.equals("XMLHttpRequest")) {
            newRedirectUrl = redirectUrl;
        }
        else {
            // 拼接实际的回掉地址
            newRedirectUrl = SSOWebUtils.parseUrlPrefix(redirectUrl, req.getRequestURI());
            newRedirectUrl = newRedirectUrl + (req.getQueryString() == null ? "" : "?" + req.getQueryString());
        }

        OAuth2ParamsBuilder builder = OAuth2ParamsBuilder.oauth2Params();
        builder.setClientId(SSOConfigEnvironment.CLIENT_ID).setRedirectUrl(newRedirectUrl)
                .setScope(SSOConfigEnvironment.SCOPE).setDisplay(SSOConfigEnvironment.DISPLAY)
                .setResponseType(ResponseType.CODE);
        return builder.buildMap();
    }

    @Override
    public String resolveAuthorizePath(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURL().toString();
        String uri = req.getRequestURI();
        String path = url.substring(0, url.indexOf(uri));
        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");
        String[] ssoServerUrls = SSOConfigEnvironment.SERVER_URL.split(",");

        int index = 0;

        for (int i = 0; i < redirectUrls.length; i++) {
            String item = redirectUrls[i];
            if (item.startsWith(path)) {
                index = i;
            }
        }

        String ssoServerUrl = ssoServerUrls[index];

        return ssoServerUrl + "/" + Constants.OAUTH_ROUTE_PREFIX + "/" + "authorize";
    }

    //    @Override
    //    public String resolveTokenPath(ServletRequest request) {
    //        HttpServletRequest req = (HttpServletRequest) request;
    //        String url = req.getRequestURL().toString();
    //        String uri = req.getRequestURI();
    //        String path = url.substring(0, url.indexOf(uri));
    //        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");
    //        String[] ssoServerUrls = SSOConfigEnvironment.SERVER_INTRANET_URL.split(",");
    //
    //        int index = 0;
    //
    //        for (int i = 0; i < redirectUrls.length; i++) {
    //            String item = redirectUrls[i];
    //            if (item.startsWith(path)) {
    //                index = i;
    //            }
    //        }
    //
    //        String ssoServerUrl = ssoServerUrls[index];
    //
    //        return ssoServerUrl + "/" + Constants.OAUTH_ROUTE_PREFIX + "/" + "token";
    //    }
    //
    //    @Override
    //    public String resolveLoginIDPath(ServletRequest request) {
    //        HttpServletRequest req = (HttpServletRequest) request;
    //        String url = req.getRequestURL().toString();
    //        String uri = req.getRequestURI();
    //        String path = url.substring(0, url.indexOf(uri));
    //        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");
    //        String[] ssoServerUrls = SSOConfigEnvironment.SERVER_INTRANET_URL.split(",");
    //
    //        int index = 0;
    //
    //        for (int i = 0; i < redirectUrls.length; i++) {
    //            String item = redirectUrls[i];
    //            if (item.startsWith(path)) {
    //                index = i;
    //            }
    //        }
    //
    //        String ssoServerUrl = ssoServerUrls[index];
    //
    //        return ssoServerUrl + "/" + Constants.OAUTH_ROUTE_PREFIX + "/" + "loginid";
    //    }
    //
    //    @Override
    //    public String resolveCheckTokenPath(ServletRequest request) {
    //        HttpServletRequest req = (HttpServletRequest) request;
    //        String url = req.getRequestURL().toString();
    //        String uri = req.getRequestURI();
    //        String path = url.substring(0, url.indexOf(uri));
    //        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");
    //        String[] ssoServerUrls = SSOConfigEnvironment.SERVER_INTRANET_URL.split(",");
    //
    //        int index = 0;
    //
    //        for (int i = 0; i < redirectUrls.length; i++) {
    //            String item = redirectUrls[i];
    //            if (item.startsWith(path)) {
    //                index = i;
    //            }
    //        }
    //
    //        String ssoServerUrl = ssoServerUrls[index];
    //
    //        return ssoServerUrl + "/" + Constants.OAUTH_ROUTE_PREFIX + "/" + "checktoken";
    //    }

    @Override
    public String resolveLogoutPath(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURL().toString();
        String uri = req.getRequestURI();
        String path = url.substring(0, url.indexOf(uri));
        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");
        String[] ssoServerUrls = SSOConfigEnvironment.SERVER_URL.split(",");

        int index = 0;

        for (int i = 0; i < redirectUrls.length; i++) {
            String item = redirectUrls[i];
            if (item.startsWith(path)) {
                index = i;
            }
        }

        String ssoServerUrl = ssoServerUrls[index];

        return ssoServerUrl + "/" + Constants.OAUTH_ROUTE_PREFIX + "/" + "logout";
    }

    @Override
    public String resolveSuccessUrl(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURL().toString();
        String uri = req.getRequestURI();
        String path = url.substring(0, url.indexOf(uri));
        String[] redirectUrls = SSOConfigEnvironment.REDIRECT_URL.split(",");
        String[] ssoSuccessUrls = SSOConfigEnvironment.SUCCESS_URL.split(",");

        int index = 0;

        for (int i = 0; i < redirectUrls.length; i++) {
            String item = redirectUrls[i];
            if (item.startsWith(path)) {
                index = i;
            }
        }

        String ssoServerUrl = ssoSuccessUrls[index];

        return ssoServerUrl;
    }
}
