package com.joindata.inf.boot.sterotype;

import com.joindata.inf.common.basic.exceptions.GenericException;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * REST 服务返回包装
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月18日 下午2:23:53
 * @param <T>
 */
@ApiModel("JSON 响应")
public class RestResponse<T>
{
    /** 返回结果 */
    @ApiModelProperty(value = "标志码，200 表示完全成功", example = "200")
    private int code;

    /** 正经数据 */
    @ApiModelProperty("正经数据")
    private T model;

    /** 消息提示 */
    @ApiModelProperty("消息提示")
    private String message;

    /** 细分异常代码 */
    private int errorCode;

    public static final <TT> RestResponse<TT> success(TT model, String message)
    {
        return new RestResponse<TT>(200, model, message);
    }

    public static final <TT> RestResponse<TT> fail(String message)
    {
        return new RestResponse<TT>(500, null, message);
    }

    public static final <TT> RestResponse<TT> fail(GenericException e)
    {
        return new RestResponse<TT>(500, e.getCode(), e.getMessage());
    }

    public static final <TT> RestResponse<TT> fail(Exception e)
    {
        return new RestResponse<TT>(500, null, e.getMessage());
    }

    public static final <TT> RestResponse<TT> fail(int code, String message)
    {
        return new RestResponse<TT>(code, null, message);
    }

    private RestResponse(int code, T model, String message)
    {
        this.code = code;
        this.model = model;
        this.message = message;
    }

    private RestResponse(int code, int errorCode, String message)
    {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public T getModel()
    {
        return model;
    }

    public void setModel(T model)
    {
        this.model = model;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

}
