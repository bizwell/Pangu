package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.ParamErrors;

/**
 * 认证异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-12-13 21:28:09
 */
public class AuthenticationException extends GenericException
{
    private static final long serialVersionUID = -5888838593647326872L;

    public AuthenticationException(Exception e)
    {
        super(e);
        this.code = ParamErrors.PARAM_ERROR.getCode();
    }

    public AuthenticationException(ErrorEntity error)
    {
        super(error);
    }

    public AuthenticationException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public AuthenticationException(int code)
    {
        super(code);
    }

    public AuthenticationException(String message)
    {
        super(message);
        super.code = ParamErrors.INVALID_FORMAT_ERROR.getCode();
    }

    public AuthenticationException(int code, String message)
    {
        super(code, message);
    }

    public AuthenticationException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }
}
