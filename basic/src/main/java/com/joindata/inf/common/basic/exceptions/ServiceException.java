package com.joindata.inf.common.basic.exceptions;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * Created by zhugongyi on 2017/8/22. 业务异常，调用方必须显式处理
 */
public class ServiceException extends Exception
{
    private static final long serialVersionUID = 4978058763771235892L;

    private int code;

    public ServiceException(String message)
    {
        super(message);
    }

    public ServiceException(int code, String message)
    {
        this(message);
        this.setCode(code);
    }

    public ServiceException(int code, String message, Throwable cause)
    {
        super(message, cause);
        this.setCode(code);
    }

    public ServiceException(int code, Throwable cause)
    {
        super(cause);
        this.setCode(code);
    }

    public ServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ServiceException(ErrorEntity entity)
    {
        this(entity.getMessage());
        this.code = entity.getCode();
    }

    public ServiceException(ErrorEntity entity, Throwable cause)
    {
        super(entity.getMessage(), cause);
        this.setCode(entity.getCode());
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
}
