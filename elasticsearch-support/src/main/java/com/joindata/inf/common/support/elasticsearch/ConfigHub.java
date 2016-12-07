package com.joindata.inf.common.support.elasticsearch;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.annotation.JoindataConfigHub;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.elasticsearch.bootconfig.ElasticSearchConfig;

/**
 * ElasticSearch 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月7日 下午1:53:33
 */
@JoindataConfigHub
@Configuration
@ComponentScan
@EnableDisconf
@Import({ElasticSearchConfig.class})
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}