package com.joindata.inf.common.support.mybatis.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import tk.mybatis.mapper.code.Style;

/**
 * MapperHelper 配置项
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Aug 30, 2017 2:53:33 PM
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MapperHelperProps
{
    /** 主键取回方式 */
    String identity() default "MYSQL";

    /** insert和update中，是否判断字符串类型!=''，少数方法会用到 */
    boolean notEmpty() default false;

    /** 字段名转换风格 */
    Style style() default Style.camelhump;
}