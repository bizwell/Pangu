package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.GenericError;

/**
 * 自定义异常基类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午10:23:32
 */
public class GenericException extends RuntimeException
{
    private static final long serialVersionUID = 5525274682314573566L;

    /** 错误代码 */
    protected int code;

    /** 异常消息 */
    protected String message;

    /** 附加数据 */
    protected Map<String, Object> extra;

    protected GenericException(GenericException e)
    {
        this.code = e.getCode();
        this.message = e.getMessage();
        this.extra = e.getExtra();
    }

    protected GenericException(Exception e)
    {
        this.code = GenericError.ERROR.getCode();
        this.message = e.toString();
    }

    /**
     * 根据错误实体中的数据来创建异常
     * 
     * @param error 错误实体
     */
    protected GenericException(ErrorEntity error)
    {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    /**
     * 根据错误实体中的数据来创建异常，并追加错误消息
     * 
     * @param error 错误实体
     * @param message 错误消息
     */
    protected GenericException(ErrorEntity error, String appendMessage)
    {
        this.code = error.getCode();
        this.message = error.getMessage().concat("：").concat(appendMessage);
    }

    /**
     * 只指定错误代码
     * 
     * @param code 错误代码
     */
    protected GenericException(int code)
    {
        this.code = code;
    }

    /**
     * 只指定错误消息，代码用默认的
     * 
     * @param message 错误消息
     */
    protected GenericException(String message)
    {
        this.message = message;
    }

    /**
     * 指定错误代码和错误消息
     * 
     * @param code 错误代码
     * @param message 错误消息
     */
    protected GenericException(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    /**
     * 指定错误代码，错误消息，以及额外的附加数据
     * 
     * @param code 错误代码
     * @param message 错误消息
     * @param extra 附加数据
     */
    protected GenericException(int code, String message, Map<String, Object> extra)
    {
        this.code = code;
        this.message = message;
        this.extra = extra;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Map<String, Object> getExtra()
    {
        return extra;
    }

    public void setExtra(Map<String, Object> extra)
    {
        this.extra = extra;
    }

}
