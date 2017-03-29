package com.joindata.inf.common.sterotype.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个库
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 20, 2017 10:51:15 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Scheme
{
    /** 库名 */
    String value();
}
