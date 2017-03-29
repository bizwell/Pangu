/**
 * 
 */
package com.joindata.inf.common.support.idgen.component.sequence.impl;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.idgen.component.Sequence;
import com.joindata.inf.common.support.idgen.core.Constant;
import com.joindata.inf.common.support.idgen.core.IDGeneratorException;
import com.joindata.inf.common.support.idgen.core.IdRange;
import com.joindata.inf.common.support.idgen.core.IdRangeFactory;
import com.joindata.inf.common.util.basic.DateUtil;
import com.joindata.inf.common.util.log.Logger;

import lombok.Setter;

/**
 * 5位时间戳+2位命名空间+序号的递增序列
 * 
 * 带前缀的sequence id 生成器 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
@Setter
public abstract class AbstactSequenceImpl implements Sequence {

	@Resource(name="idRangeFactory")
	private IdRangeFactory idRangeFactory;
	
	protected static Logger log = Logger.get();
	protected String appId = BootInfoHolder.getAppId();
	protected String name;
	/** 生成ID的区间范围*/
	protected int rangeSize;

	/**
	 * 生成的id的规则 92233 72 036854775807 前五位存从2017-01-01产生的时间戳， 中间两位prefix,
	 * 后面自增的序列。eg: 1 01 00000000001
	 * 
	 * @return
	 */
	@Override
	public long next() {
		int days = getDays();
		int prefix;
		try {
			synchronized (this) {
				prefix = getPrefix();
				long sequence = getSequence(appId, name);
				long sequenceId = new BigInteger("10").pow(14).longValue() * days + prefix * new BigInteger("10").pow(12).longValue() +  sequence;
				if (sequenceId < 0) {
					log.error("获取id失败， 已经没有可用的ID");
					throw new IDGeneratorException("获取id失败， 已经没有可用的ID");
				}
				return sequenceId;
			}
		
		} catch (Exception e) {
			log.error("get id failed!", e);
			throw new IDGeneratorException(e);
		}
	}
	
	// 获取从2017-01-01到当前时间经历的天数
	private int getDays() {
		try {
			return DateUtil.getDaysBetween(new SimpleDateFormat("yyyy-MM-dd").parse(Constant.TIMESTAMP_SINCE_DATE),
					new Date());
		} catch (ParseException e) {
			log.error("日期转换异常", e);
			throw new IDGeneratorException(e);
		}
	}

	public abstract int getPrefix() throws Exception;

	private long getSequence(String appId, String sequenceName) {
		IdRange idRange = idRangeFactory.getCurrentIdRange(appId, sequenceName);
		return idRange.next();
	}
}
