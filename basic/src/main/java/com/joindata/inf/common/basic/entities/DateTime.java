package com.joindata.inf.common.basic.entities;

import com.joindata.inf.common.basic.cst.DateFormatterCst;

/**
 * 用于表示 日期+时间格式<br />
 * <i>java.sql.Timestamp 的包装</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月5日 下午1:04:31
 */
public class DateTime extends java.sql.Timestamp
{
    private static final long serialVersionUID = 5418236686740971118L;

    public DateTime()
    {
        super(System.currentTimeMillis());
    }

    public DateTime(long date)
    {
        super(date);
    }

    public DateTime(java.util.Date date)
    {
        this(date.getTime());
    }

    /**
     * 字符串表示
     * 
     * @return yyyy-MM-dd HH:mm:ss
     */
    @Override
    public String toString()
    {
        return DateFormatterCst.DEFAULT_DATETIME_FORMATTER.format(this);
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
