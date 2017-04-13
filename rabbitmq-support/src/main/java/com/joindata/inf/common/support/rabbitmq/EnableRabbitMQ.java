package com.joindata.inf.common.support.rabbitmq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 RabbitMQ
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 11, 2017 2:38:30 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "RabbitMQ 消息队列")
public @interface EnableRabbitMQ
{
}