package com.joindata.inf.common.util.network;

import java.util.Iterator;
import java.util.Map;

import com.joindata.inf.common.util.basic.BeanUtil;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * URL 相关的工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年12月2日 下午12:02:41
 */
public class UrlUtil
{
    /**
     * 将对象转成 URL 查询串
     * 
     * @param obj 对象
     * @param hideNullKey 如果为 true，遇到 null 值属性，将不显示该属性，否则仅仅不显示值
     * @return 查询串
     */
    public static final String toQueryString(Object obj, boolean hideNullKey)
    {
        StringBuffer sb = new StringBuffer("?");

        Map<String, Object> map = BeanUtil.getFieldValues(obj);
        Iterator<String> iter = CollectionUtil.iteratorMapKey(map);
        while(iter.hasNext())
        {
            String key = iter.next();
            Object value = map.get(key);
            if(value == null)
            {
                if(hideNullKey)
                {
                    continue;
                }
                else
                {
                    value = "";
                }
            }

            sb.append(key).append("=").append(value);

            if(iter.hasNext())
            {
                sb.append('&');
            }
        }

        return sb.toString();
    }
}
