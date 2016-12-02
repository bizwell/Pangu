package com.joindata.inf.common.util.major.excel.stereotype.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导出 Excel 时实体中需要配置的工作簿选项
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-11-11 16:23:06
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExportSheet
{
    /** 工作簿名称，默认 Sheet1 */
    public String value() default "Sheet1";

    /** 工作簿名称，默认 Sheet1（同 value） */
    public String sheetName() default "Sheet1";

    /** 表格标题 */
    public String caption();
}
