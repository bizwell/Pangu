package com.joindata.inf.registry.util;

import com.joindata.inf.common.util.basic.CodecUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.registry.entity.InstanceSign;

public class InstanceUtil
{
    /**
     * 把实例签名转换成对象方便读取
     * 
     * @param signBase64
     * @return
     */
    public static final InstanceSign parseInstanceSign(String instanceId)
    {
        InstanceSign sign = new InstanceSign();

        String signStr = CodecUtil.fromBase64(instanceId);
        sign.setPid(StringUtil.substringBeforeFirst(signStr, "@"));
        sign.setStartTime(StringUtil.substringAfterLast(signStr, "#"));

        String ips = StringUtil.substringAfterFirst(signStr, "[");
        ips = StringUtil.trim(StringUtil.substringBeforeLast(ips, "]"));
        sign.setHosts(StringUtil.splitToList(ips, ","));

        return sign;
    }
}
