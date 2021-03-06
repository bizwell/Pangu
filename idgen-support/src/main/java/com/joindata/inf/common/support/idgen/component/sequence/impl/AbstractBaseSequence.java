package com.joindata.inf.common.support.idgen.component.sequence.impl;

import javax.annotation.Resource;

import com.joindata.inf.common.support.idgen.component.Sequence;
import com.joindata.inf.common.support.idgen.core.IdRangeFactory;

import lombok.Setter;

@Setter
public abstract class AbstractBaseSequence implements Sequence
{
    @Resource(name = "idRangeFactory")
    protected IdRangeFactory idRangeFactory;

    protected String name;

    protected long increase()
    {
        synchronized(this)
        {
            return idRangeFactory.getCurrentIdRange(name).next();
        }
    }
}
