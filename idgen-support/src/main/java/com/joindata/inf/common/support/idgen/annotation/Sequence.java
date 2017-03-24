package com.joindata.inf.common.support.idgen.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 序列注入标记
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 23, 2017 8:08:56 PM
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Sequence
{
    /** 序列名 */
    String value();
}
