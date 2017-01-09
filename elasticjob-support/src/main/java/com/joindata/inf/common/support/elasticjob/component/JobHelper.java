package com.joindata.inf.common.support.elasticjob.component;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.joindata.inf.common.basic.errors.OperationError;
import com.joindata.inf.common.basic.exceptions.ApplicationException;
import com.joindata.inf.common.util.basic.CollectionUtil;

/**
 * 任务操作工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 4:21:55 PM
 */
@Component
public class JobHelper
{
    private static final Map<Class<? extends ElasticJob>, JobScheduler> JobSchedulerMap = CollectionUtil.newMap();

    static void putScheduler(Class<? extends ElasticJob> clz, JobScheduler scheduler)
    {
        JobSchedulerMap.put(clz, scheduler);
    }

    /**
     * 启动 Job
     * 
     * @param clz Job 的 Class
     */
    public void start(Class<? extends ElasticJob> clz)
    {
        if(JobSchedulerMap.get(clz) == null)
        {
            throw new ApplicationException(OperationError.OPERATION_ERROR, "无法启动，并没有找到该 Job: " + clz.getCanonicalName());
        }

        JobSchedulerMap.get(clz).init();
    }

    /**
     * 停止 Job
     * 
     * @param clz Job 的 Class
     */
    public void stop(Class<? extends ElasticJob> clz)
    {
        if(JobSchedulerMap.get(clz) == null)
        {
            throw new ApplicationException(OperationError.OPERATION_ERROR, "无法停止，并没有找到该 Job: " + clz.getCanonicalName());
        }

        JobSchedulerMap.get(clz).shutdown();
    }

}
