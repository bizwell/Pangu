package com.joindata.inf.common.support.sms;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.sms.bootconfig.EmailConfig;

/**
 * 短信服务配置器
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Mar 22, 2017 11:07:59 AM
 */
@Configuration
@Import({EmailConfig.class})
@EnableDisconf
public class ConfigHub extends AbstractConfigHub
{
    @Override
    protected void check()
    {
    }
}