/**
 * 
 */
package com.joindata.inf.common.support.idgen.component.sequence.impl;

import javax.annotation.Resource;

import com.joindata.inf.common.support.idgen.core.IDGeneratorException;
import com.joindata.inf.common.support.idgen.core.SequenceRepository;
import com.joindata.inf.common.support.idgen.core.attacher.PrefixAttacher;
import com.joindata.inf.common.support.idgen.core.util.IdKeyBuilder;

import lombok.Setter;

/**
 * 
 * 带有两位命名空间的sequence生成器。 作用在保证同一应用的多张表生成 的id不重复
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
@Setter
public class PrefixSequence extends AbstractBaseSequence
{
    @Resource(name = "prefixAttacherOffset17")
    private PrefixAttacher attacher;

    @Resource(name = "sequenceRepositoryZookeeper")
    private SequenceRepository sequenceRepository;

    @Override
    public long next()
    {
        int prefix = getPrefix();
        return attacher.attach(this.increase(), prefix);
    }

    /**
     * 从父级节点获取当前节点的前缀值
     * 
     * @return 前缀
     */
    // TODO 这个功能有代码重复，考虑单独拿出来
    private int getPrefix()
    {
        String sequenceKeySpace = IdKeyBuilder.getSequenceKeySpace();
        String prefixKey = IdKeyBuilder.getPrefixKey(this.name);

        int prefix = 0;
        try
        {
            if(!sequenceRepository.exist(prefixKey))
            {
                prefix = (int)sequenceRepository.increaseAndGet(sequenceKeySpace, 1);
                // 保存到对应的prefix节点上
                sequenceRepository.set(prefixKey, prefix);
                return prefix;
            }

            prefix = (int)sequenceRepository.get(prefixKey);
        }
        catch(Exception e)
        {
            throw new IDGeneratorException(e);
        }

        return prefix;
    }
}
