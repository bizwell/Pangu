package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 认证错误代码
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015-12-13 21:31:40
 */
public interface AuthenticationError
{
    /** 认证失败 */
    public static final ErrorEntity AUTHENTICATION_ERROR = ErrorEntity.define(7000, "认证失败");

    /** 用户不存在 */
    public static final ErrorEntity USER_NOT_FOUND = ErrorEntity.define(7001, "用户不存在");

    /** 密码错误 */
    public static final ErrorEntity INVALID_PASSWORD = ErrorEntity.define(7002, "密码错误");

    /** 认证信息失效 */
    public static final ErrorEntity TOKEN_EXPIRED = ErrorEntity.define(7003, "认证信息失效");

    /** 没有权限 */
    public static final ErrorEntity PERMISSION_DENIED = ErrorEntity.define(7004, "权限不够");

    /** 验证信息异常 */
    public static final ErrorEntity VALID_INFO_INCORRECT = ErrorEntity.define(7005, "验证信息异常");

    /** 账号已存在 */
    public static final ErrorEntity DUPLICATE_IDENTITY =  ErrorEntity.define(7006, "账号已存在");
    
    /** 密码不合法 */
    public static final ErrorEntity INLEGAL_PASSWORD = ErrorEntity.define(7007, "密码不合法");
}
