package com.joindata.inf.boot.bootconfig;

import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jetty 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月5日 下午2:49:03
 */
@Configuration
public class JettyConfig
{
    @Bean
    public JettyEmbeddedServletContainerFactory jettyFactory()
    {
        return new JettyEmbeddedServletContainerFactory();
    }
}
