package com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.MultipleKeysTableShardingAlgorithm;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long.ModSuffixChooser;
import com.joindata.inf.common.support.shardingjdbc.properties.inner.ShardingColumnRule;
import com.joindata.inf.common.util.basic.StringUtil;
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
    private Map<String, ShardingColumnRule> shardingColumnRuleMap;

    public ModShardingTableWithMultikeyAlgorithm(Map<String, ShardingColumnRule> shardingColumnRuleMap)
    {
        this.tableChooser = new ModSuffixChooser();
        this.shardingColumnRuleMap = shardingColumnRuleMap;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue<?>> shardingValues)
    {
        Set<String> tableNames = new HashSet<>();
        for (ShardingValue<?> shardingValue : shardingValues) {
            long shardingValId = (long)shardingValue.getValue();
            
            ShardingColumnRule shardingColumnRule = shardingColumnRuleMap.get(shardingValue.getColumnName());
            if (shardingColumnRule == null) {
                log.warn("分库分表键:{}没有配置对应的rule!", shardingValue.getColumnName());
                continue;
            }
            String tableName = shardingColumnRule.getTableName();
            //根据schema的值过滤取得该分表键对应的数据库的列表
            Collection<String> tbNames = availableTargetNames.stream().filter(p->StringUtil.substringBeforeLast(p, "_").equalsIgnoreCase(tableName)).collect(Collectors.toList());
            
            tableNames.add(tableChooser.choose(tbNames, shardingValId));
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