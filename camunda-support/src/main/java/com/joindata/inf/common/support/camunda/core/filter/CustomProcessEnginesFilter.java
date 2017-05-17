package com.joindata.inf.common.support.camunda.core.filter;

import java.io.IOException;

import org.camunda.bpm.webapp.impl.engine.ProcessEnginesFilter;
import org.springframework.stereotype.Component;

import com.joindata.inf.common.basic.annotation.FilterComponent;
import com.joindata.inf.common.util.basic.ResourceUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.xiaoleilu.hutool.io.IoUtil;

@Component
@FilterComponent
public class CustomProcessEnginesFilter extends ProcessEnginesFilter
{
    protected String getWebResourceContents(String name) throws IOException
    {
        name = StringUtil.trimLeft(name, '/');
        return IoUtil.read(ResourceUtil.getRootResourceAsStream(name), "UTF-8");
    }
}
