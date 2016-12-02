package com.joindata.inf.common.basic.entities;

import com.joindata.inf.common.basic.cst.DateFormatterCst;

/**
 * 用于表示日期格式<br />
 * <i> java.sql.Date 的包装</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月5日 下午1:04:31
 */
public class Date extends java.sql.Date
{
    private static final long serialVersionUID = 7110480184412524120L;

    public Date()
    {
        super(System.currentTimeMillis());
    }

    public Date(long date)
    {
        super(date);
    }

    public Date(java.util.Date date)
    {
        this(date.getTime());
    }

    /**
     * 等同于 new Date(java.util.Date date)
     * 
     * @param date java.util.Date 日期对象
     * @return 表示日期格式的日期对象
     */
    public static final Date valueOf(java.util.Date date)
    {
        return new Date(date);
    }

    /**
     * 字符串表示
     * 
     * @return yyyy-MM-dd
     */
    @Override
    public String toString()
    {
        return DateFormatterCst.DEFAULT_DATE_FORMATTER.format(this);
    }

    /**
     * 转换成 java.util.Date
     * 
     * @return Date 对象
     */
    public java.util.Date toUtilDate()
    {
        return new java.util.Date(this.getTime());
    }

    /**
     * 转换成 java.sql.Date
     * 
     * @return Date 对象
     */
    public java.sql.Date toSqlDate()
    {
        return new java.sql.Date(this.getTime());
    }
}
