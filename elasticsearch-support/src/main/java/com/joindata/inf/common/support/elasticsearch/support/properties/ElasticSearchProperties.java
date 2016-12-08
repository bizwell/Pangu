package com.joindata.inf.common.support.elasticsearch.support.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import com.joindata.inf.common.support.elasticsearch.support.Cst;

@Service
@Scope("singleton")
@DisconfFile(filename = Cst.ES_PROPERTIES)
public class ElasticSearchProperties
{
    private String hosts;

    private String clusterName;

    @DisconfFileItem(name = "es.hosts", associateField = "hosts")
    public String getHosts()
    {
        return hosts;
    }

    public void setHosts(String hosts)
    {
        this.hosts = hosts;
    }

    @DisconfFileItem(name = "es.clusterName", associateField = "clusterName")
    public String getClusterName()
    {
        return clusterName;
    }

    public void setClusterName(String clusterName)
    {
        this.clusterName = clusterName;
    }

}
