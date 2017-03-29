/**
 * 
 */
package com.joindata.inf.common.support.idgen.component.sequence.impl;

import javax.annotation.Resource;

import com.joindata.inf.common.support.idgen.core.Constant;
import com.joindata.inf.common.support.idgen.core.SequenceRepository;

import lombok.Setter;

/**
 * 
 * 带有两位命名空间的sequence生成器。 
 * 作用在保证同一应用的多张表生成 的id不重复
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
@Setter
public class PrefixSequence extends AbstactSequenceImpl {

	@Resource(name= "sequenceRepositoryZookeeper")
	private SequenceRepository sequenceRepository;
	
	public PrefixSequence(String name) {
		this.name = name;
	}
	
	@Override
	public int getPrefix() throws Exception {
		String prefixDataId = new StringBuffer("/").append(this.appId).append("/").append(Constant.TIMESTAMP_SEQUENCE).toString();
		if (!sequenceRepository.exist(prefixDataId + "/" + this.name)) {
			sequenceRepository.increaseAndGet(prefixDataId, 1);
		}
		
		return (int) sequenceRepository.get(prefixDataId);
	}

	
}
