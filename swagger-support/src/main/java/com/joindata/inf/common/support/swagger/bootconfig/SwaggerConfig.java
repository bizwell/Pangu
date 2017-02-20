package com.joindata.inf.common.support.swagger.bootconfig;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.swagger.EnableSwagger;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月18日 上午10:55:51
 */
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter
{
    private static final Logger log = Logger.get();

    @Bean
    public Docket buildApiDoc()
    {
        Docket docklet = new Docket(DocumentationType.SWAGGER_2);

        Set<String> produces = new HashSet<>();
        produces.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
        docklet.produces(produces);

        Set<String> consumes = new HashSet<>();
        produces.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
        docklet.consumes(consumes);

        log.info("Swagger 文档类型: {}, 默认接收 MIME: {}, 默认返回 MIME: {}", docklet.getDocumentationType().toString(), produces, consumes);

        EnableSwagger enableSwagger = BootInfoHolder.getBootClass().getAnnotation(EnableSwagger.class);

        // API 信息设置
        {
            ApiInfoBuilder builder = new ApiInfoBuilder();

            String title = enableSwagger.title() + enableSwagger.value();

            builder.title(title);

            if(!StringUtil.isBlank(enableSwagger.author() + enableSwagger.email()))
            {
                builder.contact(new Contact(enableSwagger.author(), null, enableSwagger.email()));

            }
            builder.version(enableSwagger.version());
            docklet.apiInfo(builder.build());

            log.info("Swagger 文档标题: {}, 接口版本: {}, 文档维护人: {}, 维护人 Email: {}", title, enableSwagger.version(), enableSwagger.author(), enableSwagger.email());
        }

        String packageName = enableSwagger.scanPackages();
        if(StringUtil.isBlank(packageName))
        {
            packageName = BootInfoHolder.getBootClass().getPackage().getName();
        }

        docklet.select().apis(RequestHandlerSelectors.basePackage(packageName)).build();

        log.info("Swagger 扫描包: {}", packageName);

        return docklet;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        log.info("Swagger 页面: {}", "swagger-ui.html");
    }

}
