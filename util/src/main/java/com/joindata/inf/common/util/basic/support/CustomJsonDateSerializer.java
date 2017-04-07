package com.joindata.inf.common.util.basic.support;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.joindata.inf.common.basic.entities.TimeMillis;

/**
 * 自定义 JSON 日期序列化处理，将将 Date 各种类型的子类转化成合理的输出
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年11月30日 上午9:27:21
 */
public class CustomJsonDateSerializer implements ObjectSerializer
{

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException
    {
        if(object != null)
        {
            if(object instanceof TimeMillis)
            {
                serializer.out.writeLong(((TimeMillis)object).getTime());
            }
            else
            {
                serializer.out.writeString(object.toString());
            }
        }
    }

}
