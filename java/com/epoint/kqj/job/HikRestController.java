package com.epoint.kqj.job;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.JsonUtils;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@RestController
@RequestMapping(value = "/hik")
public class HikRestController extends AuditCommonService
{

    @Autowired
    private IHandleConfig handleConfigservice;
    @Autowired
    private IAttachService attachservice;
    @Autowired
    private IConfigService configservice;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @RequestMapping(value = "/gethikdata", method = RequestMethod.POST)
    public String getHikData(@RequestBody String params, HttpServletRequest request) {
        try {
            System.out.println("=====调用海康接口=============");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String url = jsonObject.getString("url");
            String param = jsonObject.getString("hikparam");
            System.out.println("=====海康接口传参信息===" + url + "------" + param);
            HTTPClientUtil client = new HTTPClientUtil();
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin", "xt12345678");
            client.client.getState().setCredentials(AuthScope.ANY, creds);
            String returnData = client.doPost(url, param);
            return JsonUtils.zwdtRestReturn("1", "", returnData);
        }
        catch (Exception e) {
            return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
        }
    }

}
