package com.joindata.inf.common.support.idgen.core.attacher;

import java.util.Map;

import com.joindata.inf.common.support.idgen.core.IDGeneratorException;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;

// TODO 补充文档
public class PrefixAttacher
{
    private static final Map<Integer, Long> PrefixValueMap = CollectionUtil.newMap();

    private int offset;

    public PrefixAttacher(int offset)
    {
        this.offset = offset;
    }

    public long attach(long srcVal, int prefix)
    {
        long val = srcVal + getPrefixValue(prefix);

        if(val < 0)
        {
            throw new IDGeneratorException("ID 不够用了");
        }

        return val;
    }

    private long getPrefixValue(int prefix)
    {
        if(!PrefixValueMap.containsKey(prefix))
        {
            PrefixValueMap.put(prefix, Long.parseLong(prefix + StringUtil.makeRepeat('0', offset)));
        }

        return PrefixValueMap.get(prefix);
    }
}
