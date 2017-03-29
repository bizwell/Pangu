package com.joindata.inf.common.support.email.bootconfig;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.email.EnableEmail;
import com.joindata.inf.common.support.email.properties.EmailProperties;
import com.joindata.inf.common.util.log.Logger;
import com.niwodai.inf.notification.client.email.sender.EmailNotifyJmsSender;

/**
 * 
 * email相关配置
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017-03-29
 */

@Configuration

public class EmailConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private EmailProperties emailProperties;

    /**
     * 获取默认的系统 ID
     * 
     * @return 系统 ID
     */
    public static final String getSystemId()
    {
        return BootInfoHolder.getBootClass().getAnnotation(EnableEmail.class).systemId();
    }
    
    @Bean("emailJmsTemplate")
    public JmsTemplate jmsTemplate()
    {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(emailProperties.getMqBroker());

        log.info("ActiveMQ 连接地址: {}", emailProperties.getMqBroker());

        return new JmsTemplate(factory);
    }

    
    @Bean
    public EmailNotifyJmsSender EmailNotifyJmsSender() {
    	log.info("初始化jms sender, 配置信息: {}", emailProperties);
    	EmailNotifyJmsSender emailNotifyJmsSender = new EmailNotifyJmsSender();
    	emailNotifyJmsSender.setAccessKey(emailProperties.getOnsAccessKey());
    	emailNotifyJmsSender.setDestinationAmq(emailProperties.getAmqQueueName());
    	emailNotifyJmsSender.setDestinationOns(emailProperties.getOnsQueueName());
    	emailNotifyJmsSender.setMsgServerKind(emailProperties.getServerKind());
    	emailNotifyJmsSender.setProducerId(emailProperties.getOnsProducerId());
    	emailNotifyJmsSender.setSecretKey(emailProperties.getOnsSecretKey());
    	emailNotifyJmsSender.setPersistent(true);
    	emailNotifyJmsSender.setJmsTemplate(jmsTemplate());
    	return emailNotifyJmsSender;
    }


}