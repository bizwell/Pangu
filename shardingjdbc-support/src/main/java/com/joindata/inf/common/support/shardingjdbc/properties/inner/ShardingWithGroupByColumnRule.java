package com.joindata.inf.common.support.shardingjdbc.properties.inner;

import java.util.List;

import lombok.Data;

/**
 * 针对多个分库分表键的rule
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017年3月31日
 */
@Data
public class ShardingWithGroupByColumnRule
{
    /**
     * 逻辑表名
     */
    private String logicTable;
    private int shardingCount;
    /**
     * 每个分表键对应的规则
     */
    private List<ShardingColumnRule> shardingColumnRules;

}
