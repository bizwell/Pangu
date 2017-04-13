package com.joindata.inf.common.support.rabbitmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.support.rabbitmq.enums.ExchangeType;

/**
 * 标记一个 RabbitMQ 队列设置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 11, 2017 3:35:29 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RabbitQueue
{
    /** Exchange 类型 */
    ExchangeType value() default ExchangeType.DIRECT;

    /** Exchange 名字 */
    String exchangeName() default "";

    /** 是否持久化队列 */
    boolean durable() default false;

    /** 是否自动删除交换机 */
    boolean autoDelete() default false;

    /** 连接是否独占 */
    boolean exclusive() default false;
}
