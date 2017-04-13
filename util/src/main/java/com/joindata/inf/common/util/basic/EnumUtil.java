package com.joindata.inf.common.util.basic;

/**
 * 枚举实用工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Apr 13, 2017 6:13:37 PM
 */
public class EnumUtil
{

    /**
     * 枚举列表中是否包含指定元素
     * 
     * @param enums 枚举列表
     * @param item 指定元素
     * @return true，如果包含
     */
    @SuppressWarnings({"rawtypes"})
    public static boolean hasItem(Enum[] enums, Enum item)
    {
        for(Enum e: enums)
        {
            if(e.equals(item))
            {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args)
    {
        System.out.println(hasItem(new Fruit[]{Fruit.Apple, Fruit.Banana}, Fruit.Apple));
    }

    public enum Fruit
    {
        Apple, Pair, Banana
    }
}
