package com.epoint.common.filter;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.YwztUtils;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.epoint.basic.auditauth.auditonlineappregister.domain.AuditOnlineAppregister;
import com.epoint.basic.auditauth.auditonlineappregister.inter.IAuditOnineAppregister;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.spring.util.SpringContextUtil;
import sun.misc.BASE64Encoder;

/**
 * 
 * @Description :控制大汉单点登录拦截器
 *  
 * @author male
 * @version [版本号, 2019年3月26日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DhloginFilter implements Filter
{
    private static ResourceHttpRequestHandler resourceHttpRequestHandler = null;

    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (resourceHttpRequestHandler == null) {
            resourceHttpRequestHandler = SpringContextUtil.getBeanByType(ResourceHttpRequestHandler.class);
        }

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        String reqUrl = req.getRequestURL().toString();
        boolean next = true;
        String url = WebUtil.getRequestURI(req);

        //设置放行的网页
        if (url.contains("tazwdt/pages/projectprocess") || url.contains("rest")
                || url.contains("tazwdt/pages/tzxm/guide_default")) {
            //String rediretPage = ConfigUtil.getConfigValue("taconfig", "access") + url;
            chain.doFilter(request, response);
            return;
        }

        //TODO:这边暂时处理，如果是原首页统一跳到工建
        /*if (url.contains("epointzwdt/pages/default/index")) {
            rep.sendRedirect("http://117.73.252.120:8082/epoint-web-zwdt/tazwdt/pages/tzxm/guide_default");
        }*/

        HttpSession session = req.getSession();
        //這邊判斷是否登录
        Object sessionObjImp = session.getAttribute("SessionObjImp");
        if (sessionObjImp != null) {
            if (!"{}".equals(sessionObjImp.toString())) {
                //已經登陸
                if (url.equals("epointzwdt/pages/account/dhlogin")) {
                    //獲取gotourl
                    String gotoUrl = req.getParameter("gotourl");
                    gotoUrl = URLDecoder.decode(gotoUrl, "UTF-8");
                    if (StringUtil.isNotBlank(gotoUrl)) {
                        //不为空则跳转这个页面
                        rep.sendRedirect(Base64Util.decode(gotoUrl));
                    }
                    else {
                        //为空的话跳转系统首页 localhost:8070  117.73.252.120:8082/
                        rep.sendRedirect("http://jijxxzwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/default/index");
                    }
                }
                //如果是跳转申报须知页面
                if(url.contains("epointzwmhwz/pages/onlinedeclaration/onlinedeclaration") || url.contains("onlinedeclaration/legalonlinedeclaration")){
                    log.info(url);
                    Map<String, String[]> pramMap = req.getParameterMap();
                    log.info(pramMap.toString());
                    if (pramMap != null && pramMap.containsKey("inner_code")) {
                        String inner_code = pramMap.get("inner_code")[0];
                        IAuditTask iAuditTask = (IAuditTask) ContainerFactory
                                .getContainInfo().getComponent(IAuditTask.class);
                        IJnAppRestService iJnAppRestService= (IJnAppRestService) ContainerFactory
                                .getContainInfo().getComponent(IJnAppRestService.class);

                        if (StringUtil.isNotBlank(inner_code)) {
                            String taskGuid = iJnAppRestService.getTaskguidByInnercode(inner_code).getResult();
                            log.info("输出对应的事项信息："+taskGuid);
                            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                            //判断是否是中台事项
                            if (auditTask!=null && "1".equals(auditTask.getStr("ywztcode"))) {
                                String result = YwztUtils.getWeburl(taskGuid);
                                log.info("result:"+result);
                                JSONObject resultjson = JSONObject.parseObject(result);
                                if(resultjson!=null){
                                    JSONObject customjson  = resultjson.getJSONObject("custom");
                                    String weburl = customjson.getString("weburl");
                                    //如果是，则直接跳转中台
                                    if(StringUtils.isNotBlank(weburl)){
                                        weburl = Base64Util.encode(weburl);
//                                        weburl = weburl.replace("=", "$");
                                        //##### 原先逻辑
//                                        String gotourl = "http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/carveoutnew/dhlogintransferticket.html?url=" + weburl;
//                                        rep.sendRedirect("https://tysfrz.isdapp.shandong.gov.cn/jpaas-jis-sso-server/sso/entrance/auth-center?appMark=JNSZWFWW&userType=1&backUrl=" + gotourl);
                                        //##### 原先逻辑

                                        //##### 使用省里办事指南逻辑
                                        rep.sendRedirect("http://www.shandong.gov.cn/api-gateway/jpaas-juspace-web-sdywtb/front/redirect/toRedirect?redirectUrl=" + weburl+"&type=1");

                                        //##### 使用省里办事指南逻辑

                                        return;
                                    }
                                }
                            }

                        }
                    }

                }
                chain.doFilter(request, response);
                return;
            }

        }
        //如果是跳转申报须知页面
        if(url.contains("epointzwmhwz/pages/onlinedeclaration/onlinedeclaration") || url.contains("onlinedeclaration/legalonlinedeclaration")){
            log.info(url);
            Map<String, String[]> pramMap = req.getParameterMap();
            log.info(pramMap.toString());
            if (pramMap != null && pramMap.containsKey("inner_code")) {
                String inner_code = pramMap.get("inner_code")[0];
                IAuditTask iAuditTask = (IAuditTask) ContainerFactory
                        .getContainInfo().getComponent(IAuditTask.class);
                IJnAppRestService iJnAppRestService= (IJnAppRestService) ContainerFactory
                        .getContainInfo().getComponent(IJnAppRestService.class);

                if (StringUtil.isNotBlank(inner_code)) {
                    String taskGuid = iJnAppRestService.getTaskguidByInnercode(inner_code).getResult();
                    log.info("输出对应的事项信息："+taskGuid);
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    //判断是否是中台事项
                    if (auditTask!=null && "1".equals(auditTask.getStr("ywztcode"))) {
                        String result = YwztUtils.getWeburl(taskGuid);
                        log.info("result:"+result);
                        JSONObject resultjson = JSONObject.parseObject(result);
                        if(resultjson!=null){
                            JSONObject customjson  = resultjson.getJSONObject("custom");
                            String weburl = customjson.getString("weburl");
                            //如果是，则直接跳转中台
                            if(StringUtils.isNotBlank(weburl)){
                                weburl = Base64Util.encode(weburl);
//                                weburl = weburl.replace("=", "$");
                                //##### 原先逻辑
//                                        String gotourl = "http://jizwfw.sd.gov.cn/jnzwdt/epointzwmhwz/pages/carveoutnew/dhlogintransferticket.html?url=" + weburl;
//                                        rep.sendRedirect("https://tysfrz.isdapp.shandong.gov.cn/jpaas-jis-sso-server/sso/entrance/auth-center?appMark=JNSZWFWW&userType=1&backUrl=" + gotourl);
                                //##### 原先逻辑

                                //##### 使用省里办事指南逻辑
                                rep.sendRedirect("http://www.shandong.gov.cn/api-gateway/jpaas-juspace-web-sdywtb/front/redirect/toRedirect?redirectUrl=" + weburl+"&type=1");

                                //##### 使用省里办事指南逻辑
                                return;
                            }
                        }
                    }

                }
            }

        }
        if (WebUtil.isPageRequest(url)) {

            String isDhlogin = ConfigUtil.getConfigValue("isDhlogin");
            if ("1".equals(isDhlogin)) {
                //获取需要忽略请求的关键字
                String uncheckUrls = ConfigUtil.getConfigValue("dhlogin", "skipurl");
                List<String> urls = Arrays.asList(uncheckUrls.split(","));
                int skipcount = 0;

                for (int i = 0; i < urls.size(); ++i) {
                    if (url.contains(urls.get(i))) {
                        ++skipcount;
                    }
                }

                boolean hasticket = false;
                Map<String, String[]> pramMap;
                String gotoUrl;
                String ticket;
                String decodegotoUrl;
                String rediretPage;
                String zwdtcallbackurl;

                //如果不在忽略名单中
                if (url.contains("dhlogin")) {
                    pramMap = req.getParameterMap();
                    if (pramMap != null && pramMap.containsKey("gotourl") && pramMap.containsKey("ticket")) {
                        gotoUrl = pramMap.get("gotourl")[0];
                        ticket = pramMap.get("ticket")[0];

                        //System.out.println(Base64Util.decode(gotoUrl));

                        if (StringUtil.isNotBlank(gotoUrl) && gotoUrl.contains("zwdtuuid")) {
                            gotoUrl = URLDecoder.decode(gotoUrl, "UTF-8");
                            String[] strs = gotoUrl.split("zwdtuuid=");
                            rediretPage = strs[1];
                            if (rediretPage.contains("&")) {
                                rediretPage = rediretPage.split("&")[0];
                            }

                            IAuditOnineAppregister iAuditOnineAppregister = (IAuditOnineAppregister) ContainerFactory
                                    .getContainInfo().getComponent(IAuditOnineAppregister.class);
                            AuditOnlineAppregister auditOnineAppregister = iAuditOnineAppregister
                                    .selectAuditOnlineAppregisterByUUID(rediretPage).getResult();
                            zwdtcallbackurl = "";
                            if (auditOnineAppregister != null) {
                                zwdtcallbackurl = auditOnineAppregister.getUrl();
                            }

                            gotoUrl = URLEncoder.encode(gotoUrl, "UTF-8");
                            zwdtcallbackurl = zwdtcallbackurl + "?gotourl=" + gotoUrl;
                            zwdtcallbackurl = Base64Util.encode(zwdtcallbackurl);
                            rep.sendRedirect(ConfigUtil.getConfigValue("dhlogin", "webappurl")
                                    + "/epointzwdt/pages/account/dhlogin?ticket=" + ticket + "&callbackgotourl="
                                    + zwdtcallbackurl);
                            return;
                        }

                        if (StringUtil.isNotBlank(gotoUrl)) {
                            decodegotoUrl = "";

                            try {
                                decodegotoUrl = Base64Util.decode(gotoUrl);
                            }
                            catch (Exception var24) {
                                var24.printStackTrace();
                            }

                            if (StringUtil.isNotBlank(decodegotoUrl) && decodegotoUrl.contains("zwdtuuid")) {
                                String[] strs = decodegotoUrl.split("zwdtuuid=");
                                String uuid = strs[1];
                                if (uuid.contains("&")) {
                                    uuid = uuid.split("&")[0];
                                }

                                IAuditOnineAppregister iAuditOnineAppregister = (IAuditOnineAppregister) ContainerFactory
                                        .getContainInfo().getComponent(IAuditOnineAppregister.class);
                                AuditOnlineAppregister auditOnineAppregister = iAuditOnineAppregister
                                        .selectAuditOnlineAppregisterByUUID(uuid).getResult();
                                zwdtcallbackurl = "";
                                if (auditOnineAppregister != null) {
                                    zwdtcallbackurl = auditOnineAppregister.getUrl();
                                }

                                gotoUrl = URLEncoder.encode(gotoUrl, "UTF-8");
                                String callbackgotourl = zwdtcallbackurl + "?gotourl=" + gotoUrl;
                                callbackgotourl = Base64Util.encode(callbackgotourl);
                                rep.sendRedirect(ConfigUtil.getConfigValue("dhlogin", "webappurl")
                                        + "/epointzwdt/pages/account/dhlogin?ticket=" + ticket + "&callbackgotourl="
                                        + callbackgotourl);
                                return;
                            }
                        }
                    }
                }

                if (skipcount == 0) {
                    pramMap = req.getParameterMap();
                    if (pramMap != null && pramMap.containsKey("ticket")) {
                        gotoUrl = pramMap.get("ticket")[0];
                        hasticket = true;
                        ticket = "";
                        Iterator<Entry<String, String[]>> var31 = pramMap.entrySet().iterator();

                        while (var31.hasNext()) {
                            Entry<String, String[]> entry = var31.next();
                            if (!"ticket".equals(entry.getKey())) {
                                ticket = ticket + entry.getKey() + "=" + entry.getValue()[0] + "&";
                            }
                        }

                        if (StringUtil.isNotBlank(ticket)) {
                            ticket = "?" + ticket.substring(0, ticket.length() - 1);
                        }
                        //TODO：这边做一个替换 如果为ip替换成域名
                        /*if (reqUrl.contains("http://117.73.252.120:8082")) {
                            reqUrl = reqUrl.replace("http://117.73.252.120:8082", "http://tazwfw.sd.gov.cn");
                        }*/
                        decodegotoUrl = Base64Util.encode(reqUrl + ticket);
                        rediretPage = URLEncoder.encode(decodegotoUrl, "UTF-8");

                        //System.out.println(rediretPage);
                        rep.sendRedirect(ConfigUtil.getConfigValue("dhlogin", "webappurl")
                                + "/epointzwdt/pages/account/dhlogin?ticket=" + gotoUrl + "&gotourl=" + rediretPage);
                        return;
                    }
                }

                if (skipcount == 0 && !hasticket) {
                    String action = "";
                    pramMap = req.getParameterMap();
                    if (pramMap != null) {
                        Iterator<Entry<String, String[]>> var28 = pramMap.entrySet().iterator();

                        while (var28.hasNext()) {
                            Entry<String, String[]> entry = var28.next();
                            if (!"ticket".equals(entry.getKey())) {
                                action = action + entry.getKey() + "=" + entry.getValue()[0] + "&";
                            }
                        }

                        if (StringUtil.isNotBlank(action)) {
                            action = "?" + action.substring(0, action.length() - 1);
                        }
                    }

                    ticket = Base64Util.encode(reqUrl + action);
                    decodegotoUrl = URLEncoder.encode(ticket, "UTF-8");
                    //判断未登录则跳到统一登录页面
                    rediretPage = ConfigUtil.getConfigValue("dhlogin", "dhrest") + "front/login.do?appmark="
                            + ConfigUtil.getConfigValue("dhlogin", "dhappmark") + "&uuid="
                            + ConfigUtil.getConfigValue("dhlogin", "dhuuid") + "&gotourl=" + decodegotoUrl;
                    boolean islogin = false;
                    req.getSession().getAttributeNames();
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> map = (HashMap<String, Object>) req.getSession()
                            .getAttribute("SessionObjImp");
                    if (map != null) {
                        ZwdtUserSession zwdtUserSession = (ZwdtUserSession) map.get("zwdtusersession");
                        if (zwdtUserSession != null) {
                            zwdtcallbackurl = zwdtUserSession.getAccountGuid();
                            if (StringUtil.isNotBlank(zwdtcallbackurl)) {
                                islogin = true;
                            }
                        }
                    }

                    if (!islogin) {
                        rep.sendRedirect(rediretPage);
                        return;
                    }
                }
            }
        }

        if (next) {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
