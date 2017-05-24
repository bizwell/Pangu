package com.joindata.inf.common.support.camunda.core.filter;

import java.io.IOException;

import javax.servlet.annotation.WebFilter;

import org.camunda.bpm.webapp.impl.engine.ProcessEnginesFilter;

import com.joindata.inf.common.util.basic.ResourceUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.xiaoleilu.hutool.io.IoUtil;

@WebFilter(filterName = "ProcessEngines", urlPatterns = {"/app/*", "/lib/*", "/api/*", "/plugin/*"})
public class CustomProcessEnginesFilter extends ProcessEnginesFilter
{
    protected String getWebResourceContents(String name) throws IOException
    {
        name = StringUtil.trimLeft(name, '/');
        return IoUtil.read(ResourceUtil.getRootResourceAsStream(name), "UTF-8");
    }
}
