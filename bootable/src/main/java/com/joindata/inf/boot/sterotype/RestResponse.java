package com.joindata.inf.boot.sterotype;

import java.util.Map;

import com.joindata.inf.common.basic.exceptions.BizException;
import com.joindata.inf.common.util.basic.CollectionUtil;

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

    @ApiModelProperty("额外数据")
    private Map<String, Object> extra = CollectionUtil.newMap();

    /** 消息提示 */
    @ApiModelProperty("消息提示")
    private String message;

    /** 细分异常代码 */
    @ApiModelProperty("细分异常代码")
    private int errorCode;

    /** 时间戳 */
    @ApiModelProperty("时间戳")
    private long time = System.currentTimeMillis();

    public static final <TT> RestResponse<TT> success()
    {
        return new RestResponse<TT>(200, null, "OK");
    }

    public static final <TT> RestResponse<TT> success(TT model)
    {
        return new RestResponse<TT>(200, model, "OK");
    }

    public static final <TT> RestResponse<TT> success(TT model, String message)
    {
        return new RestResponse<TT>(200, model, message);
    }

    public static final <TT> RestResponse<TT> success(TT model, Map<String, Object> extra)
    {
        return new RestResponse<TT>(200, model, "OK", extra);
    }

    public static final <TT> RestResponse<TT> success(TT model, String message, Map<String, Object> extra)
    {
        return new RestResponse<TT>(200, model, message, extra);
    }

    public static final <TT> RestResponse<TT> fail(String message)
    {
        return new RestResponse<TT>(500, null, message);
    }

    public static final <TT> RestResponse<TT> fail(BizException e)
    {
        return new RestResponse<TT>(500, e.getErrorEntity().getCode(), e.getMessage());
    }

    public static final <TT> RestResponse<TT> fail(Exception e)
    {
        return new RestResponse<TT>(500, -1, e.getMessage());
    }

    public static final <TT> RestResponse<TT> fail(int code, String message)
    {
        return new RestResponse<TT>(code, -1, message);
    }

    public static final <TT> RestResponse<TT> fail(int code, int errorCode, String message)
    {
        return new RestResponse<TT>(code, errorCode, message);
    }

    private RestResponse(int code, T model, String message)
    {
        this.code = code;
        this.model = model;
        this.message = message;
    }

    private RestResponse(int code, T model, String message, Map<String, Object> extra)
    {
        this.code = code;
        this.model = model;
        this.message = message;

        if(extra != null)
        {
            this.extra.putAll(extra);
        }

    }

    private RestResponse(int code, int errorCode, String message)
    {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;
    }

    private RestResponse(int code, int errorCode, String message, Map<String, Object> extra)
    {
        this.code = code;
        this.errorCode = errorCode;
        this.message = message;

        if(extra != null)
        {
            this.extra.putAll(extra);
        }
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

    public long getTime()
    {
        return time;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Map<String, Object> getExtra()
    {
        return extra;
    }

    @SuppressWarnings("unchecked")
    public <TTT> TTT getExtra(String name)
    {
        return (TTT)this.extra.get(name);
    }

    public void addExtra(String name, Object value)
    {
        this.extra.put(name, value);
    }
}
