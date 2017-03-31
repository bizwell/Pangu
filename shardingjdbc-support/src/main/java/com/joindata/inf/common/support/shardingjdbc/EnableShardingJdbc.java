package com.joindata.inf.common.support.shardingjdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用分表分库
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 27, 2017 8:43:35 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "分表分库支持")
public @interface EnableShardingJdbc
{
}