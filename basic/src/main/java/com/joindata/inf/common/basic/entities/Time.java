package com.joindata.inf.common.basic.entities;

import com.joindata.inf.common.basic.cst.DateFormatterCst;

/**
 * 用于表示时间格式<br />
 * <i>java.sql.Time 的简单包装</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月5日 下午1:06:54
 */
public class Time extends java.sql.Time
{
    private static final long serialVersionUID = -2141680639951483559L;

    public Time()
    {
        super(System.currentTimeMillis());
    }

    public Time(long date)
    {
        super(date);
    }

    public Time(java.util.Date date)
    {
        this(date.getTime());
    }

    /**
     * 字符串表示
     * 
     * @return HH:mm:ss
     */
    @Override
    public String toString()
    {
        return DateFormatterCst.DEFAULT_TIME_FORMATTER.format(this);
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
