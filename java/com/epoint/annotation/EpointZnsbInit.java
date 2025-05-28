package com.epoint.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EpointZnsbInit {
    //初始化任务名称
    public String jobname() default "";

    //任务执行顺序
    public int ordernum() default 0;

    public boolean once() default true;
}
