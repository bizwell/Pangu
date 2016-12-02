package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.ApplicationError;

/**
 * 程序异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午10:22:05
 */
public class ApplicationException extends GenericException
{
    private static final long serialVersionUID = -3761482262826349433L;

    public ApplicationException(Exception e)
    {
        super(e);
        this.code = ApplicationError.APPLICATION_ERROR.getCode();
    }

    public ApplicationException(ErrorEntity error)
    {
        super(error);
    }

    public ApplicationException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public ApplicationException(int code)
    {
        super(code);
    }

    public ApplicationException(String message)
    {
        super(message);
        super.code = ApplicationError.APPLICATION_ERROR.getCode();
    }

    public ApplicationException(int code, String message)
    {
        super(code, message);
    }

    public ApplicationException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }

}
