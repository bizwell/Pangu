package com.joindata.inf.common.util.basic;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;;

/**
 * 运行时工具类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jun 15, 2017 1:20:52 PM
 */
public class RuntimeUtil
{

    /**
     * 将获取 StackTraceElement[] 转换成字符串
     * 
     * @param elements StackTraceElement 数组
     * @return 可以读的堆栈
     */
    public static final String stackTraceToString(StackTraceElement[] elements)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        for(StackTraceElement el: elements)
        {
            pw.println(el);
        }

        return sw.toString();
    }

    /**
     * 判断给定的 clzName 中的指定行是否在当前线程中被调用
     * 
     * @deprecated 暂时没法用
     * @param clzName 类名
     * @param line 行号
     * @return true，如果调用了
     */
    public static final boolean isCalled(String clzName, int startLine, int endLine)
    {
        return Arrays.stream(Thread.currentThread().getStackTrace()).filter(ele -> ele.getClassName().equals(clzName) && ele.getLineNumber() > startLine && ele.getLineNumber() < endLine).count() == 1;
    }

    public static void main(String[] args) throws IOException
    {
        System.out.println(stackTraceToString(Thread.currentThread().getStackTrace()));
    }

}