package com.joindata.inf.boot.mechanism.newrest;

/**
 * HttpEntityMethodProcessor.handleReturnValue方法会将入参returnValue强转ResponseEntity，并调用ResponseEntity.getBody() ，所以ReturnValueWrapper要继承ResponseEntity。
 */
public class ReturnValueWrapper
{
    private String code;

    private Object data;

    private ReturnValueWrapper()
    {
    }

    private ReturnValueWrapper(String code, Object data)
    {
        this.code = code;
        this.data = data;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public static ReturnValueWrapper wrapper(final Integer code, final Object returnValue)
    {
        return new ReturnValueWrapper("S" + code, returnValue);
    }

    public static ReturnValueWrapper wrapper(final Object returnValue)
    {
        return new ReturnValueWrapper("S200", returnValue);
    }

}
