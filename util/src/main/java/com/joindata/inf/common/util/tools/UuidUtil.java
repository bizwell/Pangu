package com.joindata.inf.common.util.tools;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * UUID 相关工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月10日 上午11:14:53
 */
public class UuidUtil
{
    /**
     * 生成一个 UUID 字符串
     * 
     * @return 一个 UUID 字符串
     */
    public static final String make()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成一个 UUID 字符串，不包含横线
     * 
     * @return 一个 UUID 字符串
     */
    public static final String makeNoSlash()
    {
        return UUID.randomUUID().toString().replaceAll(Pattern.quote("-"), "");
    }

    /**
     * 生成指定数目个 UUID 字符串
     * 
     * @return 多个 UUID 字符串组成的数组
     */
    public static final String[] make(int count)
    {
        String uuids[] = new String[count];
        for(int i = 0; i < count; i++)
        {
            uuids[i] = UUID.randomUUID().toString();
        }

        return uuids;
    }

    public static void main(String[] args)
    {
        System.out.println(make());
    }
}
