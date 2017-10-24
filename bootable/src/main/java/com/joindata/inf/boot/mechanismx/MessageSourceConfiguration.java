package com.joindata.inf.boot.mechanismx;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Created by likanghua on 2017/9/27.
 */
@Configuration
public class MessageSourceConfiguration {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setCacheSeconds(300);
        String env = BootInfoHolder.Env.get();
        String[] basenames = {"message_".concat(env), "success_".concat(env)};
        messageSource.setBasenames(basenames);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}

