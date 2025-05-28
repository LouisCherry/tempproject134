package com.epoint.annotation;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EpointZnsbEvent {

}
