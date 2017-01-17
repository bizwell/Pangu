package com.joindata.inf.common.sterotype.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个主题
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 16, 2017 2:52:29 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Topic
{
    /** 主题名 */
    String value();
}
