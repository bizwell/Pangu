package com.joindata.inf.common.util.basic;

import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import com.xiaoleilu.hutool.util.ObjectUtil;

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
     * 各种类型数组转换为逗号分隔的字符串
     * 
     * @param objs 各种类型数组
     * @return 逗号分隔数组元素的字符串
     */
    @SuppressWarnings("unchecked")
    public static final <T> String join(T... objs)
    {
        if(objs == null)
        {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < objs.length; i++)
        {
            if(i != 0)
            {
                sb.append(',');
            }
            sb.append(objs[i]);
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

    /**
     * 输出数组
     * 
     * @return 每个数组元素的字符串表示
     */
    public static final String toString(Object arr)
    {
        return ArrayUtils.toString(arr);
    }

    /**
     * 输出字符串数组
     * 
     * @return 每个数组元素的字符串表示
     */
    @SafeVarargs
    public static final String toString(String... arr)
    {
        return ArrayUtil.join(arr);
    }

    /**
     * 在数组中是否包含指定字符串
     * 
     * @param caseSensitive 是否区分大小写
     * @param arr 数组
     * @param str 要检测的字符串
     * @return true, 如果包含
     */
    public static boolean contains(boolean caseSensitive, String[] arr, String str)
    {
        if(arr == null)
        {
            return false;
        }

        for(String item: arr)
        {
            if(caseSensitive)
            {
                if(StringUtil.isEqualsIgnoreCase(item, str))
                {
                    return true;
                }
            }
            else
            {
                if(StringUtil.isEquals(item, str))
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 在数组中是否包含指定元素<br />
     * <i>如果数组中有 null，而判断的对象是 null，也返回 true</i>
     * 
     * @param arr 数组
     * @param str 要检测的对象
     * @return true, 如果包含
     */
    public static boolean contains(Object[] arr, Object obj)
    {
        if(arr == null)
        {
            return false;
        }

        for(Object item: arr)
        {
            if(ObjectUtil.equal(item, obj))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 把字符串数组转换成 int 数组<br />
     * <strong>风险自控</strong>
     * 
     * @param arr 字符串数组
     * @return int 数组
     */
    public static int[] toIntArray(String... arr)
    {
        if(arr == null)
        {
            return null;
        }

        return Stream.of(arr).mapToInt(p -> Integer.parseInt(StringUtil.trim(p))).toArray();
    }

    /**
     * 把字符串数组转换成 long 数组<br />
     * <strong>风险自控</strong>
     * 
     * @param arr 字符串数组
     * @return long 数组
     */
    public static long[] toLongArray(String... arr)
    {
        if(arr == null)
        {
            return null;
        }

        return Stream.of(arr).mapToLong(p -> Integer.parseInt(StringUtil.trim(p))).toArray();
    }

    /**
     * 把字符串切成数组后将该数组转换成 int 数组<br />
     * <strong>风险自控</strong>
     * 
     * @see StringUtil#splitToArray(String, String)
     * @param str 字符串
     * @param separators 元素分隔符
     * @return int 数组
     */
    public static int[] splitToIntArray(String str, String separators)
    {
        String[] arr = StringUtil.splitToArray(str, separators);
        if(arr == null)
        {
            return null;
        }

        return Stream.of(arr).mapToInt(p -> Integer.parseInt(StringUtil.trim(p))).toArray();
    }

    /**
     * 把字符串切成数组后将该数组转换成 long 数组<br />
     * <strong>风险自控</strong>
     * 
     * @see StringUtil#splitToArray(String, String)
     * @param str 字符串
     * @param separators 元素分隔符
     * @return long 数组
     */
    public static long[] splitToLongArray(String str, String separators)
    {
        String[] arr = StringUtil.splitToArray(str, separators);
        if(arr == null)
        {
            return null;
        }

        return Stream.of(arr).mapToLong(p -> Integer.parseInt(StringUtil.trim(p))).toArray();
    }

    public static void main(String[] args)
    {
        System.out.println(join("A", "B", "C"));
        System.out.println(merge(new String[]{"a", "b"}, "c", "d"));
        System.out.println(join(deleteNulls(new String[]{"a", "b", null}, new String[]{"a", null, null, "c"})));
        System.out.println(isEmpty(new String[]{}));
        System.out.println(toString("A", "B", "C"));
        System.out.println(toIntArray("1", "2", "3")[2]);
        System.out.println(toLongArray("1", "2", "3")[2]);
        System.out.println(splitToIntArray("1, 2,3", ",")[1]);
        System.out.println(splitToLongArray("1,2 ,3", ",")[0]);
    }
}
