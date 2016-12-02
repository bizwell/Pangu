package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.ResourceErrors;

/**
 * 资源错误异常
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午10:22:05
 */
public class ResourceException extends GenericException
{
    private static final long serialVersionUID = -8956551913670565062L;

    public ResourceException(Exception e)
    {
        super(e);
        this.code = ResourceErrors.RESOURCE_ERROR.getCode();
    }

    public ResourceException(ErrorEntity error)
    {
        super(error);
    }

    public ResourceException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public ResourceException(int code)
    {
        super(code);
    }

    public ResourceException(String message)
    {
        super(message);
        super.code = ResourceErrors.RESOURCE_ERROR.getCode();
    }

    public ResourceException(int code, String message)
    {
        super(code, message);
    }

    public ResourceException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }
}
