package com.joindata.inf.common.support.paho.bootconfig;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.paho.core.MessageListenerScanner;
import com.joindata.inf.common.support.paho.core.PahoMqttCallBack;
import com.joindata.inf.common.support.paho.core.handler.MessageListenerHandler;
import com.joindata.inf.common.support.paho.properties.PahoProperties;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.basic.SystemUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 配置 Paho
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 24, 2017 4:32:01 PM
 */
@Configuration
public class PahoConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private PahoProperties properties;

    @Bean
    public MqttClient mqttClient() throws MqttException
    {
        MqttClient client = new MqttClient(properties.getBrokerList(), BootInfoHolder.getAppId() + SystemUtil.getRuntimeSignature(SystemUtil.getProcessId(), DateUtil.getCurrentDateTimeString()), memoryPersistence());
        client.setManualAcks(false);

        client.connect(mqttConnectOptions());
        client.setCallback(pahoMqttCallBack());

        log.info("MQTT 服务器: {}", properties.getBrokerList());

        return client;
    }

    @Bean
    public PahoMqttCallBack pahoMqttCallBack()
    {
        return new PahoMqttCallBack();
    }

    @Bean
    public MemoryPersistence memoryPersistence()
    {
        return new MemoryPersistence();
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions()
    {
        MqttConnectOptions opts = new MqttConnectOptions();
        opts.setCleanSession(true);

        if(StringUtil.isNotEmpty(properties.getBrokerUsername()) && StringUtil.isNotEmpty(properties.getBrokerPassword()))
        {
            opts.setUserName(properties.getBrokerUsername());
            opts.setPassword(properties.getBrokerPassword().toCharArray());
            log.info("MQTT 连接用户名: {}", properties.getBrokerUsername());
        }

        opts.setAutomaticReconnect(true);

        return opts;
    }

    /**
     * 注册一大堆 Topic 消息监听器
     * 
     * @throws MqttException
     */
    @Bean(initMethod = "init")
    public MessageListenerHandler messageListenerHandler() throws MqttException
    {
        MessageListenerScanner scanner = new MessageListenerScanner();
        MessageListenerHandler handler = new MessageListenerHandler(mqttClient(), scanner.scanTopicListener());
        return handler;
    }

}