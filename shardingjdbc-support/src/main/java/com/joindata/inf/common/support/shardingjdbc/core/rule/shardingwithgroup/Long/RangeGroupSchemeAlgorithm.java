package com.joindata.inf.common.support.shardingjdbc.core.rule.shardingwithgroup.Long;

import java.util.Collection;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;
import com.joindata.inf.common.support.shardingjdbc.core.algorithm.Long.GroupRangeSuffixChooser;
import com.joindata.inf.common.util.log.Logger;

/**
 * Long 型分表键按自然序列范围分库规则
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 28, 2017 6:20:17 PM
 */
public class RangeGroupSchemeAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long>
{
    private static final Logger log = Logger.get();

    private GroupRangeSuffixChooser dbChooser;

    /**
     * @param defaultGroup 默认组名，如果所有组都不能命中，使用哪个组
     * @param groupVolum 每组多少位
     * @param enob 分表键有效位数（用于去掉传入值中非“序列”部分的前缀），-1 表示全部位数有效
     */
    public RangeGroupSchemeAlgorithm(String defaultGroup, int groupVolum, int enob)
    {
        this.dbChooser = new GroupRangeSuffixChooser(defaultGroup, groupVolum, enob);
    }

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue)
    {
        // 直接把传入的值拿来计算范围
        String targetDb = dbChooser.choose(availableTargetNames, shardingValue.getValue());

        log.info("相等条件 {}, 命中库: {}", shardingValue.getValue(), targetDb);
        return targetDb;
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue)
    {
        // 从传入的值列表中遍历
        Collection<String> targetDbs = dbChooser.choose(availableTargetNames, shardingValue.getValues());

        log.info("枚举条件 {}, 命中库: {}", shardingValue.getValues(), targetDbs);
        return targetDbs;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue)
    {
        // 从数值区间中遍历值
        Range<Long> range = shardingValue.getValueRange();
        Collection<String> targetDbs = dbChooser.choose(availableTargetNames, range.lowerEndpoint(), range.upperEndpoint());

        log.info("区间条件 {}~{}, 命中库: {}", range.lowerEndpoint(), range.upperEndpoint(), targetDbs);
        return targetDbs;
    }

    /**
     * 以当前算法生成数据库分片策略
     * 
     * @param 分组键
     * @return 数据库分片策略
     */
    public DatabaseShardingStrategy toShardingStrategy(String shardingKey)
    {
        return new DatabaseShardingStrategy(shardingKey, this);
    }
}