package com.joindata.inf.boot.bootconfig;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.boot.sterotype.annotations.WebRequestInterceptor;
import com.joindata.inf.boot.sterotype.handler.RequestInterceptor;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ClassUtil;
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

        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.SkipTransientField);
        messageConverter.setFastJsonConfig(config);
        messageConverter.setDefaultCharset(Charset.forName("UTF-8"));

        List<MediaType> mediaTypeList = new ArrayList<MediaType>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        messageConverter.setSupportedMediaTypes(mediaTypeList);

        converters.add(messageConverter);

        log.info("注册 HTTP 消息转换器: {}", messageConverter.toString());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        super.addResourceHandlers(registry);
        for(String staticDir: BootInfoHolder.getBootClass().getAnnotation(JoindataWebApp.class).staticDir())
        {
            registry.addResourceHandler("**").addResourceLocations("classpath:" + staticDir + "/");
        }
    }

    /**
     * 注册拦截器，扫描项目目录中 interceptor 包下的所有拦截器实例，并注册到 MVC 中
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        Set<Class<?>> classes = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), WebRequestInterceptor.class);
        for(Class<?> clz: classes)
        {
            WebRequestInterceptor pathParams = clz.getAnnotation(WebRequestInterceptor.class);

            // 如果没有这个注解或不是 RequestInterceptor 的子类就不算拦截器，不予注册
            if(pathParams == null && clz.isAssignableFrom(clz.asSubclass(RequestInterceptor.class)))
            {
                continue;
            }

            if(ArrayUtil.isEmpty(pathParams.include()) && ArrayUtil.isEmpty(pathParams.value()))
            {
                log.info("注册拦截器: {}, 拦截目录(默认): {}, 排除目录: {}", clz.getCanonicalName(), new String[]{"/**"}, pathParams.exclude());
            }
            else
            {
                log.info("注册拦截器: {}, 拦截目录: {}, 排除目录: {}", clz.getCanonicalName(), ArrayUtil.merge(pathParams.include(), pathParams.value()), pathParams.exclude());
            }

            // 创建拦截器实例
            RequestInterceptor interceptor = (RequestInterceptor)SpringContextHolder.getBean(clz);

            MappedInterceptor mappedInterceptor = new MappedInterceptor(ArrayUtil.merge(pathParams.include(), pathParams.value()), pathParams.exclude(), interceptor);

            registry.addInterceptor(mappedInterceptor);
        }

        super.addInterceptors(registry);
    }

}
