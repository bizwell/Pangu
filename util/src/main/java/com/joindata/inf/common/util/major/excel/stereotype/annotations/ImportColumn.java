package com.joindata.inf.common.util.major.excel.stereotype.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析Excel时实体属性的字段位置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2014-2-19 下午2:58:08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ImportColumn
{
    /** 字段位置，从 1 开始数 */
    public int value();
}
