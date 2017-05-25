package com.joindata.inf.common.support.paho.core.handler;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.joindata.inf.common.sterotype.mq.MessageListener;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 消息监听器处理器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 24, 2017 5:34:30 PM
 */
public class MessageListenerHandler
{
    private static Logger log = Logger.get();

    /** topic - 消息监听器 */
    private Map<String, MessageListener<Serializable>> ListenerMap = CollectionUtil.newMap();

    /** Paho MQTT 客户端 */
    private MqttClient client = null;

    /** 线程池 */
    private Executor executor;

    /**
     * 构造二进制消息监听处理器
     * 
     * @param mqttClient
     */
    public MessageListenerHandler(MqttClient mqttClient, Map<String, MessageListener<Serializable>> listenerMap)
    {
        this.client = mqttClient;

        this.ListenerMap.putAll(listenerMap);

        // 初始化线程池
        this.executor = Executors.newFixedThreadPool(1);
    }

    /**
     * 启动
     */
    public void init()
    {
        trigger();
    }

    /**
     * 执行收取任务
     */
    public void trigger()
    {
        if(ListenerMap.isEmpty())
        {
            return;
        }

        ListenerMap.forEach((topic, listener) ->
        {
            try
            {
                executor.execute(new ListenerThread(client, topic, listener));
            }
            catch(MqttException e)
            {
                log.error("创建主题 {} 监听器时发生错误: {}", e.getMessage(), e);
            }
        });
    }

}

/**
 * 监听线程
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 25, 2017 12:17:21 PM
 */
class ListenerThread extends Thread implements IMqttMessageListener
{
    private static final Logger log = Logger.get();

    private MessageListener<Serializable> listener;

    public ListenerThread(MqttClient client, String topic, MessageListener<Serializable> listener) throws MqttException
    {
        this.listener = listener;

        client.subscribe(topic, this);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        log.info("主题 {} 收到消息, 消息长度: {}", topic, message.getPayload().length);
        listener.onReceive(BeanUtil.deserializeObject(message.getPayload()));
    }

}