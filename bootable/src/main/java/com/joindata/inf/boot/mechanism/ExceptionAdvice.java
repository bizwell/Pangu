package com.joindata.inf.boot.mechanism;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.joindata.inf.boot.sterotype.RestResponse;
import com.joindata.inf.common.basic.exceptions.BizException;
import com.joindata.inf.common.basic.exceptions.GenericException;
import com.joindata.inf.common.util.log.Logger;

/**
 * Controller 全局异常处理机制<br />
 * <b>这里可将所有 Controller 捕获的 Exception 转换为前端可识别的 JSON 错误消息对象</b><br />
 * <b>如果 Exception 为 GenericException，JSON 中的 code 和 message 将从 它中取，否则就用默认的 错误 code 和 Exception 中的 message 来赋值</b>
 * 
 * @see GenericException GenericException - 自定义异常类
 * @see RestResponse RestResponse - JSON 消息对象
 * @author 宋翔
 * @date 2015年11月17日 下午3:52:32
 */
@RestControllerAdvice
@ControllerAdvice
@Order()
public class ExceptionAdvice
{
    private static final Logger log = Logger.get();

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody RestResponse<?> handleException(BizException e)
    {
        log.warn("业务异常: {}", e.getMessage());

        return RestResponse.fail(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody RestResponse<?> handleException(Exception e)
    {
        log.error("异常响应: {}", e.getMessage(), e);

        return RestResponse.fail(e);
    }
}
