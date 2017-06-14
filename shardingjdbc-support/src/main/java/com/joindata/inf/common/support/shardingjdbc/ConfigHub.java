package com.joindata.inf.common.support.shardingjdbc;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.mybatis.EnableMyBatis;
import com.joindata.inf.common.support.shardingjdbc.bootconfig.ShardingDsConfig;
import com.joindata.inf.common.support.shardingjdbc.bootconfig.ShardingJdbcConfig;

/**
 * ShardingJDBC 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 20, 2017 5:40:03 PM
 */
@Configuration
@Import({ShardingDsConfig.class, ShardingJdbcConfig.class})
@EnableDisconf
@EnableMyBatis
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}