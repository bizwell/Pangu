package com.joindata.inf.common.util.basic;

import java.lang.management.ManagementFactory;

import com.joindata.inf.common.util.network.NetworkUtil;

/**
 * 系统相关工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年12月1日 下午6:20:21
 */
public class SystemUtil
{
    /**
     * 获取当前运行时的进程号
     * 
     * @return 进程号
     */
    public static final String getProcessId()
    {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    /**
     * 制作当前运行时的签名
     * 
     * @return base64(pid@ip#yyyy-MM-dd)
     */
    public static final String getRuntimeSignature(String pid, String startTime)
    {
        return CodecUtil.toBase64(getProcessId() + "@" + NetworkUtil.getLocalIpv4s() + "#" + startTime);
    }

    public static void main(String[] args)
    {
        System.out.println(getProcessId());
        System.out.println(getRuntimeSignature(getProcessId(), DateUtil.getCurrentDateTimeString()));
    }
}
