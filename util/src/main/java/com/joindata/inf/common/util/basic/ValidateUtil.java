package com.joindata.inf.common.util.basic;

import java.text.ParseException;

import com.xiaoleilu.hutool.lang.Validator;

/**
 * <h3>数据格式验证实用类</h3>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月3日 下午3:05:45
 */
public class ValidateUtil
{
    /**
     * 判断是否为 Email 地址
     * 
     * @param str 要判断的字符串
     * @return 是 Email 地址，返回 true
     */
    public static final boolean isEmail(String str)
    {
        return Validator.isEmail(str);
    }

    /**
     * 判断是否为合法的日期格式<br />
     * 匹配 yyyy-MM-dd，yyyy/MM/dd，yyyyMMdd，yyyy/MM/dd HH:mm:ss. 都可以
     * 
     * @param date 要判断的日期字符串
     * @return 如果符合那几样格式中的其中一个，都返回 true
     */
    public static final boolean isDateFormat(String date)
    {
        if(StringUtil.getLength(StringUtil.trim(date)) > 10)
        {
            try
            {
                DateUtil.DEFAULT_DATETIME_FORMATTER.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
            try
            {
                DateUtil.UGLY_DATETIME_FORMATTER.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
            try
            {
                DateUtil.UGLY_DATETIME_FORMATTER2.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
        }
        else
        {
            try
            {
                DateUtil.DEFAULT_DATE_FORMATTER.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
            try
            {
                DateUtil.UGLY_DATE_FORMATTER.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
            try
            {
                DateUtil.UGLY_DATE_FORMATTER2.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
            try
            {
                DateUtil.UGLY_DATE_FORMATTER3.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
            try
            {
                DateUtil.MOTHERFUCKER_DATE_FORMATTER.parse(date);
                return true;
            }
            catch(ParseException e)
            {
            }
        }

        return false;
    }

    /**
     * 判断是否为中文
     * 
     * @param str 要判断的字符串
     * @return 全是中文，返回 true
     */
    public static final boolean isChinese(String str)
    {
        return Validator.isChinese(str);
    }

    /**
     * 判断是否为 IPv4 地址
     * 
     * @param str 要判断的字符串
     * @return 是 IPv4 地址，返回 true
     */
    public static final boolean isIPv4(String str)
    {
        return Validator.isIpv4(str);
    }

    /**
     * 判断是否为手机号 <br />
     * 
     * @param str 要判断的字符串
     * @return 是手机号，返回 true
     */
    public static final boolean isMobile(String str)
    {
        return matchRegex(str, "^((\\+86)?|\\(\\+86\\))0?1[358]\\d{9}$");
    }

    /**
     * 判断是否为电话号码 <br />
     * 
     * @param str 要判断的字符串
     * @return 是电话号码，返回 true
     */
    public static final boolean isPhone(String str)
    {
        return matchRegex(str, "^((\\+86)?|\\(\\+86\\)|\\+86\\s)0?([1-9]\\d-?\\d{6,8}|[3-9][13579]\\d-?\\d{6,7}|[3-9][24680]\\d{2}-?\\d{6})(-\\d{3})?$");
    }

    /**
     * 判断是否为纯数字<br />
     * <i>小数、负数都不行</i>
     * 
     * @param str 要判断的字符串
     * @return 是数字，返回 true
     */
    public static final boolean isPureNumberText(String str)
    {
        return Validator.isNumber(str);
    }

    /**
     * 判断是否为数字<br />
     * <i>可以是正负小数、正负整数</i>
     * 
     * @param str 要判断的字符串
     * @return 是匹配的数字，返回 true
     */
    public static final boolean isNumberic(String str)
    {
        return matchRegex(str, "^[-+]?[0-9]+(\\.[0-9]+)?$");
    }

    /**
     * 判断是否为邮政编码<br />
     * <i>判断是否为6位数字</i>
     * 
     * @param str 要判断的字符串
     * @return 是邮编，返回 true
     */
    public static final boolean isZipCode(String str)
    {
        return Validator.isZipCode(str);
    }

    /**
     * 判断是否为 URL<br />
     * <i>要带协议前缀才行</i>
     * 
     * @param str 要判断的字符串
     * @return 是 URL，返回 true
     */
    public static final boolean isURL(String str)
    {
        return Validator.isUrl(str);
    }

    /**
     * 是否身份证号，支持一代二代<br />
     * <i>末尾是 X 的必须用大写</i>
     * 
     * @param str 要判断的字符串
     * @return 是身份证号，返回 true
     */
    public static final boolean isIDCardNo(String str)
    {
        return matchRegex(str, "(^\\d{6}((0[48]|[2468][048]|[13579][26])0229|\\d\\d(0[13578]|10|12)(3[01]|[12]\\d|0[1-9])|(0[469]|11)(30|[12]\\d|0[1-9])|(02)(2[0-8]|1\\d|0[1-9]))\\d{3}$)|(^\\d{6}((2000|(19|21)(0[48]|[2468][048]|[13579][26]))0229|(((20|19)\\d\\d)|2100)(0[13578]|10|12)(3[01]|[12]\\d|0[1-9])|(0[469]|11)(30|[12]\\d|0[1-9])|(02)(2[0-8]|1\\d|0[1-9]))\\d{3}[\\dX]$)");
    }

    /**
     * 是否大写字母，必须全是连续的字母才能判断
     * 
     * @param str 要判断的字符串
     * @return 如果全是大写，返回 true
     */
    public static boolean isUpperCase(String str)
    {
        return matchRegex(str, "^[A-Z]+$");
    }

    /**
     * 是否小写字母，必须全是连续字母才能判断
     * 
     * @param 待是否的字符串
     * @return 如果全是小写，返回 true
     */
    public static boolean isLowerCase(String str)
    {
        return matchRegex(str, "^[a-z]+$");
    }

    /**
     * 判断一个字符串是否符合正则表达式
     * 
     * @param str 要判断的字符串
     * @param regex 正则表达式字符串
     * @return 如果匹配，返回 true
     */
    public static final boolean matchRegex(String str, String regex)
    {
        return Validator.isMactchRegex(regex, str);
    }

    public static void main(String[] args)
    {
        System.out.println(isEmail("abc@iop.com"));
        System.out.println(isDateFormat("1026-03-12 10:23/42"));
        System.out.println(isChinese("卖女孩的小火柴"));
        System.out.println(isIPv4("255.255.255.255"));
        System.out.println(isMobile("+8618610721773"));
        System.out.println(isPhone("0917-7964516"));
        System.out.println(isPureNumberText("12312"));
        System.out.println(isNumberic("-3124.12351234"));
        System.out.println(isZipCode("100035"));
        System.out.println(isURL("ftp://a.b"));
        System.out.println(isIDCardNo("340524800229001"));
        System.out.println(isUpperCase("AASDF"));
        System.out.println(isLowerCase("asbasdfadf"));
        System.out.println(matchRegex("123", "^[0-9]+$"));
    }
}
