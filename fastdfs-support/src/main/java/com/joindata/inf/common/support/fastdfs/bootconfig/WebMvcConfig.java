package com.joindata.inf.common.support.fastdfs.bootconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import com.joindata.inf.common.support.fastdfs.support.component.FastDfsClient;
import com.joindata.inf.common.support.fastdfs.support.component.web.FastDfsMultipartResolver;
import com.joindata.inf.common.util.log.Logger;

/**
 * 配置 WebMvc 使 Spring 支持自定义的 FastDfsMultipartFile
 * 
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:29:39
 */
@Configuration
public class WebMvcConfig extends DelegatingWebMvcConfiguration
{
    private static final Logger log = Logger.get();

    @Autowired
    private FastDfsClient client;

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
        // TODO 后续将这些参数设为可外部配置的
        String defaultEncoding = "UTF-8";
        boolean lazyResolve = true;
        int maxInMemory = 40960;
        long maxUploadSize = 50 * 1024 * 1024;

        FastDfsMultipartResolver resolver = new FastDfsMultipartResolver(client);
        resolver.setDefaultEncoding(defaultEncoding);
        resolver.setResolveLazily(lazyResolve);
        resolver.setMaxInMemorySize(maxInMemory);
        resolver.setMaxUploadSize(maxUploadSize);

        log.info("注册 FastDFS 文件上传解析器, 内存缓冲大小: {}, 文件大小限制: {}", maxInMemory, maxUploadSize);

        return resolver;
    }

}
