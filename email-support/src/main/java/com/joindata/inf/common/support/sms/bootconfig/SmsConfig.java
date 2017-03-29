package com.joindata.inf.common.support.sms.bootconfig;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.sms.EnableSms;
import com.joindata.inf.common.support.sms.properties.SmsProperties;
import com.joindata.inf.common.util.log.Logger;
import com.niwodai.inf.notification.client.JmsSender;
import com.niwodai.inf.notification.client.request.SendChannel;

/**
 * SMS 配置<br />
 * <i>这个是复刻你我贷的 notification-client-sms 下面的 Bean 定义 XML 文件(notification-client-jms.xml)</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 21, 2017 5:54:08 PM
 */
@Configuration
public class SmsConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private SmsProperties smsProperties;

    /**
     * 获取默认发送通道
     * 
     * @return 发送通道
     */
    public static final SendChannel getDefaultChannel()
    {
        return BootInfoHolder.getBootClass().getAnnotation(EnableSms.class).channel();
    }

    /**
     * 获取默认的系统 ID
     * 
     * @return 系统 ID
     */
    public static final String getSystemId()
    {
        return BootInfoHolder.getBootClass().getAnnotation(EnableSms.class).systemId();
    }

    @Bean
    public JmsTemplate jmsTemplate()
    {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(smsProperties.getMqBroker());

        log.info("ActiveMQ 连接地址: {}", smsProperties.getMqBroker());

        return new JmsTemplate(factory);
    }

    @Bean
    public JmsSender jmsSender()
    {
        log.info("开始 - 非批量消息发送器配置");

        JmsSender sender = new JmsSender();

        sender.setJmsTemplate(jmsTemplate());

        // 选择 ONS 还是 ActiveMQ
        sender.setMsgServerKind(smsProperties.getServerKind());
        log.info("短信服务器类型: {}", smsProperties.getServerKind());

        // ActiveMQ 相关
        sender.setPersistent(true);
        sender.setDestination(smsProperties.getNewQueue());
        sender.setDestinationValidate(smsProperties.getValidateQueue());
        sender.setDestinationIvr(smsProperties.getIvrQueue());
        sender.setDestinationBatch(smsProperties.getBatchQueue());
        sender.setDestinationMail(smsProperties.getMailQueue());

        log.info("MQ 队列: 新消息 - {}, 验证 - {}, IVR - {}, BATCH - {}, MAIL - {}", smsProperties.getNewQueue(), smsProperties.getValidateQueue(), smsProperties.getIvrQueue(), smsProperties.getBatchQueue(), smsProperties.getMailQueue());

        // ONS 相关
        sender.setProducerId(smsProperties.getOnsProducerId());
        sender.setAccessKey(smsProperties.getOnsAccessKey());
        sender.setSecretKey(smsProperties.getOnsSecretKey());

        log.info("ONS 参数: ProducerId - {}", smsProperties.getOnsProducerId());

        log.info("结束 - 非批量消息发送器配置");
        return sender;
    }

    @Bean
    public JmsSender jmsSenderBatch()
    {
        log.info("开始 - 批量消息发送器配置");

        JmsSender sender = new JmsSender();

        sender.setJmsTemplate(jmsTemplate());

        sender.setIsBatch(true);

        // 选择 ONS 还是 ActiveMQ
        sender.setMsgServerKind(smsProperties.getServerKind());
        log.info("短信服务器类型: {}", smsProperties.getServerKind());

        // ActiveMQ 相关
        sender.setPersistent(true);
        sender.setDestination(smsProperties.getNewQueue());
        sender.setDestinationValidate(smsProperties.getValidateQueue());
        sender.setDestinationIvr(smsProperties.getIvrQueue());
        sender.setDestinationBatch(smsProperties.getBatchQueue());
        sender.setDestinationMail(smsProperties.getMailQueue());

        log.info("MQ 队列: 新消息 - {}, 验证 - {}, IVR - {}, BATCH - {}, MAIL - {}", smsProperties.getNewQueue(), smsProperties.getValidateQueue(), smsProperties.getIvrQueue(), smsProperties.getBatchQueue(), smsProperties.getMailQueue());

        // ONS 相关
        sender.setProducerId(smsProperties.getOnsProducerId());
        sender.setAccessKey(smsProperties.getOnsAccessKey());
        sender.setSecretKey(smsProperties.getOnsSecretKey());

        log.info("ONS 参数: ProducerId - {}", smsProperties.getOnsProducerId());

        log.info("结束 - 批量消息发送器配置");
        return sender;
    }

}