package com.joindata.inf.common.basic.cst;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 日期格式对象常量
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月5日 下午3:31:48
 */
public interface DateFormatterCst
{
    /** 默认的日期格式对象 yyyy-MM-dd */
    public static final DateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    /** 默认的日期+时间格式对象 yyyy-MM-dd HH:mm:ss */
    public static final DateFormat DEFAULT_DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 默认的时间格式对象 HH:mm:ss */
    public static final DateFormat DEFAULT_TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");
}
