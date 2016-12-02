package com.joindata.inf.common.basic.exceptions;

import java.util.Map;

import com.joindata.inf.common.basic.entities.ErrorEntity;
import com.joindata.inf.common.basic.errors.CommunicationError;

/**
 * 通信异常
 * 
 * @author [hui.anger]
 * @date 2015年12月3日 上午11:26:51
 */
public class CommunicationException extends GenericException
{
    private static final long serialVersionUID = 1133605694607523034L;

    public CommunicationException(Exception e)
    {
        super(e);
        this.code = CommunicationError.COMMUNICATION_ERROR.getCode();
    }

    public CommunicationException(ErrorEntity error)
    {
        super(error);
    }

    public CommunicationException(ErrorEntity error, String appendMessage)
    {
        super(error, appendMessage);
    }

    public CommunicationException(int code)
    {
        super(code);
    }

    public CommunicationException(String message)
    {
        super(message);
        super.code = CommunicationError.COMMUNICATION_ERROR.getCode();
    }

    public CommunicationException(int code, String message)
    {
        super(code, message);
    }

    public CommunicationException(int code, String message, Map<String, Object> extra)
    {
        super(code, message, extra);
    }

}
