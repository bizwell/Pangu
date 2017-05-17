package com.joindata.inf.common.support.camunda.bootconfig;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ExternalTaskService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 声明引擎相关服务
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 16, 2017 10:41:44 AM
 */
@Lazy
@Configuration
public class CamundaServiceConfig
{
    @Autowired
    private ProcessEngine processEngine;

    @Bean
    @Lazy
    public RuntimeService runtimeService()
    {
        return processEngine.getRuntimeService();
    }

    @Bean
    @Lazy
    public RepositoryService repositoryService()
    {
        return processEngine.getRepositoryService();
    }

    @Bean
    @Lazy
    public FormService formService()
    {
        return processEngine.getFormService();
    }

    @Bean
    @Lazy
    public TaskService taskService()
    {
        return processEngine.getTaskService();
    }

    @Bean
    @Lazy
    public HistoryService historyService()
    {
        return processEngine.getHistoryService();
    }

    @Bean
    @Lazy
    public IdentityService identityService()
    {
        return processEngine.getIdentityService();
    }

    @Bean
    @Lazy
    public ManagementService managementService()
    {
        return processEngine.getManagementService();
    }

    @Bean
    @Lazy
    public AuthorizationService authorizationService()
    {
        return processEngine.getAuthorizationService();
    }

    @Bean
    @Lazy
    public CaseService caseService()
    {
        return processEngine.getCaseService();
    }

    @Bean
    @Lazy
    public FilterService filterService()
    {
        return processEngine.getFilterService();
    }

    @Bean
    @Lazy
    public ExternalTaskService externalTaskService()
    {
        return processEngine.getExternalTaskService();
    }

    @Bean
    @Lazy
    public DecisionService decisionService()
    {
        return processEngine.getDecisionService();
    }
}
