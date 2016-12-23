package com.joindata.inf.common.support.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.redis.EnableRedis;
import com.joindata.inf.common.support.session.bootconfig.SpringSessionConfig;

/**
 * 会话支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Dec 19, 2016 3:38:12 PM
 */
@Configuration
@EnableDisconf
@EnableRedis
@Import(SpringSessionConfig.class)
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}