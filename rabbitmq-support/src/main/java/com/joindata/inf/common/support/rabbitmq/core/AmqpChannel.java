package com.joindata.inf.common.support.rabbitmq.core;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息发送器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 11, 2017 3:20:15 PM
 */
public abstract class AmqpChannel<T extends Serializable>
{
    protected String queue;

    public AmqpChannel(String queue, boolean durable, boolean exclusive, boolean autoDelete) throws IOException, TimeoutException
    {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
        String message = "Hello World!";
        channel.basicPublish("", queue, null, message.getBytes());

        System.out.println(" [x] Sent '" + message + "'");

        this.queue = queue;
    }

    protected abstract void send(T message);
}
