package com.epoint.xmz.filter.apimanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import com.alibaba.fastjson.JSONObject;
import com.epoint.apimanage.event.basic.api.IEventDispatchService;
import com.epoint.cert.commonutils.RecordUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.log4j.Logger;

import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import org.atmosphere.util.IOUtils;
import org.atmosphere.util.uri.UriComponent;

/**
 * 资源过滤器,承担下面几个责任 1、对静态资源cssboot、jsboot进行版本控制 2、对pages目录的页面访问进行截取处理 3、页面访问去除后缀处理
 * 
 * @author xjh
 * @version [版本号, 2018年2月23日]
 */
public class ApiManagerSendFilter implements Filter
{



    transient Logger log = LogUtil.getLog(ApiManagerSendFilter.class);


    public ApiManagerSendFilter () {

    }


    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;

        String uri = WebUtil.getRequestURI(req);

        // 判断请求包含rest
        if (uri.contains("rest")) {
            ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo().getComponent(ICodeItemsService.class);
            // 判断是否再apimanager注册
            String apino = iCodeItemsService.getItemTextByCodeName("APIManager注册映射", uri);
            if (StringUtil.isBlank(apino)) {
                chain.doFilter(request, response);
                return;
            }
            IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            // 获取apimanager地址
            String apimanagerurl = iConfigService.getFrameConfigValue("apimanagerurl");

            // 获取业务参数
            String params = getParamJson(request);
            IEventDispatchService iEventDispatchService = ContainerFactory.getContainInfo().getComponent(IEventDispatchService.class);
            // 转发请求
            try {
                Record result = iEventDispatchService.execute(apimanagerurl, apino, params);
                JSONObject resultjson = new JSONObject();
                if (result != null) {
                    Set<String> keyset = result.keySet();
                    for (String key : keyset) {
                        resultjson.put(key, result.get(key));
                    }
                }
                // 直接返回结果数据
                rep.setCharacterEncoding("UTF-8");
                // 直接响应一个200
                rep.setStatus(200);
                try {
                    PrintWriter out = rep.getWriter();
                    out.print(resultjson.toJSONString());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                rep.setStatus(500);
                PrintWriter out = rep.getWriter();
                out.print("{\"error\":\"系统出现了一点故障，请联系系统管理员进行检修处理!\",\"stackError\":\"\", \"_common_hidden_viewdata\":{\"replayAttack\":\"\"}}");
                out.close();
            }
            return;
        }

        chain.doFilter(request, response);
    }





    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
    }

    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
    }

    public String getParamJson (ServletRequest request) {
        InputStream inputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String params = "";
        try {
            inputStream = request.getInputStream();
            int len = -1;
            byte[] buf = new byte[128];
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            params = new String(out.toByteArray(), "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        finally {
            try {
                inputStream.close();
                out.close();
            }
            catch (Exception e) {

            }
        }
        return params;
    }
}
