package com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long;

import java.util.Collection;

import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 取模匹配后缀算法
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 6:32:05 PM
 */
public class ModSuffixChooser
{
    private static final Logger log = Logger.get();

    public ModSuffixChooser()
    {
    }

    /**
     * 从区间中选择匹配的字符串后缀
     * 
     * @param checkStrings 待检测字符串名集合
     * @param rangeStart 要取模区间开始值
     * @param rangeEnd 要取模区间结束值
     * @return 匹配的字符串集合，没有匹配返回空 Set
     */
    public Collection<String> choose(Collection<String> checkStrings, long rangeStart, long rangeEnd)
    {
        Collection<String> choosen = CollectionUtil.newHashSet();
        for(Long value = rangeStart; value <= rangeEnd; value++)
        {
            for(String str: checkStrings)
            {
                // 如果值取模后和字符串名后缀相等，就命中了
                if(str.endsWith("_" + String.valueOf(value % checkStrings.size())))
                {
                    choosen.add(str);
                }
            }
        }

        if(choosen.isEmpty())
        {
            log.warn("没有命中任何对象");
        }

        return choosen;
    }

    /**
     * 选择匹配的字符串后缀
     * 
     * @param checkStrings 待检测字符串名集合
     * @param value 要取模的值
     * @return 匹配的字符串，没有匹配的返回 null
     */
    public String choose(Collection<String> checkStrings, long value)
    {
        for(String str: checkStrings)
        {
            // 如果值取模后和字符串名后缀相等，就命中了
            if(str.endsWith("_" + String.valueOf(value % checkStrings.size())))
            {
                return str;
            }
        }

        log.warn("没有命中任何对象");

        return null;
    }

    /**
     * 选择匹配的字符串后缀
     * 
     * @param checkStrings 待检测字符串名集合
     * @param values 要取模的值集合
     * @return 匹配的字符串集合，没有匹配返回空 Set
     */
    public Collection<String> choose(Collection<String> checkStrings, Collection<Long> values)
    {
        Collection<String> choosen = CollectionUtil.newHashSet();
        for(Long value: values)
        {
            for(String str: checkStrings)
            {
                // 如果值取模后和字符串后缀相等，就命中了
                if(str.endsWith("_" + String.valueOf(value % checkStrings.size())))
                {
                    choosen.add(str);
                }
            }
        }

        if(choosen.isEmpty())
        {
            log.warn("没有命中任何对象");
        }

        return choosen;
    }
}
