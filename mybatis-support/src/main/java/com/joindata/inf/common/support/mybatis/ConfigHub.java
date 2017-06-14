package com.joindata.inf.common.support.mybatis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.mybatis.bootconfig.CustomMapperScannerRegistrar;
import com.joindata.inf.common.support.mybatis.bootconfig.DataSourceConfig;
import com.joindata.inf.common.support.mybatis.bootconfig.MyBatisConfig;
import com.joindata.inf.common.support.mybatis.bootconfig.TransactionConfig;

/**
 * MyBatis 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月2日 下午5:32:55
 */
@Configuration
@Import({DataSourceConfig.class, TransactionConfig.class, MyBatisConfig.class, CustomMapperScannerRegistrar.class})
@EnableDisconf
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}