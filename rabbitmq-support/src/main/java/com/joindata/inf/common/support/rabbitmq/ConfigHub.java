package com.joindata.inf.common.support.rabbitmq;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.rabbitmq.bootconfig.RabbitMqConfig;

/**
 * Kafka 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 11, 2017 10:42:11 AM
 */
@Configuration
@Import(RabbitMqConfig.class)
@EnableDisconf
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}