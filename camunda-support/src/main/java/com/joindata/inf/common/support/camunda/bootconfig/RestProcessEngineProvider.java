package com.joindata.inf.common.support.camunda.bootconfig;

import java.util.Set;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.rest.spi.ProcessEngineProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestProcessEngineProvider implements ProcessEngineProvider
{
    @Autowired
    private ProcessEngine processEngine;

    public ProcessEngine getDefaultProcessEngine()
    {
        return processEngine;
    }

    public ProcessEngine getProcessEngine(String name)
    {
        return ProcessEngines.getProcessEngine(name);
    }

    public Set<String> getProcessEngineNames()
    {
        return ProcessEngines.getProcessEngines().keySet();
    }
}
