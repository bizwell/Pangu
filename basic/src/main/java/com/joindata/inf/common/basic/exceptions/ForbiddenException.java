package com.joindata.inf.common.basic.exceptions;


import com.joindata.inf.common.basic.errors.BaseErrorCode;

/**
 * Created by likanghua on 2017/2/28.
 * 403无权限异常
 */
public class ForbiddenException extends BaseRunTimeException {


	public ForbiddenException(Enum code) {
		super(code);
	}

	/**
	 * 403无权限异常
	 */
	public ForbiddenException() {
		super(BaseErrorCode.S403);
	}


	public ForbiddenException(Enum code, Object... args) {
		super(code, args);
	}
}
