package com.joindata.inf.common.basic.exceptions;


import com.joindata.inf.common.basic.errors.BaseErrorCode;

/**
 * Created by likanghua on 2017/2/28.
 * 401 未登陆异常
 */
public class AuthException extends BaseRunTimeException {

    public AuthException(Enum code) {
      super(code);
    }
    public AuthException() {
        super(BaseErrorCode.S401);
    }

    public AuthException(Enum code, Object... args) {
        super(code, args);
    }

}
