package com.joindata.inf.boot.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 启用新的 REST 风格<br />
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Aug 30, 2017 2:07:21 PM
 * @since 1.2.5
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface NewRestStyle
{
}
