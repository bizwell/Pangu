package com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long.ModSuffixChooser;
import com.joindata.inf.common.util.log.Logger;

/**
 * Long 型分表键值取模分表规则
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 6:16:45 PM
 */
public class ModShardingTableWithMultikeyAlgorithm implements MultipleKeysTableShardingAlgorithm
{
    private static final Logger log = Logger.get();

    private ModSuffixChooser tableChooser;

    public ModShardingTableWithMultikeyAlgorithm()
    {
        this.tableChooser = new ModSuffixChooser();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue<?>> shardingValues)
    {
        Set<String> tableNames = new HashSet<>();
        for (ShardingValue<?> shardingValue : shardingValues) {
            long shardingValId = (long)shardingValue.getValue();
            tableNames.add(tableChooser.choose(availableTargetNames, shardingValId));
            log.info("sharding column: {}, sharding value:{}", shardingValue.getColumnName(), shardingValue.getValue());
            return tableNames;
        }
        log.info("sharding tables: {}", tableNames);
        return tableNames;
    }
    
    /**
     * 以当前算法生成表分片策略
     * 
     * @param 分表键
     * @return 表分片策略
     */
    public TableShardingStrategy toShardingStrategy(List<String> shardingKeys)
    {
        return new TableShardingStrategy(shardingKeys, this);
    }
}