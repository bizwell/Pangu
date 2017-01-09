package com.joindata.inf.common.support.elasticjob;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 ElasticJob
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 12:34:28 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "分布式作业")
public @interface EnableElasticJob
{
}