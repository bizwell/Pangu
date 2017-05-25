package com.joindata.inf.common.support.paho.core;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.log.Logger;

public class PahoMqttCallBack implements MqttCallback
{
    private static final Logger log = Logger.get();

    @Override
    public void connectionLost(Throwable cause)
    {
        log.error("MQTT 失去连接: ", cause.getMessage(), cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        log.info("MQTT 消息来了, 主题: {}, 消息 ID: {}, 消息长度: {}", topic, message.getId(), message.getPayload().length);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token)
    {
        log.info("MQTT 消息发送了, 主题: {}, 消息 ID: {}", ArrayUtil.toString(token.getTopics()), token.getMessageId());
    }
}
