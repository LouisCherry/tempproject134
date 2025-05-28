package com.epoint.zwfw.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.epoint.auditsp.JNAuditSpController;
import com.epoint.core.utils.web.WebUtil;

@Aspect // 定义切面类
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class JnSpAop
{

    @Autowired
    JNAuditSpController jnauditspcontroller;

    /**
     * 定义切点
     */
    @Pointcut("execution(* com.epoint.auditsp.auditsphandle.action.HandlePatchMaterialAction.save(..))")
    public void handlePatchMaterialSave() {
    }

    /**
     * 定义环绕增强
     */
    @Around(value = "handlePatchMaterialSave()")
    public Object handlePatchMaterialSave(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String subappGuid = WebUtil.getRequestParameterStr(request, "subappGuid");
        jnauditspcontroller.buzhengwc(subappGuid);
        return result;
    }

}
