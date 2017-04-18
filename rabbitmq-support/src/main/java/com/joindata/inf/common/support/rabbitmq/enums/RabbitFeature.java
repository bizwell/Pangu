package com.joindata.inf.common.support.rabbitmq.enums;

/**
 * 队列、交换机性质
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 13, 2017 5:29:07 PM
 */
public enum RabbitFeature
{
    /** 不持久化消息，服务重启后删除 */
    QueueTransient,
    /** 独占连接，该队列只能被一个连接使用 */
    QueueExclusive,
    /** 队列不使用时不自动删除 */
    QueueAutoDelete,

    /** 通过 JSON 序列化消息 */
    JsonSerialization,

    /** TODO （目前尚未支持）消费时不自动回执 */
    @Deprecated
    ConsumeNonAutoAck,
    /** true if the server should not deliver to this consumer messages published on this channel's connection */
    ConsumeNoLocal,
    /** true if this is an exclusive consumer */
    ConsumeExclusive,

    /** 交换机非持久化 */
    ExchangeTransient,
    /** 交换机自动删除 */
    ExchangeAutoDelete,
    /** 内部交换机 */
    ExchangeInternal
}
