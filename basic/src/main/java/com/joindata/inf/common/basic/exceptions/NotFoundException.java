package com.joindata.inf.common.basic.exceptions;


import com.joindata.inf.common.basic.errors.BaseErrorCode;

/**
 * Created by likanghua on 2017/2/28.
 * 404 资源找不到异常 根据资源id查找资源不存在
 */
public class NotFoundException extends BaseRunTimeException {
    public NotFoundException(Enum code) {
        super(code);
    }

    public NotFoundException() {

        super(BaseErrorCode.S404);
    }


    public NotFoundException(Enum code, Object... args) {
        super(code, args);
    }
}
