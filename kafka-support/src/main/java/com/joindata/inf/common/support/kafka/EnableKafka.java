package com.joindata.inf.common.support.kafka;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 Kafka
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 11, 2017 10:42:30 AM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "分布式消息队列")
public @interface EnableKafka
{
}