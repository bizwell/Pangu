package com.joindata.inf.common.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.joda.money.BigMoney;

import com.joindata.inf.common.basic.errors.ParamErrors;
import com.joindata.inf.common.basic.exceptions.InvalidParamException;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.basic.ValidateUtil;

/**
 * 金额计算工具<br />
 * <i>顺便提一下，金额计算最好全用字符串表示，不要用容易丢失精度的基本类型，为保证计算准确。本工具类完全弃用 double 和 float 基本浮点型</i><br />
 * <i>BigDecimal 也不建议使用。老老实实用字符串比什么都好。</i>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月5日 下午6:49:48
 */
public class MoneyUtil
{
    /**
     * 计算总额
     * 
     * @param amount 金额，必须是数字，可指定多个，如果有 null 值，将忽略
     * @return 总额，小数位数取决于参与计算的小数位最长的金额，并且无论多大的数都不会丢失精度
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String sum(String... amount)
    {
        if(amount == null)
        {
            return null;
        }

        BigMoney moneys[] = new BigMoney[amount.length];
        for(int i = 0; i < amount.length; i++)
        {
            if(amount[i] == null)
            {
                continue;
            }

            // 不是数字格式，抛出输入异常
            if(!ValidateUtil.isNumberic(amount[i]))
            {
                throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount[i] + " 不是一个正确的金额。");
            }

            moneys[i] = BigMoney.parse("CNY" + amount[i]);
        }

        return StringUtil.replaceAll(BigMoney.total(moneys).toString(), "CNY ", "");
    }

    /**
     * 把字符串变成 BigDecimal
     * 
     * @param amount 金额，必须是数字
     * @return BigDecimal
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final BigDecimal toBigDecimal(String amount)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }

        return BigMoney.parse("CNY" + amount).getAmount();
    }

    /**
     * 将金额四舍五入
     * 
     * @param amount 金额
     * @param scale 保留几位小数，如果不输入
     * @return 四舍五入后的金额
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String round(String amount, int scale)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }

        return StringUtil.replaceAll(BigMoney.parse("CNY" + amount).withScale(scale, RoundingMode.HALF_UP).toString(), "CNY ", "");

    }

    /**
     * 用金额除以一个数
     * 
     * @param amount 金额
     * @param dividedBy 除数
     * @param roundingMode 舍入方式。<i>尼玛这个其实我没搞清楚</i>
     * @return 商
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String divide(String amount, String dividedBy, RoundingMode roundingMode)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(dividedBy))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, dividedBy + " 不是一个正确的数。");
        }
        BigDecimal bd = new BigDecimal(dividedBy);
        if(bd.toString().equals("0"))
        {
            throw new InvalidParamException(ParamErrors.INVALID_VALUE_ERROR, "0 不能做除数");
        }

        return StringUtil.replaceAll((BigMoney.parse("CNY" + amount).dividedBy(bd, roundingMode)).toString(), "CNY ", "");
    }

    /**
     * 用金额乘以一个数
     * 
     * @param amount 金额
     * @param multiplyBy 乘数
     * @return 积
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String multiply(String amount, String multiplyBy)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(multiplyBy))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, multiplyBy + " 不是一个正确的数。");
        }

        return StringUtil.replaceAll((BigMoney.parse("CNY" + amount).multipliedBy(new BigDecimal(multiplyBy))).toString(), "CNY ", "");
    }

    /**
     * 用金额减去一个数
     * 
     * @param amount 金额
     * @param minusValue 减数
     * @return 减掉后的金额
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String minus(String amount, String minusValue)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(minusValue))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, minusValue + " 不是一个正确的数。");
        }

        return StringUtil.replaceAll((BigMoney.parse("CNY" + amount).minus(new BigDecimal(minusValue))).toString(), "CNY ", "");
    }

    /**
     * 用金额加上一个数，意思和求和差不太多
     * 
     * @param amount 金额
     * @param plusValue 加数
     * @return 加过后的金额
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String plus(String amount, String plusValue)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(plusValue))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, plusValue + " 不是一个正确的数。");
        }

        return StringUtil.replaceAll((BigMoney.parse("CNY" + amount).plus(new BigDecimal(plusValue))).toString(), "CNY ", "");
    }

    /**
     * 求绝对值
     * 
     * @param amount 金额
     * @return 金额的绝对值
     * @throws InvalidParamException 如果某个金额不是数字格式，抛出该异常
     */
    public static final String abs(String amount)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }

        return StringUtil.replaceAll((BigMoney.parse("CNY" + amount).abs()).toString(), "CNY ", "");
    }

    /**
     * 将金额用指定舍入方式处理
     * 
     * 
     * @param amount 金额
     * @param scale 保留几位小数，如果不输入
     * @param roundingMode 舍入方式。<i>金融方面一般用 RoundingMode.HALF_EVEN 银行家舍入法</i>
     * 
     * @see java.math.RoundingMode 舍入方式的枚举常量
     * @return 舍入后的金额
     * @throws InvalidParamException 如果某个参数不是数字格式，抛出该异常
     */
    public static final String round(String amount, int scale, RoundingMode roundingMode)
    {
        // 不是数字格式，抛出输入异常
        if(!ValidateUtil.isNumberic(amount))
        {
            throw new InvalidParamException(ParamErrors.INVALID_FORMAT_ERROR, amount + " 不是一个正确的金额。");
        }

        return StringUtil.replaceAll(BigMoney.parse("CNY" + amount).withScale(scale, roundingMode).toString(), "CNY ", "");
    }

    /**
     * 格式化金额
     * 
     * @param amount
     * @return
     */
    public static final String format(String amount)
    {
        // TODO
        return null;
    }

    // TODO 获取几个金额中最小的一个
    // TODO 获取几个金额中最大的一个
    // TODO 将金额排序后返回列表或数组

    public static void main(String[] args) throws InvalidParamException
    {
        System.out.println(sum("9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "9.99999999999999999999", "0.00000000000000000009", "0.00000000000000000001"));
        System.out.println(toBigDecimal("100.00000000000000000000"));
        System.out.println(round("-1.123456789", 4));
        System.out.println(round("1.123456789", 4, RoundingMode.HALF_EVEN));
        System.out.println(divide("9999.999999999", "1", RoundingMode.HALF_UP));
        System.out.println(multiply("998.2", "2"));
        System.out.println(minus("9999.999999999", "9009"));
        System.out.println(plus("9999.999999999", "0.000000001"));
        System.out.println(abs("-11.11"));
    }
}
