/**
 * 
 */
package com.joindata.inf.common.support.idgen.core;

/**
 * 常量
 * 
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月24日
 */
public class Constant
{
    public static final String ROOT = "/idgen";

    /** 每次从zookeeper取出的id的区间步长 */
    public static final int ID_RANGE_DEFAULT_SIZE = 200;

    public static final String SEQUENCE = "/sequence";

    public static final String PREFIX = "/prefix";
}
