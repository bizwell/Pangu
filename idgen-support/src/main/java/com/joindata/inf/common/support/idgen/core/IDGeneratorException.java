/**
 * 
 */
package com.joindata.inf.common.support.idgen.core;

/**
 *
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
public class IDGeneratorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IDGeneratorException(String msg) {
		super(msg);
	}

	public IDGeneratorException(String msg, Exception e) {
		super(msg, e);
	}

	public IDGeneratorException(Exception e) {
		super(e);
	}

}
