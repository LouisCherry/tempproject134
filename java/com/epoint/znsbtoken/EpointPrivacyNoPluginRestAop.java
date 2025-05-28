package com.epoint.znsbtoken;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.security.TokenUtils;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2Util;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;

@Component
@Aspect
public class EpointPrivacyNoPluginRestAop
{
    public Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Autowired
    private IConfigService configService;

    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        Object[] objs = point.getArgs();
        try {
            JSONObject json = JSON.parseObject(objs[0].toString());
            String userznsbtoken = configService.getFrameConfigValue("AS_USE_ZNSBTOKEN");
            String testtoken = json.getString("token");
            if (StringUtil.isNotBlank(testtoken) && testtoken.indexOf("EpointCheckss") == 0) {
                if (!"0".equals(userznsbtoken)) {
                    if (!TokenUtils.validateToken(testtoken.replace("EpointCheckss", ""))) {
                        return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
                    }
                }
            }
            else {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();
                // 获取请求对象
                if (request != null) {
                    // 获取session
                    HttpSession session = request.getSession();
                    String sid = session.getId();
                    // 根据sid获取token
                    if (StringUtil.isNotBlank(sid)) {
                        String token = StringUtil.isNotBlank(session.getAttribute("token." + sid))
                                ? session.getAttribute("token." + sid).toString() : "";
                        String macaddress_sm2 = StringUtil.isNotBlank(session.getAttribute("macaddress." + sid))
                                ? session.getAttribute("macaddress." + sid).toString() : "";
                        log.info("切点中获取到的sid：" + sid + "，对应的token：" + token + ",加密的mac：" + macaddress_sm2);
                        if (StringUtil.isBlank(token) || StringUtil.isBlank(macaddress_sm2)) {
                            return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
                        }

                        JSONObject obj = json.getJSONObject("params");
                        String macaddress = obj.getString("macaddress");
                        log.info("设备mac：" + macaddress);
                        // 1、验证mac是否一致
                        SM2Util sm2Util = new SM2Util();
                        if (!macaddress.equals(sm2Util.decrypt(macaddress_sm2))) {
                            return JsonUtils.zwdtRestReturn("0", "mac地址校验不通过！", "");
                        }
                        // 2、验证token
                        if (!"0".equals(userznsbtoken)) {
                            if (!TokenUtils.validateToken(token)) {
                                return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
                            }
                        }
                    }
                }

                /*
                 * else {
                 * 
                 * try {
                 * JSONObject json = JSON.parseObject(objs[0].toString());
                 * String userznsbtoken =
                 * configService.getFrameConfigValue("AS_USE_ZNSBTOKEN");
                 * if (!"0".equals(userznsbtoken)) {
                 * if (!TokenUtils.validateToken(json.getString("token"))) {
                 * return JsonUtils.zwdtRestReturn("0", "token验证失败！", "");
                 * }
                 * }
                 * }
                 * catch (Exception e) {
                 * log.error("=====AOP验证token异常=====");
                 * e.printStackTrace();
                 * }
                 * }
                 */
            }
        }
        catch (Exception e) {
            log.info("EpointPrivacyNoPluginRestAop切点token验证异常！");
            e.printStackTrace();
        }
        return point.proceed(objs);
    }
}
