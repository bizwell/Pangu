package com.joindata.inf.common.util.basic.support;

import java.lang.reflect.Type;
import java.text.ParseException;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.AbstractDateDeserializer;
import com.joindata.inf.common.basic.entities.Time;
import com.joindata.inf.common.util.log.Logger;

/**
 * 自定义 JSON 日期反序列化处理，将时间戳反序列化为任意一种 java.util.Date 类型的子类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午9:27:21
 */
public class CustomJsonTimeDeserizer extends AbstractDateDeserializer
{
    private static final Logger log = Logger.get();

    @Override
    public int getFastMatchToken()
    {
        return JSONToken.LITERAL_STRING;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object value)
    {
        if(value == null)
        {
            return null;
        }

        try
        {
            return (T)new Time((String)value);
        }
        catch(ParseException e)
        {
            log.error("转换时间出错: {} 不能转换", value, e);
        }

        return null;
    }

}
