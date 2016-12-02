package com.joindata.inf.common.basic.errors;

import com.joindata.inf.common.basic.entities.ErrorEntity;

/**
 * 通信错误代码
 * 
 * @author: [hui.anger]
 * @date: 2015年12月3日 上午11:25:53
 */
public interface CommunicationError
{
    /** 通用通信错误 */
    public static final ErrorEntity COMMUNICATION_ERROR = ErrorEntity.define(5000, "通信异常");

    /** 响应报文解析错误 */
    public static final ErrorEntity RESPONSE_TEXT_PARSE_ERROR = ErrorEntity.define(5001, "响应报文解析异常");
}
