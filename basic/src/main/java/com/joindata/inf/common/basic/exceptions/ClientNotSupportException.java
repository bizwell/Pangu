package com.joindata.inf.common.basic.exceptions;

import com.joindata.inf.common.basic.errors.BaseErrorCode;

/**
 * Created by likanghua on 2017/10/19.
 */
public class ClientNotSupportException extends BaseRunTimeException {
    public ClientNotSupportException() {
        super(BaseErrorCode.S412);
    }


}
