package com.epoint.annotation;

import com.epoint.constant.ZnsbConstant;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EpointZnsbCutPoint {

    public String methodname() default "";

    public ZnsbConstant.UNITTYPR unittype() default ZnsbConstant.UNITTYPR.未分类;

    public ZnsbConstant.EQUIPTYPR equiptype() default ZnsbConstant.EQUIPTYPR.未分类;

    public int ordernum() default 0;

}
