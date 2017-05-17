package com.joindata.inf.common.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.Servlet;

/**
 * 标注 Servlet
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 17, 2017 2:00:02 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ServletConfig
{
    Class<Servlet>[] value();
}