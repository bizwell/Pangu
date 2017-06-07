package com.joindata.inf.common.support.paho.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.support.paho.enums.PahoSerialization;

/**
 * 标记一个 Paho 队列属性
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 7, 2017 2:12:02 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PahoAttr
{
    /** 序列化方式，参见 {@link PahoSerialization} */
    PahoSerialization serialization();
}
