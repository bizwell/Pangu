package com.joindata.inf.common.basic.entities;

import java.io.Serializable;

/**
 * 键-值对象
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月25日 下午5:14:43
 */
public class KeyValuePair implements Serializable
{
    private static final long serialVersionUID = -7188986497524557521L;

    protected String key;

    protected String value;

    public KeyValuePair()
    {
    }

    public KeyValuePair(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public static KeyValuePair define(String key, String value)
    {
        return new KeyValuePair(key, value);
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "{" + key + ": " + value + "}";
    }
}
