package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.ParamErrors;

/**
 * 输入数据错误异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午10:22:05
 */
public class InvalidParamException extends GenericException
{
    private static final long serialVersionUID = -1915209497885608410L;

    public InvalidParamException(Exception e)
    {
        super(e);
        this.code = ParamErrors.PARAM_ERROR.getCode();
    }

    public InvalidParamException(ErrorEntity error)
    {
        super(error);
    }

    public InvalidParamException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public InvalidParamException(int code)
    {
        super(code);
    }

    public InvalidParamException(String message)
    {
        super(message);
        super.code = ParamErrors.INVALID_FORMAT_ERROR.getCode();
    }

    public InvalidParamException(int code, String message)
    {
        super(code, message);
    }

    public InvalidParamException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }
}
