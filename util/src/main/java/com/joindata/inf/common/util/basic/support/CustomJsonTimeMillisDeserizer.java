package com.joindata.inf.common.util.basic.support;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer;
import com.joindata.inf.common.basic.entities.TimeMillis;

/**
 * 自定义 JSON 日期反序列化处理，将时间戳反序列化为任意一种 java.util.Date 类型的子类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午9:27:21
 */
public class CustomJsonTimeMillisDeserizer extends AbstractDateDeserializer
{

    @Override
    public int getFastMatchToken()
    {
        return JSONToken.LITERAL_INT;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object value)
    {
        if(value == null)
        {
            return null;
        }

        return (T)new TimeMillis((long)value);
    }

}
