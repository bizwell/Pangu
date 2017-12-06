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
 * 广播消息监听器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 12, 2017 1:38:31 PM
 */
public class BroadcastMessageListenerHandler extends AbstractMessageListenerHandler
{
    private static final Logger log = Logger.get();

    public BroadcastMessageListenerHandler(ConnectionFactory connectionFactory, Map<String, MessageListener<Serializable>> listenerMap, String kafkaServer)
    {
        super(connectionFactory, listenerMap, kafkaServer);
    }

    @Override
    public void startListener()
    {
        super.getListenerMap().keySet().forEach(queue ->
        {

            boolean shared = EnumUtil.hasItem(this.getQueueConfig(queue).features(), RabbitFeature.SharedBroadcastQueue);

            String realQueue = shared ? queue : UuidUtil.make() + "@" + queue;

            ConsumeThread thread = new ConsumeThread(queue, realQueue, this);
            thread.start();

            if(shared)
            {
                log.info("启动 {} 广播的收取线程, 共享队列名", queue);
            }
            else
            {
                log.info("启动 {} 广播的收取线程, 真实的收取队列: {}", queue, realQueue);
            }

        });
    }

    class ConsumeThread extends Thread
    {
        private AbstractMessageListenerHandler handler;

        private String queue;

        private String realQueue;

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

        public ConsumeThread(String queue, String realQueue, AbstractMessageListenerHandler handler)
        {
            this.queue = queue;
            this.realQueue = realQueue;
            this.handler = handler;
            init();
        }

        public void init()
        {
            this.conn = handler.getChannel(queue).getKey();
            this.channel = handler.getChannel(queue).getValue();

            RabbitAttr config = handler.getQueueConfig(queue);

            String broadcastExchange = StringUtil.isNullOrEmpty(config.exchangeName()) ? RabbitDefault.DEFAULT_FANOUT_EXCHANGE : config.exchangeName();
            String routingKey = config.routingKey();
            consumerTag = "Broadcast - " + queue + " - " + UuidUtil.make();

            try
            {
                if(StringUtil.isNotEmpty(broadcastExchange))
                {
                    boolean durable = !EnumUtil.hasItem(config.features(), RabbitFeature.ExchangeTransient);
                    boolean autoDelete = EnumUtil.hasItem(config.features(), RabbitFeature.ExchangeAutoDelete);
                    boolean internal = EnumUtil.hasItem(config.features(), RabbitFeature.ExchangeInternal);

                    channel.exchangeDeclare(broadcastExchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, null);
                }

                {
                    boolean durable = !EnumUtil.hasItem(config.features(), RabbitFeature.QueueTransient);
                    boolean exclusive = EnumUtil.hasItem(config.features(), RabbitFeature.QueueExclusive);
                    boolean autoDelete = false;

                    // 如果是共享队列，autoDelete 取决于是否有 QueueAutoDelete 特性
                    if(EnumUtil.hasItem(config.features(), RabbitFeature.SharedBroadcastQueue))
                    {
                        autoDelete = EnumUtil.hasItem(config.features(), RabbitFeature.QueueAutoDelete);
                    }
                    // 如果是不共享队列，默认删除队列，但也可通过 DonotAutoDeleteNonSharedQueue 特性来设置不删除
                    else
                    {
                        autoDelete = !EnumUtil.hasItem(config.features(), RabbitFeature.DonotAutoDeleteNonSharedQueue);
                    }

                    channel.queueDeclare(realQueue, durable, exclusive, autoDelete, null);
                    channel.queueBind(realQueue, broadcastExchange, routingKey);
                }
            }
            catch(

            IOException e)
            {
                log.error("消费 {} 时出错: {}", realQueue, e.getMessage(), e);
            }

            ShutdownListener shutdownListener = new ShutdownListener()
            {
                @Override
                public void shutdownCompleted(ShutdownSignalException cause)
                {
                    if(!channel.isOpen())
                    {
                        log.warn("收取 {} 的连接已断开, 请检查服务器是否开启或网络是否畅通...", realQueue);
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

            String broadcastExchange = StringUtil.isNullOrEmpty(config.exchangeName()) ? RabbitDefault.DEFAULT_FANOUT_EXCHANGE : config.exchangeName();
            String routingKey = config.routingKey();

            log.info("从 {} 收取广播消息, 绑定交换机: {}, 路由键: {}", realQueue, broadcastExchange, routingKey.equals(RabbitDefault.DEFAULT_ROUTING_KEY) ? "[默认]" : routingKey);

            try
            {

                boolean autoAck = true; // TODO !EnumUtil.hasItem(config.features(), RabbitFeature.ConsumeNonAutoAck);
                boolean noLocal = EnumUtil.hasItem(config.features(), RabbitFeature.ConsumeNoLocal);
                boolean exclusive = EnumUtil.hasItem(config.features(), RabbitFeature.ConsumeExclusive);

                channel.basicConsume(realQueue, autoAck, consumerTag, noLocal, exclusive, null, handler.getConsumer(queue));
            }
            catch(IOException e)
            {
                log.error("消费 {} 时出错: {}", realQueue, e.getMessage(), e);
            }
        }
    };
}
