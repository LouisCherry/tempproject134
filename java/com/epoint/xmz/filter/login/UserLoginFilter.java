package com.epoint.xmz.filter.login;

import com.epoint.basic.authentication.UserSession;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.user.entity.FrameUserExtendInfo;
import com.epoint.xmz.filter.login.api.ICommonUserService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class UserLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String statue = request.getParameter("statue");
        if (request.getRequestURL().indexOf("dream/dream") != -1 && request.getRequestURL().indexOf("js") == -1 &&
                request.getRequestURL().indexOf("css") == -1 && StringUtil.isBlank(statue)) {
            // 需要登录的帐号
            String loginid = UserSession.getInstance().getLoginID();
            // 需访问的地址
            String url = ConfigUtil.getFrameConfigValue("systemaddress");
            //个人信息维护地址
            String gotourl;
            ICommonUserService commonUserService = ContainerFactory.getContainInfo().getComponent(ICommonUserService.class);
            IUserService userService = ContainerFactory.getContainInfo().getComponent(IUserService.class);
            //判断如果这个登录用户没有进行个人信息维护跳转个人信息维护页
            FrameUser frameUser = userService.getUserByUserField("loginid", loginid);
            FrameUserExtendInfo extendInfo = commonUserService.getExtendinfoByLoginid(loginid);
            String identityCardNum = extendInfo.getIdentityCardNum();
            Date birthday = extendInfo.getBirthday();
            String education = extendInfo.getStr("education");
            String politicaloutlook = extendInfo.getStr("politicaloutlook");
            String academicdegree = extendInfo.getStr("academicdegree");
            String officelocation = extendInfo.getStr("officelocation");
            String type = extendInfo.getStr("type");
            String sex = frameUser.getSex();
            String title = frameUser.getTitle();
            String telephoneOffice = frameUser.getTelephoneOffice();
            String mobile = frameUser.getMobile();
            if (StringUtil.isNotBlank(identityCardNum) && StringUtil.isNotBlank(birthday) && StringUtil.isNotBlank(sex) && StringUtil.isNotBlank(education) && StringUtil.isNotBlank(politicaloutlook)
                    && StringUtil.isNotBlank(academicdegree) && StringUtil.isNotBlank(officelocation) && StringUtil.isNotBlank(type) && StringUtil.isNotBlank(title) && StringUtil.isNotBlank(telephoneOffice)
                    && StringUtil.isNotBlank(mobile)) {
                gotourl = url;
            } else {
                gotourl = ConfigUtil.getFrameConfigValue("personalinfo_url");
            }
            response.sendRedirect(gotourl);
            return;
        }


        chain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {

    }

}
