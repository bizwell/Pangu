package com.joindata.inf.common.basic.entities;

import java.io.Serializable;

/**
 * 键-值对象
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月25日 下午5:14:43
 */
public class Pair<K, V> implements Serializable
{
    private static final long serialVersionUID = -7188986497524557521L;

    protected K key;

    protected V value;

    public Pair()
    {
    }

    public Pair(K key, V value)
    {
        this.key = key;
        this.value = value;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K, V> Pair define(K key, V value)
    {
        return new Pair(key, value);
    }

    public K getKey()
    {
        return key;
    }

    public void setKey(K key)
    {
        this.key = key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "{" + key + ": " + value + "}";
    }
}
