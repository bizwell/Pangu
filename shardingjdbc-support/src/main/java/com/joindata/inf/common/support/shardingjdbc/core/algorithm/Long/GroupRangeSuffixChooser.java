package com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long;

import java.util.Collection;

import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * 分组范围匹配后缀算法
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 6:32:05 PM
 */
public class GroupRangeSuffixChooser
{
    private static final Logger log = Logger.get();

    private int volum;

    private int enob;

    private String defaultTarget;

    /**
     * @param volum 每组容量
     * @param effectiveBits 传入的判断值有效位数，-1 表示全是有效位
     */
    public GroupRangeSuffixChooser(String defaultTarget, int volum, int enob)
    {
        this.defaultTarget = defaultTarget;
        this.volum = volum;
        this.enob = enob;
    }

    /**
     * 从区间中选择匹配的字符串后缀
     * 
     * @param checkStrings 待检测字符串名集合
     * @param rangeStart 要判断的区间开始值
     * @param rangeEnd 要判断的区间结束值
     * @return 匹配的字符串集合，没有匹配返回空 Set
     */
    public Collection<String> choose(Collection<String> checkStrings, long rangeStart, long rangeEnd)
    {
        Collection<String> choosen = CollectionUtil.newHashSet();
        for(Long value = rangeStart; value <= rangeEnd; value++)
        {
            if(this.enob != -1)
            {
                String trimmedValue = StringUtil.trimLeft(StringUtil.getLast(String.valueOf(value), this.enob), '0');
                value = StringUtil.isBlank(trimmedValue) ? 0 : Long.parseLong(trimmedValue);
            }
            for(String str: checkStrings)
            {
                // 如果值除以容量后和库名后缀相等，就命中了
                if(str.endsWith("_" + String.valueOf(value / volum)))
                {
                    choosen.add(str);
                }
            }
        }

        // 都没有命中，返回默认库
        if(choosen.isEmpty())
        {
            choosen.add(defaultTarget);
            log.info("命中默认组: {}", defaultTarget);
        }

        return choosen;
    }

    /**
     * 选择匹配的字符串后缀
     * 
     * @param checkStrings 待检测字符串名集合
     * @param value 要判断的值
     * @return 匹配的字符串，没有匹配的返回 null
     */
    public String choose(Collection<String> checkStrings, long value)
    {
        if(this.enob != -1)
        {
            String trimmedValue = StringUtil.trimLeft(StringUtil.getLast(String.valueOf(value), this.enob), '0');
            value = StringUtil.isBlank(trimmedValue) ? 0 : Long.parseLong(trimmedValue);
        }

        for(String str: checkStrings)
        {
            long targetGroup = value / volum;

            // 如果值除以容量后和库名后缀相等，就命中了
            if(str.endsWith("_" + targetGroup))
            {
                return str;
            }
        }

        log.info("命中默认组: {}", defaultTarget);

        // 如果都没有命中，返回默认组
        return this.defaultTarget;
    }

    /**
     * 选择匹配的字符串后缀
     * 
     * @param checkStrings 待检测字符串名集合
     * @param values 要判断的的值集合
     * @return 匹配的字符串集合，没有匹配返回空 Set
     */
    public Collection<String> choose(Collection<String> checkStrings, Collection<Long> values)
    {
        Collection<String> choosen = CollectionUtil.newHashSet();
        for(Long value: values)
        {
            if(this.enob != -1)
            {
                String trimmedValue = StringUtil.trimLeft(StringUtil.getLast(String.valueOf(value), this.enob), '0');
                value = StringUtil.isBlank(trimmedValue) ? 0 : Long.parseLong(trimmedValue);
            }

            for(String str: checkStrings)
            {
                long targetGroup = value / volum;

                // 如果值除以容量后和库名后缀相等，就命中了
                if(str.endsWith("_" + targetGroup))
                {
                    choosen.add(str);
                }
            }
        }

        // 都没有命中，返回默认库
        if(choosen.isEmpty())
        {
            choosen.add(this.defaultTarget);
            log.info("命中默认组: {}", defaultTarget);
        }

        return choosen;
    }
}
