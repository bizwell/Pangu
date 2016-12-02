package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 传参错误代码
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月6日 上午11:04:39
 */
public interface ParamErrors
{
    /** 通用参数错误 */
    public static final ErrorEntity PARAM_ERROR = ErrorEntity.define(1000, "错误的输入");

    /** 数据格式错误 */
    public static final ErrorEntity INVALID_FORMAT_ERROR = ErrorEntity.define(1001, "输入格式错误");

    /** 数据不合法错误 */
    public static final ErrorEntity INVALID_VALUE_ERROR = ErrorEntity.define(1002, "输入值不合法");

    /** 文件类型不正确 */
    public static final ErrorEntity INVALID_FILE_TYPE_ERROR = ErrorEntity.define(1003, "文件类型不正确");

    /** 参数配置错误 */
    public static final ErrorEntity INVALID_PARAM_CONFIG_ERROR = ErrorEntity.define(1004, "参数配置错误");

    /** 参数只读，不允许设值 */
    public static final ErrorEntity READ_ONLY = ErrorEntity.define(1005, "参数只读，不允许设值");

}
