package com.joindata.inf.common.support.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个 MyBatis 的插件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 25, 2017 1:39:28 PM
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MyBatisPlugin
{
}