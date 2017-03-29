/**
 * 
 */
package com.joindata.inf.common.support.idgen.core;

/**
 * 常量 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
public class Constant {

	 /** 每次从zookeeper取出的id的区间步长 */
	public static final int ID_RANGE_DEFAULT_SIZE = 200;
	/** 生成id前五位时间戳从该时间开始计 */
	public static final String TIMESTAMP_SINCE_DATE = "2017-01-01";
	public static final String TIMESTAMP_SEQUENCE = "seq-tmp";
	public static final String SEQUENCE = "sequence";
}
