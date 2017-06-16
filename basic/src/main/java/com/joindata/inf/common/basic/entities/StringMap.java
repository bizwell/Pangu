package com.joindata.inf.common.basic.entities;

import java.util.HashMap;

/**
 * 字符串 Map（HashMap）
 * 
 * @author Muyv
 * @date 2016年2月17日 下午8:43:03
 */
public class StringMap extends HashMap<String, String>
{
    private static final long serialVersionUID = 4299471689867121008L;

    /**
     * 生成一个简单的只有一个元素的 StringMap
     * 
     * @param key
     * @param value
     * @return
     */
    public static StringMap of(String key, String value)
    {
        StringMap map = new StringMap();
        map.put(key, value);

        return map;
    }
}
