package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.OperationError;

/**
 * 操作异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-12-13 21:28:09
 */
public class OperationException extends GenericException
{
    private static final long serialVersionUID = 6852854340157227722L;

    public OperationException(Exception e)
    {
        super(e);
        this.code = OperationError.OPERATION_ERROR.getCode();
    }

    public OperationException(ErrorEntity error)
    {
        super(error);
    }

    public OperationException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public OperationException(int code)
    {
        super(code);
    }

    public OperationException(String message)
    {
        super(message);
        super.code = OperationError.OPERATION_ERROR.getCode();
    }

    public OperationException(int code, String message)
    {
        super(code, message);
    }

    public OperationException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }
}
