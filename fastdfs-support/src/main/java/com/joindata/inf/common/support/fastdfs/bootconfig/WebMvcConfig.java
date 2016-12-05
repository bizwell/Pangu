package com.joindata.inf.common.support.fastdfs.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import com.joindata.inf.common.support.fastdfs.dependency.client.FastdfsClient;
import com.joindata.inf.common.support.fastdfs.support.component.web.FastDfsMultipartResolver;

/**
 * 配置 WebMvc 使 Spring 支持自定义的 FastDfsMultipartFile
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:29:39
 */
@Configuration
public class WebMvcConfig extends DelegatingWebMvcConfiguration
{
    @Autowired
    private FastdfsClient client;

    @Bean
    public CharacterEncodingFilter initializeCharacterEncodingFilter()
    {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    @Bean(name = "multipartResolver")
    public FastDfsMultipartResolver multipartResolver()
    {
        FastDfsMultipartResolver resolver = new FastDfsMultipartResolver(client);
        resolver.setDefaultEncoding("UTF-8");
        resolver.setResolveLazily(true);// resolveLazily属性启用是为了推迟文件解析
        resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(50 * 1024 * 1024);// 上传文件大小 50M 50*1024*1024
        return resolver;
    }

}
