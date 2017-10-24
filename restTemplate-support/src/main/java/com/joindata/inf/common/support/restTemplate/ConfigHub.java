package com.joindata.inf.common.support.restTemplate;

import com.joindata.inf.common.basic.exceptions.SystemException;
import com.joindata.inf.common.basic.stereotype.AbstractConfigHub;
import com.joindata.inf.common.support.disconf.EnableDisconf;
import com.joindata.inf.common.support.restTemplate.bootconfig.RestTemplateConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Disconf 支持配置器
 *
 * @author 宋翔<songxiang@joindata.com>
 * @date 2016年12月2日 下午5:31:11
 */
@Configuration
@Import(RestTemplateConfig.class)
@EnableDisconf
public class ConfigHub extends AbstractConfigHub {

    @Override
    protected void check() throws SystemException {

    }
}