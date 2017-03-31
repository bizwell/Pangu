package com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long;

import java.util.Collection;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.google.common.collect.Range;
import com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long.ModSuffixChooser;
import com.joindata.inf.common.util.log.Logger;

/**
 * Long 型分表键值取模分表规则
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 6:16:45 PM
 */
public class ModShardingTableAlgorithm implements SingleKeyTableShardingAlgorithm<Long>
{
    private static final Logger log = Logger.get();

    private ModSuffixChooser tableChooser;

    public ModShardingTableAlgorithm()
    {
        this.tableChooser = new ModSuffixChooser();
    }

    /**
     * 当传入的条件是区间
     */
    @Override
    public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue)
    {
        // 从数值区间中遍历值
        Range<Long> range = shardingValue.getValueRange();
        Collection<String> targetTables = tableChooser.choose(tableNames, range.lowerEndpoint(), range.upperEndpoint());

        log.info("区间条件 {}~{}, 命中表: {}", range.lowerEndpoint(), range.upperEndpoint(), targetTables);
        return targetTables;
    }

    /**
     * 当传入的条件是“等于”
     */
    @Override
    public String doEqualSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue)
    {
        // 直接把传入的值取模
        Long value = shardingValue.getValue();
        String targetTable = tableChooser.choose(tableNames, value);

        log.info("相等条件 {}, 命中表: {}", value, targetTable);
        return targetTable;
    }

    /**
     * 当传入的条件是枚举
     */
    @Override
    public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue)
    {
        // 从传入的值列表中遍历
        Collection<String> targetTables = tableChooser.choose(tableNames, shardingValue.getValues());

        log.info("枚举条件 {}, 命中表: {}", shardingValue.getValues(), targetTables);
        return targetTables;
    }

    /**
     * 以当前算法生成表分片策略
     * 
     * @param 分表键
     * @return 表分片策略
     */
    public TableShardingStrategy toShardingStrategy(String shardingKey)
    {
        return new TableShardingStrategy(shardingKey, this);
    }
}