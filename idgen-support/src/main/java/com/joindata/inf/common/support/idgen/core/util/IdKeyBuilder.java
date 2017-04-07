package com.joindata.inf.common.support.idgen.core.util;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.idgen.core.Constant;

public class IdKeyBuilder
{
    public static final String getSequenceKeySpace()
    {
        return new StringBuffer(Constant.ROOT).append('/').append(BootInfoHolder.getAppId()).append(Constant.SEQUENCE).toString();
    }

    public static final String getSequenceKey(String name)
    {
        return getSequenceKeySpace() + "/" + name;
    }

    /**
     * 对于带有prefix的序列， 获取存储prefix值对应的path
     * @param name sequence name
     * @return
     */
    public static final String getPrefixKey(String name)
    {
        return getSequenceKey(name) + Constant.PREFIX;
    }
}
