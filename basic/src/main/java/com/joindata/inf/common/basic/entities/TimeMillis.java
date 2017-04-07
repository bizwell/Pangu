package com.joindata.inf.common.basic.entities;

/**
 * 用于表示毫秒时间戳格式<br />
 * <i>java.sql.Timestamp 的包装</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2017-4-7 16:39:00
 */
public class TimeMillis extends java.sql.Timestamp
{
    private static final long serialVersionUID = -4787991367611091125L;

    public TimeMillis()
    {
        super(System.currentTimeMillis());
    }

    public TimeMillis(long date)
    {
        super(date);
    }

    public TimeMillis(java.util.Date date)
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
        return String.valueOf(this.getTime());
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
