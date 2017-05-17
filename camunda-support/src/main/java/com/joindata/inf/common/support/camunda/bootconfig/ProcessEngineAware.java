package com.joindata.inf.common.support.camunda.bootconfig;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.spi.impl.AbstractProcessEngineAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessEngineAware extends AbstractProcessEngineAware
{
    @Autowired
    private ProcessEngine processEngine;

    public ProcessEngineAware()
    {
    }

    public ProcessEngineAware(String engineName)
    {
        super.processEngine = processEngine;
    }
}
