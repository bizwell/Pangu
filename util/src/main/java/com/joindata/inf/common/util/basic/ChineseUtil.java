package com.joindata.inf.common.util.basic;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 汉字处理相关工具<br />
 * <i>能将汉字转换为拼音</i><br />
 * <i>拼音部分其实是 Jpinyin 类库的简单封装，你可以用 com.github.stuxuhai.jpinyin.PinyinHelper 做跟多的事，但是我搞了点更实用的功能，比如把汉字标点转换成英文标点</i><br />
 * 
 * @see com.github.stuxuhai.jpinyin.PinyinHelper
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月4日 上午10:58:59
 */
public class ChineseUtil
{
    /**
     * 将汉字标点改成英文标点<br />
     * <i>可能还不够完善，但是基本够用了</i>
     * 
     * @param str 要转换的字符串
     * @return 汉字标点会全部变成英文标点
     */
    private static final String convertChinesePunctuationToEnglish(String str)
    {
        if(str == null)
        {
            return null;
        }

        str = StringUtil.replaceAll(str, "，", ",");
        str = StringUtil.replaceAll(str, "。", ".");
        str = StringUtil.replaceAll(str, "？", "?");
        str = StringUtil.replaceAll(str, "、", ",");
        str = StringUtil.replaceAll(str, "！", "!");
        str = StringUtil.replaceAll(str, "“", "\"");
        str = StringUtil.replaceAll(str, "”", "\"");
        str = StringUtil.replaceAll(str, "‘", "'");
        str = StringUtil.replaceAll(str, "’", "'");
        str = StringUtil.replaceAll(str, "《", "\"");
        str = StringUtil.replaceAll(str, "》", "\"");
        str = StringUtil.replaceAll(str, "——", "-");
        str = StringUtil.replaceAll(str, "……", "...");
        str = StringUtil.replaceAll(str, "【", "[");
        str = StringUtil.replaceAll(str, "】", "]");

        return str;
    }

    /**
     * 将字符串转换为拼音，不带声调，用空格分隔
     * 
     * @param str 要转换的字符串
     * @return 拼音串
     * @throws PinyinException 拼音转换错误
     */
    public static final String toPinyin(String str) throws PinyinException
    {
        if(str == null)
        {
            return null;
        }

        str = convertChinesePunctuationToEnglish(str);

        return PinyinHelper.convertToPinyinString(str, " ", PinyinFormat.WITHOUT_TONE);
    }

    /**
     * 将字符串转换成带声调的拼音字符串
     * 
     * @param str 要转换的字符串
     * @param separator 拼音之间的分隔符
     * @return 带声调的拼音串
     * @throws PinyinException 拼音转换错误
     */
    public static final String toPinyinWithToneMark(String str, String separator) throws PinyinException
    {
        if(str == null)
        {
            return null;
        }
        if(separator == null)
        {
            separator = "";
        }

        str = convertChinesePunctuationToEnglish(str);
        return PinyinHelper.convertToPinyinString(str, separator, PinyinFormat.WITH_TONE_MARK);
    }

    /**
     * 将字符串转换成带声调编号的拼音字符串
     * 
     * @param str 要转换的字符串
     * @param separator 拼音之间的分隔符
     * @return 带声调编号的拼音串
     * @throws PinyinException 拼音转换错误
     */
    public static final String toPinyinWithToneNumber(String str, String separator) throws PinyinException
    {
        if(str == null)
        {
            return null;
        }
        if(separator == null)
        {
            separator = "";
        }

        str = convertChinesePunctuationToEnglish(str);
        return PinyinHelper.convertToPinyinString(str, separator, PinyinFormat.WITH_TONE_NUMBER);
    }

    /**
     * 将字符串转换拼音字符串后取首字母<br />
     * <i>如：买了个表 => mlgb</i>
     * 
     * @param str 要转换的字符串
     * @return 拼音缩写
     * @throws PinyinException 拼音转换错误
     */
    public static final String toShortPinyin(String str) throws PinyinException
    {
        if(str == null)
        {
            return null;
        }

        str = convertChinesePunctuationToEnglish(str);
        return PinyinHelper.getShortPinyin(str);
    }

    /**
     * 将字符串转换拼音字符串后取首字母，并大写<br />
     * <i>如：买了个表 => MLGB</i>
     * 
     * @param str 要转换的字符串
     * @return 拼音缩写
     * @throws PinyinException 拼音转换错误
     */
    public static final String toShortPinyinUpperCase(String str) throws PinyinException
    {
        if(str == null)
        {
            return null;
        }

        str = convertChinesePunctuationToEnglish(str);
        return StringUtil.toUpperCase(PinyinHelper.getShortPinyin(str));
    }

    /**
     * 判断一个字是否多音字
     * 
     * @param c 要判断的汉字
     * @return 是多音字，返回 true
     */
    public static final boolean hasMultipleTone(char c)
    {
        return PinyinHelper.hasMultiPinyin(c);
    }

    public static void main(String[] args) throws PinyinException
    {
        System.out.println(toPinyin("我去年买了个表，超耐磨"));
        System.out.println(toPinyinWithToneMark("我去年买了个表，超耐磨", ","));
        System.out.println(toPinyinWithToneNumber("我去年买了个表，超耐磨", ","));
        System.out.println(toShortPinyin("我去年买了个表，超耐磨"));
        System.out.println(toShortPinyinUpperCase("我去年买了个表，超耐磨"));
        System.out.println(hasMultipleTone('和'));
    }

}
