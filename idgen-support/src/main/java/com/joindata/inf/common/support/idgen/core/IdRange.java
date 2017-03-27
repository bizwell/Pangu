package com.joindata.inf.common.support.idgen.core;

import java.util.concurrent.atomic.AtomicLong;

import com.joindata.inf.common.util.log.Logger;

import lombok.ToString;

/**
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * 生成自增序列的区段
 */
@ToString
public class IdRange {

	private long startId;
	private AtomicLong currentId;
	private long endId;
	
	private static final Logger log = Logger.get();
	
	public long next() {
		if (!hasMoreIds()) {
			log.warn("区间[{}-{}]已经没有可用的ID", startId, endId);
			throw new IDGeneratorException("该区间没有可用的id");
		}
		
		return currentId.incrementAndGet();
	}
	
	public IdRange(long startId, int step) {
		this.startId = startId;
		this.currentId = new AtomicLong(startId);
		this.endId = startId + step;
	}
	
	public boolean hasMoreIds() {
		return currentId.get() < endId;
	}
	
}
