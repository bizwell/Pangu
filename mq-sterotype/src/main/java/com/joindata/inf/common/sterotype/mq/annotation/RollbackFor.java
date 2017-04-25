package com.joindata.inf.common.sterotype.mq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记启用事务
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 21, 2017 11:56:57 AM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RollbackFor
{
    /** 遇到什么异常会回滚，默认是任何异常都会回滚 */
    Class<? extends Throwable>[] value() default {Throwable.class};
}