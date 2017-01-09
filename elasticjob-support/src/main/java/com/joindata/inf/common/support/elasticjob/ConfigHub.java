package com.joindata.inf.common.support.elasticjob;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.elasticjob.bootconfig.ElasticJobConfig;

/**
 * ElasticJob 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 12:33:45 PM
 */
@Configuration
@Import({ElasticJobConfig.class})
@EnableDisconf
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}