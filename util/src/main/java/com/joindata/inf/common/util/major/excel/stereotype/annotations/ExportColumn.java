package com.joindata.inf.common.util.major.excel.stereotype.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出 Excel 时实体属性的字段设置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-11-11 16:24:13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportColumn
{
    /** 字段名 */
    public String title() default "";

    /** 单元格宽度（像素），默认 2000 */
    public int cellWidth() default 2000;
}
