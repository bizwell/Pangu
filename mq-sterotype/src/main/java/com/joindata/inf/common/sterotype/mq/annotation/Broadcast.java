package com.joindata.inf.common.sterotype.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个广播
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 14, 2017 9:42:47 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Broadcast
{
    /** 队列名 */
    String value();
}
