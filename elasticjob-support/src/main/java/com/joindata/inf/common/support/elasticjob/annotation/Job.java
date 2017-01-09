package com.joindata.inf.common.support.elasticjob.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义作业
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 3:41:39 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Job
{
    /** 作业名称 */
    String name();

    /** CRON 表达式 */
    String cron();

    /** 作业总分片数 */
    int shardingCount();

    /** 自动执行，应用启动后触发 */
    boolean autoRun() default true;

    /** 是否流式处理（如果用是流作业这个参数生效），默认为 true */
    boolean streamingProcess() default true;
}
