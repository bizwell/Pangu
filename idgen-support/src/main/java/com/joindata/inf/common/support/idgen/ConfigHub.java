package com.joindata.inf.common.support.idgen;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.idgen.bootconfig.IdGenBeanRegistry;
import com.joindata.inf.common.support.idgen.bootconfig.IdgenConfig;

/**
 * ID 生成器支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 23, 2017 9:40:09 PM
 */
@Configuration
@Import({IdgenConfig.class, IdGenBeanRegistry.class})
@EnableDisconf
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}