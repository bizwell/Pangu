package com.joindata.inf.boot.mechanism.newrest;

public class ReturnValueWrapper {
    private String code;

    private Object data;

    private ReturnValueWrapper() {
    }

    private ReturnValueWrapper(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ReturnValueWrapper wrapper(final String code, final Object returnValue) {
        return new ReturnValueWrapper(code, returnValue);
    }


}
