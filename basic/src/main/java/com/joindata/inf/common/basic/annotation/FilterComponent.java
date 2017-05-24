package com.joindata.inf.common.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 可被 Spring 代理的 Filter 组件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 29, 2017 8:02:48 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FilterComponent
{
    String name() default "";

    String[] path() default {"/*"};
}