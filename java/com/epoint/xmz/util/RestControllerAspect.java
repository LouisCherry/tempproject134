package com.epoint.xmz.util;
import com.epoint.core.utils.security.crypto.MobileAESUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2Util;
import com.epoint.core.utils.string.StringUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;

@Configuration
@EnableAspectJAutoProxy
@Aspect
@Component
public class RestControllerAspect {

	private Logger log4j = Logger.getLogger(MethodHandles.lookup().lookupClass());

	
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object modifyReturnValue(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue = null;
        boolean encrypt = false;


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if (StringUtil.isNotBlank(request.getHeader("encrypt"))) {
            encrypt = true;

            //使用新模式
            SM2Util sm2 = new SM2Util(false);
            Object[] argsarr = joinPoint.getArgs();
            //对参数解密
            for (int i = 0; i < argsarr.length; i++) {
                if (argsarr[i] instanceof String && StringUtil.isNotBlank(argsarr[i])) {

                    try {
                        //log4j.info(sm2.decrypt(String.valueOf(argsarr[i])));
                        argsarr[i] = sm2.decrypt(String.valueOf(argsarr[i]));
                    } catch (Exception e) {
                        log4j.error("参数" + String.valueOf(argsarr[i]) + "解密失败，使用原始参数");

                    }
                }
            }                //执行请求
            returnValue = joinPoint.proceed(argsarr);

            //log4j.info("===================================");

        } else {
            returnValue = joinPoint.proceed();
        }

        //执行之后
        if (returnValue instanceof String && encrypt) {
        	//log4j.info("加密前:"+returnValue );
            returnValue = MobileAESUtil.encodeData(String.valueOf(returnValue));
            //            log4j.info("aes加密{}", MobileAESUtil.encodeData(String.valueOf(returnValue)));
            //log4j.info("加密后:"+returnValue);
        }
        // 修改请求
        return returnValue;
    }
}


