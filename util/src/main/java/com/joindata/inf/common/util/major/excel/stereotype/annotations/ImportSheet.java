package com.joindata.inf.common.util.major.excel.stereotype.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析Excel时实体所要取数据的工作簿设置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2014-2-19 下午2:58:08
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ImportSheet
{
    /** 工作簿名称 */
    public String sheetName() default "Sheet1";

    /** 起始行数，从1开始 */
    public int startRow() default 1;

    /** 结束行数，从1开始。如果小于0，表示忽略 */
    public int endRow() default -1;

    /** 是否遇到一个空行后结束读取，如果为true，则在遇到空行时会停止读取，这时会忽略endRow的设置。默认为 true */
    public boolean breakByEmptyRow() default true;
}
