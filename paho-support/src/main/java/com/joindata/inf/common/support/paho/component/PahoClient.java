package com.joindata.inf.common.support.paho.component;

import java.io.Serializable;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.JsonUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * Paho 客户端
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 25, 2017 10:16:23 AM
 */
@Component
public class PahoClient
{
    private static final Logger log = Logger.get();

    @Autowired
    private MqttClient client;

    /**
     * 发送消息<br />
     * <strong>序列化方式为 Java 序列化，QOS 为 0（最多发一次）</strong>
     * 
     * @param topic 主题
     * @param message 内容，可序列化的任何对象（可指定多条）
     * @throws MqttException
     * @throws MqttPersistenceException
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> void send(String topic, T... message) throws MqttPersistenceException, MqttException
    {
        if(ArrayUtil.isEmpty(message))
        {
            log.warn("发送到主题 {} 的消息为空", topic);
            return;
        }

        for(T item: message)
        {
            byte bytes[] = BeanUtil.serializeObject(item);
            MqttMessage msg = new MqttMessage();
            msg.setPayload(bytes);
            msg.setQos(0);
            client.publish(topic, msg);

            log.info("向 {} 发送 Java 序列化消息, 长度: {}", topic, bytes.length);
            log.debug("消息内容是：{}", JsonUtil.toJSON(item));
        }
    }

    /**
     * 发送消息<br />
     * <strong>直接发送指定的字节数组，QOS 为 0（最多发一次）</strong>
     * 
     * @param topic 主题
     * @param payload 消息内容（可指定多条）
     * @throws MqttException
     * @throws MqttPersistenceException
     */
    public <T extends Serializable> void send(String topic, byte[]... payload) throws MqttPersistenceException, MqttException
    {
        if(ArrayUtil.isEmpty(payload))
        {
            log.warn("发送到主题 {} 的消息为空", topic);
            return;
        }

        for(byte[] item: payload)
        {
            MqttMessage msg = new MqttMessage();
            msg.setPayload(item);
            msg.setQos(0);
            client.publish(topic, msg);

            log.info("向 {} 发送字节数组消息, 长度: {}", topic, item.length);
            log.debug("消息内容: {}", item);
        }
    }

    /**
     * 发送消息<br />
     * <strong>发送自定义的 MqttMessage</strong>
     * 
     * @param topic 主题
     * @param message 消息内容（可指定多条）
     * @throws MqttException
     * @throws MqttPersistenceException
     */
    public <T extends Serializable> void send(String topic, MqttMessage... message) throws MqttPersistenceException, MqttException
    {
        if(ArrayUtil.isEmpty(message))
        {
            log.warn("发送到主题 {} 的消息为空", topic);
            return;
        }

        for(MqttMessage item: message)
        {
            client.publish(topic, item);

            log.info("向 {} 发送字节数组消息, 长度: {}", topic, item.getPayload().length);
            log.debug("消息内容: {}", item);
        }
    }

}
