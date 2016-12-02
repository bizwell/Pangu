package com.joindata.inf.common.basic.entities;

/**
 * 包装了错误代码和错误消息
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午11:17:43
 */
public class ErrorEntity
{
    /** 错误代码 */
    private int code;

    /** 错误消息 */
    private String message;

    private ErrorEntity(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    public static final ErrorEntity define(int code, String message)
    {
        return new ErrorEntity(code, message);
    }

    @Override
    public String toString()
    {
        return String.valueOf(code);
    }

}
