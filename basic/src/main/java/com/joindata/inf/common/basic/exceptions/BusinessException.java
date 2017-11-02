package com.joindata.inf.common.basic.exceptions;


import com.joindata.inf.common.basic.errors.BaseErrorCode;

/**
 * Created by likanghua on 2017/2/28.
 * 业务异常基类3
 */
public class BusinessException extends BaseRunTimeException {

    public BusinessException() {
        super(BaseErrorCode.S500);
    }

    public BusinessException(Enum code) {
        super(code);
    }

    public BusinessException(Enum code, Object... args) {
        super(code, args);
    }

}
