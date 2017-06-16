package com.joindata.inf.common.sterotype.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.joindata.inf.common.sterotype.jdbc.cst.DatasourceName;

/**
 * 标记使用数据源
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 15, 2017 11:09:08 AM
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Datasource
{
    /**
     * 数据源名
     */
    String value() default DatasourceName.DEFAULT;
}
