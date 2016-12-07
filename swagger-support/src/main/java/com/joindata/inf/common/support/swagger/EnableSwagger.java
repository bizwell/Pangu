package com.joindata.inf.common.support.swagger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.annotation.BindConfigHub;

/**
 * 启用 Swagger
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月6日 13:48:34
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ConfigHub.class})
@BindConfigHub(ConfigHub.class)
public @interface EnableSwagger
{
    /** 扫描的包名 */
    public String value() default "";

    /** 文档标题 */
    public String name() default "接口测试";

    /** 维护人姓名 */
    public String author() default "";

    /** 维护人 Email */
    public String email() default "";

    /** 版本，默认 1.0.0 */
    public String version() default "1.0.0";
}