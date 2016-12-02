package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.SystemError;

/**
 * 程序异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午10:22:05
 */
public class SystemException extends GenericException
{
    private static final long serialVersionUID = 1133605694607523034L;

    public SystemException(Exception e)
    {
        super(e);
        this.code = SystemError.SYSTEM_ERROR.getCode();
    }

    public SystemException(ErrorEntity error)
    {
        super(error);
    }

    public SystemException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public SystemException(int code)
    {
        super(code);
    }

    public SystemException(String message)
    {
        super(message);
        super.code = SystemError.SYSTEM_ERROR.getCode();
    }

    public SystemException(int code, String message)
    {
        super(code, message);
    }

    public SystemException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }

}
