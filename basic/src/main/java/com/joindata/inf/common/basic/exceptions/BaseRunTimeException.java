package com.joindata.inf.common.basic.exceptions;


import com.joindata.inf.common.basic.errors.BaseErrorCode;
import lombok.NonNull;

/**
 * Created by likanghua on 2017/2/28.
 * 通用业务异常程序员自己throw
 */
public class BaseRunTimeException extends RuntimeException {
    /**
     * 业务枚举code 该枚举定义在自身业务包中
     */
    private final Enum code;

    private Object[] args;

    public Object[] getArgs() {
        return args;
    }

    public Enum getCode() {
        return code;
    }


    public BaseRunTimeException() {
        this.code = BaseErrorCode.S408;
    }


    public BaseRunTimeException(@NonNull Enum code) {
        this.code = code;
    }

    public BaseRunTimeException(@NonNull Enum code, Object... args) {
        this.code = code;
        this.args = args;
    }

}
