package com.joindata.inf.common.support.idgen;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.basic.annotation.JoindataComponent;

/**
 * 启用 ID 生成器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 23, 2017 9:39:49 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JoindataComponent(bind = ConfigHub.class, name = "ID 生成器")
public @interface EnableIdgen
{
}