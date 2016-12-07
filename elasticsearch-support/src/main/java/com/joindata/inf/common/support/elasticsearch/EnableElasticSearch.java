package com.joindata.inf.common.support.elasticsearch;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.annotation.BindConfigHub;

/**
 * 启用 ElasticSearch
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月7日 下午1:53:14
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({ConfigHub.class})
@BindConfigHub(ConfigHub.class)
public @interface EnableElasticSearch
{
    /** 要扫描的顶级包，默认是当前包 */
    String[] value() default {};
}