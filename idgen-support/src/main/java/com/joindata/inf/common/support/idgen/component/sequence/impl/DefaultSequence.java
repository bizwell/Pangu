/**
 * 
 */
package com.joindata.inf.common.support.idgen.component.sequence.impl;

/**
 * 不带两位命名空间的sequence id生成器
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
public class DefaultSequence extends AbstractBaseSequence
{
    @Override
    public long next()
    {
        return this.increase();
    }
}
