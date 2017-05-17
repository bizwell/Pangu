package com.joindata.inf.common.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可被 Spring 代理的 Servlet 组件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 17, 2017 1:56:57 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ServletComponent
{
    String[] path() default {"/*"};

    @SuppressWarnings("deprecation")
    Param[] initParam() default {};
}