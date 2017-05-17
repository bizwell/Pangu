package com.joindata.inf.common.basic.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记 name-value
 * 
 * @deprecated 尚未实现
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 17, 2017 3:57:13 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Deprecated
public @interface Param
{
    String name();

    String value();
}