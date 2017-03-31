package com.joindata.inf.common.support.shardingjdbc.properties.inner;

import lombok.Data;

/**
 * 多个分库分表键时, 每个分表键对应的分表规则
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月31日
 */
@Data
public class ShardingColumnRule
{
    private String columnName;
    /** 数据库名 */
    private String schemaName;

    /**
     * 分组容量，表示在 <strong>分组键</strong> 增长到 <strong>每多少</strong> 的时候需要数据库分组递增
     */
    private int groupVolum;

    /** 分表键有效位 */
    private byte keyEnob;
}
