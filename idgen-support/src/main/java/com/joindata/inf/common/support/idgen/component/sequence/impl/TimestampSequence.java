package com.joindata.inf.common.support.idgen.component.sequence.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.joindata.inf.common.support.idgen.core.attacher.TimestampAttacher;

/**
 * 5位时间戳+序号的递增序列
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 24, 2017 7:36:26 PM
 */
public class TimestampSequence extends AbstractBaseSequence
{
    @Autowired
    private TimestampAttacher attacher;

    @Override
    public long next()
    {
        return attacher.attach(this.increase());
    }
}
