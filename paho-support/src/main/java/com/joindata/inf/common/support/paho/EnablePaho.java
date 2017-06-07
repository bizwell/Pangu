package com.joindata.inf.common.support.paho;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 Paho
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 25, 2017 5:24:08 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "MQTT 消息队列支持")
public @interface EnablePaho
{
}