package com.joindata.inf.common.support.rabbitmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.support.rabbitmq.cst.RabbitDefault;
import com.joindata.inf.common.support.rabbitmq.enums.RabbitFeature;

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
public @interface RabbitAttr
{
    /** Exchange 名字，默认值参见 {@link RabbitDefault} */
    String exchangeName() default "";

    /** 路由键，默认值参见 {@link RabbitDefault#DEFAULT_ROUTING_KEY} */
    String routingKey() default RabbitDefault.DEFAULT_ROUTING_KEY;

    /** 一些性质，参见 {@link RabbitFeature} */
    RabbitFeature[] features() default {};
}
