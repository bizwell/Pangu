package com.joindata.inf.common.support.dubbo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 Dubbo
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 28, 2016 5:04:35 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "远程服务调用支持")
public @interface EnableDubbo
{
}