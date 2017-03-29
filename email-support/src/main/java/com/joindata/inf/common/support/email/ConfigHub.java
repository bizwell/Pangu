package com.joindata.inf.common.support.email;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.email.bootconfig.EmailConfig;

/**
 * 邮件服务配置器
 * @author <a href="mailto:gaowei1@joindata.com">高伟</a>
 * @date 2017-03-29
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