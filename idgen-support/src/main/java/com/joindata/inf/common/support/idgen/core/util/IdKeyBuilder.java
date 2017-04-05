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
}
