package com.joindata.inf.common.util.tools;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * 随机性数据工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月10日 上午11:22:32
 */
public class RandomUtil
{
    /**
     * 生成一个随机的整数
     * 
     * @param start 从几开始
     * @param end 到几结束
     * 
     * @return 随机的整数
     */
    public static final int randomInt(int start, int end)
    {
        return RandomUtils.nextInt(start, end);
    }

    /**
     * 生成多个随机的整数，不保证会不同
     * 
     * @param start 从几开始
     * @param end 到几结束
     * @param count 生成几个
     * 
     * @return 随机整数的数组
     */
    public static final int[] randomInt(int start, int end, int count)
    {
        int randInts[] = new int[count];
        for(int i = 0; i < count; i++)
        {
            randInts[i] = RandomUtils.nextInt(start, end);
        }
        return randInts;
    }

    /**
     * 生成一个指定长度的随机字母字符串
     * 
     * @param length 字符串长度
     * @return 字母随机字符串
     */
    public static final String randomAlphabetic(int length)
    {
        return RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * 生成一个指定长度的随机数字字符串
     * 
     * @param length 字符串长度
     * @return 数字随机字符串
     */
    public static final String randomNumeric(int length)
    {
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * 生成一个指定长度的随机 ASCII 字符串<br />
     * <i>基本上是键盘能够打出来的字符</i>
     * 
     * @param length 字符串长度
     * @return ASCII 随机字符串
     */
    public static final String randomAscii(int length)
    {
        return RandomStringUtils.randomAscii(length);
    }

    /**
     * 生成一个指定长度的随机字母+数字字符串<br />
     * <i>字母或数字都有可能出现</i>
     * 
     * @param length 字符串长度
     * @return 字母+数字字符串
     */
    public static final String randomAlphanumeric(int length)
    {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * 生成随机字符串，字符串中出现的字符可以指定
     * 
     * @param length 字符串长度
     * @param chars 随机字符串中将出现的字符，可指定多个
     * @return 随机字符串
     */
    public static final String randomInSpecified(int length, char... chars)
    {
        return RandomStringUtils.random(length, chars);
    }

    public static void main(String[] args)
    {
        System.out.println(randomInt(1, 218));
        System.out.println(randomInt(1, 3, 20).length);
        System.out.println(randomAlphabetic(30));
        System.out.println(randomNumeric(20));
        System.out.println(randomAscii(30));
        System.out.println(randomAlphanumeric(30));
        System.out.println(randomInSpecified(20, '1', '3', '5', 'A', 'B', 'C', 'a'));
    }

}
