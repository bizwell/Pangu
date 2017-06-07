package com.joindata.inf.common.support.idgen.core;

import java.util.HashMap;

import com.joindata.inf.common.support.idgen.core.util.IdKeyBuilder;
import com.joindata.inf.common.support.idgen.properties.IdgenProperties;
import com.joindata.inf.common.util.log.Logger;

import lombok.Setter;

/**
 * 
 * 根据给定的appId和序列名称获取该应用目前id生成的区间
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
@Setter
public class IdRangeFactory
{

    private HashMap<String, IdRange> idRangeCacheMap = new HashMap<>();

    private SequenceRepository sequenceRepository;

    private IdgenProperties idgenProperties;

    private static final Logger log = Logger.get();

    public IdRangeFactory(SequenceRepository sequenceRepository)
    {
        this.sequenceRepository = sequenceRepository;
    }

    public IdRange getCurrentIdRange(String sequenceName)
    {
        String key = getCacheKey(sequenceName);

        IdRange idRange;
        if(idRangeCacheMap.containsKey(key))
        {
            idRange = idRangeCacheMap.get(key);
            if(idRange.hasMoreIds())
            {
                return idRange;
            }
            else
            {
                log.debug("区间{}id 不够了， 需要重建下一个区间", idRange);
                idRange = rebuildRange(sequenceName);
                idRangeCacheMap.put(key, idRange);
            }
        }
        else
        {
            idRange = rebuildRange(sequenceName);
            idRangeCacheMap.put(key, idRange);
        }

        return idRangeCacheMap.get(key);
    }

    private IdRange rebuildRange(String sequenceName)
    {
        String dataId = IdKeyBuilder.getSequenceKey(sequenceName);
        try
        {
            int rangeSize = idgenProperties.getRangeSize();
            rangeSize = rangeSize <= 0 ? Constant.ID_RANGE_DEFAULT_SIZE : rangeSize;
            long startId = sequenceRepository.getAndIncrease(dataId, rangeSize);
            return new IdRange(startId - 1, rangeSize);
        }
        catch(Exception e)
        {
            log.error("zookeeper add increase value failed. node path={}", dataId, e);
            throw new IDGeneratorException(e);
        }
    }

    public void clearRange(String sequenceName)
    {
        String key = getCacheKey(sequenceName);
        idRangeCacheMap.remove(key);
    }

    private static final String getCacheKey(String name)
    {
        return IdKeyBuilder.getSequenceKey(name) + ".cache";
    }
}
