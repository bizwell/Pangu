package com.joindata.inf.common.support.rabbitmq.core.handler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.support.rabbitmq.annotation.RabbitAttr;
import com.joindata.inf.common.support.rabbitmq.cst.RabbitDefault;
import com.joindata.inf.common.support.rabbitmq.enums.RabbitFeature;
import com.joindata.inf.common.util.basic.EnumUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.joindata.inf.common.util.tools.UuidUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 直连消息监听器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 14, 2017 2:22:20 PM
 */
public class QueueMessageListenerHandler extends AbstractMessageListenerHandler
{
    private static final Logger log = Logger.get();

    public QueueMessageListenerHandler(ConnectionFactory connectionFactory, Map<String, MessageListener<Serializable>> listenerMap, String kafkaServer)
    {
        super(connectionFactory, listenerMap, kafkaServer);
    }

    @Override
    public void startListener()
    {
        super.getListenerMap().keySet().forEach(queue -> {
            ConsumeThread thread = new ConsumeThread(queue, this);
            thread.start();

            log.info("启动 {} 直连队列的收取线程", queue);
        });
    }

    class ConsumeThread extends Thread
    {
        private AbstractMessageListenerHandler handler;

        private String queue;

        private Channel channel;

        private Connection conn;

        private String consumerTag;

        public Channel getChannel()
        {
            return channel;
        }

        public void setChannel(Channel channel)
        {
            this.channel = channel;
        }

        public Connection getConn()
        {
            return conn;
        }

        public void setConn(Connection conn)
        {
            this.conn = conn;
        }

        public ConsumeThread(String queue, AbstractMessageListenerHandler handler)
        {
            this.queue = queue;
            this.handler = handler;
            init();
        }

        public void init()
        {
            this.conn = handler.getChannel(queue).getKey();
            this.channel = handler.getChannel(queue).getValue();

            RabbitAttr config = handler.getQueueConfig(queue);

            String directExchange = StringUtil.isNullOrEmpty(config.exchangeName()) ? RabbitDefault.DEFAULT_DIRECT_EXCHANGE : config.exchangeName();
            String routingKey = config.routingKey();
            consumerTag = "Queue - " + queue + " - " + UuidUtil.make();

            try
            {
                if(StringUtil.isNotEmpty(directExchange))
                {
                    boolean durable = !EnumUtil.hasItem(config.features(), RabbitFeature.ExchangeTransient);
                    boolean autoDelete = EnumUtil.hasItem(config.features(), RabbitFeature.ExchangeAutoDelete);
                    boolean internal = EnumUtil.hasItem(config.features(), RabbitFeature.ExchangeInternal);

                    channel.exchangeDeclare(directExchange, BuiltinExchangeType.DIRECT, durable, autoDelete, internal, null);
                }

                {
                    boolean durable = !EnumUtil.hasItem(config.features(), RabbitFeature.QueueTransient);
                    boolean exclusive = EnumUtil.hasItem(config.features(), RabbitFeature.QueueExclusive);
                    boolean autoDelete = EnumUtil.hasItem(config.features(), RabbitFeature.QueueAutoDelete);
                    channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
                    channel.queueBind(queue, directExchange, routingKey);
                }
            }
            catch(IOException e)
            {
                log.error("消费 {} 时出错: {}", queue, e.getMessage(), e);
            }

            ShutdownListener shutdownListener = new ShutdownListener()
            {
                @Override
                public void shutdownCompleted(ShutdownSignalException cause)
                {
                    if(!channel.isOpen())
                    {
                        log.warn("收取 {} 的连接已断开, 请检查服务器是否开启或网络是否畅通...", queue);
                    }
                }
            };

            channel.addShutdownListener(shutdownListener);
            conn.addShutdownListener(shutdownListener);
        }

        @Override
        public void run()
        {
            RabbitAttr config = handler.getQueueConfig(queue);

            String directExchange = StringUtil.isNullOrEmpty(config.exchangeName()) ? RabbitDefault.DEFAULT_DIRECT_EXCHANGE : config.exchangeName();
            String routingKey = config.routingKey();

            log.info("从 {} 收取直连队列消息, 绑定交换机: {}, 路由键: {}", queue, directExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);

            try
            {

                boolean autoAck = true; // TODO !EnumUtil.hasItem(config.features(), RabbitFeature.ConsumeNonAutoAck);
                boolean noLocal = EnumUtil.hasItem(config.features(), RabbitFeature.ConsumeNoLocal);
                boolean exclusive = EnumUtil.hasItem(config.features(), RabbitFeature.ConsumeExclusive);

                channel.basicConsume(queue, autoAck, consumerTag, noLocal, exclusive, null, handler.getConsumer(queue));
            }
            catch(IOException e)
            {
                log.error("消费 {} 时出错: {}", queue, e.getMessage(), e);
            }
        }
    };
}
