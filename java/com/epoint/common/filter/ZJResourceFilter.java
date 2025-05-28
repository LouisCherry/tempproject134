package com.epoint.common.filter;

import com.epoint.core.utils.security.TokenUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * F9路径资源处理Filter 自动查找F9 html路径，如果没有找xhtml路径 ，支持原先的JSF架构 自动匹配资源的绝对路径引用
 *
 * @author Jeffrey Zhou
 * @version [版本号, 2016年1月17日]
 */
public class ZJResourceFilter implements Filter {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void init(FilterConfig arg0) throws ServletException {

        // String path = arg0.getServletContext().getRealPath("/");
        // Functions.setWebRootPath(path); // 设置系统根路径
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
            throws IOException, ServletException {
        if (!(req instanceof HttpServletRequest) || !(resp instanceof HttpServletResponse)) {
            filterChain.doFilter(req, resp);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // String refere = request.getHeader("Referer");
        String Url = request.getRequestURI();
        if (Url.contains("authentication")) {
            String user = request.getParameterValues("user")[0];
            String password = request.getParameterValues("password")[0];
            if (StringUtil.isNotBlank(user) && StringUtil.isNotBlank(password)) {
                Url = Url.replace("authentication", "rest/audittaskzjauthaction/auth?isCommondto=true&user=" + user + "&password=" + password);
                response.sendRedirect(Url);
                return;
            }
        }
        if (Url.contains("getApproveItem") && request.getParameterValues("access_token") != null && request.getParameterValues("project_code") != null && request.getParameterValues("item_instance_code") != null) {
            String token = request.getParameterValues("access_token")[0];
            String project_code = request.getParameterValues("project_code")[0];
            String item_instance_code = request.getParameterValues("item_instance_code")[0];
            if (StringUtil.isNotBlank(token) && TokenUtil.validateToken(token)) {
                Url = Url.replace("getApproveItem", "epointzwfw/auditsp/auditspdetail/index?project_code=" + project_code + "&item_instance_code=" + item_instance_code + "&access_token=" + token);
                response.sendRedirect(Url);
//                Url = Url.replace("getApproveItem", "epointzwfw/auditsp/auditspdetail/index");
//               request.getRequestDispatcher(Url).forward(req, resp);
                return;
            }
        }
        if (Url.contains("getProjectDoc") && request.getParameterValues("access_token") != null && request.getParameterValues("doc_id") != null) {
            String token = request.getParameterValues("access_token")[0];
            String doc_id = request.getParameterValues("doc_id")[0];
            if (StringUtil.isNotBlank(token) && TokenUtil.validateToken(token)) {
                Url = Url.replace("getProjectDoc", "rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=" + doc_id);
                response.sendRedirect(Url);
//                Url = Url.replace("getApproveItem", "epointzwfw/auditsp/auditspdetail/index");
//               request.getRequestDispatcher(Url).forward(req, resp);
                return;
            }
        }
        filterChain.doFilter(req, resp);
    }
}
