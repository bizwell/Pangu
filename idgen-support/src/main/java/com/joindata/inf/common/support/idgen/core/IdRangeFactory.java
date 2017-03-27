package com.joindata.inf.common.support.idgen.core;

import java.util.HashMap;

import com.joindata.inf.common.support.idgen.properties.IdgenProperties;
import com.joindata.inf.common.util.log.Logger;

import lombok.Setter;

/**
 * 
 * 根据给定的appId和序列名称获取该应用目前id生成的区间
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
@Setter
public class IdRangeFactory {

	private HashMap<String, IdRange> idRangeCacheMap = new HashMap<>();
	private SequenceRepository sequenceRepository;
	private IdgenProperties idgenProperties;
	
	private static final String ID_RANGE_CACHE_KEY_SEPERATOR = "|";
	private static final Logger log = Logger.get();

	public IdRangeFactory(SequenceRepository sequenceRepository) {
		this.sequenceRepository = sequenceRepository;
	}
	
	public synchronized IdRange getCurrentIdRange(String appId, String sequenceName) {
		String key = new StringBuffer(appId).append(ID_RANGE_CACHE_KEY_SEPERATOR).append(sequenceName)
				.append(ID_RANGE_CACHE_KEY_SEPERATOR).append(Constant.TIMESTAMP_SEQUENCE).toString();
		
		IdRange idRange = null;
		if (idRangeCacheMap.containsKey(key)) {
			idRange = idRangeCacheMap.get(key);
			if (idRange.hasMoreIds()) {
				return idRange;
			} else {
				log.error("区间{}id 不够了， 需要重建下一个区间", idRange);
				idRange = rebuildRange(appId, sequenceName);
				idRangeCacheMap.put(key, idRange);
			}
		} else {
			idRange = rebuildRange(appId, sequenceName);
			idRangeCacheMap.put(key, idRange);
		}
		
		return idRangeCacheMap.get(key);
	}

	private IdRange rebuildRange(String appId, String sequenceName) {
		String dataId = getDataId(appId, sequenceName);
		try {
			int rangeSize = idgenProperties.getRangeSize();
			rangeSize = rangeSize <= 0? Constant.ID_RANGE_DEFAULT_SIZE : rangeSize;
			long startId = sequenceRepository.increaseAndGet(dataId, rangeSize);
			return new IdRange(startId, rangeSize);
		} catch (Exception e) {
			log.error("zookeeper add increase value failed. node path={}", dataId, e);
			throw new IDGeneratorException(e);
		}
	}
	
	/**
	 * 根据给定的sequenceid 和节点类型，拼接node path
	 * @param sequenceId
	 * @param idCategory
	 * @return
	 */
	private String getDataId(String appId, String sequenceName) {
		return new StringBuffer("/").append(appId).append("/").append(Constant.TIMESTAMP_SEQUENCE).append("/").append(sequenceName).toString();
	}
}
