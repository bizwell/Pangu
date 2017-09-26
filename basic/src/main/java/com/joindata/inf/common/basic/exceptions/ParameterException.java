package com.joindata.inf.common.basic.exceptions;


import com.joindata.inf.common.basic.errors.BaseErrorCode;

/**
 * Created by liutao1 on 2017/1/11.
 * 参数异常 跟数据中
 */
public class ParameterException extends BaseRunTimeException {
    public ParameterException(Enum code) {
        super(code);
    }

    public ParameterException() {
        super(BaseErrorCode.S400);

    }

    public ParameterException(Enum code, Object... args) {
        super(code, args);
    }

}