package com.joindata.inf.common.util.basic;

import java.nio.charset.Charset;

/**
 * 字符集转码工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Aug 22, 2017 5:40:09 PM
 */
public class CharsetUtil
{
    public static final String utf8ToGBK(String src)
    {
        return com.xiaoleilu.hutool.util.CharsetUtil.convert(src, Charset.forName("UTF-8"), Charset.forName("GBK"));
    }

    public static final String asciiToGBK(String src)
    {
        return com.xiaoleilu.hutool.util.CharsetUtil.convert(src, Charset.forName("ISO-8859-1"), Charset.forName("GBK"));
    }

    public static final String gbkToUtf8(String src)
    {
        return com.xiaoleilu.hutool.util.CharsetUtil.convert(src, Charset.forName("GBK"), Charset.forName("UTF-8"));
    }

    public static final String asciiToUtf8(String src)
    {
        return com.xiaoleilu.hutool.util.CharsetUtil.convert(src, Charset.forName("ISO-8859-1"), Charset.forName("UTF-8"));
    }
}
