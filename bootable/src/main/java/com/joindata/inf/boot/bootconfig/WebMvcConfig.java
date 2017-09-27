package com.joindata.inf.boot.bootconfig;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.joindata.inf.boot.annotation.JoindataWebApp;
import com.joindata.inf.boot.annotation.MvcJsonSerializationFeature;
import com.joindata.inf.boot.sterotype.annotations.WebRequestInterceptor;
import com.joindata.inf.boot.sterotype.handler.RequestInterceptor;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.log.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    private static final Logger log = Logger.get();


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new Converter<String, Date>() {
            private static final String DATE_PATTERN_REGEX = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
            private final Pattern DATE_PATTERN = Pattern.compile(DATE_PATTERN_REGEX);
            private static final String DATE_TIME_PATTERN_REGEX = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
            private final Pattern DATE_TIME_PATTERN = Pattern.compile(DATE_TIME_PATTERN_REGEX);
            private static final String LONG_PATTERN_REGEX = "^\\d+$";
            private final Pattern LONG_PATTERN = Pattern.compile(LONG_PATTERN_REGEX);

            @Override
            public Date convert(String source) {
                try {
                    if (DATE_PATTERN.matcher(source).find()) {
                        return getDateFormat().parse(source);
                    } else if (DATE_TIME_PATTERN.matcher(source).find()) {
                        return getDateTimeFormat().parse(source);
                    } else if (LONG_PATTERN.matcher(source).find()) {
                        return new Date(Long.valueOf(source));
                    }
                } catch (ParseException e) {
                    log.error("转换失败", e);
                }
                throw new IllegalArgumentException();
            }

            private DateFormat getDateFormat() {
                return DateUtil.DEFAULT_DATE_FORMATTER;
            }

            private DateFormat getDateTimeFormat() {
                return DateUtil.DEFAULT_DATETIME_FORMATTER;
            }
        });
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        {
            StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
            List<MediaType> mediaTypeList = new ArrayList<MediaType>();
            mediaTypeList.add(MediaType.TEXT_PLAIN);
            messageConverter.setSupportedMediaTypes(mediaTypeList);
            converters.add(messageConverter);

            log.info("注册 HTTP 字符串消息转换器: {}", messageConverter.getSupportedMediaTypes());
        }

        {
            FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
            FastJsonConfig config = new FastJsonConfig();

            config.setSerializerFeatures(Util.getJsonFeature());
            messageConverter.setFastJsonConfig(config);
            messageConverter.setDefaultCharset(Charset.forName("UTF-8"));

            List<MediaType> mediaTypeList = new ArrayList<MediaType>();
            mediaTypeList.add(MediaType.APPLICATION_JSON);
            mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
            messageConverter.setSupportedMediaTypes(mediaTypeList);

            converters.add(messageConverter);
            log.info("HTTP JSON 消息转换器特性: {}", ArrayUtil.toString(Util.getJsonFeature()));
            log.info("注册 HTTP JSON 消息转换器: {}", messageConverter.getSupportedMediaTypes());
        }

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        for (String staticDir : Util.getStaticDir()) {
            registry.addResourceHandler("**").addResourceLocations("classpath:" + staticDir + "/");
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    /**
     * 注册拦截器，扫描项目目录中 interceptor 包下的所有拦截器实例，并注册到 MVC 中
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Set<Class<?>> classes = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), WebRequestInterceptor.class);
        for (Class<?> clz : classes) {
            WebRequestInterceptor pathParams = clz.getAnnotation(WebRequestInterceptor.class);

            if (ArrayUtil.isEmpty(pathParams.value())) {
                log.info("注册拦截器: {}, 拦截目录(默认): {}, 排除目录: {}", clz.getCanonicalName(), pathParams.value(), pathParams.exclude());
            } else {
                log.info("注册拦截器: {}, 拦截目录: {}, 排除目录: {}", clz.getCanonicalName(), pathParams.value(), pathParams.exclude());
            }

            Object obj = SpringContextHolder.getBean(clz);

            if (obj instanceof RequestInterceptor) {
                // 创建拦截器实例
                RequestInterceptor interceptor = (RequestInterceptor) obj;

                MappedInterceptor mappedInterceptor = new MappedInterceptor(pathParams.value(), pathParams.exclude(), interceptor);
                registry.addInterceptor(mappedInterceptor);

                log.warn("注册 RequestInterceptor 拦截器: {}", clz.getCanonicalName());
            } else if (obj instanceof com.joindata.inf.boot.sterotype.handler.WebRequestInterceptor) {
                // 创建拦截器实例
                com.joindata.inf.boot.sterotype.handler.WebRequestInterceptor interceptor = (com.joindata.inf.boot.sterotype.handler.WebRequestInterceptor) obj;

                MappedInterceptor mappedInterceptor = new MappedInterceptor(pathParams.value(), pathParams.exclude(), interceptor);
                registry.addInterceptor(mappedInterceptor);

                log.warn("注册 WebRequestInterceptor 拦截器: {}", clz.getCanonicalName());
            } else {
                log.warn("类: {} 没有继承任何拦截器, 不会生效", clz.getCanonicalName());
            }
        }

        super.addInterceptors(registry);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(false);
        configurer.setUseRegisteredSuffixPatternMatch(false);
        configurer.setUseSuffixPatternMatch(false);
    }

    /**
     * 实用工具
     *
     * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
     * @date Jun 14, 2017 2:39:06 PM
     */
    private static final class Util {
        public static SerializerFeature[] getJsonFeature() {
            MvcJsonSerializationFeature anno = BootInfoHolder.getBootClass().getAnnotation(MvcJsonSerializationFeature.class);
            if (anno == null) {
                return new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.SkipTransientField, SerializerFeature.WriteEnumUsingToString, SerializerFeature.WriteSlashAsSpecial};
            }
            return anno.value();
        }

        public static String[] getStaticDir() {
            return BootInfoHolder.getBootClass().getAnnotation(JoindataWebApp.class).staticDir();
        }
    }
}
