package com.joindata.inf.common.support.camunda.bootconfig;

import javax.sql.DataSource;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.auth.DefaultAuthorizationProvider;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.camunda.EnableCamunda;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ResourceUtil;
import com.joindata.inf.common.util.log.Logger;
import com.mysql.jdbc.Driver;

/**
 * Camunda 配置
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date May 16, 2017 10:40:05 AM
 */
@Configuration
public class CamundaConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware
{
    private static final Logger log = Logger.get();

    private ApplicationContext applicationContext = null;

    @Bean
    public DataSource dataSource()
    {
        DruidDataSource dataSource = new DruidDataSource();

        // TODO fuck 后续要改成集中式平台
        EnableCamunda enableCamunda = BootInfoHolder.getBootClass().getAnnotation(EnableCamunda.class);

        dataSource.setDriverClassName(Driver.class.getCanonicalName());
        dataSource.setUrl(enableCamunda.url());
        dataSource.setUsername(enableCamunda.username());
        dataSource.setPassword(enableCamunda.password());

        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager()
    {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource());

        return manager;
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration()
    {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDeploymentResources(Util.getBpmnResources());
        configuration.setDataSource(dataSource());
        configuration.setTransactionManager(dataSourceTransactionManager());
        configuration.setJobExecutorActivate(false);
        configuration.setDatabaseSchemaUpdate("true");
        configuration.setAuthorizationEnabled(true);
        configuration.setResourceAuthorizationProvider(new DefaultAuthorizationProvider());
        configuration.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);

        return configuration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean() throws Exception
    {
        ProcessEngineFactoryBean bean = new ProcessEngineFactoryBean();
        bean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        bean.setApplicationContext(applicationContext);

        return bean;
    }

    /**
     * 流程引擎
     */
    @Bean
    public ProcessEngine processEngine() throws Exception
    {
        return processEngineFactoryBean().getObject();
    }

    // @Bean
    // public static DefaultCockpitRuntimeDelegate cockpitRuntimeDelegate()
    // {
    // DefaultCockpitRuntimeDelegate cockpit = new DefaultCockpitRuntimeDelegate();
    // Cockpit.setCockpitRuntimeDelegate(cockpit);
    // return cockpit;
    // }
    //
    // @Bean
    // public static AdminRuntimeDelegate adminRuntimeDelegate()
    // {
    // Admin.setAdminRuntimeDelegate(new DefaultAdminRuntimeDelegate());
    // return Admin.getRuntimeDelegate();
    // }
    //
    // @Bean
    // public static WelcomeRuntimeDelegate welcomeRuntimeDelegate()
    // {
    // Welcome.setRuntimeDelegate(new DefaultWelcomeRuntimeDelegate());
    // return Welcome.getRuntimeDelegate();
    // }
    //
    // @Bean
    // public static TasklistRuntimeDelegate tasklistRuntimeDelegate()
    // {
    // Tasklist.setTasklistRuntimeDelegate(new DefaultTasklistRuntimeDelegate());
    // return Tasklist.getRuntimeDelegate();
    // }

    private static final class Util
    {
        public static final Resource[] getBpmnResources()
        {
            EnableCamunda enableCamunda = BootInfoHolder.getBootClass().getAnnotation(EnableCamunda.class);
            Resource[] resources = ResourceUtil.getResourcesEndswith(".bpmn", enableCamunda.bpmnDir());

            log.info("BPMN 文件夹: {}", ArrayUtil.toString(enableCamunda.bpmnDir()));
            log.info("BPMN 文件: {}", ArrayUtil.toString(resources));

            return resources;
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/app/**").addResourceLocations("classpath:/app/");
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/lib/");
        registry.addResourceHandler("/api/admin/plugin/adminPlugins/static/**").addResourceLocations("classpath:/plugin/admin/");
        registry.addResourceHandler("/api/tasklist/plugin/tasklistPlugins/static/**").addResourceLocations("classpath:/plugin/tasklist/");
        registry.addResourceHandler("/api/cockpit/plugin/cockpitPlugins/static/**").addResourceLocations("classpath:/plugin/cockpit/");

        log.info("Camunda 任务界面: {}", "/app/tasklist/task.html");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}