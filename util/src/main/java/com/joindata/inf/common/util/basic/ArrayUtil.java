package com.joindata.inf.common.util.basic;

import org.apache.commons.lang3.ArrayUtils;

/**
 * 数组工具类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月26日 下午10:17:23
 */
public class ArrayUtil
{
    /**
     * 生成一个数组
     * 
     * @param t 数组元素列表
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static final <E> E[] make(E... e)
    {
        return e;
    }

    /**
     * 字符串数组转换为逗号分隔的字符串
     * 
     * @param strs 字符串数组
     * @return 逗号分隔数组元素的字符串
     */
    public static final String join(String... strs)
    {
        if(strs == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < strs.length; i++)
        {
            if(i != 0)
            {
                sb.append(',');
            }
            sb.append(strs[i]);
        }
        return sb.toString();
    }

    /**
     * 合并字符串数组
     * 
     * @param arr 数组
     * @param arrs 其他数组
     * @return 合并后的数组
     */
    public static final String[] merge(String[] arr, String... arrs)
    {
        return ArrayUtils.addAll(arr, arrs);
    }

    /**
     * 删除给定字符串数组中所有的 null 值
     * 
     * @param arr 第一个数组
     * @param arrs 其他数组
     * @return 合并这些数组后，删掉 null 值的数组
     */
    @SuppressWarnings("unchecked")
    public static final <T> T[] deleteNulls(T[] arr, T... arrs)
    {
        return ArrayUtils.addAll(ArrayUtils.removeElements(arr, null, null), ArrayUtils.removeElements(arrs, null, null));
    }

    /**
     * 判断给定的数组是否为 null 或空
     * 
     * @param arr 判断的数组
     * @return true，如果为 null 或者长度为 0
     */
    public static boolean isEmpty(Object[] arr)
    {
        return ArrayUtils.isEmpty(arr);
    }

    public static void main(String[] args)
    {
        System.out.println(join("A", "B", "C"));
        System.out.println(merge(new String[]{"a", "b"}, "c", "d"));
        System.out.println(join(deleteNulls(new String[]{"a", "b", null}, new String[]{"a", null, null, "c"})));
        System.out.println(isEmpty(new String[]{}));
    }

}
