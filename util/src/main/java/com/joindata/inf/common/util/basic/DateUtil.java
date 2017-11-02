package com.joindata.inf.common.util.basic;

import org.joda.time.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h3>日期工具类</h3><br />
 * <i>有了这个工具类，妈妈再也不用担心我的日期要自己写好多代码处理了</i>
 * <p>
 * 这个封装了很多实用的日期时间处理方法，主要以字符串和 java.util.Date 对象进行输入输出。用到了 Joda 类库，可通过 toJodaDateTime() 方法获取到 Joda 的日期对象，可满足更多更奇葩的需求
 * </p>
 *
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月2日 下午12:15:44
 */
public class DateUtil {
    /**
     * 默认的日期格式 yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 操蛋的日期格式 yyyy/MM/dd
     */
    public static final String UGLY_DATE_FORMAT = "yyyy/MM/dd";

    /**
     * 操蛋的日期格式2 yyyyMMdd
     */
    public static final String UGLY_DATE_FORMAT2 = "yyyyMMdd";

    /**
     * 操蛋的日期格式3 yyyy年MM月dd日
     */
    public static final String UGLY_DATE_FORMAT3 = "yyyy'年'MM'月'dd'日'";

    /**
     * 麻痹的日期格式 M/d/yyyy
     */
    public static final String MOTHERFUCKER_DATE_FORMAT = "M/d/yyyy";

    /**
     * 默认的日期+时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 操蛋的日期+时间格式 yyyy/MM/dd HH:mm:ss
     */
    public static final String UGLY_DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    /**
     * 操蛋的日期+时间格式 yyyy/MM/dd HH:mm:ss
     */
    public static final String UGLY_DATETIME_FORMAT2 = "yyyyMMdd HH:mm:ss";

    /**
     * 默认的时间格式 HH:mm:ss
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 默认的日期格式对象 yyyy-MM-dd
     */
    public static final DateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    /**
     * 操蛋的日期格式对象 yyyy/MM/dd
     */
    public static final DateFormat UGLY_DATE_FORMATTER = new SimpleDateFormat(UGLY_DATE_FORMAT);

    /**
     * 操蛋的日期格式对象2 yyyyMMdd
     */
    public static final DateFormat UGLY_DATE_FORMATTER2 = new SimpleDateFormat(UGLY_DATE_FORMAT2);

    /**
     * 操蛋的日期格式对象3 yyyy年MM月dd日
     */
    public static final DateFormat UGLY_DATE_FORMATTER3 = new SimpleDateFormat(UGLY_DATE_FORMAT3);

    /**
     * 麻痹的日期格式对象 M/d/yyyy
     */
    public static final DateFormat MOTHERFUCKER_DATE_FORMATTER = new SimpleDateFormat(MOTHERFUCKER_DATE_FORMAT);

    /**
     * 默认的日期+时间格式对象 yyyy-MM-dd HH:mm:ss
     */
    public static final DateFormat DEFAULT_DATETIME_FORMATTER = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

    /**
     * 操蛋的日期+时间格式对象 yyyy/MM/dd HH:mm:ss
     */
    public static final DateFormat UGLY_DATETIME_FORMATTER = new SimpleDateFormat(UGLY_DATETIME_FORMAT);

    /**
     * 操蛋的日期+时间格式对象2 yyyyMMdd HH:mm:ss
     */
    public static final DateFormat UGLY_DATETIME_FORMATTER2 = new SimpleDateFormat(UGLY_DATETIME_FORMAT2);

    /**
     * 默认的时间格式对象 HH:mm:ss
     */
    public static final DateFormat DEFAULT_TIME_FORMATTER = new SimpleDateFormat(DEFAULT_TIME_FORMAT);

    public static final long SECOND_MILLIS = 1000;

    public static final long MINUTE_MILLIS = SECOND_MILLIS * 60;

    public static final long HOUR_MILLIS = MINUTE_MILLIS * 60;

    public static final long DAY_MILLIS = HOUR_MILLIS * 24;

    public static final long WEEK_MILLIS = DAY_MILLIS * 7;

    static {
        DEFAULT_DATE_FORMATTER.setLenient(false);
        UGLY_DATE_FORMATTER.setLenient(false);
        UGLY_DATE_FORMATTER2.setLenient(false);
        UGLY_DATE_FORMATTER3.setLenient(false);
        MOTHERFUCKER_DATE_FORMATTER.setLenient(false);
        DEFAULT_DATETIME_FORMATTER.setLenient(false);
        UGLY_DATETIME_FORMATTER.setLenient(false);
        UGLY_DATETIME_FORMATTER2.setLenient(false);
        DEFAULT_TIME_FORMATTER.setLenient(false);
    }

    /**
     * 获取当前日期对象
     *
     * @return 当前日期对象
     */
    public static final Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前日期字符串
     *
     * @return 当前日期字符串（<i>yyyy-MM-dd</i>）
     */
    public static final String getCurrentDateString() {
        return new DateTime().toString(DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前日期+时间字符串
     *
     * @return 当前日期+时间字符串（<i>yyyy-MM-dd HH:mm:ss</i>）
     */
    public static final String getCurrentDateTimeString() {
        return new DateTime().toString(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 获取当前时间字符串
     *
     * @return 当前时间字符串（<i>HH:mm:ss</i>）
     */
    public static final String getCurrentTimeString() {
        return new DateTime().toString(DEFAULT_TIME_FORMAT);
    }

    /**
     * 获取当前时刻的日历对象
     *
     * @return 日历对象
     */
    public static final Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 获取指定日期时间的日历对象
     *
     * @param date 指定的时间对象
     * @return 日历对象
     */
    public static final Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 获取默认日期或日期时间格式的日历对象
     *
     * @param date 指定的日期字符串
     * @return 日历对象
     * @throws ParseException 如果传入的字符串不符合格式，抛出该异常
     */
    public static final Calendar getCalendar(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date));
        return calendar;
    }

    /**
     * 获取指定日期时间格式的日历对象
     *
     * @param date    指定的日期字符串
     * @param pattern 日期格式字符串
     * @return 日历对象
     * @throws ParseException 如果传入的字符串不符合格式，抛出该异常
     */
    public static final Calendar getCalendar(String date, String pattern) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, pattern));
        return calendar;
    }

    /**
     * 根据指定年月日生成日期对象 <br />
     * <i>时分秒将为0</i>
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日期对象
     */
    public static final Date makeDate(int year, int month, int day) {
        return new LocalDate(year, month, day).toDate();
    }

    /**
     * 根据指定年月日时分秒生成日期时间对象
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return 日期时间对象
     */
    public static final Date makeDateTime(int year, int month, int day, int hour, int minute, int second) {
        return new DateTime(year, month, day, hour, minute, second, 0).toDate();
    }

    /**
     * 将默认格式的日期或日期时间字符串转换为日期对象 <br />
     * <i>如果未指定时分秒，则该字段将置为0</i>
     *
     * @param date 日期字符串（yyyy-MM-dd，HH:mm:ss 或 yyyy-MM-dd HH:mm:ss）
     * @return 日期对象，但如果不符合任何一个格式，返回 null
     */
    public static final Date parseDate(String date) {
        DateFormat formatters[] = {DEFAULT_DATETIME_FORMATTER, UGLY_DATETIME_FORMATTER, UGLY_DATETIME_FORMATTER2, DEFAULT_TIME_FORMATTER, DEFAULT_DATE_FORMATTER, UGLY_DATE_FORMATTER, UGLY_DATE_FORMATTER2, UGLY_DATE_FORMATTER3, MOTHERFUCKER_DATE_FORMATTER};

        for (DateFormat formatter : formatters) {
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                continue;
            }
        }

        return null;
    }

    /**
     * 将默认格式的日期或日期时间字符串转换为指定的日期对象 <br />
     * <i>如果未指定时分秒，则该字段将置为0</i>
     *
     * @param <T>  必须是 java.util.Date 的子类
     * @param date 日期字符串（yyyy-MM-dd，HH:mm:ss 或 yyyy-MM-dd HH:mm:ss）
     * @param clz  要输出的指定对象 Class
     * @return 日期对象
     * @throws ParseException         如果传入的字符串不符合格式，抛出该异常
     * @throws IllegalAccessException 创建新对象失败，抛出该异常
     * @throws InstantiationException 创建新对象失败，抛出该异常
     */
    public static final <T extends Date> T parseDate(String date, Class<T> clz) throws ParseException, InstantiationException, IllegalAccessException {
        T t = (T) clz.newInstance();
        t.setTime(parseDate(date).getTime());
        return t;
    }

    /**
     * 将日期字符串转换为日期对象 <br />
     * <i>如果未指定时分秒，则该字段将置为0</i>
     *
     * @param date    日期字符串
     * @param pattern 日期格式字符串
     * @return 日期对象
     * @throws ParseException 如果传入的字符串不符合格式，抛出该异常
     */
    public static final Date parseDate(String date, String pattern) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(date);
    }

    /**
     * 用默认格式格式化日期
     *
     * @param date 要格式化的日期对象
     * @return 格式化后的日期字符串
     */
    public static final String formatDate(Date date) {
        return new DateTime(date).toString(DEFAULT_DATE_FORMAT);
    }

    /**
     * 用默认格式格式化日期时间
     *
     * @param date 要格式化的日期对象
     * @return 格式化后的日期字符串
     */
    public static final String formatDateTime(Date date) {
        return new DateTime(date).toString(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 用默认格式格式化时间
     *
     * @param date 要格式化的日期对象
     * @return 格式化后的时间字符串
     */
    public static final String formatTime(Date date) {
        return new DateTime(date).toString(DEFAULT_TIME_FORMAT);
    }

    /**
     * 用指定的格式格式化日期
     *
     * @param date    要格式化的日期
     * @param pattern 格式，如 yyyy-MM-dd
     * @return 格式化后的日期字符串
     */
    public static final String formatDate(Date date, String pattern) {
        return new DateTime(date).toString(pattern);
    }

    /**
     * 获取 a 到 b 两个日期间隔多少年
     *
     * @param a 第一个时间对象
     * @param b 第二个时间对象
     * @return 年数
     */
    public static final int getYearsBetween(Date a, Date b) {
        return Years.yearsBetween(toJodaDateTime(a), toJodaDateTime(b)).getYears();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少年
     *
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 年数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getYearsBetween(String a, String b) throws ParseException {
        return Years.yearsBetween(toJodaDateTime(a), toJodaDateTime(b)).getYears();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少月
     *
     * @param a 第一个时间对象
     * @param b 第二个时间对象
     * @return 月数
     */
    public static final int getMonthsBetween(Date a, Date b) {
        return Months.monthsBetween(toJodaDateTime(a), toJodaDateTime(b)).getMonths();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少月
     *
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 月数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getMonthsBetween(String a, String b) throws ParseException {
        return Months.monthsBetween(toJodaDateTime(a), toJodaDateTime(b)).getMonths();
    }


    public static Date getBeginDate(Date startDate) {
        if (startDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date getEndDate(Date endDate) {
        if (null == endDate) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    /**
     * @param date   日期
     * @param format 日期格式 参照new SimpleDateformat yyyy MM dd hh HH mm ss SSS
     * @return
     */
    public static String formatDate(Calendar date, String format) {
        if (format == null || format.length() == 0)
            return date.toString();
        StringBuilder sbBuffer = new StringBuilder();
        int[] param = new int[]{date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE),
                date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.HOUR), date.get(Calendar.MINUTE),
                date.get(Calendar.SECOND), date.get(Calendar.MILLISECOND)};
        for (int i = format.length() - 1; i >= 0; i--) {
            char ch = format.charAt(i);
            switch (ch) {
                case 'y':
                    sbBuffer.append(param[0] % 10);
                    param[0] = param[0] / 10;
                    break;
                case 'M':
                    sbBuffer.append(param[1] % 10);
                    param[1] = param[1] / 10;
                    break;
                case 'd':
                    sbBuffer.append(param[2] % 10);
                    param[2] = param[2] / 10;
                    break;
                case 'H':
                    sbBuffer.append(param[3] % 10);
                    param[3] = param[3] / 10;
                    break;
                case 'h':
                    sbBuffer.append(param[4] % 10);
                    param[4] = param[4] / 10;
                    break;
                case 'm':
                    sbBuffer.append(param[5] % 10);
                    param[5] = param[5] / 10;
                    break;
                case 's':
                    sbBuffer.append(param[6] % 10);
                    param[6] = param[6] / 10;
                    break;
                case 'S':
                    sbBuffer.append(param[7] % 10);
                    param[7] = param[7] / 10;
                    break;
                /**
                 * 其他非日期格式字符
                 */
                default:
                    sbBuffer.append(ch);
            }

        }
        return sbBuffer.reverse().toString();
    }


    /**
     * 获取 a 到 b 两个日期间隔多少天
     *
     * @param a 第一个时间对象
     * @param b 第二个时间对象
     * @return 天数
     */
    public static final int getDaysBetween(Date a, Date b) {
        return Days.daysBetween(toJodaDateTime(a), toJodaDateTime(b)).getDays();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少天
     *
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 天数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getDaysBetween(String a, String b) throws ParseException {
        return Days.daysBetween(toJodaDateTime(a), toJodaDateTime(b)).getDays();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少小时
     *
     * @param a 第一个时间对象
     * @param b 第二个时间对象
     * @return 小时数
     */
    public static final int getHoursBetween(Date a, Date b) {
        return Hours.hoursBetween(toJodaDateTime(a), toJodaDateTime(b)).getHours();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少小时
     *
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 小时数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getHoursBetween(String a, String b) throws ParseException {
        return Hours.hoursBetween(toJodaDateTime(a), toJodaDateTime(b)).getHours();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少分钟
     *
     * @param a 第一个时间对象
     * @param b 第二个时间对象
     * @return 分钟数
     */
    public static final int getMinutesBetween(Date a, Date b) {
        return Minutes.minutesBetween(toJodaDateTime(a), toJodaDateTime(b)).getMinutes();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少分钟
     *
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 分钟数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getMinutesBetween(String a, String b) throws ParseException {
        return Minutes.minutesBetween(toJodaDateTime(a), toJodaDateTime(b)).getMinutes();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少秒
     *
     * @param a 第一个时间对象
     * @param b 第二个时间对象
     * @return 秒数
     */
    public static final int getSecondsBetween(Date a, Date b) {
        return Seconds.secondsBetween(toJodaDateTime(a), toJodaDateTime(b)).getSeconds();
    }

    /**
     * 获取 a 到 b 两个日期间隔多少秒
     *
     * @param a 第一个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @param b 第二个时间字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return 秒数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getSecondsBetween(String a, String b) throws ParseException {
        return Seconds.secondsBetween(toJodaDateTime(a), toJodaDateTime(b)).getSeconds();
    }

    /**
     * 取得 Joda 日期时间对象
     *
     * @param date 要转换的对象
     * @return Joda 对象
     */
    public static final DateTime toJodaDateTime(Date date) {
        return new DateTime(date);
    }

    /**
     * 取得 Joda 日期时间对象
     *
     * @param date 要转换的日期字符串（yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss）
     * @return Joda 对象
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final DateTime toJodaDateTime(String date) throws ParseException {
        return toJodaDateTime(parseDate(date));
    }

    /**
     * 给指定日期对象加上多少年后得出新的日期对象
     *
     * @param date  原始日期对象
     * @param years 要加的年数，<b>为负数时计算减多少年</b>
     * @return 计算后的日期对象
     */
    public static final Date plusYears(Date date, int years) {
        return toJodaDateTime(date).plusYears(years).toDate();
    }

    /**
     * 给指定日期字符串加上多少年后得出新的日期字符串
     *
     * @param date  原始日期字符串
     * @param years 要加的年数，<b>为负数时计算减多少年</b>
     * @return 计算后的日期字符串
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final Date plusYears(String date, int years) throws ParseException {
        return toJodaDateTime(date).plusYears(years).toDate();
    }

    /**
     * 给指定日期对象加上多少月后得出新的日期对象
     *
     * @param date   原始日期对象
     * @param months 要加的月数，<b>为负数时计算减多少月</b>
     * @return 计算后的日期对象
     */
    public static final Date plusMonths(Date date, int months) {
        return toJodaDateTime(date).plusMonths(months).toDate();
    }

    /**
     * 给指定日期字符串加上多少月后得出新的日期字符串
     *
     * @param date   原始日期字符串
     * @param months 要加的月数，<b>为负数时计算减多少月</b>
     * @return 计算后的日期字符串
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final Date plusMonths(String date, int months) throws ParseException {
        return toJodaDateTime(date).plusMonths(months).toDate();
    }

    /**
     * 给指定日期对象加上多少天后得出新的日期对象
     *
     * @param date 原始日期对象
     * @param days 要加的天数，<b>为负数时计算减多少天</b>
     * @return 计算后的日期对象
     */
    public static final Date plusDays(Date date, int days) {
        return toJodaDateTime(date).plusDays(days).toDate();
    }

    /**
     * 给指定日期字符串加上多少天后得出新的日期字符串
     *
     * @param date 原始日期字符串
     * @param days 要加的天数，<b>为负数时计算减多少天</b>
     * @return 计算后的日期字符串
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final Date plusDays(String date, int days) throws ParseException {
        return toJodaDateTime(date).plusDays(days).toDate();
    }

    /**
     * 给指定日期对象加上多少小时后得出新的日期对象
     *
     * @param date  原始日期对象
     * @param hours 要加的小时数，<b>为负数时计算减多少小时</b>
     * @return 计算后的日期对象
     */
    public static final Date plusHours(Date date, int hours) {
        return toJodaDateTime(date).plusHours(hours).toDate();
    }

    /**
     * 给指定日期字符串加上多少小时后得出新的日期字符串
     *
     * @param date  原始日期字符串
     * @param hours 要加的小时数，<b>为负数时计算减多少小时</b>
     * @return 计算后的日期字符串
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final Date plusHours(String date, int hours) throws ParseException {
        return toJodaDateTime(date).plusHours(hours).toDate();
    }

    /**
     * 给指定日期对象加上多少分钟后得出新的日期对象
     *
     * @param date    原始日期对象
     * @param minutes 要加的分钟数，<b>为负数时计算减多少分钟</b>
     * @return 计算后的日期对象
     */
    public static final Date plusMinutes(Date date, int minutes) {
        return toJodaDateTime(date).plusMinutes(minutes).toDate();
    }

    /**
     * 给指定日期字符串加上多少分钟后得出新的日期字符串
     *
     * @param date    原始日期字符串
     * @param minutes 要加的分钟数，<b>为负数时计算减多少分钟</b>
     * @return 计算后的日期字符串
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final Date plusMinutes(String date, int minutes) throws ParseException {
        return toJodaDateTime(date).plusMinutes(minutes).toDate();
    }

    /**
     * 给指定日期对象加上多少秒后得出新的日期对象
     *
     * @param date    原始日期对象
     * @param seconds 要加的秒数，<b>为负数时计算减多少秒</b>
     * @return 计算后的日期对象
     */
    public static final Date plusSeconds(Date date, int seconds) {
        return toJodaDateTime(date).plusSeconds(seconds).toDate();
    }

    /**
     * 给指定日期字符串加上多少秒后得出新的日期字符串
     *
     * @param date    原始日期字符串
     * @param seconds 要加的秒数，<b>为负数时计算减多少秒</b>
     * @return 计算后的日期字符串
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final Date plusSeconds(String date, int seconds) throws ParseException {
        return toJodaDateTime(date).plusSeconds(seconds).toDate();
    }

    /**
     * 判断第三个参数的日期是否在第一个参数和第二个参数区间之中
     *
     * @param a    日期 A
     * @param b    日期 B
     * @param date 要判断的日期对象
     * @return 是否在 A 和 B 区间内。<i>如果等于 B，也返回 true</i>
     */
    public static final boolean isBetween(Date a, Date b, Date date) {
        if (a == null || b == null || date == null) {
            return false;
        }

        Interval interval = new Interval(toJodaDateTime(a), toJodaDateTime(b));
        return interval.contains(date.getTime()) || date.equals(b);
    }

    /**
     * 判断第三个参数的日期是否在第一个参数和第二个参数区间之中（字符串版）
     *
     * @param a    日期 A
     * @param b    日期 B
     * @param date 要判断的日期字符串
     * @return 是否在 A 和 B 区间内。<i>如果等于 B，也返回 true</i>
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final boolean isBetween(String a, String b, String date) throws ParseException {
        if (a == null || b == null || date == null) {
            return false;
        }

        Interval interval = new Interval(toJodaDateTime(a), toJodaDateTime(b));
        return interval.contains(toJodaDateTime(date)) || date.equals(b);
    }

    /**
     * 获取两个日期之间的毫秒数
     *
     * @param a 日期A
     * @param b 日期B
     * @return 日期之间相差的毫秒数
     */
    public static final long getMillisBetween(Date a, Date b) {
        return new Duration(toJodaDateTime(a), toJodaDateTime(b)).getMillis();
    }

    /**
     * 获取两个日期之间的毫秒数（字符串版）
     *
     * @param a 日期A
     * @param b 日期B
     * @return 日期之间相差的毫秒数
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final long getMillisBetween(String a, String b) throws ParseException {
        return new Duration(toJodaDateTime(a), toJodaDateTime(b)).getMillis();
    }

    /**
     * 获取 a 和 b 当中比较晚的一个日期对象
     *
     * @param a a 日期对象
     * @param b b 日期对象
     * @return 更晚的一个日期对象，<b>如果相同，返回 null</b>
     */
    public static final Date getLater(Date a, Date b) {
        if (a.equals(b)) {
            return null;
        } else if (a.after(b)) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * 获取 a 和 b 当中比较早的一个日期对象
     *
     * @param a a 日期对象
     * @param b b 日期对象
     * @return 更早的一个日期对象，<b>如果相同，返回 null</b>
     */
    public static final Date getEarlier(Date a, Date b) {
        if (a.equals(b)) {
            return null;
        } else if (a.before(b)) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * 获取指定日期那月月初的日期对象
     *
     * @param date 要找月初的日期对象
     * @return 该月月初的日期对象，将省去时分秒
     */
    public static final Date getFirstDayOfMonth(Date date) {
        return toJodaDateTime(date).dayOfMonth().withMinimumValue().toDate();
    }

    /**
     * 获取指定日期那月月末的日期对象
     *
     * @param date 要找月末的日期对象
     * @return 该月月末的日期对象，将省去时分秒
     */
    public static final Date getLastDayOfMonth(Date date) {
        return toJodaDateTime(date).dayOfMonth().withMaximumValue().toDate();
    }

    /**
     * 获取日期中的年份
     *
     * @param date 日期对象
     * @return 年份
     */
    public static final int getYear(Date date) {
        return toJodaDateTime(date).getYear();
    }

    /**
     * 获取日期中的年份
     *
     * @param date 日期字符串
     * @return 年份
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getYear(String date) throws ParseException {
        return toJodaDateTime(date).getYear();
    }

    /**
     * 获取日期中的月份
     *
     * @param date 日期对象
     * @return 月份
     */
    public static final int getMonth(Date date) {
        return toJodaDateTime(date).getMonthOfYear();
    }

    /**
     * 获取日期中的月份
     *
     * @param date 日期对象
     * @return 月份
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getMonth(String date) throws ParseException {
        return toJodaDateTime(date).getMonthOfYear();
    }

    /**
     * 获取某天是星期几
     *
     * @param date 日期对象
     * @return 星期几，数字表示
     */
    public static final int getDayOfWeek(Date date) {
        return toJodaDateTime(date).getDayOfWeek();
    }

    /**
     * 获取某天是星期几
     *
     * @param date 日期字符串
     * @return 星期几，数字表示
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getDayOfWeek(String date) throws ParseException {
        return toJodaDateTime(date).getDayOfWeek();
    }

    /**
     * 获取日期中的天
     *
     * @param date 日期对象
     * @return 天
     */
    public static final int getDay(Date date) {
        return toJodaDateTime(date).getDayOfMonth();
    }

    /**
     * 获取日期中的天
     *
     * @param date 日期对象
     * @return 天
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getDay(String date) throws ParseException {
        return toJodaDateTime(date).getDayOfMonth();
    }

    /**
     * 获取时间中的小时
     *
     * @param date 日期对象
     * @return 小时
     */
    public static final int getHour(Date date) {
        return toJodaDateTime(date).getHourOfDay();
    }

    /**
     * 获取时间中的小时
     *
     * @param date 日期对象
     * @return 小时
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getHour(String date) throws ParseException {
        return toJodaDateTime(date).getHourOfDay();
    }

    /**
     * 获取时间中的分钟
     *
     * @param date 日期对象
     * @return 分钟
     */
    public static final int getMinute(Date date) {
        return toJodaDateTime(date).getMinuteOfHour();
    }

    /**
     * 获取时间中的分钟
     *
     * @param date 日期对象
     * @return 分钟
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getMinute(String date) throws ParseException {
        return toJodaDateTime(date).getMinuteOfHour();
    }

    /**
     * 获取时间中的秒
     *
     * @param date 日期对象
     * @return 秒
     */
    public static final int getSecond(Date date) {
        return toJodaDateTime(date).getSecondOfMinute();
    }

    /**
     * 获取时间中的秒
     *
     * @param date 日期对象
     * @return 秒
     * @throws ParseException 如果传入的日期字符串不符合格式，抛出该异常
     */
    public static final int getSecond(String date) throws ParseException {
        return toJodaDateTime(date).getSecondOfMinute();
    }

    public static void main(String[] args) throws ParseException, InstantiationException, IllegalAccessException {
        System.out.println(DateUtil.getCurrentDate());
        System.out.println(DateUtil.getCurrentDateString());
        System.out.println(DateUtil.getCurrentDateTimeString());

        System.out.println(DateUtil.getCurrentCalendar().getTime());
        System.out.println(DateUtil.getCalendar(new Date()).getTime());
        System.out.println(DateUtil.getCalendar("2015-11-3").getTime());
        System.out.println(DateUtil.getCalendar("2015-12-12T12:32:12", "yyyy-MM-dd'T'HH:mm:ss").getTime());

        System.out.println(DateUtil.makeDate(2015, 11, 2));
        System.out.println(DateUtil.makeDateTime(2015, 11, 2, 19, 43, 00));

        System.out.println(DateUtil.parseDate("20150401 12:32:12"));
        System.out.println(DateUtil.parseDate("2015-12-12T12:32:12", "yyyy-MM-dd'T'HH:mm:ss"));
        System.out.println(DateUtil.parseDate("2015-12-13", com.joindata.inf.common.basic.entities.DateTime.class));
        System.out.println(DateUtil.formatDate(new Date()));
        System.out.println(DateUtil.formatDateTime(new Date()));
        System.out.println(DateUtil.formatTime(new Date()));
        System.out.println(DateUtil.formatDate(new Date(), "yyyy-MM-dd'T'HH:mm:ss.SSS'+08:00'"));

        System.out.println(DateUtil.getYearsBetween(makeDate(2010, 10, 1), makeDate(2011, 11, 1)));
        System.out.println(DateUtil.getYearsBetween("2010-10-1", "2011-11-1"));
        System.out.println(DateUtil.getMonthsBetween(makeDate(2010, 10, 1), makeDate(2010, 11, 1)));
        System.out.println(DateUtil.getMonthsBetween("2010-10-1", "2010-11-1"));
        System.out.println(DateUtil.getDaysBetween(makeDate(2010, 10, 1), makeDate(2011, 10, 1)));
        System.out.println(DateUtil.getDaysBetween("2010-10-1", "2011-10-1"));
        System.out.println(DateUtil.getHoursBetween(makeDate(2010, 10, 1), makeDate(2010, 10, 2)));
        System.out.println(DateUtil.getHoursBetween("2010-10-1", "2010-10-2"));
        System.out.println(DateUtil.getMinutesBetween(makeDateTime(2010, 10, 1, 0, 0, 0), makeDateTime(2010, 10, 1, 0, 1, 0)));
        System.out.println(DateUtil.getMinutesBetween("2010-10-1 00:00:00", "2010-10-1 00:01:00"));
        System.out.println(DateUtil.getSecondsBetween(makeDateTime(2010, 10, 1, 0, 0, 0), makeDateTime(2010, 10, 1, 0, 1, 0)));
        System.out.println(DateUtil.getSecondsBetween("2010-10-1 00:00:00", "2010-10-1 00:01:00"));

        System.out.println(DateUtil.toJodaDateTime(new Date()));
        System.out.println(DateUtil.toJodaDateTime("2015-11-11"));

        System.out.println(DateUtil.plusYears(new Date(), -1));
        System.out.println(DateUtil.plusYears("2015-11-2 19:54:00", -1));
        System.out.println(DateUtil.plusMonths(new Date(), -1));
        System.out.println(DateUtil.plusMonths("2015-11-2 19:54:00", -1));
        System.out.println(DateUtil.plusDays(new Date(), -1));
        System.out.println(DateUtil.plusDays("2015-11-2 19:54:00", -1));
        System.out.println(DateUtil.plusHours(new Date(), -1));
        System.out.println(DateUtil.plusHours(new Date(), -1) + " = " + getCurrentDate() + " - 1h");
        System.out.println(DateUtil.plusHours("2015-11-2 19:00:00", -1) + " = " + parseDate("2015-11-2 19:00:00") + " - 1h");
        System.out.println(DateUtil.plusMinutes(new Date(), -1) + " = " + getCurrentDate() + " - 1m");
        System.out.println(DateUtil.plusMinutes("2015-11-2 19:00:00", -1) + " = " + parseDate("2015-11-2 19:00:00") + " - 1m ");
        System.out.println(DateUtil.plusSeconds(new Date(), -1) + " = " + new Date() + " - 1s");
        System.out.println(DateUtil.plusSeconds("2015-11-2 19:00:00", -1) + " = " + parseDate("2015-11-2 19:00:00") + " - 1s");

        System.out.println(DateUtil.getDayOfWeek(getCurrentDate()));

        System.out.println(DateUtil.getLastDayOfMonth(new Date()));
        System.out.println(DateUtil.getFirstDayOfMonth(new Date()));

        System.out.println(DateUtil.isBetween(makeDate(2011, 1, 1), makeDate(2011, 2, 1), makeDate(2011, 2, 1)));
        System.out.println(DateUtil.isBetween("2011-1-1", "2011-2-1", "2011-1-1"));

        System.out.println(DateUtil.getMillisBetween(makeDate(2011, 1, 1), makeDate(2011, 1, 2)));
        System.out.println(DateUtil.getMillisBetween("2011-1-1", "2011-1-2"));

        System.out.println(DateUtil.getLater(makeDate(2011, 1, 3), makeDate(2011, 1, 2)));
        System.out.println(DateUtil.getEarlier(makeDate(2011, 1, 1), makeDate(2011, 1, 2)));

        System.out.println(DateUtil.getFirstDayOfMonth(getCurrentDate()));
        System.out.println(DateUtil.getLastDayOfMonth(getCurrentDate()));

        System.out.println(DateUtil.getYear(new Date()));
        System.out.println(DateUtil.getYear("2015-11-2 21:01:30"));
        System.out.println(DateUtil.getMonth(new Date()));
        System.out.println(DateUtil.getMonth("2015-11-2 21:01:30"));
        System.out.println(DateUtil.getDay(new Date()));
        System.out.println(DateUtil.getDay("2015-11-2 21:01:30"));
        System.out.println(DateUtil.getDayOfWeek(DateUtil.plusDays(new Date(), 2)));
        System.out.println(DateUtil.getDayOfWeek(DateUtil.plusDays("2015-11-2 21:01:30", 2)));
        System.out.println(DateUtil.getHour(new Date()));
        System.out.println(DateUtil.getHour("2015-11-2 21:01:30"));
        System.out.println(DateUtil.getMinute(new Date()));
        System.out.println(DateUtil.getMinute("2015-11-2 21:01:30"));
        System.out.println(DateUtil.getSecond(new Date()));
        System.out.println(DateUtil.getSecond("2015-11-2 21:01:30"));
    }
}
