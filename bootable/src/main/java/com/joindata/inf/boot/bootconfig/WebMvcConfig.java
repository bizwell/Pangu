package com.joindata.inf.boot.bootconfig;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.joindata.inf.common.util.log.Logger;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
    private static final Logger log = Logger.get();

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();

        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        messageConverter.setFastJsonConfig(config);
        messageConverter.setDefaultCharset(Charset.forName("UTF-8"));

        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        messageConverter.setSupportedMediaTypes(mediaTypeList);

        converters.add(messageConverter);

        log.debug("注册 HTTP 消息转换器: {}", messageConverter.toString());
    }
}
