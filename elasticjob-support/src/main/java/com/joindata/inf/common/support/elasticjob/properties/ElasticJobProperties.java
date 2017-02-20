package com.joindata.inf.common.support.elasticjob.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * ElasticJob 配置变量文件
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Jan 9, 2017 1:13:11 PM
 */
@Service
@Scope("singleton")
@DisconfFile(filename = ElasticJobProperties.FILENAME)
public class ElasticJobProperties
{
    public static final String FILENAME = "job.properties";

    private String zkHosts;

    /*
     * TODO 暂时不要搞这么复杂 private int zkBaseSleep; private int zkMaxSleep; private int zkRetries;
     */

    @DisconfFileItem(name = "zk.hosts", associateField = "zkHosts")
    public String getZkHosts()
    {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts)
    {
        this.zkHosts = zkHosts;
    }

    /*
     * @DisconfFileItem(name = "zk.baseSleep", associateField = "zkBaseSleep") public int getZkBaseSleep() { return zkBaseSleep; } public void setZkBaseSleep(int zkBaseSleep) { this.zkBaseSleep = zkBaseSleep; }
     * @DisconfFileItem(name = "zk.maxSleep", associateField = "zkMaxSleep") public int getZkMaxSleep() { return zkMaxSleep; } public void setZkMaxSleep(int zkMaxSleep) { this.zkMaxSleep = zkMaxSleep; }
     * @DisconfFileItem(name = "zk.retries", associateField = "zkRetries") public int getZkRetries() { return zkRetries; } public void setZkRetries(int zkRetries) { this.zkRetries = zkRetries; }
     */

}
