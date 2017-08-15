package com.joindata.inf.common.support.camunda.bootconfig;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.auth.DefaultAuthorizationProvider;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.joindata.inf.common.basic.cst.PanguConfusing;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.support.camunda.BpmnParseListenerComponent;
import com.joindata.inf.common.support.camunda.EnableCamunda;
import com.joindata.inf.common.support.camunda.properties.CamundaProperties;
import com.joindata.inf.common.util.basic.ArrayUtil;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.basic.CodecUtil;
import com.joindata.inf.common.util.basic.ResourceUtil;
import com.joindata.inf.common.util.basic.StringUtil;
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

    @Autowired
    private CamundaProperties properties;

    @Bean
    public DataSource dataSource() throws GeneralSecurityException
    {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setDriverClassName(Driver.class.getCanonicalName());
        dataSource.setUrl(properties.getDbUrl());
        dataSource.setUsername(properties.getDbUsername());

        String password = properties.getDbPassword();
        if(StringUtil.startsWith(password, "enc("))
        {
            password = CodecUtil.decryptDES(StringUtil.substringBetweenFirstAndLast(password, "enc(", ")"), PanguConfusing.KEY);
        }

        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() throws GeneralSecurityException
    {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource());

        return manager;
    }

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() throws GeneralSecurityException
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

        List<BpmnParseListener> listeners = configuration.getCustomPostBPMNParseListeners();
        if(listeners == null)
        {
            listeners = new ArrayList<>();
        }

        Set<Class<?>> listenerClasses = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), BpmnParseListenerComponent.class);
        for(Class<?> clz: listenerClasses)
        {
            listeners.add((BpmnParseListener)SpringContextHolder.getBean(clz));
        }

        configuration.setCustomPostBPMNParseListeners(listeners);

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
        // TODO 马勒戈壁的 registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/lib/");
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