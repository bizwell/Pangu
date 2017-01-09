package com.joindata.inf.common.support.elasticjob.component;

import java.util.Set;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.joindata.inf.common.basic.errors.ParamErrors;
import com.joindata.inf.common.basic.exceptions.InvalidParamException;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.basic.support.SpringContextHolder;
import com.joindata.inf.common.support.elasticjob.annotation.Job;
import com.joindata.inf.common.util.basic.ClassUtil;
import com.joindata.inf.common.util.log.Logger;

/**
 * ElasticJob 初始化组件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 4:13:32 PM
 */
public class ElasticJobInitializer
{
    private static final Logger log = Logger.get();

    /** 注册中心 */
    private CoordinatorRegistryCenter registryCenter;

    public ElasticJobInitializer(CoordinatorRegistryCenter registryCenter)
    {
        this.registryCenter = registryCenter;
    }

    @SuppressWarnings("unchecked")
    public void init()
    {
        log.info("初始化 Job - 开始");

        Set<Class<?>> clzSet = ClassUtil.scanTypeAnnotations(BootInfoHolder.getAppPackage(), Job.class);

        for(Class<?> clz: clzSet)
        {
            if(!ElasticJob.class.isAssignableFrom(clz))
            {
                log.error("该类并不是个 Job，请注意：", clz.getCanonicalName());
                continue;
            }

            Job job = clz.getAnnotation(Job.class);

            JobCoreConfiguration config = JobCoreConfiguration.newBuilder(job.name(), job.cron(), job.shardingCount()).build();

            JobTypeConfiguration jobType = null;
            if(DataflowJob.class.isAssignableFrom(clz))
            {
                jobType = new DataflowJobConfiguration(config, clz.getCanonicalName(), job.streamingProcess());
            }
            else if(SimpleJob.class.isAssignableFrom(clz))
            {
                jobType = new SimpleJobConfiguration(config, clz.getCanonicalName());
            }
            else
            {
                throw new InvalidParamException(ParamErrors.INVALID_PARAM_CONFIG_ERROR, clz.getCanonicalName() + "没有继承正确的 Job 类");
            }

            log.info("注册 Job 实例: {} - {}", job.name(), clz.getCanonicalName());

            JobScheduler scheduler = new SpringJobScheduler((ElasticJob)SpringContextHolder.getBean(clz), registryCenter, LiteJobConfiguration.newBuilder(jobType).build());

            if(job.autoRun())
            {
                log.info("启动 Job 实例: {} - {}", job.name(), clz.getCanonicalName());
                scheduler.init();
            }
            else
            {
                log.info("该 Job 被标记为非自启动: {} - {}", job.name(), clz.getCanonicalName());
            }

            JobHelper.putScheduler((Class<? extends ElasticJob>)clz, scheduler);
        }

        log.info("初始化 Job - 结束");
    }
}
