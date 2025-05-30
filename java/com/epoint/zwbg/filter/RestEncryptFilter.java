package com.epoint.zwbg.filter;

import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.security.crypto.MobileAESUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2Util;
import com.epoint.core.utils.string.StringUtil;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;

public class RestEncryptFilter implements Filter {

    Logger log4j = LogUtil.getLog(this.getClass());

    SM2Util sm2 = new SM2Util(ConfigUtil.getConfigValue("sm2pubk"), ConfigUtil.getConfigValue("sm2prik"), true);

    private static String request_json = "application/json";

    private static String request_encoded = "application/x-www-form-urlencoded";

    private static String request_form = "multipart/form-data";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log4j.info("初始化RestEncrypt");    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httprequest = (HttpServletRequest) request;
        if (StringUtil.isNotBlank(httprequest.getHeader("encrypt"))) {
            //处理request
            HttpServletRequestWrapper wrapperRequest = null;
            try {
                wrapperRequest = modifyRequest(httprequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //转换成代理类
            ResponseWrapper wrapperResponse = new ResponseWrapper((HttpServletResponse) response);
            filterChain.doFilter(wrapperRequest, wrapperResponse);

            // 这里只拦截返回，直接让请求过去，如果在请求前有处理，可以在这里处理
            byte[] content = wrapperResponse.getContent();//获取返回值
            //判断是否有值
            if (content.length > 0) {
                String str = new String(content, "UTF-8");
                log4j.info("修改前:" + str);
                String ciphertext = "";
                try {
                    ciphertext = MobileAESUtil.encodeData(str);
                    response.setContentLength(ciphertext.getBytes().length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log4j.info("修改后:" + ciphertext);
                //把返回值输出到客户端
                ServletOutputStream out = response.getOutputStream();
                out.write(ciphertext.getBytes());
                out.flush();
                out.close();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private HttpServletRequestWrapper modifyRequest(HttpServletRequest httprequest) throws Exception {
        HttpServletRequestWrapper wrapperRequest = null;
        if (httprequest.getContentType().contains(request_encoded)) {
            //urlencode类型
            Map<String, String[]> stringMap = decodeParams(httprequest);
            wrapperRequest = new RequestWrapper(httprequest, stringMap);
        } else if (httprequest.getContentType().contains(request_json)) {
            //json类型
            String originalRequestBody = readRequestBody(httprequest);            // 读取原请求体（密文）
            originalRequestBody = sm2.decrypt(originalRequestBody);
            wrapperRequest = new RequestWrapper(httprequest, httprequest.getParameterMap(), originalRequestBody);
        }
        return wrapperRequest;
    }


    private Map<String, String[]> decodeParams(HttpServletRequest httprequest) {
        Map<String, String[]> stringMap = new HashMap();
        //对参数解密
        for (Map.Entry<String, String[]> entry : httprequest.getParameterMap().entrySet()) {
            log4j.info("参数名:" + entry.getKey() + ",参数值:" + Arrays.toString(entry.getValue()));
            String[] strings = entry.getValue();
            for (int k = 0; k < strings.length; k++) {
                try {
                    strings[k] = sm2.decrypt(strings[k]);
                } catch (Exception e) {
                    log4j.error("参数:" + Arrays.toString(entry.getValue()) + "解密失败，使用原始参数");
                    e.printStackTrace();
                }
            }
            stringMap.put(entry.getKey(), strings);
        }

        return stringMap;
    }

    private String readRequestBody(HttpServletRequest httprequest) throws Exception {
        StringBuilder requestBody = new StringBuilder();
        String line;
        BufferedReader reader = httprequest.getReader();
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return requestBody.toString();
    }

    @Override
    public void destroy() {

    }

}

class RequestWrapper extends HttpServletRequestWrapper {

    private HttpServletRequest orginalRequest;


    private Map<String, String[]> params = new HashMap<>();


    private String modifyRequestBody;


    public RequestWrapper(HttpServletRequest request) {
        this(request, null);
    }

    public RequestWrapper(HttpServletRequest request, Map<String, String[]> extendParams) {
        this(request, extendParams, "");
    }

    public RequestWrapper(HttpServletRequest request, Map<String, String[]> extendParams, String modifiedBody) {
        this(request, extendParams, modifiedBody, null);
    }

    public RequestWrapper(HttpServletRequest request, Map<String, String[]> extendParams, String modifiedBody, ServletFileUpload servletFileUpload) {
        super(request);
        //这里将扩展参数写入参数表
        this.orginalRequest = request;
        this.modifyRequestBody = modifiedBody;
        addAllParameters(extendParams);
    }

    //重写请求头，在AOP中去除请求头encrypt参数
    @Override
    public String getHeader(String name) {
        if ("encrypt".equals(name)) {
            return null;
        }
        return super.getHeader(name);
    }

    @Override
    public ServletInputStream getInputStream() throws UnsupportedEncodingException {
        return new ServletInputStream() {
            private InputStream in = new ByteArrayInputStream(modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()));

            @Override
            public int read() throws IOException {
                return in.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };

    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector(params.keySet()).elements();
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }

    private void addAllParameters(Map<String, String[]> otherParams) {
        for (Map.Entry<String, String[]> entry : otherParams.entrySet()) {
            addParameter(entry.getKey(), entry.getValue());
        }
    }

    private void addParameter(String name, Object value) {
        if (value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{(String) value});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
    }

}

class ResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream buffer;

    private ServletOutputStream out;

    public ResponseWrapper(HttpServletResponse httpServletResponse) {
        super(httpServletResponse);
        buffer = new ByteArrayOutputStream();
        out = new WrapperOutputStream(buffer);
    }

    @Override
    public ServletOutputStream getOutputStream()
            throws IOException {
        return out;
    }

    @Override
    public void flushBuffer()
            throws IOException {
        if (out != null) {
            out.flush();
        }
    }

    public byte[] getContent()
            throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    class WrapperOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bos;

        public WrapperOutputStream(ByteArrayOutputStream bos) {
            this.bos = bos;
        }

        @Override
        public void write(int b)
                throws IOException {
            bos.write(b);
        }

        @Override
        public boolean isReady() {

            // TODO Auto-generated method stub
            return false;

        }

        @Override
        public void setWriteListener(WriteListener arg0) {

            // TODO Auto-generated method stub

        }
    }

}
