/**
 * 
 */
package com.joindata.inf.common.support.idgen.component.sequence.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.joindata.inf.common.support.idgen.core.IDGeneratorException;
import com.joindata.inf.common.support.idgen.core.SequenceRepository;
import com.joindata.inf.common.support.idgen.core.attacher.PrefixAttacher;
import com.joindata.inf.common.support.idgen.core.attacher.TimestampAttacher;
import com.joindata.inf.common.support.idgen.core.util.IdKeyBuilder;

import lombok.Setter;

/**
 * 
 * 5位时间戳+2位命名空间+序号的递增序列
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
@Setter
public class TimestampPrefixSequence extends AbstractBaseSequence
{
    @Resource(name = "prefixAttacherOffset12")
    private PrefixAttacher prefixAttacher;

    @Autowired
    private TimestampAttacher timestampAttacher;

    @Resource(name = "sequenceRepositoryZookeeper")
    private SequenceRepository sequenceRepository;

    @Override
    public long next()
    {
        int prefix = this.getPrefix();
        return timestampAttacher.attach(prefixAttacher.attach(this.increase(), prefix));
    }

    /**
     * 从父级节点获取当前节点的前缀值
     * 
     * @return 前缀
     */
    // TODO 这个功能有代码重复，考虑单独拿出来
    private int getPrefix()
    {
        String prefixDataId = IdKeyBuilder.getSequenceKeySpace();

        int prefix = 0;
        try
        {
            if(!sequenceRepository.exist(IdKeyBuilder.getSequenceKey(this.name)))
            {
                prefix = (int)sequenceRepository.increaseAndGet(prefixDataId, 1);
            }

            prefix = (int)sequenceRepository.get(prefixDataId);
        }
        catch(Exception e)
        {
            throw new IDGeneratorException(e);
        }

        return prefix;
    }

}
