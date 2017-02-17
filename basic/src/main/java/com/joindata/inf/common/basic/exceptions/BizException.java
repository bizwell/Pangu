package com.joindata.inf.common.basic.exceptions;

import com.joindata.inf.common.basic.entities.ErrorEntity;

public abstract class BizException extends RuntimeException
{
    private static final long serialVersionUID = 8564997768294356109L;

    public abstract ErrorEntity getErrorEntity();

    public BizException(String message)
    {
        super(message);
    }
}
