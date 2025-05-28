package com.epoint.audittaskzj.action;

import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.controller.BaseController;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.security.TokenUtil;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * [一句话功能简述]
 * [功能详细描述]
 *
 * @version [版本号, 2018年11月23日]
 * @作者 CTY
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@RestController("audittaskzjauthaction")
@Scope("request")
public class AuditTaskZJAuthAction extends BaseController {
    /**
     *
     */
    private static final long serialVersionUID = 7003018322540177971L;

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void pageLoad() {
    }

    public void auth() {
        String AppKey = getRequestParameter("user");
        String AppSecret = getRequestParameter("password");
        String key = ConfigUtil.getConfigValue("AppKey");
        String secret = ConfigUtil.getConfigValue("AppSecret");
        JSONObject returnobj = new JSONObject();
        try {
            if (StringUtil.isNotBlank(key) && StringUtil.isNotBlank(secret) && StringUtil.isNotBlank(AppKey)
                    && StringUtil.isNotBlank(AppSecret) && key.indexOf(AppKey) >= 0 && secret.indexOf(AppSecret) >= 0) {
                String token = TokenUtil.createToken(AppKey);
                if (StringUtil.isNotBlank(token)) {
                    returnobj.put("result", "true");
                    returnobj.put("access_token", token);
                }
            } else {
                returnobj.put("result", "false");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnobj.put("result", "false");
        }
        sendRespose(JsonUtil.objectToJson(returnobj));
    }

    public String getApproveItem() {
        String token = getRequestParameter("token");
        log.info(token);
        try {
            if (TokenUtil.validateToken(token)) {
                request.getRequestDispatcher("www.baidu.com").forward(request, response);
                return "资源请求成功";
            } else {
                return "资源不存在";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "资源不存在";
        }

    }

    //与父类方法完全一致
/*    @Override
    public String authorize(String cmd) {
        return super.authorize(cmd);
    }*/

}
