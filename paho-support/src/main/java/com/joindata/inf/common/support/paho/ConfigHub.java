package com.joindata.inf.common.support.paho;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.paho.bootconfig.PahoConfig;

/**
 * Paho 支持配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 25, 2017 5:24:17 PM
 */
@Configuration
@Import(PahoConfig.class)
@EnableDisconf
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}